package com.hjhq.teamface.filelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hjhq.teamface.basis.adapter.FileNaviAdapter;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.FolderNaviData;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.callback.DownloadCallback;
import com.hjhq.teamface.basis.network.callback.UploadCallback;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.FileHelper;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.UriUtil;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.FileListAdapter;
import com.hjhq.teamface.filelib.bean.FileListResBean;
import com.hjhq.teamface.filelib.bean.FolderParentResBean;
import com.hjhq.teamface.filelib.view.FileMainDelegate;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import retrofit2.Call;
import retrofit2.Response;

public class FileMainActivity extends ActivityPresenter<FileMainDelegate, FilelibModel> {
    //公司文件夹
    public static final int TAG1 = 1;
    //应用文件夹
    public static final int TAG2 = 2;
    //个人文件夹
    public static final int TAG3 = 3;
    //我共享的文件夹
    public static final int TAG4 = 4;
    //共享给我的文件夹
    public static final int TAG5 = 5;

    private RelativeLayout mRlSearch;
    private RelativeLayout mRlNav;
    private RecyclerView mRvNavi;
    private RecyclerView mRvFileList;
    private FileListAdapter mFileAdapter;
    private FileNaviAdapter mNaviAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private EmptyView mEmptyView1;
    private View mSearchLayout;
    private Bundle mBundle;
    private int folderLevel = 1;
    private String folderName = "";
    private String folderId;
    private String currentSelectFileId;
    private String tableId;
    private String moduleId;

    private String folderUrl;
    //共享与我共享使用
    private String style;
    private int folderStyle;
    //?当前文件夹类型
    private ArrayList<FolderNaviData> naviData = new ArrayList<>();
    private List<FileListResBean.DataBean.DataListBean> dataList = new ArrayList<>();
    private ArrayList<FolderNaviData> naviDataNextLevel = new ArrayList<>();

    private int currentPageNo = 1;//当前页数
    private int pageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private int totalNum = -1;//总数
    private int state = Constants.NORMAL_STATE;
    private int pageSize = 20;
    private String isManager = "0";

    private String authUpload;
    private String authDownload;
    private String authPreview;
    private long lastRefreshTime;
    private String publicOrPrivate;

