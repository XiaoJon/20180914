package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.hjhq.teamface.basis.BaseFragment;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.view.ScaleTransitionPagerTitleView;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.bean.GroupChatInfoBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class MessageReadFragment extends BaseFragment {

    ImageView ivBack;
    MagicIndicator mMagicIndicator;
    ImageView titleBarRightIv;
    ViewPager mViewPager;
    private String[] PROJECT_TITLES;
    private List<Fragment> fragments = new ArrayList<>(2);
    private FragmentAdapter mAdapter;
    long conversationId;
    long messageId;
    List<Member> allMemberList = new ArrayList<>();
    List<Member> readMemberList = new ArrayList<>();
    List<Member> unreadMemberList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            conversationId = bundle.getLong(MsgConstant.CONVERSATION_ID);
            messageId = bundle.getLong(MsgConstant.MESSAGE_ID);
            getNetData();
        }
        ivBack = getActivity().findViewById(R.id.ivBack);
        mMagicIndicator = getActivity().findViewById(R.id.indicator);
        titleBarRightIv = getActivity().findViewById(R.id.titleBar_RightIv);
        mViewPager = getActivity().findViewById(R.id.viewpager);
        f1 = MessageReadListFragment.newInstance(MessageReadListFragment.TAG1);
        f2 = MessageUnreadListFragment.newInstance(MessageUnreadListFragment.TAG2);
        titleBarRightIv.setVisibility(View.GONE);
        PROJECT_TITLES = getResources().getStringArray(R.array.message_read_title_name);
        fragments.add(f1);
        fragments.add(f2);

        mAdapter = new FragmentAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(commonNavigatorAdapter);
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);

    }

    @Override
    protected void initData() {

    }

    private void getNetData() {
        ImLogic.getInstance().getGroupDetail(((MessageReadStateActivity) getActivity()), conversationId, new ProgressSubscriber<GroupChatInfoBean>(getActivity(), false) {
            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(GroupChatInfoBean bean) {
                allMemberList.clear();
                for (int i = 0; i < bean.getData().getEmployeeInfo().size(); i++) {
                    Member member = new Member();
                    member.setName(bean.getData().getEmployeeInfo().get(i).getEmployee_name());
                    try {
                        member.setSign_id(Long.parseLong(bean.getData().getEmployeeInfo().get(i).getSign_id()));
                    } catch (NumberFormatException e) {
                    }
                    member.setPicture(bean.getData().getEmployeeInfo().get(i).getPicture());
                    member.setPost_name(bean.getData().getEmployeeInfo().get(i).getPost_id());
                    member.setId(bean.getData().getEmployeeInfo().get(i).getId());
                    allMemberList.add(member);
                }
                filterData();
            }
        });
    }

    private void filterData() {
        /*List<MessageReadList> list = DBManager.getInstance().qureyMessageReadNumByMsgID(messageId);
        unreadMemberList.clear();
        readMemberList.clear();
        unreadMemberList.addAll(allMemberList);
        for (int i = 0; i < allMemberList.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (allMemberList.get(i).getSign_id().equals(list.get(j).getReaderId())) {
                    readMemberList.add(allMemberList.get(i));
                }
            }
        }
        for (int i = 0; i < readMemberList.size(); i++) {
            if (unreadMemberList.contains(readMemberList.get(i))) {
                unreadMemberList.remove(readMemberList.get(i));
            }

        }
        Iterator<Member> it = unreadMemberList.iterator();
        while (it.hasNext()) {
            Member m = it.next();
            if (SPHelper.getUserId().equals(m.getSign_id())) {
                it.remove();
            }
        }

        ((MessageReadListFragment) fragments.get(0)).setData(allMemberList);
        ((MessageReadListFragment) fragments.get(1)).setData(allMemberList);
        mAdapter.notifyDataSetChanged();
*/

    }

    @Override
    protected int getContentView() {
        return R.layout.im_fragment_message_read_state;
    }

    MessageReadListFragment f1;
    MessageUnreadListFragment f2;


    @Override
    protected void initView(View view) {

    }


    @Override
    protected void setListener() {
        setOnClicks(ivBack, titleBarRightIv);
    }

    CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {

        @Override
        public int getCount() {
            return PROJECT_TITLES == null ? 0 : PROJECT_TITLES.length;
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            ScaleTransitionPagerTitleView scaleTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context);
            scaleTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.__picker_gray_69));
            scaleTransitionPagerTitleView.setWidth((int) (ScreenUtils.getScreenWidth(mContext) / PROJECT_TITLES.length));
            scaleTransitionPagerTitleView.setGravity(Gravity.CENTER);
            scaleTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.main_green));
//                colorTransitionPagerTitleView.set
            scaleTransitionPagerTitleView.setTextSize(20);
            scaleTransitionPagerTitleView.setText(PROJECT_TITLES[index]);
            scaleTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(index);
                }
            });
            return scaleTransitionPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
            indicator.setLineWidth(DeviceUtils.dpToPixel(mContext, 80));
            indicator.setLineHeight(DeviceUtils.dpToPixel(mContext, 3));
//            indicator.setYOffset(DeviceUtils.dpToPixel(mContext, 47));
            indicator.setColors(ContextCompat.getColor(mContext, R.color.main_green));
            return indicator;
        }

    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivBack) {
            getActivity().finish();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE1) {
            // TODO: 2017/6/17 刷新列表
        }
        if (requestCode == Constants.TAKE_PHOTO_NEW_REQUEST_CODE) {

        }
    }


}