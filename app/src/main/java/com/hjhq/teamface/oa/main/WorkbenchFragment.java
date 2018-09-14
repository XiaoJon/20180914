package com.hjhq.teamface.oa.main;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseFragment;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.bean.QueryBannerBean;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.scrollview.MyNestedScrollView;
import com.hjhq.teamface.componentservice.project.ProjectService;
import com.hjhq.teamface.componentservice.project.WorkBenchService;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 工作台
 *
 * @author lx
 * @date 2017/3/16
 */

public class WorkbenchFragment extends BaseFragment {
    @Bind(R.id.top_banner)
    Banner topBanner;
    @Bind(R.id.iv_app_module)
    ImageView ivAppModule;
    @Bind(R.id.tv_company_name)
    TextView tvCompanyName;
    @Bind(R.id.fl_work_bench)
    FrameLayout rlWorkBench;
    @Bind(R.id.ll_workbeanch)
    View llWorkbeanch;
    @Bind(R.id.scroll_view)
    MyNestedScrollView mScrollView;
    @Bind(R.id.ll_workbench_indicator)
    LinearLayout llWorkBenchIndicaor;
    @Bind(R.id.ll_top_workbench_indicator)
    LinearLayout topWorkBenchIndicaor;
    @Bind(R.id.rl_workbench_indicator)
    View mWorkBenchIndicaor;

    @Bind(R.id.ll_indicator)
    LinearLayout llIndicator;
    @Bind(R.id.tv_over_task)
    TextView tvOverTask;
    @Bind(R.id.tv_today_task)
    TextView tvTodayTask;
    @Bind(R.id.tv_tomorrow_task)
    TextView tvTomorrowTask;
    @Bind(R.id.tv_later_task)
    TextView tvLaterTask;

    /**
     * 看板
     */
    private Fragment workBenchFragment;
    /**
     * 顶部指示器点击
     */
    private ProjectService service;

    public List<TextView> textViews = new ArrayList<>(ProjectConstants.WORK_BENCH_INDICATOR_COUNT);
    private int indexOf = 0;


    @Override
    protected int getContentView() {
        return R.layout.fragment_workbench;
    }

    @Override
    protected void initView(View view) {
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
        llWorkbeanch.setPadding(0, statusBarHeight, 0, 0);

        initIndicator();

        //服务
        service = (ProjectService) Router.getInstance().getService(ProjectService.class.getSimpleName());
        workBenchFragment = service.getWorkbench();

        //看板
        FragmentManager childFragmentManager = getChildFragmentManager();
        childFragmentManager.beginTransaction().replace(R.id.fl_work_bench, workBenchFragment).commit();
    }

