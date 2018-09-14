package com.hjhq.teamface.im.db;


import android.text.TextUtils;

import com.hjhq.teamface.basis.bean.ImMessage;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.database.PushMessage;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.database.gen.ConversationDao;
import com.hjhq.teamface.basis.database.gen.MemberDao;
import com.hjhq.teamface.basis.database.gen.PushMessageDao;
import com.hjhq.teamface.basis.database.gen.SocketMessageDao;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.im.IM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据库管理
 * Created by lx on 2017/5/25.
 */

public class DBManager {


    public boolean showTimeMessage(SocketMessage bean, boolean flag) {

        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        List<SocketMessage> list = new ArrayList<>();
        list = socketMessageDao
                .queryBuilder()
                .where(SocketMessageDao.Properties.ClientTimes.eq(bean.getClientTimes()), SocketMessageDao.Properties.Rand.eq(bean.getRand())
                        , SocketMessageDao.Properties.UcFlag.eq(0))
                .list();
        if (list.size() > 0) {
            SocketMessage old = list.get(0);
            old.setShowTime(flag);
            saveOrReplace(old);

        }


        return false;
    }

    public void sendFileFailed(String msgId) {
        long messageId = -1L;
        try {
            messageId = Long.parseLong(msgId);
        } catch (NumberFormatException e) {

        }
        if (messageId == -1L) {
            return;
        }
        List<SocketMessage> socketMessages = queryMessageById(messageId);
        if (socketMessages.size() > 0) {
            // Log.e("发送消息失败", "发送消息失败" + messageId);
            SocketMessage socketMessage = socketMessages.get(0);
            socketMessage.setSendState(2);
            saveOrReplace(socketMessage);
            EventBus.getDefault().post(new ImMessage(0, MsgConstant.SEND_FILE_FAILED, msgId));
        }

    }

    public void updatePushConversation(PushMessage pm) {
        ConversationDao conversationDao = IM.getDaoInstant().getConversationDao();
        long conversationId = 0L;
        try {
            conversationId = Long.parseLong(pm.getAssistant_id()) + MsgConstant.DEFAULT_VALUE;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        List<Conversation> convList = new ArrayList<>();
        convList = conversationDao
                .queryBuilder()
                .where(
                        ConversationDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                        ConversationDao.Properties.OneselfIMID.eq(IM.getInstance().getImId()),
                        ConversationDao.Properties.ConversationId.eq(conversationId)
                )
                .orderAsc(ConversationDao.Properties.LastMsgDate)
                .list();
        if (convList != null && convList.size() > 0) {
            long createTime = 0L;
            try {
                createTime = Long.parseLong(pm.getCreate_time());
            } catch (NumberFormatException e) {
                createTime = System.currentTimeMillis() - IM.getInstance().getServerTimeOffsetValue();
            }

            if (createTime > convList.get(0).getLastMsgDate()) {
                convList.get(0).setLastMsgDate(createTime);
            }
            convList.get(0).setLatestMessage(pm.getPush_content());
            convList.get(0).setConversationType(MsgConstant.NOTIFICATION);
            saveOrReplace(convList.get(0));
            EventBus.getDefault().post(new ImMessage(0, MsgConstant.RECEIVE_PUSH_MESSAGE, pm));
        }


    }

    /**
     * 邮件联想
     *
     * @param keyword
     * @return
     */
    public List<Member> queryMemberByKeyword(String keyword) {
        MemberDao socketMessageDao = IM.getDaoInstant().getMemberDao();
        QueryBuilder<Member> qb = socketMessageDao
                .queryBuilder();
        List<Member> list = qb.where(
                MemberDao.Properties.Company_id.eq(IM.getInstance().getCompanyId()),
                MemberDao.Properties.MyId.eq(IM.getInstance().getImId()),
                MemberDao.Properties.Email.like("%" + keyword + "%")
        )
                .orderAsc(MemberDao.Properties.Email)
                .list();

        return list;


    }

    /**
     * 根据关键字查询人员
     *
     * @param keyword
     * @return
     */
    public List<Member> queryMemberByNamePhoneEmailDeparment(String keyword) {
        MemberDao socketMessageDao = IM.getDaoInstant().getMemberDao();
        WhereCondition whereCondition1 = MemberDao.Properties.Company_id.eq(IM.getInstance().getCompanyId());
        WhereCondition whereCondition2 = MemberDao.Properties.MyId.eq(IM.getInstance().getImId());
        WhereCondition whereCondition3 = MemberDao.Properties.Name.like("%" + keyword + "%");
        WhereCondition whereCondition4 = MemberDao.Properties.Email.like("%" + keyword + "%");
        WhereCondition whereCondition5 = MemberDao.Properties.Phone.like("%" + keyword + "%");
        WhereCondition whereCondition6 = MemberDao.Properties.Department_name.like("%" + keyword + "%");
        WhereCondition whereCondition7 = MemberDao.Properties.Employee_name.like("%" + keyword + "%");
        WhereCondition whereCondition8 = MemberDao.Properties.Role_name.like("%" + keyword + "%");

        QueryBuilder<Member> qb = socketMessageDao
                .queryBuilder();
        List<Member> list =
                qb.where(whereCondition1, whereCondition2)
                        .whereOr(whereCondition3, whereCondition4, whereCondition5, whereCondition6, whereCondition7, whereCondition8)
                        .list();

        return list;


    }

    /**
     * 搜索人员
     *
     * @param keyword
     * @return
     */
    public List<Member> queryMemberByName(String keyword) {
        MemberDao socketMessageDao = IM.getDaoInstant().getMemberDao();
        WhereCondition whereCondition1 = MemberDao.Properties.Company_id.eq(IM.getInstance().getCompanyId());
        WhereCondition whereCondition2 = MemberDao.Properties.MyId.eq(IM.getInstance().getImId());
        WhereCondition whereCondition3 = MemberDao.Properties.Name.like("%" + keyword + "%");
        WhereCondition whereCondition4 = MemberDao.Properties.Post_name.like("%" + keyword + "%");
        WhereCondition whereCondition5 = MemberDao.Properties.Email.like("%" + keyword + "%");
        WhereCondition whereCondition6 = MemberDao.Properties.Phone.like("%" + keyword + "%");
        QueryBuilder<Member> queryBuilder = socketMessageDao
                .queryBuilder();
        List<Member> list =
                queryBuilder
                        .where(whereCondition1, whereCondition2)
                        .whereOr(whereCondition3, whereCondition4, whereCondition5, whereCondition6)
                        .list();

        return list;


    }

    /**
     * 更新已读状态
     *
     * @param oneselfIMID
     * @param msgId
     */
    public void updateMessageReadState(String oneselfIMID, long msgId) {
        if (TextUtils.isEmpty(oneselfIMID)) {
            return;
        }
        List<SocketMessage> socketMessages = queryMessageById(msgId);
        if (socketMessages == null || socketMessages.size() <= 0) {
            return;
        }
        SocketMessage m = socketMessages.get(0);
        if (m.getChatType() == MsgConstant.SINGLE) {
            //将会话中的所有消息标记为已读
            List<SocketMessage> messages = qureyMessageByConversationId(IM.getInstance().getCompanyId(), m.getConversationId());
            if (messages != null && messages.size() > 0) {
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getSendState() == 1) {
                        messages.get(i).setSendState(3);
                        saveOrReplace(messages.get(i));
                    }
                }
            }
        }
        if (m.getChatType() == MsgConstant.GROUP) {
            String readPeoples = m.getReadPeoples();
            if (TextUtils.isEmpty(readPeoples)) {
                m.setReadPeoples(oneselfIMID);
            } else {
                if (readPeoples.contains(oneselfIMID)) {
                    return;
                } else {
                    m.setReadPeoples(m.getReadPeoples() + "," + oneselfIMID);
                }
            }
            readPeoples = m.getReadPeoples();
            String allpeoples = m.getAllPeoples();
            if (!TextUtils.isEmpty(allpeoples)) {
                String[] split1 = allpeoples.split(",");
                if (TextUtils.isEmpty(m.getReadPeoples())) {
                    m.setReadnum(0);
                } else {
                    String[] split = readPeoples.split(",");
                    //已读人数
                    m.setReadnum(split.length);
                    if (split.length >= split1.length - 1) {
                        m.setSendState(7);
                    }
                }

            } else {
                m.setSendState(7);
            }

        }

