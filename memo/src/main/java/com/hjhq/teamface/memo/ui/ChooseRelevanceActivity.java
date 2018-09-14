package com.hjhq.teamface.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.bean.ModuleItemBean;
import com.hjhq.teamface.basis.bean.ModuleResultBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.memo.MemoModel;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.adapter.MemoAppRelevanceAdapter;
import com.hjhq.teamface.memo.adapter.MemoModuleRelevanceAdapter;
import com.hjhq.teamface.memo.view.ChooseRelevanceDelegate;

import java.util.ArrayList;
import java.util.List;

public class ChooseRelevanceActivity extends ActivityPresenter<ChooseRelevanceDelegate, MemoModel> {

    private MemoAppRelevanceAdapter mAppAdapter;
    private MemoModuleRelevanceAdapter mModuleAdapter;
    private List<AppModuleBean> moduleList = new ArrayList<>();
    private List<ModuleResultBean.DataBean> appList = new ArrayList<>();
    private RecyclerView mRvAppList;
    private RecyclerView mRvModuleList;
    private boolean appLevel = true;
    private String selectAppName = "";
    private int appIndex = 0;
    private RelevantModuleListFragment moduleListFragment;
    private FrameLayout mFramelayout;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private ArrayList<ModuleItemBean> choosedDataList;

    @Override
    public void init() {
        initIntent();
        initData();
        initListener();
        /*moduleListFragment = RelevantModuleListFragment.newInstance(0);
        mFramelayout = viewDelegate.get(R.id.fl);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.fl, moduleListFragment);
        mTransaction.commitNow();*/


    }

    private void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            choosedDataList = (ArrayList<ModuleItemBean>) bundle.getSerializable(Constants.DATA_TAG1);
            if (choosedDataList == null) {
                choosedDataList = new ArrayList<>();
            }
        }

    }

    private void initListener() {

        mRvAppList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                appLevel = false;
                //mFramelayout.setVisibility(View.VISIBLE);
                //moduleListFragment.setData(mAppAdapter.getData().get(position).getName(), mAppAdapter.getData().get(position).getModules());
                mRvAppList.setVisibility(View.GONE);
                mRvModuleList.setVisibility(View.VISIBLE);
                mModuleAdapter.getData().clear();
                mModuleAdapter.getData().addAll(mAppAdapter.getData().get(position).getModules());
                mModuleAdapter.notifyDataSetChanged();
                selectAppName = mAppAdapter.getData().get(position).getName();
            }
        });
        mRvModuleList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, selectAppName);
                bundle.putString(Constants.DATA_TAG2, mModuleAdapter.getData().get(position).getChinese_name());
                bundle.putString(Constants.DATA_TAG3, mModuleAdapter.getData().get(position).getEnglish_name());
                bundle.putSerializable(Constants.DATA_TAG4, choosedDataList);
                bundle.putString(Constants.DATA_TAG5, mModuleAdapter.getData().get(position).getIcon_type());
                bundle.putString(Constants.DATA_TAG6, mModuleAdapter.getData().get(position).getIcon_color());
                bundle.putString(Constants.DATA_TAG7, mModuleAdapter.getData().get(position).getIcon_url());
                CommonUtil.startActivtiyForResult(mContext, SearchModuleDataActivity.class, Constants.REQUEST_CODE1, bundle);

            }
        });
        viewDelegate.getToolbar().setNavigationOnClickListener(v -> {
            if (appLevel) {
                finish();
            } else {
                // mFramelayout.setVisibility(View.GONE);
                mRvAppList.setVisibility(View.VISIBLE);
                mRvModuleList.setVisibility(View.GONE);
                appLevel = true;
            }
        });

    }

    protected void initData() {
        mRvAppList = viewDelegate.get(R.id.rv_contacts);
        mRvModuleList = viewDelegate.get(R.id.rv_module);
        mRvAppList.setLayoutManager(new LinearLayoutManager(this));
        mRvModuleList.setLayoutManager(new LinearLayoutManager(this));
        mAppAdapter = new MemoAppRelevanceAdapter(appList);
        /*View header = View.inflate(mContext, R.layout.memo_choose_relevance_item, null);
        header.setOnClickListener(v -> UIRouter.getInstance().openUri(mContext, "DDComp://project/memo/choose", null, Constants.REQUEST_CODE2));
        mAppAdapter.addHeaderView(header);
        header.getLayoutParams().height = ((int) DeviceUtils.dpToPixel(mContext, 70));
        TextView headerName = header.findViewById(R.id.tv_nav);
        ImageView headerIcon = header.findViewById(R.id.iv_nav);
        TextUtil.setText(headerName, "项目");*/
        mModuleAdapter = new MemoModuleRelevanceAdapter(moduleList);
        mRvModuleList.setAdapter(mModuleAdapter);


        EmptyView em = new EmptyView(mContext);
        em.setEmptyTitle("模块列表为空");
        mRvAppList.setAdapter(mAppAdapter);
        getAppAndModule();

    }

    /**
     * 获取有邮件组件的模块列表
     */
    private void getAppAndModule() {
        model.getAllModule(mContext, new ProgressSubscriber<ModuleResultBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(ModuleResultBean moduleResultBean) {
                super.onNext(moduleResultBean);
                List<ModuleResultBean.DataBean> data = moduleResultBean.getData();
                if (data != null) {
                    appList.addAll(data);
                }
                for (int i = 0; i < data.size(); i++) {
                    moduleList.addAll(data.get(i).getModules());
                }
                mRvAppList.getAdapter().notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onBackPressed() {
        if (appLevel) {
            finish();
        } else {
            //mFramelayout.setVisibility(View.GONE);
            mRvAppList.setVisibility(View.VISIBLE);
            mRvModuleList.setVisibility(View.GONE);
            appLevel = true;
        }

    }


}
