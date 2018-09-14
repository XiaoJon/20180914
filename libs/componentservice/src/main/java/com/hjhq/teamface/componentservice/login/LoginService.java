package com.hjhq.teamface.componentservice.login;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PasswrodSetBean;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.Map;

import rx.Subscriber;

/**
 * 登录服务
 * @author Administrator
 * @date 2018/3/26
 */

public interface LoginService {
    void editPassWord(RxAppCompatActivity mActivity, String oldpwd, String newpwd, Subscriber<BaseBean> s);

    void valiLogin(RxAppCompatActivity mActivity, Map<String, String> map, Subscriber<BaseBean> s);

    void queryInfo(RxAppCompatActivity mActivity, Subscriber<UserInfoBean> s);

    void login(RxAppCompatActivity activity, String phone, String password, boolean isShow);

    void getCompanySet(RxAppCompatActivity activity, String phone, Subscriber<PasswrodSetBean> s);
}
