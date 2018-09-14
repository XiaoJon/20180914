package com.hjhq.teamface.common.ui.member;

import android.os.Build;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ParseUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.adapter.SelectEmployeeAdapter;
import com.hjhq.teamface.common.bean.PinyinComparator2;
import com.hjhq.teamface.common.bean.SortModel;
import com.hjhq.teamface.common.utils.MemberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import rx.Observable;


/**
 * 选择部门范围
 *
 * @author Administrator
 */
public class SelectRangeDepartmentFragment extends FragmentPresenter<SelectEmployeeDelegate, CommonModel> implements View.OnClickListener {

    private List<SortModel> mAllContactsList;

    //根据拼音来排列ListView里面的数据类
    private PinyinComparator2 pinyinComparator;
    private SelectRangeDepartmentActivity mActivity;
    private SelectEmployeeAdapter mAdapter;

    @Override
    protected void init() {
        mActivity = (SelectRangeDepartmentActivity) getActivity();
        viewDelegate.companyMember.setVisibility(View.GONE);
        viewDelegate.setSearchVisibility(View.GONE);
        initAdapter();
        sortContacts();
    }


    private void initAdapter() {
        // 给ListView设置adapter
        mAllContactsList = new ArrayList<>();
        pinyinComparator = PinyinComparator2.getInstance();
        // 根据a-z进行排序源数据
        Collections.sort(mAllContactsList, pinyinComparator);
        mAdapter = new SelectEmployeeAdapter(mAllContactsList);
        viewDelegate.setAdapter(mAdapter);
    }

    public void setData(List<Member> list) {

    }

    /**
     * 联系人排序
     */
    private void sortContacts() {
        Collections.sort(mActivity.allDepartment, (o1, o2) ->
                ParseUtil.getSortLetterBySortKey(o1.getEmployee_name()).compareTo(ParseUtil.getSortLetterBySortKey(o2.getEmployee_name())));
        HashSet<String> letterSet = new HashSet<>();

        for (Member dataBean : mActivity.allDepartment) {
            String departmentName = dataBean.getDepartment_name();
            String phone = dataBean.getPhone();

            SortModel sortModel = new SortModel(departmentName, phone, departmentName);
            sortModel.member = dataBean;
            String sortLetters = ParseUtil.getSortLetterBySortKey(departmentName);
            if (sortLetters == null) {
                sortLetters = ParseUtil.getSortLetter(departmentName);
            }

            letterSet.add(sortLetters);
            sortModel.sortLetters = sortLetters;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                sortModel.sortToken = ParseUtil.parseSortKey(departmentName);
            } else {
                sortModel.sortToken = ParseUtil.parseSortKeyLollipop(departmentName);
            }

            mAllContactsList.add(sortModel);
        }

        String[] letterArray = new String[]{};
        letterArray = letterSet.toArray(letterArray);
        Arrays.sort(letterArray);

        final String[] finalLetterArray = letterArray;

        mAdapter.notifyDataSetChanged();
        viewDelegate.setSideBarData(finalLetterArray);
        // LogUtil.e("排序ing3");
        if (CollectionUtils.isEmpty(mAllContactsList)) {
            viewDelegate.setStateText("数据为空");
        } else {
            viewDelegate.setStateVisibility(View.GONE);
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.company_member_contacts_rl, R.id.search_edit_text);

        //设置右侧[A-Z]快速导航栏触摸监听
        viewDelegate.sideBar.setOnTouchingLetterChangedListener(s -> {
            //该字母首次出现的位置
            int position = mAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                viewDelegate.scrollToPosition(position + 1);
            }
        });
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                SortModel sortModel = mAllContactsList.get(position);
                if (MemberUtils.checkState(sortModel.member.getSelectState(), C.FREE_TO_SELECT)) {
                    if (mActivity.checkType == C.RADIO_SELECT) {
                        Observable.from(mAllContactsList)
                                .subscribe(model -> model.member.setCheck(false));
                        sortModel.member.setCheck(true);
                    } else if (mActivity.checkType == C.MULTIL_SELECT) {
                        sortModel.member.setCheck(!sortModel.member.isCheck());
                        mActivity.setAllCheckState();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_edit_text) {
            ToastUtils.showToast(getContext(), "暂不可用");
//            Bundle bundle = new Bundle();
//            bundle.putInt(C.CHECK_TYPE_TAG, mActivity.checkType);
//            bundle.putInt(C.MEMBER_TYPE_TAG, C.EMPLOYEE);
//            CommonUtil.startActivtiyForResult(mActivity, SearchMemberActivity.class, Constants.REQUEST_SEARCH_MEMBER, bundle);
        }
    }

    /**
     * 全选
     *
     * @param isChecked
     */
    public void setAllSelect(boolean isChecked) {
        Observable.from(mActivity.allDepartment)
                .filter(member -> MemberUtils.checkState(member.getSelectState(), C.FREE_TO_SELECT))
                .subscribe(member -> member.setCheck(isChecked));
        mAdapter.notifyDataSetChanged();
    }
}