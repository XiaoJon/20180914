package com.hjhq.teamface.oa.main.logic;

import com.hjhq.teamface.api.TeamMessageApiService;
import com.hjhq.teamface.base.QueryModuleAuthResultBean;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.BaseLogic;
import com.hjhq.teamface.basis.bean.AddGroupChatReqBean;
import com.hjhq.teamface.basis.bean.AddGroupChatResponseBean;
import com.hjhq.teamface.basis.bean.AddSingleChatResponseBean;
import com.hjhq.teamface.basis.bean.AppModuleResultBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.EmployeeCardDetailBean;
import com.hjhq.teamface.basis.bean.EmployeeDetailBean;
import com.hjhq.teamface.basis.bean.EmployeeResultBean;
import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.RoleGroupResponseBean;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.basis.bean.ViewDataAuthResBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.IMState;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.SingleInstance;
import com.hjhq.teamface.bean.CommonModuleResultBean;
import com.hjhq.teamface.bean.QueryBannerBean;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.activity.TeamMessageFragment;
import com.hjhq.teamface.im.adapter.ConversationListController;
import com.hjhq.teamface.im.bean.AppAssistantInfoBean;
import com.hjhq.teamface.im.bean.AppAssistantListBean;
import com.hjhq.teamface.im.bean.ConversationListBean;
import com.hjhq.teamface.im.bean.GetGroupListBean;
import com.hjhq.teamface.im.bean.GroupChatInfoBean;
import com.hjhq.teamface.im.bean.ModuleBean;
import com.hjhq.teamface.im.bean.SingleChatInfoBean;
import com.hjhq.teamface.oa.login.bean.CompanyListBean;
import com.hjhq.teamface.oa.login.bean.CompanyStructureBean;
import com.hjhq.teamface.oa.login.bean.QueryEmployeeBean;
import com.hjhq.teamface.oa.login.bean.SwitchCompanyResultBean;
import com.hjhq.teamface.oa.login.logic.SettingHelper;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;


/**
 * Created by lx on 2017/3/21.
 */

public class MainLogic extends BaseLogic {
    public static MainLogic getInstance() {
        return (MainLogic) SingleInstance.getInstance(MainLogic.class
                .getName());
    }

    private TeamMessageApiService getApi() {
        return new ApiManager<TeamMessageApiService>().getAPI(TeamMessageApiService.class);
    }


    /**
     * 获取人员
     *
     * @param mActivity
     * @param userName
     * @param s
     */
    public void getEmployee(RxAppCompatActivity mActivity, String userName, Subscriber<EmployeeResultBean> s) {
        Observable observable = getApi().getEmployee(userName).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        toSubscribe(observable, s);
    }


    /**
     * 获取最近联系人
     *
     * @return
     */
    public List<Member> getRecentContacts() {
        List<Member> list = new ArrayList<>();
        List<Member> recentContacts = SPHelper.getRecentContacts();
        if (recentContacts != null && recentContacts.size() > 0) {
            list.addAll(recentContacts);
        }
        return list;

    }

