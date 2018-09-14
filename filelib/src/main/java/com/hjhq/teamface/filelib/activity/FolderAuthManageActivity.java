package com.hjhq.teamface.filelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.FolderAuthMemberAdapter;
import com.hjhq.teamface.filelib.bean.FolderAuthDetailBean;
import com.hjhq.teamface.filelib.view.FolderAuthManageDelegate;

import java.util.ArrayList;
import java.util.List;

public class FolderAuthManageActivity extends ActivityPresenter<FolderAuthManageDelegate, FilelibModel> implements View.OnClickListener {
    private RelativeLayout addAdmin;
    private RelativeLayout addMember;
    private RelativeLayout rlMemberNum;
    private TextView tvAdmin;
    private TextView tvMember;
    private TextView authState;
    private RecyclerView rvAdmin;
    private RecyclerView rvMember;
    private FolderAuthMemberAdapter mAdminAdapter;
    private FolderAuthMemberAdapter mMemberAdapter;
    private ArrayList<FolderAuthDetailBean.SettingBean> adminList = new ArrayList<>();
    private ArrayList<FolderAuthDetailBean.SettingBean> memberList = new ArrayList<>();

    private int folderStyle;
    private String folderId;
    private int folderType;
    private int folderLevel;
    Bundle mBundle;

    @Override
    public void init() {
        initView();
        initData();
    }


    protected void initView() {
        viewDelegate.setTitle("文件夹名字");
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            folderId = mBundle.getString(FileConstants.FOLDER_ID);
            folderStyle = mBundle.getInt(FileConstants.FOLDER_STYLE);
            folderType = mBundle.getInt(FileConstants.FOLDER_TYPE);
            folderLevel = mBundle.getInt(FileConstants.FOLDER_LEVEL);
        }

        addAdmin = (RelativeLayout) findViewById(R.id.rl3);
        addMember = (RelativeLayout) findViewById(R.id.rl7);
        rlMemberNum = (RelativeLayout) findViewById(R.id.rl6);
        tvAdmin = (TextView) findViewById(R.id.text22);
        tvMember = (TextView) findViewById(R.id.text6);
        authState = (TextView) findViewById(R.id.tv_open_state);
        rvAdmin = (RecyclerView) findViewById(R.id.rv_admin);
        rvMember = (RecyclerView) findViewById(R.id.rv_member);

