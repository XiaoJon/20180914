package com.hjhq.teamface.customcomponent.widget2.web;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.customcomponent.R;

/**
 * Created by Administrator on 2018/7/10.
 */

public class RichTextEditDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.custom_edit_rich_text_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm));
    }
}
