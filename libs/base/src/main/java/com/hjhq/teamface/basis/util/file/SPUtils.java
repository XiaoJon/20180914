package com.hjhq.teamface.basis.util.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用SP工具类，不涉及业务
 */
public class SPUtils {
    private static Map<String, SharedPreferences> spMap = new HashMap<>();
    /**
     * App默认SharedPreferences的名称
     */
    public final static String SP_NAME = "teamface";


    /**
     * 获取 默认的SharedPreferences实例
     *
     * @param context : 可以使用任何一个上下文
     * @return SharedPreferences实例
     */
    public static SharedPreferences getSP(Context context) {
        return getSP(context, SP_NAME);
    }

    /**
     * 获取 SharedPreferences实例
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : 名称
     * @return SharedPreferences实例
     */
    public static SharedPreferences getSP(Context context, String name) {
        SharedPreferences sharedPreferences = spMap.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            spMap.put(name, sharedPreferences);
        }
        return sharedPreferences;
    }

    /**
     * 清除缓存
     */
    public static void cleanCache() {
        spMap.clear();
    }

    /**
     * 移除默认SP对应的数据
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 键
     * @return true:成功 false:失败
     */
    public static boolean remove(Context context, String key) {
        return remove(context, SP_NAME, key);
    }

    /**
     * 移除数据
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 键
     * @return true:成功 false:失败
     */
    public static boolean remove(Context context, String name, String key) {
        Editor edit = getSP(context, name).edit();// 获取编辑器
        return edit.remove(key).commit();
    }

    /**
     * 通过默认的SP获得boolean类型的数据，没有默认为false
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 存储的key
     * @return true:成功 false:失败
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, SP_NAME, key, false);
    }

    /**
     * 通过SP获得boolean类型的数据，没有默认为false
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 存储的key
     * @return true:成功 false:失败
     */
    public static boolean getBoolean(Context context, String name, String key) {
        return getBoolean(context, name, key, false);
    }

    /**
     * 通过SP获得boolean类型的数据
     *
     * @param context  : 可以使用任何一个上下文
     * @param name     : sp名称
     * @param key      : 存储的key
     * @param defValue : 默认值
     * @return true:成功 false:失败
     */
    public static boolean getBoolean(Context context, String name, String key, boolean defValue) {
        return getSP(context, name).getBoolean(key, defValue);
    }

    /**
     * 设置默认SP的boolean类型数据
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 缓存对应的key
     * @param value   : 缓存对应的值
     * @return true:成功 false:失败
     */
    public static boolean setBoolean(Context context, String key, boolean value) {
        return setBoolean(context, SP_NAME, key, value);
    }

    /**
     * 设置boolean的缓存数据
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 缓存对应的key
     * @param value   : 缓存对应的值
     * @return true:成功 false:失败
     */
    public static boolean setBoolean(Context context, String name, String key, boolean value) {
        Editor edit = getSP(context, name).edit();// 获取编辑器
        return edit.putBoolean(key, value).commit();
    }

    /**
     * 通过默认SP获得String类型的数据，没有默认为空字符串
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 存储的key
     * @return key所对应的值，没有则返回空字符串
     */
    public static String getString(Context context, String key) {
        return getString(context, SP_NAME, key, "");
    }

    /**
     * 通过SP获得String类型的数据，没有默认为空字符串
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 存储的key
     * @return key所对应的值，没有则返回空字符串
     */
    public static String getString(Context context, String name, String key) {
        return getString(context, name, key, "");
    }

    /**
     * 通过SP获得String类型的数据
     *
     * @param context  : 可以使用任何一个上下文
     * @param name     : sp名称
     * @param key      : 存储的key
     * @param defValue : 默认值
     * @return key所对应的值，没有则返回空字符串
     */
    public static String getString(Context context, String name, String key, String defValue) {
        return getSP(context, name).getString(key, defValue);
    }

    /**
     * 设置默认SP的String类型的缓存数据
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 缓存对应的key
     * @param value   : 缓存对应的值
     * @return true:成功 false:失败
     */
    public static boolean setString(Context context, String key, String value) {
        return setString(context, SP_NAME, key, value);
    }

    /**
     * 设置String的缓存数据
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 缓存对应的key
     * @param value   : 缓存对应的值
     * @return true:成功 false:失败
     */
    public static boolean setString(Context context, String name, String key, String value) {
        Editor edit = getSP(context, name).edit();// 获取编辑器
        if (value == null) {
            value = "";
        }
        return edit.putString(key, value).commit();
    }

    /**
     * 通过默认SP获得Long类型的数据，没有默认为-1
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 存储的key
     * @return key所对应的值，没有则返回-1
     */
    public static long getLong(Context context, String key) {
        return getLong(context, SP_NAME, key, -1);
    }

    /**
     * 通过SP获得Long类型的数据，没有默认为-1
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 存储的key
     * @return key所对应的值，没有则返回-1
     */
    public static long getLong(Context context, String name, String key) {
        return getLong(context, name, key, -1);
    }

    /**
     * 通过SP获得Long类型的数据
     *
     * @param context  : 可以使用任何一个上下文
     * @param name     : sp名称
     * @param key      : 存储的key
     * @param defValue : 默认值
     * @return key所对应的值
     */
    public static long getLong(Context context, String name, String key, long defValue) {
        return getSP(context, name).getLong(key, defValue);
    }

    /**
     * 设置默认SP的Long类型的缓存数据
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 缓存对应的key
     * @param value   : 缓存对应的值
     * @return true:成功 false:失败
     */
    public static boolean setLong(Context context, String key, long value) {
        return setLong(context, SP_NAME, key, value);
    }

    /**
     * 设置Long的缓存数据
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 缓存对应的key
     * @param value   : 缓存对应的值
     * @return true:成功 false:失败
     */
    public static boolean setLong(Context context, String name, String key, long value) {
        Editor editor = getSP(context, name).edit();// 获取编辑器
        return editor.putLong(key, value).commit();
    }

    /**
     * 设置默认SP对象类型的缓存数据
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 缓存对应的key
     * @param object  : 缓存对应的值
     * @return true:成功 false:失败
     */
    public static boolean setObject(Context context, String key, Object object) {
        return setObject(context, SP_NAME, key, object);
    }

    /**
     * 设置对象的缓存数据
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 缓存对应的key
     * @param object  : 缓存对应的值
     * @return true:成功 false:失败
     */
    public static boolean setObject(Context context, String name, String key, Object object) {
        String json = new Gson().toJson(object);
        return setString(context, name, key, json);
    }

    /**
     * 通过默认SP获得对象的数据
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 存储的key
     * @param type    : class对象
     * @return key所对应的值
     */
    public static <T> T getObject(Context context, String key, Class<T> type) {
        return getObject(context, SP_NAME, key, type);
    }

    /**
     * 通过SP获得对象的数据
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 存储的key
     * @param type    : class对象
     * @return key所对应的值
     */
    public static <T> T getObject(Context context, String name, String key, Class<T> type) {
        String str = getString(context, name, key);
        return type.cast(new Gson().fromJson(str, type));
    }

    /**
     * 设置默认SP集合的缓存数据 默认可存储null值
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 缓存对应的key
     * @param value   : 缓存对应的值
     * @return true:成功 false:失败
     */
    public static <T> boolean setDataList(Context context, String key, List<T> value) {
        return setDataList(context, SP_NAME, key, value, true);
    }

    /**
     * 设置集合的缓存数据 默认可存储null值
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 缓存对应的key
     * @param value   : 缓存对应的值
     * @return true:成功 false:失败
     */
    public static <T> boolean setDataList(Context context, String name, String key, List<T> value) {
        return setDataList(context, name, key, value, true);
    }

    /**
     * 设置集合的缓存数据,可自由设置是否存储null值
     *
     * @param context  : 可以使用任何一个上下文
     * @param name     : sp名称
     * @param key      : 缓存对应的key
     * @param value    : 缓存对应的值
     * @param nullable : 是否存储null值
     * @return true:成功 false:失败
     */
    public static <T> boolean setDataList(Context context, String name, String key, List<T> value, boolean nullable) {
        if (!nullable && null == value) {
            return false;
        }
        String strJson = new Gson().toJson(value);
        return setString(context, name, key, strJson);
    }

    /**
     * 通过默认SP获得集合的数据
     *
     * @param context : 可以使用任何一个上下文
     * @param key     : 存储的key
     * @return key所对应的值
     */
    public static <T> List<T> getDataList(Context context, String key) {
        return getDataList(context, SP_NAME, key);
    }

    /**
     * 通过SP获得集合的数据
     *
     * @param context : 可以使用任何一个上下文
     * @param name    : sp名称
     * @param key     : 存储的key
     * @return key所对应的值
     */
    public static <T> List<T> getDataList(Context context, String name, String key) {
        String strJson = getString(context, name, key, null);
        if (null == strJson) {
            return new ArrayList<>();
        }
        return new Gson().fromJson(strJson, new TypeToken<ArrayList<T>>() {
        }.getType());
    }
}
