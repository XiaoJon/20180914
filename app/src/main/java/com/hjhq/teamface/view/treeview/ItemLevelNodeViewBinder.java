package com.hjhq.teamface.view.treeview;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.view.treeview.base.CheckableNodeViewBinder;


public class ItemLevelNodeViewBinder extends CheckableNodeViewBinder {
    TextView nodeSubtext;
    ImageView picture;
    View line;
    View nodeContainer;
    TextView textView;

    public ItemLevelNodeViewBinder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.node_name_view);
        nodeSubtext = (TextView) itemView.findViewById(R.id.node_subtext);
        picture = (ImageView) itemView.findViewById(R.id.arrow_img);
        nodeContainer = itemView.findViewById(R.id.node_container);
        line = itemView.findViewById(R.id.line);
    }

    @Override
    public int getCheckableViewId() {
        return R.id.checkBox;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_treeview_item_level;
    }

    @Override
    public void bindView(TreeNode treeNode) {
        textView.setText(treeNode.getValue().toString());
        TextUtil.setText(nodeSubtext, treeNode.getSubText());
        ImageLoader.loadImage(textView.getContext(), treeNode.getPicture(), picture);

        int level = treeNode.getParent().getLevel();
        int margin = levelMargin * (level + 1);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) nodeContainer.getLayoutParams();
        layoutParams.setMargins(margin, 0, 0, 0);
        nodeContainer.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams lineLayoutParams = (RelativeLayout.LayoutParams) this.line.getLayoutParams();
        lineLayoutParams.setMargins(margin + 50, 0, 0, 0);
        this.line.setLayoutParams(lineLayoutParams);

    }
}
