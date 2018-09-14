package com.hjhq.teamface.project.presenter.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.bean.ApproveListBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.bean.MemoListItemBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.member.AddMemberView;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.bean.TaskInfoBean;
import com.hjhq.teamface.common.bean.TaskListBean;
import com.hjhq.teamface.common.ui.comment.CommentActivity;
import com.hjhq.teamface.common.ui.dynamic.DynamicActivity;
import com.hjhq.teamface.common.ui.member.SelectRangeActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.TextWebView;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.TaskItemAdapter;
import com.hjhq.teamface.project.bean.AddRelevantBean;
import com.hjhq.teamface.project.bean.ProjectAddShareBean;
import com.hjhq.teamface.project.bean.ProjectMemberBean;
import com.hjhq.teamface.project.bean.ProjectMemberResultBean;
import com.hjhq.teamface.project.bean.ProjectShareDetailBean;
import com.hjhq.teamface.project.model.ProjectModel2;
import com.hjhq.teamface.project.presenter.ProjectShareMemberActivity;
import com.hjhq.teamface.project.presenter.SelectModuleActivity;
import com.hjhq.teamface.project.presenter.task.QuoteTaskActivity;
import com.hjhq.teamface.project.ui.add.ProjectAddShareDelegate;
import com.hjhq.teamface.project.util.TaskHelper;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目
 * Created by Administrator on 2018/4/10.
 */
@RouteNode(path = "/add_project_share", desc = "添加分享/编辑分享/分享详情")
public class ProjectAddShareActivity extends ActivityPresenter<ProjectAddShareDelegate, ProjectModel2> {
    //1新增2编辑3详情
    private int type = 0;
    private String mProjectID;
    private String mShareID;
    private AddMemberView mAddMemberView;
    private TextView mAddRelevant;
    private TextWebView mTextWebView;
    private ProjectShareDetailBean mBean;
    //private ProjectShareLikeMemberAdapter mLikeAdapter;
    private String[] menu;
    private String[] relevantMenu;
    private RecyclerView mRvRevelant;
    //private RecyclerView mRvLike;
    private String praiseStatus = "0";
    private boolean webviewReady = false;
    private boolean dataReady = false;
    List<TaskInfoBean> relevantDataList = new ArrayList<>();
    TaskItemAdapter listAdapter = new TaskItemAdapter(relevantDataList, false);
    ArrayList<Member> mProjectMembers = new ArrayList<Member>();


    @Override
    public void init() {
        mShareID = getIntent().getStringExtra(Constants.DATA_TAG1);
        type = getIntent().getIntExtra(Constants.DATA_TAG2, 0);
        mProjectID = getIntent().getStringExtra(Constants.DATA_TAG3);
        if (type == 0) {
            ToastUtils.showError(mContext, "数据错误");
            finish();
            return;
        }
        mAddMemberView = viewDelegate.get(R.id.member);
        mRvRevelant = viewDelegate.get(R.id.rv_revelant);
        mTextWebView = viewDelegate.get(R.id.rich_text);

        // mRvLike = viewDelegate.get(R.id.rv_like);
        mAddRelevant = viewDelegate.get(R.id.tv_scope_content);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 6);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        // mRvLike.setLayoutManager(gridLayoutManager);
        mRvRevelant.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        // mLikeAdapter = new ProjectShareLikeMemberAdapter(new ArrayList<>());
        mRvRevelant.setAdapter(listAdapter);
        // mRvLike.setAdapter(mLikeAdapter);
        viewDelegate.setRightMenuTexts(R.color.app_blue, "确定", "完成");
        viewDelegate.setRightMenuIcons(R.drawable.icon_more);
        switch (type) {
            case ProjectConstants.TYPE_ADD_SHARE:
                viewDelegate.setTitle("添加分享");
                viewDelegate.showMenu(1);
                viewDelegate.showEditText();
                viewDelegate.hideLike();
                viewDelegate.hideActionBtn();
                mAddMemberView.setAddMemberIvVisibility(View.VISIBLE);
                mTextWebView.loadUrl(EmailConstant.EDIT, Constants.EMAIL_EDIT_URL);
                dataReady = true;
                viewDelegate.get(R.id.rl_relevant).setVisibility(View.GONE);
                mRvRevelant.setVisibility(View.GONE);
                break;
            case ProjectConstants.TYPE_EDIT_SHARE:
                viewDelegate.setTitle("编辑分享");
                viewDelegate.showMenu(1);
                viewDelegate.showEditText();
                viewDelegate.hideLike();
                viewDelegate.hideActionBtn();
                mTextWebView.loadUrl(EmailConstant.EDIT, Constants.EMAIL_EDIT_URL);
                mBean = (ProjectShareDetailBean) getIntent().getSerializableExtra(Constants.DATA_TAG4);
                showData(mBean);
                dataReady = true;
                viewDelegate.get(R.id.rl_relevant).setVisibility(View.GONE);
                mRvRevelant.setVisibility(View.GONE);
                mAddMemberView.setAddMemberIvVisibility(View.VISIBLE);
                break;
            case ProjectConstants.TYPE_SHARE_DETAIL:
                viewDelegate.setTitle("分享详情");
                viewDelegate.showMenu(2);
                viewDelegate.hideEditText();
                mAddMemberView.setAddMemberIvVisibility(View.GONE);
                viewDelegate.showLike();
                viewDelegate.showActionBtn();
                mTextWebView.loadUrl(EmailConstant.DETAIL, Constants.EMAIL_DETAIL_URL);
                viewDelegate.get(R.id.rl_relevant).setVisibility(View.VISIBLE);
                mRvRevelant.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        initData();


    }

