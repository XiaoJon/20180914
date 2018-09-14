package com.hjhq.teamface.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hjhq.teamface.common.R;
import com.kyleduo.switchbutton.SwitchButton;


/**
 * Created by lx on 2017/6/6.
 */

public class HelperItemView extends FrameLayout {

    private boolean isLine;
    private TextView mHelperItemName;


    private String itemName = "";

    private boolean isSelected;
    private SwitchButton mSwitchButton;
    private View lineView;

    public HelperItemView(Context context) {
        this(context, null);
        init(context);
    }

    public HelperItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HelperItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.helperSettingItem);
        itemName = mTypedArray.getString(R.styleable.helperSettingItem_itemName);
        isSelected = mTypedArray.getBoolean(R.styleable.helperSettingItem_isSelect, false);
        isLine = mTypedArray.getBoolean(R.styleable.helperSettingItem_isLine, true);
        mTypedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(R.layout.view_helper_setting_item, null);
        mHelperItemName = (TextView) mView.findViewById(R.id.tv_helper_item_name);
        mSwitchButton = (SwitchButton) mView.findViewById(R.id.switch_button);
        lineView = mView.findViewById(R.id.line_view);

        addView(mView);
        initView();
    }

    public void initView() {
        mHelperItemName.setText(itemName);
        mSwitchButton.setChecked(isSelected);
        lineView.setVisibility(isLine ? View.VISIBLE : View.GONE);
    }


    @Override
    public void setSelected(boolean selected) {
//        isSelected = selected;
        mSwitchButton.setChecked(selected);
    }

    public boolean getSelected() {
        return mSwitchButton.isChecked();
    }


    public void setItemName(String itemName) {
        this.itemName = itemName;
        mHelperItemName.setText(itemName);
    }

    public void setSwitchButtonTag(int key, Object obj) {
        mSwitchButton.setTag(key, obj);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnChangedListener(OnClickListener listener) {
        mSwitchButton.setTag(R.string.app_name, this);
        mSwitchButton.setOnClickListener(listener);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnChangedListener(CompoundButton.OnCheckedChangeListener listener) {
        mSwitchButton.setOnCheckedChangeListener(listener);
    }
}
