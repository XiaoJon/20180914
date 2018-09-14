package com.hjhq.teamface.custom.ui.detail;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.DetailResultBean;
import com.hjhq.teamface.basis.bean.ModuleFunctionBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.lable.entity.GameLabel;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.comment.CommentFragment;
import com.hjhq.teamface.common.ui.dynamic.DynamicFragment;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.AuthHelper;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.bean.AutoModuleResultBean;
import com.hjhq.teamface.custom.bean.DataRelationResultBean;
import com.hjhq.teamface.custom.bean.SeasPoolResponseBean;
import com.hjhq.teamface.custom.bean.TransformationRequestBean;
import com.hjhq.teamface.custom.bean.TransformationResultBean;
import com.hjhq.teamface.custom.ui.add.AddCustomActivity;
import com.hjhq.teamface.custom.ui.add.EditCustomActivity;
import com.hjhq.teamface.custom.ui.file.FileDetailActivity;
import com.hjhq.teamface.custom.ui.funcation.SharePresenter;
import com.hjhq.teamface.custom.ui.funcation.TransferPrincipalPresenter;
import com.hjhq.teamface.custom.ui.template.AutoModuleActivity;
import com.hjhq.teamface.custom.ui.template.RelevanceTempActivity;
import com.hjhq.teamface.custom.utils.CustomDialogUtil;
import com.hjhq.teamface.custom.utils.RelevanceModuleDialog;
import com.hjhq.teamface.customcomponent.widget2.CustomUtil;
import com.luojilab.router.facade.annotation.RouteNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 自定义数据详情
 *
 * @author Administrator
 */
@RouteNode(path = "/detail", desc = "自定义数据详情")
public class DataDetailActivity extends ActivityPresenter<DataDetailDelegate2, DataDetailModel> implements View.OnClickListener {
    /**
     * 编辑请求码
     */
    public static final int REQUEST_EDIT_CODE = 0x503;
    /**
     * 复制请求码
     */
    public static final int REQUEST_COPY_CODE = 0x5013;
    /**
     * 转移负责人
     */
    public static final int REQUEST_TRANSFOR_CODE = 0x504;
    /**
     * 分享
     */
    public static final int REQUEST_SHARE_CODE = 0x5104;
    /**
     * 公海池分配选人
     */
    public static final int REQUEST_ALLOCATE_CODE = 0x5105;
    /**
     * 移动公海池
     */
    public static final int REQUEST_POOL_MOVE_CODE = 0x5106;
    /**
     * 退回公海池
     */
    public static final int REQUEST_POOL_BACK_CODE = 0x5107;

    public static final String POOL_PULL = "领取";
    public static final String POOL_EDIT = "编辑";
    public static final String POOL_ALLOCATE = "分配";
    public static final String POOL_MOVE = "移动";
    public static final String POOL_DEL = "删除";
    public static final String BACK_POOL = "退回公海池";

    /**
     * 操作菜单
     */
    protected List<ModuleFunctionBean.DataBean> functionAuth;
    /**
     * 详情
     */
    protected Map<String, Object> detailMap = new HashMap<>();
    private CustomLayoutResultBean.DataBean customLayoutData;
    /**
     * 公海池菜单ID 为null则代表不是公海池数据
     */
    private String seasPoolId;
    //是否是公海池管理员
    private boolean isPoolAdmin;
    //模块 bean
    protected String moduleBean;
    //数据ID
    protected String objectId;
    //公海池列表
    private List<SeasPoolResponseBean.DataBean> seasPoolList;

    private boolean isChange;
    private List<DataRelationResultBean.DataRelation.RefModule> refModules;

