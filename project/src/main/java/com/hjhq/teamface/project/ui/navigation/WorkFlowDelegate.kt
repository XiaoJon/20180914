package com.hjhq.teamface.project.ui.navigation

import android.support.v4.view.ViewPager
import android.widget.ImageView
import android.widget.LinearLayout
import com.hjhq.teamface.basis.util.device.DeviceUtils
import com.hjhq.teamface.basis.zygote.AppDelegate
import com.hjhq.teamface.basis.zygote.FragmentAdapter
import com.hjhq.teamface.project.R

class WorkFlowDelegate : AppDelegate() {
    override fun getRootLayoutId(): Int = R.layout.project_activity_work_flow

    override fun isToolBar(): Boolean = true

    private lateinit var llPoint: LinearLayout

    lateinit var mViewPager: ViewPager

    override fun initWidget() {
        super.initWidget()
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm))
        llPoint = get(R.id.ll_point)
        mViewPager = get(R.id.view_pager)
        mViewPager.offscreenPageLimit = 3
    }


    fun addPoint() {
        val pointSize = DeviceUtils.dpToPixel(mContext, 5f).toInt()
        // 1.根据图片多少，添加多少小圆点
        val pointParams = LinearLayout.LayoutParams(pointSize, pointSize)
        pointParams.setMargins(0, 0, pointSize, 0)
        val iv = ImageView(mContext)
        iv.layoutParams = pointParams
        iv.setBackgroundResource(R.drawable.icon_gray_point)
        llPoint.addView(iv, 0)
    }

    fun setPointIndex(i: Int) {
        val childCount = llPoint.childCount
        for (j in 0 until childCount) {
            val child = llPoint.getChildAt(j)
            if (j == i) {
                child.setBackgroundResource(R.drawable.icon_blue_point)
            } else {
                child.setBackgroundResource(R.drawable.icon_gray_point)
            }
        }
    }

    fun setAdapter(mAdapter: FragmentAdapter) {
        mViewPager.adapter = mAdapter
    }
}