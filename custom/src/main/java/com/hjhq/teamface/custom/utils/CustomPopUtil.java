package com.hjhq.teamface.custom.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.view.CustomPopWindow;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.adapter.SortAdapter;

import java.util.List;

/**
 * 自定义模块 PopupWindow 工具类
 *
 * @author Administrator
 * @date 2017/11/30
 */

public class CustomPopUtil {
    public interface OnMenuSelectedListener {
        boolean onMenuSelected(int p);
    }
    /**
     * 显示子菜单
     *
     * @param activity               当前界面活动的Activity
     * @param parent                 会显示在传入view的底部
     * @param menuArray              子菜单数据集合
     * @param onMenuSelectedlistener 回调接口
     * @return PopupWindow对象
     */
    public static CustomPopWindow showSortWindow(Activity activity, View parent,
                                                 List<String> menuArray,
                                                 OnMenuSelectedListener onMenuSelectedlistener) {

        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_sort_list, null);
        SoftKeyboardUtils.hide(parent);
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        View topView = view.findViewById(R.id.top_view);

        SortAdapter sortAdapter = new SortAdapter(menuArray);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(sortAdapter);

        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int yHeight = (int) (location[1] + DeviceUtils.dpToPixel(mContext, 8));
        ViewGroup.LayoutParams layoutParams = topView.getLayoutParams();
        layoutParams.height = location[1];

        final CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(activity)
                .setView(view)
                .setWidth((int) ScreenUtils.getScreenWidth(mContext))
                .setFocusable(true)
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.pop_sort_anim)
                .setBgAlpha(1)
                .create()
                .showAtLocation(parent, Gravity.BOTTOM, 0, yHeight);

        //点击子菜单与外的地方关闭
        view.setOnClickListener(v -> popWindow.dissmiss());
        //菜单列表被选中
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                sortAdapter.setSelectedIndex(position);
                sortAdapter.notifyDataSetChanged();
                if (onMenuSelectedlistener != null) {
                    onMenuSelectedlistener.onMenuSelected(position);
                }
                popWindow.dissmiss();
            }
        });
        return popWindow;
    }
}
