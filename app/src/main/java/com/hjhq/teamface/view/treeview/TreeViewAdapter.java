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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.view.treeview.base.BaseNodeViewBinder;
import com.hjhq.teamface.view.treeview.base.BaseNodeViewFactory;
import com.hjhq.teamface.view.treeview.base.CheckableNodeViewBinder;
import com.hjhq.teamface.view.treeview.helper.TreeHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xinyuanzhong on 2017/4/21.
 */

public class TreeViewAdapter extends RecyclerView.Adapter {
    //初始选中第一条
    private boolean flag = false;
    //不选择子节点
    private boolean chooseChildNode = false;
    //单选
    private boolean singleChoose = true;
    //单选条目(只显示内容条目,不显示上级)
    private boolean singleItem = true;
    //选中后不可取消,只能选中其他节点来取消
    private boolean canCancel = false;
    //单选时选中后发出通知
    private boolean notice = false;
    //只能选择数据节点
    private boolean onlyDataItem = false;
    private boolean canSelect = true;
    private Context context;
    private TreeNode choosedNode;
    //选中的节点集合
    private List<TreeNode> nodeList = new ArrayList<>();
    /**
     * Tree root.
     */
    private TreeNode root;
    /**
     * The current data set of Adapter,which means excluding the collapsed nodes.
     */
    private List<TreeNode> expandedNodeList;
    /**
     * The binder factory.A binder provide the layoutId which needed in method
     * <code>onCreateViewHolder</code> and the way how to render ViewHolder.
     */
    private BaseNodeViewFactory baseNodeViewFactory;
    /**
     * This parameter make no sense just for avoiding IllegalArgumentException of ViewHolder's
     * constructor.
     */
    private View EMPTY_PARAMETER;

    private TreeView treeView;

    public TreeViewAdapter(Context context, TreeNode root,
                           @NonNull BaseNodeViewFactory baseNodeViewFactory) {
        this.context = context;
        this.root = root;
        this.baseNodeViewFactory = baseNodeViewFactory;

        this.EMPTY_PARAMETER = new View(context);
        this.expandedNodeList = new ArrayList<>();

        buildExpandedNodeList();
    }

    private void buildExpandedNodeList() {
        expandedNodeList.clear();

        for (TreeNode child : root.getChildren()) {
            insertNode(expandedNodeList, child);
        }
    }

