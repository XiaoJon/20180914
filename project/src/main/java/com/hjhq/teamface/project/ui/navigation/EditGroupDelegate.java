package com.hjhq.teamface.project.ui.navigation;

import android.widget.EditText;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 新增和编辑导航标题
 *
 * @author Administrator
 * @date 2018/4/16
 */

public class EditGroupDelegate extends AppDelegate {
    private EditText etName;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_edit_navigation;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm));
        etName = get(R.id.et_name);
    }

    public void setHint(String hint){
        TextUtil.setHint(etName,hint);
    }
    public String getContent() {
        return etName.getText().toString().trim();
    }

    public void setNavigation(String navigation) {
        TextUtil.setText(etName, navigation);
    }
}
