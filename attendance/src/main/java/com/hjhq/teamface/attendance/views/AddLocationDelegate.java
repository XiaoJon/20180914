package com.hjhq.teamface.attendance.views;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class AddLocationDelegate extends AppDelegate {
    TextView tvAddress;
    TextView tvRange;
    EditText etName;

    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_add_location_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.attendance_add_location_title);
        // TODO: 2018/6/8 蓝色带返回字样切图 
        getToolbar().setNavigationIcon(R.drawable.icon_blue_back);
        setRightMenuTexts(R.color.app_blue, "保存");
        tvAddress = get(R.id.tv_location);
        tvRange = get(R.id.tv_num);
        etName = get(R.id.et_name1);
    }

    /**
     * 地址
     *
     * @param address
     */
    public void setAddress(String address) {
        TextUtil.setText(tvAddress, address);
    }

    /**
     * 范围
     *
     * @param s
     */
    public void setRangeText(String s) {
        TextUtil.setText(tvRange, s);
    }

    /**
     * 获取输入的名字
     *
     * @return
     */
    public String getName() {
        String name = "";
        if (!TextUtils.isEmpty(etName.getText())) {
            name = etName.getText().toString();
        }
        return name;
    }

    public void setName(String name) {
        TextUtil.setText(etName, name);
    }
}
