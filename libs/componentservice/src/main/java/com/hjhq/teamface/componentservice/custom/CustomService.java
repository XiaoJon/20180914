package com.hjhq.teamface.componentservice.custom;

import android.app.Activity;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.customcomponent.widget2.BaseView;

import java.util.List;

/**
 * @author Administrator
 * @date 2018/3/26
 */

public interface CustomService {
    boolean put(Activity activity, List<BaseView> mViewList, JSONObject json);

    boolean putNoCheck(List<Object> mViewList, JSONObject json);

    boolean putReference(List<Object> mViewList, JSONObject json);

    Object getSubfield(List<CustomBean> list, int state, String title, boolean isSpread, String moduleBean, boolean isHideColumnName, LinearLayout llCustomLayout);

    void handleHidenFields(int hashCode, Object tag, List<?> mViewList);

}
