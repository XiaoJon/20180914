package com.hjhq.teamface.im.bean;

/**
 * Created by lx on 2017/6/29.
 */

public class ModHelperSettingRequestBean {


    /**
     * id : 1232432
     * itemId : 113
     * itemType : 1
     * assiatNotice : 1
     * assiatRead : 0
     */

    private long id;
    private long itemId;
    private int itemType;
    private int assiatNotice;
    private int assiatRead;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getAssiatNotice() {
        return assiatNotice;
    }

    public void setAssiatNotice(int assiatNotice) {
        this.assiatNotice = assiatNotice;
    }

    public int getAssiatRead() {
        return assiatRead;
    }

    public void setAssiatRead(int assiatRead) {
        this.assiatRead = assiatRead;
    }
}
