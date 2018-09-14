package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.common.bean.FriendsListBean;

/**
 * Created by lx on 2017/7/5.
 */

public class AddFriendsCommentResponseBean extends BaseBean {


    /**
     * data : {"contentinfo":"g000d","senderPhotograph":"http://192.168.1.172:9400/1/03abf9c59e2773","senderName":"小俊","senderId":3437376035700736,"receiverId":null,"receiverPhotograph":null,"receiverName":null,"commentId":3447010355511296,"createDate":1499223790301}
     */

    private FriendsListBean.DataBean.ListBean.CommentBean data;

    public FriendsListBean.DataBean.ListBean.CommentBean getData() {
        return data;
    }

    public void setData(FriendsListBean.DataBean.ListBean.CommentBean data) {
        this.data = data;
    }

}
