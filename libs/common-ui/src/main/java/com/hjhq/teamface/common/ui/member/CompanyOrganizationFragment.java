package com.hjhq.teamface.common.ui.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.adapter.CompanyOrganizationAdapter;
import com.hjhq.teamface.common.adapter.OrganizationEmployeeAdapter;
import com.hjhq.teamface.common.adapter.OrganizationNaviAdapter;
import com.hjhq.teamface.common.bean.OrganizationNaviData;
import com.hjhq.teamface.common.utils.MemberUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Observable;


/**
 * 组织架构
 */
public class CompanyOrganizationFragment extends FragmentPresenter<CompanyOrganizationDelegate, CommonModel> implements View.OnClickListener {
    private SelectMemberActivity mActivity;

    CompanyOrganizationAdapter mDataAdapter;
    OrganizationNaviAdapter mNaviAdapter;
    OrganizationEmployeeAdapter mEmployeeAdapter;

    /**
     * 当前导航数据
     */
    ArrayList<OrganizationNaviData> naviData = new ArrayList<>();
    /**
     * 当前部门数据
     */
    List<GetDepartmentStructureBean.MemberBean> departmentData = new ArrayList<>();
    /**
     * 当前员工数据
     */
    List<Member> employeeList = new ArrayList<>();

    /**
     * 所有数据
     */
    private List<GetDepartmentStructureBean.MemberBean> allData;
    //导航层级
    int organizationLevel;

