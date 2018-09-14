package com.hjhq.teamface.oa.friends;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.common.bean.FriendsListBean;
import com.hjhq.teamface.oa.friends.logic.FriendsLogic;
import com.hjhq.teamface.oa.friends.utils.Utils;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.im.bean.AddFriendsCommentResponseBean;
import com.hjhq.teamface.im.util.ClickableForComment;
import com.hjhq.teamface.util.CommonUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;


/**
 * 企业圈详情
 * Created by lx on 2017/7/4.
 */

public class FriendsDetailActivity extends BaseTitleActivity {
    @Bind(R.id.iv_sender_headurl)
    ImageView ivSenderHeadurl;
    @Bind(R.id.tv_sender_name)
    TextView tvSenderName;
    @Bind(R.id.tv_info)
    TextView tvInfo;
    @Bind(R.id.media_lay)
    LinearLayout mediaLay;
    @Bind(R.id.tv_datetime)
    TextView tvDatetime;
    @Bind(R.id.delete_ser)
    TextView deleteSer;
    @Bind(R.id.tv_share_names)
    TextView good_names;
    @Bind(R.id.iv_good_comment)
    ImageView ivGoodComment;
    @Bind(R.id.comment_list)
    LinearLayout llCommentList;
    @Bind(R.id.praise_and_comment_lay)
    LinearLayout praiseAndCommentLay;
    @Bind(R.id.details)
    LinearLayout details;
    @Bind(R.id.bt_send)
    Button commentSend;
    @Bind(R.id.et_commentinfo)
    EditText commentInfo;
    private FriendsListBean.DataBean.ListBean itemBean;
    private FriendMedia friendMedia;

