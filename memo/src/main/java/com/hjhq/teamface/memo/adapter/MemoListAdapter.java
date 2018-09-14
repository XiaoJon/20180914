package com.hjhq.teamface.memo.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.bean.MemoListItemBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.basis.view.SwipeMenuLayout;
import com.hjhq.teamface.memo.R;

import java.util.ArrayList;
import java.util.List;


public class MemoListAdapter extends BaseQuickAdapter<MemoListItemBean, BaseViewHolder> {
    private int type;
    private boolean editState = false;
    private boolean isMenuOpen = false;

    public MemoListAdapter(int type, List<MemoListItemBean> data) {
        super(R.layout.memo_list_item, data);
        this.type = type;

    }


    @Override
    protected void convert(BaseViewHolder helper, MemoListItemBean item) {
        helper.setText(R.id.tv_name, item.getCreateObj().getEmployee_name());
        String title = item.getTitle();
        title = title.replace("\n", " ");
        helper.setText(R.id.tv_item_title, title);
        helper.setText(R.id.tv_time, DateTimeUtil.fromTime(TextUtil.parseLong(item.getCreate_time())));
        ImageView image = helper.getView(R.id.memo_pic);
        ImageView avatar = helper.getView(R.id.avatar);
        if (item.getCreateObj() != null) {
            ImageLoader.loadCircleImage(
                    helper.getConvertView()
                            .getContext(),
                    item
                            .getCreateObj()
                            .getPicture(),
                    avatar,
                    item.getCreateObj().getEmployee_name());
        } else {
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(), R.drawable.icon_im_group, avatar);
        }

        if ("0".equals(item.getRemind_time()) || TextUtils.isEmpty(item.getRemind_time())) {
            helper.setVisible(R.id.iv_remind, false);
        } else {
            helper.setVisible(R.id.iv_remind, true);
        }
        if (TextUtils.isEmpty(item.getShare_ids())) {
            helper.setVisible(R.id.iv_share, false);
        } else {
            helper.setVisible(R.id.iv_share, true);
        }
        if (TextUtils.isEmpty(item.getPic_url())) {
            image.setVisibility(View.INVISIBLE);
        } else {
            image.setVisibility(View.VISIBLE);
            ImageLoader.loadImageNoAnimCenterCrop(helper.getConvertView().getContext(), item.getPic_url(), image);
        }

        SwipeMenuLayout la = helper.getView(R.id.sml);
        la.setOnMenuSwitchListener(new SwipeMenuLayout.OnMenuSwitchListener() {
            @Override
            public void toggle(boolean open) {
                isMenuOpen = open;
            }
        });

