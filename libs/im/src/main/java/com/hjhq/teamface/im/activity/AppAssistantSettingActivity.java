package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.common.view.HelperItemView;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.bean.AppAssistantInfoBean;

/**
 * @author Administrator
 *         小助手设置界面
 */
public class AppAssistantSettingActivity extends BaseTitleActivity {
    private static final String TAG = "AppAssistantSettingActivity";

    private Context mContext;
    private LinearLayout llRoot;
    private RecyclerView rvList;
    private String id;
    private String iconType;
    private String iconColor;
    private String iconUrl;
    private String assistantType;
    private String beanName;
    private HelperItemView mPutOnTop;
    private HelperItemView mNoDisturb;
    private HelperItemView mOnlyUnread;
    private TextView mMarkAllRead;
    private TextView mName;
    private TextView mDesc;
    private ImageView mAssistantIcon;
    private boolean isChanged = false;
    private int padding;

    @Override
    protected int getChildView() {
        return R.layout.activity_app_assistant_setting;
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle(getString(R.string.setting));
        beanName = getIntent().getStringExtra(MsgConstant.BEAN_NAME);
        id = getIntent().getStringExtra(MsgConstant.CONVERSATION_ID);
        iconType = getIntent().getStringExtra(Constants.DATA_TAG1);
        iconColor = getIntent().getStringExtra(Constants.DATA_TAG2);
        iconUrl = getIntent().getStringExtra(Constants.DATA_TAG3);
        assistantType = getIntent().getStringExtra(Constants.DATA_TAG4);
        mContext = this;
        llRoot = (LinearLayout) findViewById(R.id.ll_root);
        mPutOnTop = (HelperItemView) findViewById(R.id.put_on_top);
        mNoDisturb = (HelperItemView) findViewById(R.id.no_disturb_rl);
        mOnlyUnread = (HelperItemView) findViewById(R.id.only_unread);
        mMarkAllRead = (TextView) findViewById(R.id.mark_all_read);
        mName = (TextView) findViewById(R.id.title);
        mDesc = (TextView) findViewById(R.id.desc);
        mAssistantIcon = (ImageView) findViewById(R.id.icon);
        llRoot.setVisibility(View.INVISIBLE);

    }


