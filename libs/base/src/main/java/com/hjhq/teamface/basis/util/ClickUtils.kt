package com.hjhq.teamface.basis.util

/**
 * Created by Administrator on 2018/7/18.
 * Describe：防止连击
 */
object ClickUtils {
    var time: Long = 0L
    fun click(listener: ClickUtils.ClickListener) = if (System.currentTimeMillis() - time > 1500) {
        time = System.currentTimeMillis()
        listener.click()
    } else {
        
    }

    interface ClickListener {
        fun click()
    }
}