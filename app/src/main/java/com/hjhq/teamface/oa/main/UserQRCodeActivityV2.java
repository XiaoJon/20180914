package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hjhq.lib_zxing.activity.CodeUtils;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.EmployeeCardDetailBean;
import com.hjhq.teamface.basis.bean.EmployeeDetailBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.image.ImageformatUtil;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;

/**
 * 二维码名片
 *
 * @author Administrator
 */
public class UserQRCodeActivityV2 extends BaseTitleActivity {
    public static final String TEAM_CODE = "cardType";
    @Bind(R.id.fl_user_info)
    FrameLayout flUserInfo;
    @Bind(R.id.qr_code_iv)
    ImageView qrCodeIv;
    @Bind(R.id.rl_action)
    RelativeLayout rlAction;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;
    @Bind(R.id.sv)
    ScrollView svRoot;

    private ArrayList<String> userInfos = new ArrayList<>();
    private TextView tvName;
    private TextView tvPost;
    private TextView tvEmail;
    private TextView tvCompanyName;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvFixedPhone;
    private ImageView ivLogo;
    private RelativeLayout rlCompanyName;
    private RelativeLayout rlLogo;
    private RelativeLayout rlAddress;
    private RelativeLayout rlPhone;
    private RelativeLayout rlEmail;
    private RelativeLayout rlFixedPhone;
    private String cardType;

    private boolean isCardTypeReady = false;
    private boolean isDataTypeReady = false;
    private EmployeeDetailBean mDataBean;
    private EmployeeCardDetailBean mCardBean;
    private String styleArr = "[{\"id\":\"0\",\"styleId\":\"0\"},{\"id\":\"1\",\"styleId\":\"1\"}]";


    @Override
    protected int getChildView() {
        return R.layout.acitivity_user_qr_code_v2;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle(R.string.my_card);

    }

