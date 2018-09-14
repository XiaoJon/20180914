package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.image.TextIamgeTransform;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.DrawableUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.image.ImageformatUtil;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.image.SubsamplingScaleImageView;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.db.DBManager;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;
import com.soundcloud.android.crop.Crop;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 更换头像
 *
 * @author Administrator
 */
public class ChangeAvatarActivity extends BaseTitleActivity {
    public static final String SD_PATH = "/storage";

    @Bind(R.id.scale_image_avatar)
    SubsamplingScaleImageView ivAvatar;
    private File imageFromCamera;
    private String userAvatar;
    private String userName;
    private Bitmap textAvatarBitmap;

    @Override
    protected int getChildView() {
        return R.layout.activity_change_avatar;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle("我的头像");
        Intent intent = getIntent();
        userAvatar = intent.getStringExtra(Constants.DATA_TAG1);
        userName = intent.getStringExtra(Constants.DATA_TAG2);
        boolean isChanger = intent.getBooleanExtra(Constants.DATA_TAG3, false);
        if (isChanger) {
            setRightMenuIcons(R.drawable.user_more_icon);
        }
    }

    @Override
    protected void initData() {
        ivAvatar.setMinimumDpi(50);
        ivAvatar.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        setAvatar();
    }

    private void setAvatar() {
        if (TextUtil.isEmpty(userAvatar)) {
            textAvatarBitmap = TextIamgeTransform.getSquareTextAvatarBitmap(mContext, userName);
            ivAvatar.setImageBitmap(textAvatarBitmap);
        } else if (userAvatar.startsWith(SD_PATH)) {
            Glide.with(mContext)
                    .load(userAvatar)
                    .into(new ViewTarget<SubsamplingScaleImageView, GlideDrawable>(ivAvatar) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            Bitmap bitmap = DrawableUtils.drawable2Bitmap(resource.getCurrent());
                            this.view.setImageBitmap(bitmap);
                        }
                    });
        } else {
            GlideUrl glideUrl = new GlideUrl(userAvatar, new LazyHeaders.Builder().addHeader("TOKEN", SPHelper.getToken()).build());
            Glide.with(mContext)
                    .load(glideUrl)
                    .placeholder(R.drawable.icon_default)
                    .into(new ViewTarget<SubsamplingScaleImageView, GlideDrawable>(ivAvatar) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            Bitmap bitmap = DrawableUtils.drawable2Bitmap(resource.getCurrent());
                            this.view.setImageBitmap(bitmap);
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        selectedHeadPortrait();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 更换头像
     */
    private void selectedHeadPortrait() {
        PopUtils.showBottomMenu(this, getContainer(), "选择", new String[]{"拍照", "从相册中选择", "保存照片"}, position -> {
            switch (position) {
                case 0:
                    SystemFuncUtils.requestPermissions(mContext, android.Manifest.permission.CAMERA, aBoolean -> {
                        if (aBoolean) {
                            imageFromCamera = CommonUtil.getImageFromCamera(mContext, Constants.TAKE_PHOTO_NEW_REQUEST_CODE);
                        } else {
                            ToastUtils.showError(mContext, "必须获得必要的权限才能拍照！");
                        }
                    });
                    break;
                case 1:
                    CommonUtil.getImageFromAlbumByMultiple(ChangeAvatarActivity.this, 1);
                    break;
                case 2:
                    saveAvatar();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void saveAvatar() {
        if (TextUtil.isEmpty(userAvatar)) {
            ImageformatUtil.saveBmp2Gallery(mContext, textAvatarBitmap, System.currentTimeMillis() + "");
            ToastUtils.showSuccess(mContext, "头像保存成功！");
        } else if (userAvatar.startsWith(SD_PATH)) {
            ImageformatUtil.saveBmp2Gallery(mContext, ImageformatUtil.getimage(userAvatar), System.currentTimeMillis() + "");
            ToastUtils.showSuccess(mContext, "头像保存成功！");
        } else {
            Observable.just(1).subscribeOn(Schedulers.io())
                    .filter(i -> {
                        try {
                            GlideUrl glideUrl = new GlideUrl(userAvatar, new LazyHeaders.Builder().addHeader("TOKEN", SPHelper.getToken()).build());
                            Bitmap myBitmap = Glide.with(mContext)
                                    .load(glideUrl)
                                    .asBitmap()
                                    .centerCrop()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                            ImageformatUtil.saveBmp2Gallery(mContext, myBitmap, System.currentTimeMillis() + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
                            ToastUtils.showSuccess(mContext, "头像保存成功！");
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showSuccess(mContext, "头像保存失败！");
                        }

                        @Override
                        public void onNext(Integer integer) {
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            //获取照片
            ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            Uri imageContentUri = FileUtils.getImageContentUri(mContext, photos.get(0));
            Uri destination = Uri.fromFile(new File(JYFileHelper.getFileDir(mContext, Constants.PATH_IMAGE), "cropped" + System.currentTimeMillis()));
            Crop.of(imageContentUri, destination).asSquare().start(this);
        } else if (requestCode == Constants.TAKE_PHOTO_NEW_REQUEST_CODE) {
            //拍照新建图片
            if (imageFromCamera != null && imageFromCamera.exists()) {
                Uri imageContentUri = FileUtils.getImageContentUri(mContext, imageFromCamera);
                Uri destination = Uri.fromFile(new File(JYFileHelper.getFileDir(mContext, Constants.PATH_IMAGE), "cropped" + System.currentTimeMillis()));
                Crop.of(imageContentUri, destination).asSquare().start(this);
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            //裁剪后
            if (resultCode == RESULT_OK) {
                Uri output = Crop.getOutput(data);
                String filePath = FileUtils.getFilePathFromContentUri(mContext, output);
                modPhotograph(filePath);
            } else if (resultCode == Crop.RESULT_ERROR) {
                ToastUtils.showError(mContext, Crop.getError(data).getMessage());
            }
        }
    }

    /**
     * 修改头像
     */
    private void modPhotograph(String url) {
        MainLogic.getInstance().uploadAvatarFile(this, url, new ProgressSubscriber<UpLoadFileResponseBean>(mContext) {
            @Override
            public void onNext(UpLoadFileResponseBean baseBean) {
                super.onNext(baseBean);
                Map<String, String> map = new HashMap<>(1);
                String fileUrl = baseBean.getData().get(0).getFileUrl();
                map.put("picture", fileUrl);
                editEmployeeDetail(map);
            }
        });
    }

    /**
     * 修改个人信息
     *
     * @param map 参数信息
     */
    private void editEmployeeDetail(Map<String, String> map) {
        MainLogic.getInstance().editEmployeeDetail(this, map, new ProgressSubscriber<BaseBean>(this, false) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                userAvatar = map.get("picture");
                SPHelper.setUserAvatar(userAvatar);
                DBManager.getInstance().updateMyAvatar(userAvatar);
                //更换头像时同时更改聊天中的头像
                IM.getInstance().initAvatar(userAvatar);
                initData();
                EventBusUtils.sendEvent(new MessageBean(Constants.EDIT_USER_INFO, "修改个人信息", null));
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ivAvatar = null;
    }
}
