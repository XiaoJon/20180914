package com.hjhq.teamface.common.utils;

/**
 * 成员工具类
 * Created by Administrator on 2018/1/11.
 */

public class MemberUtils {
    /**
     * 判断@param totalState 是否含有  @param state 状态
     *
     * @param totalState 总状态
     * @param state      状态
     * @return 是否含有
     */
    public static boolean checkState(int totalState, int state) {
        return (totalState & state) == state;
    }

    /**
     * 判断@param totalState 是否含有  @param state 状态
     *
     * @param totalState 总状态
     * @param state      状态
     * @return 是否含有
     */
    public static boolean checkState(int totalState, int... state) {
        boolean bl = true;
        for (int i = 0; i < state.length; i++) {
            if ((totalState & state[i]) != state[i]) {
                bl = false;
            }
        }
        return bl;
    }
}
