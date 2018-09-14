package com.hjhq.teamface.im.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseFragment;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.MessageReadAdapter;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.ArrayList;
import java.util.List;


/**
 * 已读或未读成员列表
 */

public class MessageUnreadListFragment extends BaseFragment {
    public static final int TAG1 = 100;//已读人员
    public static final int TAG2 = 200;//未读人员

    RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MessageReadAdapter mNoticeAdapter;

    private LayoutInflater inflater;
    TextView footerTv;
    private List<Member> noticeList = new ArrayList<>();
    private int index;

    static MessageUnreadListFragment newInstance(int index) {
        MessageUnreadListFragment myFragment = new MessageUnreadListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, index);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.DATA_TAG1, index);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onSetContentViewNext(Bundle savedInstanceState) {
        super.onSetContentViewNext(savedInstanceState);
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(Constants.DATA_TAG1);
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_message_unread_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_unread);
        inflater = getActivity().getLayoutInflater();
        View footer = inflater.inflate(R.layout.item_project_log_footer, null);
        footerTv = (TextView) footer.findViewById(R.id.tv_footer);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mNoticeAdapter = new MessageReadAdapter(noticeList);
        mNoticeAdapter.addFooterView(footer);
        mRecyclerView.setAdapter(mNoticeAdapter);
        Bundle bundle = getArguments();
        index = bundle.getInt(Constants.DATA_TAG1);
    }


    @Override
    protected void setListener() {
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG3, noticeList.get(position).getSign_id());
                UIRouter.getInstance().openUri(getActivity(), "DDComp://app/employee/info", bundle, Constants.REQUEST_CODE8);
                //CommonUtil.startActivtiyForResult(mContext, EmployeeInfoActivity.class, Constants.REQUEST_CODE8, bundle);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }


    public void setData(List<Member> readMemberList) {
        noticeList.clear();
        noticeList.addAll(readMemberList);
        footerTv.setText("未读成员" + noticeList.size() + "人");
        mNoticeAdapter.notifyDataSetChanged();
    }
}
