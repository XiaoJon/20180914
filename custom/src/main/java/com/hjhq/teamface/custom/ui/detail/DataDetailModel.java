package com.hjhq.teamface.custom.ui.detail;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.bean.DetailResultBean;
import com.hjhq.teamface.basis.bean.EmailListBean;
import com.hjhq.teamface.basis.bean.ModuleFunctionBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.MainRetrofit;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.factory.FastJsonConverterFactory;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.hjhq.teamface.common.bean.AddCommentRequestBean;
import com.hjhq.teamface.common.bean.CommentDetailResultBean;
import com.hjhq.teamface.common.bean.DynamicListResultBean;
import com.hjhq.teamface.custom.CustomApiService;
import com.hjhq.teamface.custom.bean.AddOrEditShareRequestBean;
import com.hjhq.teamface.custom.bean.AutoModuleResultBean;
import com.hjhq.teamface.custom.bean.DataRelationResultBean;
import com.hjhq.teamface.custom.bean.FindAutoByBean;
import com.hjhq.teamface.custom.bean.SeasPoolResponseBean;
import com.hjhq.teamface.custom.bean.ShareResultBean;
import com.hjhq.teamface.custom.bean.TransformationRequestBean;
import com.hjhq.teamface.custom.bean.TransformationResultBean;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * 详情MOdel
 *
 * @author lx
 * @date 2017/9/5
 */

public class DataDetailModel implements IModel<CustomApiService> {
    @Override
    public CustomApiService getApi() {
        return new ApiManager<CustomApiService>().getAPI(CustomApiService.class);
    }

