package com.hjhq.teamface.project.ui.navigation;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 新增任务分组
 *
 * @author Administrator
 * @date 2018/4/16
 */

public class AddGroupDelegate extends AppDelegate {
    private EditText etName;
    private LinearLayout llTaskList;
    private TextView tvContent;
    private ImageView ivRight;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_add_task_group;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_add_group);
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm));
        etName = get(R.id.et_group_name);

        llTaskList = get(R.id.ll_task_list);
        tvContent = get(R.id.tv_content);
        ivRight = get(R.id.iv_right);
        addTaskList();
    }

    public String getContent() {
        return etName.getText().toString().trim();
    }

    /**
     * 添加任务列表
     */
    public void addTaskList() {
        int childCount = llTaskList.getChildCount();
        if (childCount > 0) {
            View childAt = llTaskList.getChildAt(childCount - 1);
            childAt.findViewById(R.id.et_task_list_name).setEnabled(false);
            childAt.findViewById(R.id.iv_del).setVisibility(View.VISIBLE);
        }

        View taskListLayout = View.inflate(mContext, R.layout.project_task_group_add_list, null);
        ImageView ivDel = taskListLayout.findViewById(R.id.iv_del);
        ivDel.setOnClickListener(v -> llTaskList.removeView(taskListLayout));
        llTaskList.addView(taskListLayout);
    }

    /**
     * 判断任务列表是否输入完成
     *
     * @return
     */
    public boolean isAddFinish() {
        int childCount = llTaskList.getChildCount();
        if (childCount > 0) {
            View childAt = llTaskList.getChildAt(childCount - 1);
            EditText etTaskListName = childAt.findViewById(R.id.et_task_list_name);
            String taskListName = etTaskListName.getText().toString().trim();
            if (TextUtil.isEmpty(taskListName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回任务列表名称集合
     */
    public JSONArray getTaskListName() {
        JSONArray jsonArray = new JSONArray();
        int childCount = llTaskList.getChildCount();
        for (int i = 0; i < childCount; i++) {
            EditText etTaskListName = llTaskList.getChildAt(i).findViewById(R.id.et_task_list_name);
            String taskListName = etTaskListName.getText().toString().trim();
            if (!TextUtil.isEmpty(taskListName)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", taskListName);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    public void showFlow(String flowName) {
        setVisibility(R.id.ll_task_list, false);
        setVisibility(R.id.ll_add_task_list, false);
        setVisibility(R.id.tv_hint, false);

        setVisibility(R.id.tv_content, true);
        TextUtil.setText(tvContent, flowName);
        ImageLoader.loadImage(mContext, R.drawable.clear_button, ivRight);
    }

    public void clearFlow() {
        setVisibility(R.id.ll_task_list, true);
        setVisibility(R.id.ll_add_task_list, true);
        setVisibility(R.id.tv_hint, true);

        setVisibility(R.id.tv_content, false);
        ImageLoader.loadImage(mContext, R.drawable.project_icon_next, ivRight);
    }
}
