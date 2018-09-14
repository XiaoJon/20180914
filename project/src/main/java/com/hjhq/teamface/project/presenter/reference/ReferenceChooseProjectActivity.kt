package com.hjhq.teamface.project.presenter.reference

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.constants.ProjectConstants
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber
import com.hjhq.teamface.basis.util.CollectionUtils
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.common.bean.ProjectListBean
import com.hjhq.teamface.common.utils.CommonUtil
import com.hjhq.teamface.project.adapter.ReferenceChooseProjectAdapter
import com.hjhq.teamface.project.model.ProjectModel
import com.hjhq.teamface.project.ui.SelectReferenceModuleDelegate

/**
 * 关联选择项目
 */
class ReferenceChooseProjectActivity : ActivityPresenter<SelectReferenceModuleDelegate, ProjectModel>() {
    private lateinit var mAdaper: ReferenceChooseProjectAdapter
    private var currentPosition = 0

    override fun init() {
        viewDelegate.setTitle("项目")
        viewDelegate.hideSearchBar()
        viewDelegate.hideProject()

        mAdaper = ReferenceChooseProjectAdapter(null)
        viewDelegate.setAdaper(mAdaper)
        loadProject()
    }


    /**
     * 加载项目
     */
    private fun loadProject() = model.queryAllList(mContext, 0,
            object : ProgressSubscriber<ProjectListBean>(mContext) {
                override fun onNext(projectListBean: ProjectListBean) {
                    super.onNext(projectListBean)
                    CollectionUtils.notifyDataSetChanged(mAdaper, mAdaper.data, projectListBean.data.dataList)
                }
            })


    override fun bindEvenListener() {
        super.bindEvenListener()
        viewDelegate.mRecyclerView.addOnItemTouchListener(object : SimpleItemClickListener() {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                currentPosition = position
                val item = mAdaper.getItem(position)
                val bundle = Bundle()
                bundle.putLong(ProjectConstants.PROJECT_ID, item.id)
                CommonUtil.startActivtiyForResult(mContext, ReferenceChooseGroupActivity::class.java, Constants.REQUEST_CODE1, bundle)

            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == Activity.RESULT_OK && data != null) {
            val name = data.getStringExtra(Constants.DATA_TAG1)
            val text = mAdaper.getItem(currentPosition).name + "/" + name

            val intent = Intent()
            intent.putExtra(Constants.DATA_TAG1, text)
            intent.putExtra(Constants.DATA_TAG2, mAdaper.getItem(currentPosition).id.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
