package com.hjhq.teamface.view.treeview.helper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.view.treeview.MyNodeViewFactory;
import com.hjhq.teamface.view.treeview.TreeNode;
import com.hjhq.teamface.view.treeview.TreeView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 * Describe：层级显示帮助类
 */

public class TreeViewHelper {
    private TreeNode root;
    private static TreeView treeView;
    private View view;


    /**
     * 公司人员架构
     *
     * @param activity
     * @param viewGroup
     * @param bean
     */
    public void buildEmployeeTree(Activity activity, ViewGroup viewGroup, GetDepartmentStructureBean bean) {
        root = TreeNode.root();
        treeView = new TreeView(root, activity, new MyNodeViewFactory());
        List<GetDepartmentStructureBean.MemberBean> list = bean.getData();
        buildUserTreeChild(root, list, 0);
        view = treeView.getView();
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewGroup.addView(view);
    }


    private void buildUserTreeChild(TreeNode parent, List<GetDepartmentStructureBean.MemberBean> list, int level) {
        level = level > 5 ? 5 : level;
        for (int i = 0; i < list.size(); i++) {
            TreeNode treeNode = new TreeNode(list.get(i).getName());
            treeNode.setCategoryId(list.get(i).getId() + "");
            treeNode.setLevel(level);
            parent.addChild(treeNode);

            List<GetDepartmentStructureBean.MemberBean> childList = list.get(i).getChildList();
            if (!CollectionUtils.isEmpty(childList)) {
                buildUserTreeChild(parent.getChildren().get(i), childList, level + 1);
            }
            List<Member> users = list.get(i).getUsers();
            if (!CollectionUtils.isEmpty(users)) {
                for (Member userBean : users) {
                    TreeNode usetTreeNode = new TreeNode(userBean.getEmployee_name());
                    usetTreeNode.setCategoryId(userBean.getId() + "");
                    usetTreeNode.setLevel(-1);
                    usetTreeNode.setSubText(userBean.getPost_name());
                    usetTreeNode.setPicture(userBean.getPicture());
                    parent.getChildren().get(i).addChild(usetTreeNode);
                }
            }
        }
    }

    public TreeView getTreeView() {
        return this.treeView;
    }

}
