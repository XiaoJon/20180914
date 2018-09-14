package com.hjhq.teamface.custom.ui.add;

import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.customcomponent.widget2.subfield.SubfieldView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 新增自定义 视图
 * Created by lx on 2017/9/4.
 */

public class AddCustomDelegate extends AppDelegate {
    List<SubfieldView> mViewList = new ArrayList<>();

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_layout_linear;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.drawable.text_cancel);

        setRightMenuTexts(R.color.main_green, "保存");
    }

    /**
     * 绘制布局
     *
     * @param layoutBeanList 布局
     * @param detailMap      数据
     * @param state          0新增 1详情 2编辑
     * @param moduleBean       模块bean
     */
    public void drawLayout(CustomLayoutResultBean.DataBean layoutBeanList, HashMap detailMap, int state, String moduleBean) {
        if (layoutBeanList == null) {
            return;
        }

        LinearLayout llContent = get(R.id.ll_custom_layout);
        String title = layoutBeanList.getTitle();
        setTitle(title);
        List<CustomLayoutResultBean.DataBean.LayoutBean> layout = layoutBeanList.getLayout();
        if (layout == null) {
            return;
        }
        mViewList.clear();
        for (CustomLayoutResultBean.DataBean.LayoutBean layoutBean : layout) {
            boolean isTerminalApp = "1".equals(layoutBean.getTerminalApp());
            boolean isHideInCreate = "1".equals(layoutBean.getIsHideInCreate());
            boolean isSpread = "0".equals(layoutBean.getIsSpread());
            boolean isHideColumnName = "1".equals(layoutBean.getIsHideColumnName());
            if (!isTerminalApp || isHideInCreate) {
                //不是app端或新建时隐藏
                continue;
            }

            List<CustomBean> list = layoutBean.getRows();
            if (detailMap != null) {
                for (CustomBean customBean : list) {
                    Object o = detailMap.get(customBean.getName());
                    customBean.setValue(o);
                }
            }

            SubfieldView subfieldView = new SubfieldView(list, state, layoutBean.getTitle(), isSpread, moduleBean);
            subfieldView.setHideColumnName(isHideColumnName);
            subfieldView.addView(llContent);
            mViewList.add(subfieldView);
        }
    }

}

