package com.hjhq.teamface.basis.bean;

/**
 * Created by Administrator on 2018/4/18.
 */

public class PasswrodSetBean extends BaseBean {

    /**
     * data : {"pwd_complex":1,"pwd_length":8}
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
         * pwd_complex : 1
         * pwd_length : 8
         */

        //复杂性 0无限制 1字母和数字字符 2字母数字和特殊字符 3数字和大小写字母 4数字和大小写字母和特殊字符
        private int pwd_complex;
        //长度
        private int pwd_length;

        public int getPwd_complex() {
            return pwd_complex;
        }

        public void setPwd_complex(int pwd_complex) {
            this.pwd_complex = pwd_complex;
        }

        public int getPwd_length() {
            return pwd_length;
        }

        public void setPwd_length(int pwd_length) {
            this.pwd_length = pwd_length;
        }
    }
}
