package com.hjhq.teamface.email.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.email.EmailModel;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.adapter.ModuleContactsAdapter;
import com.hjhq.teamface.email.bean.EmailFromModuleDataBean;
import com.hjhq.teamface.email.bean.MolduleListBean;
import com.hjhq.teamface.email.view.ChooseModuleContactDelegate;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static com.hjhq.teamface.basis.constants.Constants.DATA_TAG1;

public class ChooseModuleContactActivity extends ActivityPresenter<ChooseModuleContactDelegate, EmailModel> {

    private ModuleContactsAdapter mModuleAdapter;
    private List<MolduleListBean.DataBean> moduleList = new ArrayList<>();
    private List<Member> checkedContactList = new ArrayList<>();
    private RecyclerView mRvContacts;

    @Override
    public void init() {
        initData();
        initAdapter();
        initListener();
    }

    private void initListener() {
        mRvContacts.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MODULE_BEAN, moduleList.get(position).getEnglish_name());
                bundle.putString(Constants.NAME, moduleList.get(position).getChinese_name());
                UIRouter.getInstance().openUri(mContext, "DDComp://custom/select", bundle, Constants.REQUEST_CODE1);
                super.onItemClick(adapter, view, position);
            }
        });


    }

    private void initAdapter() {
        mRvContacts.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mModuleAdapter = new ModuleContactsAdapter(moduleList);
        mRvContacts.setAdapter(mModuleAdapter);
    }


    protected void initData() {
        mRvContacts = viewDelegate.get(R.id.rv_contacts);
        Bundle bundle = new Bundle();
        if (bundle != null) {
            checkedContactList = (List<Member>) bundle.getSerializable(DATA_TAG1);
        }
        getAppAndModule();

    }

    /**
     * 获取有邮件组件的模块列表
     */
    private void getAppAndModule() {
        model.getModuleEmails(ChooseModuleContactActivity.this,
                new ProgressSubscriber<MolduleListBean>(ChooseModuleContactActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MolduleListBean moduleResultBean) {
                        super.onNext(moduleResultBean);
                        moduleList.clear();
                        moduleList.addAll(moduleResultBean.getData());
                        mModuleAdapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //引用自定义
            String moduleBean = data.getStringExtra(Constants.MODULE_BEAN);
            ArrayList<DataTempResultBean.DataBean.DataListBean> dataList = (ArrayList<DataTempResultBean.DataBean.DataListBean>) data.getSerializableExtra(DATA_TAG1);


            StringBuilder sb = new StringBuilder();
            Observable.from(dataList)
                    .filter(DataTempResultBean.DataBean.DataListBean::isCheck)
                    .subscribe(dataBean -> sb.append("," + dataBean.getId().getValue()));
            sb.deleteCharAt(0);

            model.getEmailFromModuleDetail(this, moduleBean, sb.toString(), new ProgressSubscriber<EmailFromModuleDataBean>(this) {
                @Override
                public void onNext(EmailFromModuleDataBean baseBean) {
                    super.onNext(baseBean);
                    ArrayList<Member> list = new ArrayList<Member>();
                    for (int i = 0; i < baseBean.getData().size(); i++) {
                        for (int j = 0; j < baseBean.getData().get(i).getEmail_fields().size(); j++) {
                            Member m = new Member();
                            m.setEmployee_name("");
                            m.setEmail(baseBean.getData().get(i).getEmail_fields().get(j).getValue());
                            list.add(m);
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra(DATA_TAG1, list);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