    private int fromWitch = FileConstants.FROM_FOLDER;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mBundle != null) {
            super.onSaveInstanceState(mBundle);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mBundle = savedInstanceState;
        }
    }

    @Override
    public void init() {
        initView();
    }


    protected void initView() {
        if (mBundle == null) {
            mBundle = getIntent().getExtras();
        }
        if (mBundle != null) {
            folderStyle = mBundle.getInt(FileConstants.FOLDER_TYPE);
            folderLevel = mBundle.getInt(FileConstants.FOLDER_LEVEL);
            folderName = mBundle.getString(FileConstants.FOLDER_NAME);
            folderId = mBundle.getString(FileConstants.FOLDER_ID, "");
            folderUrl = mBundle.getString(FileConstants.FOLDER_URL, "");
            fromWitch = mBundle.getInt(FileConstants.FROM_FOLDER_OR_SEARCH);
            tableId = mBundle.getString(FileConstants.TABLE_ID);
            isManager = mBundle.getString(FileConstants.IS_MANAGER);
            moduleId = mBundle.getString(FileConstants.MODULE_ID);
            if (folderLevel >= 1) {
                publicOrPrivate = mBundle.getString(FileConstants.PUBLIC_OR_PRIVATE);
            }

            switch (folderStyle) {
                case 1:
                    if (folderLevel >= 1) {
                        authDownload = mBundle.getString(FileConstants.AUTH_DOWNLOAD);
                        authPreview = mBundle.getString(FileConstants.AUTH_PREVIEW);
                        authUpload = mBundle.getString(FileConstants.AUTH_UPLOAD);
                        if ("1".equals(authUpload)) {
                            viewDelegate.showMenu(0);
                        }
                    } else {
                        viewDelegate.showMenu(0);
                    }
                    break;
                case 2:
                    break;
                case 3:
                    viewDelegate.showMenu(0);
                    break;
                case 4:

                    break;
                case 5:

                    break;
                default:
                    break;
            }
            naviData = (ArrayList<FolderNaviData>) mBundle.getSerializable(FileConstants.FOLDER_NAVI_DATA);

            if (naviData == null) {
                naviData = new ArrayList<>();
            }
            LogUtil.e("naviData==   " + naviData.size());
            //文件夹名字
            if (folderLevel == 0) {
                viewDelegate.setTitle(getRootFolderName(folderStyle));
            }
        }
        mRlSearch = (RelativeLayout) findViewById(R.id.search_rl);
        mRlNav = (RelativeLayout) findViewById(R.id.rl_nav);
        mRvNavi = (RecyclerView) findViewById(R.id.rv_navi);
        mRvFileList = (RecyclerView) findViewById(R.id.rv_department);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        mFileAdapter = new FileListAdapter(folderLevel, folderStyle, dataList);
        mSearchLayout = findViewById(R.id.search_layout);
        mRvFileList.setLayoutManager(new LinearLayoutManager(FileMainActivity.this));
        mRvFileList.setAdapter(mFileAdapter);
        ((DefaultItemAnimator) mRvFileList.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager naviLm = new LinearLayoutManager(FileMainActivity.this);
        naviLm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvNavi.setLayoutManager(naviLm);
        mNaviAdapter = new FileNaviAdapter(null);
        mRvNavi.setAdapter(mNaviAdapter);
        if (fromWitch == FileConstants.FROM_SEARCH) {
            getNaviData();
        } else {
            viewDelegate.setTitle(folderName + "");
        }
        //应用文件夹的0,1两个层级没有搜索
        setSearchBarVisibility();
        getNetData();
        mNaviAdapter.setNewData(naviData);
        mEmptyView1 = new EmptyView(this);
        mEmptyView1.setEmptyImage(R.drawable.workbench_empty);
        mEmptyView1.setEmptyTitle("正在加载~");
        mFileAdapter.setEmptyView(mEmptyView1);
        showNav();

    }

    /**
     * 层级小于2的应用文件夹不显示搜索栏(只能进行模块内文件搜索)
     */
    private void setSearchBarVisibility() {
        if (folderStyle == 2 && folderLevel < 2) {
            mSearchLayout.setVisibility(View.GONE);
        } else {
            mSearchLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 导航
     */
    private void showNav() {
        if (folderLevel == 0) {
            mRlNav.setVisibility(View.GONE);
        } else {
            mRlNav.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取导航栏数据(搜索文件进入时需要使用)
     */
    private void getNaviData() {

        if (TextUtils.isEmpty(folderId)) {
            naviData.clear();
            FolderNaviData data = new FolderNaviData();
            data.setFolderId("");
            data.setFloderType(folderStyle);
            data.setFolderName(getRootFolderName(folderStyle));
            data.setFolderLevel(0);
            naviData.add(data);
            mNaviAdapter.notifyDataSetChanged();
        } else {
            model.getBlurResultParentInfo(FileMainActivity.this, folderId,
                    new ProgressSubscriber<FolderParentResBean>(FileMainActivity.this, false) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(FolderParentResBean baseBean) {
                            super.onNext(baseBean);

                            naviData.clear();
                            FolderNaviData data = new FolderNaviData();
                            data.setFolderId("");
                            data.setFloderType(folderStyle);
                            data.setFolderName(getRootFolderName(folderStyle));
                            data.setFolderLevel(0);
                            naviData.add(data);
                            if (baseBean.getData().size() > 0) {
                                viewDelegate.setTitle(baseBean.getData().get(baseBean.getData().size() - 1).getName());
                            }
                            for (int i = 0; i < baseBean.getData().size(); i++) {
                                FolderNaviData data2 = new FolderNaviData();
                                data2.setFolderId(baseBean.getData().get(i).getId());
                                data2.setFloderType(folderStyle);
                                data2.setFolderName(baseBean.getData().get(i).getName());
                                data2.setFolderLevel(i + 1);
                                naviData.add(data2);
                            }
                            mNaviAdapter.notifyDataSetChanged();


                        }
                    });
        }

    }

    /**
     * 获取根目录文件夹名字
     *
     * @param folderStyle
     * @return
     */
    private String getRootFolderName(int folderStyle) {
        switch (folderStyle) {
            case 1:
                return "公司文件";
            case 2:
                return "应用文件";
            case 3:
                return "个人文件";
            case 4:
                return "我的共享";
            case 5:
                return "与我共享";
            default:
                break;

        }
        return "";
    }


    /**
     * 获取列表数据
     */
    private synchronized void getNetData() {
        currentPageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        pageSize = Constants.PAGESIZE;

        if (folderLevel >= 1) {

            String param = "";
            if (folderStyle == 1 || folderStyle == 3) {
                param = folderStyle + "";
            } else if (folderStyle == 4 || folderStyle == 5) {
                param = tableId;
            }
            if (folderStyle == 2) {
                getAppFileList();
            } else {
                getFileList(param);
            }
        } else {
            if (folderStyle == 2) {
                getAppFileList();
            } else {
                model.queryFileList(FileMainActivity.this, folderStyle + "",
                        currentPageNo, pageSize, FileConstants.ALL, new ProgressSubscriber<FileListResBean>(this, false) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                mRefreshLayout.finishLoadMore(false);
                                mEmptyView1.setEmptyTitle("加载错误~");
                                if (state == Constants.LOAD_STATE) {
                                    mFileAdapter.loadMoreFail();
                                }
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(FileListResBean fileListResBean) {
                                super.onNext(fileListResBean);

                                mEmptyView1.setEmptyTitle("无内容~");
                                showData(fileListResBean);
                            }
                        });
            }

        }
    }

    /**
     * 获取公开文件夹,个人文件夹,共享文件夹列表使用
     *
     * @param param
     */
    private void getFileList(String param) {
        model.queryFilePartList(FileMainActivity.this, param,
                folderId, currentPageNo, pageSize, FileConstants.ALL,
                new ProgressSubscriber<FileListResBean>(this, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRefreshLayout.finishLoadMore(false);
                        mEmptyView1.setEmptyTitle("加载错误~");
                        e.printStackTrace();
                        if (state == Constants.LOAD_STATE) {
                            mFileAdapter.loadMoreFail();
                        }
                    }

                    @Override
                    public void onNext(FileListResBean fileListResBean) {
                        super.onNext(fileListResBean);

                        mEmptyView1.setEmptyTitle("无内容~");
                        showData(fileListResBean);

                    }
                });
    }

    /**
     * 处理数据
     *
     * @param fileListResBean
     */
    private void showData(FileListResBean fileListResBean) {
        mRefreshLayout.finishLoadMore(true);
        List<FileListResBean.DataBean.DataListBean> newDataList = fileListResBean.getData().getDataList();
        List<FileListResBean.DataBean.DataListBean> oldDataList = mFileAdapter.getData();
        switch (state) {
            case Constants.NORMAL_STATE:
            case Constants.REFRESH_STATE:
                oldDataList.clear();

                break;
            case Constants.LOAD_STATE:

                break;
            default:
                break;
        }
        if (newDataList != null && newDataList.size() > 0) {
            oldDataList.addAll(newDataList);
        }
        mFileAdapter.notifyDataSetChanged();

        PageInfo pageInfo = fileListResBean.getData().getPageInfo();
        totalPages = pageInfo.getTotalPages();
        totalNum = pageInfo.getTotalRows();
        pageNo = pageInfo.getPageNum();
        if (totalNum == mFileAdapter.getData().size()) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        }
    }

    /**
     * 加载应用文件
     */
    private void getAppFileList() {
        if (folderLevel == 0) {
            model.queryAppFileList(FileMainActivity.this, folderStyle + "",
                    currentPageNo, pageSize,
                    new ProgressSubscriber<FileListResBean>(this, false) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mRefreshLayout.finishLoadMore(false);
                            mEmptyView1.setEmptyTitle("加载错误~");
                            e.printStackTrace();
                            if (state == Constants.LOAD_STATE) {
                                mFileAdapter.loadMoreFail();
                            }
                        }

                        @Override
                        public void onNext(FileListResBean fileListResBean) {
                            super.onNext(fileListResBean);

                            mEmptyView1.setEmptyTitle("无内容~");
                            showData(fileListResBean);

                        }
                    });
        }
        if (folderLevel == 1) {
            model.queryModuleFileList(FileMainActivity.this, tableId, folderStyle + "",
                    currentPageNo, pageSize,
                    new ProgressSubscriber<FileListResBean>(this, false) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mRefreshLayout.finishLoadMore(false);
                            mEmptyView1.setEmptyTitle("加载错误~");
                            e.printStackTrace();
                            if (state == Constants.LOAD_STATE) {
                                mFileAdapter.loadMoreFail();
                            }
                        }

                        @Override
                        public void onNext(FileListResBean fileListResBean) {
                            super.onNext(fileListResBean);

                            mEmptyView1.setEmptyTitle("无内容~");
                            showData(fileListResBean);

                        }
                    });
        }
        if (folderLevel == 2) {
            model.queryModulePartFileList(FileMainActivity.this, tableId, folderStyle + "",
                    currentPageNo, pageSize,
                    new ProgressSubscriber<FileListResBean>(this, false) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mRefreshLayout.finishLoadMore(false);
                            mEmptyView1.setEmptyTitle("加载错误~");
                            e.printStackTrace();
                            if (state == Constants.LOAD_STATE) {
                                mFileAdapter.loadMoreFail();
                            }
                        }

                        @Override
                        public void onNext(FileListResBean fileListResBean) {
                            super.onNext(fileListResBean);

                            mEmptyView1.setEmptyTitle("无内容~");
                            showData(fileListResBean);

                        }
                    });
        }

    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        mRlSearch.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(FileConstants.FOLDER_STYLE, folderStyle);
            if (folderStyle == 2) {
                bundle.putString(FileConstants.FOLDER_ID, tableId);
            } else {
                bundle.putString(FileConstants.FOLDER_ID, folderId);
            }

            CommonUtil.startActivtiy(FileMainActivity.this, SearchFileActivity.class, bundle);
        });
        viewDelegate.getToolbar().setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        //监听下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                        mRefreshLayout.finishRefresh();
                    }
                }, 500);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mFileAdapter.getData().size() >= totalNum) {
                    mRefreshLayout.finishLoadMore();
                    return;
                }
                state = Constants.LOAD_STATE;
                getNetData();
            }
        });


        mRvFileList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                System.gc();
                Bundle bundle = new Bundle();
                ArrayList<FolderNaviData> list = new ArrayList<>();
                if ("0".equals(dataList.get(position).getSign())) {
                    //文件夹
                    if (fromWitch == FileConstants.FROM_FOLDER) {
                        bundle.putInt(FileConstants.FROM_FOLDER_OR_SEARCH, FileConstants.FROM_FOLDER);
                        if (folderStyle == 4 || folderStyle == 5) {
                            bundle.putString(FileConstants.TABLE_ID, dataList.get(position).getTable_id());
                        }
                        if (folderStyle == 2) {
                            bundle.putString(FileConstants.TABLE_ID, dataList.get(position).getModel_id());
                        }

                        bundle.putInt(FileConstants.FOLDER_TYPE, folderStyle);
                        bundle.putString(FileConstants.FOLDER_ID, dataList.get(position).getId());
                        bundle.putString(FileConstants.FOLDER_NAME, dataList.get(position).getName());
                        bundle.putInt(FileConstants.FOLDER_LEVEL, folderLevel + 1);
                        bundle.putString(FileConstants.FOLDER_URL, dataList.get(position).getUrl());
                        bundle.putString(FileConstants.MODULE_ID, dataList.get(position).getModel_id());
                        bundle.putString(FileConstants.IS_MANAGER, dataList.get(position).getIs_manage());
                        if (folderLevel == 0) {
                            bundle.putString(FileConstants.PUBLIC_OR_PRIVATE, dataList.get(position).getType());
                            publicOrPrivate = dataList.get(position).getType() + "";
                            bundle.putString(FileConstants.AUTH_PREVIEW, dataList.get(position).getPreview());
                            bundle.putString(FileConstants.AUTH_UPLOAD, dataList.get(position).getUpload());
                            bundle.putString(FileConstants.AUTH_DOWNLOAD, dataList.get(position).getDownload());
                        } else {
                            bundle.putString(FileConstants.PUBLIC_OR_PRIVATE, publicOrPrivate);
                            bundle.putString(FileConstants.AUTH_PREVIEW, authPreview);
                            bundle.putString(FileConstants.AUTH_UPLOAD, authUpload);
                            bundle.putString(FileConstants.AUTH_DOWNLOAD, authDownload);
                        }
                        list.addAll(naviData);
                        FolderNaviData c = new FolderNaviData();
                        c.setFolderName(mFileAdapter.getData().get(position).getName());
                        c.setFloderType(folderStyle);
                        c.setFolderLevel(folderLevel + 1);
                        c.setFolderId(folderId + position);
                        list.add(c);
                        bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, list);
                        CommonUtil.startActivtiy(FileMainActivity.this, FileMainActivity.class, bundle);
                    } else if (fromWitch == FileConstants.FROM_SEARCH) {
                        folderId = dataList.get(position).getId();
                        folderLevel++;
                        viewDelegate.setTitle(dataList.get(position).getName());
                        getNaviData();
                        state = Constants.REFRESH_STATE;
                        refreshData();

                    }

                } else if ("1".equals(dataList.get(position).getSign())) {
                    //文件

                    bundle.putString(FileConstants.FILE_ID, dataList.get(position).getId());
                    bundle.putString(FileConstants.IS_MANAGER, isManager);
                    bundle.putString(FileConstants.FOLDER_URL, dataList.get(position).getUrl());
                    bundle.putString(FileConstants.FILE_TYPE, dataList.get(position).getSiffix());
                    bundle.putInt(FileConstants.FOLDER_STYLE, folderStyle);
                    list.add(naviData.get(0));
                    bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, list);
                    bundle.putString(FileConstants.AUTH_PREVIEW, dataList.get(position).getPreview());
                    bundle.putString(FileConstants.AUTH_UPLOAD, dataList.get(position).getUpload());
                    bundle.putString(FileConstants.AUTH_DOWNLOAD, dataList.get(position).getDownload());
                    CommonUtil.startActivtiyForResult(FileMainActivity.this,
                            FileDetailActivity.class, Constants.REQUEST_CODE1, bundle);
                }


            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                currentSelectFileId = dataList.get(position).getId();

                switch (folderStyle) {
                    //公司文件
                    case 1:
                        if (folderLevel < 1) {

                            rootFolderManage(position);
                        } else {
                            childFolderManage(position);
                        }
                        break;
                    //应用文件
                    case 2:
                        break;
                    //个人文件
                    case 3:
                        managePersonalFolder(position);
                        break;
                    //我共享的
                    case 4:
                        cancelShare(position);
                        break;
                    //与我共享
                    case 5:
                        quitShare(position);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                /*if ("0".equals(dataList.get(position).getSign())) {
                    DownloadService.getInstance().downloadFileFromNetdisk(dataList.get(position).getId(), dataList.get(position).getName(), 0);
                }
                if ("1".equals(dataList.get(position).getSign())) {
                    DownloadService.getInstance().downloadFileFromNetdisk(dataList.get(position).getId(), dataList.get(position).getName(), 1);
                }*/

            }
        });
        mRvNavi.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                System.gc();
                if (position == naviData.size() - 1) {
                    return;
                }
                if (fromWitch == FileConstants.FROM_FOLDER) {
                    EventBusUtils.sendEvent(new MessageBean(position, Constants.JUMP_FOLDER, null));
                } else if (fromWitch == FileConstants.FROM_SEARCH) {
                    folderId = naviData.get(position).getFolderId();
                    if (position == 0) {
                        folderLevel = 0;
                        viewDelegate.setTitle(getRootFolderName(folderStyle));
                    } else {
                        folderLevel = 1;
                        viewDelegate.setTitle(naviData.get(position).getFolderName());
                    }
                    showNav();
                    state = Constants.REFRESH_STATE;
                    refreshData();
                    getNaviData();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (folderLevel == 0) {
            Bundle bundle = new Bundle();
            if (folderStyle == 1) {
                bundle.putInt(Constants.DATA_TAG1, AddFolderActivity.TAG1);
                bundle.putString(Constants.DATA_TAG2, "");
                bundle.putInt(Constants.DATA_TAG3, folderStyle);
                CommonUtil.startActivtiyForResult(this, AddFolderActivity.class, Constants.REQUEST_CODE1, bundle);
            } else {
                childFolderAddMenu();
            }
        } else {
            childFolderAddMenu();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 根目录文件夹管理
     *
     * @param position
     */
    private void rootFolderManage(int position) {
        if (!"1".equals(dataList.get(position).getIs_manage())) {
            ToastUtils.showToast(mContext, getString(R.string.filelib_file_no_manage_auth));
            return;
        }
        String[] menu = getResources().getStringArray(R.array.filelib_file_root_company_folder_menu_array);
        Bundle bundle = new Bundle();
        PopUtils.showBottomMenu(FileMainActivity.this, mRvFileList.getRootView(), getString(R.string.filelib_operation), menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        switch (p) {
                            case 0:
                                bundle.putInt(FileConstants.FOLDER_STYLE, folderStyle);
                                bundle.putString(FileConstants.FOLDER_ID, dataList.get(position).getId());
                                bundle.putInt(FileConstants.FOLDER_LEVEL, folderLevel + 1);
                                try {
                                    bundle.putInt(FileConstants.FOLDER_TYPE, Integer.parseInt(dataList.get(position).getType()));
                                } catch (NumberFormatException e) {
                                    bundle.putInt(FileConstants.FOLDER_TYPE, 0);
                                }
                                CommonUtil.startActivtiyForResult(FileMainActivity.this,
                                        FolderAuthManageActivity.class, Constants.REQUEST_CODE1, bundle);
                                break;
                            case 1:
                                bundle.putInt(Constants.DATA_TAG1, AddFolderActivity.TAG3);
                                bundle.putString(Constants.DATA_TAG2, dataList.get(position).getId());
                                bundle.putInt(Constants.DATA_TAG3, folderStyle);
                                bundle.putString(Constants.DATA_TAG4, dataList.get(position).getName());
                                bundle.putString(Constants.DATA_TAG5, dataList.get(position).getColor());
                                CommonUtil.startActivtiyForResult(FileMainActivity.this,
                                        AddFolderActivity.class, Constants.REQUEST_CODE1, bundle);
                                break;
                            case 2:
                                //删除文件夹
                                deleteFolder(position);

                                break;
                            default:

                                break;


                        }


                        return true;
                    }
                });
    }

    /**
     * 子目录文件夹管理
     *
     * @param position
     */
    private void childFolderManage(int position) {
        String[] menu = getResources().getStringArray(R.array.filelib_non_public_folder_menu_array);
        PopUtils.showBottomMenu(FileMainActivity.this, mRvFileList.getRootView(), getString(R.string.filelib_operation), menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        Bundle bundle = new Bundle();
                        switch (p) {
                            case 0:
                                //成员管理
                                bundle.putInt(FileConstants.FOLDER_STYLE, folderStyle);
                                bundle.putString(FileConstants.FOLDER_ID, dataList.get(position).getId());
                                bundle.putInt(FileConstants.FOLDER_LEVEL, folderLevel + 1);
                                try {
                                    if (folderLevel == 0) {
                                        bundle.putInt(FileConstants.FOLDER_TYPE, TextUtil.parseInt(dataList.get(position).getType()));
                                    } else {
                                        bundle.putInt(FileConstants.FOLDER_TYPE, TextUtil.parseInt(publicOrPrivate));
                                    }

                                } catch (NumberFormatException e) {
                                    bundle.putInt(FileConstants.FOLDER_TYPE, 0);
                                }
                                CommonUtil.startActivtiyForResult(FileMainActivity.this,
                                        FolderAuthManageActivity.class, Constants.REQUEST_CODE1, bundle);


                                // CommonUtil.startActivtiyForResult(CompanyFileActivity.this, SetMemberFolderAuthActivity.class, Constants.REQUEST_CODE1, bundle);

                                break;
                            case 1:
                                //管理文件夹
                                bundle.putInt(Constants.DATA_TAG1, AddFolderActivity.TAG3);
                                bundle.putString(Constants.DATA_TAG2, dataList.get(position).getId());
                                bundle.putInt(Constants.DATA_TAG3, folderStyle);
                                bundle.putString(Constants.DATA_TAG4, dataList.get(position).getName());
                                bundle.putString(Constants.DATA_TAG5, dataList.get(position).getColor());
                                CommonUtil.startActivtiyForResult(FileMainActivity.this, AddFolderActivity.class, Constants.REQUEST_CODE1, bundle);


                                break;
                            case 2:
                                //移动
                                bundle.putInt(FileConstants.FOLDER_TYPE, naviData.get(0).getFloderType());
                                bundle.putString(FileConstants.FOLDER_ID, naviData.get(0).getFolderId());
                                bundle.putString(FileConstants.FOLDER_NAME, naviData.get(0).getFolderName());
                                bundle.putInt(FileConstants.FOLDER_LEVEL, naviData.get(0).getFolderLevel());
                                bundle.putString(FileConstants.FILE_ID, dataList.get(position).getId());
                                bundle.putInt(FileConstants.FOLDER_STYLE, folderStyle);
                                bundle.putInt(FileConstants.MOVE_OR_COPY, FileConstants.MOVE);
                                ArrayList<FolderNaviData> list = new ArrayList<>();
                                if (naviData.size() > 0) {
                                    list.add(naviData.get(0));
                                }
                                bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, list);
                                CommonUtil.startActivtiy(FileMainActivity.this, MoveFileActivity.class, bundle);

                                break;
                            case 3:
                                //共享
                                chooseShareMember();
                                break;
                            case 4:
                                deleteFolder(position);
                                break;
                            default:

                                break;


                        }


                        return true;
                    }
                });
    }

    private void chooseShareMember() {
        Bundle bundle = new Bundle();
        ArrayList<Member> members = new ArrayList<Member>();
        Member member = new Member();
        member.setCheck(false);
        member.setSelectState(C.CAN_NOT_SELECT);
        member.setId(TextUtil.parseLong(SPHelper.getEmployeeId()));
        members.add(member);
        bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
        bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
        CommonUtil.startActivtiyForResult(FileMainActivity.this,
                SelectMemberActivity.class, Constants.REQUEST_CODE4, bundle);
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        currentPageNo = 1;
        state = Constants.REFRESH_STATE;
        getNetData();
    }

    /**
     * 删除文件夹
     *
     * @param position
     */
    private void deleteFolder(int position) {
        //根据文件或文件夹更改提示语
        DialogUtils.getInstance()
                .sureOrCancel(FileMainActivity.this, getString(R.string.filelib_operation_hint),
                        getString(R.string.filelib_delete_folder_hint), mRlSearch.getRootView(), new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                model.delFileLibrary(FileMainActivity.this,
                                        dataList.get(position).getId(),
                                        new ProgressSubscriber<BaseBean>(FileMainActivity.this, false) {
                                            @Override
                                            public void onError(Throwable e) {
                                                super.onError(e);

                                                ToastUtils.showError(mContext, "删除失败!");
                                            }

                                            @Override
                                            public void onNext(BaseBean baseBean) {
                                                super.onNext(baseBean);


                                                state = Constants.REFRESH_STATE;
                                                getNetData();
                                            }
                                        });
                            }
                        });
    }

    /**
     * 子文件夹菜单
     */
    private void childFolderAddMenu() {
        String[] menu1 = getResources().getStringArray(R.array.filelib_child_folder_add_menu_array1);
        String[] menu2 = getResources().getStringArray(R.array.filelib_child_folder_add_menu_array2);
        String[] menu = menu2;

        if (folderStyle == 1) {
            if ("1".equals(isManager)) {
                menu = menu1;
            } else {
                menu = menu2;
            }
        } else if (folderStyle == 3) {
            menu = menu1;
        }


        PopUtils.showBottomMenu(FileMainActivity.this, mRvFileList.getRootView(), getString(R.string.filelib_operation), menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        switch (p) {
                            case 0:

                                //从文件夹选择
                                FileHelper.showFileChooser(FileMainActivity.this);
                                break;
                            case 1:
                                //从相册选择
                                CommonUtil.getImageFromAlbumByMultiple(FileMainActivity.this, 1);

                                break;
                            case 2:
                                //新建文件件
                                Bundle bundle = new Bundle();
                                if (folderStyle == 1) {
                                    if (folderLevel >= 1) {

                                        bundle.putInt(Constants.DATA_TAG1, AddFolderActivity.TAG4);
                                        bundle.putString(Constants.DATA_TAG2, folderId);
                                    } else {
                                        bundle.putString(Constants.DATA_TAG2, "");
                                    }
                                } else if (folderStyle == 3) {
                                    bundle.putInt(Constants.DATA_TAG1, AddFolderActivity.TAG2);
                                    if (folderLevel >= 1) {
                                        bundle.putString(Constants.DATA_TAG2, folderId);
                                    } else {
                                        bundle.putString(Constants.DATA_TAG2, "");
                                    }
                                }

                                bundle.putInt(Constants.DATA_TAG3, folderStyle);
                                CommonUtil.startActivtiyForResult(FileMainActivity.this,
                                        AddFolderActivity.class, Constants.REQUEST_CODE1, bundle);

                                break;
                            default:

                                break;


                        }


                        return true;
                    }
                });
    }

    /**
     * 管理个人文件夹
     */
    private void managePersonalFolder(int position) {
        String[] menu = getResources().getStringArray(R.array.filelib_personal_folder_menu_array);
        Bundle bundle = new Bundle();
        PopUtils.showBottomMenu(FileMainActivity.this, mRvFileList.getRootView(), getString(R.string.filelib_operation), menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        switch (p) {
                            case 0:
                                //管理文件夹
                                bundle.putInt(Constants.DATA_TAG1, AddFolderActivity.TAG3);
                                bundle.putString(Constants.DATA_TAG2, dataList.get(position).getId());
                                bundle.putInt(Constants.DATA_TAG3, folderStyle);
                                bundle.putString(Constants.DATA_TAG4, dataList.get(position).getName());
                                bundle.putString(Constants.DATA_TAG5, dataList.get(position).getColor());
                                CommonUtil.startActivtiyForResult(FileMainActivity.this, AddFolderActivity.class, Constants.REQUEST_CODE1, bundle);
                                break;
                            case 1:
                                //移动
                                bundle.putInt(FileConstants.FOLDER_TYPE, naviData.get(0).getFloderType());
                                bundle.putInt(FileConstants.FOLDER_STYLE, folderStyle);
                                bundle.putString(FileConstants.FOLDER_ID, naviData.get(0).getFolderId());
                                bundle.putString(FileConstants.FOLDER_NAME, naviData.get(0).getFolderName());
                                bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
                                bundle.putString(FileConstants.FILE_ID, dataList.get(position).getId());
                                bundle.putInt(FileConstants.MOVE_OR_COPY, FileConstants.MOVE);
                                ArrayList<FolderNaviData> list = new ArrayList<>();
                                if (naviData.size() > 0) {
                                    list.add(naviData.get(0));
                                }
                                bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, list);
                                CommonUtil.startActivtiy(FileMainActivity.this, MoveFileActivity.class, bundle);
                                break;
                            case 2:
                                //共享
                                chooseShareMember();
                                break;
                            case 3:
                                //删除
                                deleteFolder(position);
                                break;
                            default:

                                break;


                        }


                        return true;
                    }
                });
    }

    /**
     * 公司根目录文件夹操作
     */

    private void manageCompanyRootFolder() {
        String[] menu = {"管理文件夹", "删除文件夹"};
        PopUtils.showBottomMenu(FileMainActivity.this, mRvFileList.getRootView(), "操作", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        switch (p) {
                            case 0:

                                break;
                            case 1:


                                break;
                            default:

                                break;


                        }


                        return true;
                    }
                });
    }

    /**
     * 取消共享
     *
     * @param position
     */
    private void cancelShare(int position) {
        String[] menu = getResources().getStringArray(R.array.filelib_my_shared_folder_menu_array);
        PopUtils.showBottomMenu(FileMainActivity.this, mRvFileList.getRootView(),
                getString(R.string.filelib_operation), menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        model.cancelShare(FileMainActivity.this, dataList.get(position).getFile_id(),
                                new ProgressSubscriber<BaseBean>(FileMainActivity.this, false) {
                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        ToastUtils.showError(mContext, getString(R.string.filelib_cancel_share_failed_hint));
                                    }

                                    @Override
                                    public void onNext(BaseBean baseBean) {
                                        super.onNext(baseBean);
                                        ToastUtils.showSuccess(mContext, getString(R.string.filelib_cancel_share_success_hint));
                                        dataList.remove(position);
                                        mFileAdapter.notifyDataSetChanged();
                                    }
                                });
                        return true;
                    }
                });
    }

    /**
     * 退出共享
     */
    private void quitShare(int position) {
        String[] menu = getResources().getStringArray(R.array.filelib_share_to_me_folder_menu_array);
        PopUtils.showBottomMenu(FileMainActivity.this, mRvFileList.getRootView(),
                getString(R.string.filelib_operation), menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        model.quitShare(FileMainActivity.this,
                                dataList.get(position).getFile_id(),
                                new ProgressSubscriber<BaseBean>(FileMainActivity.this, false) {
                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);


                                        ToastUtils.showError(mContext, getString(R.string.filelib_quit_share_failed_hint));
                                    }

                                    @Override
                                    public void onNext(BaseBean baseBean) {
                                        super.onNext(baseBean);


                                        ToastUtils.showSuccess(mContext, getString(R.string.filelib_quit_share_success_hint));
                                        dataList.remove(position);
                                        mFileAdapter.notifyDataSetChanged();
                                    }
                                });
                        return true;
                    }
                });
    }

    /**
     * 公司二级文件夹操作
     */
    private void manageCompanyChildFolder() {
        String[] menu = {"管理员设置", "管理文件夹", "移动", "共享", "删除"};
        PopUtils.showBottomMenu(FileMainActivity.this, mRvFileList.getRootView(), "操作", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        switch (p) {
                            case 0:

                                break;
                            case 1:


                                break;
                            case 2:


                                break;
                            case 3:


                                break;
                            case 4:


                                break;
                            default:

                                break;


                        }


                        return true;
                    }
                });
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 接到EventBus消息后关闭相应的Activity
     *
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jumpFolder(MessageBean bean) {
        //关闭文件夹
        if (bean.getTag().equals(Constants.JUMP_FOLDER) && bean.getCode() < folderLevel) {
            finish();
        } else if (FileConstants.DELETE_FILE_SUCCESS.equals(bean.getTag())) {
            getNetData();
        } else if (FileConstants.MOVE_FILE_SUCCESS.equals(bean.getTag())) {
            getNetData();
        }

    }

    /**
     * 分享文件
     *
     * @param list
     */
    private void shareFile(List<Member> list) {
        if (list.size() <= 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i).getId() + "");
            } else {
                sb.append(list.get(i).getId() + ",");
            }
        }
        String ids = sb.toString();
        if (TextUtils.isEmpty(ids)) {
            return;
        }
        model.shareFileLibaray(FileMainActivity.this, currentSelectFileId, ids,
                new ProgressSubscriber<BaseBean>(FileMainActivity.this, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.showError(mContext, "共享失败!");
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        ToastUtils.showSuccess(mContext, "共享成功!");

                    }
                });

    }

    /**
     * 上传文件回调,上传成功刷新列表
     */
    DownloadCallback callback = new DownloadCallback() {

        @Override
        public void onFailure(Call call, Throwable t) {

        }

        @Override
        public void onSuccess(Call call, Response response) {
            state = Constants.REFRESH_STATE;
            //上传完成,更新列表
            refreshData();
        }

        @Override
        public void onLoading(long total, long progress) {
            LogUtil.e("上传进度" + progress + "/" + total);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //文件夹中选择文件
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.CHOOSE_LOCAL_FILE) {
            Uri uri = data.getData();
            LogUtil.e("audioUri==   " + uri);

            String path = UriUtil.getPhotoPathFromContentUri(FileMainActivity.this, uri);
            LogUtil.e("path==   " + path);
            File file = new File(path);

            if (file.exists()) {
                checkFileSizeAndUpload(file);
            } else {
                ToastUtils.showError(mContext, "数据错误，请重新上传");
            }
        }
        //创建/编辑文件夹成功,刷新列表
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE1) {
            state = Constants.REFRESH_STATE;
            refreshData();
        }
        //共享
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE4) {

            List<Member> list = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (list != null) {
                shareFile(list);
            }


        }
