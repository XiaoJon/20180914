package com.hjhq.teamface.attendance.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.SelectAdapter;
import com.hjhq.teamface.attendance.bean.AddDateBean;
import com.hjhq.teamface.attendance.bean.AttendanceTypeListBean;
import com.hjhq.teamface.attendance.bean.BaseSelectBean;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/15.
 * Describe：
 */

public class SelectView extends LinearLayout {
    final public static int RADIO = 1001;
    final public static int MULTI = 1002;

    SelectAdapter mSelectAdapter;
    private Context mContext;
    private String name;
    private int type;
    TextView tvName;
    TextView tvValue;
    ImageView ivGuide;
    RecyclerView mRecyclerView;
    RelativeLayout rlHeader;
    List<BaseSelectBean> dataList = new ArrayList<>();

    public SelectView(Context context, String name, int type) {
        super(context);
        this.mContext = context;
        this.name = name;
        this.type = type;
        initView();
    }

    private void initView() {
        View view = inflate(mContext, R.layout.attendance_select_view, null);
        addView(view);
        tvName = view.findViewById(R.id.tv1);
        tvValue = view.findViewById(R.id.tv2);
        ivGuide = view.findViewById(R.id.iv_leading_mark);
        mRecyclerView = view.findViewById(R.id.rv1);
        rlHeader = view.findViewById(R.id.rl13);
        tvName.setText(name);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mSelectAdapter = new SelectAdapter(dataList);
        mRecyclerView.setAdapter(mSelectAdapter);
        mRecyclerView.setVisibility(GONE);
        rlHeader.setOnClickListener(v -> {
            if (mRecyclerView.getVisibility() == View.VISIBLE) {
                mRecyclerView.setVisibility(GONE);
                ivGuide.setImageResource(R.drawable.attendance_closed_icon);
            } else {
                mRecyclerView.setVisibility(VISIBLE);
                ivGuide.setImageResource(R.drawable.attendance_open_icon);
            }
        });
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (type) {
                    case RADIO:
                        for (int i = 0; i < dataList.size(); i++) {
                            dataList.get(i).setCheck(false);
                        }
                        dataList.get(position).setCheck(true);
                        mSelectAdapter.notifyDataSetChanged();
                        setValue(position);
                        break;
                    case MULTI:
                        dataList.get(position).setCheck(!dataList.get(position).isCheck());
                        mSelectAdapter.notifyDataSetChanged();
                        break;
                }
                super.onItemClick(adapter, view, position);
            }
        });

    }

    public void setData(List<BaseSelectBean> list) {
        if (list == null || list.size() <= 0) {
            mSelectAdapter.getData().clear();
            mSelectAdapter.notifyDataSetChanged();
        }
        dataList.clear();
        dataList.addAll(list);
        mSelectAdapter.notifyDataSetChanged();

    }

    public List<BaseSelectBean> getData() {

        List<BaseSelectBean> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isCheck()) {
                list.add(dataList.get(i));
            }
        }
        return list;
    }

    public void setValue(int index) {
        if (index < 0 || index > dataList.size() - 1) {
            return;
        }
        BaseSelectBean bean = dataList.get(index);

        switch (bean.getItemType()) {
            case AttendanceConstants.TYPE_SELECT_TIME:
                AddDateBean bean1 = ((AddDateBean) bean);
                if ("1".equals(bean1.getType())) {
                    if (TextUtils.isEmpty(bean1.getId())) {
                        TextUtil.setText(tvValue, bean1.getName());
                    } else {
                        TextUtil.setText(tvValue, bean1.getName() + ":" + bean1.getAttendance_time());
                    }
                } else {
                    TextUtil.setText(tvValue, bean1.getName());
                }
                if (!TextUtils.isEmpty(bean1.getId())) {
                    TextUtil.setText(tvValue, bean1.getName() + ":" + bean1.getAttendance_time());
                }
                break;
            case AttendanceConstants.TYPE_SELECT_LOCATION:
                AttendanceTypeListBean bean2 = ((AttendanceTypeListBean) bean);

                break;
            case AttendanceConstants.TYPE_SELECT_WIFI:
                AttendanceTypeListBean bean3 = ((AttendanceTypeListBean) bean);

                break;
        }
        mSelectAdapter.notifyDataSetChanged();
    }

    public SelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addRestDataItem() {
        AddDateBean bean = new AddDateBean();
        bean.setId("");
        bean.setName("休息");
        bean.setAttendance_time("");
        bean.setCheck(false);
        mSelectAdapter.getData().add(0, bean);
        mSelectAdapter.notifyDataSetChanged();

    }

    public void setSelectedData(int index) {
        if (index < 0 || index > dataList.size() - 1) {
            return;
        }
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setCheck(false);
        }
        dataList.get(index).setCheck(true);
        mSelectAdapter.notifyDataSetChanged();
        setValue(index);
    }

    public List<BaseSelectBean> getSelectedData() {
        List<BaseSelectBean> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isCheck()) {
                list.add(dataList.get(i));
            }
        }
        return list;
    }


}
