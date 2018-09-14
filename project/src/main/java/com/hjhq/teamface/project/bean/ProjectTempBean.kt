package com.hjhq.teamface.project.bean

import com.hjhq.teamface.basis.bean.BaseBean
import java.io.Serializable


data class ProjectTempBean(
        val data: TempData
) : BaseBean()

data class TempData(
        val dataList: List<TempDataList>,
        val groupList: List<TempGroupList>
)

data class TempDataList(
        var temp_type: Int,
        var temp_name: String,
        var name: String,
        var system_default_pic: String,
        var id: Long,
        var sort: Int,
        var pic_url: String,
        var template_role: Int
) : Serializable

data class TempGroupList(
        val tempTypeName: String,
        val tempTypeId: Int
) : Serializable