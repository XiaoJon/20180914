package com.hjhq.teamface.customcomponent.widget2;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.DetailResultBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.customcomponent.widget2.select.PickListView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.Map;

import rx.Observable;


/**
 * 基类组件
 *
 * @author Administrator
 */
public abstract class BaseView {
    protected int code = this.hashCode() % 10000;
    protected View mView;
    /**
     * 文本类型
     */
    protected String type;
    /**
     * 中文名称
     */
    protected String title;
    /**
     * 默认值
     */
    protected String defaultValue;
    /**
     * 提示
     */
    protected String pointOut;
    /**
     * 字段控制(0都不选 1只读 2必填)
     */
    protected String fieldControl;
    /**
     * 英文名称，key
     */
    protected String keyName;
    /**
     * 新增是否显示 0：不显示  1显示
     */
    private String addView;
    /**
     * 修改是否显示 0：不显示  1显示
     */
    private String editView;

    /**
     * 组件配置
     */
    protected CustomBean bean;

    /**
     * 是否联动
     */
    protected boolean isLinkage;
    /**
     * 所在子表单的keyName
     */
    protected String subFormName;
    /**
     * 所在子表单分栏的下标
     */
    protected int subFormIndex;


    /**
     * 组件状态 0：新增  1：编辑  2：详情
     */
    protected int state;
    protected boolean isHidenField;
    protected int aHashCode;

    public BaseView(CustomBean bean) {
        if (bean == null) {
            return;
        }
        this.bean = bean;
        this.type = bean.getType();
        this.title = bean.getLabel();
        this.keyName = bean.getName();
        CustomBean.FieldBean field = bean.getField();
        if (field != null) {
            pointOut = field.getPointOut();
            defaultValue = field.getDefaultValue();
            fieldControl = field.getFieldControl();
            addView = field.getAddView();
            editView = field.getEditView();
        }
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * 设置数据
     *
     * @param value 具体类型去子类转换
     */
    protected abstract void setData(Object value);

    /**
     * 将文本View添加到父布局
     */
    public void addView(LinearLayout parent, Activity activity) {
        aHashCode = activity.hashCode();
        RxManager.$(aHashCode).on(keyName, this::setData);
        RxManager.$(aHashCode).on(keyName + CustomConstants.LINKAGE_TAG, o -> {
            if (this instanceof PickListView) {
                ((PickListView) this).setValue(o);
            } else {
                setData(o);
            }
        });

        int childCount = parent.getChildCount();
        addView(parent, activity, childCount);
    }


    /**
     * 将文本View添加到父布局指定位置
     *
     * @param parent   父组件
     * @param activity
     * @param index    位置
     */
    public abstract void addView(LinearLayout parent, Activity activity, int index);

    public void delView(LinearLayout parent) {
        parent.removeView(mView);
        mView = null;
    }

    public void setVisibility(int visibility) {
        if (mView == null) {
            return;
        }
        mView.setVisibility(visibility);
    }


    /**
     * 设置标题
     *
     * @param textView 标题控件
     * @param title    标题
     */
    protected void setTitle(TextView textView, String title) {
        if (title == null) {
            title = "";
        }
        if (CustomConstants.FIELD_MUST.equals(fieldControl) && (CustomConstants.ADD_STATE == state || CustomConstants.EDIT_STATE == state)) {
            title = String.format("%s<font color='#F94C4A'>*</font>", title);
            textView.setText(Html.fromHtml(title));
            return;
        }
        TextUtil.setText(textView, title);
    }

    /**
     * 获取对应的view
     */
    public View getView() {
        return mView;
    }

    /**
     * 获取组建显示名称
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * 将内容设置到JSONObject中
     *
     * @param jsonObj JSONObject
     */
    public abstract void put(JSONObject jsonObj);

    /**
     * 空值判断
     *
     * @return
     */
    public abstract boolean checkNull();

    /**
     * 格式校验
     *
     * @return
     */
    public abstract boolean formatCheck();

    public String getFieldControl() {
        return fieldControl;
    }

    public String getKeyName() {
        return keyName;
    }

    public CustomBean getBean() {
        return bean;
    }

    protected RxAppCompatActivity getContext() {
        return (RxAppCompatActivity) mView.getContext();
    }

    /**
     * 设置状态隐藏
     */
    public void setStateVisible() {
        if (CustomConstants.ADD_STATE == state) {
            setVisibility("1".equals(addView) ? View.VISIBLE : View.GONE);
        } else if (CustomConstants.EDIT_STATE == state) {
            setVisibility("1".equals(editView) ? View.VISIBLE : View.GONE);
        }
    }

    public boolean getVisibility() {
        return mView.getVisibility() == View.VISIBLE;
    }


    /**
     * 设置被下拉隐藏字段 进行状态
     */
    public void setHidenFieldVisible(boolean visible) {
        mView.setVisibility(visible ? View.VISIBLE : View.GONE);
        isHidenField = true;
    }

    public boolean isHidenField() {
        return isHidenField;
    }

    /**
     * 是否是详情状态，不可编辑
     */
    protected boolean isDetailState() {
        return state == CustomConstants.DETAIL_STATE || (state == CustomConstants.APPROVE_DETAIL_STATE && CustomConstants.FIELD_READ.equals(fieldControl));
    }

    /**
     * 设置所在子表单的信息
     *
     * @param keyName
     */
    public void setSubFormInfo(String keyName, int index) {
        this.subFormName = keyName;
        this.subFormIndex = index;
        RxManager.$(aHashCode).on(this.keyName + subFormIndex, this::setData);
        RxManager.$(aHashCode).on(this.keyName + CustomConstants.LINKAGE_TAG + subFormIndex, o -> {
            if (this instanceof PickListView) {
                ((PickListView) this).setValue(o);
            } else {
                setData(o);
            }
        });
    }

    /**
     * 设置联动
     */
    public void setLinkage() {
        isLinkage = true;
    }

    /**
     * 联动数据
     */
    protected void linkageData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bean", bean.getModuleBean());
        jsonObject.put("field", keyName);
        put(jsonObject);
        jsonObject.put("value", jsonObject.get(keyName));
        jsonObject.remove(keyName);
        if (!TextUtil.isEmpty(subFormName)) {
            jsonObject.put("subform", subFormName);
            jsonObject.put("currentSubIndex", subFormIndex);
        }
        new CommonModel().getLinkageData(getContext(), jsonObject, new ProgressSubscriber<DetailResultBean>(getContext()) {
            @Override
            public void onNext(DetailResultBean baseBean) {
                super.onNext(baseBean);
                Map<String, Object> map = baseBean.getData();
                Observable.from(map.keySet()).subscribe(key -> {
                    Object o = map.get(key);
                    if (key.startsWith(CustomConstants.SUBFORM)) {
                        if (o instanceof Map) {
                            Map<String, Object> subFormMap = (Map<String, Object>) o;
                            Observable.from(subFormMap.keySet())
                                    .subscribe(name -> {
                                        if (keyName.startsWith(CustomConstants.SUBFORM) && !TextUtil.isEmpty(subFormName)) {
                                            RxManager.$(aHashCode).post(name + CustomConstants.LINKAGE_TAG + subFormIndex, subFormMap.get(name));
                                        } else {
                                            RxManager.$(aHashCode).post(name + CustomConstants.LINKAGE_TAG, subFormMap.get(name));
                                        }
                                    });
                        }
                    } else {
                        RxManager.$(aHashCode).post(key + CustomConstants.LINKAGE_TAG, o);
                    }
                });
            }
        });
    }
}

