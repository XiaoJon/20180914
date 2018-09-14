package com.hjhq.teamface.filelib;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.FileResponseBody;
import com.hjhq.teamface.basis.bean.ViewDataAuthResBean;
import com.hjhq.teamface.common.bean.FriendsListBean;
import com.hjhq.teamface.filelib.bean.AddFolderReqBean;
import com.hjhq.teamface.filelib.bean.FileDetailBean;
import com.hjhq.teamface.filelib.bean.FileDownloadLogBean;
import com.hjhq.teamface.filelib.bean.FileListResBean;
import com.hjhq.teamface.filelib.bean.FileVersionLogBean;
import com.hjhq.teamface.filelib.bean.FolderAuthDetailBean;
import com.hjhq.teamface.filelib.bean.FolderParentResBean;
import com.hjhq.teamface.filelib.bean.ProjectListBean;
import com.hjhq.teamface.filelib.bean.RootFolderResBean;
import com.hjhq.teamface.filelib.bean.SearchFileListResBean;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 文件库接口
 */

public interface FilelibApiService {

    /**
     * 获取文件夹信息
     *
     * @param type
     * @param id
     * @return
     */
    @GET("fileLibrary/queryFolderDetail")
    Observable<FriendsListBean> queryFolderDetail(@Query("type") int type, @Query("id") long id);

    /**
     * 获取公司文件根目录列表
     *
     * @param style
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GET("fileLibrary/queryFileList")
    Observable<FileListResBean> queryFileList(
            @Query("style") String style,
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize,
            @Query("sign") int sign);

    /**
     * 子目录文件列表
     *
     * @param style
     * @param id
     * @return
     */
    @GET("fileLibrary/queryFilePartList")
    Observable<FileListResBean> queryFilePartList(
            @Query("style") String style,
            @Query("id") String id,
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize,
            @Query("sign") int sign);

    /**
     * 应用文件-根目录数据
     *
     * @param style
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GET("fileLibrary/queryAppFileList")
    Observable<FileListResBean> queryAppFileList(

            @Query("style") String style,
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize);

    /**
     * 应用文件-应用的模块列表
     *
     * @param style
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GET("fileLibrary/queryModuleFileList")
    Observable<FileListResBean> queryModuleFileList(
            @Query("id") String id,
            @Query("style") String style,
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize);

    /**
     * 应用文件-模块文件列表
     *
     * @param style
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GET("fileLibrary/queryModulePartFileList")
    Observable<FileListResBean> queryModulePartFileList(
            @Query("id") String id,
            @Query("style") String style,
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize);


    /**
     * 下载记录
     *
     * @param id
     * @return
     */
    @GET("fileLibrary/queryDownLoadList")
    Observable<FileDownloadLogBean> queryDownLoadList(@Query("id") String id);

    /**
     * 历史版本
     *
     * @param id
     * @return
     */
    @GET("fileLibrary/queryVersionList")
    Observable<FileVersionLogBean> queryVersionList(@Query("id") String id);

    /**
     * 文件详情
     *
     * @param id
     * @return
     */

    @GET("fileLibrary/queryFileLibarayDetail")
    Observable<FileDetailBean> queryFileLibarayDetail(@Query("id") String id);


    /**
     * 获取根目录信息
     *
     * @param style
     * @param id
     * @return
     */
    @GET("fileLibrary/queryFolderInitDetail")
    Observable<FolderAuthDetailBean> queryFolderInitDetail(@Query("style") int style, @Query("id") String id);

    /**
     * 下载文件夹
     *
     * @param id
     * @return
     */
    @GET("library/file/batchDownload")
    Observable<FileResponseBody> batchDownload(@Header("TOKEN") String token, @Query("id") long id);

    /**
     * 查询搜索
     *
     * @param style
     * @param content
     * @return
     */
    @GET("fileLibrary/blurSearchFile")
    Observable<SearchFileListResBean> blurSearchFile(
            @Query("style") String style,
            @Query("fileId") String fileId,
            @Query("content") String content);

