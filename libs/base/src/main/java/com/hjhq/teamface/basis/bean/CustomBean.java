package com.hjhq.teamface.basis.bean;

import com.hjhq.teamface.basis.database.Member;

import java.util.List;

/**
 * 自定义组件bean
 * Created by lx on 2017/9/6.
 */

public class CustomBean {
    /**
     * name : xxxxxx
     * label : XXXXXX
     * type : picklist
     * disable : true
     * field : {"type":"int","required":"true","requiredModify":"true","addView":"true","addViewModify":"true","modifyDetailView":"true","remove":"false","chooseType":"0","defaultValue":""}
     * entrys : [{"value":"0","label":"广告","color":"#FFFFFF"},{"value":"1","label":"研讨会/会议","color":"#FFFFFF"},{"value":"2","label":"电子邮件","color":"#FFFFFF"},{"value":"3","label":"电话营销","color":"#FFFFFF"},{"value":"4","label":"公共关系","color":"#FFFFFF"},{"value":"5","label":"合作伙伴","color":"#FFFFFF"},{"value":"6","label":"其他","color":"#FFFFFF"}]
     */

    private String name;//字段属性名
    private String label;//字段中文名
    private String type;//组件类型
    private FieldBean field;//字段属性
    private List<EntryBean> entrys;//下拉选项
    private List<CustomBean> componentList;//子表单
    private DefaultEntryBean defaultEntrys;//多级下拉默认值
    private String moduleBean;
    private Object value;//组件值，非请求返回字段

    private RelevanceModule relevanceModule;//关联模块
    private RelevanceField relevanceField;//关联字段
    private List<SearchFields> searchFields;//搜索字段

    public static class FieldBean {
        private String fieldControl;//字段控制(0都不选 1只读 2必填)
        private String addView;//新增时显示,1显示：0不显示
        private String editView;//修改是否显示：1显示,0不显示
        private String terminalApp;//APP终端 0 隐藏不进行任何操作 1正常
        private String repeatCheck;//查重(0不查重 1允许保存 2不允许保存)
        private String defaultValue;//默认值
        private String structure;//结构(0上下布局 1左右布局)

        private String pointOut;//提示

        private List<EntryBean> defaultEntrys;//下拉默认值
        private String defaultValueId;//默认值ID
        private String defaultValueColor;//默认值颜色
        private String chooseType;//选择类型：0单选、1多选
        private String selectType;//多级选择类型：0：2级下拉、1:3级下拉

        private String phoneType;//类型(0电话 1手机)
        private String phoneLenth;//位数(0不限 1十一位)

        private String numberType;//类型(0数字 1整数 2百分比)
        private String decimalLen;//小数位
        private String numberLenth;//位数(0 1 2 3 4)
        private String betweenMin;//范围最小值
        private String betweenMax;//范围最大值

        private String countLimit;//数量限制(0不限制 1限制)
        private String maxCount;//最大上传数
        private String maxSize;//最大上传大小
        private String codeStyle;//条形码类型
        private String codeType;//后台设置使用

        private String formatType;//日期格式
        private List<Member> chooseRange;//可选范围(公司:gs_id、部门:bm_id、人员:ry_id)
        private List<Member> defaultPersonnel;//默认人员
        private List<Member> choosePersonnel;//可供选择的成员，当值为空才可从公司选
        private List<Member> defaultDepartment;//默认部门

        public String getFieldControl() {
            return fieldControl;
        }

        public void setFieldControl(String fieldControl) {
            this.fieldControl = fieldControl;
        }

        public String getAddView() {
            return addView;
        }

        public void setAddView(String addView) {
            this.addView = addView;
        }

        public String getEditView() {
            return editView;
        }

        public void setEditView(String editView) {
            this.editView = editView;
        }

        public String getTerminalApp() {
            return terminalApp;
        }

        public void setTerminalApp(String terminalApp) {
            this.terminalApp = terminalApp;
        }

        public String getDecimalLen() {
            return decimalLen;
        }

        public void setDecimalLen(String decimalLen) {
            this.decimalLen = decimalLen;
        }

        public String getRepeatCheck() {
            return repeatCheck;
        }

        public void setRepeatCheck(String repeatCheck) {
            this.repeatCheck = repeatCheck;
        }

