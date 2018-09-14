package com.hjhq.teamface.oa.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.EmployeeDetailBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.common.bean.FriendsListBean;
import com.hjhq.teamface.oa.friends.adapter.FriendsAdapter;
import com.hjhq.teamface.oa.friends.logic.FriendsLogic;
import com.hjhq.teamface.oa.main.EmployeeInfoActivity;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;
import com.hjhq.teamface.view.recycler.GridDividerDecoration;
import com.luojilab.router.facade.annotation.RouteNode;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import me.iwf.photopicker.PhotoPicker;

/**
 * 企业圈
 *
 * @author Administrator
 */
@RouteNode(path = "/friend/detail", desc = "同事圈")
public class FriendsActivity extends BaseTitleActivity {
    @Bind(R.id.fragment_move_detail_rv)
    RecyclerView mRecyclerView;

    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private FriendsAdapter adapter;


    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private File imageFromCamera;
    public int state = REFRESH_STATE;
    private List<FriendsListBean.DataBean.ListBean> dataList = new ArrayList<>();

    private String personId = null;
    /**
     * 企业圈背景
     */
    private ImageView friendBg;
    private View headView;
    private TextView tvName;
    private ImageView headImg;
    private String employeeId;

    @Override
    protected void onSetContentViewNext(Bundle savedInstanceState) {
        super.onSetContentViewNext(savedInstanceState);
        if (savedInstanceState == null) {
            personId = getIntent().getStringExtra(Constants.DATA_TAG1);
        } else {
            personId = savedInstanceState.getString(Constants.DATA_TAG1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!TextUtil.isEmpty(personId)) {
            outState.putString(Constants.DATA_TAG1, personId);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getChildView() {
        return R.layout.friends_fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        if (personId == null) {
            //同事圈可以发表
            setRightMenuIcons(R.drawable.actionbar_camera_icon);
            setActivityTitle("同事圈");
        } else {
            setActivityTitle("相册");
        }

        headView = inflater.inflate(
                R.layout.friends_circle_head, null);
        friendBg = headView.findViewById(R.id.iv_mtopimg);
        headImg = headView.findViewById(R.id.siv_img);
        tvName = headView.findViewById(R.id.u_name);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new GridDividerDecoration(mContext, R.color.gray_f2, (int) DeviceUtils.dpToPixel(mContext, 0.3f)));
        adapter = new FriendsAdapter(dataList, this);

        View emptyView = getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        TextView tvTitlt = emptyView.findViewById(R.id.title_tv);
        TextUtil.setText(tvTitlt, "暂无动态");
        adapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(adapter);
        adapter.addHeaderView(headView);
    }

    @Override
    protected void initData() {
        employeeId = personId == null ? SPHelper.getEmployeeId() : personId;
        loadEmployee();
        //触发自动刷新
        mRefreshLayout.autoRefresh();
        getFriendBg();
    }

    /**
     * 加载人员
     */
    private void loadEmployee() {
        UserInfoBean userInfoBean = SPHelper.getUserInfo(UserInfoBean.class);
        try {
            UserInfoBean.DataBean.EmployeeInfoBean employeeInfo = userInfoBean.getData().getEmployeeInfo();

            ImageLoader.loadCircleImage(this, SPHelper.getUserAvatar(), headImg, employeeInfo.getName(), 1, ColorUtils.resToColor(mContext, R.color.white));
            TextUtil.setText(tvName, employeeInfo.getName());
        } catch (NullPointerException e) {
            e.printStackTrace();
            ToastUtils.showError(this, "人员信息异常");
        }
    }

    /**
     * 得到背景
     */
    private void getFriendBg() {
        MainLogic.getInstance().queryEmployeeInfo(this, employeeId, new ProgressSubscriber<EmployeeDetailBean>(this) {
            @Override
            public void onNext(EmployeeDetailBean userInfoBean) {
                super.onNext(userInfoBean);
                ImageLoader.loadImage(mContext, userInfoBean.getData().getEmployeeInfo().getMicroblog_background() + "&width=1080", R.drawable.user_base_info_bg, friendBg);
            }
        });
    }

    /**
     * 得到朋友圈
     */
    public void getFriendList() {
        int pageNum = state == LOAD_STATE ? currentPageNo + 1 : 1;
        FriendsLogic.getInstance().getFriends(this, personId, pageNum,
                Constants.PAGESIZE / 2, new ProgressSubscriber<FriendsListBean>(this, state == NORMAL_STATE) {
                    @Override
                    public void onNext(FriendsListBean bean) {
                        super.onNext(bean);
                        showFriends(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (state == LOAD_STATE) {
                            mRefreshLayout.finishLoadMore(false);
                        } else {
                            mRefreshLayout.finishRefresh(false);
                        }
                    }
                });
    }

    /**
     * 显示企业圈
     *
     * @param bean
     */
    private void showFriends(FriendsListBean bean) {
        totalPages = bean.getData().getTotalPages();
        currentPageNo = bean.getData().getPageNum();

        List<FriendsListBean.DataBean.ListBean> list = bean.getData().getList();
        switch (state) {
            case REFRESH_STATE:
            case NORMAL_STATE:
                dataList.clear();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.setNoMoreData(false);
                break;
            case LOAD_STATE:
                mRefreshLayout.finishLoadMore();
                break;
            default:
                break;
        }
        dataList.addAll(list);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void setListener() {
        headView.setOnClickListener(v -> {
            //弹框更换背景
            PopUtils.showBottomMenu(FriendsActivity.this, getContainer(), "更换背景", new String[]{"拍照", "相册"}, position -> {
                if (position == 0) {
                    SystemFuncUtils.requestPermissions(mContext, android.Manifest.permission.CAMERA, aBoolean -> {
                        if (aBoolean) {
                            imageFromCamera = CommonUtil.getImageFromCamera(mContext, Constants.TAKE_PHOTO_NEW_REQUEST_CODE);
                        } else {
                            ToastUtils.showError(mContext, "必须获得必要的权限才能拍照！");
                        }
                    });
                } else if (position == 1) {
                    CommonUtil.getImageFromAlbumByMultiple(FriendsActivity.this, 1);
                }
                return true;
            });
        });
        headImg.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DATA_TAG1, employeeId);
            CommonUtil.startActivtiy(this, EmployeeInfoActivity.class, bundle);
        });


        mRefreshLayout.setOnRefreshListener(refreshLayout -> refresh());
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (currentPageNo >= totalPages) {
                //将不会再次触发加载更多事件
                mRefreshLayout.finishLoadMoreWithNoMoreData();
                return;
            }
            state = Constants.LOAD_STATE;
            getFriendList();
        });

    }

    @Override
    public void onClick(View view) {

    }

    public View getRootView() {
        return getContainer();
    }

    @Override
    protected void onRightMenuClick(int itemId) {
        super.onRightMenuClick(itemId);
        addFriends();
    }

    /**
     * 新建动态
     */
    private void addFriends() {
        CommonUtil.startActivtiy(this, AddFriendsActivity.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            //获取照片
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                String url = photos.get(0);
                modBackGround(url);
            }

        } else if (requestCode == Constants.TAKE_PHOTO_NEW_REQUEST_CODE) {
            //拍照新建图片
            if (imageFromCamera != null && imageFromCamera.exists()) {
                modBackGround(imageFromCamera.getAbsolutePath());
            }
        }
    }

    /**
     * 修改背景
     */
    private void modBackGround(String filePath) {
        FriendsLogic.getInstance().modBackGround((BaseActivity) mContext, filePath, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                initData();
            }
        });
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(MessageBean event) {
        if (event.getCode() == Constants.FRIENDS_REFRESH) {
            refresh();
        } else if (event.getCode() == Constants.EDIT_USER_INFO) {
            loadEmployee();
        }
    }

    /**
     * 刷新
     */
    private void refresh() {
        currentPageNo = 1;
        state = REFRESH_STATE;
        getFriendList();
    }
}
