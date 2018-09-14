package com.hjhq.teamface.oa.friends;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.Photo;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.member.MembersView;
import com.hjhq.teamface.common.ui.location.LocationPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.oa.friends.adapter.AddCircleGridAdapter;
import com.hjhq.teamface.oa.friends.logic.FriendsLogic;
import com.hjhq.teamface.oa.friends.utils.Utils;
import com.hjhq.teamface.im.bean.AddFriendsRequestBean;
import com.hjhq.teamface.util.CommonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import me.iwf.photopicker.PhotoPicker;
import rx.Observable;


/**
 * 发表动态到企业圈
 *
 * @author lx
 * @date 2017/6/30
 */
public class AddFriendsActivity extends BaseTitleActivity {

    /**
     * 最大可选图片的数量
     */
    public static int MAX_PHOTO = 9;

    /**
     * 文字信息
     */
    @Bind(R.id.content_et_edite)
    EditText editText;
    /**
     * 图片
     */
    @Bind(R.id.gv_imageitem)
    GridView photosGridView;

    @Bind(R.id.location_tv)
    TextView locationTv;
    @Bind(R.id.location_ll)
    LinearLayout locationLL;

    @Bind(R.id.team_member_view)
    MembersView teamMemberView;

    /**
     * 图片适配器
     */
    private AddCircleGridAdapter adapter;

    /**
     * 初始字符串
     **/
    private String initString;
    private String picPath;
    private File imageFromCamera;
    private PoiItem poiInfo;
    private Uri photoUri;

    @Override
    protected int getChildView() {
        return R.layout.add_friend_circle;
    }

    @Override
    protected void initView() {
        super.initView();

        setActivityTitle("新建动态");
        setRightMenuColorTexts("发送");

    }

    @Override
    protected void setListener() {
        setOnClicks(locationLL);
        photosGridView.setOnItemClickListener((arg0, arg1, arg2, arg3) -> photoClick(arg2));
        teamMemberView.setOnMemberSumClickedListener(() -> addMember());
        teamMemberView.setOnAddMemberClickedListener(() -> addMember());
    }

    private void addMember() {
        List<Member> members = teamMemberView.getMembers();
        for (Member member : members) {
            member.setCheck(true);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, (Serializable) members);
        bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
        CommonUtil.startActivtiyForResult(AddFriendsActivity.this, SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);
    }


    @Override
    protected void initData() {
        adapter = new AddCircleGridAdapter(this);
        adapter.setMaxDisplayCount(MAX_PHOTO);
        photosGridView.setAdapter(adapter);
        initString = toContent();
    }

    /**
     * 图片
     *
     * @param position
     */
    private void photoClick(int position) {
        int count = adapter.getCount() - 1;
        int sum = MAX_PHOTO - count;
        if (position == count) {
            if (sum <= 0) {
                ToastUtils.showError(mContext, "最多上传" + MAX_PHOTO + "个文件");
                return;
            }
            PopUtils.showBottomMenu(this, getContainer(), "上传附件", new String[]{"拍照", "相册"}, position1 -> {
                if (position1 == 0) {
                    SystemFuncUtils.requestPermissions(mContext, android.Manifest.permission.CAMERA, aBoolean -> {
                        if (aBoolean) {
                            photoUri = CommonUtil.getImageFromCamera2(AddFriendsActivity.this, Constants.TAKE_PHOTO_NEW_REQUEST_CODE);
                        } else {
                            ToastUtils.showError(mContext, "必须获得必要的权限才能拍照！");
                        }
                    });
                } else if (position1 == 1) {
                    CommonUtil.getImageFromAlbumByMultiple(AddFriendsActivity.this, sum);
                }
                return true;
            });
        } else {
            Utils.gotoImagePagerActivity(this, adapter.getImageList(), position, true);
        }

    }

    @Override
    public void onClick(View view) {
        CommonUtil.startActivtiyForResult(this, LocationPresenter.class, Constants.REQUEST_CODE2);
    }

    @Override
    protected void onRightMenuClick(int itemId) {
        super.onRightMenuClick(itemId);
        addFriends();
    }

    /**
     * 发送
     */
    private void addFriends() {
        String content = editText.getText().toString().trim();
        ArrayList<Photo> imageList = adapter.getImageList();
        if (TextUtils.isEmpty(content) && CollectionUtils.isEmpty(imageList)) {
            showToast("请输入内容");
            return;
        }
        AddFriendsRequestBean bean = new AddFriendsRequestBean();

        bean.setInfo(content);

        if (poiInfo != null) {
            bean.setAddress(poiInfo.getProvinceName() + poiInfo.getCityName() + poiInfo.getAdName() + poiInfo.getTitle());
            bean.setLatitude(poiInfo.getLatLonPoint().getLatitude());
            bean.setLongitude(poiInfo.getLatLonPoint().getLongitude());
        }

        //协助人
        List<String> teamList = new ArrayList<>();
        List<Member> members = teamMemberView.getMembers();
        if (members != null) {
            Observable.from(members).subscribe(member -> teamList.add(member.getSign_id()));
        }


        bean.setImages(new ArrayList<>());
        bean.setPeoples(teamList);
        FriendsLogic.getInstance().addFriends(this, imageList, bean, new ProgressSubscriber<BaseBean>(this) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                showToast("发送成功！");
                EventBusUtils.sendEvent(new MessageBean(Constants.FRIENDS_REFRESH, "", null));
                finish();
                CommonUtil.startActivtiy(mContext, FriendsActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (initString.equals(toContent())) {
            Utils.hideInputMethod(this);
            finish();
        } else {
            DialogUtils.getInstance().sureOrCancel(this, "提示", "是否放弃编辑", getContainer(), () -> {
                Utils.hideInputMethod(AddFriendsActivity.this);
                finish();
            });
        }
    }

    public String toContent() {
        return "AddFriendCircleFragment [editText="
                + Utils.getEditString(editText) + ", imageList="
                + adapter.getImageList().size() + "]";
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean event) {
        if (event.getCode() == Constants.PHOTO_REFRESH) {
            int position = Integer.parseInt(event.getTag());
            adapter.remove(position);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            //获取照片
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                ArrayList<Photo> photoList = new ArrayList<>();
                for (int i = 0; i < photos.size(); i++) {
                    String url = photos.get(i);
                    Photo photo = new Photo();
                    photo.setUrl(url);
                    photo.setName(JYFileHelper.getPicNameFromPath(url));
                    photoList.add(photo);
                }
                adapter.addAll(photoList);

            }

        } else if (requestCode == Constants.TAKE_PHOTO_NEW_REQUEST_CODE) {
            //拍照新建图片
            String[] pojo = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                cursor.moveToFirst();
                picPath = cursor.getString(columnIndex);
                imageFromCamera = new File(picPath);
                if (imageFromCamera.exists()) {
                    Photo photo = new Photo();
                    photo.setUrl(imageFromCamera.getAbsolutePath());
                    photo.setName(imageFromCamera.getName());

                    adapter.add(photo);
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK) {
            //协助人
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            teamMemberView.setMembers(members);
        } else if (requestCode == Constants.REQUEST_CODE2 && resultCode == RESULT_OK) {
            poiInfo = data.getParcelableExtra(LocationPresenter.LOCATION_RESULT_CODE);
            locationTv.setText(poiInfo.getProvinceName() + poiInfo.getCityName() + poiInfo.getAdName() + poiInfo.getTitle());
        }
    }

}
