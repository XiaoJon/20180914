package com.hjhq.teamface.basis.util.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.bean.ToolMenu;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.dialog.ActionSheetDialog;
import com.hjhq.teamface.basis.util.dialog.ListDialog;
import com.hjhq.teamface.basis.util.dialog.SheetItem;
import com.hjhq.teamface.basis.util.dialog.SheetItemColor;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.view.CustomPopWindow;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用PopupWindow工具类
 *
 * @author lx
 * @date 2017/3/28
 */
public class PopUtils {
    /**
     * @param activity
     * @param parent
     * @param title
     * @param menuArray
     * @param onMenuSelectedListener
     * @return
     */
    public static void showBottomMenu(Activity activity, View parent, String title, String[] menuArray, OnMenuSelectedListener onMenuSelectedListener) {
        showBottomMenu(activity, parent, title, menuArray, -1, 0, onMenuSelectedListener);
    }

    /**
     * 底部PopupWindow弹窗
     *
     * @param activity
     * @param parent
     * @param title
     * @param menuArray
     * @param selectedIndex
     * @param onMenuSelectedlistener
     * @return
     */
    public static void showBottomMenu(Activity activity, View parent, String title,
                                      String[] menuArray, int selectedIndex, int type,
                                      final OnMenuSelectedListener onMenuSelectedlistener) {
        List<ActionSheetDialog.SheetItem> sheetItemList = new ArrayList<>();
        for (int i = 0; i < menuArray.length; i++) {
            ActionSheetDialog.SheetItemColor color = ActionSheetDialog.SheetItemColor.Black;
            if (i == selectedIndex) {
                color = ActionSheetDialog.SheetItemColor.Blue;
            }
            sheetItemList.add(new ActionSheetDialog.SheetItem(menuArray[i], color, which -> {
                if (onMenuSelectedlistener != null) {
                    onMenuSelectedlistener.onMenuSelected(which);
                }
            }));
        }

        new ActionSheetDialog(activity)
                .builder()
                .setTitle(title)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem(sheetItemList).show();
    }

    /**
     * 底部PopupWindow弹窗
     *
     * @param activity
     * @param title
     * @param subtitle
     * @param menuArray
     * @param selectedIndex
     * @param onMenuSelectedlistener
     * @return
     */
    public static void showBottomMenuWithoutCancel(Activity activity, String title, String subtitle,
                                                   String[] menuArray, int selectedIndex, int type,
                                                   final OnMenuSelectedListener onMenuSelectedlistener) {
        List<SheetItem> sheetItemList = new ArrayList<>();
        for (int i = 0; i < menuArray.length; i++) {
            SheetItemColor color = SheetItemColor.Black;
            if (i == selectedIndex) {
                color = SheetItemColor.Blue;
            }
            color = SheetItemColor.Blue;
            sheetItemList.add(new SheetItem(menuArray[i], color, which -> {
                if (onMenuSelectedlistener != null) {
                    onMenuSelectedlistener.onMenuSelected(which);
                }
            }));
        }

        ListDialog dialog = new ListDialog(activity)
                .builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(sheetItemList);
        dialog.show();
        WindowManager.LayoutParams attributes = dialog.getDialog().getWindow().getAttributes();
        attributes.height = -2;
        dialog.getView().requestLayout();


    }


    /**
     * 显示PopupWindow菜单(透明黑底，白字 ，左侧图片)
     *
     * @param activity
     * @param parent                 会显示在传入view的底部
     * @param list
     * @param onMenuSelectedlistener
     * @return
     */
    public static CustomPopWindow showMenuPopupWindow(Activity activity, View parent,
                                                      List<ToolMenu> list,
                                                      final OnMenuSelectedListener onMenuSelectedlistener) {

        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_menu_list, null);
        SoftKeyboardUtils.hide(parent);
        RecyclerView rv = view.findViewById(R.id.rv);

        final MenuItemAdapter menuItemAdapter = new MenuItemAdapter(list);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        /*final MyLinearDeviderDecoration decoration = new MyLinearDeviderDecoration(mContext);
        decoration.setDividerColorRes(R.color.white);
        decoration.setDividerHeight(1);
        rv.addItemDecoration(decoration);*/

        rv.setAdapter(menuItemAdapter);
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int yHeight = location[1] + parent.getHeight();

        final CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(activity)
                .setView(view)//显示的布局，还可以通过设置一个View
//                     .size(600,400) //设置显示的大小，不设置就默认包裹内容
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .setAnimationStyle(R.style.pop_born_anim)
                .setBgAlpha(1)
                .create()//创建PopupWindowpopWindow
                .showAtLocation(parent, Gravity.TOP | Gravity.RIGHT, 10, yHeight);//显示PopupWindow
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //菜单列表被选中
        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                menuItemAdapter.notifyDataSetChanged();
                if (onMenuSelectedlistener != null) {
                    onMenuSelectedlistener.onMenuSelected(position);
                }
                popWindow.dissmiss();
            }
        });
        return popWindow;
    }


    /**
     * 企信操作 PopupWindow弹窗
     *
     * @param activity               当前活动的Activity
     * @param parent
     * @param showWhere
     * @param list
     * @param onMenuSelectedlistener
     * @return
     */
    public static CustomPopWindow showMessagePopupMenu(Activity activity, View parent, int showWhere,
                                                       List<String> list,
                                                       final OnMenuSelectedListener onMenuSelectedlistener) {

        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.im_message_menu_list, null);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        final MessageMenuItemAdapter menuItemAdapter = new MessageMenuItemAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rv.addItemDecoration(new MyLinearDeviderDecoration(activity, R.color.main_green, false));
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.addItemDecoration(new MyLinearDeviderDecoration(mContext));
        rv.setAdapter(menuItemAdapter);
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int left = parent.getLeft();
        LogUtil.e("上" + parent.getTop());
        LogUtil.e("下" + parent.getBottom());
        LogUtil.e("左" + parent.getLeft());
        LogUtil.e("右" + parent.getRight());
        int yHeight = location[1] + parent.getHeight();
        int locationX = location[0];
        if (locationX > ScreenUtils.getScreenWidth(rv.getContext()) / 2) {
            locationX = locationX + 100 * list.size();
        } else {
            locationX = locationX - 100 * list.size();
        }

        final CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(activity)
                .setView(view)//显示的布局，还可以通过设置一个View
//                     .size(600,400) //设置显示的大小，不设置就默认包裹内容
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .setAnimationStyle(R.style.pop_born_anim)
                .setBgAlpha(1)
                .create()//创建PopupWindowpopWindow
                .showAtLocation(parent, showWhere, location[0] + parent.getWidth() / 2, location[1] + parent.getHeight());//显示PopupWindow
        LogUtil.e("上", parent.getTop() + "");
        LogUtil.e("左", parent.getLeft() + "");
        LogUtil.e("下", parent.getBottom() + "");
        LogUtil.e("右", parent.getRight() + "");

        //菜单列表被选中
        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                menuItemAdapter.notifyDataSetChanged();
                if (onMenuSelectedlistener != null) {
                    onMenuSelectedlistener.onMenuSelected(position);
                }
                popWindow.dissmiss();
            }
        });
        return popWindow;
    }

    public interface OnTimeSelectedListener {

        void selected(long time);

    }

    /**
     * 邮件定时发送选择时间控件
     *
     * @param activity
     * @param parent
     * @param limit    时间限制(目前只支持非负数)
     * @param timeUnit 时间单位
     * @param listener
     * @return
     */


}
