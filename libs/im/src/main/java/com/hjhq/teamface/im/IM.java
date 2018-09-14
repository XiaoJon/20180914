package com.hjhq.teamface.im;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.bean.ImDataBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.QxMessage;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.DbHelper;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.database.gen.DaoMaster;
import com.hjhq.teamface.basis.database.gen.DaoSession;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.BytesTransUtil;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.im.db.DBManager;
import com.hjhq.teamface.im.bean.CmdHeadPojo;
import com.hjhq.teamface.im.bean.LoadLevelingHead;
import com.hjhq.teamface.im.bean.LoginRequestPojo;
import com.hjhq.teamface.im.bean.MsgPojo;
import com.hjhq.teamface.im.bean.OuterHeadPojo;
import com.hjhq.teamface.im.bean.PullMessage;
import com.hjhq.teamface.im.bean.PushPojo;
import com.hjhq.teamface.im.util.ParseUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Administrator
 * @date 2017/11/22
 * Describe： 企信主控制类
 */

public class IM {
    //没事儿,放心,引用的Application的Context,没有引用Activity的.
    private static IM mIm = null;
    private Context mContext;
    private Context activityContext;
    private static DaoSession daoSession;
    //与服务器时间偏差
    private long serverTimeOffsetValue = 0L;
    /**
     * 消息随机码
     */
    private static int flag = 1000;

    private String userId = "";
    private long userIdLong = 0L;
    private String companyId;
    private String name;
    private String avatar;
    //当前聊天群成员数量
    private int groupMemberNum;
    private IMService mService;
    private static DbHelper helper;
    private static SQLiteDatabase db;
    private static DaoMaster daoMaster;

    private IM() {
    }

    /**
     * 初始化
     *
     * @return
     */
    public static IM getInstance() {
        if (mIm == null) {
            mIm = new IM();
        }
        return mIm;
    }

    /**
     * 获取long型imid
     *
     * @return
     */
    public long getImIdLong() {
        try {
            return Long.parseLong(getImId());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0L;
        }
    }


    public static DaoSession getDaoInstant() {
        if (daoSession == null) {
            IM.getInstance().setupDatabase();
        }
        return daoSession;
    }

    /**
     * 打开服务,配置数据库
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        if (null == mIm) {
            mIm = new IM();
        }
        EventBusUtils.register(getInstance());
        startIMService(context);

        //配置数据库
        setupDatabase();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageBean bean) {
        if (TextUtils.isEmpty(bean.getTag())) {
            return;
        }
        ImDataBean dataBean;
        switch (bean.getTag()) {
            case MsgConstant.SEND_FILE_MESSAGE_FAILED:
                dataBean = (ImDataBean) bean.getObject();
                if (dataBean == null) {
                    return;
                }
                sendFileMessageFailed(dataBean.getBytes(), dataBean.getMessage());
                break;
            case MsgConstant.SEND_UPLOADED_FILE_MESSAGE:
                dataBean = (ImDataBean) bean.getObject();
                if (dataBean == null) {
                    return;
                }
                QxMessage message = dataBean.getMessage();
                List<SocketMessage> list = DBManager.getInstance().queryMessageById(TextUtil.parseLong(message.getMsgId()));
                if (list != null && list.size() > 0) {
                    SocketMessage socketMessage = list.get(0);
                    socketMessage.setFileType(message.getFileType());
                    socketMessage.setFileSize(message.getFileSize());
                    socketMessage.setFileUrl(message.getFileUrl());
                    DBManager.getInstance().saveOrReplace(socketMessage);
                    sendMessage(false, dataBean.getBytes(), message);
                }
                /*boolean flag = MsgConstant.IM_TEAM_CHAT_CMD == bean.getCode();
                if (flag) {
                    String json = new Gson().toJson(message);
                    String msgContent = null;
                    try {
                        msgContent = new String(json.getBytes(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    byte[] bytes = mergeRequestInfo(Arrays.copyOfRange(dataBean.getBytes(), 0, 49), msgContent.getBytes());
                    ParseUtil.saveMessage(bytes);
                    message.setAllPeoples("");
                }
                sendMessage(!flag, dataBean.getBytes(), message);*/
                break;
            default:

