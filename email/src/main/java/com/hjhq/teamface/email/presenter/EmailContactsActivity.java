package com.hjhq.teamface.email.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.email.EmailModel;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.adapter.EmailContactsAdapter;
import com.hjhq.teamface.email.bean.EmailContactsListBean;
import com.hjhq.teamface.email.view.EmailContactsDelegate;

import java.util.ArrayList;
import java.util.List;

public class EmailContactsActivity extends ActivityPresenter<EmailContactsDelegate, EmailModel> {

    private EmailContactsAdapter mEmailContactsAdapter;
    private RecyclerView mRvContacts;
    private ArrayList<Member> memberList = new ArrayList<>();
    private ArrayList<Member> choosedMemberList = new ArrayList<>();
    private int fromWhat;

    @Override
    public void init() {
        initIntent();
        initData();
        initListener();
    }

    private void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            choosedMemberList = (ArrayList<Member>) bundle.getSerializable(Constants.DATA_TAG1);
        }
    }

    private void initListener() {
        mRvContacts.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (memberList != null) {
                    memberList.get(position).setCheck(!memberList.get(position).isCheck());
                    mEmailContactsAdapter.notifyItemChanged(position);
                }
            }
        });
    }


    protected void initData() {

        mRvContacts = viewDelegate.get(R.id.rv_contacts);
        mRvContacts.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mEmailContactsAdapter = new EmailContactsAdapter(memberList);
        mRvContacts.setAdapter(mEmailContactsAdapter);
        getEmailContact();
    }

    private void getEmailContact() {
        model.getEmailContacts(EmailContactsActivity.this, new ProgressSubscriber<EmailContactsListBean>(EmailContactsActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
            }

            @Override
            public void onNext(EmailContactsListBean baseBean) {
                super.onNext(baseBean);
                List<EmailContactsListBean.DataBean.DataListBean> dataList = baseBean.getData().getDataList();
                if (dataList != null) {
                    for (int i = 0; i < dataList.size(); i++) {
                        Member m = new Member();
                        m.setEmployee_name(dataList.get(i).getName());
                        m.setId(TextUtil.parseLong(dataList.get(i).getId()));
                        m.setEmail(dataList.get(i).getMail_address());
                        boolean flag = false;
                        if (choosedMemberList != null && choosedMemberList.size() > 0) {
                            for (int j = 0; j < choosedMemberList.size(); j++) {
                                if (!TextUtils.isEmpty(m.getEmail()) && m.getEmail().equals(choosedMemberList.get(j).getEmail())) {
                                    flag = true;
                                }
                            }
                        }
                        m.setCheck(flag);
                        memberList.add(m);
                    }
                    mEmailContactsAdapter.notifyDataSetChanged();
                }

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getCheckedMember() == null || getCheckedMember().size() <= 0) {
            ToastUtils.showToast(mContext, "请选择联系人");
            return true;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, getCheckedMember());
        setResult(RESULT_OK, intent);
        finish();

        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取选中的联系人
     *
     * @return
     */
    private ArrayList<Member> getCheckedMember() {
        ArrayList<Member> list = new ArrayList<>();
        for (int i = 0; i < memberList.size(); i++) {
            if (memberList.get(i).isCheck()) {
                list.add(memberList.get(i));
            }
        }
        list.addAll(fromModule());

        return list;
    }

    /**
     * 从通讯录和模块添加的联系人
     *
     * @return
     */
    private ArrayList<Member> fromModule() {
        ArrayList<Member> list = new ArrayList<>();
        return list;
    }

    @Override
    public void onBackPressed() {

        finish();

    }

}
