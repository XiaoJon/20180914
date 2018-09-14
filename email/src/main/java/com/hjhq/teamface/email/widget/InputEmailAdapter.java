package com.hjhq.teamface.email.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.FormValidationUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.presenter.NewEmailActivity;

import java.util.List;


public class InputEmailAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {

    String tempText = "";
    EditText et;
    Context mContext;
    EmailAddressView mView;
    boolean needRequestFoucus = false;

    public InputEmailAdapter(List<Member> data) {
        super(R.layout.email_input_address_item, data);
    }

    public InputEmailAdapter(Context context, EmailAddressView parent, List<Member> data) {
        super(R.layout.email_input_address_item, data);
        this.mContext = context;
        this.mView = parent;
    }


    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        RelativeLayout root = helper.getView(R.id.rl_root);


        LogUtil.e("EditText == InputEmailAdapter");
        LogUtil.e("EditText root 宽->" + root.getLayoutParams().width);
        LogUtil.e("EditText root 高->" + root.getLayoutParams().height);

        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.text, true);
            helper.setVisible(R.id.et_content, false);
            helper.setVisible(R.id.name, false);
            helper.setVisible(R.id.star, true);
            if (item.isCheck()) {
                helper.getView(R.id.star).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.star).setVisibility(View.INVISIBLE);
            }


            helper.setText(R.id.text, item.getEmployee_name());

        } else if (helper.getAdapterPosition() == getItemCount() - 1) {
            helper.setVisible(R.id.et_content, true);
            helper.setVisible(R.id.name, false);
            helper.setVisible(R.id.text, false);
            helper.setVisible(R.id.star, false);
            et = helper.getView(R.id.et_content);
            et.setText(tempText);
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    mView.onFocusChange(v, hasFocus);
                    if (!TextUtils.isEmpty(et.getText().toString())) {
                        addData(et.getText().toString(), true);
                    }

                }
            });
            tempText = "";
            if (getItemCount() == 2) {
                et.setHint(item.getEmployee_name());
            } else {
                et.setHint("");
            }
            if (needRequestFoucus) {
                et.requestFocus();
            }
            if (getItemCount() > 2) {
                et.requestFocus();
            }
            LogUtil.e("文字变化前->" + ScreenUtils.getScreenWidth(et.getContext()));
            LogUtil.e("EditText 左->" + et.getLeft());
            LogUtil.e("EditText 上->" + et.getTop());
            LogUtil.e("EditText 右->" + et.getRight());
            LogUtil.e("EditText 下->" + et.getBottom());

            float v = ScreenUtils.getScreenWidth(et.getContext()) - et.getLeft() - DeviceUtils.dpToPixel(et.getContext(), 50);
            et.setLayoutParams(new RelativeLayout.LayoutParams(((int) v), ViewGroup.LayoutParams.MATCH_PARENT));
            et.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL || keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (event.getAction() == KeyEvent.ACTION_UP && TextUtils.isEmpty(et.getText())) {
                            if (getItemCount() <= 2) {
                                return true;
                            }
                            if (getItemCount() > 2) {
                                int index = getItemCount() - 2;
                                if (getItem(index).isCheck()) {
                                    remove(getItemCount() - 2);
                                    if (mOnDataSetChangeListener != null) {
                                        mOnDataSetChangeListener.onChange();
                                    }
                                    tempText = "";
                                    if (needRequestFoucus) {
                                        et.requestFocus();
                                    }
                                    return true;
                                }
                                for (int i = 1; i < getData().size(); i++) {
                                    if (getData().get(i).isCheck()) {
                                        index = i;
                                        break;
                                    }
                                }
                                if (index == getItemCount() - 2) {
                                    if (getData().get(index).isCheck()) {
                                        remove(getItemCount() - 2);
                                        if (mOnDataSetChangeListener != null) {
                                            mOnDataSetChangeListener.onChange();
                                        }
                                        if (needRequestFoucus) {
                                            et.requestFocus();
                                        }
                                        return true;
                                    } else {
                                        for (int i = 0; i < getData().size(); i++) {
                                            getData().get(i).setCheck(false);
                                        }
                                        getData().get(index).setCheck(true);
                                        if (needRequestFoucus) {
                                            et.requestFocus();
                                        }
                                        notifyItemChanged(getItemCount() - 2);
                                        return true;
                                    }
                                } else if (index < getItemCount() - 2 && index != 0) {
                                    remove(index);
                                    if (mOnDataSetChangeListener != null) {
                                        mOnDataSetChangeListener.onChange();
                                    }
                                    if (needRequestFoucus) {
                                        et.requestFocus();
                                    }
                                    return true;
                                   /* if (index == getItemCount() - 1) {
                                        et.requestFocus();
                                        return true;
                                    } else {
                                        remove(index);
                                        return true;
                                    }*/
                                }
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    LogUtil.e("文字变化前->" + s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    LogUtil.e("文字变化中->" + s.toString());
                    if (et.getLineCount() >= 2) {
                        ViewGroup.LayoutParams layoutParams = et.getLayoutParams();
                        layoutParams.width = -1;
                        et.setLayoutParams(layoutParams);
                        et.requestLayout();
                        tempText = et.getText().toString().trim();
                    } else {
                        tempText = "";
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    LogUtil.e("文字变化后->" + s.toString());
                    if (s.length() > 0 && (s.toString().endsWith(" ") || s.toString().endsWith(",") || s.toString().endsWith("\n"))) {
                        if (TextUtils.isEmpty(s.toString().trim()) || ",".equals(s.toString())) {
                            return;
                        }
                        if (s.toString().endsWith(",")) {
                            s = s.delete(s.length() - 1, s.length() - 1);
                        }
                        addData(s.toString(), true);
                        return;
                    } else if (et.getTextSize() * s.length() > et.getWidth()) {
                        LogUtil.e("EditText宽度->" + s.toString());

                        //java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling
                        if (getRecyclerView() != null && !getRecyclerView().isComputingLayout() && getRecyclerView().isLayoutFrozen()) {
                            notifyDataSetChanged();
                            tempText = et.getText().toString().trim();
                            return;
                        }

                    }

                    if (s.length() >= 2) {
                        ((NewEmailActivity) mContext).showRelevant(mView, s.toString().trim());
                    }
                    if (s.length() <= 0) {
                        ((NewEmailActivity) mContext).hideRelevant(mView, s.toString().trim());
                    }

                }
            });
        } else {
            //helper.addOnClickListener(R.id.rl_root);
            TextView name = helper.getView(R.id.name);
            helper.setVisible(R.id.et_content, false);
            helper.setVisible(R.id.star, false);
            // TODO: 2018/1/25 删除选中,格式是否正确
            helper.setVisible(R.id.name, true);
            helper.setVisible(R.id.text, false);
            if (TextUtils.isEmpty(item.getEmail())) {
                try {
                    remove(helper.getAdapterPosition());
                    if (getData().size() < 2) {
                        getData().add(new Member());
                    }
                    notifyDataSetChanged();
                   // notifyItemRemoved(helper.getAdapterPosition());
                    if (mOnDataSetChangeListener != null) {
                        mOnDataSetChangeListener.onChange();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            String email = item.getEmail().replace(" ", "");
            if (!FormValidationUtils.isEmail(email)) {
                //helper.setBackgroundRes(R.id.name, R.drawable.email_format_no);
                helper.setBackgroundRes(R.id.name, R.drawable.email_format_normal);
                helper.setTextColor(R.id.name, Color.parseColor("#FF6260"));
            } else {
                //helper.setBackgroundRes(R.id.name, R.drawable.email_format_ok);
                helper.setBackgroundRes(R.id.name, R.drawable.email_format_normal);
                helper.setTextColor(R.id.name, Color.parseColor("#008FE5"));
            }
            if (item.isCheck()) {
                helper.setBackgroundRes(R.id.name, R.drawable.email_format_check);
                helper.setTextColor(R.id.name, Color.parseColor("#FFFFFF"));
            }
            if (TextUtils.isEmpty(item.getEmployee_name())) {
                //name.setMaxEms(10);
                name.setLines(1);
                if (name.getLineCount() >= 2) {
                    root.requestLayout();
                }
                helper.setText(R.id.name, email + "");
            } else {
                helper.setText(R.id.name, item.getEmployee_name() + email + "");
                // name.setMaxEms(4);
                name.setLines(1);
                if (name.getLineCount() >= 2) {
                    root.requestLayout();
                }
            }
        }
    }

    private boolean addData(String s, boolean needFocus) {
        s = s.trim();
        needRequestFoucus = needFocus;
        Member m = new Member();
        m.setEmployee_name("");
        StringBuilder sb = new StringBuilder(s.toString().trim());
        StringBuilder sb2 = new StringBuilder(sb.reverse().toString().trim());
        m.setEmail(sb2.reverse().toString());
        et.setText("");
        tempText = "";
        if (getItemCount() >= 3) {
            if (getData().get(getData().size() - 2).getEmail().equals(m.getEmail())) {
                tempText = s.toString().replace(" ", "");
                //CommonUtil.showToast("联系人已存在");
                return true;
            }
        }
        //去掉重复
        boolean flag = true;
        for (int i = 0; i < getData().size(); i++) {
            if (!TextUtils.isEmpty(getData().get(i).getEmail()) && getData().get(i).getEmail().equals(m.getEmail())) {
                flag = false;
            }
        }
        if (flag) {
            getData().add(getItemCount() - 1, m);
            notifyDataSetChanged();
        }
        return false;
    }


    public String getTempText() {
        String text = "";
        if (et != null) {
            text = et.getText().toString().trim();
        }
        return text;
    }

    public void setTempText(String text) {
        if (TextUtils.isEmpty(text)) {
            tempText = "";
        } else {
            tempText = text;
        }
    }

    /**
     * 获取焦点
     */
    public void requestFocus() {
        if (et != null) {
            et.requestFocus();
        }

    }

    public void collectData() {
        if (et != null && !TextUtils.isEmpty(et.getText().toString())) {
            addData(et.getText().toString().trim(), true);
        }

    }

    public void clearFocus() {
        if (et != null) {
            et.clearFocus();
        }
    }

    private OnDataSetChangeListener mOnDataSetChangeListener;

    public interface OnDataSetChangeListener {
        void onChange();
    }

    public void setOnDataSetChangedListener(OnDataSetChangeListener listener) {
        this.mOnDataSetChangeListener = listener;
    }
}