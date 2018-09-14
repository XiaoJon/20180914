package com.hjhq.teamface.login.model;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.login.LoginApiService;
import com.hjhq.teamface.login.R;
import com.hjhq.teamface.login.bean.LoginResponseBean;
import com.hjhq.teamface.login.presenter.LoginActivity;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;


/**
 * 登录
 */
public class LoginModel implements IModel<LoginApiService> {

    @Override
    public LoginApiService getApi() {
        return new ApiManager<LoginApiService>().getAPI(LoginApiService.class);
    }


    /**
     * 获取验证码
     *
     * @param mActivity 活动界面
     * @param telephone 手机号
     * @param type      0通用 1注册 2忘记密码
     * @param s         结果回调
     */
    public void sendSmsCode(ActivityPresenter mActivity, String telephone, int type, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("telephone", telephone);
        map.put("type", Integer.toString(type));
        getApi().sendSmsCode(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);

    }

    /**
     * 验证码校验
     *
     * @param mActivity 活动界面
     * @param telephone 手机号
     * @param type      0通用 1注册 2忘记密 3修改手机号
     * @param code      验证码
     * @param s         结果回调
     */
    public void verifySmsCode(ActivityPresenter mActivity, String telephone, int type, String code, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("telephone", telephone);
        map.put("type", Integer.toString(type));
        map.put("smsCode", code);
        getApi().verifySmsCode(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 注册并登录
     *
     * @param mActivity   活动界面
     * @param companyName 公司名称
     * @param name        姓名
     * @param pwd         密码
     * @param inviteCode  邀请码
     * @param phone       手机号
     */
    public void register(RxAppCompatActivity mActivity, String companyName, String name, String pwd, String inviteCode, String phone) {
        Map<String, String> map = new HashMap<>(5);
        map.put("company_name", companyName);
        map.put("user_name", name);
        map.put("user_pwd", pwd);
        map.put("invite_code", inviteCode);
        map.put("phone", phone);

        View childAt = ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);
        getApi().register(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(new ProgressSubscriber<BaseBean>(mActivity) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (TextUtil.isEmpty(inviteCode)) {
                            DialogUtils.getInstance().sureDialog(mActivity
                                    , mActivity.getString(R.string.hint)
                                    , mActivity.getString(R.string.login_register_hint)
                                    , childAt
                                    , () -> CommonUtil.startActivtiy(mActivity, LoginActivity.class));
                            return;
                        }
                        SPHelper.setUserAccount(phone);
                        SPHelper.setUserPassword(pwd);

                        login(mActivity, phone, pwd);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.dismissWindowView();
                        DialogUtils.getInstance().sureDialog(mActivity, mActivity.getString(R.string.hint), e.getMessage(), childAt);
                    }
                });
    }


