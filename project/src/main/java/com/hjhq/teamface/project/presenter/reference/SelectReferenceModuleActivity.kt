package com.hjhq.teamface.project.presenter.reference

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hjhq.teamface.basis.bean.DataTempResultBean
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber
import com.hjhq.teamface.basis.util.CollectionUtils
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.common.utils.CommonUtil
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.adapter.SelectReferenceModuleAdapter
import com.hjhq.teamface.project.bean.CustomModuleResultBean
import com.hjhq.teamface.project.model.TaskModel
import com.hjhq.teamface.project.ui.SelectReferenceModuleDelegate
import com.hjhq.teamface.project.widget.utils.ProjectCustomUtil
import com.luojilab.component.componentlib.router.ui.UIRouter
import java.util.*

/**
 * 选择关联模块
 */
class SelectReferenceModuleActivity : ActivityPresenter<SelectReferenceModuleDelegate, TaskModel>() {
    private lateinit var mAdaper: SelectReferenceModuleAdapter

    override fun init() {
        viewDelegate.setTitle("选择模块")
        mAdaper = SelectReferenceModuleAdapter(null)
        viewDelegate.setAdaper(mAdaper)
        getAllModule()
    }

    private fun getAllModule() =
            model.queryAllModule(mContext, object : ProgressSubscriber<CustomModuleResultBean>(mContext) {
                override fun onNext(t: CustomModuleResultBean) {
                    super.onNext(t)
                    CollectionUtils.notifyDataSetChanged(mAdaper, mAdaper.data, t.data)
                }
            })

    override fun bindEvenListener() {
        super.bindEvenListener()
        viewDelegate.mRecyclerView.addOnItemTouchListener(object : SimpleItemClickListener() {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = mAdaper.getItem(position)
                val bundle = Bundle()
                bundle.putString(Constants.MODULE_BEAN, item.bean)
                bundle.putString(Constants.MODULE_ID, item.moduleid)
                bundle.putString(Constants.NAME, item.name)
                UIRouter.getInstance().openUri(mContext, "DDComp://custom/select", bundle, Constants.REQUEST_CODE2)


            }
        })
        //项目
        viewDelegate.setOnClickListener(View.OnClickListener {
            CommonUtil.startActivtiyForResult(mContext, ReferenceChooseProjectActivity::class.java, Constants.REQUEST_CODE1)
        }, R.id.rl_project)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == Activity.RESULT_OK && data != null) {
            data.putExtra(Constants.DATA_TAG3, "1")
            setResult(Activity.RESULT_OK, data)
            finish()
        } else if (requestCode == Constants.REQUEST_CODE2 && resultCode == Activity.RESULT_OK && data != null) {
            val dataList = data.getSerializableExtra(Constants.DATA_TAG1) as ArrayList<DataTempResultBean.DataBean.DataListBean>
            val moduleBean = data.getStringExtra(Constants.MODULE_BEAN)
            if (CollectionUtils.isEmpty(dataList)) {
                return
            }
            val tempBean = dataList[0]
            val tv = TextView(mContext)
            if (tempBean.row != null && !CollectionUtils.isEmpty(tempBean.row.row1)) {
                ProjectCustomUtil.setReferenceTempValue(tv, tempBean.row.row1[0])
            }

            val intent = Intent()
            intent.putExtra(Constants.DATA_TAG1, tv.text.toString())
            intent.putExtra(Constants.DATA_TAG2, tempBean.id.value)
            intent.putExtra(Constants.DATA_TAG3, "2")
            intent.putExtra(Constants.MODULE_BEAN, moduleBean)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
