package com.hjhq.teamface.oa.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseFragment;
import com.hjhq.teamface.basis.bean.ToolMenu;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.view.ScaleTransitionPagerTitleView;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.activity.TeamMessageFragment;
import com.hjhq.teamface.common.activity.CaptureActivity;
import com.hjhq.teamface.im.activity.ChooseGroupChatActivity;
import com.hjhq.teamface.im.activity.CreateGroupActivity;
import com.hjhq.teamface.util.CommonUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * 通讯录消息
 *
 * @author Administrator
 */
public class ContactsMessageFragment extends BaseFragment {
    private static final String[] TITLE = {"消息", "通讯录"};

    @Bind(R.id.fl_contacts_message_layout)
    FrameLayout flContactsMessageLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @Bind(R.id.iv_left_bar)
    ImageView ivLeftBar;
    @Bind(R.id.iv_right_bar)
    ImageView ivRightBar;
    /**
     * 菜单条目列表
     */
    private List<ToolMenu> list = new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.contacts_message_fragment;
    }

    @Override
    protected void initView(View view) {
        //撑起状态栏
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        flContactsMessageLayout.setPadding(0, statusBarHeight, 0, 0);

        List<Fragment> fragments = new ArrayList<>(2);
        fragments.add(new TeamMessageFragment());
        fragments.add(new ContactsFragmentV2());

        mMagicIndicator.setVisibility(View.VISIBLE);
        FragmentAdapter mAdapter = new FragmentAdapter(((RxAppCompatActivity) mContext).getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mAdapter);

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new MyCommonNavigatorAdapter());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setRightPadding((int) (ScreenUtils.getScreenWidth(mContext) / 8));
        commonNavigator.setLeftPadding((int) (ScreenUtils.getScreenWidth(mContext) / 8));
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    protected void initData() {
        String[] menuString = getActivity().getResources().getStringArray(R.array.im_menu_array);
        ToolMenu menu1 = new ToolMenu(1, menuString[0], R.drawable.icon_add_group);
        ToolMenu menu2 = new ToolMenu(2, menuString[1], R.drawable.icon_choose_single);
        ToolMenu menu3 = new ToolMenu(3, menuString[2], R.drawable.icon_choose_group);
        list.add(menu1);
        list.add(menu2);
        list.add(menu3);
    }

    @Override
    protected void setListener() {
        setOnClicks(ivLeftBar, ivRightBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_bar:
                //扫码
                CommonUtil.startActivtiy(mContext, CaptureActivity.class);
                break;
            case R.id.iv_right_bar:
                showChatMenu(v);
                break;
            default:
                break;
        }
    }

    /**
     * 显示创建聊天选项菜单
     *
     * @param view
     */
    private void showChatMenu(View view) {
        PopUtils.showMenuPopupWindow(getActivity(), view, list, position -> {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    CommonUtil.startActivtiy(getActivity(), CreateGroupActivity.class);
                    break;
                case 1:

                    ArrayList<Member> members = new ArrayList<Member>();
                    Member m = new Member();
                    m.setSelectState(C.HIDE_TO_SELECT);
                    m.setCheck(false);
                    m.setName(IM.getInstance().getName());
                    m.setEmployee_name(IM.getInstance().getName());
                    m.setId(TextUtil.parseLong(SPHelper.getEmployeeId()));
                    members.add(m);
                    bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members);
                    bundle.putInt(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
                    bundle.putString(C.CHOOSE_RANGE_TAG, "0");
                    bundle.putString(Constants.NAME, "选择联系人");

                    CommonUtil.startActivtiyForResult(getActivity(),
                            SelectMemberActivity.class, Constants.REQUEST_CODE2, bundle);
                    break;
                case 2:
                    bundle.putString(Constants.DATA_TAG1, MsgConstant.CHOOSE_GROUP_CHAT);
                    CommonUtil.startActivtiy(getActivity(), ChooseGroupChatActivity.class, bundle);
                    break;
                default:
                    break;
            }
            return true;
        });
    }


    /**
     * 导航适配器
     */
    private class MyCommonNavigatorAdapter extends CommonNavigatorAdapter {

        @Override
        public int getCount() {
            return TITLE.length;
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            ScaleTransitionPagerTitleView scaleTransitionPagerTitleView = new ScaleTransitionPagerTitleView(context);
            scaleTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.gray_99));
            scaleTransitionPagerTitleView.setGravity(Gravity.CENTER);
            scaleTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.black_22));
            scaleTransitionPagerTitleView.setTextSize(18);
            scaleTransitionPagerTitleView.setText(TITLE[index]);
            scaleTransitionPagerTitleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
            return scaleTransitionPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            return null;
        }
    }

}