package com.hjhq.teamface.project.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.common.bean.ProjectLabelBean;

import java.util.List;

/**
 * 项目标签适配器
 * Created by Administrator on 2018/4/10.
 */

public class ProjectLabelsAdapter extends BaseQuickAdapter<ProjectLabelBean, BaseViewHolder> {
    private int mode = ProjectConstants.VIEW_MODE;

    public ProjectLabelsAdapter(List<ProjectLabelBean> data) {
        super(R.layout.project_label_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectLabelBean item) {
        ImageView checkIcon = helper.getView(R.id.check);
        switch (mode) {
            case ProjectConstants.VIEW_MODE:
                helper.setVisible(R.id.rl_check, false);
                helper.setVisible(R.id.delete_icon, false);
                helper.setVisible(R.id.block, true);
                helper.setVisible(R.id.block0, false);
                break;
            case ProjectConstants.EDIT_MODE:
                helper.setVisible(R.id.rl_check, false);
                helper.setVisible(R.id.delete_icon, true);
                helper.setVisible(R.id.block, true);
                helper.setVisible(R.id.block0, false);
                break;
            case ProjectConstants.SELECT_MODE:
                helper.setVisible(R.id.block0, true);
                helper.setVisible(R.id.rl_check, true);
                helper.setVisible(R.id.delete_icon, false);
                helper.setVisible(R.id.block, false);
                if (item.isCheck()) {
                    checkIcon.setImageResource(R.drawable.state_checked);
                } else {
                    checkIcon.setImageResource(R.drawable.state_uncheck);
                }
                break;
        }
        helper.setText(R.id.label_name, item.getName());
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.rl_category, true);
            helper.setText(R.id.label_category, item.getParent_name());
        }
        if (helper.getAdapterPosition() > 0) {
            if (getItem(helper.getAdapterPosition() - 1).getParent_id().equals(item.getParent_id())) {
                helper.setVisible(R.id.rl_category, false);
            } else {
                helper.setVisible(R.id.rl_category, true);
                helper.setText(R.id.label_category, item.getParent_name());
            }
        }
        helper.addOnClickListener(R.id.delete_icon);
        helper.addOnClickListener(R.id.rl_check);
        ImageView ivlabel = helper.getView(R.id.label_icon);
        ivlabel.setBackgroundColor(ColorUtils.hexToColor(item.getColour()));
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
