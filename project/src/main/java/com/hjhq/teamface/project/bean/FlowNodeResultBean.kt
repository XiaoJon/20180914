package com.hjhq.teamface.project.bean

import com.hjhq.teamface.basis.bean.BaseBean
import java.io.Serializable

/**
 * Created by Administrator on 2018/7/23.
 */

data class FlowNodeResultBean(
        val data: FlowNodeData
) : BaseBean()

data class FlowNodeData(
        val nodes: List<FlowNode>,
        val workflow: FlowNodeWorkflow
) : Serializable

data class FlowNodeWorkflow(
        val name: String,
        val id: Int,
        val describe: String
) : Serializable

data class FlowNode(
        val text: String,
        val key: String
) : Serializable
