package com.hjhq.teamface.im.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.bean.AddSingleChatResponseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.database.PushMessage;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.SearchAssistantAdapter;
import com.hjhq.teamface.im.adapter.SearchChatResultListAdapter;
import com.hjhq.teamface.im.adapter.SearchContactsAdapter;
import com.hjhq.teamface.im.adapter.SearchGroupAdapter;
import com.hjhq.teamface.im.db.DBManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author Administrator
 */
public class SearchChatActivityV2 extends BaseActivity {
    private RecyclerView historyRv;
    private RecyclerView rvChat;
    private RecyclerView rvContacts;
    private RecyclerView rvGroup;
    private RecyclerView rvAssistant;
    private SearchBar mSearchBar;
    private ArrayList<Conversation> chatList = new ArrayList<>();
    private ArrayList<Conversation> assistantList = new ArrayList<>();
    private ArrayList<Conversation> groupList = new ArrayList<>();
    private ArrayList<String> historyItemList = new ArrayList<>();
    private ArrayList<Conversation> rawChatList;
    private TextView clearAll;
    private View footerView;
    private String keyword;
    private SearchChatResultListAdapter chatAdapter;
    private SearchContactsAdapter contactsAdapter;
    private SearchGroupAdapter groupAdapter;
    private SearchAssistantAdapter assistantAdapter;
    HashMap<Conversation, ArrayList<SocketMessage>> chatMap = new HashMap<>();
    HashMap<Conversation, ArrayList<PushMessage>> assistantMap = new HashMap<>();
    HashMap<Conversation, ArrayList<SocketMessage>> groupMap = new HashMap<>();

    ArrayList<Member> contactsList = new ArrayList<>();
    ArrayList<Member> contactsRawList = new ArrayList<>();


    //
    View chatHeadView;
    View chatFooterView;
    View contactsHeadererView;
    View contactsFooterView;
    View groupHeaderView;
    View groupFooterView;
    View assistantHeaderView;
    View assistantFooterView;

    @Override
    protected int getContentView() {
        return R.layout.activity_search_chat_v2;
    }


    @Override
    protected void initView() {
        mSearchBar = (SearchBar) findViewById(R.id.search_bar);
        rvChat = (RecyclerView) findViewById(R.id.rv_chat);
        rvContacts = (RecyclerView) findViewById(R.id.rv_contacts);
        rvGroup = (RecyclerView) findViewById(R.id.rv_group);
        rvAssistant = (RecyclerView) findViewById(R.id.rv_assistant);
        historyRv = (RecyclerView) findViewById(R.id.search_history_recycler_view);
        mSearchBar.setHintText(getString(R.string.im_search_hint));
        mSearchBar.requestTextFocus();
        initAdapter();
    }

