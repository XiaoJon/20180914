package com.hjhq.teamface.memo.view;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.memo.R;


/**
 * Created by Administrator on 2018/3/21.
 * Describe：
 */

public class MemoListDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.memo_activity_main_v2;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("备忘录(全部)");
        setRightMenuIcons(R.drawable.memo_switch_view_icon, R.drawable.memo_menu_icon);
    }

}
