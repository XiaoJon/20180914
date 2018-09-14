package com.hjhq.teamface.customcomponent;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.GetBarcodeBean;
import com.hjhq.teamface.basis.bean.GetBarcodeImgBean;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * 数据列表Model
 *
 * @author lx
 * @date 2017/9/5
 */

public class CustomComponentModel implements IModel<CustomApiService> {
    @Override
    public CustomApiService getApi() {
        return new ApiManager<CustomApiService>().getAPI(CustomApiService.class);
    }

    /**
     * 获取布局
     *
     * @param mActivity
     * @param map
     * @param s
     */
    public void getCustomLayout(ActivityPresenter mActivity, Map<String, Object> map, Subscriber<CustomLayoutResultBean> s) {
        getApi().getEnableFields(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 生成条形码
     *
     * @param mActivity
     * @param barcodeType
     * @param barcodeValue
     * @param s
     */
    public void createBarcode(ActivityPresenter mActivity, String barcodeType,
                              String barcodeValue, Subscriber<GetBarcodeBean> s) {
        getApi().createBarcode(barcodeType, barcodeValue).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取条形码图片信息
     *
     * @param mActivity
     * @param bean
     * @param barcodeValue
     * @param s
     */
    public void getBarcodeMsg(ActivityPresenter mActivity, String bean, String barcodeValue, Subscriber<GetBarcodeImgBean> s) {
        getApi().getBarcodeMsg(bean, barcodeValue).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 根据条形码获取数据详情
     *
     * @param mActivity
     * @param barcodeName
     * @param barcodeValue
     * @param bean
     * @param CLIENT_FLAG
     * @param s
     */
    public void findDetailByBarcode(ActivityPresenter mActivity, String barcodeName, String barcodeValue, String bean, String CLIENT_FLAG, Subscriber<BaseBean> s) {
        getApi().findDetailByBarcode(barcodeName, barcodeValue, bean, CLIENT_FLAG).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 自定义上传文件 回调
     *
     * @param url
     */
    public void uploadApplyFile(ActivityPresenter mActivity, String url, String moduleBean, Subscriber<UpLoadFileResponseBean> s) {
        Map<String, RequestBody> fileList = new HashMap<>(2);
        File file = new File(url);

        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        fileList.put("file\"; filename=\"" + file.getName(), requestBody1);

        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), moduleBean);
        fileList.put("bean", requestBody2);

        getApi().uploadApplyFile(fileList)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);

    }


    /**
     * 通用上传附件
     *
     * @param mActivity
     * @param url
     * @param s
     */
    public void uploadFile(ActivityPresenter mActivity,
                           String url, String bean, Subscriber<UpLoadFileResponseBean> s) {
        File file = new File(url);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Map<String, RequestBody> fileList = new HashMap<>();
        fileList.put("file\"; filename=\"" + file.getAbsolutePath(), requestBody1);

        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), bean);
        fileList.put("bean", requestBody2);
        getApi().uploadFile(fileList)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

}
