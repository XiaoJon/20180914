package com.hjhq.teamface.download.utils;

import android.app.DownloadManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.bean.FileRequestBody;
import com.hjhq.teamface.basis.bean.ImDataBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.QxMessage;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.body.FileResponseBody;
import com.hjhq.teamface.basis.network.callback.DownloadCallback;
import com.hjhq.teamface.basis.network.callback.UploadCallback;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.util.BytesTransUtil;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.NetWorkUtils;
import com.hjhq.teamface.basis.util.ParseUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.download.api.DownloadApi;
import com.hjhq.teamface.download.service.DownloadChangeObserver;
import com.hjhq.teamface.download.service.DownloadObserver;
import com.hjhq.teamface.download.service.DownloadService;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 文件上传下载工具类
 *
 * @author Administrator
 */
public class FileTransmitUtils {
    static DownloadService mDownloadService;
    static DownloadObserver mDownloadObserver;
    static DownloadChangeObserver mDownloadChangeChangeObserver;
    static Handler mHandler;
    static Map<String, Long> mIdMap;
    static Map<Long, String> mGetFileIdMap;
    static DownloadManager mDownloadManager;
    static String TOKEN;

    static {
        mDownloadService = DownloadService.getInstance();
        mDownloadObserver = getDownloadObserver();
        mDownloadChangeChangeObserver = getDownloadChangeObserver();
        mHandler = getHandler();
        mIdMap = getIdMap();
        mGetFileIdMap = getFileIdMap();
        TOKEN = SPUtils.getString(mDownloadService, AppConst.TOKEN_KEY);
        mDownloadManager = getDownloadManager();
    }

