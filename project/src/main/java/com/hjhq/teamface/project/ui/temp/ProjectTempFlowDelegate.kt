package com.hjhq.teamface.project.ui.temp

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import com.hjhq.teamface.basis.util.device.DeviceUtils
import com.hjhq.teamface.basis.zygote.AppDelegate
import com.hjhq.teamface.common.view.ScaleTransitionPagerTitleView
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.adapter.ProjectTempFlowAdapter
import com.hjhq.teamface.project.bean.TempNoteDataList
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * 项目模板 的流程节点
 * Created by Administrator on 2018/7/20.
 */
class ProjectTempFlowDelegate : AppDelegate() {
    private lateinit var mMagicIndicator: MagicIndicator
    private lateinit var mViewPager: ViewPager
    lateinit var mAdapter: ProjectTempFlowAdapter
    override fun getRootLayoutId(): Int = R.layout.project_activity_temp_flow

    override fun isToolBar(): Boolean = true


    override fun initWidget() {
        super.initWidget()
        setTitle(R.string.project_temp)
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm))


        mMagicIndicator = get(R.id.magic_indicator)
        mViewPager = get(R.id.view_pager)
    }

    /**
     * 初始化导航
     */
    fun initNavigator(fragmentManagers: FragmentManager, dataList: List<TempNoteDataList>) {
        mMagicIndicator.visibility = View.VISIBLE
        mAdapter = ProjectTempFlowAdapter(fragmentManagers, dataList)
        mViewPager.adapter = mAdapter
        mViewPager.offscreenPageLimit = 2

        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.adapter = MyCommonNavigatorAdapter(dataList)
        commonNavigator.isFollowTouch = true
        commonNavigator.setPadding(15, 0, 15, 0)
        mMagicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(mMagicIndicator, mViewPager)
    }

    /**
     * 导航适配器
     */
    private inner class MyCommonNavigatorAdapter(val nodeList: List<TempNoteDataList>?) : CommonNavigatorAdapter() {

        override fun getCount(): Int = nodeList?.size ?: 0

        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
            val scaleTransitionPagerTitleView = ScaleTransitionPagerTitleView(context)
            scaleTransitionPagerTitleView.normalColor = ContextCompat.getColor(mContext, R.color.gray_99)
            scaleTransitionPagerTitleView.gravity = Gravity.CENTER
            scaleTransitionPagerTitleView.selectedColor = ContextCompat.getColor(mContext, R.color.app_blue)
            scaleTransitionPagerTitleView.textSize = 14f
            scaleTransitionPagerTitleView.text = nodeList!![index].name
            scaleTransitionPagerTitleView.setOnClickListener { mViewPager.currentItem = index }
            val padding = DeviceUtils.dpToPixel(context, 15f).toInt()
            scaleTransitionPagerTitleView.setPadding(padding, 0, padding, 0)
            return scaleTransitionPagerTitleView
        }

        override fun getIndicator(context: Context): IPagerIndicator {
            val indicator = LinePagerIndicator(context)
            indicator.mode = LinePagerIndicator.MODE_EXACTLY
            indicator.lineWidth = DeviceUtils.dpToPixel(mContext, 56f)
            indicator.lineHeight = DeviceUtils.dpToPixel(mContext, 2f)
            indicator.yOffset = DeviceUtils.dpToPixel(mContext, 0f)
            indicator.setColors(ContextCompat.getColor(mContext, R.color.app_blue))
            return indicator
        }
    }
}