        public String getPointOut() {
            return pointOut;
        }

        public void setPointOut(String pointOut) {
            this.pointOut = pointOut;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }


        public String getDefaultValueId() {
            return defaultValueId;
        }

        public void setDefaultValueId(String defaultValueId) {
            this.defaultValueId = defaultValueId;
        }

        public List<EntryBean> getDefaultEntrys() {
            return defaultEntrys;
        }

        public void setDefaultEntrys(List<EntryBean> defaultEntrys) {
            this.defaultEntrys = defaultEntrys;
        }

        public String getDefaultValueColor() {
            return defaultValueColor;
        }

        public void setDefaultValueColor(String defaultValueColor) {
            this.defaultValueColor = defaultValueColor;
        }

        public String getChooseType() {
            return chooseType;
        }

        public void setChooseType(String chooseType) {
            this.chooseType = chooseType;
        }

        public String getPhoneType() {
            return phoneType;
        }

        public void setPhoneType(String phoneType) {
            this.phoneType = phoneType;
        }

        public String getPhoneLenth() {
            return phoneLenth;
        }

        public void setPhoneLenth(String phoneLenth) {
            this.phoneLenth = phoneLenth;
        }

        public String getNumberType() {
            return numberType;
        }

        public void setNumberType(String numberType) {
            this.numberType = numberType;
        }

        public String getNumberLenth() {
            return numberLenth;
        }

        public void setNumberLenth(String numberLenth) {
            this.numberLenth = numberLenth;
        }

        public String getBetweenMin() {
            return betweenMin;
        }

        public void setBetweenMin(String betweenMin) {
            this.betweenMin = betweenMin;
        }

        public String getBetweenMax() {
            return betweenMax;
        }

        public void setBetweenMax(String betweenMax) {
            this.betweenMax = betweenMax;
        }

        public String getCountLimit() {
            return countLimit;
        }

        public void setCountLimit(String countLimit) {
            this.countLimit = countLimit;
        }

        public String getMaxCount() {
            return maxCount;
        }

        public void setMaxCount(String maxCount) {
            this.maxCount = maxCount;
        }

        public String getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(String maxSize) {
            this.maxSize = maxSize;
        }

        public String getCodeStyle() {
            return codeStyle;
        }

        public void setCodeStyle(String codeStyle) {
            this.codeStyle = codeStyle;
        }

        public String getCodeType() {
            return codeType;
        }

        public void setCodeType(String codeType) {
            this.codeType = codeType;
        }

        public String getFormatType() {
            return formatType;
        }

        public void setFormatType(String formatType) {
            this.formatType = formatType;
        }

        public String getStructure() {
            return structure;
        }

        public void setStructure(String structure) {
            this.structure = structure;
        }

        public List<Member> getChooseRange() {
            return chooseRange;
        }

        public void setChooseRange(List<Member> chooseRange) {
            this.chooseRange = chooseRange;
        }

        public List<Member> getChoosePersonnel() {
            return choosePersonnel;
        }

        public String getSelectType() {
            return selectType;
        }

        public void setSelectType(String selectType) {
            this.selectType = selectType;
        }

        public void setChoosePersonnel(List<Member> choosePersonnel) {
            this.choosePersonnel = choosePersonnel;
        }

        public List<Member> getDefaultPersonnel() {
            return defaultPersonnel;
        }

        public void setDefaultPersonnel(List<Member> defaultPersonnel) {
            this.defaultPersonnel = defaultPersonnel;
        }

        public List<Member> getDefaultDepartment() {
            return defaultDepartment;
        }

        public void setDefaultDepartment(List<Member> defaultDepartment) {
            this.defaultDepartment = defaultDepartment;
        }
    }

    public static class DefaultEntryBean {
        private String oneDefaultValue;//一级下拉选项默认值
        private String oneDefaultValueId;//一级下拉选项默认值ID
        private String oneDefaultValueColor;//一级下拉选项默认值颜色
        private String twoDefaultValue;//二级下拉选默认值
        private String twoDefaultValueId;//二级下拉选默认值ID
        private String twoDefaultValueColor;//二级下拉选默认值颜色
        private String threeDefaultValue;//三级下拉选项默认值
        private String threeDefaultValueId;//三级下拉选项默认值ID
        private String threeDefaultValueColor;//三级下拉选项默认值颜色

