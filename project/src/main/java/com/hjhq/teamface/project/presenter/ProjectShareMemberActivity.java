package com.hjhq.teamface.project.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.adapter.ProjectShareMemberAdapter;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.ProjectMemberDelegate;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目成员管理
 * Created by Administrator on 2018/4/10.
 */
@RouteNode(path = "/project_share_member", desc = "项目分享成员列表()管理")
public class ProjectShareMemberActivity extends ActivityPresenter<ProjectMemberDelegate, ProjectModel> {
    private static final String[] MEMU = {"项目角色", "移除成员"};
    private ProjectShareMemberAdapter mAdapter;
    private ArrayList<Member> mList = new ArrayList<>();

    @Override
    public void init() {
        viewDelegate.showMenu();
        mAdapter = new ProjectShareMemberAdapter(mList);
        viewDelegate.setAdapter(mAdapter);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String title = b.getString(Constants.DATA_TAG2);
            viewDelegate.setTitle(title);
            ArrayList<Member> list = (ArrayList<Member>) b.getSerializable(Constants.DATA_TAG1);
            if (list != null) {
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }

        }

    }


    @Override
    protected void bindEvenListener() {
        viewDelegate.setItemClick(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, mAdapter.getData().get(position).getId() + "");
                UIRouter.getInstance().openUri(mContext, "DDComp://app/employee/info", bundle);
            }
        });
        super.bindEvenListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
        CommonUtil.startActivtiyForResult(mContext,
                SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE1) {
            List<Member> memberList = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            ToastUtils.showToast(mContext, "人数：" + memberList.size());
        } else if (requestCode == Constants.REQUEST_CODE2) {
            int intExtra = data.getIntExtra(Constants.DATA_TAG1, 0);
            ToastUtils.showToast(mContext, "项目角色：" + (intExtra == 0 ? "项目成员" : "访客"));
        }
    }
}
