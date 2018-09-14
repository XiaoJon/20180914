package com.hjhq.teamface.attendance.bean

import com.hjhq.teamface.basis.bean.BaseBean

/**
 * Created by Administrator on 2018/7/26.
 * Describe：
 */

data class AttendanceSettingDetailBean(
        val data: AttendanceSettingDetailBeanData
) : BaseBean()

data class AttendanceSettingDetailBeanData(
        val id: String?,
        val admin_arr: String?,
        val remind_clock_before_work: String,
        val remind_clock_after_work: String,
        val list_set_type: String,
        val list_set_early_arrival: String,
        val list_set_diligent: String,
        val list_set_be_late: String,
        val list_set_sort_type: String,
        val late_nigth_walk_arr: List<Any>,
        val humanization_allow_late_times: String,
        val humanization_allow_late_minutes: String,
        val absenteeism_rule_be_late_minutes: String,
        val absenteeism_rule_leave_early_minutes: String,
        val create_by: String,
        val create_time: String,
        val modify_by: String,
        val modify_time: String,
        val del_status: String
)
