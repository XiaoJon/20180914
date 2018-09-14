package com.hjhq.teamface.memo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.bean.ModuleItemBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.ui.comment.CommentActivity;
import com.hjhq.teamface.common.ui.location.ViewAddressPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.adapter.MemoItemAdapter;
import com.hjhq.teamface.memo.adapter.MemoLocationAdapter;
import com.hjhq.teamface.memo.adapter.MemoMemberAdapter;
import com.hjhq.teamface.memo.adapter.MemoRelevanceAdapter;
import com.hjhq.teamface.memo.bean.MemoBean;
import com.hjhq.teamface.memo.bean.MemoContentBean;
import com.hjhq.teamface.memo.bean.MemoCreatorBean;
import com.hjhq.teamface.memo.bean.MemoDetailBean;
import com.hjhq.teamface.memo.bean.MemoLocationBean;
import com.hjhq.teamface.memo.bean.NewMemoBean;
import com.hjhq.teamface.memo.ui.AddMemoActivity;
import com.hjhq.teamface.memo.ui.MemoDetailActivity;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 * Describe：
 */

public class MemoDetailDelegate extends AppDelegate {
    private FrameLayout flContainer;
    private RecyclerView mRvText;
    private RecyclerView mRvLocation;
    private RecyclerView mRvMember;
    private RecyclerView mRvRelevent;
    private MemoItemAdapter mMemoItemAdapter;
    private MemoLocationAdapter mLocationAdapter;
    private MemoMemberAdapter mMemoMemberAdapter;
    private MemoRelevanceAdapter mRelevanceAdapter;
    private List<MemoBean> memoData = new ArrayList<>();
    private RelativeLayout mRlCreatorInfo;
    private RelativeLayout mRlTextContent;
    private RelativeLayout mRlRemind;
    private RelativeLayout mRlMember;
    private RelativeLayout mRlRelevant;
    private RelativeLayout mRlLocation;
    private RelativeLayout mRlRoot;
    private FrameLayout mFlError;
    private TextView mTvRemind;
    private ImageView mIvRemoveRemind;
    private ImageView mIvAvatar;
    private TextView mTvName;
    private TextView mTvPosition;
    private TextView mTvDate;
    private TextView mTvCommentNum;
    private View mActionView;
    private MemoBean.RemindBean mRemindBean = new MemoBean.RemindBean();
    private MemoDetailBean mDetailBean;

