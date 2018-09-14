package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.UriUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.oa.login.bean.CompanyStructureBean;
import com.hjhq.teamface.oa.main.adaper.DepartmentAdapter;
import com.hjhq.teamface.oa.main.adaper.EmployeeAdapter;
import com.hjhq.teamface.oa.main.adaper.NaviAdapter;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;
import com.hjhq.teamface.view.recycler.SimpleItemClickListener;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 组织架构
 */
public class CompanyOrganizationActivity extends BaseTitleActivity {


    int flag = -1;
    int folderLevel = 1;
    RecyclerView mRvNavi;
    RecyclerView mRvDepartment;
    RecyclerView mRvEmployee;
    RelativeLayout mRlSearchLayout;
    DepartmentAdapter mDepartmentAdapter;
    EmployeeAdapter mEmployeeAdapter;
    NaviAdapter mNaviAdapter;
    Bundle mBundle;
    String folderName = "";
    String mainDepartmentId;
    int folderStyle;
    CompanyStructureBean mBean;


    private ArrayList<CompanyStructureBean.DataBean> orginData = new ArrayList<>();
    private ArrayList<CompanyStructureBean.DataBean> naviData = new ArrayList<>();
    private ArrayList<CompanyStructureBean.DataBean> departmentData = new ArrayList<>();
    private ArrayList<CompanyStructureBean.UserBean> employeeData = new ArrayList<>();


    @Override
    protected int getChildView() {
        return R.layout.activity_company_organization;
    }


