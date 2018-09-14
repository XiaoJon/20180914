package com.hjhq.teamface.oa.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.FolderNaviData;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.im.activity.ChooseGroupChatActivity;
import com.hjhq.teamface.im.adapter.RecentContactsAdapter;
import com.hjhq.teamface.oa.CompanyMembersActivity;
import com.hjhq.teamface.oa.login.logic.LoginLogic;
import com.hjhq.teamface.oa.login.logic.SettingHelper;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;
import com.hjhq.teamface.view.recycler.SimpleItemClickListener;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

@RouteNode(path = "/contacts", desc = "通讯录")
public class ContactsActivity extends BaseTitleActivity {
    @Bind(R.id.search_edit_text)
    TextView mSearchEditText;
    @Bind(R.id.search_rl)
    RelativeLayout mSearchRl;
    @Bind(R.id.iv_organization)
    ImageView mIvOrganization;
    @Bind(R.id.tv_organization)
    TextView mTvOrganization;
    @Bind(R.id.ll_organization)
    LinearLayout mLlOrganization;
    @Bind(R.id.iv_department)
    ImageView mIvDepartment;
    @Bind(R.id.tv_department)
    TextView mTvDepartment;
    @Bind(R.id.ll_department)
    LinearLayout mLlDepartment;
    @Bind(R.id.iv_group)
    ImageView mIvGroup;
    @Bind(R.id.tv_group)
    TextView mTvGroup;
    @Bind(R.id.ll_group)
    LinearLayout mLlGroup;
    @Bind(R.id.text22)
    TextView mText22;
    @Bind(R.id.text23)
    TextView mText23;
    @Bind(R.id.rl2)
    RelativeLayout mRl2;
    @Bind(R.id.lv_contacts)
    RecyclerView mLvContacts;
    private List<Member> mRecentContactsList = new ArrayList<>();
    private RecentContactsAdapter mAdapter;

