package com.hjhq.teamface.custom.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.ModuleBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.bean.LinkageFieldsResultBean;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.custom.bean.SaveCustomDataRequestBean;
import com.hjhq.teamface.custom.ui.template.ReferenceTempActivity;
import com.hjhq.teamface.custom.ui.template.RepeatCheckActivity;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.customcomponent.widget2.CustomUtil;
import com.hjhq.teamface.customcomponent.widget2.ReferenceViewInterface;
import com.hjhq.teamface.customcomponent.widget2.subfield.SubfieldView;
import com.hjhq.teamface.customcomponent.widget2.subforms.CommonSubFormsView;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * 新增 自定义
 *
 * @author xj
 * @date 2017/9/4
 */

@RouteNode(path = "/add", desc = "新增 自定义")
public class AddCustomActivity extends ActivityPresenter<AddCustomDelegate, AddCustomModel> implements ReferenceViewInterface {
    private String moduleBean;
    private String moduleId;
    private HashMap detailMap;
    private String moduleTitle;
    //公海池菜单Id
    private String seasPoolId;
    /**
     * 联动组件key
     */
    private List<String> linkData;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            moduleId = intent.getStringExtra(Constants.MODULE_ID);
            detailMap = (HashMap) intent.getSerializableExtra(Constants.DATA_TAG1);
            seasPoolId = intent.getStringExtra(Constants.POOL);
        }
    }

    @Override
    public void init() {
        //审批不需要判断权限
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
        map.put("operationType", CustomConstants.ADD_STATE);
        map.put("isSeasPool", TextUtil.isEmpty(seasPoolId) ? null : "1");
        model.getCustomLayout(this, map, new ProgressSubscriber<CustomLayoutResultBean>(this) {
            @Override
            public void onNext(CustomLayoutResultBean customLayoutResultBean) {
                super.onNext(customLayoutResultBean);
                moduleTitle = customLayoutResultBean.getData().getTitle();
                viewDelegate.drawLayout(customLayoutResultBean.getData(), detailMap, CustomConstants.ADD_STATE, moduleBean);
                setLinkage();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveCustomData();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存
     */
    private void saveCustomData() {
        SaveCustomDataRequestBean bean = packageData();
        if (bean == null) {
            return;
        }
        model.saveCustomData(this, bean, new ProgressSubscriber<ModuleBean>(this, "正在保存信息") {
            @Override
            public void onNext(ModuleBean baseBean) {
                super.onNext(baseBean);
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG1, baseBean.getData());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 封装数据
     *
     * @return
     */
    private SaveCustomDataRequestBean packageData() {
        SaveCustomDataRequestBean bean = new SaveCustomDataRequestBean();
        bean.setBean(moduleBean);
//        bean.setModuleId(moduleId);
        bean.setTitle(moduleTitle);

        JSONObject json = new JSONObject();
        if (!CustomUtil.put(viewDelegate.mViewList, this, json)) {
            return null;
        }
        json.put("seas_pool_id", seasPoolId);
        LogUtil.d(json.toString());
        bean.setData(json);
        return bean;
    }


    @Override
    public JSONObject getReferenceValue() {
        JSONObject json = new JSONObject();
        CustomUtil.putReference(viewDelegate.mViewList, json);
        return json;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_SUBFORM_LINKAGE_CODE, tag -> {
            if (!CollectionUtils.isEmpty(linkData)) {
                Observable.from(linkData).filter(link -> link.equals(((BaseView) tag).getKeyName())).subscribe(link -> ((BaseView) tag).setLinkage());
            }
        });
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_REPEAT_CHECK_CODE, tag -> CommonUtil.startActivtiy(mContext, RepeatCheckActivity.class, (Bundle) tag));
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_REFERENCE_TEMP_CODE, tag -> {
            MessageBean messageBean = (MessageBean) tag;
            CommonUtil.startActivtiyForResult(mContext, ReferenceTempActivity.class, messageBean.getCode(), (Bundle) messageBean.getObject());
        });
    }
}
