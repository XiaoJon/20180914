package com.hjhq.teamface.email.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.email.EmailModel;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.adapter.EmailAccountAdapter;
import com.hjhq.teamface.email.bean.EmailAccountListBean;
import com.hjhq.teamface.email.view.ChooseEmailAccountDelegate;

import java.util.ArrayList;
import java.util.List;

public class ChooseEmailAccountActivity extends ActivityPresenter<ChooseEmailAccountDelegate, EmailModel> {
    private List<Member> dataList = new ArrayList<>();
    private EmailAccountAdapter mAdapter;
    String defaultEmailAccount;
    private RecyclerView mRecyclerView;
    ArrayList<EmailAccountListBean.DataBean> accountList = new ArrayList<>();


    @Override
    public void init() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            defaultEmailAccount = extras.getString(Constants.DATA_TAG1);
            accountList = (ArrayList<EmailAccountListBean.DataBean>) extras.getSerializable(Constants.DATA_TAG2);
        }
        initData();
    }


    protected void initData() {
        mRecyclerView = viewDelegate.get(R.id.rv);

        mAdapter = new EmailAccountAdapter(accountList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewDelegate.setAdapter(mAdapter);
        viewDelegate.setItemChildClickListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < accountList.size(); i++) {
                    if (i == position) {
                        accountList.get(i).setChecked(true);
                    } else {
                        accountList.get(i).setChecked(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, getCheckedMember());
        intent.putExtra(Constants.DATA_TAG2, accountList);
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private Member getCheckedMember() {
        Member m = null;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isCheck()) {
                m = dataList.get(i);
            }
        }
        return m;
    }

}
