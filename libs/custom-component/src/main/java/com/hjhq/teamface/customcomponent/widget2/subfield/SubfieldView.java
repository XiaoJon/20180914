package com.hjhq.teamface.customcomponent.widget2.subfield;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.customcomponent.widget2.CustomUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 分栏控件
 * Created by lx on 2017/9/18.
 */

public class SubfieldView {
    private String moduleBean;
    private List<CustomBean> customBeanList;
    private boolean isSpread;
    private View mView;
    private TextView tvTitle;
    private LinearLayout llRoot;

    private Context context;
    private int state;
    private String title;
    //是否隐藏分栏名称
    private boolean isHideColumnName;
    private ImageView ivSpread;
    private ArrayList<BaseView> mViewList;
    private View llSubfield;

    public SubfieldView(List<CustomBean> list, int state, String title, boolean isSpread, String moduleBean) {
        this.customBeanList = list;
        this.state = state;
        this.title = title;
        this.isSpread = isSpread;
        this.moduleBean = moduleBean;
    }

    public void addView(LinearLayout parent) {
        context = parent.getContext();
        mView = View.inflate(context, R.layout.custom_item_text_subfield, null);

        llRoot = mView.findViewById(R.id.ll_layout);
        tvTitle = mView.findViewById(R.id.tv_title);
        llSubfield = mView.findViewById(R.id.ll_subfield);
        ivSpread = mView.findViewById(R.id.iv_spread);
        parent.addView(mView);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //同时隐藏分栏名称和收起分栏，优先级隐藏分栏名称
        if (isHideColumnName && !isSpread) {
            llSubfield.setVisibility(View.GONE);
        } else {
            TextUtil.setText(tvTitle, title);
            ivSpread.setSelected(isSpread);

            ivSpread.setOnClickListener(v -> {
                isSpread = !isSpread;
                if (isSpread) {
                    spread();
                } else {
                    shrink();
                }
            });
            llRoot.setVisibility(isSpread ? View.VISIBLE : View.GONE);
        }
        addWidget();
    }

    /**
     * 添加组件
     */
    public void addWidget() {
        if (customBeanList == null) {
            return;
        }

        mViewList = new ArrayList<>();
        for (CustomBean customBean : customBeanList) {
            customBean.setModuleBean(moduleBean);
            BaseView baseView = CustomUtil.drawLayout(llRoot, customBean, state);
            if (baseView != null) {
                mViewList.add(baseView);
            }
        }
    }

    /**
     * 展开
     */
    private void spread() {
        ivSpread.setSelected(true);
        llRoot.setVisibility(View.VISIBLE);
    }

    /**
     * 收缩
     */
    private void shrink() {
        ivSpread.setSelected(false);
        llRoot.setVisibility(View.GONE);
    }

    /**
     * 返回分栏下的所有组件
     *
     * @return
     */
    public ArrayList<BaseView> getViewList() {
        return mViewList;
    }

    public void setHideColumnName(boolean hideColumnName) {
        isHideColumnName = hideColumnName;
    }
}
