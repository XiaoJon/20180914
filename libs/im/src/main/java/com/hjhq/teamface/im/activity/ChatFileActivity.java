package com.hjhq.teamface.im.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.ChatFileListAdapter;
import com.hjhq.teamface.im.db.DBManager;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.ArrayList;
import java.util.List;

public class ChatFileActivity extends BaseTitleActivity {
    private RecyclerView rvFileList;
    private List<SocketMessage> mFileList = new ArrayList<>();
    private ChatFileListAdapter mAdapter;
    private long conversationId = -1L;
    private EmptyView mEmptyView;

    @Override
    protected int getChildView() {
        return R.layout.chat_file_activity;
    }

    @Override
    protected void initView() {
        super.initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            conversationId = bundle.getLong(MsgConstant.CONVERSATION_ID, -1L);
        }
        rvFileList = (RecyclerView) findViewById(R.id.rv_file_list);
        rvFileList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ChatFileListAdapter(mFileList);
        rvFileList.setAdapter(mAdapter);
        mEmptyView = new EmptyView(this);
        mEmptyView.setEmptyImage(R.drawable.empty_view_img);
        mAdapter.setEmptyView(mEmptyView);
        setActivityTitle("聊天文件");

    }


    @Override
    protected void initData() {
        if (-1L != conversationId) {
            ArrayList<SocketMessage> conversationFile = DBManager.getInstance().getConversationFile(conversationId);
            if (conversationFile != null && conversationFile.size() > 0) {
                mFileList.clear();
                mFileList.addAll(conversationFile);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void setListener() {
        rvFileList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MsgConstant.MSG_DATA, mFileList.get(position));
                UIRouter.getInstance().openUri(mContext, "DDComp://filelib/file_detail", bundle);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // mFileList.get(position).delete();
                adapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onClick(View view) {
    }


}
