package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.bean.AppModuleResultBean;
import com.hjhq.teamface.basis.bean.AssistantDataBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.ImMessage;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.PageInfo;
import com.hjhq.teamface.basis.bean.QueryApprovalDataResultBean;
import com.hjhq.teamface.basis.bean.ViewDataAuthResBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.PushMessage;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.ParseUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.EmptyView;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.AssistantListAdapter;
import com.hjhq.teamface.im.db.DBManager;
import com.hjhq.teamface.im.bean.AppAssistantInfoBean;
import com.hjhq.teamface.im.bean.AppAssistantListBean;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小助手
 *
 * @author Administrator
 */
public class AppAssistantActivity extends BaseTitleActivity {
    private static final String TAG = "AppAssistantActivity";
    private static final int TYPE_NEW = 1;
    private static final int TYPE_BETWEEN = 2;
    private static final int TYPE_OLD = 3;

    private RxAppCompatActivity mContext;
    private RecyclerView rvList;
    private SmartRefreshLayout refreshLayout;
    private String assistantId;
    private String applicationId;
    private String titleString;
    private String assistantType;
    private String beanName;
    private String filterBeanName = "";
    private String iconType;
    private String iconColor;
    private String iconUrl;
    private AssistantListAdapter mAdapter;
    private ArrayList<PushMessage> dataList = new ArrayList<>();
    private List<AppModuleBean> moduleList = new ArrayList<>();
    private boolean markAllReaded = false;
    private int mPosition = 0;
    private long lastRefreshTime;
    private EmptyView mEmptyView;


    //分页
    private int currentPageNo = 1;//当前页数
    private int totalPages = 1;//总页数
    private int state = Constants.NORMAL_STATE;
    private int pageSize = Constants.PAGESIZE;

    private long lastMessageTime = 0L;
    //显示已读
    private boolean onlyUnreaded = false;
    private boolean getDataBefore = false;


