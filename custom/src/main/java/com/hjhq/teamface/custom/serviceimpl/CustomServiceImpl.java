package com.hjhq.teamface.custom.serviceimpl;

import android.app.Activity;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.componentservice.custom.CustomService;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.customcomponent.widget2.CustomUtil;
import com.hjhq.teamface.customcomponent.widget2.reference.ReferenceView;
import com.hjhq.teamface.customcomponent.widget2.subfield.SubfieldView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 自定义组件对外接口实现
 *
 * @author Administrator
 * @date 2018/3/26
 */

public class CustomServiceImpl implements CustomService {
    @Override
    public boolean putNoCheck(List<Object> mViewList, JSONObject json) {
        for (int i = 0; i < mViewList.size(); i++) {
            SubfieldView view = (SubfieldView) mViewList.get(i);
            ArrayList viewList = view.getViewList();
            boolean put = CustomUtil.putNoCheck(viewList, json);
            if (!put) {
                return put;
            }
        }
        return true;
    }

    @Override
    public boolean putReference(List<Object> mViewList, JSONObject json) {
        Observable.from(mViewList)
                .filter(object -> object instanceof SubfieldView)
                .flatMap(subFieldView -> Observable.from(((SubfieldView) subFieldView).getViewList()))
                .filter(view -> view instanceof ReferenceView && view.getVisibility())
                .subscribe(view -> view.put(json));
        return true;
    }

    @Override
    public boolean put(Activity activity, List<BaseView> mViewList, JSONObject json) {
        return CustomUtil.put(activity, mViewList, json);
    }

    @Override
    public Object getSubfield(List<CustomBean> list, int state, String title, boolean isSpread, String moduleBean, boolean isHideColumnName, LinearLayout llCustomLayout) {
        SubfieldView subfieldView = new SubfieldView(list, state, title, isSpread, moduleBean);
        subfieldView.setHideColumnName(isHideColumnName);
        subfieldView.addView(llCustomLayout);
        return subfieldView;
    }

    @Override
    public void handleHidenFields(int hashCode, Object tag, List<?> mViewList) {
        CustomUtil.handleHidenFields(hashCode,tag, (List<SubfieldView>) mViewList);
    }
}