    /**
     * 保存最近联系人
     *
     * @param memberList 多人
     */
    public void saveRecentContacts(List<Member> memberList) {
        if (memberList != null) {
            List<Member> recentContacts = getRecentContacts();
            List<Member> allContacts = new ArrayList<>();
            allContacts.addAll(recentContacts);
            allContacts.addAll(memberList);
            recentContacts.addAll(memberList);

            Comparator<Member> comparator = new Comparator<Member>() {
                @Override
                public int compare(Member o1, Member o2) {
                    return (int) (o2.getUpdateTime() - o1.getUpdateTime());
                }
            };
            Collections.sort(recentContacts, comparator);

            Iterator<Member> iterator = allContacts.iterator();
            while (iterator.hasNext()) {
                Member m = iterator.next();
                boolean eq = false;
                for (int i = 0; i < memberList.size(); i++) {
                    if (memberList.get(i).getId() == m.getId() && memberList.get(i).getUpdateTime() > m.getUpdateTime()) {
                        iterator.remove();
                    } else if (memberList.get(i).getId() == m.getId() && memberList.get(i).getUpdateTime() == m.getUpdateTime()) {
                        eq = true;
                    }
                }
                for (int i = 0; i < recentContacts.size(); i++) {
                    if (recentContacts.get(i).getId() == m.getId() && recentContacts.get(i).getUpdateTime() > m.getUpdateTime()) {
                        iterator.remove();
                    } else if (recentContacts.get(i).getId() == m.getId() && recentContacts.get(i).getUpdateTime() > m.getUpdateTime()) {
                        if (eq) {
                            iterator.remove();
                        }
                    }
                }

            }

            List<Member> list = new ArrayList<>();
            if (allContacts.size() > 10) {
                list.addAll(recentContacts.subList(0, 10));
                SPHelper.saveRecentContacts(list, true);
            } else {
                SPHelper.saveRecentContacts(allContacts, true);
            }
            SPHelper.saveRecentContacts(recentContacts, true);
        }
    }

    /**
     * 保存最近联系人(最多10人)
     *
     * @param member 单人
     */
    public void saveRecentContact(Member member) {
        if (member != null) {
            List<Member> recentContacts = getRecentContacts();
            Iterator<Member> iterator = recentContacts.iterator();
            while (iterator.hasNext()) {
                Member m = iterator.next();
                if (member.getId() == m.getId()) {
                    if (member.getUpdateTime() > m.getUpdateTime()) {
                        iterator.remove();
                        break;
                    } else {
                        return;
                    }

                }
            }
            recentContacts.add(0, member);
            List<Member> list = new ArrayList<>();
            if (recentContacts.size() > 10) {
                list.addAll(recentContacts.subList(0, 10));
                SPHelper.saveRecentContacts(list, true);
            } else {
                SPHelper.saveRecentContacts(recentContacts, true);
            }
            EventBusUtils.sendEvent(new MessageBean(0, C.UPDATE_RECENT_CONTACT, ""));
        }


    }

    /**
     * 清空所有常用联系人
     */
    public void removeAllRecentContact() {

        List<Member> list = new ArrayList<>();
        SPHelper.saveRecentContacts(list, true);
        EventBusUtils.sendEvent(new MessageBean(0, C.UPDATE_RECENT_CONTACT, ""));


    }

    /**
     * 清空单条常用联系人
     *
     * @param member
     */
    public void removeRecentContact(Member member) {

        if (member != null) {
            List<Member> recentContacts = getRecentContacts();
            Iterator<Member> iterator = recentContacts.iterator();
            while (iterator.hasNext()) {
                Member m = iterator.next();
                if (member.getId() == m.getId()) {
                    iterator.remove();
                    break;
                }


            }
            List<Member> list = new ArrayList<>();
            if (recentContacts.size() > 10) {
                list.addAll(recentContacts.subList(0, 10));
                SPHelper.saveRecentContacts(list, true);
            } else {
                SPHelper.saveRecentContacts(recentContacts, true);
            }
            EventBusUtils.sendEvent(new MessageBean(0, C.UPDATE_RECENT_CONTACT, ""));
        }


    }


