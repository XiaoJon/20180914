package com.hjhq.teamface.project.bean

import com.hjhq.teamface.basis.database.Member
import com.hjhq.teamface.common.bean.ProjectLabelBean
import java.io.Serializable

/**
 * Created by Administrator on 2018/8/2.
 */
data class PersonalTaskBean(
        val relation_data: String,
        val reference_relation: String,
        val del_status: String,
        val project_custom_id: Long,
        //项目截止时间
        val datetime_deadline: String?,
        //项目开始时间
        val datetime_starttime: String?,
        val bean_name: String,
        val picklist_tag_v: String,
        val from: Int,
        val picklist_tag: List<ProjectLabelBean>,
        val id: Long,
        val sub_id: Long?,
        val project_id: Long?,
        val personnel_create_by: String,
        val multitext_desc: String,
        var complete_status: String,
        //个人任务字段 激活次数
        val activate_number: Int?,
        //子任务总数
        val subtotal: Int?,
        //子任务完成数量
        val subfinishtotal: Int?,
        val check_member: String,
        val check_status: String,
        val relation_id: String,
        val participants_only: String,
        val personnel_execution: List<Member>,
        val employee_id: String,
        val name: String,
        val text_name: String
) : Serializable