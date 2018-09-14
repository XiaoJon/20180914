package com.hjhq.teamface.view.quickaction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow.OnDismissListener;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * QuickAction dialog, shows action list as icon and text like the one in
 * Gallery3D app. Currently supports vertical and horizontal layout.
 *
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *         <p>
 *         Contributors: - Kevin Peck <kevinwpeck@gmail.com>
 */
public class QuickAction extends PopupWindows implements OnDismissListener {

    public static final int ID_CUSTOMER = 1;
    public static final int ID_ACTIVITY = 2;
    public static final int ID_QUOTE = 3;
    public static final int ID_ORDER = 4;
    public static final int ID_PROCEEDS = 5;
    public static final int ID_INVOICE = 6;
    public static final int ID_NOTICE = 7;
    public static final int ID_TO_ALL = 8;

    public static final int QUICK_MODULE_COUNT = 12;
    public static final String ADD = "ADD";

    private View mRootView;
    private LayoutInflater mInflater;
//    private ImageView ivBg;

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    private GridView mTrack;

    private ImageButton newcreate_image;

    private ActionAdapter actionAdapter;
    // private ScrollView mScroller;
    private OnActionItemClickListener mItemClickListener;
    private OnDismissListener mDismissListener;

    private List<ActionItem> actionItems = new ArrayList<ActionItem>();

    private boolean mDidAction;

    private int mChildPos;
    private int mInsertPos;
    private int mAnimStyle;
    private int mOrientation;
    private int rootWidth = 0;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int VERTICAL_TOP = 2;

    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_REFLECT = 4;
    public static final int ANIM_AUTO = 5;

    private Activity activity;

    private boolean showdirection = false;

    public Activity getContext() {
        return activity;
    }

    public void setContext(Activity activity) {
        this.activity = activity;
    }

    public static final int POPUPWINDOW_WRAP_CONTENT = WindowManager.LayoutParams.WRAP_CONTENT;
    public static final int POPUPWINDOW_MATCH_CONTENT = WindowManager.LayoutParams.MATCH_PARENT;

    private int popupWindowWidth = POPUPWINDOW_MATCH_CONTENT;
    private int popupWindowHeight = POPUPWINDOW_WRAP_CONTENT;

    private boolean isDialogMode = false;

    /**
     * Constructor for default vertical layout
     *
     * @param context Context
     */
    public QuickAction(Activity context) {
        this(context, VERTICAL);
    }

    /**
     * Constructor allowing orientation override
     *
     * @param activity    activity
     * @param orientation Layout orientation, can be vartical or horizontal
     */
    public QuickAction(Activity activity, int orientation) {
        super(activity);

        mOrientation = orientation;

        this.activity = activity;

        mInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // if (mOrientation == HORIZONTAL) {
        // setRootViewId(getRootViewId());
        // } else {
        setRootViewId(getRootViewId());
        // }

        mAnimStyle = ANIM_AUTO;
        mChildPos = 0;
    }

    public int getRootViewId() {

        if (mOrientation == HORIZONTAL) {
            return R.layout.popup_horizontal_new;
        } else if (mOrientation == VERTICAL) {
            return R.layout.popup_vertical;
        } else {
            return mOrientation;
        }

    }

    /**
     * Get action item at an index
     *
     * @param index Index of item (position from callback)
     * @return Action Item at the position
     */
    public ActionItem getActionItem(int index) {
        return actionItems.get(index);
    }

    /**
     * Set root view.
     *
     * @param id Layout resource id
     */
    public void setRootViewId(int id) {
        mRootView = mInflater.inflate(id, null);


//        ivBg = mRootView.findViewById(R.id.iv_bg);
        mTrack = mRootView.findViewById(R.id.tracks);
        newcreate_image = mRootView
                .findViewById(R.id.newcreate_image);

        actionAdapter = new ActionAdapter(activity, actionItems);
        mTrack.setAdapter(actionAdapter);
        mTrack.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {

            ActionItem item = actionItems.get(arg2);
            if (mItemClickListener != null) {

                mItemClickListener.onItemClick(item);
            }
        });

