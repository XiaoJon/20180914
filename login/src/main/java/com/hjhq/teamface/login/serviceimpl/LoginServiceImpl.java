package com.hjhq.teamface.login.serviceimpl;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PasswrodSetBean;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.componentservice.login.LoginService;
import com.hjhq.teamface.login.model.LoginModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * 自定义组件对外接口实现
 *
 * @author Administrator
 * @date 2018/3/26
 */

public class LoginServiceImpl implements LoginService {
    @Override
    public void editPassWord(RxAppCompatActivity mActivity, String oldpwd, String newpwd, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>(2);
        map.put("passWord", oldpwd);
        map.put("newPassWord", newpwd);
        new LoginModel().getApi().editPassWord(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);

    }

    @Override
    public void valiLogin(RxAppCompatActivity mActivity, Map<String, String> map, Subscriber<BaseBean> s) {
        new LoginModel().getApi().valiLogin(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    @Override
    public void queryInfo(RxAppCompatActivity mActivity, Subscriber<UserInfoBean> s) {
        new LoginModel().getApi().queryInfo()
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    @Override
    public void login(RxAppCompatActivity activity, String phone, String password, boolean isShow) {
        new LoginModel().login(activity, phone, password, isShow);
    }

    @Override
    public void getCompanySet(RxAppCompatActivity activity, String phone, Subscriber<PasswrodSetBean> s) {
        new LoginModel().getApi().getCompanySet(phone)
                .map(new HttpResultFunc<>())
                .compose(activity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }
}
