package com.hjhq.teamface.basis.constants;

/**
 * @author Administrator
 * @date 2018/01/03
 * Describe：文件库常量类
 */

public interface FileConstants {
    //下载文件链接
    public static final String FILE_BASE_URL = Constants.BASE_URL + "library/file/download?id=";
    public static final String FILE_BASE_URL_NO_RECORD = Constants.BASE_URL + "library/file/downloadWithoutRecord?id=";
    public static final String FILE_BATCH_BASE_URL = Constants.BASE_URL + "library/file/batchDownload?id=";
    public static final String FILE_THUMB_BASE_URL = Constants.BASE_URL + "library/file/downloadCompressedPicture?id=";


    public static final String BEAN_NAME = "library";
    /**
     * 文件夹类型
     */
    public static final String FOLDER_TYPE = "folder_type";
    /**
     * 文件库的bean
     */
    public static final String FILE_LIBRARY_BEAN_NAME = "file_library";
    /**
     * 文件夹类型(公司文件,个人文件,app文件...)
     */
    public static final String FOLDER_STYLE = "folder_style";
    /**
     * 我的共享he与我共享使用
     */
    public static final String TABLE_ID = "table_id";
    /**
     * 文件夹层级
     */
    public static final String FOLDER_LEVEL = "folder_level";
    /**
     * 文件夹url
     */
    public static final String FOLDER_URL = "folder_url";
    /**
     * 从文件夹或搜索进入
     */
    public static final String FROM_FOLDER_OR_SEARCH = "from_folder_or_search";
    public static final int FROM_SEARCH = 1;
    public static final int FROM_FOLDER = 2;


    /**
     * 文件夹名字
     */
    public static final String FOLDER_NAME = "folder_name";
    /**
     * 文件夹ID
     */
    public static final String FOLDER_ID = "folder_id";
    /**
     * 文件或文件夹id
     */
    public static final String FILE_ID = "file_id";
    /**
     * 文件类型
     */
    public static final String FILE_TYPE = "file_type";
    /**
     * 应用文件夹使用
     */
    public static final String MODULE_ID = "module_id";
    /**
     *
     */
    public static final String PUBLIC_OR_PRIVATE = "public_or_private";
    /**
     * 移动或复制
     */
    public static final String MOVE_OR_COPY = "move_or_copy";
    /**
     * 文件夹导航栏数据
     */
    public static final String FOLDER_NAVI_DATA = "folder_navi_data";

    public static final int FOLDER = 0;
    public static final int FILE = 1;
    public static final int ALL = 2;
    public static final int MAX_IMAGE_FILE_SIZE = 20971520;


    public static final int MOVE = 1;
    public static final int COPY = 2;
    public static final int CONVERT = 3;
    //公开与私有文件夹
    public static final int PUBLIC_FOLDER = 0;
    public static final int PRIVATE_FOLDER = 1;


    public static final String IM_MANAGER = "1";
    public static final String IM_NOT_MANAGER = "0";


    /**
     * 移动复制文件成功
     */
    public static final String MOVE_OR_COPY_OK = "move_or_copy_ok";

    /**
     * 权限
     */
    public static final String AUTH_PREVIEW = "auth_preview";
    public static final String AUTH_UPLOAD = "auth_upload";
    public static final String AUTH_DOWNLOAD = "auth_download";
    /**
     * 删除文件成功,刷新列表
     */
    public static final String DELETE_FILE_SUCCESS = "delete_file_success";
    /**
     * 移动文件成功
     */
    public static final String MOVE_FILE_SUCCESS = "move_file_success";

    /**
     * 文件下载进度广播
     */
    public static final String FILE_DOWNLOAD_PROGRESS_ACTION = "file.download.progress.action";
    /**
     * 文件下载完成广播
     */
    public static final String FILE_DOWNLOAD_SUCCESS_ACTION = "file.download.success.action";
    /**
     * 是否是管理员
     */
    public static final String IS_MANAGER = "is_manager";

    /**
     * 非WiFi网络时文件上传下载限制大小值
     */
    public static long LIMIT_SIZE = 10 * 1024 * 1024;
}
