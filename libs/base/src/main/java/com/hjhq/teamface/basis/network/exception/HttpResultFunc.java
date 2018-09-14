package com.hjhq.teamface.basis.network.exception;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.MyResponse;
import com.hjhq.teamface.basis.util.EventBusUtils;

import rx.functions.Func1;

/**
 * @author lx
 * @date 2017/3/14
 * 描述:返回码结果处理
 */

public class HttpResultFunc<T extends BaseBean> implements Func1<T, T> {
    public static final int NORMAL = 1001;
    public static final int LOGOUT = 1003;
    public static final int RQ_PAST = 20020003;
    public static final int NO_AUTH_MOVE_FILE = 20020011;
    public static final int NOT_PERMISSION = 9999;

    public HttpResultFunc() {
    }

    @Override
    public T call(T baseBean) {
        MyResponse response = baseBean.getResponse();
        if (response == null) {
            throw new ApiException(0,
                    "返回结果为空");
        }

        if (NORMAL == response.getCode()) {
        } else if (LOGOUT == response.getCode()) {
            //用户多处登录
            EventBusUtils.sendEvent(new MessageBean(LOGOUT, null, null));
            throw new ApiException(response.getCode(),
                    "该账号已在其他地方登陆！");
        } else if (RQ_PAST == response.getCode()) {
            //二维码过期
            throw new ApiException(response.getCode(),
                    "二维码过期,请重新获取二维码后重试!");
        } else if (NOT_PERMISSION == response.getCode()) {
            //没有权限
            EventBusUtils.sendEvent(new MessageBean(NOT_PERMISSION, null, null));
            throw new ApiException(response.getCode(),
                    "没有权限，请联系企业管理员");
        } else if (NO_AUTH_MOVE_FILE == response.getCode()) {
            //没有权限复制或移动此目录

            throw new ApiException(response.getCode(),
                    "没有权限复制或移动此目录");
        } else {
            throw new ApiException(response.getCode(),
                    response.getDescribe());
        }
        return baseBean;
    }
}
