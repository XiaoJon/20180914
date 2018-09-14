package com.hjhq.teamface.filelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.AttachmentBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.FileConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.filelib.FilelibModel;
import com.hjhq.teamface.filelib.R;
import com.hjhq.teamface.filelib.adapter.RootFolderAdapter;
import com.hjhq.teamface.filelib.bean.RootFolderResBean;
import com.hjhq.teamface.filelib.view.SelectRootFileDelegate;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

@RouteNode(path = "/select_file", desc = "从文件库选择文件")
public class SelectRootFileActivity extends ActivityPresenter<SelectRootFileDelegate, FilelibModel> {


    private int folderLevel = 0;
    private int tempFolderLevel = 0;
    private RecyclerView mRvNavi;
    private RecyclerView mRvFileList;
    private RootFolderAdapter mAdapter;
    private ArrayList<RootFolderResBean.DataBean> dataList = new ArrayList<>();

    @Override
    public void init() {
        initView();
        initData();
    }


    protected void initView() {
        viewDelegate.setTitle("选择文件");
        viewDelegate.setRightMenuTexts(R.color.app_blue, "确定");
        mRvNavi = (RecyclerView) findViewById(R.id.rv_navi);
        mRvFileList = (RecyclerView) findViewById(R.id.rv_department);
        mAdapter = new RootFolderAdapter(dataList);
        mRvFileList.setLayoutManager(new LinearLayoutManager(SelectRootFileActivity.this));
        mRvFileList.setAdapter(mAdapter);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        mRvFileList.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(FileConstants.FOLDER_STYLE, position + 1);
                bundle.putInt(FileConstants.FOLDER_LEVEL, 0);
                bundle.putString(FileConstants.FOLDER_NAME, getRootFolderName(position));
                UIRouter.getInstance().openUri(mContext, "DDComp://filelib/select_filelibrary_file", bundle, Constants.REQUEST_CODE1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setResult(Activity.RESULT_OK);
        finish();
        return super.onOptionsItemSelected(item);
    }

    protected void initData() {
        getRootFolder();
    }


    /**
     * 根目录文件夹列表
     */
    private void getRootFolder() {
        model.queryfileCatalog(SelectRootFileActivity.this,
                new ProgressSubscriber<RootFolderResBean>(SelectRootFileActivity.this, false) {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RootFolderResBean rootFolderResBean) {
                        dataList.clear();
                        for (int i = 0; i < 3; i++) {
                            dataList.add(rootFolderResBean.getData().get(i));
                        }
                        mAdapter.notifyDataSetChanged();


                    }
                });
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe
    public void jumpFolder(MessageBean bean) {
        if (bean.getTag().equals(Constants.MOVE_FILE_JUMP_FOLDER) && bean.getCode() < folderLevel) {
            finish();
        }
        if (bean.getTag().equals(FileConstants.MOVE_OR_COPY_OK)) {
            setResult(Activity.RESULT_OK);
            finish();
        }

    }

    /**
     * 获取根目录文件夹名字
     *
     * @param folderStyle
     * @return
     */
    private String getRootFolderName(int folderStyle) {
        switch (folderStyle) {
            case 1:
                return "公司文件";
            case 2:
                return "应用文件";
            case 3:
                return "个人文件";
            case 4:
                return "我的共享";
            case 5:
                return "与我共享";
            default:
                break;

        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_CODE1) {
            if (data != null) {
                AttachmentBean bean = (AttachmentBean) data.getSerializableExtra(Constants.DATA_TAG1);
                Intent i = new Intent();
                i.putExtra(Constants.DATA_TAG1, bean);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (0 == folderLevel) {
                finish();
            } else {
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