        newcreate_image.setOnClickListener(v -> {
            dismiss();
        });

        mRootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        setContentView(mRootView);
    }

    public void rotateCButton(Activity activity, final View v, float start,
                              float end, int duration, int endDrawable) {
        // TODO Auto-generated method stub

        Object object = v.getTag();

        final Drawable icon;
        RotateAnimation animation;
        if (null == object) {

            icon = activity.getResources().getDrawable(endDrawable);
            v.setTag(0);
            animation = new RotateAnimation(start, end,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            icon = activity.getResources().getDrawable(endDrawable);
            v.setTag(null);
            animation = new RotateAnimation(end, start,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        }

        animation.setDuration(duration);
        animation.setFillAfter(true);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                v.setBackground(icon);

            }
        });

        v.startAnimation(animation);

    }

    /**
     * Set animation style
     *
     * @param mAnimStyle animation style, default is set to ANIM_AUTO
     */
    public void setAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
    }

    /**
     * Set listener for action item clicked.
     *
     * @param listener Listener
     */
    public void setOnActionItemClickListener(OnActionItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public OnActionItemClickListener getmItemClickListener() {
        return mItemClickListener;
    }

    public List<ActionItem> getActionItems() {
        return actionItems;
    }

    public void setActionItems(List<ActionItem> actionItems) {
        this.actionItems = actionItems;
    }

    public void addActionItem(final List<ActionItem> actionItems) {

        if (null != actionItems) {
            this.actionItems.clear();
            this.actionItems.addAll(actionItems);
            if (null != actionAdapter) {
                actionAdapter.notifyDataSetChanged();
            }
        }
    }

    public boolean ismDidAction() {
        return mDidAction;
    }

    public void setmDidAction(boolean mDidAction) {
        this.mDidAction = mDidAction;
    }

    public int getmChildPos() {
        return mChildPos;
    }

    public void setmChildPos(int mChildPos) {
        this.mChildPos = mChildPos;
    }

    public int getItemlay() {

        int layID = R.layout.action_item_vertical_top;

        if (mOrientation == HORIZONTAL) {
            layID = R.layout.action_item_horizontal;
        } else if (mOrientation == VERTICAL) {

            layID = R.layout.action_item_vertical;
        }

        return layID;
    }

    public void show(View anchor, int dx, int dy) {
        preShow(getPopupWindowWidth(), getPopupWindowHeight());

        mDidAction = false;

        int[] location = new int[2];

        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1], location[0]
                + anchor.getWidth(), location[1] + anchor.getHeight());


        if (rootWidth == 0) {
            rootWidth = mRootView.getMeasuredWidth();
        }

        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight() + StatusBarUtil.getStatusBarHeight(mContext);


        int dyTop = anchorRect.top;
        int dyBottom = screenHeight - anchorRect.bottom;

        boolean onTop = (dyTop > dyBottom) ? true : false;


        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

        int statusBarHeight = StatusBarUtil.getStatusBarHeight(mContext);
        int height = (int) ScreenUtils.getScreenHeight(mContext) + statusBarHeight;

        int decorHeight = height;
        if (DeviceUtils.checkDeviceHasNavigationBar(mContext)) {
            View ActivityRoot = activity.findViewById(R.id.activity_root);
            int heightDiff = ActivityRoot.getRootView().getHeight() - ActivityRoot.getHeight();
            if (heightDiff <= statusBarHeight) {
                height += DeviceUtils.getNavigationBarHeight(mContext);
            }
            decorHeight -= DeviceUtils.getNavigationBarHeight(mContext);
        }

        //毛玻璃效果
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        //去掉导航栏
//        Bitmap drawingCache = Bitmap.createBitmap(decorView.getDrawingCache(), 0, 0, (int) ScreenUtils.getScreenWidth(mContext),
//                decorHeight);

