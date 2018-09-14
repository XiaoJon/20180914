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
 * 选择人员范围
 *
 * @author Administrator
 * @date 2018/1/5
 */

public class SelectRangeActivity extends ActivityPresenter<SelectMemberDelegate, CommonModel> {
    /**
     * 单选多选
     */
    protected int checkType;
    /**
     * 全部员工
     */
    protected List<Member> allEmployees = new ArrayList<>();

    /**
     * 可选的成员  员工或某些部门内的员工
     */
    protected List<Member> chooseRange;
    private SelectRangeFragment selectRangeFragment;
    /**
     * 已选人员
     */
    private List<Member> selectedMember;
    /**
     * 可选范围含有部门
     */
    boolean hasDepartment = false;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            checkType = intent.getIntExtra(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
            chooseRange = (List<Member>) intent.getSerializableExtra(C.CHOOSE_RANGE_TAG);
            selectedMember = (List<Member>) intent.getSerializableExtra(C.SELECTED_MEMBER_TAG);
            allEmployees.addAll(chooseRange);
        }
    }

    @Override
    public void init() {
        viewDelegate.setTitle("选择人员");
        if (C.MULTIL_SELECT == checkType) {
            viewDelegate.setBottomSelecterVisibility(View.VISIBLE);
        }
        initData();
    }

    private void initData() {
        Observable.from(allEmployees).filter(range -> range.getType() == C.DEPARTMENT).subscribe(range -> hasDepartment = true);
        if (hasDepartment) {
            Map<String, List<Member>> map = new HashMap<>(1);
            map.put("chooseRange", chooseRange);
            model.queryRangeEmployeeList(this, map, new ProgressSubscriber<EmployeeResultBean>(this) {
                @Override
                public void onNext(EmployeeResultBean employeeResultBean) {
                    super.onNext(employeeResultBean);
                    allEmployees = employeeResultBean.getData();
                    initFragment();
                }
            });
        } else {
            initFragment();
        }
    }

    private void initFragment() {
        Observable.from(selectedMember)
                .filter(selected -> selected.getType() == C.EMPLOYEE)
                .subscribe(selected -> Observable.from(allEmployees)
                        .filter(employee -> employee.getId() == selected.getId())
                        .subscribe(employee -> {
                            employee.setCheck(true);
                            employee.setSelectState(selected.getSelectState());
                        }));
        if (C.MULTIL_SELECT == checkType) {
            setAllCheckState();
        }
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(selectRangeFragment = new SelectRangeFragment());
        viewDelegate.setFragment(fragmentList);
    }


    /**
     * 获取已选中的员工
     *
     * @return
     */
    public List<Member> getCheckedEmployee() {
        ArrayList<Member> checkEmployees = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allEmployees)) {
            Observable.from(allEmployees).filter(Member::isCheck).subscribe(employee -> {
                employee.setValue(C.EMPLOYEE + ":" + employee.getId());
                checkEmployees.add(employee);
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
        for (Member member : allEmployees) {
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
        for (Member member : allEmployees) {
            if (member.isCheck()) {
                checkEmployeeSize++;
            }
        }
        viewDelegate.setSum(checkEmployeeSize);
    }

}