    @Override
    protected void init() {
        mDataAdapter = new CompanyOrganizationAdapter(departmentData);
        mNaviAdapter = new OrganizationNaviAdapter(naviData);
        mEmployeeAdapter = new OrganizationEmployeeAdapter(employeeList);

        if (getActivity() instanceof SelectMemberActivity) {
            mActivity = (SelectMemberActivity) getActivity();
            mDataAdapter.setCheckType(mActivity.checkType);
        }

        viewDelegate.setDataAdapter(mDataAdapter);
        viewDelegate.setNaviAdapter(mNaviAdapter);
        viewDelegate.setEmployeeAdapter(mEmployeeAdapter);
        initData();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        organizationLevel = 0;
        String companyId = SPUtils.getString(getContext(), AppConst.COMPANY_ID);
        model.findUsersByCompany((RxAppCompatActivity) getActivity(), companyId, new ProgressSubscriber<GetDepartmentStructureBean>(getActivity()) {
            @Override
            public void onNext(GetDepartmentStructureBean getDepartmentStructureBean) {
                super.onNext(getDepartmentStructureBean);
                allData = getDepartmentStructureBean.getData();
                filterData(allData);
                handleData(allData, null);

                OrganizationNaviData organizationNaviData = new OrganizationNaviData();
                organizationNaviData.setOrganizationId(companyId);
                organizationNaviData.setOrganizationLevel(organizationLevel);
                organizationNaviData.setOrganizationName("联系人");
                organizationNaviData.setDataBeen(allData);
                naviData.clear();
                naviData.add(organizationNaviData);
                mNaviAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 过滤数据
     */
    private void filterData(List<GetDepartmentStructureBean.MemberBean> allData) {
        if (CollectionUtils.isEmpty(allData)) {
            return;
        }
        for (GetDepartmentStructureBean.MemberBean memberBean : allData) {
            List<GetDepartmentStructureBean.MemberBean> childList = memberBean.getChildList();
            List<Member> users = memberBean.getUsers();

            filterData(childList);
            if (CollectionUtils.isEmpty(users)) {
                continue;
            }
            //将选人的数据状态赋值给组织架构
            for (Member user : users) {
                for (Member employee : mActivity.allEmployees) {
                    if (user.getId() == employee.getId()) {
                        user.setCheck(employee.isCheck());
                        user.setSelectState(employee.getSelectState());
                    }
                }
            }

            //去掉需要隐藏的条目
            Iterator<Member> iterator = users.iterator();
            while (iterator.hasNext()) {
                if (MemberUtils.checkState(iterator.next().getSelectState(), C.HIDE_TO_SELECT)) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.search_rl);
        viewDelegate.mRvDataList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                GetDepartmentStructureBean.MemberBean dataBean = departmentData.get(position);
                List<GetDepartmentStructureBean.MemberBean> childList = dataBean.getChildList();
                List<Member> users = dataBean.getUsers();
                handleData(childList, users);

                OrganizationNaviData organizationNaviData = new OrganizationNaviData();
                organizationNaviData.setOrganizationId(dataBean.getId() + "");
                organizationNaviData.setOrganizationLevel(++organizationLevel);
                organizationNaviData.setOrganizationName(dataBean.getName());
                organizationNaviData.setDataBeen(childList);
                organizationNaviData.setMemberList(users);
                naviData.add(organizationNaviData);
                mNaviAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                GetDepartmentStructureBean.MemberBean dataBean = departmentData.get(position);
                if (mActivity.checkType == C.MULTIL_SELECT) {
                    boolean isCheck = !dataBean.isCheck();
                    setDepartMentSelect(dataBean, isCheck);
                    mDataAdapter.notifyDataSetChanged();
                }
                updateCheckState();
            }
        });
        viewDelegate.mRvEmployee.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                Member dataBean = employeeList.get(position);
                if (MemberUtils.checkState(dataBean.getSelectState(), C.CAN_NOT_SELECT)) {
                    return;
                }
                if (mActivity.checkType == C.RADIO_SELECT) {
                    setDataSelect(allData, null, false);
                    dataBean.setCheck(true);
                } else if (mActivity.checkType == C.MULTIL_SELECT) {
                    dataBean.setCheck(!dataBean.isCheck());
                }
                mEmployeeAdapter.notifyDataSetChanged();
                updateCheckState();
            }
        });
        viewDelegate.mRvNavi.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                OrganizationNaviData organizationNaviData = naviData.get(position);
                int level = organizationNaviData.getOrganizationLevel();
                if (level >= organizationLevel) {
                    return;
                }
                handleData(organizationNaviData.getDataBeen(), organizationNaviData.getMemberList());
                for (int i = organizationLevel; i > level; i--) {
                    naviData.remove(i);
                }
                organizationLevel = level;
                mNaviAdapter.notifyDataSetChanged();
                updateAllCheckState();

            }
        });
    }

    /**
     * 检查部门和员工是否已选
     *
     * @param allData
     */
    private boolean checkSelected(List<GetDepartmentStructureBean.MemberBean> allData, List<Member> userList) {
        boolean allSelect = true;
        if (!CollectionUtils.isEmpty(allData)) {
            for (GetDepartmentStructureBean.MemberBean memberBean : allData) {
                List<Member> users = memberBean.getUsers();
                //递归判断部门下的子部门和员工是否全部选中
                boolean isCheck = checkSelected(memberBean.getChildList(), users);
                if (!isCheck) {
                    allSelect = false;
                }
                memberBean.setCheck(isCheck);
            }
        }
        if (!CollectionUtils.isEmpty(userList)) {
            for (Member user : userList) {
                if (!user.isCheck() && MemberUtils.checkState(user.getSelectState(), C.FREE_TO_SELECT)) {
                    allSelect = false;
                    break;
                }
            }
        }
        return allSelect;
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        filterData(allData);
        checkSelected(departmentData, employeeList);
    }

    /**
     * 更新全选状态
     */
    private void updateAllCheckState() {
        boolean isAllCheck = true;
        for (GetDepartmentStructureBean.MemberBean memberBean : departmentData) {
            if (!memberBean.isCheck()) {
                isAllCheck = false;
            }
        }
        for (Member member : employeeList) {
            if (MemberUtils.checkState(member.getSelectState(), C.FREE_TO_SELECT) && !member.isCheck()) {
                isAllCheck = false;
            }
        }
        mActivity.setAllCheckState(isAllCheck);
    }

    /**
     * 更新选择人员的状态
     */
    private void updateCheckState() {
        Observable.from(mActivity.allEmployees)
                .filter(member -> MemberUtils.checkState(member.getSelectState(), C.FREE_TO_SELECT))
                .subscribe(member -> member.setCheck(false));

        updateCheckState(allData, null);

        updateAllCheckState();
    }

    /**
     * 选中人员状态 更新
     */
    private void updateCheckState(List<GetDepartmentStructureBean.MemberBean> allData, List<Member> userList) {
        if (!CollectionUtils.isEmpty(allData)) {
            for (GetDepartmentStructureBean.MemberBean memberBean : allData) {
                List<Member> users = memberBean.getUsers();
                //递归判断部门下的子部门和员工是否全部选中
                updateCheckState(memberBean.getChildList(), users);
            }
        }
        if (!CollectionUtils.isEmpty(userList)) {
            for (Member member : mActivity.allEmployees) {
                if (!MemberUtils.checkState(member.getSelectState(), C.FREE_TO_SELECT)) {
                    continue;
                }
                for (Member user : userList) {
                    if (user.isCheck() && member.getId() == user.getId()) {
                        member.setCheck(true);
                    }
                }
            }
        }
    }


    /**
     * 处理数据
     *
     * @param lists
     */
    private void handleData(List<GetDepartmentStructureBean.MemberBean> lists, List<Member> users) {
        if (lists == null) {
            lists = new ArrayList<>();
        }
        if (users == null) {
            users = new ArrayList<>();
        }
        checkSelected(lists, users);

        departmentData.clear();
        departmentData.addAll(lists);
        mDataAdapter.notifyDataSetChanged();


        employeeList.clear();
        employeeList.addAll(users);
        mEmployeeAdapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.search_rl) {
            Bundle bundle = new Bundle();
            bundle.putInt(C.CHECK_TYPE_TAG, mActivity.checkType);
            bundle.putInt(C.MEMBER_TYPE_TAG, C.EMPLOYEE);
            if (naviData.size() > 1) {
                bundle.putString(Constants.DATA_TAG1, naviData.get(naviData.size() - 1).getOrganizationId());
            }
            CommonUtil.startActivtiyForResult(mActivity, SearchMemberActivity.class, Constants.REQUEST_SEARCH_MEMBER, bundle);
        }
    }

    /**
     * 设置全选
     *
     * @param isChecked
     */
    public void setAllSelect(boolean isChecked) {
        setDataSelect(departmentData, employeeList, isChecked);
        checkSelected(allData, null);
        updateCheckState();
        mDataAdapter.notifyDataSetChanged();
        mEmployeeAdapter.notifyDataSetChanged();
    }

    /**
     * 设置数据
     *
     * @param allData
     * @param userList
     * @param isCheck
     */
    private void setDataSelect(List<GetDepartmentStructureBean.MemberBean> allData, List<Member> userList, boolean isCheck) {
        if (!CollectionUtils.isEmpty(allData)) {
            for (GetDepartmentStructureBean.MemberBean memberBean : allData) {
                List<Member> users = memberBean.getUsers();
                //递归判断部门下的子部门和员工是否全部选中
                setDataSelect(memberBean.getChildList(), users, isCheck);
                memberBean.setCheck(isCheck);
            }
        }
        if (!CollectionUtils.isEmpty(userList)) {
            Observable.from(userList)
                    .filter(user -> MemberUtils.checkState(user.getSelectState(), C.FREE_TO_SELECT))
                    .subscribe(user -> user.setCheck(isCheck));
        }
    }

    /**
     * 设置部门 选中
     *
     * @param memberBean
     * @param isCheck    是否选中
     */
    public void setDepartMentSelect(GetDepartmentStructureBean.MemberBean memberBean, boolean isCheck) {
        memberBean.setCheck(isCheck);
        List<Member> users = memberBean.getUsers();
        List<GetDepartmentStructureBean.MemberBean> childList = memberBean.getChildList();

        if (!CollectionUtils.isEmpty(childList)) {
            for (GetDepartmentStructureBean.MemberBean bean : childList) {
                setDepartMentSelect(bean, isCheck);
            }
        }

        if (!CollectionUtils.isEmpty(users)) {
            Observable.from(users)
                    .filter(member -> MemberUtils.checkState(member.getSelectState(), C.FREE_TO_SELECT))
                    .subscribe(member -> member.setCheck(isCheck));
        }
    }

    public boolean onBackPressed() {
        boolean isBack = true;
        if (organizationLevel > 0) {
            naviData.remove(organizationLevel);
            organizationLevel--;
            OrganizationNaviData organizationNaviData = naviData.get(organizationLevel);
            handleData(organizationNaviData.getDataBeen(), organizationNaviData.getMemberList());
            mNaviAdapter.notifyDataSetChanged();
            isBack = false;
        } else if (organizationLevel == 0) {
            mActivity.hideCompanyOrganization();
            isBack = false;
        }
        return isBack;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SEARCH_MEMBER && resultCode == Activity.RESULT_OK){
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
    }
}