    /**
     * 获取文件夹的父级目录信息
     *
     * @param id
     * @return
     */
    @GET("fileLibrary/getBlurResultParentInfo")
    Observable<FolderParentResBean> getBlurResultParentInfo(@Query("id") String id);

    /**
     * @return
     */
    @GET("fileLibrary/queryfileCatalog")
    Observable<RootFolderResBean> queryfileCatalog();

    /**
     * 文件库中的项目列表
     *
     * @return
     */
    @GET("projectLibrary/queryProjectLibraryList")
    Observable<ProjectListBean> queryProjectLibraryList(@Query("pageSize") int pageSize,
                                                        @Query("pageNum") int pageNum);

    /**
     * 文件库小助手数据查看权限查询
     *
     * @return
     */
    @GET("fileLibrary/queryAidePower")
    Observable<ViewDataAuthResBean> queryAidePower(@Query("id") String id, @Query("style") String style);

    /**
     * 下载文件
     *
     * @param token
     * @param id
     * @return
     */
    @GET("library/file/download")
    Observable<FileResponseBody> download(@Header("TOKEN") String token, @Query("id") String id);

    /**
     * @param token library/file/fileDownload
     * @param id
     * @return
     */
    @GET("library/file/fileDownload")
    Observable<FileResponseBody> fileDownload(@Header("TOKEN") String token, @Query("id") String id);

    @GET("library/file/download")
    Call<ResponseBody> download2(@Header("TOKEN") String token, @Query("id") String id);

    /**
     * 下载历史版本
     *
     * @param token
     * @param id
     * @return
     */
    @GET("library/file/downloadHistoryFile")
    Call<ResponseBody> downloadHistoryFile(@Header("TOKEN") String token, @Query("id") String id);


    /**
     * 复制文件
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/copyFileLibrary")
    Observable<BaseBean> copyFileLibrary(@Body Map<String, String> bean);

    /**
     * 点赞与取消点赞
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/whetherFabulous")
    Observable<BaseBean> whetherFabulous(@Body Map<String, String> bean);

    /**
     * 共享文件或文件夹
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/shareFileLibaray")
    Observable<BaseBean> shareFileLibaray(@Body Map<String, String> bean);

    /**
     * 取消共享
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/cancelShare")
    Observable<BaseBean> cancelShare(@Body Map<String, String> bean);


    /**
     * 退出共享
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/quitShare")
    Observable<BaseBean> quitShare(@Body Map<String, String> bean);

    /**
     * 创建文件夹
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/savaFileLibrary")
    Observable<BaseBean> savaFileLibrary(@Body AddFolderReqBean bean);

    /**
     * 删除文件
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/delFileLibrary")
    Observable<BaseBean> delFileLibrary(@Body Map<String, String> map);

    /**
     * 添加管理员
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/savaManageStaff")
    Observable<BaseBean> savaManageStaff(@Body Map<String, String> map);

    /**
     * 删除管理员
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/delManageStaff")
    Observable<BaseBean> delManageStaff(@Body Map<String, String> map);

    /**
     * 删除成员
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/delMember")
    Observable<BaseBean> delMember(@Body Map<String, String> map);

    /**
     * 添加成员
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/savaMember")
    Observable<BaseBean> savaMember(@Body Map<String, String> map);

    /**
     * 修改成员权限
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/updateSetting")
    Observable<BaseBean> updateSetting(@Body Map<String, Object> map);

    /**
     * 移动文件
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/shiftFileLibrary")
    Observable<BaseBean> shiftFileLibrary(@Body Map<String, String> bean);

    /**
     * 修改文件夹
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/editFolder")
    Observable<BaseBean> editFolder(@Body Map<String, String> bean);

    /**
     * 重命名
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("fileLibrary/editRename")
    Observable<BaseBean> editRename(@Body Map<String, String> bean);

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

}