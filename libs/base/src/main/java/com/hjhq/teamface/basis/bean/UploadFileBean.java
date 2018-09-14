package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * 文件上传实体
 *
 * @author Administrator
 * @date 2017/4/11
 */

public class UploadFileBean implements Serializable {

    /**
     * originalFileName : IMG20170303152612.jpg
     * fileName : IMG20170303152612.jpg
     * serialNumber : 1
     * fileSize : 1308690
     * fileUrl : http://192.168.1.172:9400/6/01b062297df0b6
     * fileType : jpg
     */

    protected String original_file_name;
    protected String file_name;
    protected String serial_number;
    protected String file_size;
    protected String upload_by;
    protected String upload_time;
    protected String file_url;
    protected String file_type;
    protected int voiceTime;


    public UploadFileBean() {
    }

    public UploadFileBean(String fileName, String fileUrl, String fileType) {
        this.file_name = fileName;
        this.file_url = fileUrl;
        this.file_type = fileType;
    }


    public String getOriginalFileName() {
        return original_file_name;
    }

    public void setOriginalFileName(String originalFileName) {
        this.original_file_name = originalFileName;
    }

    public String getFileName() {
        return file_name;
    }

    public void setFileName(String fileName) {
        this.file_name = fileName;
    }

    public String getSerialNumber() {
        return serial_number;
    }

    public void setSerialNumber(String serialNumber) {
        this.serial_number = serialNumber;
    }

    public String getFileSize() {
        return file_size;
    }

    public void setFileSize(String fileSize) {
        this.file_size = fileSize;
    }

    public String getFileUrl() {
        return file_url;
    }

    public void setFileUrl(String fileUrl) {
        this.file_url = fileUrl;
    }

    public String getFileType() {
        return file_type;
    }

    public void setFileType(String fileType) {
        this.file_type = fileType;
    }

    public String getUpload_by() {
        return upload_by;
    }

    public void setUpload_by(String upload_by) {
        this.upload_by = upload_by;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public int getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(int voiceTime) {
        this.voiceTime = voiceTime;
    }
}
