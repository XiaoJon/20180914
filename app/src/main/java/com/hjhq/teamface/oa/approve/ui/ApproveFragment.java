package com.hjhq.teamface.oa.approve.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.ApproveListBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.oa.approve.adapter.ApproveAdapter;
import com.hjhq.teamface.oa.approve.bean.ApproveResponseBean;
import com.hjhq.teamface.util.CommonUtil;
import com.hjhq.teamface.view.recycler.SimpleItemClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 审批
 * Created by lx on 2017/5/15.
 */

public class ApproveFragment extends FragmentPresenter<ApproveFragmentDelegate, ApproveModel> {

    public static final int TAG1 = 0;
    public static final int TAG2 = 1;
    public static final int TAG3 = 2;
    public static final int TAG4 = 3;
    //下标
    private int index;
    private ApproveAdapter adapter;

    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private int state = Constants.NORMAL_STATE;
    private Map<String, Object> queryWhere;

    private List<ApproveListBean> approveList = new ArrayList<>();

    static ApproveFragment newInstance(int index) {
        ApproveFragment myFragment = new ApproveFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, index);
        myFragment.setArguments(bundle);
        return myFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            index = arguments.getInt(Constants.DATA_TAG1, TAG1);
        } else {
            index = savedInstanceState.getInt(Constants.DATA_TAG1, TAG1);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.DATA_TAG1, index);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void init() {
        adapter = new ApproveAdapter(approveList);
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        TextView titleTv = emptyView.findViewById(R.id.title_tv);
        titleTv.setText("暂无审批，喝杯咖啡思考人生");
        adapter.setEmptyView(emptyView);

        viewDelegate.setAdapter(adapter);

        initData();
    }

    private void initData() {
        getApproveList();
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapte, View view, int position) {
                ApproveListBean item = (ApproveListBean) adapte.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(ApproveConstants.PROCESS_INSTANCE_ID, item.getProcess_definition_id());
                bundle.putString(ApproveConstants.PROCESS_FIELD_V, item.getProcess_field_v());
                bundle.putString(ApproveConstants.MODULE_BEAN, item.getModule_bean());
                bundle.putString(ApproveConstants.APPROVAL_DATA_ID, item.getApproval_data_id());
                bundle.putString(ApproveConstants.TASK_KEY, item.getTask_key());
                bundle.putString(ApproveConstants.TASK_NAME, item.getTask_name());
                bundle.putString(ApproveConstants.TASK_ID, item.getTask_id());
                bundle.putString(Constants.DATA_ID, item.getId());
                bundle.putInt(ApproveConstants.APPROVE_TYPE, index);
                bundle.putString(ApproveConstants.APPROVAL_READ, item.getStatus());
                CommonUtil.startActivtiy(getActivity(), ApproveDetailActivity.class, bundle);
            }
        });
        viewDelegate.swipeRefreshWidget.setOnRefreshListener(() -> {
            refresh();
            viewDelegate.swipeRefreshWidget.setRefreshing(false);
        });

        //加载更更多
        adapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                adapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            getApproveList();
        }, viewDelegate.mRecyclerView);
    }


    /**
     * 得到审批
     */
    public void getApproveList() {
        int pageNum = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        model.queryApprovalList((ActivityPresenter) getActivity(), index, Constants.PAGESIZE, pageNum, queryWhere, new ProgressSubscriber<ApproveResponseBean>(getActivity()) {
            @Override
            public void onNext(ApproveResponseBean baseBean) {
                super.onNext(baseBean);
                ApproveResponseBean.DataBean dataBean = baseBean.getData();
                //分页信息
                PageInfo pageInfo = dataBean.getPageInfo();

                List<ApproveListBean> data = dataBean.getDataList();

                switch (state) {
                    case Constants.NORMAL_STATE:
                    case Constants.REFRESH_STATE:
                        CollectionUtils.notifyDataSetChanged(adapter, adapter.getData(), data);
                        break;
                    case Constants.LOAD_STATE:
                        adapter.addData(data);
                        adapter.loadMoreComplete();
                        break;
                    default:
                        break;
                }
                if (pageInfo != null) {
                    totalPages = pageInfo.getTotalPages();
                    currentPageNo = pageInfo.getPageNum();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (state == Constants.LOAD_STATE) {
                    adapter.loadMoreFail();
                }
            }
        });
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean messageBean) {
        int responseCode = messageBean.getCode();
        //筛选回调
        if (responseCode == ApproveFilterFragment.APPROVE_FILTER_DATA) {
            String tag = messageBean.getTag();
            if (!(index + "").equals(tag)) {
                return;
            }
            Object object = messageBean.getObject();
            if (object instanceof Map) {
                queryWhere = (Map<String, Object>) object;
                state = Constants.REFRESH_STATE;
                getApproveList();
            }
        } else if (responseCode == ApproveConstants.REFRESH) {
            refresh();
        }
    }

    /**
     * 刷新
     */
    private void refresh(){
        state = Constants.REFRESH_STATE;
        getApproveList();
    }
}
