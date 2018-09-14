package com.hjhq.teamface.common.ui.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.basis.bean.RoleGroupResponseBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.utils.MemberUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 选人、选部门、选角色、动态选择
 *
 * @author Administrator
 * @date 2018/1/5
 */

public class SelectMemberActivity extends ActivityPresenter<SelectMemberDelegate, CommonModel> {
    //特殊成员
    public ArrayList<Member> mSpecialMembers;

    //单选多选
    public int checkType;
    //选择范围  0成员 1部门 2角色 3动态参数  4全公司   默认成员
    protected String chooseRange = "0";
    protected boolean chooseGroup;

    //全部员工
    protected List<Member> allEmployees = new ArrayList<>();
    //全部部门
    private List<GetDepartmentStructureBean.MemberBean> allDepartment;
    //角色组
    private List<RoleGroupResponseBean.DataBean> allRoleGroup;
    //员工Fragment
    private SelectEmployeeFragment employeeFragment;
    //组织机构Fragment
    private CompanyOrganizationFragment organizationFragment;
    //部门fragment
    private DepartmentFragment departmentFragment;
    //角色fragment
    private RoleGroupFragment roleGroupFragment;
    //当前fragment下标
    private int currentPosition;
    private List<Fragment> fragmentList;
    private String title;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            checkType = intent.getIntExtra(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
            chooseGroup = getIntent().getBooleanExtra(Constants.DATA_TAG1, false);
            Serializable serializableExtra = intent.getSerializableExtra(C.SPECIAL_MEMBERS_TAG);
            if (serializableExtra == null) {
                mSpecialMembers = new ArrayList<>();
            } else {
                mSpecialMembers = (ArrayList<Member>) serializableExtra;
            }
            chooseRange = intent.getStringExtra(C.CHOOSE_RANGE_TAG);
            if (TextUtil.isEmpty(chooseRange)) {
                chooseRange = "0";
            }
            title = intent.getStringExtra(Constants.NAME);
        }
    }

    @Override
    public void init() {
        viewDelegate.setTitle(title);

        String[] split = chooseRange.split(",");
        fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>(split.length);
        for (int i = 0; i < split.length; i++) {
            switch (split[i]) {
                case "0":
                    titleList.add("联系人");
                    fragmentList.add(employeeFragment = new SelectEmployeeFragment());
                    break;
                case "1":
                    titleList.add("部门");
                    fragmentList.add(departmentFragment = new DepartmentFragment());
                    break;
                case "2":
                    titleList.add("角色");
                    fragmentList.add(roleGroupFragment = new RoleGroupFragment());
                    break;
                case "3":
                    titleList.add("动态参数");
                    fragmentList.add(departmentFragment = new DepartmentFragment());
                    break;
                default:
                    break;
            }
        }

        if (split.length == 1) {
            viewDelegate.setFragment(fragmentList);
        } else {
            String[] titles = new String[titleList.size()];
            titleList.toArray(titles);
            viewDelegate.initNavigator(titles, fragmentList);
            viewDelegate.setMagicIndicatorVisibility(View.VISIBLE);
        }
        if (C.MULTIL_SELECT == checkType) {
            viewDelegate.setBottomSelecterVisibility(View.VISIBLE);
        }
    }


    /**
     * 设置角色组
     *
     * @param allData
     */
    public void setRoleGroup(List<RoleGroupResponseBean.DataBean> allData) {
        if (allData == null) {
            return;
        }
        allRoleGroup = allData;
        int checkSize = getCheckedRoleGroup().size();
        boolean isAllSelect = getRoleSum() == checkSize;
        setAllCheckState(isAllSelect);
    }


    /**
     * 得到角色总数
     *
     * @return
     */
    private int getRoleSum() {
        int size = 0;
        if (!CollectionUtils.isEmpty(allRoleGroup)) {
            for (RoleGroupResponseBean.DataBean roleGroup : allRoleGroup) {
                List<RoleGroupResponseBean.DataBean.RolesBean> roles = roleGroup.getRoles();
                if (!CollectionUtils.isEmpty(roles)) {
                    size += roles.size();
                }
            }
        }
        return size;
    }


    /**
     * 设置部门
     *
     * @param allData
     */
    public void setDepartment(List<GetDepartmentStructureBean.MemberBean> allData) {
        if (allData == null) {
            return;
        }
        allDepartment = allData;

        int checkSize = getCheckedDepartment(allDepartment).size();
        boolean isAllSelect = getDepartmentSum(allDepartment) == checkSize;
        setAllCheckState(isAllSelect);
    }

    /**
     * 得到部门总数
     *
     * @param departmentList
     * @return
     */
    private int getDepartmentSum(List<GetDepartmentStructureBean.MemberBean> departmentList) {
        int size = 0;
        if (!CollectionUtils.isEmpty(departmentList)) {
            size += departmentList.size();
            for (GetDepartmentStructureBean.MemberBean memberBean : departmentList) {
                List<GetDepartmentStructureBean.MemberBean> childList = memberBean.getChildList();
                size += getDepartmentSum(childList);
            }
        }
        return size;
    }


    /**
     * 设置员工
     *
     * @param memberList
     */
    public void setEmployee(List<Member> memberList) {
        if (memberList == null) {
            return;
        }
        allEmployees = memberList;

        boolean isAllSelect = isAllSelect();
        setAllCheckState(isAllSelect);
    }


    /**
     * 设置全选状态
     *
     * @param isAllCheck
     */
    public void setAllCheckState(boolean isAllCheck) {
        viewDelegate.setAllCheckText(isAllCheck);
        viewDelegate.setAllSelected(isAllCheck);
        showSum();
    }


    /**
     * 得到可以自由选择的 是否全部选择成功
     */
    private boolean isAllSelect() {
        boolean bl = false;
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
            bl = true;
        }
        return bl;
    }

    /**
     * 得到可以自由选择的人数
     */
    private int getFreeEmployeeSum() {
        int size = 0;
        for (Member member : allEmployees) {
            if (!MemberUtils.checkState(member.getSelectState(), C.CAN_NOT_SELECT)) {
                size++;
            }
        }
        return size;
    }

    /**
     * 得到选择的员工人数
     */
    private int getCheckEmployeeSum() {
        int size = 0;
        for (Member member : allEmployees) {
            if (member.isCheck()) {
                size++;
            }
        }
        return size;
    }


    /**
     * 显示总过选中的数量
     */
    private void showSum() {
        int checkDepartmentSize = getCheckedDepartment(allDepartment).size();
        int checkEmployeeSize = getCheckEmployeeSum();
        int checkRoleSize = getCheckedRoleGroup().size();
        viewDelegate.setSum(checkDepartmentSize + checkEmployeeSize + checkRoleSize);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        //全选
        viewDelegate.ivCheckAllMember.setOnClickListener(v -> {
            boolean isChecked = !viewDelegate.isAllSelected();
            viewDelegate.setAllCheckText(isChecked);

            if (viewDelegate.getOrganizationVisibility() == View.VISIBLE) {
                organizationFragment.setAllSelect(isChecked);
            } else {
                Fragment fragment = fragmentList.get(currentPosition);
                if (fragment instanceof SelectEmployeeFragment) {
                    employeeFragment.setAllSelect(isChecked);
                } else if (fragment instanceof DepartmentFragment) {
                    departmentFragment.setAllSelect(isChecked);
                } else if (fragment instanceof RoleGroupFragment) {
                    roleGroupFragment.setAllSelect(isChecked);
                }
            }

        });
        viewDelegate.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;

                Fragment fragment = fragmentList.get(position);
                if (fragment instanceof SelectEmployeeFragment) {
                    setEmployee(allEmployees);
                } else if (fragment instanceof DepartmentFragment) {
                    setDepartment(allDepartment);
                } else if (fragment instanceof RoleGroupFragment) {
                    setRoleGroup(allRoleGroup);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<Member> list = new ArrayList<>();

        list.addAll(getCheckedEmployee());
        list.addAll(getCheckedDepartment(allDepartment));
        list.addAll(getCheckedRoleGroup());
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, list);
        setResult(Activity.RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取要返回的的员工
     *
     * @return
     */
    public List<Member> getCheckedEmployee() {
        ArrayList<Member> checkEmployees = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allEmployees)) {
            Observable.from(allEmployees).filter(employee -> {
                boolean check = employee.isCheck();
                boolean state = !MemberUtils.checkState(employee.getSelectState(), C.NOT_FOR_SELECT);
                return check && state;
            }).subscribe(employee -> {
                employee.setType(C.EMPLOYEE);
                employee.setValue(C.EMPLOYEE + ":" + employee.getId());
                checkEmployees.add(employee);
            });
        }
        return checkEmployees;
    }

    /**
     * 获取要返回的部门
     *
     * @return
     */
    public List<Member> getCheckedDepartment(List<GetDepartmentStructureBean.MemberBean> departmentList) {
        ArrayList<Member> checkDepartment = new ArrayList<>();
        if (!CollectionUtils.isEmpty(departmentList)) {
            for (GetDepartmentStructureBean.MemberBean memberBean : departmentList) {
                if (memberBean.isCheck() && !MemberUtils.checkState(memberBean.getSelectState(), C.NOT_FOR_SELECT)) {
                    memberBean.setType(C.DEPARTMENT);
                    memberBean.setValue(C.DEPARTMENT + ":" + memberBean.getId());
                    checkDepartment.add(memberBean);
                }
                List<GetDepartmentStructureBean.MemberBean> childList = memberBean.getChildList();

                List<Member> checkDepartment1 = getCheckedDepartment(childList);
                checkDepartment.addAll(checkDepartment1);
            }
        }
        return checkDepartment;
    }


    /**
     * 获取要返回的角色组
     *
     * @return
     */
    public List<Member> getCheckedRoleGroup() {
        ArrayList<Member> checkDepartment = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allRoleGroup)) {
            for (RoleGroupResponseBean.DataBean roleGroup : allRoleGroup) {
                List<RoleGroupResponseBean.DataBean.RolesBean> roles = roleGroup.getRoles();
                if (CollectionUtils.isEmpty(roles)) {
                    continue;
                }
                for (RoleGroupResponseBean.DataBean.RolesBean roleBean : roles) {
                    if (roleBean.isCheck() && !MemberUtils.checkState(roleBean.getSelectState(), C.NOT_FOR_SELECT)) {
                        roleBean.setType(C.ROLE);
                        roleBean.setValue(C.ROLE + ":" + roleBean.getId());
                        checkDepartment.add(roleBean);
                    }
                }
            }
        }
        return checkDepartment;
    }

    /**
     * 显示组织架构
     */
    public void showCompanyOrganization() {
        if (organizationFragment == null) {
            FragmentManager manager = getSupportFragmentManager();
            organizationFragment = new CompanyOrganizationFragment();
            manager.beginTransaction().add(R.id.fl_company_organization, organizationFragment).commit();
        } else {
            organizationFragment.refresh();
        }


        viewDelegate.setOrganizationVisibility(View.VISIBLE);
        viewDelegate.setMagicIndicatorVisibility(View.GONE);
        viewDelegate.setViewPager(View.GONE);
        viewDelegate.setNavigationIcon(R.drawable.icon_blue_back, view -> {
            boolean isBack = true;
            if (viewDelegate.getOrganizationVisibility() == View.VISIBLE) {
                isBack = organizationFragment.onBackPressed();
            } else {
                Fragment fragment = fragmentList.get(currentPosition);
                if (fragment instanceof DepartmentFragment) {
                    isBack = departmentFragment.onBackPressed();
                } else if (fragment instanceof RoleGroupFragment) {
                    isBack = roleGroupFragment.onBackPressed();
                }
            }
            if (isBack) {
                hideCompanyOrganization();
            }
        });
    }

    /**
     * 隐藏组织架构
     */
    public void hideCompanyOrganization() {
        viewDelegate.setNavigationIcon(R.drawable.text_cancel, v -> finish());
        viewDelegate.setOrganizationVisibility(View.GONE);

        viewDelegate.setMagicIndicatorVisibility(fragmentList.size() > 1 ? View.VISIBLE : View.GONE);
        viewDelegate.setViewPager(View.VISIBLE);
        setEmployee(allEmployees);
        employeeFragment.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        boolean isBack = true;
        if (viewDelegate.getOrganizationVisibility() == View.VISIBLE) {
            isBack = organizationFragment.onBackPressed();
        } else {
            Fragment fragment = fragmentList.get(currentPosition);
            if (fragment instanceof DepartmentFragment) {
                isBack = departmentFragment.onBackPressed();
            } else if (fragment instanceof RoleGroupFragment) {
                isBack = roleGroupFragment.onBackPressed();
            }
        }

        if (isBack) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SEARCH_MEMBER && resultCode == RESULT_OK) {
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }
}