/**
 * 相册选择图片
 */
        if (resultCode == Activity.RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

                for (int i = 0; i < photos.size(); i++) {
                    File file = new File(photos.get(i));
                    if (file.exists()) {
                        checkFileSizeAndUpload(file);
                    } else {
                        ToastUtils.showError(mContext, "数据错误，请重新上传");
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 检查网络类型及文件大小限制
     *
     * @param file
     */
    public void checkFileSizeAndUpload(File file) {
        if (FileTransmitUtils.checkLimit(file)) {
            DialogUtils.getInstance().sureOrCancel(mContext, "",
                    "当前为移动网络且文件大小超过10M,继续上传吗?",
                    viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
                        @Override
                        public void clickSure() {
                            uploadFile(file);
                        }
                    });
        } else {
            uploadFile(file);
        }
    }

    /**
     * 上传文件
     *
     * @param file
     */
    private void uploadFile(File file) {
        ToastUtils.showToast(mContext, "正在后台上传,请稍等");
        DownloadService.getInstance().uploadNetDiskFile2(file.getAbsolutePath(), folderUrl,
                folderId, folderStyle, new UploadCallback<BaseBean>() {
                    @Override
                    public BaseBean parseNetworkResponse(okhttp3.Response response, int id) throws Exception {
                        String string = response.body().string();
                        BaseBean user = new Gson().fromJson(string, BaseBean.class);
                        if (user.getResponse().getCode() == 1001) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshData();
                                }
                            });
                        } else {
                        }
                        return user;
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(BaseBean response, int id) {
                        refreshData();
                    }
                });
    }

    @Override
    public void onBackPressed() {

        if (fromWitch == FileConstants.FROM_SEARCH) {
            if (naviData.size() >= 2) {
                folderId = naviData.get(naviData.size() - 2).getFolderId();
                viewDelegate.setTitle(naviData.get(naviData.size() - 2).getFolderName());
                folderLevel = naviData.size() - 2;
                showNav();
                getNaviData();
                state = Constants.REFRESH_STATE;
                refreshData();
                setSearchBarVisibility();
            } else if (naviData.size() <= 1) {
                finish();
            }

        } else if (fromWitch == FileConstants.FROM_FOLDER) {
            finish();
        } else {
            finish();
        }
    }
}
