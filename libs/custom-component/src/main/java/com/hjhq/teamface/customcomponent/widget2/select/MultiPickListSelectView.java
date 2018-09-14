package com.hjhq.teamface.customcomponent.widget2.select;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.EntryBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.JsonParser;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.common.view.FlowLayout;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.util.WidgetDialogUtil;
import com.hjhq.teamface.customcomponent.widget2.BaseView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 多级下拉
 *
 * @author lx
 * @date 2017/8/23
 */

public class MultiPickListSelectView extends BaseView {
    //多级选择类型：0：2级下拉、1:3级下拉
    private String selectType;
    //默认值
    private CustomBean.DefaultEntryBean defaultEntrys;
    private List<EntryBean> entrys1 = new ArrayList<>();
    private List<EntryBean> entrys2 = new ArrayList<>();
    private List<EntryBean> entrys3 = new ArrayList<>();

    private List<EntryBean> selectEntryBean = new ArrayList<>();
    private FlowLayout flowLayout1;
    private FlowLayout flowLayout2;
    private FlowLayout flowLayout3;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private ImageView ivRight3;
    private TextView tvTitle;
    private TextView tvContent1;
    private TextView tvContent2;
    private TextView tvContent3;
    private View llContent1;
    private View llContent2;
    private View llContent3;

    public MultiPickListSelectView(CustomBean bean) {
        super(bean);
        this.entrys1 = bean.getEntrys();
        defaultEntrys = bean.getDefaultEntrys();
        CustomBean.FieldBean field = bean.getField();
        if (field != null) {
            selectType = field.getSelectType();
            if ("0".equals(selectType)) {
                for (EntryBean entry : entrys1) {
                    List<EntryBean> subList = entry.getSubList();
                    for (EntryBean entry2 : subList) {
                        entry2.setSubList(null);
                    }
                }
            }
        }
    }

    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {
        mView = View.inflate(activity, R.layout.custom_select_multi_widget_layout, null);
        parent.addView(mView);

        tvTitle = mView.findViewById(R.id.tv_title);

        tvContent1 = mView.findViewById(R.id.tv_content1);
        tvContent2 = mView.findViewById(R.id.tv_content2);
        tvContent3 = mView.findViewById(R.id.tv_content3);
        llContent1 = mView.findViewById(R.id.ll_content1);
        llContent2 = mView.findViewById(R.id.ll_content2);
        llContent3 = mView.findViewById(R.id.ll_content3);
        ivRight1 = mView.findViewById(R.id.iv_right1);
        ivRight2 = mView.findViewById(R.id.iv_right2);
        ivRight3 = mView.findViewById(R.id.iv_right3);

        flowLayout1 = mView.findViewById(R.id.pick_flow_layout1);
        flowLayout2 = mView.findViewById(R.id.pick_flow_layout2);
        flowLayout3 = mView.findViewById(R.id.pick_flow_layout3);

        initOption();
        onListener();
    }

    public void initOption() {
        if ("1".equals(selectType)) {
            llContent3.setVisibility(View.VISIBLE);
        }

        if (isDetailState()) {
            ivRight1.setVisibility(View.GONE);
            ivRight2.setVisibility(View.GONE);
            ivRight3.setVisibility(View.GONE);
            llContent1.setClickable(false);
            llContent2.setClickable(false);
            llContent3.setClickable(false);
        } else {
            if (state == CustomConstants.ADD_STATE) {
                setDefaultValue();
            }
            if (!CustomConstants.FIELD_READ.equals(fieldControl)) {
                llContent1.setOnClickListener(view -> onClick1());
                llContent2.setOnClickListener(view -> onClick2());
                llContent3.setOnClickListener(view -> onClick3());
            } else {
                llContent1.setOnClickListener(v -> ToastUtils.showError(getContext(), "只读属性不可更改"));
                llContent2.setOnClickListener(v -> ToastUtils.showError(getContext(), "只读属性不可更改"));
                llContent3.setOnClickListener(v -> ToastUtils.showError(getContext(), "只读属性不可更改"));
            }
        }

        setTitle(tvTitle, title);
        Object value = bean.getValue();
        if (value != null && !"".equals(value)) {
            setData(value);
        }
    }

    /**
     * 监听
     */
    private void onListener() {
        if (CustomConstants.DETAIL_STATE != state && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            ivRight1.setOnClickListener(v -> {
                if (flowLayout1.getChildCount() != 0) {
                    clearEntry1();
                }
            });
            ivRight2.setOnClickListener(v -> {
                if (flowLayout2.getChildCount() != 0) {
                    clearEntry2();
                }
            });
            ivRight3.setOnClickListener(v -> {
                if (flowLayout3.getChildCount() != 0) {
                    clearEntry3();
                }
            });
        }
    }

