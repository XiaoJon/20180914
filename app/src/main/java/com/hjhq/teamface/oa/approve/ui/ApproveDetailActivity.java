package com.hjhq.teamface.oa.approve.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.DetailResultBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.JsonParser;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.bean.ApproveAuthBean;
import com.hjhq.teamface.common.bean.LinkageFieldsResultBean;
import com.hjhq.teamface.common.ui.comment.CommentActivity;
import com.hjhq.teamface.common.ui.email.EmailDetailFragment;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.ApproveUtils;
import com.hjhq.teamface.componentservice.custom.CustomService;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.customcomponent.widget2.ReferenceViewInterface;
import com.hjhq.teamface.customcomponent.widget2.subfield.SubfieldView;
import com.hjhq.teamface.customcomponent.widget2.subforms.CommonSubFormsView;
import com.hjhq.teamface.oa.approve.bean.ApproveCopyRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveRevokeRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproverBean;
import com.hjhq.teamface.oa.approve.bean.ProcessFlowResponseBean;
import com.hjhq.teamface.util.CommonUtil;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * 审批数据详情
 *
 * @author Administrator
 */
@RouteNode(path = "/approve/detail", desc = "审批数据详情")
public class ApproveDetailActivity extends ActivityPresenter<ApproveDetailDelegate, ApproveModel> implements View.OnClickListener, ReferenceViewInterface {
    /**
     * 编辑请求码
     */
    public static final int REQUEST_EDIT_CODE = 0x503;
    /**
     * 催办
     */
    public static final int REQUEST_URGETO_CODE = 0x505;
    /**
     * 驳回
     */
    public static final int REQUEST_REJECT_CODE = 0x506;
    /**
     * 转交
     */
    public static final int REQUEST_TRANSFER_CODE = 0x507;
    /**
     * 通过
     */
    public static final int REQUEST_PASS_CODE = 0x508;

