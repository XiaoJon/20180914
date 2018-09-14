package com.hjhq.teamface.project.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.adapter.ProjectTemplateAdapter;
import com.hjhq.teamface.project.bean.ProjectTempBean;
import com.hjhq.teamface.project.bean.TempDataList;
import com.hjhq.teamface.project.bean.TempGroupList;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.presenter.temp.ProjectTempFlowActivity;
import com.hjhq.teamface.project.ui.TemplateListDelegate;

import java.util.List;

import rx.Observable;


/**
 * 项目模板
 * Created by Administrator on 2018/4/10.
 */
public class ProjectTemplateListFragment extends FragmentPresenter<TemplateListDelegate, ProjectModel> {
    private int index;
    ProjectTemplateAdapter adapter;
    private ActivityPresenter mActivity;

    static ProjectTemplateListFragment newInstance(int index) {
        ProjectTemplateListFragment myFragment = new ProjectTemplateListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, index);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            index = arguments.getInt(Constants.DATA_TAG1, 0);
        }
    }

    @Override
    protected void init() {
        mActivity = (ActivityPresenter) getActivity();

        adapter = new ProjectTemplateAdapter(null);
        viewDelegate.setAdapter(adapter);

        loadTemp();
    }

    private void loadTemp() {
        model.queryProjectTemplateList(mActivity, index, new ProgressSubscriber<ProjectTempBean>(mActivity) {
            @Override
            public void onNext(ProjectTempBean baseBean) {
                super.onNext(baseBean);
                List<TempDataList> tempDataLists = baseBean.getData().getDataList();
                List<TempGroupList> tempGroupLists = baseBean.getData().getGroupList();
                Observable.from(tempGroupLists)
                        .subscribe(group -> Observable.from(tempDataLists)
                                .filter(temp -> temp.getTemp_type() == group.getTempTypeId())
                                .subscribe(temp -> temp.setTemp_name(group.getTempTypeName())));
                CollectionUtils.notifyDataSetChanged(adapter, adapter.getData(), tempDataLists);
            }
        });
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                TempDataList item = (TempDataList) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.DATA_TAG1, item);
                CommonUtil.startActivtiyForResult(mActivity, ProjectTempFlowActivity.class, Constants.REQUEST_CODE1, bundle);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == Activity.RESULT_OK) {
            mActivity.setResult(Activity.RESULT_OK, data);
            mActivity.finish();
        }
    }
}
