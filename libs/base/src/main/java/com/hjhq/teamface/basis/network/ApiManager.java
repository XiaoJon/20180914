/*
 *
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 * Copyright (C) 2015 GuDong <maoruibin9035@gmail.com>
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hjhq.teamface.basis.network;


import android.util.ArrayMap;


/**
 * API 管理
 *
 * @author Administrator
 */
public class ApiManager<T> {
    /**
     * 默认配置 api 的集合,线程不安全但不会出现问题
     */
    private static ArrayMap<String, Object> apis = new ArrayMap<>();

    /**
     * 获取默认api
     *
     * @param t
     * @return
     */
    public T getAPI(Class<T> t) {
        T o = (T) apis.get(t);
        if (o == null) {
            o = new MainRetrofit.Builder<T>().build(t);
            apis.put(t.toString(), o);
        }
        return o;
    }

    public static void clear() {
        apis.clear();
    }
}
