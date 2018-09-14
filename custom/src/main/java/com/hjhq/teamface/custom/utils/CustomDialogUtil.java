package com.hjhq.teamface.custom.utils;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.adapter.MultiItemAdapter;
import com.hjhq.teamface.custom.bean.TransformationResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class CustomDialogUtil {

    /**
     * 多选接口
     */
    public interface OnMutilSelectListner {
        void mutilSelectSure(List<String> positions);
    }
    /**
     * 多选 Dialog
     *
     * @param activity
     * @param title
     * @param root
     * @param listener
     */
    public static void mutilDialog(final Activity activity, String title, List<TransformationResultBean.DataBean> datas, View root, OnMutilSelectListner listener) {
        mutilDialog(activity, title, datas, "确定", "取消", root, listener);
    }

    /**
     * 多选 Dialog
     *
     * @param activity
     * @param title
     * @param root
     * @param listener
     */
    public static void mutilDialog(final Activity activity, String title, List<TransformationResultBean.DataBean> datas, String sendName, String cancelName, View root, OnMutilSelectListner listener) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(activity).inflate(
                R.layout.custom_dialog_multi, null);
        TextView tvTitle = (TextView) mResendMailPopupView.findViewById(R.id.dialog_resend_mail_tv_tip);
        tvTitle.setText(title);
        RecyclerView mRecyclerView = (RecyclerView) mResendMailPopupView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        MultiItemAdapter adapter = new MultiItemAdapter(datas);
        mRecyclerView.setAdapter(adapter);

        PopupWindow mResendMailPopup = initDisPlay(activity, dm, mResendMailPopupView);

        List<String> positions = new ArrayList<>();
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    positions.remove(datas.get(position).getId());
                } else {
                    positions.add(datas.get(position).getId());
                }
            }
        });
        /**
         * 点击了确定
         */
        TextView sendView = (TextView) mResendMailPopupView.findViewById(R.id.dialog_resend_mail_btnSure);
        TextUtil.setText(sendView, sendName);
        sendView.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
                ScreenUtils.letScreenLight(activity);
                if (listener != null) {
                    listener.mutilSelectSure(positions);
                }
            }
        });

        /**
         * 点击了取消
         */
        TextView cancel = (TextView) mResendMailPopupView.findViewById(R.id.dialog_resend_mail_btnRefuse);
        TextUtil.setText(cancel, cancelName);
        mResendMailPopupView.findViewById(R.id.dialog_resend_mail_btnRefuse).setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
                ScreenUtils.letScreenLight(activity);
            }
        });
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
    }

    private static PopupWindow initDisPlay(Activity activity, DisplayMetrics dm, View mResendMailPopupView) {
        return initDisPlay(activity, dm, mResendMailPopupView, true);
    }

    private static PopupWindow initDisPlay(Activity activity, DisplayMetrics dm, View mResendMailPopupView, boolean bl) {
        PopupWindow mResendMailPopup = new PopupWindow(mResendMailPopupView,
                dm.widthPixels, dm.heightPixels,
                true);
        //没有设置宽高显示不全的问题
        mResendMailPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mResendMailPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mResendMailPopup.setTouchable(true);
        mResendMailPopup.setOutsideTouchable(true);
        mResendMailPopup.setBackgroundDrawable(new ColorDrawable());
        ScreenUtils.letScreenGray(activity);
        if (bl) {
            mResendMailPopup.setAnimationStyle(R.style.AnimTools);
        }
        return mResendMailPopup;
    }
}