    private void onClick1() {
        RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
        if (CollectionUtils.isEmpty(entrys1)) {
            ToastUtils.showError(getContext(), "未获取到选项值");
            return;
        }
        WidgetDialogUtil.mutilDialog(getContext(), false, entrys1, llContent1, entryBeanList -> {
            Observable.from(entryBeanList).filter(EntryBean::isCheck).subscribe(entrysBean -> {
                if (selectEntryBean.size() > 0) {
                    if (!entrysBean.getValue().equals(selectEntryBean.get(0).getValue())) {
                        clearEntry2();
                    }
                    selectEntryBean.set(0, entrysBean);
                } else {
                    selectEntryBean.add(entrysBean);
                }
            });
            setOtherEntryBean();
            setPickValue(selectEntryBean);
        });
    }

    private void onClick2() {
        RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
        if (CollectionUtils.isEmpty(selectEntryBean)) {
            ToastUtils.showError(getContext(), "请先选择上一级选项");
            return;
        }
        if (CollectionUtils.isEmpty(entrys2)) {
            ToastUtils.showError(getContext(), "未获取到选项值");
            return;
        }
        WidgetDialogUtil.mutilDialog(getContext(), false, entrys2, llContent2, entryBeanList -> {
            Observable.from(entryBeanList).filter(EntryBean::isCheck).subscribe(entrysBean -> {
                if (selectEntryBean.size() > 1) {
                    if (!entrysBean.getValue().equals(selectEntryBean.get(1).getValue())) {
                        clearEntry3();
                    }
                    selectEntryBean.set(1, entrysBean);
                } else {
                    selectEntryBean.add(entrysBean);
                }
            });
            setOtherEntryBean();
            setPickValue(selectEntryBean);
        });
    }

    private void onClick3() {
        RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
        if (selectEntryBean.size() < 2) {
            ToastUtils.showError(getContext(), "请先选择上一级选项");
            return;
        }
        if (CollectionUtils.isEmpty(entrys3)) {
            ToastUtils.showError(getContext(), "未获取到选项值");
            return;
        }
        WidgetDialogUtil.mutilDialog(getContext(), false, entrys3, llContent3, entryBeanList -> {
            Observable.from(entryBeanList).filter(EntryBean::isCheck).subscribe(entrysBean -> {
                if (selectEntryBean.size() > 2) {
                    selectEntryBean.set(2, entrysBean);
                } else {
                    selectEntryBean.add(entrysBean);
                }
            });
            setOtherEntryBean();
            setPickValue(selectEntryBean);
        });
    }

    private void clearEntry1() {
        Observable.from(entrys1).subscribe(entry -> entry.setCheck(false));
        ivRight1.setImageResource(R.drawable.icon_to_next);
        flowLayout1.removeAllViews();
        tvContent1.setVisibility(View.VISIBLE);
        selectEntryBean.clear();
        clearEntry2();
    }

    private void clearEntry2() {
        Observable.from(entrys2).subscribe(entry -> entry.setCheck(false));
        ivRight2.setImageResource(R.drawable.icon_to_next);
        clearEntry3();
        flowLayout2.removeAllViews();
        tvContent2.setVisibility(View.VISIBLE);
        if (selectEntryBean.size() > 1) {
            selectEntryBean.remove(1);
        }
    }

    private void clearEntry3() {
        Observable.from(entrys3).subscribe(entry -> entry.setCheck(false));
        ivRight3.setImageResource(R.drawable.icon_to_next);
        flowLayout3.removeAllViews();
        tvContent3.setVisibility(View.VISIBLE);
        if (selectEntryBean.size() > 2) {
            selectEntryBean.remove(2);
        }
    }

    @Override
    protected void setData(Object value) {
        selectEntryBean = new JsonParser<EntryBean>().jsonFromList(value, EntryBean.class);

        setPickValue(selectEntryBean);
        setOtherEntryBean();
    }


    @Override
    public void put(JSONObject jsonObj) {
        jsonObj.put(keyName, getValue());
    }

    @Override
    public boolean checkNull() {
        return "".equals(getValue());
    }

    @Override
    public boolean formatCheck() {
        return true;
    }


