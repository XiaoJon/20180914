package com.hjhq.teamface.project.ui.navigation;

import android.widget.EditText;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 新增任务列表
 *
 * @author Administrator
 * @date 2018/4/16
 */

public class AddTaskTempDelegate extends AppDelegate {
    private EditText etName;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_add_task_temp;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("新建任务列表");
        setRightMenuTexts(mContext.getString(R.string.finish));
        etName = get(R.id.et_name);
    }

    public String getContent() {
        return etName.getText().toString().trim();
    }


    public void setNavigation(String navigation) {
        TextUtil.setText(etName, navigation);
    }
}
