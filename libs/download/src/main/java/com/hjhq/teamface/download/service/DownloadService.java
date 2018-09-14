package com.hjhq.teamface.download.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.FileRequestBody;
import com.hjhq.teamface.basis.bean.ProgressBean;
import com.hjhq.teamface.basis.bean.QxMessage;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.callback.DownloadCallback;
import com.hjhq.teamface.basis.network.callback.UploadCallback;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.download.api.DownloadApi;
import com.hjhq.teamface.download.utils.FileTransmitUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author Administrator
 */
public class DownloadService extends Service {
    private static DownloadService mDownloadService;

    private static Map<String, Call<ResponseBody>> callMap = new HashMap<>();
    public static Map<Long, String> mGetFileIdMap = new HashMap<>();
    public static Map<String, Long> mGetDownloadIdMap = new HashMap<>();

    public static DownloadService getInstance() {

        if (downloadManager == null) {
            downloadManager = (DownloadManager) mDownloadService.getSystemService(Context.DOWNLOAD_SERVICE);

        }
        return mDownloadService;
    }


    private static String TOKEN = "";
    private static long COMPANYID;
    private static long USERID;
    private static long EMPLOYEEID;
    //0：其他1：Android客户端2：IOS客户端3:Windows客户端4：Mac客户端
    private static int CLIENT_FLAG = 1;

    @Override
    public void onCreate() {
        mDownloadService = this;
        initReceiver();

    }

    private MyReceiver mReceiver;

    /**
     * 注册广播,接收下载完成广播(下载完成后将下载请求移除)
     */
    private void initReceiver() {
        if (mReceiver == null) {
            mReceiver = new MyReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FileConstants.FILE_DOWNLOAD_SUCCESS_ACTION);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the mDownloadService.

        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void saveId(String fileId) {

    }

    public DownloadManager getDM() {

        return downloadManager;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public DownloadObserver getDownloadObserver() {
        return mDownloadObserver;
    }

    public Handler getHandler() {
        return myHandler;
    }

    public Map<String, Long> getIdMap() {
        return mGetDownloadIdMap;
    }

    public Map<Long, String> getFileIdMap() {
        return mGetFileIdMap;
    }

    public DownloadChangeObserver getDownloadChangeObserver() {
        return mDownloadChangeObserver;
    }

    /**
     * 下载完成将请求从Map中移除
     */
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ProgressBean bean = (ProgressBean) intent.getSerializableExtra(Constants.DATA_TAG1);
            callMap.remove(bean.getFileId());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            try {
                unregisterReceiver(mReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消下载
     *
     * @param fileId
     * @return
     */
    public static boolean cancelDownload(String fileId) {
        String s = mGetFileIdMap.get(fileId);
        long downloadId = TextUtil.parseLong(s);


        Call<ResponseBody> call = callMap.get(fileId);
        if (call != null) {
            call.cancel();
            return true;
        }
        return true;
    }

    /**
     * (上传文件后)发送文件消息
     *
     * @param localFilePath
     * @param bytes
     * @param message
     * @param callback
     */
    public static void sendFileMessage(String localFilePath, byte[] bytes, QxMessage message, DownloadCallback callback) {
        FileTransmitUtils.sendFileMessage2(localFilePath, bytes, message, callback);


    }

    /**
     * 上传头像(初始化时使用)
     *
     * @param file
     * @param s
     */
    public static void uploadAvatar(File file, Subscriber<UpLoadFileResponseBean> s) {
        DownloadCallback callback = new DownloadCallback() {
            @Override
            public void onSuccess(Call call, retrofit2.Response response) {

            }

            @Override
            public void onLoading(long total, long progress) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        };
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), "user111");
        FileRequestBody requestBody11 = new FileRequestBody(requestBody1, callback);
        Map<String, RequestBody> fileList = new HashMap<>();
        fileList.put("file\"; filename=\"" + file.getAbsolutePath(), requestBody11);
        fileList.put("bean", requestBody2);
        //添加请求头

        Observable<UpLoadFileResponseBean> observable = new ApiManager<DownloadApi>().getAPI(DownloadApi.class)
                .imageUpload(fileList)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFunc<UpLoadFileResponseBean>());

        observable.subscribe(s);

    }

    /**
     * 批量上传文件
     *
     * @param fileList
     * @param s
     */
    public void uploadFile(List<File> fileList, Subscriber<UpLoadFileResponseBean> s) {

        for (int i = 0; i < fileList.size(); i++) {
            boolean b = FileTransmitUtils.checkLimit(fileList.get(i));

        }
        DownloadCallback callback = new DownloadCallback() {
            @Override
            public void onSuccess(Call call, retrofit2.Response response) {

            }

            @Override
            public void onLoading(long total, long progress) {
                // TODO: 2018/3/9 发送进度广播

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        };
        Map<String, RequestBody> fileMap = new HashMap<>();
        RequestBody beanRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "user111");
        fileMap.put("bean", beanRequestBody);
        for (int i = 0; i < fileList.size(); i++) {
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), fileList.get(i));
            FileRequestBody requestBody11 = new FileRequestBody(requestBody1, callback);
            fileMap.put("file" + i + "\"; filename=\"" + fileList.get(i).getAbsolutePath(), requestBody11);
        }
        Observable<UpLoadFileResponseBean> observable = new ApiManager<DownloadApi>().getAPI(DownloadApi.class)
                .commonUpload(fileMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .map(new HttpResultFunc<UpLoadFileResponseBean>());

        observable.subscribe(s);

    }

