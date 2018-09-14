package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.lib_zxing.activity.CodeUtils;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.basis.bean.EmployeeDetailBean;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 二维码名片
 *
 * @author Administrator
 */
public class UserQRCodeActivity extends BaseTitleActivity {
    public static final String TEAM_CODE = "teamCode";
    @Bind(R.id.fl_user_info)
    FrameLayout flUserInfo;
    @Bind(R.id.qr_code_iv)
    ImageView qrCodeIv;

    private List<String> userInfos = new ArrayList<>();
    private TextView tvName;
    private TextView tvPost;
    private TextView tvEmail;
    private TextView tvCompanyName;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvFixedPhone;
    private int teamCode;

    @Override
    protected int getChildView() {
        return R.layout.acitivity_user_qr_code;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle(R.string.my_card);
        setRightMenuColorTexts(R.color.black_4a, "模版");
        initTeam();
    }

    /**
     * 初始化模版
     */
    private void initTeam() {
        int layout;
        teamCode = (int) SPUtils.getLong(mContext, SPUtils.SP_NAME, TEAM_CODE, 0);
        switch (teamCode) {
            case 0:
                layout = R.layout.user_temp_01_layout;
                break;
            case 1:
                layout = R.layout.user_temp_02_layout;
                break;
            case 2:
                layout = R.layout.user_temp_03_layout;
                break;
            case 3:
                layout = R.layout.user_temp_04_layout;
                break;
            default:
                layout = R.layout.user_temp_01_layout;
                break;
        }
        View inflate = getLayoutInflater().inflate(layout, null);
        flUserInfo.addView(inflate);
        tvName = inflate.findViewById(R.id.tv_name);
        tvPost = inflate.findViewById(R.id.tv_post);
        tvEmail = inflate.findViewById(R.id.tv_email);
        tvCompanyName = inflate.findViewById(R.id.tv_company_name);
        tvAddress = inflate.findViewById(R.id.tv_address);
        tvPhone = inflate.findViewById(R.id.tv_phone);
        tvFixedPhone = inflate.findViewById(R.id.tv_fixed_phone);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, teamCode);
        CommonUtil.startActivtiyForResult(this, ChooseCardTeamActivity.class, Constants.REQUEST_CODE1, bundle);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setListener() {
    }


    @Override
    protected void initData() {
        MainLogic.getInstance().queryEmployeeInfo(this, SPHelper.getEmployeeId(), new ProgressSubscriber<EmployeeDetailBean>(this, true) {
            @Override
            public void onNext(EmployeeDetailBean userInfoBean) {
                super.onNext(userInfoBean);
                EmployeeDetailBean.DataBean employeeDetailBean = userInfoBean.getData();
                EmployeeDetailBean.DataBean.EmployeeInfoBean employeeInfo = employeeDetailBean.getEmployeeInfo();
                EmployeeDetailBean.DataBean.CompanyInfoBean companyInfo = employeeDetailBean.getCompanyInfo();


                String employeeName = employeeInfo.getEmployee_name();
                String companyName = companyInfo.getCompany_name();
                String postName = employeeInfo.getPost_name();
                String phone = employeeInfo.getPhone();
                String region = employeeInfo.getRegion();
                String website = companyInfo.getWebsite();
                String email = employeeInfo.getEmail();
                userInfos.add(employeeName);
                userInfos.add(companyName);
                userInfos.add(postName);
                userInfos.add(phone);
//                userInfos.add(companyInfo.getAddress());
                userInfos.add(region);
                userInfos.add(employeeInfo.getMobile_phone());
                userInfos.add(website);
                userInfos.add(email);
                Bitmap qrBitmap = CodeUtils.createImage(enQrCodeOneContact(userInfos), (int) DeviceUtils.dpToPixel(mContext, 240), (int) DeviceUtils.dpToPixel(mContext, 240), null);
                qrCodeIv.setImageBitmap(qrBitmap);

                TextUtil.setText(tvName, employeeName);
                TextUtil.setText(tvPhone, phone);
                TextUtil.setText(tvPost, postName);
                TextUtil.setText(tvCompanyName, companyName);
                TextUtil.setText(tvEmail, email);
                TextUtil.setText(tvAddress, region);
                TextUtil.setText(tvFixedPhone, employeeInfo.getMobile_phone());
            }
        });
    }

    /**
     * 组成NameCard名片格式
     *
     * @param nameCard
     * @return
     */
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
                .append("\nURL:")
                .append(nameCard.get(6))
                .append("\nEMAIL:")
                .append(nameCard.get(7))
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
            initTeam();
            initData();
        }
    }
}
