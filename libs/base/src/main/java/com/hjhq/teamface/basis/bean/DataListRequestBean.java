package com.hjhq.teamface.basis.bean;

import java.util.Map;

/**
 * 数据列表请求
 * Created by lx on 2017/9/25.
 */

public class DataListRequestBean {
    private String bean;
    private String menuId;
    //模糊匹配
    private String fuzzyMatching;
    private String seas_pool_id;
    private Map<String, Object> queryWhere;
    private PageInfo pageInfo;
//       "beanType": 0,//在customer0: 全部客户，1：我负责的客户2：我参与的客户3：我部门的客户4：星标客户5：公海池客户


    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getFuzzyMatching() {
        return fuzzyMatching;
    }

    public void setFuzzyMatching(String fuzzyMatching) {
        this.fuzzyMatching = fuzzyMatching;
    }

    public Map<String, Object> getQueryWhere() {
        return queryWhere;
    }

    public void setQueryWhere(Map<String, Object> queryWhere) {
        this.queryWhere = queryWhere;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getSeas_pool_id() {
        return seas_pool_id;
    }

    public void setSeas_pool_id(String seas_pool_id) {
        this.seas_pool_id = seas_pool_id;
    }
}
