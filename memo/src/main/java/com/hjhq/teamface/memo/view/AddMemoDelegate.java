package com.hjhq.teamface.memo.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjhq.teamface.basis.bean.ModuleItemBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
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
import com.hjhq.teamface.memo.ui.MemoRemindTimeAndModeActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 * Describe：
 */

public class AddMemoDelegate extends AppDelegate {
    private RecyclerView mRvText;
    private RecyclerView mRvLocation;
    private RecyclerView mRvMember;
    private RecyclerView mRvRelevent;
    private MemoItemAdapter mMemoItemAdapter;
    private MemoLocationAdapter mLocationAdapter;
    private MemoRelevanceAdapter mRelevanceAdapter;
    private MemoMemberAdapter mMemoMemberAdapter;
    private List<MemoBean> memoData = new ArrayList<>();
    private RelativeLayout mRlRemind;
    private RelativeLayout mRlMember;
    private RelativeLayout mRlRelevant;
    private RelativeLayout mRlLocation;
    private TextView mTvRemind;
    private ImageView mIvRemoveRemind;
    private MemoBean.RemindBean mRemindBean;

    @Override
    public int getRootLayoutId() {
        return R.layout.memo_activity_add;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("备忘录");
        setRightMenuTexts(R.color.black, "保存");
        mRemindBean = new MemoBean.RemindBean();
        mRemindBean.setTime(0L);
        mRemindBean.setMode(MemoConstant.REMIND_MODE_SELF);
        mRvText = get(R.id.rv_text);
        mRvLocation = get(R.id.rv_location);
        mRvRelevent = get(R.id.rv_relevant);
        mRvMember = get(R.id.rv_member);
        mRlRemind = get(R.id.rl_remind);
        mRlRelevant = get(R.id.rl_relevant);
        mRlLocation = get(R.id.rl_location);
        mTvRemind = get(R.id.tv_time_content);
        mIvRemoveRemind = get(R.id.iv_remove_remind);
        mRlMember = get(R.id.rl_member);
        ((SimpleItemAnimator) mRvText.getItemAnimator()).setSupportsChangeAnimations(false);

        mRvText.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRvText.setItemViewCacheSize(0);
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
        mMemoItemAdapter = new MemoItemAdapter(true, memoData);
        mMemoItemAdapter.setCreator(true);
        mLocationAdapter = new MemoLocationAdapter(1, null);
        mMemoMemberAdapter = new MemoMemberAdapter(1, null);
        mRelevanceAdapter = new MemoRelevanceAdapter(1, null);
        mRelevanceAdapter.setOnDataSetChangeListener(i -> {
            if (0 == i) {
                mRlRelevant.setVisibility(View.GONE);
            } else {
                mRvRelevent.setVisibility(View.VISIBLE);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 6);
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
        mRlRemind.setOnClickListener(new View.OnClickListener() {
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
                bundle.putBoolean(Constants.DATA_TAG3, getRemindBean().getTime() > 0L);
                CommonUtil.startActivtiyForResult(getActivity(),
                        MemoRemindTimeAndModeActivity.class, Constants.REQUEST_CODE1, bundle);
            }
        });
        mIvRemoveRemind.setVisibility(View.VISIBLE);
        mIvRemoveRemind.setOnClickListener(v -> {
            mRemindBean.setTime(0L);
            mRemindBean.setMode(MemoConstant.REMIND_MODE_SELF);
            mRlRemind.setVisibility(View.GONE);
        });
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
        mRvText.setOnClickListener(v -> {
            mMemoItemAdapter.requestInputFocus();
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

    public List<MemoBean> getData() {
        return mMemoItemAdapter.getItemData();

    }

    public void addImage(String fileUrl) {

        mMemoItemAdapter.addImage(fileUrl);
    }

    public EditText getEditText() {
        return mMemoItemAdapter.getEditText();
    }

    public void requestInputFocus() {
        mMemoItemAdapter.requestInputFocus();

    }

    public void addLocation(MemoBean memoBean) {
        mMemoItemAdapter.addLocation(memoBean);


    }

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


    public long getRemindTime() {

        return mRemindBean.getTime();
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
        List<MemoBean.MemberBean> data = mMemoMemberAdapter.getData();
        data.clear();
        data.addAll(list);
        mMemoMemberAdapter.notifyDataSetChanged();

    }

    public ArrayList<Member> getMembers() {
        ArrayList<Member> list1 = new ArrayList<>();
        List<MemoBean.MemberBean> list2 = mMemoMemberAdapter.getData();
        for (int i = 0; i < list2.size(); i++) {
            Member m = new Member();
            m.setCheck(true);
            m.setEmployee_name(list2.get(i).getName());
            m.setPicture(list2.get(i).getAvatar());
            m.setId(TextUtil.parseLong(list2.get(i).getId()));
            list1.add(m);
        }
        return list1;
    }

    /**
     * 获取备忘提醒数据
     *
     * @return
     */
    public MemoBean.RemindBean getRemindBean() {
        return mRemindBean;
    }

    public void addLocation(MemoLocationBean bean) {
        List<MemoLocationBean> data = mLocationAdapter.getData();
        if (data.size() <= 0) {
            mLocationAdapter.addData(bean);
            mLocationAdapter.notifyDataSetChanged();
        } else {

            boolean flag = true;
            for (int i = 0; i < data.size(); i++) {
                boolean f1 = !TextUtils.isEmpty(bean.getAddress());
                boolean f3 = bean.getAddress().equals(data.get(i).getAddress());
                if (f1 && f3) {
                    flag = false;
                }

            }
            if (flag) {
                mLocationAdapter.getData().add(bean);
                mLocationAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showToast(getActivity(), "地址重复");
            }
        }
        if (mLocationAdapter.getData() != null && mLocationAdapter.getData().size() > 0) {
            mRlLocation.setVisibility(View.VISIBLE);
        } else {
            mRlLocation.setVisibility(View.GONE);
        }

    }

    public List<MemoLocationBean> getLocationData() {

        return mLocationAdapter.getData();
    }

    public String getSharedIds() {
        StringBuilder sb = new StringBuilder();
        List<MemoBean.MemberBean> data = mMemoMemberAdapter.getData();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (i == 0) {
                    sb.append(data.get(i).getId());
                } else {
                    sb.append("," + data.get(i).getId());
                }
            }
            return sb.toString();
        } else {
            return "";
        }


    }

    /**
     * 清除分享人
     */
    public void clearMember() {
        mRlMember.setVisibility(View.GONE);
        mMemoMemberAdapter.getData().clear();
        mMemoMemberAdapter.notifyDataSetChanged();

    }

    /**
     * 编辑备忘录时初始化数据
     *
     * @param bean
     */
    public void setData(MemoDetailBean bean) {
        if (bean.getData() != null) {
            //分享人
            ArrayList<MemoCreatorBean> shareObj = bean.getData().getShareObj();
            List<MemoBean.MemberBean> mList = new ArrayList<>();
            for (int i = 0; i < shareObj.size(); i++) {
                MemoBean.MemberBean mb = new MemoBean.MemberBean();
                mb.setId(shareObj.get(i).getId());
                mb.setName(shareObj.get(i).getEmployee_name());
                mb.setAvatar(shareObj.get(i).getPicture());
                mList.add(mb);
            }
            mMemoMemberAdapter.setNewData(mList);
            if (mMemoMemberAdapter.getData().size() > 0) {
                mRlMember.setVisibility(View.VISIBLE);
            } else {
                mRlMember.setVisibility(View.GONE);
            }
            //位置
            mLocationAdapter.setNewData(bean.getData().getLocation());
            if (mLocationAdapter.getData().size() > 0) {
                mRlLocation.setVisibility(View.VISIBLE);
            } else {
                mRlLocation.setVisibility(View.GONE);
            }
            //提醒
            if ("0".equals(bean.getData().getRemind_time())) {
                mRemindBean.setTime(0L);
            } else {
                mRemindBean.setTime(TextUtil.parseLong(bean.getData().getRemind_time()));
            }

            mRemindBean.setMode(TextUtil.parseInt(bean.getData().getRemind_status(), 1));
            if (mRemindBean.getTime() > 0) {
                mRlRemind.setVisibility(View.VISIBLE);
                TextUtil.setText(mTvRemind, DateTimeUtil.longToStr(mRemindBean.getTime(), "yyyy-MM-dd HH:mm"));
            } else {
                mRlRemind.setVisibility(View.GONE);
            }
            //内容
            ArrayList<MemoContentBean> content = bean.getData().getContent();
            List<MemoBean> beanList = new ArrayList<>();
            for (int i = 0; i < content.size(); i++) {
                MemoBean m = new MemoBean();
                m.setType(content.get(i).getType());
                if (m.getType() == MemoConstant.ITEM_TEXT) {
                    MemoBean.TextBean tb = new MemoBean.TextBean();
                    tb.setContent(content.get(i).getContent());
                    tb.setNum(content.get(i).getNum());
                    tb.setCheck(content.get(i).getCheck());
                    m.setText(tb);
                    beanList.add(m);
                } else if (m.getType() == MemoConstant.ITEM_IMAGE) {
                    MemoBean.ImgBean ib = new MemoBean.ImgBean();
                    ib.setUrl(content.get(i).getContent());
                    m.setImg(ib);
                    beanList.add(m);
                }
            }
            mMemoItemAdapter.getData().addAll(0, beanList);
            mMemoItemAdapter.notifyDataSetChanged();
            //关联
            addRelevant(bean.getData().getItemsArr());
        }


    }

    /**
     * 添加关联数据
     *
     * @param list2
     */
    public void addRelevant(ArrayList<ModuleItemBean> list2) {
        if (list2 == null) {
            list2 = new ArrayList<>();
        }
        List<ModuleItemBean> list1 = mRelevanceAdapter.getData();
        if (list1.size() <= 0) {
            mRelevanceAdapter.getData().addAll(list2);
        } else {
            for (int i = 0; i < list2.size(); i++) {
                boolean flag = false;
                ModuleItemBean bea = list2.get(i);

                for (int j = 0; j < list1.size(); j++) {
                    String s1 = new Gson().toJson(bea);
                    String s2 = new Gson().toJson(list1.get(j));
                    if (s1.equals(s2)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    list1.add(bea);
                }
            }
        }

        mRelevanceAdapter.notifyDataSetChanged();
        if (mRelevanceAdapter.getData().size() > 0) {
            mRlRelevant.setVisibility(View.VISIBLE);
        } else {
            mRlRelevant.setVisibility(View.GONE);
        }
    }

    /**
     * 获取关联数据
     *
     * @return
     */
    public List<ModuleItemBean> getRelevantData() {
        return mRelevanceAdapter.getData();
    }

    public void setFocusChangeListener(MemoItemAdapter.OnFocusChangeListener listener) {
        mMemoItemAdapter.setOnFocusChangeListener(listener);
    }

    public boolean isContentChange() {
        boolean flag = false;
        flag = mMemoItemAdapter.isContentChanged();
        if (flag) {
            return flag;
        }
        flag = mLocationAdapter.isContentChanged();
        if (flag) {
            return flag;
        }
        flag = mMemoMemberAdapter.isContentChanged();
        if (flag) {
            return flag;
        }
        flag = mRelevanceAdapter.isContentChanged();
        if (flag) {
            return flag;
        }
        return flag;
    }
}
