package com.hjhq.teamface.memo.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.memo.MemoModel;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.adapter.MemoModuleRelevanceAdapter;
import com.hjhq.teamface.memo.view.MemoListFragmentDelegate;

import java.util.ArrayList;

public class RelevantModuleListFragment extends FragmentPresenter<MemoListFragmentDelegate, MemoModel> {
    private int type;
    private RecyclerView mRecyclerView;
    private MemoModuleRelevanceAdapter mAdapter;
    private EmptyView mEmptyView;
    private SwipeRefreshLayout srlLayout;
    private ArrayList<AppModuleBean> dataList = new ArrayList<>();


    static RelevantModuleListFragment newInstance(int index) {
        RelevantModuleListFragment myFragment = new RelevantModuleListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, index);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt(Constants.DATA_TAG1, 0);
        }
    }

    @Override
    protected void init() {
        mRecyclerView = viewDelegate.get(R.id.rv);
        srlLayout = viewDelegate.get(R.id.srl);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MemoModuleRelevanceAdapter(dataList);
        mRecyclerView.setAdapter(mAdapter);

        mEmptyView = new EmptyView(getActivity());
        mEmptyView.setEmptyImage(R.drawable.memo_empty_view_img);
        mEmptyView.setEmptyTitle("无数据");
        srlLayout.setEnabled(false);
        mAdapter.setEmptyView(mEmptyView);

    }

    private String selectAppName;
    private String selectModuleName;
    private String selectBeanName;

    public void setData(String appName,
                        ArrayList<AppModuleBean> list) {

        selectAppName = appName;
        dataList.clear();
        dataList.addAll(list);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, selectAppName);
                bundle.putString(Constants.DATA_TAG2, selectModuleName);
                bundle.putString(Constants.DATA_TAG3, selectBeanName);
                CommonUtil.startActivtiyForResult(getActivity(), SearchModuleDataActivity.class, Constants.REQUEST_CODE1, bundle);
            }
        });
    }
}
