/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hjhq.teamface.basis.zygote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.bean.ToolMenu;
import com.hjhq.teamface.basis.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * View delegate base class
 * 视图层代理的基类
 */
public abstract class AppDelegate implements IDelegate {

    protected AppCompatActivity mContext;
    protected final SparseArray<View> mViews = new SparseArray<>();

    protected ViewGroup rootLayout;
    /**
     * Menu 集合
     */
    List<ToolMenu> menus = new ArrayList<>();


    public abstract int getRootLayoutId();

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int rootLayoutId = getRootLayoutId();
        if (isToolBar()) {
            rootLayout = (ViewGroup) inflater.inflate(R.layout.root_layout, container, false);
            View toolbar = inflater.inflate(R.layout.toolbar_comment, container, false);
            View rootView = inflater.inflate(rootLayoutId, container, false);
            rootLayout.addView(toolbar);
            rootLayout.addView(rootView);
        } else {
            rootLayout = (ViewGroup) inflater.inflate(rootLayoutId, container, false);
        }
    }

    @Override
    public Toolbar getToolbar() {
        return get(R.id.toolbar);
    }

    @Override
    public View getRootView() {
        return rootLayout;
    }

    /**
     * 是否需要toolbar
     *
     * @return
     */
    public abstract boolean isToolBar();

    /**
     * 初始化控件
     */
    @Override
    public void initWidget() {
        mContext = getActivity();
        if (isToolBar()) {
            Toolbar toolbar = getToolbar();
            getActivity().setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.icon_blue_back);

            getActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(view -> getActivity().finish());
        }
    }

    public <T extends View> T bindView(int id) {
        T view = (T) mViews.get(id);
        if (view == null) {
            view = (T) rootLayout.findViewById(id);
            mViews.put(id, view);
        }
        return view;
    }

    public <T extends View> T get(int id) {
        return (T) bindView(id);
    }

    public void setVisibility(int id, int visibility) {
        get(id).setVisibility(visibility);
    }

    public void setVisibility(int id, boolean visibility) {
        get(id).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    public void setText(int id, String content) {
        TextUtil.setText((TextView) get(id), content);
    }

    public void setOnClickListener(View.OnClickListener listener, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            get(id).setOnClickListener(listener);
        }
    }

    public void toast(CharSequence msg) {
        Toast.makeText(rootLayout.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public <T extends AppCompatActivity> T getActivity() {
        return (T) rootLayout.getContext();
    }

    @Override
    public List<ToolMenu> getMenus() {
        return menus;
    }

    /**
     * 添加文本Menu
     *
     * @param rightMenuTexts 文本内容
     */
    public void setRightMenuTexts(String... rightMenuTexts) {
        setRightMenuTexts(0, rightMenuTexts);
    }

    /**
     * 添加带颜色的文本menu
     *
     * @param color
     * @param rightMenuTexts
     */
    public void setRightMenuTexts(int color, String... rightMenuTexts) {
        int[] colors = new int[rightMenuTexts.length];
        for (int i = 0; i < rightMenuTexts.length; i++) {
            colors[i] = color;
        }
        setRightMenuTexts(colors, rightMenuTexts);
    }

    /**
     * 为每个文本menu添加颜色
     *
     * @param colors
     * @param rightMenuTexts
     */
    public void setRightMenuTexts(int[] colors, String... rightMenuTexts) {
        if (rightMenuTexts == null) {
            throw new NullPointerException("MenuTexts is Null");
        }
        if (colors.length != rightMenuTexts.length) {
            throw new NegativeArraySizeException("colors和 menuTexts数量对不上");
        }
        int size = menus.size();
        int length = rightMenuTexts.length;
        for (int i = 0; i < length; i++) {
            ToolMenu menu = new ToolMenu(i + size, rightMenuTexts[i], colors[i], null);
            menus.add(menu);
        }
    }

    /**
     * 添加图标
     *
     * @param rightMenuIcons
     */
    public void setRightMenuIcons(int... rightMenuIcons) {
        if (rightMenuIcons == null) {
            throw new NullPointerException("MenuIcons is Null");
        }
        int size = menus.size();
        int length = rightMenuIcons.length;
        for (int i = 0; i < length; i++) {
            ToolMenu menu = new ToolMenu(i + size, null, rightMenuIcons[i]);
            menus.add(menu);
        }
    }

    /**
     * 清除掉图标
     */
    public void clearRightMenu() {
        menus.clear();
    }

    /**
     * 设置显示的menu,未设置的隐藏
     *
     * @param menuIds
     */
    public void showMenu(int... menuIds) {
        for (ToolMenu menu : menus) {
            menu.setShow(false);
        }
        for (int menuId : menuIds) {
            if (menus.size() > menuId) {
                menus.get(menuId).setShow(true);
            }
        }
        getActivity().invalidateOptionsMenu();
    }

    /**
     * 修改menu按钮颜色
     *
     * @param menuId
     * @param color
     */
    public void setMenuTextColor(int menuId, int color) {
        getMenus().get(menuId).setTitleColor(color);
        getActivity().invalidateOptionsMenu();
    }

    /**
     * 隐藏标题的下划线
     */
    public void hideTitleLine() {
        get(R.id.title_line).setVisibility(View.GONE);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void close() {
        mViews.clear();
        rootLayout = null;
    }

    public void setTitle(String title) {
        TextView titleTv = get(R.id.title_tv);
        TextUtil.setText(titleTv, title);
    }

    public void setTitle(int res) {
        setTitle(mContext.getString(res));
    }

    public void setToolTitle(String title) {
        getToolbar().setTitle(title);
    }

    public void setToolTitle(int res) {
        setToolTitle(mContext.getString(res));
    }


}
