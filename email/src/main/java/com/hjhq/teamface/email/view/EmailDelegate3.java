package com.hjhq.teamface.email.view;

import android.view.View;
import android.widget.TextView;

import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.bean.EmailUnreadNumBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class EmailDelegate3 extends AppDelegate {
    private TextView tvNum1;
    private TextView tvNum2;
    private TextView tvNum3;
    private TextView tvNum4;
    private TextView tvNum5;
    private TextView tvNum6;

    @Override
    public int getRootLayoutId() {
        return R.layout.email_main_activity;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        tvNum1 = get(R.id.tv12);
        tvNum2 = get(R.id.tv22);
        tvNum3 = get(R.id.tv32);
        tvNum4 = get(R.id.tv42);
        tvNum5 = get(R.id.tv52);
        tvNum6 = get(R.id.tv62);
    }


    /**
     * 侧滑菜单条目点击事件
     *
     * @param listener
     */
    public void setMenuItemClickListener(View.OnClickListener listener) {

        get(R.id.rl1).setOnClickListener(listener);
        get(R.id.rl2).setOnClickListener(listener);
        get(R.id.rl3).setOnClickListener(listener);
        get(R.id.rl4).setOnClickListener(listener);
        get(R.id.rl5).setOnClickListener(listener);
        get(R.id.rl6).setOnClickListener(listener);
        get(R.id.rl7).setOnClickListener(listener);
    }

    /**
     * 显示未读数量
     *
     * @param unreadNunBean
     */
    public void showUnreadNum(EmailUnreadNumBean unreadNunBean) {
        if (unreadNunBean != null) {
            TextUtil.setText(tvNum1, "0");
            TextUtil.setText(tvNum2, "0");
            TextUtil.setText(tvNum3, "");
            TextUtil.setText(tvNum4, "0");
            TextUtil.setText(tvNum5, "");
            TextUtil.setText(tvNum6, "");
            List<EmailUnreadNumBean.DataBean> data = unreadNunBean.getData();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    if ("0".equals(data.get(i).getCount())) {
                        setUnreadNum(data.get(i).getMail_box_id(), "0");
                    } else {
                        setUnreadNum(data.get(i).getMail_box_id(), data.get(i).getCount());
                    }

                }
            }
        }

    }

    /**
     * 设置未读数
     *
     * @param mailBoxId
     * @param count
     */
    private void setUnreadNum(String mailBoxId, String count) {

        switch (mailBoxId + "") {
            case EmailConstant.INBOX:
                TextUtil.setText(tvNum1, count);
                break;
            case EmailConstant.UNREAD:
                TextUtil.setText(tvNum2, count);
                break;
            case EmailConstant.SENDED:
                TextUtil.setText(tvNum3, count);
                break;
            case EmailConstant.DRAFTBOX:
                TextUtil.setText(tvNum4, count);
                break;
            case EmailConstant.DELETED:
                TextUtil.setText(tvNum5, count);
                break;
            case EmailConstant.TRASH:
                TextUtil.setText(tvNum6, count);
                break;
            default:
                break;
        }

    }
}
