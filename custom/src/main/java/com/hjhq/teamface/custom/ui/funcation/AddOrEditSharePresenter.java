package com.hjhq.teamface.custom.ui.funcation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.custom.bean.AddOrEditShareRequestBean;
import com.hjhq.teamface.custom.bean.ShareResultBean;
import com.hjhq.teamface.custom.ui.detail.DataDetailModel;

import java.util.ArrayList;

/**
 * 新增或编辑共享
 *
 * @author Administrator
 * @date 2017/12/20
 */

public class AddOrEditSharePresenter extends ActivityPresenter<ShareDelegate, DataDetailModel> {
    private int state = CustomConstants.ADD_STATE;
    private String objectId;
    private String moduleBean;
    private ShareResultBean.DataBean dataBean;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            objectId = intent.getStringExtra(Constants.MODULE_ID);
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            state = intent.getIntExtra(Constants.DATA_TAG1, CustomConstants.ADD_STATE);
            if (state == CustomConstants.EDIT_STATE) {
                dataBean = (ShareResultBean.DataBean) intent.getSerializableExtra(Constants.DATA_TAG2);
            }
        }
    }

    @Override
    public void init() {
        viewDelegate.setAddOrEditState();
        if (state == CustomConstants.EDIT_STATE) {
            viewDelegate.setEditState(dataBean);
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnAddMemberClickedListener(() -> {
            Bundle bundle = new Bundle();
            ArrayList<Member> members = (ArrayList<Member>) viewDelegate.getMembers();
            for (Member member : members) {
                member.setCheck(true);
            }
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
            bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
            CommonUtil.startActivtiyForResult(this, SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AddOrEditShareRequestBean.BasicsBean addOrEditData = viewDelegate.getAddOrEditData();
        if (addOrEditData == null) {
            ToastUtils.showError(mContext,"请选择成员");
            return super.onOptionsItemSelected(item);
        }
        AddOrEditShareRequestBean bean = new AddOrEditShareRequestBean();
        bean.setDataId(objectId);
        bean.setBean_name(moduleBean);
        bean.setBasics(addOrEditData);
        if (state == CustomConstants.ADD_STATE) {
            model.saveShare(this, bean, new ProgressSubscriber<BaseBean>(this) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(mContext,"保存成功！");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else if (state == CustomConstants.EDIT_STATE) {
            bean.setId(dataBean.getId() + "");
            model.editShare(this, bean, new ProgressSubscriber<BaseBean>(this) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(mContext,"修改成功！");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK) {
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            viewDelegate.setMembers(members);
        }
    }

}
