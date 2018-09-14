package com.hjhq.teamface.basis.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ken on 2015/11/13.
 */
public class FileHelper {
    private Activity mActivity;
    private int FILE_SELECT_CODE = 1001;
    public static final String PICTURE_DIR = "sdcard/JChatDemo/pictures/";

    private static FileHelper mInstance = new FileHelper();

    public static FileHelper getInstance() {
        return mInstance;
    }

    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getUserAvatarPath(String userName) {
        return PICTURE_DIR + userName + ".png";
    }

    public static String createAvatarPath(String userName) {
        String dir = PICTURE_DIR;
        File destDir = new File(dir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file;
        if (userName != null) {
            file = new File(dir, userName + ".png");
        } else {
            file = new File(dir, DateFormat.format("yyyy_MMdd_hhmmss",
                    Calendar.getInstance(Locale.CHINA)) + ".png");
        }
        return file.getAbsolutePath();
    }


    public interface CopyFileCallback {
        public void copyCallback(Uri uri);
    }

/*

    public void copyFile(final String fileName, final String filePath, final Activity context,
                         final CopyFileCallback callback) {
        if (isSdCardExist()) {
            final Dialog dialog = DialogCreator.createLoadingDialog(context,
                    context.getString(R.string.jmui_loading));
            dialog.show();
            Thread thread = new Thread(() -> {
                try {
                    FileInputStream fis = new FileInputStream(new File(filePath));
                    File destDir = new File(Constants.FILE_DIR);
                    if (!destDir.exists()) {
                        destDir.mkdirs();
                    }
                    final File tempFile = new File(Constants.FILE_DIR + fileName);
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    byte[] bt = new byte[1024];
                    int c;
                    while ((c = fis.read(bt)) > 0) {
                        fos.write(bt, 0, c);
                    }
                    //关闭输入、输出流
                    fis.close();
                    fos.close();

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.copyCallback(Uri.fromFile(tempFile));
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                }
            });
            thread.start();
        } else {
            Toast.makeText(context, context.getString(R.string.jmui_sdcard_not_exist_toast), Toast.LENGTH_SHORT).show();
        }
    }
*/

    /**
     * 调用文件选择软件来选择文件
     **/
    public static void showFileChooser(Activity mActivity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            mActivity.startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"), Constants.CHOOSE_LOCAL_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            ToastUtils.showError(mActivity, "无文件选择器");

        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        LogUtil.e(scheme);
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] proj = {MediaStore.MediaColumns.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            try {
                if (null != cursor
                    // && cursor.moveToFirst()
                        ) {
                    cursor.moveToFirst();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    data = cursor.getString(column_index);
                }
            } finally {
                cursor.close();

            }

        }
        return data;
    }

    //public static void openDocument(Activity mActivity,) {}

    /**
     * 质量压缩
     * 设置bitmap options属性，降低图片的质量，像素不会减少
     * 第一个参数为需要压缩的bitmap图片对象，第二个参数为压缩后图片保存的位置
     * 设置options 属性0-100，来实现压缩
     *
     * @param bmp
     * @param file
     */
    public static void qualityCompress(Bitmap bmp, File file) {
        // 0-100 100为不压缩
        int quality = 20;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sizeCompress(Bitmap bmp, File file) {
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 8;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存bitmap到SD卡
     *
     * @param bitmap
     * @param imagename
     */
    public static String saveBitmapToSDCard(Context context, Bitmap bitmap, String imagename) {
        File file = new File(JYFileHelper.getFileDir(context, Constants.PATH_IMAGE), imagename);
        String path = file.getAbsolutePath();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
            }

            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkLimit(Context context, long size) {
        //非WiFi网络环境下检测下载文件大小
        boolean flag = false;
        if (
                NetWorkUtils.getNetworkType(context) != ConnectivityManager.TYPE_WIFI &&
                        SPHelper.getSizeLimitState(true) && size > FileConstants.LIMIT_SIZE) {
            flag = true;
        }
        return flag;
    }

    /**
     * 检查文件下载大小限制
     *
     * @param localFilePath 文件绝对路径
     * @return true 限制,false 不限制
     */
    public static boolean checkLimit(Context context, String localFilePath) {
        //非WiFi网络环境下检测下载文件大小
        File file = new File(localFilePath);
        long fileSize = 0L;
        try {
            if (!file.exists()) {
                return false;
            }
            fileSize = file.length();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        boolean flag = false;
        if (NetWorkUtils.getNetworkType(context) != ConnectivityManager.TYPE_WIFI
                && SPHelper.getSizeLimitState(true)
                && fileSize > FileConstants.LIMIT_SIZE) {
            flag = true;
        }
        return flag;
    }

    /**
     * 检查文件(集合)大小
     *
     * @param fileList
     * @return
     */
    public static boolean checkLimit(Context context, List<File> fileList) {
        //非WiFi网络环境下检测下载文件大小
        long fileSize = 0L;
        boolean flag = false;
        if (fileList == null || fileList.size() <= 0) {
            return flag;
        }
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            if (!file.exists()) {
                continue;
            }
            try {
                fileSize = fileSize + file.length();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (NetWorkUtils.getNetworkType(context) != ConnectivityManager.TYPE_WIFI
                && SPHelper.getSizeLimitState(true)
                && fileSize > FileConstants.LIMIT_SIZE) {
            flag = true;
        }
        return flag;
    }

    /**
     * 检查文件下载大小限制
     *
     * @param file
     * @return true 限制,false 不限制
     */
    public static boolean checkLimit(Context context, File file) {

        //非WiFi网络环境下检测下载文件大小
        long fileSize = 0L;
        try {
            if (!file.exists()) {
                return false;
            }
            fileSize = file.length();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        boolean flag = false;
        if (
                NetWorkUtils.getNetworkType(context) != ConnectivityManager.TYPE_WIFI &&
                        SPHelper.getSizeLimitState(true) &&
                        fileSize > FileConstants.LIMIT_SIZE) {
            flag = true;
        }
        // TODO: 2018/9/5 内网测试,正式包此判断移除
        /*if (fileSize > FileConstants.LIMIT_SIZE) {
            return true;
        }*/
        return flag;

    }
}
