package com.hjhq.teamface.customcomponent.widget2.file;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.image.ImageformatUtil;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.ImagePagerActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.adapter.PictureItemAdapter;
import com.hjhq.teamface.customcomponent.widget2.base.FileView;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import rx.Observable;

/**
 * 图片组件
 *
 * @author lx
 * @date 2017/6/20
 */

public class PictureView extends FileView implements ActivityPresenter.OnActivityResult {
    public final int REQUEST_CODE_AT_PICTURE = 0x2102 + code;
    protected BaseQuickAdapter fileAdapter;
    private File imageFromCamera;

    public PictureView(CustomBean bean) {
        super(bean);
    }

    @Override
    public void initOption() {
        rlAdd.setVisibility(View.GONE);
        TextUtil.setText(tvTitle, title);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(mActivity, 5);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(new MyLinearDeviderDecoration(mActivity, R.color.gray_f2, (int) DeviceUtils.dpToPixel(mActivity, 1)));
        if (CustomConstants.DETAIL_STATE != state && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            UploadFileBean fakeBean = new UploadFileBean();
            fakeBean.setFileUrl(CustomConstants.URL);
            uploadFileBeanList.add(fakeBean);
        }
        initAdapter();

        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //清除焦点
                RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
                List<UploadFileBean> pictureList = fileAdapter.getData();
                if (view.getId() == R.id.iv_icon) {
                    if (CustomConstants.URL.equals(pictureList.get(position).getFileUrl())) {
                        uploadFile();
                    } else {
                        if (isDetailState()) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constants.DATA_TAG1, (Serializable) fileAdapter.getItem(position));
                            UIRouter.getInstance().openUri(getContext(), "DDComp://custom/file", bundle);
                            return;
                        }
                        ArrayList<Photo> list = new ArrayList<>(pictureList.size());
                        Observable.from(pictureList).subscribe(picture -> {
                            if (!CustomConstants.URL.equals(picture.getFileUrl())) {
                                list.add(new Photo(picture.getFileUrl()));
                            }
                        });
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ImagePagerActivity.PICTURE_LIST, list);
                        bundle.putInt(ImagePagerActivity.SELECT_INDEX, position);
                        CommonUtil.startActivtiy(mActivity, ImagePagerActivity.class, bundle);
                    }

                } else if (view.getId() == R.id.iv_del) {
                    adapter.remove(position);
                }
            }
        });
    }

    protected void initAdapter() {
        fileAdapter = new PictureItemAdapter((ActivityPresenter) mActivity, uploadFileBeanList, state, bean.getModuleBean(), fieldControl);
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
        int fileCount = getUploadCount();
        if (fileCount <= 0) {
            ToastUtils.showError(mActivity, "最多上传" + maxCount + "张图片");
            return;
        }

        String[] strs = {"相册", "拍照"};
        PopUtils.showBottomMenu(mActivity, mView, "上传图片", strs, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                switch (p) {
                    case 0:
                        ((ActivityPresenter) mView.getContext()).setOnActivityResult(PhotoPicker.REQUEST_CODE, PictureView.this);
                        CommonUtil.getImageFromAlbumByMultiple((ActivityPresenter) mView.getContext(), fileCount);
                        break;
                    case 1:
                        SystemFuncUtils.requestPermissions(getContext(), android.Manifest.permission.CAMERA, aBoolean -> {
                            if (aBoolean) {
                                ((ActivityPresenter) mView.getContext()).setOnActivityResult(REQUEST_CODE_AT_PICTURE, PictureView.this);
                                imageFromCamera = CommonUtil.getImageFromCamera((ActivityPresenter) mView.getContext(), REQUEST_CODE_AT_PICTURE);
                            } else {
                                ToastUtils.showError(getContext(), "必须获得必要的权限才能拍照！");
                            }
                        });

                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 获取还可上传的数量
     */
    private int getUploadCount() {
        int fileCount = PhotoPicker.DEFAULT_MAX_COUNT;
        int size = fileAdapter.getData().size();
        if (countLimit == 1) {
            fileCount = maxCount - (fileAdapter.getData().size() - 1);
        }
        return fileCount;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_AT_PICTURE) {
            //拍照
            if (imageFromCamera != null && imageFromCamera.exists()) {
                List<UploadFileBean> fileList = fileAdapter.getData();
                File file = ImageformatUtil.saveBitmapFile(mActivity, ImageformatUtil.getimage(imageFromCamera.getAbsolutePath()));
                String fiilName = file.getName();
                String fileType = fiilName.substring(fiilName.lastIndexOf(".") + 1);
                int index = 0;
                if (fileList.size() > 1) {
                    index = fileList.size() - 1;
                }
                if (!file.exists()) {
                    ToastUtils.showToast(mActivity, "本地照片不存在");
                } else {
                    if (checkFileSize(file)) {
                        UploadFileBean fileBean = new UploadFileBean(fiilName, file.getAbsolutePath(), fileType);
                        isOverLimit(file, fileList, index, fileBean);
                    } else {
                        ToastUtils.showToast(mActivity, "照片大小超出限制");
                    }
                }
            }
        } else if (requestCode == PhotoPicker.REQUEST_CODE) {
            //相册
            if (data != null) {
                List<UploadFileBean> fileList = fileAdapter.getData();

                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                //超出后台设置大小
                int overLimitNum = 0;
                int notExistNum = 0;
                //超出本地设置大小
                int limitNum = 0;
                List<UploadFileBean> overLimitList = new ArrayList<>();
                for (int i = 0; i < photos.size(); i++) {
                    String url = photos.get(i);
                    File file = ImageformatUtil.saveBitmapFile(mActivity, ImageformatUtil.getimage(url));
                    String fiilName = file.getName();
                    String fileType = fiilName.substring(fiilName.lastIndexOf(".") + 1);
                    int index = 0;
                    if (fileList.size() > 1) {
                        index = fileList.size() - 1;
                    }
                    if (!file.exists()) {
                        notExistNum++;
                        continue;
                    }
                    UploadFileBean fileBean = new UploadFileBean(fiilName, file.getAbsolutePath(), fileType);
                    if (checkFileSize(file)) {
                        if (FileHelper.checkLimit(mActivity, file)) {
                            overLimitList.add(fileBean);
                            limitNum++;
                        } else {
                            fileList.add(index, fileBean);
                        }
                    } else {
                        overLimitNum++;
                    }
                }
                String alert = "";
                if ((overLimitNum + notExistNum) > 0) {
                    alert = alert + (overLimitNum + notExistNum) + "张图片不存在或超出后台设置的" + maxSize + "M大小限制;";
                }
                if (overLimitList.size() > 0) {
                    alert = alert + "当前为移动网络且有" + overLimitList.size() + "张图片大小超过10M,继续上传吗?";
                    DialogUtils.getInstance().sureOrCancel(mActivity, "", alert, mView, new DialogUtils.OnClickSureListener() {
                        @Override
                        public void clickSure() {
                            int index = 0;
                            if (fileList.size() > 1) {
                                index = fileList.size() - 1;
                            }
                            fileList.addAll(index, overLimitList);
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
    private void isOverLimit(File file, List<UploadFileBean> fileList, int index, UploadFileBean fileBean) {
        if (FileHelper.checkLimit(mActivity, file)) {
            DialogUtils.getInstance().sureOrCancel(mActivity, "", "当前为移动网络且文件大小超过10M,继续上传吗?", mView, () -> {
                fileList.add(index, fileBean);
                fileAdapter.notifyDataSetChanged();
            });
        } else {
            fileList.add(index, fileBean);
            fileAdapter.notifyDataSetChanged();
        }
    }
}
