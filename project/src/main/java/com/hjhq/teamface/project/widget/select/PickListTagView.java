package com.hjhq.teamface.project.widget.select;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.customcomponent.widget2.select.PickListView;

import rx.Observable;


/**
 * 标签
 *
 * @author lx
 * @date 2017/8/23
 */

public class PickListTagView extends PickListView {
    public PickListTagView(CustomBean bean) {
        super(bean);
    }

    @Override
    public Object getValue() {
        if (CollectionUtils.isEmpty(checkEntrys)) {
            return "";
        }
        if (ProjectConstants.PROJECT_TASK_LABEL.equals(keyName)) {
            StringBuilder sb = new StringBuilder();
            Observable.from(checkEntrys).subscribe(entry -> {
                sb.append(",").append(entry.getValue());
            });
            sb.deleteCharAt(0);
            return sb.toString();
        } else {
            return checkEntrys;
        }
    }

}
