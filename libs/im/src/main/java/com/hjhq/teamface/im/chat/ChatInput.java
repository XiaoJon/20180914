package com.hjhq.teamface.im.chat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmoticonsEditText;
import com.hjhq.teamface.im.R;


/**
 * 聊天界面输入控件
 */
public class ChatInput extends RelativeLayout implements TextWatcher, View.OnClickListener, View.OnTouchListener {
    private Uri photoUri;

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    private static final String TAG = "ChatInput";
    private ImageButton btnAdd, btnSend, btnVoice, btnKeyboard;
    private EmoticonsEditText editText;
    private boolean isSendVisible, isHoldVoiceBtn;
    private InputMode inputMode = InputMode.NONE;
    private LinearLayout morePanel, textPanel;
    private TextView voicePanel;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    private View mLike;
    private Handler mHandler = new Handler();
    private Activity mActivity;

    public ChatInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.chat_input, this);
        initView();
        mActivity = (Activity) context;
    }

    private void initView() {
        textPanel = (LinearLayout) findViewById(R.id.text_panel);
        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnSend = (ImageButton) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnVoice = (ImageButton) findViewById(R.id.btn_voice);
        btnVoice.setOnClickListener(this);
        morePanel = (LinearLayout) findViewById(R.id.morePanel);
        LinearLayout BtnImage = (LinearLayout) findViewById(R.id.btn_photo);
        BtnImage.setOnClickListener(this);
        LinearLayout BtnPhoto = (LinearLayout) findViewById(R.id.btn_image);
        BtnPhoto.setOnClickListener(this);
        mLike = findViewById(R.id.chat_input_like);
        mLike.setOnClickListener(this);
        setSendBtn();
        btnKeyboard = (ImageButton) findViewById(R.id.btn_keyboard);
        btnKeyboard.setOnClickListener(this);
        voicePanel = (TextView) findViewById(R.id.voice_panel);
        voicePanel.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mOnClickVoiceListner != null) {
                            isHoldVoiceBtn = true;
                            ScreenUtils.letScreenGray(mActivity);
                            mOnClickVoiceListner.startRecord();
                            updateVoiceView();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        if (mOnClickVoiceListner != null) {
                            ScreenUtils.letScreenLight(mActivity);
                            isHoldVoiceBtn = false;
                            updateVoiceView();
                            mOnClickVoiceListner.stopRecord();
                        }

                        break;
                }
                return true;
            }
        });
        editText = (EmoticonsEditText) findViewById(R.id.input);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                updateView(InputMode.TEXT);
            }
        });
        editText.setOnTouchListener((v, event) -> {
            if (!editText.isFocused()) {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
            }
            return false;
        });
        isSendVisible = editText.getText().length() != 0;

    }

    private void updateView(InputMode mode) {
        if (mode == inputMode) {
            return;
        }
        leavingCurrentState();
        switch (inputMode = mode) {
            case MORE:
                morePanel.setVisibility(VISIBLE);
                break;
            case TEXT:
                if (editText.requestFocus()) {
                    mLike.setVisibility(VISIBLE);

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
                break;
            case VOICE:
                //mLike.setVisibility(GONE);
                mLike.setVisibility(VISIBLE);
                voicePanel.setVisibility(VISIBLE);
                textPanel.setVisibility(GONE);
                btnVoice.setVisibility(GONE);
                btnKeyboard.setVisibility(VISIBLE);
                break;
        }
    }

    private void leavingCurrentState() {
        switch (inputMode) {
            case TEXT:
                View view = ((Activity) getContext()).getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editText.clearFocus();
                break;
            case MORE:
                morePanel.setVisibility(GONE);
                break;
            case VOICE:
                voicePanel.setVisibility(GONE);
                textPanel.setVisibility(VISIBLE);
                btnVoice.setVisibility(VISIBLE);
                btnKeyboard.setVisibility(GONE);
                break;
        }
    }


    private void updateVoiceView() {
        if (isHoldVoiceBtn) {
            voicePanel.setText(getResources().getString(R.string.chat_release_send));
            voicePanel.setBackground(getResources().getDrawable(R.drawable.btn_voice_pressed));
            //TODO 录音

        } else {
            voicePanel.setText(getResources().getString(R.string.chat_press_talk));
            voicePanel.setBackground(getResources().getDrawable(R.drawable.btn_voice_normal));

        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isSendVisible = s != null && s.length() > 0;
        setSendBtn();
    }

    /**
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setSendBtn() {
        if (isSendVisible) {
            btnAdd.setVisibility(GONE);
            btnSend.setVisibility(VISIBLE);
        } else {
            btnAdd.setVisibility(VISIBLE);
            btnSend.setVisibility(GONE);
        }
    }

    private boolean isLike = false;

    public void setLike(boolean likeOrNot) {
        this.isLike = likeOrNot;
    }

    @Override
    public void onClick(View v) {
        Activity activity = (Activity) getContext();
        int id = v.getId();
        if (id == R.id.btn_send) {
//       发送文本
            if (TextUtils.isEmpty(editText.getText().toString())) {
                ToastUtils.showToast(activity, "内容不能为空");
            } else if (mOnClickSendTextListner != null) {
                mOnClickSendTextListner.sendText(editText.getText().toString());
                editText.setText("");
            }

        }
        if (id == R.id.btn_add) {
            //切换试图
            updateView(inputMode == InputMode.MORE ? InputMode.TEXT : InputMode.MORE);
        }
        if (id == R.id.btn_photo) {
            //相机
            if (activity != null && requestCamera(activity)) {
                String SDState = Environment.getExternalStorageState();
                if (SDState.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
                    /***
                     * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
                     * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
                     * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
                     */
                    LogUtil.e("相机");
                    ContentValues values = new ContentValues();
                    photoUri = mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                    mActivity.startActivityForResult(intent, Constants.TAKE_PHOTO_NEW_REQUEST_CODE);
                } else {
                    ToastUtils.showError(getContext(), "当前储存不可用");
                }

            }
        }
        if (id == R.id.btn_image) {
            if (activity != null && requestStorage(activity)) {
                //相册
                LogUtil.e("相册");
                CommonUtil.getImageFromAlbum(activity, Constants.PHOTO_ABLUM_NEW_REQUEST_CODE);
            }
        }
        if (id == R.id.btn_voice) {
            if (activity != null && requestAudio(activity)) {
                updateView(InputMode.VOICE);

            }
        }
        if (id == R.id.btn_keyboard) {
            updateView(InputMode.TEXT);
        }
        if (id == R.id.chat_input_like) {
            if (mOnClickAtListner != null) {
                mOnClickAtListner.at();
            }
        }

    }


    /**
     * 获取输入框文字
     */
    public Editable getText() {
        return editText.getText();
    }

    public EmoticonsEditText getEditText() {
        return editText;
    }

    /**
     * 设置输入框文字
     */
    public void setText(String text) {
        editText.setText(text);
    }


    /**
     * 设置输入模式
     */
    public void setInputMode(InputMode mode) {
        updateView(mode);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    public enum InputMode {
        TEXT,
        VOICE,
        MORE,
        NONE,
    }


    /**
     * 相机的权限
     *
     * @param activity
     * @return
     */
    private boolean requestCamera(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    /**
     * 声音的权限
     *
     * @param activity
     * @return
     */
    private boolean requestAudio(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    /**
     * 储存的权限
     *
     * @param activity
     * @return
     */
    private boolean requestStorage(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean afterM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

//    录音接口回调

    private OnClickVoiceListner mOnClickVoiceListner;

    public void setOnClickVoiceListner(OnClickVoiceListner l) {
        this.mOnClickVoiceListner = l;
    }


    public interface OnClickVoiceListner {
        void startRecord();

        void stopRecord();
    }

    //    发送文本回调
    private OnClickSendTextListner mOnClickSendTextListner;

    public void OnClickSendTextListner(OnClickSendTextListner l) {
        this.mOnClickSendTextListner = l;
    }

    public void setOnClickSendTextListner(OnClickSendTextListner l) {
        this.mOnClickSendTextListner = l;
    }

    public interface OnClickSendTextListner {
        void sendText(String text);


    }
    //    喜欢不喜欢接口回调

    private OnClickLikeListner mOnClickLikeListner;

    public void setOnClickLikeListner(OnClickLikeListner l) {
        this.mOnClickLikeListner = l;
    }

    public interface OnClickLikeListner {
        void like();

        void unLike();
    }

    private OnClickAtListner mOnClickAtListner;

    public void setOnClickAtListner(OnClickAtListner l) {
        this.mOnClickAtListner = l;
    }

    public interface OnClickAtListner {
        void at();
    }

}
