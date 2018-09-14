package com.hjhq.teamface.customcomponent.select;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.EntryBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.customcomponent.CustomComponentModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 选择组件 (复选框、多级下拉)
 *
 * @author lx
 * @date 2017/9/8
 */

public class SelectPresenter extends ActivityPresenter<SelectDelegate, CustomComponentModel> {

    private List<EntryBean> entrys;
    /**
     * 选择类型：0单选、1多选
     */
    private boolean isMulti;
    /**
     * 组件类型
     */
    private String type;
    /**
     * 多级下拉选中的bean
     */
    private EntryBean multiPickBean;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            entrys = (List<EntryBean>) getIntent().getSerializableExtra(Constants.DATA_TAG1);
            isMulti = getIntent().getBooleanExtra(Constants.DATA_TAG2, false);
            type = getIntent().getStringExtra(Constants.DATA_TAG3);
        }
    }

    @Override
    public void init() {
        if (!isMulti) {
            viewDelegate.hideRightMenu();
        }

        viewDelegate.selectAdapter.setNewData(entrys);
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                EntryBean entry = (EntryBean) adapter.getItem(position);
                if (isMulti) {
                    entry.setCheck(!entry.isCheck());
                    adapter.notifyDataSetChanged();
                } else {
                    if (CustomConstants.MUTLI_PICKLIST.equals(type)) {
                        //多级下拉
                        next(entry);
                    } else {
                        //复选框
                        Observable.from(entrys).subscribe(entryBean -> entryBean.setCheck(false));
                        entry.setCheck(true);
                        setResult();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setResult();
        return super.onOptionsItemSelected(item);
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, (Serializable) entrys);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 多级下拉  下一步
     *
     * @param entryBean
     */
    private void next(EntryBean entryBean) {
        this.multiPickBean = entryBean;
        List<EntryBean> subEntrys = entryBean.getSubList();
        if (CollectionUtils.isEmpty(subEntrys)) {
            Intent intent = new Intent();
            ArrayList<EntryBean> list = new ArrayList<>();
            list.add(multiPickBean);
            intent.putExtra(Constants.DATA_TAG1, list);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.DATA_TAG1, (ArrayList) subEntrys);
            bundle.putString(Constants.DATA_TAG3, type);
            CommonUtil.startActivtiyForResult(this, SelectPresenter.class, Constants.REQUEST_CODE1, bundle);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK) {
            ArrayList<EntryBean> list = (ArrayList<EntryBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            list.add(0, multiPickBean);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
