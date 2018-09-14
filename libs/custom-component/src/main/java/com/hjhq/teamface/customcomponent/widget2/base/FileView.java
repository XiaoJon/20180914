package com.hjhq.teamface.customcomponent.widget2.base;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.BaseView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件类型组件
 *
 * @author lx
 * @date 2017/6/20
 */

public abstract class FileView extends BaseView {
    protected RecyclerView mRecyclerView;
    protected Activity mActivity;
    protected TextView tvTitle;
    protected RelativeLayout rlAdd;

    //数量限制(0不限制 1限制)
    protected int countLimit;
    //最大上传数
    protected int maxCount;
    //最大上传大小 MB
    protected String maxSize;

    protected List<UploadFileBean> uploadFileBeanList = new ArrayList<>();

    public FileView(CustomBean bean) {
        super(bean);
        CustomBean.FieldBean field = bean.getField();
        if (field != null) {
            countLimit = TextUtil.parseInt(field.getCountLimit());
            maxCount = TextUtil.parseInt(field.getMaxCount());
            maxSize = field.getMaxSize();
        }
    }

    @Override
    public void addView(LinearLayout parent, Activity activity, int index) {
        this.mActivity = activity;
        mView = View.inflate(mActivity, R.layout.custom_item_attachment_vertical_view, null);

        mRecyclerView = mView.findViewById(R.id.recyler_upload_file);
        tvTitle = mView.findViewById(R.id.tv_title);
        rlAdd = mView.findViewById(R.id.rl_add);

        initView();

        parent.addView(mView, index);
        initOption();

    }

    private void initView() {
        Object value = bean.getValue();
        if (value != null && !"".equals(value)) {
            setData(bean.getValue());
        }
        if (state != CustomConstants.DETAIL_STATE && !CustomConstants.FIELD_READ.equals(fieldControl)) {
            rlAdd.setVisibility(View.VISIBLE);
        } else {
            rlAdd.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setData(Object value) {
        try {
            if (value instanceof JSONArray) {
                uploadFileBeanList = JSONArray.parseArray(value + "", UploadFileBean.class);
            } else if (value instanceof List) {
                List<Map<String, Object>> value1 = (List<Map<String, Object>>) value;
                for (Map<String, Object> map : value1) {
                    UploadFileBean uploadFileBean = JSONObject.parseObject(JSON.toJSONString(map), UploadFileBean.class);
                    uploadFileBeanList.add(uploadFileBean);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void initOption();

    public abstract void uploadFile();

    public abstract Object getValue();


    @Override
    public void put(JSONObject jsonObj) {
        jsonObj.put(keyName, getValue());
    }

    @Override
    public boolean checkNull() { //新增时显示，不能是只读，需要必填 时为空 检测才不通过
        return "".equals(getValue());
    }

    @Override
    public boolean formatCheck() {
        return true;
    }

    /**
     * 检查文件大小是否符合
     *
     * @param file
     * @return
     */
    protected boolean checkFileSize(File file) {
        long maxSizeLong = TextUtil.parseLong(maxSize) * 1024 * 1024;
        if (countLimit == 0) {
            return true;
        } else if (file.length() < maxSizeLong) {
            return true;
        } else {
            return false;
        }
    }
}