    private static DownloadChangeObserver getDownloadChangeObserver() {
        return mDownloadService.getDownloadChangeObserver();

    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static long downloadFileFromUrl(String url, String name) {
        return downloadFileFromUrl("0", url, name);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static void downloadFileFromUrl(String url, String name, int size) {

        downloadFileFromUrl("0", url, name);
    }

    public static boolean checkLimit(long size) {
        //非WiFi网络环境下检测下载文件大小
        boolean flag = false;
        if (
                NetWorkUtils.getNetworkType(mDownloadService) != ConnectivityManager.TYPE_WIFI &&
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
    public static boolean checkLimit(String localFilePath) {
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
        if (NetWorkUtils.getNetworkType(mDownloadService) != ConnectivityManager.TYPE_WIFI
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
    public static boolean checkLimit(List<File> fileList) {
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
        if (NetWorkUtils.getNetworkType(mDownloadService) != ConnectivityManager.TYPE_WIFI
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
    public static boolean checkLimit(File file) {
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
                NetWorkUtils.getNetworkType(mDownloadService) != ConnectivityManager.TYPE_WIFI &&
                        SPHelper.getSizeLimitState(true) &&
                        fileSize > FileConstants.LIMIT_SIZE) {
            flag = true;
        }
        return flag;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static long downloadFileFromUrl(String url, String name, long size) {
        long id = 0L;
        if (!checkLimit(size)) {
            id = downloadFileFromUrl(url, name);
        }
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static long downloadFileFromUrl(String fileId, String url, String name) {
        if (TextUtils.isEmpty(url)) {
            return 0L;
        }
        //url="http://192.168.1.180:8080/custom-gateway/common/file/download?bean=bean1526451250363&fileName=1528946874173.download2018-06-12 下午.txt&fileSize=651";
        Log.e("fileurl=", url);
        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")
                && !url.toLowerCase().startsWith("ftp://") && !url.toLowerCase().startsWith("ftps://")) {
            ToastUtils.showToast(mDownloadService, "不支持当前下载地址!");
            return 0L;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));//fileUrl下载路径
        request.addRequestHeader("TOKEN", SPUtils.getString(mDownloadService, AppConst.TOKEN_KEY));
        //设置在什么网络状态下面能够下载软件
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //是否允许网络漫游
        request.setAllowedOverRoaming(true);
        //设置Notification的标题和描述
        request.setTitle(name);
        request.setDescription("正在下载");

        //设置Notification的显示，和隐藏。
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        File file = new File(JYFileHelper.getFileDir(mDownloadService, Constants.PATH_DOWNLOAD), fileId + name);
        if (file.exists()) {
            // TODO: 2018/3/7 产品帅胜余说直接覆盖掉(签字画押)
            file.delete();
            /*file = new File(JYFileHelper.getFileDir(MyApplication.getInstance(),
                    Constants.PATH_RECEIVE), DateTimeUtil.DateTimeToString(Calendar.getInstance().getTime()) + name);*/

        }
        //设置文件存放目录，filePath是保存文件的路径
        request.setDestinationUri(Uri.fromFile(file));

        //记录id用于取消下载
        //返回下载的Id，可以根据id查询下载的状态，下载进度
        long downloadId = mDownloadManager.enqueue(request);
        mIdMap.put(fileId, downloadId);
        mGetFileIdMap.put(downloadId, fileId);
        // mDownloadObserver = new DownloadObserver(mHandler, mDownloadService, downloadId);
        mDownloadChangeChangeObserver = new DownloadChangeObserver(mHandler, mDownloadManager, downloadId);
        mDownloadService.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, mDownloadChangeChangeObserver);
        return downloadId;
    }

    public static void sendFileMessage(String localFilePath,
                                       final byte[] bytes,
                                       final QxMessage message,
                                       DownloadCallback callback) {
        File file = new File(localFilePath);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), "bean1515808869861");
        FileRequestBody requestBody11 = new FileRequestBody(requestBody1, callback);
        Map<String, RequestBody> fileList = new HashMap<>();
        fileList.put("file\"; filename=\"" + file.getAbsolutePath(), requestBody11);
        fileList.put("bean", requestBody2);
        //添加请求头
        Observable<UpLoadFileResponseBean> observable =
                new ApiManager<DownloadApi>().getAPI(DownloadApi.class)
                        .commonUpload(fileList)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        //.compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                        .map(new HttpResultFunc<UpLoadFileResponseBean>());

        observable.subscribe(new Subscriber<UpLoadFileResponseBean>() {
            @Override
            public void onStart() {
                LogUtil.e("DownloadService   onStart");
            }

            @Override
            public void onCompleted() {
                LogUtil.e("DownloadService   onCompleted");
            }

            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                LogUtil.e("DownloadService   onError");
                LogUtil.e("DownloadService   onError" + e.getMessage() + "\n" + e.getLocalizedMessage());
                //文件上传失败
                // IM.getInstance().sendFileMessageFailed(bytes, message);
                ImDataBean bean = new ImDataBean();
                bean.setBytes(bytes);
                bean.setMessage(message);
                EventBusUtils.sendEvent(new MessageBean(MsgConstant.SEND_FAILURE, MsgConstant.SEND_FILE_MESSAGE_FAILED, bean));
            }

            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onNext(UpLoadFileResponseBean o) {
                List<UploadFileBean> uploadFileBeanList = o.getData();
                if (!CollectionUtils.isEmpty(uploadFileBeanList)) {
                    message.setFileUrl(uploadFileBeanList.get(0).getFileUrl());
                    String fileSize = uploadFileBeanList.get(0).getFileSize();
                    message.setFileSize(TextUtil.parseInt(fileSize));
                    message.setFileType(uploadFileBeanList.get(0).getFileType());
                    // IM.getInstance().sendMessage(bytes, message);
                    ImDataBean bean = new ImDataBean();
                    bean.setBytes(bytes);
                    bean.setMessage(message);
                    EventBusUtils.sendEvent(new MessageBean(MsgConstant.SENDING, MsgConstant.SEND_UPLOADED_FILE_MESSAGE, bean));
                } else {
                    // IM.getInstance().sendFileMessageFailed(bytes, message);
                    ImDataBean bean = new ImDataBean();
                    bean.setBytes(bytes);
                    bean.setMessage(message);
                    EventBusUtils.sendEvent(new MessageBean(MsgConstant.SEND_FAILURE, MsgConstant.SEND_FILE_MESSAGE_FAILED, bean));
                }
            }

        });

    }

    public static void sendFileMessage2(String localFilePath,
                                        final byte[] bytes,
                                        final QxMessage message,
                                        final DownloadCallback callback) {
        UploadCallback<UpLoadFileResponseBean> callback2 = new UploadCallback<UpLoadFileResponseBean>() {
            @Override
            public UpLoadFileResponseBean parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                UpLoadFileResponseBean user = new Gson().fromJson(string, UpLoadFileResponseBean.class);
                return user;
            }

            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                //文件上传失败
                // IM.getInstance().sendFileMessageFailed(bytes, message);
                ImDataBean bean = new ImDataBean();
                bean.setBytes(bytes);
                bean.setMessage(message);
                EventBusUtils.sendEvent(new MessageBean(MsgConstant.SEND_FAILURE, MsgConstant.SEND_FILE_MESSAGE_FAILED, bean));
            }

            @Override
            public void onResponse(UpLoadFileResponseBean o, int id) {
                List<UploadFileBean> uploadFileBeanList = o.getData();
                if (!CollectionUtils.isEmpty(uploadFileBeanList)) {
                    long clientTime = ParseUtil.bytes2Long(Arrays.copyOfRange(bytes, 37, 45));
                    int rand = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(bytes, 45, 49));
                    message.setMsgId(clientTime + "" + rand);
                    message.setFileUrl(uploadFileBeanList.get(0).getFileUrl());
                    String fileSize = uploadFileBeanList.get(0).getFileSize();
                    message.setFileSize(TextUtil.parseInt(fileSize));
                    message.setFileType(uploadFileBeanList.get(0).getFileType());
                    // IM.getInstance().sendMessage(bytes, message);
                    ImDataBean bean = new ImDataBean();
                    bean.setBytes(bytes);
                    bean.setMessage(message);
                    int code = 0;
                    if (TextUtils.isEmpty(message.getAllPeoples())) {
                        code = MsgConstant.IM_PERSONAL_CHAT_CMD;
                    } else {
                        code = MsgConstant.IM_TEAM_CHAT_CMD;
                    }
                    EventBusUtils.sendEvent(new MessageBean(code, MsgConstant.SEND_UPLOADED_FILE_MESSAGE, bean));
                } else {
                    // IM.getInstance().sendFileMessageFailed(bytes, message);
                    ImDataBean bean = new ImDataBean();
                    bean.setBytes(bytes);
                    bean.setMessage(message);
                    EventBusUtils.sendEvent(new MessageBean(MsgConstant.SEND_FAILURE, MsgConstant.SEND_FILE_MESSAGE_FAILED, bean));
                }
            }
        };
        File file = new File(localFilePath);
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bean", FileConstants.FILE_LIBRARY_BEAN_NAME);
        headersMap.put("TOKEN", SPHelper.getToken());
        OkHttpUtils.post()
                .addFile("file\"; filename=\"", file.getName(), file)
                .url(Constants.BASE_URL + "common/file/upload")
                .params(paramsMap)
                .headers(headersMap)
                .build()
                .execute(callback2);
    }

