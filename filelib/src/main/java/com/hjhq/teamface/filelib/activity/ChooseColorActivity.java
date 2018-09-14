package com.hjhq.teamface.filelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.ColorItemAdapter;
import com.hjhq.teamface.filelib.view.ChooseColorDelegate;

/**
 * Created by mou on 2017/3/29.
 */

public class ChooseColorActivity extends ActivityPresenter<ChooseColorDelegate, FilelibModel> implements AdapterView.OnItemClickListener {
    GridView mActChooseColorGrid;
    public static int[] colors =
            {
                    R.color._FF1D32,
                    R.color._FF7777,
                    R.color._DD361A,
                    R.color._DB6A20,
                    R.color._F57221,
                    R.color._FFEB57,
                    R.color._F9B239,
                    R.color._B4E550,
                    R.color._73B32D,
                    R.color._119E29,
                    R.color._00781B,
                    R.color._00CFC9,
                    R.color._51D0B1,
                    R.color._5CC1FC,
                    R.color._0091FA,
                    R.color._3057E5,
                    R.color._0051AF,
                    R.color._FF85BE,
                    R.color._F52E94,
                    R.color._9856D9,
                    R.color._5821A7,
                    R.color._C0C0C0,
                    R.color._8C8C8C,
                    R.color._000000
            };

    public static int[] colorString = {
            R.string._FF1D32,
            R.string._FF7777,
            R.string._DD361A,
            R.string._DB6A20,
            R.string._F57221,
            R.string._FFEB57,
            R.string._F9B239,
            R.string._B4E550,
            R.string._73B32D,
            R.string._119E29,
            R.string._00781B,
            R.string._00CFC9,
            R.string._51D0B1,
            R.string._5CC1FC,
            R.string._0091FA,
            R.string._3057E5,
            R.string._0051AF,
            R.string._FF85BE,
            R.string._F52E94,
            R.string._9856D9,
            R.string._5821A7,
            R.string._C0C0C0,
            R.string._8C8C8C,
            R.string._000000
    };

    private ColorItemAdapter mColorItemAdapter;
    private int mColorPosition = 0;
    private int mItemPostion;//位置
    private long fileId;//位置
    private String libFlag;
    private int labelFlag;
    private int fromWitch = -1;
    String choosedColor = "#FF1D32";


    protected void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            choosedColor = bundle.getString(Constants.DATA_TAG1);
            for (int i = 0; i < colorString.length; i++) {
                if (getString(colorString[i]).equals(choosedColor)) {
                    mColorPosition = i;
                }
            }
        }
        mColorItemAdapter = new ColorItemAdapter(this, colors, mColorPosition);
        mActChooseColorGrid.setAdapter(mColorItemAdapter);
        mActChooseColorGrid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mColorItemAdapter.setCurrentPostion(position);
        Intent intent = new Intent();
        intent.putExtra(Constants.COLOR_CHOOSE, getString(colorString[position]));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    public void init() {
        mActChooseColorGrid = findViewById(R.id.act_choose_color_grid);
        initData();

    }
}
