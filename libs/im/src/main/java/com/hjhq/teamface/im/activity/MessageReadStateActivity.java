package com.hjhq.teamface.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
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
import java.util.Iterator;
import java.util.List;

public class MessageReadStateActivity extends BaseActivity {
    // FrameLayout mNoticeContainer;
    ImageView ivBack;
    MagicIndicator mMagicIndicator;
    ImageView titleBarRightIv;
    ViewPager mViewPager;
    private String[] PROJECT_TITLES;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentAdapter mAdapter;
    long conversationId;
    long messageId;
    List<Member> allMemberList = new ArrayList<>();
    List<Member> readMemberList = new ArrayList<>();
    List<Member> unreadMemberList = new ArrayList<>();
    MessageReadListFragment f1;
    MessageUnreadListFragment f2;
    SocketMessage mSocketMessage;

    @Override
    protected int getContentView() {
        return R.layout.im_fragment_message_read_state;
    }

    @Override
    protected void initView() {
        SoftKeyboardUtils.hide(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            conversationId = bundle.getLong(MsgConstant.CONVERSATION_ID);
            messageId = bundle.getLong(MsgConstant.MESSAGE_ID);
            mSocketMessage = (SocketMessage) bundle.getSerializable(MsgConstant.MSG_DATA);
            getNetData();
        }
        ivBack = findViewById(R.id.ivBack);
        mMagicIndicator = findViewById(R.id.indicator);
        titleBarRightIv = findViewById(R.id.titleBar_RightIv);
        mViewPager = findViewById(R.id.viewpager);
        f1 = MessageReadListFragment.newInstance(MessageReadListFragment.TAG1);
        f2 = MessageUnreadListFragment.newInstance(MessageUnreadListFragment.TAG2);
        titleBarRightIv.setVisibility(View.GONE);
        PROJECT_TITLES = getResources().getStringArray(R.array.message_read_title_name);
        fragments.add(f1);
        fragments.add(f2);


        mAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(0);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(commonNavigatorAdapter);
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);


    }

    private void getNetData() {
        ImLogic.getInstance().getGroupDetail(MessageReadStateActivity.this, conversationId,
                new ProgressSubscriber<GroupChatInfoBean>(this, false) {
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
                            member.setPost_id(bean.getData().getEmployeeInfo().get(i).getPost_id());
//                            member.setPost_name(bean.getData().getEmployeeInfo().get(i).getPost_id());
//                            member.setDepartment_name(bean.getData().getEmployeeInfo().get(i).getPost_id());
                            member.setId(bean.getData().getEmployeeInfo().get(i).getId());
                            allMemberList.add(member);
                        }
                        filterData();
                    }
                });
    }

    private void filterData() {
        Iterator<Member> iterator = allMemberList.iterator();
        while (iterator.hasNext()) {
            if (SPHelper.getUserId().equals(iterator.next().getSign_id())) {
                iterator.remove();
            }
        }
        List<Member> listAll = new ArrayList<>();
        if (mSocketMessage != null) {
            String all = mSocketMessage.getAllPeoples();
            if (TextUtils.isEmpty(all)) {
                finish();
                return;
            }
            for (int i = 0; i < allMemberList.size(); i++) {
                if (all.contains(allMemberList.get(i).getSign_id())) {
                    listAll.add(allMemberList.get(i));
                }
            }
            String readString = mSocketMessage.getReadPeoples();
            if (!TextUtils.isEmpty(readString)) {
                for (int i = 0; i < listAll.size(); i++) {
                    if (SPHelper.getUserId().equals(allMemberList.get(i).getSign_id())) {
                        continue;
                    }
                    if (readString.contains(listAll.get(i).getSign_id())) {
                        readMemberList.add(listAll.get(i));
                    } else {
                        unreadMemberList.add(listAll.get(i));
                    }
                }
            }
        }


        f1.setData(readMemberList);
        f2.setData(unreadMemberList);


    }

    @Override
    protected void setListener() {
        setOnClicks(ivBack);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //CommonUtil.showToast(position + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivBack) {
            finish();
        }
    }


    CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {

        @Override
        public int getCount() {
            //return PROJECT_TITLES == null ? 0 : PROJECT_TITLES.length;
            return 2;
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
}