    public void setDefaultValue() {
        if (defaultEntrys == null) {
            return;
        }
        if (TextUtil.isEmpty(defaultEntrys.getOneDefaultValueId())) {
            return;
        }
        EntryBean entryBean = new EntryBean(defaultEntrys.getOneDefaultValueId(), defaultEntrys.getOneDefaultValue(), defaultEntrys.getOneDefaultValueColor());
        selectEntryBean.add(entryBean);
        if (!TextUtil.isEmpty(defaultEntrys.getTwoDefaultValueId())) {
            entryBean = new EntryBean(defaultEntrys.getTwoDefaultValueId(), defaultEntrys.getTwoDefaultValue(), defaultEntrys.getTwoDefaultValueColor());
            selectEntryBean.add(entryBean);
            if (!TextUtil.isEmpty(defaultEntrys.getThreeDefaultValueId())) {
                entryBean = new EntryBean(defaultEntrys.getThreeDefaultValueId(), defaultEntrys.getThreeDefaultValue(), defaultEntrys.getThreeDefaultValueColor());
                selectEntryBean.add(entryBean);
            }
        }
        setPickValue(selectEntryBean);
        setOtherEntryBean();
    }

    /**
     * 设置二三级的选项
     */
    private void setOtherEntryBean() {
        if (selectEntryBean.size() > 0) {
            Observable.from(entrys1)
                    .filter(entry1 -> entry1.getValue().equals(selectEntryBean.get(0).getValue()))
                    .subscribe(entry1 -> {
                        entry1.setCheck(true);
                        entrys2.clear();
                        CollectionUtils.addAll(entrys2, entry1.getSubList());
                    });
        }
        if (selectEntryBean.size() > 1) {
            Observable.from(entrys2)
                    .filter(entry2 -> entry2.getValue().equals(selectEntryBean.get(1).getValue()))
                    .subscribe(entry2 -> {
                        entry2.setCheck(true);
                        entrys3.clear();
                        CollectionUtils.addAll(entrys3, entry2.getSubList());
                    });
        }
        if (selectEntryBean.size() > 2) {
            Observable.from(entrys3)
                    .filter(entry3 -> entry3.getValue().equals(selectEntryBean.get(2).getValue()))
                    .subscribe(entry3 -> entry3.setCheck(true));
        }
    }

    public Object getValue() {
        return CollectionUtils.isEmpty(selectEntryBean) ? "" : selectEntryBean;
    }


    /**
     * 设置选项值
     *
     * @param labels 集合
     */
    protected void setPickValue(List<EntryBean> labels) {
        flowLayout1.removeAllViews();
        tvContent1.setVisibility(View.VISIBLE);
        flowLayout2.removeAllViews();
        tvContent1.setVisibility(View.VISIBLE);
        flowLayout3.removeAllViews();
        tvContent1.setVisibility(View.VISIBLE);
        for (int i = 0; i < labels.size(); i++) {
            EntryBean entryBean = labels.get(i);
            String color = entryBean.getColor();
            TextView view = new TextView(mView.getContext());
            view.setText(entryBean.getLabel());
            view.setTextSize(14);
            view.setGravity(Gravity.CENTER);
            if (TextUtil.isEmpty(color)) {
                view.setTextColor(ColorUtils.resToColor(getContext(), R.color.custom_content_color));
            } else {
                view.setTextColor(Color.WHITE);
                view.setPadding(12, 4, 12, 4);
                view.setBackgroundResource(R.drawable.custom_flow_label);
                GradientDrawable myGrad = (GradientDrawable) view.getBackground();
                if ("#FFFFFF".equals(color)) {
                    view.setTextColor(ColorUtils.resToColor(getContext(), R.color.black_4a));
                }
                myGrad.setColor(ColorUtils.hexToColor(entryBean.getColor(), "#000000"));
            }
            switch (i) {
                case 0:
                    tvContent1.setVisibility(View.GONE);
                    flowLayout1.addView(view);
                    break;
                case 1:
                    tvContent2.setVisibility(View.GONE);
                    flowLayout2.addView(view);
                    break;
                case 2:
                    tvContent3.setVisibility(View.GONE);
                    flowLayout3.addView(view);
                    break;
            }
        }

        if (CustomConstants.DETAIL_STATE != state && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            if (CollectionUtils.isEmpty(labels)) {
                ivRight1.setImageResource(R.drawable.icon_to_next);
                ivRight2.setImageResource(R.drawable.icon_to_next);
                ivRight3.setImageResource(R.drawable.icon_to_next);
            } else if (labels.size() == 1) {
                ivRight1.setImageResource(R.drawable.clear_button);
                ivRight2.setImageResource(R.drawable.icon_to_next);
                ivRight3.setImageResource(R.drawable.icon_to_next);
            } else if (labels.size() == 2) {
                ivRight1.setImageResource(R.drawable.clear_button);
                ivRight2.setImageResource(R.drawable.clear_button);
                ivRight3.setImageResource(R.drawable.icon_to_next);
            } else {
                ivRight1.setImageResource(R.drawable.clear_button);
                ivRight2.setImageResource(R.drawable.clear_button);
                ivRight3.setImageResource(R.drawable.clear_button);
            }
        }
    }

}
