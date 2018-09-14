package com.hjhq.teamface.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.im.R;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberGridAdapter extends BaseAdapter {

    private static final String TAG = "GroupMemberGridAdapter";

    private LayoutInflater mInflater;
    //群成员列表
    private List<Member> mMemberList = new ArrayList<Member>();
    private List<Member> allMemberList = new ArrayList<Member>();
    private boolean mIsCreator = false;
    //群成员个数
    private int mCurrentNum;
    //记录空白项的数组
    private int[] mRestArray = new int[]{2, 1, 0, 3};
    //用群成员项数余4得到，作为下标查找mRestArray，得到空白项
    private int mRestNum;
    public static final int MAX_GRID_ITEM = 40;
    private boolean mIsGroup;
    private String mTargetId;
    private Context mContext;
    private int mAvatarSize;
    private String mTargetAppKey;

    //群聊
    public GroupMemberGridAdapter(Context context, List<Member> memberList, boolean isCreator,
                                  int size) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mIsGroup = true;
        this.allMemberList = memberList;
        mCurrentNum = 9;
        // mCurrentNum = mMemberList.size();
        this.mIsCreator = isCreator;
        this.mAvatarSize = size;
        if (isCreator) {
            if (allMemberList.size() > 8) {
                mMemberList = allMemberList.subList(0, 8);
            } else {
                mMemberList = allMemberList;
            }

        } else {
            if (allMemberList.size() > 10) {
                mMemberList = allMemberList.subList(0, 10);
            } else {
                mMemberList = allMemberList;
            }
        }
        initBlankItem();
    }

    //单聊
    public GroupMemberGridAdapter(Context context, String targetId, String appKey) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mTargetId = targetId;
        this.mTargetAppKey = appKey;
    }

    public void initBlankItem() {
        if (mMemberList.size() > MAX_GRID_ITEM) {
            mCurrentNum = MAX_GRID_ITEM - 1;
        } else {
            mCurrentNum = mMemberList.size();
        }
        mRestNum = mRestArray[mCurrentNum % 5];
    }

    public void refreshMemberList() {
        if (mMemberList.size() > MAX_GRID_ITEM) {
            mCurrentNum = MAX_GRID_ITEM - 1;
        } else {
            mCurrentNum = mMemberList.size();
        }
        mRestNum = mRestArray[mCurrentNum % 5];
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //如果是普通成员，并且群组成员余4等于3，特殊处理，隐藏下面一栏空白
        if (mCurrentNum % 4 == 3 && !mIsCreator) {
            return mCurrentNum + 1;
        } else {
            return mCurrentNum + mRestNum + 2;
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ItemViewTag viewTag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_group, null);
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.grid_avatar),
                    (TextView) convertView.findViewById(R.id.grid_name),
                    (ImageView) convertView.findViewById(R.id.grid_delete_icon));
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }
        //群聊
        if (mIsGroup) {
            //群成员
            if (position < mMemberList.size()) {
                Member userInfo = mMemberList.get(position);
                viewTag.icon.setVisibility(View.VISIBLE);
                viewTag.name.setVisibility(View.VISIBLE);
                ImageLoader.loadCircleImage(viewTag.icon.getContext(), userInfo.getPicture(),
                        viewTag.icon, userInfo.getName());


                viewTag.name.setText(userInfo.getName() + "");
            }
            viewTag.deleteIcon.setVisibility(View.INVISIBLE);
            if (position < mCurrentNum) {
                viewTag.icon.setVisibility(View.VISIBLE);
                viewTag.name.setVisibility(View.VISIBLE);
            } else if (position == mCurrentNum) {
                viewTag.icon.setImageResource(R.drawable.chat_detail_add);
                viewTag.icon.setVisibility(View.VISIBLE);
                viewTag.name.setVisibility(View.INVISIBLE);

                //设置删除群成员按钮
            } else if (position == mCurrentNum + 1) {
                //if (mIsCreator && mCurrentNum > 1) {
                if (2 > 1) {
                    viewTag.icon.setImageResource(R.drawable.chat_detail_del);
                    viewTag.icon.setVisibility(View.VISIBLE);
                    viewTag.name.setVisibility(View.INVISIBLE);
                } else {
                    viewTag.icon.setVisibility(View.GONE);
                    viewTag.name.setVisibility(View.GONE);
                }
                //空白项
            } else {
                viewTag.icon.setVisibility(View.INVISIBLE);
                viewTag.name.setVisibility(View.INVISIBLE);
            }
        } else {
            if (position == 0) {
                /*Conversation conv = JMessageClient.getSingleConversation(mTargetId, mTargetAppKey);
                UserInfo userInfo = (UserInfo) conv.getTargetInfo();
                if (!TextUtils.isNotEmpty(userInfo.getAvatar())) {
                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int status, String desc, Bitmap bitmap) {
                            if (status == 0) {
                                Log.d(TAG, "Get small avatar success");
                                viewTag.icon.setImageBitmap(bitmap);
                            } else {
                                HandleResponseCode.onHandle(mContext, status, false);
                            }
                        }
                    });
                }
                String displayName = userInfo.getNotename();
                if (TextUtils.isNotEmpty(displayName)) {
                    displayName = userInfo.getNickname();
                } else if (TextUtils.isNotEmpty(displayName)) {
                    displayName = userInfo.getUserName();
                }
                viewTag.name.setText(displayName);*/
                viewTag.icon.setVisibility(View.VISIBLE);
                viewTag.name.setVisibility(View.VISIBLE);
            } else {
                viewTag.icon.setImageResource(R.drawable.chat_detail_add);
                viewTag.icon.setVisibility(View.VISIBLE);
                viewTag.name.setVisibility(View.INVISIBLE);
            }

        }
        return convertView;
    }

    public void setCreator(boolean isCreator) {
        mIsCreator = isCreator;
        notifyDataSetChanged();
    }

    public void setNewData(List<Member> userInfoList) {
        this.mMemberList = userInfoList;
        notifyDataSetChanged();

    }

    private static class ItemViewTag {

        private ImageView icon;
        private ImageView deleteIcon;
        private TextView name;

        public ItemViewTag(ImageView icon, TextView name, ImageView deleteIcon) {
            this.icon = icon;
            this.deleteIcon = deleteIcon;
            this.name = name;
        }
    }
}
