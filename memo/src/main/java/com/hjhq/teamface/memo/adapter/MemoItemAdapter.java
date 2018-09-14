package com.hjhq.teamface.memo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.bean.MemoBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wkd
 * @desc 聊天信息列表
 */
public class MemoItemAdapter extends BaseMultiItemQuickAdapter<MemoBean, BaseViewHolder> {
    private int focusedIndex = 0;
    private EditText mEditText;
    private String foucsedItemText = "";
    private OnItemNumChangeListener mListener;
    private OnFocusChangeListener mFocusListener;
    private boolean isFirstTimeOpen = true;
    private boolean isEditState = true;
    private boolean contentChangedFlag = false;
    private boolean isCreator = false;
    private InputMethodManager inputMethodManager;

    public MemoItemAdapter(boolean isEditState, List<MemoBean> data) {
        super(data);

        this.isEditState = isEditState;
        if (data != null && data.size() > 0) {
            focusedIndex = data.size() - 1;
        }
        Log.e("初始焦点位置==", "" + focusedIndex);
        //文本
        addItemType(MemoConstant.ITEM_TEXT, R.layout.memo_content_text);
        //图片
        addItemType(MemoConstant.ITEM_IMAGE, R.layout.memo_content_image);
        //项目
        addItemType(MemoConstant.ITEM_PROJECT, R.layout.memo_content_relevant_layout);
        //提醒
        addItemType(MemoConstant.ITEM_REMIND, R.layout.memo_content_remind);
        //地点
        addItemType(MemoConstant.ITEM_LOCATION, R.layout.memo_content_location);
        //分享
        addItemType(MemoConstant.ITEM_MEMBER, R.layout.memo_content_share);

    }


