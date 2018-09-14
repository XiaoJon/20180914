package com.hjhq.teamface.project.presenter.temp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.project.bean.TaskNoteResultBean
import com.hjhq.teamface.project.bean.TempDataList
import com.hjhq.teamface.project.bean.TempNoteDataList
import com.hjhq.teamface.project.model.ProjectModel
import com.hjhq.teamface.project.ui.temp.ProjectTempFlowDelegate

/**
 * 项目模板节点流程
 */
class ProjectTempFlowActivity : ActivityPresenter<ProjectTempFlowDelegate, ProjectModel>() {

    private lateinit var dataList: List<TempNoteDataList>
    private lateinit var itemBean: TempDataList

    override fun create(savedInstanceState: Bundle?) {
        super.create(savedInstanceState)
        if (savedInstanceState == null) {
            itemBean = intent.getSerializableExtra(Constants.DATA_TAG1) as TempDataList
        }
    }


    override fun init() =
            model.queryTaskNoteByTempId(mContext, itemBean.id, object : ProgressSubscriber<TaskNoteResultBean>(mContext) {
                override fun onNext(t: TaskNoteResultBean) {
                    super.onNext(t)
                    dataList = t.data.dataList
                    viewDelegate.initNavigator(supportFragmentManager, dataList)
                }
            })

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent = Intent()
        intent.putExtra(Constants.DATA_TAG1, itemBean)
        setResult(Activity.RESULT_OK, intent)
        finish()
        return super.onOptionsItemSelected(item)
    }
}
