package com.hjhq.teamface.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.internal.LinkedTreeMap;
import com.hjhq.teamface.basis.bean.ModuleItemBean;
import com.hjhq.teamface.basis.bean.RowDataBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.memo.CustomDataUtil;
import com.hjhq.teamface.memo.MemoModel;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.adapter.SearchDataListAdapter;
import com.hjhq.teamface.memo.bean.SearchModuleDataBean;
import com.hjhq.teamface.memo.view.SearchMemoDelegate;

import java.util.ArrayList;
import java.util.List;

public class SearchModuleDataActivity extends ActivityPresenter<SearchMemoDelegate, MemoModel> {

    private SearchDataListAdapter mAdapter;
    private RecyclerView mRvContacts;
    private RelativeLayout rlProject;
    private SearchBar mSearchBar;
    private String beanName;
    private String appName;
    private String moduleName;
    private String keyword;
    private String iconColor;
    private String iconType;
    private String iconUrl;
    private ArrayList<ModuleItemBean> choosedDataList;
    private MyRunnable myRunnable;

    @Override
    public void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            appName = bundle.getString(Constants.DATA_TAG1);
            moduleName = bundle.getString(Constants.DATA_TAG2);
            beanName = bundle.getString(Constants.DATA_TAG3);
            choosedDataList = (ArrayList<ModuleItemBean>) bundle.getSerializable(Constants.DATA_TAG4);
            iconType = bundle.getString(Constants.DATA_TAG5);
            iconColor = bundle.getString(Constants.DATA_TAG6);
            iconUrl = bundle.getString(Constants.DATA_TAG7);
            if (choosedDataList == null) {
                choosedDataList = new ArrayList<>();
            }
        }
        initData();
        initListener();
        searchData("");
    }

    private void initListener() {
        mRvContacts.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < choosedDataList.size(); i++) {
                    if (beanName.equals(choosedDataList.get(i).getModule())
                            && mAdapter.getData().get(position).getId().equals(choosedDataList.get(i).getId())) {
                        ToastUtils.showToast(mContext, "已关联该数据");
                        return;
                    }
                }
                SearchModuleDataBean.DataBean dataBean = mAdapter.getData().get(position);
                String name = "";
                String picture = "";
                String subTitle = "";
                String dataId = dataBean.getId().getValue();
                ArrayList<ModuleItemBean> list = new ArrayList<>();
                List<RowDataBean> rowList = dataBean.getRow();
                if (rowList != null && rowList.size() > 0) {
                    for (int i = 0; i < rowList.size(); i++) {
                        RowDataBean rowDataBean = rowList.get(i);
                        if (rowDataBean.getName() != null) {
                            if (rowDataBean.getName().startsWith("personnel_")) {
                                if (rowDataBean.getValue() instanceof List) {
                                    try {
                                        List<LinkedTreeMap<String, String>> memberList = (List<LinkedTreeMap<String, String>>) rowDataBean.getValue();
                                        if (memberList != null && memberList.size() > 0) {
                                            LinkedTreeMap<String, String> member = memberList.get(0);
                                            name = member.get("name");
                                            picture = member.get("picture");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                if (TextUtils.isEmpty(subTitle)) {
                                    subTitle = CustomDataUtil.getDataValue(rowDataBean);
                                }
                            }
                        }
                    }
                }
                ModuleItemBean data = new ModuleItemBean();
                //1项目,2自定义数据
                data.setType("2");
                data.setId(dataId);
                data.setModule(beanName);
                data.setTitle(moduleName);
                data.setSubTitle(subTitle);
                data.setPicture(picture);
                data.setCreatorName(name);
                data.setIcon_color(iconColor);
                data.setIcon_type(iconType);
                data.setIcon_url(iconUrl);
                list.add(data);
                if (list.size() <= 0) {
                    ToastUtils.showToast(mContext, "请选择数据");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DATA_TAG1, list);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }


        });
        mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void search() {
                mSearchBar.postDelayed(myRunnable, 1000);

            }

            @Override
            public void getText(String text) {
                System.gc();
                keyword = text;
                searchData(keyword);
            }
        });


    }

    protected void initData() {
        mRvContacts = viewDelegate.get(R.id.search_result_recycler_view);
        mSearchBar = viewDelegate.get(R.id.search_bar);

        mRvContacts.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mAdapter = new SearchDataListAdapter(null);
        mRvContacts.setAdapter(mAdapter);
        EmptyView emptyView1 = new EmptyView(mContext);
        emptyView1.setEmptyImage(R.drawable.memo_empty_view_img);
        emptyView1.setEmptyTitle("");
        mAdapter.setEmptyView(emptyView1);
        myRunnable = new MyRunnable();

    }

    /**
     * 获取有邮件组件的模块列表
     */
    private void searchData(String word) {


        model.getFirstFieldFromModule(mContext, beanName, word,
                new ProgressSubscriber<SearchModuleDataBean>(mContext, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(SearchModuleDataBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.getData() != null) {
                            mAdapter.getData().clear();
                            mAdapter.getData().addAll(baseBean.getData());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.getData().clear();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<ModuleItemBean> list = (ArrayList<ModuleItemBean>) data.getSerializableExtra(Constants.DATA_TAG1);
                if (list != null && list.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DATA_TAG1, list);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showToast(mContext, "请选择数据");
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    class MyRunnable implements Runnable {
        @Override
        public void run() {
            searchData(keyword);
        }
    }
}
