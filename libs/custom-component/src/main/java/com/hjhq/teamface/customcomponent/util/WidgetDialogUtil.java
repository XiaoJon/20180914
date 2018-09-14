package com.hjhq.teamface.customcomponent.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.EntryBean;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.adapter.WidgetItemAdapter;

import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2018/3/22.
 */

public class WidgetDialogUtil {

    /**
     * 多选接口
     */
    public interface OnMutilSelectListner {
        void mutilSelectSure(List<EntryBean> entryBeanList);
    }

    /**
     * 多选 Dialog
     *
     * @param activity
     * @param multi    多选
     * @param root
     * @param listener
     */
    public static void mutilDialog(final Activity activity, boolean multi, List<EntryBean> datas, View root, OnMutilSelectListner listener) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(activity).inflate(
                R.layout.custom_dialog_widget, null);

        RecyclerView mRecyclerView = mResendMailPopupView.findViewById(R.id.recycler_view);
        int maxHeight = (int) (ScreenUtils.getScreenHeight(activity) / 3 * 2);
        int height = (int) DeviceUtils.dpToPixel(activity, 40 * datas.size());
        height = height > maxHeight ? maxHeight : WindowManager.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
        lp.height = height;
        mRecyclerView.setLayoutParams(lp);

        //动态设置最大高度
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        WidgetItemAdapter adapter = new WidgetItemAdapter(datas);
        mRecyclerView.setAdapter(adapter);

        PopupWindow mResendMailPopup = initDisPlay(activity, dm, mResendMailPopupView);

        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                EntryBean entryBean = datas.get(position);
                if (multi) {
                    entryBean.setCheck(!entryBean.isCheck());
                    adapter.notifyItemChanged(position);
                } else {
                    Observable.from(datas).filter(data -> {
                        data.setCheck(false);
                        return data == entryBean;
                    }).subscribe(data -> data.setCheck(true));
                    listener.mutilSelectSure(datas);

                    if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                        mResendMailPopup.dismiss();
                        ScreenUtils.letScreenLight(activity);
                    }
                }
            }
        });
        //关闭按钮
        mResendMailPopupView.findViewById(R.id.iv_del).setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
                ScreenUtils.letScreenLight(activity);
            }
        });
        if (!multi) {
            mResendMailPopupView.findViewById(R.id.ll_option).setVisibility(View.GONE);
            mResendMailPopupView.findViewById(R.id.dialog_line).setVisibility(View.GONE);
        }
        //点击了确定
        mResendMailPopupView.findViewById(R.id.dialog_btnSure).setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
                ScreenUtils.letScreenLight(activity);
                listener.mutilSelectSure(datas);
            }
        });
        // 点击了取消
        mResendMailPopupView.findViewById(R.id.dialog_btnRefuse).setOnClickListener(view -> {
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
        //解决华为闪屏
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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