    /**
     * 初始化指示器
     */
    private void initIndicator() {
        textViews.add(tvOverTask);
        textViews.add(tvTodayTask);
        textViews.add(tvTomorrowTask);
        textViews.add(tvLaterTask);

        llIndicator.removeAllViews();
        int padding = (int) DeviceUtils.dpToPixel(mContext, 15);
        for (int i = 0; i < ProjectConstants.WORK_BENCH_INDICATOR_COUNT; i++) {
            View view = new View(mContext);
            view.setBackgroundResource(R.color.project_work_task_magic_indicator_color);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -1);
            params.weight = 1;
            params.leftMargin = padding;
            params.rightMargin = padding;
            view.setVisibility(i == 0 ? View.VISIBLE : View.INVISIBLE);
            llIndicator.addView(view, params);
        }
    }

    @Override
    protected void initData() {
        initBanner();
        tvCompanyName.setText(SPHelper.getCompanyName());
    }


    /**
     * 初始化轮播图
     */
    private void initBanner() {
        List<String> companyBanner = new ArrayList();
        companyBanner.add("http://teamface.oss-cn-shenzhen.aliyuncs.com/teamface/1502697309583/TOP1%403x.png?Expires=33038697309&OSSAccessKeyId=LTAITSghA9rHy6Jv&Signature=J/wJ25xN7%2Byqo0Im7h9dtbdyboA%3D");
        companyBanner.add("http://teamface.oss-cn-shenzhen.aliyuncs.com/teamface/1502697386209/TOP3%403x.png?Expires=33038697386&OSSAccessKeyId=LTAITSghA9rHy6Jv&Signature=IXs0l9VtuKaitF1qmXVxutUJ%2B08%3D");
        companyBanner.add("http://teamface.oss-cn-shenzhen.aliyuncs.com/teamface/1502697413966/TOP4%403x.png?Expires=33038697414&OSSAccessKeyId=LTAITSghA9rHy6Jv&Signature=OPOz80jSSjTIOAlml55RXjQeFhw%3D");
        loadBanner(companyBanner);

        MainLogic.getInstance().queryCompanyBanner((RxAppCompatActivity) getActivity(), new ProgressSubscriber<QueryBannerBean>(getActivity(), false) {
            @Override
            public void onNext(QueryBannerBean queryBannerBean) {
                super.onNext(queryBannerBean);
                String banners = queryBannerBean.getData().getBanner();

                List<String> banner = JSONArray.parseArray(banners, String.class);
                loadBanner(banner);
            }

            @Override
            public void onError(Throwable e) {
                dismissWindowView();
                e.printStackTrace();
            }
        });
    }

    /**
     * 加载轮播图
     *
     * @param companyBanner
     */
    private void loadBanner(List<String> companyBanner) {
        topBanner.stopAutoPlay();
        if (companyBanner == null || companyBanner.size() <= 0) {
            return;
        }
        topBanner.setImages(companyBanner).setImageLoader(new com.youth.banner.loader.ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                ImageLoader.loadRoundImage(context, (String) path, imageView, 10, R.drawable.icon_default);
            }
        }).start();
    }


    @Override
    protected void setListener() {
        setOnClicks(ivAppModule, tvCompanyName, tvOverTask, tvTodayTask, tvTomorrowTask, tvLaterTask);
        //看板滑动回调
        service.setWorkBenchService(workBenchFragment, new WorkBenchService() {
            @Override
            public void setTextIndex(int index) {
                setIndictorIndex(index);
            }

            @Override
            public void canDragItemAtPosition(int column, int dragPosition) {
                mScrollView.setTag(R.id.scroll_view, Constants.DATA_TAG1);
                mScrollView.scrollTo(0, llWorkBenchIndicaor.getTop() + 5);
            }

            @Override
            public void canDropItemAtPosition(int oldColumn, int oldRow, int newColumn, int newRow) {

            }
        });


        mScrollView.setFixListener(new MyNestedScrollView.OnFixListener() {
            @Override
            public void onFix() {
                fix();
            }

            @Override
            public void onDismiss() {
                dismiss();
            }
        });

        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LogUtil.d("scrollX:" + scrollX + "  scrollY:" + scrollY + "  oldScrollX:" + oldScrollX + "  oldScrollY:" + oldScrollY);

                int[] location = new int[2];
                llWorkBenchIndicaor.getLocationOnScreen(location);
                int yPosition = location[1];

                int[] moduleLocation = new int[2];
                topWorkBenchIndicaor.getLocationOnScreen(moduleLocation);
                if (yPosition < moduleLocation[1]) {
                    mScrollView.setNeedScroll(false);
                } else {
                    mScrollView.setNeedScroll(true);
                }
            }
        });
    }

    private void fix() {
        llWorkBenchIndicaor.removeView(mWorkBenchIndicaor);
        topWorkBenchIndicaor.addView(mWorkBenchIndicaor);
    }

    public void dismiss() {
        topWorkBenchIndicaor.removeView(mWorkBenchIndicaor);
        llWorkBenchIndicaor.addView(mWorkBenchIndicaor);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_company_name:
                CommonUtil.startActivtiy(mContext, SelectCompanyActivity.class);
                break;
            case R.id.iv_app_module:
                UIRouter.getInstance().openUri(getActivity(), "DDComp://project/appModule", null);
                break;
            case R.id.tv_over_task:
            case R.id.tv_today_task:
            case R.id.tv_tomorrow_task:
            case R.id.tv_later_task:
                int indexOf = textViews.indexOf(view);
                service.clickIndex(workBenchFragment, indexOf);
                setIndictorIndex(indexOf);
                break;
            default:
                break;
        }
    }

    /**
     * 停止或自动播放图片
     *
     * @param flag
     */
    public void setAutoPlay(boolean flag) {
        if (topBanner == null) {
            return;
        }
        if (flag) {
            topBanner.startAutoPlay();
        } else {
            topBanner.stopAutoPlay();
        }
    }


    public void setIndictorIndex(int index) {
        indexOf = index;
        textViews.get(indexOf).setTextColor(ColorUtils.resToColor(mContext, R.color.project_work_task_magic_indicator_color));
        for (int i = 0; i < ProjectConstants.WORK_BENCH_INDICATOR_COUNT; i++) {
            llIndicator.getChildAt(i).setVisibility(i == indexOf ? View.VISIBLE : View.INVISIBLE);
            int color = i == indexOf ? R.color.project_work_task_magic_indicator_color : R.color.gray_90;
            textViews.get(i).setTextColor(ColorUtils.resToColor(mContext, color));
        }
    }

    public void refreshData() {
        if (service != null) {
            service.refreshColumn(workBenchFragment);
        }
    }
}
