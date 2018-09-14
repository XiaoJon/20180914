package com.hjhq.teamface.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjhq.teamface.basis.MylayoutManager;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.ModuleItemBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.memo.MemoModel;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.adapter.SearchMemoListAdapter;
import com.hjhq.teamface.memo.bean.MemoListBean;
import com.hjhq.teamface.memo.view.SearchMemoDelegate;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class SearchMemoActivity extends ActivityPresenter<SearchMemoDelegate, MemoModel> {
    public static final int VIEW = 0x11;
    public static final int CHOOSE = 0x12;
    private SearchMemoListAdapter mAdapter;
    private RecyclerView mRvContacts;
    private RelativeLayout rlProject;
    private SearchBar mSearchBar;
    private TextView mTvResultnum;
    private RelativeLayout rlResult;
    private String type = "1";
    private String keyword;
    private EmptyView emptyView1;
    private int mode = VIEW;


    @Override
    public void init() {
        initData();
        initListener();
    }

    private void initListener() {
        mRvContacts.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                switch (mode) {
                    case VIEW:
                        Intent intent = new Intent(mContext, MemoDetailActivity.class);
                        intent.putExtra(Constants.DATA_TAG1, mAdapter.getData().get(position).getId());
                        startActivityForResult(intent, Constants.REQUEST_CODE2);
                        break;
                    case CHOOSE:
                        Intent intent2 = new Intent();
                        intent2.putExtra(Constants.DATA_TAG1, mAdapter.getData().get(position));
                        setResult(RESULT_OK, intent2);
                        finish();
                        break;
                }

            }
        });
        mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {

            }

            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void search() {
                searchData(keyword);
            }

            @Override
            public void getText(String text) {
                keyword = text;

            }
        });

    }

    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString(Constants.DATA_TAG1);
            mode = bundle.getInt(Constants.DATA_TAG2);
        }
        mRvContacts = viewDelegate.get(R.id.search_result_recycler_view);
        mTvResultnum = viewDelegate.get(R.id.tv_num);
        rlResult = viewDelegate.get(R.id.rl_result_remind);
        mSearchBar = viewDelegate.get(R.id.search_bar);
        rlResult.setVisibility(View.GONE);
        mRvContacts.setLayoutManager(new MylayoutManager(this));
        mAdapter = new SearchMemoListAdapter(5, R.layout.memo_list_item_in_search, null);
        mRvContacts.setAdapter(mAdapter);
        emptyView1 = new EmptyView(mContext);
        emptyView1.setEmptyImage(R.drawable.memo_empty_view_img);
        emptyView1.setEmptyTitle("输入关键字进行搜索~");
        mAdapter.setEmptyView(emptyView1);

    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(MessageBean bean) {
        switch (bean.getCode()) {

            case MemoConstant.DATALIST_CHANGE:
                if (!TextUtils.isEmpty(keyword)) {
                    searchData(keyword);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 搜索备忘录
     */
    private void searchData(String word) {
        if (TextUtils.isEmpty(word)) {
            rlResult.setVisibility(View.GONE);
            return;
        }
        emptyView1.setEmptyTitle("正在搜索~");
        model.findMemoWebList(mContext, type, word, new ProgressSubscriber<MemoListBean>(mContext, true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(MemoListBean baseBean) {
                super.onNext(baseBean);
                /*适用于网络端搜索且自动搜索的场景*/
                /*一种缓存方式,防止搜索结果返回顺序搜索顺序不一致导致的搜索关键字和结果不匹配*/
                /*将结果缓存到Map,以关键字为key,结果为value*/
                /*以最后一次网络返回时关键字为key,取出value进行显示*/
                if (baseBean.getData() != null && baseBean.getData().getDataList() != null) {
                    mAdapter.getData().clear();
                    mAdapter.getData().addAll(baseBean.getData().getDataList());
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.getData().clear();
                    mAdapter.notifyDataSetChanged();
                    rlResult.setVisibility(View.GONE);
                }
                rlResult.setVisibility(View.VISIBLE);
                TextUtil.setText(mTvResultnum, "已找到" + mAdapter.getData().size() + "项");
                emptyView1.setEmptyTitle("找不到相关数据。");
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
}
