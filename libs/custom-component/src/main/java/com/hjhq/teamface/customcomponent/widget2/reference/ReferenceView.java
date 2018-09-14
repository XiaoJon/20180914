package com.hjhq.teamface.customcomponent.widget2.reference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.ModuleAuthBean;
import com.hjhq.teamface.basis.bean.ReferDataTempResultBean;
import com.hjhq.teamface.basis.bean.RowBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.TextWatcherUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.AuthHelper;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.customcomponent.widget2.CustomUtil;
import com.hjhq.teamface.customcomponent.widget2.ReferenceViewInterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 关联组件
 *
 * @author xj
 * @date 2017/10/11
 */

public class ReferenceView extends BaseView implements ActivityPresenter.OnActivityResult, View.OnClickListener {
    private CustomBean.RelevanceModule relevanceModule;
    private TextView tvTitle;
    private LinearLayout llRoot;
    protected TextView tvContent;
    private ImageView ivRight;


    protected String value;
    private String referenceFieldName;
    private ReferenceViewInterface referenceViewInterface;
    private ReferDataTempResultBean.DataBean checkItem;

    public ReferenceView(CustomBean bean) {
        super(bean);
        CustomBean.RelevanceField relevanceField = bean.getRelevanceField();
        relevanceModule = bean.getRelevanceModule();
        if (relevanceField != null) {
            referenceFieldName = relevanceField.getFieldName();
        }
    }


    public void setReferenceViewInterface(ReferenceViewInterface referenceViewInterface) {
        this.referenceViewInterface = referenceViewInterface;
    }

    @Override
    public void addView(LinearLayout parent, Activity activity, int code) {
        mView = View.inflate(activity, R.layout.custom_select_single_widget_layout, null);

        tvTitle = mView.findViewById(R.id.tv_title);
        ivRight = mView.findViewById(R.id.iv_right);

        llRoot = mView.findViewById(R.id.ll_content);
        tvContent = mView.findViewById(R.id.tv_content);

        onListener();
        initView();
        parent.addView(mView);
    }


    /**
     * 初始化组件
     */
    protected void initView() {
        ivRight.setImageResource(R.drawable.custom_icon_reference);
        setTitle(tvTitle, title);
        setData(bean.getValue());
        llRoot.setOnClickListener(this);

        if (isDetailState()) {
            TextUtil.setHint(tvContent, "");
            ivRight.setVisibility(View.GONE);
            tvContent.setTextColor(ColorUtils.resToColor(getContext(), R.color.app_blue));
            if (TextUtil.isEmpty(value)) {
                llRoot.setClickable(false);
            }
        } else {
            if (CustomConstants.FIELD_READ.equals(fieldControl)) {
                TextUtil.setHint(tvContent, "");
                ivRight.setVisibility(View.GONE);
                llRoot.setOnClickListener(v -> ToastUtils.showError(getContext(), "只读属性不可更改"));
            } else {
                TextUtil.setHint(tvContent, pointOut);
            }
        }
    }

    private void onListener() {
        if (CustomConstants.DETAIL_STATE != state && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            tvContent.addTextChangedListener(new TextWatcherUtil.MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    if (TextUtil.isEmpty(s.toString())) {
                        ivRight.setImageResource(R.drawable.custom_icon_reference);
                    } else {
                        ivRight.setImageResource(R.drawable.clear_button);
                    }
                }
            });

