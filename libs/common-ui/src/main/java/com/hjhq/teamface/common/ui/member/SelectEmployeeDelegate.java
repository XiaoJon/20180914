package com.hjhq.teamface.common.ui.member;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.view.SideBar;


/**
 * 选人、选部门、选角色、动态 视图代理类
 *
 * @author lx
 * @date 2017/8/31
 */

public class SelectEmployeeDelegate extends AppDelegate {
    public RecyclerView mRecyclerView;
    TextView stateTv;
    public SideBar sideBar;
    TextView dialog;
    LinearLayout companyMember;
    LinearLayout chooseGroup;
    TextView searchEditText;
    private LinearLayoutManager mLinearLayoutmanager;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_select_contacts;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }


    @Override
    public void initWidget() {
        super.initWidget();

        companyMember = get(R.id.company_member_contacts_rl);
        chooseGroup = get(R.id.choose_group);
            if (getActivity() instanceof  SelectMemberActivity){
                if (((SelectMemberActivity) getActivity()).chooseGroup) {
                    chooseGroup.setVisibility(View.VISIBLE);
                }
            }

        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutmanager = new LinearLayoutManager(mContext));

        stateTv = get(R.id.state_tv);
        sideBar = get(R.id.sidrbar);
        dialog = get(R.id.dialog);
        searchEditText = get(R.id.search_edit_text);
        sideBar.setTextView(dialog);
    }

    /**
     * 设置状态文字
     *
     * @param state
     */
    public void setStateText(String state) {
        TextUtil.setText(stateTv, state);
    }

    /**
     * 设置状态控件是否显示
     *
     * @param visibility
     */
    public void setStateVisibility(int visibility) {
        stateTv.setVisibility(visibility);
    }

    /**
     * 设置搜索是否显示
     *
     * @param visibility
     */
    public void setSearchVisibility(int visibility) {
        searchEditText.setVisibility(visibility);
    }

    public void setSideBarData(String[] finalLetterArray) {
        sideBar.refresh(finalLetterArray);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(BaseQuickAdapter adapter) {
        View emptyView = mContext.getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        adapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(adapter);
    }

    public void scrollToPosition(int n) {

        int firstItem = mLinearLayoutmanager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutmanager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }
}