    @Override
    public void setNewData(List<MemoBean> data) {
        super.setNewData(data);
        if (data.size() > 0) {
            focusedIndex = data.size() - 1;
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MemoBean item) {
        if (inputMethodManager == null) {
            inputMethodManager = (InputMethodManager) helper.convertView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        helper.setIsRecyclable(false);
        if (isEditState) {
            if (isFirstTimeOpen) {

                if (getItemCount() > 0 && getData().get(getItemCount() - 1).getType() == MemoConstant.ITEM_TEXT) {
                    focusedIndex = getItemCount() - 1;
                } else {
                    MemoBean bean = new MemoBean();
                    bean.setPosition(0);
                    bean.setType(MemoConstant.ITEM_TEXT);
                    MemoBean.TextBean tb = new MemoBean.TextBean();
                    tb.setContent("");
                    tb.setCheck(0);
                    tb.setNum(0);
                    bean.setText(tb);
                    getData().add(bean);
                    focusedIndex = getData().size() - 1;
                }
                isFirstTimeOpen = false;
            }
        }
        if (helper.getAdapterPosition() == -1) {
            item.setPosition(0);
        } else {
            item.setPosition(helper.getAdapterPosition());
        }

        switch (item.getItemType()) {
            case MemoConstant.ITEM_TEXT:
                ImageView state = helper.getView(R.id.checkbox);
                helper.addOnClickListener(R.id.checkbox);

                EditText et = helper.getView(R.id.et_content);
                et.setTag(false);
                if (helper.getAdapterPosition() == getItemCount() - 1) {
                    et.setTag(true);
                }
                TextView tv = helper.getView(R.id.tv_content);
                if (isEditState) {
                    et.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.GONE);
                    et.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                    if (helper.getAdapterPosition() == focusedIndex) {
                        et.setTag(false);
                        et.requestFocus();
                        mEditText = et;
                        mEditText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mEditText.performClick();
                                SoftKeyboardUtils.show(mEditText);
                                inputMethodManager.showSoftInput(et, 0);
                            }
                        }, 500);

                        if (mFocusListener != null) {
                            mFocusListener.onFocus(item.getText().getCheck() > 0, item.getText().getNum() > 0);
                        }
                    }

                    et.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            List<MemoBean> data = getData();
                            et.setTag(true);
                            mEditText = et;
                            item.getText().setContent(et.getText().toString());
                            if (keyCode == KeyEvent.KEYCODE_DEL) {
                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    if (TextUtils.isEmpty(et.getText())) {
                                        if (item.getText().getCheck() == 0 && item.getText().getNum() == 0) {
                                            if (getData().size() >= 1 && helper.getAdapterPosition() > 0) {
                                                if (getData().get(helper.getAdapterPosition() - 1).getType() == MemoConstant.ITEM_IMAGE) {
                                                    focusedIndex = helper.getAdapterPosition() - 1;
                                                    getData().get(helper.getAdapterPosition() - 1).setType(MemoConstant.ITEM_TEXT);
                                                    MemoBean.TextBean tb = new MemoBean.TextBean();
                                                    tb.setContent("");
                                                    tb.setNum(0);
                                                    tb.setCheck(0);
                                                    getData().get(helper.getAdapterPosition() - 1).setText(tb);
                                                    notifyItemChanged(helper.getAdapterPosition() - 1);

                                                    contentChangedFlag = true;
                                                    mEditText = null;


                                                } else {
                                                    // TODO: 2018/8/28 删除条目键盘收起问题
                                                    focusedIndex = helper.getAdapterPosition() - 1;
                                                    mEditText = null;
                                                    getData().remove(helper.getAdapterPosition());
                                                    contentChangedFlag = true;
                                                    notifyDataSetChanged();

                                                    /*focusedIndex = helper.getAdapterPosition() - 1;
                                                    mEditText = null;
                                                    contentChangedFlag = true;
                                                    remove(helper.getAdapterPosition());
                                                    notifyDataSetChanged();*/
                                                    /*focusedIndex = helper.getAdapterPosition() - 1;
                                                    getData().get(helper.getAdapterPosition()).setText(getData().get(helper.getAdapterPosition()-1).getText());
                                                    getData().remove(helper.getAdapterPosition()-1);
                                                    contentChangedFlag = true;*/


                                                }
                                                et.setTag(false);
                                                return true;
                                            } else {
                                                return false;
                                            }

                                        }
                                        if (item.getText().getNum() > 0) {
                                            item.getText().setContent("");
                                            item.getText().setNum(0);
                                            if (mFocusListener != null) {
                                                mFocusListener.onFocus(item.getText().getCheck() > 0, item.getText().getNum() > 0);
                                            }
                                            focusedIndex = helper.getAdapterPosition();
                                            notifyItemChanged(helper.getAdapterPosition());
                                            //notifyDataSetChanged();
                                            return true;
                                        }
                                        if (item.getText().getCheck() > 0) {
                                            item.getText().setContent("");
                                            item.getText().setCheck(0);
                                            item.getText().setNum(0);
                                            if (mFocusListener != null) {
                                                mFocusListener.onFocus(item.getText().getCheck() > 0, item.getText().getNum() > 0);
                                            }
                                            focusedIndex = helper.getAdapterPosition();
                                            notifyItemChanged(helper.getAdapterPosition());
                                            //notifyDataSetChanged();
                                            return true;
                                        }

                                        if (helper.getAdapterPosition() == 0 || getItemCount() == 1) {
                                            return true;
                                        }

                                        return true;

                                    } else {
                                        return false;
                                    }
                                } else {
                                    return true;
                                }
                            }
                            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                                    item.getText().setContent(et.getText().toString());
                                    if (TextUtils.isEmpty(et.getText())) {
                                        MemoBean beanNew = new MemoBean();
                                        beanNew.setType(item.getType());
                                        MemoBean.TextBean textBean = new MemoBean.TextBean();
                                        textBean.setContent("");
                                        if (item.getText().getNum() == 0) {
                                            textBean.setNum(0);
                                        } else {
                                            textBean.setNum(item.getText().getNum() + 1);
                                        }
                                        if (item.getText().getCheck() == 0) {
                                            textBean.setCheck(0);
                                        } else {
                                            textBean.setCheck(1);
                                        }
                                        item.getText().setContent("");
                                        beanNew.setText(textBean);
                                        focusedIndex = helper.getAdapterPosition() + 1;
                                        getData().add(helper.getLayoutPosition() + 1, beanNew);
                                        contentChangedFlag = true;
                                        // notifyItemChanged(helper.getAdapterPosition());
                                        notifyItemInserted(helper.getAdapterPosition() + 1);
                                        //notifyDataSetChanged();
                                        return true;
                                    }
                                    item.getText().setContent(et.getText().toString());
                                    MemoBean beanNew = new MemoBean();
                                    MemoBean.TextBean textBean = new MemoBean.TextBean();
                                    textBean.setContent(et.getText().toString().substring(et.getSelectionStart()));
                                    item.getText().setContent(et.getText().toString().substring(0, et.getSelectionStart()));
                                    mEditText.setText(item.getText().getContent());
                                    beanNew.setType(item.getType());
                                    beanNew.setText(textBean);

