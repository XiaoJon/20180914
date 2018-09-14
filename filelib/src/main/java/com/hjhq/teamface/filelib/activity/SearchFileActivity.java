package com.hjhq.teamface.filelib.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.FolderNaviData;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.SearchFileResultListAdapter;
import com.hjhq.teamface.filelib.bean.SearchFileListResBean;
import com.hjhq.teamface.filelib.view.SearchFileDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author Administrator
 */
public class SearchFileActivity extends ActivityPresenter<SearchFileDelegate, FilelibModel> {
    private RecyclerView historyRv;
    private RecyclerView resultRv;
    private SearchBar mSearchBar;
    private List<SearchFileListResBean.DataBean> fileListData = new ArrayList<>();
    private List<String> historyItemList = new ArrayList<>();
    private EmptyView emptyView;
    private EmptyView emptyView2;
    private TextView clearAll;
    private View footerView;
    private String keyword;
    private SearchFileResultListAdapter mSearchResultListAdapter;
    HashMap<Conversation, ArrayList<SocketMessage>> map = new HashMap<>();
    private TextView title;
    private TextView num;
    private int folderStyle;
    private String folderId;
    private Bundle mBundle;

    @Override
    public void init() {
        initView();
        initData();
    }


    protected void initView() {
        mSearchBar = (SearchBar) findViewById(R.id.search_bar);
        resultRv = (RecyclerView) findViewById(R.id.search_result_recycler_view);
        historyRv = (RecyclerView) findViewById(R.id.search_history_recycler_view);
        resultRv.setLayoutManager(new LinearLayoutManager(this));
        historyRv.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultListAdapter = new SearchFileResultListAdapter(fileListData);
        resultRv.setAdapter(mSearchResultListAdapter);
        emptyView = new EmptyView(this);
        emptyView.setEmptyTitle(getString(R.string.filelib_search_hint));
        emptyView.setEmptyImage(R.drawable.icon_no_search_result);
        emptyView2 = new EmptyView(this);
        emptyView2.setEmptyTitle(getString(R.string.filelib_search_hint));
        emptyView2.setEmptyImage(R.drawable.icon_no_search_result);
        footerView = getLayoutInflater().inflate(R.layout.item_crm_search_goods_history_footer, null);
        clearAll = (TextView) footerView.findViewById(R.id.tv_clean_history_search_log);
        mSearchBar.setHintText(getString(R.string.file_search_hint));
        mSearchBar.requestTextFocus();
        View headerView = getLayoutInflater().inflate(R.layout.filelib_search_header_layout, null);
        title = (TextView) headerView.findViewById(R.id.name);
        num = (TextView) headerView.findViewById(R.id.num);
        title.setText(getString(R.string.filelib_search_result_title));
        num.setVisibility(View.GONE);
        mSearchResultListAdapter.setEmptyView(emptyView);
        mSearchResultListAdapter.addHeaderView(headerView);


    }


    protected void initData() {
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            folderStyle = mBundle.getInt(FileConstants.FOLDER_STYLE);
            folderId = mBundle.getString(FileConstants.FOLDER_ID);
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
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
                searchFile(keyword);
                SoftKeyboardUtils.hide(mSearchBar.getEditText());

            }

            @Override
            public void getText(String text) {
                keyword = text;
                // searchFile(keyword);

            }
        });
        resultRv.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                ArrayList<FolderNaviData> list = new ArrayList<>();
                if ("0".equals(fileListData.get(position).getSign())) {
                    bundle.putInt(FileConstants.FOLDER_TYPE, folderStyle);
                    bundle.putString(FileConstants.TABLE_ID, fileListData.get(position).getTable_id());
                    bundle.putString(FileConstants.FOLDER_ID, fileListData.get(position).getId());
                    bundle.putInt(FileConstants.FROM_FOLDER_OR_SEARCH, FileConstants.FROM_SEARCH);
                    bundle.putInt(FileConstants.FOLDER_LEVEL, 1);
                    CommonUtil.startActivtiy(SearchFileActivity.this, FileMainActivity.class, bundle);
                } else if ("1".equals(fileListData.get(position).getSign())) {
                    bundle.putString(FileConstants.FILE_ID, fileListData.get(position).getId());
                    bundle.putInt(FileConstants.FOLDER_STYLE, folderStyle);
                    CommonUtil.startActivtiy(SearchFileActivity.this, FileDetailActivity.class, bundle);
                }


            }
        });
    }

    private void searchFile(String word) {
        if (TextUtils.isEmpty(word)) {
            return;
        } else {
            mSearchResultListAdapter.setKeyword(keyword);
            mSearchResultListAdapter.getData().clear();
            mSearchResultListAdapter.notifyDataSetChanged();
            num.setVisibility(View.GONE);
            emptyView.setEmptyTitle(getString(R.string.filelib_searching));
        }
        model.blurSearchFile(SearchFileActivity.this, folderStyle + "", folderId, word,
                new ProgressSubscriber<SearchFileListResBean>(SearchFileActivity.this, true) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        emptyView2.setEmptyTitle(getString(R.string.filelib__search_failed));
                    }

                    @Override
                    public void onNext(SearchFileListResBean searchFileListResBean) {
                        super.onNext(searchFileListResBean);
                        if (word.equals(keyword)) {
                            fileListData.clear();
                            fileListData.addAll(searchFileListResBean.getData());
                            mSearchResultListAdapter.notifyDataSetChanged();
                            num.setVisibility(View.VISIBLE);
                            num.setText(fileListData.size() + "条");
                            emptyView.setEmptyTitle(getString(R.string.filelib__no_search_result));
                        } else {
                            mSearchResultListAdapter.getData().clear();
                            mSearchResultListAdapter.notifyDataSetChanged();
                            num.setVisibility(View.VISIBLE);
                            num.setText(mSearchResultListAdapter.getData().size() + "条");
                            if (TextUtils.isEmpty(keyword)) {
                                emptyView.setEmptyTitle(getString(R.string.filelib_search_hint));
                            } else {
                                emptyView.setEmptyTitle(getString(R.string.filelib_searching));
                            }
                        }


                    }
                });
    }


    public void dismissSoftInputAndShowMenu() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(keyword)) {
            searchFile(keyword);
        }
    }
}
