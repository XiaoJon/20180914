package com.hjhq.teamface.common.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;

import java.io.File;

import me.iwf.photopicker.PhotoPicker;

/**
 * 通用工具类
 *
 * @author Administrator
 * @date 2016/7/6
 */
public class CommonUtil {

    private static Toast toast = null;
    private static Toast imageToast = null;

    public static void startActivtiy(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        //加入此flag会导致onActivityResult不会被回调
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivtiyNewTask(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        //加入此flag会导致onActivityResult不会被回调
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivtiy(Context context, Class cls, Bundle bundle) {
        if (bundle == null) {
            startActivtiy(context, cls);
            return;
        }
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT < 23) {
            //加入此flag会导致onActivityResult不会被回调
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startActivtiy2(Activity context, Class cls, Bundle bundle) {
        if (bundle == null) {
            startActivtiy(context, cls);
            return;
        }
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        /*if (Build.VERSION.SDK_INT < 23) {
            //加入此flag会导致onActivityResult不会被回调
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }*/
        context.startActivity(intent);
    }

    public static void startActivtiyForResult(Context context, Class cls, int requestCode) {
        Intent intent = new Intent(context, cls);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            throw new IllegalThreadStateException();
        }
    }

    public static void startActivtiyForResult(Context context, Class cls, int requestCode, Bundle bundle) {
        if (bundle == null) {
            startActivtiyForResult(context, cls, requestCode);
            return;
        }
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);

        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            throw new IllegalThreadStateException();
        }
    }

    /**
     * Fragment调用
     *
     * @param fragment    fragment
     * @param cls         目标
     * @param requestCode 请求码
     * @param bundle      参数
     */
    public static void startActivtiyForResult(Fragment fragment, Class cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent(fragment.getContext(), cls);
        intent.putExtras(bundle);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void sendBroadcast(Context mContext, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(mContext.getPackageName());
        //只限在应用内使用的广播,安全高效
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }


    /**
     * 拍照获取图片
     *
     * @param requestCode
     */
    public static File getImageFromCamera(Activity activity, int requestCode) {
        File photoFile = new File(JYFileHelper.getFileDir(activity, Constants.PATH_IMAGE), String.valueOf(System.currentTimeMillis()) + ".jpg");
        LogUtil.e("photoFile==" + photoFile.getAbsolutePath());
        Uri imageUri = Uri.fromFile(photoFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            doTakePhotoIn7(activity, photoFile.getAbsolutePath(), requestCode);
        } else {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            activity.startActivityForResult(getImageByCamera, requestCode);
        }
        return photoFile;
    }


    //在Android7.0以上拍照
    private static void doTakePhotoIn7(Activity act, String path, int requestCode) {
        Uri mCameraTempUri;
        try {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put(MediaStore.Images.Media.DATA, path);
            mCameraTempUri = act.getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (mCameraTempUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            }
            act.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 相册获取图片
     *
     * @param requestCode
     */
    public static void getImageFromAlbum(Activity act, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");//相片类型
        act.startActivityForResult(intent, requestCode);
    }


    /**
     * 图片多选
     *
     * @param activity
     */
    public static void getImageFromAlbumByMultiple(Activity activity) {
        getImageFromAlbumByMultiple(activity, PhotoPicker.DEFAULT_MAX_COUNT);
    }

    /**
     * 图片多选
     *
     * @param activity
     * @param count
     */
    public static void getImageFromAlbumByMultiple(Activity activity, int count) {
        getImageFromAlbumByMultiple(activity, count, PhotoPicker.REQUEST_CODE);
    }

    /**
     * 图片多选
     *
     * @param activity
     * @param count
     */
    public static void getImageFromAlbumByMultiple(Activity activity, int count, int requestCode) {
        PhotoPicker.builder()
                .setPhotoCount(count)
                .setShowCamera(false)
                .setShowGif(false)
                .setPreviewEnabled(false)
                .start(activity, requestCode);
    }
}
