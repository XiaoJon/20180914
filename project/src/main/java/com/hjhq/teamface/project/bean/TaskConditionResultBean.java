package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/17.
 */

public class TaskConditionResultBean extends BaseBean {

    /**
     * data : {"condition":[{"field":"datetime_deadline","label":"截止日期","entity":[{"colour":"#66C060","name":"低风险","id":27},{"colour":"#3057E5","name":"一般","id":3},{"colour":"#FF1D32","name":"Red","id":15},{"colour":"","name":"风险","id":24},{"colour":"#19BFA4","name":"长这个自很长这个自很长这个自很长这个自很长这个自很长这个自很长这个自很长这个自很长","id":28},{"colour":"#FF1D32","name":"紧急","id":4},{"colour":"#000000","name":"Black","id":16},{"colour":"#5821A7","name":"Purple","id":20},{"colour":"","name":"颜色","id":14},{"colour":"#FF7748","name":"中风险","id":26},{"colour":"#73B32D","name":"轻微","id":2},{"colour":"#F57221","name":"Orange","id":18},{"colour":"","name":"程度","id":1},{"colour":"#FA5A55","name":"高风险","id":25},{"colour":"#0051AF","name":"Blue","id":21},{"colour":"#73B32D","name":"Green","id":17}],"member":[{"name":"超级张","id":"32","picture":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=1534382032094/blob&fileSize=2563"},{"name":"张少阳","id":"43","picture":""},{"name":"杨建","id":"10","picture":""},{"name":"尹明亮","id":"50","picture":""},{"name":"小冬","id":"29","picture":""},{"name":"张无忌","id":"42","picture":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=1534381794081/blob&fileSize=2309"},{"name":"小凉","id":"28","picture":""},{"name":"李萌","id":"46","picture":""},{"name":"请叫我娣娣","id":"11","picture":""},{"name":"洪得财","id":"22","picture":""},{"name":"赖志君","id":"2","picture":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=201808011921550.jpg&fileSize=8634"},{"name":"胡蓉蓉","id":"48","picture":""},{"name":"洪","id":"27","picture":""},{"name":"曹建华","id":"3","picture":""},{"name":"孙小蝶","id":"30","picture":""},{"name":"张小凡","id":"41","picture":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=1534381962824/blob&fileSize=2227"},{"name":"徐兵","id":"38","picture":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=1534232592741/blob&fileSize=5456"},{"name":"赖测一","id":"12","picture":""}]},{"field":"datetime_create_time","label":"创建时间"},{"field":"datetime_modify_time","label":"修改时间"},{"field":"tag","label":"标签","entity":[{"colour":"#66C060","name":"低风险","id":27},{"colour":"#3057E5","name":"一般","id":3},{"colour":"#FF1D32","name":"Red","id":15},{"colour":"","name":"风险","id":24},{"colour":"#19BFA4","name":"长这个自很长这个自很长这个自很长这个自很长这个自很长这个自很长这个自很长这个自很长","id":28},{"colour":"#FF1D32","name":"紧急","id":4},{"colour":"#000000","name":"Black","id":16},{"colour":"#5821A7","name":"Purple","id":20},{"colour":"","name":"颜色","id":14},{"colour":"#FF7748","name":"中风险","id":26},{"colour":"#73B32D","name":"轻微","id":2},{"colour":"#F57221","name":"Orange","id":18},{"colour":"","name":"程度","id":1},{"colour":"#FA5A55","name":"高风险","id":25},{"colour":"#0051AF","name":"Blue","id":21},{"colour":"#73B32D","name":"Green","id":17}]},{"field":"personnel_execution","member":[{"name":"超级张","id":"32","picture":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=1534382032094/blob&fileSize=2563"},{"name":"张少阳","id":"43","picture":""},{"name":"杨建","id":"10","picture":""},{"name":"尹明亮","id":"50","picture":""},{"name":"小冬","id":"29","picture":""},{"name":"张无忌","id":"42","picture":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=1534381794081/blob&fileSize=2309"},{"name":"小凉","id":"28","picture":""},{"name":"李萌","id":"46","picture":""},{"name":"请叫我娣娣","id":"11","picture":""},{"name":"洪得财","id":"22","picture":""},{"name":"赖志君","id":"2","picture":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=201808011921550.jpg&fileSize=8634"},{"name":"胡蓉蓉","id":"48","picture":""},{"name":"洪","id":"27","picture":""},{"name":"曹建华","id":"3","picture":""},{"name":"孙小蝶","id":"30","picture":""},{"name":"张小凡","id":"41","picture":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=1534381962824/blob&fileSize=2227"},{"name":"徐兵","id":"38","picture":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=1534232592741/blob&fileSize=5456"},{"name":"赖测一","id":"12","picture":""}],"label":"执行人"}]}
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ConditionBean> condition;
        private String beanName;
        private String bean;

        public List<ConditionBean> getCondition() {
            return condition;
        }

        public void setCondition(List<ConditionBean> condition) {
            this.condition = condition;
        }

        public String getBeanName() {
            return beanName;
        }

        public void setBeanName(String beanName) {
            this.beanName = beanName;
        }

        public String getBean() {
            return bean;
        }

        public void setBean(String bean) {
            this.bean = bean;
        }
    }
}
