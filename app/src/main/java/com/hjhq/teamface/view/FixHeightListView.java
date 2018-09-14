package com.hjhq.teamface.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by lx on 2017/3/29.
 */

public class FixHeightListView extends ListView {


    public FixHeightListView(Context context) {
        super(context);
    }

    public FixHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixHeightListView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);

    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

            MeasureSpec.AT_MOST);

    super.onMeasure(widthMeasureSpec, expandSpec);

}
}