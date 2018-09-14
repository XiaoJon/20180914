package com.hjhq.teamface.project.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.project.R;

import java.util.List;

/**
 * @author hackware
 * @date 2016/9/10
 */

public class ModulePagerAdapter extends PagerAdapter {
    private SimpleItemClickListener listener;

    private Context context;
    private List<List<AppModuleBean>> viewList;
    private boolean isEditMode;

    public ModulePagerAdapter(Context context, List<List<AppModuleBean>> viewList) {
        this.viewList = viewList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return viewList == null ? 0 : viewList.size();
    }

    public List<AppModuleBean> getItem(int position) {
        return viewList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View imageLayout = inflater.inflate(R.layout.app_module_item, container,
                false);
        RecyclerView mRecyclerView = imageLayout.findViewById(R.id.recycler_view);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(context, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        AppAdapter adapter = new AppAdapter(getItem(position));
        mRecyclerView.setAdapter(adapter);

        if (listener != null) {
            mRecyclerView.addOnItemTouchListener(listener);
        }

        container.addView(imageLayout);
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setListener(SimpleItemClickListener listener) {
        this.listener = listener;
    }


    public interface ItemClickListener {
        void onItemClick(BaseQuickAdapter adapter, View view, int position, int superPosition);

        void onItemChildClick(BaseQuickAdapter adapter, View view, int position, int superPosition);
    }
}
