/*
 * Copyright 2016 - 2017 ShineM (Xinyuan)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under.
 */

package com.hjhq.teamface.view.treeview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.hjhq.teamface.view.treeview.animator.TreeItemAnimator;
import com.hjhq.teamface.view.treeview.base.BaseNodeViewFactory;
import com.hjhq.teamface.view.treeview.base.SelectableTreeAction;
import com.hjhq.teamface.view.treeview.helper.TreeHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xinyuanzhong on 2017/4/20.
 */

public class TreeView implements SelectableTreeAction {
    private TreeNode root;
    private boolean singleChoose = true;
    private boolean chooseChildNode = false;
    private boolean onlyDataItem = false;
    private boolean canSelect = true;
    private boolean canCancel = false;
    private boolean sendMessageWhenChange = false;
    private boolean chooseFirstNode = false;

    private Context context;

    private BaseNodeViewFactory baseNodeViewFactory;

    private RecyclerView rootView;

    private TreeViewAdapter adapter;

    private boolean itemSelectable = true;

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        this.itemAnimator = itemAnimator;
        if (rootView != null && itemAnimator != null) {
            rootView.setItemAnimator(itemAnimator);
        }
    }

    RecyclerView.ItemAnimator itemAnimator;

    public TreeView(@NonNull TreeNode root, @NonNull Context context, @NonNull BaseNodeViewFactory baseNodeViewFactory) {
        this.root = root;
        this.context = context;
        this.baseNodeViewFactory = baseNodeViewFactory;
        if (baseNodeViewFactory == null) {
            throw new IllegalArgumentException("You must assign a BaseNodeViewFactory!");
        }
    }

    public View getView() {
        if (rootView == null) {
            this.rootView = buildRootView();
        }

        return rootView;
    }

    @NonNull
    private RecyclerView buildRootView() {

        RecyclerView recyclerView = new RecyclerView(context);
        /**
         * disable multi touch event to prevent terrible data set error when calculate list.
         */
        recyclerView.setMotionEventSplittingEnabled(false);

        recyclerView.setItemAnimator(itemAnimator != null ? itemAnimator : new TreeItemAnimator());
        SimpleItemAnimator itemAnimator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TreeViewAdapter(context, root, baseNodeViewFactory);
        adapter.setCanSelect(canSelect);
        adapter.setSingleChoose(singleChoose);
        adapter.setChooseChildNode(chooseChildNode);
        adapter.setSingleItem(onlyDataItem);
        adapter.setCanCancel(canCancel);
        adapter.setNotice(sendMessageWhenChange);
        adapter.chooseFirstNode(chooseFirstNode);
        adapter.setTreeView(this);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void expandAll() {
        if (root == null) {
            return;
        }
        TreeHelper.expandAll(root);

        refreshTreeView();
    }


    public void refreshTreeView() {
        if (rootView != null) {
            ((TreeViewAdapter) rootView.getAdapter()).refreshView();
        }
    }

    @Override
    public void expandNode(TreeNode treeNode) {
        adapter.expandNode(treeNode);
    }

    @Override
    public void expandLevel(int level) {
        TreeHelper.expandLevel(root, level);

        refreshTreeView();
    }

    @Override
    public void collapseAll() {
        if (root == null) {
            return;
        }
        TreeHelper.collapseAll(root);

        refreshTreeView();
    }

    @Override
    public void collapseNode(TreeNode treeNode) {
        adapter.collapseNode(treeNode);
    }

    @Override
    public void collapseLevel(int level) {
        TreeHelper.collapseLevel(root, level);

        refreshTreeView();
    }

    @Override
    public void toggleNode(TreeNode treeNode) {
        if (treeNode.isExpanded()) {
            collapseNode(treeNode);
        } else {
            expandNode(treeNode);
        }
    }

    @Override
    public void deleteNode(TreeNode node) {
        adapter.deleteNode(node);
    }

    @Override
    public void addNode(TreeNode parent, TreeNode treeNode) {
        parent.addChild(treeNode);

        refreshTreeView();
    }

    @Override
    public List<TreeNode> getAllNodes() {
        return TreeHelper.getAllNodes(root);
    }

    @Override
    public void selectNode(TreeNode treeNode) {
        if (treeNode != null) {
            adapter.selectNode(true, treeNode);
        }
    }

    @Override
    public void deselectNode(TreeNode treeNode) {
        if (treeNode != null) {
            adapter.selectNode(false, treeNode);
        }
    }

    @Override
    public void selectAll() {
        TreeHelper.selectNodeAndChild(root, true);

        refreshTreeView();
    }

    @Override
    public void deselectAll() {
        TreeHelper.selectNodeAndChild(root, false);

        refreshTreeView();
    }

    @Override
    public List<TreeNode> getSelectedNodes() {
        return TreeHelper.getSelectedNodes(root);
    }

    public boolean isItemSelectable() {
        return itemSelectable;
    }

    public void setItemSelectable(boolean itemSelectable) {
        this.itemSelectable = itemSelectable;
    }

    //单选或多选
    public void setSingleChoose(boolean b) {
        this.singleChoose = b;
        if (adapter != null) {
            adapter.setSingleChoose(b);
        }
    }

    //是否选择子节点
    public void setChooseChildNode(boolean b) {
        this.chooseChildNode = b;
        if (adapter != null) {
            adapter.setChooseChildNode(b);
        }
    }

    //获取选中的节点
    public TreeNode getChoosedNode() {
        return adapter.getChoosedNode();
    }

    //获取选中的节点集合,**节点可能会有子节点
    public List<TreeNode> getChoosedNodeList() {

        return adapter.getNodeList(adapter.getRoot(), new ArrayList<TreeNode>());
    }

    //是否可以点击取消选中的节点
    public void setCanCancel(boolean b) {
        canCancel = b;
        if (adapter != null) {
            adapter.setCanCancel(b);
        }
    }

    /**
     * 是否只能选择数据节点
     *
     * @param b
     */
    public void onlyDataItem(boolean b) {
        onlyDataItem = b;
        if (adapter != null) {
            adapter.onlyDataItem(b);
        }
    }


    /**
     * 是否可选
     *
     * @param b
     */
    public void setCanSelect(boolean b) {
        canSelect = b;
        if (adapter != null) {
            adapter.setCanSelect(b);
        }
    }

    /**
     * 选择数据后是否发送通知
     *
     * @param b
     */

    public void sendMessageWhenChange(boolean b) {
        sendMessageWhenChange = b;
        if (adapter != null) {
            adapter.setNotice(b);
        }

    }

    /**
     * 选中第一个节点
     *
     * @param flag
     */
    public void chooseFirstNode(boolean flag) {
        this.chooseFirstNode = flag;
        if (adapter != null) {
            adapter.chooseFirstNode(flag);
        }
    }
}
