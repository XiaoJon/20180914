package com.hjhq.teamface.email;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.DataListRequestBean;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.bean.EmailListBean;
import com.hjhq.teamface.basis.bean.ModuleResultBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.hjhq.teamface.email.bean.CheckNextApprovalResultBean;
import com.hjhq.teamface.email.bean.EmailAccountListBean;
import com.hjhq.teamface.email.bean.EmailContactsListBean;
import com.hjhq.teamface.email.bean.EmailDetailBean;
import com.hjhq.teamface.email.bean.EmailFromModuleDataBean;
import com.hjhq.teamface.email.bean.EmailRecentContactsListBean;
import com.hjhq.teamface.email.bean.EmailUnreadNumBean;
import com.hjhq.teamface.email.bean.MolduleListBean;
import com.hjhq.teamface.email.bean.NewEmailBean;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/28.
 * Describe：
 */

public class EmailModel implements IModel<EmailApiService> {

    @Override
    public EmailApiService getApi() {
        return new ApiManager<EmailApiService>().getAPI(EmailApiService.class);
    }

    /**
     * 发送邮件
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void sendEmail(ActivityPresenter mActivity, NewEmailBean bean, Subscriber<BaseBean> s) {
        getApi().sendEmail(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 回复
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void mailReply(ActivityPresenter mActivity, NewEmailBean bean, Subscriber<BaseBean> s) {
        getApi().mailReply(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 转发
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void mailForward(ActivityPresenter mActivity, NewEmailBean bean, Subscriber<BaseBean> s) {
        getApi().mailForward(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 保存到草稿
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void saveToDraft(ActivityPresenter mActivity, NewEmailBean bean, Subscriber<BaseBean> s) {
        getApi().saveToDraft(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 编辑后再次保存
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void editDraft(ActivityPresenter mActivity, NewEmailBean bean, Subscriber<BaseBean> s) {
        getApi().editDraft(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 手动发送
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void manualSend(ActivityPresenter mActivity, String id, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        getApi().manualSend(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取有效邮箱账户
     *
     * @param mActivity
     * @param s
     */
    public void queryPersonnelAccount(ActivityPresenter mActivity, Subscriber<EmailAccountListBean> s) {
        getApi().queryPersonnelAccount().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取邮箱最近联系人
     *
     * @param mActivity
     * @param s
     */
    public void getRecentEmailContacts(ActivityPresenter mActivity, Subscriber<EmailRecentContactsListBean> s) {
        getApi().getRecentEmailContacts(Integer.MAX_VALUE, 1).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 邮箱未读数
     *
     * @param mActivity
     * @param s
     */
    public void queryUnreadNumsByBox(ActivityPresenter mActivity, Subscriber<EmailUnreadNumBean> s) {
        getApi().queryUnreadNumsByBox().map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    public void receive(ActivityPresenter mActivity, Subscriber<EmailListBean> s) {
        getApi().receive().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取邮箱通讯录
     *
     * @param mActivity
     * @param s
     */
    public void getEmailContacts(ActivityPresenter mActivity, Subscriber<EmailContactsListBean> s) {
        getApi().getEmailContacts(Integer.MAX_VALUE, 1).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取有邮件组件的模块列表
     *
     * @param mActivity
     * @param s
     */
    public void getModuleEmails(ActivityPresenter mActivity, Subscriber<MolduleListBean> s) {
        getApi().getModuleEmails().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 标记邮件已读和未读
     *
     * @param mActivity
     * @param ids
     * @param readStatus
     * @param boxId
     * @param s
     */
    public void markMailReadOrUnread(ActivityPresenter mActivity, String ids,
                                     String readStatus, String boxId,
                                     Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("ids", ids);
        map.put("readStatus", readStatus);
        map.put("boxId", boxId);
        getApi().markMailReadOrUnread(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 全部标记为已读
     *
     * @param mActivity
     * @param boxId
     * @param s
     */
    public void markAllRead(ActivityPresenter mActivity, String boxId,
                            Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("boxId", boxId);
        getApi().markAllRead(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 移动邮件
     *
     * @param mActivity
     * @param mailId
     * @param boxId     1 收件箱 3 草稿箱 4 已删除
     * @param s
     */
    public void moveToBox(ActivityPresenter mActivity, String mailId, String boxId,
                          Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("mailId", mailId);
        map.put("boxId", boxId);
        getApi().moveToBox(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 草稿箱删除
     *
     * @param mActivity
     * @param mailIds
     * @param boxId
     * @param s
     */
    public void deleteDraft(ActivityPresenter mActivity, String mailIds, String boxId,
                            Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", mailIds);
        getApi().deleteDraft(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 标记不是垃圾邮件
     *
     * @param mActivity
     * @param mailId    邮件id
     * @param s
     */
    public void markNotTrash(ActivityPresenter mActivity, String mailId,
                             Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", mailId);
        getApi().markNotTrash(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 彻底删除
     *
     * @param mActivity
     * @param mailId
     * @param s
     */
    public void clearMail(ActivityPresenter mActivity, String mailId,
                          Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", mailId);
        getApi().clearMail(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 收件箱拒收
     *
     * @param mActivity
     * @param accountName
     * @param s
     */
    public void refuseReceive(ActivityPresenter mActivity, String accountName,
                              Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("accountName", accountName);
        getApi().refuseReceive(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取邮件列表
     *
     * @param mActivity
     * @param pageNum
     * @param pageSize
     * @param accountId
     * @param boxId     1 收件箱 2 已发送 3 草稿箱 4 已删除 5 垃圾箱
     * @param s
     */
    public void getEmailList(ActivityPresenter mActivity, int pageNum, int pageSize, String accountId, String boxId, Subscriber<EmailListBean> s) {
        getApi().getEmailList(pageNum, pageSize, accountId, boxId).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 邮件详情
     *
     * @param id   邮件ID
     * @param type 1 邮箱邮件详情，2标签邮件详情
     * @return
     */
    public void getEmailDetail(ActivityPresenter mActivity, String id, String type, Subscriber<EmailDetailBean> s) {
        getApi().getEmailDetail(id, type).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取所有应用/模块
     *
     * @param mActivity
     * @param s
     */
    public void getAllModule(ActivityPresenter mActivity, Subscriber<ModuleResultBean> s) {
        getApi().getAllModule("1")
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取业务信息列表
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void getDataTemp(ActivityPresenter mActivity, DataListRequestBean bean, Subscriber<DataTempResultBean> s) {
        getApi().getDataTemp(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 判断是否需要选择下一审批人
     *
     * @param mActivity
     * @param s
     */
    public void checkChooseNextApproval(ActivityPresenter mActivity, Subscriber<CheckNextApprovalResultBean> s) {
        getApi().checkChooseNextApproval(CustomConstants.MAIL_BOX_SCOPE).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取邮件相关模块的子菜单
     *
     * @param mActivity
     * @param bean
     * @param ids
     * @param s
     */
    public void getEmailFromModuleDetail(ActivityPresenter mActivity, String bean, String ids, Subscriber<EmailFromModuleDataBean> s) {
        getApi().getEmailFromModuleDetail(bean, ids).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

}
