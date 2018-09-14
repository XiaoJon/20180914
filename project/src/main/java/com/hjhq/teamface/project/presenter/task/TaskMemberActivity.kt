package com.hjhq.teamface.project.presenter.task

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.alibaba.fastjson.JSONObject
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hjhq.teamface.basis.bean.BaseBean
import com.hjhq.teamface.basis.constants.C
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.constants.ProjectConstants
import com.hjhq.teamface.basis.database.Member
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber
import com.hjhq.teamface.basis.util.CollectionUtils
import com.hjhq.teamface.basis.util.TextUtil
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.common.ui.member.SelectMemberActivity
import com.hjhq.teamface.common.ui.member.SelectRangeActivity
import com.hjhq.teamface.common.utils.CommonUtil
import com.hjhq.teamface.project.adapter.TaskMemberAdapter
import com.hjhq.teamface.project.bean.ProjectMemberResultBean
import com.hjhq.teamface.project.bean.TaskMemberListResultBean
import com.hjhq.teamface.project.model.TaskModel
import com.hjhq.teamface.project.ui.task.TaskMemberDelegate
import rx.Observable
import java.io.Serializable
import java.util.HashMap
import kotlin.collections.ArrayList

/**
 * 协作人
 * Created by Administrator on 2018/9/7.
 */

class TaskMemberActivity : ActivityPresenter<TaskMemberDelegate, TaskModel>() {
    private var projectId: Long = 0
    private lateinit var members: ArrayList<Member>
    private var chooseRanger: MutableList<Member>? = null

    private var taskId: Long = 0
    private var fromType: Long = 0
    private lateinit var mAdapter: TaskMemberAdapter

    private var mainTaskId: Long = 0

