package com.hjhq.teamface.im.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Administrator
 */
public class SearchChatDetailActivity extends BaseActivity {

    private RecyclerView rvContacts;
    private SearchBar mSearchBar;
    private List<Conversation> chatList = new ArrayList<>();
    private List<Conversation> assistantList = new ArrayList<>();
    private List<Conversation> groupList = new ArrayList<>();
    private String keyword;
    private int type;
    private BaseQuickAdapter mAdapter;
    private HashMap<Conversation, ArrayList<SocketMessage>> chatMap = new HashMap<>();
    private HashMap<Conversation, ArrayList<PushMessage>> assistantMap = new HashMap<>();
    private Map<Conversation, ArrayList<SocketMessage>> groupMap = new HashMap<>();
    private List<Member> contactsList = new ArrayList<>();
    private TextView title;
    private View chatHeadView;
    private String[] typeMenu;
    private Bundle mBundle;


    @Override
    protected int getContentView() {
        return R.layout.activity_search_chat_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mBundle = savedInstanceState;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mBundle != null) {
            super.onSaveInstanceState(mBundle);
        } else {
            super.onSaveInstanceState(outState);
        }

    }

    @Override
    protected void initView() {
        mSearchBar = (SearchBar) findViewById(R.id.search_bar);
        rvContacts = (RecyclerView) findViewById(R.id.rv_contacts);
        // mSearchBar.requestTextFocus();
    }

    private void initAdapter() {
        rvContacts.setLayoutManager(new LinearLayoutManager(mContext));
        chatHeadView = inflater.inflate(R.layout.item_search_result_header, null);
        title = (TextView) chatHeadView.findViewById(R.id.name);
        title.setText(typeMenu[type - 1]);
        rvContacts.setAdapter(mAdapter);
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
        rvContacts.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                switch (type) {
                    case MsgConstant.SEARCH_CONTACTS:
                        //联系人
                        sendChat(position);
                        break;
                    case MsgConstant.SEARCH_GROUP:
                        //群组
                        viewMessage(groupMap, groupList, position);
                        break;
                    case MsgConstant.SEARCH_CHAT:
                        //聊天记录
                        viewMessage(chatMap, chatList, position);
                        break;
                    case MsgConstant.SEARCH_ASSISTANT:
                        //小助手
                        viewAssistantDataList(position);
                        break;
                    default:
                        break;
                }
                super.onItemClick(adapter, view, position);
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
        SoftKeyboardUtils.hide(mSearchBar.getEditText());
        switch (type) {
            case MsgConstant.SEARCH_CONTACTS:
                //联系人
                contactsList.clear();
                contactsList = DBManager.getInstance().queryMemberByName(keyword);
                break;
            case MsgConstant.SEARCH_GROUP:
                //群组
                groupMap = DBManager.getInstance().queryGroupMessage(keyword);
                groupList.clear();
                groupList.addAll(groupMap.keySet());
                break;
            case MsgConstant.SEARCH_CHAT:
                //聊天记录
                chatMap = ((HashMap<Conversation, ArrayList<SocketMessage>>) DBManager.getInstance().queryMessage2(keyword));
                chatList.clear();
                chatList.addAll(chatMap.keySet());
                break;
            case MsgConstant.SEARCH_ASSISTANT:
                //小助手
                assistantMap = (HashMap<Conversation, ArrayList<PushMessage>>) DBManager.getInstance().queryAssistantMessage(keyword);
                assistantList.clear();
                assistantList.addAll(assistantMap.keySet());
                break;
            default:
                break;
        }
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 清空搜索结果
     */
    private void clearResultData() {

    }

    @Override
    protected void initData() {
        typeMenu = getResources().getStringArray(R.array.im_search_type_menu);
        if (mBundle == null) {
            mBundle = getIntent().getExtras();
        }
        if (mBundle != null) {
            keyword = mBundle.getString(Constants.DATA_TAG1);
            type = mBundle.getInt(Constants.DATA_TAG2);
            switch (type) {
                case MsgConstant.SEARCH_CONTACTS:
                    contactsList = (ArrayList<Member>) mBundle.getSerializable(Constants.DATA_TAG3);
                    mAdapter = new SearchContactsAdapter(contactsList);
                    break;
                case MsgConstant.SEARCH_GROUP:
                    groupMap = (Map<Conversation, ArrayList<SocketMessage>>) mBundle.getSerializable(Constants.DATA_TAG3);
                    if (groupMap != null) {
                        groupList.addAll(groupMap.keySet());
                    }
                    mAdapter = new SearchGroupAdapter(groupList);
                    break;
                case MsgConstant.SEARCH_CHAT:
                    chatMap = (HashMap<Conversation, ArrayList<SocketMessage>>) mBundle.getSerializable(Constants.DATA_TAG3);
                    if (chatMap != null) {
                        chatList.addAll(chatMap.keySet());
                    }
                    mAdapter = new SearchChatResultListAdapter(this, chatList);
                    break;
                case MsgConstant.SEARCH_ASSISTANT:
                    assistantMap = (HashMap<Conversation, ArrayList<PushMessage>>) mBundle.getSerializable(Constants.DATA_TAG3);
                    if (assistantMap != null) {
                        assistantList.addAll(assistantMap.keySet());
                    }
                    mAdapter = new SearchAssistantAdapter(assistantList);
                    break;
                default:
                    break;
            }
            initAdapter();
            mSearchBar.setHintText(typeMenu[type - 1]);
            mSearchBar.setText(keyword, false);
        }
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
        ImLogic.getInstance().addSingleChat(SearchChatDetailActivity.this,
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
                        CommonUtil.startActivtiy(SearchChatDetailActivity.this, ChatActivity.class, bundle);
                    }
                });
    }

    /**
     * 查看结果详情
     *
     * @param map
     * @param position
     */
    private void viewMessage(Map<Conversation, ArrayList<SocketMessage>> map, List<Conversation> list, int position) {
        if (list.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.DATA_TAG1, list.get(position).getConversationId());
            bundle.putSerializable(Constants.DATA_TAG2, map.get(list.get(position)));
            bundle.putSerializable(Constants.DATA_TAG3, list.get(position));
            bundle.putString(Constants.DATA_TAG4, keyword);
            CommonUtil.startActivtiyForResult(SearchChatDetailActivity.this,
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
            CommonUtil.startActivtiy(SearchChatDetailActivity.this, ChatActivity.class, bundle);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }

    /**
     * 查看小助手搜索结果
     *
     * @param position
     */
    private void viewAssistantDataList(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.DATA_TAG1, assistantMap.get(assistantList.get(position)));
        bundle.putString(MsgConstant.CONVERSATION_ID, (assistantList.get(position).getConversationId() - MsgConstant.DEFAULT_VALUE) + "");
        bundle.putString(MsgConstant.CONV_TITLE, assistantList.get(position).getTitle());
        bundle.putString(MsgConstant.BEAN_NAME, "");
        bundle.putString(Constants.DATA_TAG2, keyword);
        CommonUtil.startActivtiyForResult(mContext, SearchAssistantListActivity.class, Constants.REQUEST_CODE8, bundle);
    }

}
