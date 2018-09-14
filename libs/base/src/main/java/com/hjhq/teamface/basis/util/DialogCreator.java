package com.hjhq.teamface.basis.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.R;


public class DialogCreator {
    public static Dialog createLoadingDialog(Context context, String msg) {
        Dialog loadingDialog = new Dialog(context, R.style.jmui_loading_dialog_style);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.jmui_loading_view, null);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.jmui_dialog_view);
        ImageView mLoadImg = (ImageView) v.findViewById(R.id.jmui_loading_img);
        TextView mLoadText = (TextView) v.findViewById(R.id.jmui_loading_txt);
        AnimationDrawable mDrawable = (AnimationDrawable) mLoadImg.getDrawable();
        mDrawable.start();
        mLoadText.setText(msg);
        loadingDialog.setCancelable(true);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }

    public static Dialog createBaseCustomDialog(Context context, String title, String text,
                                                View.OnClickListener onClickListener) {
        Dialog baseDialog = new Dialog(context, R.style.jmui_default_dialog_style);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.jmui_dialog_base, null);
        TextView titleTv = (TextView) v.findViewById(R.id.jmui_dialog_base_title_tv);
        TextView textTv = (TextView) v.findViewById(R.id.jmui_dialog_base_text_tv);
        Button confirmBtn = (Button) v.findViewById(R.id.jmui_dialog_base_confirm_btn);
        titleTv.setText(title);
        textTv.setText(text);
        confirmBtn.setOnClickListener(onClickListener);
        baseDialog.setContentView(v);
        baseDialog.setCancelable(false);
        return baseDialog;
    }

    public static Dialog createLongPressMessageDialog(Context context, String title, boolean hide,
                                                      View.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.jmui_dialog_msg_alert, null);
        builder.setView(view);
        Button copyBtn = (Button) view.findViewById(R.id.jmui_copy_msg_btn);
        Button forwardBtn = (Button) view.findViewById(R.id.jmui_forward_msg_btn);
        View line1 = view.findViewById(R.id.jmui_forward_split_line);
        View line2 = view.findViewById(R.id.jmui_delete_split_line);
        Button deleteBtn = (Button) view.findViewById(R.id.jmui_delete_msg_btn);
        final TextView titleTv = (TextView) view.findViewById(R.id.jmui_dialog_title);
        if (hide) {
            copyBtn.setVisibility(View.GONE);
            forwardBtn.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
        }
        titleTv.setText(title);
        final Dialog dialog = builder.create();
        copyBtn.setOnClickListener(listener);
        forwardBtn.setOnClickListener(listener);
        deleteBtn.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public static Dialog createResendDialog(Context context, View.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.jmui_dialog_base_with_button, null);
        builder.setView(view);
        Button cancelBtn = (Button) view.findViewById(R.id.jmui_cancel_btn);
        Button resendBtn = (Button) view.findViewById(R.id.jmui_commit_btn);
        final Dialog dialog = builder.create();
        cancelBtn.setOnClickListener(listener);
        resendBtn.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public static Dialog createDelConversationDialog(Context context, String title,
                                                     View.OnClickListener listener) {
        Dialog dialog = new Dialog(context, R.style.jmui_default_dialog_style);
        View v = LayoutInflater.from(context).inflate(R.layout.jmui_dialog_delete_conv, null);
        dialog.setContentView(v);
        TextView titleTv = (TextView) v.findViewById(R.id.jmui_dialog_title);
        final LinearLayout deleteLl = (LinearLayout) v.findViewById(R.id.jmui_delete_conv_ll);
        titleTv.setText(title);
        deleteLl.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public static Dialog createDeleteMessageDialog(Context context, View.OnClickListener listener) {
        Dialog dialog = new Dialog(context, R.style.jmui_default_dialog_style);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.jmui_dialog_base_with_button, null);
        dialog.setContentView(v);
        TextView title = (TextView) v.findViewById(R.id.jmui_title);
        title.setText(R.string.jmui_clear_history_confirm_title);
        final Button cancel = (Button) v.findViewById(R.id.jmui_cancel_btn);
        final Button commit = (Button) v.findViewById(R.id.jmui_commit_btn);
        commit.setText(R.string.confirm);
        cancel.setOnClickListener(listener);
        commit.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public static Dialog createExitGroupDialog(Context context, View.OnClickListener listener) {
        Dialog dialog = new Dialog(context, R.style.jmui_default_dialog_style);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.jmui_dialog_base_with_button, null);
        dialog.setContentView(v);
        TextView title = (TextView) v.findViewById(R.id.jmui_title);
        title.setText(R.string.jmui_delete_group_confirm_title);
        final Button cancel = (Button) v.findViewById(R.id.jmui_cancel_btn);
        final Button commit = (Button) v.findViewById(R.id.jmui_commit_btn);
        commit.setText(R.string.confirm);
        cancel.setOnClickListener(listener);
        commit.setOnClickListener(listener);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


}
