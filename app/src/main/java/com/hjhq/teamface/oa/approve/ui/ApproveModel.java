package com.hjhq.teamface.oa.approve.ui;

import com.hjhq.teamface.api.ApproveApiService;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.DetailResultBean;
import com.hjhq.teamface.basis.bean.FilterFieldResultBean;
import com.hjhq.teamface.basis.bean.ModuleResultBean;
import com.hjhq.teamface.basis.bean.UpdateModuleBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.MainRetrofit;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.factory.FastJsonConverterFactory;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.hjhq.teamface.basis.bean.QueryApprovalDataResultBean;
import com.hjhq.teamface.oa.approve.bean.ApproveCopyRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApprovePassRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveRejectRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveResponseBean;
import com.hjhq.teamface.oa.approve.bean.ApproveRevokeRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveTransferRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveUnReadCountResponseBean;
import com.hjhq.teamface.oa.approve.bean.PassTypeResponseBean;
import com.hjhq.teamface.oa.approve.bean.ProcessFlowResponseBean;
import com.hjhq.teamface.oa.approve.bean.RejectTypeResponseBean;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * 审批Model
 * Created by lx on 2017/9/5.
 */

public class ApproveModel implements IModel<ApproveApiService> {
    @Override
    public ApproveApiService getApi() {
        return new ApiManager<ApproveApiService>().getAPI(ApproveApiService.class);
    }

    /**
     * 获取未读数量
     *
     * @param mActivity
     * @param s
     */
    public void queryApprovalCount(ActivityPresenter mActivity, Subscriber<ApproveUnReadCountResponseBean> s) {
        getApi().queryApprovalCount().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取筛选条件
     *
     * @param mActivity
     * @param type
     * @param s
     */
    public void querySearchMenu(ActivityPresenter mActivity, int type, Subscriber<FilterFieldResultBean> s) {
        getApi().querySearchMenu(type).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取审批列表
     *
     * @param mActivity
     * @param type
     * @param s
     */
    public void queryApprovalList(ActivityPresenter mActivity, int type, int pageSize, int pageNum, Map queryWhere, Subscriber<ApproveResponseBean> s) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);
        map.put("queryWhere", queryWhere);
        int sign = 2;
        if (queryWhere == null) {
            sign = 3;
        }
        map.put("sign", sign);

        getApi().queryApprovalList(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取审批列表
     *
     * @param mActivity
     * @param s
     */
    public void queryProjectApprovaList(ActivityPresenter mActivity, String moduleBean, int pageSize, int pageNum, String keyWord, Subscriber<ApproveResponseBean> s) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);
        map.put("moduleBean", moduleBean);
        map.put("keyWord", keyWord);

        getApi().queryProjectApprovaList(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 得到全部审批模块
     *
     * @param mActivity
     * @param s
     */
    public void getAllModule(ActivityPresenter mActivity, Subscriber<ModuleResultBean> s) {
        getApi().getAllModule(null).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 得到审批模块
     *
     * @param mActivity
     * @param s
     */
    public void getApproveModule(ActivityPresenter mActivity, Subscriber<ModuleResultBean> s) {
        getApi().getAllModule("1").map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 更新模块
     *
     * @param mActivity
     * @param moduleIds
     * @param s
     */
    public void updateModule(ActivityPresenter mActivity, String[] moduleIds, Subscriber<BaseBean> s) {
        UpdateModuleBean bean = new UpdateModuleBean();
        bean.setModule_ids(moduleIds);
        getApi().updateModule(bean)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取业务信息详情
     *
     * @param mActivity
     * @param id        对象id
     * @param bean
     * @param s
     */
    public void getDataDetail(ActivityPresenter mActivity, String id, String bean, String taskKey, String processFieldV, Subscriber<DetailResultBean> s) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("id", id);
        map.put("bean", bean);
        map.put("taskKey", taskKey);
        map.put("processFieldV", processFieldV);

        ApproveApiService approveApiService = new MainRetrofit.Builder<ApproveApiService>()
                .addConverterFactory(new FastJsonConverterFactory())
                .build(ApproveApiService.class);
        approveApiService.getDataDetail(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取布局
     *
     * @param mActivity
     * @param map
     * @param s
     */
    public void getCustomLayout(ActivityPresenter mActivity, Map<String, Object> map, Subscriber<CustomLayoutResultBean> s) {
        getApi().getEnableFields(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取审批列表
     *
     * @param mActivity
     * @param map
     * @param s
     */
    public void getProcessWholeFlow(ActivityPresenter mActivity, Map<String, Object> map, Subscriber<ProcessFlowResponseBean> s) {
        getApi().getProcessWholeFlow(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 审批通过
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void approvePass(ActivityPresenter mActivity, ApprovePassRequestBean bean, Subscriber<BaseBean> s) {
        getApi().approvePass(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 审批驳回
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void approveReject(ActivityPresenter mActivity, ApproveRejectRequestBean bean, Subscriber<BaseBean> s) {
        getApi().approveReject(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 审批转交
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void approveTransfer(ActivityPresenter mActivity, ApproveTransferRequestBean bean, Subscriber<BaseBean> s) {
        getApi().approveTransfer(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 审批撤销
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void approveRevoke(ActivityPresenter mActivity, ApproveRevokeRequestBean bean, Subscriber<BaseBean> s) {
        getApi().approveRevoke(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 审批抄送
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void approveCopy(ActivityPresenter mActivity, ApproveCopyRequestBean bean, Subscriber<BaseBean> s) {
        getApi().approveCopy(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 审批催办
     *
     * @param mActivity
     * @param map
     * @param s
     */
    public void approveUrge(ActivityPresenter mActivity, Map<String, String> map, Subscriber<BaseBean> s) {
        getApi().approveUrge(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 审批催办
     *
     * @param mActivity
     * @param moduleBean
     * @param moduleDataId
     * @param s
     */
    public void approveDel(ActivityPresenter mActivity, String moduleBean, String moduleDataId, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>(2);
        map.put("moduleBean", moduleBean);
        map.put("moduleDataId", moduleDataId);
        getApi().approveDel(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取通过方式
     *
     * @param mActivity
     * @param moduleBean
     * @param processInstanceId
     * @param taskId
     * @param taskKey
     * @param s
     */
    public void getPassType(ActivityPresenter mActivity, String moduleBean, String processInstanceId, String taskId, String taskKey, Subscriber<PassTypeResponseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("moduleBean", moduleBean);
        map.put("processInstanceId", processInstanceId);
        map.put("taskId", taskId);
        map.put("taskKey", taskKey);
        getApi().getPassType(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取驳回方式
     *
     * @param mActivity
     * @param moduleBean
     * @param processInstanceId
     * @param taskKey
     * @param s
     */
    public void getRejectType(ActivityPresenter mActivity, String moduleBean, String processInstanceId, String taskKey, Subscriber<RejectTypeResponseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("moduleBean", moduleBean);
        map.put("processInstanceId", processInstanceId);
        map.put("taskKey", taskKey);
        getApi().getRejectType(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取审批小助手跳转详情参数
     *
     * @param mActivity
     * @param map
     * @param s
     */
    public void queryApprovalData(RxAppCompatActivity mActivity, Map<String, String> map, Subscriber<QueryApprovalDataResultBean> s) {
        getApi().queryApprovalData(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 标记已读
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void approvalRead(ActivityPresenter mActivity, String id, String type, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>(2);
        map.put("process_definition_id", id);
        map.put("type", type);
        getApi().approvalRead(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

}