                                    if (item.getText().getCheck() == 1 || item.getText().getCheck() == 2) {
                                        textBean.setCheck(1);
                                    }
                                    if (item.getText().getNum() == 0) {
                                        beanNew.getText().setNum(0);
                                    } else {
                                        textBean.setNum(item.getText().getNum() + 1);
                                    }
                                    beanNew.setText(textBean);
                                    item.getText().setContent(et.getText().toString());
                                    focusedIndex = helper.getAdapterPosition() + 1;

                                    getData().add(helper.getLayoutPosition() + 1, beanNew);
                                    contentChangedFlag = true;
                                    notifyItemChanged(helper.getAdapterPosition());
                                    notifyItemInserted(helper.getAdapterPosition() + 1);
                                    //notifyDataSetChanged();
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                            return false;
                        }
                    });
                    et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus && mFocusListener != null) {
                                mFocusListener.onFocus(item.getText().getCheck() > 0, item.getText().getNum() > 0);
                            }
                            if (!hasFocus) {
                                //丢失焦点时将EditText中的文字赋值到数据对象中.
                                /*if (mEditText != null && et.getTag().equals(true)) {
                                    item.getText().setContent(et.getText().toString());
                                }*/
                                item.getText().setContent(et.getText().toString());

                                Log.e("失去焦点位置", "==      " + focusedIndex);
                            } else {

                            }
                        }
                    });
                    /*helper.getView(R.id.ll_et).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mEditText = et;
                            et.setSelection(et.getText().length());
                            Log.e("之前位置", "==      " + focusedIndex);
                            if (helper.getAdapterPosition() >= 0) {
                                focusedIndex = helper.getAdapterPosition();
                            }
                            Log.e("之后位置", "==      " + focusedIndex);
                            if (et.isFocused()) {
                                return;
                            }
                            et.requestFocus();
                        }
                    });*/
                    et.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // Log.e("之前位置2", "==      " + focusedIndex);
                            focusedIndex = helper.getAdapterPosition();
                            mEditText = et;
                            et.setTag(true);
                            // Log.e("之前位置3", "==      " + focusedIndex);
                            if (mFocusListener != null) {
                                mFocusListener.onFocus(item.getText().getCheck() > 0, item.getText().getNum() > 0);
                            }
                            return false;
                        }
                    });


                    et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (start == 0 && count == 0) {
                                return;
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                           /* Log.e("afterTextChanged", "文字                " + s);
                            Log.e("afterTextChanged", "getAdapterPosition " + helper.getAdapterPosition());
                            Log.e("afterTextChanged", "focusedIndex       " + focusedIndex);*/
                            if (focusedIndex != helper.getAdapterPosition()) {
                                return;
                            }
                            if (et.getTag().equals(false)) {
                                return;
                            }
//                            if (TextUtils.isEmpty(s.toString())) {
//                                getData().get(focusedIndex).getText().setContent(s.toString());
//                                return;
//                            }

                            /*if (getLastTextItem() != null && getLastTextItem().getText() != null && s.toString().equals(getLastTextItem().getText().getContent())) {
                                return;
                            }*/
                            if (getItemCount() > 0 && focusedIndex <= getItemCount() - 1 && focusedIndex >= 0 && getData().get(focusedIndex).getType() == MemoConstant.ITEM_TEXT) {
                                getData().get(focusedIndex).getText().setContent(s.toString());
                                contentChangedFlag = true;
                            }
                        }
                    });
                } else {
                    et.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                }
                et.setTextColor(Color.parseColor("#4A4A4A"));
                tv.setTextColor(Color.parseColor("#4A4A4A"));
                switch (item.getText().getCheck()) {
                    case 0:
                        helper.setVisible(R.id.checkbox, false);
                        if (isEditState) {
                            et.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                            et.setText(item.getText().getContent());
                        } else {
                            tv.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                            tv.setText(item.getText().getContent());
                        }

                        break;
                    case 1:
                        helper.setVisible(R.id.checkbox, true);
                        state.setSelected(false);
                        helper.setImageResource(R.id.checkbox, R.drawable.state_uncheck);
                        if (isEditState) {
                            et.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                            et.setText(item.getText().getContent());
                        } else {
                            tv.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                            tv.setText(item.getText().getContent());
                        }
                        break;
                    case 2:
                        helper.setVisible(R.id.checkbox, true);
                        state.setSelected(true);
                        helper.setImageResource(R.id.checkbox, R.drawable.state_checked);
                        if (isEditState) {
                            et.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            et.setText(item.getText().getContent());
                            et.setTextColor(Color.parseColor("#909090"));
                            tv.setTextColor(Color.parseColor("#909090"));
                        } else {
                            tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            tv.setText(item.getText().getContent());
                            et.setTextColor(Color.parseColor("#909090"));
                            tv.setTextColor(Color.parseColor("#909090"));
                        }
                        break;
                    default:

                        break;
                }
                et.setSelection(et.getText().length());
                state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isCreator) {
                            return;
                        }
                        if (isEditState) {
                            item.getText().setContent(et.getText().toString());
                        }
                        if (item.getText().getCheck() == 1) {
                            helper.setImageResource(R.id.checkbox, R.drawable.state_checked);
                            item.getText().setCheck(2);
                            et.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                        } else {
                            item.getText().setCheck(1);
                            helper.setImageResource(R.id.checkbox, R.drawable.state_uncheck);
                            et.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);

                        }
                        notifyItemChanged(helper.getAdapterPosition());
                    }
                });
                switch (item.getText().getNum()) {
                    case 0:
                        helper.setVisible(R.id.num, false);
                        break;
                    default:
                        helper.setVisible(R.id.num, true);
                        helper.setText(R.id.num, item.getText().getNum() + ".");
                        break;
                }

                break;
            case MemoConstant.ITEM_IMAGE:
                ImageView image = helper.getView(R.id.image);
                switch (item.getImg().getFrom()) {
                    case MemoConstant.LOCAL_FILE:
                        ImageLoader.loadRoundImage(helper.getConvertView().getContext(),
                                item.getImg().getLocalPath(), image, 5, R.drawable.ic_image);
                        break;
                    case MemoConstant.NET_FILE:
                        ImageLoader.loadRoundImage(helper.getConvertView().getContext(),
                                item.getImg().getUrl(), image, 5, R.drawable.ic_image);
                        break;
                    default:

                        break;
                }
                image.requestLayout();
               /* helper.getView(R.id.remove).setOnClickListener(v -> {
                    focusedIndex = helper.getAdapterPosition() - 1;
                    getData().remove(helper.getAdapterPosition());
                    notifyItemRemoved(helper.getAdapterPosition());
                });*/

                break;
            case MemoConstant.ITEM_PROJECT:
                break;
            case MemoConstant.ITEM_REMIND:
                helper.setText(R.id.tv_time_content,
                        DateTimeUtil.longToStr(item.getRemind().getTime(), "yyyy年MM月dd日 HH:mm"));
                break;
            case MemoConstant.ITEM_LOCATION:

                break;
            default:
                break;
        }

    }

    /**
     * 添加或移除待办
     */
    public void changeCheckState() {
        if (focusedIndex < 0 || focusedIndex > getItemCount() - 1) {
            return;
        }
        Log.e("添加或移除待办", focusedIndex + "");
        if (getItem(focusedIndex).getType() == MemoConstant.ITEM_TEXT) {

            if (mEditText != null) {
                getItem(focusedIndex).getText().setContent(mEditText.getText().toString());
            }
            if (getItem(focusedIndex).getText().getCheck() == 0) {
                getItem(focusedIndex).getText().setCheck(1);
            } else {
                getItem(focusedIndex).getText().setCheck(0);
            }
            notifyItemChanged(focusedIndex);
        }

    }

    /**
     * 添加或移除序号
     */
    public void changeNumState() {
        if (focusedIndex < 0 || focusedIndex > getItemCount() - 1) {
            return;
        }
        if (getItem(focusedIndex).getType() != MemoConstant.ITEM_TEXT) {
            return;
        }
        if (mEditText != null) {
            getItem(focusedIndex).getText().setContent(mEditText.getText().toString());
        }
        if (getItem(focusedIndex).getType() == MemoConstant.ITEM_TEXT) {
            if (getItem(focusedIndex).getText().getNum() == 0) {
                if (focusedIndex == 0) {
                    getItem(focusedIndex).getText().setNum(1);
                }
                if (focusedIndex > 0 && getItem(focusedIndex - 1).getType() == MemoConstant.ITEM_TEXT) {
                    getItem(focusedIndex).getText().setNum(getItem(focusedIndex - 1).getText().getNum() + 1);
                } else if (focusedIndex > 0 && getItem(focusedIndex - 1).getType() != MemoConstant.ITEM_TEXT) {
                    MemoBean lastTextItem = getLastTextItem();
                    if (lastTextItem != null) {
                        getItem(focusedIndex).getText().setNum(lastTextItem.getText().getNum() + 1);
                    } else {
                        getItem(focusedIndex).getText().setNum(1);
                    }
                }

            } else {
                getItem(focusedIndex).getText().setNum(0);
            }
            notifyItemChanged(focusedIndex);
        }

    }

    /**
     * 获取当前位置之前最近的一个Text条目
     *
     * @return
     */
    private MemoBean getLastTextItem() {
        MemoBean bean = null;
        List<MemoBean> data = getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getType() == MemoConstant.ITEM_TEXT) {
                if (i < focusedIndex) {
                    bean = data.get(i);
                }
            }
        }
        return bean;
    }

    /**
     * 添加图片
     */
    public void addImage(String path) {
        MemoBean bean = new MemoBean();
        bean.setType(MemoConstant.ITEM_IMAGE);
        MemoBean.ImgBean imgBean = new MemoBean.ImgBean();
        imgBean.setLocalPath(path);
        imgBean.setUrl(path);
        imgBean.setFrom(MemoConstant.LOCAL_FILE);
        bean.setImg(imgBean);
        MemoBean bean2 = new MemoBean();
        bean2.setType(MemoConstant.ITEM_TEXT);
        MemoBean.TextBean tb = new MemoBean.TextBean();
        if (getData().get(focusedIndex).getText().getNum() > 0) {
            tb.setNum(getData().get(focusedIndex).getText().getNum() + 1);
        } else {
            tb.setNum(0);
        }
        if (getData().get(focusedIndex).getText().getCheck() > 0) {
            tb.setCheck(1);
        } else {
            tb.setCheck(0);
        }
        bean2.setText(tb);
        tb.setContent("");
        if (focusedIndex >= 0) {
            getData().add(focusedIndex + 1, bean);
            contentChangedFlag = true;
            getData().add(focusedIndex + 2, bean2);
            focusedIndex = focusedIndex + 2;

        }
        notifyDataSetChanged();
        if (mListener != null) {
            mListener.onAdd(focusedIndex + 2);
            contentChangedFlag = true;
        }
    }

    public EditText getEditText() {
        return mEditText;
    }

    public void requestFocus(int position) {
        if (position >= 0 && position < getItemCount() - 1) {
            focusedIndex = position;
        }

    }

    /**
     * 输入控件获取焦点
     */
    public void requestInputFocus() {
        if (getData().size() <= 0 || getData().get(getItemCount() - 1).getType() != MemoConstant.ITEM_TEXT) {
            MemoBean bean = new MemoBean();
            bean.setType(MemoConstant.ITEM_TEXT);
            MemoBean.TextBean tb = new MemoBean.TextBean();
            tb.setContent("");
            tb.setCheck(0);
            tb.setNum(0);
            bean.setText(tb);
            getData().add(bean);
            focusedIndex = getData().size() - 1;
            notifyItemInserted(focusedIndex);
        } else {
            if (mEditText != null) {
                mEditText.requestFocus();
            }
        }

    }

    /**
     * 获取数据,同时将EditText中的数据赋值给item;
     *
     * @return
     */
    public List<MemoBean> getItemData() {
        if (mEditText != null) {
            Log.e(focusedIndex + "", getData().get(focusedIndex).getText().getContent() + "  ");
            getData().get(focusedIndex).getText().setContent(mEditText.getText().toString());
            Log.e(focusedIndex + "", getData().get(focusedIndex).getText().getContent() + "  ");
        }

        return getData();
    }

    public void addLocation(MemoBean memoBean) {
        getData().add(getItemCount(), memoBean);
//        notifyDataSetChanged();
        contentChangedFlag = true;
        notifyItemInserted(getItemCount() - 1);

    }

    public boolean isContentChanged() {
        return contentChangedFlag;
    }


    /**
     * 数据增加或移除监听
     */
    public interface OnItemNumChangeListener {
        void onAdd(int position);

        void onRemove(int position);
    }

    public interface OnFocusChangeListener {
        void onFocus(boolean check, boolean num);
    }

    /**
     * 焦点变化监听
     *
     * @param listener
     */
    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        mFocusListener = listener;
    }

    public void setOnItemNumChangeListener(OnItemNumChangeListener listener) {
        mListener = listener;
    }

    /**
     * 获取备忘录中图片
     *
     * @return
     */
    public List<File> getPictureFiles() {
        List<File> list = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getType() == MemoConstant.ITEM_IMAGE) {
                File file = new File(getData().get(i).getImg().getLocalPath());
                if (file.exists()) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    public void setEditState(boolean editState) {
        isEditState = editState;
    }

    public void setCreator(boolean creator) {
        isCreator = creator;
    }

    public boolean isCreator() {
        return isCreator;
    }
}