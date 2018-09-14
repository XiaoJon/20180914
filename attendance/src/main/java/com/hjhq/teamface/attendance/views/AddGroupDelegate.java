package com.hjhq.teamface.attendance.views;

import android.text.TextUtils;
import android.widget.EditText;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class AddGroupDelegate extends AppDelegate {

    EditText etName;

    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_add_rules_header;
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
        setRightMenuTexts(R.color.app_blue, "确定");
        etName = get(R.id.et_name1);
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
