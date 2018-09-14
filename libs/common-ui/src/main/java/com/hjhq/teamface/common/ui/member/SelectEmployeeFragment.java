package com.hjhq.teamface.common.ui.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.EmployeeResultBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ParseUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.adapter.SelectEmployeeAdapter;
import com.hjhq.teamface.common.bean.PinyinComparator2;
import com.hjhq.teamface.common.bean.SortModel;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.utils.MemberUtils;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import rx.Observable;


/**
 * 人员
 */
public class SelectEmployeeFragment extends FragmentPresenter<SelectEmployeeDelegate, CommonModel> implements View.OnClickListener {

    private List<SortModel> mAllContactsList;
    //全部人员数据
    List<Member> memberList = new ArrayList<>();

    //根据拼音来排列ListView里面的数据类
    private PinyinComparator2 pinyinComparator;
    private SelectMemberActivity mActivity;
    private SelectEmployeeAdapter mAdapter;

    @Override
    protected void init() {
        mActivity = (SelectMemberActivity) getActivity();
        initAdapter();
        loadContacts();
    }


    private void initAdapter() {
        // 给ListView设置adapter
        mAllContactsList = new ArrayList<>();
        pinyinComparator = PinyinComparator2.getInstance();
        Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
        mAdapter = new SelectEmployeeAdapter(mAllContactsList);
        viewDelegate.setAdapter(mAdapter);
    }

    /**
     * 加载联系人
     */
    private void loadContacts() {
        model.getEmployee((RxAppCompatActivity) getContext(), null, new ProgressSubscriber<EmployeeResultBean>(getContext()) {
            @Override
            public void onNext(EmployeeResultBean employeeResultBean) {
                super.onNext(employeeResultBean);
                memberList = employeeResultBean.getData();
                filterData();
                mActivity.setEmployee(memberList);
                sortContacts();
            }
        });
    }

    /**
     * 过滤数据
     */
    private void filterData() {
        for (Member dataBean : memberList) {
            for (Member member : mActivity.mSpecialMembers) {
                if (member.getType() == 1 && member.getId() == dataBean.getId()) {
                    dataBean.setCheck(member.isCheck());
                    dataBean.setSelectState(member.getSelectState());
                }
            }
        }
        //去掉需要隐藏的条目
        Iterator<Member> iterator = memberList.iterator();
        while (iterator.hasNext()) {
            if (MemberUtils.checkState(iterator.next().getSelectState(), C.HIDE_TO_SELECT)) {
                iterator.remove();
            }
        }
    }

    /**
     * 联系人排序
     */
    private void sortContacts() {
        Collections.sort(memberList, (o1, o2) ->
                ParseUtil.getSortLetterBySortKey(o1.getEmployee_name()).compareTo(ParseUtil.getSortLetterBySortKey(o2.getEmployee_name())));
        HashSet<String> letterSet = new HashSet<>();

        for (Member dataBean : memberList) {
            String employee_name = dataBean.getEmployee_name();
            String phone = dataBean.getPhone();

            SortModel sortModel = new SortModel(employee_name, phone, employee_name);
            sortModel.role = dataBean.getPost_name();
            sortModel.photo = dataBean.getPicture();
            sortModel.member = dataBean;
            String sortLetters = ParseUtil.getSortLetterBySortKey(employee_name);
            if (sortLetters == null) {
                sortLetters = ParseUtil.getSortLetter(employee_name);
            }

            letterSet.add(sortLetters);
            sortModel.sortLetters = sortLetters;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                sortModel.sortToken = ParseUtil.parseSortKey(employee_name);
            } else {
                sortModel.sortToken = ParseUtil.parseSortKeyLollipop(employee_name);
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
        viewDelegate.setOnClickListener(this, R.id.company_member_contacts_rl, R.id.search_edit_text, R.id.choose_group);

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
                    }
                    adapter.notifyDataSetChanged();
                    mActivity.setEmployee(memberList);
                }
            }
        });
    }

    /**
     * 刷新数据
     */
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        Bundle bundle = new Bundle();
        if (i == R.id.company_member_contacts_rl) {
            mActivity.showCompanyOrganization();
        } else if (i == R.id.search_edit_text) {
            bundle.putInt(C.CHECK_TYPE_TAG, mActivity.checkType);
            CommonUtil.startActivtiyForResult(getActivity(), SearchMemberActivity.class, Constants.REQUEST_CODE2, bundle);
        } else if (i == R.id.choose_group) {
            bundle.putString(Constants.DATA_TAG1, MsgConstant.CHOOSE_GROUP_CHAT);
            UIRouter.getInstance().openUri(getActivity(), "DDComp://app/choose_group", bundle, Constants.REQUEST_CODE1);

        }
    }

    /**
     * 全选
     *
     * @param isChecked
     */
    public void setAllSelect(boolean isChecked) {
        Observable.from(memberList)
                .filter(member -> MemberUtils.checkState(member.getSelectState(), C.FREE_TO_SELECT))
                .subscribe(member -> member.setCheck(isChecked));

        mAdapter.notifyDataSetChanged();
        mActivity.setEmployee(memberList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE1) {
            getActivity().finish();
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE2) {
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}