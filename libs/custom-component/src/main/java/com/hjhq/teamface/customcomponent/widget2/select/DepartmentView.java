package com.hjhq.teamface.customcomponent.widget2.select;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.JsonParser;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.TextWatcherUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.ui.member.SelectRangeDepartmentActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.SelectCommonView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 部门组件
 *
 * @author lx
 * @date 2017/6/21
 */
public class DepartmentView extends SelectCommonView implements ActivityPresenter.OnActivityResult {
    /**
     * 选择范围
     */
    private List<Member> chooseRange;
    /**
     * 默认部门
     */
    private List<Member> defaultDepartment;

    /**
     * 是否多选  选择类型：0单选、1多选
     */
    private boolean isMulti;

    /**
     * 已选人员
     */
    private List<Member> mMembers;

    public DepartmentView(CustomBean bean) {
        super(bean);
        CustomBean.FieldBean field = bean.getField();
        if (field != null) {
            defaultDepartment = field.getDefaultDepartment();
            isMulti = "1".equals(field.getChooseType());
            chooseRange = field.getChooseRange();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.custom_select_single_widget_layout;
    }

    @Override
    public void initView() {
        if (CustomConstants.DETAIL_STATE != state && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            tvContent.addTextChangedListener(new TextWatcherUtil.MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    if (TextUtil.isEmpty(s.toString())) {
                        ivRight.setImageResource(R.drawable.custom_icon_department);
                    } else {
                        ivRight.setImageResource(R.drawable.clear_button);
                    }
                }
            });
            ivRight.setOnClickListener(v -> {
                List<Member> members = getMembers();
                if (!CollectionUtils.isEmpty(members)) {
                    members.clear();
                    setMembers(members);
                }
            });
        }
        super.initView();
    }

    @Override
    public void initOption() {
        ivRight.setImageResource(R.drawable.custom_icon_department);
    }

    @Override
    protected void setData(Object value) {
        List<Member> members = new JsonParser<Member>().jsonFromList(value, Member.class);
        setMembers(members);
    }


    private void setMembers(List<Member> members) {
        mMembers = members;
        StringBuilder sb = new StringBuilder();
        Observable.from(members).subscribe(member -> sb.append("、").append(member.getName()));
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        TextUtil.setText(tvContent, sb.toString().trim());
    }

    private List<Member> getMembers() {
        return mMembers == null ? new ArrayList<>() : mMembers;
    }

    @Override
    public void onClick() {
        super.onClick();
        Bundle bundle = new Bundle();
        bundle.putInt(C.CHECK_TYPE_TAG, isMulti ? C.MULTIL_SELECT : C.RADIO_SELECT);
        bundle.putString(C.CHOOSE_RANGE_TAG, "0");
        ((ActivityPresenter) mView.getContext()).setOnActivityResult(code, this);
        List<Member> members = getMembers();

        if (!CollectionUtils.isEmpty(chooseRange)) {
            bundle.putSerializable(C.CHOOSE_RANGE_TAG, (Serializable) chooseRange);
            bundle.putSerializable(C.SELECTED_MEMBER_TAG, (Serializable) members);

            CommonUtil.startActivtiyForResult(mView.getContext(), SelectRangeDepartmentActivity.class, code, bundle);
        } else {
            //公司员工
            for (Member member : members) {
                member.setCheck(true);
                member.setSelectState(C.FREE_TO_SELECT);
            }
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, (Serializable) members);
            bundle.putString(C.CHOOSE_RANGE_TAG, "1");
            bundle.putString(Constants.NAME, "选择部门");
            CommonUtil.startActivtiyForResult(mView.getContext(), SelectMemberActivity.class, code, bundle);
        }
    }

    @Override
    public boolean formatCheck() {
        return true;
    }

    @Override
    public void setDefaultValue() {
        if (CollectionUtils.isEmpty(defaultDepartment)) {
            return;
        }
        ArrayList<Member> members = new ArrayList<>();
        if (defaultDepartment.get(0).getId() == -1) {
            //动态参数的值 替换成自己
            String departmentId = SPHelper.getDepartmentId();
            Member member = new Member(TextUtil.parseLong(departmentId), SPHelper.getDepartmentName(), 0);
            member.setValue("0:" + departmentId);
            members.add(member);
        } else {
            members.addAll(defaultDepartment);
        }
        setMembers(members);
    }

    /**
     * 添加人员
     */
    private void addMember() {
        Bundle bundle = new Bundle();
        bundle.putInt(C.CHECK_TYPE_TAG, isMulti ? C.MULTIL_SELECT : C.RADIO_SELECT);
        bundle.putString(C.CHOOSE_RANGE_TAG, "0");
        ((ActivityPresenter) mView.getContext()).setOnActivityResult(code, this);
        ArrayList<Member> members = (ArrayList<Member>) getMembers();

        if (!CollectionUtils.isEmpty(chooseRange)) {
            bundle.putSerializable(C.CHOOSE_RANGE_TAG, (Serializable) chooseRange);
            bundle.putSerializable(C.SELECTED_MEMBER_TAG, members);

            CommonUtil.startActivtiyForResult(mView.getContext(), SelectRangeDepartmentActivity.class, code, bundle);
        } else {
            //公司员工
            for (Member member : members) {
                member.setCheck(true);
                member.setSelectState(C.FREE_TO_SELECT);
            }
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
            bundle.putString(C.CHOOSE_RANGE_TAG, "1");
            CommonUtil.startActivtiyForResult(mView.getContext(), SelectMemberActivity.class, code, bundle);
        }
    }

    @Override
    public Object getValue() {
        List<Member> members = getMembers();

        if (CollectionUtils.isEmpty(members)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Member member : members) {
            sb.append(member.getId() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == code && resultCode == Activity.RESULT_OK) {
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            setMembers(members);
            if (!checkNull() && isLinkage) {
                linkageData();
            }
        }
    }
}
