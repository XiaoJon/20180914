package com.hjhq.teamface.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.ModuleItemBean;
import com.hjhq.teamface.basis.bean.RowDataBean;
import com.hjhq.teamface.basis.bean.ViewDataAuthResBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.memo.CustomDataUtil;
import com.hjhq.teamface.memo.MemoModel;
import com.hjhq.teamface.memo.bean.MemoDetailBean;
import com.hjhq.teamface.memo.bean.NewMemoBean;
import com.hjhq.teamface.memo.bean.RelevantDataBean;
import com.hjhq.teamface.memo.view.MemoDetailDelegate;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func2;

@RouteNode(path = "/detail", desc = "备忘录详情")
public class MemoDetailActivity extends ActivityPresenter<MemoDetailDelegate, MemoModel> {
    private String memoId;


    @Override
    public void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            memoId = bundle.getString(Constants.DATA_TAG1);
            if (TextUtils.isEmpty(memoId)) {
                finish();
                return;
            }
            getNetData(true);
        }

    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.getToolbar().setNavigationOnClickListener(v -> {

            finish();
        });
    }

    private void getNetData(boolean flag) {

        /*model.findMemoDetail(MemoDetailActivity.this, memoId, new ProgressSubscriber<MemoDetailBean>(MemoDetailActivity.this, flag) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(MemoDetailBean baseBean) {
                super.onNext(baseBean);
                viewDelegate.showData(baseBean);
            }
        });*/
        model.queryMemoDetail(mContext, memoId, new Func2<MemoDetailBean, RelevantDataBean, MemoDetailBean>() {
            @Override
            public MemoDetailBean call(MemoDetailBean memoDetailBean, RelevantDataBean relevantDataBean) {
                if (relevantDataBean != null && relevantDataBean.getData() != null && relevantDataBean.getData().getModuleDataList().size() > 0) {
                    final List<RelevantDataBean.DataBean.ModuleDataListBean> moduleDataList = relevantDataBean.getData().getModuleDataList();
                    ArrayList<ModuleItemBean> list = new ArrayList<ModuleItemBean>();
                    for (int i = 0; i < moduleDataList.size(); i++) {
                        final RelevantDataBean.DataBean.ModuleDataListBean moduleBean = moduleDataList.get(i);
                        ModuleItemBean data = new ModuleItemBean();
                        data.setType("2");
                        data.setId(moduleBean.getId().getValue());
                        data.setTitle("" + moduleBean.getModule_name());
                        data.setModule(moduleBean.getBeanName());
                        if (moduleBean.getRow().size() > 0) {
                            data.setSubTitle(CustomDataUtil.getDataValue(moduleBean.getRow().get(0)));
                        } else {
                            data.setSubTitle("");
                        }
                        data.setIcon_color(moduleBean.getIcon_color());
                        data.setIcon_type(moduleBean.getIcon_type());
                        data.setIcon_url(moduleBean.getIcon_url());
                        data.setPicture("");
                        data.setCreatorName("");
                        List<RowDataBean> rowList = moduleBean.getRow();
                        if (rowList != null && rowList.size() > 0) {
                            for (int j = 0; j < rowList.size(); j++) {
                                if ("personnel_create_by".equals(rowList.get(j).getName())) {

                                    try {
                                        String string = JSONObject.toJSONString(rowList.get(j).getValue());
                                        org.json.JSONArray ja = new org.json.JSONArray(string);
                                        if (ja != null && ja.length() > 0) {
                                            data.setCreatorName(ja.getJSONObject(0).optString("name"));
                                            data.setPicture(ja.getJSONObject(0).optString("picture"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
                        }

                        list.add(data);
                    }
                    memoDetailBean.getData().setItemsArr(list);
                }
                return memoDetailBean;
            }
        }, new ProgressSubscriber<MemoDetailBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                viewDelegate.showError();
            }

            @Override
            public void onNext(MemoDetailBean memoDetailBean) {
                super.onNext(memoDetailBean);
                viewDelegate.showData(memoDetailBean);
            }
        });
    }

    public void updateMemo(NewMemoBean bean) {
        model.updateMemo(MemoDetailActivity.this, bean, new ProgressSubscriber<BaseBean>(mContext, false) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case Constants.REQUEST_CODE1:
                    break;
                case Constants.REQUEST_CODE2:
                    getNetData(false);
                    //dataChanged = true;
                    break;
                case Constants.REQUEST_CODE3:
                    getNetData(false);
                    EventBusUtils.sendEvent(new MessageBean(MemoConstant.DATALIST_CHANGE, MemoConstant.MEMO_DATA_CHANGED, null));
                    break;
                case Constants.REQUEST_CODE4:
                    getNetData(false);
                    //dataChanged = true;
                    break;
                case Constants.REQUEST_CODE5:
                    if (data != null) {
                        String num = data.getStringExtra(Constants.DATA_TAG1);
                        viewDelegate.setCommentNum(num);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    public void deleteMemo() {
        model.memoOperation(MemoDetailActivity.this, MemoConstant.MEMO_OPERATION_DELETE, memoId, new ProgressSubscriber<BaseBean>(MemoDetailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "操作成功");
                EventBusUtils.sendEvent(new MessageBean(MemoConstant.DATALIST_CHANGE, MemoConstant.MEMO_DATA_CHANGED, null));
                finish();
            }
        });

    }

    public void quitMemo() {
        model.memoOperation(MemoDetailActivity.this, MemoConstant.MEMO_OPERATION_QUIT, memoId, new ProgressSubscriber<BaseBean>(MemoDetailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "操作成功");
                EventBusUtils.sendEvent(new MessageBean(MemoConstant.DATALIST_CHANGE, MemoConstant.MEMO_DATA_CHANGED, null));
                finish();
            }
        });

    }

    /**
     * 彻底删除
     */
    public void deleteForever() {
        model.memoOperation(MemoDetailActivity.this, MemoConstant.MEMO_OPERATION_DELETE_FOREVER, memoId, new ProgressSubscriber<BaseBean>(MemoDetailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "操作成功");
                setResult(RESULT_OK);
                EventBusUtils.sendEvent(new MessageBean(MemoConstant.DATALIST_CHANGE, MemoConstant.MEMO_DATA_CHANGED, null));
                finish();
            }
        });

    }

    /**
     * 恢复
     */
    public void recover() {
        model.memoOperation(MemoDetailActivity.this, MemoConstant.MEMO_OPERATION_RECOVER, memoId, new ProgressSubscriber<BaseBean>(MemoDetailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                setResult(RESULT_OK);
                EventBusUtils.sendEvent(new MessageBean(MemoConstant.DATALIST_CHANGE, MemoConstant.MEMO_DATA_CHANGED, null));
                finish();
            }
        });

    }

    public void quitShare() {
        model.memoOperation(MemoDetailActivity.this, MemoConstant.MEMO_OPERATION_QUIT, memoId, new ProgressSubscriber<BaseBean>(MemoDetailActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                EventBusUtils.sendEvent(new MessageBean(MemoConstant.DATALIST_CHANGE, MemoConstant.MEMO_DATA_CHANGED, null));
                finish();
            }
        });

    }

    /**
     * 验证权限后查看关联
     *
     * @param moduleItemBean
     */
    public void viewData(ModuleItemBean moduleItemBean) {
        model.queryAuth(mContext, moduleItemBean.getModule(), "", moduleItemBean.getId(),
                new ProgressSubscriber<ViewDataAuthResBean>(mContext) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(ViewDataAuthResBean viewDataAuthResBean) {
                        super.onNext(viewDataAuthResBean);
                        if (viewDataAuthResBean.getData() != null && !TextUtils.isEmpty(viewDataAuthResBean.getData().getReadAuth())) {
                            switch (viewDataAuthResBean.getData().getReadAuth()) {
                                case "0":
                                    ToastUtils.showError(mContext, "无权查看或数据已删除");
                                    break;
                                case "1":
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.MODULE_BEAN, moduleItemBean.getModule());
                                    bundle.putString(Constants.DATA_ID, moduleItemBean.getId());
                                    UIRouter.getInstance().openUri(mContext, "DDComp://custom/detail", bundle, Constants.REQUEST_CODE1);
                                    break;
                                case "2":
                                    ToastUtils.showError(mContext, "无权查看或数据已删除");
                                    break;
                                case "3":
                                    ToastUtils.showError(mContext, "无权查看或数据已删除");
                                    break;
                            }
                        } else {
                            ToastUtils.showError(mContext, "查询权限错误");
                        }
                    }
                });
    }
}