    /**
     * 重设密码并登录
     *
     * @param mActivity 活动界面
     * @param loginName 帐号
     * @param loginPwd  密码
     */
    public void resetPwd(RxAppCompatActivity mActivity, String loginName, String loginPwd) {
        Map<String, String> map = new HashMap<>(2);
        map.put("loginName", loginName);
        map.put("loginPwd", loginPwd);
        getApi().modifyPwd(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(new ProgressSubscriber<LoginResponseBean>(mActivity) {
                    @Override
                    public void onNext(LoginResponseBean baseBean) {
                        super.onNext(baseBean);
                        SPHelper.setUserAccount(loginName);
                        SPHelper.setUserPassword(loginPwd);
                        login(mActivity, loginName, loginPwd);
                    }
                });
    }

    /**
     * 登录
     *
     * @param activity 活动界面
     * @param phone    手机号
     * @param password 密码
     */
    public void login(RxAppCompatActivity activity, String phone, String password) {
        login(activity, phone, password, true);
    }

    /**
     * 登录
     *
     * @param activity 活动界面
     * @param phone    手机号
     * @param password 密码
     * @param isShow   是否显示Loading
     */
    public void login(RxAppCompatActivity activity, String phone, String password, boolean isShow) {
        Map<String, String> map = new HashMap<>(2);
        map.put("userName", phone);
        map.put("passWord", password);
        getApi().login(map)
                .map(new HttpResultFunc<>())
                .compose(activity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(new ProgressSubscriber<LoginResponseBean>(activity, isShow) {
                    @Override
                    public void onNext(LoginResponseBean loginResponseBean) {
                        super.onNext(loginResponseBean);
                        LoginResponseBean.DataBean data = loginResponseBean.getData();
                        //保存Token
                        SPHelper.setToken(data.getToken());
                        //保存账号
                        SPHelper.setUserAccount(phone);

                        if (Constants.TERM_SIGN.equals(data.getTerm_sign())) {
                            //需要重新修改密码
                            DialogUtils.getInstance().sureDialog(activity
                                    , null
                                    , activity.getString(R.string.login_term_sign)
                                    , ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0)
                                    , () -> {
                                        Bundle bundle = new Bundle();
                                        bundle.putBoolean(Constants.DATA_TAG1, true);
                                        UIRouter.getInstance().openUri(activity, "DDComp://app/changePwd", bundle);
                                    }
                            );
                            return;
                        }

                        //保存密码(已加密)
                        SPHelper.setUserPassword(password);
                        //初始化
                        initUserInfo(activity, isShow);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.dismissWindowView();
                        e.printStackTrace();
                        View childAt = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
                        SPHelper.setLoginBefore(false);
                        if (activity instanceof LoginActivity) {
                            DialogUtils.getInstance().sureDialog(activity, activity.getString(R.string.login_failed), e.getMessage(), childAt);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.DATA_TAG1, e.getMessage());
                            CommonUtil.startActivtiy(activity, LoginActivity.class, bundle);
                        }
                    }
                });
    }

    /**
     * 初始化登录后信息
     *
     * @param activity 活动界面
     * @param isShow   是否显示Loading
     */
    public void initUserInfo(RxAppCompatActivity activity, boolean isShow) {
        getApi().queryInfo()
                .map(new HttpResultFunc<>())
                .compose(activity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(new ProgressSubscriber<UserInfoBean>(activity, isShow) {
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
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showError(activity, "初始化公司信息失败!");
                            EventBusUtils.sendEvent(new MessageBean(HttpResultFunc.LOGOUT, null, null));
                        }

                        try {
                            //保存部门信息
                            List<UserInfoBean.DataBean.DepartmentInfoBean> departmentInfo = userInfoBean.getData().getDepartmentInfo();
                            if (!CollectionUtils.isEmpty(departmentInfo)){
                                SPHelper.setDepartmentId(departmentInfo.get(0).getId());
                                SPHelper.setDepartmentName(departmentInfo.get(0).getDepartment_name());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showError(activity, "初始化部门信息失败!");
                            SPHelper.setLoginBefore(false);
                            EventBusUtils.sendEvent(new MessageBean(HttpResultFunc.LOGOUT, null, null));
                        }

                        try {
                            //保存用户信息
                            UserInfoBean.DataBean.EmployeeInfoBean employeeInfo = userInfoBean.getData().getEmployeeInfo();
                            SPHelper.setUserId(employeeInfo.getSign_id());
                            SPHelper.setEmployeeId(employeeInfo.getId());
                            SPHelper.setUserAvatar(employeeInfo.getPicture());
                            SPHelper.setUserName(employeeInfo.getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showError(activity, "初始化个人信息失败!");
                            EventBusUtils.sendEvent(new MessageBean(HttpResultFunc.LOGOUT, null, null));
                        }
                        SPHelper.setUserInfo(userInfoBean);

                        //切换数据库
                        IM.getInstance().init(context);

                        AppManager.getAppManager().finishAllActivity();
                        EventBusUtils.sendEvent(new MessageBean(EventConstant.LOGIN_TAG, null, null));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        EventBusUtils.sendEvent(new MessageBean(HttpResultFunc.LOGOUT, null, null));
                    }
                });
    }


    /**
     * 修改手机号
     *
     * @param mActivity 活动界面
     * @param phone     手机号
     * @param smsCode   短信验证码
     * @param s         观察者
     */
    public void editPhone(RxAppCompatActivity mActivity, String phone, String smsCode, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>(2);
        map.put("phone", phone);
        map.put("sms_code", smsCode);
        getApi().editPhone(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }
}