    @Override
    protected int getChildView() {
        return R.layout.activity_app_assistant;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);

    }

    private void initIntent(Intent intent) {
        if (intent != null) {
            assistantType = intent.getStringExtra(Constants.DATA_TAG1);
            iconType = intent.getStringExtra(Constants.DATA_TAG3);
            iconColor = intent.getStringExtra(Constants.DATA_TAG4);
            iconUrl = intent.getStringExtra(Constants.DATA_TAG5);
            assistantId = intent.getStringExtra(MsgConstant.CONVERSATION_ID);
            applicationId = intent.getStringExtra(MsgConstant.APPLICATION_ID);
            titleString = intent.getStringExtra(MsgConstant.CONV_TITLE);
            beanName = intent.getStringExtra(MsgConstant.BEAN_NAME);
            lastMessageTime = intent.getLongExtra(Constants.DATA_TAG2, 0L);
            String viewReaded = intent.getStringExtra(MsgConstant.VIEW_READED);
            onlyUnreaded = "1".equals(viewReaded);
            if ("1".equals(assistantType)) {
                setRightMenuIcons(R.drawable.icon_filtrate, R.drawable.icon_setting);
                getFilterData();
            } else {
                setRightMenuIcons(R.drawable.icon_setting);
            }

            setActivityTitle(titleString + " ");
            getLocalData();
        } else {
            ToastUtils.showError(mContext, "数据错误");
            finish();
        }

    }

    /**
     * 全部标记为已读
     */
    private void markAllRead() {
        ImLogic.getInstance().markAllRead(((BaseActivity) mContext),
                assistantId,
                new ProgressSubscriber<BaseBean>(((BaseActivity) mContext), false) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        markAllReaded = true;
                        DBManager.getInstance().markAllPushMessageRead(assistantId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        e.printStackTrace();
                    }
                });

    }

    @Override
    protected void initView() {
        super.initView();
        mContext = this;
        mEmptyView = new EmptyView(this);
        mEmptyView.setEmptyImage(R.drawable.empty_view_img);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        initAdapter();
        initIntent(getIntent());
    }

    private void initAdapter() {
        mAdapter = new AssistantListAdapter(dataList);
        mAdapter.setEmptyView(mEmptyView);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(mAdapter);

    }

    private void refreshData() {
        if (dataList.size() <= 0) {
            getNetData();
        } else {
            //获取比时间戳更新的数据
            getLimitData(TYPE_NEW, TextUtil.parseLong(dataList.get(0).getCreate_time()), null);
        }
    }

    @Override
    protected void setListener() {
        //刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            state = Constants.REFRESH_STATE;
            refreshData();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            state = Constants.LOAD_STATE;
            if (dataList.size() <= 0) {
                refreshLayout.finishLoadMore();
                return;
            }
            getLimitData(TYPE_OLD, null, TextUtil.parseLong(dataList.get(dataList.size() - 1).getCreate_time()));
        });
        //加载更更多

        rvList.addOnItemTouchListener(new SimpleItemClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final PushMessage message = dataList.get(position);
                //小助手类型
                switch (dataList.get(position).getType()) {
                    case MsgConstant.TYPE_PROJECT_TASK:
                        //项目任务
                        viewProjectTask(message);
                        break;
                    case MsgConstant.TYPE_PERSONAL_TASK:
                        //个人任务
                        viewPersonalTask(message);
                        break;
                    case MsgConstant.TYPE_GROUP_OPERATION:
                    case MsgConstant.TYPE_GROUP_INFO_CHANGED:
                    case MsgConstant.TYPE_QUIT_GROUP:
                    case MsgConstant.TYPE_ADD_MEMBER:
                    case MsgConstant.TYPE_REMOVE_MEMBER:
                        //showPreview(position);
                        break;
                    case MsgConstant.TYPE_EMAIL:
                        //邮件推送
                        bundle.putString(Constants.DATA_TAG1, dataList.get(position).getData_id());
                        bundle.putInt(Constants.DATA_TAG2, EmailConstant.NO_OPERATION);
                        UIRouter.getInstance().openUri(mContext, "DDComp://email/detail", bundle, Constants.REQUEST_CODE2);
                        break;
                    case MsgConstant.TYPE_AT_ME:
                        viewAtData(position);
                        break;
                    case MsgConstant.TYPE_SELF_DEFINE:
                        //自定义推送
                        //模块权限判断
                        viewModuleDataWithAuth(position);
                        break;
                    case MsgConstant.TYPE_APPROVE:
                    case MsgConstant.TYPE_APPROVE_OPERATION:
                        //审批
                        viewApprovalWithoutAuth(position);
                        break;
                    case MsgConstant.TYPE_FROM_FILE_LIB:
                        //文件库
                        viewFileWithAuthV2(position);
                        break;
                    case MsgConstant.TYPE_FRIEND_CIRCLE:
                        //同事圈
                        UIRouter.getInstance().openUri(mContext, "DDComp://friend/detail", bundle);
                        //CommonUtil.startActivtiy(mContext, FriendsActivity.class, new Bundle());
                        break;
                    case MsgConstant.TYPE_MEMO:
                        //备忘录
                        viewMemoWithAuth(position);
                        break;
                    case MsgConstant.TYPE_FLOW:
                        break;
                    default:
                        break;
                }
                markReaded(position);
            }


        });

    }

    /**
     * 查看项目任务
     *
     * @param message
     */
    private void viewProjectTask(PushMessage message) {

        try {
            if (message == null || TextUtils.isEmpty(message.getParam_fields())) {
                return;
            }
            Bundle bundle = new Bundle();
            org.json.JSONObject jsonObject = new org.json.JSONObject(message.getParam_fields());
            if (jsonObject == null) {
                return;
            }
            //1主任务,2子任务
            int taskType = TextUtils.isEmpty(jsonObject.optString("task_Type")) ? 1 : 2;
            String projectId = jsonObject.optString("projectId");
            String taskId = jsonObject.optString("task_id");
            String mainTaskId = jsonObject.optString("taskInfoId");
            String id = jsonObject.optString("id");
            bundle.putInt(ProjectConstants.TASK_FROM_TYPE, taskType);
            bundle.putLong(ProjectConstants.PROJECT_ID, TextUtil.parseLong(projectId));
            if (taskType == 2) {
                bundle.putLong(ProjectConstants.TASK_ID, TextUtil.parseLong(mainTaskId));
                // bundle.putLong(ProjectConstants.MAIN_TASK_ID, TextUtil.parseLong(mainTaskId));
            } else if (taskType == 1) {
                //主任务
                bundle.putLong(ProjectConstants.TASK_ID, TextUtil.parseLong(mainTaskId));

            }
            bundle.putString(ProjectConstants.TASK_NAME, jsonObject.optString("taskName"));
            bundle.putString(Constants.MODULE_BEAN, message.getBean_name());
            UIRouter.getInstance().openUri(mContext, "DDComp://project/project_task_detail", bundle, Constants.REQUEST_CODE2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看项目任务-临时使用
     *
     * @param message
     */
    private void viewProjectTask2(PushMessage message) {
        Bundle bundle = new Bundle();
        bundle.putLong(ProjectConstants.PROJECT_ID, 15L);
        bundle.putLong(ProjectConstants.SUBNODE_ID, 61L);
        bundle.putLong(ProjectConstants.TASK_ID, 34);
        bundle.putString(ProjectConstants.TASK_NAME, "【我的】个人名片 递名片功能");
        bundle.putString(Constants.MODULE_BEAN, message.getBean_name());
        UIRouter.getInstance().openUri(mContext, "DDComp://project/project_task_detail", bundle, Constants.REQUEST_CODE2);

    }

    /**
     * 查看个人任务
     *
     * @param message
     */
    private void viewPersonalTask(PushMessage message) {


        try {
            if (message == null || TextUtils.isEmpty(message.getParam_fields())) {
                return;
            }
            Bundle bundle = new Bundle();
            org.json.JSONObject jsonObject = new org.json.JSONObject(message.getParam_fields());
            if (jsonObject == null) {
                return;
            }
            //0主任务1子任务
            String taskId = jsonObject.optString("task_id");
            String id = jsonObject.optString("id");
            String mainTaskId = jsonObject.optString("taskInfoId");
            int taskType = TextUtils.isEmpty(jsonObject.optString("task_Type")) ? 0 : 1;
           /* if (taskType == 0) {
                bundle.putLong(ProjectConstants.TASK_ID, TextUtil.parseLong(taskId));
            } else if (taskType == 1) {
            }*/
            bundle.putLong(ProjectConstants.TASK_ID, TextUtil.parseLong(taskId));
            bundle.putInt(ProjectConstants.TASK_FROM_TYPE, taskType);

            bundle.putString(ProjectConstants.TASK_NAME, jsonObject.optString("taskName"));
            bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PERSONAL_TASK_BEAN);
            UIRouter.getInstance().openUri(mContext, "DDComp://project/personal_task_detail", bundle, Constants.REQUEST_CODE2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 标记已读
     *
     * @param position
     */
    private void markReaded(final int position) {
        if (TextUtils.isEmpty(dataList.get(position).getId() + "")) {
            return;
        }
        if ("0".equals(dataList.get(position).getRead_status())) {
            ImLogic.getInstance().readMessage(AppAssistantActivity.this, assistantId, dataList.get(position).getId() + "", new ProgressSubscriber<BaseBean>(AppAssistantActivity.this) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    // ToastUtils.showToast(mContext, "已读请求失败!");
                }

                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);

                    if (TextUtils.isEmpty(dataList.get(position).getId() + "")) {
                        return;
                    }
                    dataList.get(position).setRead_status("1");
                    mAdapter.notifyDataSetChanged();
                    EventBusUtils.sendEvent(new MessageBean(11, MsgConstant.ASSISTANT_READ_TAG, assistantId));
                    DBManager.getInstance().saveOrReplace(dataList.get(position));
                }
            });
        }
    }

    /**
     * 查看文件
     *
     * @param position
     */
    private void viewFileWithAuth(final int position) {
        ImLogic.getInstance().queryAuth(AppAssistantActivity.this,
                FileConstants.FILE_LIBRARY_BEAN_NAME,
                dataList.get(position).getStyle(), dataList.get(position).getData_id(),
                new ProgressSubscriber<ViewDataAuthResBean>(AppAssistantActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(ViewDataAuthResBean viewDataAuthResBean) {
                        super.onNext(viewDataAuthResBean);
                        if (viewDataAuthResBean.getData() != null && !TextUtils.isEmpty(viewDataAuthResBean.getData().getReadAuth())) {
                            switch (viewDataAuthResBean.getData().getReadAuth()) {
                                case "0":
                                    ToastUtils.showError(mContext, "无权查看或数据已删除");
                                    break;
                                case "1":
                                    Bundle bundle = new Bundle();
                                    bundle.putString(FileConstants.FILE_ID, dataList.get(position).getData_id());
                                    bundle.putInt(FileConstants.FOLDER_STYLE, TextUtil.parseInt(dataList.get(position).getStyle()));
                                    UIRouter.getInstance().openUri(mContext, "DDComp://filelib/file_detail", bundle, Constants.REQUEST_CODE1);
                                    break;
                                case "2":
                                    ToastUtils.showError(mContext, "无权查看或数据已删除");
                                    break;
                                default:
                                    ToastUtils.showError(mContext, "无权查看或数据已删除");
                                    break;
                            }
                        } else {
                            // ToastUtils.showError(mContext, "查询权限失败");
                        }
                    }
                });
    }

    /**
     * 查看文件库文件(已做权限控制)
     *
     * @param position
     */
    private void viewFileWithAuthV2(final int position) {
        ImLogic.getInstance().queryAuth(AppAssistantActivity.this,
                dataList.get(position).getBean_name(), dataList.get(position).getStyle(),
                dataList.get(position).getData_id(),
                new ProgressSubscriber<ViewDataAuthResBean>(AppAssistantActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        // ToastUtils.showError(mContext, "查询权限失败");
                    }

                    @Override
                    public void onNext(ViewDataAuthResBean viewDataAuthResBean) {
                        super.onNext(viewDataAuthResBean);
                        if (viewDataAuthResBean.getData() != null && "1".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            Bundle bundle = new Bundle();
                            bundle.putString(FileConstants.FILE_ID, dataList.get(position).getData_id());
                            bundle.putInt(FileConstants.FOLDER_STYLE, TextUtil.parseInt(dataList.get(position).getStyle()));
                            UIRouter.getInstance().openUri(mContext, "DDComp://filelib/file_detail", bundle, Constants.REQUEST_CODE1);

                        } /*else if (viewDataAuthResBean.getData() != null && "0".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");

                        } else if (viewDataAuthResBean.getData() != null && "2".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");
                        } */ else {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");
                            //ToastUtils.showError(mContext, "查询权限失败");
                        }
                    }
                });
        /*ImLogic.getInstance().queryAuth(AppAssistantActivity.this,
                FileConstants.FILE_LIBRARY_BEAN_NAME,
                dataList.get(position).getStyle(), dataList.get(position).getData_id(),
                new ProgressSubscriber<ViewDataAuthResBean>(AppAssistantActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(ViewDataAuthResBean viewDataAuthResBean) {
                        super.onNext(viewDataAuthResBean);
                        if (viewDataAuthResBean.getData() != null && !TextUtils.isEmpty(viewDataAuthResBean.getData().getReadAuth())) {
                            switch (viewDataAuthResBean.getData().getReadAuth()) {
                                case "0":
                                    ToastUtils.showError(mContext, "无权查看");
                                    break;
                                case "1":
                                    Bundle bundle = new Bundle();
                                    bundle.putString(FileConstants.FILE_ID, dataList.get(position).getData_id());
                                    bundle.putInt(FileConstants.FOLDER_STYLE, TextUtil.parseInt(dataList.get(position).getStyle()));
                                    CommonUtil.startActivtiyForResult(AppAssistantActivity.this,
                                            FileDetailActivity.class, Constants.REQUEST_CODE1, bundle);
                                    break;
                                case "2":
                                    ToastUtils.showError(mContext, "数据已删除");
                                    break;
                            }
                        } else {
                            ToastUtils.showError(mContext, "查询权限失败");
                        }
                    }
                });*/
    }

    /**
     * 判断权限,查看数据(@数据)
     *
     * @param position
     */
    private void viewAtData(int position) {
        PushMessage dataListBean = dataList.get(position);
        if (ApproveConstants.APPROVAL_MODULE_BEAN.equals(dataListBean.getBean_name())) {
            viewApprovalWithoutAuth(position);
            return;
        }
        ImLogic.getInstance().queryAuth(AppAssistantActivity.this,
                dataListBean.getBean_name(), dataListBean.getStyle(),
                dataListBean.getData_id(),
                new ProgressSubscriber<ViewDataAuthResBean>(AppAssistantActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        // ToastUtils.showToast(mContext, "查询权限失败");
                    }

                    @Override
                    public void onNext(ViewDataAuthResBean viewDataAuthResBean) {
                        super.onNext(viewDataAuthResBean);
                        if (viewDataAuthResBean.getData() != null && "1".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            Bundle bundle = new Bundle();
                            switch (dataList.get(position).getBean_name()) {
                                case MemoConstant.BEAN_NAME:
                                    viewMemoWithoutAuth(position);
                                    break;
                                case EmailConstant.BEAN_NAME:
                                    bundle.putString(Constants.DATA_TAG1, dataListBean.getData_id());
                                    bundle.putInt(Constants.DATA_TAG2, EmailConstant.NO_OPERATION);
                                    UIRouter.getInstance().openUri(mContext, "DDComp://email/detail", bundle, Constants.REQUEST_CODE2);
                                    break;
                                case ProjectConstants.PROJECT_BEAN_NAME:

                                    break;
                                case FileConstants.FILE_LIBRARY_BEAN_NAME:
                                    bundle.putString(FileConstants.FILE_ID, dataListBean.getData_id());
                                    bundle.putInt(FileConstants.FOLDER_STYLE, TextUtil.parseInt(dataListBean.getStyle()));
                                    UIRouter.getInstance().openUri(mContext, "DDComp://filelib/file_detail", bundle, Constants.REQUEST_CODE1);

                                    break;
                                default:
                                    break;
                            }
                        } /*else if (viewDataAuthResBean.getData() != null && "0".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");

                        } else if (viewDataAuthResBean.getData() != null && "2".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");
                        } else if (viewDataAuthResBean.getData() != null && "3".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");
                        } */ else {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");
                            // ToastUtils.showError(mContext, "查询权限失败");
                        }
                    }
                });
    }

    /**
     * 查看备忘录(包括权限验证)
     *
     * @param position
     */
    private void viewMemoWithAuth(final int position) {
        ImLogic.getInstance().queryAuth(AppAssistantActivity.this,
                dataList.get(position).getBean_name(), dataList.get(position).getStyle(),
                dataList.get(position).getData_id(),
                new ProgressSubscriber<ViewDataAuthResBean>(AppAssistantActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //  ToastUtils.showError(mContext, "查询权限失败");
                    }

                    @Override
                    public void onNext(ViewDataAuthResBean viewDataAuthResBean) {
                        super.onNext(viewDataAuthResBean);
                        if (viewDataAuthResBean.getData() != null && "1".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            viewMemoWithoutAuth(position);
                        } /*else if (viewDataAuthResBean.getData() != null && "0".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");
                        } else if (viewDataAuthResBean.getData() != null && "2".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");
                        } else if (viewDataAuthResBean.getData() != null && "3".equals(viewDataAuthResBean.getData().getReadAuth())) {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");
                        } */ else {
                            ToastUtils.showError(mContext, "无权查看或数据已删除");
                            //ToastUtils.showError(mContext, "查询权限失败");
                        }
                    }
                });
    }

    /**
     * 查询权限查看自定义模块数据详情
     *
     * @param position
     */
    private void viewModuleDataWithAuth(final int position) {
        ImLogic.getInstance().queryAuth(AppAssistantActivity.this,
                dataList.get(position).getBean_name(), dataList.get(position).getStyle(),
                dataList.get(position).getData_id(),
                new ProgressSubscriber<ViewDataAuthResBean>(AppAssistantActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        // ToastUtils.showError(mContext, "查询权限失败");
                    }

                    @Override
                    public void onNext(ViewDataAuthResBean viewDataAuthResBean) {
                        super.onNext(viewDataAuthResBean);
                        if (viewDataAuthResBean == null || viewDataAuthResBean.getData() == null || TextUtils.isEmpty(viewDataAuthResBean.getData().getReadAuth())) {
                            // ToastUtils.showError(mContext, "查询权限失败");
                        } else {
                            String readAuth = viewDataAuthResBean.getData().getReadAuth();
                            switch (readAuth) {
                                case MsgConstant.AUTH_NO:
                                    ToastUtils.showError(mContext, "无权查看或数据已删除");
                                    break;
                                case MsgConstant.AUTH_YES:
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.MODULE_BEAN, dataList.get(position).getBean_name());
                                    bundle.putString(Constants.DATA_ID, dataList.get(position).getData_id());
                                    UIRouter.getInstance().openUri(mContext, "DDComp://custom/detail", bundle, Constants.REQUEST_CODE1);
                                    break;
                                case MsgConstant.AUTH_DATA_DELETED:
                                    ToastUtils.showError(mContext, "无权查看或数据已删除");
                                    break;
                                case MsgConstant.AUTH_SEAS_POOL:
                                    ToastUtils.showError(mContext, "无权查看或数据已删除");
                                    break;
                                default:
                                    ToastUtils.showError(mContext, "无权查看或数据已删除");
                                    break;
                            }
                        }
                    }
                });
    }

    /**
     * 查看审批 (验证权限)
     *
     * @param position
     */
    private void viewApprovalWithoutAuth(int position) {
        PushMessage bean = dataList.get(position);
        String paramFields = bean.getParam_fields();
        if (paramFields == null) {
            ToastUtils.showError(mContext, "数据异常");

            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(paramFields);
        String moduleBean = jsonObject.getString("moduleBean");
        String fromType = jsonObject.getString("fromType");
        String dataId = jsonObject.getString("dataId");
        String taskKey = jsonObject.getString("taskKey");
        String processInstanceId = jsonObject.getString("processInstanceId");

        if (TextUtil.isEmpty(moduleBean) || TextUtil.isEmpty(dataId) || TextUtil.isEmpty(fromType)) {
            ToastUtils.showError(mContext, "数据异常");
            return;
        }

        Map<String, String> map = new HashMap<>(4);
        map.put("moduleBean", moduleBean);
        map.put("dataId", dataId);
        map.put("taskKey", taskKey);
        map.put("type", fromType);
        map.put("processInstanceId", processInstanceId);
        ImLogic.getInstance()
                .queryApprovalData(AppAssistantActivity.this, map, new ProgressSubscriber<QueryApprovalDataResultBean>(mContext) {
                    @Override
                    public void onNext(QueryApprovalDataResultBean approvalBean) {
                        super.onNext(approvalBean);
                        QueryApprovalDataResultBean.DataBean data = approvalBean.getData();
                        Bundle bundle = new Bundle();
                        bundle.putString(ApproveConstants.PROCESS_INSTANCE_ID, data.getProcess_definition_id());
                        bundle.putString(ApproveConstants.PROCESS_FIELD_V, data.getProcess_field_v());
                        bundle.putString(ApproveConstants.MODULE_BEAN, moduleBean);
                        bundle.putString(ApproveConstants.APPROVAL_DATA_ID, data.getApproval_data_id());
                        bundle.putString(ApproveConstants.TASK_KEY, data.getTask_key());
                        bundle.putString(ApproveConstants.TASK_NAME, data.getTask_name());
                        bundle.putString(ApproveConstants.TASK_ID, data.getTask_id());
                        bundle.putString(ApproveConstants.APPROVAL_APP_ASSISTANT, paramFields);
                        bundle.putString(ApproveConstants.APPROVAL_APP_ASSISTANT_ID, bean.getId() + "");
                        bundle.putString(Constants.DATA_ID, data.getId());
                        //审批小助手评论消息 使用详情返回的fromType
                        bundle.putInt(ApproveConstants.APPROVE_TYPE, TextUtil.parseInt(data.getFromType()));
                        UIRouter.getInstance().openUri(mContext, "DDComp://app/approve/detail", bundle);
                        //CommonUtil.startActivtiy(AppAssistantActivity.this, ApproveDetailActivity.class, bundle);
                    }
                });
    }

    /**
     * 查看备忘录
     *
     * @param position
     */
    private void viewMemoWithoutAuth(int position) {
        Bundle bundle2 = new Bundle();
        bundle2.putString(Constants.DATA_TAG1, dataList.get(position).getData_id());
        UIRouter.getInstance().openUri(mContext, "DDComp://memo/detail", bundle2, Constants.REQUEST_CODE1);
    }

    private void showPreview(int position) {
        String title = dataList.get(position).getBean_name_chinese();
        String subTitle = dataList.get(position).getPush_content();
        String time = null;
        try {
            time = DateTimeUtil.fromTime(Long.parseLong(dataList.get(position).getCreate_time()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (true) {
            DialogUtils.getInstance().noAuthView(AppAssistantActivity.this, title + " ", subTitle + " ", time + " ", rvList.getRootView(), new DialogUtils.OnClickSureListener() {
                @Override
                public void clickSure() {

                }
            });
        }
    }

    @Override
    protected void onRightMenuClick(int itemId) {
        switch (itemId) {
            case 0:
                if ("1".equals(assistantType)) {
                    showFilter();
                } else {
                    openSettings();
                }
                break;
            case 1:
                openSettings();

                break;
            default:

                break;


        }
    }

    /**
     * 打开小助手设置
     */
    private void openSettings() {
        Bundle bundle = new Bundle();
        bundle.putString(MsgConstant.CONVERSATION_ID, assistantId);
        bundle.putString(MsgConstant.BEAN_NAME, beanName);
        bundle.putString(Constants.DATA_TAG1, iconType);
        bundle.putString(Constants.DATA_TAG2, iconColor);
        bundle.putString(Constants.DATA_TAG3, iconUrl);
        bundle.putString(Constants.DATA_TAG4, assistantType);
        CommonUtil.startActivtiyForResult(this, AppAssistantSettingActivity.class, Constants.REQUEST_CODE3, bundle);
    }

    private boolean isAppModule() {
        boolean flag = false;
        if (TextUtils.isEmpty(beanName)) {
            return false;
        }
        switch (beanName) {
            case "email":
            case "memo":
            case "project":
            case "file_library":
            case "chat":
            case "approval":
                flag = false;
                break;
            default:
                flag = true;
                break;
        }

        return flag;
    }

    /**
     * 筛选
     */
    private void showFilter() {
        /*if (!flag) {
            getFilterData();
            CommonUtil.showToast("正在获取数据,请稍后重试!");
            return;
        }*/
        if (moduleList == null) {
            getFilterData();
            ToastUtils.showToast(mContext, "正在获取数据,请稍后重试!");
            return;
        }
        //添加全部
        String[] menu = new String[moduleList.size() + 1];
        menu[0] = getString(R.string.im_all);
        for (int i = 0; i < moduleList.size(); i++) {
            menu[i + 1] = moduleList.get(i).getChinese_name();
        }

        PopUtils.showBottomMenuWithoutCancel(this, "全部", getString(R.string.im_choose_module_title), menu, mPosition, 0, position -> {
            ToastUtils.showToast(mContext, menu[position]);
            mPosition = position;
            if (position == 0) {
                filterBeanName = "";
            } else {
                filterBeanName = moduleList.get(position - 1).getEnglish_name();
            }
            getLocalData();
            return true;
        });

    }

    @Override
    protected void initData() {
        // getNetData();
        getLocalData();
    }

    /**
     * 获取小助手列表数据
     */
    private void getNetData() {

        pageSize = Constants.PAGESIZE;

        ImLogic.getInstance().getAssistantMessage(AppAssistantActivity.this,
                assistantId, beanName, 1, pageSize,
                new ProgressSubscriber<AppAssistantListBean>(AppAssistantActivity.this, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onNext(AppAssistantListBean bean) {
                        super.onNext(bean);
                        getDataBefore = true;
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        List<AssistantDataBean> newDataList = bean.getData().getDataList();
                        switch (state) {
                            case Constants.NORMAL_STATE:
                            case Constants.REFRESH_STATE:
                                dataList.clear();
                                refreshLayout.finishRefresh();
                                break;
                            case Constants.LOAD_STATE:
                                refreshLayout.finishLoadMore();

                                break;
                            default:
                                break;
                        }
                        if (newDataList != null && newDataList.size() > 0) {
                            save2Database(newDataList);
                        }
                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    private void getStateData() {
        ImLogic.getInstance().getAssisstantInfo(AppAssistantActivity.this, assistantId, new ProgressSubscriber<AppAssistantInfoBean>(this, false) {
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(AppAssistantInfoBean appAssistantInfoBean) {
                String show_type = appAssistantInfoBean.getData().getShow_type();
                //更新是否只查看未读状态

                onlyUnreaded = "1".equals(show_type);
            }
        });
    }

    /**
     * 根据时间戳获取指定时间范围内的数据
     *
     * @param type 1获取新的数据,2获取区间内的数据,3获取旧的数据
     */
    private void getLimitData(int type, Long upTime, Long downTime) {
        pageSize = Constants.PAGESIZE;

        ImLogic.getInstance().getAssistantMessageLimit(AppAssistantActivity.this,
                assistantId, beanName, pageSize, upTime, downTime,
                new ProgressSubscriber<AppAssistantListBean>(AppAssistantActivity.this, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();

                    }

                    @Override
                    public void onNext(AppAssistantListBean bean) {
                        super.onNext(bean);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        List<AssistantDataBean> newDataList = bean.getData().getDataList();

                        PageInfo pageInfo = bean.getData().getPageInfo();
                        switch (type) {
                            case TYPE_NEW:

                                break;
                            case TYPE_BETWEEN:
                                if (pageInfo.getTotalRows() > 20) {
                                    //清空旧数据
                                    dataList.clear();
                                    mAdapter.notifyDataSetChanged();
                                    DBManager.getInstance().deletePushMessageByAssistantId(assistantId);
                                }

                                break;
                            case TYPE_OLD:
                                if (newDataList == null || newDataList.size() <= 0) {
                                    //将不会再次触发加载更多事件
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                }
                                break;
                        }
                        save2Database(type, upTime + "", downTime + "", newDataList);
                    }
                });
    }

    private void save2Database(List<AssistantDataBean> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            AssistantDataBean bean = list.get(i);
            PushMessage message = new PushMessage();
            message.setId(TextUtil.parseLong(bean.getId()));
            message.setData_id(bean.getData_id());
            message.setType(bean.getType());
            message.setPush_content(bean.getPush_content());
            message.setAssistant_id(bean.getAssistant_id());
            message.setBean_name(bean.getBean_name());
            message.setBean_name_chinese(bean.getBean_name_chinese());
            message.setTitle(bean.getPush_content());
            //message.setSender_name(bean.getBean_name_chinese());
            message.setCreate_time(bean.getDatetime_create_time());
            message.setRead_status(bean.getRead_status());
            message.setGroup_id(bean.getAssistant_id());
            message.setAssistant_name(bean.getBean_name_chinese());
            message.setParam_fields(bean.getParam_fields());
            //用于查找该数据之前的推送
            message.setIm_apr_id(bean.getIm_apr_id());
            message.setIcon_type(bean.getIcon_type());
            message.setIcon_url(bean.getIcon_url());
            message.setIcon_color(bean.getIcon_color());
            message.setMyId(SPHelper.getUserId());
            message.setCompanyId(SPHelper.getCompanyId());
            message.setFieldInfo(ParseUtil.getStringValue(bean.getField_info()));
            DBManager.getInstance().saveOrReplace(message);
        }
        //获取本地数据
        getLocalData();
    }

    /**
     * 获取数据后保存到数据库并显示到列表
     *
     * @param type
     * @param upTime
     * @param downTime
     * @param list
     */
    private void save2Database(int type, String upTime, String downTime, List<AssistantDataBean> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            AssistantDataBean bean = list.get(i);
            PushMessage message = new PushMessage();
            message.setId(TextUtil.parseLong(bean.getId()));
            message.setData_id(bean.getData_id());
            message.setType(bean.getType());
            message.setPush_content(bean.getPush_content());
            message.setAssistant_id(bean.getAssistant_id());
            message.setBean_name(bean.getBean_name());
            message.setBean_name_chinese(bean.getBean_name_chinese());
            message.setTitle(bean.getPush_content());
            //message.setSender_name(bean.getBean_name_chinese());
            message.setCreate_time(bean.getDatetime_create_time());
            message.setRead_status(bean.getRead_status());
            message.setGroup_id(bean.getAssistant_id());
            message.setAssistant_name(bean.getBean_name_chinese());
            message.setParam_fields(bean.getParam_fields());
            message.setMyId(SPHelper.getUserId());
            message.setCompanyId(SPHelper.getCompanyId());
            message.setFieldInfo(ParseUtil.getStringValue(bean.getField_info()));
            message.setIcon_url(bean.getIcon_url());
            message.setIcon_color(bean.getIcon_color());
            message.setIcon_type(bean.getIcon_type());
            DBManager.getInstance().saveOrReplace(message);
        }
        //获取本地数据
        getLocalData(type, upTime, downTime);
    }

    /**
     * 根据时间戳获取本地数据库中数据
     *
     * @param type
     * @param upTime
     * @param downTime
     */
    private void getLocalData(int type, String upTime, String downTime) {
        List<PushMessage> pushMessages = new ArrayList<>();
        switch (type) {
            case TYPE_NEW:
            case TYPE_BETWEEN:
                pushMessages = DBManager.getInstance().queryPushMessageByAssistantId(onlyUnreaded, assistantId, filterBeanName);
                break;
            case TYPE_OLD:
                pushMessages = DBManager.getInstance().queryPushMessageByAssistantId(onlyUnreaded, assistantId, filterBeanName);
                break;
            default:
                break;
        }
        if (pushMessages != null && pushMessages.size() > 0) {
            Log.e("数量2", pushMessages.size() + "");

            dataList.clear();
            dataList.addAll(pushMessages);

        } else {

        }
        if ("2".equals(assistantType) && !markAllReaded) {
            //企信小助手
            markAllRead();
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 查询本地数据
     */
    private void getLocalData() {
        dataList.clear();
        List<PushMessage> pushMessages = DBManager.getInstance().queryPushMessageByAssistantId(onlyUnreaded, assistantId, filterBeanName);
        Log.e("数量1", pushMessages.size() + "");
        if (pushMessages != null) {
            dataList.addAll(pushMessages);
            mAdapter.notifyDataSetChanged();
            if (dataList.size() > 0 && lastMessageTime > TextUtil.parseLong(dataList.get(0).getCreate_time())) {
                getLimitData(TYPE_BETWEEN, lastMessageTime, TextUtil.parseLong(dataList.get(0).getCreate_time()));

            }
            if (dataList.size() <= 0 && !getDataBefore) {
                getNetData();
            }
        }
    }

    /**
     * 获取筛选列表数据
     */
    private void getFilterData() {
        ImLogic.getInstance().getModule(AppAssistantActivity.this, applicationId,
                new ProgressSubscriber<AppModuleResultBean>(AppAssistantActivity.this, false) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AppModuleResultBean bean) {
                        super.onNext(bean);
                        Log.e("筛选数据结果  =    ", new Gson().toJson(bean));
                        moduleList.clear();
                        moduleList.addAll(bean.getData());

                    }
                });
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case Constants.REQUEST_CODE1:
                    getFilterData();
                    break;
                case Constants.REQUEST_CODE2:

                    break;
                case Constants.REQUEST_CODE3:
                    getNetData();
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageBean bean) {
        if (bean != null && MsgConstant.MARK_ALL_READ.equals(bean.getTag())) {
            String assistantId = (((long) bean.getObject()) - MsgConstant.DEFAULT_VALUE) + "";
            DBManager.getInstance().markAllPushMessageRead(assistantId);
            for (int i = 0; i < dataList.size(); i++) {
                dataList.get(i).setRead_status("1");
            }
            mAdapter.notifyDataSetChanged();
        }
        if (bean != null && MsgConstant.VIEW_READED.equals(bean.getTag())) {
            onlyUnreaded = bean.getCode() == 1;
            getLocalData();
            return;
        }
        if (bean != null && MsgConstant.TYPE_MARK_ONE_ITEM_READ.equals(bean.getTag())) {
            long dataId = TextUtil.parseLong((String) bean.getObject());
            for (int i = 0; i < dataList.size(); i++) {
                if (dataId == dataList.get(i).getId()) {
                    dataList.get(i).setRead_status("1");
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
            return;
        }
        if (bean != null && MsgConstant.TYPE_APPROVE_OPERATION.equals(bean.getTag())) {
            PushMessage message = (PushMessage) bean.getObject();
            if (assistantId.equals(message.getAssistant_id())) {
                getLocalData();
            }
            return;
        }
        if (bean != null && MsgConstant.RECEIVE_ASSISTANT_NAME_CHANGE_PUSH_MESSAGE.equals(bean.getTag())) {
            long assisId = (long) bean.getObject();
            if (TextUtil.parseLong(assistantId) + MsgConstant.DEFAULT_VALUE == assisId) {
                getLocalData();
            }
            return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImMessage(ImMessage message) {
        if (message != null && EventConstant.TYPE_APPLICATION_UPDATE.equals(message.getTag())) {
            PushMessage pm = (PushMessage) message.getObject();
            if (assistantId.equals(pm.getAssistant_id())) {
                setActivityTitle(pm.getTitle());
                iconColor = pm.getIcon_color();
                iconType = pm.getIcon_type();
                iconUrl = pm.getIcon_url();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null && mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            refreshData();
        }
    }


}
