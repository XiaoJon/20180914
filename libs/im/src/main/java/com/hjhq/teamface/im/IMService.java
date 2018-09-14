package com.hjhq.teamface.im;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.hjhq.teamface.basis.bean.ImMessage;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.QxMessage;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.network.callback.DownloadCallback;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.download.service.DownloadService;
import com.hjhq.teamface.basis.constants.IMState;
import com.hjhq.teamface.im.db.DBManager;
import com.hjhq.teamface.im.util.ParseUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.NotYetConnectedException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author Administrator
 * @date 2017/11/23
 * Describe：企信服务
 */

public class IMService extends Service implements SocketStateListener {
    private static String TAG = "IMService";
    public static SocketClient mSocketClient;
    public static LoadLevelingClient mLlClient;
    private static ThreadPoolExecutor mThreadPoolExecutor;
    private static final SocketRunnable mRunnable = new SocketRunnable();
    private ScheduledExecutorService scheduledExecutorService;
    ScheduledFuture<?> scheduleAtFixedRate;
    private MyReceiver mReceiver;
    private NetStateReceiver mNetStateReceiver;
    private static IMService mIMService;
    private static List<byte[]> msgCache = new ArrayList<>();
    //加密WebSocket使用
    private static SSLContext sslContext = null;
    private static SSLSocketFactory factory;
    private static NotificationManager mNotificationManager;
    private boolean isSocketOpen = false;
    private boolean isWifiOk = false;
    private boolean isMobile = false;

    private static Context mContext;

    public IMService(Context context) {
        mContext = context;
    }

    public IMService() {
    }

    @Override
    public void onOpen(int socketType, ServerHandshake serverHandshake) {
        switch (socketType) {
            case MsgConstant.LL_SOCKET:
                if (!IMState.isImConnectLlOk() && IMState.isImCanLogin() && !IMState.getImOnlineState()) {
                    IM.getInstance().getLlServerUrl();
                }
                break;
            case MsgConstant.IM_SOCKET:
                isSocketOpen = true;
                // resendFailedMessage();
                break;
            default:

                break;
        }

    }

    /**
     * 重发发送失败的信息
     */
    private void resendFailedMessage() {
        if (mLlClient != null) {
            mLlClient.close();
        }
        ArrayList<SocketMessage> messages = DBManager.getInstance().getSendFailedMessage(MsgConstant.SEND_MSG_TIMEOUT);
        for (int i = 0; i < messages.size(); i++) {
            messages.get(i).setSendState(MsgConstant.SENDING);
            DBManager.getInstance().saveOrReplace(messages.get(i));
            resendFailedMessage(messages.get(i));
        }
    }

