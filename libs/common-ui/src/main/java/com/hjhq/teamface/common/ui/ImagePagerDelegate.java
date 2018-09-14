package com.hjhq.teamface.common.ui;

import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;

/**
 * @author Administrator
 * @date 2018/3/22
 */

public class ImagePagerDelegate extends AppDelegate {

    /**
     * 图片显示的容器
     */
    ViewPager pager;

    TextView titleTV;

    ImageButton delete;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_imagepage;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        pager = get(R.id.imageviewpager);
        titleTV = get(R.id.title_text);
        delete = get(R.id.delete);
    }

    @Override
    public void setTitle(String title) {
        titleTV.setText(title);
    }

    public void setDelVisibility(int visibility) {
        delete.setVisibility(visibility);
    }

    public void setAdapter(ImageItemAdapter adapter) {
        pager.setAdapter(adapter);
    }

    public void setCurrentItem(int index) {
        pager.setCurrentItem(index);
    }

    public int getCurrentItem() {
        return pager.getCurrentItem();
    }
}