        saveOrReplace(m);
        EventBus.getDefault().post(new ImMessage(0, MsgConstant.READ_MESSAGE_TAG, m));


    }

    /**
     * 查询指定时间内发送失败/正在发送的消息
     *
     * @param seconds
     */
    public ArrayList<SocketMessage> getSendFailedMessage(long seconds) {
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        WhereCondition whereCondition1 = SocketMessageDao.Properties.SendState.eq(MsgConstant.SEND_FAILURE);
        WhereCondition whereCondition2 = SocketMessageDao.Properties.SendState.eq(MsgConstant.SENDING);
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        List<SocketMessage> list = qb.where(
                SocketMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId()),
                SocketMessageDao.Properties.ServerTimes.gt(IM.getInstance().getServerTime() - seconds)

        ).whereOr(whereCondition1, whereCondition2)
                .orderAsc(SocketMessageDao.Properties.ServerTimes)
                .list();

        return ((ArrayList<SocketMessage>) list);

    }

    /**
     * 拉取历史消息后更新会话列表
     *
     * @param conversation
     */
    public void updateConversation(Conversation conversation) {
        ConversationDao cd = IM.getDaoInstant().getConversationDao();
        QueryBuilder<Conversation> qb = cd
                .queryBuilder();
        List<Conversation> list = qb.where(
                ConversationDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                ConversationDao.Properties.MyId.eq(IM.getInstance().getImId()),
                ConversationDao.Properties.ConversationId.eq(conversation.getConversationId())
        ).list();

        if (list.size() > 0) {
            list.get(0).setLastMsgDate(conversation.getLastMsgDate());
            list.get(0).setLatestMessage(conversation.getLatestMessage());
            list.get(0).setLastMessageType(conversation.getLastMessageType());
            saveOrReplace(list.get(0));
        }

    }

    /**
     * 删除当前用户本地消息
     */
    public void deleteLocalDataByImID() {
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        List<SocketMessage> list = qb.where(
                SocketMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId())

        ).list();
        for (int i = 0; i < list.size(); i++) {
            //清除聊天数据
            delete(list.get(i));
        }
        PushMessageDao dao = IM.getDaoInstant().getPushMessageDao();
        WhereCondition whereCondition1 = PushMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId());
        WhereCondition whereCondition2 = PushMessageDao.Properties.MyId.eq(IM.getInstance().getImId());
        QueryBuilder<PushMessage> qb2 = dao
                .queryBuilder();
        List<PushMessage> list2 = qb2
                .where(whereCondition1, whereCondition2)
                .list();
        for (int i = 0; i < list2.size(); i++) {
            //清除小助手
            delete(list2.get(i));
        }

    }

    /**
     * 根据推送修改小助手名字,图标
     *
     * @param id
     * @param pushMessage
     */
    public void updateAssistantName(long id, PushMessage pushMessage) {
        PushMessageDao dao = IM.getDaoInstant().getPushMessageDao();
        WhereCondition whereCondition1 = PushMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId());
        WhereCondition whereCondition2 = PushMessageDao.Properties.MyId.eq(IM.getInstance().getImId());
        WhereCondition whereCondition3 = PushMessageDao.Properties.Assistant_id.eq(id - MsgConstant.DEFAULT_VALUE);
        QueryBuilder<PushMessage> qb = dao
                .queryBuilder();
        List<PushMessage> list = qb
                .where(whereCondition1, whereCondition2, whereCondition3)
                .list();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setAssistant_name(pushMessage.getTitle());
                list.get(i).setBean_name_chinese(pushMessage.getTitle());
                list.get(i).setIcon_url(pushMessage.getIcon_url());
                list.get(i).setIcon_color(pushMessage.getIcon_color());
                list.get(i).setIcon_type(pushMessage.getIcon_type());
                saveOrReplace(list.get(i));
            }

        }
        EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.RECEIVE_ASSISTANT_NAME_CHANGE_PUSH_MESSAGE, id));
    }

    /**
     * 判断本地消息是否为空
     *
     * @return
     */
    public boolean isMessageEmpty() {

        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        List<SocketMessage> list = qb.where(
                SocketMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId())

        ).list();
        return list.size() <= 0;
    }


    /**
     * 根据sign_Id 员工信息
     *
     * @return
     */


    private static class SingletonHolder {
        private static final DBManager INSTANCE = new DBManager();
    }

    public static DBManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private DBManager() {
    }

    /**
     * 删除数据
     *
     * @return the query result list
     */
    public <T> boolean delete(T object) {
        boolean flag = false;
        try {
            IM.getDaoInstant().delete(object);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除全部数据
     *
     * @param claxx
     * @param <T>
     */
    public <T> boolean deleteAll(Class<T> claxx) {
        boolean flag = false;
        try {
            IM.getDaoInstant().deleteAll(claxx);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 添加数据
     *
     * @return
     */
    public <T> boolean save(T object) {
        boolean flag = false;
        flag = IM.getDaoInstant().insert(object) == -1 ? false : true;
        return flag;
    }

   /* public <T> boolean save(SocketMessage object) {
        boolean flag = false;
        if (object.getUsCmdID() != 5 || object.getUsCmdID() != 6 || object.getUsCmdID() != 7 || object.getUsCmdID() != 8) {
            return flag;
        } else {
            flag = IM.getDaoInstant().insert(object) == -1 ? false : true;
        }
        return flag;
    }*/

    /**
     * 添加数据
     *
     * @return
     */
    public <T> boolean saveOrReplace(T object) {

        boolean flag = false;
        if (null == object) {
            return flag;
        }
        flag = IM.getDaoInstant().insertOrReplace(object) == -1 ? false : true;
        if (object.getClass().getName().equals("com.hjhq.teamface.im.pojo.SocketMessage")) {
            SocketMessage ms = ((SocketMessage) object);
            //Log.e("写入内容", new Gson().toJson(ms));
        }
//        Log.e("数据库写入数据", flag + "" + object.getClass().getName());
//        Log.e("数据库写入数据", new Gson().toJson(object));
        return flag;
    }

    /**
     * 删除所有后添加
     *
     * @param object
     * @param <T>
     * @return
     */
    public <T> boolean saveAndDelectAll(final T object) {
        boolean flag = false;
        try {
            IM.getDaoInstant().runInTx(new Runnable() {
                @Override
                public void run() {

                    IM.getDaoInstant().deleteAll(object.getClass());
                    if (IM.getDaoInstant().insert(object) == -1) {
                        throw new RuntimeException("添加失败！");
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 添加全部数据
     *
     * @return
     */
    public <T> boolean saveAll(final List<T> list) {
        boolean flag = false;
        try {
            IM.getDaoInstant().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T t : list) {
                        IM.getDaoInstant().insertOrReplace(t);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            // Log.e("数据库错误", "需要重新安装");
            // TODO: 2018/1/20 数据库升级 
        }
        return flag;
    }


    /**
     * 修改一条数据
     *
     * @param object
     * @return
     */
    public <T> boolean updateMessage(T object) {
        boolean flag = false;
        try {
            IM.getDaoInstant().update(object);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public <T> List<T> queryAll(Class<T> claxx) {
        return IM.getDaoInstant().loadAll(claxx);
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public <T> T queryById(long key, Class<T> claxx) {
        return IM.getDaoInstant().load(claxx, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public <T> List<T> queryByNativeSql(String sql, String[] conditions, Class<T> claxx) {
        return IM.getDaoInstant().queryRaw(claxx, sql, conditions);
    }


    /*************************************以上是通用数据库操作**********************************************/

    /**
     * 根据公司id和会话id获取会话列表数据
     *
     * @param companyId
     * @param conversationId
     * @return
     */
    public List<SocketMessage> qureyMessageByConversationId(String companyId, long conversationId) {
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        List<SocketMessage> list = new ArrayList<>();
        if (TextUtils.isEmpty(companyId)) {
            return list;
        }
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        qb.where(
                SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId()),
                SocketMessageDao.Properties.CompanyId.eq(companyId),
                SocketMessageDao.Properties.ConversationId.eq(conversationId))
                .orderDesc(SocketMessageDao.Properties.ServerTimes).limit(20)
        ;
        list = qb.list();
        if (list == null) {
            list = new ArrayList<>();
        } else {
            Collections.reverse(list);
        }
        return list;
    }


    /**
     * 查询消息
     *
     * @param type           根据类型查询更早或更晚的消息
     * @param conversationId
     * @param serverTime     时间戳更早或更晚的消息
     * @return
     */
    public List<SocketMessage> qureyMessageByConversationId(int type, long conversationId, long serverTime) {
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        List<SocketMessage> list = new ArrayList<>();

        WhereCondition whereCondition0 = SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId());
        WhereCondition whereCondition1 = SocketMessageDao.Properties.ServerTimes.lt(serverTime);
        WhereCondition whereCondition2 = SocketMessageDao.Properties.ServerTimes.gt(serverTime);
        switch (type) {
            case MsgConstant.GREAT_THAN:
                whereCondition0 = whereCondition2;
                break;
            case MsgConstant.LITTLE_THAN:
                whereCondition0 = whereCondition1;
                break;
            default:
                return list;
        }

        if (TextUtils.isEmpty(SPHelper.getCompanyId())) {
            return list;
        }
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        qb.where(
                SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId()),
                SocketMessageDao.Properties.CompanyId.eq(SPHelper.getCompanyId()),
                SocketMessageDao.Properties.ConversationId.eq(conversationId), whereCondition0)
                .orderAsc(SocketMessageDao.Properties.ServerTimes)
        ;
        if (MsgConstant.LITTLE_THAN == type) {
            qb.limit(20);
        }

        list = qb.list();
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;

    }

    /**
     * 根据signId获取人员头像
     *
     * @param signId
     * @return
     */
    public String qureyAvatarUrl(String signId) {
        String url = "";
        MemberDao dao = IM.getDaoInstant().getMemberDao();
        List<Member> list = new ArrayList<>();

        if (TextUtils.isEmpty(signId)) {
            return "";
        }
        QueryBuilder<Member> qb = dao
                .queryBuilder();
        qb.where(MemberDao.Properties.Sign_id.eq(signId));
        list = qb.list();
        if (list == null || list.size() <= 0) {
            return "";
        } else {
            url = list.get(0).getPicture();
            if (TextUtils.isEmpty(url)) {
                url = list.get(0).getName();
                return url;
            }
        }
        return url;

    }

    /**
     * 更新个人头像
     *
     * @param avatarUrl
     */
    public void updateMyAvatar(String avatarUrl) {
        MemberDao dao = IM.getDaoInstant().getMemberDao();
        List<Member> list = new ArrayList<>();
        QueryBuilder<Member> qb = dao
                .queryBuilder();
        qb.where(MemberDao.Properties.MyId.eq(IM.getInstance().getImId()), MemberDao.Properties.Sign_id.eq(IM.getInstance().getImId()));
        list = qb.list();
        if (list == null || list.size() > 0) {
            list.get(0).setPicture(avatarUrl);
            DBManager.getInstance().saveOrReplace(list.get(0));
        }

    }

    /**
     * 更新头像
     *
     * @param employeeId
     * @param avatarUrl
     */
    public void updateUserAvatarByEmployeeId(String employeeId, String avatarUrl) {
        long id = TextUtil.parseLong(employeeId);
        MemberDao dao = IM.getDaoInstant().getMemberDao();
        List<Member> list = new ArrayList<>();
        QueryBuilder<Member> qb = dao
                .queryBuilder();
        qb.where(MemberDao.Properties.MyId.eq(IM.getInstance().getImId()), MemberDao.Properties.Id.eq(id));
        list = qb.list();
        if (list == null || list.size() > 0) {
            list.get(0).setPicture(avatarUrl);
            DBManager.getInstance().saveOrReplace(list.get(0));
            EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.MEMBER_MAYBE_CHANGED_TAG, null));
        }

    }

    /**
     * 更新头像
     *
     * @param signId
     * @param avatarUrl
     */
    public void updateUserAvatarBySignId(String signId, String avatarUrl) {
        MemberDao dao = IM.getDaoInstant().getMemberDao();
        List<Member> list = new ArrayList<>();
        QueryBuilder<Member> qb = dao
                .queryBuilder();
        qb.where(MemberDao.Properties.MyId.eq(IM.getInstance().getImId()), MemberDao.Properties.Sign_id.eq(signId));
        list = qb.list();
        if (list == null || list.size() > 0) {
            list.get(0).setPicture(avatarUrl);
            DBManager.getInstance().saveOrReplace(list.get(0));
            EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.MEMBER_MAYBE_CHANGED_TAG, null));
        }

    }

    /**
     * 分页查询消息
     *
     * @param companyId
     * @param conversationId
     * @return
     */
    public List<SocketMessage> qureyMessageByConversationId(long companyId, long conversationId, int offset, int pageSize) {
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        List<SocketMessage> list = new ArrayList<>();
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        qb.where(
                SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId()),
                SocketMessageDao.Properties.CompanyId.eq(companyId),
                SocketMessageDao.Properties.ConversationId.eq(conversationId))
                .orderAsc(SocketMessageDao.Properties.ServerTimes)
                .offset(offset * pageSize).limit(pageSize)
        ;
        list = qb.list();
        return list;

    }

    /**
     * 在指定会话中搜索消息
     *
     * @param conversationId
     * @param keyword
     * @return
     */
    public ArrayList<SocketMessage> queryMessage(long conversationId, String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return new ArrayList<SocketMessage>();
        }
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        WhereCondition wc1 = SocketMessageDao.Properties.MsgContent.like("%" + keyword + "%");
        WhereCondition wc2 = SocketMessageDao.Properties.FileName.like("%" + keyword + "%");
        // WhereCondition wc3 = SocketMessageDao.Properties.SenderName.like("%" + keyword + "%");
        List<SocketMessage> list = qb.where(
                SocketMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId()),
                SocketMessageDao.Properties.ConversationId.eq(conversationId)
        ).whereOr(wc1, wc2)
                .orderAsc(SocketMessageDao.Properties.ServerTimes)
                .list();

        return ((ArrayList<SocketMessage>) list);
    }

    /**
     * 聊天文件
     *
     * @param conversationId
     * @return
     */
    public ArrayList<SocketMessage> getConversationFile(long conversationId) {
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        WhereCondition whereCondition1 = SocketMessageDao.Properties.MsgType.eq(MsgConstant.IMAGE);
        WhereCondition whereCondition2 = SocketMessageDao.Properties.MsgType.eq(MsgConstant.VOICE);
        WhereCondition whereCondition3 = SocketMessageDao.Properties.MsgType.eq(MsgConstant.FILE);
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        List<SocketMessage> list = qb.where(
                SocketMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                SocketMessageDao.Properties.ConversationId.eq(conversationId)
                , SocketMessageDao.Properties.MsgType.eq(MsgConstant.FILE)
        )
                .orderAsc(SocketMessageDao.Properties.ServerTimes)
                .list();

        return ((ArrayList<SocketMessage>) list);
    }


    /**
     * 在全部会话中搜索消息
     *
     * @param keyword
     * @return
     */
    public Map<Conversation, ArrayList<SocketMessage>> queryMessage2(String keyword) {

        Map<Conversation, ArrayList<SocketMessage>> map = new HashMap<>();
        if (TextUtils.isEmpty(keyword)) {
            return map;
        }

        ConversationDao conversationDao = IM.getDaoInstant().getConversationDao();

        List<Conversation> convList = new ArrayList<>();
        convList = conversationDao
                .queryBuilder()
                .where(
                        ConversationDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                        ConversationDao.Properties.OneselfIMID.eq(IM.getInstance().getImId()),
                        ConversationDao.Properties.ConversationType.notEq(MsgConstant.SELF_DEFINED))

                .orderAsc(ConversationDao.Properties.LastMsgDate)
                .list();


        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        WhereCondition whereCondition1 = SocketMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId());
        WhereCondition whereCondition5 = SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId());
        WhereCondition whereCondition2 = SocketMessageDao.Properties.MsgContent.like("%" + keyword + "%");
        WhereCondition whereCondition3 = SocketMessageDao.Properties.FileName.like("%" + keyword + "%");

        List<SocketMessage> list = qb
                .where(whereCondition1, whereCondition5)
                .whereOr(whereCondition2, whereCondition3)
                .orderDesc(SocketMessageDao.Properties.ServerTimes)
                .list();

        for (int i = 0; i < convList.size(); i++) {
            Conversation old = convList.get(i);
            long conversationId = old.getConversationId();
            old.setResultNum(0);
            ArrayList<SocketMessage> ll = new ArrayList<SocketMessage>();
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getConversationId() == conversationId) {
                    ll.add(list.get(j));
                }
            }

            if (ll.size() > 0) {

                if (ll.get(ll.size() - 1).getMsgType() == 1) {
                    old.setLatestText(ll.get(ll.size() - 1).getMsgContent());
                } else if (ll.get(ll.size() - 1).getMsgType() == 4) {
                    old.setLatestText("[文件]" + ll.get(0).getFileName());
                }
                if (old.getConversationType() == MsgConstant.GROUP) {

                } else if (old.getConversationType() == MsgConstant.SINGLE) {
                    if (SPHelper.getUserId().equals(ll.get(0).getSenderID())) {
                        old.setTargetId(ll.get(0).getReceiverID());
                    } else {
                        old.setTargetId(ll.get(0).getSenderID());
                    }
                }
                old.setResultNum(ll.size());
                map.put(old, ll);
            } else {
                if (!TextUtils.isEmpty(old.getTitle()) && old.getTitle().contains(keyword)) {
                    map.put(old, new ArrayList<SocketMessage>());
                }
            }
        }
        return map;

    }

    /**
     * 查找小助手信息
     *
     * @param keyword
     * @return
     */
    public Map<Conversation, ArrayList<PushMessage>> queryAssistantMessage(String keyword) {
        Map<Conversation, ArrayList<PushMessage>> result = new HashMap<>();
        List<Conversation> convList = qureyAssistantList();
        if (TextUtils.isEmpty(keyword)) {
            return result;
        }
        PushMessageDao dao = IM.getDaoInstant().getPushMessageDao();
        WhereCondition whereCondition1 = PushMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId());
        WhereCondition whereCondition2 = PushMessageDao.Properties.MyId.eq(IM.getInstance().getImId());
        WhereCondition whereCondition3 = PushMessageDao.Properties.Push_content.like("%" + keyword + "%");
        WhereCondition whereCondition4 = PushMessageDao.Properties.Title.like("%" + keyword + "%");
        WhereCondition whereCondition5 = PushMessageDao.Properties.FieldInfo.like("%" + keyword + "%");
        QueryBuilder<PushMessage> qb = dao
                .queryBuilder();
        List<PushMessage> list = qb
                .where(whereCondition1, whereCondition2)
                .whereOr(whereCondition3, whereCondition4, whereCondition5)
                .orderDesc(PushMessageDao.Properties.Create_time)
                .list();
        for (int i = 0; i < convList.size(); i++) {
            Conversation old = convList.get(i);
            long conversationId = old.getConversationId();
            if (conversationId <= 0) {
                continue;
            }
            old.setResultNum(0);
            ArrayList<PushMessage> ll = new ArrayList<>();
            for (int j = 0; j < list.size(); j++) {
                if (((conversationId - MsgConstant.DEFAULT_VALUE) + "").equals(list.get(j).getAssistant_id())) {
                    ll.add(list.get(j));
                }
            }
            if (ll.size() > 0) {
                old.setResultNum(ll.size());
                /*if (ll.size() == 1) {
                    old.setLatestMessage(ll.get(0).getPush_content());
                    old.setAvatarUrl(ll.get(0).getIcon_url());
                } else if (ll.size() > 1) {
                    old.setLatestMessage(ll.get(0).getPush_content());
                }*/
                old.setLatestMessage(ll.get(0).getPush_content());
                old.setAvatarUrl(ll.get(0).getIcon_url());
                result.put(old, ll);
            } else if (!TextUtils.isEmpty(old.getTitle()) && old.getTitle().contains(keyword)) {
                result.put(old, ll);
            }
        }

        return result;
    }

    /**
     * 查询指定小助手中的信息
     *
     * @param assistantId
     * @param keyword
     * @return
     */
    public List<PushMessage> queryAssistantMessage(String assistantId, String keyword) {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(assistantId)) {
            return new ArrayList<PushMessage>();
        }
        WhereCondition whereCondition1 = PushMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId());
        WhereCondition whereCondition2 = PushMessageDao.Properties.MyId.eq(IM.getInstance().getImId());
        WhereCondition whereCondition6 = PushMessageDao.Properties.Assistant_id.eq(assistantId);
        WhereCondition whereCondition3 = PushMessageDao.Properties.Push_content.like("%" + keyword + "%");
        WhereCondition whereCondition4 = PushMessageDao.Properties.Title.like("%" + keyword + "%");
        WhereCondition whereCondition5 = PushMessageDao.Properties.FieldInfo.like("%" + keyword + "%");
        QueryBuilder<PushMessage> qb = IM.getDaoInstant().getPushMessageDao()
                .queryBuilder();
        List<PushMessage> list = qb
                .where(whereCondition1, whereCondition2, whereCondition6)
                .whereOr(whereCondition3, whereCondition4, whereCondition5)
                .orderAsc(PushMessageDao.Properties.Create_time)
                .list();
        return list;
    }

    /**
     * 查询群聊
     *
     * @param keyword
     * @return
     */
    public Map<Conversation, ArrayList<SocketMessage>> queryGroupMessage(String keyword) {

        Map<Conversation, ArrayList<SocketMessage>> result = new HashMap<>();
        if (TextUtils.isEmpty(keyword)) {
            return result;
        }
        List<Conversation> convList = qureyGroupList();
        SocketMessageDao dao = IM.getDaoInstant().getSocketMessageDao();
        WhereCondition whereCondition1 = SocketMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId());
        WhereCondition whereCondition2 = SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId());
        WhereCondition whereCondition3 = SocketMessageDao.Properties.MsgContent.like("%" + keyword + "%");
        WhereCondition whereCondition4 = SocketMessageDao.Properties.SenderName.like("%" + keyword + "%");
        WhereCondition whereCondition5 = SocketMessageDao.Properties.FileName.like("%" + keyword + "%");
        QueryBuilder<SocketMessage> qb = dao
                .queryBuilder();
        List<SocketMessage> list = qb
                .where(whereCondition1, whereCondition2)
                .whereOr(whereCondition3, whereCondition4, whereCondition5)
                .orderAsc(SocketMessageDao.Properties.ServerTimes)
                .list();
        for (int i = 0; i < convList.size(); i++) {
            Conversation old = convList.get(i);
            long conversationId = old.getConversationId();
            if (conversationId <= 0) {
                continue;
            }
            old.setResultNum(0);
            ArrayList<SocketMessage> ll = new ArrayList<>();
            for (int j = 0; j < list.size(); j++) {
                if (conversationId == list.get(j).getConversationId()) {
                    ll.add(list.get(j));
                }
            }
            if (ll.size() > 0) {
                old.setResultNum(ll.size());
                //old.setLatestMessage(ll.get(0).getMsgContent());
                old.setAvatarUrl(ll.get(0).getSenderAvatar());
                result.put(old, ll);
            }
        }

        return result;
    }

    /**
     * 将所有发送的消息状态改为已读
     *
     * @param conversationId
     */
    public int markAllMessageRead(long conversationId) {
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        List<SocketMessage> list = new ArrayList<>();
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        qb.where(SocketMessageDao.Properties.ConversationId.eq(conversationId),
                SocketMessageDao.Properties.SendState.eq(1))
                .orderAsc(SocketMessageDao.Properties.ServerTimes);
        list = qb.list();
        for (int i = 0; i < list.size(); i++) {
            SocketMessage m = list.get(i);
            m.setSendState(3);
            saveOrReplace(m);
        }
        return list.size();
    }

    /**
     * 更新会话草稿
     *
     * @param conversationId
     * @param draft
     */
    public void updateDraft(long conversationId, String draft) {
        ConversationDao conv = IM.getDaoInstant().getConversationDao();
        List<Conversation> list = new ArrayList<>();
        QueryBuilder<Conversation> qb = conv
                .queryBuilder();
        qb.where(ConversationDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                ConversationDao.Properties.ConversationId.eq(conversationId))
                .orderAsc(ConversationDao.Properties.LastMsgDate);
        list = qb.list();
        if (list.size() >= 1) {
            Conversation c = list.get(0);
            c.setDraft(draft);
            saveOrReplace(c);
        }
    }

    /**
     * 更新未读数
     *
     * @param conversationId
     */
    public void updateUnreadNumber(long conversationId) {
        ConversationDao conv = IM.getDaoInstant().getConversationDao();
        List<Conversation> list = new ArrayList<>();
        QueryBuilder<Conversation> qb = conv
                .queryBuilder();
        qb.where(ConversationDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                ConversationDao.Properties.ConversationId.eq(conversationId))
                .orderAsc(ConversationDao.Properties.LastMsgDate);
        list = qb.list();
        if (list.size() >= 1) {
            Conversation c = list.get(0);
            c.setUnreadMsgCount(0);
            saveOrReplace(c);
        }
    }

    /**
     * 根据公司id查询会话列表
     *
     * @return
     */
    public List<Conversation> qureyConversationList(String companyId) {
        if (TextUtils.isEmpty(companyId)) {
            return new ArrayList<Conversation>();
        }
        ConversationDao conversationDao = IM.getDaoInstant().getConversationDao();

        List<Conversation> list = new ArrayList<>();
        list = conversationDao
                .queryBuilder()
                .where(ConversationDao.Properties.CompanyId.eq(companyId), ConversationDao.Properties.MyId.eq(IM.getInstance().getImId()))
                .list();
        return list;

    }

    /**
     * 获取小助手列表
     *
     * @return
     */
    public List<Conversation> qureyAssistantList() {
        ConversationDao conversationDao = IM.getDaoInstant().getConversationDao();

        List<Conversation> list = conversationDao
                .queryBuilder()
                .where(ConversationDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                        ConversationDao.Properties.ConversationType.eq(MsgConstant.SELF_DEFINED),
                        ConversationDao.Properties.MyId.eq(IM.getInstance().getImId()))
                .list();
        return list;

    }

    /**
     * 查询群聊
     *
     * @return
     */
    public List<Conversation> qureyGroupList() {
        ConversationDao conversationDao = IM.getDaoInstant().getConversationDao();
        List<Conversation> list = conversationDao
                .queryBuilder()
                .where(ConversationDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                        ConversationDao.Properties.ConversationType.eq(MsgConstant.GROUP),
                        ConversationDao.Properties.MyId.eq(IM.getInstance().getImId()))
                .list();
        return list;
    }

    /**
     * 获取单个小助手
     *
     * @param assistantId
     * @return
     */
    public List<Conversation> qureyAssistanById(String assistantId) {
        ConversationDao conversationDao = IM.getDaoInstant().getConversationDao();
        List<Conversation> list = conversationDao
                .queryBuilder()
                .where(ConversationDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                        ConversationDao.Properties.ConversationType.eq(MsgConstant.GROUP),
                        ConversationDao.Properties.MyId.eq(IM.getInstance().getImId()),
                        ConversationDao.Properties.ConversationId.eq(assistantId))

                .list();
        return list;
    }

    /**
     * 清除草稿
     *
     * @param conversationId
     */
    public void cleanDraft(long conversationId) {
        ConversationDao conversationDao = IM.getDaoInstant().getConversationDao();

        List<Conversation> list = new ArrayList<>();
        list = conversationDao
                .queryBuilder()
                .where(ConversationDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                        ConversationDao.Properties.ConversationId.eq(conversationId))
                .list();
        if (list.size() > 0) {

            Conversation c = list.get(0);
            c.setDraft("");
            saveOrReplace(c);
        }
    }


    /**
     * 跟新消息发送状态
     *
     * @param bean
     * @return
     */
    public boolean updateMessage(SocketMessage bean) {
//        Log.e("ack服务器时间", bean.getServerTimes() + "");
//        Log.e("ack客户端时间", bean.getClientTimes() + "");
        if (bean.getUsCmdID() == 1) {
            return false;
        }
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        List<SocketMessage> list = new ArrayList<>();
        list = socketMessageDao
                .queryBuilder()
                .where(SocketMessageDao.Properties.ClientTimes.eq(bean.getClientTimes()), SocketMessageDao.Properties.Rand.eq(bean.getRand()))
                .list();
        if (list.size() > 0) {
            SocketMessage old = list.get(0);
            if (old.getSendState() == 6 || old.getSendState() == 2 || old.getSendState() == 4) {
                old.setSendState(1);
                old.setServerTimes(bean.getServerTimes());
                saveOrReplace(old);
                return true;
            }

        }
// 1514261691328 1514261692513 1514261677784 1514261677784
        return false;

    }

    /**
     * 查询未读消息并标记为已读
     *
     * @param conversationId
     */
    public void qureyUnreadMessage(long conversationId) {
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        List<SocketMessage> list = new ArrayList<>();
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        qb.where(SocketMessageDao.Properties.CompanyId.eq(IM.getInstance().getCompanyId()),
                SocketMessageDao.Properties.ConversationId.eq(conversationId), SocketMessageDao.Properties.IsRead.eq(false))
                .orderAsc(SocketMessageDao.Properties.ServerTimes);
        list = qb.list();
        for (int i = 0; i < list.size(); i++) {
            SocketMessage message = list.get(i);
            message.setIsRead(true);
            saveOrReplace(message);
        }

    }

    /**
     * 更新已读状态
     *
     * @param bean
     */
    public void updateMessageReadState(SocketMessage bean) {

    }

    /**
     * 更新已读数量
     *
     * @param msg
     * @param size
     */
    public void updateMessageReadNum(SocketMessage msg, int size) {
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        List<SocketMessage> list = new ArrayList<>();
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        qb.where(SocketMessageDao.Properties.MsgID.eq(Long.parseLong(msg.getClientTimes() + "" + msg.getRand())));
        list = qb.list();
        if (list.size() > 0) {
            SocketMessage socketMessage = list.get(0);
            socketMessage.setReadnum(size);
            saveOrReplace(socketMessage);
        }

    }

    /**
     * 删除会话消息
     *
     * @param object
     */
    public void deleteMessageByConversationId(long object) {
        List<SocketMessage> list = qureyMessageByConversationId(IM.getInstance().getCompanyId(), object);
        //delete(list);
        for (int i = 0; i < list.size(); i++) {
            boolean delete = delete(list.get(i));
        }
        List<Conversation> conversations = qureyConversationList(IM.getInstance().getCompanyId());
        for (int i = 0; i < conversations.size(); i++) {
            if (conversations.get(i).getConversationId() == object) {
                delete(conversations.get(i));
            }


        }
    }

    /**
     * 删除撤回的消息并提醒
     * 撤回消息
     *
     * @param msgId
     */
    public void deleteMessageAndNotify(long msgId) {
        List<SocketMessage> list = queryMessageById(msgId);
        for (int i = 0; i < list.size(); i++) {
            delete(list.get(i));
        }
        if (list != null && list.size() > 0) {
            IM.getInstance().sendRecallNotify(list.get(0));
        }


    }

    /**
     * 通过消息id查询消息
     *
     * @param msgId
     * @return
     */
    public List<SocketMessage> queryMessageById(long msgId) {
        SocketMessageDao socketMessageDao = IM.getDaoInstant().getSocketMessageDao();
        List<SocketMessage> list = new ArrayList<>();
        QueryBuilder<SocketMessage> qb = socketMessageDao
                .queryBuilder();
        qb.where(
                SocketMessageDao.Properties.MyId.eq(IM.getInstance().getImId()),
                SocketMessageDao.Properties.MsgID.eq(msgId))
                .orderAsc(SocketMessageDao.Properties.ServerTimes)
        ;
        list = qb.list();


        return list;

    }

    /**
     * 更新撤回失败状态
     *
     * @param m
     */
    public void updateMessageRecallState(SocketMessage m) {
        List<SocketMessage> list = queryMessageById(m.getMsgID());
        if (list != null && list.size() > 0) {
            m.setSendState(5);
            saveOrReplace(m);

        }

    }

    /**
     * 根据sign_id查询员工信息
     *
     * @param signId
     * @return
     */
    public List<Member> getMemberBySignId(String signId) {
        if (TextUtils.isEmpty(signId)) {
            return new ArrayList<Member>();
        }
        MemberDao memberDao = IM.getDaoInstant().getMemberDao();
        List<Member> list = new ArrayList<>();
        QueryBuilder<Member> qb = memberDao
                .queryBuilder();
        qb.where(
                MemberDao.Properties.Company_id.eq(IM.getInstance().getCompanyId()),
                MemberDao.Properties.Sign_id.eq(signId));
        list = qb.list();


        return list;

    }

    /**
     * 根据employee_id查询员工信息
     *
     * @param employeeId
     * @return
     */
    public List<Member> getMemberByEmployeeId(String employeeId) {
        if (TextUtils.isEmpty(employeeId)) {
            return new ArrayList<Member>();
        }
        MemberDao memberDao = IM.getDaoInstant().getMemberDao();
        List<Member> list = new ArrayList<>();
        QueryBuilder<Member> qb = memberDao
                .queryBuilder();
        qb.where(
                MemberDao.Properties.Company_id.eq(IM.getInstance().getCompanyId()),
                MemberDao.Properties.MyId.eq(IM.getInstance().getImId()),
                MemberDao.Properties.Id.eq(employeeId));
        list = qb.list();


        return list;

    }

    /**
     * 根据小助手id获取数据
     *
     * @param assistantId
     * @return
     */
    public List<PushMessage> queryPushMessageByAssistantId(boolean showReaded, String assistantId, String filterBeanName) {
        WhereCondition w1 = PushMessageDao.Properties.CompanyId.eq(SPHelper.getCompanyId());
        WhereCondition w2 = PushMessageDao.Properties.MyId.eq(IM.getInstance().getImId());
        WhereCondition w3 = PushMessageDao.Properties.Assistant_id.eq(assistantId);
        WhereCondition w4 = PushMessageDao.Properties.Bean_name.eq(filterBeanName);
        WhereCondition w5 = PushMessageDao.Properties.Read_status.eq("0");

        PushMessageDao dao = IM.getDaoInstant().getPushMessageDao();
        List<PushMessage> list = new ArrayList<>();
        QueryBuilder<PushMessage> qb = dao
                .queryBuilder();
        if (!showReaded) {
            if (TextUtils.isEmpty(filterBeanName)) {
                qb.where(w1, w2, w3)
                        .orderDesc(PushMessageDao.Properties.Create_time)
                ;
            } else {
                qb.where(w1, w2, w3, w4)
                        .orderDesc(PushMessageDao.Properties.Create_time)
                ;
            }

        } else {
            if (TextUtils.isEmpty(filterBeanName)) {
                qb.where(w1, w2, w3, w5)
                        .orderDesc(PushMessageDao.Properties.Create_time)
                ;
            } else {
                qb.where(w1, w2, w3, w4, w5)
                        .orderDesc(PushMessageDao.Properties.Create_time)
                ;
            }

        }

        list = qb.list();
        return list;
    }

    /**
     * 查询指定范围内的数据
     *
     * @param assistantId
     * @param time
     * @param num
     * @return
     */
    public List<PushMessage> queryPushMessageByAssistantId(String assistantId, String time, int num) {
        PushMessageDao dao = IM.getDaoInstant().getPushMessageDao();
        List<PushMessage> list = new ArrayList<>();
        QueryBuilder<PushMessage> qb = dao
                .queryBuilder();
        qb.where(
                PushMessageDao.Properties.CompanyId.eq(SPHelper.getCompanyId()),
                PushMessageDao.Properties.Create_time.lt(time),
                PushMessageDao.Properties.MyId.eq(IM.getInstance().getImId()),
                PushMessageDao.Properties.Assistant_id.eq(assistantId))
                .orderAsc(PushMessageDao.Properties.Create_time).limit(num)
        ;
        list = qb.list();
        return list;
    }

    /**
     * 删除旧数据
     *
     * @param assistantId
     */
    public void deletePushMessageByAssistantId(String assistantId) {
        List<PushMessage> list = queryPushMessageByAssistantId(true, assistantId, null);
        //delete(list);
        for (int i = 0; i < list.size(); i++) {
            boolean delete = delete(list.get(i));
        }
    }

    /**
     * 移除小助手
     *
     * @param assistantId
     */
    public void deleteAssistant(String assistantId) {
        final List<Conversation> conversations = qureyAssistanById(assistantId);
        if (conversations != null && conversations.size() > 0) {
            delete(conversations.get(0));
        }
    }

    /**
     * 更新旧数据
     *
     * @param messge
     */
    public void updateOldPushMessage(PushMessage messge) {
        WhereCondition w1 = PushMessageDao.Properties.CompanyId.eq(SPHelper.getCompanyId());
        WhereCondition w2 = PushMessageDao.Properties.MyId.eq(IM.getInstance().getImId());
        WhereCondition w3 = PushMessageDao.Properties.Id.eq(messge.getIm_apr_id());

        PushMessageDao dao = IM.getDaoInstant().getPushMessageDao();
        List<PushMessage> list = new ArrayList<>();
        QueryBuilder<PushMessage> qb = dao
                .queryBuilder();
        qb.where(w1, w2, w3).orderDesc(PushMessageDao.Properties.Create_time);
        list = qb.list();
        if (list != null && list.size() > 0) {
            PushMessage pushMessage = list.get(0);
            pushMessage.setParam_fields(messge.getParam_fields());
            final boolean b = saveOrReplace(pushMessage);
            if (b) {
                //更新完成
                EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.TYPE_APPROVE_OPERATION, pushMessage));
            }


        }
    }

    /**
     * 标记小助手所有消息为已读
     *
     * @param assistantId
     * @return
     */
    public int markAllPushMessageRead(String assistantId) {
        List<PushMessage> list = queryPushMessageByAssistantId(false, assistantId, null);
        //delete(list);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRead_status("1");
            saveOrReplace(list.get(i));
        }
        return list.size();
    }

    /**
     * 标记小助手一条消息为已读
     *
     * @param dataId
     * @return
     */
    public boolean markOnePushMessageRead(String dataId) {
        boolean flag = false;
        PushMessageDao dao = IM.getDaoInstant().getPushMessageDao();
        List<PushMessage> list = new ArrayList<>();
        QueryBuilder<PushMessage> qb = dao
                .queryBuilder();
        qb.where(
                PushMessageDao.Properties.CompanyId.eq(SPHelper.getCompanyId()),
                PushMessageDao.Properties.MyId.eq(IM.getInstance().getImId()),
                PushMessageDao.Properties.Id.eq(dataId))
                .orderDesc(PushMessageDao.Properties.Create_time)
        ;
        list = qb.list();
        if (list != null && list.size() > 0) {
            if ("1".equals(list.get(0).getRead_status())) {
                flag = false;
            } else {
                list.get(0).setRead_status("1");
                saveOrReplace(list.get(0));
                flag = true;
            }

        }
        return flag;
    }
}
