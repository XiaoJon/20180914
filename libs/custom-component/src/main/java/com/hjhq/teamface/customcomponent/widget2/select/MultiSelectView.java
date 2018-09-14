package com.hjhq.teamface.customcomponent.widget2.select;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.EntryBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.JsonParser;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.adapter.MultiItemAdapter;
import com.hjhq.teamface.customcomponent.widget2.BaseView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;


/**
 * 复选
 *
 * @author lx
 * @date 2017/8/23
 */

public class MultiSelectView extends BaseView {
    private List<EntryBean> defaultEntrys;
    /**
     * 当前下拉选项集合
     */
    private List<EntryBean> entrys;

    /**
     * 是否多选  选择类型：0单选、1多选
     */
    private boolean isMulti;
    private TextView tvTitle;
    private View llRoot;
    private RecyclerView mRecyclerView;

    public MultiSelectView(CustomBean bean) {
        super(bean);
        CustomBean.FieldBean field = bean.getField();
        this.entrys = bean.getEntrys();
        if (field != null) {
            defaultEntrys = field.getDefaultEntrys();
            isMulti = "1".equals(field.getChooseType());
        }
    }

    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {
        mView = View.inflate(activity, R.layout.custom_multi_select_widget_layout, null);
        parent.addView(mView);

        tvTitle = mView.findViewById(R.id.tv_title);
        llRoot = mView.findViewById(R.id.ll_content);
        mRecyclerView = mView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MultiItemAdapter(entrys));
        initOption();
    }


    public void initOption() {
        if (state == CustomConstants.ADD_STATE) {
            setDefaultValue();
        }
        setTitle(tvTitle, title);

        Object value = bean.getValue();
        if (value != null && !"".equals(value)) {
            setData(value);
        }

        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                RxManager.$(aHashCode).post(CustomConstants.MESSAGE_CLEAR_FOCUS_CODE, "");
                if (isDetailState()) {
                } else if (CustomConstants.FIELD_READ.equals(fieldControl)) {
                    ToastUtils.showError(getContext(), "只读属性不可更改");
                } else {
                    EntryBean entry = entrys.get(position);
                    if (isMulti) {
                        entry.setCheck(!entry.isCheck());
                        adapter.notifyItemChanged(position);
                    } else {
                        if (!checkNull() && isLinkage) {
                            linkageData();
                        }
                        Observable.from(entrys).filter(EntryBean::isCheck).subscribe(entryBean -> entryBean.setCheck(false));
                        entry.setCheck(true);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    @Override
    protected void setData(Object value) {
        List<EntryBean> checkEntrys = new JsonParser<EntryBean>().jsonFromList(value, EntryBean.class);

        //设置成选中
        for (EntryBean entry : entrys) {
            Observable.from(checkEntrys)
                    .filter(checkEntry -> entry.getValue().equals(checkEntry.getValue()))
                    .subscribe(checkEntry -> entry.setCheck(true));
        }
    }

    public void setDefaultValue() {
        if (CollectionUtils.isEmpty(entrys) || CollectionUtils.isEmpty(defaultEntrys)) {
            return;
        }
        for (EntryBean entry : entrys) {
            Observable.from(defaultEntrys).filter(bean -> bean.getValue().equals(entry.getValue())).subscribe(bean -> {
                entry.setCheck(true);
            });
        }
    }

    @Override
    public void put(JSONObject jsonObj) {
        jsonObj.put(keyName, CollectionUtils.isEmpty(getValue()) ? "" : getValue());
    }

    @Override
    public boolean checkNull() {
        return CollectionUtils.isEmpty(getValue());
    }

    private List<EntryBean> getValue() {
        List<EntryBean> checkEntrys = new ArrayList<>();
        Observable.from(entrys).filter(EntryBean::isCheck).subscribe(entryBean -> checkEntrys.add(entryBean));
        return checkEntrys;
    }

    @Override
    public boolean formatCheck() {
        return true;
    }


}