    /**
     * 初始化数据
     */
    private void initData() {
        relevantMenu = getResources().getStringArray(R.array.project_share_relevant_menu);
        menu = getResources().getStringArray(R.array.project_share_detail_menu_array_without_auth);
        switch (type) {
            case 1:
                //新增
                break;
            case 2:
                //编辑
                break;
            case 3:
                //详情
                getNetData();
                initActionBtn();
                break;
            default:
                break;
        }
    }

    /**
     * 底部操作按钮
     */
    private void initActionBtn() {
        View view = View.inflate(mContext, R.layout.project_share_actionbar_in_detail, null);
        view.findViewById(R.id.rl_action1).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PROJECT_SHARE_BEAN_NAME);
            bundle.putString(Constants.DATA_ID, mShareID);
            CommonUtil.startActivtiyForResult(mContext, CommentActivity.class, Constants.REQUEST_CODE1, bundle);
        });
        view.findViewById(R.id.rl_action2).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PROJECT_SHARE_BEAN_NAME);
            bundle.putString(Constants.DATA_ID, mShareID);
            CommonUtil.startActivtiyForResult(mContext, DynamicActivity.class, Constants.REQUEST_CODE2, bundle);
        });
        viewDelegate.setActionView(view);

    }

    /**
     * 获取分享详情
     */
    private void getNetData() {
        //获取详情数据
        getDetailData();
        //获取关联数据
        getRelevantData();
    }

    private void getDetailData() {
        model.getProjectShareDetail(mContext, mShareID, new ProgressSubscriber<ProjectShareDetailBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(ProjectShareDetailBean bean) {
                super.onNext(bean);
                mBean = bean;
                dataReady = true;
                showData(bean);
            }
        });
    }

    private void getRelevantData() {
        model.queryRelationList(mContext, mShareID, new ProgressSubscriber<TaskListBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(TaskListBean bean) {
                super.onNext(bean);
                showRelevantData(bean);
            }
        });
    }

    /**
     * 显示关联数据
     *
     * @param bean
     */
    private void showRelevantData(TaskListBean bean) {
        relevantDataList.clear();
        relevantDataList.addAll(bean.getData().getDataList());
        listAdapter.notifyDataSetChanged();
    }

    private void showData(ProjectShareDetailBean bean) {
        //标题
        viewDelegate.setShareTitle(bean.getData().getShare_title());
        //内容
        if (webviewReady) {
            mTextWebView.setWebText(bean.getData().getShare_content());
        }
        //分享人
        ArrayList<ProjectMemberBean> shareObj = bean.getData().getShareObj();
        if (shareObj != null) {
            List<Member> list = new ArrayList<>();
            for (int i = 0; i < shareObj.size(); i++) {
                Member m = new Member();
                m.setId(TextUtil.parseLong(shareObj.get(i).getId()));
                m.setName(shareObj.get(i).getEmployee_name());
                m.setEmployee_name(shareObj.get(i).getEmployee_name());
                m.setPicture(shareObj.get(i).getPicture());
                m.setPost_name(shareObj.get(i).getDuty_name());
                list.add(m);
            }
            mAddMemberView.setMembers(list);
            if (type == 3) {
                mAddMemberView.setAddMemberIvVisibility(View.GONE);
            }
        }
        //点赞人列表
       /* ArrayList<ProjectMemberBean> praiseObj = bean.getData().getPraiseObj();
        if (praiseObj != null) {
            mLikeAdapter.getData().clearWay();
            mLikeAdapter.getData().addAll(praiseObj);
            mLikeAdapter.notifyDataSetChanged();
        }*/
        //点赞状态
        praiseStatus = bean.getData().getShare_praise_status();

        viewDelegate.setPraiseIcon("1".equals(praiseStatus));

        //点赞人数
        viewDelegate.setPraiseNum(bean.getData().getShare_praise_number());
        //viewDelegate.setPraiseNum(bean.getData().getPraiseObj().size() + "");
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        mAddMemberView.setOnAddMemberClickedListener(() -> {
            switch (type) {
                case 1:
                case 2:
                    chooseMember();
                    break;
                case 3:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.DATA_TAG1, ((ArrayList<Member>) mAddMemberView.getMembers()));
                    bundle.putString(Constants.DATA_TAG2, "分享人员");
                    CommonUtil.startActivtiyForResult(mContext, ProjectShareMemberActivity.class, Constants.REQUEST_CODE1, bundle);
                    break;
                default:
                    break;
            }
        });
        mTextWebView.setOnStateChanListener(new TextWebView.OnStateChangeListener() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webviewReady = true;
                if (dataReady) {
                    if (type == 1) {
                        mTextWebView.setWebText("");
                    } else {
                        mTextWebView.setWebText(mBean.getData().getShare_content());
                    }
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                webviewReady = true;
            }
        });
        //点赞
        viewDelegate.get(R.id.rl_like_action).setOnClickListener(v -> {

            model.sharePraise(mContext, mShareID, "1".equals(praiseStatus) ? "0" : "1", new ProgressSubscriber<BaseBean>(mContext) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    getDetailData();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        });
        //查看点赞人
        viewDelegate.get(R.id.rl_like).setOnClickListener(v -> {
            if (mBean != null) {
                ArrayList<ProjectMemberBean> praiseObj = mBean.getData().getPraiseObj();
                ArrayList<Member> memberList = new ArrayList<Member>();
                if (praiseObj != null) {
                    for (int i = 0; i < praiseObj.size(); i++) {
                        ProjectMemberBean projectMemberBean = praiseObj.get(i);
                        Member member = new Member();
                        member.setName(projectMemberBean.getName());
                        member.setEmployee_name(projectMemberBean.getName());
                        member.setPicture(projectMemberBean.getPicture());
                        member.setId(TextUtil.parseLong(projectMemberBean.getId()));
                        //member.setPost_name(projectMemberBean.getP());
                        memberList.add(member);
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.DATA_TAG1, memberList);
                bundle.putString(Constants.DATA_TAG2, "点赞人员");
                CommonUtil.startActivtiyForResult(mContext, ProjectShareMemberActivity.class, Constants.REQUEST_CODE1, bundle);
            } else {
                ToastUtils.showError(mContext, "数据错误");
            }


        });

        //添加关联
        mAddRelevant.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DATA_TAG1, Constants.SELECT_TYPE);
            CommonUtil.startActivtiyForResult(mContext, SelectModuleActivity.class, ProjectConstants.QUOTE_TASK_REQUEST_CODE, bundle);
        });
        listAdapter.setOnItemClickListener(new TaskItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DragItemAdapter adapter, View view, int position) {
                viewDataDetail(position);
            }

            @Override
            public void onItemChildClick(DragItemAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemLongClick(DragItemAdapter adapter, View view, int position) {
                cancelRelevant(position);
            }
        });


    }

    /**
     * 查看关联数据详情
     *
     * @param position
     */
    private void viewDataDetail(int position) {
        TaskInfoBean infoBean = relevantDataList.get(position);
        TaskHelper.INSTANCE.clickTaskItem(mContext, infoBean);
    }

    /**
     * 取消关联
     *
     * @param position
     */
    private void cancelRelevant(final int position) {
        DialogUtils.getInstance().sureOrCancel(mContext, "", "确认取消该关联吗?", viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
            @Override
            public void clickSure() {
                model.cancleRelation(mContext,
                        relevantDataList.get(position).getTaskInfoId() + "",
                        new ProgressSubscriber<BaseBean>(mContext) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                super.onNext(baseBean);
                                getRelevantData();
                            }
                        });
            }
        });
    }

    /**
     * 选择分享人
     */
    private void chooseMember() {
        if (mProjectMembers.size() <= 0) {
            model.queryProjectMember(mContext, TextUtil.parseLong(mProjectID), new ProgressSubscriber<ProjectMemberResultBean>(mContext, true) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }

                @Override
                public void onNext(ProjectMemberResultBean projectMemberResultBean) {
                    super.onNext(projectMemberResultBean);
                    List<ProjectMemberResultBean.DataBean.DataListBean> data = projectMemberResultBean.getData().getDataList();
                    mProjectMembers.clear();
                    for (int i = 0; i < data.size(); i++) {
                        ProjectMemberResultBean.DataBean.DataListBean bean = data.get(i);
                        Member m = new Member();
                        m.setId(bean.getEmployee_id());
                        m.setName(bean.getEmployee_name());
                        m.setEmployee_name(bean.getEmployee_name());
                        m.setPicture(bean.getEmployee_pic());
                        m.setPost_name(bean.getProject_role());
                        mProjectMembers.add(m);
                    }
                    addShareMember();
                }
            });
        } else {
            addShareMember();
        }
    }

    /**
     * 添加项目内的分享人
     */
    private void addShareMember() {
        //分享人
        Bundle bundle = new Bundle();
        ArrayList<Member> list = (ArrayList<Member>) mAddMemberView.getMembers();
        for (Member member : list) {
            member.setCheck(true);
        }
        bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
        bundle.putSerializable(C.CHOOSE_RANGE_TAG, mProjectMembers);
        bundle.putSerializable(C.SELECTED_MEMBER_TAG, list);
        CommonUtil.startActivtiyForResult(mContext,
                SelectRangeActivity.class, Constants.REQUEST_CODE4, bundle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (type) {
            case 1:
                addShare();
                break;
            case 2:
                saveShare();
                break;
            case 3:
                showMenu();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 菜单
     */
    private void showMenu() {

        if (mBean != null && !TextUtils.isEmpty(mBean.getData().getCreate_by()) && mBean.getData().getCreate_by().equals(SPHelper.getEmployeeId())) {
            menu = getResources().getStringArray(R.array.project_share_detail_menu_array);
        }
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), "", menu, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                if (menu.length == 1) {
                    putOnTop();
                    return true;
                }

                switch (p) {
                    case 0:
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.DATA_TAG1, mShareID);
                        bundle.putInt(Constants.DATA_TAG2, ProjectConstants.TYPE_EDIT_SHARE);
                        bundle.putString(Constants.DATA_TAG3, mProjectID);
                        bundle.putSerializable(Constants.DATA_TAG4, mBean);
                        CommonUtil.startActivtiyForResult(mContext, ProjectAddShareActivity.class, Constants.REQUEST_CODE3, bundle);
                        break;
                    case 1:
                        //置顶
                        putOnTop();
                        break;
                    case 2:
                        // 删除
                        DialogUtils.getInstance().sureOrCancel(mContext, "", "确定要删除该分享吗?", viewDelegate.getRootView(), new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                deleteShare();
                            }
                        });

                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 删除分享
     */
    private void deleteShare() {
        model.deleteProjectShare(mContext, mShareID, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    /**
     * 置顶
     */
    private void putOnTop() {
        model.editShareStickStatus(mContext, mShareID, "1", new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                setResult(RESULT_OK);
            }
        });
    }

    /**
     * 保存编辑后的分享
     */
    private void saveShare() {
        ProjectAddShareBean bean = new ProjectAddShareBean();
        bean.setId(TextUtil.parseLong(mShareID));
        if (TextUtils.isEmpty(mProjectID)) {
            return;
        }
        String title = viewDelegate.getTitle();
        if (TextUtils.isEmpty(title)) {
            ToastUtils.showToast(mContext, "标题不能为空");
            return;
        }
        bean.setTitle(title);
        List<Member> members = mAddMemberView.getMembers();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < members.size(); i++) {
            if (TextUtils.isEmpty(sb.toString())) {
                sb.append(members.get(i).getId() + "");
            } else {
                sb.append("," + members.get(i).getId());
            }
        }
        bean.setShareIds(sb.toString());
        bean.setShareStatus(1);
        bean.setSubmitStatus(1);
        mTextWebView.getWebText(new TextWebView.TextWebInterface() {
            @Override
            public void getWebText(String text) {
                if (TextUtil.isEmpty(text)) {
                    ToastUtils.showToast(mContext, "内容不能为空");
                    return;
                } else {
                    bean.setContent(text);
                    model.editProjectShare(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            ToastUtils.showError(mContext, "操作失败");
                        }

                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);
                            ToastUtils.showSuccess(mContext, "操作成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }
            }
        });

    }

    /**
     * 添加分享
     */
    private void addShare() {

        ProjectAddShareBean bean = new ProjectAddShareBean();

        bean.setId(TextUtil.parseLong(mProjectID));
        String title = viewDelegate.getTitle();
        if (TextUtils.isEmpty(title)) {
            ToastUtils.showToast(mContext, "标题不能为空");
            return;
        }

        bean.setTitle(title);
        List<Member> members = mAddMemberView.getMembers();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < members.size(); i++) {
            if (TextUtils.isEmpty(sb.toString())) {
                sb.append(members.get(i).getId() + "");
            } else {
                sb.append("," + members.get(i).getId());
            }
        }
        bean.setShareIds(sb.toString());
        bean.setShareStatus(1);
        bean.setSubmitStatus(1);
        mTextWebView.getWebText(new TextWebView.TextWebInterface() {
            @Override
            public void getWebText(String text) {
                if (TextUtils.isEmpty(text)) {
                    ToastUtils.showToast(mContext, "内容不能为空");
                    return;
                } else {
                    bean.setContent(text);
                    model.addProjectShare(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            ToastUtils.showError(mContext, "操作失败");
                        }

                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            ToastUtils.showSuccess(mContext, "添加成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE1:
                    // TODO: 2018/5/15 更新评论数
                    break;
                case Constants.REQUEST_CODE2:
                    //动态
                    break;
                case Constants.REQUEST_CODE3:
                    //编辑保存成功
                    getDetailData();
                    break;
                case Constants.REQUEST_CODE4:
                    //分享人
                    if (data != null) {
                        ArrayList<Member> list = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                        if (list != null) {
                            mAddMemberView.setMembers(list);
                        } else {
                            mAddMemberView.setMembers(new ArrayList<>());
                        }
                    }
                    break;
                default:

                    break;


            }
            if (requestCode == ProjectConstants.QUOTE_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                //引用
                AppModuleBean appModeluBean = (AppModuleBean) data.getSerializableExtra(Constants.DATA_TAG1);
                String moduleBean = appModeluBean.getEnglish_name();
                String moduleChineseName = appModeluBean.getChinese_name();
                String moduleId = appModeluBean.getId();

                Bundle bundle = new Bundle();
                switch (moduleBean) {
                    case ProjectConstants.TASK_MODULE_BEAN:
                        //任务
                        /*bundle.putLong(ProjectConstants.PROJECT_ID, TextUtil.parseLong(mProjectID));
                        bundle.putString(Constants.DATA_TAG1, moduleBean);
                        bundle.putString(Constants.DATA_TAG2, moduleId);*/
                        CommonUtil.startActivtiyForResult(mContext, QuoteTaskActivity.class, ProjectConstants.QUOTE_TASK_TASK_REQUEST_CODE, bundle);
                        break;
                    case MemoConstant.BEAN_NAME:
                        UIRouter.getInstance().openUri(mContext, "DDComp://memo/choose_memo", bundle, ProjectConstants.QUOTE_TASK_MEMO_REQUEST_CODE);
                        break;
                    default:
                        if (ApproveConstants.APPROVAL_MODULE_BEAN.equals(appModeluBean.getApplication_id())) {
                            //审批下面的模块
                            bundle.putString(Constants.DATA_TAG1, moduleBean);
                            bundle.putString(Constants.DATA_TAG2, moduleChineseName);
                            bundle.putString(Constants.DATA_TAG3, moduleId);
                            UIRouter.getInstance().openUri(mContext, "DDComp://app/approve/select", bundle, ProjectConstants.QUOTE_TASK_APPROVE_REQUEST_CODE);
                        } else {
                            //自定义模块
                            bundle.putString(Constants.MODULE_BEAN, moduleBean);
                            bundle.putString(Constants.MODULE_ID, moduleId);
                            bundle.putString(Constants.NAME, appModeluBean.getChinese_name());
                            UIRouter.getInstance().openUri(mContext, "DDComp://custom/select", bundle, ProjectConstants.QUOTE_TASK_CUSTOM_REQUEST_CODE);
                        }
                        break;
                }
            } else if (requestCode == ProjectConstants.QUOTE_TASK_MEMO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                //引用备忘录
                ArrayList<MemoListItemBean> choosedItem = (ArrayList<MemoListItemBean>) data.getSerializableExtra(Constants.DATA_TAG1);
                if (CollectionUtils.isEmpty(choosedItem)) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (MemoListItemBean memo : choosedItem) {
                    sb.append("," + memo.getId());
                }
                sb.deleteCharAt(0);
                AddRelevantBean bean = new AddRelevantBean();
                bean.setProjectId(TextUtil.parseLong(mProjectID));
                bean.setRelation_id(sb.toString());
                bean.setBean_name(MemoConstant.BEAN_NAME);
                bean.setModule_id(1L);
                bean.setModule_name("");
                bean.setBean_type(1L);
                bean.setShare_id(TextUtil.parseLong(mShareID));
                addRelevantData(bean);


            } else if (requestCode == ProjectConstants.QUOTE_TASK_APPROVE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                //引用审批
                ArrayList<ApproveListBean> datas = (ArrayList<ApproveListBean>) data.getSerializableExtra(Constants.DATA_TAG1);
                //模块中文名
                String moduleChineseName = data.getStringExtra(Constants.DATA_TAG2);
                String moduleId = data.getStringExtra(Constants.DATA_TAG3);
                String moduleName = data.getStringExtra(Constants.DATA_TAG4);
                if (CollectionUtils.isEmpty(datas)) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (ApproveListBean approve : datas) {
                    sb.append("," + approve.getApproval_data_id());
                }
                sb.deleteCharAt(0);
                AddRelevantBean bean = new AddRelevantBean();
                bean.setProjectId(TextUtil.parseLong(mProjectID));
                bean.setRelation_id(sb.toString());
                //模块的bean_name
                bean.setBean_name(moduleName);
                //模块的id
                bean.setModule_id(TextUtil.parseLong(moduleId));
                bean.setModule_name(moduleChineseName);
                bean.setBean_type(4L);
                bean.setShare_id(TextUtil.parseLong(mShareID));
                addRelevantData(bean);

            } else if (requestCode == ProjectConstants.QUOTE_TASK_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                //引用任务
                if (data != null) {
                    String ids = data.getStringExtra(Constants.DATA_TAG1);
                    String moduleBean = data.getStringExtra(Constants.DATA_TAG2);
                    int beanType = data.getIntExtra(Constants.DATA_TAG3, 0);
                    AddRelevantBean bean = new AddRelevantBean();
                    bean.setProjectId(TextUtil.parseLong(mProjectID));
                    bean.setRelation_id(ids);
                    bean.setBean_name(moduleBean);
                    bean.setModule_id(0L);
                    bean.setModule_name("任务");
                    //2项目任务,5个人任务
                    if (beanType == 0) {
                        bean.setBean_type(2L);
                    } else if (beanType == 1) {
                        bean.setBean_type(5L);
                    }
                    bean.setShare_id(TextUtil.parseLong(mShareID));
                    addRelevantData(bean);
                }
            } else if (requestCode == ProjectConstants.QUOTE_TASK_CUSTOM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                //引用自定义
                String moduleBean = data.getStringExtra(Constants.MODULE_BEAN);
                String moduleId = data.getStringExtra(Constants.MODULE_ID);
                String moduleName = data.getStringExtra(Constants.NAME);
                ArrayList<DataTempResultBean.DataBean.DataListBean> datas = (ArrayList<DataTempResultBean.DataBean.DataListBean>) data.getSerializableExtra(Constants.DATA_TAG1);
                if (CollectionUtils.isEmpty(datas)) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (DataTempResultBean.DataBean.DataListBean dataListBean : datas) {
                    sb.append("," + dataListBean.getId().getValue());
                }
                sb.deleteCharAt(0);
                AddRelevantBean bean = new AddRelevantBean();
                bean.setProjectId(TextUtil.parseLong(mProjectID));
                bean.setRelation_id(sb.toString());
                bean.setBean_name(moduleBean);
                bean.setModule_id(TextUtil.parseLong(moduleId));
                bean.setModule_name(moduleName);
                bean.setBean_type(3L);
                bean.setShare_id(TextUtil.parseLong(mShareID));
                addRelevantData(bean);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 新增关联数据
     *
     * @param bean
     */
    private void addRelevantData(AddRelevantBean bean) {
        model.saveRelation(mContext, bean, new ProgressSubscriber<BaseBean>(mContext, true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                //刷新关联列表
                getRelevantData();
            }
        });
    }
}
