package com.hjhq.teamface.filelib.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.FolderNaviData;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.Photo;
import com.hjhq.teamface.basis.bean.ProgressBean;
import com.hjhq.teamface.basis.bean.QxMessage;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.callback.DownloadCallback;
import com.hjhq.teamface.basis.network.callback.UploadCallback;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.FileHelper;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.UriUtil;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.image.ScaleImageView;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.activity.FullscreenViewImageActivity;
import com.hjhq.teamface.common.ui.ImagePagerActivity;
import com.hjhq.teamface.common.ui.comment.CommentActivity;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.player.AudioPlayerView;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.bean.FileDetailBean;
import com.hjhq.teamface.filelib.view.FileDetailDelegate;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.activity.ChooseSendMemberActivity;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 文件预览界面
 *
 * @author Administrator
 */

@RouteNode(path = "/file_detail", desc = "文件详情界面")
public class FileDetailActivity extends ActivityPresenter<FileDetailDelegate, FilelibModel> implements View.OnClickListener {
    private RelativeLayout rlMore;
    private RelativeLayout rlDownload;
    private RelativeLayout rlDownloadAndProgress;
    private RelativeLayout rlLike;
    private RelativeLayout rlComment;
    private RelativeLayout rlProgress;
    private RelativeLayout rlCancel;
    private RelativeLayout rlOther;
    private RelativeLayout rlVideo;
    private RelativeLayout rlImage;
    private RelativeLayout rlAudio;
    private LinearLayout llRoot;
    private ScaleImageView imageView;
    private TextView tvComment;
    private TextView tvProgressNum;
    private TextView tvLike;
    private TextView notice;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private SeekBar sbSeekbar;
    private TextView tvProcess;
    private TextView fileName;
    private ImageView fileIcon;
    private ImageView ivImage;
    private ImageView ivLike;
    private ImageView playOrPause;
    private ProgressBar progressbar;
    private Button downloadOrOpen;
    private String fileId;
    private String mFileName;
    private String folderUrl;
    private String fileType;
    private String likeStatus;
    private String likeNum;
    private String commentNum;
    private Bundle mBundle;
    private boolean isFileExist = false;
    private boolean clickable = false;
    private boolean hasInitReceiver = false;
    private long downloadId;
    private long fileSize;
    private File mFile;
    private int folderStyle;
    private int folderLevel;
    private String folderName;
    private String folderId;
    private String isManager;
    private String downloadAuth = "0";
    private String uploadAuth = "0";
    private boolean isCreator = false;
    private String parentFolderId;
    private ArrayList<FolderNaviData> mNaviDataList = new ArrayList<>();
    protected String moduleBean;
    private FileDetailBean mBean;
    private String fileUrl;
    private String sendFileUrl;
    //是否是栈顶
    private boolean isTopActivity = false;

    private SocketMessage mSocketMessage;
    private AudioPlayerView mPlayer;

    private String[] chooseFileMenu = {"文件夹中上传", "相册中上传"};

    @Override
    public void init() {
        initView();
    }


    protected void initView() {
        llRoot = (LinearLayout) findViewById(R.id.ll_action_root);
        rlMore = (RelativeLayout) findViewById(R.id.rl_more);
        rlDownload = (RelativeLayout) findViewById(R.id.rl_download);
        rlDownloadAndProgress = (RelativeLayout) findViewById(R.id.rl_download_and_progress);
        rlLike = (RelativeLayout) findViewById(R.id.rl_like);
        rlComment = (RelativeLayout) findViewById(R.id.rl_comment);
        rlProgress = (RelativeLayout) findViewById(R.id.rl_progress);
        rlCancel = (RelativeLayout) findViewById(R.id.cancel_download);
        rlOther = (RelativeLayout) findViewById(R.id.rl_other_type);
        rlImage = (RelativeLayout) findViewById(R.id.rl_image);
        rlAudio = (RelativeLayout) findViewById(R.id.rl_audio);
        rlVideo = (RelativeLayout) findViewById(R.id.rl_video);
        fileIcon = (ImageView) findViewById(R.id.file_icon);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        downloadOrOpen = (Button) findViewById(R.id.download_or_open);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        imageView = (ScaleImageView) findViewById(R.id.siv);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        tvProgressNum = (TextView) findViewById(R.id.tv_progress_num);
        tvLike = (TextView) findViewById(R.id.tv_like);
        ivLike = (ImageView) findViewById(R.id.icon_like);
        playOrPause = (ImageView) findViewById(R.id.play_pause);
        tvProcess = (TextView) findViewById(R.id.tv_progress);
        fileName = (TextView) findViewById(R.id.file_name);
        notice = (TextView) findViewById(R.id.file_info);
        tvCurrentTime = (TextView) findViewById(R.id.current);
        tvTotalTime = (TextView) findViewById(R.id.total);
        sbSeekbar = (SeekBar) findViewById(R.id.seekbar);
        initData();
        llRoot.setVisibility(View.GONE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
        llRoot.setVisibility(View.GONE);

    }

    private void initData() {
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            fileId = mBundle.getString(FileConstants.FILE_ID);
            folderStyle = mBundle.getInt(FileConstants.FOLDER_STYLE);
            fileType = mBundle.getString(FileConstants.FILE_TYPE);
            isManager = mBundle.getString(FileConstants.IS_MANAGER);
            mSocketMessage = (SocketMessage) mBundle.getSerializable(MsgConstant.MSG_DATA);
            LogUtil.e("fileId == " + fileId);
            LogUtil.e("folderStyle == " + folderStyle);
            LogUtil.e("fileType == " + fileType);
            LogUtil.e("isManager == " + isManager);
            //判断文件来自文件库还是聊天
            if (mSocketMessage == null) {
                fileUrl = FileConstants.FILE_BASE_URL + fileId;
                sendFileUrl = FileConstants.FILE_BASE_URL_NO_RECORD + fileId;
                getNetData(true);
                if (folderStyle == 2 || folderStyle == 4 || folderStyle == 5) {
                    rlMore.setVisibility(View.GONE);
                    if (folderStyle == 2) {
                        viewDelegate.setRightMenuIcons(R.drawable.icon_view_download);
                        rlComment.setVisibility(View.GONE);
                        rlLike.setVisibility(View.VISIBLE);
                        rlMore.setVisibility(View.VISIBLE);
                        rlDownload.setVisibility(View.VISIBLE);
                    } else {
                        viewDelegate.setRightMenuIcons(R.drawable.icon_view_download, R.drawable.icon_view_version);
                    }
                } else {
                    viewDelegate.setRightMenuIcons(R.drawable.icon_view_download, R.drawable.icon_view_version);
                }
                if (folderStyle == 3) {
                    rlDownload.setVisibility(View.VISIBLE);
                }
                viewDelegate.setTitle(getString(R.string.filelib_detail));
            } else {
                if (mSocketMessage.getMsgType() == MsgConstant.VIDEO) {
                    fileUrl = mSocketMessage.getVideoUrl();
                    sendFileUrl = mSocketMessage.getVideoUrl();
                } else {
                    fileUrl = mSocketMessage.getFileUrl();
                    sendFileUrl = mSocketMessage.getFileUrl();
                }
                clickable = true;
                llRoot.setVisibility(View.GONE);
                viewDelegate.setTitle(mSocketMessage.getFileName());
                if (!TextUtils.isEmpty(mSocketMessage.getFileLocalPath())) {
                    mFile = new File(mSocketMessage.getFileLocalPath());
                }
                fileId = mSocketMessage.getMsgID() + "";
                mFileName = mSocketMessage.getFileName();
                fileType = mSocketMessage.getFileType();
                initFileData();
                initReceiver();
            }
        }
    }