    @Override
    protected int getChildView() {
        return R.layout.fragment_contacts_v2;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle(R.string.contacts);
        /*FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction mTransaction = manager.beginTransaction();
        mTransaction.add(R.id.ll_content, new ContactsFragmentV2());
        mTransaction.commitNow();*/
        try {
            TextUtil.setText(mTvDepartment, LoginLogic.getInstance().getMainDepartmentName());
        } catch (Exception e) {
            mTvDepartment.setText("我当前的部门");
        }
        //常用联系人
        mRecentContactsList = MainLogic.getInstance().getRecentContacts();
        mText23.setVisibility(View.GONE);

        mLvContacts.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mAdapter = new RecentContactsAdapter(mRecentContactsList);
        mLvContacts.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
        mLvContacts.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, mRecentContactsList.get(position).getId() + "");
                CommonUtil.startActivtiy(mContext, EmployeeInfoActivity.class, bundle);
            }
        });

    }

    @OnClick({R.id.search_rl, R.id.ll_organization, R.id.ll_department, R.id.ll_group, R.id.text23})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.search_rl:
                bundle.putInt(Constants.DATA_TAG1, MsgConstant.VIEW_DETAIL);
                bundle.putInt(Constants.DATA_TAG2, MsgConstant.VIEW_NET_DATA);
                CommonUtil.startActivtiy(mContext, SearchContactActivity.class);

                break;
            case R.id.ll_organization:
                bundle.putInt(FileConstants.FOLDER_TYPE, 1);
                bundle.putString(FileConstants.FOLDER_ID, 1 + "");
                bundle.putString(FileConstants.FOLDER_NAME, "联系人");
                bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
                bundle.putString(FileConstants.FILE_ID, 0 + "");
                bundle.putInt(FileConstants.MOVE_OR_COPY, FileConstants.MOVE);
                ArrayList<FolderNaviData> list = new ArrayList<>();
                FolderNaviData data = new FolderNaviData();
                data.setFolderLevel(0);
                data.setFolderName("联系人");
                data.setFloderType(1);
                data.setFolderId("1");
                list.add(data);
                bundle.putSerializable(FileConstants.FOLDER_NAVI_DATA, list);
                CommonUtil.startActivtiy(mContext, CompanyOrganizationActivity.class, bundle);

                break;
            case R.id.ll_department:
                bundle.putString(Constants.DATA_TAG1, LoginLogic.getInstance().getMainDepartmentId());
                CommonUtil.startActivtiy(mContext, CompanyOrganizationActivity.class, bundle);
                break;
            case R.id.ll_group:
                CommonUtil.startActivtiy(mContext, ChooseGroupChatActivity.class);
                break;
            case R.id.text23:
                if (mAdapter.getData().size() <= 0) {
                    CommonUtil.showToast("常用联系人是空的哦!");
                    return;
                }
                DialogUtils.getInstance()
                        .sureOrCancel(mContext, getString(R.string.operation_hint),
                                getString(R.string.file_delete_recent_contact_hint), mText23.getRootView(), () -> MainLogic.getInstance().removeAllRecentContact());

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.company_member_contacts_item_rl:
                CommonUtil.startActivtiy(mContext, CompanyMembersActivity.class);
                break;

            default:
                break;
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe
    public void onMessage(MessageBean bean) {
        if (C.UPDATE_RECENT_CONTACT.equals(bean.getTag())) {
            updateRecentContactData();
        }
        if (MsgConstant.MEMBER_MAYBE_CHANGED_TAG.equals(bean.getTag())) {
            updateRecentContactData();
        }
        if (EventConstant.TYPE_ORGANIZATIONAL_AND_PERSONNEL_CHANGES.equals(bean.getTag())) {
            LoginLogic.getInstance().initUserInfo(ContactsActivity.this, new ProgressSubscriber<UserInfoBean>(mContext, false) {
                @Override
                public void onNext(UserInfoBean userInfoBean) {
                    super.onNext(userInfoBean);
                    //更改登录标记位
                    SPHelper.setLoginBefore(true);
                    try {
                        //保存公司信息
                        UserInfoBean.DataBean.CompanyInfoBean companyInfo = userInfoBean.getData().getCompanyInfo();
                        SPHelper.setCompanyId(companyInfo.getId());
                        SPHelper.setCompanyName(companyInfo.getCompany_name());
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showError(mContext, "初始化公司信息失败!");
                    }

                    try {
                        //保存部门信息
                        List<UserInfoBean.DataBean.DepartmentInfoBean> departmentInfo = userInfoBean.getData().getDepartmentInfo();
                        if (!CollectionUtils.isEmpty(departmentInfo)) {
                            SPHelper.setDepartmentId(departmentInfo.get(0).getId());
                            SPHelper.setDepartmentName(departmentInfo.get(0).getDepartment_name());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showError(mContext, "初始化部门信息失败!");
                    }

                    try {
                        //保存用户信息
                        UserInfoBean.DataBean.EmployeeInfoBean employeeInfo = userInfoBean.getData().getEmployeeInfo();
                        SPHelper.setUserId(employeeInfo.getSign_id());
                        SPHelper.setEmployeeId(employeeInfo.getId());
                        SPHelper.setUserAvatar(employeeInfo.getPicture());
                        SPHelper.setUserName(employeeInfo.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showError(mContext, "初始化个人信息失败!");
                    }
                    SPHelper.setUserInfo(userInfoBean);
                    TextUtil.setText(mTvDepartment, LoginLogic.getInstance().getMainDepartmentName());
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    SPHelper.setLoginBefore(false);
                    SettingHelper.logout(MsgConstant.TYPE_IM_ACCOUNT_ERROR);
                }
            });
        }
    }

    private void updateRecentContactData() {
        mRecentContactsList.clear();
        mRecentContactsList.addAll(MainLogic.getInstance().getRecentContacts());
        //现在不需要清空
        mText23.setVisibility(View.GONE);
        //mText23.setVisibility(mRecentContactsList.size() > 0 ? View.VISIBLE : View.GONE);
        mAdapter.notifyDataSetChanged();
    }
}
