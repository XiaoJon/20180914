package com.hjhq.teamface.project.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.view.CustomPopWindow;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.ModulePagerAdapter;
import com.hjhq.teamface.project.adapter.TaskListPopAdapter;
import com.hjhq.teamface.project.model.TaskModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.List;

import rx.Subscriber;

/**
 * @author Administrator
 * @date 2018/5/10
 */

public class ProjectDialog {
    private static ProjectDialog instance;

    private ProjectDialog() {
    }

    public synchronized static ProjectDialog getInstance() {
        if (instance == null) {
            instance = new ProjectDialog();
        }
        return instance;
    }


    private PopupWindow initDisPlay(Activity activity, DisplayMetrics dm, View mResendMailPopupView) {
        return initDisPlay(activity, dm, mResendMailPopupView, true);
    }

    private PopupWindow initDisPlay(Activity activity, DisplayMetrics dm, View mResendMailPopupView, boolean bl) {
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


    /**
     * 显示应用下的模块
     *
     * @param title
     * @param list
     * @param root
     * @param listener
     */
    public void showAppModule(String title, List<List<AppModuleBean>> list, View root, SimpleItemClickListener listener) {
        Activity context = (Activity) root.getContext();
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(context).inflate(
                R.layout.dialog_module_layout, null);
        MagicIndicator magicIndicator = mResendMailPopupView.findViewById(R.id.magic_indicator);
        ViewPager mViewPager = mResendMailPopupView.findViewById(R.id.view_pager);
        TextView tvAppName = mResendMailPopupView.findViewById(R.id.tv_app_name);
        TextUtil.setText(tvAppName, title);

        ModulePagerAdapter modulePagerAdapter = new ModulePagerAdapter(context, list);
        mViewPager.setAdapter(modulePagerAdapter);
        CircleNavigator circleNavigator = new CircleNavigator(context);
        circleNavigator.setCircleCount(list.size());
        circleNavigator.setCircleColor(ColorUtils.resToColor(context, R.color.gray_a9));
        circleNavigator.setCircleClickListener(index -> mViewPager.setCurrentItem(index));
        magicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);

        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        PopupWindow mResendMailPopup = initDisPlay(context, dm, mResendMailPopupView);

        modulePagerAdapter.setListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                listener.onItemClick(adapter, view, position);
                mResendMailPopup.dismiss();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                listener.onItemChildClick(adapter, view, position);
            }
        });
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(context));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
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
                                                 List<String> menuArray, int selectIndex,
                                                 OnMenuSelectedListener onMenuSelectedlistener) {

        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_sort_list, null);
        SoftKeyboardUtils.hide(parent);
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        View topView = view.findViewById(R.id.top_view);

        TaskListPopAdapter sortAdapter = new TaskListPopAdapter(menuArray, selectIndex);
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

    /**
     * 更新任务状态
     *
     * @param mActivity
     * @param view
     * @param jsonObject
     * @param completeStatus
     * @param projectCompleteStatus
     * @param s
     */
    public static PopupWindow updateTaskStatus(RxAppCompatActivity mActivity, View view, JSONObject jsonObject, boolean completeStatus, String projectCompleteStatus, Subscriber<BaseBean> s) {
        return updateTaskStatus(false, mActivity, view, jsonObject, completeStatus, projectCompleteStatus, s);
    }

    public static PopupWindow updateTaskStatus(boolean isSubTask, RxAppCompatActivity mActivity, View view, JSONObject jsonObject, boolean completeStatus, String projectCompleteStatus, Subscriber<BaseBean> s) {
        if (completeStatus) {
            //激活
            if ("1".equals(projectCompleteStatus)) {
                //需要激活原因
                PopupWindow popupWindow = DialogUtils.getInstance().inputDialog(mActivity, "激活原因", null, "必填", view, content -> {
                    if (TextUtil.isEmpty(content)) {
                        ToastUtils.showError(mActivity, "请输入激活原因");
                        return false;
                    }
                    jsonObject.put("remark", content);
                    updateStatus(isSubTask, mActivity, jsonObject, s);
                    return true;
                });
                return popupWindow;
            } else {
                DialogUtils.getInstance().sureOrCancel(mActivity, mActivity.getString(R.string.hint), "确定要激活该任务？", view
                        , () -> updateStatus(isSubTask, mActivity, jsonObject, s));
            }
        } else {
            //完成
            DialogUtils.getInstance().sureOrCancel(mActivity, mActivity.getString(R.string.hint), "确定要完成该任务？", view
                    , () -> updateStatus(isSubTask, mActivity, jsonObject, s));
        }
        return null;
    }

    private static void updateStatus(boolean isSubTask, RxAppCompatActivity mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        if (isSubTask) {
            new TaskModel().updateSubStatus(mActivity, jsonObject, s);
        } else {
            new TaskModel().updateStatus(mActivity, jsonObject, s);
        }
    }


    /**
     * 更新个人任务状态
     *
     * @param mActivity
     * @param view
     * @param taskId
     * @param completeStatus
     * @param s
     */
    public static PopupWindow updatePersonalTaskStatus(boolean isSubTask, RxAppCompatActivity mActivity, View view, long taskId, boolean completeStatus, Subscriber<BaseBean> s) {
        if (completeStatus) {
            //激活
            DialogUtils.getInstance().sureOrCancel(mActivity, mActivity.getString(R.string.hint), "确定要激活该任务？", view
                    , () -> updatePersonalStatus(isSubTask, mActivity, taskId, s));
        } else {
            //完成
            DialogUtils.getInstance().sureOrCancel(mActivity, mActivity.getString(R.string.hint), "确定要完成该任务？", view
                    , () -> updatePersonalStatus(isSubTask, mActivity, taskId, s));
        }
        return null;
    }

    private static void updatePersonalStatus(boolean isSubTask, RxAppCompatActivity mActivity, long taskId, Subscriber<BaseBean> s) {
        if (isSubTask) {
            new TaskModel().updatePersonelSubTaskStatus(mActivity, taskId, s);
        } else {
            new TaskModel().updatePersonelTaskStatus(mActivity, taskId, s);
        }
    }
}
