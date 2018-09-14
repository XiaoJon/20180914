package com.hjhq.teamface.im.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.FormatTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.view.SwipeMenuLayout;
import com.hjhq.teamface.emoji.EmojiUtil;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.db.DBManager;

import java.io.IOException;
import java.util.List;


public class ConversationListAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> {
    int openitemPosition = -1;
    BaseActivity mBaseActivity;
    private boolean isMenuOpen;
    int padding;


    public ConversationListAdapter(BaseActivity activity, List<Conversation> data) {
        super(R.layout.item_conversation_list, data);
        this.mBaseActivity = activity;
        padding = (int) DeviceUtils.dpToPixel(activity, 5);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final Conversation item) {
        //   helper.setIsRecyclable(true);
        helper.setVisible(R.id.tv_conversation_unread_num, false);
        helper.setVisible(R.id.tv_last_msg_time, false);
        ImageView ivAvatar = helper.getView(R.id.iv_conversation_avatar);
        ivAvatar.setPadding(0, 0, 0, 0);

        //置顶
        if (item.getTopStatus() == 1) {
            helper.getConvertView().setBackgroundColor(Color.parseColor("#1020779A"));
        } else {
            helper.getConvertView().setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        //未读数
        /*DragBallView dragBallView = helper.getView(R.id.drag_view);
        if (item.getUnreadMsgCount() > 0) {
            dragBallView.setVisibility(View.VISIBLE);
            dragBallView.setMsgCount(item.getUnreadMsgCount());
        } else {
            dragBallView.setVisibility(View.GONE);
        }
        dragBallView.setOnDragBallListener(new DragBallView.OnDragBallListener() {
            @Override
            public void onDisappear() {
                dragBallView.setVisibility(View.GONE);
                ToastUtils.showToast(mContext, item.getTitle() + ">>>标记为已读");
            }
        });*/
        if (item.getUnreadMsgCount() > 0 && item.getUnreadMsgCount() <= MsgConstant.SHOW_MAX_EXACT_NUM) {
            helper.setVisible(R.id.tv_conversation_unread_num, true);
            if (item.getUnreadMsgCount() < 10) {
                helper.setBackgroundRes(R.id.tv_conversation_unread_num, R.drawable.im_unread_num_round_bg);
            } else {
                helper.setBackgroundRes(R.id.tv_conversation_unread_num, R.drawable.im_unread_num_bg);
            }
            helper.setVisible(R.id.tv_conversation_unread_num, true);
            helper.setText(R.id.tv_conversation_unread_num, item.getUnreadMsgCount() + "");
        } else if (item.getUnreadMsgCount() > MsgConstant.SHOW_MAX_EXACT_NUM) {
            helper.setVisible(R.id.tv_conversation_unread_num, true);
            helper.setBackgroundRes(R.id.tv_conversation_unread_num, R.drawable.im_unread_num_bg);
            helper.setVisible(R.id.tv_conversation_unread_num, true);
            helper.setText(R.id.tv_conversation_unread_num, R.string.im_unread_99_plus);
        }
        //标题
        helper.setText(R.id.tv_conversation_title, item.getTitle() + "");
        //内容
        if (TextUtils.isEmpty(item.getDraft())) {
            String msgContent = "";
            if (item.getLastMessageType() == MsgConstant.TEXT) {
                if (TextUtils.isEmpty(item.getLatestMessage())) {
                    msgContent = "[无消息]";
                } else {
                    msgContent = item.getLatestMessage() + "";
                    try {
                        EmojiUtil.handlerEmojiText2(((TextView) helper.getView(R.id.tv_last_message)),
                                item.getLatestMessage(), helper.getView(R.id.tv_last_message).getContext());
                    } catch (IOException e) {

                    }
                }
            } else if (item.getLastMessageType() == MsgConstant.IMAGE) {
                msgContent = "[图片]";
            } else if (item.getLastMessageType() == MsgConstant.VOICE) {
                msgContent = "[语音]";
            } else if (item.getLastMessageType() == MsgConstant.FILE) {
                msgContent = "[文件]";
            } else if (item.getLastMessageType() == MsgConstant.VIDEO) {
                msgContent = "[视频]";
            } else if (item.getLastMessageType() == MsgConstant.LOCATION) {
                msgContent = "[位置]";
            } else if (item.getLastMessageType() == MsgConstant.NOTIFICATION) {
                SpannableString ss = new SpannableString("" + item.getLatestMessage());
//                SpannableString ss = new SpannableString("[通知]" + item.getLatestMessage());
               /* ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                ss.setSpan(fcs, 0, 4, 0);*/
                msgContent = ss.toString();
            } else {
                msgContent = "[无消息]";
            }
            SpannableString ss = new SpannableString(msgContent);
            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#FF0000"));
            if (MsgConstant.SB_AT_ME == item.getLastMessageState()) {
                ss = new SpannableString("[有人@我] " + msgContent);
                ss.setSpan(fcs, 0, 6, 0);
            } else if (MsgConstant.SB_AT_ALL == item.getLastMessageState()) {
                ss = new SpannableString("[@所有人] " + msgContent);
                ss.setSpan(fcs, 0, 6, 0);
            }
            helper.setText(R.id.tv_last_message, ss);
        } else {
            SpannableString ss = new SpannableString("[草稿] " + item.getDraft());
            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#FF0000"));
            ss.setSpan(fcs, 0, 4, 0);
            helper.setText(R.id.tv_last_message, ss);
        }
        SwipeMenuLayout view = helper.getView(R.id.sml);
        view.setOnMenuSwitchListener(new SwipeMenuLayout.OnMenuSwitchListener() {
            @Override
            public void toggle(boolean open) {
                isMenuOpen = open;
            }
        });
        view.setSwipeEnable(false);
        //时间
        if (item.getLastMsgDate() > 0) {
            helper.setVisible(R.id.tv_last_msg_time, true);
            helper.setText(R.id.tv_last_msg_time, FormatTimeUtil.getNewChatTime(item.getLastMsgDate()));
        } else {
            helper.setText(R.id.tv_last_msg_time, "");
        }
        ivAvatar.setBackground(null);
        if (item.getGroupType() == 2) {
            //普通群
            view.setSwipeEnable(true);
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(), R.drawable.icon_normal_group, ivAvatar);
        }
        if (item.getConversationType() == MsgConstant.SINGLE) {
            //单聊
            view.setSwipeEnable(true);
            String avatarUrl = DBManager.getInstance().qureyAvatarUrl(item.getReceiverId());
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(), avatarUrl, ivAvatar, item.getTitle());
        } else if (item.getGroupType() == 1 || item.getConversationType() == MsgConstant.SELF_DEFINED) {
            //公司群和小助手无法侧滑删除
            view.setSwipeEnable(false);
            if (item.getGroupType() == 1) {
                ImageLoader.loadCircleImage(helper.getConvertView().getContext(), R.drawable.icon_first_group, ivAvatar);
            } else if (item.getConversationType() == MsgConstant.SELF_DEFINED) {
                if (MsgConstant.MODULE_ASSISTANT == item.getTotalMsgNum()) {
                    ivAvatar.setBackground(ivAvatar.getContext().getResources().getDrawable(R.drawable.module_tag_round_bg));
                    GradientDrawable drawable = (GradientDrawable) ivAvatar.getBackground();
                    drawable.setColor(Color.RED);
                    String iconColor = item.getIcon_color();
                    drawable.setColor(ColorUtils.hexToColor(iconColor, "#3689E9"));
                    String imageUrl = item.getIcon_url();
                    if ("1".equals(item.getIcon_type())) {

                        ImageLoader.loadCircleImage(mContext, imageUrl, ivAvatar, R.drawable.ic_image);
                    } else if (TextUtil.isEmpty(imageUrl)) {
                        ImageLoader.loadCircleImage(mContext, R.drawable.ic_image, ivAvatar);
                    } else {
                        ivAvatar.setPadding(padding, padding, padding, padding);
                        //将 - 转换成 _
                        String replace = imageUrl.replace("-", "_");
                        int resId = mContext.getResources().getIdentifier(replace, "drawable", mContext.getPackageName());
                        ImageLoader.loadCircleImage(mContext, resId, ivAvatar, R.drawable.ic_image);
                    }

                } else {
                    int drawableRes = 0;
                    switch (item.getTotalMsgNum()) {
                        case MsgConstant.MODULE_ASSISTANT:
                            //自定义模块助手
                            break;
                        case MsgConstant.IM_ASSISTANT:
                            drawableRes = R.drawable.icon_chat_assistant;
                            break;
                        case MsgConstant.APPROVE_ASSISTANT:
                            drawableRes = R.drawable.icon_approval_assistant;
                            break;
                        case MsgConstant.FILE_LIB_ASSISTANT:
                            drawableRes = R.drawable.icon_file_assistant;
                            break;
                        case MsgConstant.EMAIL_ASSISTANT:
                            drawableRes = R.drawable.icon_email_assistant;
                            break;
                        case MsgConstant.MEMO_ASSISTANT:
                            drawableRes = R.drawable.icon_memo_assistant;
                            break;
                        case MsgConstant.TASK_ASSISTANT:
                            drawableRes = R.drawable.icon_task_assistant;
                            break;
                    }
                    if (drawableRes != 0) {
                        ImageLoader.loadCircleImage(helper.getConvertView().getContext(), drawableRes, ivAvatar);
                    } else {
                        String name = item.getTitle();
                        if (TextUtils.isEmpty(name)) {
                            name = "助手";
                        } else {
                            if (name.length() > 2) {
                                name = name.substring(0, 2);
                            }
                        }

                        ImageLoader.loadCircleImage(helper.getConvertView().getContext(), "", ivAvatar, name);
                    }

                }

                SpannableString ss = new SpannableString("" + item.getLatestMessage());
                /*ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                ss.setSpan(fcs, 0, 4, 0);*/
                helper.setText(R.id.tv_last_message, ss);
            }
        }
        helper.addOnClickListener(R.id.delete_item);
        //将会话状态改为隐藏
        helper.getView(R.id.delete_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImLogic.getInstance().hideSessionWithStatus(mBaseActivity, item.getConversationId(),
                        item.getConversationType(), 1, new ProgressSubscriber<BaseBean>(mBaseActivity, false) {
                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.showError(mContext, "删除会话失败");
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                remove(helper.getAdapterPosition());
                                DBManager.getInstance().saveOrReplace(item);
                                if (item.getUnreadMsgCount() > 0) {
                                    EventBusUtils.sendEvent(new MessageBean(item.getConversationType(), MsgConstant.IM_MARK_CONV_ALL_READ, item.getConversationId()));
                                }
                                item.setIsHide(1);
                                notifyDataSetChanged();
                            }
                        });

            }
        });


    }

    public void closetDrawer() {

    }

    public boolean isMenuOpen() {
        return isMenuOpen;
    }
}