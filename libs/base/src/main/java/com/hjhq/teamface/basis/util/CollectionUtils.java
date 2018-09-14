package com.hjhq.teamface.basis.util;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.Collection;
import java.util.List;

/**
 * 集合工具类
 * Created by Samda on 2017/2/25.
 */

public class CollectionUtils {

    /**
     * 判断集合是否为null或者0个元素
     *
     * @param c 集合
     */
    public static boolean isEmpty(Collection c) {
        if (null == c || c.isEmpty()) {
            return true;
        }
        return false;
    }

    public static void addAll(Collection a, Collection b) {
        if (a != null && b != null) {
            a.addAll(b);
        }
    }
    public static void add(Collection a,Object o) {
        if (a != null) {
            a.add(o);
        }
    }
    public static void clear(Collection a) {
        if (a != null) {
            a.clear();
        }
    }

    public static int size(Collection c) {
        if (c == null) {
            return 0;
        }
        return c.size();
    }

    /**
     * '
     *
     * @param adapter
     * @param oldData
     * @param newData
     */
    public static void notifyDataSetChanged(RecyclerView.Adapter adapter, List oldData, List newData) {
        oldData.clear();
        addAll(oldData, newData);
        adapter.notifyDataSetChanged();
    }

    public static void notifyDataSetChanged(PagerAdapter adapter, List data, List list) {
        data.clear();
        addAll(data, list);
        adapter.notifyDataSetChanged();
    }

    public static void move(List c, int a, int b) {
        c.add(b, c.remove(a));
    }
}
