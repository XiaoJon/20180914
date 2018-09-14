package com.hjhq.teamface.oa.approve.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.member.MembersView;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.componentservice.custom.CustomService;
import com.hjhq.teamface.oa.approve.bean.ApproverBean;
import com.hjhq.teamface.oa.approve.bean.ProcessFlowResponseBean;
import com.hjhq.teamface.common.utils.ApproveUtils;
import com.hjhq.teamface.oa.approve.widget.ApproveTaskView;
import com.luojilab.component.componentlib.router.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 审批详情视图
 *
 * @author lx
 * @date 2017/9/4
 */

public class ApproveDetailDelegate extends AppDelegate {
    private LinearLayout llCustomLayout;
    private TextView tvOption1;
    private TextView tvOption2;
    private TextView tvOption3;
    private TextView tvOption4;
    private ImageView ivAvatar;
    private TextView tvName;
    private TextView tvSubTitle;
    private ApproveTaskView approveTaskView;
    MembersView membersView;
    private ImageView ivApproveTag;

    List<Object> mViewList = new ArrayList<>();
    private CustomService service;

    @Override
    public int getRootLayoutId() {
        return R.layout.approve_detail_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        llCustomLayout = get(R.id.ll_custom_layout);
        tvOption1 = get(R.id.tv_option1);
        tvOption2 = get(R.id.tv_option2);
        tvOption3 = get(R.id.tv_option3);
        tvOption4 = get(R.id.tv_option4);
        ivAvatar = get(R.id.iv_avatar);
        ivApproveTag = get(R.id.iv_approve_tag);
        membersView = get(R.id.member_view);
        membersView.setAddMemberIvVisibility(View.GONE);
        tvName = get(R.id.tv_name);
        tvSubTitle = get(R.id.tv_subTitle);
        approveTaskView = get(R.id.approve_task_view);
        service = (CustomService) Router.getInstance().getService(CustomService.class.getSimpleName());
    }


    /**
     * 绘制布局
     *
     * @param layoutBeanList 布局
     * @param detailMap      数据
     * @param state          0新增 1详情 2编辑
     * @param moduleId       模块id
     */
    public void drawLayout(CustomLayoutResultBean.DataBean layoutBeanList, Map detailMap, int state, String moduleId) {
        if (layoutBeanList == null) {
            return;
        }

        llCustomLayout.removeAllViews();
        String title = layoutBeanList.getTitle();
        setTitle(title);
        List<CustomLayoutResultBean.DataBean.LayoutBean> layout = layoutBeanList.getLayout();
        if (layout == null) {
            return;
        }
        mViewList.clear();
        for (CustomLayoutResultBean.DataBean.LayoutBean layoutBean : layout) {
            boolean isSpread = "0".equals(layoutBean.getIsSpread());
            String name = layoutBean.getName();
            boolean isHideColumnName = "1".equals(layoutBean.getIsHideColumnName());
            if ("systemInfo".equals(name)) {
                //系统信息 隐藏
                continue;
            }

            List<CustomBean> list = layoutBean.getRows();
            for (CustomBean customBean : list) {
                Object o = detailMap.get(customBean.getName());
                customBean.setValue(o);
            }

            if (service != null) {
                Object subfieldView = service.getSubfield(list, state, layoutBean.getTitle(), isSpread, moduleId, isHideColumnName, llCustomLayout);
                mViewList.add(subfieldView);
            }
        }
    }

    /**
     * 获取详情信息
     *
     * @return
     */
    public JSONObject getDetail() {
        JSONObject json = new JSONObject();
        if (service != null) {
            boolean put = service.putNoCheck(mViewList, json);
            if (!put) {
                return null;
            }
        }
        return json;
    }

    /**
     * 设置审批申请人
     *
     * @param beginUser
     */
    public void setBeginUser(ApproverBean beginUser, String processStatus) {
        if (beginUser == null) {
            return;
        }
        View stateView = get(R.id.state_view);
        stateView.setBackgroundResource(ApproveUtils.state2CircleIcon(processStatus));

        TextUtil.setText(tvName, beginUser.getEmployee_name());
        TextUtil.setText(tvSubTitle, ApproveUtils.state2String(processStatus));
        ImageLoader.loadCircleImage(mContext, beginUser.getPicture(), ivAvatar, beginUser.getEmployee_name());
        TextUtil.setText(tvName, beginUser.getEmployee_name());
    }

    /**
     * 设置抄送人
     *
     * @param ccTos
     */
    public void setCCTo(List<Member> ccTos) {
        membersView.setMembers(ccTos);
    }

    /**
     * 设置审批流程
     *
     * @param taskFlow
     */
    public void setApproveTaskFlow(List<ProcessFlowResponseBean.DataBean> taskFlow) {
        approveTaskView.setApproveTaskFlow(taskFlow);
    }

    public void setOptionVisibility(int visibility) {
        get(R.id.ll_bottom_option).setVisibility(visibility);
    }

    public void setOption1Text(String text) {
        tvOption1.setVisibility(View.VISIBLE);
        get(R.id.line1).setVisibility(View.VISIBLE);
        TextUtil.setText(tvOption1, text);
    }

    public void setOption2Text(String text) {
        tvOption2.setVisibility(View.VISIBLE);
        get(R.id.line2).setVisibility(View.VISIBLE);
        TextUtil.setText(tvOption2, text);
    }

    public void setOption3Text(String text) {
        tvOption3.setVisibility(View.VISIBLE);
        get(R.id.line3).setVisibility(View.VISIBLE);
        TextUtil.setText(tvOption3, text);
    }

    public void setOption4Text(String text) {
        tvOption4.setVisibility(View.VISIBLE);
        TextUtil.setText(tvOption4, text);
    }


    public void hideOption1() {
        tvOption1.setVisibility(View.GONE);
    }

    public void hideOption2() {
        tvOption2.setVisibility(View.GONE);
    }

    public void hideOption3() {
        tvOption3.setVisibility(View.GONE);
    }

    public void hideOption4() {
        tvOption4.setVisibility(View.GONE);
    }

    /**
     * 设置审批标签图标
     *
     * @param visible
     */
    public void setApproveTagVisible(int visible) {
        ivApproveTag.setVisibility(visible);
    }

    /**
     * 设置审批标签图标
     *
     * @param res
     */
    public void setApproveTag(int res) {
        setApproveTagVisible(View.VISIBLE);
        ivApproveTag.setImageResource(res);
    }
}