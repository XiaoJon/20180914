package com.hjhq.teamface.email.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.adapter.EmailContactsAdapter;
import com.hjhq.teamface.email.view.ChooseEmailContactsDelegate;


public class ChooseAppActivity extends ActivityPresenter<ChooseEmailContactsDelegate, CommonModel> {

    EmailContactsAdapter mEmailContactsAdapter;
    RecyclerView mRvContacts;

    @Override
    public void init() {
        initData();
    }


    protected void initData() {
        mRvContacts = viewDelegate.get(R.id.rv_contacts);
        mRvContacts.setLayoutManager(new LinearLayoutManager(this));
        mEmailContactsAdapter = new EmailContactsAdapter(null);
        mRvContacts.setAdapter(mEmailContactsAdapter);
       /* List<Member> list = DBManager.getInstance().queryMemberByKeyword("aa");
        if (list != null & list.size() >= 0) {
            mEmailContactsAdapter.setNewData(list);
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finish();

    }

}
