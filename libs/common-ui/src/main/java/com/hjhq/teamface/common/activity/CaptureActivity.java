package com.hjhq.teamface.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.hjhq.lib_zxing.ZxingUtils;
import com.hjhq.lib_zxing.activity.CaptureFragment;
import com.hjhq.lib_zxing.activity.CodeUtils;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.QueryBarcodeDataBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonApiService;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.uuzuche.lib_zxing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Initial the camera
 * <p>
 * 默认的二维码扫描Activity
 *
 * @author Administrator
 */
public class CaptureActivity extends ActivityPresenter<CaptureDelegate, CommonModel> {
    public static final int FOR_RESULT = 1;
    public static final int FOR_SCAN = 2;
    public static final int FOR_BARCODE = 3;
    public static final String FLAG = "flag";
    private Pattern p = Pattern.compile("^((http|ftp|https)://).{1,1000}$", Pattern.CASE_INSENSITIVE);
    private FrameLayout rootView;
    private CaptureFragment captureFragment;
    private int scanType = FOR_RESULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SystemFuncUtils.requestPermissions(mContext, android.Manifest.permission.CAMERA, aBoolean -> {
            if (!aBoolean) {
                ToastUtils.showError(mContext, "必须获得必要的权限才能正常使用！");
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scanType = bundle.getInt(Constants.DATA_TAG1);
        }
        captureFragment = new CaptureFragment();
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();
    }

    @Override
    public void init() {
        initView();
    }


    protected void initView() {
        rootView = (FrameLayout) findViewById(R.id.fl_zxing_container);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.get(R.id.tv_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.getImageFromAlbumByMultiple(mContext, 1);
            }
        });
        viewDelegate.get(R.id.tv_flash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (captureFragment != null) {
                    captureFragment.switchFlash();
                }
            }
        });
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            handleResult(result);
            /*switch (scanType) {
                case FOR_BARCODE:
                    handleBarcode(getFromat(mBitmap), result);
                    break;
                case FOR_RESULT:
                    handleResult(Activity.RESULT_OK, getFromat(mBitmap), result);
                    break;
                case FOR_SCAN:

                    break;
            }*/

        }

        @Override
        public void onAnalyzeFailed() {
            captureFragment.restartPreview();
            /*Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(Activity.RESULT_OK, resultIntent);
            CaptureActivity.this.finish();*/
        }
    };

    private BarcodeFormat getFromat(Bitmap mBitmap) {

        int[] pixels = new int[mBitmap.getWidth() * mBitmap.getHeight()];
        mBitmap.getPixels(pixels, 0, mBitmap.getWidth(), 0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(mBitmap.getWidth(), mBitmap.getHeight(), pixels);
        BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        try {
            final Result result2 = reader.decodeWithState(bb);
            return result2.getBarcodeFormat();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理扫码结果
     *
     * @param result
     */
    private void handleResult(String result) {
        if (TextUtils.isEmpty(result)) {
            showPreview(result);
            return;
        }

        if (scanType == FOR_BARCODE) {
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA_TAG1, result);
            setResult(RESULT_OK, intent);
            finish();
            return;
        } else if (scanType == FOR_RESULT) {
            if (result.contains("ScanLogin") || result.contains("fileLink")) {
                handleQRcode(result);
            } else {
                model.findDetailByBarcode(mContext, result,
                        new ProgressSubscriber<QueryBarcodeDataBean>(mContext) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                showPreview(result);
                                return;
                            }

                            @Override
                            public void onNext(QueryBarcodeDataBean baseBean) {
                                super.onNext(baseBean);
                                if (baseBean != null && baseBean.getData() != null) {
                                    if ("1".equals(baseBean.getData().getReadAuth())) {
                                        viewDataDetail(result, baseBean);
                                        return;
                                    } else {
                                        showPreview(result);
                                        return;
                                    }


                                }

                            }
                        });
            }
        }
    }

    /**
     * 处理条形码结果
     *
     * @param format
     * @param result
     */
    private void handleBarcode(BarcodeFormat format, String result) {

        Log.e("条形码格式", format.name());
        switch (format.name()) {
            //商品条形码
            case "UPC_A":
            case "UPC_E":
            case "UPC_EAN_EXTENSION":
            case "EAN_8":
            case "EAN_13":
                //普通条形码
            case "CODABAR":
            case "CODE_39":
            case "CODE_93":
            case "CODE_128":
            case "ITF":
            case "PDF_417":
            case "RSS_14":
            case "RSS_EXPANDED":
                if (scanType == FOR_BARCODE) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DATA_TAG1, result);
                    intent.putExtra(Constants.DATA_TAG2, format.name());
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                } else if (scanType == FOR_RESULT) {
                    model.findDetailByBarcode(mContext, result,
                            new ProgressSubscriber<QueryBarcodeDataBean>(mContext) {
                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    showPreview(result);
                                    return;
                                }

                                @Override
                                public void onNext(QueryBarcodeDataBean baseBean) {
                                    super.onNext(baseBean);
                                    if (baseBean != null && baseBean.getData() != null) {
                                        viewDataDetail(result, baseBean);
                                        return;
                                    }

                                }
                            });
                    return;
                }
                break;
            case "QR_CODE":
            case "AZTEC":
            case "DATA_MATRIX":
            case "MAXICODE":
                //二维码
                if (scanType == FOR_RESULT) {
                    handleQRcode(result);
                } else if (scanType == FOR_BARCODE) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DATA_TAG1, result);
                    intent.putExtra(Constants.DATA_TAG2, format.name());
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                }
                break;

            default:

                break;


        }
    }

    /**
     * 处理二维码结果
     *
     * @param result
     */
    private void handleQRcode(String result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            if (jsonObject == null) {
                // ToastUtils.showError(mContext, "json为空");
                return;
            }
            //扫描二维码登录
            if ("ScanLogin".equals(jsonObject.optString("useType"))) {
                final String code = jsonObject.optString("uniqueId");
                scanLogin(code);
            } else {
                //ToastUtils.showError(mContext, "这个json俺识别不了~(@^_^@)~");
                showPreview(result);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Matcher matcher = p.matcher(result);
            if (matcher.find()) {
                //打开网页
                openWebpage(matcher.group(0));
            } else {
                showPreview(result);
            }

        }
    }

    /**
     * 查看条形码关联数据详情
     */
    private void viewDataDetail(String result, QueryBarcodeDataBean baseBean) {
        String id = baseBean.getData().getId();
        String bean = baseBean.getData().getBean();
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(bean)) {
            showPreview(result);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, bean);
        bundle.putString(Constants.DATA_ID, id);
        UIRouter.getInstance().openUri(mContext, "DDComp://custom/detail", bundle, Constants.REQUEST_CODE1);
        finish();

    }

    //    返回键不退出程序，改为显示桌面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void handleResult(int resultCode, BarcodeFormat format, String resultString) {
        LogUtil.e(resultString);
        if (CodeUtils.RESULT_FAILED == resultCode) {
            ToastUtils.showError(mContext, "扫描失败!");
            return;
        }
        if (resultString.equals("")) {
            ToastUtils.showError(mContext, "结果为空!");
            return;
        }
        handleBarcode(format, resultString);
       /* Matcher matcher = p.matcher(resultString);


        if (matcher.find()) {
            //打开网页
            openWebpage(matcher.group(0));
        } else if (FormValidationUtils.isMobile(resultString)) {
            //拨打电话
            SystemFuncUtils.callPhone(CaptureActivity.this, resultString);
            finish();
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(resultString);
                if (jsonObject == null) {
                    ToastUtils.showError(mContext, "json为空");
                    return;
                }
                //扫描二维码登录
                if ("ScanLogin".equals(jsonObject.optString("useType"))) {
                    final String code = jsonObject.optString("uniqueId");
                    scanLogin(code);
                } else {
                    ToastUtils.showError(mContext, "这个json俺识别不了~(@^_^@)~");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                showPreview(resultString);
            }
        }*/
    }

    /**
     * 文本内容
     *
     * @param str
     */
    private void showPreview(String str) {
        String title = "内容";
        String subTitle = str;

        if (true) {
            DialogUtils.getInstance().sureOrCancel(CaptureActivity.this, title + " ", subTitle + " ", rootView.getRootView(), "复制", "取消", new DialogUtils.OnClickSureOrCancelListener() {
                @Override
                public void clickSure() {
                    SystemFuncUtils.copyTextToClipboard(CaptureActivity.this, str);
                    ToastUtils.showSuccess(CaptureActivity.this, "复制成功");
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DATA_TAG1, str);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void clickCancel() {
                    finish();
                }
            });
        }
    }

    /**
     * 扫码登录
     *
     * @param code
     */
    private void scanLogin(final String code) {
        DialogUtils.getInstance().sureOrCancel(this, "确认登录",
                "确定授权网页端登录码?",
                rootView, "确定", "取消",
                new DialogUtils.OnClickSureOrCancelListener() {
                    @Override
                    public void clickSure() {
                        Map<String, String> map = new HashMap<>();
                        map.put("userName", SPHelper.getUserAccount());
                        map.put("id", code);
                        final Observable<BaseBean> observable = new ApiManager<CommonApiService>().getAPI(CommonApiService.class)
                                .valiLogin(map).map(new HttpResultFunc<>())
                                .compose(CaptureActivity.this.bindUntilEvent(ActivityEvent.DESTROY));
                        observable.subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ProgressSubscriber<BaseBean>(mContext) {
                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                    }

                                    @Override
                                    public void onNext(BaseBean baseBean) {
                                        super.onNext(baseBean);
                                        finish();
                                    }
                                });
                    }

                    @Override
                    public void clickCancel() {
                        finish();
                        //继续扫描
//                        captureFragment.restartPreview();
                    }
                });
    }

    /**
     * 打开网页
     *
     * @param resultString
     */
    private void openWebpage(final String resultString) {
        if (resultString.contains("fileLink")) {
            openSharedFile(resultString);
            return;
        } else {
            showPreview(resultString);
        }

       /* DialogUtils.getInstance().sureOrCancel(this, "打开网页", resultString,

                rootView, "打开", "取消",
                new DialogUtils.OnClickSureOrCancelListener() {
                    @Override
                    public void clickSure() {
                        Intent intent = new Intent(Intent.ACTION_VIEW, (Uri.parse(resultString))).addCategory(Intent.CATEGORY_BROWSABLE).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void clickCancel() {
                        finish();
                    }
                });*/
    }

    /**
     * 打开共享文件页面
     *
     * @param resultString
     */
    private void openSharedFile(String resultString) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, resultString);
        UIRouter.getInstance().openUri(mContext, "DDComp://filelib/shared_file", bundle);
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (photos != null && photos.size() > 0) {
                    File file = new File(photos.get(0));
                    Result result = ZxingUtils.scanningImage(mContext,file.getAbsolutePath());
                    if (result != null) {
                        String resultText = result.getText();
                        Log.e("resultText", "--" + resultText);
                        handleResult(resultText);
                    } else {
                        ToastUtils.showError(mContext, "未识别");
                    }

                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}