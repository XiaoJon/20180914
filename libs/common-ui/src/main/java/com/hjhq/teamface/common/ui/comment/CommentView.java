package com.hjhq.teamface.common.ui.comment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmoticonsEditText;
import com.hjhq.teamface.common.view.RecordVoiceButton;
import com.hjhq.teamface.emoji.Emoji;
import com.hjhq.teamface.emoji.EmojiUtil;
import com.hjhq.teamface.emoji.FaceFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论
 *
 * @author Administrator
 */
public class CommentView extends RelativeLayout implements FaceFragment.OnEmojiClickListener {

    private TableLayout mMoreMenuTl;
    private RecordVoiceButton mVoiceBtn;
    //public EditText mChatInputEt;
    public EmoticonsEditText mChatInputEt;
    private ImageButton mSwitchIb;
    private ImageButton mExpressionIb;
    private ImageButton mPickFileIb;
    private ImageButton mAddFileIb;
    private ImageButton mTakePhotoIb;
    private ImageButton mPickPictureIb;
    private LinearLayout mSendMsgLl;
    private Button mSendMsgBtn;
    private FrameLayout mContainer;
    Context mContext;
    private OnSizeChangedListener mListener;
    private OnKeyBoardChangeListener mKeyboardListener;
    Activity mActivity;


    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;
    private boolean mHasInit;
    private boolean mHasKeybord;
    private int mHeight;
    private boolean showAtIcon = true;
    private ArrayList<Member> mAllMemberList = new ArrayList<Member>();
    private List<Member> atList = new ArrayList<>();
    private List<Member> forDel = new ArrayList<>();

    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }


    public void initModule() {
        mContainer = findViewById(R.id.container);
        mTakePhotoIb = findViewById(R.id.jmui_pick_from_camera_btn);
        mPickPictureIb = findViewById(R.id.jmui_pick_from_local_btn);
        mPickFileIb = findViewById(R.id.jmui_pick_from_file_btn);
        mMoreMenuTl = findViewById(R.id.jmui_more_menu_tl);
        mAddFileIb = findViewById(R.id.jmui_add_file_btn);
        mSendMsgBtn = findViewById(R.id.jmui_send_msg_btn);
        mVoiceBtn = findViewById(R.id.jmui_voice_btn);
        mExpressionIb = findViewById(R.id.jmui_expression_btn);
        mChatInputEt = findViewById(R.id.jmui_chat_input_et);
        mChatInputEt.setFocusable(true);
        mChatInputEt.requestFocus();
        mSwitchIb = findViewById(R.id.jmui_switch_voice_ib);
        mSendMsgLl = findViewById(R.id.jmui_send_msg_layout);
        FaceFragment faceFragment = FaceFragment.Instance();
        faceFragment.setListener(this);
        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().add(R.id.container, faceFragment).commit();
        mChatInputEt.addTextChangedListener(watcher);
        mChatInputEt.setMaxLines(4);
        setMoreMenuHeight();
    }

    public void setMoreMenuHeight() {
//        int softKeyboardHeight = SharePreferenceManager.getCachedKeyboardHeight();
//        if (softKeyboardHeight > 0) {
//            mMoreMenuTl.setLayoutParams(new LinearLayout
//                    .LayoutParams(LayoutParams.MATCH_PARENT, softKeyboardHeight));
//        }

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
            if (mKeyboardListener != null) {
                mKeyboardListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
            }
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


    public void showSoftInputFromWindow(Context context, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void initActivity(Activity activity) {
        this.mActivity = activity;
    }

    public void showEmoji() {
        if (mContainer.getVisibility() == VISIBLE) {
            mContainer.setVisibility(GONE);
            mExpressionIb.setImageResource(R.drawable.icon_emoji);
            SoftKeyboardUtils.show(mChatInputEt);

        } else {
            mChatInputEt.setVisibility(View.VISIBLE);
            mVoiceBtn.setVisibility(View.GONE);
            SoftKeyboardUtils.hide(mChatInputEt);
            mMoreMenuTl.setVisibility(View.GONE);
            mContainer.postDelayed(() -> {
                mContainer.setVisibility(VISIBLE);
                mExpressionIb.setImageResource(R.drawable.ic_keyboard);
                mSwitchIb.setBackgroundResource(R.drawable.ic_voice);
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
            if (!showAtIcon) {
                return;
            }
            if (!TextUtils.isEmpty(s.toString()) && s.toString().endsWith("@")) {
                SoftKeyboardUtils.hide(mChatInputEt);
                CommonUtil.startActivtiyForResult(mActivity, SelectMemberActivity.class, Constants.RESULT_CODE_SELECT_MEMBER);
            }
        }

    };

    public void focusToInput(boolean inputFocus) {
        if (inputFocus) {
            mChatInputEt.requestFocus();
            Log.i("ChatView", "show softInput");
        } else {
            mAddFileIb.requestFocusFromTouch();
        }
    }

    public void setListeners(OnClickListener onClickListener) {
        mChatInputEt.setOnClickListener(v -> {
            dismissAll();
            SoftKeyboardUtils.show(mChatInputEt);
            mExpressionIb.setImageResource(R.drawable.icon_emoji);
        });
        mSendMsgBtn.setOnClickListener(onClickListener);
        mSwitchIb.setOnClickListener(onClickListener);
        mVoiceBtn.setOnClickListener(onClickListener);
        mExpressionIb.setOnClickListener(onClickListener);
        mAddFileIb.setOnClickListener(onClickListener);
        mTakePhotoIb.setOnClickListener(onClickListener);
        mPickPictureIb.setOnClickListener(onClickListener);
        mPickFileIb.setOnClickListener(onClickListener);
    }

    @Override
    public void setOnTouchListener(OnTouchListener listener) {
        mChatInputEt.setOnTouchListener(listener);
    }

    //如果是文字输入
    public void isKeyBoard() {
        mSwitchIb.setBackgroundResource(R.drawable.ic_voice);
        mChatInputEt.setVisibility(View.VISIBLE);
        mVoiceBtn.setVisibility(View.GONE);
        if (mChatInputEt.getText().length() > 0) {
            mSendMsgBtn.setVisibility(View.VISIBLE);
            mAddFileIb.setVisibility(View.GONE);
        } else {
            mSendMsgBtn.setVisibility(View.GONE);
            mAddFileIb.setVisibility(View.VISIBLE);
        }
    }

    //语音输入
    public void notKeyBoard() {
        mChatInputEt.setVisibility(View.GONE);
        mSwitchIb.setBackgroundResource(R.drawable.ic_keyboard);
        mExpressionIb.setImageResource(R.drawable.icon_emoji);
        mVoiceBtn.setVisibility(View.VISIBLE);
        mSendMsgBtn.setVisibility(View.GONE);
        mAddFileIb.setVisibility(View.VISIBLE);
        mContainer.setVisibility(GONE);
    }

    public String getChatInput() {
        return mChatInputEt.getText().toString();
    }


    public void clearInput() {
        mChatInputEt.setText("");
    }


    public EmoticonsEditText getInputView() {
        return mChatInputEt;
    }

    public void setKeyboardStateHide() {
        SoftKeyboardUtils.hide(mChatInputEt);
    }

    public TableLayout getMoreMenu() {
        return mMoreMenuTl;
    }

    public void showMoreMenu() {
        mMoreMenuTl.setVisibility(View.VISIBLE);
        mContainer.setVisibility(GONE);
    }

    public void invisibleMoreMenu() {
        mMoreMenuTl.setVisibility(INVISIBLE);
    }

    public void dismissMoreMenu() {
        mMoreMenuTl.setVisibility(View.GONE);
    }

    public void dismissAll() {
        mMoreMenuTl.setVisibility(View.GONE);
        mContainer.setVisibility(GONE);
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


    public void setAtList(List<Member> atList) {
        this.atList = atList;
    }

}
