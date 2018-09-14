package com.hjhq.teamface.custom.ui.funcation;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.member.MembersView;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.adapter.ShareAdapter;
import com.hjhq.teamface.custom.bean.AddOrEditShareRequestBean;
import com.hjhq.teamface.custom.bean.ShareResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 共享
 *
 * @author Administrator
 * @date 2017/12/20
 */

public class ShareDelegate extends AppDelegate {

    private ShareItemView shareItemView;
    private RecyclerView mRecyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_share_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("共享");

        setRightMenuTexts(R.color.main_green, "新增");
        setRightMenuTexts(R.color.main_green, "保存");

        shareItemView = get(R.id.share_item_view);
        mRecyclerView = get(R.id.recycler_view);
    }


    /**
     * 设置子项 编辑 删除事件
     */
    public void addOnitemTouchListener(SimpleItemClickListener simpleItemClickListener) {
        mRecyclerView.addOnItemTouchListener(simpleItemClickListener);
    }

    /**
     * 设置数据模式
     */
    public void setTempState() {
        shareItemView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        showMenu(0);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    /**
     * 设置新增编辑模式
     */
    public void setAddOrEditState() {
        shareItemView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        shareItemView.setState(CustomConstants.ADD_STATE);
        showMenu(1);
    }

    public void setOnAddMemberClickedListener(MembersView.onAddMemberClickedListener onAddMemberClickedListener) {
        shareItemView.setOnAddMemberClickedListener(onAddMemberClickedListener);
    }

    public void setAdapter(ShareAdapter shareAdapter) {
        mRecyclerView.setAdapter(shareAdapter);
    }

    public List<Member> getMembers() {
        return shareItemView.getMembers();
    }

    public void setMembers(ArrayList<Member> members) {
        shareItemView.setMembers(members);
    }

    public AddOrEditShareRequestBean.BasicsBean getAddOrEditData() {
        return shareItemView.getAddOrEditData();
    }

    public void setEditState(ShareResultBean.DataBean dataBean) {
        shareItemView.setMembers(dataBean.getAllot_employee());
        shareItemView.setContent(TextUtil.parseInt(dataBean.getAccess_permissions()));
    }
}