    private void initAdapter() {
        rvChat.setLayoutManager(getLayoutManager());
        rvContacts.setLayoutManager(getLayoutManager());
        rvGroup.setLayoutManager(getLayoutManager());
        rvAssistant.setLayoutManager(getLayoutManager());
        historyRv.setLayoutManager(getLayoutManager());
        chatAdapter = new SearchChatResultListAdapter(this, chatList);
        rvChat.setAdapter(chatAdapter);
        contactsAdapter = new SearchContactsAdapter(contactsList);
        rvContacts.setAdapter(contactsAdapter);
        assistantAdapter = new SearchAssistantAdapter(assistantList);
        rvAssistant.setAdapter(assistantAdapter);
        groupAdapter = new SearchGroupAdapter(groupList);
        rvGroup.setAdapter(groupAdapter);
        footerView = getLayoutInflater().inflate(R.layout.item_crm_search_goods_history_footer, null);


        chatHeadView = inflater.inflate(R.layout.item_search_result_header, null);
        chatFooterView = inflater.inflate(R.layout.item_search_result_footer, null);
        contactsHeadererView = inflater.inflate(R.layout.item_search_result_header, null);
        contactsFooterView = inflater.inflate(R.layout.item_search_result_footer, null);
        groupHeaderView = inflater.inflate(R.layout.item_search_result_header, null);
        groupFooterView = inflater.inflate(R.layout.item_search_result_footer, null);
        assistantHeaderView = inflater.inflate(R.layout.item_search_result_header, null);
        assistantFooterView = inflater.inflate(R.layout.item_search_result_footer, null);
        //头部
        TextView chatHeadTitle = chatHeadView.findViewById(R.id.name);
        TextUtil.setText(chatHeadTitle, getString(R.string.im_chat_history));
        TextView contactsHeadTitle = contactsHeadererView.findViewById(R.id.name);
        TextUtil.setText(contactsHeadTitle, "联系人");
        TextView groupHeadTitle = groupHeaderView.findViewById(R.id.name);
        TextUtil.setText(groupHeadTitle, "群聊");
        TextView assistantHeadTitle = assistantHeaderView.findViewById(R.id.name);
        TextUtil.setText(assistantHeadTitle, "小助手");
        //底部
        TextView chatFooterTitle = chatFooterView.findViewById(R.id.name);
        TextUtil.setText(chatFooterTitle, "查看更多");
        TextView contactsFooterTitle = contactsFooterView.findViewById(R.id.name);
        TextUtil.setText(contactsFooterTitle, "查看更多");
        TextView groupFooterTitle = groupFooterView.findViewById(R.id.name);
        TextUtil.setText(groupFooterTitle, "查看更多");
        TextView assistantFooterTitle = assistantFooterView.findViewById(R.id.name);
        TextUtil.setText(assistantFooterTitle, "查看更多");
        //头部
        contactsAdapter.addHeaderView(contactsHeadererView);
        groupAdapter.addHeaderView(groupHeaderView);
        assistantAdapter.addHeaderView(assistantHeaderView);
        chatAdapter.addHeaderView(chatHeadView);
        //底部
        contactsAdapter.addFooterView(contactsFooterView);
        chatAdapter.addFooterView(chatFooterView);
        groupAdapter.addFooterView(groupFooterView);
        assistantAdapter.addFooterView(assistantFooterView);
        contactsFooterView.setOnClickListener(v -> {
            viewDetailResult(MsgConstant.SEARCH_CONTACTS, contactsRawList);
        });
        chatFooterView.setOnClickListener(v -> {
            viewDetailResult(MsgConstant.SEARCH_CHAT, chatMap);
        });
        assistantFooterView.setOnClickListener(v -> {
            viewDetailResult(MsgConstant.SEARCH_ASSISTANT, assistantMap);
        });
        groupFooterView.setOnClickListener(v -> {
            viewDetailResult(MsgConstant.SEARCH_GROUP, groupMap);
        });
    }

