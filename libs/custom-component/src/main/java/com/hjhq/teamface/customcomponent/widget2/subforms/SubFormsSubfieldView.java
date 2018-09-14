package com.hjhq.teamface.customcomponent.widget2.subforms;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.util.NumberFormatUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.customcomponent.R;

/**
 * 子表单分栏控件
 * Created by lx on 2017/9/18.
 */

public class SubFormsSubfieldView {
    private String fieldControl;
    private int state;
    private View mView;
    private TextView tvTitle;
    private LinearLayout llRoot;
    private TextView tvDel;

    private Context context;
    private String title;
    private int position;

    private OnSubFieldClickListener listener;

    public interface OnSubFieldClickListener {
        void onClick(int position);
    }

    public SubFormsSubfieldView(Context context, int position, int state, String fieldControl, OnSubFieldClickListener listener) {
        this.context = context;
        this.position = position;
        this.listener = listener;
        this.title = "栏目" + NumberFormatUtil.formatInteger(position);
        this.state = state;
        this.fieldControl = fieldControl;
    }

    public void addView(LinearLayout parent) {
        mView = View.inflate(context, R.layout.custom_subforms_subfield, null);

        tvTitle = mView.findViewById(R.id.tv_title);
        llRoot = mView.findViewById(R.id.ll_content);
        tvDel = mView.findViewById(R.id.tv_del);
        parent.addView(mView);

        TextUtil.setText(tvTitle, title);

        if (listener != null) {
            tvDel.setOnClickListener(v -> listener.onClick(position - 1));
        }
        if (state == CustomConstants.DETAIL_STATE || (state == CustomConstants.APPROVE_DETAIL_STATE && CustomConstants.FIELD_READ.equals(fieldControl))) {
            tvDel.setVisibility(View.GONE);
        }
    }

    public void delView(LinearLayout parent) {
        parent.removeView(mView);
        mView = null;
    }

    /**
     * 设置标题
     */
    public void setTitle(int position) {
        this.position = position;
        this.title = "栏目" + NumberFormatUtil.formatInteger(position);
        TextUtil.setText(tvTitle, title);
    }
}
