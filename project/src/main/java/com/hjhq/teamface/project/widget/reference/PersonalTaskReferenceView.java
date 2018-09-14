package com.hjhq.teamface.project.widget.reference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.customcomponent.widget2.reference.ReferenceView;
import com.hjhq.teamface.project.presenter.ProjectDetailActivity;
import com.hjhq.teamface.project.presenter.reference.SelectReferenceModuleActivity;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人任务关联组件
 *
 * @author xj
 * @date 2017/10/11
 */

public class PersonalTaskReferenceView extends ReferenceView implements ActivityPresenter.OnActivityResult, View.OnClickListener {
    private static final int SELECT_MODULE_REQUEST_CODE = 3284;
    /**
     * 1 项目 2 自定义
     */
    private String from_status = "0";
    /**
     * 自定义moduleBean
     */
    private String moduleBean;

    public PersonalTaskReferenceView(CustomBean bean) {
        super(bean);
    }

    @Override
    protected void setData(Object value) {
        if (value == null || TextUtil.isEmpty(value + "")) {
            return;
        }
        try {
            Map<String, Object> map = new HashMap<>();
            if (value instanceof JSONArray) {
                map = (Map<String, Object>) ((JSONArray) value).get(0);
            } else if (value instanceof List) {
                map = (Map<String, Object>) ((List) value).get(0);
            }
            this.value = map.get("id") + "";
            setContent(map.get("name") + "");
            this.from_status = map.get("from_status") + "";
            this.moduleBean = map.get("bean_name") + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(JSONObject jsonObj) {
        jsonObj.put(ProjectConstants.RELATION_ID, value);
        String text = tvContent.getText().toString();
        int index = text.indexOf("/");
        if (index > 0) {
            text = text.substring(0, text.indexOf("/"));
        }
        jsonObj.put(ProjectConstants.RELATION_DATA, text);
        jsonObj.put("from_status", from_status);
        jsonObj.put("bean_name", moduleBean);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_MODULE_REQUEST_CODE + code && resultCode == Activity.RESULT_OK) {
            //个人任务关联关系
            String text = data.getStringExtra(Constants.DATA_TAG1);
            value = data.getStringExtra(Constants.DATA_TAG2);
            from_status = data.getStringExtra(Constants.DATA_TAG3);
            if ("1".equals(from_status)) {
                moduleBean = ProjectConstants.PERSONAL_TASK_BEAN;
            } else if ("2".equals(from_status)) {
                moduleBean = data.getStringExtra(Constants.MODULE_BEAN);
            }
            setContent(text);
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        if (state == CustomConstants.DETAIL_STATE) {
            //详情
            if ("1".equals(from_status)) {
                bundle.putLong(ProjectConstants.PROJECT_ID, TextUtil.parseLong(value));
                CommonUtil.startActivtiy(getContext(), ProjectDetailActivity.class, bundle);
            } else if ("2".equals(from_status)) {
                bundle.putString(Constants.MODULE_BEAN, moduleBean);
                bundle.putString(Constants.DATA_ID, value);
                UIRouter.getInstance().openUri(getContext(), "DDComp://custom/detail", bundle);
            }
        } else {
            //新增、编辑
            ((ActivityPresenter) getContext()).setOnActivityResult(SELECT_MODULE_REQUEST_CODE + code, this);
            CommonUtil.startActivtiyForResult(getContext(), SelectReferenceModuleActivity.class, SELECT_MODULE_REQUEST_CODE + code);
        }
    }


    /**
     * 设置子表单的keyName
     *
     * @param keyName
     */
    public void setSubFormInfo(String keyName) {
        this.subFormName = keyName;
    }

}
