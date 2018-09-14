package com.hjhq.teamface.im.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.SearchChatResultListAdapter;
import com.hjhq.teamface.im.db.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author Administrator
 */
public class SearchChatActivity extends BaseActivity {
    private RecyclerView historyRv;
    private RecyclerView resultRv;
    private SearchBar mSearchBar;
    private List<Conversation> resultList = new ArrayList<>();
    private List<String> historyItemList = new ArrayList<>();
    private EmptyView emptyView;
    private EmptyView emptyView2;
    private TextView clearAll;
    private View footerView;
    private String keyword;
    private SearchChatResultListAdapter mSearchChatResultListAdapter;
    HashMap<Conversation, ArrayList<SocketMessage>> map = new HashMap<>();
    private TextView title;
    private TextView num;

    @Override
    protected int getContentView() {
        return R.layout.search_chat_activity;
    }


    @Override
    protected void initView() {
        mSearchBar = (SearchBar) findViewById(R.id.search_bar);
        resultRv = (RecyclerView) findViewById(R.id.search_result_recycler_view);
        historyRv = (RecyclerView) findViewById(R.id.search_history_recycler_view);
        resultRv.setLayoutManager(new LinearLayoutManager(this));
        historyRv.setLayoutManager(new LinearLayoutManager(this));
        mSearchChatResultListAdapter = new SearchChatResultListAdapter(this, resultList);
        resultRv.setAdapter(mSearchChatResultListAdapter);
        emptyView = new EmptyView(this);
        emptyView.setEmptyTitle(getString(R.string.im_search_hint));
        emptyView.setEmptyImage(R.drawable.icon_no_search_result);
        emptyView2 = new EmptyView(this);
        emptyView2.setEmptyTitle(getString(R.string.im_search_hint));
        emptyView2.setEmptyImage(R.drawable.icon_no_search_result);
        footerView = getLayoutInflater().inflate(R.layout.item_crm_search_goods_history_footer, null);
        clearAll = (TextView) footerView.findViewById(R.id.tv_clean_history_search_log);
        mSearchBar.setHintText(getString(R.string.im_search_hint));
        mSearchBar.requestTextFocus();
        View headerView = inflater.inflate(R.layout.item_search_result_header, null);
        title = (TextView) headerView.findViewById(R.id.name);
        num = (TextView) headerView.findViewById(R.id.num);
        title.setText(getString(R.string.im_chat_history));
        num.setVisibility(View.GONE);
        mSearchChatResultListAdapter.setEmptyView(emptyView);
        mSearchChatResultListAdapter.addHeaderView(headerView);

    }

    @Override
    protected void setListener() {
        mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {
                clearResultData();
            }

            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void search() {
                searchChat();

            }

            @Override
            public void getText(String text) {
                keyword = text;

            }
        });
        resultRv.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (map.get(((Conversation) adapter.getData().get(position))).size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constants.DATA_TAG1, ((Conversation) adapter.getData().get(position)).getConversationId());
                    bundle.putSerializable(Constants.DATA_TAG2, map.get(((Conversation) adapter.getData().get(position))));
                    bundle.putSerializable(Constants.DATA_TAG3, (Conversation) adapter.getData().get(position));
                    bundle.putString(Constants.DATA_TAG4, keyword);
                    CommonUtil.startActivtiyForResult(SearchChatActivity.this,
                            SearchConvChatActivity.class, Constants.REQUEST_CODE1, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MsgConstant.CONVERSATION_TAG, (Conversation) adapter.getData().get(position));
                    bundle.putLong(MsgConstant.MSG_ID, -1L);
                    bundle.putString(MsgConstant.RECEIVER_ID, ((Conversation) adapter.getData().get(position)).getReceiverId() + "");
                    bundle.putLong(MsgConstant.CONVERSATION_ID, ((Conversation) adapter.getData().get(position)).getConversationId());
                    bundle.putString(MsgConstant.CONV_TITLE, ((Conversation) adapter.getData().get(position)).getTitle());
                    bundle.putInt(MsgConstant.CHAT_TYPE, ((Conversation) adapter.getData().get(position)).getConversationType());
                    bundle.putLong(MsgConstant.SERVER_TIME, ((Conversation) adapter.getData().get(position)).getLastMsgDate());
                    CommonUtil.startActivtiy(SearchChatActivity.this, ChatActivity.class, bundle);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }

            }
        });
    }

    /**
     * 搜索会话/消息
     */
    private void searchChat() {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(keyword.trim())) {
            return;
        }
        mSearchChatResultListAdapter.setKeyword(keyword);
        map.clear();
        emptyView.setEmptyTitle(getString(R.string.im_searching));
        if (TextUtils.isEmpty(keyword) || "".equals(keyword.trim())) {
            map = new HashMap<Conversation, ArrayList<SocketMessage>>();
        } else {
            map = ((HashMap<Conversation, ArrayList<SocketMessage>>) DBManager.getInstance().queryMessage2(keyword));
        }
        resultList.clear();
        resultList.addAll(map.keySet());
        mSearchChatResultListAdapter.notifyDataSetChanged();
        if (TextUtils.isEmpty(keyword)) {
            emptyView.setEmptyTitle(getString(R.string.im_search_hint));
        } else {
            emptyView.setEmptyTitle(getString(R.string.im_no_search_result));
        }
    }

    private void clearResultData() {
        map.clear();
        resultList.clear();
        mSearchChatResultListAdapter.notifyDataSetChanged();
        emptyView.setEmptyTitle(getString(R.string.im_search_hint));
    }

    @Override
    protected void initData() {

    }


    public void dismissSoftInputAndShowMenu() {

    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    public void onClick(View v) {

    }
}
