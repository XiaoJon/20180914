package com.hjhq.teamface.oa.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.BaseFragment;
import com.hjhq.teamface.basis.bean.EmployeeDetailBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.emoji.EmojiUtil;
import com.hjhq.teamface.oa.friends.AddFriendsActivity;
import com.hjhq.teamface.oa.friends.FriendsActivity;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.Bind;


/**
 * 我的
 *
 * @author lx
 * @date 2017/3/16
 */

public class MineFragment extends BaseFragment {
    @Bind(R.id.iv_add_friend)
    ImageView ivAddFriend;
    @Bind(R.id.fl_info_card)
    FrameLayout flInfoCard;
    @Bind(R.id.mine_avatar)
    ImageView mineAvatar;
    @Bind(R.id.mine_name_tv)
    TextView mineNameTv;
    @Bind(R.id.mine_company_tv)
    TextView mineCompanyTv;
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.mine_friends)
    LinearLayout mineFriends;
    @Bind(R.id.mine_collection)
    LinearLayout mineCollection;
    @Bind(R.id.mine_about)
    LinearLayout mineAbout;
    @Bind(R.id.mine_online_help)
    LinearLayout mineOnlineHelp;
    @Bind(R.id.mine_setting)
    LinearLayout mineSetting;
    @Bind(R.id.ll_card)
    LinearLayout mineCard;
    private String sign;
    private String mood;


    @Override
    protected void initData() {
        getNetData(true);
    }

    /**
     * 获取数据
     *
     * @param flag 是否显示加载动画
     */
    private void getNetData(boolean flag) {
        MainLogic.getInstance().queryEmployeeInfo((BaseActivity) getActivity(),
                SPHelper.getEmployeeId(), new ProgressSubscriber<EmployeeDetailBean>(getActivity(), flag) {
                    @Override
                    public void onNext(EmployeeDetailBean userInfoBean) {
                        super.onNext(userInfoBean);
                        EmployeeDetailBean.DataBean.EmployeeInfoBean employeeInfo = userInfoBean.getData().getEmployeeInfo();

                        String employeeName = employeeInfo.getEmployee_name();
                        String companyName = SPHelper.getCompanyName();
                        TextUtil.setText(mineNameTv, employeeName);
                        TextUtil.setText(mineCompanyTv, companyName);
                        ImageLoader.loadCircleImage(mContext, employeeInfo.getPicture(), mineAvatar, employeeName, 1, ColorUtils.resToColor(mContext, R.color.white));


                        sign = employeeInfo.getSign();
                        mood = employeeInfo.getMood();
                        //签名
                        try {
                            if (!TextUtil.isEmpty(sign) || !TextUtil.isEmpty(mood)) {
                                EmojiUtil.handlerEmojiText2(tvSign, mood + " " + sign, mContext);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        flInfoCard.setPadding(0, statusBarHeight, 0, 0);

    }


    @Override
    protected void setListener() {
        setOnClicks(this, mineSetting, mineAvatar, mineNameTv, tvSign, mineCard, mineCompanyTv,
                mineFriends, mineOnlineHelp, mineAbout, mineCollection, ivAddFriend);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.mine_company_tv:
                //选择公司
                CommonUtil.startActivtiy(mContext, SelectCompanyActivity.class);
                break;
            case R.id.mine_setting:
                //设置
                CommonUtil.startActivtiy(mContext, SettingActivity.class);
                break;
            case R.id.mine_name_tv:
            case R.id.mine_avatar:
                //个人信息
                bundle.putString(Constants.DATA_TAG1, SPHelper.getEmployeeId());
                CommonUtil.startActivtiyForResult(mContext, UserInfoActivity.class, Constants.REQUEST_CODE1, bundle);
                break;
            case R.id.tv_sign:
                bundle.putString(Constants.DATA_TAG1, sign);
                bundle.putString(Constants.DATA_TAG2, mood);
                CommonUtil.startActivtiyForResult(this, SignatureActivity.class, Constants.REQUEST_CODE1, bundle);
                break;
            case R.id.ll_card:
                //我的名片
                CommonUtil.startActivtiy(mContext, UserQRCodeActivityV2.class);
                //CommonUtil.startActivtiy(mContext, UserQRCodeActivity.class);
                break;
            case R.id.iv_add_friend:
                //同事圈
                CommonUtil.startActivtiy(getActivity(), AddFriendsActivity.class, bundle);
                break;
            case R.id.mine_friends:
                //同事圈
                CommonUtil.startActivtiy(getActivity(), FriendsActivity.class, bundle);
                break;
            case R.id.mine_collection:
                ToastUtils.showToast(getActivity(), "敬请期待");
                break;
            case R.id.mine_about:
                //关于
                ToastUtils.showToast(getActivity(), "敬请期待");
                break;
            case R.id.mine_online_help:
                //在线帮助
                Intent intent = new Intent(Intent.ACTION_VIEW, (Uri.parse("http://www.teamface.cn"))).addCategory(Intent.CATEGORY_BROWSABLE).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(MessageBean event) {
        if (event.getCode() == Constants.EDIT_USER_INFO) {
            getNetData(false);
        }
        if (EventConstant.TYPE_ORGANIZATIONAL_AND_PERSONNEL_CHANGES.equals(event.getTag())) {
            getNetData(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == Activity.RESULT_OK && data != null) {
            //签名
            sign = data.getStringExtra(Constants.DATA_TAG1);
            mood = data.getStringExtra(Constants.DATA_TAG2);
            try {
                if (TextUtil.isEmpty(sign) && TextUtil.isEmpty(mood)) {
                    tvSign.setText("");
                }else{
                    EmojiUtil.handlerEmojiText2(tvSign, mood + " " + sign, mContext);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
