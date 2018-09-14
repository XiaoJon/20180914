package com.hjhq.teamface.im.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.im.R;

import java.util.List;

public class MsgAdapter extends BaseQuickAdapter<SocketMessage, BaseViewHolder> {


    public MsgAdapter(List<SocketMessage> data) {
        super(R.layout.item_msg, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SocketMessage bean) {
        if (MsgConstant.IM_ACK_CHAT_PERSONAL_CMD == bean.getUsCmdID()) {
            helper.setText(R.id.body, "发送个人聊天消息成功,接收者为:" + bean.getReceiverID());
            helper.setTextColor(R.id.body,Color.parseColor("#0000ff"));

        } else if (MsgConstant.IM_ACK_CHAT_TEAM_CMD == bean.getUsCmdID()) {
            helper.setText(R.id.body, "发送群聊天消息成功,接收者为:" + bean.getReceiverID());
            helper.setTextColor(R.id.body, Color.parseColor("#ff0000"));
        } else {
            if(bean.getUsCmdID()==5){
                //单聊
                
            }
            if(bean.getUsCmdID()==6){
                //群聊

            }
            helper.setTextColor(R.id.body,Color.parseColor("#000000"));
            helper.setText(R.id.body,
                    "发送人" + bean.getOneselfIMID() + "    " +
                            "命令" + bean.getUsCmdID() + "    " +
                            "版本" + bean.getUcVer() + "\n" +
                            "设备" + bean.getUcDeviceType() + "   " +
                            "标志" + bean.getUcFlag() + "\n" +
                            "服务器时间" + DateTimeUtil.longToStr(bean.getServerTimes(), "yyyy-MM-dd HH:mm:ss") + "\n" +
                            "本机时间:" + DateTimeUtil.longToStr(bean.getClientTimes(), "yyyy-MM-dd HH:mm:ss") + "\n" +
                            "发送人" + bean.getSenderID() + "    " +
                            "接收人" + bean.getReceiverID() + "    " +
                            "随机数" + bean.getRand() + "\n" +
                            "登录" + bean.getResultCode() + "\n" +
                            "消息: " + (bean.getContent() + ""));
        }

    }


}
