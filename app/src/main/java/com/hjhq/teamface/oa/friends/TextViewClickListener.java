package com.hjhq.teamface.oa.friends;

import android.view.View;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.common.bean.FriendsListBean;
import com.hjhq.teamface.oa.friends.adapter.FriendsAdapter;
import com.hjhq.teamface.oa.friends.logic.FriendsLogic;
import com.hjhq.teamface.util.CommonUtil;

/**
 * 评论列表的点击监听
 *
 * @author kin
 */
public class TextViewClickListener implements View.OnClickListener {

    private FriendsAdapter adapter;
    private FriendsListBean.DataBean.ListBean categoryItem;

    private Member sender;

    private FriendsListBean.DataBean.ListBean.CommentBean item;

    public TextViewClickListener(FriendsListBean.DataBean.ListBean categoryItem, FriendsListBean.DataBean.ListBean.CommentBean item,
                                 FriendsAdapter adapter, Member sender) {
        this.item = item;
        this.categoryItem = categoryItem;
        this.adapter = adapter;
        this.sender = sender;
    }

    @Override
    public void onClick(View arg0) {
        // 询问是否删除自己的评论
        if (SPHelper.getEmployeeId().equals(item.getSenderId())) {
            showDialog();
            return;
        }

        adapter.showEditText(categoryItem, sender);
    }

    private void showDialog() {

        DialogUtils.getInstance().sureOrCancel(adapter.getContext(), "提示", "是否确认删除？", adapter.getContext().getRootView(), () ->
                FriendsLogic.getInstance().delComment(adapter.getContext(), item.getCommentId(), new ProgressSubscriber<BaseBean>(adapter.getContext()) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        adapter.getContext().state = FriendsActivity.NORMAL_STATE;
                        adapter.getContext().getFriendList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        CommonUtil.showToast("删除失败");
                    }
                })
        );
    }


}
