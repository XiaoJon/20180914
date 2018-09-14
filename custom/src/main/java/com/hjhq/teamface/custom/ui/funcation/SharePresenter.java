package com.hjhq.teamface.custom.ui.funcation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.adapter.ShareAdapter;
import com.hjhq.teamface.custom.bean.ShareResultBean;
import com.hjhq.teamface.custom.ui.detail.DataDetailModel;

/**
 * 共享
 *
 * @author Administrator
 * @date 2017/12/20
 */

public class SharePresenter extends ActivityPresenter<ShareDelegate, DataDetailModel> {

    private String objectId;
    private String moduleBean;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            objectId = intent.getStringExtra(Constants.DATA_ID);
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
        }
    }

    @Override
    public void init() {
        viewDelegate.setTempState();
        model.getSingleShare(this, objectId, new ProgressSubscriber<ShareResultBean>(this) {
            @Override
            public void onNext(ShareResultBean shareResultBean) {
                super.onNext(shareResultBean);
                viewDelegate.setAdapter(new ShareAdapter(shareResultBean.getData()));
            }
        });
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.addOnitemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                ShareResultBean.DataBean item = (ShareResultBean.DataBean) adapter.getItem(position);

                int id = view.getId();
                if (id == R.id.tv_edit){
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.MODULE_BEAN, moduleBean);
                    bundle.putString(Constants.MODULE_ID, objectId);
                    bundle.putInt(Constants.DATA_TAG1, CustomConstants.EDIT_STATE);
                    bundle.putSerializable(Constants.DATA_TAG2, item);
                    CommonUtil.startActivtiyForResult(SharePresenter.this, AddOrEditSharePresenter.class, Constants.REQUEST_CODE1, bundle);
                }else if(id == R.id.tv_del){
                    delShare(item.getId());
                }
            }
        });
    }

    private void delShare(int dataId) {
        DialogUtils.getInstance().sureOrCancel(this, "提示", "确定要删除该数据共享", viewDelegate.getRootView(), () -> {
            model.delShare(SharePresenter.this, dataId + "", new ProgressSubscriber<BaseBean>(SharePresenter.this) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(context,"删除成功！");
                    init();
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, CustomConstants.ADD_STATE);
        bundle.putString(Constants.MODULE_BEAN, moduleBean);
        bundle.putString(Constants.MODULE_ID, objectId);
        CommonUtil.startActivtiyForResult(this, AddOrEditSharePresenter.class, Constants.REQUEST_CODE1, bundle);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE1) {
            init();
        }
    }
}
