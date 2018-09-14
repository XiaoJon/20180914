package com.hjhq.teamface.oa.friends.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.common.ui.location.ViewAddressPresenter;
import com.hjhq.teamface.im.util.EventUtil;
import com.hjhq.teamface.oa.friends.FriendMedia;
import com.hjhq.teamface.oa.friends.FriendsActivity;
import com.hjhq.teamface.common.bean.FriendsListBean;
import com.hjhq.teamface.oa.friends.logic.FriendsLogic;
import com.hjhq.teamface.oa.main.EmployeeInfoActivity;
import com.hjhq.teamface.im.bean.AddFriendsCommentResponseBean;
import com.hjhq.teamface.oa.friends.utils.FriendMediaPicture;
import com.hjhq.teamface.oa.friends.Clickable;
import com.hjhq.teamface.im.util.ClickableForComment;
import com.hjhq.teamface.oa.friends.TextClickListener;
import com.hjhq.teamface.oa.friends.TextViewClickListener;
import com.hjhq.teamface.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 企业圈适配器
 *
 * @author mou
 * @date 2017/4/5
 */

public class FriendsAdapter extends BaseQuickAdapter<FriendsListBean.DataBean.ListBean, BaseViewHolder> {
    private final LayoutInflater layoutInflater;
    private final FriendsActivity mActivity;

