package com.hjhq.teamface.common.ui;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.Photo;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.view.image.SubsamplingScaleImageView;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;

import java.util.ArrayList;


/**
 * 图片展示
 *
 * @author Administrator
 */
public class ImagePagerActivity extends ActivityPresenter<ImagePagerDelegate, CommonModel> implements ViewPager.OnPageChangeListener, View.OnClickListener {

    /**
     * 当前显示的图片
     */
    public static final String SELECT_INDEX = "select_index";

    /**
     * 图片列表
     */
    public static final String PICTURE_LIST = "picture_list";
    /**
     * 是否可删除
     */
    public static final String IS_CAN_DELETE = "is_can_delete";

    /**
     * 广播action
     **/
    public static final String DELETE_BROADCAST_ACTION = "broadcast_action";

    /**
     * 删除index
     **/
    public static final String DELETE_POSITION_KEY = "delete_position_key";

    /**
     * 是否可以删除
     **/
    boolean isCanDelete = false;

    /**
     * 图片总数量
     **/
    private int totalCount;

    /**
     * 图片信息集合
     **/
    ArrayList<Photo> photos;

    ImageItemAdapter adapter;

    @Override
    public void init() {
        // 朋友圈的媒体信息
        photos = (ArrayList<Photo>) getIntent().getSerializableExtra(
                PICTURE_LIST);
        isCanDelete = getIntent().getBooleanExtra(IS_CAN_DELETE, false);

        if (photos == null) {
            photos = new ArrayList<>();
        }
        totalCount = photos.size();
        // 选中的图片
        int index = getIntent().getIntExtra(SELECT_INDEX, 0);

        viewDelegate.setDelVisibility(isCanDelete ? View.VISIBLE : View.GONE);

        adapter = new ImageItemAdapter(this, photos);

        viewDelegate.setAdapter(adapter);

        // 显示当前位置的View
        viewDelegate.setCurrentItem(index);

        int currentItem = 0;
        if (totalCount != 0) {

            currentItem = viewDelegate.getCurrentItem() + 1;
        }
        viewDelegate.setTitle(currentItem + "/" + totalCount);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.pager.addOnPageChangeListener(this);
        viewDelegate.setOnClickListener(this, R.id.back, R.id.delete, R.id.rotation);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
        } else if (id == R.id.delete) {
            DialogUtils.getInstance().sureOrCancel(this, "提示", "是否确定删除？", viewDelegate.delete, () -> {
                        int currentItem = viewDelegate.getCurrentItem();
                        photos.remove(currentItem);
                        adapter.notifyDataSetChanged();
                        totalCount = photos.size();
                        viewDelegate.setTitle(((currentItem + 1) > totalCount ? totalCount
                                : (currentItem + 1))
                                + "/" + totalCount);
                        EventBusUtils.sendEvent(new MessageBean(Constants.PHOTO_REFRESH, currentItem + "", null));
                        if (totalCount == 0) {
                            finish();
                        }
                    }
            );
        } else if (id == R.id.rotation) {
            // 图片旋转
            View pagerChildView = viewDelegate.pager.findViewById(viewDelegate.getCurrentItem());
            if (pagerChildView != null) {
                SubsamplingScaleImageView imageView = pagerChildView
                        .findViewById(R.id.i_image);
                rotateImageView(imageView, 90);
            }

        }
    }

    /**
     * 旋转图片
     *
     * @param imageView
     * @param degrees
     */
    private void rotateImageView(final SubsamplingScaleImageView imageView,
                                 float degrees) {
        final Bitmap bitmap = imageView.getBitmap();
        if (bitmap != null) {
            int bitmapW = bitmap.getWidth();
            int bitmapH = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees, bitmapW / 2, bitmapH / 2);
            Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapW, bitmapH,
                    matrix, true);
            imageView.setImageBitmap(bmp);

            imageView.postInvalidate();

            new Thread(() -> {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (imageView.getTag() != null) {
                    bitmap.recycle();
                } else {
                    imageView.setTag(true);
                }

//					bitmap.recycle();
            }).start();

            // bitmap.recycle();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewDelegate.setTitle((position + 1) + "/" + totalCount);
        if (isCanDelete) {
            Photo ph = photos.get(position);
            viewDelegate.setDelVisibility(ph.isCanDelete() ? View.VISIBLE : View.GONE);
        } else {
            viewDelegate.setDelVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
