package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.EmployeeDetailBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.RegionUtil;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import kankan.wheel.widget.regionselect.ProvinceSelectActivity;
import kankan.wheel.widget.regionselect.model.Result;


/**
 * 个人信息
 *
 * @author lx
 * @date 2017/6/15
 */

public class UserInfoActivity extends BaseTitleActivity {
    @Bind(R.id.iv_head_arrow)
    ImageView ivHeadArrow;
    @Bind(R.id.iv_sex_arrow)
    ImageView ivSexArrow;
    @Bind(R.id.iv_birthday_arrow)
    ImageView ivBirthdayArrow;
    @Bind(R.id.iv_location_arrow)
    ImageView ivLocationArrow;

    @Bind(R.id.iv_head_portrait)
    ImageView ivHeadPortrait;
    @Bind(R.id.ll_head_portrait)
    LinearLayout llHeadPortrait;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;
    @Bind(R.id.ll_birthday)
    LinearLayout llBirthday;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_fixed_phone)
    TextView tvFixedPhone;
    @Bind(R.id.tv_email)
    TextView tvEmail;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.ll_sex)
    LinearLayout llSex;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.ll_location)
    LinearLayout llLocation;
    String[] sexs = new String[]{"男", "女"};

    Calendar mCalendar;
    private EmployeeDetailBean.DataBean.EmployeeInfoBean employeeInfo;
    private String employeeId;
    private String regionCode;

    @Override
    protected int getChildView() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle("个人信息");
        employeeId = getIntent().getStringExtra(Constants.DATA_TAG1);
    }

    @Override
    protected void setListener() {
        setOnClicks(llHeadPortrait, llBirthday, llSex, llLocation, tvPhone, tvFixedPhone);
    }

    @Override
    protected void initData() {
        //他人信息不可修改
        if (!SPHelper.getEmployeeId().equals(employeeId)) {
            llHeadPortrait.setEnabled(false);
            llBirthday.setEnabled(false);
            llSex.setEnabled(false);
            llLocation.setEnabled(false);
            ivHeadArrow.setVisibility(View.GONE);
            ivSexArrow.setVisibility(View.GONE);
            ivBirthdayArrow.setVisibility(View.GONE);
            ivLocationArrow.setVisibility(View.GONE);
        }
        MainLogic.getInstance().queryEmployeeInfo(this, employeeId, new ProgressSubscriber<EmployeeDetailBean>(this, true) {
            @Override
            public void onNext(EmployeeDetailBean userInfoBean) {
                super.onNext(userInfoBean);
                employeeInfo = userInfoBean.getData().getEmployeeInfo();
                setUserInfo();
            }
        });
    }

    /**
     * 设置个人信息
     */
    private void setUserInfo() {
        TextUtil.setText(tvPhone, employeeInfo.getPhone());
        TextUtil.setText(tvEmail, employeeInfo.getEmail());
        regionCode = employeeInfo.getRegion();
        TextUtil.setText(tvLocation, RegionUtil.code2String(regionCode));
        TextUtil.setText(tvFixedPhone, employeeInfo.getMobile_phone());

        String sex = employeeInfo.getSex();
        if (!TextUtil.isEmpty(sex)) {
            tvSex.setText(sexs[TextUtil.parseInt(sex)]);
        }
        ImageLoader.loadCircleImage(mContext, employeeInfo.getPicture(), ivHeadPortrait, employeeInfo.getEmployee_name());

        String birth = employeeInfo.getBirth();
        if (TextUtil.isEmpty(birth)) {
            return;
        }
        try {
            long l = new BigDecimal(birth).longValue();
            if (l != 0) {
                mCalendar = Calendar.getInstance();
                mCalendar.setTimeInMillis(l);
                setBirthday();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置生日
     */
    private void setBirthday() {
        StringBuilder sb = new StringBuilder();
        if (mCalendar != null) {
            int month = mCalendar.get(Calendar.MONTH) + 1;
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            sb.append(mCalendar.get(Calendar.YEAR))
                    .append("-")
                    .append(month < 10 ? "0" + month : month)
                    .append("-")
                    .append(day < 10 ? "0" + day : day);
        }
        TextUtil.setText(tvBirthday, sb.toString());
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_head_portrait:
                selectedHeadPortrait();
                break;
            case R.id.ll_birthday:
                bundle.putSerializable(DateTimeSelectPresenter.CALENDAR, mCalendar != null ? mCalendar : Calendar.getInstance());
                bundle.putString(DateTimeSelectPresenter.FORMAT, DateTimeSelectPresenter.YYYY_MM_DD);
                CommonUtil.startActivtiyForResult(this, DateTimeSelectPresenter.class, Constants.REQUEST_CODE1, bundle);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_sex:
                selectedSex();
                break;
            case R.id.ll_location:
                bundle.putString(ProvinceSelectActivity.DEFAULT_SELECT_KEY, RegionUtil.getArea(regionCode));
                CommonUtil.startActivtiyForResult(this, ProvinceSelectActivity.class, Constants.REQUEST_CODE2, bundle);
                overridePendingTransition(0,0);
                break;
            case R.id.tv_phone:
                call(tvPhone.getText().toString().trim());
                break;
            case R.id.tv_fixed_phone:
                call(tvFixedPhone.getText().toString().trim());
                break;
            default:
                break;
        }
    }


    /**
     * 打电话
     */
    private void call(String phone) {
        if (TextUtil.isEmpty(phone)) {
            return;
        }
        DialogUtils.getInstance().sureOrCancel(this, phone, null, "呼叫", "取消", tvPhone, () -> SystemFuncUtils.callPhone(UserInfoActivity.this, phone));
    }

    /**
     * 选择性别
     */
    private void selectedSex() {
        PopUtils.showBottomMenu(this, getContainer(), "选择性别", sexs, position -> {
            tvSex.setText(sexs[position]);
            Map<String, String> map = new HashMap<>(1);
            map.put("sex", position + "");
            editEmployeeDetail(map);
            return true;
        });
    }

    /**
     * 更换头像
     */
    private void selectedHeadPortrait() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, employeeInfo.getPicture() == null ? "" : employeeInfo.getPicture());
        bundle.putString(Constants.DATA_TAG2, employeeInfo.getEmployee_name());
        bundle.putBoolean(Constants.DATA_TAG3, true);
        CommonUtil.startActivtiy(this, ChangeAvatarActivity.class, bundle);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1) {
            //截止时间
            if (resultCode == RESULT_OK && data != null) {
                mCalendar = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                Map<String, String> map = new HashMap<>(1);
                map.put("birth", mCalendar.getTimeInMillis() + "");
                editEmployeeDetail(map);
                setBirthday();
            } else if (resultCode == Constants.CLEAR_RESULT_CODE) {
                mCalendar = null;
                Map<String, String> map = new HashMap<>(1);
                map.put("birth", "");
                editEmployeeDetail(map);
                setBirthday();
            }
        } else if (requestCode == Constants.REQUEST_CODE2 && resultCode == ProvinceSelectActivity.SUCESS) {
            Result result = (Result) data.getSerializableExtra(ProvinceSelectActivity.RESULT_KEY);
            String provinceName = result.getProvinceName();
            String cityName = result.getCityName();
            String districtName = result.getDistrictName();
            StringBuilder builder = new StringBuilder();

            regionCode = "";
            if (!TextUtils.isEmpty(provinceName)) {
                builder.append(provinceName);
                regionCode += result.getProvinceID() + ":" + provinceName;
            }
            if (!TextUtils.isEmpty(cityName)) {
                builder.append(cityName);
                regionCode += "," + result.getCityID() + ":" + cityName;
            }
            if (!TextUtils.isEmpty(districtName)) {
                builder.append(districtName);
                regionCode += "," + result.getDistrictID() + ":" + districtName;
            }

            TextUtil.setText(tvLocation, builder.toString());
            Map<String, String> map = new HashMap<>(1);
            map.put("region", regionCode);
            editEmployeeDetail(map);
        }
    }


    /**
     * 修改个人信息(头像除外)
     *
     * @param map
     */
    private void editEmployeeDetail(Map<String, String> map) {
        editEmployeeDetail(map, false);
    }

    /**
     * 修改个人信息
     *
     * @param map        参数信息
     * @param editAvatar 是否修改头像
     */
    private void editEmployeeDetail(Map<String, String> map, boolean editAvatar) {
        MainLogic.getInstance().editEmployeeDetail(this, map, new ProgressSubscriber<BaseBean>(this, false) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                if (editAvatar) {
                    SPHelper.setUserAvatar(map.get("picture"));
                }
                initData();
                EventBusUtils.sendEvent(new MessageBean(Constants.EDIT_USER_INFO, "修改个人信息", null));
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(MessageBean event) {
        if (event.getCode() == Constants.EDIT_USER_INFO) {
            initData();
        }
    }

}
