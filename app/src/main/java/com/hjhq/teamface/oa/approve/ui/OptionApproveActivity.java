package com.hjhq.teamface.oa.approve.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectEmployeeActivity;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.oa.approve.bean.ApprovePassRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveRejectRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveTransferRequestBean;
import com.hjhq.teamface.oa.approve.bean.PassTypeResponseBean;
import com.hjhq.teamface.oa.approve.bean.RejectTypeResponseBean;
import com.hjhq.teamface.oa.approve.bean.RejectTypeResponseBean.DataBean.RejectTypeBean;
import com.hjhq.teamface.util.CommonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * 审批操作类
 * Created by Administrator on 2018/1/4.
 */

public class OptionApproveActivity extends ActivityPresenter<OptionApproveDelegate, ApproveModel> implements View.OnClickListener {
    public static final int REQUEST_SELECT_PASS_TYPE_CODE = 0x265;
    public static final int REQUEST_SELECT_REJECT_TYPE_CODE = 0x165;
    public static final int REQUEST_SELECT_REJECT_TASK_CODE = 0x166;
    //审批操作
    private String approveOption;

    //模块bean
    protected String moduleBean;
    //数据id
    protected String moduleDataId;
    //流程实例id
    protected String processInstanceId;
    //当前节点key
    private String taskKey;
    //当前节点ID
    private String taskId;
    //当前节点名称
    private String taskName;
    //审批通过信息
    private PassTypeResponseBean.DataBean passTypeBean;
    //审批驳回信息
    private RejectTypeResponseBean.DataBean rejectTypeBean;
    //驳回方式ID
    private String rejectTypeId;
    //驳回节点Key
    private String rejectTaskKey;
    /**
     * 当前节点审批人
     */
    private List<String> currentNodeUsers;

