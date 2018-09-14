package com.hjhq.teamface.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.MyApplication;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.util.file.JYFileHelper;

import java.io.File;

import me.iwf.photopicker.PhotoPicker;

/**
 * 通用工具类
 * Created by Administrator on 2016/7/6.
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


    @Deprecated
    public static void showToast(String text) {

        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance()
                    .getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 显示成功或失败或警告等
     *
     * @param text 提示文字
     * @param type 类型
     */
    @Deprecated
    public static void showToast(Context mContext, String text, String type) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        View view = View.inflate(mContext, R.layout.center_toast_layout, null);
        ImageView icon = ((ImageView) view.findViewById(R.id.iv_icon));
        TextView toastText = ((TextView) view.findViewById(R.id.tv_name));
        if (imageToast == null) {
            imageToast = new Toast(mContext);
            imageToast.setGravity(Gravity.CENTER, 0, 0);
            imageToast.setView(view);
            toastText.setText(text);
        } else {
            toastText.setText(text);
            imageToast.setView(view);
        }
        if ("0".equals(type)) {
            icon.setImageResource(R.drawable.action_ok);
        } else if ("1".equals(type)) {
            icon.setImageResource(R.drawable.icon_delete_red);
        }

        imageToast.show();
    }

    @Deprecated
    public static void showToast(int res) {
        showToast(MyApplication.getInstance().getString(res));
    }


    /**
     * 拍照获取图片
     *
     * @param requestCode
     */
    public static File getImageFromCamera(Activity act, int requestCode) {
        File photoFile = new File(JYFileHelper.getFileDir(act, Constants.PATH_IMAGE), String.valueOf(System.currentTimeMillis()) + ".jpg");
        LogUtil.e("photoFile==" + photoFile.getAbsolutePath());
        Uri imageUri = Uri.fromFile(photoFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            doTakePhotoIn7(act, photoFile.getAbsolutePath(), requestCode);
        } else {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            act.startActivityForResult(getImageByCamera, requestCode);
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

    public static Uri getImageFromCamera2(Activity act, int requestCode) {
        //拍照
        String SDState = Environment.getExternalStorageState();

        //if (SDState.equals(Environment.MEDIA_MOUNTED)) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
        /***
         * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
         * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
         * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
         */
        ContentValues values = new ContentValues();
        Uri photoUri = act.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
        act.startActivityForResult(intent, requestCode);
        //}


        //File photoFile = new File(JYFileHelper.getFileDir(Constants.PATH_IMAGE), String.valueOf(System.currentTimeMillis()) + ".jpg");
        LogUtil.e("onActivityResult--拍照新建图片" + photoUri.getEncodedPath());
        return photoUri;
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

    public static final int MAX_FILE_COUNT = 5;

    /**
     * 图片多选
     *
     * @param activity
     */
    public static void getImageFromAlbumByMultiple(Activity activity) {
        getImageFromAlbumByMultiple(activity, MAX_FILE_COUNT);
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