    override fun create(savedInstanceState: Bundle?) {
        super.create(savedInstanceState)
        if (savedInstanceState == null) {
            members = intent.getSerializableExtra(Constants.DATA_TAG1) as ArrayList<Member>
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0)
            taskId = intent.getLongExtra(ProjectConstants.TASK_ID, 0)
            fromType = intent.getLongExtra(ProjectConstants.TASK_FROM_TYPE, 0)
            mainTaskId = intent.getLongExtra(ProjectConstants.MAIN_TASK_ID, 0)
        }
    }


    override fun init() {
        if (projectId != 0L) {
            queryProjectMember(false)
        }
        mAdapter = TaskMemberAdapter(members)
        viewDelegate.setAdapter(mAdapter)
    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        viewDelegate.toolbar.setNavigationOnClickListener {
            val intent = Intent()
            intent.putExtra(Constants.DATA_TAG1, members)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        viewDelegate.mRecyclerView.addOnItemTouchListener(object : SimpleItemClickListener() {
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
                super.onItemChildClick(adapter, view, position)
                if (projectId == 0L) {
                    delPersonalTaskMembers(members, position)
                } else {
                    delTaskMembers(members[position], position)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        addMember()
        return super.onOptionsItemSelected(item)
    }

    private fun addMember() = if (projectId == 0L) {
        Observable.from<Member>(members).subscribe({ member -> member.isCheck = true })
        val bundle = Bundle()
        bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT)
        bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, members)
        CommonUtil.startActivtiyForResult(mContext, SelectMemberActivity::class.java, Constants.REQUEST_CODE1, bundle)
    } else {
        queryProjectMember(true)
    }


    private fun queryProjectMember(bl: Boolean) = if (chooseRanger == null) {
        model.queryProjectMember(mContext, projectId, object : ProgressSubscriber<ProjectMemberResultBean>(mContext) {
            override fun onNext(baseBean: ProjectMemberResultBean) {
                super.onNext(baseBean)
                val data = baseBean.data.dataList
                chooseRanger = ArrayList()
                if (!CollectionUtils.isEmpty(data)) {
                    for (projectMember in data) {
                        (chooseRanger as ArrayList<Member>).add(Member(projectMember.employee_id, projectMember.employee_name, C.EMPLOYEE))
                    }
                }
                if (bl) {
                    selectRangeMember()
                }
            }
        })
    } else {
        selectRangeMember()
    }

    private fun selectRangeMember() {
        Observable.from<Member>(members).subscribe { member ->
            member.selectState = C.CAN_NOT_SELECT
            member.isCheck = true
        }
        val bundle = Bundle()
        bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT)
        bundle.putSerializable(C.CHOOSE_RANGE_TAG, chooseRanger as Serializable)
        bundle.putSerializable(C.SELECTED_MEMBER_TAG, members)
        CommonUtil.startActivtiyForResult(mContext, SelectRangeActivity::class.java, Constants.REQUEST_CODE2, bundle)
    }

    /**
     * 获取协作人列表信息
     */
    private fun queryTaskMemberList() =
            model.queryTaskMemberList(this, projectId, taskId, fromType, 1, object : ProgressSubscriber<TaskMemberListResultBean>(this) {
                override fun onNext(taskMemberListResultBean: TaskMemberListResultBean) {
                    super.onNext(taskMemberListResultBean)
                    val data = taskMemberListResultBean.data
                    val newData = ArrayList<Member>()
                    if (!CollectionUtils.isEmpty(data.dataList)) {
                        for (dataBean in data.dataList) {
                            val member = Member(dataBean.employee_id, dataBean.employee_name, C.EMPLOYEE)
                            member.setSign_id(dataBean.id)
                            newData.add(member)
                        }
                    }
                    CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.data, newData)
                }
            })

    /**
     * 新增协作人
     */
    private fun saveTaskMembers(addMembers: List<Member>) {
        val json = JSONObject()
        val list = ArrayList<Map<String, Any>>()
        json.put("dataList", list)
        Observable.from(addMembers).subscribe { member ->
            val map = HashMap<String, Any>()
            map.put("projectId", projectId)
            map.put("taskId", taskId)
            map.put("typeStatus", fromType)
            map.put("employeeIds", member.id)
            map.put("project_task_role", "2")
            if (fromType == 2L) {
                //主任务id
                map.put("id", mainTaskId)
            }
            list.add(map)
        }

        model.saveTaskMembers(mContext, json, object : ProgressSubscriber<BaseBean>(mContext) {
            override fun onNext(baseBean: BaseBean) {
                super.onNext(baseBean)
                CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.data, members)
                queryTaskMemberList()
            }
        })
    }

    /**
     * 删除协作人
     *
     */
    private fun delTaskMembers(member: Member, position: Int) =
            model.delTaskMembers(mContext, TextUtil.parseLong(member.sign_id), object : ProgressSubscriber<BaseBean>(mContext) {
                override fun onNext(baseBean: BaseBean) {
                    super.onNext(baseBean)
                    mAdapter.remove(position)
                }
            })


    /**
     * 删除个人任务协作人
     *
     */
    private fun delPersonalTaskMembers(members: List<Member>, position: Int) {
        val sb = StringBuilder()
        Observable.from(members).filter { member -> member.id != members[position].id }.subscribe { member -> sb.append(",").append(member.id) }
        if (sb.isNotEmpty()) {
            sb.deleteCharAt(0)
        }
        model.updatePersonalTaskMembers(mContext, taskId, sb.toString(), fromType, object : ProgressSubscriber<BaseBean>(mContext) {
            override fun onNext(baseBean: BaseBean) {
                super.onNext(baseBean)
                mAdapter.remove(position)
            }
        })
    }

    /**
     * 编辑个人任务协作人
     *
     * @param data
     */
    private fun updatePersonalTaskMembers(members: List<Member>) {
        val sb = StringBuilder()
        Observable.from(members).subscribe { member -> sb.append(",").append(member.id) }
        if (sb.isNotEmpty()) {
            sb.deleteCharAt(0)
        }
        model.updatePersonalTaskMembers(mContext, taskId, sb.toString(), fromType, object : ProgressSubscriber<BaseBean>(mContext) {
            override fun onNext(baseBean: BaseBean) {
                super.onNext(baseBean)
                CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.data, members)
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        if (requestCode == Constants.REQUEST_CODE1) {
            var members = data.getSerializableExtra(Constants.DATA_TAG1) as List<Member>
            updatePersonalTaskMembers(members)
        } else if (requestCode == Constants.REQUEST_CODE2) {
            var members = data.getSerializableExtra(Constants.DATA_TAG1) as List<Member>
            saveTaskMembers(members)
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(Constants.DATA_TAG1, members as Serializable)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

}
