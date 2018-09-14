package com.hjhq.teamface.basis;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import butterknife.ButterKnife;


/**
 * 所有标题的activity的父类
 * 在这里主要统一处理标题
 * Created by Administrator on 2016/7/13.
 */
public abstract class BaseTitleActivity extends BaseActivity {

    protected final static int MENU_GROUP_ID = 0;
    protected final static int[] MENU_IDS = {0, 1, 2, 3, 4, 5};
    protected final static String[] DESC = {"文字", "图片"};
    public TextView titleTv;
    public Toolbar toolbar;
    protected View gradientLine;
    protected FrameLayout container;
    public RelativeLayout baseParentRl;
    public String[] rightMenuTexts;
    public int[] rightMenuIcons;
    public int menuTextColor = R.color.title_bar_right_txt_default_color;
    String[] color = new String[2];
    private MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        color[0] = "#00CFC9";
        color[1] = "#F9B239";
    }

    @Override
    @Deprecated
    protected int getContentView() {
        return R.layout.activtiy_base_title;
    }


    @Override
    protected void initView() {
        baseParentRl = (RelativeLayout) findViewById(R.id.base_content);
        container = (FrameLayout) findViewById(R.id.container);
        container.addView(getLayoutInflater().inflate(getChildView(), null));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        gradientLine = findViewById(R.id.gradient_line);
        titleTv = (TextView) findViewById(R.id.title_tv);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_blue_back);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setGradientVisiblilty(false);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    protected void setNavigationOnClickListener(View.OnClickListener listener){
        toolbar.setNavigationOnClickListener(listener);
    }

    protected void setStatusBarBackground(int res) {
        toolbar.setBackgroundResource(res);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (rightMenuIcons != null) {
            for (int i = 0; i < rightMenuIcons.length; i++) {
                MenuItem item = menu.add(MENU_GROUP_ID, MENU_IDS[i], 0, DESC[1]);
                item.setIcon(ContextCompat.getDrawable(mContext, rightMenuIcons[i]));
                item.setShowAsAction(Menu.FLAG_ALWAYS_PERFORM_CLOSE);
            }
        }
        if (rightMenuTexts != null) {
            for (int i = 0; i < rightMenuTexts.length; i++) {
                int rightIconLenth = rightMenuIcons == null ? 0 : rightMenuIcons.length;
                MenuItem item = menu.add(MENU_GROUP_ID, MENU_IDS[i + rightIconLenth], 0, DESC[0]);
                item.setTitle(Html.fromHtml(rightMenuTexts[i]));
                setActionBarText(this, menuTextColor);
                item.setShowAsAction(Menu.FLAG_ALWAYS_PERFORM_CLOSE);
            }
        }
        menuId = new int[menu.size()];
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            menuId[i] = item.getItemId();
        }
        return true;
    }

    private Menu aMenu;         //获取optionmenu

    public void setMenuId(int... menuId) {
        this.menuId = menuId;
    }

    private int[] menuId;     //显示menu ID

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuItem = item;
        onRightMenuClick(item.getItemId());
        onRightMenuClick(item.getItemId(), item.getTitle().toString());
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        aMenu = menu;
        checkOptionMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    public void checkOptionMenu() {
        int index = 0;
        if (null != aMenu && menuId != null) {
            for (int i = 0; i < aMenu.size(); i++) {
                MenuItem item = aMenu.getItem(i);
                item.setVisible(false);
                for (int itemId : menuId) {
                    if (item.getItemId() == itemId) {
                        item.setVisible(true);
                        break;
                    }
                }
            }
        }
    }


    protected MenuItem getMunuItem() {
        return menuItem;
    }

    /**
     * toolBar右边图片的点击事件,数组0,1,2,3
     *
     * @param itemId
     */
    protected void onRightMenuClick(int itemId) {
    }

    /**
     * toolBar右边图片的点击事件,数组0,1,2,3
     *
     * @param itemId
     */
    protected void onRightMenuClick(int itemId, String desc) {
    }

    protected int[] getMenuIDs() {
        return MENU_IDS;
    }

    protected abstract int getChildView();

    @Override
    protected abstract void setListener();

    @Override
    public abstract void onClick(View view);

    @Override
    protected boolean isTitleActivity() {
        return true;
    }

    /**
     * @param resId
     */
    protected void setActivityTitle(int resId) {
        titleTv.setText(resId);
    }

    public void setActivityTitleColor(int colorId) {
        titleTv.setTextColor(getResources().getColor(colorId));
    }

    protected void setActivityTitle(String text) {
        titleTv.setText(text);
    }

    protected View getContainer() {
        return container;
    }

    protected View getTitleView() {
        return titleTv;
    }

    protected void setGradientVisiblilty(boolean flag) {
        gradientLine.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    public RelativeLayout getBaseParentRl() {
        return baseParentRl;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public String[] getRightMenuTexts() {
        return rightMenuTexts;
    }

    public void setRightMenuTexts(String... rightMenuTexts) {
        this.rightMenuTexts = rightMenuTexts;
    }

    public void setRightMenuColorTexts(String... rightMenuTexts) {

        this.rightMenuTexts = rightMenuTexts;
        menuTextColor = getResources().getColor(R.color.title_bar_right_txt_bright_color);
    }

    public void setRightMenuColorTexts(int colorRes, String... rightMenuTexts) {

        this.rightMenuTexts = rightMenuTexts;
        menuTextColor = getResources().getColor(colorRes);
    }

    public int[] getRightMenuIcons() {
        return rightMenuIcons;
    }

    public void setRightMenuIcons(int... rightMenuIcons) {
        this.rightMenuIcons = rightMenuIcons;
    }

    /**
     * 设置toolbar不同见
     */
    public void setToolBarGone() {
        toolbar.setVisibility(View.GONE);
    }

    public static void setActionBarText(final Activity activity, int mColor) {

        try {
            final LayoutInflater inflater = activity.getLayoutInflater();
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(inflater, false);
            LayoutInflaterCompat.setFactory(inflater, new LayoutInflaterFactory() {
                @Override
                public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                    //因为我使用的是supportv7包
                    if (name.equalsIgnoreCase("android.support.v7.view.menu.IconMenuItemView")
                            || name.equalsIgnoreCase("android.support.v7.view.menu.ActionMenuItemView")) {
                        final View view;
                        try {
                            view = inflater.createView(name, null, attrs);
                            if (view instanceof TextView) {
                                ((TextView) view).setTextColor(mColor);
                            }

                            return view;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InflateException ex) {
                            ex.printStackTrace();
                        }
                    }
                    return null;
                }
            });
        } catch (Exception e) {

        }

    }
}
