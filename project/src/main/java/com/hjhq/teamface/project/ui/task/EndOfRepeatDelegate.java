package com.hjhq.teamface.project.ui.task;

import android.widget.EditText;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 结束重复
 * Created by Administrator on 2018/7/17.
 */

public class EndOfRepeatDelegate extends AppDelegate {
    public EditText etCount;
    private TextView tvEndDate;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_end_repeat_task;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_end_repeat);
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm));

        etCount = get(R.id.et_count);
        tvEndDate = get(R.id.tv_end_date);
    }

    /**
     * 日期
     */
    public void setEndByDate() {
        setVisibility(R.id.iv_never, false);
        setVisibility(R.id.iv_count, false);
        setVisibility(R.id.iv_date, true);

        setVisibility(R.id.ll_end_count, false);
        setVisibility(R.id.ll_end_date, true);
    }


    /**
     * 次数
     */
    public void setEndByCount() {
        setVisibility(R.id.iv_never, false);
        setVisibility(R.id.iv_count, true);
        setVisibility(R.id.iv_date, false);

        setVisibility(R.id.ll_end_count, true);
        setVisibility(R.id.ll_end_date, false);
    }

    /**
     * 永不
     */
    public void setEndByNever() {
        setVisibility(R.id.iv_never, true);
        setVisibility(R.id.iv_count, false);
        setVisibility(R.id.iv_date, false);

        setVisibility(R.id.ll_end_count, false);
        setVisibility(R.id.ll_end_date, false);
    }

    /**
     * 设置结束时间
     *
     * @param s
     */
    public void setEndDate(String s) {
        TextUtil.setText(tvEndDate, s);
    }

    public void setEndCount(String endRepeatCount) {
        etCount.setText(endRepeatCount);
    }

    public int getEndCount() {
        int count = TextUtil.parseInt(etCount.getText().toString().trim());
        return count;
    }
}
