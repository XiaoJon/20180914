package com.hjhq.teamface.email.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.listener.NoDoubleClickListener;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ClickUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.email.EmailModel;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.bean.EmailUnreadNumBean;
import com.hjhq.teamface.email.view.EmailDelegate3;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮箱列表
 *
 * @author Administrator
 */

@RouteNode(path = "/email", desc = "邮件列表主界面")
public class EmailActivity3 extends ActivityPresenter<EmailDelegate3, EmailModel> {

    private String boxTag = EmailConstant.INBOX;

    private TextView mTvTitle;
    private ImageView mIvAdd;
    private ImageView mIvMenu;
    private TextView mTvAdd;
    private TextView mTvMore;
    private RelativeLayout mRlTitle;
    private RelativeLayout mRlAdd;
    private RelativeLayout mRlMenu;
    private RelativeLayout mRl0;
    private RelativeLayout mRlBack;
    private LinearLayout mLlContentMain;
    private DrawerLayout mDrawer;
    private RecyclerView mRecyclerView;
    private TextView currentDataTitle;
    private TextView currentDataSubTitle;
    //当前列表序号
    private int currentIndex = 0;

    private String[] mMenuListItem;
    private List<View> viewList = new ArrayList<>();
    private List<EmailListFragment> fragmentList = new ArrayList<>();

    private EmailUnreadNumBean unreadNunBean;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    @Override
    public void init() {
        initView();
        initData();
        initListener();
        getUnreadNum();

    }