    private void viewDetailResult(int type, Serializable data) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, keyword);
        bundle.putInt(Constants.DATA_TAG2, type);
        bundle.putSerializable(Constants.DATA_TAG3, data);
        bundle.putSerializable(Constants.DATA_TAG3, data);
        CommonUtil.startActivtiyForResult(mContext, SearchChatDetailActivity.class, Constants.REQUEST_CODE7, bundle);
    }

    /**
     * 获取LayoutManager
     *
     * @return
     */
    @NonNull
    private LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }

    @Override
    protected void setListener() {
        mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {
                //clearResultData();
            }

            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void search() {
                searchData();
                SoftKeyboardUtils.hide(mSearchBar.getEditText());
            }

            @Override
            public void getText(String text) {
                keyword = text;
                searchData();
            }
        });
        rvChat.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                viewMessage(chatMap, chatList, position);
            }
        });
        rvContacts.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //联系人
                sendChat(position);
                super.onItemClick(adapter, view, position);
            }
        });
        rvAssistant.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //小助手
                viewAssistantDataList(position);
                super.onItemClick(adapter, view, position);
            }
        });
        rvGroup.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //群聊
                viewMessage(groupMap, groupList, position);
                super.onItemClick(adapter, view, position);
            }
        });

    }

    private void viewAssistantDataList(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.DATA_TAG1, assistantMap.get(assistantList.get(position)));
        bundle.putString(MsgConstant.CONVERSATION_ID, (assistantList.get(position).getConversationId() - MsgConstant.DEFAULT_VALUE) + "");
        bundle.putString(MsgConstant.CONV_TITLE, assistantList.get(position).getTitle());
        bundle.putString(MsgConstant.BEAN_NAME, "");
        bundle.putString(Constants.DATA_TAG2, keyword);
        CommonUtil.startActivtiyForResult(mContext, SearchAssistantListActivity.class, Constants.REQUEST_CODE8, bundle);
    }

    /**
     * 查看结果详情
     *
     * @param map
     * @param position
     */
    private void viewMessage(Map<Conversation, ArrayList<SocketMessage>> map, List<Conversation> list, int position) {
        if (map.get(list.get(position)).size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.DATA_TAG1, list.get(position).getConversationId());
            bundle.putSerializable(Constants.DATA_TAG2, map.get(list.get(position)));
            bundle.putSerializable(Constants.DATA_TAG3, list.get(position));
            bundle.putString(Constants.DATA_TAG4, keyword);
            CommonUtil.startActivtiyForResult(SearchChatActivityV2.this,
                    SearchConvChatActivity.class, Constants.REQUEST_CODE1, bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(MsgConstant.CONVERSATION_TAG, list.get(position));
            bundle.putLong(MsgConstant.MSG_ID, -1L);
            bundle.putString(MsgConstant.RECEIVER_ID, list.get(position).getReceiverId() + "");
            bundle.putLong(MsgConstant.CONVERSATION_ID, list.get(position).getConversationId());
            bundle.putString(MsgConstant.CONV_TITLE, list.get(position).getTitle());
            bundle.putInt(MsgConstant.CHAT_TYPE, list.get(position).getConversationType());
            bundle.putLong(MsgConstant.SERVER_TIME, list.get(position).getLastMsgDate());
            CommonUtil.startActivtiy(SearchChatActivityV2.this, ChatActivity.class, bundle);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }

    /**
     * 搜索会话/消息
     */
    private void searchData() {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(keyword.trim())) {
            return;
        }
        //聊天内容
        chatAdapter.setKeyword(keyword);
        chatMap.clear();
        if (TextUtils.isEmpty(keyword) || "".equals(keyword.trim())) {
            chatMap = new HashMap<Conversation, ArrayList<SocketMessage>>();
        } else {
            chatMap = ((HashMap<Conversation, ArrayList<SocketMessage>>) DBManager.getInstance().queryMessage2(keyword));
        }
        chatList.clear();
        rawChatList = new ArrayList<>();
        rawChatList.addAll(chatMap.keySet());
        for (int i = 0; i < rawChatList.size(); i++) {
            rawChatList.get(i).setResultNum(chatMap.get(rawChatList.get(i)).size());
            Log.e("条相关聊天记录 ======", "" + rawChatList.get(i).getResultNum());
        }
        if (rawChatList.size() <= MsgConstant.MAX_SEARCH_RESULT_NUM) {
            chatList.addAll(rawChatList);
            if (chatAdapter.getFooterLayoutCount() > 0) {
                chatAdapter.removeAllFooterView();
            }

        } else {
            chatList.addAll(rawChatList.subList(0, MsgConstant.MAX_SEARCH_RESULT_NUM));
            if (chatAdapter.getFooterLayoutCount() <= 0) {
                chatAdapter.addFooterView(chatFooterView);
            }
        }
        chatAdapter.notifyDataSetChanged();
        if (chatList.size() > 0) {
            rvChat.setVisibility(View.VISIBLE);

        } else {
            rvChat.setVisibility(View.GONE);
        }
        chatAdapter.notifyDataSetChanged();
        //联系人
        contactsAdapter.setKeyword(keyword);
        List<Member> list = DBManager.getInstance().queryMemberByName(keyword);
        final Iterator<Member> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Member member = iterator.next();
            if (SPHelper.getUserId().equals(member.getSign_id())) {
                iterator.remove();
            }
        }
        contactsList.clear();
        contactsRawList.clear();
        contactsRawList.addAll(list);
        if (list != null && list.size() <= MsgConstant.MAX_SEARCH_RESULT_NUM) {
            contactsList.addAll(list);
            if (contactsAdapter.getFooterLayoutCount() > 0) {
                contactsAdapter.removeAllFooterView();
            }
        } else if (list != null && list.size() > MsgConstant.MAX_SEARCH_RESULT_NUM) {
            contactsList.addAll(list.subList(0, MsgConstant.MAX_SEARCH_RESULT_NUM));
            if (contactsAdapter.getFooterLayoutCount() <= 0) {
                contactsAdapter.addFooterView(contactsFooterView);
            }
        }
        contactsAdapter.notifyDataSetChanged();
        if (contactsList.size() > 0) {
            rvContacts.setVisibility(View.VISIBLE);
        } else {
            rvContacts.setVisibility(View.GONE);
        }

        //群组
        groupAdapter.setKeyword(keyword);
        groupMap = (HashMap<Conversation, ArrayList<SocketMessage>>) DBManager.getInstance().queryGroupMessage(keyword);
        groupList.clear();
        List<Conversation> gList = new ArrayList<>();
        gList.addAll(groupMap.keySet());
        if (gList.size() <= MsgConstant.MAX_SEARCH_RESULT_NUM) {
            groupList.addAll(gList);
            if (groupAdapter.getFooterLayoutCount() > 0) {
                groupAdapter.removeAllFooterView();
            }
        } else {
            groupList.addAll(gList.subList(0, MsgConstant.MAX_SEARCH_RESULT_NUM));
            if (groupAdapter.getFooterLayoutCount() <= 0) {
                groupAdapter.addFooterView(groupFooterView);
            }
        }
        groupAdapter.notifyDataSetChanged();
        if (groupList.size() > 0) {
            rvGroup.setVisibility(View.GONE);
        } else {
            rvGroup.setVisibility(View.GONE);
        }
        groupAdapter.notifyDataSetChanged();

        //小助手
        assistantAdapter.setKeyword(keyword);
        assistantMap = (HashMap<Conversation, ArrayList<PushMessage>>) DBManager.getInstance().queryAssistantMessage(keyword);
        List<Conversation> cList = new ArrayList<>();
        cList.addAll(assistantMap.keySet());
        assistantList.clear();
        if (cList.size() <= MsgConstant.MAX_SEARCH_RESULT_NUM) {
            assistantList.addAll(cList);
            if (assistantAdapter.getFooterLayoutCount() > 0) {
                assistantAdapter.removeAllFooterView();
            }
        } else {
            assistantList.addAll(cList.subList(0, MsgConstant.MAX_SEARCH_RESULT_NUM));
            if (assistantAdapter.getFooterLayoutCount() <= 0) {
                assistantAdapter.addFooterView(assistantFooterView);

            }
        }
        assistantAdapter.notifyDataSetChanged();
        if (assistantMap.size() > 0) {
            rvAssistant.setVisibility(View.VISIBLE);

        } else {
            rvAssistant.setVisibility(View.GONE);

        }
        assistantAdapter.notifyDataSetChanged();
    }

    private void clearResultData() {
        chatMap.clear();
        chatList.clear();
        chatAdapter.notifyDataSetChanged();
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

    /**
     * 发起聊天
     */
    private void sendChat(int position) {
        ImLogic.getInstance().saveRecentContact(contactsList.get(position));
        //如果会话为空，使用EventBus通知会话列表添加新会话
        ImLogic.getInstance().addSingleChat(SearchChatActivityV2.this,
                contactsList.get(position).getSign_id(), new ProgressSubscriber<AddSingleChatResponseBean>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(AddSingleChatResponseBean bean) {
                        super.onNext(bean);
                        Bundle bundle = new Bundle();
                        ImLogic.getInstance().saveRecentContact(contactsList.get(position));
                        Conversation conversation = new Conversation();
                        conversation.setConversationId(TextUtil.parseLong(bean.getData().getId()));
                        conversation.setCompanyId(bean.getData().getId() + "");
                        conversation.setOneselfIMID(SPHelper.getUserId());
                        conversation.setReceiverId(bean.getData().getReceiver_id());
                        conversation.setConversationType(MsgConstant.SINGLE);
                        conversation.setTitle(bean.getData().getEmployee_name());
                        conversation.setIsHide(1);
                        conversation.setAvatarUrl(bean.getData().getPicture());
                        conversation.setSenderAvatarUrl(bean.getData().getPicture());
                        bundle.putSerializable(MsgConstant.CONVERSATION_TAG, conversation);
                        bundle.putString(MsgConstant.RECEIVER_ID, bean.getData().getReceiver_id());
                        bundle.putLong(MsgConstant.CONVERSATION_ID, TextUtil.parseLong(bean.getData().getId()));
                        bundle.putString(MsgConstant.CONV_TITLE, bean.getData().getEmployee_name());
                        bundle.putInt(MsgConstant.CHAT_TYPE, bean.getData().getChat_type());
                        CommonUtil.startActivtiy(SearchChatActivityV2.this, ChatActivity.class, bundle);
                    }
                });
    }
}
