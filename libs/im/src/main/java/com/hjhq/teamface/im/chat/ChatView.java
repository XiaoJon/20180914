package com.hjhq.teamface.im.chat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.SharePreferenceManager;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmoticonsEditText;
import com.hjhq.teamface.common.view.RecordVoiceButton;
import com.hjhq.teamface.emoji.Emoji;
import com.hjhq.teamface.emoji.EmojiUtil;
import com.hjhq.teamface.emoji.FaceFragment;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.activity.ChatActivity;
import com.hjhq.teamface.im.activity.GroupMemberActivity;
import com.hjhq.teamface.im.adapter.MessageListAdapter;
import com.hjhq.teamface.im.bean.GroupChatInfoBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatView extends RelativeLayout implements FaceFragment.OnEmojiClickListener {

    private RelativeLayout mBackground;
    private TableLayout mMoreMenuTl;
    private RecyclerView mChatListView;
    private ImageButton mReturnBtn;
    private ImageButton mRightBtn;
    private TextView mChatTitle;
    private TextView mGroupNumTv;
    private RecordVoiceButton mVoiceBtn;
    //public EditText mChatInputEt;
    public EmoticonsEditText mChatInputEt;
    private ImageView mSwitchIb;
    private ImageView mExpressionIb;
    private ImageView mAddFileIb;
    private ImageButton mTakePhotoIb;
    private ImageButton mPickPictureIb;
    private ImageButton mLocationIb;
    private ImageButton mCallPhoneIb;
    private LinearLayout mCallPhoneLl;
    private LinearLayout mLlAt;
    private ImageButton ibAt;
    private LinearLayout mSendMsgLl;
    private Button mSendMsgBtn;
    private FrameLayout mContainer;
    //新来的未查看的消息数量
    private TextView tvNewMsgCount;
    //刚进入会话时未读数量
    private TextView tvUnreadMsgCount;
    //添加@
    private TextView tvAt;
    private ImageView ivAt;

    Context mContext;
    private OnSizeChangedListener mListener;
    private OnKeyBoardChangeListener mKeyboardListener;
    ChatActivity chatActivity;


    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;
    private boolean mHasInit;
    private boolean mHasKeybord;
    private int mHeight;
    private boolean showAtIcon = true;
    private ArrayList<Member> mAllMemberList = new ArrayList<Member>();
    private List<GroupChatInfoBean.DataBean.EmployeeInfoBean> mListBeen = new ArrayList<>();
    private List<Member> atList = new ArrayList<>();
    private List<Member> forDel = new ArrayList<>();

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }


    public void initModule(float density, int densityDpi) {
        mChatTitle = (TextView) findViewById(R.id.jmui_title);
        if (densityDpi <= 160) {
            mChatTitle.setMaxWidth((int) (180 * density + 0.5f));
        } else if (densityDpi <= 240) {
            mChatTitle.setMaxWidth((int) (190 * density + 0.5f));
        } else {
            mChatTitle.setMaxWidth((int) (200 * density + 0.5f));
        }
        mReturnBtn = (ImageButton) findViewById(R.id.jmui_return_btn);
        mContainer = (FrameLayout) findViewById(R.id.container);
        tvNewMsgCount = (TextView) findViewById(R.id.tv_new_msg_count);
        tvUnreadMsgCount = (TextView) findViewById(R.id.tv_unread_msg_count);
        tvAt = (TextView) findViewById(R.id.tv_at);
        ivAt = (ImageView) findViewById(R.id.iv_at);
        mChatListView = (RecyclerView) findViewById(R.id.jmui_chat_list);
        ((DefaultItemAnimator) mChatListView.getItemAnimator()).setSupportsChangeAnimations(false);

        mVoiceBtn = (RecordVoiceButton) findViewById(R.id.jmui_voice_btn);

        mChatInputEt = (EmoticonsEditText) findViewById(R.id.jmui_chat_input_et);
        mChatInputEt.setFocusable(true);
        mChatInputEt.requestFocus();
        mSwitchIb = (ImageView) findViewById(R.id.jmui_switch_voice_ib);
        mAddFileIb = (ImageView) findViewById(R.id.jmui_add_file_btn);
        mTakePhotoIb = (ImageButton) findViewById(R.id.jmui_pick_from_camera_btn);
        mPickPictureIb = (ImageButton) findViewById(R.id.jmui_pick_from_local_btn);
        mSendMsgBtn = (Button) findViewById(R.id.jmui_send_msg_btn);
        mBackground = (RelativeLayout) findViewById(R.id.jmui_chat_background);
        mMoreMenuTl = (TableLayout) findViewById(R.id.jmui_more_menu_tl);
        mRightBtn = (ImageButton) findViewById(R.id.jmui_right_btn);
        mGroupNumTv = (TextView) findViewById(R.id.jmui_group_num_tv);
        mExpressionIb = (ImageView) findViewById(R.id.jmui_expression_btn);
        mLocationIb = (ImageButton) findViewById(R.id.jmui_send_location_btn);
        mCallPhoneIb = (ImageButton) findViewById(R.id.jmui_phone_btn);
        mCallPhoneLl = (LinearLayout) findViewById(R.id.jmui_phone_ll);
        mLlAt = (LinearLayout) findViewById(R.id.ll_at);
        ibAt = (ImageButton) findViewById(R.id.ib_at);
        mSendMsgLl = (LinearLayout) findViewById(R.id.jmui_send_msg_layout);
        FaceFragment faceFragment = FaceFragment.Instance();
        faceFragment.setListener(this);
        ((ChatActivity) mContext).getSupportFragmentManager().beginTransaction().add(R.id.container, faceFragment).commit();
        //mBackground.requestFocus();
        mChatInputEt.addTextChangedListener(watcher);

        mChatInputEt.setOnTouchListener((v, event) -> {
            Runnable run2 = new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        View rootview = chatActivity.getWindow().getDecorView();
                        View aaa = rootview.findFocus();
                        Log.i("获取焦点", aaa.toString());
                    }

                }
            };
            run2.run();
            showSoftInputFromWindow(mContext, mChatInputEt);
            mContainer.setVisibility(GONE);
            mChatInputEt.requestFocus();
            SoftKeyboardUtils.show(mChatInputEt);
            mMoreMenuTl.setVisibility(GONE);
            scrollToBottom();
            if (!mChatInputEt.isFocused()) {
                mChatInputEt.setFocusable(true);
                mChatInputEt.requestFocus();
                mChatInputEt.setFocusableInTouchMode(true);
                focusToInput(mChatInputEt.hasFocus());
            }
            return false;
        });
        mChatInputEt.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mChatInputEt, InputMethodManager.SHOW_IMPLICIT);
                SoftKeyboardUtils.show(mChatInputEt);
                mMoreMenuTl.setVisibility(GONE);

                scrollToBottom();

            } else {
                SoftKeyboardUtils.show(mChatInputEt);
                mMoreMenuTl.setVisibility(GONE);
            }
        });
        mChatInputEt.setOnFocusChangeListener(listener);
        mChatInputEt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mChatInputEt.setSingleLine(false);
        mChatInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    dismissMoreMenu();
                    Log.i("ChatView", "dismissMoreMenu()----------");
                }
                return false;
            }
        });
        mChatInputEt.setMaxLines(4);
        setMoreMenuHeight();
    }

    public void setMoreMenuHeight() {
        int softKeyboardHeight = SharePreferenceManager.getCachedKeyboardHeight();
        if (softKeyboardHeight > 0) {
            mMoreMenuTl.setLayoutParams(new LinearLayout
                    .LayoutParams(LayoutParams.MATCH_PARENT, softKeyboardHeight));
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mListener != null) {
            mListener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mHasInit) {
            mHasInit = true;
            mHeight = b;
            if (mKeyboardListener != null) {
                mKeyboardListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
            }
        } else {
            mKeyboardListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
            mHeight = mHeight < b ? b : mHeight;
        }
        if (mHasInit && mHeight > b) {
            mHasKeybord = true;
            if (mKeyboardListener != null) {
                mKeyboardListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW);
            }
        }
        if (mHasInit && mHasKeybord && mHeight == b) {
            mHasKeybord = false;
            if (mKeyboardListener != null) {
                mKeyboardListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE);
            }
        }
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        this.mListener = listener;
    }

    public void dismissGroupNum() {
        mGroupNumTv.setVisibility(View.GONE);
    }

    public void setInputText(String text) {
        mChatInputEt.setText(text);
    }

    public void setAtIcon(boolean show) {
        showAtIcon = show;
    }

    public void showSoftInputFromWindow(Context context, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        chatActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        scrollToBottom();

    }

    /**
     * 显示最新消息
     */
    public void scrollToBottom() {
        if (chatActivity.getDataSize() > 0) {
            mChatListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getListView().scrollToPosition(chatActivity.getDataSize() - 1);
                }
            }, 300);

        }
    }

    public void initActivity(ChatActivity activity) {
        this.chatActivity = activity;
    }

    public void showEmoji() {
        mAddFileIb.setImageResource(R.drawable.ic_more);
        if (mContainer.getVisibility() == VISIBLE) {
            mContainer.setVisibility(GONE);
            mExpressionIb.setImageResource(R.drawable.icon_emoji);
            SoftKeyboardUtils.show(mChatInputEt);
            mMoreMenuTl.setVisibility(GONE);
            mContainer.setVisibility(GONE);
            mAddFileIb.setImageResource(R.drawable.ic_more);
            scrollToBottom();
        } else {
            SoftKeyboardUtils.hide(mChatInputEt);
            mMoreMenuTl.setVisibility(View.GONE);
            mContainer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mContainer.setVisibility(VISIBLE);
                    mExpressionIb.setImageResource(R.drawable.ic_keyboard);
                }
            }, 50);
        }
    }

    @Override
    public void onEmojiDelete() {
        String text = mChatInputEt.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                mChatInputEt.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                displayTextView();
                return;
            }
            mChatInputEt.getText().delete(index, text.length());
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        mChatInputEt.onKeyDown(KeyEvent.KEYCODE_DEL, event);
        displayTextView();
    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        Editable editable = mChatInputEt.getEditableText();
        if (emoji != null) {
            int index = mChatInputEt.getSelectionStart();

            if (index < 0) {
                editable.append(emoji.getContent());
            } else {
                editable.insert(index, emoji.getContent());
            }
        }
        displayTextView();