    /**
     * 初始化模版
     */
    private void initCard(EmployeeCardDetailBean cardBean) {
        flUserInfo.removeAllViews();
        int layout;
        int drawableRes = 0;
        cardType = TextUtils.isEmpty(cardBean.getData().getChoice_template()) ? "0" : cardBean.getData().getChoice_template();
        switch (cardType) {
            case "0":
                layout = R.layout.user_card_diy_style1_layout;
                drawableRes = R.drawable.diy_card_style0_bg;
                break;
            case "1":
                layout = R.layout.user_card_diy_style2_layout;
                drawableRes = R.drawable.diy_card_style1_bg;
                break;
            default:
                layout = R.layout.user_card_diy_style1_layout;
                drawableRes = R.drawable.diy_card_style0_bg;
                break;
        }
        View inflate = getLayoutInflater().inflate(layout, null);
        inflate.setBackgroundResource(drawableRes);
        rlCompanyName = inflate.findViewById(R.id.rl1);
        rlAddress = inflate.findViewById(R.id.rl2);
        rlPhone = inflate.findViewById(R.id.rl3);
        rlFixedPhone = inflate.findViewById(R.id.rl4);
        rlLogo = inflate.findViewById(R.id.rl_logo);
        flUserInfo.addView(inflate);
        tvName = inflate.findViewById(R.id.tv_name);
        tvPost = inflate.findViewById(R.id.tv_duty);
        tvEmail = inflate.findViewById(R.id.tv_email);
        tvCompanyName = inflate.findViewById(R.id.text1);
        tvAddress = inflate.findViewById(R.id.text2);
        tvPhone = inflate.findViewById(R.id.text3);
        tvFixedPhone = inflate.findViewById(R.id.text4);
        ivLogo = inflate.findViewById(R.id.iv_logo);
        final String hideItems = cardBean.getData().getHide_set();
        final String[] split = hideItems.split(",");
        if (split != null && split.length > 0) {
            for (int i = 0; i < split.length; i++) {
                switch (split[i]) {
                    case "0":
                        //logo
                        rlLogo.setVisibility(View.GONE);
                        break;
                    case "1":
                        //座机
                        rlFixedPhone.setVisibility(View.GONE);
                        break;
                    case "2":
                        //手机
                        rlPhone.setVisibility(View.GONE);
                        break;
                    case "3":
                        //邮件
                        tvEmail.setVisibility(View.GONE);
                        break;
                    case "4":
                        //地址
                        rlAddress.setVisibility(View.GONE);
                        break;
                    case "5":

                        break;
                    case "6":

                        break;
                }
            }
        }
        isCardTypeReady = true;
        if (isDataTypeReady) {
            fillData(mDataBean);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, cardType);
        CommonUtil.startActivtiyForResult(this, ChooseCardTeamActivity.class, Constants.REQUEST_CODE1, bundle);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setListener() {
        findViewById(R.id.rl_diy).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DATA_TAG1, cardType);
            bundle.putSerializable(Constants.DATA_TAG2, userInfos);
            bundle.putSerializable(Constants.DATA_TAG3, mCardBean);
            CommonUtil.startActivtiyForResult(mContext, UserDiyCardActivity.class, Constants.REQUEST_CODE2, bundle);
        });
        findViewById(R.id.rl_share).setOnClickListener(v -> {
            shareCard();
        });
    }

    private void shareCard() {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_IMAGE);
        rlAction.setVisibility(View.GONE);
        final File file = ImageformatUtil.saveBitmapFile(mContext, svRoot.getDrawingCache());
        final String path = file.getAbsolutePath();
        shareParams.setImagePath(path);
        rlAction.setVisibility(View.VISIBLE);
        JShareInterface.share("Wechat", shareParams, new PlatActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastUtils.showToast(mContext, "分享成功");
            }

            @Override
            public void onError(Platform platform, int i, int i1, Throwable throwable) {
                ToastUtils.showToast(mContext, "分享失败");
                throwable.printStackTrace();
                Log.e("分享失败", platform.getName());
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
    }


    @Override
    protected void initData() {
        MainLogic.getInstance().queryEmployeeInfo(this, SPHelper.getEmployeeId(), new ProgressSubscriber<EmployeeDetailBean>(this, true) {
            @Override
            public void onNext(EmployeeDetailBean userInfoBean) {
                super.onNext(userInfoBean);
                isDataTypeReady = true;
                mDataBean = userInfoBean;
                if (isCardTypeReady) {
                    fillData(userInfoBean);
                }

            }
        });
        MainLogic.getInstance().queryEmployeeCard(this, SPHelper.getEmployeeId(), new ProgressSubscriber<EmployeeCardDetailBean>(this, true) {
            @Override
            public void onNext(EmployeeCardDetailBean cardBean) {
                super.onNext(cardBean);
                mCardBean = cardBean;
                if (mCardBean == null || mCardBean.getData() == null ||
                        TextUtils.isEmpty(mCardBean.getData().getChoice_template())) {
                    mCardBean = new EmployeeCardDetailBean();
                    EmployeeCardDetailBean.DataBean bean = new EmployeeCardDetailBean.DataBean();
                    bean.setChoice_template("0");
                    bean.setHide_set("");
                    bean.setCard_template(styleArr);
                    mCardBean.setData(bean);
                } else {
                    if (TextUtils.isEmpty(mCardBean.getData().getChoice_template())) {
                        mCardBean.getData().setChoice_template("0");
                    }
                    if (TextUtils.isEmpty(mCardBean.getData().getHide_set())) {
                        mCardBean.getData().setHide_set("");
                    }
                }
                initCard(mCardBean);
            }
        });

    }

    /**
     * 显示数据
     *
     * @param userInfoBean
     */
    private void fillData(EmployeeDetailBean userInfoBean) {
        EmployeeDetailBean.DataBean employeeDetailBean = userInfoBean.getData();
        EmployeeDetailBean.DataBean.EmployeeInfoBean employeeInfo = employeeDetailBean.getEmployeeInfo();
        EmployeeDetailBean.DataBean.CompanyInfoBean companyInfo = employeeDetailBean.getCompanyInfo();
        String employeeName = employeeInfo.getEmployee_name();
        String companyName = companyInfo.getCompany_name();
        String postName = employeeInfo.getPost_name();
        String phone = employeeInfo.getPhone();
        String website = companyInfo.getWebsite();
        String email = employeeInfo.getEmail();
        String address = employeeInfo.getRegion();
        if (!TextUtils.isEmpty(address)) {
            String[] split1 = address.split(",");
            if (split1 != null & split1.length > 0) {
                StringBuilder addressSb = new StringBuilder();
                for (int i = 0; i < split1.length; i++) {
                    String s = split1[i].substring(split1[i].indexOf(":") + 1, split1[i].length());
                    addressSb.append(s);
                }
                if (!TextUtils.isEmpty(addressSb.toString())) {
                    address = addressSb.toString();
                } else {
                    address = "";
                }
            }
        }
        String logo = companyInfo.getLogo();
        //Attention!!! 此处的添加顺序会影响自定义名片界面↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        //0
        userInfos.add(employeeName);
        //1
        userInfos.add(companyName);
        //2
        userInfos.add(postName);
        //3
        userInfos.add(phone);
//                userInfos.add(companyInfo.getAddress());
        //4
        userInfos.add(address);
        // TODO: 2018/8/6 座机
        //5
        userInfos.add(employeeInfo.getMobile_phone());
        //6
        userInfos.add(email);
        //7
        userInfos.add(website);
        //8
        userInfos.add(logo);
        //Attention!!! 此处的添加顺序会影响自定义名片界面萨很难过↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        Bitmap qrBitmap = CodeUtils.createImage(enQrCodeOneContact(userInfos),
                (int) DeviceUtils.dpToPixel(mContext, 240),
                (int) DeviceUtils.dpToPixel(mContext, 240), null);
        qrCodeIv.setImageBitmap(qrBitmap);

        TextUtil.setText(tvName, employeeName);
        TextUtil.setText(tvPhone, phone);
        TextUtil.setText(tvPost, postName);
        TextUtil.setText(tvCompanyName, companyName);
        TextUtil.setText(tvEmail, email);
        TextUtil.setText(tvAddress, address);
        TextUtil.setText(tvFixedPhone, employeeInfo.getMobile_phone());
        ImageLoader.loadImage(mContext, ivLogo, logo, R.drawable.blank);
        svRoot.setDrawingCacheEnabled(true);
    }

    public String enQrCodeOneContact(List<String> nameCard) {

        StringBuilder ss = new StringBuilder();
        ss.append(String.format("BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:%s", nameCard.get(0)))
                .append("\nORG:")
                .append(nameCard.get(1))
                .append("\nTITLE:")
                .append(nameCard.get(2))
                .append("\nTEL:")
                .append(nameCard.get(3))
                .append(String.format("\nADR;WORK:%s", nameCard.get(4)))
//                .append(String.format("\nADR;HOME:%s", nameCard.get(4)))
                .append(String.format("\nTEL;WORK;VOICE:%s", nameCard.get(5)))
                //.append("\nURL:")
                //.append(nameCard.get(6))
                .append("\nEMAIL:")
                .append(nameCard.get(6))
                .append("\nEND:VCARD");
        return ss.toString();

    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK) {

            initData();
        }
        if (requestCode == Constants.REQUEST_CODE2 && resultCode == RESULT_OK) {

            initData();
        }
    }
}
