package com.hjhq.teamface.project.ui;

import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class MemoChooseProjectDelegate extends AppDelegate {
    public RecyclerView mRecyclerView;
    public EditText mEditText;
    public RelativeLayout rlSearchLayout;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_memo_choose;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuTexts(R.color.main_green, getActivity().getString(R.string.confirm));
        showMenu();
        TextView titleTv = get(R.id.title_tv);
        titleTv.setText("备忘录");
        mRecyclerView = get(R.id.rv_contacts);
        mEditText = get(R.id.et);
        rlSearchLayout = get(R.id.search);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemClickListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }
}
