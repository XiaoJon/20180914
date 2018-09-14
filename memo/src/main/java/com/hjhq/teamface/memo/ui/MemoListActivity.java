package com.hjhq.teamface.memo.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MemoListItemBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.memo.MemoModel;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.adapter.MemoListAdapter;
import com.hjhq.teamface.memo.view.MemoListDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@RouteNode(path = "/main", desc = "备忘录列表")
public class MemoListActivity extends ActivityPresenter<MemoListDelegate, MemoModel> {
    private FrameLayout flContainer;
    private ImageView mFloatingActionButton;
    private FragmentManager mFragmentManager;
    private List<MemoListItemBean> checkedList = new ArrayList<>();
    private String[] mainMenu;
    private String[] titleMenu;
    private int currentIndex = MemoConstant.TYPE_ALL;


    private List<MemoListFragment> fragmentList = new ArrayList<>();

    @Override
    public void init() {
        initFragment();
        // initActionListener();
        setListener();
        mFloatingActionButton = viewDelegate.get(R.id.fab);
        mainMenu = getResources().getStringArray(R.array.memo_main_menu_array);
        titleMenu = getResources().getStringArray(R.array.memo_main_title_array);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                switchStyle();
                break;
            case 1:
                showSwitchMenu();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindEvenListener() {
        mFloatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AddMemoActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE1);
        });

        super.bindEvenListener();
    }

    /**
     * 切换列表类型
     */
    private void switchStyle() {
        fragmentList.get(currentIndex).switchLayoutmanager();
    }

    private void showSwitchMenu() {
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), "请选择", mainMenu, currentIndex, 1, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                if (currentIndex == p) {
                    return true;
                }
                switchFragment(p);
                viewDelegate.setTitle(titleMenu[p]);
                currentIndex = p;
                return true;
            }
        });

    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        fragmentList.add(MemoListFragment.newInstance(0));
        fragmentList.add(MemoListFragment.newInstance(1));
        fragmentList.add(MemoListFragment.newInstance(2));
        fragmentList.add(MemoListFragment.newInstance(3));
        /*for (int i = 0; i < fragmentList.size(); i++) {
            fragmentList.get(i).setActivity(this);
        }*/
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.fl, fragmentList.get(0));
        mTransaction.commitNow();

    }


    /**
     * 切换数据
     *
     * @param index
     */
    private void switchFragment(int index) {
        currentIndex = index;
        FragmentTransaction tr = mFragmentManager.beginTransaction();
        tr.replace(R.id.fl, fragmentList.get(index));
        tr.commitNow();
        fragmentList.get(currentIndex).refreshIfEmpty();
    }

    /**
     * 添加监听
     */
    private void setListener() {
        viewDelegate.get(R.id.search_rl).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SearchMemoActivity.class);
            intent.putExtra(Constants.DATA_TAG1, currentIndex + "");
            intent.putExtra(Constants.DATA_TAG2, SearchMemoActivity.VIEW);
            startActivity(intent);
        });
        /*tvMenu.setOnClickListener(v -> {
            if (currentIndex == 0) {
                return;
            }
            if (getListAdapter(currentIndex).getData() == null || getListAdapter(currentIndex).getData().size() <= 0) {
                return;
            }
            getListAdapter(currentIndex).notifyDataSetChanged();

            if (tvMenu.isSelected()) {

                TextUtil.setText(tvMenu, "编辑");
                tvMenu.setSelected(false);

                getListAdapter(currentIndex).setEditState(false);
                isEditState = false;
                fragmentList.get(currentIndex).setEditState(isEditState);
                flContainer.removeAllViews();
                flContainer.addView(viewForAll);
                checkedList.clear();
            } else {
                TextUtil.setText(tvMenu, "取消");
                getListAdapter(currentIndex).setEditState(true);
                tvMenu.setSelected(true);
                isEditState = true;
                fragmentList.get(currentIndex).setEditState(isEditState);
                setActionBar();
            }
        });*/
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(MessageBean bean) {
        switch (bean.getCode()) {
            case MemoConstant.DELETE_ITEM:
                memoOperation(MemoConstant.MEMO_OPERATION_DELETE, bean.getTag());
                break;
            case MemoConstant.QUIT_SHARE:
                memoOperation(MemoConstant.MEMO_OPERATION_QUIT, bean.getTag());
                break;
            case MemoConstant.DELETE_FOREVER:

                memoOperation(MemoConstant.MEMO_OPERATION_DELETE_FOREVER, bean.getTag());

                break;
            case MemoConstant.RECOVER_MEMO:
                memoOperation(MemoConstant.MEMO_OPERATION_RECOVER, bean.getTag());
                break;
            case MemoConstant.DATALIST_CHANGE:
                refreshData();
                break;
            default:
                break;
        }

    }

    private MemoListAdapter getAdapter(int index) {

        return fragmentList.get(index).getListAdapter();

    }

    private void initActionListener() {


        //删除全部
        doAction("您确认删除?", MemoConstant.MEMO_OPERATION_DELETE);

        //退出共享
       /* viewForShare.findViewById(R.id.tv_delete_forever).setOnClickListener(v -> {
            doAction("您确认退出共享?", MemoConstant.MEMO_OPERATION_QUIT);
        });*/
        //彻底删除
        DialogUtils.getInstance().sureOrCancel(mContext, "", "您确认彻底删除?", viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
            @Override
            public void clickSure() {
                doAction("您确认彻底删除?", MemoConstant.MEMO_OPERATION_DELETE_FOREVER);
            }
        });

        //恢复备忘
        doAction("您确认恢复?", MemoConstant.MEMO_OPERATION_RECOVER);

    }

    private void doAction(String tip, String type) {
        if (checkedList == null || checkedList.size() <= 0) {
            ToastUtils.showToast(mContext, "未选择备忘录");
            return;
        }
        dataOperation(type);
    }

    private void dataOperation(String index) {
        StringBuilder ids = new StringBuilder();
        for (int i1 = 0; i1 < checkedList.size(); i1++) {
            if (i1 == checkedList.size() - 1) {
                ids.append(checkedList.get(i1).getId());
            } else {
                ids.append(checkedList.get(i1).getId() + ",");
            }
        }
        if (TextUtils.isEmpty(ids.toString())) {
            ToastUtils.showToast(mContext, "未选择备忘录");
            return;
        }
        memoOperation(index, ids.toString());


    }

    /**
     * 1:删除  2：彻底删除 3：恢复备忘  4：退出共享
     *
     * @param index
     * @param ids
     */
    private void memoOperation(String index, String ids) {

        model.memoOperation(MemoListActivity.this, index, ids, new ProgressSubscriber<BaseBean>(MemoListActivity.this) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                refreshData();
                //ToastUtils.showSuccess(mContext, "操作成功");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void refreshIfEmpty() {
        fragmentList.get(currentIndex).refreshIfEmpty();
    }

    private void refreshData() {
        fragmentList.get(currentIndex).refreshData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case Constants.REQUEST_CODE1:
                    refreshData();
                    break;
                case Constants.REQUEST_CODE2:
                    refreshData();

                    break;
                default:

                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
