package com.hjhq.teamface.basis.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 详情返回Bean
 *
 * @author lx
 * @date 2017/9/19
 */

public class DetailResultBean extends BaseBean implements Serializable {

    /**
     * data : {"customerAddress":"140000,140300,140303","customerLeve":"[{\"color\":\"#fff\",\"label\":\"中型客户\",\"value\":\"2\"}]","publicPool_startTime":"","companyTel":"354845345","remark":"倒萨发送到饭店符合法规和规范化斯蒂芬斯蒂芬","dealStatus":"[{\"color\":\"#fff\",\"label\":\"未成交\",\"value\":\"1\"}]","companyUrl":"http://www.dsfdsf.com","customerName":"帅剩余","principal":"[{\"employeeName\":\"李萌\",\"id\":3489871441805312,\"$$hashKey\":274}]","activityId":"","companyEmail":"35468154@qq.com","detailedAddress":"广东省但是法国","customerTrade":"[{\"color\":\"#fff\",\"label\":\"教育\",\"value\":\"1\"}]","commonCustomerPond":"[{\"color\":\"#fff\",\"label\":\"公用公海池\",\"value\":\"0\"}]","id":"3","customerSource":"[{\"color\":\"#fff\",\"label\":\"客户推荐\",\"value\":\"2\"}]"}
     */

    private Map<String, Object> data;

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

}
