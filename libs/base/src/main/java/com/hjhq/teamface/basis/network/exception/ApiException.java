package com.hjhq.teamface.basis.network.exception;


import android.text.TextUtils;

/**
 * 网络请求错误处理
 *
 * @author Administrator
 */
public class ApiException extends RuntimeException {

    public static final int USER_NOT_EXIST = 100;
    public static final int WRONG_PASSWORD = 101;
    public static final int SERVER_ERROR = 500;
    public static final int NO_AUTH_MOVE_FILE = 20020011;

    int errorCode;

    public ApiException(int errorCode, String errorMsg) {
        this(getApiExceptionMessage(errorCode, errorMsg));
        this.errorCode = errorCode;

    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code, String errorMsg) {

        if (!TextUtils.isEmpty(errorMsg)) {
            return errorMsg;
        }

        String message;

        switch (code) {
            case USER_NOT_EXIST:
                message = "该用户不存在";
                break;
            case WRONG_PASSWORD:
                message = "密码错误";
                break;
            case SERVER_ERROR:
                message = "服务器错误，请稍后再试！";
                break;
            case NO_AUTH_MOVE_FILE:
                message = "没有权限复制或移动此目录";
                break;
            default:
                message = "未知错误";
                break;

        }
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

