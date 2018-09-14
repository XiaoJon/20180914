package com.hjhq.teamface.common.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.FileTypeUtils;

import java.util.List;

/**
 * 评论
 *
 * @author lx
 * @date 2017/9/22
 */

public class CommentDetailResultBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements MultiItemEntity {
        /**
         * sign_id : 9
         * employee_id : 1
         * datetime_time : 1516765114672
         * employee_name : Jun
         * id : 6
         * bean : bean1515979590079
         * content : null
         * picture :
         * relation_id : 5
         */

        private String sign_id;
        private String employee_id;
        private String datetime_time;
        private String employee_name;
        private long id;
        private String bean;
        private String content;
        private String picture;
        private String relation_id;
        private List<UploadFileBean> information;

        public List<UploadFileBean> getInformation() {
            return information;
        }

        public void setInformation(List<UploadFileBean> information) {
            this.information = information;
        }

        public String getSign_id() {
            return sign_id;
        }

        public void setSign_id(String sign_id) {
            this.sign_id = sign_id;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }

        public String getDatetime_time() {
            return datetime_time;
        }

        public void setDatetime_time(String datetime_time) {
            this.datetime_time = datetime_time;
        }

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getBean() {
            return bean;
        }

        public void setBean(String bean) {
            this.bean = bean;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getRelation_id() {
            return relation_id;
        }

        public void setRelation_id(String relation_id) {
            this.relation_id = relation_id;
        }

        @Override
        public int getItemType() {
            if (!CollectionUtils.isEmpty(getInformation())) {
                UploadFileBean uploadFileBean = getInformation().get(0);
                String fileType = uploadFileBean.getFileType();
                if (FileTypeUtils.isImage(fileType)) {
                    return 1;
                } else if (FileTypeUtils.isAudio(fileType) && uploadFileBean.getVoiceTime() != 0) {
                    return 2;
                } else {
                    return 3;
                }
            }
            return 0;
        }

    }
}
