package com.hjhq.teamface.email.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.member.AddMemberView;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectEmployeeActivity;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.email.EmailModel;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.bean.CheckNextApprovalResultBean;
import com.hjhq.teamface.email.view.ChooseApprovalDelegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 新增/编辑/转发/回复邮件
 */
public class ChooseApprovalActivity extends ActivityPresenter<ChooseApprovalDelegate, EmailModel> {
    public static final int REQUEST_SELECT_MEMBER_CODE = 0x211;
    public static final int REQUEST_SELECT_CC_MEMBER_CODE = 0x212;
    private CheckNextApprovalResultBean.CheckNextApproval mProcessData;
    private String mProcessType;
    private String mProcessCcType;
    private AddMemberView mApproval;
    private AddMemberView mCc;
    RelativeLayout rlApproval;
    RelativeLayout rlCc;


    @Override
    public void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mProcessData = (CheckNextApprovalResultBean.CheckNextApproval) bundle.getSerializable(Constants.DATA_TAG1);
            mProcessType = mProcessData.getProcess_type();
            mProcessCcType = mProcessData.getCcTo();
        }
        viewDelegate.setTitle("请选择审批人");
        rlApproval = viewDelegate.get(R.id.rl_approval);
        rlCc = viewDelegate.get(R.id.rl_cc);
        mApproval = viewDelegate.get(R.id.add_approval);
        mCc = viewDelegate.get(R.id.add_cc);

       /* if ("0".equals(mProcessCcType)) {
            rlCc.setVisibility(View.GONE);
        } else if ("1".equals(mProcessCcType)) {
            rlCc.setVisibility(View.VISIBLE);

        } else {
            rlCc.setVisibility(View.GONE);
        }*/
        rlCc.setVisibility(View.GONE);
        rlApproval.setVisibility(View.VISIBLE);
       /* if ("0".equals(mProcessType)) {
            rlApproval.setVisibility(View.GONE);
        } else if ("1".equals(mProcessType)) {
            rlApproval.setVisibility(View.VISIBLE);
        } else {
            rlApproval.setVisibility(View.GONE);
        }*/
        mCc.setOnAddMemberClickedListener(new AddMemberView.OnAddMemberClickedListener() {
            @Override
            public void onAddMemberClicked() {
                Bundle bundle = new Bundle();
                List<Member> list = mCc.getMembers();
                for (int li = 0; li < list.size(); li++) {
                    list.get(li).setSelectState(C.FREE_TO_SELECT);
                    list.get(li).setCheck(true);
                }
                bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, ((ArrayList<Member>) mCc.getMembers()));
                bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
                CommonUtil.startActivtiyForResult(mContext,
                        SelectMemberActivity.class, REQUEST_SELECT_CC_MEMBER_CODE, bundle);
            }
        });
        mApproval.setOnAddMemberClickedListener(new AddMemberView.OnAddMemberClickedListener() {
            @Override
            public void onAddMemberClicked() {
                chooseMember2();
            }
        });
        viewDelegate.setRightMenuTexts(R.color.main_green, "确定");
        viewDelegate.showMenu();


    }

    private void chooseMember() {
        mProcessType = mProcessData.getProcess_type();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NAME, "选择审批人");
        if ("0".equals(mProcessType)) {
            //固定审批
            List<Member> choosePersonnel = mProcessData.getChoosePersonnel();
            if (CollectionUtils.isEmpty(choosePersonnel)) {
                CommonUtil.startActivtiyForResult(mContext, SelectMemberActivity.class, REQUEST_SELECT_MEMBER_CODE, bundle);
            } else {
                //从个给定的人员中选择
                bundle.putSerializable(C.OPTIONAL_MEMBERS_TAG, (Serializable) choosePersonnel);
                CommonUtil.startActivtiyForResult(mContext, SelectEmployeeActivity.class, REQUEST_SELECT_MEMBER_CODE, bundle);
            }
        } else if ("1".equals(mProcessType)) {
            //自由审批
            CommonUtil.startActivtiyForResult(mContext, SelectMemberActivity.class, REQUEST_SELECT_MEMBER_CODE, bundle);
        }
    }

    private void chooseMember2() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NAME, "选择审批人");
        ArrayList<Member> members = new ArrayList<>();
        List<Member> members1 = mApproval.getMembers();
        if (members1 != null && members1.size() > 0) {
            members1.get(0).setCheck(true);
            members.add(members1.get(0));
        }
        bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
        bundle.putInt(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
        CommonUtil.startActivtiyForResult(mContext,
                SelectMemberActivity.class, REQUEST_SELECT_MEMBER_CODE, bundle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mApproval.getMembers().size() < 0) {
            ToastUtils.showToast(mContext, "请选择审批人");
            return true;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, ((ArrayList<Member>) mApproval.getMembers()));
        intent.putExtra(Constants.DATA_TAG2, ((ArrayList<Member>) mCc.getMembers()));
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_MEMBER_CODE:
                    List<Member> aList = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                    if (aList != null && aList.size() > 0) {
                        mApproval.setMembers(aList);
                        viewDelegate.showMenu(0);
                    } else {
                        viewDelegate.showMenu();
                        mApproval.setMembers(new ArrayList<Member>());
                    }
                    break;
                case REQUEST_SELECT_CC_MEMBER_CODE:
                    List<Member> cList = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                    if (cList != null) {
                        mCc.setMembers(cList);
                    } else {
                        mCc.setMembers(new ArrayList<Member>());
                    }

                    break;
                default:

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
