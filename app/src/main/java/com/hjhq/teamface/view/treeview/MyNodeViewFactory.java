package com.hjhq.teamface.view.treeview;

import android.view.View;

import com.hjhq.teamface.view.treeview.base.BaseNodeViewBinder;
import com.hjhq.teamface.view.treeview.base.BaseNodeViewFactory;

/**
 * nodeView工厂
 */
public class MyNodeViewFactory extends BaseNodeViewFactory {

    @Override
    public BaseNodeViewBinder getNodeViewBinder(View view, int level) {
        switch (level) {
            case -1:
                return new ItemLevelNodeViewBinder(view);
            default:
                return new ParentLevelNodeViewBinder(view);
        }
    }
}