    protected Map<String, Object> detailMap = new HashMap<>();
    private CustomLayoutResultBean.DataBean customLayoutData;
    private int type;
    //模块bean
    protected String moduleBean;
    //审批数据id，可以当模块数据Id用
    protected String approveDataId;
    //数据id，用于评论
    protected String dataId;
    //流程实例id
    protected String processInstanceId;
    //当前节点key
    private String taskKey;
    //当前节点ID
    private String taskId;
    //当前节点名称
    private String taskName;
    //流程状态
    private String processStatus;
    /**
     * 当前节点 审批人ID
     */
    private List<String> currentNodeUsers;
    //权限
    private List<ApproveAuthBean> auths;
    private List<String> optionList = new ArrayList<>();
    //流程字段版本
    private String processFieldV;
    private EmailDetailFragment emailDetailFragment;
    /**
     * 0未读 1已读
     */
    private String status;
    /**
     * 小助手进入 携带信息
     */
    private String appAssistant;
    /**
     * 小助手ID
     */
    private String appAssistantId;
    /**
     * 联动组件key
     */
    private List<String> linkData;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            moduleBean = intent.getStringExtra(ApproveConstants.MODULE_BEAN);
            approveDataId = intent.getStringExtra(ApproveConstants.APPROVAL_DATA_ID);
            taskKey = intent.getStringExtra(ApproveConstants.TASK_KEY);
            taskName = intent.getStringExtra(ApproveConstants.TASK_NAME);
            taskId = intent.getStringExtra(ApproveConstants.TASK_ID);
            processInstanceId = intent.getStringExtra(ApproveConstants.PROCESS_INSTANCE_ID);
            processFieldV = intent.getStringExtra(ApproveConstants.PROCESS_FIELD_V);
            type = intent.getIntExtra(ApproveConstants.APPROVE_TYPE, ApproveFragment.TAG1);
            status = intent.getStringExtra(ApproveConstants.APPROVAL_READ);
            appAssistantId = intent.getStringExtra(ApproveConstants.APPROVAL_APP_ASSISTANT_ID);
            appAssistant = intent.getStringExtra(ApproveConstants.APPROVAL_APP_ASSISTANT);
            dataId = intent.getStringExtra(Constants.DATA_ID);
        }
    }


    @Override
    public void init() {
        if (ApproveConstants.EMAIL_BEAN.equals(moduleBean)) {
            FragmentManager manager = getSupportFragmentManager();
            emailDetailFragment = new EmailDetailFragment();
            manager.beginTransaction().add(R.id.fl_detail, emailDetailFragment).commit();
        }
        if (ApproveFragment.TAG2 == type) {
            approvalRead(taskId);
        } else if (ApproveFragment.TAG4 == type) {
            approvalRead(processInstanceId);
        }
        loadData();
        CustomService service = (CustomService) Router.getInstance().getService(CustomService.class.getSimpleName());
        service.handleHidenFields(hashCode(), toString(), viewDelegate.mViewList);
    }


    /**
     * 加载数据
     */
    protected void loadData() {
        getDataDetail();
        getCustomLayout();
        getProcessWholeFlow();
        if (ApproveFragment.TAG2 == type && !ApproveConstants.EMAIL_BEAN.equals(moduleBean)) {
            getLinkageFields();
        }
    }

    /**
     * 标记已读，小助手进入不执行该方法
     *
     * @param id
     */
    private void approvalRead(String id) {
        if (status == null || ApproveConstants.READ.equals(status)) {
            return;
        }
        model.approvalRead(this, id, type + "", new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                EventBusUtils.sendEvent(new MessageBean(ApproveConstants.REFRESH, null, null));
            }
        });
    }


    /**
     * 获取联动字段
     */
    private void getLinkageFields() {
        new CommonModel().getLinkageFields(mContext, moduleBean, new ProgressSubscriber<LinkageFieldsResultBean>(mContext) {
            @Override
            public void onNext(LinkageFieldsResultBean linkageFieldsResultBean) {
                super.onNext(linkageFieldsResultBean);
                linkData = linkageFieldsResultBean.getData();
                setLinkage();
            }
        });
    }

    /**
     * 设置驱动
     */
    private void setLinkage() {
        if (linkData == null || CollectionUtils.isEmpty(viewDelegate.mViewList)) {
            return;
        }

        Observable.from(viewDelegate.mViewList).flatMap(new Func1<Object, Observable<BaseView>>() {
            @Override
            public Observable<BaseView> call(Object subfieldView) {
                return Observable.from(((SubfieldView) subfieldView).getViewList());
            }
        }).subscribe(baseView -> {
            if (baseView instanceof CommonSubFormsView) {
                Observable.from(((CommonSubFormsView) baseView).getViewList()).flatMap(new Func1<List<BaseView>, Observable<BaseView>>() {
                    @Override
                    public Observable<BaseView> call(List<BaseView> baseViews) {
                        return Observable.from(baseViews);
                    }
                }).subscribe(subView -> Observable.from(linkData).filter(link -> link.equals(subView.getKeyName())).subscribe(link -> subView.setLinkage()));
            } else {
                Observable.from(linkData).filter(link -> link.equals(baseView.getKeyName())).subscribe(link -> baseView.setLinkage());
            }
        });
    }

    /**
     * 获取审批流
     */
    private void getProcessWholeFlow() {
        Map<String, Object> map = new HashMap<>();
        map.put("processInstanceId", processInstanceId);
        map.put("dataId", approveDataId);
        map.put("moduleBean", moduleBean);
        model.getProcessWholeFlow(this, map, new ProgressSubscriber<ProcessFlowResponseBean>(this) {
            @Override
            public void onNext(ProcessFlowResponseBean baseBean) {
                super.onNext(baseBean);
                List<ProcessFlowResponseBean.DataBean> data = baseBean.getData();
                viewDelegate.setApproveTaskFlow(data);
            }
        });
    }

    /**
     * 资料
     */
    protected void getCustomLayout() {
        if (ApproveConstants.EMAIL_BEAN.equals(moduleBean)) {
            return;
        }
        Map<String, Object> map = new HashMap<>(5);
        map.put("bean", moduleBean);
        map.put("taskKey", taskKey);
        map.put("operationType", CustomConstants.DETAIL_STATE);
        map.put("dataId", approveDataId);
        map.put("processFieldV", processFieldV);
        model.getCustomLayout(this, map, new ProgressSubscriber<CustomLayoutResultBean>(this) {
            @Override
            public void onNext(CustomLayoutResultBean customLayoutResultBean) {
                super.onNext(customLayoutResultBean);
                customLayoutData = customLayoutResultBean.getData();
                handleDetailData();
            }
        });
    }

    /**
     * 得到详情数据
     */
    private void getDataDetail() {
        model.getDataDetail(this, approveDataId, moduleBean, taskKey, processFieldV, new ProgressSubscriber<DetailResultBean>(this) {
            @Override
            public void onNext(DetailResultBean detailResultBean) {
                super.onNext(detailResultBean);
                detailMap = detailResultBean.getData();
                handleDetailData();
                setLinkage();
            }
        });
    }

    /**
     * 处理详情数据
     */
    private synchronized void handleDetailData() {
        //非邮件审批 布局不能为null
        if (!ApproveConstants.EMAIL_BEAN.equals(moduleBean) && customLayoutData == null) {
            return;
        }
        //详情不能为空
        if (detailMap.size() == 0) {
            return;
        }
        //邮件审批
        if (ApproveConstants.EMAIL_BEAN.equals(moduleBean)) {
            emailDetailFragment.setData(detailMap.get("mailDetail"));
        } else {
            viewDelegate.drawLayout(customLayoutData, detailMap, type == 1 ? CustomConstants.APPROVE_DETAIL_STATE : CustomConstants.DETAIL_STATE, moduleBean);
        }
        processStatus = TextUtil.doubleParseInt(detailMap.get("process_status") + "");


        Object currentNodeUsersObject = detailMap.get("currentNodeUsers");
        currentNodeUsers = new JsonParser<String>().jsonFromList(currentNodeUsersObject, String.class);

        Object btnAuth = detailMap.get("btnAuth");
        auths = new JsonParser<ApproveAuthBean>().jsonFromList(btnAuth, ApproveAuthBean.class);
        setOptions(auths, type, processStatus);

        Object beginUserObject = detailMap.get("beginUser");
        ApproverBean beginUser = new JsonParser<ApproverBean>().jsonFromObject(beginUserObject, ApproverBean.class);
        viewDelegate.setBeginUser(beginUser, processStatus);

        Object ccToObject = detailMap.get("ccTo");
        List<Member> ccTo = new JsonParser<Member>().jsonFromList(ccToObject, Member.class);
        viewDelegate.setCCTo(ccTo);

    }


    /**
     * 设置操作按钮
     *
     * @param auths
     * @param type
     */

    public void setOptions(List<ApproveAuthBean> auths, int type, String processStatus) {
        optionList.clear();
        if (ApproveConstants.APPROVE_PASS.equals(processStatus)) {
            viewDelegate.setApproveTag(R.drawable.icon_approve_pass_tag);
        } else if (ApproveConstants.APPROVE_REJECT.equals(processStatus)) {
            viewDelegate.setApproveTag(R.drawable.icon_approve_reject_tag);
        } else {
            viewDelegate.setApproveTagVisible(View.GONE);
        }

        boolean isCCtoAuth = false;
        viewDelegate.setOptionVisibility(View.VISIBLE);
        switch (type) {
            case ApproveFragment.TAG1: {
                isCCtoAuth = ApproveUtils.checkAuth(auths, ApproveConstants.BEGIN_CCTO_AUTH);
                switch (processStatus) {
                    case ApproveConstants.APPROVE_PENDING:
                        boolean isRevokeAuth = ApproveUtils.checkAuth(auths, ApproveConstants.REVOKE_AUTH);
                        if (isRevokeAuth) {
                            viewDelegate.setOption2Text(ApproveConstants.REVOKE);
                        }
                        viewDelegate.setOption1Text(ApproveConstants.URGETO);
                        viewDelegate.setOption4Text(ApproveConstants.COMMENT);
                        break;
                    case ApproveConstants.APPROVING:
                        viewDelegate.setOption1Text(ApproveConstants.URGETO);
                        viewDelegate.setOption4Text(ApproveConstants.COMMENT);
                        break;
                    case ApproveConstants.APPROVE_PASS:
                        viewDelegate.setOption4Text(ApproveConstants.COMMENT);
                        break;
                    case ApproveConstants.APPROVE_REJECT:
                        viewDelegate.setOption4Text(ApproveConstants.COMMENT);
                        break;
                    case ApproveConstants.APPROVE_REVOKED:
                    case ApproveConstants.APPROVE_PEND_SUBMIT:
                        viewDelegate.setOptionVisibility(View.GONE);
                        optionList.add(ApproveConstants.EDIT);
                        optionList.add(ApproveConstants.DEL);
                        isCCtoAuth = false;
                        break;
                    default:
                        break;
                }
                break;
            }
            case ApproveFragment.TAG2: {
                isCCtoAuth = ApproveUtils.checkAuth(auths, ApproveConstants.APPROVER_CCTO_AUTH);
                boolean isTransferAuth = ApproveUtils.checkAuth(auths, ApproveConstants.TRANSFER_AUTH);
                if (isTransferAuth) {
                    viewDelegate.setOption3Text(ApproveConstants.TRANSFER);
                }

                viewDelegate.setOption1Text(ApproveConstants.PASS);
                viewDelegate.setOption2Text(ApproveConstants.REJECT);
                viewDelegate.setOption4Text(ApproveConstants.COMMENT);
                break;
            }
            case ApproveFragment.TAG3: {
                isCCtoAuth = ApproveUtils.checkAuth(auths, ApproveConstants.APPROVER_CCTO_AUTH);
                viewDelegate.setOption4Text(ApproveConstants.COMMENT);
                break;
            }
            case ApproveFragment.TAG4: {
                isCCtoAuth = ApproveUtils.checkAuth(auths, ApproveConstants.CCTO_CCTO_AUTH);
                viewDelegate.setOption4Text(ApproveConstants.COMMENT);
                break;
            }
            default:
                break;
        }
        if (isCCtoAuth) {
            optionList.add(ApproveConstants.CCTO);
        }

        if (CollectionUtils.isEmpty(optionList)) {
            viewDelegate.showMenu();
        } else {
            viewDelegate.setRightMenuIcons(R.drawable.icon_more);
            viewDelegate.showMenu(0);
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.tv_option1, R.id.tv_option2, R.id.tv_option3, R.id.tv_option4);
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_SUBFORM_LINKAGE_CODE, tag -> {
            if (!CollectionUtils.isEmpty(linkData)) {
                Observable.from(linkData).filter(link -> link.equals(((BaseView) tag).getKeyName())).subscribe(link -> ((BaseView) tag).setLinkage());
            }
        });
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_DATA_DETAIL_CODE, tag -> UIRouter.getInstance().openUri(mContext, "DDComp://custom/detail", (Bundle) tag));
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_FILE_DETAIL_CODE, tag -> UIRouter.getInstance().openUri(mContext, "DDComp://custom/file", (Bundle) tag));
        viewDelegate.membersView.setOnMemberSumClickedListener(() -> {
            Bundle bundle = new Bundle();
            List<Member> members = viewDelegate.membersView.getMembers();
            bundle.putSerializable(Constants.DATA_TAG1, (Serializable) members);
            CommonUtil.startActivtiy(mContext, ApproveCcActivity.class, bundle);
        });
    }

    @Override
    public void onClick(View v) {
        TextView tvOption = (TextView) v;
        String option = tvOption.getText().toString();
        switch (option) {
            case ApproveConstants.PASS:
                pass();
                break;
            case ApproveConstants.URGETO:
                urgeTo();
                break;
            case ApproveConstants.TRANSFER:
                transfer();
                break;
            case ApproveConstants.REJECT:
                reject();
                break;
            case ApproveConstants.REVOKE:
                revokeApprove();
                break;
            case ApproveConstants.COMMENT:
                comment();
                break;
            default:
                break;
        }
    }

    /**
     * 评论
     */
    private void comment() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, ApproveConstants.APPROVAL_MODULE_BEAN);
        bundle.putString(Constants.DATA_ID, dataId);
        bundle.putString(ApproveConstants.APPROVAL_DATA_ID, approveDataId);
        bundle.putString(ApproveConstants.APPROVAL_BEAN, moduleBean);
        bundle.putString(ApproveConstants.PROCESS_INSTANCE_ID, processInstanceId);

        CommonUtil.startActivtiy(this, CommentActivity.class, bundle);
    }

    /**
     * 驳回
     */
    private void reject() {
        Bundle bundle = new Bundle();
        bundle.putString(ApproveConstants.PROCESS_INSTANCE_ID, processInstanceId);
        bundle.putString(ApproveConstants.APPROVE_OPTION, ApproveConstants.REJECT);
        bundle.putString(ApproveConstants.MODULE_BEAN, moduleBean);
        bundle.putString(ApproveConstants.APPROVAL_DATA_ID, approveDataId);
        bundle.putString(ApproveConstants.TASK_KEY, taskKey);
        bundle.putString(ApproveConstants.TASK_NAME, taskName);
        bundle.putString(ApproveConstants.TASK_ID, taskId);
        bundle.putSerializable(Constants.DATA_TAG1, viewDelegate.getDetail());
        bundle.putString(ApproveConstants.APPROVAL_APP_ASSISTANT, appAssistant);
        bundle.putString(ApproveConstants.APPROVAL_APP_ASSISTANT_ID, appAssistantId);
        CommonUtil.startActivtiyForResult(this, OptionApproveActivity.class, REQUEST_REJECT_CODE, bundle);
    }

    /**
     * 转交
     */
    private void transfer() {
        Bundle bundle = new Bundle();
        bundle.putString(ApproveConstants.PROCESS_INSTANCE_ID, processInstanceId);
        bundle.putString(ApproveConstants.APPROVE_OPTION, ApproveConstants.TRANSFER);
        bundle.putString(ApproveConstants.MODULE_BEAN, moduleBean);
        bundle.putString(ApproveConstants.APPROVAL_DATA_ID, approveDataId);
        bundle.putString(ApproveConstants.TASK_KEY, taskKey);
        bundle.putString(ApproveConstants.TASK_NAME, taskName);
        bundle.putString(ApproveConstants.TASK_ID, taskId);
        if (!CollectionUtils.isEmpty(currentNodeUsers)) {
            bundle.putSerializable(ApproveConstants.CURRENT_NODE_USERS, (Serializable) currentNodeUsers);
        }
        bundle.putSerializable(Constants.DATA_TAG1, viewDelegate.getDetail());
        bundle.putString(ApproveConstants.APPROVAL_APP_ASSISTANT, appAssistant);
        bundle.putString(ApproveConstants.APPROVAL_APP_ASSISTANT_ID, appAssistantId);
        CommonUtil.startActivtiyForResult(this, OptionApproveActivity.class, REQUEST_TRANSFER_CODE, bundle);
    }

    /**
     * 催办
     */
    private void urgeTo() {
        Bundle bundle = new Bundle();
        bundle.putString(ApproveConstants.MODULE_BEAN, moduleBean);
        bundle.putString(ApproveConstants.APPROVAL_DATA_ID, approveDataId);
        bundle.putString(ApproveConstants.PROCESS_INSTANCE_ID, processInstanceId);
        bundle.putString(ApproveConstants.APPROVE_OPTION, ApproveConstants.URGETO);
        CommonUtil.startActivtiyForResult(this, OptionApproveActivity.class, REQUEST_URGETO_CODE, bundle);
    }

    /**
     * 通过
     */
    private void pass() {
        Bundle bundle = new Bundle();
        bundle.putString(ApproveConstants.PROCESS_INSTANCE_ID, processInstanceId);
        bundle.putString(ApproveConstants.APPROVE_OPTION, ApproveConstants.PASS);
        bundle.putString(ApproveConstants.MODULE_BEAN, moduleBean);
        bundle.putString(ApproveConstants.APPROVAL_DATA_ID, approveDataId);
        bundle.putString(ApproveConstants.TASK_KEY, taskKey);
        bundle.putString(ApproveConstants.TASK_NAME, taskName);
        bundle.putString(ApproveConstants.TASK_ID, taskId);
        bundle.putSerializable(Constants.DATA_TAG1, viewDelegate.getDetail());
        bundle.putString(ApproveConstants.APPROVAL_APP_ASSISTANT, appAssistant);
        bundle.putString(ApproveConstants.APPROVAL_APP_ASSISTANT_ID, appAssistantId);
        CommonUtil.startActivtiyForResult(this, OptionApproveActivity.class, REQUEST_PASS_CODE, bundle);
    }

    /**
     * 撤销审批
     */
    private void revokeApprove() {
        DialogUtils.getInstance().sureOrCancel(this, "提示", "撤销后，该审批将从审批人与抄送人处撤回，审批流程将会直接终止。你确认要撤销吗？", viewDelegate.getRootView(), () -> {
                    ApproveRevokeRequestBean bean = new ApproveRevokeRequestBean();
                    bean.setProcessInstanceId(processInstanceId);
                    bean.setTaskDefinitionKey(taskKey);
                    bean.setTaskDefinitionName(taskName);
                    bean.setCurrentTaskId(taskId);
                    bean.setDataId(approveDataId);
                    bean.setModuleBean(moduleBean);
                    model.approveRevoke(ApproveDetailActivity.this, bean, new ProgressSubscriber<BaseBean>(ApproveDetailActivity.this) {
                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);
                            ToastUtils.showSuccess(ApproveDetailActivity.this, "撤销成功");
                            EventBusUtils.sendEvent(new MessageBean(ApproveConstants.REFRESH, ApproveConstants.APPROVE_REVOKED, null));
                            finish();
                        }
                    });
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        optionsMenu();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 操作更多
     */
    private void optionsMenu() {
        if (CollectionUtils.isEmpty(optionList)) {
            return;
        }
        String[] options = new String[optionList.size()];
        optionList.toArray(options);

        PopUtils.showBottomMenu(this, viewDelegate.getRootView(), "操作", options, position -> {
            switch (options[position]) {
                case ApproveConstants.EDIT:
                    editApprove();
                    break;
                case ApproveConstants.DEL:
                    delApprove();
                    break;
                case ApproveConstants.CCTO:
                    ccToSelectMember();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /**
     * 编辑
     */
    private void editApprove() {
        Bundle bundle = new Bundle();
        if (ApproveConstants.EMAIL_BEAN.equals(moduleBean)) {
            bundle.putInt(Constants.DATA_TAG3, EmailConstant.EDIT_AGAIN);
            bundle.putSerializable(Constants.DATA_TAG5, moduleBean);
            UIRouter.getInstance().openUri(this, "DDComp://email/new_email", bundle, REQUEST_EDIT_CODE);
        } else {
            bundle.putString(Constants.MODULE_BEAN, moduleBean);
            bundle.putString(Constants.DATA_ID, approveDataId);
            bundle.putString(ApproveConstants.TASK_KEY, taskKey);
            bundle.putString(ApproveConstants.PROCESS_FIELD_V, processFieldV);
            bundle.putSerializable(Constants.DATA_TAG1, (Serializable) detailMap);
            if (ApproveConstants.APPROVE_REJECT.equals(processStatus) || ApproveConstants.APPROVE_REVOKED.equals(processStatus)) {
                bundle.putInt(ApproveConstants.OPERATION_TYPE, CustomConstants.APPROVE_AGAIN_STATE);
            }
            UIRouter.getInstance().openUri(this, "DDComp://custom/edit", bundle, REQUEST_EDIT_CODE);
        }
    }

    /**
     * 抄送选人
     */
    private void ccToSelectMember() {
        Bundle bundle = new Bundle();
        bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
        CommonUtil.startActivtiyForResult(this, SelectMemberActivity.class, Constants.REQUEST_SELECT_MEMBER, bundle);
    }

    /**
     * 删除审批
     */
    private void delApprove() {
        DialogUtils.getInstance().sureOrCancel(this, "提示", "删除后不可恢复，你确认要删除吗？", viewDelegate.getRootView(), () -> {
                    model.approveDel(ApproveDetailActivity.this, moduleBean, approveDataId, new ProgressSubscriber<BaseBean>(ApproveDetailActivity.this) {
                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);
                            ToastUtils.showSuccess(ApproveDetailActivity.this, "删除成功");
                            EventBusUtils.sendEvent(new MessageBean(ApproveConstants.REFRESH, ApproveConstants.APPROVE_DEL, null));
                            finish();
                        }
                    });
                }
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.REQUEST_SELECT_MEMBER) {
            List<Member> member = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (CollectionUtils.isEmpty(member)) {
                return;
            }
            DialogUtils.getInstance().sureOrCancel(this, "提示", "确定要将审批抄送给选中的成员吗？"
                    , viewDelegate.getRootView()
                    , () -> ccTo(member));
        } else if (requestCode == REQUEST_EDIT_CODE) {
            EventBusUtils.sendEvent(new MessageBean(ApproveConstants.REFRESH, null, null));
            finish();
        } else if (requestCode == REQUEST_REJECT_CODE) {
            EventBusUtils.sendEvent(new MessageBean(ApproveConstants.REFRESH, ApproveConstants.APPROVE_REJECT, null));
            finish();
        } else if (requestCode == REQUEST_PASS_CODE) {
            EventBusUtils.sendEvent(new MessageBean(ApproveConstants.REFRESH, ApproveConstants.APPROVE_PASS, null));
            finish();
        } else if (requestCode == REQUEST_TRANSFER_CODE) {
            EventBusUtils.sendEvent(new MessageBean(ApproveConstants.REFRESH, ApproveConstants.APPROVE_TRANSFER, null));
            finish();
        }
    }

    /**
     * 抄送
     */
    private void ccTo(List<Member> members) {
        if (CollectionUtils.isEmpty(members)) {
            ToastUtils.showToast(ApproveDetailActivity.this, "请选择抄送人");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Member member : members) {
            sb.append(member.getId() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);

        ApproveCopyRequestBean bean = new ApproveCopyRequestBean();
        bean.setProcessInstanceId(processInstanceId);
        bean.setTaskDefinitionKey(taskKey);
        bean.setTaskDefinitionId(taskId);
        bean.setBeanName(moduleBean);
        bean.setCcTo(sb.toString());
        bean.setDataId(approveDataId);
        model.approveCopy(this, bean, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(ApproveDetailActivity.this, "抄送成功");
                getDataDetail();
            }
        });
    }

    @Override
    public JSONObject getReferenceValue() {
        JSONObject json = new JSONObject();
        CustomService service = (CustomService) Router.getInstance().getService(CustomService.class.getSimpleName());
        service.putReference(viewDelegate.mViewList, json);
        return json;
    }
}
