package com.hjhq.teamface.basis.util.dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.bean.IconButtonBean;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.EncodingUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mou
 *
 * @author Administrator
 */
public class DialogUtils {
    private static DialogUtils instance;

    private DialogUtils() {
    }

    public synchronized static DialogUtils getInstance() {
        if (instance == null) {
            instance = new DialogUtils();
        }
        return instance;
    }

    /**
     * 确定和取消的接口
     */
    public interface OnShareClickListner {
        void clickSure(String desc);

    }

    /**
     * 输入的接口
     */
    public interface OnInputClickListner {
        boolean inputClickSure(String content);
    }

    /**
     * 确定和取消的接口
     */
    public interface OnClickSureListener {
        void clickSure();
    }

    public interface OnClickSureOrCancelListener {
        void clickSure();

        void clickCancel();
    }

    /**
     * 确定对话框
     *
     * @param activity
     * @param title      标题
     * @param subTitle   内容
     * @param sendName   确定按钮名称
     * @param cancelName 取消按钮名称
     * @param root
     * @param listener
     */
    public void sureOrCancel(final Activity activity, String title, String subTitle, String sendName, String cancelName, View root, OnClickSureListener listener) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_sure_cancel, null);
        TextView tvTitle = mResendMailPopupView.findViewById(R.id.dialog_resend_mail_tv_tip);
        TextView tvSubTitle = mResendMailPopupView.findViewById(R.id.dialog_resend_mail_tv_subTitle);
        if (TextUtil.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        if (TextUtil.isEmpty(subTitle)) {
            tvSubTitle.setVisibility(View.GONE);
        } else {
            tvSubTitle.setText(subTitle);
        }
        PopupWindow mResendMailPopup = initDisPlay(activity, dm, mResendMailPopupView);
        /**
         * 点击了确定
         */
        TextView sendView = mResendMailPopupView.findViewById(R.id.dialog_resend_mail_btnSure);
        TextUtil.setText(sendView, sendName);
        sendView.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
                ScreenUtils.letScreenLight(activity);
                if (listener != null) {
                    listener.clickSure();
                }
            }
        });


        /**
         * 点击了取消
         */
        TextView cancel = mResendMailPopupView.findViewById(R.id.dialog_resend_mail_btnRefuse);
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

    /**
     * 有确定和取消回调的弹框
     *
     * @param activity
     * @param title
     * @param subTitle
     * @param root
     * @param sureString
     * @param cancelString
     * @param listener
     */
    public void sureOrCancel(final Activity activity, String title, String subTitle,
                             View root, String sureString, String cancelString, OnClickSureOrCancelListener listener) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_sure_cancel, null);
        TextView tvTitle = mResendMailPopupView.findViewById(R.id.dialog_resend_mail_tv_tip);
        TextView tvSubTitle = mResendMailPopupView.findViewById(R.id.dialog_resend_mail_tv_subTitle);

        if (TextUtil.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        if (TextUtil.isEmpty(subTitle)) {
            tvSubTitle.setVisibility(View.GONE);
        } else {
            tvSubTitle.setText(subTitle);
        }

        PopupWindow mResendMailPopup = initDisPlay(activity, dm, mResendMailPopupView);
        //点击区域外不关闭
        mResendMailPopup.setOutsideTouchable(false);
        mResendMailPopup.setTouchInterceptor((v, event) -> {
            if (!mResendMailPopup.isOutsideTouchable()) {
                View mView = mResendMailPopup.getContentView();
                if (null != mView) {
                    mView.dispatchTouchEvent(event);
                }
            }
            return mResendMailPopup.isFocusable() && !mResendMailPopup.isOutsideTouchable();
        });
        mResendMailPopup.setOnDismissListener(() -> {
            ScreenUtils.letScreenLight(activity);
            if (listener != null) {
                listener.clickCancel();
            }
        });
        /**
         * 点击了确定
         */
        Button sendView = mResendMailPopupView.findViewById(R.id.dialog_resend_mail_btnSure);
        TextUtil.setText(sendView, sureString);
        sendView.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                //点击确定，就不再回调 clickCancel 方法
                mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
                mResendMailPopup.dismiss();
                ScreenUtils.letScreenLight(activity);
                if (listener != null) {
                    listener.clickSure();
                }
            }
        });


        /**
         * 点击了取消
         */
        Button cancel = mResendMailPopupView.findViewById(R.id.dialog_resend_mail_btnRefuse);
        TextUtil.setText(cancel, cancelString);
        cancel.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                //点击取消，就不再回调 clickCancel 方法
                mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));

                mResendMailPopup.dismiss();
                ScreenUtils.letScreenLight(activity);
                if (listener != null) {
                    listener.clickCancel();
                }
            }
        });

        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
    }

    public void noAuthView(final Activity activity, String title, String subTitle, String time, View root, OnClickSureListener listener) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_no_auth_preview, null);
        TextView tvTitle = (TextView) mResendMailPopupView.findViewById(R.id.tv_name);
        TextView tvWho = (TextView) mResendMailPopupView.findViewById(R.id.sender_name);
        TextView tvTime = (TextView) mResendMailPopupView.findViewById(R.id.tv_receive_file_size);
        TextUtil.setText(tvTitle, title);
        TextUtil.setText(tvWho, subTitle);
        TextUtil.setText(tvTime, time);


        PopupWindow mResendMailPopup = initDisPlay(activity, dm, mResendMailPopupView);
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
    }

    public void sureOrCancel(final Activity activity, String title, String subTitle, View root, OnClickSureListener listener) {
        sureOrCancel(activity, title, subTitle, "确定", "取消", root, listener);
    }

    /**
     * 查看条形码
     *
     * @param activity
     * @param title
     * @param root
     * @param listener
     */
    public void showBarcode(final Activity activity, String title, String content, View root, OnClickSureListener listener) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View rootView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_show_barcode_layout, null);
        TextView tvTitle = rootView.findViewById(R.id.dialog_resend_mail_tv_tip);
        ImageView imageView = rootView.findViewById(R.id.iv);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = EncodingUtils.createBarcode(activity, content, imageView.getWidth(), imageView.getHeight(), false);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        });

        if (TextUtil.isEmpty(content)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(content);
            tvTitle.setVisibility(View.VISIBLE);
        }
        PopupWindow mResendMailPopup = initDisPlay(activity, dm, rootView);
        /**
         * 点击了确定
         */
        TextView sendView = rootView.findViewById(R.id.dialog_resend_mail_btnSure);
        sendView.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
                ScreenUtils.letScreenLight(activity);
                if (listener != null) {
                    listener.clickSure();
                }
            }
        });
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
    }

    private PopupWindow initDisPlay(Activity activity, DisplayMetrics dm, View mResendMailPopupView) {
        return initDisPlay(activity, dm, mResendMailPopupView, true);
    }

    private PopupWindow initDisPlay(Activity activity, DisplayMetrics dm, View mResendMailPopupView, boolean bl) {
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

    /**
     * 发送到聊天
     *
     * @param activity
     * @param contactName
     * @param messageType
     * @param fileName
     * @param photograph
     * @param root
     * @param listner
     * @return
     */
    public DialogUtils share2cantact(final Activity activity, String contactName, String msgContent, String messageType,
                                     String fileName, String photograph, View root, OnShareClickListner listner) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_share_contact, null);
        ImageView avatar = (ImageView) mResendMailPopupView.findViewById(R.id.dialog_share_contact_ivIcon);
        TextView tvName = (TextView) mResendMailPopupView.findViewById(R.id.dialog_share_contact_tvName);
        TextView tvFileType = (TextView) mResendMailPopupView.findViewById(R.id.dialog_share_contact_fileType);
        TextView tvFileName = (TextView) mResendMailPopupView.findViewById(R.id.dialog_share_contact_fileName);
        EditText mEtDesc = (EditText) mResendMailPopupView.findViewById(R.id.dialog_share_contact_et_desc);
        Button btnCancel = (Button) mResendMailPopupView.findViewById(R.id.dialog_share_contact_btn_cancel);
        Button btnSure = (Button) mResendMailPopupView.findViewById(R.id.dialog_share_contact_btn_sure);
        tvName.setText(contactName);
        tvFileType.setText("[" + messageType + "]");
        if (TextUtil.isEmpty(fileName)) {
            tvFileName.setText(msgContent + " ");
        } else {
            tvFileName.setText(fileName + " ");
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ImageLoader.loadCircleImage(avatar.getContext(), photograph, avatar, contactName);
        PopupWindow mResendMailPopup = initDisPlay(activity, dm, mResendMailPopupView);
        /**
         * 点击了确定
         */
        btnSure.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {


                if (listner != null) {
                    String etDescStr = mEtDesc.getText().toString().trim();
                    if (TextUtils.isEmpty(etDescStr)) {
                        listner.clickSure("");
                    } else {
                        listner.clickSure(etDescStr);
                    }
                }
                mResendMailPopup.dismiss();
            }
        });

        /**
         * 点击了取消
         */
        btnCancel.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
            }
        });
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
        return instance;
    }


    public PopupWindow inputDialog(Activity activity, CharSequence title, CharSequence subTitle, String inputHint, View root, OnInputClickListner listener) {
        return inputDialog(activity, title, subTitle, inputHint, activity.getString(R.string.confirm), activity.getString(R.string.cancel), root, listener);
    }

    /**
     * @param activity   活动界面
     * @param title      标题
     * @param subTitle   副标题
     * @param inputHint  输入框提示
     * @param sendName   确定按钮名称
     * @param cancelName 取消按钮名称
     * @param root       布局控件
     * @param listener   监听
     */
    public PopupWindow inputDialog(Activity activity, CharSequence title, CharSequence subTitle, String inputHint, String sendName, String cancelName, View root, OnInputClickListner listener) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_input, null);

        TextView tvTitle = mResendMailPopupView.findViewById(R.id.tv_dialog_title);
        TextView tvSubTitle = mResendMailPopupView.findViewById(R.id.tv_dialog_subTitle);
        EditText dialogInput = mResendMailPopupView.findViewById(R.id.et_dialog_input);

        TextUtil.setTextorVisibility(tvTitle, title);
        TextUtil.setTextorVisibility(tvSubTitle, subTitle);
        TextUtil.setHint(dialogInput, inputHint);

        PopupWindow mResendMailPopup = initDisPlay(activity, dm, mResendMailPopupView);

        //点击了确定
        TextView sendView = mResendMailPopupView.findViewById(R.id.dialog_btnSure);
        TextUtil.setText(sendView, sendName);
        sendView.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                if (listener != null) {
                    boolean bl = listener.inputClickSure(dialogInput.getText().toString());
                    if (bl) {
                        mResendMailPopup.dismiss();
                        ScreenUtils.letScreenLight(activity);
                    }
                } else {
                    ScreenUtils.letScreenLight(activity);
                    mResendMailPopup.dismiss();
                }
            }
        });

        //点击了取消
        TextView cancel = mResendMailPopupView.findViewById(R.id.dialog_btnCancel);
        TextUtil.setText(cancel, cancelName);
        cancel.setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
                ScreenUtils.letScreenLight(activity);
            }
        });
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
        return mResendMailPopup;
    }


    public void sureDialog(Activity activity, String title, String subTitle, View root) {
        sureDialog(activity, title, subTitle, root, null);
    }

    /**
     * 确认性弹窗
     *
     * @param activity
     * @param title
     * @param subTitle
     * @param root
     * @param mOnClickSureListener
     */
    public void sureDialog(Activity activity, String title, String subTitle, View root, OnClickSureListener mOnClickSureListener) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_sure_layout, null);
        TextView tvTitle = mResendMailPopupView.findViewById(R.id.dialog_title);
        TextView tvSubTitle = mResendMailPopupView.findViewById(R.id.dialog_subTitle);

        TextUtil.setTextorVisibility(tvTitle, title);
        TextUtil.setTextorVisibility(tvSubTitle, subTitle);

        PopupWindow mResendMailPopup = initDisPlay(activity, dm, mResendMailPopupView);

        //点击区域外不关闭
        mResendMailPopup.setOutsideTouchable(false);
        mResendMailPopup.setTouchInterceptor((v, event) -> {
            if (!mResendMailPopup.isOutsideTouchable()) {
                View mView = mResendMailPopup.getContentView();
                if (null != mView) {
                    mView.dispatchTouchEvent(event);
                }
            }
            return mResendMailPopup.isFocusable() && !mResendMailPopup.isOutsideTouchable();
        });

        mResendMailPopupView.findViewById(R.id.dialog_btnSure).setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
                ScreenUtils.letScreenLight(activity);
                if (mOnClickSureListener != null) {
                    mOnClickSureListener.clickSure();
                }
            }
        });

        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.CENTER, 0, 0);
    }


    /**
     * 列数
     */
    public static final int NUMCOLUMNS = 4;

    /**
     * 底部弹窗
     *
     * @param activity
     * @param root
     * @param menuArray
     * @param resIds
     */
    public void bottomDialog(Activity activity, View root, String title, String[] menuArray, int[] resIds, AdapterView.OnItemClickListener listener) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if (menuArray == null || resIds == null || resIds.length != menuArray.length) {
            LogUtil.e("底部弹窗数据有问题");
            return;
        }

        List<IconButtonBean> data = new ArrayList<>();
        for (int i = 0; i < menuArray.length; i++) {
            IconButtonBean bean = new IconButtonBean(menuArray[i], resIds[i]);
            data.add(bean);
        }

        int i = data.size() % NUMCOLUMNS == 0 ? 0 : NUMCOLUMNS - data.size() % NUMCOLUMNS;
        for (int j = 0; j < i; j++) {
            data.add(null);
        }

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        View mResendMailPopupView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_bottom_choose, null);
        TextView tvTitle = (TextView) mResendMailPopupView.findViewById(R.id.dialog_bottom_tv_title);
        GridView mGridView = (GridView) mResendMailPopupView.findViewById(R.id.m_gridview);
        mGridView.setNumColumns(NUMCOLUMNS);
        mGridView.setAdapter(new DialogAdapter(activity, data));

        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        PopupWindow mResendMailPopup = initDisPlay(activity, dm, mResendMailPopupView, false);

        mGridView.setOnItemClickListener((viewParent, viewView, viewPosition, viewId) -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
            }
            listener.onItemClick(viewParent, viewView, viewPosition, viewId);
        });

        /**
         * 点击了取消
         */
        mResendMailPopupView.findViewById(R.id.dialog_bottom_btnCancel).setOnClickListener(view -> {
            if (mResendMailPopup != null && mResendMailPopup.isShowing()) {
                mResendMailPopup.dismiss();
            }
        });
        mResendMailPopup.setOnDismissListener(() -> ScreenUtils.letScreenLight(activity));
        mResendMailPopup.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    class DialogAdapter extends BaseAdapter {
        List<IconButtonBean> data;
        Activity mActivity;

        public DialogAdapter(Activity activity, List<IconButtonBean> data) {
            this.data = data;
            this.mActivity = activity;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public IconButtonBean getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            IconButtonBean item = getItem(position);

            if (convertView == null) {
                // 通过动态布局加载器得到视图转换器对象
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_bottom_dialog, null);
                // 设置控件集到convertView
                convertView.setTag(convertView);
            } else {
                convertView = (View) convertView.getTag();
            }
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_title);
            View line = convertView.findViewById(R.id.line);

            if (item == null) {
                tvName.setText("");
                ivIcon.setImageResource(R.drawable.icon_transparent);
            } else {
                tvName.setText(item.getTitle());
                ImageLoader.loadImage(mActivity, item.getIcon(), ivIcon);
            }

            line.setVisibility(getCount() - NUMCOLUMNS > position ? View.VISIBLE : View.GONE);

            return convertView;
        }
    }


}
