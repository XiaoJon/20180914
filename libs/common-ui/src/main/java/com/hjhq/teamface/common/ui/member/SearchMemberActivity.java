package com.hjhq.teamface.common.ui.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.EmployeeResultBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.adapter.EmployeeAdapter;
import com.hjhq.teamface.common.view.SearchBar;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;


/**
 * 成员通用搜索界面
 *
 * @author Administrator
 */
public class SearchMemberActivity extends ActivityPresenter<SearchMemberDelegate, CommonModel> implements View.OnClickListener {
    private String keyword;
    private EmployeeAdapter mEmployeeAdapter;

    /**
     * 选择类型  单选、多选、不选
     */
    private int checkType;
    /**
     * 成员类型 部门，员工，角色，动态参数
     */
    private int memberType;
    private String departmentId;


    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            checkType = intent.getIntExtra(C.CHECK_TYPE_TAG, C.NO_SELECT);
            departmentId = intent.getStringExtra(Constants.DATA_TAG1);
            //暂时只搜索人员
            memberType = intent.getIntExtra(C.MEMBER_TYPE_TAG, C.EMPLOYEE);
        }
    }

    @Override
    public void init() {
        mEmployeeAdapter = new EmployeeAdapter(null);
        viewDelegate.setAdapter(mEmployeeAdapter);
        if (checkType == C.NO_SELECT) {
            viewDelegate.setVisibility(R.id.tv_confirm, View.GONE);
        }
    }

    @Override
    protected void getDelegateView() {
        viewDelegate = new SearchMemberDelegate();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();


        viewDelegate.setOnClickListener(this, R.id.tv_cancel, R.id.tv_confirm);
        viewDelegate.mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {
                keyword = "";
            }

            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void search() {
                searchMember();
            }

            @Override
            public void getText(String text) {
                keyword = text;

            }

        });
        viewDelegate.resultRv.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<Member> data = mEmployeeAdapter.getData();
                Member item = mEmployeeAdapter.getItem(position);
                switch (checkType) {
                    case C.NO_SELECT:
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.DATA_TAG1, item.getId() + "");
                        UIRouter.getInstance().openUri(SearchMemberActivity.this, "DDComp://app/employee/info", bundle);

                        break;
                    case C.RADIO_SELECT:
                        Observable.from(data)
                                .subscribe(member -> member.setCheck(false));
                        item.setCheck(true);
                        adapter.notifyDataSetChanged();
                        break;
                    case C.MULTIL_SELECT:
                        item.setCheck(!item.isCheck());
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 搜索人员
     */
    private void searchMember() {
        model.findByUserName(this, departmentId, keyword, new ProgressSubscriber<EmployeeResultBean>(this) {
            @Override
            public void onNext(EmployeeResultBean employeeResultBean) {
                super.onNext(employeeResultBean);
                List<Member> data = mEmployeeAdapter.getData();
                data.clear();
                data.addAll(employeeResultBean.getData());
                if (checkType == C.NO_SELECT) {
                    for (Member member : data) {
                        member.setSelectState(C.NOT_TO_SELECT);
                    }
                }
                mEmployeeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            finish();

        } else if (i == R.id.tv_confirm) {
            confirm();

        }

    }

    private void confirm() {
        ArrayList<Member> resultData = new ArrayList<>();
        List<Member> data = mEmployeeAdapter.getData();
        for (Member member : data) {
            if (member.isCheck()) {
                resultData.add(member);
            }
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, resultData);
        setResult(RESULT_OK, intent);
        finish();
    }
}
