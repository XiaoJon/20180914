package com.hjhq.teamface.basis.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 仿IOS风格底部弹窗
 *
 * @author Administrator
 */
public class ListDialog {
    private Context context;
    private Dialog dialog;
    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvCancel;
    private LinearLayout lLayoutContent;
    private ScrollView sLayoutContent;
    private boolean showTitle = false;
    private boolean showSubTitle = false;
    private List<SheetItem> sheetItemList;
    private Display display;
    private View titleLine;
    private View view;

    public ListDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ListDialog builder() {
        // 获取Dialog布局
        view = LayoutInflater.from(context).inflate(
                R.layout.view_action_sheet, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 获取自定义Dialog布局中的控件
        sLayoutContent = view.findViewById(R.id.sLayout_content);
        lLayoutContent = view.findViewById(R.id.lLayout_content);
        tvTitle = view.findViewById(R.id.txt_title);
        tvSubTitle = view.findViewById(R.id.tv_sub_title);
        titleLine = view.findViewById(R.id.title_line);
        tvCancel = view.findViewById(R.id.txt_cancel);
        tvCancel.setOnClickListener(v -> dialog.dismiss());

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ListDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    /**
     * 标题
     *
     * @param title
     * @return
     */
    public ListDialog setTitle(String title) {
        TextUtil.setTextorVisibility(tvTitle, title);
        showTitle = !TextUtil.isEmpty(title);
        titleLine.setVisibility(showTitle ? View.VISIBLE : View.GONE);
        titleLine.setVisibility(View.GONE);
        return this;
    }

    /**
     * 副标题
     *
     * @param title
     * @return
     */
    public ListDialog setSubtitle(String title) {
        TextUtil.setTextorVisibility(tvSubTitle, title);
        showSubTitle = !TextUtil.isEmpty(title);
        titleLine.setVisibility(showSubTitle ? View.VISIBLE : View.GONE);
        titleLine.setVisibility(View.GONE);
        return this;
    }

    public ListDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ListDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * @param strItem  条目名称
     * @param color    条目字体颜色，设置null则默认蓝色
     * @param listener
     * @return
     */
    public ListDialog addSheetItem(String strItem, SheetItemColor color,
                                   OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<>();
        }
        sheetItemList.add(new SheetItem(strItem, color, listener));
        return this;
    }

    public ListDialog addSheetItem(List<SheetItem> list) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<>();
        }
        sheetItemList.addAll(list);
        return this;
    }

    /**
     * 设置条目布局
     */
    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }

        // item高度
        float scale = context.getResources().getDisplayMetrics().density;
        int itemHeight = (int) (45 * scale + 0.5f);

        //屏幕最多容纳item的数量
        int screenItemCount = display.getHeight() / itemHeight / 2;

        int size = sheetItemList.size();

        // 添加条目过多的时候控制高度
        if (size >= screenItemCount) {
            LayoutParams params = (LayoutParams) sLayoutContent
                    .getLayoutParams();
            params.height = itemHeight * screenItemCount;
            sLayoutContent.setLayoutParams(params);
        }

        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            final int index = i;
            SheetItem sheetItem = sheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            final OnSheetItemClickListener listener = sheetItem.itemClickListener;

            TextView textView = new TextView(context);
            textView.setText(strItem);
            textView.setTextSize(18);
            textView.setTextColor(R.color.app_blue);
            textView.setGravity(Gravity.CENTER);

            // 背景图片
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    }
                }
            }

            // 字体颜色
            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .getName()));
            } else {
                textView.setTextColor(Color.parseColor(color.getName()));
            }

            textView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, itemHeight));

            // 点击事件
            textView.setOnClickListener(v -> {
                listener.onClick(index - 1);
                dialog.dismiss();
            });

            if (i != 1) {
                View line = new View(context);
                line.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, ((int) DeviceUtils.dpToPixel(context, 1))));
                line.setBackgroundResource(R.color.line_color);
                lLayoutContent.addView(line);
            }
            lLayoutContent.addView(textView);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public Dialog getDialog() {
        return dialog;
    }

    public View getView() {
        return view;
    }
}