    private void initFileData() {
        if (mFile != null) {
            isFileExist = mFile.exists();
            if (!isFileExist) {
                mFile = new File(JYFileHelper.getFileDir(this, Constants.PATH_DOWNLOAD), mSocketMessage.getMsgID() + mSocketMessage.getFileName());
                if (mFile != null) {
                    isFileExist = mFile.exists();
                }

            }
        } else {
            mFile = new File(JYFileHelper.getFileDir(this, Constants.PATH_DOWNLOAD), mSocketMessage.getMsgID() + mSocketMessage.getFileName());
            if (mFile != null) {
                isFileExist = mFile.exists();
            }
        }
        fileUrl = mSocketMessage.getFileUrl();
        sendFileUrl = mSocketMessage.getFileUrl();
        LogUtil.e("聊天文件详情   " + new Gson().toJson(mSocketMessage));

        //文件名
        mFileName = mSocketMessage.getFileName();
        //文件类型
        fileType = mSocketMessage.getFileType();
        fileSize = mSocketMessage.getFileSize();
        fileId = mSocketMessage.getMsgID() + "";


        if (FileTypeUtils.isImage(fileType)) {
            if (isFileExist) {
                loadImage();
            } else {
                rlImage.setVisibility(View.VISIBLE);
                ivImage.setVisibility(View.VISIBLE);
                ImageLoader.loadImage(FileDetailActivity.this, fileUrl, ivImage);
            }
        } else if (FileTypeUtils.isAudio(fileType)) {
            rlImage.setVisibility(View.GONE);
            rlOther.setVisibility(View.GONE);
            rlVideo.setVisibility(View.GONE);
            rlAudio.setVisibility(View.VISIBLE);
            try {
                initAudioPlayer();
            } catch (IOException e) {

            }
        } else {
            rlImage.setVisibility(View.GONE);
            rlVideo.setVisibility(View.GONE);
            rlAudio.setVisibility(View.GONE);
            rlOther.setVisibility(View.VISIBLE);
            fileIcon.setImageResource(FileTypeUtils.getFileTypeIcon(fileType));
            fileName.setText(String.format(getString(R.string.filelib_send_info),
                    mSocketMessage.getSenderName(),
                    DateTimeUtil.fromTime(mSocketMessage.getServerTimes())));

            if (mFile.exists()) {
                isFileExist = true;
                notice.setVisibility(View.VISIBLE);
                rlProgress.setVisibility(View.GONE);
                downloadOrOpen.setText(getString(R.string.filelib_open_with_other_app));
            } else {
                isFileExist = false;
                notice.setVisibility(View.VISIBLE);
                rlProgress.setVisibility(View.GONE);
                downloadOrOpen.setText(String.format(getString(R.string.filelib_download_file_btn), FormatFileSizeUtil.formatFileSize(fileSize)));
            }
        }


    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

        rlMore.setOnClickListener(this);
        rlDownload.setOnClickListener(this);
        rlLike.setOnClickListener(this);
        rlComment.setOnClickListener(this);
        rlCancel.setOnClickListener(this);
        fileIcon.setOnClickListener(this);
        downloadOrOpen.setOnClickListener(this);
        ivImage.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }


