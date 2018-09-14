package com.hjhq.teamface.customcomponent.widget2.select;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.EntryBean;
import com.hjhq.teamface.basis.bean.HidenFieldBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.JsonParser;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.TextWatcherUtil;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.common.view.FlowLayout;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.util.WidgetDialogUtil;
import com.hjhq.teamface.customcomponent.widget2.base.SelectCommonView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static rx.Observable.from;


/**
 * 下拉选择
 *
 * @author lx
 * @date 2017/8/23
 */

public class PickListView extends SelectCommonView {

    private FlowLayout flowLayout;
    private View rlFlowLayout;
    private List<EntryBean> defaultEntrys;
    /**
     * 当前下拉选项集合
     */
    private List<EntryBean> entrys;
    /**
     * 未改变的选项，即从布局中取出的选项，不受字段依赖影响
     */
    private List<EntryBean> unChangedEntrys;
    protected List<EntryBean> checkEntrys = new ArrayList<>();

    /**
     * 是否使用流式布局
     */
    private boolean isFlow;

    /**
     * 是否多选  选择类型：0单选、1多选
     */
    private boolean isMulti;

    public PickListView(CustomBean bean) {
        super(bean);
        CustomBean.FieldBean field = bean.getField();
        this.entrys = bean.getEntrys();
        this.unChangedEntrys = this.entrys;
        if (field != null) {
            defaultEntrys = field.getDefaultEntrys();
            isMulti = "1".equals(field.getChooseType());
        }
    }

    @Override
    public int getLayout() {
        return R.layout.custom_select_single_widget_layout;
    }

    @Override
    public void initView() {
        flowLayout = mView.findViewById(R.id.pick_flow_layout);
        rlFlowLayout = mView.findViewById(R.id.rl_flow_layout);
        super.initView();
    }

