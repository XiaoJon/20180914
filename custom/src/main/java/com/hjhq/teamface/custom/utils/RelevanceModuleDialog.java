package com.hjhq.teamface.custom.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hjhq.teamface.basis.view.lable.entity.GameLabel;
import com.hjhq.teamface.basis.view.lable.inface.IOnItemClickListener;
import com.hjhq.teamface.basis.view.lable.view.LabelListView;
import com.hjhq.teamface.custom.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 关联模块弹窗
 *
 * @author Administrator
 */
public class RelevanceModuleDialog {
    private Context context;
    private Dialog dialog;
    private boolean showTitle = false;
    private Display display;
    private LabelListView labelListView;
    private List<GameLabel> labelList = new ArrayList<>();
    private View ivDel;
    IOnItemClickListener listener;

    public RelevanceModuleDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public RelevanceModuleDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.custom_dialog_relevance_module, null);

        // 设置Dialog最小宽度为屏幕宽度
        int width = display.getWidth();
        view.setMinimumWidth(width);

        // 获取自定义Dialog布局中的控件
        labelListView = view.findViewById(R.id.label_list_view);
        ivDel = view.findViewById(R.id.iv_del);
        ivDel.setOnClickListener(v -> dialog.dismiss());

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    public RelevanceModuleDialog addLabel(List<GameLabel> list) {
        if (list == null) {
            labelList = new ArrayList<>();
        } else {
            labelList = list;
        }
        return this;
    }

    public RelevanceModuleDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public RelevanceModuleDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 设置条目布局
     */
    private void setLabel() {
        labelListView.setData(labelList);
        if (listener != null) {
            labelListView.setOnClickListener(listener);
        }
    }

    public void show() {
        setLabel();
        dialog.show();
    }


    public RelevanceModuleDialog setListener(IOnItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public enum SheetItemColor {
        Black("#212121"), Blue("#3689E9"), Red("#FC591F");

        private String name;

        private SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
