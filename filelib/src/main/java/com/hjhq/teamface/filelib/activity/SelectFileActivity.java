package com.hjhq.teamface.filelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.adapter.FileNaviAdapter;
import com.hjhq.teamface.basis.bean.AttachmentBean;
import com.hjhq.teamface.basis.bean.FolderNaviData;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.UriUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.SelectFileAdapter;
import com.hjhq.teamface.filelib.bean.FileListResBean;
import com.hjhq.teamface.filelib.bean.FolderParentResBean;
import com.hjhq.teamface.filelib.bean.RootFolderResBean;
import com.hjhq.teamface.filelib.view.SelectFileDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RouteNode(path = "/select_filelibrary_file", desc = "从文件库选择文件")
public class SelectFileActivity extends ActivityPresenter<SelectFileDelegate, FilelibModel> {


    private int folderLevel = 0;
    private int tempFolderLevel = 0;
    private RecyclerView mRvNavi;
    private RecyclerView mRvFileList;
    private SelectFileAdapter mFileAdapter;
    private FileNaviAdapter mNaviAdapter;
    private EmptyView mEmptyView;
    private String folderName = "";
    private String folderId;
    private int folderStyle;
    private String tableId;
    private ArrayList<FolderNaviData> naviData = new ArrayList<>();
    private ArrayList<RootFolderResBean.DataBean> dataList = new ArrayList<>();
    private ArrayList<FileListResBean.DataBean.DataListBean> fileListData = new ArrayList<>();

    //分页
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    //总数
    private int totalNum = 0;
    private int state = Constants.NORMAL_STATE;
    private int pageSize = 20;

    @Override
    public void init() {
        initView();
    }


    protected void initView() {
        viewDelegate.setTitle("选择文件");
        viewDelegate.setRightMenuTexts("确定");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            folderLevel = bundle.getInt(FileConstants.FOLDER_LEVEL);
            folderStyle = bundle.getInt(FileConstants.FOLDER_STYLE);
            folderName = bundle.getString(FileConstants.FOLDER_NAME);
            tableId = bundle.getString(FileConstants.TABLE_ID);
        }