    private void insertNode(List<TreeNode> nodeList, TreeNode treeNode) {
        nodeList.add(treeNode);

        if (!treeNode.hasChild()) {
            return;
        }
        if (treeNode.isExpanded()) {
            for (TreeNode child : treeNode.getChildren()) {
                insertNode(nodeList, child);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return expandedNodeList.get(position).getLevel();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int level) {
        View view = LayoutInflater.from(context).inflate(baseNodeViewFactory
                .getNodeViewBinder(EMPTY_PARAMETER, level).getLayoutId(), parent, false);

        BaseNodeViewBinder nodeViewBinder = baseNodeViewFactory.getNodeViewBinder(view, level);
        nodeViewBinder.setTreeView(treeView);
        return nodeViewBinder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final View nodeView = holder.itemView;
        final TreeNode treeNode = expandedNodeList.get(position);
        final BaseNodeViewBinder viewBinder = (BaseNodeViewBinder) holder;


        if (viewBinder.getToggleTriggerViewId() != 0) {
            View triggerToggleView = nodeView.findViewById(viewBinder.getToggleTriggerViewId());

            if (triggerToggleView != null) {
                triggerToggleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNodeToggled(treeNode);
                        viewBinder.onNodeToggled(treeNode, treeNode.isExpanded());
                    }
                });
            }
        } else if (treeNode.getChildren() != null && treeNode.getChildren().size() > 0 && treeNode.isItemClickEnable()) {
            nodeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNodeToggled(treeNode);
                    viewBinder.onNodeToggled(treeNode, treeNode.isExpanded());
                }
            });
        }

        if (viewBinder instanceof CheckableNodeViewBinder) {
            final View view = nodeView
                    .findViewById(((CheckableNodeViewBinder) viewBinder).getCheckableViewId());

            if (view != null && view instanceof CheckBox) {
                final CheckBox checkableView = (CheckBox) view;
                if (!canSelect) {
                    checkableView.setVisibility(View.GONE);
                }
                if (onlyDataItem && treeNode.getLevel() != -1) {
                    checkableView.setVisibility(View.GONE);
                }

                //首次初始化默认选中第一个节点
                if (position == 0 && flag) {
                    checkableView.setChecked(true);
                    choosedNode = treeNode;
                    flag = false;
                } else {
                    checkableView.setChecked(treeNode.isSelected());
                }


                checkableView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean checked = checkableView.isChecked();
                        if (singleChoose) {
                            treeView.deselectAll();
                        }
                        try {

                            if (choosedNode.equals(treeNode)) {
                                if (!canCancel) {

                                    checked = true;
                                    checkableView.setChecked(true);
                                    choosedNode = treeNode;
                                }
                            }
                        } catch (Exception e) {

                        }
                        changeNodeList(treeNode);

                        if (checked) {
                            choosedNode = treeNode;
                            if (notice) {
                                EventBusUtils.sendEvent(choosedNode);
                            }

                        } else {
                            choosedNode = null;
                        }
                        selectNode(checked, treeNode);
                        ((CheckableNodeViewBinder) viewBinder).onNodeSelectedChanged(treeNode, checked);
                    }
                });

            } else {
                throw new ClassCastException("The getCheckableViewId() " +
                        "must return a CheckBox's id");
            }
        }

        viewBinder.bindView(treeNode);
    }

    private void changeNodeList(TreeNode treeNode) {
        boolean flag = true;
        Iterator<TreeNode> it = nodeList.iterator();
        while (it.hasNext()) {
            if (it.next().getCategoryId() == treeNode.getCategoryId()) {
                it.remove();
                flag = false;
            }
        }
        if (flag) {
            nodeList.add(treeNode);

        }


    }

    public void selectNode(boolean checked, TreeNode treeNode) {
        treeNode.setSelected(checked);
//        LogUtil.e("id===" + treeNode.getId());
//        LogUtil.e("categoryId===" + treeNode.getCategoryId());
//        LogUtil.e("getLevel===" + treeNode.getLevel());
//        LogUtil.e("value===" + treeNode.getValue());
//        LogUtil.e("parent===" + treeNode.getParent());
//        LogUtil.e("checked===" + checked);
        //是否选择子节点
        if (chooseChildNode) {
            selectChildren(treeNode, checked);
            selectParentIfNeed(treeNode, checked);
        }

    }

    private void selectChildren(TreeNode treeNode, boolean checked) {
        List<TreeNode> impactedChildren = TreeHelper.selectNodeAndChild(treeNode, checked);
        int index = expandedNodeList.indexOf(treeNode);
        if (index != -1 && impactedChildren.size() > 0) {
            notifyItemRangeChanged(index, impactedChildren.size() + 1);
        }
    }

    private void selectParentIfNeed(TreeNode treeNode, boolean checked) {
        List<TreeNode> impactedParents = TreeHelper.selectParentIfNeedWhenNodeSelected(treeNode, checked);
        if (impactedParents.size() > 0) {
            for (TreeNode parent : impactedParents) {
                int position = expandedNodeList.indexOf(parent);
                if (position != -1) {
                    notifyItemChanged(position);
                }
            }
        }
    }

    private void onNodeToggled(TreeNode treeNode) {
        treeNode.setExpanded(!treeNode.isExpanded());

        if (treeNode.isExpanded()) {
            expandNode(treeNode);
        } else {
            collapseNode(treeNode);
        }
    }

    @Override
    public int getItemCount() {
        return expandedNodeList == null ? 0 : expandedNodeList.size();
    }

    /**
     * Refresh all,this operation is only used for refreshing list when a large of nodes have
     * changed value or structure because it take much calculation.
     */
    public void refreshView() {
        buildExpandedNodeList();
        notifyDataSetChanged();
    }

    /**
     * Insert a node list after index.
     *
     * @param index         the index before new addition nodes's first position
     * @param additionNodes nodes to add
     */
    private void insertNodesAtIndex(int index, List<TreeNode> additionNodes) {
        if (index < 0 || index > expandedNodeList.size() - 1 || additionNodes == null) {
            return;
        }
        expandedNodeList.addAll(index + 1, additionNodes);
        notifyItemRangeInserted(index + 1, additionNodes.size());
    }

    /**
     * Remove a node list after index.
     *
     * @param index        the index before the removedNodes nodes's first position
     * @param removedNodes nodes to remove
     */
    private void removeNodesAtIndex(int index, List<TreeNode> removedNodes) {
        if (index < 0 || index > expandedNodeList.size() - 1 || removedNodes == null) {
            return;
        }
        expandedNodeList.removeAll(removedNodes);
        notifyItemRangeRemoved(index + 1, removedNodes.size());
    }

    /**
     * Expand node. This operation will keep the structure of children(not expand children)
     *
     * @param treeNode
     */
    public void expandNode(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        List<TreeNode> additionNodes = TreeHelper.expandNode(treeNode, false);
        int index = expandedNodeList.indexOf(treeNode);

        insertNodesAtIndex(index, additionNodes);
    }


    /**
     * Collapse node. This operation will keep the structure of children(not collapse children)
     *
     * @param treeNode
     */
    public void collapseNode(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        List<TreeNode> removedNodes = TreeHelper.collapseNode(treeNode, false);
        int index = expandedNodeList.indexOf(treeNode);

        removeNodesAtIndex(index, removedNodes);
    }

    /**
     * Delete a node from list.This operation will also delete its children.
     *
     * @param node
     */
    public void deleteNode(TreeNode node) {
        if (node == null || node.getParent() == null) {
            return;
        }
        List<TreeNode> allNodes = TreeHelper.getAllNodes(root);
        if (allNodes.indexOf(node) != -1) {
            node.getParent().removeChild(node);
        }

        //remove children form list before delete
        collapseNode(node);

        int index = expandedNodeList.indexOf(node);
        if (index != -1) {
            expandedNodeList.remove(node);
        }
        notifyItemRemoved(index);
    }

    public void setTreeView(TreeView treeView) {

        this.treeView = treeView;
    }

    //是否选中子节点
    public boolean isChooseChildNode() {
        return chooseChildNode;
    }

    public void setChooseChildNode(boolean chooseChildNode) {
        this.chooseChildNode = chooseChildNode;
    }

    //是否可以点击取消选中的节点
    public void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }

    //单选
    public boolean isSingleChoose() {
        return singleChoose;
    }

    public void setSingleChoose(boolean singleChoose) {
        this.singleChoose = singleChoose;
    }

    //选中的节点
    public TreeNode getChoosedNode() {
        return choosedNode;
    }

    //选中的节点集合
    public List<TreeNode> getNodeList(TreeNode treeNode, List<TreeNode> list) {
        if (treeNode.hasChild()) {
            for (int i = 0; i < treeNode.getChildren().size(); i++) {
                getNodeList(treeNode.getChildren().get(i), list);
            }
        } else {
            if (treeNode.isSelected()) {
                list.add(treeNode);
            }
        }
        return list;
    }

    public boolean isSingleItem() {
        return singleItem;
    }

    //只能选择数据节点
    public void setSingleItem(boolean singleItem) {
        this.singleItem = singleItem;
    }

    public void onlyDataItem(boolean singleItem) {
        this.onlyDataItem = singleItem;
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }

    public void setNotice(boolean sendMessageWhenChange) {
        notice = sendMessageWhenChange;
    }

    /**
     * 获取根节点
     *
     * @return
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * 初始化时选中第一个节点
     *
     * @param flag
     */
    public void chooseFirstNode(boolean flag) {
        this.flag = flag;
    }
}