    /**
     * 从文件库下载文件
     *
     * @param id
     * @param name
     * @param fileOrfolder
     * @param downloadId
     * @param mHadler
     */

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static long downloadFileFromNetdisk(String id, String name, int fileOrfolder, long downloadId, Handler mHadler) {
        String url = "";
        if (fileOrfolder == FileConstants.FOLDER) {
            //文件夹
            url = FileConstants.FILE_BATCH_BASE_URL + id;
        } else if (fileOrfolder == FileConstants.FILE) {
            //文件
            url = FileConstants.FILE_BASE_URL + id;
        }

        LogUtil.e("文件名=" + name);
        LogUtil.e("文件url=" + url);
        if (TextUtils.isEmpty(url)) {
            return 0L;
        }
        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
            return 0L;
        }
        url = "https://www.phaseone.com/~/media/NEW_WEB/P1000/Gallery/Alexia/Phase-one-100MP-alexia-sinclair-image1.ashx";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));//fileUrl下载路径
        request.addRequestHeader("TOKEN", SPUtils.getString(mDownloadService, AppConst.TOKEN_KEY));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);//设置在什么网络状态下面能够下载软件
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedOverRoaming(true);//是否允许网络漫游
        //设置Notification的标题和描述
        request.setTitle("标题");
        request.setDescription("描述");
        //设置Notification的显示，和隐藏。
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        if (fileOrfolder == 0) {
            name = id + name + ".zip";
        }
        File file = new File(JYFileHelper.getFileDir(mDownloadService, Constants.PATH_DOWNLOAD), id + name);
        if (file.exists()) {
            file.delete();
        }
        LogUtil.e("文件名2=" + name);
        LogUtil.e("文件url2=" + url);
        request.setDestinationUri(Uri.fromFile(file));//设置文件存放目录，filePath是保存文件的路径
        downloadId = mDownloadManager.enqueue(request);//返回下载的Id，可以根据id查询下载的状态，下载进度
        mDownloadObserver = new DownloadObserver(mHadler, mDownloadService, downloadId);
        LogUtil.e("文件名3=" + name);
        LogUtil.e("文件url3=" + url);
        mDownloadService.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, mDownloadObserver);
        return downloadId;
    }

    /**
     * 文件库文件下载
     *
     * @param id
     * @param fileName
     */
    public static void downloadFileFromNetdisk(final String id, final String fileName) {


        Call<FileResponseBody> call = new ApiManager<DownloadApi>()
                .getAPI(DownloadApi.class)
                .downloadFileFromNetDisk(id);

        call.enqueue(new Callback<FileResponseBody>() {

            @Override
            public void onResponse(Call<FileResponseBody> call, retrofit2.Response<FileResponseBody> response) {
                try {
                    InputStream is;
                    if (response == null || response.body() == null) {
                        return;
                    }
                    is = response.body().byteStream();

                    File file = new File(JYFileHelper.getFileDir(mDownloadService, Constants.PATH_DOWNLOAD).getAbsolutePath(), id + fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FileResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * 文件库下载历史文件
     *
     * @param id
     * @param fileName
     */
    public static void downloadHistoryFile(final String id, final String fileName) {

        Call<ResponseBody> call = new ApiManager<DownloadApi>()
                .getAPI(DownloadApi.class)
                .downloadHistoryFile(id);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    InputStream is;
                    if (response == null || response.body() == null) {
                        return;
                    }
                    is = response.body().byteStream();

                    File file = new File(JYFileHelper.getFileDir(mDownloadService, Constants.PATH_DOWNLOAD).getAbsolutePath(), "his-" + id + fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private static Map<String, Long> getIdMap() {

        return mDownloadService.getIdMap();
    }

    private static Map<Long, String> getFileIdMap() {

        return mDownloadService.getFileIdMap();
    }

    private static DownloadManager getDownloadManager() {
        return mDownloadService.getDM();

    }

    public static void cancelDownload(String fileId) {
        mDownloadManager.remove(mIdMap.get(fileId));
    }

    /**
     * 取消下载
     *
     * @param downloadId
     */
    public static void cancelDownload(long downloadId) {
        mDownloadManager.remove(downloadId);

    }

    private long getDownloadId() {
        return mDownloadService.getDownloadId();

    }

    private static DownloadObserver getDownloadObserver() {
        return mDownloadService.getDownloadObserver();

    }

    private static Handler getHandler() {
        return mDownloadService.getHandler();

    }


}