    @Override
    protected void setListener() {
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChanged) {
                    setResult(Activity.RESULT_OK);
                }
                finish();
            }
        });
        setOnClicks(mMarkAllRead);
        //置顶
        mPutOnTop.setOnChangedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CommonUtil.showToast("置顶");
                ImLogic.getInstance().setTop(AppAssistantSettingActivity.this, id,
                        MsgConstant.SELF_DEFINED,
                        new ProgressSubscriber<BaseBean>(AppAssistantSettingActivity.this, false) {
                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.showToast(mContext, "执行失败!");
                                mPutOnTop.setSelected(!mPutOnTop.getSelected());
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.UPDATE_CONVERSATION_TAG, null));
                            }
                        });
            }
        });
        //免打扰
        mNoDisturb.setOnChangedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CommonUtil.showToast("置顶");
                ImLogic.getInstance().noBother(AppAssistantSettingActivity.this, id,
                        MsgConstant.SELF_DEFINED,
                        new ProgressSubscriber<BaseBean>(AppAssistantSettingActivity.this, false) {
                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.showToast(mContext, "执行失败!");
                                mNoDisturb.setSelected(!mNoDisturb.getSelected());
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.UPDATE_CONVERSATION_TAG, null));
                            }
                        });
            }
        });
        //只看未读
        mOnlyUnread.setOnChangedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImLogic.getInstance().markReadOption(AppAssistantSettingActivity.this, id,
                        new ProgressSubscriber<BaseBean>(AppAssistantSettingActivity.this, false) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);

                                ToastUtils.showToast(mContext, "操作失败!");
                                mOnlyUnread.setSelected(!mOnlyUnread.getSelected());
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                super.onNext(baseBean);
                                EventBusUtils.sendEvent(new MessageBean(mOnlyUnread.getSelected() ? 1 : 0, MsgConstant.VIEW_READED, id));
                                isChanged = true;
                            }
                        });
            }
        });

    }


    @Override
    protected void initData() {
        padding = (int) DeviceUtils.dpToPixel(mContext, 5);
        ImLogic.getInstance().getAssisstantInfo(AppAssistantSettingActivity.this, id,
                new ProgressSubscriber<AppAssistantInfoBean>(this, true) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AppAssistantInfoBean appAssistantInfoBean) {
                        super.onNext(appAssistantInfoBean);

                        mNoDisturb.setSelected("1".equals(appAssistantInfoBean.getData().getNo_bother()));
                        mPutOnTop.setSelected("1".equals(appAssistantInfoBean.getData().getTop_status()));
                        mOnlyUnread.setSelected("1".equals(appAssistantInfoBean.getData().getShow_type()));
                        mName.setText(appAssistantInfoBean.getData().getName());
                        mDesc.setText("汇集所有的" + appAssistantInfoBean.getData().getName().replace("小助手", "") + "信息");
                        String assistantName = appAssistantInfoBean.getData().getName();
                        int assistantType = appAssistantInfoBean.getData().getType();
                        // initIcon(assistantType, assistantName);
                        loadImage(AppAssistantSettingActivity.this, assistantType);
                        if (MsgConstant.IM_ASSISTANT != appAssistantInfoBean.getData().getType()) {
                            findViewById(R.id.only_unread).setVisibility(View.VISIBLE);
                            findViewById(R.id.mark_all_read).setVisibility(View.VISIBLE);
                        }
                        llRoot.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * 图标
     *
     * @param assistantType
     * @param assistantName
     */
    private void initIcon(int assistantType, String assistantName) {
        int drawableRes = 0;
        switch (assistantType) {
            case MsgConstant.MODULE_ASSISTANT:
                mAssistantIcon.setBackground(mContext.getResources().getDrawable(R.drawable.module_tag_stroke_bg));
                GradientDrawable drawable = (GradientDrawable) mAssistantIcon.getBackground();
                drawable.setColor(Color.WHITE);

                if (!TextUtil.isEmpty(iconColor)) {
                    drawable.setColor(ColorUtils.hexToColor(iconColor, "#3689E9"));
                }

                if ("1".equals(iconType)) {
                    ImageLoader.loadCircleImage(mContext, iconUrl, mAssistantIcon, R.drawable.ic_image);
                } else if (TextUtil.isEmpty(iconUrl)) {
                    ImageLoader.loadCircleImage(mContext, R.drawable.ic_image, mAssistantIcon);
                } else {
                    //将 - 转换成 _
                    String replace = iconUrl.replace("-", "_");
                    int resId = mContext.getResources().getIdentifier(replace, "drawable", mContext.getPackageName());
                    ImageLoader.loadCircleImage(mContext, resId, mAssistantIcon, R.drawable.ic_image);
                }
                break;
            case MsgConstant.EMAIL_ASSISTANT:
                drawableRes = R.drawable.icon_email_assistant;
                break;
            case MsgConstant.MEMO_ASSISTANT:
                drawableRes = R.drawable.icon_memo_assistant;
                break;
            case MsgConstant.APPROVE_ASSISTANT:
                drawableRes = R.drawable.icon_approval_assistant;
                break;
            case MsgConstant.IM_ASSISTANT:
                drawableRes = R.drawable.icon_chat_assistant;
                break;
            case MsgConstant.FILE_LIB_ASSISTANT:
                drawableRes = R.drawable.icon_file_assistant;
                break;
            case MsgConstant.TASK_ASSISTANT:
                drawableRes = R.drawable.icon_task_assistant;
                break;
            default:
                break;
        }
        if (drawableRes > 0) {
            mAssistantIcon.setBackground(null);
            ImageLoader.loadCircleImage(this, drawableRes, mAssistantIcon);
        } else {

            if (TextUtils.isEmpty(assistantName)) {
                assistantName = "助手";
            } else {
                if (assistantName.length() > 2) {
                    assistantName = assistantName.substring(0, 2);
                }
            }

            ImageLoader.loadCircleImage(this, "", mAssistantIcon, assistantName);
        }

    }

    /**
     * 加载小助手图标
     */
    public void loadImage(Context context, int assistantType) {
        String english_name = beanName;
        if (english_name == null) english_name = "";
        int drawableRes = -1;
        switch (assistantType) {
            case MsgConstant.MODULE_ASSISTANT:
                mAssistantIcon.setBackground(getResources().getDrawable(R.drawable.module_tag_round_bg));
                GradientDrawable drawable = (GradientDrawable) mAssistantIcon.getBackground();
                drawable.setColor(Color.WHITE);
                if ("1".equals(iconType)) {

                    mAssistantIcon.setPadding(padding, padding, padding, padding);
                } else {
                    mAssistantIcon.setPadding(0, 0, 0, 0);
                }
                drawable.setColor(ColorUtils.hexToColor(iconColor, "#3689E9"));

                if ("1".equals(iconType)) {
                    ImageLoader.loadCircleImage(mContext, iconUrl, mAssistantIcon, R.drawable.ic_image);
                    return;
                } else if (TextUtil.isEmpty(iconUrl)) {
                    ImageLoader.loadCircleImage(mContext, R.drawable.ic_image, mAssistantIcon);
                    return;
                } else {
                    int padding = (int) DeviceUtils.dpToPixel(mContext, 10);
                    //将 - 转换成 _
                    String replace = iconUrl.replace("-", "_");
                    int resId = mContext.getResources().getIdentifier(replace, "drawable", mContext.getPackageName());
                    ImageLoader.loadCircleImage(mContext, resId, mAssistantIcon, R.drawable.ic_image);
                    return;
                }
            case MsgConstant.IM_ASSISTANT:
                drawableRes = R.drawable.icon_chat_assistant;
                break;
            case MsgConstant.APPROVE_ASSISTANT:
                drawableRes = R.drawable.icon_approval_assistant;
                break;
            case MsgConstant.FILE_LIB_ASSISTANT:
                drawableRes = R.drawable.icon_file_assistant;
                break;
            case MsgConstant.EMAIL_ASSISTANT:
                drawableRes = R.drawable.icon_email_assistant;
                break;
            case MsgConstant.MEMO_ASSISTANT:
                drawableRes = R.drawable.icon_memo_assistant;
                break;
            case MsgConstant.TASK_ASSISTANT:
                drawableRes = R.drawable.icon_task_assistant;
                break;
        }
        if (drawableRes > 0) {
            mAssistantIcon.setBackground(null);
            ImageLoader.loadCircleImage(this, drawableRes, mAssistantIcon);
            return;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mark_all_read) {
            ImLogic.getInstance().markAllRead(AppAssistantSettingActivity.this, id, new ProgressSubscriber<BaseBean>(AppAssistantSettingActivity.this) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    isChanged = true;
                    ToastUtils.showToast(mContext, "操作成功");
                    long conversationId = TextUtil.parseLong(id) + MsgConstant.DEFAULT_VALUE;
                    EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.MARK_ALL_READ, conversationId));
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (isChanged) {
            setResult(Activity.RESULT_OK);
        }
        finish();
    }
}
