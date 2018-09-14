package com.hjhq.teamface.memo;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CommonNewResultBean;
import com.hjhq.teamface.basis.bean.ModuleResultBean;
import com.hjhq.teamface.basis.bean.ViewDataAuthResBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.hjhq.teamface.common.bean.ProjectListBean;
import com.hjhq.teamface.common.bean.TaskListBean;
import com.hjhq.teamface.memo.bean.AddRelevantBean;
import com.hjhq.teamface.memo.bean.MemoDetailBean;
import com.hjhq.teamface.memo.bean.MemoListBean;
import com.hjhq.teamface.memo.bean.NewMemoBean;
import com.hjhq.teamface.memo.bean.RelevantDataBean;
import com.hjhq.teamface.memo.bean.SearchModuleDataBean;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

/**
 * Created by Administrator on 2018/3/21.
 * Describe：
 */

public class MemoModel implements IModel<MemoApi> {

    @Override
    public MemoApi getApi() {
        //   SPHelper.setToken("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxOSIsInN1YiI6IjE5IiwiYXVkIjoiMSIsImlzcyI6IjEwMDE5IiwiaWF0IjoxNTM0OTA1NjczfQ.tM0056Mz5Y4gtHqwN7gqpXYnmlLadWRCSFuniT7OrXE");
        return new ApiManager<MemoApi>().getAPI(MemoApi.class);
    }

    /**
     * 添加备忘录
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void saveMemo(ActivityPresenter mActivity, NewMemoBean bean, Subscriber<CommonNewResultBean> s) {
        getApi().saveMemo(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 添加关联
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void updateRelationById(ActivityPresenter mActivity, AddRelevantBean bean, Subscriber<BaseBean> s) {
        getApi().updateRelationById(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 编辑备忘录
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void updateMemo(ActivityPresenter mActivity, NewMemoBean bean, Subscriber<BaseBean> s) {
        getApi().updateMemo(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 1:删除  2：彻底删除 3：恢复备忘  4：退出共享
     *
     * @param mActivity
     * @param type
     * @param ids
     * @param s
     */
    public void memoOperation(ActivityPresenter mActivity, String type, String ids, Subscriber<BaseBean> s) {
        getApi().memoOperation(type, ids).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取备忘录列表
     *
     * @param mActivity
     * @param pageNum
     * @param pageSize
     * @param type
     * @param keyword
     * @param s
     */
    public void findMemoList(ActivityPresenter mActivity, String pageNum, String pageSize, String type,
                             String keyword, Subscriber<MemoListBean> s) {
        getApi().findMemoList(pageNum, pageSize, type, keyword).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 不分页搜索
     *
     * @param mActivity
     * @param type
     * @param keyword
     * @param s
     */
    public void findMemoWebList(ActivityPresenter mActivity, String type,
                                String keyword, Subscriber<MemoListBean> s) {
        getApi().findMemoWebList(type, keyword).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 备忘录详情
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void findMemoDetail(ActivityPresenter mActivity, String id, Subscriber<MemoDetailBean> s) {
        getApi().findMemoDetail(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取关联
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void findRelationList(ActivityPresenter mActivity, String id, Subscriber<RelevantDataBean> s) {
        getApi().findRelationList(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 备忘录详情,合并请求
     *
     * @param mActivity
     * @param id
     * @param func2
     * @param s
     */
    public void queryMemoDetail(ActivityPresenter mActivity, String id,
                                Func2<MemoDetailBean, RelevantDataBean, MemoDetailBean> func2, Subscriber<MemoDetailBean> s) {
        Observable.zip(getApi().findMemoDetail(id), getApi().findRelationList(id), func2)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取模块列表
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
     * 搜索自定义数据
     *
     * @param mActivity
     * @param beanName
     * @param keyword
     * @param s
     */
    public void getFirstFieldFromModule(ActivityPresenter mActivity, String beanName,
                                        String keyword, Subscriber<SearchModuleDataBean> s) {
        getApi().getFirstFieldFromModule(beanName, keyword).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 查询权限
     *
     * @param mActivity
     * @param bean
     * @param style
     * @param dataId
     * @param s
     */
    public void queryAuth(ActivityPresenter mActivity, String bean, String style,
                          String dataId, Subscriber<ViewDataAuthResBean> s) {
        getApi().queryAuth(bean, style, dataId).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取全部项目列表
     *
     * @param mActivity
     * @param pageNum   页码
     * @param pageSize  页的大小
     * @param keyword   关键字（模糊匹配）
     * @param s
     */
    public void queryAllList(RxAppCompatActivity mActivity, int type, int pageNum, int pageSize, String keyword, Subscriber<ProjectListBean> s) {
        getApi().queryAllList(type, pageNum, pageSize, keyword).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取全部项目列表
     *
     * @param mActivity
     * @param s
     */
    public void queryAllTaskList(RxAppCompatActivity mActivity, Long projectId, Subscriber<TaskListBean> s) {
        getApi().queryAllTaskList(projectId).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

}
