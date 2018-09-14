package com.hjhq.teamface.common.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.FileHelper;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;
import com.luojilab.router.facade.annotation.RouteNode;

@RouteNode(path = "/view_barcode_image", desc = "查看条形码图片")
public class ViewBarcodeImageActivity extends ActivityPresenter<ViewBarcodeImageDelegate, CommonModel> {
    private ImageView ivImage;
    private String imageName;
    private RelativeLayout rlImage;


    @Override
    public void init() {
        ivImage = viewDelegate.get(R.id.iv);
        rlImage = viewDelegate.get(R.id.rl_image);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String url = bundle.getString(Constants.DATA_TAG1);
            imageName = bundle.getString(Constants.DATA_TAG2);
            ImageLoader.loadImage(mContext, url, ivImage, R.drawable.ic_image, R.drawable.jmui_send_error);
        }
        viewDelegate.get(R.id.save).setOnClickListener(v -> {
            FileHelper.saveBitmapToSDCard(mContext, ImageLoader.getBitmapFromView(rlImage), imageName + ".jpg");
            ToastUtils.showSuccess(mContext, "保存成功");
        });

    }
}