    //通过方式列表（手动定义的）
    ArrayList<RejectTypeBean> passTypeList = new ArrayList<>();
    private HashMap detail;
    //通过指定下一审批人选
    private ArrayList<Member> passOptionalMembers = new ArrayList<>();
    /**
     * 小助手进入 携带信息
     */
    private String appAssistant;
    /**
     * 小助手ID
     */
    private String appAssistantId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            moduleBean = intent.getStringExtra(ApproveConstants.MODULE_BEAN);
            moduleDataId = intent.getStringExtra(ApproveConstants.APPROVAL_DATA_ID);
            taskKey = intent.getStringExtra(ApproveConstants.TASK_KEY);
            taskName = intent.getStringExtra(ApproveConstants.TASK_NAME);
            taskId = intent.getStringExtra(ApproveConstants.TASK_ID);
            processInstanceId = intent.getStringExtra(ApproveConstants.PROCESS_INSTANCE_ID);
            approveOption = intent.getStringExtra(ApproveConstants.APPROVE_OPTION);
            currentNodeUsers = (List<String>) intent.getSerializableExtra(ApproveConstants.CURRENT_NODE_USERS);
            detail = (HashMap) intent.getSerializableExtra(Constants.DATA_TAG1);
            appAssistantId = intent.getStringExtra(ApproveConstants.APPROVAL_APP_ASSISTANT_ID);
            appAssistant = intent.getStringExtra(ApproveConstants.APPROVAL_APP_ASSISTANT);
        }
    }

    @Override
    public void init() {
        switch (approveOption) {
            case ApproveConstants.URGETO:
                viewDelegate.showUrd();
                break;
            case ApproveConstants.PASS:
                getPassType();
                viewDelegate.showPass();
                break;
            case ApproveConstants.TRANSFER:
                viewDelegate.showTransfer();
                break;
            case ApproveConstants.REJECT:
                getRejectType();
                viewDelegate.showReject();
                break;
            default:
                break;
        }
    }

    /**
     * 得到驳回方式
     */
    private void getRejectType() {
        model.getRejectType(this, moduleBean, processInstanceId, taskKey, new ProgressSubscriber<RejectTypeResponseBean>(this) {
            @Override
            public void onNext(RejectTypeResponseBean rejectTypeResponseBean) {
                super.onNext(rejectTypeResponseBean);
                rejectTypeBean = rejectTypeResponseBean.getData();
                List<RejectTypeResponseBean.DataBean.RejectTypeBean> rejectType = rejectTypeBean.getRejectType();
                if (CollectionUtils.isEmpty(rejectType)) {
                    ToastUtils.showError(OptionApproveActivity.this, "驳回方式获取失败!");
                    return;
                }
                int size = rejectType.size();

                if (size > 1) {
                    viewDelegate.setTopSelecterVisibility(View.VISIBLE);
                } else {
                    RejectTypeBean bean = rejectType.get(0);
                    rejectTypeId = bean.getId();
                    if ("2".equals(bean.getId())) {
                        //指定驳回节点
                        viewDelegate.setBottomSelecterVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * 得到通过方式
     */
    private void getPassType() {
        model.getPassType(this, moduleBean, processInstanceId, taskId, taskKey, new ProgressSubscriber<PassTypeResponseBean>(this) {
            @Override
            public void onNext(PassTypeResponseBean passTypeResponseBean) {
                super.onNext(passTypeResponseBean);
                passTypeBean = passTypeResponseBean.getData();
                String processType = passTypeBean.getProcessType();
                if (ApproveConstants.FREE_FLOW.equals(processType)) {
                    //自由流程
                    RejectTypeBean bean = new RejectTypeBean();
                    bean.setLabel("通过且结束");
                    bean.setId("0");
                    bean.setCheck(true);
                    passTypeList.add(bean);
                    RejectTypeBean bean2 = new RejectTypeBean();
                    bean2.setLabel("通过并转审");
                    bean2.setId("1");
                    passTypeList.add(bean2);

                    viewDelegate.setTopSelecterVisibility(View.VISIBLE);
                    viewDelegate.setTopContent(bean.getLabel());
                } else if (ApproveConstants.FIX_FLOW.equals(processType)) {
                    //固定流程
                    String approvalFlag = passTypeBean.getApprovalFlag();
                    //0不需要指定下一审批人 1需要指定下一审批人
                    if ("1".equals(approvalFlag)) {
                        viewDelegate.setAddApproverVisibility(View.VISIBLE);
                        List<PassTypeResponseBean.DataBean.EmployeeListBean> employeeList = passTypeBean.getEmployeeList();
                        if (!CollectionUtils.isEmpty(employeeList)) {
                            for (PassTypeResponseBean.DataBean.EmployeeListBean employee : employeeList) {
                                Member member = new Member(TextUtil.parseLong(employee.getId()), employee.getEmployee_name(), C.EMPLOYEE, employee.getPicture());
                                member.setPost_name(employee.getPost_name());
                                passOptionalMembers.add(member);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void bindEvenListener() {
        viewDelegate.setOnClickListener(this, R.id.ll_top_selecter, R.id.ll_bottom_selecter);
        viewDelegate.mMemberView.setOnAddMemberClickedListener(() -> {
            Bundle bundle = new Bundle();
            bundle.putInt(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
            if (ApproveConstants.PASS.equals(approveOption)) {
                //审批通过需要指定下一审批人
                if ("1".equals(passTypeBean.getApprovalFlag())) {
                    bundle.putSerializable(C.OPTIONAL_MEMBERS_TAG, passOptionalMembers);
                    CommonUtil.startActivtiyForResult(this, SelectEmployeeActivity.class, Constants.REQUEST_SELECT_MEMBER, bundle);
                    return;
                }
            }
            List<Member> members = viewDelegate.mMemberView.getMembers();
            //当前节点审批人不为空，选人时不能选
            if (!CollectionUtils.isEmpty(currentNodeUsers)) {
                for (String user : currentNodeUsers) {
                    Member member = new Member();
                    member.setId(TextUtil.parseLong(user));
                    member.setCheck(false);
                    member.setSelectState(C.HIDE_TO_SELECT);
                    members.add(member);
                }
            }
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, (Serializable) members);
            CommonUtil.startActivtiyForResult(this, SelectMemberActivity.class, Constants.REQUEST_SELECT_MEMBER, bundle);
        });
        super.bindEvenListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_top_selecter:
                topSelect();
                break;
            case R.id.ll_bottom_selecter:
                bottomSelect();
                break;
            default:
                break;
        }
    }

    /**
     * 底部选择
     */
    private void bottomSelect() {
        Bundle bundle = new Bundle();
        List<RejectTypeResponseBean.DataBean.HistoricTaskListBean> historicTaskList = rejectTypeBean.getHistoricTaskList();
        bundle.putSerializable(Constants.DATA_TAG1, (Serializable) historicTaskList);
        CommonUtil.startActivtiyForResult(this, SelectTaskPresenter.class, REQUEST_SELECT_REJECT_TASK_CODE, bundle);
    }

    /**
     * 顶部选择
     */
    private void topSelect() {
        Bundle bundle = new Bundle();
        switch (approveOption) {
            case ApproveConstants.PASS:
                //自由流程
                bundle.putSerializable(Constants.DATA_TAG1, passTypeList);
                bundle.putString(ApproveConstants.APPROVE_TYPE, ApproveConstants.PASS);
                CommonUtil.startActivtiyForResult(this, SelectTypePresenter.class, REQUEST_SELECT_PASS_TYPE_CODE, bundle);
                break;
            case ApproveConstants.REJECT:
                List<RejectTypeBean> rejectType = rejectTypeBean.getRejectType();
                bundle.putSerializable(Constants.DATA_TAG1, (Serializable) rejectType);
                bundle.putString(ApproveConstants.APPROVE_TYPE, ApproveConstants.REJECT);
                CommonUtil.startActivtiyForResult(this, SelectTypePresenter.class, REQUEST_SELECT_REJECT_TYPE_CODE, bundle);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (approveOption) {
            case ApproveConstants.URGETO:
                urgeTo();
                break;
            case ApproveConstants.PASS:
                pass();
                break;
            case ApproveConstants.TRANSFER:
                transfer();
                break;
            case ApproveConstants.REJECT:
                reject();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 驳回
     */
    private void reject() {
        String content = viewDelegate.getContent();
        if (content.length() > 200) {
            ToastUtils.showError(this, "审批意见不能超过200字");
            return;
        }
        ApproveRejectRequestBean bean = new ApproveRejectRequestBean();
        bean.setMessage(content);
        bean.setProcessInstanceId(processInstanceId);
        bean.setModuleBean(moduleBean);
        bean.setDataId(moduleDataId);
        bean.setTaskDefinitionName(taskName);
        bean.setTaskDefinitionKey(taskKey);
        bean.setCurrentTaskId(taskId);
        bean.setRejectType(rejectTypeId);
        bean.setRejectToTaskKey(rejectTaskKey);
        bean.setData(detail);
        bean.setAstDataId("1");
        bean.setImAprId(appAssistantId);
        bean.setParamFields(appAssistant);
        model.approveReject(this, bean, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(OptionApproveActivity.this, "驳回成功");
                OptionApproveActivity.this.setResult(RESULT_OK);
                finish();
            }
        });
    }

    /**
     * 转交
     */
    private void transfer() {
        String content = viewDelegate.getContent();
        if (content.length() > 50) {
            ToastUtils.showError(this, "转交理由不能超过50字");
            return;
        }
        List<Member> members = viewDelegate.mMemberView.getMembers();
        if (CollectionUtils.isEmpty(members)) {
            ToastUtils.showError(this, "审批人不能为空");
            return;
        }

        String nextAssignee = members.get(0).getId() + "";
        ApproveTransferRequestBean bean = new ApproveTransferRequestBean();
        bean.setApprover(nextAssignee);
        bean.setMessage(content);
        bean.setProcessInstanceId(processInstanceId);
        bean.setTaskDefinitionName(taskName);
        bean.setTaskDefinitionKey(taskKey);
        bean.setCurrentTaskId(taskId);
        bean.setModuleBean(moduleBean);
        bean.setDataId(moduleDataId);
        bean.setData(detail);
        bean.setAstDataId("1");
        bean.setImAprId(appAssistantId);
        bean.setParamFields(appAssistant);

        model.approveTransfer(this, bean, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(OptionApproveActivity.this, "转交成功");
                OptionApproveActivity.this.setResult(RESULT_OK);
                finish();
            }
        });
    }

    /**
     * 通过
     */
    private void pass() {
        String content = viewDelegate.getContent();
        if (content.length() > 200) {
            ToastUtils.showError(this, "审批意见不能超过200字");
            return;
        }
        List<Member> members = viewDelegate.mMemberView.getMembers();
        String nextAssignee = null;
        if (!CollectionUtils.isEmpty(members)) {
            nextAssignee = members.get(0).getId() + "";
        }
        //审批通过 不一定需要指定审批人
        if (nextAssignee == null && viewDelegate.getAddApproverVisibility() == View.VISIBLE) {
            ToastUtils.showError(this, "审批人不能为空");
            return;
        }

        ApprovePassRequestBean bean = new ApprovePassRequestBean();
        bean.setMessage(content);
        bean.setProcessInstanceId(processInstanceId);
        bean.setTaskDefinitionName(taskName);
        bean.setTaskDefinitionKey(taskKey);
        bean.setCurrentTaskId(taskId);
        bean.setDataId(moduleDataId);
        bean.setModuleBean(moduleBean);
        bean.setImAprId(appAssistantId);
        bean.setParamFields(appAssistant);
        bean.setNextAssignee(nextAssignee);
        bean.setAstDataId("1");
        bean.setData(detail);
        model.approvePass(this, bean, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(OptionApproveActivity.this, "通过成功");
                OptionApproveActivity.this.setResult(RESULT_OK);
                finish();
            }
        });
    }

    /**
     * 催办
     */
    private void urgeTo() {
        String content = viewDelegate.getContent();
        if (TextUtil.isEmpty(content)) {
            ToastUtils.showError(this, "请输入原因");
            return;
        }
        if (content.length() > 50) {
            ToastUtils.showError(this, "催办原因不能超过50字");
            return;
        }
        Map<String, String> map = new HashMap<>(4);
        map.put("processInstanceId", processInstanceId);
        map.put("message", content);
        map.put("dataId", moduleDataId);
        map.put("moduleBean", moduleBean);
        model.approveUrge(this, map, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(OptionApproveActivity.this, "催办成功");
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_SELECT_PASS_TYPE_CODE) {
            //通过
            passTypeList = (ArrayList<RejectTypeBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            Observable.from(passTypeList).filter(type -> type.isCheck()).subscribe(type -> {
                String id = type.getId();
                switch (id) {
                    case "0":
                        viewDelegate.setTopSelecterVisibility(View.VISIBLE);
                        viewDelegate.setAddApproverVisibility(View.GONE);
                        viewDelegate.setTopContent(type.getLabel());
                        break;
                    case "1":
                        viewDelegate.setTopSelecterVisibility(View.VISIBLE);
                        viewDelegate.setAddApproverVisibility(View.VISIBLE);
                        viewDelegate.setTopContent(type.getLabel());
                        break;
                    default:
                        break;
                }
            });
        } else if (requestCode == REQUEST_SELECT_REJECT_TYPE_CODE) {
            //驳回
            List<RejectTypeResponseBean.DataBean.RejectTypeBean> typeList = (List<RejectTypeBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            Observable.from(typeList).filter(type -> type.isCheck()).subscribe(type -> {
                rejectTypeId = type.getId();
                viewDelegate.setTopContent(type.getLabel());
                if ("2".equals(rejectTypeId)) {
                    viewDelegate.setBottomSelecterVisibility(View.VISIBLE);
                } else {
                    viewDelegate.setBottomSelecterVisibility(View.GONE);
                }
            });
        } else if (requestCode == REQUEST_SELECT_REJECT_TASK_CODE) {
            //驳回节点
            List<RejectTypeResponseBean.DataBean.HistoricTaskListBean> taskList = (List<RejectTypeResponseBean.DataBean.HistoricTaskListBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            Observable.from(taskList).filter(task -> task.isCheck()).subscribe(task -> {
                rejectTaskKey = task.getTaskKey();
                viewDelegate.setBottomContent(task.getTaskName());
            });
        } else if (requestCode == Constants.REQUEST_SELECT_MEMBER) {
            //选人
            ArrayList<Member> list = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (ApproveConstants.PASS.equals(approveOption)) {
                //审批通过需要指定下一审批人
                if ("1".equals(passTypeBean.getApprovalFlag())) {
                    for (Member passMember : passOptionalMembers) {
                        passMember.setCheck(false);
                        Observable.from(list)
                                .filter(Member::isCheck)
                                .filter(member -> member.getId() == passMember.getId())
                                .subscribe(member -> passMember.setCheck(true));
                    }
                    return;
                }
            }
            viewDelegate.mMemberView.setMembers(list);
        }
    }
}
