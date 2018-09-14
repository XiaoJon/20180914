package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2018/01/03
 * Describe：文件件导航数据实体类
 */

public class FolderNaviData implements Serializable {
    String folderName;
    String folderId;
    int folderLevel;
    int floderType;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public int getFolderLevel() {
        return folderLevel;
    }

    public void setFolderLevel(int folderLevel) {
        this.folderLevel = folderLevel;
    }

    public int getFloderType() {
        return floderType;
    }

    public void setFloderType(int floderType) {
        this.floderType = floderType;
    }
}