    @Override
    public void initOption() {
        //下拉映射
        RxManager.$(aHashCode).onSticky(CustomConstants.CONTROL_FIELD_TAG + keyName, entrys -> controlField(entrys));
        //下拉映射并清除值
        RxManager.$(aHashCode).onSticky(CustomConstants.CONTROL_FIELD_CLEAR_TAG + keyName, entrys -> {
            controlField(entrys);
            tvContent.setText("");
            flowLayout.removeAllViews();
        });
        //清除下拉控制
        RxManager.$(aHashCode).on(CustomConstants.CLEAR_FIELD_CONTROL_TAG + keyName, bean -> this.entrys = this.unChangedEntrys);


        if (CustomConstants.DETAIL_STATE != state && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            tvContent.addTextChangedListener(new TextWatcherUtil.MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    if (TextUtil.isEmpty(s.toString())) {
                        ivRight.setImageResource(R.drawable.icon_to_next);
                    } else {
                        ivRight.setImageResource(R.drawable.clear_button);
                    }
                }
            });
            ivRight.setOnClickListener(v -> {
                String content = tvContent.getText().toString();
                if (!TextUtil.isEmpty(content) || flowLayout.getChildCount() != 0) {
                    Observable.from(checkEntrys).subscribe(checkEntrys -> {
                        String controlField = checkEntrys.getControlField();
                        if (!TextUtil.isEmpty(controlField)) {
                            RxManager.$(aHashCode).post(CustomConstants.CLEAR_FIELD_CONTROL_TAG + controlField, null);
                        }
                        List<HidenFieldBean> hidenFields = checkEntrys.getHidenFields();
                        //使用克隆避免重复添加keyName
                        if (hidenFields != null) {
                            RxManager.$(aHashCode).post(CustomConstants.CLEAR_FIELD_HIDE_TAG + getContext().toString(), hidenFields);
                        }
                    });
                    clear();
                }
            });
        }
    }

    /**
     * 字段映射
     *
     * @param entrys
     */
    private void controlField(Object entrys) {
        this.entrys = (List<EntryBean>) entrys;
        for (EntryBean entry : this.entrys) {
            Observable.from(this.unChangedEntrys)
                    .filter(unChangeEntry -> entry.getValue().equals(unChangeEntry.getValue()))
                    .subscribe(unChangeEntry -> {
                        entry.setControlField(unChangeEntry.getControlField());
                        entry.setHidenFields(unChangeEntry.getHidenFields());
                        entry.setRelyonList(unChangeEntry.getRelyonList());
                        entry.setSubList(unChangeEntry.getSubList());
                        entry.setCheck(unChangeEntry.isCheck());
                        entry.setLabel(unChangeEntry.getLabel());
                        entry.setColor(unChangeEntry.getColor());
                    });
        }
    }

    /**
     * 清除选值
     */
    public void clear() {
        checkEntrys.clear();
        tvContent.setText("");
        flowLayout.removeAllViews();
        for (EntryBean entry : entrys) {
            entry.setCheck(false);
        }
    }

    @Override
    protected void setData(Object value) {
        setValue(value);
        controlAndHind(true);
    }


    public void setValue(Object value) {
        checkEntrys = new JsonParser<EntryBean>().jsonFromList(value, EntryBean.class);

        //设置成选中
        for (EntryBean entry : entrys) {
            for (EntryBean checkEntry : checkEntrys) {
                checkEntry.setCheck(true);
                if (entry.getValue().equals(checkEntry.getValue())) {
                    entry.setCheck(true);
                    checkEntry.setHidenFields(entry.getHidenFields());
                    checkEntry.setRelyonList(entry.getRelyonList());
                    checkEntry.setControlField(entry.getControlField());
                }
            }
        }
        setPickValue(checkEntrys);
    }

    @Override
    public void onClick() {
        super.onClick();
        if (entrys == null) {
            entrys = new ArrayList<>();
        }
        SoftKeyboardUtils.hide(llRoot);
        List<EntryBean> clone = (List<EntryBean>) ((ArrayList) entrys).clone();
        WidgetDialogUtil.mutilDialog(getContext(), isMulti, clone, llRoot, entryBeanList -> {
            entrys = entryBeanList;
            //清除之前被隐藏的组件
            if (!CollectionUtils.isEmpty(checkEntrys)) {
                List<HidenFieldBean> hidenFields = checkEntrys.get(0).getHidenFields();
                if (!CollectionUtils.isEmpty(hidenFields)) {
                    RxManager.$(aHashCode).post(CustomConstants.CLEAR_FIELD_HIDE_TAG + getContext().toString(), hidenFields);
                }
            }
            checkEntrys.clear();
            Observable.from(entrys).filter(entrysBean -> entrysBean.isCheck()).subscribe(entrysBean -> checkEntrys.add(entrysBean));
            setPickValue(checkEntrys);
            controlAndHind(false);
            if (!isMulti && !checkNull() && isLinkage) {
                linkageData();
            }
        });
    }

    @Override
    public boolean formatCheck() {
        return true;
    }

    @Override
    public void setDefaultValue() {
        if (CollectionUtils.isEmpty(entrys) || CollectionUtils.isEmpty(defaultEntrys)) {
            return;
        }
        for (EntryBean entry : entrys) {
            Observable.from(defaultEntrys).filter(bean -> bean.getValue().equals(entry.getValue())).subscribe(bean -> {
                entry.setCheck(true);
                checkEntrys.add(entry);
            });
        }
        setPickValue(checkEntrys);
        controlAndHind(true);
    }

    /**
     * 控制和隐藏 其他组件
     *
     * @param isAuth 是否是自动赋值
     */
    private void controlAndHind(boolean isAuth) {
        if (isMulti || CollectionUtils.isEmpty(checkEntrys)) {
            return;
        }
        EntryBean entryBean = checkEntrys.get(0);
        String controlField = entryBean.getControlField();
        if (!TextUtil.isEmpty(controlField)) {
            List<EntryBean> relyonList = entryBean.getRelyonList();
            //避免和其他通知冲突 使用 keyName+code 作为键
            if (isAuth) {
                RxManager.$(aHashCode).post(CustomConstants.CONTROL_FIELD_TAG + controlField, relyonList);
            } else {
                RxManager.$(aHashCode).post(CustomConstants.CONTROL_FIELD_CLEAR_TAG + controlField, relyonList);
            }
        }
        ArrayList<HidenFieldBean> hidenFields = (ArrayList<HidenFieldBean>) entryBean.getHidenFields();
        //使用克隆避免重复添加keyName
        if (hidenFields != null) {
            ArrayList<HidenFieldBean> clone = (ArrayList<HidenFieldBean>) hidenFields.clone();
            clone.add(0, new HidenFieldBean(keyName, title));
            if (isAuth) {
                RxManager.$(aHashCode).post(getContext().toString(), clone);
            } else {
                //手动选值需要将其他 下拉控制主键清空
                RxManager.$(aHashCode).post(CustomConstants.CLEAR_FIELD_VALUE_TAG + getContext().toString(), clone);
            }
        }
    }

    @Override
    public Object getValue() {
        return CollectionUtils.isEmpty(checkEntrys) ? "" : checkEntrys;
    }

    /**
     * 设置选项值
     *
     * @param list 集合
     */
    protected void setPickValue(List<EntryBean> list) {
        isFlow = false;
        from(list)
                .filter(entrysBean -> !TextUtils.isEmpty(entrysBean.getColor()))
                .subscribe(entry -> isFlow = true);

        tvContent.setVisibility(isFlow ? View.GONE : View.VISIBLE);
        rlFlowLayout.setVisibility(isFlow ? View.VISIBLE : View.GONE);

        if (isFlow) {
            initFlowLayout(flowLayout, list);
        } else {
            StringBuilder label = new StringBuilder();
            for (EntryBean entry : list) {
                label.append(entry.getLabel() + "/");
            }
            if (!TextUtils.isEmpty(label.toString())) {
                String substring = label.substring(0, label.length() - 1);
                TextUtil.setText(tvContent, substring);
            }
        }
    }

    /**
     * 初始化流式布局
     */
    private void initFlowLayout(FlowLayout mFragmentFileDetailFlowLayout,
                                List<EntryBean> labels) {

        mFragmentFileDetailFlowLayout.removeAllViews();
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        if (labels != null) {
            for (EntryBean entryBean : labels) {
                TextView view = new TextView(mView.getContext());
                view.setPadding(12, 4, 12, 4);
                view.setText(entryBean.getLabel());
                view.setTextColor(Color.WHITE);
                view.setTextSize(14);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.custom_flow_label);
                GradientDrawable myGrad = (GradientDrawable) view.getBackground();
                if ("#FFFFFF".equals(entryBean.getColor())) {
                    view.setTextColor(ColorUtils.resToColor(getContext(), R.color.black_4a));
                }
                myGrad.setColor(ColorUtils.hexToColor(entryBean.getColor(), "#000000"));
                mFragmentFileDetailFlowLayout.addView(view, lp);
            }
        }

        if (CustomConstants.DETAIL_STATE != state && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            if (CollectionUtils.isEmpty(labels)) {
                ivRight.setImageResource(R.drawable.icon_to_next);
            } else {
                ivRight.setImageResource(R.drawable.clear_button);
            }
        }
    }


    public boolean isMulti() {
        return isMulti;
    }

    public List<EntryBean> getCheckEntrys() {
        return checkEntrys;
    }
}
