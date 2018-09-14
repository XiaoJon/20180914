package com.hjhq.teamface.memo.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hjhq.teamface.basis.bean.ModuleItemBean;
import com.hjhq.teamface.basis.constants.MemoConstant;

/**
 * Created by Administrator on 2018/3/21.
 * Describe：
 */

public class MemoBean implements MultiItemEntity {


    /**
     * type : 1
     * text : {"check":1,"num":1,"content":"neirong"}
     * remind : {"time":1521613224,"mode":"提醒模式"}
     * voice : {"duration":34341,"num":1}
     * img : {"url":"文件url","localPath":"本地路径,适用于","type":"png","size":3243}
     * project : {"name":"项目名字","id":"项目id","项目其他东西":"比如任务列表等"}
     * location : {"address":"地址","longitude":"经度","latitude":"纬度"}
     * member : {"name":"地址","id":"经度","avatar":"纬度"}
     */

    private int type;
    private TextBean text;
    private RemindBean remind;
    private VoiceBean voice;
    private ImgBean img;
    private ModuleItemBean project;
    private MemoLocationBean location;
    private MemberBean member;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TextBean getText() {
        return text;
    }

    public void setText(TextBean text) {
        this.text = text;
    }

    public RemindBean getRemind() {
        return remind;
    }

    public void setRemind(RemindBean remind) {
        this.remind = remind;
    }

    public VoiceBean getVoice() {
        return voice;
    }

    public void setVoice(VoiceBean voice) {
        this.voice = voice;
    }

    public ImgBean getImg() {
        return img;
    }

    public void setImg(ImgBean img) {
        this.img = img;
    }

    public ModuleItemBean getProject() {
        return project;
    }

    public void setProject(ModuleItemBean project) {
        this.project = project;
    }

    public MemoLocationBean getLocation() {
        return location;
    }

    public void setLocation(MemoLocationBean location) {
        this.location = location;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public static class TextBean {
        /**
         * check : 1
         * num : 1
         * content : neirong
         */

        private int check;
        private int num;
        private String content;

        public int getCheck() {
            return check;
        }

        public void setCheck(int check) {
            this.check = check;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class RemindBean {
        /**
         * time : 1521613224
         * mode : 提醒模式
         */

        private long time;
        private int mode;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }
    }

    public static class VoiceBean {
        /**
         * duration : 34341
         * num : 1
         */

        private int duration;
        private int num;

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

    public static class ImgBean {
        /**
         * url : 文件url
         * localPath : 本地路径,适用于
         * type : png
         * size : 3243
         */

        private String url;
        private String localPath;
        private String type;
        private int size;
        private int from;

        public int getFrom() {
            return TextUtils.isEmpty(url) ? MemoConstant.LOCAL_FILE : MemoConstant.NET_FILE;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLocalPath() {
            return localPath;
        }

        public void setLocalPath(String localPath) {
            this.localPath = localPath;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }




    public static class MemberBean {
        /**
         * name : 地址
         * id : 经度
         * avatar : 纬度
         */

        private String name;
        private String id;
        private String avatar;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
