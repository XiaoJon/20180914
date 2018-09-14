package com.hjhq.teamface.project.ui.task;

import android.view.View;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 选择分组列表
 * Created by Administrator on 2018/7/12.
 */

public class SelectGroupTempDelegate extends AppDelegate {
    TextView tvGroup;
    TextView tvTemp;
    TextView tvSubTemp;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_select_group_temp;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuTexts(mContext.getString(R.string.confirm));
        tvGroup = get(R.id.tv_group_name);
        tvTemp = get(R.id.tv_temp_name);
        tvSubTemp = get(R.id.tv_sub_temp_name);

        setOnClickListener((View.OnClickListener) mContext, R.id.ll_group, R.id.ll_temp,R.id.ll_sub_temp);
    }

    /**
     * 列
     *
     * @param visibility
     */
    public void setTempVisibility(boolean visibility) {
        setVisibility(R.id.tv_temp, visibility);
        setVisibility(R.id.ll_temp, visibility);
    }

    /**
     * 子列
     *
     * @param visibility
     */
    public void setSubTempVisbility(boolean visibility) {
        setVisibility(R.id.tv_sub_temp, visibility);
        setVisibility(R.id.ll_sub_temp, visibility);
    }

    public void setNodeName(String nodeName) {
        TextUtil.setText(tvGroup, nodeName);
    }
    public void setSubNodeName(String subNodeName) {
        TextUtil.setText(tvTemp, subNodeName);
    }
    public void setSubTempName(String subTempName) {
        TextUtil.setText(tvSubTemp, subTempName);
    }


    public void clearSubNode() {
        TextUtil.setText(tvTemp,"");
    }

    public void clearSubTemp() {
        TextUtil.setText(tvSubTemp,"");
    }

}
