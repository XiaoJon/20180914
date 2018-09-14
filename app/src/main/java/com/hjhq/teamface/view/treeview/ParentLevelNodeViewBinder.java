package com.hjhq.teamface.view.treeview;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.view.treeview.base.CheckableNodeViewBinder;

/**
 * 父级NodeView
 */
public class ParentLevelNodeViewBinder extends CheckableNodeViewBinder {
    View nodeContainer;
    TextView textView;
    ImageView imageView;

    public ParentLevelNodeViewBinder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.node_name_view);
        imageView = (ImageView) itemView.findViewById(R.id.arrow_img);
        nodeContainer = itemView.findViewById(R.id.node_container);
    }

    @Override
    public int getCheckableViewId() {
        return R.id.checkBox;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_treeview_parent_level;
    }

    @Override
    public void bindView(TreeNode treeNode) {
        if (treeNode == null || treeNode.getValue() == null) {
            return;
        }
        textView.setText(treeNode.getValue().toString());
        if (treeNode.hasChild()) {
            imageView.setRotation(treeNode.isExpanded() ? 90 : 0);
        }
        int level = treeNode.getLevel();
        int margin = levelMargin * level;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) nodeContainer.getLayoutParams();
        layoutParams.setMargins(margin, 0, 0, 0);
        nodeContainer.setLayoutParams(layoutParams);

    }

    @Override
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        if (expand) {
            imageView.animate().rotation(90).setDuration(200).start();
        } else {
            imageView.animate().rotation(0).setDuration(200).start();
        }
    }

}
