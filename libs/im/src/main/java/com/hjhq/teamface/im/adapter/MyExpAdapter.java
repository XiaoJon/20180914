package com.hjhq.teamface.im.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.im.R;


/**
 * Created by Administrator on 2017/5/9.
 */

public class MyExpAdapter extends BaseExpandableListAdapter {

    Context context;
    //Model：定义的数据
    String[] groups = {"A1", "A2", "A3", "A4"} ;

    //注意，字符数组不要写成{{"A1,A2,A3,A4"}, {"B1,B2,B3,B4，B5"}, {"C1,C2,C3,C4"}}
    String[][] childs = {{"A1", "A2", "A3", "A4"}, {"A1", "A2", "A3", "B4"}, {"A1", "A2", "A3", "C4"}};

    public MyExpAdapter(Context context) {

        this.context = context;


    }
    public MyExpAdapter(Context context,String[] groups) {

        this.context = context;
        this.groups = groups;


    }

    //返回一级列表的个数
    @Override
    public int getGroupCount() {
        return groups.length;
    }

    //返回每个二级列表的个数
    @Override
    public int getChildrenCount(int groupPosition) { //参数groupPosition表示第几个一级列表
        Log.d("smyhvae", "-->" + groupPosition);
        return childs[groupPosition].length;
    }

    //返回一级列表的单个item（返回的是对象）
    @Override
    public Object getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    //返回二级列表中的单个item（返回的是对象）
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs[groupPosition][childPosition]; //不要误写成groups[groupPosition][childPosition]
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //每个item的id是否是固定？一般为true
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //【重要】填充一级列表
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.item_company_department, null);


        } else {
            convertView.getTag();
        }
        TextView tv_group = (TextView) convertView.findViewById(R.id.department_name_and_member_num);
        tv_group.setText(groups[groupPosition]);
        return convertView;
    }

    //【重要】填充二级列表
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_company_member, null);
        }

        ImageView iv_child = (ImageView) convertView.findViewById(R.id.company_member_avatar_iv);
        TextView tv_child = (TextView) convertView.findViewById(R.id.member_name_and_member_num);

        //iv_child.setImageResource(resId);
        tv_child.setText(childs[groupPosition][childPosition]);

        return convertView;
    }

    //二级列表中的item是否能够被选中？可以改为true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