    public void uploadFile2(List<File> fileList, Subscriber<UpLoadFileResponseBean> s) {
        // TODO: 2018/5/22 改造为支持大文件上传 
        /*File file = new File(path);
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("url", url);
        paramsMap.put("id", id);
        paramsMap.put("style", style + "");
        paramsMap.put("bean", FileConstants.FILE_LIBRARY_BEAN_NAME);
        headersMap.put("TOKEN", SPHelper.getToken());
        OkHttpUtils.post()
                .addFile("file\"; filename=\"", file.getName(), file)
                .url(Constants.BASE_URL + "common/file/upload")
                .params(paramsMap)
                .headers(headersMap)
                .build()
                .execute(callback);*/

    }


    /**
     * 下载历史版本
     *
     * @param id
     * @param fileName
     */
    public void downloadHistoryFile(String id, String fileName) {
        FileTransmitUtils.downloadHistoryFile(id, fileName);
    }

    private static int progress = 0;
    private static DownloadObserver mDownloadObserver;
    private static DownloadChangeObserver mDownloadChangeObserver;
    private static long downloadId = -1L;
    private static long downloadProgress = -1L;
    private static long time = -1L;
    static DownloadManager downloadManager;

    private static MyHandler myHandler = new MyHandler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            int[] bytesAndStatus = (int[]) msg.getData().getSerializable(Constants.DATA_TAG1);
            long downloadId = msg.getData().getLong(Constants.DATA_TAG2);
            try {
                progress = bytesAndStatus[0] * 100 / bytesAndStatus[1];
            } catch (Exception e) {
                e.printStackTrace();
                progress = 0;
            }
            LogUtil.e("DownloadService进度===" + progress);
            progressBean = new ProgressBean();
            progressBean.setFileId(mGetFileIdMap.get(downloadId));
            progressBean.setBytesRead(bytesAndStatus[0]);
            progressBean.setContentLength(bytesAndStatus[1]);
            progressBean.setStatus(bytesAndStatus[2]);
            progressBean.setDone(bytesAndStatus[2] == 8);
            if (bytesAndStatus[2] == 8) {
                ToastUtils.showSuccess(DownloadService.getInstance(), "下载完成");
            }
            Intent intent = new Intent();
            intent.setAction(FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION);
            intent.putExtra(Constants.DATA_TAG1, progressBean);
            getInstance().sendBroadcast(intent);
            // LocalBroadcastManager.getInstance(getInstance()).sendBroadcast(intent);
            if (progressBean.isDone()) {
                intent.setAction(FileConstants.FILE_DOWNLOAD_SUCCESS_ACTION);
                LocalBroadcastManager.getInstance(getInstance()).sendBroadcast(intent);
            }
            LogUtil.e("progressBean进度===" + new Gson().toJson(progressBean));

        }
    };

    private static ProgressBean progressBean = new ProgressBean();

    /**
     * 从url下载文件
     *
     * @param fileId
     * @param url
     * @param name
     */
    public long downloadFileFromUrl(String fileId, String url, String name) {
        return FileTransmitUtils.downloadFileFromUrl(fileId, url, name);
    }


    public long downloadFileFromUrl(String url, String name) {
        return FileTransmitUtils.downloadFileFromUrl(url, name);
    }

    public long downloadFileFromUrl(String url, String name, long size) {
        return FileTransmitUtils.downloadFileFromUrl(url, name, size);
    }

    /**
     * 支持文件夹下载
     *
     * @param id
     * @param name
     * @param fileOrfolder
     * @param downloadId
     * @param mHadler
     */
    public long downloadFileFromNetdisk(String id, String name, int fileOrfolder, long downloadId, Handler mHadler) {
        return FileTransmitUtils.downloadFileFromNetdisk(id, name, fileOrfolder, downloadId, mHadler);
    }


    private static class MyHandler extends Handler {

    }

    /**
     * 上传项目文库文件
     *
     * @param path
     * @param url
     * @param id
     * @param style
     * @param callback
     */
    public void uploadNetDiskFile2(String path, String url, String id, int style,
                                   UploadCallback<BaseBean> callback) {
        File file = new File(path);
        long timeOut = file.length();
        if (timeOut < 1000) {
            timeOut = 1000;
        }
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("url", url);
        paramsMap.put("id", id);
        paramsMap.put("style", style + "");
        paramsMap.put("bean", FileConstants.FILE_LIBRARY_BEAN_NAME);
        headersMap.put("TOKEN", SPHelper.getToken());
        OkHttpUtils.post()
                .addFile("file\"; filename=\"", file.getName(), file)
                .url(Constants.BASE_URL + "common/file/fileLibraryUpload")
                .params(paramsMap)
                .headers(headersMap)
                .build().connTimeOut(timeOut).writeTimeOut(timeOut).readTimeOut(timeOut)
                .execute(callback);
    }

    /**
     * 项目文库个人创建文件夹上传文件
     *
     * @param path
     * @param id
     * @param callback
     */
    public void uploadProjectPersonalFile(String path, String id, String projectId,
                                          UploadCallback<BaseBean> callback) {
        File file = new File(path);
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id", id);
        paramsMap.put("project_id", projectId);
        paramsMap.put("bean", ProjectConstants.PROJECT_BEAN_NAME);
        headersMap.put("TOKEN", SPHelper.getToken());
        OkHttpUtils.post()
                .addFile("file\"; filename=\"", file.getName(), file)
                .url(Constants.BASE_URL + "common/file/projectPersonalUpload")
                .params(paramsMap)
                .headers(headersMap)
                .build().connTimeOut(file.length()).writeTimeOut(file.length()).readTimeOut(file.length())
                .execute(callback);
    }

    /**
     * 项目文件上传(任务使用)
     *
     * @param path
     * @param type
     * @param parentId
     * @param dataId
     * @param callback
     */
    public void uploadProjectFile(String path, String type, String parentId, String dataId,
                                  UploadCallback<BaseBean> callback) {
        File file = new File(path);
        long timeOut = file.length();
        if (timeOut < 1000) {
            timeOut = 1000;
        }
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", type);
        paramsMap.put("parent_id", parentId);
        paramsMap.put("data_id", dataId);
        paramsMap.put("bean", ProjectConstants.PROJECT_BEAN_NAME);
        headersMap.put("TOKEN", SPHelper.getToken());
        OkHttpUtils.post()
                .addFile("file\"; filename=\"", file.getName(), file)
                .url(Constants.BASE_URL + "common/file/projectUpload")
                .params(paramsMap)
                .headers(headersMap)
                .build().connTimeOut(timeOut).writeTimeOut(timeOut).readTimeOut(timeOut)
                .execute(callback);
    }

    /**
     * 手动上传文件(手动上传使用)
     *
     * @param path
     * @param type
     * @param parentId
     * @param dataId
     * @param callback
     */
    public void uploadProjectFileManuel(String path, String type, String parentId, String dataId,
                                        UploadCallback<BaseBean> callback) {
        File file = new File(path);
        long timeOut = file.length();
        if (timeOut < 1000) {
            timeOut = 1000;
        }
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", type);
        paramsMap.put("parent_id", parentId);
        paramsMap.put("data_id", dataId);
        paramsMap.put("bean", ProjectConstants.PROJECT_BEAN_NAME);
        headersMap.put("TOKEN", SPHelper.getToken());
        OkHttpUtils.post()
                .addFile("file\"; filename=\"", file.getName(), file)
                .url(Constants.BASE_URL + "common/file/projectPersonalUpload")
                .params(paramsMap)
                .headers(headersMap)
                .build().connTimeOut(timeOut).writeTimeOut(timeOut).readTimeOut(timeOut)
                .execute(callback);
    }

    /**
     * 上传新版本文件
     *
     * @param path
     * @param url
     * @param id
     * @param style
     * @param callback
     */
    public void fileVersionUpload(String path, String url, String id, int style,
                                  UploadCallback callback) {
        File file = new File(path);
        long timeOut = file.length();
        if (timeOut < 1000) {
            timeOut = 1000;
        }
        Map<String, String> headersMap = new HashMap<>();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("url", url);
        paramsMap.put("id", id);
        paramsMap.put("style", style + "");
        headersMap.put("TOKEN", SPHelper.getToken());
        OkHttpUtils.post()
                .addFile("file\"; filename=\"", file.getName(), file)
                .url(Constants.BASE_URL + "common/file/fileVersionUpload")
                .params(paramsMap)
                .headers(headersMap)
                .build().connTimeOut(timeOut).writeTimeOut(timeOut).readTimeOut(timeOut)
                .execute(callback);
    }

    public void retrofitDownload(String url, String name) {
       /* LogUtil.e("文件url==" + url);
        LogUtil.e("文件名字==" + name);
        GlobeHttpHandler globeHttpHandler = new GlobeHttpHandler() {

            @Override
            public okhttp3.Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, okhttp3.Response response) {
                return response;
            }

            @Override
            public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
               *//* if (TextUtils.isEmpty(TOKEN) || "no".equals(request.header("Token-Type"))) {
                    return request;
                }*//*

                //加上统一的请求头
                request = request.newBuilder()
                        .header("TOKEN", SPHelper.getToken())
                        .header("CLIENT_FLAG", "" + CLIENT_FLAG)
                        .method(request.method(), request.body())
                        .build();
                return request;
            }

        };
        String baseUrl = "http://192.168.1.58:8281/custom-gateway/common/file/";
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl);
        OkHttpClient.Builder builder = ProgressHelper.addProgress(null);
        //手动创建一个OkHttpClient并设置超时时间
        builder.connectTimeout(1 * 60 * 1000, TimeUnit.SECONDS);
        //后台数据返回超时时间
        builder.readTimeout(1 * 60 * 1000, TimeUnit.SECONDS);
        //自定义拦截器，负责请求日志打印，请求头统一处理，请求结果统一处理等
        builder.addNetworkInterceptor(new RequestIntercept(globeHttpHandler));
        TeamMessageApiService retrofit = retrofitBuilder
                .client(builder.build())
                .build().create(TeamMessageApiService.class);

        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(String fileId, long bytesRead, long contentLength, boolean done) {
                Log.e("ds-bytesRead==", bytesRead + "");
                Log.e("ds-contentLength==", contentLength + "");
                Log.e("ds-done==", done + "");
            }
        });
        if (url.startsWith(baseUrl)) {
            url = url.replace(baseUrl, "");

        }
        LogUtil.e("截取后 " + url);
        Observable<FileResponseBody> call = retrofit
                .downloadFile22(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .map(new Func1<FileResponseBody, FileResponseBody>() {
                    @Override
                    public FileResponseBody call(FileResponseBody fileResponseBody) {
                        LogUtil.e("长度     " + fileResponseBody.contentLength());
                        return fileResponseBody;
                    }
                });
        call.subscribe(new ProgressSubscriber<FileResponseBody>(MyApplication.getInstance(), false) {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e("DownloadService onError");
                e.printStackTrace();
            }

            @Override
            public void onNext(FileResponseBody fileResponseBody) {
                LogUtil.e("DownloadService onNext");
                InputStream in = null;
                OutputStream out = null;
                try {
                    //多态应用，FileInputStream继承InputStream
                    in = fileResponseBody.byteStream();
                    //多态应用，FileOutputStream继承OutputStream
                    File file = new File(JYFileHelper.getFileDir(MyApplication.getInstance(), Constants.PATH_DOWNLOAD), name);
                    out = new FileOutputStream(file);
                    //申请1M内存，用于存放读入的数据，其实是作为缓冲（cache）
                    byte[] buf = new byte[1024 * 8];
                    LogUtil.e("长度     " + fileResponseBody.contentLength());
                    *//**
         * cread(buf))指将数据先读入buf内，
         * 当buf满时，跳出read方法，并返回buf的容量，
         * 然后赋值给n;当buf不满但已经读取完毕就返回buf的实际存放字节数
         * 涉及到不用资源时，先释放资源再赋值null这样使得垃圾回收更快
         **//*
                    int n;
                    while ((n = in.read(buf)) > 0) {
                        out.write(buf, 0, n);
                    }
                    out.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        *//**
         * close()方法本身就有可能抛出异常，故而用try catch 包裹;
         * 如果抛出异常in就不能正常关闭但是资源还被占用，故而在finally里 in=null;
         * 无论抛出异常与否都将in赋值null，这样有利于垃圾回收机制将其回收;我其实挺建议这样写
         * 涉及到不用资源时，先释放资源再赋值null这样使得垃圾回收更快
         **//*
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        in = null;
                        out = null;
                    }
                }
            }

            @Override
            public void onCancelProgress() {

            }
        });
       *//* call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    File file = new File(JYFileHelper.getFileDir(Constants.PATH_DOWNLOAD), name);
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
        });*/

    }
}
