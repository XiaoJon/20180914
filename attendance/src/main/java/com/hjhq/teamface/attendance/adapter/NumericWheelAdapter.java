package com.hjhq.teamface.attendance.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

public class NumericWheelAdapter extends AbstractWheelTextAdapter {
    public static final int DEFAULT_MAX_VALUE = 9;
    private static final int DEFAULT_MIN_VALUE = 0;
    private int minValue;
    private int maxValue;
    private String format;
    private String label;
    private ArrayList<View> arrayList;
    private int position;

    public NumericWheelAdapter(Context context) {
        this(context, 0, 9);
    }

    public NumericWheelAdapter(Context context, int minValue, int maxValue) {
        this(context, minValue, maxValue, (String) null);
    }

    public NumericWheelAdapter(Context context, int minValue, int maxValue, String format) {
        super(context);
        this.arrayList = new ArrayList();
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }


    public CharSequence getItemText(int index) {
        if (index >= 0 && index < this.getItemsCount()) {
            int value = this.minValue + index;
            return this.format != null ? String.format(this.format, new Object[]{Integer.valueOf(value)}) : Integer.toString(value);
        } else {
            return null;
        }
    }

    public int getItemsCount() {
        return this.maxValue - this.minValue + 1;
    }

    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index >= 0 && index < this.getItemsCount()) {
            if (convertView == null) {
                convertView = this.getView(this.itemResourceId, parent);
            }

            TextView textView = this.getTextView(convertView, this.itemTextResourceId);
            if (!this.arrayList.contains(textView)) {
                this.arrayList.add(textView);
            }

            if (textView != null) {
                Object text = this.getItemText(index);
                if (text == null) {
                    text = "";
                }

                textView.setText(text + this.label);
                if (this.itemResourceId == -1) {
                    if (index == this.position) {
                        this.configureCurrentTextView(textView);
                    } else {
                        this.configureTextView(textView);
                    }
                }
            }

            return convertView;
        } else {
            return null;
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }
}