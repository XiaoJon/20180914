package com.hjhq.teamface.filelib.logic;

/**
 * Created by lx on 2017/3/21.
 */

public class FileLogic{}/*
extends BaseLogic {
    public static FileLogic getInstance() {
        return (FileLogic) SingleInstance.getInstance(FileLogic.class
                .getName());
    }

    private NetdiskApiService getApi() {
        return new ApiManager<NetdiskApiService>().getAPI(NetdiskApiService.class);
    }


    *//**
     * 获取人员
     *
     * @param mActivity
     * @param userName
     * @param s
     *//*
    *//*public void queryFileList(RxAppCompatActivity mActivity, String userName, Subscriber<EmployeeResultBean> s) {
        Observable observable = getApi().getEmployee(userName).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        toSubscribe(observable, s);
    }*//*


    *//**
     * 初始化登录后信息
     *//*
    *//*public void initUserInfo(BaseActivity activity) {
        Observable observable = getApi().queryInfo()
                .map(new HttpResultFunc<>())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY));

        toSubscribe(observable, new ProgressSubscriber<UserInfoBean>(activity) {
            @Override
            public void onNext(UserInfoBean userInfoBean) {
                super.onNext(userInfoBean);
                //保存当前公司id
                try {
                    IM.getInstance().initCompanyId(userInfoBean.getData().getCompanyInfo().getId());
                    IM.getInstance().initID(userInfoBean.getData().getEmployeeInfo().getId());
                    //此处登录恐MainActivity还未启动,改为在TeamMessageFragment登录
                    // IM.getInstance().login();
                    IM.getInstance().initName(userInfoBean.getData().getEmployeeInfo().getName());
                    IM.getInstance().initAvatar(userInfoBean.getData().getEmployeeInfo().getPicture());
//                    IM.getInstance().initCompanyId(userInfoBean.getData().getCompanyInfo().getId());
//                    IM.getInstance().initCompanyId(userInfoBean.getData().getCompanyInfo().getId());
                    HttpMethods.setCOMPANYID(userInfoBean.getData().getCompanyInfo().getId());
                    SPUtils.setLong(activity, SPUtils.COMPANY_ID, userInfoBean.getData().getCompanyInfo().getId());
                    SPUtils.setString(activity, SPUtils.LATELY_COMPANY_NAME, userInfoBean.getData().getCompanyInfo().getCompany_name());
                    //保存员工id
                    HttpMethods.setEMPLOYEEID(userInfoBean.getData().getEmployeeInfo().getId());
                    HttpMethods.setUSERID(userInfoBean.getData().getEmployeeInfo().getSign_id());
                    SPUtils.setLong(activity, SPUtils.EMPLOYEE_ID, userInfoBean.getData().getEmployeeInfo().getId());
                    SPUtils.setString(activity, SPUtils.USER_INFO, new Gson().toJson(userInfoBean));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    LogUtil.e("公司id为空");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    CommonUtil.showToast("初始公司信息失败!");
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                CommonUtil.showToast("初始化个人信息失败!");
            }
        });
    }*//*


    *//**
     * 获取根目录文件夹列表
     *
     * @param mActivity
     * @param type
     * @param pageNum
     * @param pageSize
     * @param s
     *//*
    public void queryFileList(BaseActivity mActivity,
                              @NonNull String style, int pageNum, int pageSize, int sign,
                              Subscriber<FileListResBean> s) {

        Observable observable = getApi()
                .queryFileList(style, pageNum, pageSize, sign)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 获取子目录文件列表
     *
     * @param mActivity
     * @param style
     * @param id
     * @param pageNum
     * @param pageSize
     * @param s
     *//*
    public void queryFilePartList(BaseActivity mActivity,
                                  @NonNull String style, @NonNull String id, int pageNum, int pageSize, int sign,
                                  Subscriber<FileListResBean> s) {

        Observable observable = getApi()
                .queryFilePartList(style, id, pageNum, pageSize, sign)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 应用文件-应用文件夹列表
     *
     * @param mActivity
     * @param style
     * @param pageNum
     * @param pageSize
     * @param s
     *//*
    public void queryAppFileList(BaseActivity mActivity,
                                 @NonNull String style, int pageNum, int pageSize,
                                 Subscriber<FileListResBean> s) {

        Observable observable = getApi()
                .queryAppFileList(style, pageNum, pageSize)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 应用文件-模块文件夹列表
     *
     * @param mActivity
     * @param style
     * @param id
     * @param pageNum
     * @param pageSize
     * @param sign
     * @param s
     *//*
    public void queryModuleFileList(BaseActivity mActivity, @NonNull String id,
                                    @NonNull String style, int pageNum, int pageSize,
                                    Subscriber<FileListResBean> s) {

        Observable observable = getApi()
                .queryModuleFileList(id, style, pageNum, pageSize)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 应用文件-模块文件列表
     *
     * @param mActivity
     * @param style
     * @param id
     * @param pageNum
     * @param pageSize
     * @param s
     *//*
    public void queryModulePartFileList(BaseActivity mActivity, @NonNull String id,
                                        @NonNull String style, int pageNum, int pageSize,
                                        Subscriber<FileListResBean> s) {

        Observable observable = getApi()
                .queryModulePartFileList(id, style, pageNum, pageSize)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));
        BaseLogic.toSubscribe(observable, s);
    }


    *//**
     * 文件详情
     *
     * @param mActivity
     * @param id
     * @param s
     *//*
    public void queryFileLibarayDetail(BaseActivity mActivity, @NonNull String id,
                                       Subscriber<FileDetailBean> s) {
//        Map<String, String> map = new HashMap<>();
//        map.put("id", id);
        Observable observable = getApi()
                .queryFileLibarayDetail(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 获取文件夹管理信息
     *
     * @param mActivity
     * @param style
     * @param id
     * @param s
     *//*
    public void queryFolderInitDetail(BaseActivity mActivity, @NonNull int style, @NonNull String id,
                                      Subscriber<FolderAuthDetailBean> s) {

        Observable observable = getApi()
                .queryFolderInitDetail(style, id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 历史版本
     *
     * @param mActivity
     * @param id
     * @param s
     *//*
    public void queryVersionList(BaseActivity mActivity, @NonNull String id,
                                 Subscriber<FileVersionLogBean> s) {

        Observable observable = getApi()
                .queryVersionList(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 文件库文件夹
     *
     * @param mActivity
     * @param s
     *//*
    public void queryfileCatalog(BaseActivity mActivity,
                                 Subscriber<RootFolderResBean> s) {

        Observable observable = getApi()
                .queryfileCatalog()
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 获取文件库项目文件项目列表
     *
     * @param mActivity
     * @param s
     *//*
    public void queryProjectLibraryList(BaseActivity mActivity, int pageSize, int pageNum,
                                        Subscriber<ProjectListBean> s) {

        Observable observable = getApi()
                .queryProjectLibraryList(pageSize, pageNum)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 文件库查看文件详情权限查询
     *
     * @param mActivity
     * @param id
     * @param style
     * @param s
     *//*
    public void queryAidePower(BaseActivity mActivity, String id, String style,
                               Subscriber<ViewDataAuthResBean> s) {

        Observable observable = getApi()
                .queryAidePower(id, style)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 下载记录
     *
     * @param mActivity
     * @param id
     * @param s
     *//*
    public void queryDownLoadList(BaseActivity mActivity, @NonNull String id,
                                  Subscriber<FileDownloadLogBean> s) {

        Observable observable = getApi()
                .queryDownLoadList(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 创建文件夹
     *
     * @param mActivity
     * @param bean
     * @param s
     *//*
    public void addFolder(BaseActivity mActivity,
                          @NonNull AddFolderReqBean bean,
                          Subscriber<BaseBean> s) {

        Observable observable = getApi()
                .savaFileLibrary(bean)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 删除文件或文件夹
     *
     * @param mActivity
     * @param id
     * @param s
     *//*
    public void delFileLibrary(BaseActivity mActivity,
                               @NonNull String id,
                               Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        Observable observable = getApi()
                .delFileLibrary(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 添加管理员
     *
     * @param mActivity
     * @param id
     * @param s
     *//*
    public void savaManageStaff(BaseActivity mActivity,
                                @NonNull String id, String manage_id, String file_leve,
                                Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("manage_id", manage_id);
        map.put("file_leve", file_leve);
        Observable observable = getApi()
                .savaManageStaff(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 删除管理员
     *
     * @param mActivity
     * @param id
     * @param manage_id
     * @param s
     *//*
    public void delManageStaff(BaseActivity mActivity,
                               @NonNull String id, String manage_id, String file_leve,
                               Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("manage_id", manage_id);
        map.put("file_leve", file_leve);
        Observable observable = getApi()
                .delManageStaff(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 删除成员
     *
     * @param mActivity
     * @param id
     * @param member_id
     * @param s
     *//*
    public void delMember(BaseActivity mActivity,
                          @NonNull String id, String member_id,
                          Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("member_id", member_id);
        Observable observable = getApi()
                .delMember(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 添加成员
     *
     * @param mActivity
     * @param id
     * @param member_id
     * @param s
     *//*
    public void savaMember(BaseActivity mActivity,
                           @NonNull String id, String member_id,
                           Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("member_id", member_id);
        Observable observable = getApi()
                .savaMember(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * @param mActivity
     * @param id
     * @param upload
     * @param download
     * @param s
     *//*
    public void updateSetting(BaseActivity mActivity, String folderId,
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
        Observable observable = getApi()
                .updateSetting(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 编辑文件夹
     *
     * @param mActivity
     * @param id
     * @param name
     * @param color
     * @param s
     *//*
    public void editFolder(BaseActivity mActivity,
                           @NonNull String id, String name, String color,
                           Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("color", color);
        Observable observable = getApi()
                .editFolder(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 重命名文件
     *
     * @param mActivity
     * @param id
     * @param name
     * @param s
     *//*
    public void renameFile(BaseActivity mActivity,
                           @NonNull String id, String name,
                           Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        Observable observable = getApi()
                .editRename(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 复制文件/文件夹
     *
     * @param mActivity
     * @param current_id 移动到的ID
     * @param worn_id    要移动的ID
     * @param s
     *//*
    public void copyFileLibrary(BaseActivity mActivity,
                                @NonNull String current_id, String worn_id, String style,
                                Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("current_id", current_id);
        map.put("worn_id", worn_id);
        map.put("style", style);
        Observable observable = getApi()
                .copyFileLibrary(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    public void blurSearchFile(BaseActivity mActivity,
                               @NonNull String style, String fileId, String content,
                               Subscriber<SearchFileListResBean> s) {
       *//* Map<String, String> map = new HashMap<>();
        map.put("style", style);
        map.put("content", content);*//*
        Observable observable = getApi()
                .blurSearchFile(style, fileId, content)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 获取文件夹上级目录信息
     *
     * @param mActivity
     * @param id
     * @param s
     *//*
    public void getBlurResultParentInfo(BaseActivity mActivity,
                                        @NonNull String id,
                                        Subscriber<FolderParentResBean> s) {
       *//* Map<String, String> map = new HashMap<>();
        map.put("style", style);
        map.put("content", content);*//*
        Observable observable = getApi()
                .getBlurResultParentInfo(id)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 取消共享
     *
     * @param mActivity
     * @param id
     * @param s
     *//*
    public void quitShare(BaseActivity mActivity,
                          @NonNull String id,
                          Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        Observable observable = getApi()
                .quitShare(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 取消共享
     *
     * @param mActivity
     * @param id
     * @param s
     *//*
    public void cancelShare(BaseActivity mActivity,
                            @NonNull String id,
                            Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        Observable observable = getApi()
                .cancelShare(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 移动文件或文件夹
     *
     * @param mActivity
     * @param current_id
     * @param worn_id
     * @param s
     *//*
    public void shiftFileLibrary(BaseActivity mActivity,
                                 @NonNull String current_id, String worn_id, String style,
                                 Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("current_id", current_id);
        map.put("worn_id", worn_id);
        map.put("style", style);
        Observable observable = getApi()
                .shiftFileLibrary(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 分享文件,文件夹
     *
     * @param mActivity
     * @param id
     * @param ids
     * @param s
     *//*
    public void shareFileLibaray(BaseActivity mActivity,
                                 @NonNull String id, String ids,
                                 Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("share_by", ids);
        Observable observable = getApi()
                .shareFileLibaray(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 点赞与取消点赞
     *
     * @param mActivity
     * @param id
     * @param status
     * @param s
     *//*
    public void whetherFabulous(BaseActivity mActivity,
                                @NonNull String id, String status,
                                Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        Observable observable = getApi()
                .whetherFabulous(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 上传网盘文件件
     *
     * @param mActivity
     * @param path
     * @param url
     * @param id
     * @param s
     *//*
    public void uploadNetDiskFile(BaseActivity mActivity,
                                  String path, String url, String id, int style, Subscriber<UpLoadFileResponseBean> s) {
        File file = new File(path);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Map<String, RequestBody> fileList = new HashMap<>();
        fileList.put("file\"; filename=\"" + file.getName(), requestBody1);
        RequestBody urlBody = RequestBody.create(MediaType.parse("multipart/form-data"), url);
        RequestBody idBody = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        RequestBody typeBody = RequestBody.create(MediaType.parse("multipart/form-data"), style + "");
        fileList.put("url", urlBody);
        fileList.put("id", idBody);
        fileList.put("style", typeBody);
        Observable observable = getApi()
                .uploadNetdiskFile(fileList)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        BaseLogic.toSubscribe(observable, s);
    }

    *//**
     * 上传文件
     *
     * @param mActivity
     * @param url
     * @param s
     *//*
    *//*public void uploadMp3File(BaseActivity mActivity,
                              String url, Subscriber<UpLoadFileResponseBean> s) {
        File file = new File(url);

        String mineType = FileTypeUtils.getMimeType(file);
        LogUtil.e(mineType);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse(mineType), file);
        Map<String, RequestBody> fileList = new HashMap<>();
        fileList.put("file\"; filename=\"" + file.getName(), requestBody1);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), "user111");
        fileList.put("bean", requestBody2);
        Observable observable = getApi()
                .commonUpload(fileList)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        toSubscribe(observable, s);
    }*//*

    *//*public void sendFileMessage(BaseActivity mActivity,
                           String url, Subscriber<UpLoadFileResponseBean> s) {
        File file = new File(url);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Map<String, RequestBody> fileList = new HashMap<>();
        fileList.put("file\"; filename=\"" + file.getAbsolutePath(), requestBody1);

        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), "user111");
        fileList.put("bean", requestBody2);
        Observable observable = getApi()
                .commonUpload(fileList)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY));

        toSubscribe(observable, s);
    }*//*


}
*/