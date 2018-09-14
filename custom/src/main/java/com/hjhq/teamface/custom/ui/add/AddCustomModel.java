package com.hjhq.teamface.custom.ui.add;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.ModuleBean;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.body.FileRequestBody;
import com.hjhq.teamface.basis.network.callback.RetrofitCallback;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.hjhq.teamface.custom.CustomApiService;
import com.hjhq.teamface.custom.bean.SaveCustomDataRequestBean;
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

public class AddCustomModel implements IModel<CustomApiService> {
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
     * 保存新增业务信息
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void saveCustomData(ActivityPresenter mActivity, final SaveCustomDataRequestBean bean, Subscriber<ModuleBean> s) {
        getApi().saveCustomData(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 修改业务信息
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void updateCustomData(ActivityPresenter mActivity, SaveCustomDataRequestBean bean, Subscriber<BaseBean> s) {
        getApi().updateCustomData(bean).map(new HttpResultFunc<>())
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
    public void createBarcode(ActivityPresenter mActivity, String barcodeType, String barcodeValue, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("barcodeType", barcodeType);
        map.put("barcodeValue", barcodeValue);
        getApi().createBarcode(map).map(new HttpResultFunc<>())
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
    public void getBarcodeMsg(ActivityPresenter mActivity, String bean, String barcodeValue, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("bean", bean);
        map.put("barcodeValue", barcodeValue);
        getApi().getBarcodeMsg(map).map(new HttpResultFunc<>())
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
        Map<String, String> map = new HashMap<>();
        map.put("barcodeName", barcodeName);
        map.put("barcodeValue", barcodeValue);
        map.put("bean", bean);
        map.put("CLIENT_FLAG", CLIENT_FLAG);
        getApi().findDetailByBarcode(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 上传文件 回调
     *
     * @param url
     * @param callback
     */
    public void uploadFile(ActivityPresenter mActivity, String url, String moduleBean, RetrofitCallback callback, Subscriber<UpLoadFileResponseBean> s) {
        Map<String, RequestBody> fileList = new HashMap<>(2);
        File file = new File(url);

        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), moduleBean);
        FileRequestBody fileRequestBody = new FileRequestBody(requestBody1, callback);

        fileList.put("file\"; filename=\"" + file.getName(), fileRequestBody);
        fileList.put("bean", requestBody2);

        getApi().uploadApplyFile(fileList)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);

    }


    /**
     * 评论上传附件
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
