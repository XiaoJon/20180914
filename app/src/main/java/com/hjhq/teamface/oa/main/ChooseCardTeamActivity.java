package com.hjhq.teamface.oa.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.oa.main.adaper.ChooseCardTeamAdapter;
import com.hjhq.teamface.view.recycler.SimpleItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 选择名片模版
 *
 * @author Administrator
 * @date 2018/3/29
 */

public class ChooseCardTeamActivity extends BaseTitleActivity {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ChooseCardTeamAdapter mAdapter;
    private ArrayList<String> userInfos = new ArrayList<>();

    @Override
    protected int getChildView() {
        return R.layout.user_activity_choose_card;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle("我的名片");
        setRightMenuColorTexts(R.color.black_4a, getString(R.string.confirm));
    }

    @Override
    protected void setListener() {
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                if (position == mAdapter.getTeamCode()) {
                    return;
                }
                mAdapter.setTeamCode(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add("");
        }
        int teamCode = getIntent().getIntExtra(Constants.DATA_TAG1, 0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new ChooseCardTeamAdapter(list, teamCode));
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int teamCode = mAdapter.getTeamCode();
        boolean b = SPUtils.setLong(mContext, UserQRCodeActivity.TEAM_CODE, teamCode);
        setResult(RESULT_OK);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