        if (folderType == 0) {
            authState.setText(getString(R.string.filelib_type_public));
            if (folderLevel == 1) {
                addMember.setVisibility(View.GONE);
                rvMember.setVisibility(View.VISIBLE);
                rlMemberNum.setVisibility(View.VISIBLE);
            } else {
                addMember.setVisibility(View.GONE);
                rvMember.setVisibility(View.GONE);
                rlMemberNum.setVisibility(View.GONE);
            }

        } else if (folderType == 1) {
            authState.setText(getString(R.string.filelib_type_personal));
            if (folderLevel == 1) {
                addMember.setVisibility(View.VISIBLE);
                rvMember.setVisibility(View.VISIBLE);
                rlMemberNum.setVisibility(View.VISIBLE);
            } else {
                addMember.setVisibility(View.GONE);
                rvMember.setVisibility(View.GONE);
                rlMemberNum.setVisibility(View.GONE);

            }

        }
        rvAdmin.setLayoutManager(new LinearLayoutManager(FolderAuthManageActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvMember.setLayoutManager(new LinearLayoutManager(FolderAuthManageActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mAdminAdapter = new FolderAuthMemberAdapter(this, folderType, FolderAuthMemberAdapter.ADMIN_TAG, adminList);
        mMemberAdapter = new FolderAuthMemberAdapter(this, folderType, FolderAuthMemberAdapter.MEMBER_TAG, memberList);
        rvAdmin.setAdapter(mAdminAdapter);
        rvMember.setAdapter(mMemberAdapter);


    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        addAdmin.setOnClickListener(this);
        addMember.setOnClickListener(this);
        rvAdmin.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        rvMember.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, memberList.get(position).getId());
                bundle.putString(FileConstants.FOLDER_ID, folderId);
                bundle.putString(FileConstants.AUTH_DOWNLOAD, memberList.get(position).getDownload());
                bundle.putString(FileConstants.AUTH_UPLOAD, memberList.get(position).getUpload());
                CommonUtil.startActivtiyForResult(FolderAuthManageActivity.this, SetMemberFolderAuthActivity.class, Constants.REQUEST_CODE3, bundle);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO: 2017/12/28 移除成员
                ToastUtils.showToast(mContext, "移除成员");
            }
        });
    }

    protected void initData() {
        getNetData();
    }

    private void getNetData() {
        if (TextUtils.isEmpty(folderId)) {
            ToastUtils.showToast(mContext, "文件夹id为空");
            return;
        }
        model.queryFolderInitDetail(FolderAuthManageActivity.this,
                folderStyle, folderId, new ProgressSubscriber<FolderAuthDetailBean>(FolderAuthManageActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);


                    }

                    @Override
                    public void onNext(FolderAuthDetailBean folderAuthDetailBean) {
                        super.onNext(folderAuthDetailBean);

                        adminList.clear();
                        memberList.clear();
                        List<FolderAuthDetailBean.SettingBean> list1 = folderAuthDetailBean.getData().getManage();
                        List<FolderAuthDetailBean.SettingBean> list2 = folderAuthDetailBean.getData().getSetting();
                        if (list1 != null && list1.size() > 0) {
                            adminList.addAll(list1);
                        }
                        if (list2 != null && list2.size() > 0) {
                            memberList.addAll(list2);
                        }
                        mAdminAdapter.notifyDataSetChanged();
                        mMemberAdapter.notifyDataSetChanged();
                        tvAdmin.setText(String.format(getString(R.string.filelib_folder_admin_num), adminList.size()));
                        tvMember.setText(String.format(getString(R.string.filelib_folder_member_num), memberList.size()));
                        //文件夹名字
                        viewDelegate.setTitle(folderAuthDetailBean.getData().getBasics().getName() + "");

                    }
                });

    }

    int menuPosition = 0;

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        ArrayList<Member> members = new ArrayList<>();
        if (view.getId() == R.id.rl3) {
            for (int i = 0; i < adminList.size(); i++) {
                Member m = new Member();
                try {
                    m.setId(Long.parseLong(adminList.get(i).getEmployee_id()));
                } catch (NumberFormatException e) {

                }
                m.setCheck(true);
                m.setSelectState(C.CAN_NOT_SELECT | C.NOT_FOR_SELECT);
                m.setName(adminList.get(i).getEmployee_name());
                m.setEmployee_name(adminList.get(i).getEmployee_name());
                members.add(m);
            }
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
            bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
            bundle.putString(C.CHOOSE_RANGE_TAG, "0");

            CommonUtil.startActivtiyForResult(FolderAuthManageActivity.this,
                    SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);

        } else if (view.getId() == R.id.rl7) {
            for (int i = 0; i < memberList.size(); i++) {
                Member m = new Member();
                try {
                    m.setId(Long.parseLong(memberList.get(i).getEmployee_id()));
                } catch (NumberFormatException e) {

                }
                m.setCheck(true);
                m.setSelectState(C.CAN_NOT_SELECT | C.NOT_FOR_SELECT);
                m.setName(memberList.get(i).getEmployee_name());
                m.setEmployee_name(memberList.get(i).getEmployee_name());
                members.add(m);
            }
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
            bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);

            CommonUtil.startActivtiyForResult(FolderAuthManageActivity.this,
                    SelectMemberActivity.class, Constants.REQUEST_CODE2, bundle);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //添加管理员
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == Activity.RESULT_OK) {
            ArrayList<Member> list = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (list != null && list.size() > 0) {
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        sb.append(list.get(i).getId() + "");
                    } else {
                        sb.append(list.get(i).getId() + ",");
                    }
                }
                String ids = sb.toString();
                if (!TextUtils.isEmpty(ids)) {
                    addAdmin(ids);
                }

            }

        }
        //添加成员
        if (requestCode == Constants.REQUEST_CODE2 && resultCode == Activity.RESULT_OK) {
            ArrayList<Member> list = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (list != null && list.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        sb.append(list.get(i).getId() + "");
                    } else {
                        sb.append(list.get(i).getId() + ",");
                    }
                }
                String ids = sb.toString();
                if (!TextUtils.isEmpty(ids)) {
                    addMember(ids);
                }

            }

        }
        //设置成员权限
        if (requestCode == Constants.REQUEST_CODE3 && resultCode == Activity.RESULT_OK) {
            getNetData();


        }


    }

    /**
     * 添加管理员
     *
     * @param s
     */
    private void addAdmin(String s) {
        String level = folderLevel == 1 ? "1" : "";
        model.savaManageStaff(FolderAuthManageActivity.this, folderId, s, level, new ProgressSubscriber<BaseBean>(this, false) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);

                ToastUtils.showError(mContext, "设置失败!");
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                if (baseBean.getResponse().getCode() == 1001) {
                    getNetData();
                }

            }
        });

    }

    /**
     * 添加成员
     *
     * @param s
     */
    private void addMember(String s) {
        model.savaMember(FolderAuthManageActivity.this, folderId, s, new ProgressSubscriber<BaseBean>(this, false) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                getNetData();
            }
        });

    }

    /**
     * 删除管理员
     *
     * @param adapterPosition
     */
    public void delMamager(int adapterPosition) {
        String level = folderLevel == 1 ? "1" : "";
        model.delManageStaff(this, folderId, adminList.get(adapterPosition).getEmployee_id(), level, new ProgressSubscriber<BaseBean>(this, false) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                //ToastUtils.showError(mContext, "删除管理员失败!");
                getNetData();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                //ToastUtils.showSuccess(mContext, "删除管理员成功!");
                getNetData();
            }
        });

    }

    /**
     * 删除成员
     *
     * @param adapterPosition
     */
    public void delMember(int adapterPosition) {

        model.delMember(this, folderId, memberList.get(adapterPosition).getEmployee_id(), new ProgressSubscriber<BaseBean>(this, false) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                // ToastUtils.showError(mContext, "删除成员失败!");
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                //ToastUtils.showSuccess(mContext, "删除成员成功!");
                getNetData();
            }
        });
    }
}