    @Override
    public int getRootLayoutId() {
        return R.layout.memo_activity_detail2;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("备忘录");
        flContainer = get(R.id.fl_bar);
        flContainer.setVisibility(View.GONE);
//        mRemindBean.setTime(0L);
//        mRemindBean.setMode(MemoConstant.REMIND_MODE_SELF);
        //顶部信息
        mIvAvatar = get(R.id.avatar);
        mTvName = get(R.id.tv_name);
        mTvPosition = get(R.id.tv_position);
        mTvDate = get(R.id.tv_create_time);

        mRvText = get(R.id.rv_text);
        mRvLocation = get(R.id.rv_location);
        mRlRelevant = get(R.id.rl_relevant);
        mRvRelevent = get(R.id.rv_relevant);
        mRvMember = get(R.id.rv_member);
        mRlRemind = get(R.id.rl_remind);
        mRlCreatorInfo = get(R.id.rl_head);
        mRlTextContent = get(R.id.rl_text);
        mRlRoot = get(R.id.rl_root);
        mFlError = get(R.id.fl_error);
        mRlLocation = get(R.id.rl_location);
        mTvRemind = get(R.id.tv_time_content);
        mIvRemoveRemind = get(R.id.iv_remove_remind);
        mRlMember = get(R.id.rl_member);
        mIvRemoveRemind.setVisibility(View.GONE);
        ((SimpleItemAnimator) mRvText.getItemAnimator()).setSupportsChangeAnimations(false);

        mRvText.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRvLocation.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRvRelevent.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mMemoItemAdapter = new MemoItemAdapter(false, null);
        mLocationAdapter = new MemoLocationAdapter(0, null);
        mMemoMemberAdapter = new MemoMemberAdapter(0, null);
        mRelevanceAdapter = new MemoRelevanceAdapter(0, null);


        mRvLocation.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 6) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        mRvMember.setLayoutManager(gridLayoutManager);
        mRvLocation.setAdapter(mLocationAdapter);
        mRvMember.setAdapter(mMemoMemberAdapter);
        mRvText.setAdapter(mMemoItemAdapter);
        mRvRelevent.setAdapter(mRelevanceAdapter);
        mRvText.getItemAnimator().setChangeDuration(0);
        for (int i = 0; i < 1; i++) {
            MemoBean bean = new MemoBean();
            bean.setType(MemoConstant.ITEM_TEXT);
            MemoBean.TextBean tb = new MemoBean.TextBean();
            tb.setCheck(0);
            tb.setContent("");
            tb.setNum(0);
            bean.setText(tb);
            memoData.add(bean);
        }
        mMemoItemAdapter.notifyDataSetChanged();
        /*mRlRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Calendar calendar = Calendar.getInstance();
                if (mRemindBean.getTime() == 0L) {
                    calendar.setTimeInMillis(System.currentTimeMillis());
                } else {
                    calendar.setTimeInMillis(mRemindBean.getTime());
                }
                bundle.putSerializable(DateTimeSelectPresenter.CALENDAR, calendar);
                bundle.putInt(Constants.DATA_TAG1, mRemindBean.getMode());
                CommonUtil.startActivtiyForResult(getActivity(),
                        MemoRemindTimeAndModeActivity.class, Constants.REQUEST_CODE1, bundle);
            }
        });*/
        mMemoMemberAdapter.setOnMemberRemoveListener(new MemoMemberAdapter.OnMemberRemoveListener() {
            @Override
            public void onRemove() {
                if (mMemoMemberAdapter.getData().size() <= 0) {
                    mRlMember.setVisibility(View.GONE);
                } else {
                    mRlMember.setVisibility(View.VISIBLE);
                }
            }
        });
        mRvText.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.checkbox) {

                    if (mMemoItemAdapter.isCreator()) {
                        updateMemo(position);
                    }
                }
            }
        });
        mRvRelevent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                //模块数据
                if ("2".equals(mRelevanceAdapter.getData().get(position).getType())) {
                    ((MemoDetailActivity) getActivity()).viewData(mRelevanceAdapter.getData().get(position));
                } else if ("1".equals(mRelevanceAdapter.getData().get(position).getType())) {
                    //项目详情
                    ToastUtils.showError(mContext, "数据已删除");

                }
            }
        });
        mRvLocation.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                //查看位置
                Intent intent = new Intent(getActivity(), ViewAddressPresenter.class);
                intent.putExtra(Constants.DATA_TAG1, mLocationAdapter.getData().get(position).getLng());
                intent.putExtra(Constants.DATA_TAG2, mLocationAdapter.getData().get(position).getLat());
                intent.putExtra(Constants.DATA_TAG3, true);
                getActivity().startActivity(intent);
            }
        });
        mRvMember.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                //人员详情
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DATA_TAG1, mMemoMemberAdapter.getData().get(position).getId() + "");
                UIRouter.getInstance().openUri(getActivity(), "DDComp://app/employee/info", bundle);

            }
        });


    }

    public void changeCheckState() {
        mMemoItemAdapter.changeCheckState();


    }

    public void changeNumState() {
        mMemoItemAdapter.changeNumState();
    }

    public void notifyDataChanged() {
        mMemoItemAdapter.notifyDataSetChanged();

    }

    public void addLocation(MemoBean memoBean) {
        mMemoItemAdapter.addLocation(memoBean);


    }

    /**
     * 提醒
     *
     * @param timeInMillis
     * @param mode
     */
    public void addRemind(long timeInMillis, int mode) {
        if (mRlRemind.getVisibility() == View.GONE || mRlRemind.getVisibility() == View.INVISIBLE) {
            mRlRemind.setVisibility(View.VISIBLE);
            mTvRemind.setText(DateTimeUtil.longToStr(timeInMillis, "yyyy-MM-dd HH:mm"));
            mRemindBean.setMode(mode);
            mRemindBean.setTime(timeInMillis);
        } else {
            mTvRemind.setText(DateTimeUtil.longToStr(timeInMillis, "yyyy-MM-dd HH:mm"));
            mRemindBean.setMode(mode);
            mRemindBean.setTime(timeInMillis);
        }
    }


    /**
     * 分享人
     *
     * @param list
     */
    public void setMember(List<MemoBean.MemberBean> list) {
        if (list.size() > 0) {
            mRlMember.setVisibility(View.VISIBLE);
        } else {
            mRlMember.setVisibility(View.GONE);
        }
        mMemoMemberAdapter.setNewData(list);
    }


    public void showData(MemoDetailBean baseBean) {
        mDetailBean = baseBean;


        if (baseBean != null && baseBean.getData() != null) {
            mRlRoot.setVisibility(View.VISIBLE);
            if ("2".equals(baseBean.getData().getDel_status())) {
                ToastUtils.showError(getActivity(), "数据已删除");
                getActivity().finish();
                return;
            }
            //是否是创建者,可对备忘中的待办进行状态操作
            mMemoItemAdapter.setCreator(SPHelper.getEmployeeId().equals(mDetailBean.getData().getCreate_by()));
            //头部信息
            MemoCreatorBean cBean = baseBean.getData().getCreateObj();
            if (cBean != null) {
                ImageLoader.loadCircleImage(getActivity(),
                        cBean.getPicture(),
                        mIvAvatar, cBean.getEmployee_name());
                TextUtil.setText(mTvName, cBean.getEmployee_name());
                TextUtil.setText(mTvPosition, cBean.getDuty_name());
                TextUtil.setText(mTvDate, DateTimeUtil.longToStr(TextUtil.parseLong(baseBean.getData().getCreate_time()), "yyyy-MM-dd HH:mm"));
            }


            //文字图片
            List<MemoContentBean> content = baseBean.getData().getContent();
            List<MemoBean> contentList = new ArrayList<>();
            if (content != null) {
                for (int i = 0; i < content.size(); i++) {
                    if (MemoConstant.ITEM_TEXT == content.get(i).getType()) {
                        MemoBean mb = new MemoBean();
                        mb.setType(MemoConstant.ITEM_TEXT);
                        MemoBean.TextBean bean = new MemoBean.TextBean();
                        bean.setCheck(content.get(i).getCheck());
                        bean.setNum(content.get(i).getNum());
                        bean.setContent(content.get(i).getContent());
                        mb.setText(bean);
                        contentList.add(mb);
                    } else if (MemoConstant.ITEM_IMAGE == content.get(i).getType()) {
                        MemoBean mb = new MemoBean();
                        mb.setType(MemoConstant.ITEM_IMAGE);
                        MemoBean.ImgBean bean = new MemoBean.ImgBean();
                        bean.setUrl(content.get(i).getContent());
                        mb.setImg(bean);
                        contentList.add(mb);
                    }
                }
                mMemoItemAdapter.setNewData(contentList);
                mMemoItemAdapter.notifyDataSetChanged();
            }
            //分享
            ArrayList<MemoCreatorBean> shareObj = baseBean.getData().getShareObj();
            List<MemoBean.MemberBean> memberList = new ArrayList<>();
            if (shareObj != null) {
                for (int i = 0; i < shareObj.size(); i++) {
                    MemoBean.MemberBean bean = new MemoBean.MemberBean();
                    bean.setAvatar(shareObj.get(i).getPicture());
                    bean.setName(shareObj.get(i).getEmployee_name());
                    bean.setId(shareObj.get(i).getId());
                    memberList.add(bean);
                }
                if (memberList.size() > 0) {
                    mRlMember.setVisibility(View.VISIBLE);
                    mMemoMemberAdapter.setNewData(memberList);
                    mMemoMemberAdapter.notifyDataSetChanged();
                } else {
                    mRlMember.setVisibility(View.GONE);
                }

            }
            //位置
            List<MemoLocationBean> locationList = baseBean.getData().getLocation();
            if (locationList != null) {
                mLocationAdapter.setNewData(locationList);
                mLocationAdapter.notifyDataSetChanged();
                if (locationList.size() > 0) {
                    mRlLocation.setVisibility(View.VISIBLE);
                } else {
                    mRlLocation.setVisibility(View.GONE);
                }
            }
            //提醒
            long remind_time = TextUtil.parseLong(baseBean.getData().getRemind_time());

            if (remind_time > 0) {
                TextUtil.setText(mTvRemind, DateTimeUtil.longToStr(remind_time, "yyyy-MM-dd HH:mm"));
                mRlRemind.setVisibility(View.VISIBLE);
            } else {
                mRlRemind.setVisibility(View.GONE);
            }
            //关联
            addRelevant(baseBean.getData().getItemsArr());

            //底部操作按钮
            flContainer.removeAllViews();
            String id = SPUtils.getString(getActivity(), AppConst.EMPLOYEE_ID);
            String create_by = baseBean.getData().getCreate_by();
            String delStatus = baseBean.getData().getDel_status();
            if ("0".equals(delStatus)) {
                //创建者
                if (!TextUtils.isEmpty(id) && id.equals(create_by)) {
                    mActionView = getActivity().getLayoutInflater().inflate(R.layout.memo_detail_actionbar_for_mine, null);
                    mTvCommentNum = mActionView.findViewById(R.id.tv_comment_num);
                    TextUtil.setText(mTvCommentNum, baseBean.getData().getCommentsCount());
                    mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.MODULE_BEAN, MemoConstant.BEAN_NAME);
                        bundle.putString(Constants.DATA_ID, mDetailBean.getData().getId());
                        CommonUtil.startActivtiyForResult(getActivity(), CommentActivity.class, Constants.REQUEST_CODE5, bundle);
                    });
                    mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.DATA_TAG1, mDetailBean);
                        CommonUtil.startActivtiyForResult(getActivity(), AddMemoActivity.class, Constants.REQUEST_CODE3, bundle);
                    });
                    mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> {
                        ((MemoDetailActivity) getActivity()).deleteMemo();
                        /*DialogUtils.getInstance().sureOrCancel(getActivity(), "", "确认删除?", getRootView(), new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                ((MemoDetailActivity) getActivity()).deleteMemo();
                            }
                        });*/
                    });
                    flContainer.addView(mActionView);
                    flContainer.setVisibility(View.VISIBLE);
                } else if (!TextUtils.isEmpty(id) && !id.equals(create_by)) {
                    mActionView = getActivity().getLayoutInflater().inflate(R.layout.memo_detail_actionbar_for_share, null);
                    mTvCommentNum = mActionView.findViewById(R.id.tv_comment_num);
                    TextUtil.setText(mTvCommentNum, baseBean.getData().getCommentsCount());
                    mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.MODULE_BEAN, MemoConstant.BEAN_NAME);
                        bundle.putString(Constants.DATA_ID, mDetailBean.getData().getId());
                        CommonUtil.startActivtiyForResult(getActivity(), CommentActivity.class, Constants.REQUEST_CODE5, bundle);
                    });
                    mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> {
                        ((MemoDetailActivity) getActivity()).quitMemo();
                        /*DialogUtils.getInstance().sureOrCancel(getActivity(), "", "确认退出共享?", getRootView(), new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                ((MemoDetailActivity) getActivity()).deleteMemo();
                            }
                        });*/
                    });
                    flContainer.addView(mActionView);
                    flContainer.setVisibility(View.VISIBLE);
                } else {
                    flContainer.removeAllViews();
                    flContainer.setVisibility(View.GONE);
                    getActivity().finish();
                }
            } else if ("1".equals(delStatus)) {
                if (!TextUtils.isEmpty(id) && id.equals(create_by)) {
                    mActionView = getActivity().getLayoutInflater().inflate(R.layout.memo_detail_actionbar_for_deleted, null);
                    mTvCommentNum = mActionView.findViewById(R.id.tv_comment_num);
                    TextUtil.setText(mTvCommentNum, baseBean.getData().getCommentsCount());
                    mActionView.findViewById(R.id.rl_action1).setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.MODULE_BEAN, MemoConstant.BEAN_NAME);
                        bundle.putString(Constants.DATA_ID, mDetailBean.getData().getId());
                        CommonUtil.startActivtiyForResult(getActivity(), CommentActivity.class, Constants.REQUEST_CODE5, bundle);
                    });
                    mActionView.findViewById(R.id.rl_action2).setOnClickListener(v -> {
                       /* DialogUtils.getInstance().sureOrCancel(getActivity(), "", "您确认恢复?", getRootView(), new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                ((MemoDetailActivity) getActivity()).recover();
                            }
                        });*/
                        ((MemoDetailActivity) getActivity()).recover();
                    });
                    mActionView.findViewById(R.id.rl_action3).setOnClickListener(v -> {
                        DialogUtils.getInstance().sureOrCancel(getActivity(), "", "您确认删除?", getRootView(), new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                ((MemoDetailActivity) getActivity()).deleteForever();
                            }
                        });
