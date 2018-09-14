package com.hjhq.teamface.oa.login.logic;

import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.BaseLogic;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.SingleInstance;
import com.hjhq.teamface.componentservice.login.LoginService;
import com.luojilab.component.componentlib.router.Router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/28.
 * Describe：
 */

public class LoginLogic extends BaseLogic {
    public static LoginLogic getInstance() {
        return (LoginLogic) SingleInstance.getInstance(LoginLogic.class
                .getName());
    }

    private LoginService getApi() {
        return (LoginService) Router.getInstance().getService(LoginService.class.getSimpleName());
    }

    /**
     * 扫码登录
     *
     * @param mActivity
     * @param userName
     * @param id
     * @param s
     */
    public void valiLogin(BaseActivity mActivity, String userName, String id, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("id", id);
        getApi().valiLogin(mActivity,map,s);
    }

    /**
     * 获取主部门id
     *
     * @return
     */
    public String getMainDepartmentId() {
        String mainDepartmentId = "";
        UserInfoBean userInfoBean = SPHelper.getUserInfo(UserInfoBean.class);
        List<UserInfoBean.DataBean.DepartmentInfoBean> departmentInfo = userInfoBean.getData().getDepartmentInfo();
        for (int i = 0; i < departmentInfo.size(); i++) {
            if ("1".equals(departmentInfo.get(i).getIs_main())) {
                mainDepartmentId = departmentInfo.get(i).getId();
            }
        }
        return mainDepartmentId;
    }

    /**
     * 获取主部门名字
     *
     * @return
     */
    public String getMainDepartmentName() {
        String mainDepartmentName = "我的部门";
        UserInfoBean userInfoBean = SPHelper.getUserInfo(UserInfoBean.class);
        List<UserInfoBean.DataBean.DepartmentInfoBean> departmentInfo = userInfoBean.getData().getDepartmentInfo();
        for (int i = 0; i < departmentInfo.size(); i++) {
            if ("1".equals(departmentInfo.get(i).getIs_main())) {
                mainDepartmentName = departmentInfo.get(i).getDepartment_name();
            }
        }
        return mainDepartmentName;
    }

    /**
     * 初始化个人信息
     *
     * @param activity
     */
    public void initUserInfo(BaseActivity activity, Subscriber<UserInfoBean> s) {
        getApi().queryInfo(activity,s);
    }

}
