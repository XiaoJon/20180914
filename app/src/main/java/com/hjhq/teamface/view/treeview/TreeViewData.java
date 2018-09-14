package com.hjhq.teamface.view.treeview;

/**
 * Created by Administrator on 2017/10/9.
 * Describe：
 */

public class TreeViewData {
    private boolean checked;
    private String itemName;
    private String id;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
