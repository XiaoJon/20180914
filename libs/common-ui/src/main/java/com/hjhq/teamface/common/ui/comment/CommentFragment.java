package com.hjhq.teamface.common.ui.comment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.Photo;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.bean.UploadFileBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.SharePreferenceManager;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.UriUtil;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.adapter.CommentAdapter;
import com.hjhq.teamface.common.bean.AddCommentRequestBean;
import com.hjhq.teamface.common.bean.CommentDetailResultBean;
import com.hjhq.teamface.common.ui.ImagePagerActivity;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.ui.voice.VoicePlayActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.RecordVoiceButton;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论
 *
 * @author mou
 * @date 2017/4/1
 */

public class CommentFragment extends FragmentPresenter<CommentWidgetDelegate, CommonModel> implements View.OnClickListener, View.OnTouchListener, CommentView.OnKeyBoardChangeListener, CommentView.OnSizeChangedListener {

    private List<Member> atList = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    //文件所属文件夹类型,只有文件评论用
    private String folderStyle = "";

    private String moduleBean;
    private String objectId;
    private boolean isInputByKeyBoard = true;
    private File imageFromCamera;
    private RxAppCompatActivity mContext;


    public static CommentFragment newInstance(String moduleBean, String objectId) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MODULE_BEAN, moduleBean);
        bundle.putString(Constants.DATA_ID, objectId);
        commentFragment.setArguments(bundle);
        return commentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            moduleBean = arguments.getString(Constants.MODULE_BEAN);
            objectId = arguments.getString(Constants.DATA_ID);
        }
    }

    @Override
    public void init() {
        mContext = (RxAppCompatActivity) getActivity();
        mCommentAdapter = new CommentAdapter(null);
        mCommentAdapter.setShowMore(false);
        viewDelegate.setAdapter(mCommentAdapter);
        loadData();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.jmui_switch_voice_ib);
        viewDelegate.setOnClickListener(this, R.id.jmui_send_msg_btn);
        viewDelegate.setOnClickListener(this, R.id.jmui_add_file_btn);
        viewDelegate.setOnClickListener(this, R.id.jmui_pick_from_camera_btn);
        viewDelegate.setOnClickListener(this, R.id.jmui_pick_from_local_btn);
        viewDelegate.setOnClickListener(this, R.id.jmui_pick_from_file_btn);
        viewDelegate.setOnClickListener(this, R.id.jmui_pick_from_member_btn);
        viewDelegate.setOnClickListener(this, R.id.jmui_expression_btn);
        viewDelegate.setOnClickListener(this, R.id.tv_show_more_comment);


        viewDelegate.getCommentView().setListeners(this);
        viewDelegate.getCommentView().setOnTouchListener(this);
        viewDelegate.getCommentView().setOnSizeChangedListener(this);
        viewDelegate.getCommentView().setOnKbdStateListener(this);
        //点击
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                CommentDetailResultBean.DataBean bean = (CommentDetailResultBean.DataBean) adapter.getItem(position);
                if (CollectionUtils.isEmpty(bean.getInformation())) {
                    return;
                }
                UploadFileBean uploadFileBean = bean.getInformation().get(0);
                if (bean.getItemType() == 2) {
                    //语音
                    bundle.putString(Constants.DATA_TAG1, uploadFileBean.getFileUrl());
                    bundle.putInt(Constants.DATA_TAG2, uploadFileBean.getVoiceTime());
                    CommonUtil.startActivtiy(mContext, VoicePlayActivity.class, bundle);
                } else if (bean.getItemType() == 3) {
                    //文件
                    SocketMessage socketMessage = new SocketMessage();
                    socketMessage.setMsgID(bean.getId());
                    socketMessage.setFileName(uploadFileBean.getFileName());
                    socketMessage.setFileUrl(uploadFileBean.getFileUrl());
                    socketMessage.setFileSize(TextUtil.parseInt(uploadFileBean.getFileSize()));
                    socketMessage.setFileType(uploadFileBean.getFileType());
                    socketMessage.setSenderName(bean.getEmployee_name());
                    socketMessage.setServerTimes(TextUtil.parseLong(bean.getDatetime_time()));
                    bundle.putSerializable(MsgConstant.MSG_DATA, socketMessage);
                    UIRouter.getInstance().openUri(mContext, "DDComp://filelib/file_detail", bundle);
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommentDetailResultBean.DataBean bean = (CommentDetailResultBean.DataBean) adapter.getItem(position);
                UploadFileBean uploadFileBean = bean.getInformation().get(0);
                ArrayList<Photo> list = Photo.toPhotoList(uploadFileBean.getFileUrl());
                Bundle bundle = new Bundle();
                bundle.putSerializable(ImagePagerActivity.PICTURE_LIST, list);
                bundle.putInt(ImagePagerActivity.SELECT_INDEX, 0);
                CommonUtil.startActivtiy(mContext, ImagePagerActivity.class, bundle);
            }
        });

    }


    /**
     * 得到评论列表
     */
    private void loadData() {
        model.getCommentDetail(mContext, objectId, moduleBean, new ProgressSubscriber<CommentDetailResultBean>(mContext) {
            @Override
            public void onNext(CommentDetailResultBean commentDetailResultBean) {
                super.onNext(commentDetailResultBean);
                List<CommentDetailResultBean.DataBean> data = commentDetailResultBean.getData();
                CollectionUtils.notifyDataSetChanged(mCommentAdapter, mCommentAdapter.getData(), data);
//                EventBusUtils.sendEvent(new MessageBean(CustomConstants.MESSAGE_COMMENT_COUNT_CODE,mCommentAdapter.getItemCount()+"",null));
                if (data.size() > 0) {
                    viewDelegate.getmRecyclerView().scrollToPosition(data.size() - 1);
                    viewDelegate.showMore(false);
                }
            }
        });
    }

    /**
     * 发表文字评论
     *
     * @param content
     */
    public void sendText(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showError(mContext, "内容不能为空");
            return;
        }
        if (content.length() > 140) {
            ToastUtils.showError(mContext, "内容不能超过140个字符");
            return;
        }
        viewDelegate.getCommentView().clearInput();
        AddCommentRequestBean bean = new AddCommentRequestBean();
        bean.setBean(moduleBean);
        bean.setContent(content);
        bean.setRelation_id(objectId);
        bean.setStyle(folderStyle);

        //@人员数据
        StringBuilder sb = new StringBuilder();
        for (Member member : atList) {
            sb.append(member.getSign_id() + ",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        bean.setAt_employee(sb.toString());
        if (atList != null) {
            atList.clear();
        }
        sentComment(bean);
    }

    /**
     * 发表评论
     *
     * @param bean
     */
    private void sentComment(AddCommentRequestBean bean) {
        model.addComment(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                viewDelegate.getCommentView().setKeyboardStateHide();
                loadData();
            }
        });
    }

    /**
     * 文件上传
     *
     * @param uri
     */
    private void fileUpload(Uri uri) {
        String photoPathFromContentUri = UriUtil.getPhotoPathFromContentUri(mContext, uri);
        File pickPic = new File(photoPathFromContentUri);
        commentFileUpload(null, pickPic);
    }

    /**
     * 相册上传
     *
     * @param uri
     */
    private void picktrueUpload(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //picturePath就是图片在储存卡所在的位置
            String picturePath = cursor.getString(columnIndex);
            if (Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
            File pickPic = new File(picturePath);
            commentFileUpload(null, pickPic);
        } else {
            ToastUtils.showError(mContext, "图片读取失败!");
        }
    }

    /**
     * 拍照上传
     */
    private void prepareUploadPic() {
        if (imageFromCamera == null || !imageFromCamera.exists()) {
            return;
        }
        LogUtil.i("照片路径" + imageFromCamera.getAbsolutePath());
        commentFileUpload(null, imageFromCamera);
    }

    /**
     * 附件上传 ,检查文件大小
     *
     * @param voiceDuration
     * @param file
     */
    private void commentFileUpload(Integer voiceDuration, File file) {
        if (FileUtils.checkLimit(mContext, file)) {
            DialogUtils.getInstance().sureOrCancel(mContext, "",
                    "当前为移动网络且文件大小超过10M,继续上传吗?",
                    viewDelegate.getRootView()
                    , () -> uploadFile(voiceDuration, file));
        } else {
            uploadFile(voiceDuration, file);
        }
    }

    /**
     * 附件上传
     *
     * @param voiceDuration
     * @param file
     */
    private void uploadFile(Integer voiceDuration, File file) {
        model.uploadFile(mContext, file.getAbsolutePath(), moduleBean,
                new ProgressSubscriber<UpLoadFileResponseBean>(mContext) {
                    @Override
                    public void onNext(UpLoadFileResponseBean upLoadFileResponseBean) {
                        super.onNext(upLoadFileResponseBean);
                        List<UploadFileBean> data = upLoadFileResponseBean.getData();
                        AddCommentRequestBean bean = new AddCommentRequestBean();
                        if (voiceDuration != null && !CollectionUtils.isEmpty(data)) {
                            for (UploadFileBean uploadFileBean : data) {
                                uploadFileBean.setVoiceTime(voiceDuration);
                            }
                        }
                        bean.setBean(moduleBean);
                        bean.setRelation_id(objectId);
                        bean.setInformation(data);
                        bean.setContent("");
                        sentComment(bean);
                    }
                });
    }


    private void close() {
        if (RecordVoiceButton.mIsPressed) {
            viewDelegate.getCommentView().dismissRecordDialog();
            viewDelegate.getCommentView().releaseRecorder();
            RecordVoiceButton.mIsPressed = false;
        }
        if (viewDelegate.getCommentView().getMoreMenu().getVisibility() == View.VISIBLE) {
            viewDelegate.getCommentView().dismissMoreMenu();
        }
        SoftKeyboardUtils.hide(viewDelegate.get(R.id.jmui_chat_input_et));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!RecordVoiceButton.mIsPressed) {
            viewDelegate.getCommentView().dismissRecordDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        RecordVoiceButton.mIsPressed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == Constants.TAKE_PHOTO_NEW_REQUEST_CODE) {
            //上传拍照图片
            prepareUploadPic();
        } else if (requestCode == Constants.PHOTO_ABLUM_NEW_REQUEST_CODE) {
            //上传相册图片
            Uri selectedImage = data.getData();
            picktrueUpload(selectedImage);
        } else if (requestCode == Constants.SELECT_FILE_NEW_REQUEST_CODE) {
            //文件
            Uri uri = data.getData();
            fileUpload(uri);
        } else if (requestCode == Constants.RESULT_CODE_SELECT_MEMBER) {
            //@功能
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            appendMention(members);
        }
    }

    /**
     * 添加@
     *
     * @param members
     */
    private void appendMention(List<Member> members) {
        int size = members.size();
        if (size > 0) {
            String[] mentions = new String[size];
            for (int i = 0; i < size; i++) {
                mentions[i] = members.get(i).getName();
            }
            atList.addAll(members);
            viewDelegate.getCommentView().setAtList(atList);
            viewDelegate.appendMention(mentions);
        }
    }

    /**
     * 选择文件
     */
    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, Constants.SELECT_FILE_NEW_REQUEST_CODE);
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        viewDelegate.getCommentView().dismissMoreMenu();
        if (JYFileHelper.existSDCard()) {
            SystemFuncUtils.requestPermissions(mContext, android.Manifest.permission.CAMERA, aBoolean -> {
                if (aBoolean) {
                    imageFromCamera = CommonUtil.getImageFromCamera(mContext, Constants.TAKE_PHOTO_NEW_REQUEST_CODE);
                } else {
                    ToastUtils.showError(mContext, "必须获得必要的权限才能拍照！");
                }
            });
        } else {
            ToastUtils.showError(mContext, "暂无外部存储");
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.jmui_switch_voice_ib) {
            viewDelegate.getCommentView().dismissMoreMenu();
            isInputByKeyBoard = !isInputByKeyBoard;
            //当前为语音输入，点击后切换为文字输入，弹出软键盘
            if (isInputByKeyBoard) {
                viewDelegate.getCommentView().isKeyBoard();
                viewDelegate.showSoftInputAndDismissMenu();
            } else {
                //否则切换到语音输入
                SoftKeyboardUtils.hide(mContext);
                viewDelegate.getCommentView().notKeyBoard();
                viewDelegate.dismissSoftInputAndShowMenu();
                viewDelegate.getCommentView().dismissMoreMenu();
            }

            //发送文本消息
        } else if (i == R.id.jmui_send_msg_btn) {
            String msgContent = viewDelegate.getCommentView().getChatInput().trim();
            sendText(msgContent);

            //点击添加按钮，弹出更多选项菜单
            //如果在语音输入时点击了添加按钮，则显示菜单并切换到输入框
        } else if (i == R.id.jmui_add_file_btn) {
            if (!isInputByKeyBoard) {
                viewDelegate.getCommentView().isKeyBoard();
                isInputByKeyBoard = true;
                viewDelegate.getCommentView().showMoreMenu();
            } else {
                //如果弹出软键盘 则隐藏软键盘
                if (viewDelegate.getCommentView().getMoreMenu().getVisibility() != View.VISIBLE) {
                    viewDelegate.dismissSoftInputAndShowMenu();
                    viewDelegate.getCommentView().focusToInput(false);
                    //如果弹出了更多选项菜单，则隐藏菜单并显示软键盘
                } else {
                    viewDelegate.showSoftInputAndDismissMenu();
                }
            }

            //拍照
        } else if (i == R.id.jmui_pick_from_camera_btn) {
            takePhoto();

            //从相册选择
        } else if (i == R.id.jmui_pick_from_local_btn) {
            if (viewDelegate.getCommentView().getMoreMenu().getVisibility() == View.VISIBLE) {
                viewDelegate.getCommentView().dismissMoreMenu();
            }
            CommonUtil.getImageFromAlbum(mContext, Constants.PHOTO_ABLUM_NEW_REQUEST_CODE);

            //文件
        } else if (i == R.id.jmui_pick_from_file_btn) {
            selectFile();
        } else if (i == R.id.jmui_expression_btn) {
            viewDelegate.getCommentView().showEmoji();

        } else if (i == R.id.jmui_pick_from_member_btn) {
            //@成员
            CommonUtil.startActivtiyForResult(mContext, SelectMemberActivity.class, Constants.RESULT_CODE_SELECT_MEMBER);
        } else if (i == R.id.tv_show_more_comment) {
            //显示更多评论
            mCommentAdapter.setShowMore(true);
            mCommentAdapter.notifyDataSetChanged();
            viewDelegate.showMore(true);
        }
    }


    @Override
    public void onKeyBoardStateChange(int state) {
        viewDelegate.onKeyBoardStateChange(state);
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (view.getId() == R.id.jmui_chat_input_et) {
                    if (viewDelegate.getCommentView().getMoreMenu().getVisibility() == View.VISIBLE) {
                        viewDelegate.showSoftInputAndDismissMenu();
                        viewDelegate.getCommentView().dismissMoreMenu();
                    }
                    return false;
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (oldh - h > 300) {
            if (SharePreferenceManager.getCachedWritableFlag()) {
                SharePreferenceManager.setCachedKeyboardHeight(oldh - h);
                SharePreferenceManager.setCachedWritableFlag(false);
            }

            viewDelegate.getCommentView().setMoreMenuHeight();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageBean event) {
        if (Constants.DATA_TAG1.equals(event.getTag())) {
            File file = (File) event.getObject();
            int code = event.getCode();
            commentFileUpload(code, file);
        }
    }
}