                break;
        }

    }

    /**
     * 启动企信服务
     *
     * @param context
     */
    public void startIMService(Context context) {
        Intent intent = new Intent(context, IMService.class);
        try {
            if (!SystemFuncUtils.isServiceRunning(mContext, IMService.class.getName())) {
                context.startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动企信服务
     *
     * @param context
     */
    public void startDownloadService(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        try {
            if (!SystemFuncUtils.isServiceRunning(mContext, DownloadService.class.getName())) {
                context.startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stopService(Context context) {
//        if (mService != null) {
//            mService.stopForeground(true);
//        }
    }


    /**
     * 初始公司ID
     *
     * @param companyId
     */
    public void initCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void initName(String name) {
        this.name = name;
    }

    public void initAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void initID(String userID) {
        this.userId = userID;
    }


    /**
     * 初始数据库
     */
    public void setupDatabase() {
        if (mContext == null) {
            AppManager.restartApp();
            return;
        }
        //创建数据库
        //DaoMaster.DevOpenHelper helper1 = new DaoMaster.DevOpenHelper(this, Constants.DATABASE_NAME);
        //自定义的 OpenHelper
        //DbHelper helper = new DbHelper(mContext, MsgConstant.DATABASE_NAME);
        helper = new DbHelper(mContext, getCompanyId() + "_" + getImId());
        //获取可写数据库
        // TODO: 2018/7/19  java.lang.NullPointerException

        db = helper.getWritableDatabase();
        //获取数据库对象
        daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
       /* Schema schema = new Schema(1000, mContext.getPackageName());
        Entity socketMessage = schema.addEntity("SocketMessage");
        socketMessage.setDbName();
        Entity conversation = schema.addEntity("Conversation");

        addNote(schema);
        try {
            new DaoGenerator().generateAll(schema, "./DaoExample/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty().primaryKey().autoincrement();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }


    /**
     * 登录
     */
    public void login() {

        userId = getImId();
        if (!TextUtils.isEmpty(userId)) {
            login(userId);
        } else {

            // Toast.makeText(mContext, "请重新登录", Toast.LENGTH_LONG).show();
            // TODO: 2017/12/13 企信账号为空,需要重新登录
        }
    }

    /**
     * 获取负载均衡url
     */
    public void getLlServerUrl() {
        if (!Constants.USE_LOAD_LEVELING) {
            return;
        }
        try {
            LoadLevelingHead llHead = new LoadLevelingHead();
            llHead.setNetFlag(MsgConstant.LL_IDENTITY);
            llHead.setState((byte) 0);
            llHead.setImId(getImIdLong());
            llHead.setDeviceType((byte) MsgConstant.DEVICE_TYPE);
            byte[] bytes = llHead.toByte();
            send(false, bytes, MsgConstant.IM_ACTION_LOAD_LEVELING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * im登录
     *
     * @param imId
     * @return
     */
    public boolean login(String imId) {

        try {
            // 命令包头
            long imid = getImIdLong();
            if (imid == 0L) {
                Log.e("IM", "Login failed.Cause account is null.");
                return false;
            }

            byte[] cmdHead = setCmdHead(getImIdLong(), MsgConstant.IM_LOG_IN_CMD,
                    TextUtil.parseLong(SPHelper.getImei()), 1, MsgConstant.DEVICE_TYPE);
            // 登录
            LoginRequestPojo loginRequestPojo = new LoginRequestPojo();
            loginRequestPojo.setSzUsername(imId);
            loginRequestPojo.setChStatus((byte) 1);
            loginRequestPojo.setChUserType((byte) 1);
            byte[] loginReq = loginRequestPojo.toByte();
            // 请求数据
            byte[] reqData = mergeRequestInfo(cmdHead, loginReq);
            send(false, reqData, MsgConstant.IM_ACTION_LOGIN);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public boolean login(long userId) {
        if (userId == -1L) {
            // TODO: 2017/12/18 获取企信id错误,请重新登录
            // Toast.makeText(mContext, "获取企信id错误,请重新登录 ", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            // 命令包头
            byte[] cmdHead = setCmdHead(userId, MsgConstant.IM_LOG_IN_CMD, 1, 1, MsgConstant.DEVICE_TYPE);
            // 登录
            LoginRequestPojo loginRequestPojo = new LoginRequestPojo();
            loginRequestPojo.setSzUsername(userId + "");
            loginRequestPojo.setChStatus((byte) 1);
            loginRequestPojo.setChUserType((byte) 1);
            byte[] loginReq = loginRequestPojo.toByte();
            // 请求数据
            byte[] reqData = mergeRequestInfo(cmdHead, loginReq);
            send(false, reqData, MsgConstant.IM_ACTION_LOGIN);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 发送广播
     *
     * @param needSave
     * @param reqData
     * @param action
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void send(boolean needSave, byte[] reqData, String action) {

        Intent intent = new Intent();
        intent.putExtra(MsgConstant.MSG_DATA, reqData);
        intent.setAction(action);
        try {
            if (!SystemFuncUtils.isServiceRunning(mContext, IMService.class.getName())) {
                IM.getInstance().writeFileToSDCard("企信服务未启动,正在启动");
                startIMService(mContext);
            } else {
                IM.getInstance().writeFileToSDCard("企信服务已启动");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (MsgConstant.IM_ACTION_SEND.equals(action) && needSave) {
            //将消息保存到数据库
            ParseUtil.saveMessage(reqData);
        }
        mContext.sendBroadcast(intent);
    }

    /**
     * 发送广播
     *
     * @param action
     */
    public void sendBoardcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (mContext != null) {
            intent.setPackage(mContext.getPackageName());
            mContext.sendBroadcast(intent);
        }
        //只限在应用内使用的广播,安全高效
        // LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }


    /**
     * 发送ack回应包
     *
     * @param b
     */
    public void sendAckMessage(byte[] b) {
        send(false, b, MsgConstant.IM_ACTION_ACK);
    }


    /**
     * 发送群文本消息
     *
     * @param atList
     * @param conversationId
     * @param content
     */
    public void sendTeamTextMessage(ArrayList<QxMessage.AtListBean> atList, long conversationId, String content, String groupPeoples) {
        try {
            // 命令包头
            byte[] cmdHead = setCmdHead(getImIdLong(), MsgConstant.IM_TEAM_CHAT_CMD, getImIdLong(), conversationId, MsgConstant.DEVICE_TYPE);
            // 消息转码
            QxMessage message = new QxMessage();
            message.setSenderAvatar(getavatar());
            message.setSenderName(getName());
            message.setType(MsgConstant.TEXT);
            message.setChatId(conversationId);
            message.setMsg(content);
            message.setAtList(atList);
            String json = new Gson().toJson(message);
            String msgContent = new String(json.getBytes(), "UTF-8");
            byte[] msgData = mergeRequestInfo(cmdHead, msgContent.getBytes());
            send(false, msgData, MsgConstant.IM_ACTION_SEND);
            message.setAllPeoples(groupPeoples);
            String json2 = new Gson().toJson(message);
            String msgContent2 = new String(json2.getBytes(), "UTF-8");
            byte[] msgData2 = mergeRequestInfo(cmdHead, msgContent2.getBytes());
            ParseUtil.saveMessage(msgData2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 退群消息
     *
     * @param conversationId
     */
    public void sendQuitTeamMessage(long conversationId) {
        try {
            // 命令包头
            byte[] cmdHead = setCmdHead(getImIdLong(),
                    MsgConstant.IM_TEAM_CHAT_CMD, getImIdLong(),
                    conversationId, MsgConstant.DEVICE_TYPE,
                    MsgConstant.IM_QUIT_GROUP_FLAG);
            // 消息转码
            QxMessage message = new QxMessage();
            message.setType(MsgConstant.NOTIFICATION);
            message.setChatId(conversationId);
            message.setMsg(getName() + "退出了该群。");
            String json = new Gson().toJson(message);
            String msgContent = new String(json.getBytes(), "UTF-8");
            // 拼装数据
            byte[] msgData = mergeRequestInfo(cmdHead, msgContent.getBytes());
            send(true, msgData, MsgConstant.IM_ACTION_SEND);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送群通知
     *
     * @param teamId
     * @param content
     */
    public void sendTeamNotificationMessage(long teamId, String content) {
        try {
            // 命令包头
            byte[] cmdHead = setCmdHead(getImIdLong(), MsgConstant.IM_TEAM_CHAT_CMD, getImIdLong(), teamId, MsgConstant.DEVICE_TYPE);
            // 消息转码
            QxMessage message = new QxMessage();
            message.setSenderAvatar(getavatar());
            message.setSenderName("通知");
            message.setType(MsgConstant.NOTIFICATION);
            message.setChatId(teamId);
            message.setMsg(content);
            String json = new Gson().toJson(message);
            String msgContent = new String(json.getBytes(), "UTF-8");
            // 拼装数据
            byte[] msgData = mergeRequestInfo(cmdHead, msgContent.getBytes());
            send(true, msgData, MsgConstant.IM_ACTION_SEND);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建群聊图片消息
     *
     * @param receiverId
     * @return
     */
    public byte[] createTeamImageMessage(long receiverId) {
        // 命令包头
        QxMessage message = new QxMessage();
        message.setSenderAvatar(getavatar());
        message.setSenderName(getName());
        message.setChatId(receiverId);
        message.setSenderName(getName());
        message.setType(MsgConstant.IMAGE);
        String json = new Gson().toJson(message);
        String msgContent = null;
        try {
            msgContent = new String(json.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mergeRequestInfo(setCmdHead(getImIdLong(), MsgConstant.IM_TEAM_CHAT_CMD, getImIdLong(), receiverId, MsgConstant.DEVICE_TYPE)
                , msgContent.getBytes());
    }


    /**
     * 获取离线消息(登录后会主动推送,不需要去请求)
     *
     * @param sender
     * @return
     */
    @Deprecated
    public boolean getOfflineMessage(String sender) {
        try {
            // 命令包头
            byte[] cmdHead = setCmdHead(getImIdLong(), MsgConstant.IM_REQUEST_OFFLINE_MSG, 1, 1, MsgConstant.DEVICE_TYPE);
            // 登录
            LoginRequestPojo loginRequestPojo = new LoginRequestPojo();
            loginRequestPojo.setSzUsername(sender);
            loginRequestPojo.setChStatus((byte) 1);
            loginRequestPojo.setChUserType((byte) 1);
            byte[] loginReq = loginRequestPojo.toByte();
            // 请求数据
            byte[] reqData = mergeRequestInfo(cmdHead, loginReq);
            boolean flag = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;

    }

    /**
     * 注销
     *
     * @param sender
     * @return
     */
    @Deprecated
    public boolean logOff(String sender) {
        try {
            // 命令包头
            byte[] cmdHead = setCmdHead(getImIdLong(), MsgConstant.IM_LOG_OUT_CMD, 1, 1, MsgConstant.DEVICE_TYPE);
            // 登录
            LoginRequestPojo loginRequestPojo = new LoginRequestPojo();
            loginRequestPojo.setSzUsername(sender);
            loginRequestPojo.setChStatus((byte) 1);
            loginRequestPojo.setChUserType((byte) 1);
            byte[] loginReq = loginRequestPojo.toByte();
            // 请求数据
            byte[] reqData = mergeRequestInfo(cmdHead, loginReq);
            send(false, reqData, MsgConstant.IM_ACTION_LOGOUT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;

    }

    /**
     * 发送单条推送
     *
     * @param content
     * @param senderId
     * @param receiverId
     * @return
     */
    @Deprecated
    public synchronized boolean singlePush(String sender, long senderId, long receiverId, String content) {
        try {
            // 命令包头
            byte[] cmdHead = setCmdHead(getImIdLong(), MsgConstant.IM_PERSONAL_CHAT_CMD, senderId, receiverId, MsgConstant.DEVICE_TYPE);
            // 消息转码
            String msgContent = new String(content.getBytes(), "UTF-8");
            // 拼装数据
            byte[] msgData = mergeRequestInfo(cmdHead, msgContent.getBytes());
//            Log.e("cmdHead==" + cmdHead.length, "msgData==" + msgData.length);
//            Log.e("发送消息,内容======", (new String(Arrays.copyOfRange(msgData, 49, msgData.length))));

            send(false, msgData, MsgConstant.IM_ACTION_SEND);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return true;
    }


    public boolean sendSingleTextMessage(String content, long receiverId, long conversationId) {
        // 命令包头
        QxMessage message = new QxMessage();
        message.setSenderAvatar(getavatar());
        message.setSenderName(getName());
        message.setType(MsgConstant.TEXT);
        message.setChatId(conversationId);
        message.setMsg(content);
        String json = new Gson().toJson(message);
        sendMessage(json, receiverId, MsgConstant.IM_PERSONAL_CHAT_CMD);

        return true;
    }

    /**
     * @param chatType
     * @param id
     * @param timeStamp
     * @return
     */
    public boolean sendPullHistoryMessage(int chatType, long id, long timeStamp, int num) {
        byte type = 0;
        if (chatType == 1) {
            type = 1;
        } else if (chatType == 2) {
            type = 2;
        }

        // 命令包头
        PullMessage message = new PullMessage();
        message.setMsgType(type);
        message.setId(id);
        message.setTimeStamp(timeStamp);
        message.setNum(((short) num));
        //Log.e("历史", json);
        //ToastUtils.showToast(mContext, json);
        byte[] cmdHead = setCmdHead(getImIdLong(), MsgConstant.IM_PULL_HISTORY_MESSAGE, getImIdLong(), id, MsgConstant.DEVICE_TYPE);
        try {
            byte[] reqData = mergeRequestInfo(cmdHead, message.toByte());
            send(false, reqData, MsgConstant.IM_ACTION_HISTORY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    public byte[] createSingleImageMessage(long conversationId, long receiverId) {
        // 命令包头
        QxMessage message = new QxMessage();
        message.setSenderAvatar(getavatar());
        message.setSenderName(getName());
        message.setChatId(conversationId);
        message.setType(MsgConstant.IMAGE);
        String json = new Gson().toJson(message);
        String msgContent = null;
        try {
            msgContent = new String(json.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return mergeRequestInfo(setCmdHead(getImIdLong(), MsgConstant.IM_PERSONAL_CHAT_CMD, getImIdLong(), receiverId, MsgConstant.DEVICE_TYPE)
                , msgContent.getBytes());
    }

    public byte[] createSingleImageMessage(long receiverId, QxMessage message) {
        // 命令包头

        String json = new Gson().toJson(message);
        String msgContent = null;
        try {
            msgContent = new String(json.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return mergeRequestInfo(setCmdHead(getImIdLong(), MsgConstant.IM_PERSONAL_CHAT_CMD, getImIdLong(), receiverId, MsgConstant.DEVICE_TYPE)
                , msgContent.getBytes());
    }

    public byte[] createMessage(long receiverId, SocketMessage sm, QxMessage message) {
        // 命令包头
        if (message == null) {
            // Toast.makeText(mContext, "发送失败!", Toast.LENGTH_LONG).show();
            return null;
        }

        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        try {
            cmdHeadPojo.setOneselfIMID(Long.parseLong(sm.getOneselfIMID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        cmdHeadPojo.setUsCmdID((short) sm.getUsCmdID());
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) MsgConstant.DEVICE_TYPE);
        cmdHeadPojo.setUcFlag((byte) 0);
        cmdHeadPojo.setServerTimes(getServerTime());
        MsgPojo msgPojo = new MsgPojo();
        try {
            msgPojo.setSenderID(Long.parseLong(sm.getSenderID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            msgPojo.setReceiverID(Long.parseLong(sm.getReceiverID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        msgPojo.setClientTimes(sm.getClientTimes());
        msgPojo.setRand(sm.getRand());
        cmdHeadPojo.setMsgPojo(msgPojo);

        String json = new Gson().toJson(message);
        String msgContent = null;
        try {
            msgContent = new String(json.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mergeRequestInfo(cmdHeadPojo.toByte(), msgContent.getBytes());
    }

    public byte[] createTeamImageMessage(long receiverId, QxMessage message) {
        // 命令包头

        String json = new Gson().toJson(message);
        String msgContent = null;
        try {
            msgContent = new String(json.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return mergeRequestInfo(setCmdHead(getImIdLong(), MsgConstant.IM_TEAM_CHAT_CMD, getImIdLong(), receiverId, MsgConstant.DEVICE_TYPE)
                , msgContent.getBytes());
    }

    public byte[] createTeamFileMessage(long groupId, QxMessage message) {
        // 命令包头

        String json = new Gson().toJson(message);
        String msgContent = null;
        try {
            msgContent = new String(json.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mergeRequestInfo(setCmdHead(getImIdLong(), MsgConstant.IM_TEAM_CHAT_CMD, getImIdLong(), groupId, MsgConstant.DEVICE_TYPE)
                , msgContent.getBytes());
    }

    /**
     * 创建单聊文件消息
     *
     * @param receiverId
     * @param message
     * @return
     */
    public byte[] createSingleFileMessage(long receiverId, QxMessage message) {
        // 命令包头

        String json = new Gson().toJson(message);
        String msgContent = null;
        try {
            msgContent = new String(json.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return mergeRequestInfo(setCmdHead(getImIdLong(), MsgConstant.IM_PERSONAL_CHAT_CMD, getImIdLong(), receiverId, MsgConstant.DEVICE_TYPE)
                , msgContent.getBytes());
    }


    public boolean sendSingleLocationMessage(long receiverId) {
        // 命令包头
        QxMessage message = new QxMessage();
        message.setSenderAvatar(getavatar());
        message.setSenderName(getName());
        message.setType(MsgConstant.LOCATION);
        message.setLocation("深圳市南山区思创大厦");
        String json = new Gson().toJson(message);
        sendMessage(json, receiverId, MsgConstant.IM_PERSONAL_CHAT_CMD);

        return true;
    }

    /**
     * 发送单聊位置
     *
     * @param receiverId
     * @param address
     * @param longitude
     * @param latitude
     * @return
     */
    @Deprecated
    public boolean sendSingleLocationMessage(long receiverId, String address, String longitude, String latitude) {
        // 命令包头
        QxMessage message = new QxMessage();
        message.setType(MsgConstant.LOCATION);
        message.setSenderAvatar(getavatar());
        message.setSenderName(getName());
        message.setLocation(address);
        message.setLongitude(longitude);
        message.setLatitude(latitude);
        String json = new Gson().toJson(message);
        sendMessage(json, receiverId, MsgConstant.IM_PERSONAL_CHAT_CMD);

        return true;
    }

    /**
     * 发送单聊位置
     *
     * @param receiverId
     * @param message
     * @return
     */
    public boolean sendSingleLocationMessage(long receiverId, QxMessage message) {

        try {
            message.setType(MsgConstant.LOCATION);
            message.setSenderAvatar(getavatar());
            message.setSenderName(getName());
            String json = new Gson().toJson(message);
            String msgContent = new String(json.getBytes(), "UTF-8");
            sendMessage(msgContent, receiverId, MsgConstant.IM_PERSONAL_CHAT_CMD);
        } catch (UnsupportedEncodingException e) {

        }

        return true;
    }

    /**
     * 发送群聊位置
     *
     * @param receiverId
     * @param message
     * @return
     */
    public boolean sendTeamLocationMessage(long receiverId, QxMessage message) {

        message.setType(MsgConstant.LOCATION);
        message.setSenderAvatar(getavatar());
        message.setSenderName(getName());

        String json = new Gson().toJson(message);
        sendMessage(json, receiverId, MsgConstant.IM_TEAM_CHAT_CMD);

        return true;
    }

    public byte[] createSingleVoiceMessage(long receiverId, QxMessage message) {
        // 命令包头

        String json = new Gson().toJson(message);

        return mergeRequestInfo(setCmdHead(getImIdLong(), MsgConstant.IM_PERSONAL_CHAT_CMD, getImIdLong(), receiverId, MsgConstant.DEVICE_TYPE)
                , json.getBytes());

    }

    public byte[] createTeamVoiceMessage(long receiverId, QxMessage message) {
        // 命令包头

        String json = new Gson().toJson(message);

        return mergeRequestInfo(setCmdHead(getImIdLong(), MsgConstant.IM_TEAM_CHAT_CMD, getImIdLong(), receiverId, MsgConstant.DEVICE_TYPE)
                , json.getBytes());

    }


    public boolean sendMessage(boolean flag, byte[] bytes, QxMessage message) {
        // 命令包头
        String json = new Gson().toJson(message);
        String msgContent = null;
        try {
            msgContent = new String(json.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        send(flag, mergeRequestInfo(Arrays.copyOfRange(bytes, 0, 49), msgContent.getBytes()), MsgConstant.IM_ACTION_SEND);
        return true;

    }

    public boolean sendRecallMessage(byte[] bytes) {
        // 命令包头
        send(false, Arrays.copyOfRange(bytes, 0, 49), MsgConstant.IM_ACTION_RECALL);
        return true;

    }


    /**
     * 发送消息(单聊,群聊,推送)通用
     *
     * @param content    消息内容(UTF-8编码)
     * @param receiverId 接收者id
     * @param cmd        命令(单聊,群聊,自定义单聊,自定义群聊)
     */
    private void sendMessage(String content, long receiverId, int cmd) {

        byte[] cmdHead = setCmdHead(getImIdLong(), cmd, getImIdLong(), receiverId, MsgConstant.DEVICE_TYPE);
        try {
            String msgContent = new String(content.getBytes(), "UTF-8");
            // 发送内容
            PushPojo pushPojo = new PushPojo();
            pushPojo.setMessage(msgContent);
            // 请求数据
            byte[] reqData = mergeRequestInfo(cmdHead, pushPojo.getMessage().getBytes());
            boolean flag = MsgConstant.IM_ACK_CHAT_TEAM_CMD == cmd;
            send(!flag, reqData, MsgConstant.IM_ACTION_SEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置数据包的包头
     *
     * @param funcDataLen
     * @return
     * @Description:外部包头转字节数组
     */
    public byte[] setOuterHead(int funcDataLen) {
        OuterHeadPojo outerHeadPojo = new OuterHeadPojo();
        outerHeadPojo.setiDataLen(49 + funcDataLen);
        outerHeadPojo.setiPacketFlag(1097745780);
        return outerHeadPojo.toByte();
    }

    /**
     * @param oneselfIMID
     * @param usCmdId
     * @param senderID
     * @param receiverID
     * @param ucDeviceType
     * @return
     * @Description:命令包头转字节数组
     */


    public byte[] setCmdHead(long oneselfIMID, int usCmdId, long senderID,
                             long receiverID, int ucDeviceType) {
        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        cmdHeadPojo.setOneselfIMID(oneselfIMID);
        cmdHeadPojo.setUsCmdID((short) usCmdId);
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) ucDeviceType);
        cmdHeadPojo.setUcFlag((byte) 0);
        cmdHeadPojo.setServerTimes(0);
        // 信息
        MsgPojo msgPojo = new MsgPojo();
        msgPojo.setSenderID(senderID);
        msgPojo.setReceiverID(receiverID);
        msgPojo.setClientTimes(System.currentTimeMillis());
        msgPojo.setRand(flag);
        flag++;
        cmdHeadPojo.setMsgPojo(msgPojo);
        return cmdHeadPojo.toByte();
    }

    /**
     * 包头
     *
     * @param oneselfIMID
     * @param usCmdId
     * @param senderID
     * @param receiverID
     * @param ucDeviceType
     * @param uCflag
     * @return
     */
    public byte[] setCmdHead(long oneselfIMID, int usCmdId, long senderID,
                             long receiverID, int ucDeviceType, int uCflag) {
        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        cmdHeadPojo.setOneselfIMID(oneselfIMID);
        cmdHeadPojo.setUsCmdID((short) usCmdId);
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) ucDeviceType);
        cmdHeadPojo.setUcFlag((byte) uCflag);
        cmdHeadPojo.setServerTimes(0);
        // 信息
        MsgPojo msgPojo = new MsgPojo();
        msgPojo.setSenderID(senderID);
        msgPojo.setReceiverID(receiverID);
        msgPojo.setClientTimes(System.currentTimeMillis());
        msgPojo.setRand(flag);
        flag++;
        cmdHeadPojo.setMsgPojo(msgPojo);
        return cmdHeadPojo.toByte();
    }

    public byte[] setCmdHead(long oneselfIMID, int usCmdId, long senderID,
                             long receiverID, long serverTime, int ucDeviceType) {

        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        cmdHeadPojo.setOneselfIMID(oneselfIMID);
        cmdHeadPojo.setUsCmdID((short) usCmdId);
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) ucDeviceType);
        cmdHeadPojo.setUcFlag((byte) 0);
        cmdHeadPojo.setServerTimes(serverTime);

        // 信息
        MsgPojo msgPojo = new MsgPojo();
        msgPojo.setSenderID(senderID);
        msgPojo.setReceiverID(receiverID);
        msgPojo.setClientTimes(System.currentTimeMillis());
        msgPojo.setRand(flag);
        msgPojo.setServerTimes(serverTime);

        flag++;
        cmdHeadPojo.setMsgPojo(msgPojo);
        return cmdHeadPojo.toByte();
    }

    /**
     * @param outHeadByte
     * @param cmdHeadByte
     * @param funcByte
     * @return
     * @Description:外部包头，命令包头和发送内容转字节数组
     */
    public byte[] mergeRequestInfo(byte[] outHeadByte, byte[] cmdHeadByte,
                                   byte[] funcByte) {
        byte[] mergeAfterData =
                new byte[outHeadByte.length + cmdHeadByte.length + funcByte.length];
        System.arraycopy(outHeadByte, 0, mergeAfterData, 0, outHeadByte.length);
        System.arraycopy(cmdHeadByte, 0, mergeAfterData, outHeadByte.length, cmdHeadByte.length);
        System.arraycopy(funcByte, 0, mergeAfterData, outHeadByte.length + cmdHeadByte.length, funcByte.length);
        return mergeAfterData;
    }

    /**
     * @param cmdHeadByte
     * @param funcByte
     * @return
     * @Description:命令包头和发送内容转字节数组
     */
    public byte[] mergeRequestInfo(byte[] cmdHeadByte, byte[] funcByte) {
        byte[] mergeAfterData =
                new byte[cmdHeadByte.length + funcByte.length];
        System.arraycopy(cmdHeadByte, 0, mergeAfterData, 0, cmdHeadByte.length);
        System.arraycopy(funcByte, 0, mergeAfterData, cmdHeadByte.length, funcByte.length);
        return mergeAfterData;
    }

    public byte[] mergeRequestInfo(byte[]... src) {
        int len = 0;
        for (int i = 0; i < src.length; i++) {
            len = len + src[i].length;
        }
        byte[] dest = new byte[len];
        int len2 = 0;
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dest, len2, src[i].length);
            len2 = len2 + src[i].length;
        }
        return dest;
    }

    public String getImId() {
        userId = SPUtils.getString(mContext, SPUtils.SP_NAME, AppConst.USER_ID, "0");
        return userId;
    }


    public long getServerTimeOffsetValue() {
        return serverTimeOffsetValue;
    }

    public void setServerTimeOffsetValue(long serverTimeOffsetValue) {
        this.serverTimeOffsetValue = serverTimeOffsetValue;
    }


    public String getCompanyId() {
        this.companyId = SPUtils.getString(mContext, SPUtils.SP_NAME, AppConst.COMPANY_ID, "0");
        return companyId;
    }

    public String getName() {
        name = SPUtils.getString(mContext, SPUtils.SP_NAME, AppConst.USER_NAME, " ");
        return name;
    }

    public String getavatar() {
        avatar = SPUtils.getString(mContext, SPUtils.SP_NAME, AppConst.USER_AVATAR, " ");
        return avatar;
    }

    /**
     * 获取服务器时间
     *
     * @return
     */
    public long getServerTime() {

        return System.currentTimeMillis() - serverTimeOffsetValue;
    }


    /**
     * 将消息转为字节数组(可以用于回应已读未读ack)
     *
     * @param message
     * @return
     */
    public byte[] parseSocketMessageToByte(SocketMessage message, int cmd) {


        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        try {
            cmdHeadPojo.setOneselfIMID(Long.parseLong(message.getOneselfIMID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        cmdHeadPojo.setUsCmdID((short) cmd);
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) MsgConstant.DEVICE_TYPE);
        cmdHeadPojo.setUcFlag((byte) 0);
        cmdHeadPojo.setServerTimes(message.getServerTimes());
        MsgPojo msgPojo = new MsgPojo();
        try {
            msgPojo.setSenderID(Long.parseLong(message.getSenderID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            msgPojo.setReceiverID(Long.parseLong(message.getReceiverID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        msgPojo.setClientTimes(message.getClientTimes());
        msgPojo.setRand(message.getRand());
        cmdHeadPojo.setMsgPojo(msgPojo);
        return cmdHeadPojo.toByte();
    }

    /**
     * 转发
     *
     * @param conversationId 会话id
     * @param receiverId     接收人(群)id
     * @param cmd            命令(单聊,群发)
     * @param message        要转发的消息
     */
    public void sendToSb(long conversationId, String receiverId, int cmd, SocketMessage message) {
        if (message == null) {
            // Toast.makeText(mContext, "发送失败!", Toast.LENGTH_LONG).show();
            return;
        }
        boolean saveFlag = true;
        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        cmdHeadPojo.setOneselfIMID(getImIdLong());
        cmdHeadPojo.setUsCmdID((short) cmd);
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) MsgConstant.DEVICE_TYPE);
        cmdHeadPojo.setUcFlag((byte) 0);
        cmdHeadPojo.setServerTimes(getServerTime());
        MsgPojo msgPojo = new MsgPojo();
        msgPojo.setSenderID(getImIdLong());
        try {
            msgPojo.setReceiverID(Long.parseLong(receiverId));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        msgPojo.setClientTimes(System.currentTimeMillis());
        msgPojo.setRand(flag);
        flag++;
        cmdHeadPojo.setMsgPojo(msgPojo);
        QxMessage qxMsg = new QxMessage();
        qxMsg.setChatId(conversationId);
        qxMsg.setType(message.getMsgType());
        qxMsg.setSenderName(getName());
        qxMsg.setSenderAvatar(getavatar());
        qxMsg.setFileName(message.getFileName());
        if (MsgConstant.IM_TEAM_CHAT_CMD == cmd) {
            saveFlag = false;
        }
        qxMsg.setFileUrl(message.getFileUrl());
        qxMsg.setFileSize(message.getFileSize());
        qxMsg.setCompanyId(companyId);
        qxMsg.setDuration(message.getDuration());
        qxMsg.setFileType(message.getFileType());
        qxMsg.setMsg(message.getMsgContent());
        qxMsg.setLocation(message.getLocation());
        qxMsg.setLatitude(message.getLatitude());
        qxMsg.setLongitude(message.getLongitude());
        sendMessage(saveFlag, cmdHeadPojo.toByte(), qxMsg);
        if (!saveFlag) {
            qxMsg.setAllPeoples(message.getAllPeoples());
            String json = new Gson().toJson(qxMsg);
            String msgContent = null;
            try {
                msgContent = new String(json.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] bytes = mergeRequestInfo(Arrays.copyOfRange(cmdHeadPojo.toByte(), 0, 49), msgContent.getBytes());
            ParseUtil.saveMessage(bytes);
        }
    }

    public void sendFileToSb(long conversationId, String receiverId, int cmd, QxMessage message) {
        if (message == null) {
            //  Toast.makeText(mContext, "发送失败!", Toast.LENGTH_LONG).show();
            return;
        }
        boolean saveFlag = true;
        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        cmdHeadPojo.setOneselfIMID(getImIdLong());
        cmdHeadPojo.setUsCmdID((short) cmd);
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) MsgConstant.DEVICE_TYPE);
        cmdHeadPojo.setUcFlag((byte) 0);
        cmdHeadPojo.setServerTimes(getServerTime());
        MsgPojo msgPojo = new MsgPojo();
        msgPojo.setSenderID(getImIdLong());
        try {
            msgPojo.setReceiverID(Long.parseLong(receiverId));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        msgPojo.setClientTimes(System.currentTimeMillis());
        msgPojo.setRand(flag);
        flag++;
        cmdHeadPojo.setMsgPojo(msgPojo);
        QxMessage qxMsg = new QxMessage();
        qxMsg.setChatId(conversationId);
        qxMsg.setType(MsgConstant.FILE);
        qxMsg.setFileId(message.getFileId());
        qxMsg.setSenderName(getName());
        qxMsg.setSenderAvatar(getavatar());
        qxMsg.setFileName(message.getFileName());
        qxMsg.setFileUrl(message.getFileUrl());
        qxMsg.setCompanyId(companyId);
        qxMsg.setDuration(message.getDuration());
        qxMsg.setFileType(message.getFileType());
        qxMsg.setFileSize(message.getFileSize());
        qxMsg.setMsg(message.getMsg());
        qxMsg.setLocation(message.getLocation());
        qxMsg.setLatitude(message.getLatitude());
        qxMsg.setLongitude(message.getLongitude());
        if (MsgConstant.IM_TEAM_CHAT_CMD == cmd) {
            saveFlag = false;
        }
        sendMessage(saveFlag, cmdHeadPojo.toByte(), qxMsg);
        if (!saveFlag) {
            qxMsg.setAllPeoples(message.getAllPeoples());
            String json = new Gson().toJson(qxMsg);
            String msgContent = null;
            try {
                msgContent = new String(json.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] bytes = mergeRequestInfo(Arrays.copyOfRange(cmdHeadPojo.toByte(), 0, 49), msgContent.getBytes());
            ParseUtil.saveMessage(bytes);
        }
    }

    public void resendFailedMessage(SocketMessage message) {
        if (message == null) {
            // Toast.makeText(mContext, "发送失败!", Toast.LENGTH_LONG).show();
            return;
        }
        boolean saveFlag = true;
        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        try {
            cmdHeadPojo.setOneselfIMID(Long.parseLong(message.getOneselfIMID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        cmdHeadPojo.setUsCmdID((short) message.getUsCmdID());
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) MsgConstant.DEVICE_TYPE);
        cmdHeadPojo.setUcFlag((byte) 0);
        cmdHeadPojo.setServerTimes(getServerTime());
        MsgPojo msgPojo = new MsgPojo();
        try {
            msgPojo.setSenderID(Long.parseLong(message.getSenderID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            msgPojo.setReceiverID(Long.parseLong(message.getReceiverID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        msgPojo.setClientTimes(message.getClientTimes());
        msgPojo.setRand(message.getRand());
        cmdHeadPojo.setMsgPojo(msgPojo);
        QxMessage qxMsg = new QxMessage();
        qxMsg.setChatId(message.getConversationId());
        qxMsg.setType(message.getMsgType());
        qxMsg.setSenderName(getName());
        qxMsg.setSenderAvatar(getavatar());
        qxMsg.setFileSize(message.getFileSize());
        qxMsg.setFileId(message.getFileId());
        qxMsg.setFileName(message.getFileName());
        qxMsg.setFileUrl(message.getFileUrl());
        qxMsg.setCompanyId(companyId);
        qxMsg.setDuration(message.getDuration());
        qxMsg.setFileType(message.getFileType());
        qxMsg.setMsg(message.getMsgContent());
        qxMsg.setLocation(message.getLocation());
        qxMsg.setLatitude(message.getLatitude());
        qxMsg.setLongitude(message.getLongitude());
        if (MsgConstant.IM_TEAM_CHAT_CMD == message.getUsCmdID()) {
            saveFlag = false;
        }
        sendMessage(saveFlag, cmdHeadPojo.toByte(), qxMsg);
        if (!saveFlag) {
            qxMsg.setAllPeoples(message.getAllPeoples());
            String json = new Gson().toJson(qxMsg);
            String msgContent = null;
            try {
                msgContent = new String(json.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] bytes = mergeRequestInfo(Arrays.copyOfRange(cmdHeadPojo.toByte(), 0, 49), msgContent.getBytes());
            ParseUtil.saveMessage(bytes);
        }
    }

    /**
     * 顺便说些什么
     *
     * @param c
     * @param msg
     */
    public void sendTextMessage(Conversation c, String msg) {
        int cmd = -1;
        boolean saveFlag = true;
        if (c.getConversationType() == MsgConstant.SINGLE) {
            cmd = MsgConstant.IM_PERSONAL_CHAT_CMD;
        }
        if (c.getConversationType() == MsgConstant.GROUP) {
            cmd = MsgConstant.IM_TEAM_CHAT_CMD;
            saveFlag = false;
        }
        if (cmd == -1) {
            return;
        }

        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        cmdHeadPojo.setOneselfIMID(getImIdLong());
        cmdHeadPojo.setUsCmdID((short) cmd);
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) MsgConstant.DEVICE_TYPE);
        cmdHeadPojo.setUcFlag((byte) 0);
        cmdHeadPojo.setServerTimes(getServerTime());
        MsgPojo msgPojo = new MsgPojo();
        msgPojo.setSenderID(getImIdLong());
        try {
            msgPojo.setReceiverID(Long.parseLong(c.getReceiverId()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        msgPojo.setClientTimes(System.currentTimeMillis());
        msgPojo.setRand(flag);
        flag++;
        cmdHeadPojo.setMsgPojo(msgPojo);
        QxMessage qxMsg = new QxMessage();
        qxMsg.setChatId(c.getConversationId());
        qxMsg.setType(MsgConstant.TEXT);
        qxMsg.setSenderName(getName());
        qxMsg.setSenderAvatar(getavatar());
        qxMsg.setCompanyId(companyId);
        qxMsg.setMsg(msg);
        sendMessage(saveFlag, cmdHeadPojo.toByte(), qxMsg);
        if (!saveFlag) {
            qxMsg.setAllPeoples(c.getPeoples());
            String json = new Gson().toJson(qxMsg);
            String msgContent = null;
            try {
                msgContent = new String(json.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] bytes = mergeRequestInfo(Arrays.copyOfRange(cmdHeadPojo.toByte(), 0, 49), msgContent.getBytes());
            ParseUtil.saveMessage(bytes);
        }

    }

    /**
     * 发送已读
     *
     * @param message
     * @param cmd
     */
    public void sendReadAck(SocketMessage message, int cmd) {
        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        cmdHeadPojo.setOneselfIMID(getImIdLong());
        cmdHeadPojo.setUsCmdID((short) cmd);
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) MsgConstant.DEVICE_TYPE);
        cmdHeadPojo.setUcFlag((byte) 0);
        cmdHeadPojo.setServerTimes(message.getServerTimes());
        MsgPojo msgPojo = new MsgPojo();
        try {
            msgPojo.setSenderID(Long.parseLong(message.getSenderID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            msgPojo.setReceiverID(Long.parseLong(message.getReceiverID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        msgPojo.setClientTimes(message.getClientTimes());
        msgPojo.setRand(message.getRand());
        cmdHeadPojo.setMsgPojo(msgPojo);
        send(false, cmdHeadPojo.toByte(), MsgConstant.IM_ACTION_ACK);

    }

    /**
     * 收到推送回应ack
     *
     * @param message
     */
    public void sendPushAck(SocketMessage message) {
        int cmd = 0;
        if (message.getUsCmdID() == MsgConstant.IM_USER_DEFINED_PERSONAL_CMD) {
            cmd = MsgConstant.IM_ACK_USER_DEFINED_PERSONAL_CMD;
        } else if (message.getUsCmdID() == MsgConstant.IM_USER_DEFINED_TEAM_CMD) {
            cmd = MsgConstant.IM_ACK_USER_DEFINED_TEAM_CMD;
        }
        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        cmdHeadPojo.setOneselfIMID(getImIdLong());
        cmdHeadPojo.setUsCmdID((short) cmd);
        cmdHeadPojo.setUcVer((byte) message.getUcVer());
        cmdHeadPojo.setUcDeviceType((byte) MsgConstant.DEVICE_TYPE);
        cmdHeadPojo.setUcFlag((byte) message.getUcFlag());
        cmdHeadPojo.setServerTimes(message.getServerTimes());
        MsgPojo msgPojo = new MsgPojo();
        msgPojo.setSenderID(Long.parseLong(message.getSenderID()));
        try {
            msgPojo.setReceiverID(Long.parseLong(message.getReceiverID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        msgPojo.setClientTimes(message.getClientTimes());
        msgPojo.setRand(message.getRand());
        cmdHeadPojo.setMsgPojo(msgPojo);
        send(false, cmdHeadPojo.toByte(), MsgConstant.IM_ACTION_ACK);

    }

    public void logout() {
        //send(null, MsgConstant.IM_ACTION_OPEN_NEW_SOCKET);
        try {
            // 命令包头
            byte[] cmdHead = setCmdHead(getImIdLong(), MsgConstant.IM_LOG_OUT_CMD, 1, 1, MsgConstant.DEVICE_TYPE);
            // 登录
            LoginRequestPojo loginRequestPojo = new LoginRequestPojo();
            loginRequestPojo.setSzUsername(getImId() + "");
            loginRequestPojo.setChStatus((byte) 1);
            loginRequestPojo.setChUserType((byte) 1);
            byte[] loginReq = loginRequestPojo.toByte();
            // 请求数据
            byte[] reqData = mergeRequestInfo(cmdHead, loginReq);
            send(false, reqData, MsgConstant.IM_ACTION_LOGOUT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 拉取历史消息
     */
    public void pullHistoryMessage() {

        try {
            // 命令包头
            byte[] cmdHead = setCmdHead(getImIdLong(), MsgConstant.IM_PULL_HISTORY_MESSAGE, 1, 1, MsgConstant.DEVICE_TYPE);
            // 登录
            LoginRequestPojo loginRequestPojo = new LoginRequestPojo();
            loginRequestPojo.setSzUsername(getImId());
            loginRequestPojo.setChStatus((byte) 1);
            loginRequestPojo.setChUserType((byte) 1);
            byte[] loginReq = loginRequestPojo.toByte();
            // 请求数据
            byte[] reqData = mergeRequestInfo(cmdHead, loginReq);
            send(false, reqData, MsgConstant.IM_ACTION_HISTORY);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 撤回消息
     */
    public void recallMessage(SocketMessage message) {

        if (message == null) {
            // Toast.makeText(mContext, "撤回失败!", Toast.LENGTH_LONG).show();
            return;
        }
        int cmd = 0;
        if (message.getUsCmdID() == MsgConstant.IM_PERSONAL_CHAT_CMD) {
            cmd = MsgConstant.IM_RECALL_PERSONAL_MSG;
        } else if (message.getUsCmdID() == MsgConstant.IM_TEAM_CHAT_CMD) {
            cmd = MsgConstant.IM_RECALL_TEAM_MSG;
        } else if (cmd == 0) {
            // Toast.makeText(mContext, "撤回失败!", Toast.LENGTH_LONG).show();
            return;
        }
        CmdHeadPojo cmdHeadPojo = new CmdHeadPojo();
        try {
            cmdHeadPojo.setOneselfIMID(Long.parseLong(message.getOneselfIMID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        cmdHeadPojo.setUsCmdID((short) cmd);
        cmdHeadPojo.setUcVer((byte) 1);
        cmdHeadPojo.setUcDeviceType((byte) message.getUcDeviceType());
        cmdHeadPojo.setUcFlag((byte) message.getUcFlag());
        cmdHeadPojo.setServerTimes(message.getServerTimes());
        MsgPojo msgPojo = new MsgPojo();
        try {
            msgPojo.setSenderID(Long.parseLong(message.getSenderID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            msgPojo.setReceiverID(Long.parseLong(message.getReceiverID()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        msgPojo.setClientTimes(message.getClientTimes());
        msgPojo.setRand(message.getRand());
        flag++;
        cmdHeadPojo.setMsgPojo(msgPojo);
        sendRecallMessage(cmdHeadPojo.toByte());
    }

    /**
     * 撤回消息
     *
     * @param oldMessage
     */
    public void sendRecallNotify(SocketMessage oldMessage) {
        try {
            // 命令包头
            String receiverId = oldMessage.getReceiverID();
            long receiverIdLong = 0L;

            if (receiverId == userId) {
                receiverId = oldMessage.getSenderID();
            }
            byte[] cmdHead = setCmdHead(getImIdLong(), oldMessage.getUsCmdID(), getImIdLong(), receiverIdLong, oldMessage.getServerTimes(), MsgConstant.DEVICE_TYPE);
            // 消息转码
            QxMessage message = new QxMessage();
            message.setSenderAvatar(getavatar());
            message.setSenderName("通知");
            message.setType(MsgConstant.NOTIFICATION);
            message.setChatId(oldMessage.getConversationId());
            if (getImId().equals(oldMessage.getSenderID())) {
                message.setMsg("您撤回了一条消息");
            } else {
                if (oldMessage.getUsCmdID() == MsgConstant.IM_PERSONAL_CHAT_CMD) {
                    message.setMsg("对方撤回了一条消息");
                } else if (oldMessage.getUsCmdID() == MsgConstant.IM_TEAM_CHAT_CMD) {
                    message.setMsg(oldMessage.getSenderName() + "撤回了一条消息");
                }

            }

            String json = new Gson().toJson(message);
            String msgContent = new String(json.getBytes(), "UTF-8");
            // 拼装数据
            byte[] msgData = mergeRequestInfo(cmdHead, msgContent.getBytes());
//撤销成功,发送通知
            EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.RECALL_MESSAGE_SUCCESS, message.getChatId()));
            ParseUtil.saveMessage(msgData);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    public int getGroupNumberNum() {
        return groupMemberNum;
    }

    public void setGroupNumberNum(int num) {
        this.groupMemberNum = num;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void sendFileMessageFailed(byte[] bytes, QxMessage message) {

        long clientTime = ParseUtil.bytes2Long(Arrays.copyOfRange(bytes, 37, 45));
        int rand = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(bytes, 45, 49));
        String msgId = clientTime + "" + rand;
        DBManager.getInstance().sendFileFailed(msgId);


    }


    /**
     * 重发文件上传失败的消息
     *
     * @param message
     */
    public void resendUploadFailedMessage(SocketMessage message) {


    }

    public Context getContext() {
        return mContext;
    }


    /**
     * 此方法为android程序写入sd文件文件，用到了android-annotation的支持库@
     *
     * @ buffer   写入文件的内容
     * @ folder   保存文件的文件夹名称,如log；可为null，默认保存在sd卡根目录
     * @ fileName 文件名称，默认app_log.txt
     * @ append   是否追加写入，true为追加写入，false为重写文件
     * @ autoLine 针对追加模式，true为增加时换行，false为增加时不换行
     */
    public synchronized void writeFileToSDCard(@NonNull String s) {
        if (!Constants.IS_DEBUG) {
            return;
        }
        @NonNull final byte[] buffer = s.getBytes();
        final String folder = JYFileHelper.getFileDir(mContext, Constants.PATH_DOWNLOAD).getAbsolutePath();
        final String fileName = folder + DateTimeUtil.longToStr(System.currentTimeMillis(), "yyyy-MM-dd a") + ".txt";
        final boolean append = true;
        final boolean autoLine = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean sdCardExist = Environment.getExternalStorageState().equals(
                        android.os.Environment.MEDIA_MOUNTED);
                String folderPath = new File(folder).getAbsolutePath();

                File file;
                //判断文件名是否为空
                if (TextUtils.isEmpty(fileName)) {
                    file = new File(folderPath + DateTimeUtil.longToStr(System.currentTimeMillis(), "yyyy-MM-dd a") + ".txt");
                } else {
                    file = new File(fileName);
                }
                RandomAccessFile raf = null;
                FileOutputStream out = null;
                try {
                    if (append) {
                        //如果为追加则在原来的基础上继续写文件
                        raf = new RandomAccessFile(file, "rw");
                        raf.seek(file.length());
                        raf.write(buffer);
                        if (autoLine) {
                            raf.write("\n".getBytes());
                            raf.write("\n".getBytes());
                            raf.write("*************************华丽的分割线*************************".getBytes());
                            raf.write("\n".getBytes());
                            raf.write("\n".getBytes());
                        }
                    } else {
                        //重写文件，覆盖掉原来的数据
                        out = new FileOutputStream(file);
                        out.write(buffer);
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (raf != null) {
                            raf.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 初始化Context
     *
     * @param context
     */
    public void initContext(Context context) {
        mContext = context;
        try {
            startIMService(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            startDownloadService(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
