package com.hjhq.teamface.project.util

import android.content.Context
import com.hjhq.teamface.basis.util.TextUtil
import com.hjhq.teamface.basis.util.ToastUtils

/**
 * Created by Administrator on 2018/6/20.
 */

object ProjectUtil {
    /**
     * 检测项目权限
     */
    fun checkProjectPermission(context: Context, priviledgeIds: String?, permission: Int): Boolean {
        if (priviledgeIds == null) {
            ToastUtils.showError(context, "未获取到权限")
            return false
        }
        if (TextUtil.isEmpty(priviledgeIds)) {
            ToastUtils.showError(context, "权限不足")
            return false
        }
        val split = priviledgeIds.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        return split.indices.any { split[it] == permission.toString() }
    }

    fun checkPermission(priviledgeIds: String?, permission: Int): Boolean {
        if (TextUtil.isEmpty(priviledgeIds)) {
            return false
        }
        val split = priviledgeIds!!.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        return split.indices.any { split[it] == permission.toString() }
    }
}
