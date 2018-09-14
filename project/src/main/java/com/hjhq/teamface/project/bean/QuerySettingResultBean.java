package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 *
 * @author Administrator
 * @date 2018/6/25
 */

public class QuerySettingResultBean extends BaseBean {

    /**
     * data : {"note":"111","leader":2,"star_level":0,"temp_status":"0","temp_id":0,"modify_time":1530180289691,"project_status":"0","visual_range_status":"1","system_default_pic":"","del_status":"0","modify_by":2,"project_remind_type":"0","project_progress_status":"0","create_by":2,"project_labels_content":"[{\"field\":{\"fieldControl\":\"0\",\"editView\":\"1\",\"terminalPc\":\"1\",\"repeatCheck\":\"0\",\"terminalApp\":\"1\",\"pointOut\":\"\",\"defaultValue\":\"\",\"isShowCard\":\"0\",\"isFillReason\":\"0\",\"addView\":\"1\",\"structure\":\"1\"},\"typeText\":\"富文本\",\"name\":\"multitext_desc\",\"width\":\"100%\",\"active\":\"0\",\"label\":\"描述\",\"state\":\"0\",\"type\":\"multitext\",\"remove\":\"1\",\"isactive\":0},{\"field\":{\"fieldControl\":\"0\",\"countLimit\":\"0\",\"editView\":\"1\",\"terminalPc\":\"1\",\"terminalApp\":1,\"maxSize\":\"\",\"addView\":\"1\",\"maxCount\":\"\",\"structure\":\"1\"},\"typeText\":\"附件\",\"name\":\"attachment_1520248017179\",\"width\":\"100%\",\"active\":\"0\",\"label\":\"附件\",\"state\":\"0\",\"type\":\"attachment\",\"remove\":\"1\",\"isactive\":0},{\"relevanceWhere\":[{\"fieldName\":\"\",\"fieldLabel\":\"\",\"operatorType\":\"\",\"value\":\"\"}],\"active\":\"0\",\"label\":\"关联\",\"type\":\"reference\",\"remove\":\"1\",\"field\":{\"fieldControl\":\"0\",\"editView\":\"1\",\"terminalPc\":\"1\",\"terminalApp\":\"1\",\"pointOut\":\"\",\"isShowCard\":\"1\",\"isFillReason\":\"0\",\"addView\":\"1\",\"structure\":\"1\"},\"relevanceModule\":{\"moduleName\":\"\",\"moduleLabel\":\"\"},\"typeText\":\"关联关系\",\"name\":\"reference_relation\",\"width\":\"100%\",\"relevanceField\":{\"fieldName\":\"\",\"fieldLabel\":\"\"},\"seniorWhere\":\"1 AND 2\",\"searchFields\":[{\"fieldName\":\"\",\"fieldLabel\":\"\"}],\"state\":\"0\",\"isactive\":0}]","project_remind_title":"0","project_time_status":"1","id":51,"project_remind_unit":"0","leader_pic":"http://192.168.1.58:8281/custom-gateway/common/file/imageDownload?bean=company&fileName=201806251810360.jpg&fileSize=36791","create_time":1529639483585,"leader_name":"张琴","star_time":0,"end_time":1530201600000,"project_progress_number":17,"sort":"","start_time":"","temp_type":"","project_progress_content":0,"project_complete_status":"1","project_remind_content":"","name":"111","pic_url":""}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * note : 111
         * leader : 2
         * star_level : 0
         * temp_status : 0
         * temp_id : 0
         * modify_time : 1530180289691
         * project_status : 0
         * visual_range_status : 1
         * system_default_pic :
         * del_status : 0
         * modify_by : 2
         * project_remind_type : 0
         * project_progress_status : 0
         * create_by : 2
         * project_labels_content : [{"field":{"fieldControl":"0","editView":"1","terminalPc":"1","repeatCheck":"0","terminalApp":"1","pointOut":"","defaultValue":"","isShowCard":"0","isFillReason":"0","addView":"1","structure":"1"},"typeText":"富文本","name":"multitext_desc","width":"100%","active":"0","label":"描述","state":"0","type":"multitext","remove":"1","isactive":0},{"field":{"fieldControl":"0","countLimit":"0","editView":"1","terminalPc":"1","terminalApp":1,"maxSize":"","addView":"1","maxCount":"","structure":"1"},"typeText":"附件","name":"attachment_1520248017179","width":"100%","active":"0","label":"附件","state":"0","type":"attachment","remove":"1","isactive":0},{"relevanceWhere":[{"fieldName":"","fieldLabel":"","operatorType":"","value":""}],"active":"0","label":"关联","type":"reference","remove":"1","field":{"fieldControl":"0","editView":"1","terminalPc":"1","terminalApp":"1","pointOut":"","isShowCard":"1","isFillReason":"0","addView":"1","structure":"1"},"relevanceModule":{"moduleName":"","moduleLabel":""},"typeText":"关联关系","name":"reference_relation","width":"100%","relevanceField":{"fieldName":"","fieldLabel":""},"seniorWhere":"1 AND 2","searchFields":[{"fieldName":"","fieldLabel":""}],"state":"0","isactive":0}]
         * project_remind_title : 0
         * project_time_status : 1
         * id : 51
         * project_remind_unit : 0
         * leader_pic : http://192.168.1.58:8281/custom-gateway/common/file/imageDownload?bean=company&fileName=201806251810360.jpg&fileSize=36791
         * create_time : 1529639483585
         * leader_name : 张琴
         * star_time : 0
         * end_time : 1530201600000
         * project_progress_number : 17.0
         * sort :
         * start_time :
         * temp_type :
         * project_progress_content : 0
         * project_complete_status : 1
         * project_remind_content :
         * name : 111
         * pic_url :
         */

