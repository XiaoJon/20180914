package com.hjhq.teamface.filelib;

import android.support.annotation.NonNull;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/9/28.
 * Describe：
 */

public class FilelibModel implements IModel<FilelibApiService> {

    @Override
    public FilelibApiService getApi() {
        return new ApiManager<FilelibApiService>().getAPI(FilelibApiService.class);
    }

    /**
     * 获取子目录文件列表
     *
     * @param mActivity
     * @param s
     */
    public void queryFilePartList(ActivityPresenter mActivity, @NonNull String style, @NonNull String id, int pageNum, int pageSize, int sign,
                                  Subscriber<FileListResBean> s) {
        getApi().queryFilePartList(style, id, pageNum, pageSize, sign).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取根目录文件夹列表
     *
     * @param mActivity
     * @param style
     * @param pageNum
     * @param pageSize
     * @param sign
     * @param s
     */
    public void queryFileList(ActivityPresenter mActivity, @NonNull String style, int pageNum, int pageSize, int sign,
                              Subscriber<FileListResBean> s) {
        getApi().queryFileList(style, pageNum, pageSize, sign).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 移动文件或文件夹
     *
     * @param mActivity
     * @param current_id
     * @param worn_id
     * @param style
     * @param s
     */
    public void shiftFileLibrary(ActivityPresenter mActivity,
                                 @NonNull String current_id, String worn_id, String style,
                                 Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("current_id", current_id);
        map.put("worn_id", worn_id);
        map.put("style", style);
        getApi().shiftFileLibrary(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 复制文件/文件夹
     *
     * @param mActivity
     * @param current_id
     * @param worn_id
     * @param style
     * @param s
     */
    public void copyFileLibrary(ActivityPresenter mActivity,
                                @NonNull String current_id, String worn_id, String style,
                                Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("current_id", current_id);
        map.put("worn_id", worn_id);
        map.put("style", style);
        getApi().copyFileLibrary(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 文件库根目录
     *
     * @param mActivity
     * @param s
     */
    public void queryfileCatalog(ActivityPresenter mActivity, Subscriber<RootFolderResBean> s) {
        getApi().queryfileCatalog().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 创建文件夹
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addFolder(ActivityPresenter mActivity,
                          @NonNull AddFolderReqBean bean,
                          Subscriber<BaseBean> s) {
        getApi().savaFileLibrary(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取文件夹管理信息
     *
     * @param mActivity
     * @param style
     * @param id
     * @param s
     */
    public void queryFolderInitDetail(ActivityPresenter mActivity, @NonNull int style, @NonNull String id,
                                      Subscriber<FolderAuthDetailBean> s) {
        getApi().queryFolderInitDetail(style, id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 编辑文件夹
     *
     * @param mActivity
     * @param id
     * @param name
     * @param color
     * @param s
     */
    public void editFolder(ActivityPresenter mActivity,
                           @NonNull String id, String name, String color,
                           Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("color", color);
        getApi().editFolder(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取文件夹上级目录信息
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void getBlurResultParentInfo(ActivityPresenter mActivity, @NonNull String id,
                                        Subscriber<FolderParentResBean> s) {

        getApi().getBlurResultParentInfo(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 应用文件-应用文件夹列表
     *
     * @param mActivity
     * @param style
     * @param pageNum
     * @param pageSize
     * @param s
     */
    public void queryAppFileList(ActivityPresenter mActivity,
                                 @NonNull String style, int pageNum, int pageSize,
                                 Subscriber<FileListResBean> s) {

        getApi().queryAppFileList(style, pageNum, pageSize).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 应用文件-模块文件夹列表
     *
     * @param mActivity
     * @param id
     * @param style
     * @param pageNum
     * @param pageSize
     * @param s
     */
    public void queryModuleFileList(ActivityPresenter mActivity, @NonNull String id,
                                    @NonNull String style, int pageNum, int pageSize,
                                    Subscriber<FileListResBean> s) {

        getApi().queryModuleFileList(id, style, pageNum, pageSize).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 应用文件-模块文件列表
     *
     * @param mActivity
     * @param id
     * @param style
     * @param pageNum
     * @param pageSize
     * @param s
     */
    public void queryModulePartFileList(ActivityPresenter mActivity, @NonNull String id,
                                        @NonNull String style, int pageNum, int pageSize,
                                        Subscriber<FileListResBean> s) {

        getApi().queryModulePartFileList(id, style, pageNum, pageSize)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除文件或文件夹
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void delFileLibrary(ActivityPresenter mActivity, @NonNull String id,
                               Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        getApi().delFileLibrary(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 取消共享
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void cancelShare(ActivityPresenter mActivity, @NonNull String id,
                            Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        getApi().cancelShare(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 退出共享
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void quitShare(ActivityPresenter mActivity, @NonNull String id,
                          Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        getApi().quitShare(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 分享文件
     *
     * @param mActivity
     * @param id
     * @param ids
     * @param s
     */
    public void shareFileLibaray(ActivityPresenter mActivity,
                                 @NonNull String id, String ids,
                                 Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("share_by", ids);
        getApi().shareFileLibaray(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 文件详情
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void queryFileLibarayDetail(ActivityPresenter mActivity,
                                       @NonNull String id,
                                       Subscriber<FileDetailBean> s) {

        getApi().queryFileLibarayDetail(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 点赞与取消点赞
     *
     * @param mActivity
     * @param id
     * @param status
     * @param s
     */
    public void whetherFabulous(ActivityPresenter mActivity,
                                @NonNull String id, String status,
                                Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        getApi().whetherFabulous(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 重命名文件
     *
     * @param mActivity
     * @param id
     * @param name
     * @param s
     */
    public void renameFile(ActivityPresenter mActivity,
                           @NonNull String id, String name,
                           Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        getApi().editRename(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 下载记录
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void queryDownLoadList(ActivityPresenter mActivity,
                                  @NonNull String id,
                                  Subscriber<FileDownloadLogBean> s) {

        getApi().queryDownLoadList(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 添加管理员
     *
     * @param mActivity
     * @param id
     * @param manage_id
     * @param file_leve
     * @param s
     */
    public void savaManageStaff(ActivityPresenter mActivity,
                                @NonNull String id, String manage_id, String file_leve,
                                Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("manage_id", manage_id);
        map.put("file_leve", file_leve);
        getApi().savaManageStaff(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 添加成员
     *
     * @param mActivity
     * @param id
     * @param member_id
     * @param s
     */
    public void savaMember(ActivityPresenter mActivity,
                           @NonNull String id, String member_id,
                           Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("member_id", member_id);
        getApi().savaMember(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除管理员
     *
     * @param mActivity
     * @param id
     * @param manage_id
     * @param file_leve
     * @param s
     */
    public void delManageStaff(ActivityPresenter mActivity,
                               @NonNull String id, String manage_id, String file_leve,
                               Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("manage_id", manage_id);
        map.put("file_leve", file_leve);
        getApi().delManageStaff(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除成员
     *
     * @param mActivity
     * @param id
     * @param member_id
     * @param s
     */
    public void delMember(ActivityPresenter mActivity,
                          @NonNull String id, String member_id,
                          Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("member_id", member_id);
        getApi().delMember(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 历史版本
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void queryVersionList(ActivityPresenter mActivity, @NonNull String id,
                                 Subscriber<FileVersionLogBean> s) {

        getApi().queryVersionList(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取文件库项目文件项目列表
     *
     * @param mActivity
     * @param pageSize
     * @param pageNum
     * @param s
     */
    public void queryProjectLibraryList(ActivityPresenter mActivity, int pageSize, int pageNum,
                                        Subscriber<ProjectListBean> s) {

        getApi().queryProjectLibraryList(pageSize, pageNum)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * @param mActivity
     * @param folderId
     * @param id
     * @param upload
     * @param download
     * @param s
     */
    public void updateSetting(ActivityPresenter mActivity, String folderId,
                              @NonNull String id, String upload, String download,
                              Subscriber<BaseBean> s) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> authMap = new HashMap<>();
        authMap.put("id", id);
        authMap.put("upload", upload);
        authMap.put("download", download);
        authMap.put("preview", "1");
        list.add(authMap);
        map.put("data", list);
        map.put("id", folderId);
        getApi().updateSetting(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 文件模糊搜索
     *
     * @param mActivity
     * @param style
     * @param fileId
     * @param content
     * @param s
     */
    public void blurSearchFile(ActivityPresenter mActivity, @NonNull String style, String fileId, String content,
                               Subscriber<SearchFileListResBean> s) {

        getApi().blurSearchFile(style, fileId, content)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }
}