    private List<Fragment> fragments = new ArrayList<>();
    private int viewPagerHeight;
    private int toolBarPositionY;
    /**
     * 是否锁定
     */
    private boolean isLockedState;
    private List<AutoModuleResultBean.DataBean.DataListBean> autoModuleList;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            objectId = intent.getStringExtra(Constants.DATA_ID);
            seasPoolId = intent.getStringExtra(Constants.POOL);
            isPoolAdmin = intent.getBooleanExtra(Constants.IS_POOL_ADMIN, false);
        }
    }


    @Override
    public void init() {
        loadData();
        CustomUtil.handleHidenFields(hashCode(), toString(), viewDelegate.getViewList());
        viewDelegate.getToolbar().post(this::dealWithViewPager);
    }

    @Override
    protected void translucentstatus() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 加载数据
     */
    protected void loadData() {
        loadDataTetail();
        loadDataRelation();
        getAutoModule();
        getModuleFunctionAuth();
        getSeasPool();
    }

    /**
     * 得到公海池
     */
    private void getSeasPool() {
        model.getSeapools(this, moduleBean, new ProgressSubscriber<SeasPoolResponseBean>(this) {
            @Override
            public void onNext(SeasPoolResponseBean baseBean) {
                super.onNext(baseBean);
                seasPoolList = baseBean.getData();
            }
        });
    }

    /**
     * 得到功能权限
     */
    public void getModuleFunctionAuth() {
        if (!TextUtil.isEmpty(seasPoolId)) {
            return;
        }
        AuthHelper.getInstance().getModuleFunctionAuth(this, moduleBean, new AuthHelper.InitialDataCompleteListener() {
            @Override
            public void complete(ModuleFunctionBean moduleFunctionBean) {
                functionAuth = moduleFunctionBean.getData();
            }

            @Override
            public void error() {
                ToastUtils.showError(mContext, "获取权限失败，请退出重试");
            }
        });
    }

    /**
     * 加载关联数据模块和title
     */
    private void loadDataRelation() {
        model.getDataRelation(this, objectId, moduleBean, new ProgressSubscriber<DataRelationResultBean>(this) {
            @Override
            public void onNext(DataRelationResultBean dataRelationResultBean) {
                super.onNext(dataRelationResultBean);
                DataRelationResultBean.DataRelation data = dataRelationResultBean.getData();
                if (data == null) {
                    ToastUtils.showError(mContext, "数据异常");
                    return;
                }
                viewDelegate.setDetailHeadView(data.getOperationInfo());
                refModules = data.getRefModules();
                handleModule();
            }
        });
    }

    /**
     * 获取所有自动化模块
     */
    private void getAutoModule() {
        model.getAutoModule(this, moduleBean, new ProgressSubscriber<AutoModuleResultBean>(this) {
            @Override
            public void onNext(AutoModuleResultBean autoModuleResultBean) {
                super.onNext(autoModuleResultBean);
                autoModuleList = autoModuleResultBean.getData().getDataList();
                handleModule();
            }
        });
    }

    private void handleModule() {
        viewDelegate.setAdapterData(refModules);
        viewDelegate.setAuthModule(autoModuleList);
    }


    /**
     * 资料
     */
    protected void loadDataTetail() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", objectId);
        map.put("bean", moduleBean);
        model.getDataDetail(this, map, new ProgressSubscriber<DetailResultBean>(this) {
            @Override
            public void onNext(DetailResultBean detailResultBean) {
                super.onNext(detailResultBean);
                detailMap = detailResultBean.getData();
                showDetail();
            }
        });
        //客户资料
        Map<String, Object> layoutMap = new HashMap<>();
        layoutMap.put("bean", moduleBean);
        layoutMap.put("operationType", CustomConstants.DETAIL_STATE + "");
        layoutMap.put("dataId", objectId);
        //1 是公海池
        layoutMap.put("isSeasPool", TextUtil.isEmpty(seasPoolId) ? null : "1");
        model.getCustomLayout(this, layoutMap, new ProgressSubscriber<CustomLayoutResultBean>(this) {
            @Override
            public void onNext(CustomLayoutResultBean customLayoutResultBean) {
                super.onNext(customLayoutResultBean);
                customLayoutData = customLayoutResultBean.getData();
                showDetail();
            }
        });
    }

    /**
     * 显示详情数据
     */
    private synchronized void showDetail() {
        if (customLayoutData != null && detailMap.size() > 0) {
            fragments.clear();
            if ("1".equals(customLayoutData.getCommentControl())) {
                viewDelegate.showComment();
                CommentFragment commentFragment = CommentFragment.newInstance(moduleBean, objectId);
                fragments.add(commentFragment);
            }
            if ("1".equals(customLayoutData.getDynamicControl())) {
                viewDelegate.showDynamic();
                DynamicFragment dynamicFragment = DynamicFragment.newInstance(moduleBean, objectId);
                fragments.add(dynamicFragment);
            }
            isLockedState = "1".equals(detailMap.get(CustomConstants.LOCKED_STATE));
            String email = CustomUtil.getEmail(detailMap);
            if (email != null) {
                viewDelegate.showEmail();
                EmailBoxFragment emailBoxFragment = EmailBoxFragment.newInstance(email);
                fragments.add(emailBoxFragment);
            }

            viewDelegate.drawLayout(customLayoutData, detailMap, CustomConstants.DETAIL_STATE, moduleBean);
            viewDelegate.setViewPage(getSupportFragmentManager(), fragments);
            viewDelegate.setCurrentItem(0);
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_DATA_DETAIL_CODE, tag -> CommonUtil.startActivtiy(mContext, DataDetailActivity.class, (Bundle) tag));
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_FILE_DETAIL_CODE, tag -> {
            CommonUtil.startActivtiy(mContext, FileDetailActivity.class, (Bundle) tag);
        });

        viewDelegate.recyclerModule.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                clickRelevance(position);
            }
        });
        viewDelegate.setOnClickListener(this, R.id.tv_dynamic, R.id.tv_comment, R.id.tv_email, R.id.iv_more);
        viewDelegate.getToolbar().setNavigationOnClickListener(view -> {
            if (isChange) {
                setResult(RESULT_OK);
            }
            finish();
        });
        viewDelegate.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewDelegate.setCurrentStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewDelegate.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] location = new int[2];
                viewDelegate.get(R.id.ll_bottom_option).getLocationOnScreen(location);
                int yPosition = location[1];

                int[] moduleLocation = new int[2];
                viewDelegate.recyclerModule.getLocationOnScreen(moduleLocation);
                float v1 = moduleLocation[1] + DeviceUtils.dpToPixel(mContext, 50);
                if (yPosition < v1) {
                    viewDelegate.scrollView.setNeedScroll(false);
                } else {
                    viewDelegate.scrollView.setNeedScroll(true);
                }
            }
        });
    }

    /**
     * 点击关联模块
     *
     * @param position
     */
    private void clickRelevance(int position) {
        Bundle bundle = new Bundle();
        if (position < CollectionUtils.size(refModules)) {
            DataRelationResultBean.DataRelation.RefModule item = refModules.get(position);
            bundle.putString(Constants.MODULE_BEAN, item.getModuleName());
            bundle.putString(Constants.NAME, item.getModuleLabel());
            bundle.putString(Constants.DATA_ID, objectId);
            bundle.putString(Constants.FIELD_NAME, item.getFieldName());

            bundle.putString(CustomConstants.FIELD_VALUE_TAG, detailMap.get(item.getReferenceField()) + "");
            CommonUtil.startActivtiy(mContext, RelevanceTempActivity.class, bundle);
        } else {
            AutoModuleResultBean.DataBean.DataListBean dataListBean = autoModuleList.get(position - CollectionUtils.size(refModules));
            bundle.putString(Constants.DATA_TAG1, dataListBean.getSorce_bean());
            bundle.putString(Constants.DATA_TAG2, dataListBean.getTarget_bean());
            bundle.putString(Constants.DATA_ID, objectId);
            bundle.putString(Constants.NAME, dataListBean.getTitle());
            CommonUtil.startActivtiy(mContext, AutoModuleActivity.class, bundle);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_comment) {
            viewDelegate.setCurrentItem(0);
        } else if (id == R.id.tv_dynamic) {
            viewDelegate.setCurrentItem(1);
        } else if (id == R.id.tv_email) {
            viewDelegate.setCurrentItem(2);
        } else if (id == R.id.iv_more) {
            showRelevanceDialog();
        }
    }


    /**
     * 显示关联模块弹窗
     */
    private void showRelevanceDialog() {
        List<GameLabel> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(refModules)) {
            for (DataRelationResultBean.DataRelation.RefModule remodule : refModules) {
                GameLabel label = new GameLabel();
                label.name = remodule.getModuleLabel();
                list.add(label);
            }
        }
        new RelevanceModuleDialog(mContext)
                .builder().setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setListener((name, position) -> clickRelevance(position))
                .addLabel(list).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        optionsMenu();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 操作事件
     */
    private void optionsMenu() {
        String[] options;
        if (!TextUtil.isEmpty(seasPoolId)) {
            options = getSeasPoolOptions();
            PopUtils.showBottomMenu(this, viewDelegate.getRootView(), "设置", options, position -> {
                switch (options[position]) {
                    case POOL_PULL:
                        pull();
                        break;
                    case POOL_EDIT:
                        editCustom();
                        break;
                    case POOL_ALLOCATE:
                        allocate();
                        break;
                    case POOL_MOVE:
                        moveSeasPool();
                        break;
                    case POOL_DEL:
                        delData();
                        break;
                    default:
                        break;
                }
                return true;
            });

        } else {
            //普通数据
            if (CollectionUtils.isEmpty(functionAuth)) {
                ToastUtils.showError(mContext, "未获取到功能权限");
                return;
            }
            options = getDataOptions();
            PopUtils.showBottomMenu(this, viewDelegate.getRootView(), "设置", options, position -> {
                if (BACK_POOL.equals(options[position])) {
                    //退回公海池
                    backSeasPool();
                    return true;
                }
                switch (functionAuth.get(position).getAuth_code()) {
                    case CustomConstants.UPDATE:
                        editCustom();
                        break;
                    case CustomConstants.TRANSFER:
                        transferPrincipal();
                        break;
                    case CustomConstants.ADD_NEW:
                        copyData();
                        break;
                    case CustomConstants.CONVERT:
                        convert();
                        break;
                    case CustomConstants.SHARE:
                        shareData();
                        break;
                    case CustomConstants.DELETE:
                        delData();
                        break;
                    default:
                        break;
                }
                return true;
            });
        }

    }

    /**
     * 得到公海池的操作功能
     */
    private String[] getSeasPoolOptions() {
        String[] options;
        if (isPoolAdmin) {
            if (CollectionUtils.isEmpty(seasPoolList)) {
                options = new String[4];
                options[0] = POOL_PULL;
                options[1] = POOL_EDIT;
                options[2] = POOL_ALLOCATE;
                options[3] = POOL_DEL;
            } else {
                options = new String[5];
                options[0] = POOL_PULL;
                options[1] = POOL_EDIT;
                options[2] = POOL_ALLOCATE;
                options[3] = POOL_MOVE;
                options[4] = POOL_DEL;
            }
        } else {
            options = new String[1];
            options[0] = POOL_PULL;
        }
        return options;
    }

    /**
     * 得到正常数据的操作功能
     */
    private String[] getDataOptions() {
        String[] options;
        Iterator<ModuleFunctionBean.DataBean> iterator = functionAuth.iterator();
        while (iterator.hasNext()) {
            ModuleFunctionBean.DataBean next = iterator.next();
            switch (next.getAuth_code()) {
                case CustomConstants.ADD_NEW:
                    next.setFunc_name("复制");
                    break;
                case CustomConstants.UPDATE:
                case CustomConstants.SHARE:
                case CustomConstants.DELETE:
                case CustomConstants.CONVERT:
                case CustomConstants.TRANSFER:
                    if (isLockedState) {
                        iterator.remove();
                    }
                    break;
                default:
                    iterator.remove();
                    break;
            }
        }
        //编辑、转移负责人、复制、转换、删除、共享；
        int size = functionAuth.size();
        if (!CollectionUtils.isEmpty(seasPoolList)) {
            options = new String[size + 1];
            options[size] = BACK_POOL;
        } else {
            options = new String[size];
        }
        int i = 0;
        for (ModuleFunctionBean.DataBean dataBean : functionAuth) {
            options[i++] = dataBean.getFunc_name();
        }
        return options;
    }

    /**
     * 退回公海池
     */
    private void backSeasPool() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, BACK_POOL);
        bundle.putSerializable(Constants.DATA_TAG2, (Serializable) seasPoolList);
        bundle.putString(Constants.MODULE_BEAN, moduleBean);
        bundle.putString(Constants.MODULE_ID, objectId);
        CommonUtil.startActivtiyForResult(mContext, SeasPoolActivity.class, REQUEST_POOL_BACK_CODE, bundle);
    }

    /**
     * 公海池移动
     */
    private void moveSeasPool() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, POOL_MOVE);
        bundle.putSerializable(Constants.DATA_TAG2, (Serializable) seasPoolList);
        bundle.putString(Constants.MODULE_BEAN, moduleBean);
        bundle.putString(Constants.MODULE_ID, objectId);
        bundle.putString(Constants.POOL, seasPoolId);
        CommonUtil.startActivtiyForResult(mContext, SeasPoolActivity.class, REQUEST_POOL_MOVE_CODE, bundle);
    }

    /**
     * 公海池分配
     */
    private void allocate() {
        Bundle bundle = new Bundle();
        bundle.putInt(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
        CommonUtil.startActivtiyForResult(mContext, SelectMemberActivity.class, REQUEST_ALLOCATE_CODE, bundle);
    }

    /**
     * 公海池领取
     */
    private void pull() {
        model.take(this, objectId, moduleBean, seasPoolId, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "领取成功！");
                seasPoolId = null;
                isPoolAdmin = false;
                loadData();
            }
        });
    }


    /**
     * 转换
     */
    private void convert() {
        model.getFieldTransformations(this, moduleBean, new ProgressSubscriber<TransformationResultBean>(this) {
            @Override
            public void onNext(TransformationResultBean baseBean) {
                super.onNext(baseBean);
                convertDialog(baseBean.getData());
            }
        });
    }

    private void convertDialog(List<TransformationResultBean.DataBean> dataBeanList) {
        if (CollectionUtils.isEmpty(dataBeanList)) {
            ToastUtils.showToast(this, "无可转换的模块");
            return;
        }
        CustomDialogUtil.mutilDialog(this, "可转换目标模块", dataBeanList, viewDelegate.getRootView(), positions -> {
            if (!CollectionUtils.isEmpty(positions)) {
                TransformationRequestBean bean = new TransformationRequestBean();
                bean.setModuleId(objectId);
                bean.setBean(moduleBean);
                bean.setIds(positions);
                model.transformations(this, bean, new ProgressSubscriber<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        ToastUtils.showSuccess(mContext, "转换成功！");
                        isChange = true;
                    }
                });
            }
        });
    }

    /**
     * 共享数据
     */
    private void shareData() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, moduleBean);
        bundle.putString(Constants.DATA_ID, objectId);
        CommonUtil.startActivtiyForResult(this, SharePresenter.class, REQUEST_SHARE_CODE, bundle);
    }

    /**
     * 复制
     */
    private void copyData() {
        model.copy(this, objectId, moduleBean, new ProgressSubscriber<DetailResultBean>(this) {
            @Override
            public void onNext(DetailResultBean detailResultBean) {
                super.onNext(detailResultBean);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MODULE_BEAN, moduleBean);
                bundle.putSerializable(Constants.DATA_TAG1, (Serializable) detailMap);
                CommonUtil.startActivtiyForResult(DataDetailActivity.this, AddCustomActivity.class, REQUEST_COPY_CODE, bundle);
            }
        });
    }

    /**
     * 删除
     */
    private void delData() {
        DialogUtils.getInstance().sureOrCancel(this, "提示", "删除后不可恢复，确认要删除吗？", viewDelegate.getRootView(), () -> {
            model.del(this, objectId, moduleBean, new ProgressSubscriber<BaseBean>(this) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(mContext, "删除成功");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        });
    }

    /**
     * 编辑
     */
    protected void editCustom() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, moduleBean);
        bundle.putString(Constants.DATA_ID, objectId);
        bundle.putSerializable(Constants.DATA_TAG1, (Serializable) detailMap);
        bundle.putSerializable(Constants.POOL, seasPoolId);
        CommonUtil.startActivtiyForResult(this, EditCustomActivity.class, REQUEST_EDIT_CODE, bundle);
    }

    /**
     * 转移负责人
     */
    private void transferPrincipal() {
        CommonUtil.startActivtiyForResult(this, TransferPrincipalPresenter.class, REQUEST_TRANSFOR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == REQUEST_EDIT_CODE) {
            isChange = true;
            loadDataTetail();
            loadDataRelation();
        } else if (REQUEST_TRANSFOR_CODE == requestCode) {
            int share = data.getIntExtra(Constants.DATA_TAG1, 1);
            Member member = (Member) data.getSerializableExtra(Constants.DATA_TAG2);
            model.transfor(this, objectId, moduleBean, member.getId() + "", share + "", new ProgressSubscriber<BaseBean>(this) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(mContext, "转移负责人成功");
                    isChange = true;
                    init();
                }
            });
        } else if (REQUEST_ALLOCATE_CODE == requestCode) {
            List<Member> members = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            Member member = members.get(0);
            model.allocate(mContext, objectId, moduleBean, member.getId() + "", new ProgressSubscriber<BaseBean>(mContext) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    seasPoolId = null;
                    isPoolAdmin = false;
                    isChange = true;
                    loadData();
                }
            });
        } else if (REQUEST_POOL_BACK_CODE == requestCode) {
            setResult(RESULT_OK);
            finish();
        } else if (REQUEST_POOL_MOVE_CODE == requestCode) {
            setResult(RESULT_OK);
            finish();
        } else if (REQUEST_COPY_CODE == requestCode) {
            isChange = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (isChange) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    private void dealWithViewPager() {
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
        int toolBarHeight = (int) (viewDelegate.getToolbar().getHeight() + statusBarHeight + DeviceUtils.dpToPixel(mContext, 40));
        toolBarPositionY = (int) (toolBarHeight + DeviceUtils.dpToPixel(mContext, 45));
        ViewGroup.LayoutParams params = viewDelegate.mViewPager.getLayoutParams();

        params.height = (int) (ScreenUtils.getScreenHeight(mContext) - toolBarHeight + 1);
        viewDelegate.mViewPager.setLayoutParams(params);
    }
}