            ivRight.setOnClickListener(v -> {
                String content = tvContent.getText().toString();
                if (!TextUtil.isEmpty(content)) {
                    TextUtil.setText(tvContent, "");
                    value = "";

                    if (checkItem != null) {
                        Map<String, Object> relationField = checkItem.getRelationField();
                        if (relationField != null && relationField.size() > 0) {
                            Iterator<String> iterator = relationField.keySet().iterator();
                            while (iterator.hasNext()) {
                                String next = iterator.next();
                                RxManager.$(aHashCode).post(next, "");
                            }
                        }
                    }
                }
            });
        }
    }

    protected void setContent(String content) {
        TextUtil.setText(tvContent, content);
    }

    @Override
    protected void setData(Object value) {
        if (value == null || TextUtil.isEmpty(value + "")) {
            return;
        }
        try {
            Map<String, Object> map = new HashMap<>();
            if (value instanceof JSONArray && ((JSONArray) value).size() > 0) {
                map = (Map<String, Object>) ((JSONArray) value).get(0);
            } else if (value instanceof List && ((List) value).size() > 0) {
                map = (Map<String, Object>) ((List) value).get(0);
            }
            if (map.get("name") != null) {
                this.value = map.get("id") + "";
                TextUtil.setText(tvContent, map.get("name") + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void put(JSONObject jsonObj) {
        jsonObj.put(keyName, value == null ? "" : value);
    }

    @Override
    public boolean checkNull() {
        return TextUtil.isEmpty(value);
    }

    @Override
    public boolean formatCheck() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == code && resultCode == Activity.RESULT_OK) {
            checkItem = (ReferDataTempResultBean.DataBean) data.getSerializableExtra(Constants.DATA_TAG1);
            value = checkItem.getId().getValue();

            List<RowBean> rows = checkItem.getRow();
            for (RowBean rowBean : rows) {
                String name = rowBean.getName();
                if (referenceFieldName.equals(name)) {
                    CustomUtil.setReferenceTempValue(tvContent, rowBean);
                    break;
                }
            }

            Map<String, Object> relationField = checkItem.getRelationField();
            if (relationField != null && relationField.size() > 0) {
                Iterator<String> iterator = relationField.keySet().iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    Object relationValue = relationField.get(next);
                    if (relationValue != null) {
                        if (!TextUtil.isEmpty(subFormName) && next.startsWith(CustomConstants.SUBFORM)) {
                            //子表单内的组件
                            RxManager.$(aHashCode).post(next + subFormIndex, relationValue);
                        } else {
                            RxManager.$(aHashCode).post(next, relationValue);
                        }
                    }
                }
            }
            if (!checkNull() && isLinkage) {
                linkageData();
            }
        }
    }

    @Override
    public void onClick(View v) {
        //清除焦点
        RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
        Bundle bundle = new Bundle();
        if (isDetailState()) {
            String relevanceModuleBean = relevanceModule.getModuleName();
            AuthHelper.getInstance().checkAuthByModule(getContext(), relevanceModuleBean, new ProgressSubscriber<ModuleAuthBean>(getContext()) {
                @Override
                public void onNext(ModuleAuthBean moduleAuthBean) {
                    super.onNext(moduleAuthBean);
                    String readAuth = moduleAuthBean.getData().getReadAuth();
                    if (!"1".equals(readAuth)) {
                        ToastUtils.showError(getContext(), "没有 " + relevanceModule.getModuleLabel() + " 的权限！");
                        return;
                    }
                    bundle.putString(Constants.DATA_ID, value);
                    bundle.putString(Constants.MODULE_BEAN, relevanceModuleBean);
                    RxManager.$(aHashCode).post(CustomConstants.MESSAGE_DATA_DETAIL_CODE, bundle);
                }
            });

            return;
        }
        bundle.putString(Constants.MODULE_BEAN, bean.getModuleBean());
        bundle.putString(Constants.DATA_TAG2, pointOut);
        bundle.putString(Constants.REFERENCE_FIELD, keyName);
        if (!TextUtil.isEmpty(subFormName)) {
            bundle.putString(Constants.SUBFORM_NAME, subFormName);
        }
        if (referenceViewInterface != null) {
            JSONObject moduleValue = referenceViewInterface.getReferenceValue();
            bundle.putSerializable(Constants.DATA_TAG1, moduleValue);
        }
        ((ActivityPresenter) getContext()).setOnActivityResult(code, this);
        RxManager.$(aHashCode).post(CustomConstants.MESSAGE_REFERENCE_TEMP_CODE,
                new MessageBean(code, null, bundle));
    }
}
