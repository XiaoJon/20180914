package com.hjhq.teamface.basis.util;

import com.hjhq.teamface.basis.util.log.LogUtil;


/**
 * 省市区 工具类
 *
 * @author lx
 * @date 2017/9/25
 */

public class RegionUtil {

    /**
     * 编码转换成地区名
     *
     * @param code 编码
     * @return 地区名
     */
    public static String code2String(String code) {
        if (TextUtil.isEmpty(code)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        String[] split = code.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                String[] split1 = split[i].split(":");
                sb.append(split1[split1.length - 1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("省市区数据异常！");
        }
        return sb.toString();
    }

    public static String getCode(String value) {
        if (TextUtil.isEmpty(value)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        String[] split = value.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                String[] split1 = split[i].split(":");
                sb.append(split1[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("省市区数据异常！");
        }
        return sb.toString();
    }

    public static String getArea(String value) {
        String area = "";
        if (value == null) {
            return area;
        }
        try {
            String[] split = value.split(",");
            String[] split2 = split[split.length - 1].split(":");
            area = split2[0];
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("省市区数据异常！");
        }
        return area;
    }
}
