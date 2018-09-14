package com.hjhq.teamface.basis.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.provider.MyFileProvider;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
    String[] textTypes = {"text/*", "application/*"};
    String[] audioTypes = {"audio/*", "application/pdf"};
    String[] videoTypes = {"video/*", "application/pdf"};
    String[] imageTypes = {"image/*", "application/pdf"};

    public static final String DEF_FILEPATH = "/XhsEmoticonsKeyboard/Emoticons/";

    public static String getFolderPath(String folder) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + DEF_FILEPATH + folder;
    }

    public static void unzip(InputStream is, String dir) throws IOException {
        File dest = new File(dir);
        if (!dest.exists()) {
            dest.mkdirs();
        }

        if (!dest.isDirectory()) {
            throw new IOException("Invalid Unzip destination " + dest);
        }
        if (null == is) {
            throw new IOException("InputStream is null");
        }

        ZipInputStream zip = new ZipInputStream(is);

        ZipEntry ze;
        while ((ze = zip.getNextEntry()) != null) {
            final String path = dest.getAbsolutePath()
                    + File.separator + ze.getName();

            String zeName = ze.getName();
            char cTail = zeName.charAt(zeName.length() - 1);
            if (cTail == File.separatorChar) {
                File file = new File(path);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        throw new IOException("Unable to create folder " + file);
                    }
                }
                continue;
            }
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(path);
                byte[] bytes = new byte[1024];
                int c;
                while ((c = zip.read(bytes)) != -1) {
                    fout.write(bytes, 0, c);
                }
                zip.closeEntry();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fout != null) {

                    fout.close();
                }
            }


        }
    }

    public static String getFileSize(Number fileSize) {
        NumberFormat ddf1 = NumberFormat.getNumberInstance();
        //保留小数点后两位
        ddf1.setMaximumFractionDigits(2);
        double size = fileSize.doubleValue();
        String sizeDisplay;
        if (size > 1048576.0) {
            double result = size / 1048576.0;
            sizeDisplay = ddf1.format(result) + " MB";
        } else if (size > 1024) {
            double result = size / 1024;
            sizeDisplay = ddf1.format(result) + " KB";

        } else {
            sizeDisplay = ddf1.format(size) + " B";
        }
        return sizeDisplay;
    }

    public static Bitmap getImage(String srcPath, int sizeKB) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是1920*1080分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap, sizeKB);//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image, int sizeKb) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > sizeKb) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            if (options <= 0) {
                options = 10;
            }
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //压缩图片
    public static File getCompressedImage(Bitmap bitmap) throws Exception {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "//" + Constants.PATH_IMAGE,
                System.currentTimeMillis() + ".jpg");//将要保存图片的路径
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap = compressImage(bitmap, 100);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("IOException:保存图片失败");
        } finally {
            bos.close();
        }
        return file;
    }

    public static File getCompressedImage(String path, int sizeKb) throws Exception {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "//" + Constants.PATH_IMAGE,
                System.currentTimeMillis() + ".jpg");//将要保存图片的路径
        BufferedOutputStream bos = null;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap = compressImage(bitmap, sizeKb);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("IOException:保存图片失败");
        } finally {
            bos.close();
        }
        return file;
    }

    /**
     * 预览本地文件
     *
     * @param context
     * @param fileName
     * @param path
     */
    public static void browseDocument(Context context, String fileName, String path) {
        try {
            String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            //  String mimeType = mimeTypeMap.getMimeTypeFromExtension(ext);
            String mimeType = FileTypeUtils.getMimeType(new File(path));
            File file = new File(path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            LogUtil.e("FileUtils-文件名" + file.getAbsolutePath());
            LogUtil.e("FileUtils-mimeType==" + mimeType);
            if (Build.VERSION.SDK_INT <= 23) {//
                intent.setDataAndType(Uri.fromFile(file), mimeType);
            } else {//版本24及以上
                Uri uri = MyFileProvider.getUriForFile(context,
                        Constants.FILE_PROVIDER,//authorities,应该与Manifest文件中定义的相同
                        file);
                intent.setDataAndType(uri, mimeType);
                //读写权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();


            ToastUtils.showError(context, context.getResources().getString(R.string.file_not_support_hint));


        }
    }

    public static void browseDocument(Activity context, View root, String fileName, String path) {
        try {
            String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            // String mimeType = mimeTypeMap.getMimeTypeFromExtension(ext);
            String mimeType = FileTypeUtils.getMimeType(path);
            File file = new File(path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            LogUtil.e("FileUtils-文件名" + file.getAbsolutePath());
            LogUtil.e("FileUtils-mimeType==" + mimeType);
            if (Build.VERSION.SDK_INT <= 23) {//
                intent.setDataAndType(Uri.fromFile(file), mimeType);
            } else {//版本24及以上
                Uri uri = MyFileProvider.getUriForFile(context,
                        Constants.FILE_PROVIDER,//authorities,应该与Manifest文件中定义的相同
                        file);
                intent.setDataAndType(uri, mimeType);
                //读写权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();


            //  CommonUtil.showToast(context.getResources().getString(R.string.file_not_support_hint));
            String[] menu = {"文本", "音频", "视频", "图片", "其他"};
            PopUtils.showBottomMenu(context, root, "选择打开方式", menu, p -> {
                switch (p) {
                    case 0:
                        context.startActivity(getTextFileIntent(context, path, false));
                        break;
                    case 1:
                        context.startActivity(getAudioFileIntent(context, path));
                        break;
                    case 2:
                        context.startActivity(getVideoFileIntent(context, path));
                        break;
                    case 3:
                        context.startActivity(getImageFileIntent(context, path));
                        break;
                    case 4:
                        context.startActivity(getFileIntent(context, path));
                        break;
                    default:

                        break;


                }
                return true;
            });
        }
    }

    //android获取一个用于打开HTML文件的intent
    public static Intent getHtmlFileIntent(String param) {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(Context context, String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        //intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = MyFileProvider.getUriForFile(context,
                Constants.FILE_PROVIDER,//authorities,应该与Manifest文件中定义的相同
                new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //android获取一个用于打开文件的intent
    public static Intent getFileIntent(Context context, String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        // intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = MyFileProvider.getUriForFile(context,
                Constants.FILE_PROVIDER,//authorities,应该与Manifest文件中定义的相同
                new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        //intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    //android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(Context context, String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        // intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (paramBoolean) {
            uri = Uri.parse(param);

        } else {
            uri = MyFileProvider.getUriForFile(context,
                    Constants.FILE_PROVIDER,//authorities,应该与Manifest文件中定义的相同
                    new File(param));

        }
        intent.setData(uri);
        intent.setType("text/*");
        String[] mimeTypes = {"text/*", "application/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    //android获取一个用于打开音频文件的intent
    public static Intent getAudioFileIntent(Context context, String param) {
        /*Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = MyFileProvider.getUriForFile(context,
                Constants.FILE_PROVIDER,//authorities,应该与Manifest文件中定义的相同
                new File(param));
        intent.setDataAndType(uri, "audio*//*");*/
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = "audio/x-mpeg";
        String type2 = "audio/*";
        Uri uri = MyFileProvider.getUriForFile(context,
                Constants.FILE_PROVIDER,//authorities,应该与Manifest文件中定义的相同
                new File(param));
        Uri uri2 = new Uri.Builder().path("file://" + param).build();
        Uri uri3 = Uri.parse("file://" + param);
        //intent.setDataAndType(uri, type);
        intent.setDataAndType(uri, type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        return intent;
    }

    //android获取一个用于打开视频文件的intent
    public static Intent getVideoFileIntent(Context context, String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = MyFileProvider.getUriForFile(context,
                Constants.FILE_PROVIDER,//authorities,应该与Manifest文件中定义的相同
                new File(param));
        intent.setDataAndType(uri, "video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    //android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(Context context, String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(Context context, String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    //android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(Context context, String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    //android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(Context context, String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取文件夹大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    public static String getFileType(String filePath) {
        HashMap<String, String> mFileTypes = new HashMap<String, String>();
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E47", "png");
        mFileTypes.put("47494638", "gif");
        mFileTypes.put("49492A00", "tif");
        mFileTypes.put("424D", "bmp");
        return mFileTypes.get(getFileHeader(filePath));
    }

    public static String getFileHeader(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
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
        long fileSize;
        if (!file.exists()) {
            return false;
        }
        fileSize = file.length();
        return checkLimit(context, fileSize);
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
        if (CollectionUtils.isEmpty(fileList)) {
            return false;
        }
        for (File file : fileList) {
            if (!file.exists()) {
                continue;
            }
            fileSize = fileSize + file.length();
        }
        return checkLimit(context, fileSize);
    }

    /**
     * 检查文件下载大小限制
     *
     * @param file
     * @return true 限制,false 不限制
     */
    public static boolean checkLimit(Context context, File file) {
        //非WiFi网络环境下检测下载文件大小
        long fileSize;
        if (!file.exists()) {
            return false;
        }
        fileSize = file.length();
        return checkLimit(context, fileSize);
    }

    public static boolean checkLimit(Context context, long size) {
        //非WiFi网络环境下检测下载文件大小
        boolean flag = false;
        if (NetWorkUtils.getNetworkType(context) != ConnectivityManager.TYPE_WIFI &&
                SPHelper.getSizeLimitState(true) && size > FileConstants.LIMIT_SIZE) {
            flag = true;
        }
        return flag;
    }


    /**
     * 本地路径转换成uri
     *
     * @param context
     * @param filePath
     * @return
     */
    public static Uri getImageContentUri(Context context, String filePath) {
        return getImageContentUri(context, new File(filePath));
    }

    /**
     * 本地文件转换成uri
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * uri 转换成本地路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathFromContentUri(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 安装app
     *
     * @param context
     * @param packagename
     */
    public static void installApk(Context context, final String packagename, final String apkPath) {
        //首先判断系统中是否已经安装
        try {
            context.getPackageManager().getPackageInfo(packagename, 0);
            Log.d("Installed", "安装了");
        } catch (Exception e) {
            Log.d("Installed", "未安装");
            //apk存放的路径
            File file = new File(apkPath);
            if (!file.exists()) {
                return;
            }
            try {
                //打开管道，开启可读可写的权限
                Runtime.getRuntime().exec("adb shell mount -o remount /system");
                //执行强制安装的命令
                Process exec = Runtime.getRuntime().exec("adb install -r " + apkPath);
                Log.d("Installed", exec.waitFor() + "");
                //如果安装成功则删除存放的apk包,0表示成功
                if (exec.waitFor() == 0) {
                    file.delete();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 安装应用
     *
     * @param context
     * @param packagename
     * @param apkName
     */
    public void installApkAsSystemApp(Context context, final String packagename, final String apkName) {
        //首先判断系统中是否已经安装
        try {
            context.getPackageManager().getPackageInfo(packagename, 0);
            Log.d("Installed", "安装了");
        } catch (Exception e) {
            Log.d("Installed", "未安装");

            //apk存放的路径
            String apkPath = Environment.getExternalStorageDirectory().getPath() + File.separator + apkName;
            File file = new File(apkPath);
            if (!file.exists()) {
                return;
            }
            try {
                //打开管道，开启可读可写的权限
                Runtime.getRuntime().exec("adb shell mount -o remount /system");
                //执行拷贝安装的命令
                Process exec = Runtime.getRuntime().exec("adb push " + apkPath + " /system/app");
                Log.d("Installed", exec.waitFor() + "");
                //如果安装成功则删除存放的apk包,0表示成功
                if (exec.waitFor() == 0) {
                    file.delete();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