    public FriendsAdapter(List<FriendsListBean.DataBean.ListBean> data, FriendsActivity activity) {
        super(R.layout.friends_circle_item, data);
        this.mActivity = activity;
        this.layoutInflater = LayoutInflater.from(mActivity);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendsListBean.DataBean.ListBean item) {
        ImageView head = helper.getView(R.id.iv_sender_headurl);
        TextView senderName = helper.getView(R.id.tv_sender_name);
        TextView info = helper.getView(R.id.tv_info);
        LinearLayout contentLay = helper.getView(R.id.media_lay);
        TextView delete = helper.getView(R.id.delete_ser);
        TextView datetime = helper.getView(R.id.tv_datetime);
        ImageView iv_good_comment = helper.getView(R.id.iv_good_comment);
        TextView good_names = helper.getView(R.id.tv_share_names);
        TextView locationTv = helper.getView(R.id.location_tv);
        LinearLayout commentList = helper.getView(R.id.comment_list);
        LinearLayout praise_and_comment_lay = helper.getView(R.id.praise_and_comment_lay);


        ImageLoader.loadCircleImage(mContext, item.getPhotograph(), head, item.getEmployeeName());
        TextUtil.setText(senderName, item.getEmployeeName());
        head.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DATA_TAG1, item.getEmployeeId());
            CommonUtil.startActivtiy(mActivity, EmployeeInfoActivity.class, bundle);
        });

        //位置
        String address = item.getAddress();
        locationTv.setVisibility(TextUtils.isEmpty(address) ? View.GONE : View.VISIBLE);
        TextUtil.setText(locationTv, address);
        locationTv.setOnClickListener(rootView -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DATA_TAG1, item.getLongitude());
            bundle.putString(Constants.DATA_TAG2, item.getLatitude());
            CommonUtil.startActivtiy(mContext, ViewAddressPresenter.class, bundle);
        });

        // 加载发表的文字信息
        setTextMessage(item.getInfo(), info);

        // 加载发表的图片、视频、链接
        FriendMedia friendMedia = createAndinitFriendMedia(item);
        if (null == friendMedia) {
            contentLay.setVisibility(View.GONE);
        } else {
            contentLay.setVisibility(View.VISIBLE);
            contentLay.removeAllViews();
            contentLay.addView(friendMedia.getView());
        }

        // 删除按钮
        setDeleteText(delete, item, head);

        // 加载发表时间
        datetime.setText(DateTimeUtil.fromTime(TextUtil.parseLong(item.getDatetimeCreateDate())));

        // 设置评论和点赞的父布局
        int visiable = item.getCommentList().isEmpty() && item.getPraiseList().isEmpty() ? View.GONE : View.VISIBLE;
        praise_and_comment_lay.setVisibility(visiable);

        // 加载点赞列表的的布局
        setPraiseList(item.getPraiseList(), good_names);

        // 加载评论列表
        setCommentList(item, commentList);

        // 显示点赞和评论按钮的监听
        iv_good_comment.setOnClickListener(v -> showWindow(v, item, friendMedia));
    }

    /**
     * 设置发表的文字信息
     *
     * @param info
     * @param info
     */
    private void setTextMessage(String info, TextView text) {
        if (TextUtils.isEmpty(info)) {
            text.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.VISIBLE);
            text.setText(info);
        }
    }

    /**
     * 创建朋友圈媒体类
     *
     * @param categoryItem
     * @return
     */
    private FriendMedia createAndinitFriendMedia(FriendsListBean.DataBean.ListBean categoryItem) {

        FriendMedia melay = new FriendMediaPicture();

        melay.setData(categoryItem, mActivity);

        melay.inflate(layoutInflater);

        melay.createMedia();

        return melay;

    }

    /**
     * 设置删除按钮
     *
     * @param delete
     */
    private void setDeleteText(TextView delete, final FriendsListBean.DataBean.ListBean item, final View view) {

        delete.setVisibility(SPHelper.getEmployeeId().equals(item.getEmployeeId()) ? View.VISIBLE : View.GONE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.getInstance().sureOrCancel((Activity) mContext, "提示", "确认删除？", view, () ->
                        FriendsLogic.getInstance().delFriends((BaseActivity) mContext, item.getId(), new ProgressSubscriber<BaseBean>(mContext) {
                            @Override
                            public void onNext(BaseBean baseBean) {
                                super.onNext(baseBean);
                                ToastUtils.showToast(delete.getContext(), "删除成功");
                                EventUtil.sendEvent(new MessageBean(Constants.FRIENDS_REFRESH, "", null));
                            }
                        })
                );
            }
        });
    }


    /**
     * 设置评论
     */
    private void setCommentList(final FriendsListBean.DataBean.ListBean categoryItem,
                                LinearLayout listLay) {

        List<FriendsListBean.DataBean.ListBean.CommentBean> commentlist = categoryItem.getCommentList();

        if (commentlist == null || commentlist.size() == 0) {
            listLay.setVisibility(View.GONE);
        } else {
            listLay.setVisibility(View.VISIBLE);
            listLay.removeAllViews();

            // 添加与点赞列表的分割线
            addSplitView(categoryItem, listLay);

            for (int i = 0; i < commentlist.size(); i++) {

                FriendsListBean.DataBean.ListBean.CommentBean item = commentlist.get(i);

                TextView commentText = (TextView) layoutInflater.inflate(
                        R.layout.friend_priase_comment_textview, null);

                listLay.addView(commentText);

                // 注册点击事件 (这里不执行任何动作，只是让下边的spanableInfo.setSpan有点击的声音)
                commentText.setOnClickListener(arg0 -> {
                });

                // 为TextView拼接字符串
                StringBuilder sb = new StringBuilder();

                int frist[] = new int[2];
                frist[0] = 0;

                sb.append(item.getSenderName());
                frist[1] = sb.length();


                int second[] = null;
                String receiverName = item.getReceiverName();
                if (!TextUtil.isEmpty(receiverName)) {
                    second = new int[2];
                    sb.append("回复");
                    second[0] = sb.length();
                    sb.append(item.getReceiverName());
                    second[1] = sb.length();
                }


                sb.append(": ");
                int beginContent = sb.length();
                sb.append(item.getContentinfo());

                // 添加TextView文字点击
                SpannableString spanableInfo = new SpannableString(
                        sb.toString());

                spanableInfo.setSpan(new Clickable(new TextClickListener(item.getSenderId(), mActivity)), frist[0], frist[1],
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (null != second) {

                    spanableInfo.setSpan(new Clickable(new TextClickListener(item.getReceiverId(), mActivity)), second[0], second[1],
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                Member member = new Member(TextUtil.parseInt(item.getSenderId()), item.getSenderName(), 1);
                // 评论的点击事件
                spanableInfo.setSpan(new ClickableForComment(
                                new TextViewClickListener(categoryItem, item, this,
                                        member)), beginContent, sb.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                commentText.setText(spanableInfo);
                commentText.setMovementMethod(LinkMovementMethod.getInstance());

            }
        }
    }

    /**
     * 添加与点赞列表的分割线
     *
     * @param categoryItem
     * @param listLay
     */
    private void addSplitView(FriendsListBean.DataBean.ListBean categoryItem, LinearLayout listLay) {
        if (!categoryItem.getPraiseList().isEmpty()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);

            View view = new View(getContext());
            view.setBackgroundColor(Color.LTGRAY);
            params.bottomMargin = ((int) DeviceUtils.dpToPixel(getContext(), 10));

            listLay.addView(view, params);
        }
    }

    /**
     * 设置点赞的人
     *
     * @param paraises
     * @param good_names
     */
    private void setPraiseList(List<FriendsListBean.DataBean.ListBean.PraiseBean> paraises, TextView good_names) {

        if (paraises.isEmpty()) {
            good_names.setVisibility(View.GONE);

        } else {
            good_names.setVisibility(View.VISIBLE);

            List<int[]> intlist = new ArrayList<>();

            StringBuilder sb = new StringBuilder();

            String seperater = ", ";

            // 第一个分割会被替换为图片
            sb.append("1");

            for (FriendsListBean.DataBean.ListBean.PraiseBean bean : paraises) {
                int size[] = new int[2];
                size[0] = sb.length();
                if (null != bean) {
                    sb.append(bean.getEmployeeName());
                    sb.append(seperater);
                    size[1] = sb.length() - seperater.length();
                    intlist.add(size);
                }
            }

            try {
                sb.deleteCharAt(sb.length() - seperater.length());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ImageSpan imageSpan = new ImageSpan(mActivity,
                    R.drawable.icon_friends_like);

            SpannableString spanableInfo = new SpannableString(sb.toString());

            spanableInfo.setSpan(imageSpan, 0, 1,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            for (int i = 0; i < intlist.size(); i++) {
                FriendsListBean.DataBean.ListBean.PraiseBean praiseBean = paraises.get(i);
                spanableInfo.setSpan(new Clickable(new TextClickListener(praiseBean.getEmployeeId(), mActivity)), intlist.get(i)[0],
                        intlist.get(i)[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            good_names.setText(spanableInfo);
            good_names.setTextSize(mActivity.getResources().getDimension(
                    R.dimen.comment_textsize));
            good_names.setMovementMethod(LinkMovementMethod.getInstance());

        }
    }


    /**
     * 操作 弹出popupWindow
     *
     * @param v
     * @param categoryItem
     * @param friendMedia
     */
    @SuppressWarnings({"deprecation", "unused"})
    protected void showWindow(View v, final FriendsListBean.DataBean.ListBean categoryItem,
                              final FriendMedia friendMedia) {

        int layoutID = R.layout.friend_item_menu;
        View view = layoutInflater.inflate(layoutID, null);

        View good = view.findViewById(R.id.bt_good);
        TextView tvGood = view.findViewById(R.id.tv_good);
        View comment = view.findViewById(R.id.bt_comment);
        View share = view.findViewById(R.id.bt_share);

        // 设置弹出框的高度
        int height = ((int) DeviceUtils.dpToPixel(mActivity, 40));
        int width = ((int) DeviceUtils.dpToPixel(mActivity, 255));

        PopupWindow popupWindow = new PopupWindow(view, width, height);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.AnimationPreview);

        // 显示的位置为:按钮的左边
        int[] location = new int[2];

        v.getLocationOnScreen(location);

        int padingRight = ((int) DeviceUtils.dpToPixel(mActivity, 6));

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]
                        - popupWindow.getWidth() - padingRight,
                location[1] + (v.getHeight() - height) / 2);

        // 判断是否点过赞
        boolean praise = judgePraise(categoryItem.getPraiseList());

        String text = "赞";
        if (praise) {
            text = "取消";
        }
        tvGood.setText(text);
        good.setOnClickListener(goodView -> {
            // 点赞/取消点赞
            FriendsLogic.getInstance().likeFriends(mActivity, categoryItem.getId(), new ProgressSubscriber<BaseBean>(mActivity) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ((FriendsActivity) mContext).state = FriendsActivity.NORMAL_STATE;
                    ((FriendsActivity) mContext).getFriendList();
                }
            });
            popupWindow.dismiss();
        });

        comment.setOnClickListener(commentView -> {
            // 显示评论输入框
            FriendsAdapter.this.showEditText(categoryItem, null);
            popupWindow.dismiss();
        });

        share.setOnClickListener(shareView -> {
            friendMedia.share();
            popupWindow.dismiss();
        });

    }

    /**
     * 判断是否已经点过赞
     *
     * @return
     */
    private boolean judgePraise(List<FriendsListBean.DataBean.ListBean.PraiseBean> list) {
        for (FriendsListBean.DataBean.ListBean.PraiseBean temp : list) {
            if (SPHelper.getEmployeeId().equals(temp.getEmployeeId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 发表评论
     *
     * @param categoryItem
     * @param friend
     */
    public void showEditText(final FriendsListBean.DataBean.ListBean categoryItem,
                             final Member friend) {

        View view = layoutInflater.inflate(R.layout.edittext, null);

        EditText commentInfo = (EditText) view
                .findViewById(R.id.et_commentinfo);
        Button commentSend = (Button) view.findViewById(R.id.bt_send);

        // 设置弹出框：设置大小
        PopupWindow popupWindow = new PopupWindow(view,
                ((int) ScreenUtils.getScreenWidth(getContext())), LinearLayout.LayoutParams.WRAP_CONTENT);

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 设置弹出窗口中有键盘
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 设置软键盘默认显示，自动适配
        popupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // 设置弹出位置
        popupWindow.showAtLocation(
                mActivity.getRootView(),
                Gravity.BOTTOM, 0, 0);

        if (friend != null) {
            commentInfo.setHint("回复：" + friend.getName());
            commentInfo.clearFocus();
        }
        final String receiverId = friend != null ? friend.getId() + "" : null;

        commentSend.setOnClickListener(v -> {
            String content = commentInfo.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                CommonUtil.showToast("请输入评论内容");
                return;
            }
            popupWindow.dismiss();
            //评论
            FriendsLogic.getInstance().commentFriends(mActivity, categoryItem.getId(), receiverId, content, new ProgressSubscriber<AddFriendsCommentResponseBean>(mActivity) {
                @Override
                public void onNext(AddFriendsCommentResponseBean baseBean) {
                    super.onNext(baseBean);
                    EventUtil.sendEvent(new MessageBean(Constants.FRIENDS_REFRESH, "", null));
                }


                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    CommonUtil.showToast("评论失败");
                }
            });
        });
    }

    public FriendsActivity getContext() {
        return mActivity;
    }
}