    @Override
    protected void onSetContentViewNext(Bundle savedInstanceState) {
        super.onSetContentViewNext(savedInstanceState);
        if (savedInstanceState == null) {
            itemBean = (FriendsListBean.DataBean.ListBean) getIntent().getSerializableExtra(Constants.DATA_TAG1);
        } else {
            itemBean = (FriendsListBean.DataBean.ListBean) savedInstanceState.getSerializable(Constants.DATA_TAG1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.DATA_TAG1, itemBean);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getChildView() {
        return R.layout.activity_friends_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle("详情");

    }

    @Override
    protected void setListener() {
        setOnClicks(ivSenderHeadurl, ivGoodComment);
        ivSenderHeadurl.setOnClickListener(new TextClickListener(itemBean.getEmployeeId(), this));
        // 显示点赞和评论按钮的监听
        ivGoodComment.setOnClickListener(v -> showWindow(v, friendMedia));

        commentSend.setOnClickListener(v -> {
            String content = commentInfo.getText().toString();
            String receiverId = (String) commentInfo.getTag(R.string.app_name);
            if (TextUtils.isEmpty(content)) {
                CommonUtil.showToast("请输入评论内容");
                return;
            }
            //评论
            FriendsLogic.getInstance().commentFriends(this, itemBean.getId(), receiverId, content, new ProgressSubscriber<AddFriendsCommentResponseBean>(this) {
                @Override
                public void onNext(AddFriendsCommentResponseBean baseBean) {
                    super.onNext(baseBean);
                    FriendsListBean.DataBean.ListBean.CommentBean data = baseBean.getData();
                    itemBean.getCommentList().add(data);
                    // 加载评论列表
                    setCommentList(llCommentList);
                    EventBusUtils.sendEvent(new MessageBean(Constants.FRIENDS_REFRESH, "", null));
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    CommonUtil.showToast("评论失败");
                }
            });
        });
    }

    @Override
    protected void initData() {


        ImageLoader.loadRoundImage(mContext, itemBean.getPhotograph(), ivSenderHeadurl, itemBean.getEmployeeName());
        TextUtil.setText(tvSenderName, itemBean.getEmployeeName());

        // 加载发表的文字信息
        setTextMessage(itemBean.getInfo(), tvInfo);

        // 加载发表的图片、视频、链接
        friendMedia = createAndinitFriendMedia(itemBean);
        if (null == friendMedia) {
            mediaLay.setVisibility(View.GONE);
        } else {
            mediaLay.setVisibility(View.VISIBLE);
            mediaLay.removeAllViews();
            mediaLay.addView(friendMedia.getView());
        }

        // 删除按钮
        setDeleteText(deleteSer, itemBean, ivSenderHeadurl);

        // 加载发表时间
        tvDatetime.setText(DateTimeUtil.fromTime(TextUtil.parseLong(itemBean.getDatetimeCreateDate())));

        // 设置评论和点赞的父布局
        int visiable = itemBean.getCommentList().isEmpty() && itemBean.getPraiseList().isEmpty() ? View.GONE : View.VISIBLE;
        praiseAndCommentLay.setVisibility(visiable);

        // 加载点赞列表的的布局
        setPraiseList(itemBean.getPraiseList(), good_names);

        // 加载评论列表
        setCommentList(llCommentList);

    }

    @Override
    public void onClick(View view) {
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

        melay.setData(categoryItem, this);

        melay.inflate(inflater);

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
                                CommonUtil.showToast("删除成功");
                                EventBusUtils.sendEvent(new MessageBean(Constants.FRIENDS_REFRESH, "", null));
                                finish();
                            }
                        })
                );
            }
        });
    }


    /**
     * 设置评论
     */
    private void setCommentList(LinearLayout listLay) {

        List<FriendsListBean.DataBean.ListBean.CommentBean> commentlist = itemBean.getCommentList();

        if (commentlist == null || commentlist.size() == 0) {
            listLay.setVisibility(View.GONE);
        } else {
            listLay.setVisibility(View.VISIBLE);
            listLay.removeAllViews();

            // 添加与点赞列表的分割线
            addSplitView(listLay);

            for (int i = 0; i < commentlist.size(); i++) {

                FriendsListBean.DataBean.ListBean.CommentBean commentItem = commentlist.get(i);

                TextView commentText = (TextView) inflater.inflate(
                        R.layout.friend_priase_comment_textview, null);

                listLay.addView(commentText);

                // 注册点击事件 (这里不执行任何动作，只是让下边的spanableInfo.setSpan有点击的声音)
                commentText.setOnClickListener(arg0 -> {
                });

                // 为TextView拼接字符串
                StringBuilder sb = new StringBuilder();

                int frist[] = new int[2];
                frist[0] = 0;

                String senderId = commentItem.getSenderId();
                String senderName = commentItem.getSenderName();
                sb.append(senderName);

                frist[1] = sb.length();

                int second[] = null;

                String receiverName = commentItem.getReceiverName();
                if (!TextUtil.isEmpty(receiverName)) {
                    second = new int[2];
                    sb.append("回复");
                    second[0] = sb.length();
                    sb.append(receiverName);
                    second[1] = sb.length();
                }

                sb.append(": ");
                int beginContent = sb.length();
                sb.append(commentItem.getContentinfo());

                // 添加TextView文字点击
                SpannableString spanableInfo = new SpannableString(
                        sb.toString());

                spanableInfo.setSpan(new Clickable(new TextClickListener(senderId, this)), frist[0], frist[1],
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (null != second) {

                    spanableInfo.setSpan(new Clickable(new TextClickListener(commentItem.getReceiverId(), this)), second[0], second[1],
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
                // 评论的点击事件
                spanableInfo.setSpan(new ClickableForComment(v -> {
                            if (SPHelper.getEmployeeId().equals(senderId)) {
                                delCommentDialog(commentItem);
                                return;
                            }
                            showEditText(senderId, senderName);
                        }), beginContent, sb.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                commentText.setText(spanableInfo);
                commentText.setMovementMethod(LinkMovementMethod.getInstance());

            }
        }
    }

    /**
     * 删除评论
     *
     * @param commentItem
     */
    private void delCommentDialog(FriendsListBean.DataBean.ListBean.CommentBean commentItem) {

        DialogUtils.getInstance().sureOrCancel(this, "提示", "是否确认删除？", getContainer(), () ->
                // 删除
                FriendsLogic.getInstance().delComment(this, commentItem.getCommentId(), new ProgressSubscriber<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        List<FriendsListBean.DataBean.ListBean.CommentBean> commentList = itemBean.getCommentList();
                        Observable.from(commentList)
                                .filter(comment -> comment.getCommentId() == commentItem.getCommentId())
                                .subscribe(comment -> commentList.remove(comment));

                        itemBean.setCommentList(commentList);
                        // 加载评论列表
                        setCommentList(llCommentList);
                        EventBusUtils.sendEvent(new MessageBean(Constants.FRIENDS_REFRESH, "", null));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        CommonUtil.showToast("删除失败");
                    }
                })
        );
    }


    /**
     * 添加与点赞列表的分割线
     *
     * @param listLay
     */
    private void addSplitView(LinearLayout listLay) {
        if (!itemBean.getPraiseList().isEmpty()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);

            View view = new View(this);
            view.setBackgroundColor(Color.LTGRAY);
            params.bottomMargin = Utils.dip2px(this, 10);

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

            ImageSpan imageSpan = new ImageSpan(this,
                    R.drawable.heart_red);

            SpannableString spanableInfo = new SpannableString(sb.toString());

            spanableInfo.setSpan(imageSpan, 0, 1,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            for (int i = 0; i < intlist.size(); i++) {
                FriendsListBean.DataBean.ListBean.PraiseBean praiseBean = paraises.get(i);
                spanableInfo.setSpan(new Clickable(new TextClickListener(praiseBean.getEmployeeId(), this)), intlist.get(i)[0],
                        intlist.get(i)[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            good_names.setText(spanableInfo);
            good_names.setTextSize(this.getResources().getDimension(
                    R.dimen.comment_textsize));
            good_names.setMovementMethod(LinkMovementMethod.getInstance());

        }
    }


    /**
     * 操作 弹出popupWindow
     *
     * @param v
     * @param friendMedia
     */
    @SuppressWarnings({"deprecation", "unused"})
    protected void showWindow(View v, FriendMedia friendMedia) {
        int layoutID = R.layout.friend_item_menu;
        View view = inflater.inflate(layoutID, null);

        View good =  view.findViewById(R.id.bt_good);
        TextView tvGood = view.findViewById(R.id.bt_good);
        View comment =  view.findViewById(R.id.bt_comment);
        View share = view.findViewById(R.id.bt_share);

        // 设置弹出框的高度
        int height = Utils.dip2px(this, 40);
        int width = Utils.dip2px(this, 255);

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

        int padingRight = Utils.dip2px(this, 6);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]
                        - popupWindow.getWidth() - padingRight,
                location[1] + (v.getHeight() - height) / 2);

        // 判断是否点过赞
        boolean praise = judgePraise(itemBean.getPraiseList());

        String text = "赞";
        if (praise) {
            text = "取消";
        }
        tvGood.setText(text);
        good.setOnClickListener(goodView -> {
            // 点赞/取消点赞
            FriendsLogic.getInstance().likeFriends(this, itemBean.getId(), new ProgressSubscriber<BaseBean>(this) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    EventBusUtils.sendEvent(new MessageBean(Constants.FRIENDS_REFRESH, "", null));

                    String isPraise = itemBean.getIsPraise();
                    List<FriendsListBean.DataBean.ListBean.PraiseBean> praiseList = itemBean.getPraiseList();
                    if ("0".equals(isPraise)) {
                        FriendsListBean.DataBean.ListBean.PraiseBean bean = new FriendsListBean.DataBean.ListBean.PraiseBean();
                        UserInfoBean userInfoBean = SPHelper.getUserInfo(UserInfoBean.class);
                        UserInfoBean.DataBean.EmployeeInfoBean employeeInfo = userInfoBean.getData().getEmployeeInfo();


                        bean.setEmployeeId(SPHelper.getEmployeeId());
                        bean.setEmployeeName(employeeInfo.getName());
                        bean.setPhotograph(employeeInfo.getPicture());
                        praiseList.add(bean);
                        itemBean.setIsPraise("1");
                    } else {
                        Observable.from(praiseList)
                                .filter(praiseBean -> SPHelper.getEmployeeId().equals(praiseBean.getEmployeeId()))
                                .subscribe(praiseBean -> praiseList.remove(praiseBean));
                        itemBean.setIsPraise("0");
                    }
                    itemBean.setPraiseList(praiseList);
                    // 加载点赞列表的的布局
                    setPraiseList(itemBean.getPraiseList(), good_names);
                }
            });
            popupWindow.dismiss();
        });

        comment.setOnClickListener(commentView -> {
            // 显示评论输入框
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
     * 回复
     */
    public void showEditText(String senderId, String senderName) {
        if (!TextUtil.isEmpty(senderName)) {
            commentInfo.setHint("回复：" + senderName);
            commentInfo.clearFocus();
            commentInfo.setTag(R.string.app_name, senderId);
        } else {
            commentInfo.setHint("");
            commentInfo.setTag(R.string.app_name, null);
        }
    }

}
