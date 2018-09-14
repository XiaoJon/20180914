package com.hjhq.teamface.filelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ClickUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.member.AddMemberView;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.bean.AddFolderReqBean;
import com.hjhq.teamface.filelib.bean.FolderAuthDetailBean;
import com.hjhq.teamface.filelib.view.AddFolderDelegate;

import java.util.ArrayList;
import java.util.List;

public class AddFolderActivity extends ActivityPresenter<AddFolderDelegate, FilelibModel> implements View.OnClickListener {
    //添加公司文件夹
    public static final int TAG1 = 1;
    //添加个人文件夹
    public static final int TAG2 = 2;
    //管理文件夹
    public static final int TAG3 = 3;
    public static final int TAG4 = 4;
    public static final int TAG5 = 5;
    private int tag;
    private long dirId;
    private EditText mFolderName;
    private RelativeLayout mFolderColor;
    private RelativeLayout mMember;
    private RelativeLayout mManager;
    private RelativeLayout mFolderType;
    private AddMemberView mFolderManager;
    private AddMemberView mFolderMember;
    private ImageView mFolderIcon;
    private TextView mTvType;
    private String folderColor = "#F9B239";
    private String oldFolderName = "";
    private int typePosition = 0;
    private String parentId = "";
    private int folderStyle;
    boolean menuClickable = true;
    private ArrayList<FolderAuthDetailBean.SettingBean> adminList = new ArrayList<>();
    private ArrayList<Member> adminMemberList = new ArrayList<>();
    private ArrayList<Member> newAdminList = new ArrayList<>();

    @Override
    public void init() {
        initView();

    }