//        byte[] bytes = ImageformatUtil.bitmapToByte(decorView.getDrawingCache());
//        Glide.with(mContext).load(bytes)
//                .bitmapTransform(new BlurTransformation(mContext, 20), new CenterCrop(mContext))
//                .into(ivBg);

        //简单粗暴，设置window的高度为屏幕高度，前面的不管了
        mWindow.setHeight(height);
        //允许弹出窗口超出屏幕范围。默认情况下，窗口被夹到屏幕边界。设置为false将允许Windows精确定位。
        mWindow.setClippingEnabled(false);
        //缩进到状态栏
        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0,
                -StatusBarUtil.getStatusBarHeight(mContext));

//        rotateCButton(activity, newcreate_image, 0f, 135f, 500,
//                R.drawable.icon_quick_close);

//		loadMirrorView();

    }

    /**
     * Show quickaction popup. Popup is automatically positioned, on top or
     * bottom of anchor view.
     */
    public void show(View anchor) {

        show(anchor, 0, 0);
    }

    /**
     * Set animation style
     *
     * @param screenWidth screen width
     * @param requestedX  distance from left edge
     * @param onTop       flag to indicate where the popup should be displayed. Set TRUE
     *                    if displayed on top of anchor view and vice versa
     */
    private void setAnimationStyle(int screenWidth, int requestedX,
                                   boolean onTop) {
        // int arrowPos = requestedX - mArrowUp.getMeasuredWidth()/2;
        int arrowPos = requestedX;
        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
                        : R.style.Animations_PopDownMenu_Left);
                break;

            case ANIM_GROW_FROM_RIGHT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
                        : R.style.Animations_PopDownMenu_Right);
                break;

            case ANIM_GROW_FROM_CENTER:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
                        : R.style.Animations_PopDownMenu_Center);
                break;

            case ANIM_REFLECT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect
                        : R.style.Animations_PopDownMenu_Reflect);
                break;

            case ANIM_AUTO:
                if (arrowPos <= screenWidth / 4) {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
                            : R.style.Animations_PopDownMenu_Left);
                } else if (arrowPos > screenWidth / 4
                        && arrowPos < 3 * (screenWidth / 4)) {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
                            : R.style.Animations_PopDownMenu_Center);
                } else {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
                            : R.style.Animations_PopDownMenu_Right);
                }

                break;
            default:
                break;
        }
    }

    /**
     * Set listener for window dismissed. This listener will only be fired if
     * the quicakction dialog is dismissed by clicking outside the dialog or
     * clicking on sticky item.
     */
    public void setOnDismissListener(QuickAction.OnDismissListener listener) {
        setOnDismissListener(this);

        mDismissListener = listener;
    }

    @Override
    public void onDismiss() {

//        rotateCButton(activity, newcreate_image, 0f, 135f, 500,
//                R.drawable.icon_quick_close);

        if (!mDidAction && mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    /**
     * Listener for item click
     */
    public interface OnActionItemClickListener {
        public abstract void onItemClick(ActionItem item);
    }

    /**
     * Listener for window dismiss
     */
    public interface OnDismissListener {
        void onDismiss();
    }

    public int getPopupWindowWidth() {
        return popupWindowWidth;
    }

    public void setPopupWindowWidth(int popupWindowWidth) {
        this.popupWindowWidth = popupWindowWidth;
    }

    public int getPopupWindowHeight() {
        return popupWindowHeight;
    }

    public void setPopupWindowHeight(int popupWindowHeight) {
        this.popupWindowHeight = popupWindowHeight;
    }

    public boolean isDialogMode() {
        return isDialogMode;
    }

    public void setDialogMode(boolean isDialogMode) {
        this.isDialogMode = isDialogMode;
    }

    public boolean isShowdirection() {
        return showdirection;
    }

    public void setShowdirection(boolean showdirection) {
        this.showdirection = showdirection;
    }

}