package com.hjhq.teamface.customcomponent.widget2.barcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.GetBarcodeBean;
import com.hjhq.teamface.basis.bean.GetBarcodeImgBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.activity.CaptureActivity;
import com.hjhq.teamface.common.activity.ViewBarcodeImageActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.customcomponent.CustomComponentModel;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.customcomponent.widget2.InputFocusInterface;

/**
 * Created by Administrator on 2018/8/17.
 * Describe：条形码组件
 */

public class BarcodeView extends BaseView implements ActivityPresenter.OnActivityResult, InputFocusInterface {
    protected Activity mActivity;
    protected EditText etContent;
    protected TextView tvContent;
    protected TextView tvTitle;
    protected ImageView ivScan;
    protected ImageView ivGet;
    protected ImageView ivViewBarcode;
    private String barcodeValue = "";
    private String barcodeType = "";
    private String beanName;
    private boolean isEdit = true;
    private View rlContent;

    public BarcodeView(CustomBean bean) {
        super(bean);
        beanName = bean.getModuleBean();
    }

    @Override
    protected void setData(Object value) {
        if (value != null && value instanceof String) {
            String valueStr = (String) value;
            TextUtil.setText(etContent, valueStr);
            TextUtil.setText(tvContent, valueStr);
            barcodeValue = valueStr;
        }

    }

    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {
        this.mActivity = activity;
        mView = View.inflate(mActivity, R.layout.custom_barcode_view, null);
        // TODO: 2018/8/17 编辑或查看
        rlContent = mView.findViewById(R.id.rl_content);
        etContent = mView.findViewById(R.id.et);
        tvContent = mView.findViewById(R.id.tv);
        tvTitle = mView.findViewById(R.id.tv_title);
        ivScan = mView.findViewById(R.id.iv_scan);
        ivGet = mView.findViewById(R.id.iv_get);
        ivViewBarcode = mView.findViewById(R.id.iv_show);
        switch (state) {
            case CustomConstants.APPROVE_DETAIL_STATE:
                if (CustomConstants.FIELD_READ.equals(fieldControl)) {
                    detailState();
                } else {
                    editState();
                }
                break;
            case CustomConstants.ADD_STATE:
            case CustomConstants.EDIT_STATE:
                editState();
                break;
            case CustomConstants.DETAIL_STATE:
                detailState();
                break;
            default:
                break;


        }
        setData(bean.getValue());
        barcodeType = bean.getField().getCodeStyle();
        setTitle(tvTitle, title);
        parent.addView(mView);
        initOption();
    }

    /**
     * 详情/展示状态
     */
    private void detailState() {
        tvContent.setVisibility(View.VISIBLE);
        ivViewBarcode.setVisibility(View.VISIBLE);
        etContent.setVisibility(View.GONE);
        ivGet.setVisibility(View.GONE);
        ivScan.setVisibility(View.GONE);
    }

    /**
     * 编辑状态
     */
    private void editState() {
        tvContent.setVisibility(View.GONE);
        ivViewBarcode.setVisibility(View.GONE);
        etContent.setVisibility(View.VISIBLE);
        ivGet.setVisibility(View.VISIBLE);
        ivScan.setVisibility(View.VISIBLE);
        etContent.setOnFocusChangeListener((v, hasFocus) -> {
            LayerDrawable layerDrawable = (LayerDrawable) rlContent.getBackground();
            Drawable drawable = layerDrawable.getDrawable(0);
            if (drawable instanceof GradientDrawable) {
                ((GradientDrawable) drawable).setColor(ColorUtils.hexToColor(hasFocus ? "#008FE5" : "#D5D5D5"));
            }
        });

        etContent.setOnClickListener(v ->{
            etContent.setFocusable(true);//设置输入框可聚集
            etContent.setFocusableInTouchMode(true);//设置触摸聚焦
            etContent.requestFocus();//请求焦点
            etContent.findFocus();//获取焦点
        });
    }


    private void initOption() {
        //清除焦点%
        RxManager.$(aHashCode).on(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, o -> clearFocus());
        //扫描条形码
        ivScan.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DATA_TAG1, CaptureActivity.FOR_BARCODE);
            ((ActivityPresenter) getContext()).setOnActivityResult(code, this);
            CommonUtil.startActivtiyForResult(mActivity, CaptureActivity.class, code, bundle);
        });
        //查看条形码
        ivViewBarcode.setOnClickListener(v -> {
            new CustomComponentModel().getBarcodeMsg(((ActivityPresenter) mActivity), beanName,
                    barcodeValue, new ProgressSubscriber<GetBarcodeImgBean>(mActivity, true) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            //ToastUtils.showToast(mActivity, "错误");
                        }

                        @Override
                        public void onNext(GetBarcodeImgBean baseBean) {
                            super.onNext(baseBean);
                            if (baseBean != null && baseBean.getData() != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.DATA_TAG1, baseBean.getData().getBarcodePic());
                                bundle.putString(Constants.DATA_TAG2, barcodeValue);
                                CommonUtil.startActivtiy(mActivity, ViewBarcodeImageActivity.class, bundle);
                            }

                        }

                    });
        });
        //获取条形码
        ivGet.setOnClickListener(v -> {
            new CustomComponentModel().createBarcode(((ActivityPresenter) mActivity), "",
                    barcodeValue, new ProgressSubscriber<GetBarcodeBean>(mActivity, true) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onNext(GetBarcodeBean baseBean) {
                            super.onNext(baseBean);
                            if (baseBean != null && baseBean.getData() != null) {
                                barcodeValue = baseBean.getData().getBarcodeValue();
                                if (TextUtils.isEmpty(barcodeValue)) {
                                    ToastUtils.showToast(mActivity, baseBean.getResponse().getDescribe());
                                } else {
                                    TextUtil.setText(etContent, barcodeValue);
                                    TextUtil.setText(tvContent, barcodeValue);
                                }

                            }

                        }

                    });

        });

    }

    @Override
    public void put(JSONObject jsonObj) {
        jsonObj.put(keyName, etContent.getText().toString());

    }

    @Override
    public boolean checkNull() {
        return TextUtils.isEmpty(etContent.getText().toString());
    }


    @Override
    public boolean formatCheck() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode && requestCode == code) {
            if (data != null) {
                String content = data.getStringExtra(Constants.DATA_TAG1);
                if (!TextUtils.isEmpty(content)) {
                    mView.post(new Runnable() {
                        @Override
                        public void run() {
                            // showBarcode(content);
                            TextUtil.setText(etContent, content);
                            TextUtil.setText(tvContent, content);
                        }
                    });
                }

            }
        }
    }

    private void showBarcode(String content) {
        DialogUtils.getInstance().showBarcode(mActivity, "", content, mView, new DialogUtils.OnClickSureListener() {
            @Override
            public void clickSure() {

            }
        });
    }

    public void clearFocus() {
        if (etContent != null) {
            etContent.setFocusable(false);//设置输入框不可聚焦，即失去焦点和光标
        }
    }
}
