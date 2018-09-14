package com.hjhq.teamface.project.bean

import com.hjhq.teamface.basis.bean.BaseBean

/**
 * 企业工作流程
 * Created by Administrator on 2018/7/23.
 */

data class WorkFlowResultBean(
        val data: List<WorkFlowData>
) : BaseBean()

data class WorkFlowData(
        val members: String,
        val name: String,
        val nodeDataArray: String,
        val linkDataArray: String,
        val id: Long,
        val describe: String
)