package com.hjhq.teamface.statistic.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 * @date 2018/3/28
 */

public class ReportDataDetailResultBean extends BaseBean {

    private List<Map<String,Object>> data;

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