//        mChatInputEt.setText(editable);
    }

    private void displayTextView() {
        try {
            EmojiUtil.handlerEmojiText(mChatInputEt, mChatInputEt.getText().toString(), mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入聊天时显示未读消息数
     *
     * @param newMessageNum
     */
    public void setNewMessageCount(int newMessageNum) {
        //  tvNewMsgCount.setVisibility(VISIBLE);
        tvUnreadMsgCount.setVisibility(GONE);
        TextUtil.setText(tvNewMsgCount, newMessageNum + "条新消息");
        if (newMessageNum <= 0) {
            tvNewMsgCount.setVisibility(GONE);
        }
    }

    /**
     * 显示未读提示
     *
     * @param unreadMsgCount
     */
    public void showUnreadMessageNum(int unreadMsgCount) {
        if (unreadMsgCount > 0) {
            tvUnreadMsgCount.setVisibility(VISIBLE);
            TextUtil.setText(tvUnreadMsgCount, unreadMsgCount + "条新消息");
        }

    }

    /**
     * 隐藏未读提示
     */
    public void hideUnreadMessageNum() {
        if (View.VISIBLE == tvUnreadMsgCount.getVisibility()) {
            tvUnreadMsgCount.setVisibility(GONE);
        }
    }

    /**
     * 隐藏新消息数提示无法可说
     */
    public void hideNewMessageNum() {
        if (View.VISIBLE == tvNewMsgCount.getVisibility()) {
            tvNewMsgCount.setVisibility(GONE);
        }
    }

    public interface OnSizeChangedListener {
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }

    public interface OnKeyBoardChangeListener {
        void onKeyBoardStateChange(int state);
    }

    public void setOnKbdStateListener(OnKeyBoardChangeListener listener) {
        mKeyboardListener = listener;
    }

    private TextWatcher watcher = new TextWatcher() {
        private CharSequence temp = "";

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            if (temp.length() > 0) {
                mAddFileIb.setVisibility(View.GONE);
                mSendMsgBtn.setVisibility(View.VISIBLE);
            } else {
                mAddFileIb.setVisibility(View.VISIBLE);
                mSendMsgBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
            //  chooseAtMember(s);
           /* try {
                EmojiUtil.handlerEmojiText(mChatInputEt,s.toString(),mContext);
            } catch (IOException e) {

            }*/
        }


    };

    public void chooseAtMember(Activity activity, CharSequence s) {
        if (!showAtIcon) {
            return;
        }
        if (!TextUtils.isEmpty(s.toString()) && s.toString().endsWith("@")) {

            ImLogic.getInstance().getGroupDetail((BaseActivity) mContext,
                    MsgConstant.openedConversationId, new ProgressSubscriber<GroupChatInfoBean>(mContext) {
                        @Override
                        public void onNext(GroupChatInfoBean employeeResultBean) {
                            super.onNext(employeeResultBean);
                            mListBeen = employeeResultBean.getData().getEmployeeInfo();
                            mAllMemberList.clear();
                            for (int i = 0; i < mListBeen.size(); i++) {
                                Member member = new Member();
                                GroupChatInfoBean.DataBean.EmployeeInfoBean bean = mListBeen.get(i);
                                member.setName(bean.getEmployee_name());
                                member.setEmployee_name(bean.getEmployee_name());
                                member.setPicture(bean.getPicture());
                                member.setSign_id(bean.getSign_id());
                                // member.setDepartmentName(bean.getRolename());
                                member.setRole_name(bean.getPhone());
                                mAllMemberList.add(member);
                            }
                            Iterator<Member> it = mAllMemberList.iterator();

                            while (it.hasNext()) {
                                String id = it.next().getSign_id();
                                if (SPHelper.getUserId().equals(id)) {
                                    it.remove();
                                    break;
                                }
                            }
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constants.DATA_TAG1, mAllMemberList);
                            bundle.putSerializable(Constants.DATA_TAG2, GroupMemberActivity.FLAG_AT);
                            CommonUtil.startActivtiy2(activity, GroupMemberActivity.class, bundle);


                        }
                    });
        }
    }

    public void focusToInput(boolean inputFocus) {
        if (inputFocus) {
            mChatInputEt.requestFocus();
            Log.i("ChatView", "show softInput");
        } else {
            mAddFileIb.requestFocusFromTouch();
        }
        setToBottom();
    }

    OnFocusChangeListener listener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Log.i("ChatView", "Input focus");
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dismissMoreMenu();
                        mContainer.setVisibility(GONE);

                    }
                });
            }
        }
    };

    public void setListeners(OnClickListener onClickListener) {
        mReturnBtn.setOnClickListener(onClickListener);
        mRightBtn.setOnClickListener(onClickListener);
        mChatInputEt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainer.setVisibility(GONE);
                SoftKeyboardUtils.show(mChatInputEt);
                mMoreMenuTl.setVisibility(GONE);
                mExpressionIb.setImageResource(R.drawable.icon_emoji);
                scrollToBottom();
            }
        });
        mSendMsgBtn.setOnClickListener(onClickListener);
        mSwitchIb.setOnClickListener(onClickListener);
        mVoiceBtn.setOnClickListener(onClickListener);
        mExpressionIb.setOnClickListener(onClickListener);
        mAddFileIb.setOnClickListener(onClickListener);
        mTakePhotoIb.setOnClickListener(onClickListener);
        mPickPictureIb.setOnClickListener(onClickListener);
        mLocationIb.setOnClickListener(onClickListener);
        mCallPhoneIb.setOnClickListener(onClickListener);
        mLlAt.setOnClickListener(onClickListener);
        ibAt.setOnClickListener(onClickListener);
        tvNewMsgCount.setOnClickListener(onClickListener);
        tvUnreadMsgCount.setOnClickListener(onClickListener);
        tvAt.setOnClickListener(onClickListener);
        ivAt.setOnClickListener(onClickListener);
    }


    @Override
    public void setOnTouchListener(OnTouchListener listener) {
        mChatListView.setOnTouchListener(listener);
        mChatInputEt.setOnTouchListener(listener);
    }

    public void setChatListAdapter(MessageListAdapter adapter) {
        mChatListView.setLayoutManager(new LinearLayoutManager(mContext));
        mChatListView.setAdapter(adapter);
    }

    public void setListItemClickListener(RecyclerView.SimpleOnItemTouchListener listener) {
        mChatListView.addOnItemTouchListener(listener);
    }

    //如果是文字输入
    public void isKeyBoard() {
        mSwitchIb.setImageResource(R.drawable.ic_voice);
        mAddFileIb.setImageResource(R.drawable.ic_more);
        mChatInputEt.setVisibility(View.VISIBLE);
        mVoiceBtn.setVisibility(View.GONE);
        // mExpressionIb.setVisibility(View.GONE);
        if (mChatInputEt.getText().length() > 0) {
            mSendMsgBtn.setVisibility(View.VISIBLE);
            mAddFileIb.setVisibility(View.GONE);
        } else {
            mSendMsgBtn.setVisibility(View.GONE);
            mAddFileIb.setVisibility(View.VISIBLE);
        }
    }

    //语音输入
    public void notKeyBoard(ChatView chatView) {
        mChatInputEt.setVisibility(View.GONE);
        mSwitchIb.setImageResource(R.drawable.ic_keyboard);
        mAddFileIb.setImageResource(R.drawable.ic_more);
        mVoiceBtn.setVisibility(View.VISIBLE);
        mSendMsgBtn.setVisibility(View.GONE);
        mAddFileIb.setVisibility(View.VISIBLE);
        mContainer.setVisibility(GONE);
    }

    //语音输入
    public void notKeyBoard() {
        notKeyBoard(this);
    }

    public String getChatInput() {
        return mChatInputEt.getText().toString();
    }


    public void setChatTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            mChatTitle.setText("");
        } else {
            mChatTitle.setText(title);
        }

    }

    public void setChatTitle(int id) {
        mChatTitle.setText(id);
    }

    //设置群聊名字
    public void setChatTitle(String name, int count) {
        mChatTitle.setText(name);
        mGroupNumTv.setText("(" + count + ")");
        mGroupNumTv.setVisibility(View.VISIBLE);
    }

    public void setChatTitle(int id, int count) {
        mChatTitle.setText(id);
        mGroupNumTv.setText("(" + count + ")");
        mGroupNumTv.setVisibility(View.VISIBLE);
    }

    public void clearInput() {
        mChatInputEt.setText("");
    }

    public void setToBottom() {
        if (mChatListView != null) {
            scrollToBottom();
        }
    }

    public void setGroupIcon() {
        // tvAt.setVisibility(VISIBLE);
        //ivAt.setVisibility(VISIBLE);
        mLlAt.setVisibility(VISIBLE);
        mRightBtn.setImageResource(R.drawable.icon_group_chat_detail);
    }

    public EmoticonsEditText getInputView() {
        return mChatInputEt;
    }

    public TableLayout getMoreMenu() {
        return mMoreMenuTl;
    }

    public void showMoreMenu() {
        mMoreMenuTl.setVisibility(View.VISIBLE);
        //  mAddFileIb.setImageResource(R.drawable.ic_more);
        mExpressionIb.setImageResource(R.drawable.icon_emoji);

        mContainer.setVisibility(GONE);
    }

    public void invisibleMoreMenu() {
        mMoreMenuTl.setVisibility(GONE);
    }

    public void dismissMoreMenu() {
        mMoreMenuTl.setVisibility(View.GONE);
    }

    public void dismissRightBtn() {
        mRightBtn.setVisibility(View.GONE);
    }

    public void showRightBtn() {
        mRightBtn.setVisibility(View.VISIBLE);
    }

    public void dismissSendMsgLl() {
        mSendMsgLl.setVisibility(View.GONE);
    }

    public void showSendMsgLl() {
        mSendMsgLl.setVisibility(View.VISIBLE);
    }

    public void dismissRecordDialog() {
        mVoiceBtn.dismissDialog();
    }

    public void releaseRecorder() {
        mVoiceBtn.releaseRecorder();
    }

    public RecyclerView getListView() {
        return mChatListView;
    }


    public void setCallPhoneVisibility(int Visibility) {
        mCallPhoneLl.setVisibility(Visibility);
    }

    public void setAtList(List<Member> atList) {
        this.atList = atList;
    }
}
