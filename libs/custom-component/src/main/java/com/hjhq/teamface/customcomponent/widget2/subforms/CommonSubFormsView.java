package com.hjhq.teamface.customcomponent.widget2.subforms;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.customcomponent.widget2.CustomUtil;
import com.hjhq.teamface.customcomponent.widget2.base.InputCommonView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 通用子表单
 *
 * @author lx
 * @date 2017/10/9
 */

public class CommonSubFormsView extends BaseView {
    protected LinearLayout llContent;
    protected TextView tvTitle;
    protected TextView tvDescription;
    protected LinearLayout llRoot;
    protected ImageView ivRight;
    protected List<JSONObject> subFormsValue = new ArrayList<>();
    private List<List<BaseView>> mViewList = new ArrayList<>();
    private List<SubFormsSubfieldView> subfieldList = new ArrayList<>();

    public CommonSubFormsView(CustomBean bean) {
        super(bean);
    }

    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {
        mView = View.inflate(activity, R.layout.custom_item_subforms_view, null);
        llContent = mView.findViewById(R.id.ll_layout);
        tvTitle = mView.findViewById(R.id.tv_title);
        tvDescription = mView.findViewById(R.id.tv_description);

        llRoot = mView.findViewById(R.id.ll_content);
        ivRight = mView.findViewById(R.id.iv_right);

        initView();
        parent.addView(mView);
    }

    private void initView() {
        setTitle(tvTitle, title);
        setData(bean.getValue());
        if (isDetailState()) {
            tvDescription.setVisibility(View.GONE);
        } else {
            tvDescription.setOnClickListener(v -> addSubForms(true));
        }

    }

    @Override
    protected void setData(Object value) {
        if (value == null || "".equals(value)) {
            addSubForms(false);
            return;
        }
        try {
            subFormsValue.clear();
            if (value instanceof List) {
                subFormsValue = JSONArray.parseArray(JSON.toJSONString(value), JSONObject.class);
            }
            for (JSONObject jsonObject : subFormsValue) {
                addSubForms(jsonObject, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(JSONObject jsonObj) {
        jsonObj.put(keyName, getValue());
    }

    public Object getValue() {
        JSONArray jsonArray = new JSONArray();
        getSubFormsValue(jsonArray);
        if (jsonArray.size() == 0) {
            return "";
        }
        return jsonArray;
    }

    @Override
    public boolean checkNull() {
        return true;
    }

    @Override
    public boolean formatCheck() {
        return getSubFormsValue(new JSONArray());
    }

    private boolean getSubFormsValue(JSONArray jsonArray) {
        try {
            Map<String, Set<String>> map = new HashMap<>();
            for (List<BaseView> listView : mViewList) {
                JSONObject json = new JSONObject();
                for (BaseView view : listView) {
                    CustomBean bean = view.getBean();
                    if (!view.getVisibility() && view.isHidenField()) {
                        //被下拉组件隐藏的需要返还空字符串
                        json.put(view.getKeyName(), "");
                        continue;
                    }
                    boolean must = CustomConstants.FIELD_MUST.equals(view.getFieldControl());

                    //新增时显示，不能是只读，需要必填 时为空 检测才不通过
                    if (view.checkNull() && must) {
                        ToastUtils.showError(getContext(), bean.getLabel() + "不能为空");
                        return false;
                    } else if (view.formatCheck()) {
                        view.put(json);
                    } else {
                        return false;
                    }

                    String repeatCheck = bean.getField().getRepeatCheck();
                    if ("2".equals(repeatCheck)) {
                        if (!"".equals(((InputCommonView) view).getValue())) {
                            //不等于空，不允许重复
                            Set<String> stringSet = map.get(bean.getName());
                            if (stringSet == null) {
                                stringSet = new HashSet<>();
                                map.put(bean.getName(), stringSet);
                            }
                            boolean add = stringSet.add(((InputCommonView) view).getValue() + "");
                            if (!add) {
                                ToastUtils.showError(getContext(), getTitle() + "的" + view.getTitle() + "组件值重复");
                                return false;
                            }
                        }
                    }
                }
                jsonArray.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showError(getContext(), "数据异常");
            return false;
        }
        return true;
    }

    public void addSubForms(boolean isAdd) {
        addSubForms(null, isAdd);
    }

    /**
     * 添加子表单 子项
     *
     * @param jsonObject 值
     * @param isAdd      是否是手动新增的子表单分栏
     */
    public void addSubForms(JSONObject jsonObject, boolean isAdd) {
        List<CustomBean> componentList = bean.getComponentList();
        if (componentList == null) {
            return;
        }

        SubFormsSubfieldView subFormsSubfieldView = new SubFormsSubfieldView(getContext(), mViewList.size() + 1, state, fieldControl, position -> {
            List<BaseView> baseViews = mViewList.get(position);
            for (BaseView view : baseViews) {
                view.delView(llContent);
            }
            mViewList.remove(position);

            subfieldList.get(position).delView(llContent);
            subfieldList.remove(position);

            for (int i = 0; i < subfieldList.size(); i++) {
                SubFormsSubfieldView subfieldView = subfieldList.get(i);
                subfieldView.setTitle(i + 1);
            }
        });
        subFormsSubfieldView.addView(llContent);

        List<BaseView> list = new ArrayList<>();
        for (CustomBean component : componentList) {
            component.setModuleBean(bean.getModuleBean());

            component.setValue("");
            if (jsonObject != null) {
                Object o = jsonObject.get(component.getName());
                component.setValue(o);
            }
            BaseView baseView;
            if (isAdd) {
                baseView = CustomUtil.drawLayout(llContent, component, CustomConstants.ADD_STATE);
                RxManager.$(hashCode()).post(CustomConstants.MESSAGE_SUBFORM_LINKAGE_CODE, baseView);
            } else {
                baseView = CustomUtil.drawLayout(llContent, component, state);
            }
            if (baseView != null) {
                baseView.setSubFormInfo(keyName, subfieldList.size());
                list.add(baseView);
            }
        }
        mViewList.add(list);
        subfieldList.add(subFormsSubfieldView);
    }

    public List<List<BaseView>> getViewList() {
        return mViewList;
    }
}
