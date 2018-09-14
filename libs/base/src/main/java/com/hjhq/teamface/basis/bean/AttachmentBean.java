package com.hjhq.teamface.basis.bean;

import android.support.annotation.NonNull;

import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.util.FileTypeUtils;

import java.io.File;
import java.io.Serializable;

public class AttachmentBean implements Serializable {
    /**
     * fileName :
     * fileType : jpg
     * fileSize : 12323
     * fileUrl : http://234324.dd/fd.jpg
     */

    private String file_name;
    private String file_type;
    private String file_size;
    private String file_url;
    private int fromWhere;

    public AttachmentBean() {
    }

    public AttachmentBean(@NonNull String pathname) {
        this.file_type = pathname;
        File file = new File(pathname);
        if (file.exists()) {
            file_type = FileTypeUtils.getExtensionName(file.getName());
            file_name = file.getName();
            file_size = file.length() + "";
            setFromWhere(EmailConstant.FROM_LOCAL_FILE);
        }
    }

    public AttachmentBean(@NonNull File file) {
        setFromWhere(EmailConstant.FROM_LOCAL_FILE);
        if (file.exists()) {
            file_url = file.getAbsolutePath();
            file_type = FileTypeUtils.getExtensionName(file.getName());
            file_name = file.getName();
            file_size = file.length() + "";
            setFromWhere(EmailConstant.FROM_LOCAL_FILE);
        }
    }



    public int getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(int fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getFileName() {
        return file_name;
    }

    public void setFileName(String fileName) {
        this.file_name = fileName;
    }

    public String getFileType() {
        return file_type;
    }

    public void setFileType(String fileType) {
        this.file_type = fileType;
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
}