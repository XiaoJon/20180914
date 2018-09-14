package com.hjhq.teamface.attendance.adapter;

import android.graphics.Paint;
import android.text.TextPaint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.presenter.AddApprovalActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.common.utils.CommonUtil;

import java.util.List;


public class TodayAttendanceAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TodayAttendanceAdapter(List<String> data) {
        super(R.layout.attendance_state_item, data);


    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView updateTime = helper.getView(R.id.tv_update_time);
        TextPaint paint = updateTime.getPaint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.header, true);
        } else {
            helper.setVisible(R.id.header, false);
        }
        helper.addOnClickListener(R.id.tv_update_time);
        helper.getView(R.id.tv_update_time).setOnClickListener(v -> {
            ToastUtils.showToast(v.getContext(), "更新打卡");
            CommonUtil.startActivtiyForResult(helper.getConvertView().getContext(), AddApprovalActivity.class, Constants.REQUEST_CODE1);
        });


    }
}