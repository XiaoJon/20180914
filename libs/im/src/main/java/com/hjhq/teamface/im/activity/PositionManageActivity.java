package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.PositionAdapter;
import com.hjhq.teamface.im.bean.PositionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 职位管理
 * Created by lx on 2017/5/19.
 */

public class PositionManageActivity extends BaseTitleActivity {
    public static final int NORMAL_MODE = 1001;//正常状态
    public static final int SELECT_MODE = 1002;//选择状态

    public int currentMode = NORMAL_MODE;

    RecyclerView mRecyclerView;

    private PositionAdapter positionAdapter;
    private List<PositionBean> positionList = new ArrayList<>();

    @Override
    protected int getChildView() {
        return R.layout.activity_position_manage;
    }

    @Override
    protected void onSetContentViewNext(Bundle savedInstanceState) {
        super.onSetContentViewNext(savedInstanceState);
        if (savedInstanceState == null) {
            currentMode = getIntent().getIntExtra(Constants.DATA_TAG1, NORMAL_MODE);
        } else {
            currentMode = savedInstanceState.getInt(Constants.DATA_TAG1, NORMAL_MODE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.DATA_TAG1, currentMode);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView = findViewById(R.id.recyclerview);
        if (currentMode == NORMAL_MODE) {
            setActivityTitle("职务管理");
            setRightMenuIcons(R.drawable.icon_add);
        } else {
            setActivityTitle("职务选择");
        }
    }


    @Override
    protected void initData() {
        /*positionList = ContactLogic.getInstance().getPosition(HttpMethods.getCOMPANYID());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        positionAdapter = new PositionAdapter(positionList);
        positionAdapter.setMode(currentMode);
        mRecyclerView.setAdapter(positionAdapter);*/
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void setListener() {
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PositionBean positionBean = positionList.get(position);
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG1, positionBean);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                PositionBean item = (PositionBean) adapter.getItem(position);
                if (view.getId() == R.id.iv_edit) {
                    editPosition(item);
                } else if (view.getId() == R.id.iv_del) {
                    delPosition(item);
                }
            }
        });
    }

    @Override
    protected void onRightMenuClick(int itemId) {
        super.onRightMenuClick(itemId);
        addPosition();
    }


    /**
     * 删除职位
     *
     * @param item
     */
    private void delPosition(PositionBean item) {
        DialogUtils.getInstance().sureOrCancel((Activity) mContext, "删除职务", "你确定要删除此职务吗？", getContainer(), () -> {
                }

        );
    }

    /**
     * 修改职位
     *
     * @param item
     */
    private void editPosition(PositionBean item) {
        /*Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, NewPositionActivity.EDIT_TYPE);
        bundle.putString(Constants.DATA_TAG2, item.getPosition());
        bundle.putLong(Constants.DATA_TAG3, item.getId());
        CommonUtil.startActivtiy(mContext, NewPositionActivity.class, bundle);*/
    }

    /**
     * 添加职位
     */
    private void addPosition() {
       /* Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, NewPositionActivity.NEW_TYPE);
        CommonUtil.startActivtiy(this, NewPositionActivity.class, bundle);*/
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        int responseCode = messageEvent.getResponseCode();
        if (responseCode == Constants.POSITION_REFRESH) {
            initData();
        }
    }*/
}
