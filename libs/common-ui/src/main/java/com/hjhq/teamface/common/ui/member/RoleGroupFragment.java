package com.hjhq.teamface.common.ui.member;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.bean.RoleGroupResponseBean;
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
import com.hjhq.teamface.common.adapter.SelectRoleGroupAdapter;
import com.hjhq.teamface.common.adapter.SelectRolesAdapter;
import com.hjhq.teamface.common.bean.OrganizationNaviData;
import com.hjhq.teamface.common.utils.MemberUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 角色
 */
public class RoleGroupFragment extends FragmentPresenter<RoleGroupDelegate, CommonModel> implements View.OnClickListener {
    private SelectMemberActivity mActivity;
    private SelectRoleGroupAdapter mSelectRoleGroupAdapter;
    private OrganizationNaviAdapter mNaviAdapter;
    private SelectRolesAdapter mSelectRolesAdapter;

    /**
     * 当前导航数据
     */
    ArrayList<OrganizationNaviData> naviData = new ArrayList<>();
    /**
     * 当前角色
     */
    private List<RoleGroupResponseBean.DataBean.RolesBean> roleGroupList = new ArrayList<>();

    /**
     * 所有数据
     */
    private List<RoleGroupResponseBean.DataBean> allData;
    //导航层级
    int roleLevel;

    @Override
    protected void init() {
        mActivity = (SelectMemberActivity) getActivity();
        mNaviAdapter = new OrganizationNaviAdapter(naviData);

        viewDelegate.setNaviAdapter(mNaviAdapter);
        initData();
    }


