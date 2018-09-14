package com.hjhq.teamface.custom.ui.funcation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.ui.detail.DataDetailModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 转移负责人
 *
 * @author Administrator
 * @date 2017/12/20
 */

public class TransferPrincipalPresenter extends ActivityPresenter<TransferPrincipalDelegate, DataDetailModel> implements View.OnClickListener {
    public static final String[] SELECT_VALUE = {"是", "否"};
    private int selectId = 1;

    @Override
    public void init() {

    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.ll_select);
        viewDelegate.setOnAddMemberClickedListener(() -> {
            //公司员工
            Bundle bundle = new Bundle();
            List<Member> members = viewDelegate.getMembers();
            for (Member member : members) {
                member.setCheck(true);
            }
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, (Serializable) members);
            bundle.putInt(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
            CommonUtil.startActivtiyForResult(TransferPrincipalPresenter.this, SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);
        });
    }

    @Override
    public void onClick(View v) {
        PopUtils.showBottomMenu(this, viewDelegate.getRootView(), "请选择", SELECT_VALUE, position -> {
            viewDelegate.setSelectValue(SELECT_VALUE[position]);
            this.selectId = Math.abs(position - 1);
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<Member> members = viewDelegate.getMembers();
        if (CollectionUtils.isEmpty(members)) {
            ToastUtils.showError(mContext,"请选择负责人");
        } else {
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA_TAG1, selectId);
            intent.putExtra(Constants.DATA_TAG2, members.get(0));
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK) {//协助人
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            viewDelegate.setMembers(members);
        }
    }
}
