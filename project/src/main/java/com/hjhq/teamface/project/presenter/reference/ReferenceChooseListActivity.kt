package com.hjhq.teamface.project.presenter.reference

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.util.CollectionUtils
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.common.utils.CommonUtil
import com.hjhq.teamface.project.adapter.ReferenceChooseNodeAdapter
import com.hjhq.teamface.project.bean.NodeBean
import com.hjhq.teamface.project.model.ProjectModel
import com.hjhq.teamface.project.ui.SelectReferenceModuleDelegate

/**
 * 关联选择列
 */
class ReferenceChooseListActivity : ActivityPresenter<SelectReferenceModuleDelegate, ProjectModel>() {
    private lateinit var mAdaper: ReferenceChooseNodeAdapter
    private var currentPosition = 0

    private var subNodeList: List<NodeBean>? = null

    private var isSubList = false

    override fun create(savedInstanceState: Bundle?) {
        super.create(savedInstanceState)
        if (savedInstanceState == null) {
            val serializableExtra = intent.getSerializableExtra(Constants.DATA_TAG1)
            isSubList = intent.getBooleanExtra(Constants.DATA_TAG2, false)
            subNodeList = serializableExtra as List<NodeBean>
        }
    }

    override fun init() {
        viewDelegate.hideSearchBar()
        viewDelegate.hideProject()
        if (isSubList) {
            viewDelegate.setTitle("选择子列")
        } else {
            viewDelegate.setTitle("选择列")
        }
        mAdaper = ReferenceChooseNodeAdapter(subNodeList)
        viewDelegate.setAdaper(mAdaper)
    }


    override fun bindEvenListener() {
        super.bindEvenListener()
        viewDelegate.mRecyclerView.addOnItemTouchListener(object : SimpleItemClickListener() {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                currentPosition = position
                val item = mAdaper.getItem(position)
                if (CollectionUtils.isEmpty(item.subnodeArr)) {
                    //没有子列
                    val intent = Intent()
                    intent.putExtra(Constants.DATA_TAG1, item.name)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.DATA_TAG1, item.subnodeArr as ArrayList)
                    bundle.putBoolean(Constants.DATA_TAG2, true)
                    CommonUtil.startActivtiyForResult(mContext, ReferenceChooseListActivity::class.java, Constants.REQUEST_CODE1, bundle)
                }
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
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
