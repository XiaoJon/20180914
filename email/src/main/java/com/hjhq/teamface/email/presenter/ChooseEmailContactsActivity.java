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
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.email.EmailModel;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.adapter.EmailContactsAdapter;
import com.hjhq.teamface.email.bean.EmailRecentContactsListBean;
import com.hjhq.teamface.email.view.ChooseEmailContactsDelegate;

import java.util.ArrayList;
import java.util.List;

public class ChooseEmailContactsActivity extends ActivityPresenter<ChooseEmailContactsDelegate, EmailModel> {

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
            fromWhat = bundle.getInt(Constants.DATA_TAG1);
            choosedMemberList = (ArrayList<Member>) bundle.getSerializable(Constants.DATA_TAG2);
            choosedMemberList.clear();
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
        //从模块中选择联系人
        viewDelegate.get(R.id.from_module).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.DATA_TAG1, getCheckedMember());
            CommonUtil.startActivtiyForResult(ChooseEmailContactsActivity.this,
                    ChooseModuleContactActivity.class, Constants.REQUEST_CODE1, bundle);

        });
        //从邮箱通讯录选择联系人
        viewDelegate.get(R.id.from_contact).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.DATA_TAG1, getCheckedMember());
            CommonUtil.startActivtiyForResult(ChooseEmailContactsActivity.this,
                    EmailContactsActivity.class, Constants.REQUEST_CODE2, bundle);
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
        getRecentEmailContacts();
    }

    /**
     * 获取最近联系人
     */
    private void getRecentEmailContacts() {
        model.getRecentEmailContacts(ChooseEmailContactsActivity.this,
                new ProgressSubscriber<EmailRecentContactsListBean>(ChooseEmailContactsActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(EmailRecentContactsListBean emailRecentContactsListBean) {
                        super.onNext(emailRecentContactsListBean);
                        List<EmailRecentContactsListBean.DataBean> dataList = emailRecentContactsListBean.getData();
                        memberList.clear();
                        if (dataList != null && dataList.size() > 0) {
                            for (int i = 0; i < dataList.size(); i++) {
                                Member m = new Member();
                                m.setEmail(dataList.get(i).getMail_account());
                                m.setEmployee_name(dataList.get(i).getEmployee_name());
                                for (int j = 0; j < choosedMemberList.size(); j++) {
                                    if (choosedMemberList.get(j).getEmail().equals(m.getEmail())) {
                                        m.setCheck(true);
                                    }
                                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE1:
                case Constants.REQUEST_CODE2:
                    if (data != null) {
                        ArrayList<Member> list = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                        if (list == null || list.size() <= 0) {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.DATA_TAG1, getCheckedMember());
                            setResult(RESULT_OK, intent);
                            return;
                        }
                        ArrayList<Member> checkedMember = getCheckedMember();
                        if (checkedMember.size() <= 0) {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.DATA_TAG1, list);
                            setResult(RESULT_OK, intent);
                            finish();
                            return;
                        } else {
                            //去重
                            for (int i = 0; i < list.size(); i++) {
                                String email = list.get(i).getEmail();
                                boolean flag = false;
                                for (int j = 0; j < checkedMember.size(); j++) {
                                    if (!TextUtils.isEmpty(email) && email.equals(checkedMember.get(j).getEmail())) {
                                        flag = true;
                                    }
                                }
                                if (flag) {
                                    continue;
                                } else {
                                    checkedMember.add(list.get(i));
                                }
                            }
                            Intent intent = new Intent();
                            intent.putExtra(Constants.DATA_TAG1, checkedMember);
                            setResult(RESULT_OK, intent);
                            finish();
                            return;
                        }
                    }
                    break;
                case Constants.REQUEST_CODE3:

                    break;
                default:
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
