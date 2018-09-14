package com.hjhq.teamface.common.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.common.R;

/**
 * Created by Administrator on 2017/9/5.
 * Describe：
 */

public class SearchBar extends FrameLayout {
    private EditText mEditText;
    private TextView mTextView;
    private ImageView mClean;
    View mView;
    /**
     * 是否点击软键盘搜索
     */
    private boolean pressSearch = false;

    public SearchBar(Context context) {
        super(context);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(R.layout.common_search_bar, this);
        initView();
    }

    int keycode = -1;

    private void initView() {
        mEditText = mView.findViewById(R.id.et_search_in_searchbar);
        mTextView = mView.findViewById(R.id.cancel);
        mClean = mView.findViewById(R.id.iv_clean);
        initListener();
        mEditText.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (mSearchListener != null) {
                        mSearchListener.search();
                        return true;
                    }
                } else {
                    return true;
                }
            }
            return false;
        });


    }

    //设置hint
    public void setHintText(String hintText) {
        mEditText.setHint(hintText);
    }

    private void initListener() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSearchListener != null) {
                    if (TextUtils.isEmpty(s)) {
                        if (mSearchListener != null) {

                            mSearchListener.getText("");
                        }
                        mClean.setVisibility(GONE);
                    } else {
                        if (mSearchListener != null) {
                            mSearchListener.getText(s.toString().trim());
                        }
                        mClean.setVisibility(VISIBLE);
                    }
                }
            }
        });
        mTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftKeyboardUtils.hide(mEditText);
                if (mSearchListener != null) {
                    mSearchListener.cancel();
                }

            }
        });
        mClean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
                if (mSearchListener != null) {
                    mSearchListener.clear();
                }
            }
        });

    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text) {
//        if (TextUtils.isNotEmpty(text)) {
//
//        }
        mEditText.setText(text);
        mEditText.requestFocus();
        mEditText.setSelection(text.length());
        SoftKeyboardUtils.show(mEditText);
    }

    public void setText(String text, boolean showKeyboard) {

        mEditText.setText(text);
        mEditText.setSelection(text.length());
        if (showKeyboard) {
            mEditText.requestFocus();
            SoftKeyboardUtils.show(mEditText);
        } else {
            mEditText.clearFocus();
            SoftKeyboardUtils.hide(mEditText);
        }

    }

    private SearchListener mSearchListener;

    public void setSearchListener(SearchListener listener) {
        this.mSearchListener = listener;
    }


    public void requestTextFocus() {
        mEditText.requestFocus();
    }


    public interface SearchListener {
        void clear();

        void cancel();

        void search();

        void getText(String text);
    }

    /**
     * 设置SearchBar中EditText背景为灰色
     */
    public void setSearchBarGray() {
        mEditText.setBackground(mEditText.getContext().getResources().getDrawable(R.drawable.bg_gray_search_bar));

    }

    /**
     * 设置SearchBar中EditText背景为白色
     */
    public void setSeatchBarWhite() {
        mEditText.setBackground(mEditText.getContext().getResources().getDrawable(R.drawable.bg_search_bar));

    }

    /**
     * 取消按钮是否显示
     *
     * @param isShow
     */
    public void setCancelVis(boolean isShow) {
        mTextView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setCancelColor(int color) {
        mTextView.setTextColor(color);
    }

    public EditText getEditText() {
        return mEditText;
    }

    public void setCancelMargin(int left, int top, int right, int bottom) {


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTextView.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
        mTextView.setLayoutParams(layoutParams);
    }
}