    private void getNetData(boolean flag) {
        if (TextUtils.isEmpty(fileId)) {
            ToastUtils.showToast(mContext, getString(R.string.filelib_get_file_id_failed));
            return;
        }
        model.queryFileLibarayDetail(FileDetailActivity.this, fileId,
                new ProgressSubscriber<FileDetailBean>(this, flag) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(FileDetailBean baseBean) {
                        super.onNext(baseBean);
                        llRoot.setVisibility(View.VISIBLE);
                        if (!hasInitReceiver) {
                            initReceiver();
                        }
                        clickable = true;
                        FileDetailBean.DataBean.BasicsBean basics = baseBean.getData().getBasics();
                        if (!TextUtils.isEmpty(basics.getCreate_by()) && SPHelper.getEmployeeId().equals(basics.getCreate_by())) {
                            isCreator = true;
                        }
                        mBean = baseBean;
                        isManager = baseBean.getData().getIs_manage();
                        downloadAuth = baseBean.getData().getDownload();
                        if (folderStyle == 3 || folderStyle == 4 || folderStyle == 5) {
                            downloadAuth = "1";
                        }
                        uploadAuth = baseBean.getData().getUpload();
                        if ("0".equals(downloadAuth)) {
                            rlDownload.setVisibility(View.GONE);
                            rlDownloadAndProgress.setVisibility(View.GONE);

                        } else if ("1".equals(downloadAuth)) {
                            rlDownload.setVisibility(View.VISIBLE);
                            rlDownloadAndProgress.setVisibility(View.VISIBLE);
                        }


                        //文件名
                        mFileName = baseBean.getData().getBasics().getName();
                        //文件类型
                        fileType = baseBean.getData().getBasics().getSiffix();
                        if (FileTypeUtils.isAudio(fileType)) {
                            fileUrl = FileConstants.FILE_BASE_URL_NO_RECORD + fileId;
                            sendFileUrl = FileConstants.FILE_BASE_URL_NO_RECORD + fileId;
                        } else {
                            fileUrl = FileConstants.FILE_BASE_URL + fileId;
                            sendFileUrl = FileConstants.FILE_BASE_URL_NO_RECORD + fileId;
                        }
                        //判断后缀是否符合返回类型
                        if (!TextUtils.isEmpty(mFileName) && !TextUtils.isEmpty(fileType)) {
                            if (!mFileName.toLowerCase().endsWith(fileType.toLowerCase())) {
                                mFileName = mFileName + fileType;
                            }
                        }
                        mFile = new File(JYFileHelper.getFileDir(mContext, Constants.PATH_DOWNLOAD).getAbsolutePath(), fileId + mFileName);
                        fileSize = baseBean.getData().getBasics().getSize();
                        folderUrl = baseBean.getData().getBasics().getUrl();

                        //标题/文件名
                        viewDelegate.setTitle(mFileName + "");
                        //文件类型
                        if (FileTypeUtils.isImage(baseBean.getData().getBasics().getSiffix())) {

                            if (mFile.exists()) {
                                loadImage();
                            } else {
                                if (fileSize < FileConstants.MAX_IMAGE_FILE_SIZE) {
                                    String url = FileConstants.FILE_THUMB_BASE_URL + fileId + "&width=480&url=" + mBean.getData().getBasics().getUrl();
                                    ImageLoader.loadImage(FileDetailActivity.this, url, ivImage);
                                    imageView.setVisibility(View.GONE);
                                    rlImage.setVisibility(View.VISIBLE);
                                    ivImage.setVisibility(View.VISIBLE);
                                    rlOther.setVisibility(View.GONE);
                                } else {
                                    isFileExist = false;
                                    ImageLoader.loadImage(FileDetailActivity.this, R.drawable.ic_image, fileIcon);
                                    notice.setVisibility(View.VISIBLE);
                                    rlProgress.setVisibility(View.GONE);
                                    rlOther.setVisibility(View.VISIBLE);
                                    rlImage.setVisibility(View.GONE);

                                    downloadOrOpen.setText(String.format(getString(R.string.filelib_download_file_btn), FormatFileSizeUtil.formatFileSize(fileSize)));
                                }
                            }
                        } else if (FileTypeUtils.isAudio(baseBean.getData().getBasics().getSiffix())) {
                            rlImage.setVisibility(View.GONE);
                            rlOther.setVisibility(View.GONE);
                            rlVideo.setVisibility(View.GONE);
                            rlAudio.setVisibility(View.VISIBLE);
                            try {
                                if (mPlayer == null) {
                                    initAudioPlayer();
                                }

                            } catch (IOException e) {

                            }


                        } else {
                            rlImage.setVisibility(View.GONE);
                            rlVideo.setVisibility(View.GONE);
                            rlAudio.setVisibility(View.GONE);
                            rlOther.setVisibility(View.VISIBLE);
                            fileIcon.setImageResource(FileTypeUtils.getFileTypeIcon(fileType));
                            fileName.setText(String.format(getString(R.string.filelib_update_info),
                                    baseBean.getData().getBasics().getEmployee_name(),
                                    DateTimeUtil.fromTime(baseBean.getData().getBasics().getCreate_time())));

                            if (mFile.exists()) {
                                isFileExist = true;
                                notice.setVisibility(View.VISIBLE);
                                rlProgress.setVisibility(View.GONE);
                                downloadOrOpen.setText(getString(R.string.filelib_open_with_other_app));
                            } else {
                                isFileExist = false;
                                notice.setVisibility(View.VISIBLE);
                                rlProgress.setVisibility(View.GONE);
                                downloadOrOpen.setText(String.format(getString(R.string.filelib_download_file_btn), FormatFileSizeUtil.formatFileSize(fileSize)));
                            }
                        }

                        //点赞状态
                        likeStatus = baseBean.getData().getFabulous_status();
                        //点赞数量
                        likeNum = baseBean.getData().getFabulous_count();
                        tvLike.setText(likeNum + "");
                        ivLike.setImageResource("1".equals(likeStatus) ? R.drawable.heart_red : R.drawable.heart_gray);
                        //评论数量
                        commentNum = baseBean.getData().getComment_count();
                        tvComment.setText(commentNum + "");


                    }
                });

    }

    /**
     * 初始化音频播放
     *
     * @throws IOException
     */
    private void initAudioPlayer() throws IOException {
        if (mPlayer == null) {
            mPlayer = new AudioPlayerView(this);
        }
        rlAudio.removeAllViews();
        rlAudio.addView(mPlayer);
        if (mFile.exists()) {
            mPlayer.setResource(mFile);
        } else {
            mPlayer.setUrlResource(fileUrl);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (TextUtils.isEmpty(fileId)) {
            ToastUtils.showToast(mContext, "文件参数错误!");
            return true;
        }
        if (!clickable) {
            ToastUtils.showToast(mContext, "文件信息初始化未完成!");
            return true;
        }
        Bundle bundle = new Bundle();
        bundle.putString(FileConstants.FILE_ID, fileId);
        bundle.putBoolean(Constants.DATA_TAG1, isCreator || FileConstants.IM_MANAGER.equals(isManager) || "1".equals(downloadAuth));
        if (item.getItemId() == 0) {
            CommonUtil.startActivtiyForResult(FileDetailActivity.this, FileDownloadHistoryActivity.class, Constants.REQUEST_CODE1, bundle);
        } else if (item.getItemId() == 1) {
            CommonUtil.startActivtiyForResult(FileDetailActivity.this, FileHistoryVersionActivity.class, Constants.REQUEST_CODE2, bundle);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        if (!clickable) {
            ToastUtils.showToast(mContext, "请等待数据初始化完成!");
            return;
        }
        if (view.getId() == R.id.rl_comment) {
            bundle.putString(Constants.MODULE_BEAN, FileConstants.FILE_LIBRARY_BEAN_NAME);
            bundle.putString(Constants.DATA_ID, fileId);
            bundle.putString(FileConstants.FOLDER_STYLE, folderStyle + "");
            CommonUtil.startActivtiyForResult(this, CommentActivity.class, Constants.REQUEST_CODE6, bundle);
        } else if (view.getId() == R.id.rl_like) {
            //点赞
            rlLike.setClickable(false);
            model.whetherFabulous(FileDetailActivity.this, fileId,
                    "1".equals(likeStatus) ? "0" : "1", new ProgressSubscriber<BaseBean>(FileDetailActivity.this, false) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);

                            rlLike.setClickable(true);
                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);

                            getNetData(false);
                            rlLike.setClickable(true);
                        }
                    });
        } else if (view.getId() == R.id.rl_download) {

            if (mFile.exists()) {
                DialogUtils.getInstance().sureOrCancel(FileDetailActivity.this,
                        "提示", "文件已存在,需要重新下载吗?", rlDownload.getRootView(), new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                mFile.delete();
                                checkLimitAndDownload();
                            }
                        });
            } else {
                checkLimitAndDownload();
            }
        } else if (view.getId() == R.id.download_or_open) {
            if (mFile.exists()) {
                openFile();
            } else {
                downloadFile();
            }
        } else if (view.getId() == R.id.rl_more) {
            if (folderStyle == 1) {
                companyFileMenu();
            } else if (folderStyle == 2) {
                appFileMenu();
            } else if (folderStyle == 3) {
                personalFileMenu();
            }
        } else if (view.getId() == R.id.cancel_download) {
            calcelDownload();
        } else if (view.getId() == R.id.iv_image) {
            fullscreenViewImage();
        } else if (view.getId() == R.id.siv) {
            fullscreenViewImage();
        }

    }


    private void checkLimitAndDownload() {
        if (FileTransmitUtils.checkLimit(fileSize)) {
            DialogUtils.getInstance().sureOrCancel(mContext, "",
                    "当前为移动网络且文件大小超过10M,继续下载吗?", llRoot,
                    new DialogUtils.OnClickSureListener() {
                        @Override
                        public void clickSure() {
                            showProgressBar();
                            DownloadService.getInstance().downloadFileFromUrl(fileId, fileUrl, mFileName);
                            ToastUtils.showToast(mContext, "正在后台下载,请稍等!");
                        }
                    });
        } else {
            showProgressBar();
            DownloadService.getInstance().downloadFileFromUrl(fileId, fileUrl, mFileName);
            ToastUtils.showToast(mContext, "正在后台下载,请稍等!");
        }
    }

    private void showProgressBar() {

        rlProgress.setVisibility(View.VISIBLE);
        notice.setVisibility(View.GONE);
        tvProcess.setVisibility(View.VISIBLE);
        progressbar.setProgress(0);
        downloadOrOpen.setVisibility(View.GONE);
        tvProcess.setText(String.format(getString(R.string.filelib_download_progress),
                FormatFileSizeUtil.formatFileSize(0) + "", FormatFileSizeUtil.formatFileSize(fileSize)));

    }

    private void fullscreenViewImage() {
        ArrayList<Photo> photoList = new ArrayList<Photo>();
        if (mFile.exists()) {
            Photo photo = new Photo(mFile.getAbsolutePath());
            photoList.add(photo);
        } else {
            Photo photo = new Photo(fileUrl);
            photoList.add(photo);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePagerActivity.PICTURE_LIST, photoList);
        CommonUtil.startActivtiy(FileDetailActivity.this, FullscreenViewImageActivity.class, bundle);


    }


    /**
     * 取消共享
     */
    private void cancelShare() {
        String[] menu = {"取消共享"};
        PopUtils.showBottomMenu(FileDetailActivity.this, rlMore.getRootView(), "操作", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        model.cancelShare(FileDetailActivity.this, fileId,
                                new ProgressSubscriber<BaseBean>(FileDetailActivity.this, false) {
                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);

                                        ToastUtils.showError(mContext, "取消共享失败!");
                                    }

                                    @Override
                                    public void onNext(BaseBean baseBean) {
                                        super.onNext(baseBean);

                                        ToastUtils.showSuccess(mContext, "取消共享成功!");
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                });
                        return true;
                    }
                });
    }

    /**
     * 退出共享
     */
    private void quitShare() {
        String[] menu = {"退出共享"};
        PopUtils.showBottomMenu(FileDetailActivity.this, rlMore.getRootView(), "操作", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        model.quitShare(FileDetailActivity.this,
                                fileId,
                                new ProgressSubscriber<BaseBean>(FileDetailActivity.this, false) {
                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);

                                        ToastUtils.showError(mContext, "退出共享失败!");
                                    }

                                    @Override
                                    public void onNext(BaseBean baseBean) {
                                        super.onNext(baseBean);

                                        ToastUtils.showSuccess(mContext, "退出共享成功!");
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                });
                        return true;
                    }
                });
    }

    /**
     * 取消下载
     */
    private void calcelDownload() {
        FileTransmitUtils.cancelDownload(fileId);
        if (isFileExist) {
            checkTimes = 0;
            fileExist();
        } else {
            fileNotExist();
        }


    }

    private void openFile() {
        if (!mFile.exists()) {
            ToastUtils.showToast(FileDetailActivity.this, getString(R.string.filelib_not_found));
            isFileExist = false;
            downloadOrOpen.setText(String.format(getString(R.string.filelib_download_file_btn), FormatFileSizeUtil.formatFileSize(fileSize)));
        }
        FileUtils.browseDocument(this, downloadOrOpen.getRootView(), mFile.getName(), mFile.getAbsolutePath());
        LogUtil.e("文件名" + mFile + "绝对路径" + mFile.getAbsolutePath());


    }

    private void downloadFile() {
        /*if (FileTypeUtils.isImage(mBean.getData().getBasics().getSiffix())) {
            tvProgressNum.setVisibility(View.VISIBLE);
            tvProgressNum.setText("正在加载...");
            downloadOrOpen.setVisibility(View.GONE);
            notice.setVisibility(View.GONE);
        } else*/

        /*{
            rlProgress.setVisibility(View.VISIBLE);
            notice.setVisibility(View.GONE);
            tvProcess.setVisibility(View.VISIBLE);
            progressbar.setProgress(0);
            downloadOrOpen.setVisibility(View.GONE);
            tvProcess.setText(String.format(getString(R.string.file_download_progress),
                    FormatFileSizeUtil.formatFileSize(0) + "", FormatFileSizeUtil.formatFileSize(fileSize)));
        }*/

        if (mSocketMessage == null) {
            if (!TextUtils.isEmpty(mFileName) && !TextUtils.isEmpty(fileType)) {
                if (!mFileName.toLowerCase().endsWith(fileType.toLowerCase())) {
                    mFileName = mFileName + fileType;
                }

            }
            checkLimitAndDownload();
        } else {
            checkLimitAndDownload();
        }


    }

    /**
     * 应用文件详情菜单
     */
    private void appFileMenu() {
        String[] menu = {"发送到聊天"};
        PopUtils.showBottomMenu(FileDetailActivity.this, rlMore.getRootView(), "操作", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int position) {
                        Bundle bundle = new Bundle();
                        switch (position) {
                            //发送到聊天
                            case 0:
                                bundle.putString(Constants.DATA_TAG1, MsgConstant.SEND_FILE_TO_SB);
                                CommonUtil.startActivtiyForResult(mContext, ChooseSendMemberActivity.class, Constants.REQUEST_CODE5, bundle);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

    }

    /**
     * 公司文件更多操作
     */
    private void companyFileMenu() {

        String[] menu = null;
        String[] menu1 = {"发送到聊天", "重命名", "上传新版本"};
        String[] menu2 = {"发送到聊天", "重命名", "上传新版本", "移动", "复制", "共享", "删除"};

        if (FileConstants.IM_MANAGER.equals(isManager)) {
            menu = menu2;
        } else if (isCreator) {
            menu = menu1;
        } else {
            appFileMenu();
            return;
        }
        PopUtils.showBottomMenu(FileDetailActivity.this, rlMore.getRootView(), "操作", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int position) {
                        Bundle bundle = new Bundle();
                        switch (position) {
                            //发送到聊天
                            case 0:
                                bundle.putString(Constants.DATA_TAG1, MsgConstant.SEND_FILE_TO_SB);
                                CommonUtil.startActivtiyForResult(mContext, ChooseSendMemberActivity.class, Constants.REQUEST_CODE5, bundle);
                                break;
                            //重命名
                            case 1:
                                bundle.putString(Constants.DATA_TAG1, mFileName);
                                CommonUtil.startActivtiyForResult(FileDetailActivity.this, RenameFileActivity.class, Constants.REQUEST_CODE3, bundle);

                                break;
                            //上传新版本
                            case 2:
                                uploadNewVersionFile();
                                break;
                            //移动
                            case 3:
                                moveOrCopyFile(FileConstants.MOVE);

                                break;
                            //复制
                            case 4:
                                moveOrCopyFile(FileConstants.COPY);

                                break;
                            //共享
                            case 5:
                                chooseShareMember();
                                break;
                            //删除
                            case 6:
                                deleteFile();
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
        member.setSelectState(C.NOT_FOR_SELECT);
        member.setId(TextUtil.parseLong(SPHelper.getEmployeeId()));
        members.add(member);
        bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
        bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
        CommonUtil.startActivtiyForResult(FileDetailActivity.this,
                SelectMemberActivity.class, Constants.REQUEST_CODE4, bundle);
    }

    private String getRootFolderName() {
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
     * 移动 复制
     */
    private void moveOrCopyFile(int moveOrCopy) {
        Bundle bundle = new Bundle();
        ArrayList<FolderNaviData> list = new ArrayList<>();
        bundle.putInt(FileConstants.FOLDER_TYPE, folderStyle);
        bundle.putInt(FileConstants.FOLDER_STYLE, folderStyle);
        bundle.putString(FileConstants.FOLDER_ID, "");
        bundle.putString(FileConstants.FOLDER_NAME, getRootFolderName());
        bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
        bundle.putString(FileConstants.FILE_ID, fileId);
        bundle.putInt(FileConstants.MOVE_OR_COPY, moveOrCopy);
        FolderNaviData fnd = new FolderNaviData();
        fnd.setFolderLevel(0);
        fnd.setFolderName(getRootFolderName());
        fnd.setFloderType(folderStyle);
        fnd.setFolderId("");
        list.add(fnd);
        bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, list);
        CommonUtil.startActivtiy(FileDetailActivity.this, MoveFileActivity.class, bundle);
    }

    /**
     * 转为公司文件
     */
    private void convertToCompanyFile() {
        Bundle bundle = new Bundle();
        ArrayList<FolderNaviData> list = new ArrayList<>();
        bundle.putInt(FileConstants.FOLDER_STYLE, 1);
        bundle.putInt(FileConstants.FOLDER_TYPE, 1);
        bundle.putString(FileConstants.FOLDER_ID, "");
        bundle.putString(FileConstants.FOLDER_NAME, "公司文件");
        bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
        bundle.putString(FileConstants.FILE_ID, fileId);
        bundle.putInt(FileConstants.MOVE_OR_COPY, FileConstants.CONVERT);
        FolderNaviData fnd = new FolderNaviData();
        fnd.setFolderLevel(0);
        fnd.setFolderName("公司文件");
        fnd.setFloderType(1);
        fnd.setFolderId("");
        list.add(fnd);
        bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, list);
        CommonUtil.startActivtiyForResult(FileDetailActivity.this, MoveFileActivity.class, Constants.REQUEST_CODE9, bundle);
    }

    /**
     * 个人文件-更多操作
     */
    private void personalFileMenu() {
        String[] menu = {"发送到聊天", "重命名", "上传新版本", "转为公司文件", "移动", "复制", "共享", "删除"};
        PopUtils.showBottomMenu(FileDetailActivity.this, rlMore.getRootView(), "操作", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int position) {
                        Bundle bundle = new Bundle();
                        switch (position) {
                            case 0:
                                //发送到聊天
                                bundle.putString(Constants.DATA_TAG1, MsgConstant.SEND_FILE_TO_SB);
                                CommonUtil.startActivtiyForResult(mContext, ChooseSendMemberActivity.class, Constants.REQUEST_CODE5, bundle);

                                break;
                            case 1:
                                //重命名
                                bundle.putString(Constants.DATA_TAG1, mFileName);
                                CommonUtil.startActivtiyForResult(FileDetailActivity.this, RenameFileActivity.class, Constants.REQUEST_CODE3, bundle);

                                break;
                            case 2:
                                uploadNewVersionFile();
                                break;
                            case 3:
                                //转为公司文件
                                convertToCompanyFile();
                                break;
                            case 4:
                                //移动
                                moveOrCopyFile(FileConstants.MOVE);
                                break;
                            case 5:
                                //复制
                                moveOrCopyFile(FileConstants.COPY);
                                break;
                            case 6:
                                //共享
                                chooseShareMember();
                                break;
                            case 7:
                                //删除
                                deleteFile();
                                break;

                            default:

                                break;


                        }


                        return true;
                    }
                });
    }


    /**
     * 上传新版本文件
     */
    private void uploadNewVersionFile() {
        PopUtils.showBottomMenu(FileDetailActivity.this, llRoot.getRootView(), "操作", chooseFileMenu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int position) {
                        switch (position) {
                            case 0:
                                //文件夹中上传
                                FileHelper.showFileChooser(FileDetailActivity.this);
                                break;
                            case 1:
                                //相册中上传
                                CommonUtil.getImageFromAlbumByMultiple(FileDetailActivity.this, 1);
                                break;
                            default:

                                break;
                        }
                        return true;
                    }
                });
        /*Observable.just(0).delay(500, TimeUnit.MILLISECONDS).subscribe(i -> {
            PopUtils.showBottomMenu(FileDetailActivity.this, llRoot.getRootView(), "操作", chooseFileMenu,
                    new OnMenuSelectedListener() {
                        @Override
                        public boolean onMenuSelected(int position) {
                            switch (position) {
                                case 0:
                                    //文件夹中上传
                                    FileHelper.showFileChooser(FileDetailActivity.this);
                                    break;
                                case 1:
                                    //相册中上传
                                    CommonUtil.getImageFromAlbumByMultiple(FileDetailActivity.this, 1);
                                    break;
                                default:

                                    break;
                            }
                            return true;
                        }
                    });
        });*/


    }

    /**
     * 退出共享
     */
    private void doNotShareThisFileWithMe() {
        String[] menu = {"退出共享"};
        PopUtils.showBottomMenu(FileDetailActivity.this, rlMore.getRootView(), "操作", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int position) {
                        switch (position) {
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
     * 应用文件发送到聊天
     */
    private void appFileSendToChat() {
        String[] menu = {"发送到聊天"};
        PopUtils.showBottomMenu(FileDetailActivity.this, rlMore.getRootView(), "操作", menu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int position) {
                        switch (position) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.DATA_TAG1, MsgConstant.SEND_FILE_TO_SB);
                                CommonUtil.startActivtiyForResult(mContext, ChooseSendMemberActivity.class, Constants.REQUEST_CODE5, bundle);

                                break;

                            default:

                                break;


                        }


                        return true;
                    }
                });
    }

    /**
     * 删除文件
     */
    private void deleteFile() {
        DialogUtils.getInstance()
                .sureOrCancel(FileDetailActivity.this, "提示",
                        "删除文件后历史版本也会被一并删除，且不可恢复。你确定要删除吗？", rlMore.getRootView(), new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                model.delFileLibrary(FileDetailActivity.this, fileId,
                                        new ProgressSubscriber<BaseBean>(FileDetailActivity.this, false) {
                                            @Override
                                            public void onError(Throwable e) {

                                                super.onError(e);

                                                ToastUtils.showError(mContext, "删除失败!");
                                            }

                                            @Override
                                            public void onNext(BaseBean baseBean) {

                                                super.onNext(baseBean);

                                                ToastUtils.showSuccess(mContext, "删除文件成功!");
                                                EventBusUtils.sendEvent(new MessageBean(1, FileConstants.DELETE_FILE_SUCCESS, folderId));
                                                finish();
                                            }
                                        });
                            }
                        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == Activity.RESULT_OK) {

        }
        if (requestCode == Constants.REQUEST_CODE2 && resultCode == Activity.RESULT_OK) {

        }
        if (requestCode == Constants.REQUEST_CODE3 && resultCode == Activity.RESULT_OK) {
            String fileNameString = data.getStringExtra(Constants.DATA_TAG1);
            model.renameFile(FileDetailActivity.this, fileId, fileNameString,
                    new ProgressSubscriber<BaseBean>(FileDetailActivity.this, false) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);

                            ToastUtils.showError(mContext, "重命名失败!");
                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);

                            getNetData(false);
                            setResult(Activity.RESULT_OK);
                            ToastUtils.showSuccess(mContext, "重命名成功!");

                        }
                    });

        }
        //共享
        if (requestCode == Constants.REQUEST_CODE4 && resultCode == Activity.RESULT_OK) {
            List<Member> list = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (list != null && list.size() > 0) {
                shareFile(list);
            } else {
                ToastUtils.showToast(FileDetailActivity.this, "未选择共享人,共享失败!");
                //共享
                Bundle bundle = new Bundle();
                bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);

                CommonUtil.startActivtiyForResult(FileDetailActivity.this,
                        SelectMemberActivity.class, Constants.REQUEST_CODE4, bundle);
            }
        }
        //文件夹中上传
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.CHOOSE_LOCAL_FILE) {
            Uri uri = data.getData();
            String path = UriUtil.getPhotoPathFromContentUri(FileDetailActivity.this, uri);
            File file = new File(path);

            if (file.exists()) {
                checkFileSizeAndUpload(file);
            }


        }
        //相册中上传
        if (resultCode == Activity.RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (photos == null || photos.size() <= 0) {
                    ToastUtils.showError(mContext, "数据错误，请重新上传");
                    return;
                }
                File file = new File(photos.get(0));
                if (!file.exists()) {
                    ToastUtils.showError(mContext, "数据错误，请重新上传");
                    return;
                }
                checkFileSizeAndUpload(file);
            }
        }
        //发送文件到聊天
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE5) {
            sendFile(data);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE6) {
            getNetData(false);
        }
        //转为公司文件
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE9) {
            folderStyle = 1;
            getNetData(false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendFile(Intent data) {
        if (data == null) {
            return;
        }
        Conversation c = (Conversation) data.getSerializableExtra(Constants.DATA_TAG1);
        if (c == null) {
            return;
        }
        QxMessage message = new QxMessage();
        if (c.getConversationType() == MsgConstant.GROUP) {
            message.setAllPeoples(c.getPeoples());
        }
        message.setFileSize(mBean.getData().getBasics().getSize());
        message.setFileName(mBean.getData().getBasics().getName());
        message.setFileType(mBean.getData().getBasics().getSiffix());
        message.setChatId(c.getConversationId());
        message.setFileId(mBean.getData().getBasics().getId());
        message.setType(MsgConstant.FILE);
        message.setFileUrl(sendFileUrl);
        DialogUtils.getInstance().share2cantact(FileDetailActivity.this, c.getTitle(), "",
                "文件", message.getFileName(), c.getAvatarUrl(), rlMore.getRootView(), new DialogUtils.OnShareClickListner() {
                    @Override
                    public void clickSure(String desc) {
                        int cmd = -1;
                        if (c.getConversationType() == MsgConstant.SINGLE) {
                            cmd = MsgConstant.IM_PERSONAL_CHAT_CMD;
                        }
                        if (c.getConversationType() == MsgConstant.GROUP) {
                            cmd = MsgConstant.IM_TEAM_CHAT_CMD;
                            message.setAllPeoples(c.getPeoples());
                        }
                        if (cmd == -1) {
                            return;
                        }
                        IM.getInstance().sendFileToSb(c.getConversationId(), c.getReceiverId(), cmd, message);
                        if (!TextUtils.isEmpty(desc)) {
                            IM.getInstance().sendTextMessage(c, desc);
                        }
                        ToastUtils.showSuccess(mContext, "发送成功");
                    }
                });
    }

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

    private void uploadFile(File file) {
        ToastUtils.showToast(mContext, "正在后台上传,请稍等");

        DownloadService.getInstance().fileVersionUpload(file.getAbsolutePath(), folderUrl,
                fileId, folderStyle, new UploadCallback<BaseBean>() {
                    @Override
                    public BaseBean parseNetworkResponse(okhttp3.Response response, int id) throws Exception {
                        String string = response.body().string();
                        BaseBean user = new Gson().fromJson(string, BaseBean.class);
                        if (user.getResponse().getCode() == 1001) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getNetData(false);
                                    setResult(Activity.RESULT_OK);
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getNetData(false);
                                setResult(Activity.RESULT_OK);
                            }
                        });
                    }
                });


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
        model.shareFileLibaray(FileDetailActivity.this, fileId, ids,
                new ProgressSubscriber<BaseBean>(FileDetailActivity.this, true) {
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

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe
    public void onMessage(MessageBean event) {
        if (MsgConstant.RESEND_MEMBER_TAG.equals(event.getTag()) && isTopActivity) {
            Conversation c = (Conversation) event.getObject();
            QxMessage message = new QxMessage();
            if (c.getConversationType() == MsgConstant.GROUP) {
                message.setAllPeoples(c.getPeoples());
            }
            message.setFileSize(mBean.getData().getBasics().getSize());
            message.setFileName(mBean.getData().getBasics().getName());
            message.setFileType(mBean.getData().getBasics().getSiffix());
            message.setChatId(c.getConversationId());
            message.setFileId(mBean.getData().getBasics().getId());
            message.setType(MsgConstant.FILE);
            message.setFileUrl(sendFileUrl);
            DialogUtils.getInstance().share2cantact(FileDetailActivity.this, c.getTitle(), "",
                    "文件", message.getFileName(), c.getAvatarUrl(), rlMore.getRootView(), new DialogUtils.OnShareClickListner() {
                        @Override
                        public void clickSure(String desc) {
                            int cmd = -1;
                            if (c.getConversationType() == MsgConstant.SINGLE) {
                                cmd = MsgConstant.IM_PERSONAL_CHAT_CMD;
                            }
                            if (c.getConversationType() == MsgConstant.GROUP) {
                                cmd = MsgConstant.IM_TEAM_CHAT_CMD;
                                message.setAllPeoples(c.getPeoples());
                            }
                            if (cmd == -1) {
                                return;
                            }
                            IM.getInstance().sendFileToSb(c.getConversationId(), c.getReceiverId(), cmd, message);
                            if (!TextUtils.isEmpty(desc)) {
                                IM.getInstance().sendTextMessage(c, desc);
                            }
                            ToastUtils.showSuccess(mContext, "发送成功");
                        }
                    });
        }
    }


    private static class MyHandler extends Handler {

    }

    DownloadCallback callback = new DownloadCallback() {
        @Override
        public void onSuccess(Call call, Response response) {
            ToastUtils.showToast(mContext, "上传完成!");
        }

        @Override
        public void onLoading(long total, long progress) {

            // CommonUtil.showToast(FormatFileSizeUtil.formatFileSize(progress) + "" + FormatFileSizeUtil.formatFileSize(total));

        }

        @Override
        public void onFailure(Call call, Throwable t) {

        }
    };

    long downloadBytes = 0;
    boolean receiveMessageBefore = false;

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
                //耳机拔出或无线耳机中断
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                }
            } else if (FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION.equals(action)) {
                //文件下载进度
                ProgressBean bean = (ProgressBean) intent.getSerializableExtra(Constants.DATA_TAG1);

                Log.e("收到消息1", new Gson().toJson(bean));
                Log.e("文件id收到1", bean.getFileId());
                Log.e("文件id原来2", fileId);
                if (bean != null && fileId.equals(bean.getFileId())) {

                    if (downloadOrOpen.getVisibility() == View.VISIBLE) {
                        downloadOrOpen.setVisibility(View.GONE);

                    }
                    if (notice.getVisibility() == View.VISIBLE) {
                        notice.setVisibility(View.GONE);
                    }
                    if (mSocketMessage == null) {

                        if (FileTypeUtils.isImage(fileType)) {
                            tvProgressNum.setVisibility(View.VISIBLE);
                            tvProgressNum.setText((int) ((bean.getBytesRead() * 100) / bean.getContentLength()) + "%");
                        } else {
                            rlProgress.setVisibility(View.VISIBLE);
                            progressbar.setVisibility(View.VISIBLE);
                            progressbar.setProgress((int) ((bean.getBytesRead() * 100) / bean.getContentLength()));
                            tvProcess.setText(String.format(getString(R.string.filelib_download_progress),
                                    FormatFileSizeUtil.formatFileSize(bean.getBytesRead()) + "",
                                    FormatFileSizeUtil.formatFileSize(bean.getContentLength())));

                        }
                        if (bean.isDone() || bean.getBytesRead() == bean.getContentLength()
                                || (int) ((bean.getBytesRead() * 100) / bean.getContentLength()) == 100) {
                            if (FileTypeUtils.isImage(fileType)) {
                                tvProgressNum.setVisibility(View.GONE);
                                loadImage();
                            } else {
                                checkTimes = 0;
                                fileExist();
                            }

                        }
                    } else {
                        if (FileTypeUtils.isImage(fileType)) {
                            tvProgressNum.setVisibility(View.VISIBLE);
                            tvProgressNum.setText((int) ((bean.getBytesRead() * 100) / bean.getContentLength()) + "%");
                        } else {
                            rlProgress.setVisibility(View.VISIBLE);
                            progressbar.setVisibility(View.VISIBLE);
                            progressbar.setProgress((int) ((bean.getBytesRead() * 100) / bean.getContentLength()));
                            tvProcess.setText(String.format(getString(R.string.filelib_download_progress),
                                    FormatFileSizeUtil.formatFileSize(bean.getBytesRead()) + "",
                                    FormatFileSizeUtil.formatFileSize(bean.getContentLength())));

                        }
                        if (bean.isDone()
                                || bean.getBytesRead() == bean.getContentLength()
                                || (int) ((bean.getBytesRead() * 100) / bean.getContentLength()) == 100
                                ) {
                            downloadBytes = 0;
                            //   LogUtil.e("文件下载完成");
                            if (FileTypeUtils.isImage(fileType)) {
                                tvProgressNum.setVisibility(View.GONE);
                                loadImage();
                            } else {
                                checkTimes = 0;
                                fileExist();
                            }

                        }
                    }

                }

            }


        }
    }

    private void checkFileExist() {
        if (mFile.exists()) {
            receiveMessageBefore = false;
            if (FileTypeUtils.isImage(fileType)) {
                tvProgressNum.setVisibility(View.GONE);
                loadImage();
            } else {
                checkTimes = 0;
                fileExist();
            }
        } else {
            downloadOrOpen.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkFileExist();
                }
            }, 2000);
        }


    }

    /**
     * 加载图片
     */
    private void loadImage() {


        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mFile.exists()) {
                    isFileExist = true;
                    rlImage.setVisibility(View.VISIBLE);
                    rlOther.setVisibility(View.GONE);
                    rlVideo.setVisibility(View.GONE);
                    rlAudio.setVisibility(View.GONE);
                    ivImage.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    try {
                        if (mFile != null && mFile.length() > 0) {
                            imageView.setImageBitmap(BitmapFactory.decodeFile(mFile.getAbsolutePath()));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tvProgressNum.setVisibility(View.GONE);
                } else {
                    if (mSocketMessage == null) {
                        loadImage();
                    }

                }

            }
        }, 100);

    }

    private MyReceiver mReceiver;

    /**
     * 接收下载广播和耳机拔出广播
     */
    private void initReceiver() {
        if (hasInitReceiver) {
            return;
        }
        if (mReceiver == null) {
            mReceiver = new MyReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION);
        //有线耳机拔出或无线耳机中断
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFile != null) {
            isFileExist = mFile.exists();
            if (isFileExist) {
                checkTimes = 0;
                fileExist();
            }
            initReceiver();
        }
        isTopActivity = true;
    }


    int checkTimes = 0;

    private void fileExist() {
        isFileExist = mFile.exists();
        if (!isFileExist && checkTimes > 3) {
            checkTimes = 0;
            rlProgress.setVisibility(View.GONE);
            notice.setVisibility(View.VISIBLE);
            downloadOrOpen.setText(String.format(getString(R.string.filelib_download_file_btn), FormatFileSizeUtil.formatFileSize(fileSize)));
            downloadOrOpen.setVisibility(View.VISIBLE);
            tvProcess.setVisibility(View.GONE);
            return;
        }
        if (isFileExist) {
            checkTimes = 0;
            rlProgress.setVisibility(View.GONE);
            notice.setVisibility(View.VISIBLE);
            downloadOrOpen.setText("用其他应用打开");
            downloadOrOpen.setVisibility(View.VISIBLE);
            tvProcess.setVisibility(View.GONE);
            return;
        }
        downloadOrOpen.postDelayed(new Runnable() {
            @Override
            public void run() {
                isFileExist = mFile.exists();
                if (isFileExist) {
                    checkTimes = 0;
                    rlProgress.setVisibility(View.GONE);
                    notice.setVisibility(View.VISIBLE);
                    downloadOrOpen.setText("用其他应用打开");
                    downloadOrOpen.setVisibility(View.VISIBLE);
                    tvProcess.setVisibility(View.GONE);
                } else {
                    fileExist();
                    checkTimes++;
                }
            }
        }, 500);


    }

    private void fileNotExist() {
        rlProgress.setVisibility(View.GONE);
        notice.setVisibility(View.VISIBLE);
        downloadOrOpen.setText(String.format(getString(R.string.filelib_download_file_btn), FormatFileSizeUtil.formatFileSize(fileSize)));
        downloadOrOpen.setVisibility(View.VISIBLE);
        tvProcess.setVisibility(View.GONE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //  initReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        isTopActivity = false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
