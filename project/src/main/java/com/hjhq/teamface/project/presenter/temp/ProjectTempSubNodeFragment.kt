package com.hjhq.teamface.project.presenter.temp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.util.TextUtil
import com.hjhq.teamface.basis.util.device.DeviceUtils
import com.hjhq.teamface.basis.util.device.ScreenUtils
import com.hjhq.teamface.project.R

/**
 * 项目模板流程 任务列表
 * Created by Administrator on 2018/7/20.
 */
class ProjectTempSubNodeFragment : Fragment() {
    private lateinit var tempName: String
    private lateinit var tvTaskTempName: TextView
    private lateinit var llTaskTemp: View
    private var index: Int = 0

    companion object {
        fun newInstance(tempName: String, index: Int): ProjectTempSubNodeFragment {
            val myFragment = ProjectTempSubNodeFragment()
            val bundle = Bundle()
            bundle.putString(Constants.DATA_TAG1, tempName)
            bundle.putInt(Constants.DATA_TAG2, index)
            myFragment.arguments = bundle
            return myFragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            tempName = arguments.getString(Constants.DATA_TAG1)
            index = arguments.getInt(Constants.DATA_TAG2)
        }
        TextUtil.setText(tvTaskTempName, tempName)

        val screenWidth = ScreenUtils.getScreenWidth(activity)
        val dpToPixel = DeviceUtils.dpToPixel(activity, (30 * 2).toFloat())
        var layoutParams = llTaskTemp.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = (screenWidth - dpToPixel).toInt()

        var marginStart = 0F
        if (index != 0) {
            marginStart = 15F
        }
        layoutParams.marginStart = DeviceUtils.dpToPixel(activity, marginStart).toInt()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(activity, R.layout.project_fragment_temp_sub_node, null)
        tvTaskTempName = view.findViewById<TextView>(R.id.tv_task_temp_name)
        llTaskTemp = view.findViewById<View>(R.id.ll_tesk_temp)
        return view
    }


}