        mRvNavi = (RecyclerView) findViewById(R.id.rv_navi);
        mRvFileList = (RecyclerView) findViewById(R.id.rv_department);
        mFileAdapter = new SelectFileAdapter(folderLevel, fileListData);
        mRvFileList.setLayoutManager(new LinearLayoutManager(SelectFileActivity.this));
        mRvFileList.setAdapter(mFileAdapter);
        LinearLayoutManager naviLm = new LinearLayoutManager(SelectFileActivity.this);
        naviLm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvNavi.setLayoutManager(naviLm);
        mNaviAdapter = new FileNaviAdapter(naviData);
        mRvNavi.setAdapter(mNaviAdapter);
        mEmptyView = new EmptyView(this);
        mEmptyView.setEmptyImage(R.drawable.empty_view_img);
        mFileAdapter.setEmptyView(mEmptyView);
        getNaviData();
        getNetData();

    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.getToolbar().setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        mFileAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (currentPageNo >= totalPages) {
                    mFileAdapter.loadMoreComplete();
                    //mAdapter.setEnableLoadMore(false);
                    mFileAdapter.loadMoreEnd(false);
                    return;
                }
                state = Constants.LOAD_STATE;
                getNetData();
            }
        }, mRvFileList);
        mRvFileList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ("0".equals(fileListData.get(position).getSign())) {
                    folderLevel = folderLevel + 1;
                    folderId = fileListData.get(position).getId();
                    folderName = fileListData.get(position).getName();
                    tableId = fileListData.get(position).getModel_id();
                    FolderNaviData data = new FolderNaviData();
                    data.setFolderId(tableId);
                    data.setFloderType(folderStyle);
                    data.setFolderName(folderName);
                    data.setFolderLevel(folderLevel + 1);
                    naviData.add(data);
                    mNaviAdapter.notifyDataSetChanged();
                    getNetData();
                    mFileAdapter.getData().clear();
                    mFileAdapter.notifyDataSetChanged();

                    getNetData();

                } else {
                    AttachmentBean bean = new AttachmentBean();
                    bean.setFromWhere(EmailConstant.FROM_UPLOAD);
                    bean.setFileName(fileListData.get(position).getName());
                    bean.setFileSize(fileListData.get(position).getSize());
                    bean.setFileType(fileListData.get(position).getSiffix());
                    bean.setFileUrl(FileConstants.FILE_BASE_URL + fileListData.get(position).getId());
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DATA_TAG1, bean);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        mRvNavi.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                System.gc();
                if (mNaviAdapter.getData().size() > 1 && position == mNaviAdapter.getData().size() - 1) {
                    return;
                }
                if (position == 0) {
                    folderLevel = 0;
                    folderId = "";
                    tableId = "";
                    FolderNaviData folderNaviData = mNaviAdapter.getData().get(0);
                    mNaviAdapter.getData().clear();
                    mNaviAdapter.getData().add(folderNaviData);
                    mNaviAdapter.notifyDataSetChanged();
                    getNetData();
                } else {
                    folderId = mNaviAdapter.getData().get(position).getFolderId();
                    tableId = mNaviAdapter.getData().get(position).getFolderId();
                    folderLevel = position;
                    List<FolderNaviData> naviDatas = mNaviAdapter.getData();
                    List<FolderNaviData> newNaviDatas = new ArrayList<FolderNaviData>();
                    for (int i = 0; i < position + 1; i++) {
                        newNaviDatas.add(naviDatas.get(i));
                    }
                    mNaviAdapter.getData().clear();
                    mNaviAdapter.getData().addAll(newNaviDatas);
                    mNaviAdapter.notifyDataSetChanged();

                    getNetData();
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setResult(Activity.RESULT_OK);
        finish();
        return super.onOptionsItemSelected(item);

    }


    private void getNetData() {
        if (folderStyle != 2) {
            getNaviData();
        }
        currentPageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        pageSize = Constants.PAGESIZE;
        if (folderStyle == 2) {
            getAppFileList();
        } else {
            if (folderLevel >= 1) {
                model.queryFilePartList(SelectFileActivity.this, folderStyle + "",
                        folderId, currentPageNo, pageSize, FileConstants.ALL,
                        new ProgressSubscriber<FileListResBean>(this, true) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);

                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(FileListResBean fileListResBean) {
                                super.onNext(fileListResBean);
                                showData(fileListResBean);
                                folderLevel++;
                            }
                        });
            } else {
                model.queryFileList(SelectFileActivity.this, folderStyle + "", 1,
                        200, FileConstants.ALL, new ProgressSubscriber<FileListResBean>(this, true) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);

                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(FileListResBean fileListResBean) {
                                super.onNext(fileListResBean);
                                showData(fileListResBean);
                                folderLevel++;
                            }
                        });
            }
        }


    }

    private void getAppFileList() {
        if (folderLevel == 0) {
            model.queryAppFileList(SelectFileActivity.this, folderStyle + "",
                    currentPageNo, pageSize,
                    new ProgressSubscriber<FileListResBean>(this, false) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            e.printStackTrace();
                            if (state == Constants.LOAD_STATE) {
                                mFileAdapter.loadMoreFail();
                            }
                        }

                        @Override
                        public void onNext(FileListResBean fileListResBean) {
                            super.onNext(fileListResBean);
                            showData(fileListResBean);

                        }
                    });
        }
        if (folderLevel == 1) {
            model.queryModuleFileList(SelectFileActivity.this, tableId, folderStyle + "",
                    currentPageNo, pageSize,
                    new ProgressSubscriber<FileListResBean>(this, false) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            e.printStackTrace();
                            if (state == Constants.LOAD_STATE) {
                                mFileAdapter.loadMoreFail();
                            }
                        }

                        @Override
                        public void onNext(FileListResBean fileListResBean) {
                            super.onNext(fileListResBean);
                            showData(fileListResBean);

                        }
                    });
        }
        if (folderLevel == 2) {
            model.queryModulePartFileList(SelectFileActivity.this, tableId, folderStyle + "",
                    currentPageNo, pageSize,
                    new ProgressSubscriber<FileListResBean>(this, false) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            e.printStackTrace();
                            if (state == Constants.LOAD_STATE) {
                                mFileAdapter.loadMoreFail();
                            }
                        }

                        @Override
                        public void onNext(FileListResBean fileListResBean) {
                            super.onNext(fileListResBean);
                            showData(fileListResBean);

                        }
                    });
        }

    }

    private void showData(FileListResBean fileListResBean) {
        List<FileListResBean.DataBean.DataListBean> newDataList = new ArrayList<>();
        if (fileListResBean.getData() != null && fileListResBean.getData().getDataList() != null) {
            newDataList.addAll(fileListResBean.getData().getDataList());
        }

        List<FileListResBean.DataBean.DataListBean> oldDataList = mFileAdapter.getData();
        if (oldDataList == null) {
            oldDataList = new ArrayList<>();
        }
        switch (state) {
            case Constants.REFRESH_STATE:
            case Constants.NORMAL_STATE:
                oldDataList.clear();
                mFileAdapter.setEnableLoadMore(true);
                break;
            case Constants.LOAD_STATE:
                mFileAdapter.loadMoreEnd();
                break;
            default:
                break;
        }
        if (newDataList.size() > 0) {
            oldDataList.addAll(newDataList);
            mFileAdapter.notifyDataSetChanged();
        }
        PageInfo pageInfo = fileListResBean.getData().getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
        totalNum = pageInfo.getTotalRows();
        if (mFileAdapter.getData().size() == totalNum) {
            mFileAdapter.loadMoreComplete();
        }
    }


    private void getNaviData() {
        String id = "";
        if (folderStyle == 2) {
            id = tableId;
        } else {
            id = folderId;
        }
        if (TextUtils.isEmpty(id)) {
            naviData.clear();
            FolderNaviData data = new FolderNaviData();
            data.setFolderId("");
            data.setFloderType(folderStyle);
            data.setFolderName(getRootFolderName(folderStyle));
            data.setFolderLevel(0);
            naviData.add(data);
            mNaviAdapter.notifyDataSetChanged();
        } else {

            model.getBlurResultParentInfo(SelectFileActivity.this, id,
                    new ProgressSubscriber<FolderParentResBean>(SelectFileActivity.this, false) {
                        @Override
                        public void onError(Throwable e) {


                        }

                        @Override
                        public void onNext(FolderParentResBean baseBean) {
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


    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe
    public void jumpFolder(MessageBean bean) {
        if (Constants.MOVE_FILE_JUMP_FOLDER.equals(bean.getTag()) && bean.getCode() < folderLevel) {
            finish();
        }
        if (FileConstants.MOVE_OR_COPY_OK.equals(bean.getTag())) {
            setResult(Activity.RESULT_OK);
            finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        //发送文件
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.CHOOSE_LOCAL_FILE) {
            Uri uri = data.getData();
            String path = UriUtil.getPhotoPathFromContentUri(SelectFileActivity.this, uri);
            File file = new File(path);

            if (file.exists()) {
                //  CommonUtil.showToast(file.getAbsolutePath());
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        if (naviData.size() >= 2) {
            folderId = naviData.get(naviData.size() - 2).getFolderId();
            tableId = naviData.get(naviData.size() - 2).getFolderId();
            viewDelegate.setTitle(naviData.get(naviData.size() - 2).getFolderName());
            folderLevel = naviData.size() - 2;
            if (folderStyle == 2) {
                mNaviAdapter.getData().remove(mNaviAdapter.getData().size() - 1);
                mNaviAdapter.notifyDataSetChanged();
            } else {
                getNaviData();
            }
            mFileAdapter.getData().clear();
            mFileAdapter.notifyDataSetChanged();
            getNetData();
        } else if (naviData.size() <= 1) {
            finish();
        }
    }


}
