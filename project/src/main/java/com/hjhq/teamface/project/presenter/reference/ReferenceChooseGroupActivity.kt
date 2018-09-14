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
import com.hjhq.teamface.common.utils.CommonUtil
import com.hjhq.teamface.project.adapter.ReferenceChooseNodeAdapter
import com.hjhq.teamface.project.bean.AllNodeResultBean
import com.hjhq.teamface.project.model.ProjectModel
import com.hjhq.teamface.project.ui.SelectReferenceModuleDelegate

/**
 * 关联选择分组
 */
class ReferenceChooseGroupActivity : ActivityPresenter<SelectReferenceModuleDelegate, ProjectModel>() {
    private lateinit var mAdaper: ReferenceChooseNodeAdapter
    private var projectId = 0L
    private var currentPosition = 0

    override fun create(savedInstanceState: Bundle?) {
        super.create(savedInstanceState)
        if (savedInstanceState == null) {
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0)
        }
    }

    override fun init() {
        viewDelegate.setTitle("选择分组")
        viewDelegate.hideSearchBar()
        viewDelegate.hideProject()

        mAdaper = ReferenceChooseNodeAdapter(null)
        viewDelegate.setAdaper(mAdaper)
        getNode()
    }


    /**
     * 加载分组
     */
    private fun getNode() =
            model.getAllNode(mContext, projectId, object : ProgressSubscriber<AllNodeResultBean>(mContext) {
                override fun onNext(allNodeResultBean: AllNodeResultBean) {
                    super.onNext(allNodeResultBean)
                    CollectionUtils.notifyDataSetChanged(mAdaper, mAdaper.data, allNodeResultBean.data.dataList)
                }
            })


    override fun bindEvenListener() {
        super.bindEvenListener()
        viewDelegate.mRecyclerView.addOnItemTouchListener(object : SimpleItemClickListener() {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                currentPosition = position
                val item = mAdaper.getItem(position)
                if (CollectionUtils.isEmpty(item.subnodeArr)) {
                    //没有列
                    val intent = Intent()
                    intent.putExtra(Constants.DATA_TAG1, item.name)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.DATA_TAG1, item.subnodeArr as ArrayList)
                    CommonUtil.startActivtiyForResult(mContext, ReferenceChooseListActivity::class.java, Constants.REQUEST_CODE1, bundle)
                }
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == Activity.RESULT_OK && data!=null){
            val name = data.getStringExtra(Constants.DATA_TAG1)
            val text = mAdaper.getItem(currentPosition).name + "/" + name

            val intent = Intent()
            intent.putExtra(Constants.DATA_TAG1, text)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