    /**
     * 点击事件
     */
    private void initListener() {
        //标记全部已读
        viewDelegate.get(R.id.rv_read).setOnClickListener(v -> {
            /*if (!"0".equals(currentDataSubTitle.getText().toString()) {

            }*/
            if (fragmentList.get(currentIndex).getAdapter().getData().size() > 0) {
                markAllEmailReaded();
            }
        });
        viewDelegate.setMenuItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.rl1) {
                    switchFragment(0, 1);
                } else if (v.getId() == R.id.rl2) {
                    switchFragment(1, 6);
                } else if (v.getId() == R.id.rl3) {
                    switchFragment(2, 2);
                } else if (v.getId() == R.id.rl4) {
                    switchFragment(3, 3);
                } else if (v.getId() == R.id.rl5) {
                    switchFragment(4, 4);
                } else if (v.getId() == R.id.rl6) {
                    switchFragment(5, 5);
                } else if (v.getId() == R.id.rl7) {
                    switchFragment(0, 1);
                }

            }
        });
        mRlAdd.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
               /* ClickUtils.INSTANCE.click(() -> {
                    addNewEmail();
                });*/
                ClickUtil.click(() -> {
                    addNewEmail();
                });
            }
        });

        mIvMenu.setOnClickListener(v -> {
            if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });
        mRlBack.setOnClickListener(v -> {
            finish();
        });
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //设置主布局随菜单滑动而滑动
                int drawerViewWidth = drawerView.getWidth();
                mLlContentMain.setTranslationX(drawerViewWidth * slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mDrawer.setDrawerShadow(R.drawable.email_voice_sending_bg, GravityCompat.START);


    }

    /**
     * 新增邮件
     */
    private void addNewEmail() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG3, EmailConstant.ADD_NEW_EMAIL);
        CommonUtil.startActivtiyForResult(EmailActivity3.this, NewEmailActivity.class, Constants.REQUEST_CODE1, bundle);
    }

    /**
     * 标记邮件已读
     *
     * @param id
     */
    public void markEmailReaded(String id, int position) {
        model.markMailReadOrUnread(mContext, id,
                EmailConstant.EMAIL_READ_TAG, boxTag,
                new ProgressSubscriber<BaseBean>(EmailActivity3.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        getUnreadNum();

                        if (EmailConstant.UNREAD.equals(boxTag)) {
                            // TODO: 2018/4/20
                        }


                    }
                });

    }

    /**
     * 编辑邮件(草稿,需要审批的邮件已撤销/驳回)
     *
     * @param mBean
     */
    public void editEmail(EmailBean mBean) {
        if (mBean == null) {
            return;
        }
        Bundle bundle = new Bundle();
        //标记为草稿
        bundle.putInt(Constants.DATA_TAG3, EmailConstant.EDIT_DRAFT);
        //邮件内容
        bundle.putSerializable(Constants.DATA_TAG5, mBean);
        CommonUtil.startActivtiyForResult(EmailActivity3.this, NewEmailActivity.class, Constants.REQUEST_CODE1, bundle);

    }


    /**
     * 标记所有邮件为已读
     */
    private void markAllEmailReaded() {
        model.markAllRead(EmailActivity3.this, "1",
                new ProgressSubscriber<BaseBean>(EmailActivity3.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        ToastUtils.showSuccess(EmailActivity3.this, "标记失败");
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        ToastUtils.showSuccess(mContext, "标记成功");
                        refreshData();
                        markLocalEmailReaded();
                       /* getAdapter(currentIndex).setNewData(new ArrayList<EmailBean>());
                        getAdapter(currentIndex).notifyDataSetChanged();*/
                    }
                });
    }


    /**
     * 邮件标记为已读
     */
    private void markLocalEmailReaded() {
       /* List<EmailBean> data = getAdapter(currentIndex).getData();
        for (int i = 0; i < data.size(); i++) {
            try {
                data.get(i).setRead_status(EmailConstant.EMAIL_READ_TAG);
            } catch (Exception e) {
                e.printStackTrace();
        }
            }
        getAdapter(currentIndex).notifyDataSetChanged();*/
        refreshData();

    }

    /**
     * 切换列表数据
     *
     * @param index 菜单位置
     * @param tag   邮箱tag
     */
    private void switchMailBox(int index, int tag) {
        /*if (index == 1) {
            viewDelegate.get(R.id.rv_read).setVisibility(View.VISIBLE);
        } else {
            viewDelegate.get(R.id.rv_read).setVisibility(View.GONE);
        }
        if (index == 6) {
            finish();
            return;
        }
        if (mDrawer.isDrawerOpen(mRlMenu)) {
            mDrawer.closeDrawer(mRlMenu);
        }
        if (currentIndex == index) {
            return;
        }
        currentIndex = index;
        currentPageInfo = pageInfoMap.get(currentIndex);
        try {
            state = stateMap.get(currentIndex);
        } catch (Exception e) {
            e.printStackTrace();
            state = REFRESH_STATE;
        }
        currentPageNo = currentPageInfo.getPageNum();
        totalPages = currentPageInfo.getTotalPages();
        boxTag = tag + "";
        mRecyclerView.swapAdapter(getAdapter(currentIndex), true);
        mRecyclerView.requestLayout();
        mRecyclerView.invalidate();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        for (int i1 = 0; i1 < viewList.size(); i1++) {
            if (index == i1) {
                viewList.get(i1).setBackgroundColor(getResources().getColor(R.color.email_menu_item_selected));
            } else {
                viewList.get(i1).setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
        TextUtil.setText(currentDataTitle, mMenuListItem[index]);
        if ("0".equals(getUnreadNum(boxTag))) {
            TextUtil.setText(currentDataSubTitle, "");
        } else {

            TextUtil.setText(currentDataSubTitle, currentPageInfo.getTotalRows() + "");
        }
        if (getAdapter(currentIndex).getData().size() <= 0) {
            getNetData();
        }*/
    }

    private void switchFragment(int index, int tag) {
        if (index == 1 || index == 0) {
            viewDelegate.get(R.id.rv_read).setVisibility(View.VISIBLE);
        } else {
            viewDelegate.get(R.id.rv_read).setVisibility(View.GONE);
        }
        if (index == 6) {
            finish();
            return;
        }
        if (mDrawer.isDrawerOpen(mRlMenu)) {
            mDrawer.closeDrawer(mRlMenu);
        }
        if (currentIndex == index) {
            return;
        }
        currentIndex = index;
        boxTag = tag + "";
        FragmentTransaction tr = mFragmentManager.beginTransaction();
        tr.replace(R.id.fl, fragmentList.get(index));
        tr.commitNow();
        for (int i1 = 0; i1 < viewList.size(); i1++) {
            if (index == i1) {
                viewList.get(i1).setBackgroundColor(getResources().getColor(R.color.email_menu_item_selected));
            } else {
                viewList.get(i1).setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
        TextUtil.setText(currentDataTitle, mMenuListItem[index]);
        if ("0".equals(getUnreadNum(boxTag))) {
            TextUtil.setText(currentDataSubTitle, "");
        } else {
            TextUtil.setText(currentDataSubTitle, "" + "");
        }
        fragmentList.get(currentIndex).refreshData();
       /* if (fragmentList.get(currentIndex).getAdapter().getData().size() < 0) {
            fragmentList.get(currentIndex).refreshData();
        }*/
    }


    /**
     * 邮箱未读数
     *
     * @param boxTag
     * @return
     */
    private String getUnreadNum(String boxTag) {
        if (TextUtils.isEmpty(boxTag)) {
            return " ";
        }
        if (unreadNunBean != null) {
            List<EmailUnreadNumBean.DataBean> data = unreadNunBean.getData();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    if ((boxTag).equals(data.get(i).getMail_box_id())) {
                        return data.get(i).getCount();
                    }
                }
            }
        }
        return "";
    }


    /**
     * 刷新数据
     */
    private void refreshData() {
        fragmentList.get(currentIndex).refreshData();
        getUnreadNum();

    }

    /**
     * 收信
     */
    private void receiveEmail() {
        /*if (boxTag.equals(UNREAD) || boxTag.equals(TRASH) || boxTag.equals(DRAFTBOX)) {
            model.receive(mContext, new ProgressSubscriber<EmailListBean>(mContext) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }

                @Override
                public void onNext(EmailListBean emailListBean) {
                    super.onNext(emailListBean);
                    if (emailListBean.getData().getDataList() != null && emailListBean.getData().getDataList().size() > 0) {
                        refreshData();
                    }
                }
            });
        }*/
    }


    /**
     * 获取侧滑菜单上需要的数据
     */
    private void getMenuInfo() {
        // TODO: 2018/3/2 获取侧滑菜单上需要的数据 ,并分配到菜单上

    }

    /**
     * 初始化Adapter
     */
    protected void initData() {

        fragmentList.add(EmailListFragment.newInstance(EmailConstant.INBOX));
        fragmentList.add(EmailListFragment.newInstance(EmailConstant.UNREAD));
        fragmentList.add(EmailListFragment.newInstance(EmailConstant.SENDED));
        fragmentList.add(EmailListFragment.newInstance(EmailConstant.DRAFTBOX));
        fragmentList.add(EmailListFragment.newInstance(EmailConstant.DELETED));
        fragmentList.add(EmailListFragment.newInstance(EmailConstant.TRASH));
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.fl, fragmentList.get(0));
        mTransaction.commitNow();
        mMenuListItem = getResources().getStringArray(R.array.email_menu_array);
    }

    /**
     * 切换数据
     *
     * @param index
     */
    private void switchFragment(int index) {
        if (mDrawer.isDrawerOpen(mRlMenu)) {
            mDrawer.closeDrawer(mRlMenu, true);
        }
        if (index == currentIndex) {
            return;
        }
        currentIndex = index;
        FragmentTransaction tr = mFragmentManager.beginTransaction();
        tr.replace(R.id.fl, fragmentList.get(index));
        tr.commitNow();
        fragmentList.get(currentIndex).refreshData();

    }

    /**
     * 获取个箱的数据统计
     */
    public void getUnreadNum() {
        model.queryUnreadNumsByBox(EmailActivity3.this, new ProgressSubscriber<EmailUnreadNumBean>(EmailActivity3.this, false) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(EmailUnreadNumBean emailUnreadNumBean) {
                super.onNext(emailUnreadNumBean);
                unreadNunBean = emailUnreadNumBean;
                viewDelegate.showUnreadNum(emailUnreadNumBean);
            }
        });

    }


    private void initView() {
        mRlBack = viewDelegate.get(R.id.rl_back);
        mDrawer = viewDelegate.get(R.id.drawer_layout);
        mRlMenu = viewDelegate.get(R.id.rl_left_main_menu);
        mLlContentMain = viewDelegate.get(R.id.ll_content_main);
        mRlTitle = viewDelegate.get(R.id.rl_toolbar);
        mIvMenu = viewDelegate.get(R.id.toobar_menu);
        mRlAdd = viewDelegate.get(R.id.add);
        mRecyclerView = viewDelegate.get(R.id.rv);
        mRl0 = viewDelegate.get(R.id.rl0);
        currentDataTitle = viewDelegate.get(R.id.tv_title);
        currentDataSubTitle = viewDelegate.get(R.id.sub_title);
        View view1 = viewDelegate.get(R.id.rl1);
        View view2 = viewDelegate.get(R.id.rl2);
        View view3 = viewDelegate.get(R.id.rl3);
        View view4 = viewDelegate.get(R.id.rl4);
        View view5 = viewDelegate.get(R.id.rl5);
        View view6 = viewDelegate.get(R.id.rl6);
        View view7 = viewDelegate.get(R.id.rl7);

        view1.setTag(1);
        view2.setTag(6);
        view3.setTag(2);
        view4.setTag(3);
        view5.setTag(4);
        view6.setTag(5);
        view7.setTag(7);
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        viewList.add(view5);
        viewList.add(view6);
        viewList.add(view7);

        viewList.get(0).setBackgroundColor(getResources().getColor(R.color.email_menu_item_selected));
        //全部标记为已读
        viewDelegate.get(R.id.rv_read).setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE1:
                refreshData();
                break;
            case Constants.REQUEST_CODE2:

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(mRlMenu)) {
            mDrawer.closeDrawer(mRlMenu);
        } else {
            finish();
        }
    }

    public void setTotalNum(int i) {
        TextUtil.setText(currentDataSubTitle, i + "");
    }
}