    @Override
    protected void initView() {
        super.initView();
        mBundle = getIntent().getExtras();
        if (mBundle != null) {

            mainDepartmentId = mBundle.getString(Constants.DATA_TAG1);

        }
        setActivityTitle("通讯录");

        mRlSearchLayout = (RelativeLayout) findViewById(R.id.search_rl);

        mRvNavi = (RecyclerView) findViewById(R.id.rv_navi);
        mRvDepartment = (RecyclerView) findViewById(R.id.rv_department);
        mRvEmployee = (RecyclerView) findViewById(R.id.rv_employee);

        LinearLayoutManager naviLm = new LinearLayoutManager(CompanyOrganizationActivity.this);
        naviLm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvNavi.setLayoutManager(naviLm);
        mNaviAdapter = new NaviAdapter(CompanyOrganizationActivity.this, naviData);
        mRvNavi.setAdapter(mNaviAdapter);

        mEmployeeAdapter = new EmployeeAdapter(employeeData);
        mDepartmentAdapter = new DepartmentAdapter(departmentData);
        mRvDepartment.setLayoutManager(new LinearLayoutManager(CompanyOrganizationActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRvEmployee.setLayoutManager(new LinearLayoutManager(CompanyOrganizationActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRvDepartment.setAdapter(mDepartmentAdapter);
        mRvEmployee.setAdapter(mEmployeeAdapter);
        getNetData();

    }

    @Override
    protected void setListener() {
        getToolbar().setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        mRlSearchLayout.setOnClickListener(v -> {
            if (mNaviAdapter == null || mNaviAdapter.getData() == null || mNaviAdapter.getData().size() <= 0) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DATA_TAG1, MsgConstant.VIEW_DETAIL);
            bundle.putInt(Constants.DATA_TAG2, MsgConstant.VIEW_NET_DATA);
            if (mNaviAdapter.getData().size() <= 2) {
                bundle.putString(Constants.DATA_TAG3, "");
            } else {
                bundle.putString(Constants.DATA_TAG3, mNaviAdapter.getData().get(mNaviAdapter.getData().size() - 1).getId());
            }
            bundle.putString(Constants.DATA_TAG4, mNaviAdapter.getData().get(mNaviAdapter.getData().size() - 1).getName());
            com.hjhq.teamface.common.utils.CommonUtil.startActivtiy(mContext, SearchContactActivity.class, bundle);
        });
        mRvDepartment.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (departmentData.size() > 0) {
                    CompanyStructureBean.DataBean bean = departmentData.get(position);
                    naviData.add(bean);
                    departmentData.clear();
                    departmentData.addAll(bean.getChildList());
                    mDepartmentAdapter.notifyDataSetChanged();
                    mNaviAdapter.notifyDataSetChanged();
                    employeeData.clear();
                    employeeData.addAll(bean.getUsers());
                    mEmployeeAdapter.notifyDataSetChanged();

                } else {
                    CommonUtil.showToast("当前部门无人员!");
                }


            }


        });
        mRvEmployee.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, employeeData.get(position).getId());
                CommonUtil.startActivtiy(mContext, EmployeeInfoActivity.class, bundle);
            }
        });
        mRvNavi.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == naviData.size() - 1) {
                    return;
                }


                if (mNaviAdapter.getData().size() == 0 && position == 0) {
                    return;
                }
                CompanyStructureBean.DataBean bean = naviData.get(position);
                List<CompanyStructureBean.DataBean> list = new ArrayList<CompanyStructureBean.DataBean>();
                list.addAll(naviData);
                naviData.clear();
                for (int i = 0; i <= position; i++) {
                    naviData.add(list.get(i));
                }
                mNaviAdapter.notifyDataSetChanged();
                departmentData.clear();
                departmentData.addAll(bean.getChildList());
                mDepartmentAdapter.notifyDataSetChanged();
                employeeData.clear();
                employeeData.addAll(bean.getUsers());
                mEmployeeAdapter.notifyDataSetChanged();


            }
        });

    }


    @Override
    protected void onRightMenuClick(int itemId) {

    }

    @Override
    protected void initData() {
        // getNetData();
    }

    private void getNetData() {
        MainLogic.getInstance().getCompanyEmployee(this, SPHelper.getCompanyId(), new ProgressSubscriber<CompanyStructureBean>(this, false) {
            @Override
            public void onNext(CompanyStructureBean bean) {

                orginData.clear();
                orginData.addAll(bean.getData());
                naviData.clear();
                mNaviAdapter.notifyDataSetChanged();
                employeeData.clear();
                mEmployeeAdapter.notifyDataSetChanged();

                departmentData.clear();
                if (TextUtils.isEmpty(mainDepartmentId)) {
                    CompanyStructureBean.DataBean naviBean = new CompanyStructureBean.DataBean();
                    naviBean.setName(getString(R.string.contacts));
                    naviBean.setChildList(orginData);
                    naviBean.setUsers(new ArrayList<>());
                    naviData.add(naviBean);
                    mNaviAdapter.notifyDataSetChanged();
                    departmentData.addAll(orginData);
                    mDepartmentAdapter.notifyDataSetChanged();
                } else {
                    findDepartment(orginData, mainDepartmentId);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    private void findDepartment(List<CompanyStructureBean.DataBean> orginData, String mainDepartmentId) {
        for (int i = 0; i < orginData.size(); i++) {
            if (mainDepartmentId.equals(orginData.get(i).getId())) {
                departmentData.clear();
                departmentData.addAll(orginData.get(i).getChildList());
                mDepartmentAdapter.notifyDataSetChanged();
                employeeData.clear();
                employeeData.addAll(orginData.get(i).getUsers());
                mEmployeeAdapter.notifyDataSetChanged();
                naviData.clear();
                naviData.add(orginData.get(i));
                mNaviAdapter.notifyDataSetChanged();
                // TODO: 2018/1/27 导航数据
                break;
            } else {
                if (orginData.get(i).getChildList().size() > 0) {
                    findDepartment(orginData.get(i).getChildList(), mainDepartmentId);
                }
            }
        }
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe
    public void jumpFolder(MessageBean bean) {
        if (Constants.MOVE_FILE_JUMP_FOLDER.equals(bean.getTag()) && bean.getCode() < folderLevel) {
            finish();
        }
        if (FileConstants.MOVE_OR_COPY_OK.equals(bean.getTag())) {
            setResult(RESULT_OK);
            finish();
        }
        if (EventConstant.TYPE_ORGANIZATIONAL_AND_PERSONNEL_CHANGES.equals(bean.getTag())) {
            LogUtil.e("公司人员或组织架构变更,正在重新加载数据!");
            mRlSearchLayout.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(CompanyOrganizationActivity.this, "公司人员或组织架构变更,正在重新加载数据!");
                }
            });
            getNetData();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //发送文件
        if (resultCode == RESULT_OK && requestCode == Constants.CHOOSE_LOCAL_FILE) {
            Uri uri = data.getData();
            String path = UriUtil.getPhotoPathFromContentUri(CompanyOrganizationActivity.this, uri);
            File file = new File(path);

            if (file.exists()) {
                //CommonUtil.showToast(file.getAbsolutePath());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (naviData.size() == 0) {
                finish();
                return true;
            } else if (naviData.size() == 1) {
                finish();
                return true;
            } else if (naviData.size() > 1) {
                CompanyStructureBean.ModuleInfoBean bean = naviData.get(naviData.size() - 2);
                int size = naviData.size();
                naviData.remove(size - 1);
                mNaviAdapter.notifyDataSetChanged();
                departmentData.clear();
                departmentData.addAll(bean.getChildList());
                mDepartmentAdapter.notifyDataSetChanged();
                employeeData.clear();
                employeeData.addAll(bean.getUsers());
                mEmployeeAdapter.notifyDataSetChanged();
                return true;


            }
        }

        return super.onKeyDown(keyCode, event);
    }*/


    @Override
    public void onBackPressed() {
        if (naviData.size() == 0) {
            finish();
            return;
        } else if (naviData.size() == 1) {
            finish();
            return;
        } else if (naviData.size() > 1) {
            CompanyStructureBean.DataBean bean = naviData.get(naviData.size() - 2);
            int size = naviData.size();
            naviData.remove(size - 1);
            mNaviAdapter.notifyDataSetChanged();
            departmentData.clear();
            departmentData.addAll(bean.getChildList());
            mDepartmentAdapter.notifyDataSetChanged();
            employeeData.clear();
            employeeData.addAll(bean.getUsers());
            mEmployeeAdapter.notifyDataSetChanged();
            return;


        }
        super.onBackPressed();

    }
}
