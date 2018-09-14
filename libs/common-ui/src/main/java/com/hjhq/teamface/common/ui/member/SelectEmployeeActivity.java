package com.hjhq.teamface.common.ui.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.adapter.EmployeeAdapter;
import com.hjhq.teamface.common.utils.MemberUtils;

import java.util.ArrayList;
import java.util.Iterator;

import rx.Observable;

/**
 * 员工选择
 *
 * @author lx
 * @date 2017/9/30
 */

public class SelectEmployeeActivity extends ActivityPresenter<EmployeeDelegate, CommonModel> {
    //可供选择的人员
    private ArrayList<Member> optionalMembers;
    //单选多选
    private int checkType;


    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            checkType = intent.getIntExtra(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
            optionalMembers = (ArrayList<Member>) intent.getSerializableExtra(C.OPTIONAL_MEMBERS_TAG);
            if (optionalMembers == null) {
                optionalMembers = new ArrayList<>();
            }
        }
    }

    @Override
    public void init() {
        viewDelegate.setTitle("人员选择");
        loadEmployee();
    }

    /**
     * 加载员工
     */
    public void loadEmployee() {
        filterData();
        viewDelegate.setAdapter(new EmployeeAdapter(optionalMembers));
    }

    /**
     * 过滤数据
     */
    private void filterData() {
        //去掉需要隐藏的条目
        Iterator<Member> iterator = optionalMembers.iterator();
        while (iterator.hasNext()) {
            if (MemberUtils.checkState(iterator.next().getSelectState(), C.HIDE_TO_SELECT)) {
                iterator.remove();
            }
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 先隐藏键盘
                SoftKeyboardUtils.hide(viewDelegate.mSearch);
                String content = viewDelegate.mSearch.getText().toString();
                keySearch(content);
                return true;
            }
            return false;
        });

        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                Member data = optionalMembers.get(position);
                //非自由选择
                if (!MemberUtils.checkState(data.getSelectState(), C.FREE_TO_SELECT)) {
                    return;
                }

                if (checkType == C.RADIO_SELECT) {
                    Observable.from(optionalMembers).subscribe(dataBean -> dataBean.setCheck(false));
                    data.setCheck(true);
                } else {
                    data.setCheck(!data.isCheck());
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 关键字搜索
     *
     * @param content
     */
    private void keySearch(String content) {
        if (TextUtil.isEmpty(content)) {
            viewDelegate.setAdapter(new EmployeeAdapter(optionalMembers));
            return;
        }
        ArrayList<Member> members = new ArrayList<>();
        Observable.from(optionalMembers).filter(member -> member.getName().contains(content)).subscribe(members::add);
        viewDelegate.setAdapter(new EmployeeAdapter(members));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        selectEmployee();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 选择员工
     */
    private void selectEmployee() {
        ArrayList<Member> members = new ArrayList<>();
        Observable.from(optionalMembers).filter(employee -> {
            boolean check = employee.isCheck();
            boolean state = !MemberUtils.checkState(employee.getSelectState(), C.NOT_FOR_SELECT);
            return check && state;
        }).subscribe(employee -> {
            employee.setType(C.EMPLOYEE);
            employee.setValue(C.EMPLOYEE + ":" + employee.getId());
            members.add(employee);
        });


        if (members.isEmpty()) {
            ToastUtils.showError(this, "请选择员工");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, members);
        setResult(RESULT_OK, intent);
        finish();
    }
}
