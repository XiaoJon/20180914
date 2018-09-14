package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.emoji.Emoji;
import com.hjhq.teamface.emoji.EmojiUtil;
import com.hjhq.teamface.emoji.FaceFragment;
import com.hjhq.teamface.oa.main.logic.MainLogic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;


/**
 * 个性签名
 *
 * @author lx
 * @date 2017/6/15
 */

public class SignatureActivity extends BaseTitleActivity implements FaceFragment.OnEmojiClickListener {

    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.et_sign)
    EditText etSign;
    @Bind(R.id.tv_mood)
    TextView tvMood;
    @Bind(R.id.rl_mood)
    View rlMood;
    @Bind(R.id.fl_container)
    FrameLayout moodContainer;
    FaceFragment faceFragment;
    private String emojiText;
    String sign = "";

    @Override
    protected int getChildView() {
        return R.layout.activity_signature;
    }


    @Override
    protected void onSetContentViewNext(Bundle savedInstanceState) {
        super.onSetContentViewNext(savedInstanceState);
        if (savedInstanceState == null) {
            sign = getIntent().getStringExtra(Constants.DATA_TAG1);
            emojiText = getIntent().getStringExtra(Constants.DATA_TAG2);
        } else {
            sign = savedInstanceState.getString(Constants.DATA_TAG1);
            emojiText = savedInstanceState.getString(Constants.DATA_TAG2);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DATA_TAG1, sign);
        outState.putString(Constants.DATA_TAG2, emojiText);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle(getString(R.string.setting_signature_hit));
        setRightMenuColorTexts(getString(R.string.commit));
    }

    @Override
    protected void initData() {
        TextUtil.setText(etSign, sign);
        if (!TextUtil.isEmpty(emojiText)) {
            ivRight.setImageResource(R.drawable.clear_button);
            try {
                EmojiUtil.handlerEmojiText2(tvMood, emojiText, mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        faceFragment = FaceFragment.Instance();
        faceFragment.setShowDelFace(false);
        faceFragment.setListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, faceFragment).commit();
    }

    @Override
    protected void onRightMenuClick(int itemId) {
        super.onRightMenuClick(itemId);
        setSignature();
    }

    /**
     * 设置签名
     */
    private void setSignature() {
        sign = etSign.getText().toString().trim();
        if (sign.length() > 12) {
            ToastUtils.showError(this, "签名不能超过12个字");
            return;
        }
        Map<String, String> map = new HashMap<>(2);
        map.put("sign", sign);
        map.put("mood", emojiText);
        MainLogic.getInstance().editEmployeeDetail(this, map, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG1, sign);
                intent.putExtra(Constants.DATA_TAG2, emojiText);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void setListener() {
        setOnClicks(rlMood, etSign, ivRight);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_sign:
                etSign.setCursorVisible(true);
                moodContainer.setVisibility(View.GONE);
                break;
            case R.id.rl_mood:
                SoftKeyboardUtils.hide(etSign);
                etSign.setCursorVisible(false);
                moodContainer.setVisibility(moodContainer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.iv_right:
                if (!TextUtil.isEmpty(emojiText)) {
                    setEmoji("");
                    ivRight.setImageResource(R.drawable.icon_to_next);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (moodContainer.getVisibility() == View.VISIBLE) {
            moodContainer.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onEmojiDelete() {
    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        ivRight.setImageResource(R.drawable.clear_button);
        setEmoji(emoji.getContent());
    }

    private void setEmoji(String emoji) {
        try {
            emojiText = emoji;
            EmojiUtil.handlerEmojiText2(tvMood, emojiText, mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
