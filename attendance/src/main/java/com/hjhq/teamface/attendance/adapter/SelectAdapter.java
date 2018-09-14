package com.hjhq.teamface.attendance.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AddDateBean;
import com.hjhq.teamface.attendance.bean.AttendanceTypeListBean;
import com.hjhq.teamface.attendance.bean.BaseSelectBean;
import com.hjhq.teamface.basis.constants.AttendanceConstants;

import java.util.List;

/**
 * @author wkd
 * @desc 聊天信息列表
 */
public class SelectAdapter extends BaseMultiItemQuickAdapter<BaseSelectBean, BaseViewHolder> {
    private int memberNum;

    public SelectAdapter(List<BaseSelectBean> data) {
        super(data);
        //文本
        addItemType(AttendanceConstants.TYPE_SELECT_TIME, R.layout.attendance_select_time_item);
        addItemType(AttendanceConstants.TYPE_SELECT_LOCATION, R.layout.attendance_select_location_item);
        addItemType(AttendanceConstants.TYPE_SELECT_WIFI, R.layout.attendance_select_wifi_item);
    }


    @Override
    protected void convert(BaseViewHolder helper, BaseSelectBean item) {
        switch (item.getItemType()) {
            case AttendanceConstants.TYPE_SELECT_TIME:
                AddDateBean bean1 = ((AddDateBean) item);
                if ("1".equals(bean1.getType())) {
                    if (TextUtils.isEmpty(bean1.getId())) {
                        helper.setText(R.id.text2, bean1.getName());
                    } else {
                        helper.setText(R.id.text2, bean1.getName() + ":" + bean1.getAttendance_time());
                    }
                } else {
                    helper.setText(R.id.text2, bean1.getName());
                }
                if (!TextUtils.isEmpty(bean1.getAttendance_time())) {
                    helper.setText(R.id.text2, bean1.getName() + ":" + bean1.getAttendance_time());
                }

                break;
            case AttendanceConstants.TYPE_SELECT_LOCATION:
                AttendanceTypeListBean bean2 = ((AttendanceTypeListBean) item);
                helper.setText(R.id.text1, bean2.getName());
                helper.setText(R.id.text2, "有效范围:" + bean2.getEffective_range() + "米");
                helper.setText(R.id.text3, "考勤地址:" + bean2.getAddress());
                break;
            case AttendanceConstants.TYPE_SELECT_WIFI:
                AttendanceTypeListBean bean3 = ((AttendanceTypeListBean) item);
                helper.setText(R.id.text1, bean3.getName());
                helper.setText(R.id.text2, bean3.getAddress());
                break;
        }
        View view = helper.getView(R.id.check);
        view.setVisibility(View.INVISIBLE);
        if (item.isCheck()) {
            view.setVisibility(View.VISIBLE);
        }


    }


}