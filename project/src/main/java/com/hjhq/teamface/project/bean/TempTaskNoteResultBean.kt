package com.hjhq.teamface.project.bean

import com.hjhq.teamface.basis.bean.BaseBean
import java.io.Serializable

/**
 * Created by Administrator on 2018/7/20.
 */
data class TaskNoteResultBean(
        val data: TempNoteData
) : BaseBean()

data class TempNoteData(
        val dataList: List<TempNoteDataList>
)

data class TempNoteDataList(
        val subnodeArr: List<TempNoteSubnodeArr>,
        val flow_status: String,
        val create_time: String,
        val temp_id: Long,
        val flow_id: String,
        val name: String,
        val del_status: String,
        val id: Long,
        val sort: Int
)

data class TempNoteSubnodeArr(
        val flow_status: String,
        val create_time: String,
        val flow_id: String,
        val name: String,
        val main_id: Long,
        val del_status: String,
        val id: Long,
        val sort: Int
) : Serializable