    /**
     * 获取公司人员架构
     *
     * @param mActivity
     * @param companyId
     * @param s
     */
    public void findUsersByCompany(RxAppCompatActivity mActivity, String companyId,
                                   Subscriber<GetDepartmentStructureBean> s) {
        Observable observable = getApi().findUsersByCompany(companyId)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 获取公司员工
     *
     * @param mActivity
     * @param companyId
     * @param s
     */
    public void getCompanyEmployee(RxAppCompatActivity mActivity, String companyId,
                                   Subscriber<CompanyStructureBean> s) {
        Observable observable = getApi().getCompanyEmployee(companyId)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 查找员工
     *
     * @param mActivity
     * @param departmentId
     * @param employeeName
     * @param s
     */
    public void findEmployee(RxAppCompatActivity mActivity, String departmentId, String employeeName,
                             Subscriber<QueryEmployeeBean> s) {
        Observable observable = getApi().findEmployeeVague(departmentId, employeeName)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }


    /**
     * 获取公司人员架构
     *
     * @param mActivity
     * @param s
     */
    public void getRoleGroupList(RxAppCompatActivity mActivity,
                                 Subscriber<RoleGroupResponseBean> s) {
        Observable observable = getApi().getRoleGroupList()
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }


    /**
     * 获取个人及公司信息
     *
     * @param mActivity
     * @param s
     */
    public void queryEmployeeInfo(BaseActivity mActivity, String id, Subscriber<EmployeeDetailBean> s) {
        Observable observable = getApi().queryEmployeeInfo(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 保存名片信息
     *
     * @param mActivity
     * @param s
     */
    public void savaCardInfo(BaseActivity mActivity, EmployeeCardDetailBean.DataBean bean, Subscriber<BaseBean> s) {
        Observable observable = getApi().savaCardInfo(bean)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 获取名片信息
     *
     * @param mActivity
     * @param s
     */
    public void queryEmployeeCard(BaseActivity mActivity, String id, Subscriber<EmployeeCardDetailBean> s) {
        Observable observable = getApi().queryEmployeeCard(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    public void queryEmployeeInfoBySingId(BaseActivity mActivity, String id, Subscriber<EmployeeDetailBean> s) {
        Observable observable = getApi().queryEmployeeInfoBySingId(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }


    /**
     * 获取公司列表
     *
     * @param mActivity
     * @param s
     */
    public void getCompanyList(BaseActivity mActivity, Subscriber<CompanyListBean> s) {
        getApi().getCompanyList()
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    public void whetherFabulous(BaseActivity mActivity, String id, String status, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        getApi().whetherFabulous(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 切换公司
     *
     * @param mActivity
     * @param companyId 公司id
     */
    public void switchCurrentCompany(BaseActivity mActivity, String companyId) {
        Map<String, String> map = new HashMap<>(1);
        map.put("company_id", companyId);
        getApi().switchCurrentCompany(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(new ProgressSubscriber<SwitchCompanyResultBean>(mActivity) {
                    @Override
                    public void onNext(SwitchCompanyResultBean switchCompanyResultBean) {
                        super.onNext(switchCompanyResultBean);
                        SPUtils.cleanCache();

                        SwitchCompanyResultBean.DataBean data = switchCompanyResultBean.getData();
                        //保存TOKEN
                        SPHelper.setToken(data.getToken());

                        initUserInfo(mActivity);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        SPHelper.setLoginBefore(false);
                    }
                });
    }

    /**
     * 切换公司后后信息
     */
    public void initUserInfo(BaseActivity activity) {
        getApi().queryInfo()
                .map(new HttpResultFunc<>())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(new ProgressSubscriber<UserInfoBean>(activity) {
                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        super.onNext(userInfoBean);
                        //更改登录标记位
                        SPHelper.setLoginBefore(true);
                        try {
                            //保存公司信息
                            UserInfoBean.DataBean.CompanyInfoBean companyInfo = userInfoBean.getData().getCompanyInfo();
                            SPHelper.setCompanyId(companyInfo.getId());
                            SPHelper.setCompanyName(companyInfo.getCompany_name());

                            IM.getInstance().initCompanyId(companyInfo.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showError(activity, "初始化公司信息失败!");
                            SPHelper.setLoginBefore(false);
                            SettingHelper.logout(MsgConstant.TYPE_IM_ACCOUNT_ERROR);
                        }

                        try {
                            //保存部门信息
                            List<UserInfoBean.DataBean.DepartmentInfoBean> departmentInfo = userInfoBean.getData().getDepartmentInfo();
                            if (!CollectionUtils.isEmpty(departmentInfo)) {
                                SPHelper.setDepartmentId(departmentInfo.get(0).getId());
                                SPHelper.setDepartmentName(departmentInfo.get(0).getDepartment_name());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showError(activity, "初始化部门信息失败!");
                            SPHelper.setLoginBefore(false);
                            SettingHelper.logout(MsgConstant.TYPE_IM_ACCOUNT_ERROR);
                        }

                        try {
                            //保存用户信息
                            UserInfoBean.DataBean.EmployeeInfoBean employeeInfo = userInfoBean.getData().getEmployeeInfo();
                            SPHelper.setUserId(employeeInfo.getSign_id());
                            SPHelper.setEmployeeId(employeeInfo.getId());
                            SPHelper.setUserAvatar(employeeInfo.getPicture());
                            SPHelper.setUserName(employeeInfo.getName());


                            IM.getInstance().initID(employeeInfo.getId());
                            IM.getInstance().initName(employeeInfo.getName());
                            IM.getInstance().initAvatar(employeeInfo.getPicture());
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showError(activity, "初始化个人信息失败!");
                            SPHelper.setLoginBefore(false);
                            SettingHelper.logout(MsgConstant.TYPE_IM_ACCOUNT_ERROR);
                        }
                        SPHelper.setUserInfo(userInfoBean);


                        //企信退出登录
                        IM.getInstance().logout();
                        IMState.setImOnlineState(false);
                        //IMState.setImCanLogin(true);

                        //清除内存中会话列表的缓存
                        TeamMessageFragment.clear();
                        //清除通知栏提醒消息
                        ConversationListController.cleanNotify();

                        //切换数据库
                        IM.getInstance().setupDatabase();

                        ToastUtils.showSuccess(activity, "切换成功");
//                        AppManager.getAppManager().finishAllActivity();
//                        CommonUtil.startActivtiy(activity, MainActivity.class);
                        EventBusUtils.sendStickyEvent(new MessageBean(Constants.REQUEST_SWITCH_COMPANY, "切换成功", null));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        SPHelper.setLoginBefore(false);
                        SettingHelper.logout(MsgConstant.TYPE_IM_ACCOUNT_ERROR);
                    }
                });
    }


    /**
     * 上传文件
     *
     * @param mActivity
     * @param url
     * @param s
     */
    public void uploadAvatarFile(BaseActivity mActivity,
                                 String url, Subscriber<UpLoadFileResponseBean> s) {

        File file = null;
        try {
            file = FileUtils.getCompressedImage(FileUtils.getImage(url, 120));
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showError(mActivity, "图片压缩错误");
            return;
        }
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Map<String, RequestBody> fileList = new HashMap<>(2);
        fileList.put("file\"; filename=\"" + file.getName(), requestBody1);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), "user111");
        fileList.put("bean", requestBody2);
        getApi().uploadAvatarFile(fileList)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 上传文件
     *
     * @param mActivity
     * @param url
     * @param s
     */
    public void uploadMp3File(BaseActivity mActivity,
                              String url, Subscriber<UpLoadFileResponseBean> s) {
        File file = new File(url);

        String mineType = FileTypeUtils.getMimeType(file);
        LogUtil.e(mineType);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse(mineType), file);
        Map<String, RequestBody> fileList = new HashMap<>();
        fileList.put("file\"; filename=\"" + file.getName(), requestBody1);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), "user111");
        fileList.put("bean", requestBody2);
        getApi()
                .uploadAvatarFile(fileList)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    public void uploadFile(BaseActivity mActivity,
                           String url, Subscriber<UpLoadFileResponseBean> s) {
        File file = new File(url);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Map<String, RequestBody> fileList = new HashMap<>();
        fileList.put("file\"; filename=\"" + file.getAbsolutePath(), requestBody1);

        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), "user111");
        fileList.put("bean", requestBody2);
        getApi()
                .uploadAvatarFile(fileList)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 修改个人中心信息
     *
     * @param mActivity
     * @param s
     */
    public void editEmployeeDetail(RxAppCompatActivity mActivity, Map<String, String> map, Subscriber<BaseBean> s) {
        Map<String, Map> obj = new HashMap<>();
        obj.put("data", map);
        getApi().editEmployeeDetail(obj)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 添加个人聊天
     *
     * @param mActivity
     * @param receiverId
     * @param s
     */
    public void addSingleChat(BaseActivity mActivity, String receiverId, Subscriber<AddSingleChatResponseBean> s) {
        Observable observable = getApi().addSingleChat(receiverId)
                .map(new HttpResultFunc<AddSingleChatResponseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 获取会话列表
     *
     * @param mActivity
     * @param s
     */
    public void getListInfo(BaseActivity mActivity, Subscriber<ConversationListBean> s) {
        Observable observable = getApi().getListInfo()
                .map(new HttpResultFunc<ConversationListBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 创建群聊
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addGroupChat(BaseActivity mActivity, AddGroupChatReqBean bean, Subscriber<AddGroupChatResponseBean> s) {
        Observable observable = getApi().addGroupChat(bean)
                .map(new HttpResultFunc<AddGroupChatResponseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 获取群详情
     *
     * @param mActivity
     * @param groupId
     * @param s
     */
    public void getGroupDetail(BaseActivity mActivity, long groupId, Subscriber<GroupChatInfoBean> s) {
        Observable observable = getApi().getGroupInfo(groupId)
                .map(new HttpResultFunc<GroupChatInfoBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 获取群详情
     *
     * @param mActivity
     * @param groupId
     * @param s
     */
    public void getGroupDetail(ActivityPresenter mActivity, long groupId, Subscriber<GroupChatInfoBean> s) {
        Observable observable = getApi().getGroupInfo(groupId)
                .map(new HttpResultFunc<GroupChatInfoBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 获取单聊详情
     *
     * @param mActivity
     * @param chatId
     * @param s
     */
    public void getSingleInfo(BaseActivity mActivity, String chatId, Subscriber<SingleChatInfoBean> s) {
        Observable observable = getApi().getSingleInfo(chatId)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 获取所有群
     *
     * @param mActivity
     * @param s
     */
    public void getAllGroupsInfo(BaseActivity mActivity, Subscriber<GetGroupListBean> s) {
        Observable observable = getApi().getAllGroupsInfo()
                .map(new HttpResultFunc<GetGroupListBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 置顶会话
     *
     * @param mActivity
     * @param id
     * @param chatType
     * @param s
     */
    public void setTop(BaseActivity mActivity, String id, int chatType, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("chat_type", chatType + "");
        Observable observable = getApi().setTop(map)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 免打扰
     *
     * @param mActivity
     * @param id
     * @param chatType
     * @param s
     */
    public void noBother(BaseActivity mActivity, String id, int chatType, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("chat_type", chatType + "");
        Observable observable = getApi().noBother(map)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 修改群名和群公告
     *
     * @param mActivity
     * @param id
     * @param groupName
     * @param gropuNotice
     * @param s
     */
    public void modifyGroupInfo(BaseActivity mActivity, long id, String groupName,
                                String gropuNotice, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("name", groupName);
        map.put("notice", gropuNotice);
        Observable observable = getApi().modifyGroupInfo(map)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 群聊添加成员
     *
     * @param mActivity
     * @param id
     * @param ids
     * @param s
     */
    public void pullPeople(BaseActivity mActivity, long id, String ids, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("selected_person", ids);
        Observable observable = getApi().pullPeople(map)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 群聊移除成员
     *
     * @param mActivity
     * @param id
     * @param ids
     * @param s
     */
    public void kickPeople(BaseActivity mActivity, long id, String ids, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("selected_person", ids);
        Observable observable = getApi().kickPeople(map)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 删除会话(隐藏)
     *
     * @param mActivity
     * @param id
     * @param chatType
     * @param s
     */
    public void hideSession(BaseActivity mActivity, long id, int chatType, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("chat_type", chatType + "");
        Observable observable = getApi().hideSession(map)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 会话显示或隐藏
     *
     * @param mActivity
     * @param id
     * @param chatType
     * @param status    0显示,1隐藏
     * @param s
     */
    public void hideSessionWithStatus(BaseActivity mActivity, long id, int chatType, int status, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("chat_type", chatType + "");
        //{"chat_type":"2","id":"0","status":"0"}
        map.put("status", status + "");
        Observable observable = getApi().hideSessionWithStatus(map)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 标记全部为已读
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void markAllRead(BaseActivity mActivity, String id, Subscriber<BaseBean> s) {

        Observable observable = getApi().markAllRead(id)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 只看未读消息
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void markReadOption(BaseActivity mActivity, String id, Subscriber<BaseBean> s) {

        Observable observable = getApi().markReadOption(id)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 请求将消息标记位已读
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void readMessage(BaseActivity mActivity, String assistantId, String id, Subscriber<BaseBean> s) {

        Observable observable = getApi().readMessage(assistantId, id)
                .map(new HttpResultFunc<BaseBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 获取小助手数据列表
     *
     * @param mActivity
     * @param id
     * @param beanName
     * @param s
     */
    public void getAssistantMessage(BaseActivity mActivity, String id, String beanName, int pageNo, int pageSize, Subscriber<AppAssistantListBean> s) {

        Observable observable = getApi().getAssistantMessage(id, beanName, pageNo, pageSize)
                .map(new HttpResultFunc<AppAssistantListBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 根据时间戳获取列表数据
     *
     * @param mActivity
     * @param id
     * @param beanName
     * @param dataSize
     * @param upTime
     * @param downTime
     * @param s
     */
    public void getAssistantMessageLimit(BaseActivity mActivity, String id, String beanName, int dataSize, Long upTime, Long downTime, Subscriber<AppAssistantListBean> s) {

        Observable observable = getApi().getAssistantMessageLimit(id, beanName, dataSize, upTime, downTime)
                .map(new HttpResultFunc<AppAssistantListBean>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        toSubscribe(observable, s);
    }

    /**
     * 查询权限
     *
     * @param mActivity
     * @param bean
     * @param style
     * @param dataId
     * @param s
     */
    public void queryAuth(BaseActivity mActivity, String bean, String style, String dataId, Subscriber<ViewDataAuthResBean> s) {

        getApi().queryAuth(bean, style, dataId)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 查询权限
     *
     * @param mActivity
     * @param applicationId
     * @param s
     */
    public void getModule(BaseActivity mActivity, String applicationId, Subscriber<AppModuleResultBean> s) {

        getApi().getModule(applicationId)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取小助手详情
     *
     * @param mActivity
     * @param assisstantId
     * @param s
     */
    public void getAssisstantInfo(BaseActivity mActivity, String assisstantId, Subscriber<AppAssistantInfoBean> s) {
        getApi().getAssisstantInfo(assisstantId)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    public void findModuleList(BaseActivity mActivity, Subscriber<ModuleBean> s) {
        getApi().findModuleList()
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 退出群
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void quitGroup(BaseActivity mActivity, long id, Subscriber<BaseBean> s) {
        getApi().quitGroup(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 解散群
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void releaseGroup(BaseActivity mActivity, long id, Subscriber<BaseBean> s) {
        getApi().releaseGroup(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 转让群组
     *
     * @param mActivity
     * @param groupId
     * @param newChargerId
     * @param s
     */
    public void transferGroup(BaseActivity mActivity, long groupId, long newChargerId, Subscriber<BaseBean> s) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", groupId);
        map.put("signId", newChargerId);
        getApi().transferGroup(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 验证权限
     *
     * @param mActivity
     * @param moduleId
     * @param s
     */
    public void queryModuleAuth(RxAppCompatActivity mActivity, String moduleId, Subscriber<QueryModuleAuthResultBean> s) {
        getApi().queryModuleAuth(moduleId)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取快速新增
     */
    public void getQuicklyAdd(RxAppCompatActivity mActivity, Subscriber<CommonModuleResultBean> s) {
        getApi().getAppList()
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 得到轮播图
     */
    public void queryCompanyBanner(RxAppCompatActivity mActivity, Subscriber<QueryBannerBean> s) {
        getApi().queryCompanyBanner()
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }
}
