package com.hjhq.teamface.project.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hjhq.teamface.basis.adapter.FileNaviAdapter;
import com.hjhq.teamface.basis.bean.AttachmentBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.FolderNaviData;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.callback.UploadCallback;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.FileHelper;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
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
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.ProjectFileListAdapter;
import com.hjhq.teamface.project.bean.ProjectFileListBean;
import com.hjhq.teamface.project.bean.QueryManagerRoleResultBean;
import com.hjhq.teamface.project.model.ProjectModel2;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.presenter.add.ProjectAddFolderActivity;
import com.hjhq.teamface.project.ui.ProjectFileDelegate;
import com.hjhq.teamface.project.util.ProjectUtil;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

/**
 * 项目文件
 * Created by Administrator on 2018/4/10.
 */
@RouteNode(path = "/project_file", desc = "项目文件(文件列表)")
public class ProjectFileActivity extends ActivityPresenter<ProjectFileDelegate, ProjectModel2> {
    private String[] uploadMemu = {"发语音", "拍照上传", "手机相册", "文件夹选"};
    private String[] operationMemu;
    private int[] uploadMemuIcon = {R.drawable.icon_record, R.drawable.new_takephoto, R.drawable.new_photoalbum, R.drawable.new_lib};
    private File imageFromCamera;
    private String folderType;
    private String mFolderId;
    private String mDataId;
    private boolean mIsCreator;
    private String mFolderName;
    private String mProjectId;
    private String priviledgeIds;
    private ProjectFileListAdapter mFileListAdapter;
    ArrayList<FolderNaviData> naviDataList = new ArrayList<>();
    FileNaviAdapter naviAdapter;
    /**
     * 总页数
     */
    private int totalPages = 1;
    private int currentPageNo = 1;
    private int state = Constants.NORMAL_STATE;

    private List<ProjectFileListBean.DataBean.DataListBean> dataList = new ArrayList<>();

    @Override
    public void init() {
        mFolderId = getIntent().getStringExtra(Constants.DATA_TAG1);
        mIsCreator = getIntent().getBooleanExtra(Constants.DATA_TAG2, false);
        folderType = getIntent().getStringExtra(Constants.DATA_TAG3);
        mFolderName = getIntent().getStringExtra(Constants.DATA_TAG4);
        mProjectId = getIntent().getStringExtra(Constants.DATA_TAG5);
        mDataId = getIntent().getStringExtra(Constants.DATA_TAG6);
        naviDataList = (ArrayList<FolderNaviData>) getIntent().getSerializableExtra(Constants.DATA_TAG7);
        if (naviDataList != null && naviDataList.size() > 0) {
            initNaviData();
        } else {
            naviDataList = new ArrayList<>();
            initNaviData();
        }
        if (ProjectConstants.PROJECT_FOLDER.equals(folderType)) {
            viewDelegate.showMenu();
        } else if (ProjectConstants.USER_FOLDER.equals(folderType)) {
            viewDelegate.showMenu();
            getProjectRoleInfo();
        }

        mFileListAdapter = new ProjectFileListAdapter(mProjectId, folderType, dataList);
        viewDelegate.setAdapter(mFileListAdapter);
        viewDelegate.setTitle(mFolderName);

        getNetData();
    }

