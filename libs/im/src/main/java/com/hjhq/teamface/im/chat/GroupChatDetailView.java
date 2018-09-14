package com.hjhq.teamface.im.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.common.view.HelperItemView;
import com.hjhq.teamface.im.R;

public class GroupChatDetailView extends LinearLayout {

    private RelativeLayout mAllGroupMemberLL;
    private RelativeLayout mGroupNameRL;
    private RelativeLayout mGroupChatDelRL;
    private RelativeLayout mGroupPrincipal;
    private TextView mGroupPrincipalName;
    private TextView mMembersNum;
    private Button mDelGroupBtn;
    private TextView mGroupName;
    private RecyclerView mGridView;
    private Context mContext;
    private RelativeLayout mGroupDescribeRL;
    private HelperItemView mPutOnTop;
    private HelperItemView mNoDisturb;
    private RelativeLayout mLayoutChatMember;
    private RelativeLayout mGroupChatClose;
    private TextView mCloseGroupChat;
    private ImageView mArrowGroupNameIv;
    private ImageView mArrowGroupDesIv;
    private TextView mMembersDes;
    private ImageView mAvatarInContactsIv;
    private TextView mTitleTv;
    private TextView mNumberTv;
    private ImageView mArrowGroupMessageIv;
    private boolean isMemberClick = true;


    CompoundButton.OnClickListener listenerTop;
    CompoundButton.OnClickListener listenerDisturb;

    public GroupChatDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        // TODO Auto-generated constructor stub
    }

    public void initModule() {
        mAllGroupMemberLL = (RelativeLayout) findViewById(R.id.all_member_rl);//全部成员
        mLayoutChatMember = (RelativeLayout) findViewById(R.id.layout_chat_member);//个人信息
        mGroupNameRL = (RelativeLayout) findViewById(R.id.group_name_rl);//名称
        mGroupDescribeRL = (RelativeLayout) findViewById(R.id.group_describe_rl);//描述
        mPutOnTop = (HelperItemView) findViewById(R.id.top_rl);//置顶
        /*if (listenerTop != null) {
            mPutOnTop.setOnChangedListener(listenerTop);
            mPutOnTop.setSwitchButtonTag(R.string.app_name, R.id.put_on_top);
        }
*/
        mNoDisturb = (HelperItemView) findViewById(R.id.no_disturb_rl);//消息免打扰
       /* if (listenerDisturb != null) {
            mNoDisturb.setOnChangedListener(listenerDisturb);
            mNoDisturb.setSwitchButtonTag(R.string.app_name, R.id.no_disturb_rl);
        }*/
        mGroupChatDelRL = (RelativeLayout) findViewById(R.id.group_chat_del_ll);//清空聊天记录
        //群管理员
        mGroupPrincipal = (RelativeLayout) findViewById(R.id.group_principle_rl);
        //群管理员名字
        mGroupPrincipalName = (TextView) findViewById(R.id.chat_detail_group_principal_tv);
        mMembersNum = (TextView) findViewById(R.id.members_num);
        mMembersDes = (TextView) findViewById(R.id.chat_detail_group_describe_tv);//描述
        mGroupChatClose = (RelativeLayout) findViewById(R.id.group_chat_close);//关闭聊天
        mDelGroupBtn = (Button) findViewById(R.id.chat_detail_del_group);//退出群组
        mGroupName = (TextView) findViewById(R.id.chat_detail_group_name);
        mCloseGroupChat = (TextView) findViewById(R.id.close_group_chat_tv);
        mGridView = (RecyclerView) findViewById(R.id.chat_detail_group_gv);//群成员
        mArrowGroupNameIv = (ImageView) findViewById(R.id.arrow_iv_group_name);//群组名称
        mArrowGroupDesIv = (ImageView) findViewById(R.id.arrow_iv_group_describe);//群聊头部箭头
        mArrowGroupMessageIv = (ImageView) findViewById(R.id.arrow_iv_group_message_clean);//个人头部箭头

        mAvatarInContactsIv = (ImageView) findViewById(R.id.avatar_in_contacts_iv);
        mTitleTv = (TextView) findViewById(R.id.title);
        mNumberTv = (TextView) findViewById(R.id.number);


    }




    public void setGroupName(String str) {
        mGroupName.setText(str + " ");
    }

    public void setGroupNotice(String str) {
        mMembersDes.setText(str + " ");
    }

    public void setGroupPrincipal(String str) {
        mGroupPrincipalName.setText(str + " ");
    }

    public void setGroupDescription(String str) {
        mMembersDes.setText(str);
    }

    /**
     * 设置成单聊
     */
    public void setSingleView() {
        mGroupNameRL.setVisibility(View.GONE);
        mGroupDescribeRL.setVisibility(View.GONE);
        mDelGroupBtn.setVisibility(View.GONE);
        mAllGroupMemberLL.setVisibility(View.GONE);
        mGridView.setVisibility(View.GONE);
        mLayoutChatMember.setVisibility(View.VISIBLE);
        mCloseGroupChat.setText("关闭聊天");
    }


    /**
     * 设置成群聊
     */
    public void setGroupView() {
        mGroupNameRL.setVisibility(View.VISIBLE);
        mGroupDescribeRL.setVisibility(View.VISIBLE);
        mDelGroupBtn.setVisibility(View.VISIBLE);
        mAllGroupMemberLL.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.VISIBLE);
        mLayoutChatMember.setVisibility(View.GONE);
        mCloseGroupChat.setText("关闭群聊");
    }

    /**
     * 设置成员信息
     *
     * @param bean
     */
    public void setEmployeeInfo(Member bean) {
        if (bean == null) {
            ToastUtils.showError(mContext,"成员信息获取失败！");
            return;
        }

        ImageLoader.loadRoundImage(mContext, bean.getPicture(), mAvatarInContactsIv, R.drawable.actionbar_mine_normal);
        TextUtil.setText(mTitleTv, bean.getName());
        TextUtil.setText(mNumberTv, bean.getPost_name());
    }

    /**
     * 群主设置
     *
     * @param bl true:是群主
     */
    public void setGroupOwner(boolean bl) {
        mArrowGroupNameIv.setVisibility(bl ? View.VISIBLE : View.GONE);
        mArrowGroupDesIv.setVisibility(bl ? View.VISIBLE : View.GONE);
        if (!bl) {
            mGroupDescribeRL.setOnClickListener(null);
            mGroupNameRL.setOnClickListener(null);
        }
    }

    /**
     * 设置助手
     */
    public void setCompanyGroup() {
        mDelGroupBtn.setVisibility(View.GONE);
    }

    public void updateGroupName(String newName) {
        mGroupName.setText(newName);
    }



    public void setMembersNum(int size) {
        String text = mContext.getString(R.string.all_group_members)
                + mContext.getString(R.string.combine_title);
        mMembersNum.setText(String.format(text, size));
    }

    /**
     * 0：未设置，1：免打扰
     *
     * @param status
     */
    public void initNoDisturb(int status) {
        mNoDisturb.setSelected(status == 1);
    }

    /**
     * 0：未置顶，1：置顶
     *
     * @param status
     */
    public void initOnTop(int status) {
        mPutOnTop.setSelected(status == 1);
    }

    public void setNoDisturbChecked(boolean flag) {
        mNoDisturb.setSelected(flag);
    }

}
