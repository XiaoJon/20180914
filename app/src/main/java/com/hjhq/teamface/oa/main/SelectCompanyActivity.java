package com.hjhq.teamface.oa.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.oa.login.bean.CompanyListBean;
import com.hjhq.teamface.oa.main.adaper.SelectCompanyAdapter;
import com.hjhq.teamface.oa.main.logic.MainLogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import rx.Observable;

/**
 * 选择企业
 *
 * @author lx
 * @date 2017/3/23
 */

public class SelectCompanyActivity extends BaseTitleActivity {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_company_name)
    TextView tvCompanyName;
    SelectCompanyAdapter selectCompanyAdapter;
    ArrayList<CompanyListBean.DataBean> companies = new ArrayList<>();
    private String companyId;

    @Override
    protected int getChildView() {
        return R.layout.select_company_activity;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle(getString(R.string.select_company));
        setRightMenuColorTexts(getString(R.string.confirm));

    }

    @Override
    protected void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyLinearDeviderDecoration(mContext, R.color.gray_f2, (int) DeviceUtils.dpToPixel(mContext, 8)));
        selectCompanyAdapter = new SelectCompanyAdapter(companies);
        mRecyclerView.setAdapter(selectCompanyAdapter);
        companyId = SPHelper.getCompanyId();
        TextUtil.setText(tvCompanyName, SPHelper.getCompanyName());

        getCompanyList();
    }

    /**
     * 获取公司列表
     */
    private void getCompanyList() {
        MainLogic.getInstance().getCompanyList(this, new ProgressSubscriber<CompanyListBean>(this) {
            @Override
            public void onNext(CompanyListBean companyListBean) {
                super.onNext(companyListBean);
                List<CompanyListBean.DataBean> companyLsit = companyListBean.getData();
                if (CollectionUtils.isEmpty(companyLsit)) {
                    ToastUtils.showError(mContext, "未获取到公司列表！");
                    return;
                }
                companies.clear();
                Iterator<CompanyListBean.DataBean> iterator = companyLsit.iterator();
                while (iterator.hasNext()) {
                    String id = iterator.next().getId();
                    //将自己和无效公司清除
                    if (TextUtils.isEmpty(id) || companyId.equals(id)) {
                        iterator.remove();
                    }
                }
                companies.addAll(companyLsit);
                Observable.from(companies).filter(dataBean -> dataBean.getId().equals(companyId)).subscribe(dataBean -> dataBean.setSelect(true));
                selectCompanyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void setListener() {
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, final int position) {
                if (companies.get(position).isSelect()) {
                    return;
                }
                Observable.from(companies)
                        .filter(dataBean -> {
                            dataBean.setSelect(false);
                            return companies.indexOf(dataBean) == position;
                        })
                        .subscribe(dataBean -> dataBean.setSelect(true));
                selectCompanyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Observable.from(companies)
                .filter(CompanyListBean.DataBean::isSelect)
                .subscribe(dataBean -> DialogUtils.getInstance()
                        .sureOrCancel(SelectCompanyActivity.this
                                , "提示"
                                , "切换公司后无法查看当前公司数据!您确定要切换到" + dataBean.getCompany_name() + "吗？"
                                , mRecyclerView.getRootView()
                                , () -> MainLogic.getInstance().switchCurrentCompany(SelectCompanyActivity.this, dataBean.getId())));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }
}