        private String note;
        private int leader;
        private int star_level;
        private String temp_status;
        private long temp_id;
        private String modify_time;
        private String project_status;
        private String visual_range_status;
        private String system_default_pic;
        private String del_status;
        private String project_remind_type;
        private String project_progress_status;
        private long create_by;
        private String project_labels_content;
        private String project_remind_title;
        private long id;
        private String project_remind_unit;
        private String leader_pic;
        private String create_time;
        private String leader_name;
        private String star_time;
        private String end_time;
        private String project_progress_number;
        private String sort;
        private String start_time;
        private String temp_type;
        private String project_progress_content;
        private String project_complete_status;
        private String project_time_status;
        private String project_remind_content;
        private String name;
        private String pic_url;

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public int getLeader() {
            return leader;
        }

        public void setLeader(int leader) {
            this.leader = leader;
        }

        public int getStar_level() {
            return star_level;
        }

        public void setStar_level(int star_level) {
            this.star_level = star_level;
        }

        public String getTemp_status() {
            return temp_status;
        }

        public void setTemp_status(String temp_status) {
            this.temp_status = temp_status;
        }

        public long getTemp_id() {
            return temp_id;
        }

        public void setTemp_id(long temp_id) {
            this.temp_id = temp_id;
        }

        public String getModify_time() {
            return modify_time;
        }

        public void setModify_time(String modify_time) {
            this.modify_time = modify_time;
        }

        public String getProject_status() {
            return project_status;
        }

        public void setProject_status(String project_status) {
            this.project_status = project_status;
        }

        public String getVisual_range_status() {
            return visual_range_status;
        }

        public void setVisual_range_status(String visual_range_status) {
            this.visual_range_status = visual_range_status;
        }

        public String getSystem_default_pic() {
            return system_default_pic;
        }

        public void setSystem_default_pic(String system_default_pic) {
            this.system_default_pic = system_default_pic;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getProject_remind_type() {
            return project_remind_type;
        }

        public void setProject_remind_type(String project_remind_type) {
            this.project_remind_type = project_remind_type;
        }

        public String getProject_progress_status() {
            return project_progress_status;
        }

        public void setProject_progress_status(String project_progress_status) {
            this.project_progress_status = project_progress_status;
        }

        public long getCreate_by() {
            return create_by;
        }

        public void setCreate_by(long create_by) {
            this.create_by = create_by;
        }

        public String getProject_labels_content() {
            return project_labels_content;
        }

        public void setProject_labels_content(String project_labels_content) {
            this.project_labels_content = project_labels_content;
        }

        public String getProject_remind_title() {
            return project_remind_title;
        }

        public void setProject_remind_title(String project_remind_title) {
            this.project_remind_title = project_remind_title;
        }

        public String getProject_time_status() {
            return project_time_status;
        }

        public void setProject_time_status(String project_time_status) {
            this.project_time_status = project_time_status;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getProject_remind_unit() {
            return project_remind_unit;
        }

        public void setProject_remind_unit(String project_remind_unit) {
            this.project_remind_unit = project_remind_unit;
        }

        public String getLeader_pic() {
            return leader_pic;
        }

        public void setLeader_pic(String leader_pic) {
            this.leader_pic = leader_pic;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getLeader_name() {
            return leader_name;
        }

        public void setLeader_name(String leader_name) {
            this.leader_name = leader_name;
        }

        public String getStar_time() {
            return star_time;
        }

        public void setStar_time(String star_time) {
            this.star_time = star_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getProject_progress_number() {
            return project_progress_number;
        }

        public void setProject_progress_number(String project_progress_number) {
            this.project_progress_number = project_progress_number;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getTemp_type() {
            return temp_type;
        }

        public void setTemp_type(String temp_type) {
            this.temp_type = temp_type;
        }

        public String getProject_progress_content() {
            return project_progress_content;
        }

        public void setProject_progress_content(String project_progress_content) {
            this.project_progress_content = project_progress_content;
        }

        public String getProject_complete_status() {
            return project_complete_status;
        }

        public void setProject_complete_status(String project_complete_status) {
            this.project_complete_status = project_complete_status;
        }

        public String getProject_remind_content() {
            return project_remind_content;
        }

        public void setProject_remind_content(String project_remind_content) {
            this.project_remind_content = project_remind_content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }
    }
}