    protected void initView() {
        mFolderName = (EditText) findViewById(R.id.et_folder_name);
        mFolderColor = (RelativeLayout) findViewById(R.id.rl2);
        mFolderType = (RelativeLayout) findViewById(R.id.rl3);
        mManager = (RelativeLayout) findViewById(R.id.rl4);
        mMember = (RelativeLayout) findViewById(R.id.rl5);
        mFolderManager = (AddMemberView) findViewById(R.id.mv_manager);
        mFolderMember = (AddMemberView) findViewById(R.id.mv_member);
        mTvType = (TextView) findViewById(R.id.tv_folder_type);
        mFolderIcon = (ImageView) findViewById(R.id.iv_folder_icon);
        //隐藏选择成员
        mMember.setVisibility(View.GONE);
        initData();


    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tag = bundle.getInt(Constants.DATA_TAG1);
            parentId = bundle.getString(Constants.DATA_TAG2, "");
            folderStyle = bundle.getInt(Constants.DATA_TAG3);

            switch (tag) {
                //新增公司文件夹
                case TAG1:
                    viewDelegate.setTitle(R.string.filelib_add_new_folder);
                    mMember.setVisibility(View.GONE);
                    mFolderType.setVisibility(View.VISIBLE);
                    mManager.setVisibility(View.VISIBLE);
                    mFolderIcon.setBackgroundColor(Color.parseColor(folderColor));
                    break;
                //新增个人文件夹
                case TAG2:
                    viewDelegate.setTitle(R.string.filelib_add_new_folder);
                    mMember.setVisibility(View.GONE);
                    mFolderType.setVisibility(View.GONE);
                    mManager.setVisibility(View.GONE);
                    mFolderIcon.setBackgroundColor(Color.parseColor(folderColor));
                    break;
                //管理/修改文件夹
                case TAG3:
                    viewDelegate.setTitle(R.string.filelib_manage_folder);
                    oldFolderName = bundle.getString(Constants.DATA_TAG4);
                    folderColor = bundle.getString(Constants.DATA_TAG5);
                    mMember.setVisibility(View.GONE);
                    mFolderType.setVisibility(View.GONE);
                    mManager.setVisibility(View.GONE);
                    mFolderName.setText(oldFolderName);
                    mFolderIcon.setBackgroundColor(Color.parseColor(folderColor));
                    break;
                case TAG4:
                    //新增公司文件夹子文件夹
                    viewDelegate.setTitle(R.string.filelib_add_new_folder);
                    mMember.setVisibility(View.GONE);
                    mFolderType.setVisibility(View.GONE);
                    mManager.setVisibility(View.VISIBLE);
                    mFolderIcon.setBackgroundColor(Color.parseColor(folderColor));
                    getFolderAdmin();
                    break;
                case TAG5:

                    break;
                default:
                    break;
            }

        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        mFolderColor.setOnClickListener(this);
        mFolderType.setOnClickListener(this);
        mFolderManager.setOnAddMemberClickedListener(new AddMemberView.OnAddMemberClickedListener() {
            @Override
            public void onAddMemberClicked() {
                Bundle bundle = new Bundle();
                ArrayList<Member> members = (ArrayList<Member>) mFolderManager.getMembers();
                for (int i = 0; i < members.size(); i++) {
                    members.get(i).setCheck(true);
                    boolean flag = false;
                    for (int j = 0; j < adminMemberList.size(); j++) {
                        if (members.get(i).getId() == adminMemberList.get(j).getId()) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        members.get(i).setSelectState(C.CAN_NOT_SELECT | C.NOT_FOR_SELECT);
                    } else {
                        members.get(i).setSelectState(C.FREE_TO_SELECT);
                    }
                }
                bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
                bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);

                CommonUtil.startActivtiyForResult(AddFolderActivity.this,
                        SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);
            }
        });
        mFolderMember.setOnAddMemberClickedListener(() -> {
            Bundle bundle = new Bundle();
            ArrayList<Member> members = (ArrayList<Member>) mFolderMember.getMembers();
            for (Member member : members) {
                member.setCheck(true);
            }
            List<Member> mFolderManagerMembers = mFolderManager.getMembers();
            for (int i = 0; i < mFolderManagerMembers.size(); i++) {
                mFolderManagerMembers.get(i).setCheck(false);
                mFolderManagerMembers.get(i).setSelectState(C.CAN_NOT_SELECT);

            }
            //过滤掉管理员
            members.addAll(mFolderManagerMembers);
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
            bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);

            CommonUtil.startActivtiyForResult(AddFolderActivity.this,
                    SelectMemberActivity.class, Constants.REQUEST_CODE2, bundle);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ClickUtil.click(new ClickUtil.ClickListener() {
            @Override
            public void click() {
                switch (tag) {
                    case TAG1:
                        addCompanyFolder();
                        break;
                    case TAG2:
                        addPersonalFolder();
                        break;
                    case TAG3:
                        editFolder();
                        break;
                    case TAG4:
                        addCompanyChildFolder();
                        break;
                    default:
                        break;
                }
            }
        });

        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建公司子文件夹
     */
    private void addCompanyChildFolder() {
        String folderName = mFolderName.getText().toString().trim();
        if (TextUtils.isEmpty(folderName) || folderName.length() > 20) {
            ToastUtils.showToast(AddFolderActivity.this, "文件夹名称不能为空");
            menuClickable = true;
            return;
        }
        AddFolderReqBean bean = new AddFolderReqBean();
        bean.setName(folderName);
        bean.setColor(folderColor);
        bean.setParent_id(parentId);
        bean.setStyle(folderStyle);
        bean.setType("");
        bean.setMember_by("");
        bean.setManage_by("");
        if (mFolderManager.getVisibility() == View.VISIBLE) {
            StringBuilder manageSb = new StringBuilder();

            if (newAdminList.size() > 0) {
                for (int i = 0; i < newAdminList.size(); i++) {
                    if (i == newAdminList.size() - 1) {
                        manageSb.append(newAdminList.get(i).getId() + "");
                    } else {
                        manageSb.append(newAdminList.get(i).getId() + ",");
                    }
                }
            }
            if (adminMemberList.size() <= 0 && newAdminList.size() <= 0) {
                menuClickable = true;
                ToastUtils.showToast(AddFolderActivity.this, "管理员不能为空!");
                return;
            }
            bean.setManage_by(manageSb.toString());
        }

        if (typePosition == 1) {
            StringBuilder memberSb = new StringBuilder();
            List<Member> list2 = mFolderManager.getMembers();
            if (list2.size() > 0) {
                for (int i = 0; i < list2.size(); i++) {
                    if (i == list2.size() - 1) {
                        memberSb.append(list2.get(i).getId() + "");
                    } else {
                        memberSb.append(list2.get(i).getId() + ",");
                    }
                }
            } else {
                menuClickable = true;
                ToastUtils.showToast(AddFolderActivity.this, "成员不能为空!");
                return;
            }
            bean.setMember_by(memberSb.toString());

        }
        model.addFolder(AddFolderActivity.this, bean,
                new ProgressSubscriber<BaseBean>(this, true) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        menuClickable = true;
                    }

                    @Override
                    public void onNext(BaseBean fileListResBean) {
                        super.onNext(fileListResBean);
                        setResult(Activity.RESULT_OK);
                        menuClickable = true;
                        finish();
                    }
                });


    }

    /**
     * 获取父目录管理员列表
     */
    public void getFolderAdmin() {
        model.queryFolderInitDetail(AddFolderActivity.this,
                folderStyle, parentId, new ProgressSubscriber<FolderAuthDetailBean>(AddFolderActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(FolderAuthDetailBean folderAuthDetailBean) {
                        super.onNext(folderAuthDetailBean);

                        adminList.clear();
                        //父目录管理员列表
                        List<FolderAuthDetailBean.SettingBean> list1 = folderAuthDetailBean.getData().getManage();
                        //父目录成员列表
                        List<FolderAuthDetailBean.SettingBean> list2 = folderAuthDetailBean.getData().getSetting();
                        if (list1 != null && list1.size() > 0) {
                            adminList.addAll(list1);
                            adminMemberList.clear();
                            for (int i = 0; i < adminList.size(); i++) {
                                Member m = new Member();
                                FolderAuthDetailBean.SettingBean bean = adminList.get(i);
                                m.setCheck(true);
                                m.setSelectState(C.NOT_FOR_SELECT);
                                m.setName(bean.getEmployee_name());
                                m.setEmployee_name(bean.getEmployee_name());
                                m.setId(TextUtil.parseLong(bean.getEmployee_id()));
                                m.setPicture(bean.getPicture());
                                adminMemberList.add(m);
                            }
                            mFolderManager.setMembers(adminMemberList);
                        }

                    }
                });
    }

    /**
     * 管理文件夹
     */
    private void editFolder() {
        String folderName = mFolderName.getText().toString().trim();
        if (TextUtils.isEmpty(folderName) || folderName.length() > 20) {
            ToastUtils.showToast(AddFolderActivity.this, "文件夹名称不能为空");
            menuClickable = true;
            return;
        }
        model.editFolder(AddFolderActivity.this, parentId, folderName,
                folderColor, new ProgressSubscriber<BaseBean>(AddFolderActivity.this, true) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        menuClickable = true;
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
    }

    /**
     * 创建公司文件夹
     */
    private void addCompanyFolder() {

        String folderName = mFolderName.getText().toString().trim();
        if (TextUtils.isEmpty(folderName) || folderName.length() > 20) {
            ToastUtils.showToast(AddFolderActivity.this, "文件夹名称不能为空");
            menuClickable = true;
            return;
        }
        AddFolderReqBean bean = new AddFolderReqBean();
        bean.setName(folderName);
        bean.setColor(folderColor);
        bean.setParent_id(parentId);
        bean.setStyle(folderStyle);
        bean.setType(typePosition + "");
        bean.setMember_by("");
        bean.setManage_by("");
        if (mFolderManager.getVisibility() == View.VISIBLE) {
            StringBuilder manageSb = new StringBuilder();
            List<Member> list = mFolderManager.getMembers();

            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        manageSb.append(list.get(i).getId() + "");
                    } else {
                        manageSb.append(list.get(i).getId() + ",");
                    }
                }
            } else {
                menuClickable = true;
                ToastUtils.showToast(AddFolderActivity.this, "管理员不能为空!");
                return;
            }
            bean.setManage_by(manageSb.toString());
        }

        if (typePosition == 1) {
            StringBuilder memberSb = new StringBuilder();
            List<Member> list2 = mFolderMember.getMembers();
            if (list2.size() > 0) {
                for (int i = 0; i < list2.size(); i++) {
                    if (i == list2.size() - 1) {
                        memberSb.append(list2.get(i).getId() + "");
                    } else {
                        memberSb.append(list2.get(i).getId() + ",");
                    }
                }
            } else {
                menuClickable = true;
                ToastUtils.showToast(AddFolderActivity.this, "成员不能为空");
                return;
            }
            bean.setMember_by(memberSb.toString());

        }
        model.addFolder(AddFolderActivity.this, bean,
                new ProgressSubscriber<BaseBean>(this, true) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        menuClickable = true;

                    }

                    @Override
                    public void onNext(BaseBean fileListResBean) {
                        super.onNext(fileListResBean);
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });


    }

    /**
     * 创建个人文件
     */
    private void addPersonalFolder() {

        String folderName = mFolderName.getText().toString().trim();
        if (TextUtils.isEmpty(folderName) || folderName.length() > 12) {
            ToastUtils.showToast(AddFolderActivity.this, "文件夹名字不能为空!");
            menuClickable = true;
            return;
        }
        AddFolderReqBean bean = new AddFolderReqBean();
        bean.setName(folderName);
        bean.setColor(folderColor);
        bean.setParent_id(parentId);
        bean.setStyle(folderStyle);
        bean.setType("");
        bean.setMember_by("");
        bean.setManage_by("");
        model.addFolder(AddFolderActivity.this, bean,
                new ProgressSubscriber<BaseBean>(this, true) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        menuClickable = true;
                    }

                    @Override
                    public void onNext(BaseBean fileListResBean) {
                        super.onNext(fileListResBean);
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });


    }

    int menuPosition = 0;

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        if (view.getId() == R.id.rl2) {
            bundle.putString(Constants.DATA_TAG1, folderColor);
            CommonUtil.startActivtiyForResult(AddFolderActivity.this, ChooseColorActivity.class,
                    Constants.CHOOSE_COLOR_REQUESTCODE, bundle);
        } else if (view.getId() == R.id.rl3) {
            String[] menu = {"公开（企业所有成员可见此文件夹）", "私有（只有加入成员可见此文件夹）"};
            PopUtils.showBottomMenu(this, mFolderType.getRootView(), "文件夹类型", menu, menuPosition, 0,
                    new OnMenuSelectedListener() {
                        @Override
                        public boolean onMenuSelected(int position) {
                            menuPosition = position;
                            mTvType.setText(menu[position]);
                            switch (position) {
                                case 0:
                                    typePosition = 0;
                                    mMember.setVisibility(View.GONE);
                                    break;
                                case 1:
                                    typePosition = 1;
                                    mMember.setVisibility(View.VISIBLE);
                                    break;
                                default:

                                    break;


                            }
                            return true;
                        }
                    });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CHOOSE_COLOR_REQUESTCODE && resultCode == Activity.RESULT_OK) {
            folderColor = data.getStringExtra(Constants.COLOR_CHOOSE);
            mFolderIcon.setBackgroundColor(Color.parseColor(folderColor));

        }
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == Activity.RESULT_OK) {
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            newAdminList.clear();
            if (newAdminList != null && members.size() > 0) {
                newAdminList.addAll(members);
                members.addAll(adminMemberList);
                mFolderManager.setMembers(members);
            } else {
                mFolderManager.setMembers(adminMemberList);
            }

        }
        if (requestCode == Constants.REQUEST_CODE2 && resultCode == Activity.RESULT_OK) {
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (members != null && members.size() > 0) {
                mFolderMember.setMembers(members);
            } else {
                mFolderMember.setMembers(new ArrayList<>());
            }

        }

    }
}
