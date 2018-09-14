package com.hjhq.teamface.common.utils;

import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.bean.ApproveAuthBean;

import com.hjhq.teamface.common.R;
import java.util.List;


/**
 * 审批工具类
 *
 * @author Administrator
 * @date 2018/1/10
 */

public class ApproveUtils {
    /**
     * 将状态id转换成文字
     *
     * @param state
     * @return
     */
    public static String state2String(String state) {
        switch (state) {
            case ApproveConstants.APPROVE_SUBMITTED:
                return "已提交";
            case ApproveConstants.APPROVE_PENDING:
                return "待审批";
            case ApproveConstants.APPROVING:
                return "审批中";
            case ApproveConstants.APPROVE_PASS:
                return "审批通过";
            case ApproveConstants.APPROVE_REJECT:
                return "审批驳回";
            case ApproveConstants.APPROVE_REVOKED:
                return "已撤销";
            case ApproveConstants.APPROVE_TRANSFER:
                return "已转交";
            case ApproveConstants.APPROVE_PEND_SUBMIT:
                return "待提交";
            default:
                return "流程结束";
        }
    }

    /**
     * 将状态id转换成圆形图标
     *
     * @param state
     * @return
     */
    public static int state2CircleIcon(String state) {
        switch (state) {
            case ApproveConstants.APPROVE_PENDING:
                return R.drawable.circle_approve_pending_bg;
            case ApproveConstants.APPROVING:
                return R.drawable.circle_approving_bg;
            case ApproveConstants.APPROVE_PASS:
                return R.drawable.circle_approve_pass_bg;
            case ApproveConstants.APPROVE_REJECT:
                return R.drawable.circle_approve_reject_bg;
            case ApproveConstants.APPROVE_REVOKED:
                return R.drawable.circle_approve_revoked_bg;
            default:
                break;
        }
        return R.drawable.circle_approve_pass_bg;
    }


    /**
     * 将状态id转换成颜色
     *
     * @param state
     * @return
     */
    public static String state2Color(String state) {
        String color;
        switch (state) {
            case ApproveConstants.APPROVE_PENDING:
                color = "#FFBC5F";
                break;
            case ApproveConstants.APPROVING:
                color = "#1A9AE7";
                break;
            case ApproveConstants.APPROVE_PASS:
                color = "#1AB06B";
                break;
            case ApproveConstants.APPROVE_REJECT:
                color = "#FA524D";
                break;
            case ApproveConstants.APPROVE_REVOKED:
                color = "#CFCFCF";
                break;
            case ApproveConstants.APPROVE_PEND_SUBMIT:
                color = "#CFCFCF";
                break;
            default:
                color = "#CFCFCF";
                break;
        }
        return color;
    }

    /**
     * 权限检测
     *
     * @param auths 权限集合
     * @param auth  权限
     * @return
     */
    public static boolean checkAuth(List<ApproveAuthBean> auths, String auth) {
        if (TextUtil.isEmpty(auth)) {
            return false;
        }
        if (!CollectionUtils.isEmpty(auths)) {
            for (ApproveAuthBean bean : auths) {
                if (bean == null) {
                    continue;
                }
                //将浮点型字符串转换成整形字符串
                String id = TextUtil.doubleParseInt(bean.getId());
                if (id != null) {
                    int index = id.indexOf(".");
                    if (index > 0) {
                        id = id.substring(index);
                    }
                }
                if (auth.equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }
}
