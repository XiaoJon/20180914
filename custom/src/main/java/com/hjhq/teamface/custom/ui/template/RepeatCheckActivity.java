package com.hjhq.teamface.custom.ui.template;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.adapter.RepeatCheckAdapter;
import com.hjhq.teamface.custom.bean.RepeatCheckResponseBean;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;


/**
 * 查重
 *
 * @author Administrator
 * @date 2017/9/5
 * Describe：搜索页
 */
@RouteNode(path = "/repeatCheck", desc = "查重")
public class RepeatCheckActivity extends ActivityPresenter<RepeatCheckDelegate, DataTempModel> {

    private String moduleBean;
    private String keyword;
    private RepeatCheckAdapter mAdapter;
    private String searchKey;
    private String searchName;
    List<RepeatCheckResponseBean.DataBean> dataBeanList = new ArrayList<>();

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            moduleBean = getIntent().getStringExtra(Constants.MODULE_BEAN);
            keyword = getIntent().getStringExtra(Constants.DATA_TAG1);
            searchKey = getIntent().getStringExtra(Constants.DATA_TAG2);
            searchName = getIntent().getStringExtra(Constants.DATA_TAG3);
        }
    }

    @Override
    public void init() {
        mAdapter = new RepeatCheckAdapter(dataBeanList);
        View emptyView = mContext.getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        mAdapter.setEmptyView(emptyView);
        viewDelegate.setAdapter(mAdapter);
        viewDelegate.setHintText("请输入" + searchName);
        viewDelegate.setSearchText(keyword);
        if (!TextUtil.isEmpty(keyword)) {
            repeatCheck();
        }
    }

    /**
     * 查重
     */
    public void repeatCheck() {
        if (TextUtil.isEmpty(keyword)) {
            ToastUtils.showError(this, "请输入" + searchName);
            return;
        }
        if (keyword.length() < 2) {
            ToastUtils.showError(this, "搜索内容不能少于两个字");
            return;
        }
        model.getRecheckingFields(this, moduleBean, searchKey, keyword, new ProgressSubscriber<RepeatCheckResponseBean>(this) {
            @Override
            public void onNext(RepeatCheckResponseBean baseBean) {
                super.onNext(baseBean);
                List<RepeatCheckResponseBean.DataBean> data = baseBean.getData();
                dataBeanList.clear();
                dataBeanList.addAll(data);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        ((SearchBar) viewDelegate.get(R.id.search_bar)).setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void clear() {
                viewDelegate.setSearchText("");
            }

            @Override
            public void getText(String text) {
                if (!TextUtils.isEmpty(text)) {
                    keyword = text;
                }
            }

            @Override
            public void search() {
                repeatCheck();
            }
        });
    }

}
