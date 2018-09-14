package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.ChooseGroupChatAdapter;
import com.hjhq.teamface.im.bean.GetGroupListBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author Administrator
 */
public class SearchGroupChatActivity extends BaseActivity {
    private RecyclerView historyRv;
    private RecyclerView resultRv;
    private SearchBar mSearchBar;
    private List<GetGroupListBean.DataBean> resultList = new ArrayList<>();
    private ArrayList<GetGroupListBean.DataBean> orginData = new ArrayList<>();
    private List<String> historyItemList = new ArrayList<>();
    private EmptyView emptyView;
    private EmptyView emptyView2;
    private TextView clearAll;
    private View footerView;
    private String keyword;
    private ChooseGroupChatAdapter mSearchResultListAdapter;
    private HashMap<Conversation, ArrayList<SocketMessage>> map = new HashMap<>();
    private boolean chooseMember = false;
    private String chooseTag = "";


    @Override
    protected int getContentView() {
        return R.layout.search_chat_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orginData = (ArrayList<GetGroupListBean.DataBean>) bundle.getSerializable(Constants.DATA_TAG1);
            chooseMember = bundle.getBoolean(Constants.DATA_TAG2);
            chooseTag = bundle.getString(Constants.DATA_TAG3);
        }
    }

    @Override
    protected void initView() {
        mSearchBar = (SearchBar) findViewById(R.id.search_bar);
        resultRv = (RecyclerView) findViewById(R.id.search_result_recycler_view);
        historyRv = (RecyclerView) findViewById(R.id.search_history_recycler_view);
        resultRv.setLayoutManager(new LinearLayoutManager(this));
        historyRv.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultListAdapter = new ChooseGroupChatAdapter(resultList);
        resultRv.setAdapter(mSearchResultListAdapter);
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
        mSearchResultListAdapter.setEmptyView(emptyView);

    }

    @Override
    protected void setListener() {
        mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {

            }

            @Override
            public void cancel() {
                if (MsgConstant.CHOOSE_GROUP_CHAT.equals(chooseTag)) {
                    setResult(Activity.RESULT_OK);
                }
                finish();
            }

            @Override
            public void search() {

                emptyView.setEmptyTitle(getString(R.string.im_searching));
                searchGroup();


            }

            @Override
            public void getText(String text) {
                keyword = text;
                /*if (TextUtils.isEmpty(keyword)) {
                    resultList.clear();
                    mSearchResultListAdapter.notifyDataSetChanged();
                    return;
                }
                searchGroup();*/

            }
        });
        resultRv.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (chooseMember) {
                    sendFileToGroup(position);
                } else {
                    openGroupChat(position);
                }

            }
        });
    }

    /**
     * 打开群聊
     *
     * @param position
     */
    private void openGroupChat(int position) {
        if (chooseMember) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        Conversation conversation = new Conversation();
        conversation.setCompanyId(SPHelper.getCompanyId());
        conversation.setOneselfIMID(SPHelper.getUserId());
        conversation.setReceiverId(resultList.get(position).getId() + "");
        conversation.setConversationType(MsgConstant.GROUP);
        conversation.setTitle(resultList.get(position).getName());
        conversation.setPeoples(resultList.get(position).getPeoples());
        conversation.setConversationId(resultList.get(position).getId());
        try {
            conversation.setIsHide(Integer.parseInt(resultList.get(position).getIs_hide()));
        } catch (NumberFormatException e) {
            conversation.setIsHide(0);
        }
        // TODO: 2017/12/14 群头像
        conversation.setAvatarUrl(resultList.get(position).getNotice());
        Bundle bundle = new Bundle();
        bundle.putSerializable(MsgConstant.CONVERSATION_TAG, conversation);
        bundle.putString(MsgConstant.RECEIVER_ID, resultList.get(position).getId() + "");
        bundle.putLong(MsgConstant.CONVERSATION_ID, resultList.get(position).getId());
        bundle.putString(MsgConstant.CONV_TITLE, resultList.get(position).getName());
        bundle.putInt(MsgConstant.CHAT_TYPE, MsgConstant.GROUP);
        CommonUtil.startActivtiy(mContext, ChatActivity.class, bundle);


    }

    /**
     * 文件库发送文件-选择群
     *
     * @param position
     */
    private void sendFileToGroup(int position) {
        Conversation conversation = new Conversation();
        conversation.setCompanyId(SPHelper.getCompanyId());
        conversation.setOneselfIMID(SPHelper.getUserId());
        conversation.setReceiverId(resultList.get(position).getId() + "");
        conversation.setConversationType(MsgConstant.GROUP);
        conversation.setTitle(resultList.get(position).getName());
        conversation.setPeoples(resultList.get(position).getPeoples());
        conversation.setConversationId(resultList.get(position).getId());
        conversation.setSenderAvatarUrl(IM.getInstance().getavatar());
        conversation.setAvatarUrl("");
        conversation.setSenderName(IM.getInstance().getName());
        EventBusUtils.sendEvent(new MessageBean(Constants.REQUEST_CODE5, MsgConstant.RESEND_MEMBER_TAG, conversation));
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void searchGroup() {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(keyword.trim())) {
            return;
        }
        resultList.clear();
        mSearchResultListAdapter.notifyDataSetChanged();
        if (orginData != null) {
            for (int i = 0; i < orginData.size(); i++) {
                if (!TextUtils.isEmpty(orginData.get(i).getName()) && orginData.get(i).getName().contains(keyword)) {
                    resultList.add(orginData.get(i));
                }

            }
            mSearchResultListAdapter.notifyDataSetChanged();
            emptyView.setEmptyTitle(getString(R.string.im_no_search_result_group));
        }


    }

    @Override
    protected void initData() {

    }


    public void dismissSoftInputAndShowMenu() {

    }


    @Override
    public void onClick(View v) {

    }
}