//                        ((MemoDetailActivity) getActivity()).deleteForever();
                    });
                    flContainer.addView(mActionView);
                    flContainer.setVisibility(View.VISIBLE);
                } else {
                    flContainer.removeAllViews();
                    flContainer.setVisibility(View.GONE);
                    getActivity().finish();
                }
            }
        } else {
            showError();
        }
    }

    public void showError() {
        mFlError.setVisibility(View.GONE);
        // mFlError.setVisibility(View.VISIBLE);
    }

    private void updateMemo(int position) {

        if (mDetailBean != null && mDetailBean.getData() != null) {
            NewMemoBean bean = new NewMemoBean();
            MemoDetailBean.DataBean dataBean = mDetailBean.getData();
            bean.setId(dataBean.getId());
            bean.setShareIds(dataBean.getShare_ids());
            bean.setTitle(dataBean.getTitle());
            bean.setPicUrl(dataBean.getPic_url());
            bean.setItemsArr(dataBean.getItemsArr());
            bean.setRemindStatus(TextUtil.parseInt(dataBean.getRemind_status(), 1));
            bean.setRemindTime(TextUtil.parseLong(dataBean.getRemind_time(), 0L));
            bean.setLocation(dataBean.getLocation());
            for (int i = 0; i < dataBean.getContent().size(); i++) {
                if (i == position) {
                    if (dataBean.getContent().get(i).getCheck() == 1) {
                        dataBean.getContent().get(i).setCheck(2);
                    } else if (dataBean.getContent().get(i).getCheck() == 2) {
                        dataBean.getContent().get(i).setCheck(1);
                    }
                }
            }
            bean.setContent(dataBean.getContent());
            ((MemoDetailActivity) getActivity()).updateMemo(bean);
        } else {
            return;
        }
    }

    public void addRelevant(ArrayList<ModuleItemBean> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        mRelevanceAdapter.setNewData(list);
        if (list.size() > 0) {
            mRlRelevant.setVisibility(View.VISIBLE);
        } else {
            mRlRelevant.setVisibility(View.GONE);
        }
    }

    public void setCommentNum(String num) {
        TextUtil.setText(mTvCommentNum, TextUtils.isEmpty(num) ? "" : num);
    }
}