    /**
     * 获取业务信息详情
     *
     * @param mActivity
     * @param map
     * @param s
     */
    public void getDataDetail(ActivityPresenter mActivity, Map<String, Object> map, Subscriber<DetailResultBean> s) {
        CustomApiService customApiService = new MainRetrofit.Builder<CustomApiService>()
                .addConverterFactory(new FastJsonConverterFactory())
                .build(CustomApiService.class);
        customApiService.getDataDetail(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取关联模块和头部标题
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void getDataRelation(ActivityPresenter mActivity, String id, String bean, Subscriber<DataRelationResultBean> s) {
        getApi().getDataRelation(id, bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取所有自动化模块
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void getAutoModule(ActivityPresenter mActivity, String bean, Subscriber<AutoModuleResultBean> s) {
        getApi().getAutoModule(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 根据bean获取自动匹配列表
     *
     * @param mActivity
     * @param s
     */
    public void findAutoByBean(ActivityPresenter mActivity, String sorceBean, String targetBean, Subscriber<FindAutoByBean> s) {
        getApi().findAutoByBean(sorceBean, targetBean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 根据bean获取自动匹配列表
     *
     * @param mActivity
     * @param s
     */
    public void findAutoList(ActivityPresenter mActivity, String dataId,long ruleId, String sorceBean, String targetBean, int pageNum, int pageSize, Subscriber<DataTempResultBean> s) {
        getApi().findAutoList(dataId,ruleId, sorceBean, targetBean, pageNum, pageSize).map(new HttpResultFunc<>())
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
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 添加评论
     *
     * @param s
     */
    public void addComment(ActivityPresenter mActivity, AddCommentRequestBean bean, Subscriber<BaseBean> s) {
        getApi().addComment(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 评论详情
     *
     * @param s
     */
    public void getCommentDetail(ActivityPresenter mActivity, String id, String bean, Subscriber<CommentDetailResultBean> s) {
        getApi().getCommentDetail(id, bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 动态列表
     *
     * @param s
     */
    public void getDynamicList(ActivityPresenter mActivity, String id, String bean, Subscriber<DynamicListResultBean> s) {
        getApi().getDynamicList(id, bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取模块的功能权限(ActivityPresenter)
     *
     * @param mActivity
     * @param moduleBean
     * @param s
     */
    public void getMoudleFunctionAuth(ActivityPresenter mActivity, String moduleBean,
                                      Subscriber<ModuleFunctionBean> s) {
        getApi().getModuleFunctionAuth(moduleBean)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 转移责任人
     *
     * @param mActivity
     * @param s
     */
    public void transfor(ActivityPresenter mActivity, String id, String bean, String data, String share,
                         Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("bean", bean);
        map.put("data", data);
        map.put("share", share);
        getApi().transfor(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 复制
     *
     * @param mActivity
     * @param s
     */
    public void copy(ActivityPresenter mActivity, String id, String bean,
                     Subscriber<DetailResultBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("bean", bean);
        getApi().copy(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 删除
     *
     * @param mActivity
     * @param s
     */
    public void del(ActivityPresenter mActivity, String id, String bean,
                    Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("bean", bean);
        getApi().del(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 根据模块数据ID查它的共享设置
     *
     * @param mActivity
     * @param dataId
     * @param s
     */
    public void getSingleShare(ActivityPresenter mActivity, String dataId,
                               Subscriber<ShareResultBean> s) {
        getApi().getSingleShare(dataId)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除共享
     *
     * @param mActivity
     * @param s
     */
    public void delShare(ActivityPresenter mActivity, String id,
                         Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        getApi().delShare(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 保存共享
     *
     * @param mActivity
     * @param s
     */
    public void saveShare(ActivityPresenter mActivity, AddOrEditShareRequestBean bean,
                          Subscriber<BaseBean> s) {
        getApi().saveShare(bean)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 编辑共享
     *
     * @param mActivity
     * @param s
     */
    public void editShare(ActivityPresenter mActivity, AddOrEditShareRequestBean bean,
                          Subscriber<BaseBean> s) {
        getApi().editShare(bean)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 根据模块bean 查询 转换 列表
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void getFieldTransformations(ActivityPresenter mActivity, String bean,
                                        Subscriber<TransformationResultBean> s) {
        getApi().getFieldTransformations(bean)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 转换
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void transformations(ActivityPresenter mActivity, TransformationRequestBean bean,
                                Subscriber<BaseBean> s) {
        getApi().transformations(bean)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 领取
     *
     * @param mActivity  请求界面的activity
     * @param dataId     数据ID
     * @param bean       模块bean
     * @param seasPoolId 公海池菜单ID
     * @param s          请求观察者，接收请求响应
     */
    public void take(ActivityPresenter mActivity, String dataId, String bean, String seasPoolId,
                     Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", dataId);
        map.put("bean", bean);
        map.put("seas_pool_id", seasPoolId);

        getApi().take(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 退回公海池
     *
     * @param mActivity  请求界面的activity
     * @param dataId     数据ID
     * @param bean       模块bean
     * @param seasPoolId 公海池菜单ID
     * @param s          请求观察者，接收请求响应
     */
    public void returnBack(ActivityPresenter mActivity, String dataId, String bean, String seasPoolId,
                           Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", dataId);
        map.put("bean", bean);
        map.put("seas_pool_id", seasPoolId);

        getApi().returnBack(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 公海池分配
     *
     * @param mActivity  请求界面的activity
     * @param dataId     数据ID
     * @param bean       模块bean
     * @param employeeId 人员ID
     * @param s          请求观察者，接收请求响应
     */
    public void allocate(ActivityPresenter mActivity, String dataId, String bean, String employeeId,
                         Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", dataId);
        map.put("bean", bean);
        map.put("employee_id", employeeId);

        getApi().allocate(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 移动公海池
     *
     * @param mActivity  请求界面的activity
     * @param dataId     数据ID
     * @param bean       模块bean
     * @param seasPoolId 公海池ID
     * @param s          请求观察者，接收请求响应
     */
    public void moveSeapool(ActivityPresenter mActivity, String dataId, String bean, String seasPoolId,
                            Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", dataId);
        map.put("bean", bean);
        map.put("seas_pool_id", seasPoolId);

        getApi().moveSeapool(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取模块的公海池列表
     *
     * @param mActivity 请求界面的activity
     * @param bean      模块bean
     * @param s         请求观察者，接收请求响应
     */
    public void getSeapools(ActivityPresenter mActivity, String bean,
                            Subscriber<SeasPoolResponseBean> s) {
        getApi().getSeapools(bean)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 自定义详情获取邮件列表
     *
     * @param mActivity
     * @param pageNum
     * @param pageSize
     * @param accountName
     * @param s
     */
    public void getEmailListyAccount(ActivityPresenter mActivity, int pageNum, int pageSize, String accountName, Subscriber<EmailListBean> s) {
        getApi().getEmailListyAccount(pageNum, pageSize, accountName).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }
}
