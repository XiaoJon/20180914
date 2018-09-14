package com.hjhq.teamface.oa.friends.logic;

import android.text.TextUtils;

import com.hjhq.teamface.api.TeamMessageApiService;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.BaseLogic;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.Photo;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.MainRetrofit;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.SingleInstance;
import com.hjhq.teamface.common.bean.FriendsListBean;
import com.hjhq.teamface.im.bean.AddFriendsCommentResponseBean;
import com.hjhq.teamface.im.bean.AddFriendsRequestBean;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author Administrator
 * @date 2017/11/24
 */

public class FriendsLogic extends BaseLogic {
    public static FriendsLogic getInstance() {
        return (FriendsLogic) SingleInstance.getInstance(FriendsLogic.class
                .getName());
    }

    private TeamMessageApiService getApi() {
        return new ApiManager<TeamMessageApiService>().getAPI(TeamMessageApiService.class);
    }


    /**********************企业圈********************/
    /**
     * 企业圈列表
     *
     * @param mActivity
     * @param isPerson
     * @param pageNo
     * @param pageSize
     * @param s
     */
    public void getFriends(BaseActivity mActivity, String isPerson, Integer pageNo, Integer pageSize, Subscriber<FriendsListBean> s) {
        TeamMessageApiService api = new MainRetrofit.Builder<TeamMessageApiService>().readTimeout(60).build(TeamMessageApiService.class);
        api.getFriends(isPerson, pageNo, pageSize)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 删除
     *
     * @param mActivity
     * @param circleMainId
     * @param s
     */
    public void delFriends(BaseActivity mActivity, String circleMainId, Subscriber<BaseBean> s) {
        getApi().delFriends(circleMainId)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 点赞
     *
     * @param mActivity
     * @param circleMainId
     * @param s
     */
    public void likeFriends(BaseActivity mActivity, String circleMainId, Subscriber<BaseBean> s) {
        getApi().likeFriends(circleMainId)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 评论
     *
     * @param mActivity
     * @param circleMainId
     * @param s
     */
    public void commentFriends(BaseActivity mActivity, String circleMainId, String receiverId, String contentInfo, Subscriber<AddFriendsCommentResponseBean> s) {
        Map<String, String> map = new HashMap<>(3);
        map.put("circleMainId", circleMainId);
        map.put("receiverId", receiverId);
        map.put("contentInfo", contentInfo);
        getApi().commentFriends(map)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除评论
     *
     * @param mActivity
     * @param commentId
     * @param s
     */
    public void delComment(BaseActivity mActivity, String commentId, Subscriber<BaseBean> s) {
        getApi().delComment(commentId)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 发表动态
     *
     * @param mActivity
     * @param s
     */
    public void addFriends(RxAppCompatActivity mActivity, List<Photo> photos, final AddFriendsRequestBean bean, Subscriber<BaseBean> s) {
        if (CollectionUtils.isEmpty(photos)) {
            addFriends(mActivity, bean, s);
            return;
        } else {
            Map<String, RequestBody> fileRequest = new HashMap<>(photos.size());
            for (int i = 0; i < photos.size(); i++) {
                try {
                    String url = photos.get(i).getUrl();
                    File file = FileUtils.getCompressedImage(FileUtils.getImage(url, 120));
                    fileRequest.put("file" + i + "\"; filename=\"" + url, RequestBody.create(MediaType.parse("multipart/form-data"), file));
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showError(mActivity, "图片压缩失败");
                    return;
                }
            }
            RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), "friends111");
            fileRequest.put("bean", requestBody2);

            addFriends(mActivity, fileRequest, bean, s);
        }
    }


    /**
     * 发表动态 非图片
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    private void addFriends(RxAppCompatActivity mActivity, AddFriendsRequestBean bean, Subscriber<BaseBean> s) {
        getApi().addFriends(bean)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 发表动态 带图片
     * @param mActivity
     * @param fileList
     * @param bean
     * @param s
     */
    private void addFriends(RxAppCompatActivity mActivity, Map<String, RequestBody> fileList, AddFriendsRequestBean bean, Subscriber<BaseBean> s) {
        getApi().uploadAvatarFile(fileList).map(new HttpResultFunc<>())
                .flatMap(new Func1<UpLoadFileResponseBean, Observable<BaseBean>>() {
                    @Override
                    public Observable<BaseBean> call(UpLoadFileResponseBean upLoadFileResponseBean) {
                        List<UploadFileBean> data = upLoadFileResponseBean.getData();
                        bean.setImages(data);
                        return getApi().addFriends(bean);
                    }
                }).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 修改背景
     *
     * @param mActivity
     * @param s
     */
    public void modBackGround(BaseActivity mActivity, String filePath, Subscriber<BaseBean> s) {
        if (TextUtils.isEmpty(filePath)) {
            ToastUtils.showToast(mActivity, "请选择需要上传的文件");
            return;
        }
        final Map<String, RequestBody> fileList = new HashMap<>();

        fileList.put("file" + "\"; filename=\"" + filePath, RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)));

        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), "user111");
        fileList.put("bean", requestBody2);

        getApi().uploadAvatarFile(fileList).map(new HttpResultFunc<>())
                .flatMap(new Func1<UpLoadFileResponseBean, Observable<BaseBean>>() {
                    @Override
                    public Observable<BaseBean> call(UpLoadFileResponseBean upLoadFileResponseBean) {
                        List<UploadFileBean> data = upLoadFileResponseBean.getData();
                        String fileUrl = data.get(0).getFileUrl();
                        Map<String, String> map = new HashMap<>();
                        map.put("microblog_background", fileUrl);
                        Map<String, Map> obj = new HashMap<>();
                        obj.put("data", map);
                        return getApi().editEmployeeDetail(obj);
                    }
                }).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

}