    private void getNetData() {
        int pageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        model.getProjectFolderTaskList(ProjectFileActivity.this, mFolderId, folderType, Constants.PAGESIZE, pageNo,
                new ProgressSubscriber<ProjectFileListBean>(mContext) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(ProjectFileListBean projectFolderListBean) {
                        super.onNext(projectFolderListBean);
                        showDataResult(projectFolderListBean);
                    }
                });

    }

    /**
     * 添加文件夹"22, "编辑文件夹", 23, "删除文件" :24
     */
    public void getProjectRoleInfo() {
        //查询管理员权限
        new TaskModel().queryManagementRoleInfo(mContext, TextUtil.parseLong(SPHelper.getEmployeeId()), TextUtil.parseLong(mProjectId), new ProgressSubscriber<QueryManagerRoleResultBean>(mContext) {
            @Override
            public void onNext(QueryManagerRoleResultBean queryManagerRoleResultBean) {
                super.onNext(queryManagerRoleResultBean);
                QueryManagerRoleResultBean.DataBean.PriviledgeBean priviledge = queryManagerRoleResultBean.getData().getPriviledge();
                priviledgeIds = priviledge.getPriviledge_ids();
                if (TextUtils.isEmpty(priviledgeIds)) {
                    viewDelegate.showMenu();
                } else {
                    if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 29) && ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 30)) {
                        viewDelegate.showMenu(0, 1);
                        operationMemu = new String[]{"编辑", "删除"};
                        return;
                    }
                    if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 29)) {
                        viewDelegate.showMenu(0, 1);
                        operationMemu = new String[]{"编辑"};
                        return;
                    }
                    if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 30)) {
                        viewDelegate.showMenu(0, 1);
                        operationMemu = new String[]{"删除"};
                        return;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissWindowView();
                e.printStackTrace();
                ToastUtils.showError(mContext, "获取项目角色权限失败");
            }
        });
    }

    /**
     * 初始导航数据
     */
    private void initNaviData() {
        if (naviDataList != null && naviDataList.size() > 0) {
            viewDelegate.get(R.id.rv_navi).setVisibility(View.VISIBLE);
            naviAdapter = new FileNaviAdapter(naviDataList);
            viewDelegate.setNaviAdapter(naviAdapter);
        } else {
            viewDelegate.get(R.id.rv_navi).setVisibility(View.GONE);
            naviDataList = new ArrayList<>();
            naviAdapter = new FileNaviAdapter(naviDataList);
            viewDelegate.setNaviAdapter(naviAdapter);
        }


    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        //导航栏的点击事件
        viewDelegate.setNaviClick(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                EventBusUtils.sendEvent(new MessageBean(position, Constants.JUMP_FOLDER, null));
            }
        });
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                Bundle bundle = new Bundle();
               /* SocketMessage socketMessage = new SocketMessage();
                ProjectFileListBean.ModuleInfoBean.DataListBean listBean = mFileListAdapter.getData().get(position);
                socketMessage.setMsgID(TextUtil.parseLong(listBean.getId()));
                socketMessage.setFileName(listBean.getFile_name());
                String fileUrl = String.format(ProjectConstants.PROJECT_FILE_DOWNLOAD_URL, listBean.getId(), mProjectId);
                socketMessage.setFileUrl(fileUrl);
                socketMessage.setFileSize(TextUtil.parseInt(listBean.getSize()));
                socketMessage.setFileType(listBean.getSuffix());
                socketMessage.setSenderName(listBean.getEmployee_name());
                socketMessage.setServerTimes(TextUtil.parseLong(listBean.getCreate_time()));
                bundle.putSerializable(MsgConstant.MSG_DATA, socketMessage);
                UIRouter.getInstance().openUri(mContext, "DDComp://app/file/detail", bundle);*/

                bundle.putString(Constants.DATA_TAG1, mFileListAdapter.getData().get(position).getId());
                bundle.putString(Constants.DATA_TAG2, mProjectId);
                bundle.putString(Constants.DATA_TAG3, mFileListAdapter.getData().get(position).getData_id());
                bundle.putSerializable(Constants.DATA_TAG4, mFileListAdapter.getData().get(position));
                bundle.putString(Constants.DATA_TAG5, folderType);
                bundle.putString(Constants.DATA_TAG6, priviledgeIds);
                CommonUtil.startActivtiyForResult(mContext, ProjectFileDetailActivity.class, Constants.REQUEST_CODE4, bundle);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                //下载
                if (view.getId() == R.id.rl_menu) {
                    checkFileSizeAndDownload(position, TextUtil.parseLong(mFileListAdapter.getData().get(position).getSize()));
                }

            }
        });
        viewDelegate.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            viewDelegate.mSwipeRefreshLayout.setRefreshing(false);
        });
        mFileListAdapter.setOnLoadMoreListener(() -> {
            if (currentPageNo >= totalPages) {
                mFileListAdapter.loadMoreEnd();
                return;
            }
            state = Constants.LOAD_STATE;
            getNetData();
        }, viewDelegate.mRecyclerView);
        viewDelegate.get(R.id.search_layout).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DATA_TAG1, ProjectConstants.SEARCH_PROJECT_FILE);
            bundle.putString(Constants.DATA_TAG2, mProjectId);
            bundle.putString(Constants.DATA_TAG3, folderType);
            bundle.putString(Constants.DATA_TAG4, mFolderId);
            CommonUtil.startActivtiy(mContext, ProjectSearchActivity.class, bundle);
        });

    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = Constants.REFRESH_STATE;
        getNetData();
    }

    private void showDataResult(ProjectFileListBean projectListBean) {
        List<ProjectFileListBean.DataBean.DataListBean> newDataList = projectListBean.getData().getDataList();

        switch (state) {
            case Constants.NORMAL_STATE:
            case Constants.REFRESH_STATE:
                CollectionUtils.notifyDataSetChanged(mFileListAdapter, mFileListAdapter.getData(), newDataList);
                break;
            case Constants.LOAD_STATE:
                mFileListAdapter.addData(newDataList);
                mFileListAdapter.loadMoreComplete();
                break;
            default:
                break;
        }
        PageInfo pageInfo = projectListBean.getData().getPageInfo();
        totalPages = pageInfo.getTotalPages();
        currentPageNo = pageInfo.getPageNum();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean event) {
        if (Constants.DATA_TAG1.equals(event.getTag())) {
            //录音文件
            viewDelegate.hideRecordBtn();
            File file = (File) event.getObject();
            checkFileSizeAndUpload(file);
            /*Bundle bundle = new Bundle();
            bundle.putString(Constants.DATA_TAG1, file.getAbsolutePath());
            bundle.putInt(Constants.DATA_TAG2, event.getCode());
            CommonUtil.startActivtiyForResult(mContext, VoicePlayActivity.class, Constants.REQUEST_CODE2, bundle);*/
        }
        if (event != null && Constants.JUMP_FOLDER.equals(event.getTag()) && event.getCode() < 3) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUploadMenu();
                break;
            case 1:
                showOperationMenu();
                break;
            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showOperationMenu() {
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), "", operationMemu, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                switch (operationMemu[p]) {
                    case "编辑":
                        Bundle bundle = new Bundle();
                        bundle.putInt(ProjectConstants.EDIT_FOLDER_TYPE, ProjectConstants.EDIT_FOLDER);
                        bundle.putLong(Constants.DATA_TAG1, TextUtil.parseLong(mProjectId));
                        bundle.putString(Constants.DATA_TAG2, mFolderId);
                        bundle.putString(Constants.DATA_TAG3, mFolderName);
                        bundle.putString(Constants.DATA_TAG4, mDataId);
                        CommonUtil.startActivtiyForResult(mContext, ProjectAddFolderActivity.class, Constants.REQUEST_CODE3, bundle);
                        break;
                    case "删除":
                        DialogUtils.getInstance().sureOrCancel(mContext, "", "确定要删除该文件夹吗？", viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                deleteProjectFolder();
                            }
                        });
                        break;
                    default:

                        break;
                }
                return false;
            }
        });
    }

    /**
     * 删除项目文件夹
     */
    private void deleteProjectFolder() {
        model.deleteProjectFolder(mContext, mDataId, mProjectId, mFolderName,
                new ProgressSubscriber<BaseBean>(mContext) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    private void showUploadMenu() {
        DialogUtils.getInstance().bottomDialog(mContext,
                viewDelegate.getRootView(), "上传附件",
                uploadMemu, uploadMemuIcon, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        chooseFile(position);
                    }
                });

    }

    private void chooseFile(int position) {
        switch (position) {
            case 0:
                viewDelegate.showRecordBtn();
                break;
            case 1:
                takePhoto();
                break;
            case 2:
                CommonUtil.getImageFromAlbumByMultiple(mContext, 1);
                break;
            case 3:
                //从本地文件夹选择文件
                FileHelper.showFileChooser(ProjectFileActivity.this);
                //UIRouter.getInstance().openUri(mContext, "DDComp://filelib/select_file", new Bundle(), Constants.REQUEST_CODE6);
                break;
            default:

                break;
        }

    }

    /**
     * 拍照
     */
    private void takePhoto() {
        SystemFuncUtils.requestPermissions(mContext, android.Manifest.permission.CAMERA, aBoolean -> {
            if (aBoolean) {
                imageFromCamera = CommonUtil.getImageFromCamera(mContext, Constants.TAKE_PHOTO_NEW_REQUEST_CODE);
            } else {
                ToastUtils.showError(mContext, "必须获得必要的权限才能拍照！");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE6:
                    //文件库选择的文件
                    if (data != null) {
                        AttachmentBean bean = (AttachmentBean) data.getSerializableExtra(Constants.DATA_TAG1);
                        bean.setFromWhere(EmailConstant.FROM_UPLOAD);

                    }
                    break;
                case Constants.CHOOSE_LOCAL_FILE:
                    //从本地文件夹选择的文件
                    Uri uri = data.getData();
                    LogUtil.e("audioUri==   " + uri);
                    String path = UriUtil.getPhotoPathFromContentUri(ProjectFileActivity.this, uri);
                    LogUtil.e("path==   " + path);
                    File localFile = new File(path);
                    if (localFile.exists()) {
                        checkFileSizeAndUpload(localFile);
                    } else {
                        ToastUtils.showError(mContext, "数据错误，请重新上传");
                    }
                    break;
                case PhotoPicker.REQUEST_CODE:
                    //相册
                    ArrayList<String> photos =
                            data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    if (photos != null) {
                        for (int i = 0; i < photos.size(); i++) {
                            File file = new File(photos.get(i));
                            checkFileSizeAndUpload(file);
                        }
                    }
                    break;
                case Constants.TAKE_PHOTO_NEW_REQUEST_CODE:
                    //拍照
                    if (imageFromCamera != null && imageFromCamera.exists()) {
                        checkFileSizeAndUpload(imageFromCamera);
                    }
                    break;
                case Constants.REQUEST_CODE3:
                    //文件夹重命名
                    if (data != null) {
                        String newName = data.getStringExtra(Constants.DATA_TAG1);
                        viewDelegate.setTitle(newName);
                        if (naviDataList != null && naviDataList.size() > 0) {
                            naviDataList.get(naviDataList.size() - 1).setFolderName(newName);
                            naviAdapter.notifyDataSetChanged();
                        }
                    }

                    break;
                case Constants.REQUEST_CODE4:
                    //从详情返回
                    refreshData();

                    break;
                default:

                    break;
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
     * 检查文件大小并下载
     *
     * @param position
     * @param size
     */
    public void checkFileSizeAndDownload(int position, long size) {
        if (FileTransmitUtils.checkLimit(size)) {
            DialogUtils.getInstance().sureOrCancel(mContext, "",
                    "当前为移动网络且文件大小超过10M,继续下载吗?",
                    viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
                        @Override
                        public void clickSure() {
                            downloadFile(position);
                        }
                    });
        } else {
            downloadFile(position);
        }
    }

    private void downloadFile(int position) {
        ProjectFileListBean.DataBean.DataListBean bean = mFileListAdapter.getData().get(position);
        String url = String.format(ProjectConstants.PROJECT_FILE_DOWNLOAD_URL, bean.getId(), mProjectId);
        DownloadService.getInstance().downloadFileFromUrl(bean.getId(), url, bean.getId() + bean.getFile_name());
    }

    /**
     * 上传文件
     *
     * @param file
     */
    private void uploadFile(File file) {
        ToastUtils.showToast(mContext, "正在后台上传,请稍等");
        DownloadService.getInstance().uploadProjectPersonalFile(file.getAbsolutePath(), mFolderId,
                mProjectId, new UploadCallback<BaseBean>() {
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
                        //refreshData();
                    }
                });
    }

}
