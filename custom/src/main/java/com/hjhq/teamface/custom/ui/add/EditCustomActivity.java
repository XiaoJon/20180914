package com.hjhq.teamface.custom.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.bean.LinkageFieldsResultBean;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.custom.bean.SaveCustomDataRequestBean;
import com.hjhq.teamface.custom.ui.template.ReferenceTempActivity;
import com.hjhq.teamface.custom.ui.template.RepeatCheckActivity;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.customcomponent.widget2.CustomUtil;
import com.hjhq.teamface.customcomponent.widget2.subfield.SubfieldView;
import com.hjhq.teamface.customcomponent.widget2.subforms.CommonSubFormsView;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * 修改业务信息
 *
 * @author xj
 * @date 2017/9/4
 */

@RouteNode(path = "/edit", desc = "修改 自定义")
public class EditCustomActivity extends ActivityPresenter<AddCustomDelegate, AddCustomModel> {
    private HashMap detailMap;
    private String moduleBean;
    private String objectId;
    //公海池菜单ID
    private String seasPoolId;
    private String processFieldV;
    private String taskKey;
    private int operationType;
    /**
     * 联动组件key
     */
    private List<String> linkData;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            detailMap = (HashMap) intent.getSerializableExtra(Constants.DATA_TAG1);
            objectId = intent.getStringExtra(Constants.DATA_ID);
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            seasPoolId = intent.getStringExtra(Constants.POOL);
            taskKey = intent.getStringExtra(ApproveConstants.TASK_KEY);
            processFieldV = intent.getStringExtra(ApproveConstants.PROCESS_FIELD_V);
            operationType = intent.getIntExtra(ApproveConstants.OPERATION_TYPE, CustomConstants.EDIT_STATE);
        }
    }

    @Override
    public void init() {
        getCustomLayout();
        getLinkageFields();
        CustomUtil.handleHidenFields(hashCode(), toString(), viewDelegate.mViewList);
    }
    /**
     * 获取联动字段
     */
    private void getLinkageFields() {
        new CommonModel().getLinkageFields(mContext, moduleBean, new ProgressSubscriber<LinkageFieldsResultBean>(mContext) {
            @Override
            public void onNext(LinkageFieldsResultBean linkageFieldsResultBean) {
                super.onNext(linkageFieldsResultBean);
                linkData = linkageFieldsResultBean.getData();
                setLinkage();
            }
        });
    }
    /**
     * 设置驱动
     */
    private void setLinkage() {
        if (linkData == null || CollectionUtils.isEmpty(viewDelegate.mViewList)) {
            return;
        }

        Observable.from(viewDelegate.mViewList).flatMap(new Func1<SubfieldView, Observable<BaseView>>() {
            @Override
            public Observable<BaseView> call(SubfieldView subfieldView) {
                return Observable.from(subfieldView.getViewList());
            }
        }).subscribe(baseView -> {
            if (baseView instanceof CommonSubFormsView) {
                Observable.from(((CommonSubFormsView) baseView).getViewList()).flatMap(new Func1<List<BaseView>, Observable<BaseView>>() {
                    @Override
                    public Observable<BaseView> call(List<BaseView> baseViews) {
                        return Observable.from(baseViews);
                    }
                }).subscribe(subView -> Observable.from(linkData).filter(link -> link.equals(subView.getKeyName())).subscribe(link -> subView.setLinkage()));
            } else {
                Observable.from(linkData).filter(link -> link.equals(baseView.getKeyName())).subscribe(link -> baseView.setLinkage());
            }
        });
    }

    /**
     * 得到自定义布局
     */
    private void getCustomLayout() {
        Map<String, Object> map = new HashMap<>();
        map.put("bean", moduleBean);
        map.put("operationType", operationType);
        map.put("dataId", objectId);
        map.put("taskKey", taskKey);
        map.put("isSeasPool", TextUtil.isEmpty(seasPoolId) ? null : "1");
        map.put("processFieldV", processFieldV);
        model.getCustomLayout(this, map, new ProgressSubscriber<CustomLayoutResultBean>(this) {
            @Override
            public void onNext(CustomLayoutResultBean customLayoutResultBean) {
                super.onNext(customLayoutResultBean);
                viewDelegate.drawLayout(customLayoutResultBean.getData(), detailMap, CustomConstants.EDIT_STATE, moduleBean);
                setLinkage();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        editCustomData();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存
     */
    private void editCustomData() {
        SaveCustomDataRequestBean bean = new SaveCustomDataRequestBean();
        bean.setId(objectId);
        bean.setBean(moduleBean);

        JSONObject json = new JSONObject();
        if (!CustomUtil.put(viewDelegate.mViewList, this, json)) {
            return;
        }
        bean.setData(json);

        model.updateCustomData(this, bean, new ProgressSubscriber<BaseBean>(this, "正在保存信息") {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        //子表单数据联动联动
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_SUBFORM_LINKAGE_CODE, tag -> {
            if (!CollectionUtils.isEmpty(linkData)) {
                Observable.from(linkData).filter(link -> link.equals(((BaseView) tag).getKeyName())).subscribe(link -> ((BaseView) tag).setLinkage());
            }
        });
        //查重
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_REPEAT_CHECK_CODE, tag -> CommonUtil.startActivtiy(mContext, RepeatCheckActivity.class, (Bundle) tag));
        //关联列表
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_REFERENCE_TEMP_CODE, tag -> {
            MessageBean messageBean = (MessageBean) tag;
            CommonUtil.startActivtiyForResult(mContext, ReferenceTempActivity.class, messageBean.getCode(), (Bundle) messageBean.getObject());
        });
    }
}