    /**
     * 重发失败的信息
     *
     * @param message
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void resendFailedMessage(SocketMessage message) {
        if (message.getMsgType() == MsgConstant.FILE
                || message.getMsgType() == MsgConstant.VIDEO
                || message.getMsgType() == MsgConstant.VOICE
                || message.getMsgType() == MsgConstant.IMAGE) {
            if (TextUtils.isEmpty(message.getFileUrl())) {
                //文件未上传成功
                String fileLocalPath = message.getFileLocalPath();
                File file = new File(fileLocalPath);
                if (file.exists()) {
                    //再次上传发送
                    QxMessage qxMessage = new QxMessage();
                    qxMessage.setSenderAvatar(IM.getInstance().getavatar());
                    qxMessage.setSenderName(IM.getInstance().getName());
                    qxMessage.setType(MsgConstant.IMAGE);
                    qxMessage.setChatId(message.getConversationId());
                    qxMessage.setFileName(file.getName());
                    qxMessage.setFilePath(fileLocalPath);
                    byte[] bytes = null;
                    if (message.getChatType() == MsgConstant.SINGLE) {
                        bytes = IM.getInstance().createMessage(TextUtil.parseLong(message.getReceiverID()), message, qxMessage);
                    } else if (message.getChatType() == MsgConstant.GROUP) {
                        message.setAllPeoples(message.getAllPeoples());
                        bytes = IM.getInstance().createMessage(message.getConversationId(), message, qxMessage);
                    }
                    ParseUtil.saveMessage(bytes);

                    DownloadService.sendFileMessage(file.getAbsolutePath(), bytes, qxMessage, new DownloadCallback() {
                        @Override
                        public void onSuccess(Call call, Response response) {
                            LogUtil.e("DownloadService   onSuccess");
                        }

                        @Override
                        public void onLoading(long total, long progress) {
                            LogUtil.e("DownloadService  total" + total);
                            LogUtil.e("DownloadService   progress" + progress);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            LogUtil.e("DownloadService   失败");
                        }
                    });
                }

            } else {
                IM.getInstance().resendFailedMessage(message);
            }


        } else {

            IM.getInstance().resendFailedMessage(message);
        }
    }

    @Override
    public void onError(Exception e) {
        //checkConnection();
    }

    @Override
    public void onClose(int type) {
        switch (type) {
            case 1:
                //聊天Socket关闭
                isSocketOpen = false;
                IMState.setImConnectLlOk(false);
                break;
            case 2:
                //负载均衡查询Socket关闭
                break;
            default:
                break;
        }
    }


    private void createNotificationChannel() {
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        /*if (!Constants.USE_LOAD_LEVELING && (scheduledExecutorService.isShutdown() || scheduledExecutorService.isTerminated())) {
            scheduleAtFixedRate = scheduledExecutorService.scheduleAtFixedRate(mRunnable, 3, 3, TimeUnit.SECONDS);
        }*/
        scheduleAtFixedRate = scheduledExecutorService.scheduleAtFixedRate(mRunnable, 3, 3, TimeUnit.SECONDS);
        mThreadPoolExecutor = new ThreadPoolExecutor(
                2,
                2,
                2L,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(),
                new com.hjhq.teamface.im.MessageThreadFactory()
        );
        mIMService = this;
        if (MsgConstant.USE_TLS) {
            initSocketClient();
            initIMSocket();
        } else {
            initIMSocket();
        }
        if (Constants.USE_LOAD_LEVELING) {
            initLlSocket();
        }
        EventBus.getDefault().register(this);
        initReceiver();

    }

    /**
     * 连接聊天Socket
     */
    private static void connectSocket() {
        try {
            mSocketClient.setSocket(factory.createSocket());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSocketClient.connect();
    }

    /**
     * 连接负载均衡Socket
     */
    public static void connectLLSocket() {
        try {
            mLlClient.setSocket(factory.createSocket());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mLlClient.connect();
    }

    /**
     * 初始化Socket
     */
    private void initSocketClient() {
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("创建TLS错误");
        }

        try {
            sslContext.init(null, new TrustManager[]{
                    new X509TrustManager() {

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            System.out.println("checkClientTrusted1");
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                                throws CertificateException {
                            System.out.println("checkClientTrusted2");
                        }

                        public void checkServerTrusted(X509Certificate[] certs,
                                                       String authType) {
                            System.out.println("checkServerTrusted1");
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                                throws CertificateException {
                            System.out.println("checkServerTrusted2");
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                            return new java.security.cert.X509Certificate[0];
                        }
                    }
            }, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = sslContext.getSocketFactory();
    }


    private void initReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MsgConstant.IM_ACTION_SEND);
        intentFilter.addAction(MsgConstant.IM_ACTION_LOGIN);
        //在其他设备登录
        intentFilter.addAction(MsgConstant.LOGIN_ON_OTHER_DEVICES);
        //退出登录
        intentFilter.addAction(MsgConstant.IM_ACTION_LOGOUT);
        //负载均衡
        intentFilter.addAction(MsgConstant.IM_ACTION_LOAD_LEVELING);
        //
        intentFilter.addAction(MsgConstant.IM_ACTION_ACK);
        //撤回消息
        intentFilter.addAction(MsgConstant.IM_ACTION_RECALL);
        //拉取历史消息
        intentFilter.addAction(MsgConstant.IM_ACTION_HISTORY);
        //账号注销后关闭之前的Socket并重新初始化一个Socket.
        intentFilter.addAction(MsgConstant.IM_ACTION_OPEN_NEW_SOCKET);
        registerReceiver(mReceiver, intentFilter);
        mNetStateReceiver = new NetStateReceiver();
        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
//        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetStateReceiver, filter);

    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        startForeground(100, getNotification());
        Log.e("IMService", "IM Service is staring.");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化Socket
     */
    private static void initIMSocket() {
        if (!IMState.isImCanLogin()) {
            closeSocket();
            return;
        }
        if (Constants.USE_LOAD_LEVELING && !IMState.isImConnectLlOk()) {
            closeSocket();
            return;
        }
        if (mSocketClient == null || !mSocketClient.isConnecting()) {
            mThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (Constants.USE_LOAD_LEVELING) {
                            mSocketClient = new SocketClient(new URI(SPHelper.getImSocketUri()));
                            //mSocketClient = new SocketClient(new URI(Constants.SOCKET_URI));
                        } else {
                            mSocketClient = new SocketClient(new URI(Constants.SOCKET_URI));
                        }

                        mSocketClient.setSocketStateChangeListener(mIMService);

                       /* Log.e("连接状态=", mSocketClient.isConnecting() + "");
                        Log.e("打开状态=", mSocketClient.isOpen() + "");
                        Log.e("mSocketClient状 态=", mSocketClient.getReadyState().name());*/
                        try {
                            if (MsgConstant.USE_TLS) {
                                connectSocket();
                            } else {
                                mSocketClient.connect();
                            }
                        } catch (Exception e) {
                            Log.e("IMService:", "SocketClient连接错误!");
                        }
                        /*Log.e("mSocketClient线程名字=", Thread.currentThread().getName());
                        Log.e("mSocketClient连接=", mSocketClient.isConnecting() + "");
                        Log.e("mSocketClient打开状态=", mSocketClient.isOpen() + "");
                        Log.e("mSocketClient状态=", mSocketClient.getReadyState().name());*/


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            try {
                if (MsgConstant.USE_TLS) {
                    connectSocket();
                } else {
                    mSocketClient.connect();
                }
            } catch (Exception e) {
                Log.e("IMService:", "SocketClient连接错误!");
            }
        }
    }

    public static void initLlSocket() {
        if (!Constants.USE_LOAD_LEVELING) {
            return;
        }
        if (!IMState.isImCanLogin()) {
            return;
        }
        if (mLlClient == null || !mLlClient.isConnecting()) {
            mThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        mLlClient = new LoadLevelingClient(new URI(Constants.LL_URI));
                        mLlClient.setSocketStateChangeListener(mIMService);

                        Log.e("mmLlClient状 态=", mLlClient.getReadyState().name());

                        try {
                            if (IMState.isImCanLogin()) {
                                if (MsgConstant.USE_TLS) {
                                    connectLLSocket();
                                } else {
                                    mLlClient.connect();
                                }
                            } else {
                                closeSocket();
                            }

                        } catch (Exception e) {
                            Log.e("IMService:", "SocketClient连接错误!");
                        }
                       /* Log.e("mLlClient线程名字=", Thread.currentThread().getName());
                        Log.e("mLlClient连接=", mLlClient.isConnecting() + "");
                        Log.e("mLlClient打开状态=", mLlClient.isOpen() + "");
                        Log.e("mLlClient状态=", mLlClient.getReadyState().name());*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            try {
                if (IMState.isImCanLogin()) {
                    if (MsgConstant.USE_TLS) {
                        connectLLSocket();
                    } else {
                        mLlClient.connect();
                    }
                } else {
                    closeSocket();
                }

            } catch (Exception e) {
                Log.e("IMService:", "mLlClient连接错误!");
            }
        }
    }

    /**
     * 检测WebSocket连接状态
     */
    public static void checkConnection() {
        if (mSocketClient == null || WebSocket.READYSTATE.CLOSED.equals(mSocketClient.getReadyState())
                || WebSocket.READYSTATE.CLOSING.equals(mSocketClient.getReadyState())
            // || WebSocket.READYSTATE.NOT_YET_CONNECTED.equals(mSocketClient.getReadyState())
                ) {
            if (IMState.isImCanLogin()) {
                initIMSocket();
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSocketClient != null) {
            mSocketClient = null;
        }
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        if (mNetStateReceiver != null) {
            unregisterReceiver(mNetStateReceiver);
        }
//        timer.cancel();
//        timer = null;
        if (!scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdownNow();
        }
        //this.startForeground(MsgConstant.NOTIFICATION_ID, getNotification());
        EventBusUtils.sendEvent(new MessageBean(0, MsgConstant.IM_SERVICE_DYING, null));
        IM.getInstance().writeFileToSDCard(DateTimeUtil.longToStr(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss   ") + "IMService onDestroy");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serviceMethod(ImMessage m) {
        if (MsgConstant.IM_LOGIN_SUCCESSS_TAG.equals(m.getTag())) {
            scheduleAtFixedRate = scheduledExecutorService.scheduleAtFixedRate(mRunnable, 3, 3, TimeUnit.SECONDS);
            resendFailedMessage();
        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            if (MsgConstant.IM_ACTION_LOAD_LEVELING.equals(intent.getAction())) {
                sendLlSocketData(intent);
                /*if (scheduledExecutorService.isShutdown()) {
                    scheduledExecutorService.scheduleAtFixedRate(mRunnable, 3, 3, TimeUnit.SECONDS);
                }*/
                return;
            }

            if (MsgConstant.IM_ACTION_OPEN_NEW_SOCKET.equals(intent.getAction())) {
                if (mSocketClient != null) {
                    mSocketClient.close();
                }
                initIMSocket();
                return;
            }

            if (mSocketClient == null || WebSocket.READYSTATE.CLOSED.equals(mSocketClient.getReadyState())
                    || WebSocket.READYSTATE.CLOSING.equals(mSocketClient.getReadyState())
                // || WebSocket.READYSTATE.NOT_YET_CONNECTED.equals(mSocketClient.getReadyState())
                    ) {

                IMState.setImOnlineState(false);
                initIMSocket();
            } else {
                switch (intent.getAction()) {
                    case MsgConstant.IM_ACTION_LOGIN:
                        sendImSocketData(intent);
                        break;
                    case MsgConstant.IM_ACTION_SEND:
                        sendImSocketData(intent);
                        break;
                    case MsgConstant.IM_ACTION_ACK:
                        sendImSocketData(intent);
                        break;
                    case MsgConstant.IM_ACTION_RECALL:
                        sendImSocketData(intent);
                        break;
                    case MsgConstant.IM_ACTION_HISTORY:
                        sendImSocketData(intent);
                        break;
                    case MsgConstant.IM_ACTION_LOGOUT:
                        sendImSocketData(intent);
                        closeSocket();
                        IMState.setImConnectLlOk(false);
                        if (scheduleAtFixedRate != null || !scheduleAtFixedRate.isDone()) {
                            scheduleAtFixedRate.cancel(true);
                        }
                        break;
                    case MsgConstant.LOGIN_ON_OTHER_DEVICES:
                        //账号在其它设备登录,停止检测socket状态
                        if (!scheduledExecutorService.isShutdown()) {
                            scheduledExecutorService.shutdownNow();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 关闭Socket
     */
    private static void closeSocket() {
        if (mSocketClient != null) {
            mSocketClient.close();
        }
    }

    /**
     * WebSocket发送数据
     *
     * @param intent
     */
    private void sendImSocketData(Intent intent) {

        if (WebSocket.READYSTATE.OPEN.equals(mSocketClient.getReadyState()))
            try {
                mSocketClient.send(intent.getByteArrayExtra(MsgConstant.MSG_DATA));
            } catch (NotYetConnectedException e) {
                e.printStackTrace();
            }
    }

    private void sendLlSocketData(Intent intent) {
        initLlSocket();
        if (mLlClient != null && WebSocket.READYSTATE.OPEN.equals(mLlClient.getReadyState())) {
            try {
                mLlClient.send(intent.getByteArrayExtra(MsgConstant.MSG_DATA));
            } catch (NotYetConnectedException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification getNotification() {
        /*Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(mContext,
                Constants.REQUEST_CODE1, intent, PendingIntent.FLAG_CANCEL_CURRENT);*/
        //RemoteViews rvs = new RemoteViews("com.hjhq.teamface.im", R.layout.custom_filter_fragment);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(mContext, "push")
                    .setWhen(System.currentTimeMillis())
                    .setCustomContentView(null)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentTitle("TEAMFACE")
                    //.setContentText("TeamFace正在运行")
                    //.setSmallIcon(R.mipmap.status_bar_icon)
                    //.setContentIntent(pi)
                    //.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo))
                    .setAutoCancel(false)
                    .setOngoing(true)
                    //.setCustomContentView(getRemoteViews(mContext))
                    .build();

        } else {
            notification = new Notification.Builder(mContext)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), com.hjhq.teamface.basis.R.mipmap.ic_launcher))
                    /**设置通知右边的小图标**/
                    .setSmallIcon(com.hjhq.teamface.basis.R.mipmap.status_bar_icon)
                    /**通知首次出现在通知栏，带上升动画效果的**/
                    .setTicker(Constants.APP_NAME)
                    /**点击跳转**/
                    // .setContentIntent(pi)
                    /**设置通知的标题**/
                    .setContentTitle("TEAMFACE")
                    /**设置通知的内容**/
                    //.setContentText("TeamFace正在运行")
                    /**通知产生的时间，会在通知信息里显示**/
                    .setWhen(System.currentTimeMillis())
                    /**设置该通知优先级**/
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                    .setAutoCancel(false)
                    /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                    .setOngoing(true)
                    /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：Notification.DEFAULT_ALL就是3种全部提醒**/
                    // .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                    //  .setContentIntent(PendingIntent.getActivity(mContext, MsgConstant.NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();
            //notification.contentView = getRemoteViews(mContext);
        }


        return notification;
    }

    public RemoteViews getRemoteViews(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_notification_action);
        Intent intent = new Intent();
        //设置PendingIntent

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //为id为openActivity的view设置单击事件

        remoteViews.setOnClickPendingIntent(R.id.rl_action1, pendingIntent);

        //将RemoteView作为Notification的布局

       /* notification.contentView = remoteViews;

        //将pendingIntent作为Notification的intent，这样当点击其他部分时，也能实现跳转

        notification.contentIntent = pendingIntent;*/


        return remoteViews;
    }

    /**
     * 定时检测Socket
     */
    static class SocketRunnable implements Runnable {
        @Override
        public void run() {
            Log.e(TAG, "定时检测Socket");
            if (Constants.USE_LOAD_LEVELING && IMState.isImConnectLlOk()) {
                checkConnection();
            } else {
                if (IMState.isImCanLogin()) {
                    initIMSocket();
                }
            }
        }
    }

    /**
     * 监听网络连接变化(断网,联网等)
     */
    class NetStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取ConnectivityManager对象对应的NetworkInfo对象
                //获取WIFI连接的信息
                NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    //  Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
                    isWifiOk = true;
                    isMobile = true;
                } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                    //  Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
                    isWifiOk = true;
                    isMobile = false;
                } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    //  Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
                    isWifiOk = false;
                    isMobile = true;
                } else {
                    //  Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
                    isWifiOk = false;
                    isMobile = false;
                }
            } else {
                //API大于23时使用下面的方式进行网络监听

                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                //获取所有网络连接的信息
                Network[] networks = connMgr.getAllNetworks();
                //用于存放网络连接信息
                StringBuilder sb = new StringBuilder();
                //通过循环将网络信息逐个取出来
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                    sb.append(networkInfo.getTypeName() + networkInfo.getSubtypeName() + " connect is " + networkInfo.isConnected());
                    if ("WIFI".equals(networkInfo.getTypeName())) {
                        isWifiOk = networkInfo.isConnected();
                    } else if ("MOBILE".equals(networkInfo.getTypeName())) {
                        isWifiOk = networkInfo.isConnected();
                    }
                }
                // Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
            }
            if (isWifiOk) {

                if (IMState.isImCanLogin()) {
                    if (Constants.USE_LOAD_LEVELING) {
                        initLlSocket();
                    } else {
                        initIMSocket();
                    }
                }

            }
            if (!isWifiOk && isMobile) {
// TODO: 2018/7/19 连接移动网络时的处理
                if (Constants.IS_DEBUG) {
                    ToastUtils.showToast(context, "大佬,测试请打开wifi");
                }
            }
        }
    }
}
