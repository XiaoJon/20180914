package com.hjhq.teamface.customcomponent.widget2.file;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.Photo;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.FileHelper;
import com.hjhq.teamface.basis.util.FileTypeUtils;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.UriUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.ImagePagerActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.adapter.AttachmentItemAdapter;
import com.hjhq.teamface.customcomponent.widget2.base.FileView;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import rx.Observable;


/**
 * 附件
 *
 * @author lx
 * @date 2017/6/20
 */

public class AttachmentView extends FileView implements ActivityPresenter.OnActivityResult {
    public final int REQUEST_CODE_AT_ATTACHMENT = 0x2501 + code;
    public final int FILE_SELECT = 0x3015 + code;

    protected BaseQuickAdapter fileAdapter;
    private File imageFromCamera;

    public AttachmentView(CustomBean bean) {
        super(bean);
    }


    @Override
    public void initOption() {
        TextUtil.setText(tvTitle, title);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new MyLinearDeviderDecoration(mActivity, R.color.gray_f2, (int) DeviceUtils.dpToPixel(mActivity, 1)));

        initAdapter();
        if (CustomConstants.DETAIL_STATE != state && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            rlAdd.setOnClickListener(v -> uploadFile());
        }
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
                if (isDetailState()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.DATA_TAG1, (Serializable) fileAdapter.getItem(position));
                    UIRouter.getInstance().openUri(getContext(), "DDComp://custom/file", bundle);
                    return;
                }
                UploadFileBean item = (UploadFileBean) adapter.getItem(position);
                if (FileTypeUtils.isImage(item.getFileType())) {
                    //图片
                    List<UploadFileBean> pictureList = fileAdapter.getData();
                    ArrayList<Photo> list = new ArrayList<>(pictureList.size());
                    Observable.from(pictureList)
                            .filter(picture -> FileTypeUtils.isImage(picture.getFileType()))
                            .subscribe(picture -> list.add(new Photo(picture.getFileUrl())));

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImagePagerActivity.PICTURE_LIST, list);
                    bundle.putInt(ImagePagerActivity.SELECT_INDEX, list.indexOf(item));
                    CommonUtil.startActivtiy(mActivity, ImagePagerActivity.class, bundle);
                } else {
                    FileUtils.browseDocument(mView.getContext(), item.getFileName(), item.getFileUrl());
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //清除焦点
                RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
                adapter.remove(position);
            }
        });
    }

    protected void initAdapter() {
        fileAdapter = new AttachmentItemAdapter((ActivityPresenter) mActivity, uploadFileBeanList, state, bean.getModuleBean(), fieldControl);
        mRecyclerView.setAdapter(fileAdapter);
    }

    @Override
    public Object getValue() {
        List<UploadFileBean> data = fileAdapter.getData();
        List<UploadFileBean> uploadFileList = new ArrayList<>();
        //过滤上传失败的附件
        Observable.from(data)
                .filter(bean -> bean.getSerialNumber() != null)
                .subscribe(bean -> uploadFileList.add(bean));

        return CollectionUtils.isEmpty(uploadFileList) ? "" : uploadFileList;
    }

    /**
     * 上传附件
     */
    @Override
    public void uploadFile() {
        //清除焦点
        RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
        int fileCount = getUploadCount();
        if (fileCount <= 0) {
            ToastUtils.showError(mActivity, "最多上传" + maxCount + "个文件");
            return;
        }

        String[] strs = {"选择文件", "相册", "拍照"};
        PopUtils.showBottomMenu(mActivity, mView, "上传附件", strs, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                switch (p) {
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        //设置类型，我这里是任意类型，任意后缀的可以这样写。
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        ((ActivityPresenter) mView.getContext()).setOnActivityResult(FILE_SELECT, AttachmentView.this);
                        mActivity.startActivityForResult(intent, FILE_SELECT);

                        break;
                    case 1:
                        ((ActivityPresenter) mView.getContext()).setOnActivityResult(PhotoPicker.REQUEST_CODE, AttachmentView.this);
                        CommonUtil.getImageFromAlbumByMultiple((Activity) mView.getContext(), fileCount);
                        break;
                    case 2:
                        SystemFuncUtils.requestPermissions(getContext(), android.Manifest.permission.CAMERA, aBoolean -> {
                            if (aBoolean) {
                                ((ActivityPresenter) mView.getContext()).setOnActivityResult(REQUEST_CODE_AT_ATTACHMENT, AttachmentView.this);
                                imageFromCamera = CommonUtil.getImageFromCamera((ActivityPresenter) mView.getContext(), REQUEST_CODE_AT_ATTACHMENT);
                            } else {
                                ToastUtils.showError(getContext(), "必须获得必要的权限才能拍照！");
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
     * 获取还可上传的数量
     */
    private int getUploadCount() {
        int fileCount = PhotoPicker.DEFAULT_MAX_COUNT;
        if (countLimit == 1) {
            fileCount = maxCount - fileAdapter.getItemCount();
        }
        return fileCount;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == FILE_SELECT) {
            //附件上传 文件选择
            Uri uri = data.getData();
            String photoPathFromContentUri = UriUtil.getPhotoPathFromContentUri(mView.getContext(), uri);
            String substring = photoPathFromContentUri.substring(photoPathFromContentUri.lastIndexOf("/") + 1);
            String substring1 = photoPathFromContentUri.substring(photoPathFromContentUri.lastIndexOf(".") + 1);
            UploadFileBean fileBean = new UploadFileBean(substring, photoPathFromContentUri, substring1);

            List<UploadFileBean> fileList = fileAdapter.getData();
            File file = new File(photoPathFromContentUri);
            if (!file.exists()) {
                ToastUtils.showToast(mActivity, "本地文件不存在");
                return;
            }
            if (checkFileSize(file)) {
                isOverLimit(file, fileList, fileBean);
            } else {
                ToastUtils.showToast(mActivity, "文件大小超出限制");
            }

        } else if (requestCode == REQUEST_CODE_AT_ATTACHMENT) {
            //拍照
            if (imageFromCamera != null && imageFromCamera.exists()) {
                List<UploadFileBean> fileList = fileAdapter.getData();

                String fiilName = imageFromCamera.getName();
                String fileType = fiilName.substring(fiilName.lastIndexOf(".") + 1);

                if (checkFileSize(imageFromCamera)) {
                    UploadFileBean fileBean =
                            new UploadFileBean(fiilName, imageFromCamera.getAbsolutePath(), fileType);
                    isOverLimit(imageFromCamera, fileList, fileBean);
                } else {
                    ToastUtils.showToast(mActivity, "文件大小超出限制");
                }

            } else {
                ToastUtils.showToast(mActivity, "本地照片不存在");
            }
        } else if (requestCode == PhotoPicker.REQUEST_CODE) {
            //相册
            if (data != null) {
                List<UploadFileBean> fileList = fileAdapter.getData();
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                int overLimitNum = 0;
                int notExistNum = 0;
                int limitNum = 0;
                List<UploadFileBean> overLimitList = new ArrayList<>();
                for (int i = 0; i < photos.size(); i++) {
                    String url = photos.get(i);
                    String fiilName = JYFileHelper.getPicNameFromPath(url);
                    String fileType = fiilName.substring(fiilName.lastIndexOf(".") + 1);
                    File file = new File(url);
                    if (!file.exists()) {
                        notExistNum++;
                        continue;
                    }
                    UploadFileBean fileBean = new UploadFileBean(fiilName, url, fileType);
                    if (checkFileSize(file)) {
                        if (FileHelper.checkLimit(mActivity, file)) {
                            overLimitList.add(fileBean);
                        } else {
                            fileList.add(fileBean);
                        }

                    } else {
                        overLimitNum++;
                    }

                }
                String alert = "";
                if ((overLimitNum + notExistNum) > 0) {
                    alert = alert + (overLimitNum + notExistNum) + "个文件不存在或超出后台设置的" + maxSize + "M大小限制;";
                }
                if (overLimitList.size() > 0) {
                    alert = alert + "当前为移动网络且有" + overLimitList.size() + "个文件大小超过10M,继续上传吗?";
                    DialogUtils.getInstance().sureOrCancel(mActivity, "", alert, mView, new DialogUtils.OnClickSureListener() {
                        @Override
                        public void clickSure() {
                            fileList.addAll(overLimitList);
                            fileAdapter.notifyDataSetChanged();
                        }
                    });
                } else if (!TextUtils.isEmpty(alert)) {
                    ToastUtils.showToast(mActivity, alert);
                }
                fileAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 判断是否超出本地设置的条件
     *
     * @param file
     * @param fileList
     * @param fileBean
     */
    private void isOverLimit(File file, List<UploadFileBean> fileList, UploadFileBean fileBean) {
        if (FileHelper.checkLimit(mActivity, file)) {
            DialogUtils.getInstance().sureOrCancel(mActivity, "", "当前为移动网络且文件大小超过10M,继续上传吗?", mView, new DialogUtils.OnClickSureListener() {
                @Override
                public void clickSure() {
                    fileList.add(fileBean);
                    fileAdapter.notifyDataSetChanged();
                }
            });
        } else {
            fileList.add(fileBean);
            fileAdapter.notifyDataSetChanged();
        }
    }
}
