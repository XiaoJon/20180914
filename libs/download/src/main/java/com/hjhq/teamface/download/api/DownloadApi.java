package com.hjhq.teamface.download.api;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.network.body.FileResponseBody;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * @author Administrator
 *         上传下载接口
 */
public interface DownloadApi {


    /**
     * 上传附件
     *
     * @param fileList 文件集合
     * @return url
     */
    @Multipart
    @POST("common/file/upload")
    Observable<UpLoadFileResponseBean> commonUpload(@PartMap Map<String, RequestBody> fileList);


    /**
     * 初始化时使用
     *
     * @param fileList
     * @return
     */
    @Multipart
    @POST("common/file/imageUpload")
    Observable<UpLoadFileResponseBean> imageUpload(@PartMap Map<String, RequestBody> fileList);

    /**
     * 应用模块上传文件(文件)
     *
     * @param fileList
     * @return
     */
    @Multipart
    @POST("common/file/applyFileUpload")
    Observable<UpLoadFileResponseBean> applyFileUpload(@PartMap Map<String, RequestBody> fileList);

    /**
     * 上传文件库文件
     *
     * @param fileList
     * @return
     */
    @Multipart
    @POST("common/file/fileLibraryUpload")
    Observable<BaseBean> uploadNetdiskFile(@PartMap Map<String, RequestBody> fileList);

    /**
     * 上传新版本
     *
     * @param fileList
     * @return
     */
    @Multipart
    @POST("common/file/fileVersionUpload")
    Observable<BaseBean> fileVersionUpload(@PartMap Map<String, RequestBody> fileList);

    /**
     * 从文件库下载文件
     *
     * @param id
     * @return
     */
    @GET("library/file/download")
    Call<FileResponseBody> downloadFileFromNetDisk(@Query("id") String id);

    /**
     * 下载历史文件
     *
     * @param id
     * @return
     */
    @GET("library/file/downloadHistoryFile")
    Call<ResponseBody> downloadHistoryFile(@Query("id") String id);

    /**
     * 文件下载
     * http://192.168.1.58:8281/custom-gateway/common/file/download?bean=user1111&fileName=user1111/1514168698904.201712251025560.jpg
     *
     * @param url
     * @return
     */

    @GET("{url}")
    Observable<FileResponseBody> downloadFile(@Path("url") String url);

    @GET("{url}")
    Observable<FileResponseBody> downloadFile22(@Path("url") String url);


}
