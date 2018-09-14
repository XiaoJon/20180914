package com.hjhq.teamface.customcomponent;


import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.GetBarcodeBean;
import com.hjhq.teamface.basis.bean.GetBarcodeImgBean;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author xj
 */
public interface CustomApiService {

    /**
     * 获取自定义布局
     */
    @GET("layout/getEnableLayout")
    Observable<CustomLayoutResultBean> getEnableFields(@QueryMap Map<String, Object> map);


    /**
     * 评论上传附件
     *
     * @param fileList 文件集合
     * @return url
     */
    @Multipart
    @POST("common/file/upload")
    Observable<UpLoadFileResponseBean> uploadFile(@PartMap Map<String, RequestBody> fileList);


    /**
     * 自定义上传附件
     *
     * @param fileList 文件集合
     * @return url
     */
    @Multipart
    @POST("common/file/applyFileUpload")
    Observable<UpLoadFileResponseBean> uploadApplyFile(@PartMap Map<String, RequestBody> fileList);

    /**
     * 生成条形码
     *
     * @return
     */
    @GET("barcode/createBarcode")
    Observable<GetBarcodeBean> createBarcode(@Query("barcodeType") String barcodeType,
                                             @Query("barcodeValue") String barcodeValue);

    /**
     * 根据条形码获取数据详情
     *
     * @return
     */
    @GET("barcode/findDetailByBarcode")
    Observable<BaseBean> findDetailByBarcode(@Query("barcodeName") String barcodeName,
                                             @Query("barcodeValue") String barcodeValue,
                                             @Query("bean") String bean,
                                             @Query("CLIENT_FLAG") String CLIENT_FLAG
    );

    /**
     * 获取条形码图片信息
     *
     * @return
     */
    @GET("barcode/getBarcodeMsg")
    Observable<GetBarcodeImgBean> getBarcodeMsg(@Query("bean") String bean,
                                                @Query("barcodeValue") String barcodeValue);
}
