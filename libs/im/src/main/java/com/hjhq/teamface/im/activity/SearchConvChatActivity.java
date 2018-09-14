package com.hjhq.teamface.im.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.SearchConvResultListAdapter;
import com.hjhq.teamface.im.db.DBManager;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Administrator
 */
public class SearchConvChatActivity extends BaseActivity {
    private RecyclerView historyRv;
    private RecyclerView resultRv;
    private SearchBar mSearchBar;
    private List<Conversation> resultList = new ArrayList<>();
    private List<SocketMessage> messageList = new ArrayList<>();
    private List<String> historyItemList = new ArrayList<>();
    private EmptyView emptyView;
    private EmptyView emptyView2;
    private TextView clearAll;
    private TextView title;
    private TextView num;
    private ImageView ivBack;
    private View footerView;
    private String keyword = "";
    private SearchConvResultListAdapter mSearchResultListAdapter;
    private long conversationId;
    private Conversation mConversation;

    @Override
    protected int getContentView() {
        return R.layout.activity_search_conv_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        mSearchBar = (SearchBar) findViewById(R.id.search_bar);
        resultRv = (RecyclerView) findViewById(R.id.search_result_recycler_view);
        historyRv = (RecyclerView) findViewById(R.id.search_history_recycler_view);
        ivBack = (ImageView) findViewById(R.id.back);
        resultRv.setLayoutManager(new LinearLayoutManager(this));
        historyRv.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultListAdapter = new SearchConvResultListAdapter(this, messageList);
        resultRv.setAdapter(mSearchResultListAdapter);
        emptyView = new EmptyView(this);
        emptyView.setEmptyTitle(getString(R.string.im_no_search_result));
        emptyView.setEmptyImage(R.drawable.icon_no_search_result);
        emptyView2 = new EmptyView(this);
        emptyView2.setEmptyTitle("没有搜索记录~");
        footerView = getLayoutInflater().inflate(R.layout.item_crm_search_goods_history_footer, null);
        clearAll = (TextView) footerView.findViewById(R.id.tv_clean_history_search_log);
        title = (TextView) findViewById(R.id.name);
        num = (TextView) findViewById(R.id.num);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            conversationId = bundle.getLong(Constants.DATA_TAG1);
            messageList.addAll((ArrayList<SocketMessage>) bundle.getSerializable(Constants.DATA_TAG2));
            mConversation = (Conversation) bundle.getSerializable(Constants.DATA_TAG3);
            keyword = bundle.getString(Constants.DATA_TAG4);
            mSearchResultListAdapter.notifyDataSetChanged();
        }
        if (mConversation != null) {
            title.setText(mConversation.getTitle());
            mSearchBar.setHintText(mConversation.getTitle());
        }
        if (messageList != null) {
            num.setText(messageList.size() + "条");
        }
        mSearchBar.setText(keyword, false);
        mSearchResultListAdapter.setEmptyView(emptyView);
        mSearchBar.setHintText(getString(R.string.im_search_hint));
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(v -> {
            finish();
        });
        resultRv.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MsgConstant.CONVERSATION_TAG, mConversation);
                bundle.putLong(MsgConstant.MSG_ID, messageList.get(position).getMsgID());
                bundle.putString(MsgConstant.RECEIVER_ID, mConversation.getReceiverId() + "");
                bundle.putLong(MsgConstant.CONVERSATION_ID, mConversation.getConversationId());
                bundle.putString(MsgConstant.CONV_TITLE, mConversation.getTitle());
                bundle.putInt(MsgConstant.CHAT_TYPE, mConversation.getConversationType());
                bundle.putLong(MsgConstant.SERVER_TIME, messageList.get(position).getServerTimes());
                CommonUtil.startActivtiy(SearchConvChatActivity.this, ChatActivity.class, bundle);
            }
        });
        mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {
               /* mSearchResultListAdapter.getData().clear();
                mSearchResultListAdapter.notifyDataSetChanged();*/
            }

            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void search() {
                searchChat();
                SoftKeyboardUtils.hide(mSearchBar.getEditText());
            }

            @Override
            public void getText(String text) {
                keyword = text;
                searchChat();
            }
        });
    }

    private void searchChat() {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(keyword.trim())) {
            return;
        }
        ArrayList<SocketMessage> list = DBManager.getInstance().queryMessage(conversationId, keyword);
        num.setText(list.size() + "条");
        messageList.clear();
        messageList.addAll(list);
        mSearchResultListAdapter.notifyDataSetChanged();
    }


    public void dismissSoftInputAndShowMenu() {

    }


    @Override
    public void onClick(View v) {


    }
}
