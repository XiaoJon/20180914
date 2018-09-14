package com.hjhq.teamface.attendance.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.AddGroupDelegate;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.member.AddMemberView;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/add_location", desc = "添加地址")
public class AddGroupActivity extends ActivityPresenter<AddGroupDelegate, AttendanceModel> implements View.OnClickListener {

    String id;
    int type;
    String name;
    AddMemberView mustMemberView;
    AddMemberView notMemberView;

    @Override
    public void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Constants.DATA_TAG1);
            type = bundle.getInt(Constants.DATA_TAG2);

        }
        if (AttendanceConstants.TYPE_EDIT == type && !TextUtils.isEmpty(id)) {
            getNetData();
        }
        initView();
    }

    private void initView() {
        mustMemberView = viewDelegate.get(R.id.member_view1);
        notMemberView = viewDelegate.get(R.id.member_view2);
    }


    @Override
    protected void bindEvenListener() {

        super.bindEvenListener();
        mustMemberView.setOnAddMemberClickedListener(new AddMemberView.OnAddMemberClickedListener() {
            @Override
            public void onAddMemberClicked() {
                ArrayList<Member> list = (ArrayList<Member>) mustMemberView.getMembers();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCheck(true);
                    list.get(i).setSelectState(C.FREE_TO_SELECT);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, list);
                bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
                bundle.putString(C.CHOOSE_RANGE_TAG, "0,1");
                CommonUtil.startActivtiyForResult(AddGroupActivity.this, SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);

            }
        });
        notMemberView.setOnAddMemberClickedListener(new AddMemberView.OnAddMemberClickedListener() {
            @Override
            public void onAddMemberClicked() {
                ArrayList<Member> list = (ArrayList<Member>) notMemberView.getMembers();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCheck(true);
                    list.get(i).setSelectState(C.FREE_TO_SELECT);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, list);
                bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
                bundle.putString(C.CHOOSE_RANGE_TAG, "0,1");
                CommonUtil.startActivtiyForResult(AddGroupActivity.this, SelectMemberActivity.class, Constants.REQUEST_CODE2, bundle);
            }
        });

    }

    public void getNetData() {
        model.getAttendanceRulesDetail(mContext, id, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Bundle bundle = new Bundle();
        switch (type) {

            case AttendanceConstants.TYPE_ADD:
                name = viewDelegate.getName();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showToast(mContext, "名字不能为空");
                    return true;
                }
                bundle.putInt(Constants.DATA_TAG1, AttendanceConstants.TYPE_ADD);
                bundle.putString(Constants.DATA_TAG2, id);
                bundle.putSerializable(Constants.DATA_TAG3, ((ArrayList<Member>) mustMemberView.getMembers()));
                bundle.putSerializable(Constants.DATA_TAG4, ((ArrayList<Member>) notMemberView.getMembers()));
                bundle.putString(Constants.DATA_TAG5, name);
                CommonUtil.startActivtiyForResult(mContext, AddRulesActivity.class, Constants.REQUEST_CODE3, bundle);

                break;
            case AttendanceConstants.TYPE_EDIT:

                break;
            default:

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE1:
                //必须考勤人员/部门
                if (data != null) {
                    List<Member> list1 = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                    mustMemberView.setMembers(list1);
                }
                break;
            case Constants.REQUEST_CODE2:
                //非必须考勤人员/部门
                if (data != null) {
                    List<Member> list2 = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                    notMemberView.setMembers(list2);
                }
                break;
            case Constants.REQUEST_CODE3:
                setResult(RESULT_OK);
                finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
