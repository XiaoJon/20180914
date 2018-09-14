package com.hjhq.teamface.basis.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * json数据处理工具类
 *
 * @author Administrator
 * @date 2018/5/17
 */

public class JsonParser<T> {

    /**
     * 将未知的json对象转换成集合
     *
     * @param object 未知json对象
     * @param t      对象类型
     * @return 集合
     */
    public List<T> jsonFromList(Object object, Class<T> t) {
        List<T> list = new ArrayList<>();
        try {
            if (object instanceof JSONArray) {
                list = JSONArray.parseArray(object + "", t);
            } else if (object instanceof List) {
                List<Map<String, Object>> value = (List<Map<String, Object>>) object;
                for (Map<String, Object> map : value) {
                    T approveAuthBean = JSONObject.parseObject(JSON.toJSONString(map), t);
                    list.add(approveAuthBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将未知的json对象转换成实体对象
     *
     * @param object 未知json对象
     * @param t      对象类型
     * @return 对象
     */
    public T jsonFromObject(Object object, Class<T> t) {
        T t1 = null;
        try {
            if (object instanceof JSONObject) {
                t1 = JSONObject.parseObject(object + "", t);
            } else if (object instanceof Map) {
                t1 = JSONObject.parseObject(JSON.toJSONString(object), t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t1;
    }
}
