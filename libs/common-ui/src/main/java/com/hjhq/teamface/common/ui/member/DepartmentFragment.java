package com.hjhq.teamface.common.ui.member;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.adapter.OrganizationNaviAdapter;
import com.hjhq.teamface.common.adapter.SelectDepartmentAdapter;
import com.hjhq.teamface.common.bean.OrganizationNaviData;
import com.hjhq.teamface.common.utils.MemberUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 部门
 */
public class DepartmentFragment extends FragmentPresenter<DepartmentDelegate, CommonModel> implements View.OnClickListener {
    private SelectMemberActivity mActivity;
    SelectDepartmentAdapter mDepartmentAdapter;
    OrganizationNaviAdapter mNaviAdapter;

    /**
     * 当前导航数据
     */
    ArrayList<OrganizationNaviData> naviData = new ArrayList<>();
    /**
     * 当前部门数据
     */
    List<GetDepartmentStructureBean.MemberBean> departmentData = new ArrayList<>();

    /**
     * 所有数据
     */
    private List<GetDepartmentStructureBean.MemberBean> allData;
    //导航层级
    int organizationLevel;

    @Override
    protected void init() {
        mDepartmentAdapter = new SelectDepartmentAdapter(departmentData);
        mNaviAdapter = new OrganizationNaviAdapter(naviData);

        mActivity = (SelectMemberActivity) getActivity();

        viewDelegate.setDataAdapter(mDepartmentAdapter);
        viewDelegate.setNaviAdapter(mNaviAdapter);
        initData();
    }


    private void initData() {
        organizationLevel = 0;

        String companyId = SPUtils.getString(getContext(), AppConst.COMPANY_ID);
        model.findUsersByCompany((RxAppCompatActivity) getActivity(), companyId, new ProgressSubscriber<GetDepartmentStructureBean>(getActivity()) {
            @Override
            public void onNext(GetDepartmentStructureBean getDepartmentStructureBean) {
                super.onNext(getDepartmentStructureBean);
                allData = getDepartmentStructureBean.getData();
                filterData(allData);
                mActivity.setDepartment(allData);
                handleDepartmentData(allData);

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
    private void filterData(List<GetDepartmentStructureBean.MemberBean> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (GetDepartmentStructureBean.MemberBean dataBean : dataList) {
            filterData(dataBean.getChildList());
            for (Member member : mActivity.mSpecialMembers) {
                if (member.getType() == 0 && member.getId() == dataBean.getId()) {
                    dataBean.setCheck(member.isCheck());
                    dataBean.setSelectState(member.getSelectState());
                }
            }
        }
        //去掉需要隐藏的条目
        Iterator<GetDepartmentStructureBean.MemberBean> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            if (MemberUtils.checkState(iterator.next().getSelectState(), C.HIDE_TO_SELECT)) {
                iterator.remove();
            }
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRvDataList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                GetDepartmentStructureBean.MemberBean dataBean = departmentData.get(position);
                List<GetDepartmentStructureBean.MemberBean> childList = dataBean.getChildList();
                handleDepartmentData(childList);

                if (!CollectionUtils.isEmpty(childList)) {
                    OrganizationNaviData organizationNaviData = new OrganizationNaviData();
                    organizationNaviData.setOrganizationId(dataBean.getId() + "");
                    organizationNaviData.setOrganizationLevel(++organizationLevel);
                    organizationNaviData.setOrganizationName(dataBean.getName());
                    organizationNaviData.setDataBeen(childList);
                    naviData.add(organizationNaviData);
                    mNaviAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                GetDepartmentStructureBean.MemberBean dataBean = departmentData.get(position);
                if (MemberUtils.checkState(dataBean.getSelectState(), C.FREE_TO_SELECT)) {
                    if (mActivity.checkType == C.RADIO_SELECT) {
                        setDepartMentSelect(allData, false);
                        dataBean.setCheck(true);
                    } else if (mActivity.checkType == C.MULTIL_SELECT) {
                        dataBean.setCheck(!dataBean.isCheck());
                    }
                    adapter.notifyDataSetChanged();
                    mActivity.setDepartment(allData);
                }
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

                //处理部门
                handleDepartmentData(organizationNaviData.getDataBeen());

                for (int i = organizationLevel; i > level; i--) {
                    naviData.remove(i);
                }
                organizationLevel = level;
                mNaviAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 处理部门数据
     *
     * @param data
     */
    private void handleDepartmentData(List<GetDepartmentStructureBean.MemberBean> data) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        departmentData.clear();
        departmentData.addAll(data);
        mDepartmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.search_edit_text) {
            Bundle bundle = new Bundle();
            bundle.putInt(C.CHECK_TYPE_TAG, mActivity.checkType);
            bundle.putInt(C.MEMBER_TYPE_TAG, C.DEPARTMENT);
            ToastUtils.showError(getActivity(),"敬请期待");
//            CommonUtil.startActivtiyForResult(mActivity, SearchMemberActivity.class, Constants.REQUEST_SEARCH_MEMBER, bundle);

        }
    }

    /**
     * 设置全选
     *
     * @param isChecked
     */
    public void setAllSelect(boolean isChecked) {
        setDepartMentSelect(allData, isChecked);
        mDepartmentAdapter.notifyDataSetChanged();
        mActivity.setDepartment(allData);
    }

    /**
     * 设置部门 选中
     *
     * @param memberBeans
     * @param isCheck     是否选中
     */
    public void setDepartMentSelect(List<GetDepartmentStructureBean.MemberBean> memberBeans, boolean isCheck) {
        if (CollectionUtils.isEmpty(memberBeans)) {
            return;
        }
        for (GetDepartmentStructureBean.MemberBean memberBean : memberBeans) {
            setDepartMentSelect(memberBean, isCheck);
        }
    }

    /**
     * 设置部门 选中
     *
     * @param memberBean
     * @param isCheck    是否选中
     */
    public void setDepartMentSelect(GetDepartmentStructureBean.MemberBean memberBean, boolean isCheck) {
        if (memberBean == null) {
            return;
        }
        if (MemberUtils.checkState(memberBean.getSelectState(), C.FREE_TO_SELECT)) {
            memberBean.setCheck(isCheck);
        }
        List<GetDepartmentStructureBean.MemberBean> childList = memberBean.getChildList();

        if (!CollectionUtils.isEmpty(childList)) {
            for (GetDepartmentStructureBean.MemberBean bean : childList) {
                setDepartMentSelect(bean, isCheck);
            }
        }
    }

    public boolean onBackPressed() {
        boolean isBack = true;
        if (organizationLevel > 0) {
            naviData.remove(organizationLevel);
            organizationLevel--;
            OrganizationNaviData organizationNaviData = naviData.get(organizationLevel);
            handleDepartmentData(organizationNaviData.getDataBeen());
            mNaviAdapter.notifyDataSetChanged();
            isBack = false;
        }
        return isBack;
    }
}