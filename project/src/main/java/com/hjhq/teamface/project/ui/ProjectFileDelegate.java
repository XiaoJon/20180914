package com.hjhq.teamface.project.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.view.RecordVoiceButton;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.ProjectFileListAdapter;

/**
 * 新增项目/项目设置
 */

public class ProjectFileDelegate extends AppDelegate {

    public FrameLayout flRecord;
    public RecordVoiceButton rvBtn;
    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRvNavi;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_file_list2;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuIcons(R.drawable.project_upload_personal_file_icon, R.drawable.icon_menu);
        rvBtn = get(R.id.jmui_voice_btn);
        flRecord = get(R.id.fl_record);
        mRecyclerView = get(R.id.rv);
        mSwipeRefreshLayout = get(R.id.swipe_refresh_layout);
        mRvNavi = get(R.id.rv_navi);
        mRvNavi.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public void setOrientation(int orientation) {
                super.setOrientation(HORIZONTAL);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBtn.setOnClickListener(v -> {

        });

    }

    /**
     * 导航栏数据
     *
     * @param adapter
     */
    public void setNaviAdapter(BaseQuickAdapter adapter) {
        mRvNavi.setAdapter(adapter);
    }

    /**
     * @param listener
     */
    public void setNaviClick(RecyclerView.OnItemTouchListener listener) {
        mRvNavi.addOnItemTouchListener(listener);
    }

    public void showRecordBtn() {
        flRecord.setVisibility(View.VISIBLE);
        rvBtn.setVisibility(View.VISIBLE);
    }

    public void hideRecordBtn() {
        flRecord.setVisibility(View.GONE);
        rvBtn.setVisibility(View.GONE);
        getRootView().requestLayout();
    }

    public void setAdapter(ProjectFileListAdapter fileListAdapter) {
        mRecyclerView.setAdapter(fileListAdapter);
    }
}
