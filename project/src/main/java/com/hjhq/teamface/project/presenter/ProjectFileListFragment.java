package com.hjhq.teamface.project.presenter;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.ProjectFileListDelegate;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目文件件列表
 */
public class ProjectFileListFragment extends FragmentPresenter<ProjectFileListDelegate, ProjectModel> {
    private int index;
    BaseQuickAdapter adapter;

    static ProjectFileListFragment newInstance(int index) {
        ProjectFileListFragment myFragment = new ProjectFileListFragment();
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
            index = arguments.getInt(Constants.DATA_TAG1, index);
        }
    }

    @Override
    protected void init() {

        List<String> list = getData(20);
       // allTaskAdapter = new ProjectFileListAdapter(new ArrayList<>());
        viewDelegate.setAdapter(adapter);
        viewDelegate.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                CommonUtil.startActivtiyForResult(getActivity(), ProjectFileDetailActivity.class, 1000 + index, bundle);
            }
        });
    }


    private List<String> getData(int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(i + "");
        }
        return list;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }
}