       /* la.setOnCloseListener(() -> {
            try {
                notifyItemChanged(helper.getAdapterPosition());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/
        la.scrollTo(0, 0);
        CheckBox cb = helper.getView(R.id.checkbox);
        RelativeLayout rl = helper.getView(R.id.rl_check);
        if (editState) {
            cb.setChecked(item.isChecked());
            if (la.getLeft() == 0 || rl.getVisibility() != View.VISIBLE) {
                helper.getConvertView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        la.scrollTo(-rl.getLayoutParams().width, 0);
                    }
                }, 500);
            }
            rl.setVisibility(View.VISIBLE);
            la.setSwipeEnable(false);
            la.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb.setChecked(!cb.isChecked());
                    item.setChecked(cb.isChecked());

                    if (mListener != null) {
                        mListener.onCheck(getSelectItem());
                    }
                }
            });
            la.scrollTo(-rl.getLayoutParams().width, 0);

            return;
        } else {
            rl.setVisibility(View.INVISIBLE);
            la.setSwipeEnable(true);
            la.scrollTo(0, 0);
        }


        switch (type) {
            case MemoConstant.TYPE_ALL:
                if (item.getCreateObj() != null) {
                    la.setLeftSwipe(true);
                    if (SPUtils.getString(helper.getConvertView().getContext(), AppConst.EMPLOYEE_ID).equals(item.getCreate_by())) {
                        la.setLeftSwipe(true);
                        helper.getView(R.id.delete_item).setVisibility(View.VISIBLE);
                        helper.getView(R.id.delete_item_conform).setVisibility(View.GONE);
                        helper.getView(R.id.quit_share).setVisibility(View.GONE);
                        helper.getView(R.id.delete_forever).setVisibility(View.GONE);
                        //删除
                        helper.getView(R.id.delete_item).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                helper.getView(R.id.delete_item_conform).setVisibility(View.VISIBLE);
//                                helper.getView(R.id.delete_item).setVisibility(View.GONE);
                                la.scrollTo(0, 0);
                                EventBusUtils.sendEvent(new MessageBean(MemoConstant.DELETE_ITEM, item.getId(), ""));
                                remove(helper.getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        });
                    } else {
                        la.setLeftSwipe(true);
                        helper.getView(R.id.delete_item).setVisibility(View.GONE);
                        helper.getView(R.id.delete_item_conform).setVisibility(View.GONE);
                        helper.getView(R.id.quit_share).setVisibility(View.VISIBLE);
                        helper.getView(R.id.delete_forever).setVisibility(View.GONE);
                        helper.getView(R.id.recover).setVisibility(View.GONE);
                        helper.getView(R.id.quit_share).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //退出共享
                                EventBusUtils.sendEvent(new MessageBean(MemoConstant.QUIT_SHARE, item.getId(), ""));
                                remove(helper.getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        });
                    }

                } else {
                    la.setLeftSwipe(false);
                }

                break;
            case MemoConstant.TYPE_MINE:
                la.setLeftSwipe(true);
                helper.getView(R.id.delete_item).setVisibility(View.VISIBLE);
                helper.getView(R.id.delete_item_conform).setVisibility(View.GONE);
                helper.getView(R.id.quit_share).setVisibility(View.GONE);
                helper.getView(R.id.delete_forever).setVisibility(View.GONE);
                helper.getView(R.id.recover).setVisibility(View.GONE);
                helper.getView(R.id.delete_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        helper.getView(R.id.delete_item_conform).setVisibility(View.VISIBLE);
//                        helper.getView(R.id.delete_item).setVisibility(View.GONE);
                        EventBusUtils.sendEvent(new MessageBean(MemoConstant.DELETE_ITEM, item.getId(), ""));
                        remove(helper.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
                break;
            case MemoConstant.TYPE_SHARED:
                la.setLeftSwipe(true);
                helper.getView(R.id.delete_item).setVisibility(View.GONE);
                helper.getView(R.id.delete_item_conform).setVisibility(View.GONE);
                helper.getView(R.id.quit_share).setVisibility(View.VISIBLE);
                helper.getView(R.id.delete_forever).setVisibility(View.GONE);
                helper.getView(R.id.recover).setVisibility(View.GONE);
                helper.getView(R.id.quit_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //退出共享
                        EventBusUtils.sendEvent(new MessageBean(MemoConstant.QUIT_SHARE, item.getId(), ""));
                        remove(helper.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
                break;
            case MemoConstant.TYPE_DELETE:
                la.setLeftSwipe(true);
                helper.getView(R.id.delete_item).setVisibility(View.GONE);
                helper.getView(R.id.delete_item_conform).setVisibility(View.GONE);
                helper.getView(R.id.quit_share).setVisibility(View.GONE);
                helper.getView(R.id.recover).setVisibility(View.VISIBLE);
                helper.getView(R.id.delete_forever).setVisibility(View.VISIBLE);

                helper.getView(R.id.delete_forever).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除
                        helper.getView(R.id.delete_forever).setVisibility(View.GONE);
                        helper.getView(R.id.delete_item_conform).setVisibility(View.VISIBLE);

                    }
                });
                helper.getView(R.id.delete_item_conform).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //彻底删除
                        helper.getView(R.id.delete_forever).setVisibility(View.VISIBLE);
                        helper.getView(R.id.delete_item_conform).setVisibility(View.GONE);
                        EventBusUtils.sendEvent(new MessageBean(MemoConstant.DELETE_FOREVER, item.getId(), ""));
                        remove(helper.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
                helper.getView(R.id.recover).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //恢复
                        helper.getView(R.id.delete_item).setVisibility(View.VISIBLE);
                        helper.getView(R.id.delete_item_conform).setVisibility(View.GONE);
                        EventBusUtils.sendEvent(new MessageBean(MemoConstant.RECOVER_MEMO, item.getId(), ""));
                        remove(helper.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
                break;
            default:
                la.setSwipeEnable(false);
                break;


        }

    }

    public void setEditState(boolean editState) {
        this.editState = editState;
        if (editState) {
            for (int i = 0; i < getData().size(); i++) {
                getData().get(i).setChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    public boolean isEditState() {
        return editState;
    }

    public List<MemoListItemBean> getSelectItem() {
        List<MemoListItemBean> li = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).isChecked()) {
                li.add(getData().get(i));
            }
        }
        return li;
    }

    private OnCheckListener mListener;

    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    public interface OnCheckListener {
        void onCheck(List<MemoListItemBean> list);
    }

    public void setCheckListener(OnCheckListener l) {
        this.mListener = l;
    }

    public void setType(int type) {
        this.type = type;
    }
}