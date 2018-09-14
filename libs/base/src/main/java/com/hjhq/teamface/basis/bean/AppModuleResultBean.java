package com.hjhq.teamface.basis.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/9/9.
 */

public class AppModuleResultBean extends BaseBean implements Serializable {

    private ArrayList<AppModuleBean> data;

    public ArrayList<AppModuleBean> getData() {
        return data;
    }

    public void setData(ArrayList<AppModuleBean> data) {
        this.data = data;
    }
}