    private void initData() {
        roleLevel = 0;
        model.getRoleGroupList((RxAppCompatActivity) getActivity(), new ProgressSubscriber<RoleGroupResponseBean>(getActivity()) {
            @Override
            public void onNext(RoleGroupResponseBean baseBean) {
                super.onNext(baseBean);
                allData = baseBean.getData();
                filterData();
                mActivity.setRoleGroup(allData);
                handleRoleGroupData();

                OrganizationNaviData organizationNaviData = new OrganizationNaviData();
                organizationNaviData.setOrganizationId(SPUtils.getString(getContext(), AppConst.COMPANY_ID));
                organizationNaviData.setOrganizationLevel(roleLevel);
                organizationNaviData.setOrganizationName("角色");
                naviData.clear();
                naviData.add(organizationNaviData);
                mNaviAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 过滤数据
     */
    private void filterData() {
        for (RoleGroupResponseBean.DataBean dataBean : allData) {
            List<RoleGroupResponseBean.DataBean.RolesBean> roles = dataBean.getRoles();
            if (CollectionUtils.isEmpty(roles)) {
                continue;
            }
            for (RoleGroupResponseBean.DataBean.RolesBean rolesBean : roles) {
                for (Member member : mActivity.mSpecialMembers) {
                    if (member.getType() == 2 && member.getId() == rolesBean.getId()) {
                        rolesBean.setCheck(member.isCheck());
                        rolesBean.setSelectState(member.getSelectState());
                    }
                }
            }

            //去掉需要隐藏的条目
            Iterator<RoleGroupResponseBean.DataBean.RolesBean> iterator = roles.iterator();
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
        viewDelegate.mRvDataList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                if (roleLevel == 0) {
                    RoleGroupResponseBean.DataBean dataBean = (RoleGroupResponseBean.DataBean) adapter.getItem(position);
                    List<RoleGroupResponseBean.DataBean.RolesBean> roles = dataBean.getRoles();
                    handleRolesData(roles);

                    OrganizationNaviData organizationNaviData = new OrganizationNaviData();
                    organizationNaviData.setOrganizationId(dataBean.getId() + "");
                    organizationNaviData.setOrganizationLevel(++roleLevel);
                    organizationNaviData.setOrganizationName(dataBean.getName());
                    naviData.add(organizationNaviData);
                    mNaviAdapter.notifyDataSetChanged();
                } else {
                    RoleGroupResponseBean.DataBean.RolesBean rolesBean = roleGroupList.get(position);
                    if (MemberUtils.checkState(rolesBean.getSelectState(), C.FREE_TO_SELECT)) {
                        if (mActivity.checkType == C.RADIO_SELECT) {
                            setRolesSelect(roleGroupList, false);
                            rolesBean.setCheck(true);
                        } else if (mActivity.checkType == C.MULTIL_SELECT) {
                            rolesBean.setCheck(!rolesBean.isCheck());
                        }
                        mSelectRolesAdapter.notifyDataSetChanged();
                        mActivity.setRoleGroup(allData);
                    }
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if (roleLevel == 0) {
                    RoleGroupResponseBean.DataBean dataBean = (RoleGroupResponseBean.DataBean) adapter.getItem(position);
                    List<RoleGroupResponseBean.DataBean.RolesBean> roles = dataBean.getRoles();

                    setRolesSelect(roles, !dataBean.isCheck());
                    handleRoleGroupData();

                    mActivity.setRoleGroup(allData);
                } else {
                    RoleGroupResponseBean.DataBean.RolesBean rolesBean = roleGroupList.get(position);

                    if (MemberUtils.checkState(rolesBean.getSelectState(), C.FREE_TO_SELECT)) {
                        if (mActivity.checkType == C.RADIO_SELECT) {
                            setRolesSelect(roleGroupList, false);
                            rolesBean.setCheck(true);
                        } else if (mActivity.checkType == C.MULTIL_SELECT) {
                            boolean isCheck = !rolesBean.isCheck();
                            rolesBean.setCheck(isCheck);
                        }
                        mSelectRolesAdapter.notifyDataSetChanged();
                        mActivity.setRoleGroup(allData);
                    }
                }
            }
        });
        viewDelegate.mRvNavi.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                OrganizationNaviData organizationNaviData = naviData.get(position);
                int level = organizationNaviData.getOrganizationLevel();
                if (level > 0 || level == roleLevel) {
                    return;
                }

                //处理角色组
                handleRoleGroupData();
                naviData.remove(1);
                roleLevel = level;
                mNaviAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 处理角色组数据
     */
    private void handleRoleGroupData() {
        if (mSelectRoleGroupAdapter == null) {
            mSelectRoleGroupAdapter = new SelectRoleGroupAdapter(allData, mActivity.checkType);
        }
        if (!CollectionUtils.isEmpty(allData)) {
            for (RoleGroupResponseBean.DataBean dataBean : allData) {
                List<RoleGroupResponseBean.DataBean.RolesBean> roles = dataBean.getRoles();
                if (CollectionUtils.isEmpty(roles)) {
                    continue;
                }
                boolean allSelect = true;
                for (RoleGroupResponseBean.DataBean.RolesBean roleBean : roles) {
                    //自由选未选中
                    if (!roleBean.isCheck() && MemberUtils.checkState(roleBean.getSelectState(), C.FREE_TO_SELECT)) {
                        allSelect = false;
                        break;
                    }
                }
                dataBean.setCheck(allSelect);
            }
        }
        viewDelegate.setDataAdapter(mSelectRoleGroupAdapter);
    }

    private void handleRolesData(List<RoleGroupResponseBean.DataBean.RolesBean> newData) {
        roleGroupList.clear();
        if (!CollectionUtils.isEmpty(newData)) {
            roleGroupList.addAll(newData);
        }
        if (mSelectRolesAdapter == null) {
            mSelectRolesAdapter = new SelectRolesAdapter(roleGroupList);
        }
        viewDelegate.setDataAdapter(mSelectRolesAdapter);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.search_edit_text) {
            Bundle bundle = new Bundle();
            bundle.putInt(C.CHECK_TYPE_TAG, mActivity.checkType);
            bundle.putInt(C.MEMBER_TYPE_TAG, C.ROLE);
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
        setRolesGroupSelect(allData, isChecked);
        mSelectRoleGroupAdapter.notifyDataSetChanged();
        mActivity.setRoleGroup(allData);
    }

    /**
     * 设置角色 选中
     *
     * @param roleGroupList
     * @param isCheck       是否选中
     */
    public void setRolesGroupSelect(List<RoleGroupResponseBean.DataBean> roleGroupList, boolean isCheck) {
        if (CollectionUtils.isEmpty(roleGroupList)) {
            return;
        }
        for (RoleGroupResponseBean.DataBean roleGroup : roleGroupList) {
            roleGroup.setCheck(isCheck);
            setRolesSelect(roleGroup.getRoles(), isCheck);
        }
    }

    /**
     * 设置角色 选中
     *
     * @param rolesBeanList
     * @param isCheck       是否选中
     */
    public void setRolesSelect(List<RoleGroupResponseBean.DataBean.RolesBean> rolesBeanList, boolean isCheck) {
        if (!CollectionUtils.isEmpty(rolesBeanList)) {
            for (RoleGroupResponseBean.DataBean.RolesBean bean : rolesBeanList) {
                bean.setCheck(isCheck);
            }
        }
    }

    public boolean onBackPressed() {
        boolean isBack = true;
        if (roleLevel > 0) {
            naviData.remove(roleLevel);
            roleLevel--;
            //处理角色组
            handleRoleGroupData();
            mNaviAdapter.notifyDataSetChanged();
            isBack = false;
        }
        return isBack;
    }
}