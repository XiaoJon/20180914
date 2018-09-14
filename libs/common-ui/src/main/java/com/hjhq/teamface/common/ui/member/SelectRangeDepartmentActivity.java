package com.hjhq.teamface.common.ui.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.bean.EmployeeResultBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.utils.MemberUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * 选择部门范围
 *
 * @author Administrator
 * @date 2018/1/5
 */

public class SelectRangeDepartmentActivity extends ActivityPresenter<SelectMemberDelegate, CommonModel> {
    /**
     * 单选多选
     */
    protected int checkType;
    /**
     * 全部部门
     */
    protected List<Member> allDepartment = new ArrayList<>();

    /**
     * 可选的 部门
     */
    protected List<Member> chooseRange;
    private SelectRangeDepartmentFragment selectRangeFragment;
    /**
     * 已选部门
     */
    private List<Member> selectedMember;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            checkType = intent.getIntExtra(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
            chooseRange = (List<Member>) intent.getSerializableExtra(C.CHOOSE_RANGE_TAG);
            selectedMember = (List<Member>) intent.getSerializableExtra(C.SELECTED_MEMBER_TAG);
            allDepartment.addAll(chooseRange);
        }
    }

    @Override
    public void init() {
        viewDelegate.setTitle("选择部门");
        if (C.MULTIL_SELECT == checkType) {
            viewDelegate.setBottomSelecterVisibility(View.VISIBLE);
        }
        initData();
    }

    private void initData() {
        Map<String, List<Member>> map = new HashMap<>(1);
        map.put("chooseRange", chooseRange);
        model.queryRangeDepartmentList(this, map, new ProgressSubscriber<EmployeeResultBean>(this) {
            @Override
            public void onNext(EmployeeResultBean employeeResultBean) {
                super.onNext(employeeResultBean);
                allDepartment = employeeResultBean.getData();
                initFragment();
            }
        });
    }

    private void initFragment() {
        Observable.from(selectedMember)
                .filter(selected -> selected.getType() == C.DEPARTMENT)
                .subscribe(selected -> Observable.from(allDepartment)
                        .filter(employee -> employee.getId() == selected.getId())
                        .subscribe(employee -> employee.setCheck(true)));
        if (C.MULTIL_SELECT == checkType) {
            setAllCheckState();
        }
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(selectRangeFragment = new SelectRangeDepartmentFragment());
        viewDelegate.setFragment(fragmentList);
    }


    /**
     * 获取已选中的部门
     *
     * @return
     */
    public List<Member> getCheckedEmployee() {
        ArrayList<Member> checkEmployees = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allDepartment)) {
            Observable.from(allDepartment).filter(Member::isCheck).subscribe(department -> {
                department.setType(C.DEPARTMENT);
                department.setValue(C.DEPARTMENT + ":" + department.getId());
                checkEmployees.add(department);
            });
        }
        return checkEmployees;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, (Serializable) getCheckedEmployee());
        setResult(Activity.RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        //全选
        viewDelegate.ivCheckAllMember.setOnClickListener(v -> {
            boolean isChecked = !viewDelegate.isAllSelected();
            selectRangeFragment.setAllSelect(isChecked);
            setAllCheckState(isChecked);

        });
    }


    /**
     * 设置全选状态
     */
    public void setAllCheckState() {
        boolean isAllCheck = false;
        int size = 0;
        int selectCount = 0;
        for (Member member : allDepartment) {
            if (!MemberUtils.checkState(member.getSelectState(), C.CAN_NOT_SELECT)) {
                size++;
                if (member.isCheck()) {
                    selectCount++;
                }
            }
        }
        if (size != 0 && size == selectCount) {
            isAllCheck = true;
        }
        setAllCheckState(isAllCheck);
    }

    /**
     * 设置全选状态 和已选人数
     */
    public void setAllCheckState(boolean isAllCheck) {
        viewDelegate.setAllCheckText(isAllCheck);
        viewDelegate.setAllSelected(isAllCheck);
        showSum();
    }

    /**
     * 显示总过选中的数量
     */
    private void showSum() {
        int checkEmployeeSize = 0;
        for (Member member : allDepartment) {
            if (member.isCheck()) {
                checkEmployeeSize++;
            }
        }
        viewDelegate.setSum(checkEmployeeSize);
    }

}
