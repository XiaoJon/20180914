package com.hjhq.teamface.oa.main;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjhq.teamface.R;
import com.hjhq.teamface.base.QueryModuleAuthResultBean;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.bean.EmployeeResultBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.ProgressBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EmailConstant;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.provider.MyFileProvider;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.BadgeUtil;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.bean.CommonModuleResultBean;
import com.hjhq.teamface.common.view.NoScrollViewPager;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.IMService;
import com.hjhq.teamface.im.activity.TeamMessageFragment;
import com.hjhq.teamface.im.db.DBManager;
import com.hjhq.teamface.oa.approve.ui.ApproveActivity;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;
import com.hjhq.teamface.view.quickaction.ActionItem;
import com.hjhq.teamface.view.quickaction.QuickAction;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;


/**
 * @author lx
 * @date 2017/3/16
 */

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.main_vp)
    NoScrollViewPager mViewPager;
    @Bind(R.id.iv_data_analysis)
    ImageView ivDataAnalysis;
    @Bind(R.id.tv_data_analysis)
    TextView tvDataAnalysis;
    @Bind(R.id.workbench_iv)
    ImageView workbenchIv;
    @Bind(R.id.workbench_tv)
    TextView workbenchTv;
    @Bind(R.id.workbench_rl)
    RelativeLayout workbenchRl;
    @Bind(R.id.teamwork_rl)
    RelativeLayout teamworkRl;
    @Bind(R.id.addIv)
    ImageView addIv;
    @Bind(R.id.team_message_iv)
    ImageView teamMessageIv;
    @Bind(R.id.team_message_tv)
    TextView teamMessageTv;
    @Bind(R.id.team_message_rl)
    RelativeLayout teamMessageRl;
    @Bind(R.id.team_mine_rl)
    RelativeLayout teamMineRl;
    @Bind(R.id.mine_iv)
    ImageView mineIv;
    @Bind(R.id.mine_tv)
    TextView mineTv;
    @Bind(R.id.actionbar_ll)
    LinearLayout actionbarLl;
    @Bind(R.id.tv_im_total_unread_num)
    TextView imUnreadNum;


    private static NotificationManager mNotificationManager;
    private ScheduledExecutorService scheduledExecutorService;
    private Runnable progressRunnable;
    private MyReceiver mReceiver;
    /**
     * 记录上次点击返回时间
     */
    private long time;


    private ImageView[] actionBarIvs = new ImageView[4];
    private TextView[] actionBarTvs = new TextView[4];
    private int[] normalActionBarDrawable = {R.drawable.actionbar_statistic_normal, R.drawable.actionbar_workbench_normal, R.drawable.actionbar_team_message_normal, R.drawable.actionbar_mine_normal};
    private int[] clickActionBarDrawable = {R.drawable.actionbar_statistic_click, R.drawable.actionbar_workbench_click, R.drawable.actionbar_team_message_click, R.drawable.actionbar_mine_click};

    private List<Fragment> fragments = new ArrayList<>(4);

    /**
     * 动作组 快速创建
     **/
    private QuickAction quickActionAdd;
    /**
     * 当前位置
     */
    private int currentItem;

    protected void onSetContentViewNext(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentItem = getIntent().getIntExtra(Constants.DATA_TAG1, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startIMService();
        //检测企信服务是否运行
        checkIMServiceIsAlive();
        initReceiver();
        initIMEI();
    }

    /**
     * 获取手机IMEI码并保存到sp
     */
    private void initIMEI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SystemFuncUtils.requestPermissions(this, Manifest.permission.READ_PHONE_STATE, aBoolean -> {
                if (aBoolean) {
                    saveIMEI();
                } else {
                    ToastUtils.showError(mContext, "必须获得必要的权限才能正常使用!");
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_PHONE_STATE}, Constants.REQUEST_CODE7);
                }
            });
        } else {
            saveIMEI();
        }

    }

    private void saveIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Activity.TELEPHONY_SERVICE);
        String imei = "";
        if (Build.VERSION.SDK_INT >= 26) {
            imei = telephonyManager.getImei();
        } else {
            imei = telephonyManager.getDeviceId();
        }
        SPHelper.setImei(imei);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_CODE7:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    saveIMEI();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 接收网络状态变化广播
     */
    private void initReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION);
        registerReceiver(mReceiver, intentFilter);
    }

    /**
     * 检测企信服务是否运行
     */
    private void checkIMServiceIsAlive() {
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                startIMService(MainActivity.this);
            }
        };
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(progressRunnable, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * 开启企信服务
     *
     * @param context
     */
    private void startIMService(Context context) {
        /*//应用程序最大可用内存
        int maxMemory = ((int) Runtime.getRuntime().maxMemory()) / 1024 / 1024;
//应用程序已获得内存
        long totalMemory = ((int) Runtime.getRuntime().totalMemory()) / 1024 / 1024;
//应用程序已获得内存中未使用内存
        long freeMemory = ((int) Runtime.getRuntime().freeMemory()) / 1024 / 1024;
        System.out.println("---> maxMemory=" + maxMemory + "M,totalMemory=" + totalMemory + "M,freeMemory=" + freeMemory + "M");*/
        try {
            if (!SystemFuncUtils.isServiceRunning(mContext, IMService.class.getName())) {
                Intent intent = new Intent(context, IMService.class);
                context.startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        fragments.clear();
        fragments.add(new StatisticFragment());
        fragments.add(new WorkbenchFragment());
        fragments.add(new TeamMessageFragment());
        fragments.add(new MineFragment());

        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        //禁止ViewPager滑动，以免造成逻辑混乱
        mViewPager.setNoScroll(true);

        actionBarIvs[0] = ivDataAnalysis;
        actionBarIvs[1] = workbenchIv;
        actionBarIvs[2] = teamMessageIv;
        actionBarIvs[3] = mineIv;

        actionBarTvs[0] = tvDataAnalysis;
        actionBarTvs[1] = workbenchTv;
        actionBarTvs[2] = teamMessageTv;
        actionBarTvs[3] = mineTv;

        changeActionBar(currentItem);
        mViewPager.setCurrentItem(currentItem);

        // 初始化快速添加菜单
        quickActionAdd = initAddMenu(this);

    }

    @Override
    protected void setListener() {
        setOnClicks(workbenchRl, teamworkRl, teamMessageRl, teamMineRl, addIv);
        /*addIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                File file = new File(JYFileHelper.getFileDir(mContext, Constants.PATH_DOWNLOAD), MsgConstant.NEW_VERSION_APK_ID + "12.apk");
                installProcess(file);
                //测试安装软件
                return false;
            }
        });*/

    }

    @Override
    protected void initData() {
        initNotification();
        initContacts();
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "message";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
            channelId = "push";
            channelName = "推送消息";
            importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        mNotificationManager.createNotificationChannel(channel);
    }

    private void initContacts() {
        MainLogic.getInstance().getEmployee(mContext, null, new ProgressSubscriber<EmployeeResultBean>(mContext, false) {
            @Override
            public void onNext(EmployeeResultBean employeeResultBean) {
                super.onNext(employeeResultBean);
                List<Member> list = employeeResultBean.getData();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setMyId(SPHelper.getUserId());
                    list.get(i).setCompany_id(SPHelper.getCompanyId());
                }
                DBManager.getInstance().saveAll(list);
                EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.MEMBER_MAYBE_CHANGED_TAG, null));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.team_message_rl:
                mViewPager.setCurrentItem(0);
                ((WorkbenchFragment) fragments.get(1)).setAutoPlay(false);
                break;
            case R.id.workbench_rl:
                mViewPager.setCurrentItem(1);
                ((WorkbenchFragment) fragments.get(1)).setAutoPlay(true);
                ((WorkbenchFragment) fragments.get(1)).refreshData();
                break;
            case R.id.teamwork_rl:
                mViewPager.setCurrentItem(2);
                ((WorkbenchFragment) fragments.get(1)).setAutoPlay(false);
                break;
            case R.id.team_mine_rl:
                mViewPager.setCurrentItem(3);
                ((WorkbenchFragment) fragments.get(1)).setAutoPlay(false);
                break;
            case R.id.addIv:
                quickAdd(view);
                break;
            default:
                break;
        }
    }

    /**
     * 快速新增
     */
    private void quickAdd(View view) {
        MainLogic.getInstance().getQuicklyAdd(this, new ProgressSubscriber<CommonModuleResultBean>(this, false) {
            @Override
            public void onNext(CommonModuleResultBean appListResultBean) {
                super.onNext(appListResultBean);
                List<AppModuleBean> commonApplication = appListResultBean.getData();
                List<ActionItem> actionItems = new ArrayList<>();
                if (!CollectionUtils.isEmpty(commonApplication)) {
                    Observable.from(commonApplication)
                            .filter(app -> actionItems.size() < QuickAction.QUICK_MODULE_COUNT)
                            .subscribe(bean -> actionItems.add(new ActionItem(bean.getId(), bean.getEnglish_name(), bean.getChinese_name(), bean.getIcon_url(), bean.getIcon_color(), bean.getIcon_type())));
                } else {
                    actionItems.add(new ActionItem(ProjectConstants.PROJECT_BEAN_NAME, "协作"));
                    actionItems.add(new ActionItem(EmailConstant.BEAN_NAME, "邮件"));
                    actionItems.add(new ActionItem(FileConstants.BEAN_NAME, "文件库"));
                    actionItems.add(new ActionItem(MemoConstant.BEAN_NAME, "备忘录"));
                    actionItems.add(new ActionItem(ApproveConstants.APPROVAL_MODULE_BEAN, "审批"));
                }
                quickActionAdd.addActionItem(actionItems);
                quickActionAdd.show(view, addIv.getWidth() / 2,
                        (int) DeviceUtils.dpToPixel(mContext, -15));
            }
        });
    }

    /**
     * 设置actionBar的选中状态
     *
     * @param position
     */
    private void changeActionBar(int position) {
        resetActionBar();
        actionBarIvs[position].setImageResource(clickActionBarDrawable[position]);
        actionBarTvs[position].setTextColor(ContextCompat.getColor(mContext, R.color.app_blue));

    }

    /**
     * actionBar重置为未选中状态
     */
    private void resetActionBar() {

        for (int i = 0; i < actionBarIvs.length; i++) {
            actionBarIvs[i].setImageResource(normalActionBarDrawable[i]);
            actionBarTvs[i].setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeActionBar(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 初始动作 （ActionAdd）
     */
    public QuickAction initAddMenu(final Activity activity) {
        QuickAction quickActionAdd = new QuickAction(activity,
                QuickAction.HORIZONTAL);

        // 设置 Item点击监听事件
        quickActionAdd.setOnActionItemClickListener(item -> {
            String moduleId = item.getModuleId();
            String moduleBean = item.getModuleBean();
            if (moduleBean == null) moduleBean = "";
            //系统模块
            Bundle bundle = new Bundle();
            switch (moduleBean) {
                case ApproveConstants.APPROVAL_MODULE_BEAN:
                    bundle.putString(Constants.MODULE_BEAN, ApproveConstants.APPROVAL_MODULE_BEAN);
                    CommonUtil.startActivtiy(MainActivity.this, ApproveActivity.class);
                    break;
                case ProjectConstants.PROJECT_BEAN_NAME:
                    bundle.putString(Constants.MODULE_BEAN, moduleBean);
                    UIRouter.getInstance().openUri(mContext, "DDComp://project/addproject", bundle);
                    break;
                case MemoConstant.BEAN_NAME:
                    bundle.putString(Constants.MODULE_BEAN, moduleBean);
                    UIRouter.getInstance().openUri(mContext, "DDComp://memo/add", bundle);
                    break;
                case EmailConstant.BEAN_NAME:
                    bundle.putInt(Constants.DATA_TAG3, EmailConstant.ADD_NEW_EMAIL);
                    UIRouter.getInstance().openUri(mContext, "DDComp://email/new_email", bundle);
                    break;
                case FileConstants.BEAN_NAME:
                    UIRouter.getInstance().openUri(mContext, "DDComp://filelib/netdisk", bundle);
                    break;
                default:
                    if (TextUtil.isEmpty(moduleId)) {
                        ToastUtils.showError(mContext, "未获取到模块ID");
                        return;
                    }
                    bundle.putString(Constants.MODULE_BEAN, item.getModuleBean());
                    bundle.putString(Constants.MODULE_ID, moduleId);
                    UIRouter.getInstance().openUri(mContext, "DDComp://custom/add", bundle);
                    break;
            }

        });
        return quickActionAdd;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回键不退出程序，改为显示桌面
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - time > 3000) {
                ToastUtils.showToast(mContext, "再次点击进入后台");
                time = System.currentTimeMillis();
            } else {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageBean bean) {
        if (bean.getCode() == Constants.REQUEST_SWITCH_COMPANY) {
            int currentItem = mViewPager.getCurrentItem();

            AppManager.getAppManager().finishAllActivity();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DATA_TAG1, currentItem);
            CommonUtil.startActivtiy(this, MainActivity.class, bundle);
            return;
        }
        if (bean.getTag() == null) {
            return;
        }
        switch (bean.getTag()) {
            case EventConstant.TYPE_ORGANIZATIONAL_AND_PERSONNEL_CHANGES:
                //人员或组织架构变化,更新数据
                initContacts();
                break;
            case MsgConstant.IM_TOTAL_UNREAD_NUM:
                //显示总的未读消息数
                setUnreadNum(bean.getCode());
                break;
            case MsgConstant.IM_SERVICE_DYING:
                //企信服务关闭,重启
                Log.e("MainActivity", "正在重启企信服务");
                IM.getInstance().writeFileToSDCard(DateTimeUtil.longToStr(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss   ") + "正在重启企信服务");

                actionbarLl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, IMService.class);
                        try {
                            if (!SystemFuncUtils.isServiceRunning(mContext, IMService.class.getName())) {
                                ComponentName name = mContext.startService(intent);
                                IM.getInstance().writeFileToSDCard(new Gson().toJson(name));
                                /*if (Build.VERSION.SDK_INT >= 26) {
                                    startForegroundService(intent);
                                } else {
                                    mContext.startService(intent);
                                }*/
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);


                break;
            default:

                break;
        }
    }

    /**
     * 企信总的未读数
     *
     * @param num
     */
    private void setUnreadNum(int num) {
        if (num < 0) {
            num = 0;
        }
        showBadgeNum(num);
        /*if (num <= 0) {
            imUnreadNum.setVisibility(View.GONE);
            return;
        }
        imUnreadNum.setVisibility(View.VISIBLE);
        if (num > 0 && num <= MsgConstant.SHOW_MAX_EXACT_NUM) {
            if (num < 10) {
                imUnreadNum.setBackground(getResources().getDrawable(R.drawable.im_unread_num_round_bg));
            } else {
                imUnreadNum.setBackground(getResources().getDrawable(R.drawable.im_unread_num_bg));
            }
            imUnreadNum.setText(num + "");
        } else if (num > MsgConstant.SHOW_MAX_EXACT_NUM) {
            imUnreadNum.setBackground(getResources().getDrawable(R.drawable.im_unread_num_bg));
            imUnreadNum.setText("99+");
        }*/
    }

    /**
     * 显示桌面未读数角标
     *
     * @param num
     */
    private void showBadgeNum(int num) {
        BadgeUtil.setBadge(MainActivity.this, num);
    }

    private Notification getNotification() {
        Log.e("MainActivity", "启动前台服务");
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(mContext,
                100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notification = new NotificationCompat.Builder(mContext, "push")
                    .setContentTitle("TEAMFACE")
                    .setContentText("")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.status_bar_icon)
                    .setContentIntent(pi)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                    .setAutoCancel(true)
                    .build();

        } else {
            notification = new Notification.Builder(mContext)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                    /**设置通知右边的小图标**/
                    .setSmallIcon(R.mipmap.status_bar_icon)
                    /**通知首次出现在通知栏，带上升动画效果的**/
                    .setTicker(Constants.APP_NAME)
                    /**点击跳转**/
                    .setContentIntent(pi)
                    /**设置通知的标题**/
                    .setContentTitle("TEAMFACE")
                    /**设置通知的内容**/
                    .setContentText("")
                    /**通知产生的时间，会在通知信息里显示**/
                    .setWhen(System.currentTimeMillis())
                    /**设置该通知优先级**/
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                    .setAutoCancel(true)
                    /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                    .setOngoing(false)
                    /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：Notification.DEFAULT_ALL就是3种全部提醒**/
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                    .setContentIntent(PendingIntent.getActivity(mContext, MsgConstant.NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();
        }
        return notification;
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (FileConstants.FILE_DOWNLOAD_PROGRESS_ACTION.equals(action)) {
                //文件下载进度
                ProgressBean bean = (ProgressBean) intent.getSerializableExtra(Constants.DATA_TAG1);
                if (bean != null && MsgConstant.NEW_VERSION_APK_ID.equals(bean.getFileId())) {
                    if (bean.isDone()
                            || bean.getBytesRead() == bean.getContentLength()
                            || (int) ((bean.getBytesRead() * 100) / bean.getContentLength()) == 100
                            ) {
                        File file = new File(JYFileHelper.getFileDir(context, Constants.PATH_DOWNLOAD), MsgConstant.NEW_VERSION_APK_ID + "12.apk");
                        installProcess(file);
                    }
                }
            }

        }

    }

    //安装应用的流程
    public void installProcess(File apk) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = isHasInstallPermissionWithO(mContext);
            if (!hasInstallPermission) {
                startInstallPermissionSettingActivity();
                return;
            } else {
                //有权限，开始安装应用程序
                installApk(apk);
            }
        } else {
            installApk(apk);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, Constants.REQUEST_CODE9);
    }

    //安装应用
    private void installApk(File apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        } else {//Android7.0之后获取uri要用contentProvider
            Uri uri = MyFileProvider.getUriForFile(getBaseContext(), Constants.FILE_PROVIDER, apk);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getBaseContext().startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context) {
        if (context == null) {
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }

    /**
     * 开启设置安装未知来源应用权限界面
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE8);
    }

    public void startIMService() {
        Intent intent = new Intent(mContext, IMService.class);
        try {
            if (!SystemFuncUtils.isServiceRunning(mContext, IMService.class.getName())) {
                //ComponentName name = mContext.startService(intent);
                //IM.getInstance().writeFileToSDCard(new Gson().toJson(name));
                if (Build.VERSION.SDK_INT >= 26) {
                    startForegroundService(intent);
                } else {
                    mContext.startService(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode && requestCode == Constants.REQUEST_CODE9) {
            File file = new File(JYFileHelper.getFileDir(mContext, Constants.PATH_DOWNLOAD), MsgConstant.NEW_VERSION_APK_ID + "12.apk");
            installProcess(file);
        }
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE8) {
            File file = new File(JYFileHelper.getFileDir(mContext, Constants.PATH_DOWNLOAD), MsgConstant.NEW_VERSION_APK_ID + "12.apk");
            installProcess(file);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mViewPager.getCurrentItem() == 1) {
            ((WorkbenchFragment) fragments.get(1)).setAutoPlay(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新未读数
        teamMessageRl.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.UPDATE_UNREAD_MSG_NUM, null));
            }
        }, 200);
        if (mViewPager == null || fragments.get(1) == null) {

        }
        if (mViewPager.getCurrentItem() == 1) {
            ((WorkbenchFragment) fragments.get(1)).setAutoPlay(true);
        } else {
            ((WorkbenchFragment) fragments.get(1)).setAutoPlay(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mReceiver != null) {
                unregisterReceiver(mReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