        public String getOneDefaultValue() {
            return oneDefaultValue;
        }

        public void setOneDefaultValue(String oneDefaultValue) {
            this.oneDefaultValue = oneDefaultValue;
        }

        public String getOneDefaultValueId() {
            return oneDefaultValueId;
        }

        public void setOneDefaultValueId(String oneDefaultValueId) {
            this.oneDefaultValueId = oneDefaultValueId;
        }

        public String getOneDefaultValueColor() {
            return oneDefaultValueColor;
        }

        public void setOneDefaultValueColor(String oneDefaultValueColor) {
            this.oneDefaultValueColor = oneDefaultValueColor;
        }

        public String getTwoDefaultValue() {
            return twoDefaultValue;
        }

        public void setTwoDefaultValue(String twoDefaultValue) {
            this.twoDefaultValue = twoDefaultValue;
        }

        public String getTwoDefaultValueId() {
            return twoDefaultValueId;
        }

        public void setTwoDefaultValueId(String twoDefaultValueId) {
            this.twoDefaultValueId = twoDefaultValueId;
        }

        public String getTwoDefaultValueColor() {
            return twoDefaultValueColor;
        }

        public void setTwoDefaultValueColor(String twoDefaultValueColor) {
            this.twoDefaultValueColor = twoDefaultValueColor;
        }

        public String getThreeDefaultValue() {
            return threeDefaultValue;
        }

        public void setThreeDefaultValue(String threeDefaultValue) {
            this.threeDefaultValue = threeDefaultValue;
        }

        public String getThreeDefaultValueId() {
            return threeDefaultValueId;
        }

        public void setThreeDefaultValueId(String threeDefaultValueId) {
            this.threeDefaultValueId = threeDefaultValueId;
        }

        public String getThreeDefaultValueColor() {
            return threeDefaultValueColor;
        }

        public void setThreeDefaultValueColor(String threeDefaultValueColor) {
            this.threeDefaultValueColor = threeDefaultValueColor;
        }
    }

    public static class RelevanceModule {
        private String moduleLabel;    //关联模块中文名称
        private String moduleName;    //关联模块英文名称

        public String getModuleLabel() {
            return moduleLabel;
        }

        public void setModuleLabel(String moduleLabel) {
            this.moduleLabel = moduleLabel;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }
    }

    public static class RelevanceField {
        private String fieldLabel;    //关联字段中文名称
        private String fieldName;    //关联字段英文名称

        public String getFieldLabel() {
            return fieldLabel;
        }

        public void setFieldLabel(String fieldLabel) {
            this.fieldLabel = fieldLabel;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }
    }

    public static class SearchFields {
        private String fieldLabel;    //搜索字段中文名称
        private String fieldName;    //搜索字段英文名称

        public String getFieldLabel() {
            return fieldLabel;
        }

        public void setFieldLabel(String fieldLabel) {
            this.fieldLabel = fieldLabel;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }
    }

    public RelevanceModule getRelevanceModule() {
        return relevanceModule;
    }

    public void setRelevanceModule(RelevanceModule relevanceModule) {
        this.relevanceModule = relevanceModule;
    }

    public RelevanceField getRelevanceField() {
        return relevanceField;
    }

    public void setRelevanceField(RelevanceField relevanceField) {
        this.relevanceField = relevanceField;
    }

    public List<SearchFields> getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(List<SearchFields> searchFields) {
        this.searchFields = searchFields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FieldBean getField() {
        return field;
    }

    public void setField(FieldBean field) {
        this.field = field;
    }

    public List<EntryBean> getEntrys() {
        return entrys;
    }

    public void setEntrys(List<EntryBean> entrys) {
        this.entrys = entrys;
    }

    public List<CustomBean> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<CustomBean> componentList) {
        this.componentList = componentList;
    }

    public DefaultEntryBean getDefaultEntrys() {
        return defaultEntrys;
    }

    public void setDefaultEntrys(DefaultEntryBean defaultEntrys) {
        this.defaultEntrys = defaultEntrys;
    }

    public void setModuleBean(String moduleBean) {
        this.moduleBean = moduleBean;
    }

    public String getModuleBean() {
        return moduleBean;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
