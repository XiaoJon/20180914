package com.hjhq.teamface.oa;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.view.treeview.TreeView;
import com.hjhq.teamface.view.treeview.helper.TreeViewHelper;

import butterknife.Bind;

/**
 * 企业成员
 */
public class CompanyMembersActivity extends BaseTitleActivity {


    @Bind(R.id.treeview_container)
    RelativeLayout mContainer;

    @Bind(R.id.tv_company_name)
    TextView mTvCompanyName;
    TreeViewHelper helper;

    @Override
    protected int getChildView() {
        return R.layout.activity_company_members;
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle(getString(R.string.company_members));

    }


    @Override
    protected void initData() {
        TextUtil.setText(mTvCompanyName, SPHelper.getCompanyName());
        getElements();
    }


    private void getElements() {
        helper = new TreeViewHelper();
        TreeViewHelper helper = new TreeViewHelper();
        MainLogic.getInstance().findUsersByCompany(this, SPHelper.getCompanyId(), new ProgressSubscriber<GetDepartmentStructureBean>(this) {
            @Override
            public void onNext(GetDepartmentStructureBean getDepartmentStructureBean) {
                helper.buildEmployeeTree(CompanyMembersActivity.this, mContainer, getDepartmentStructureBean);
                TreeView treeView = helper.getTreeView();
                treeView.setCanSelect(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
    }

}
