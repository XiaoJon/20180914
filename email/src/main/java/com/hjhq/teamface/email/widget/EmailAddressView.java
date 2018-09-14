package com.hjhq.teamface.email.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.FormValidationUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.email.MyLayoutManager;
import com.hjhq.teamface.email.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 * Describe：
 */

public class EmailAddressView extends RelativeLayout {
    private Context mContext;
    private String titleText;
    private InputEmailAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RelativeLayout mRlList;
    private RelativeLayout mRlText;
    private RelativeLayout mRlAdd;
    private TextView mTitle;
    private TextView mContent;
    private TextView mTvStar;
    private List<Member> mDataList = new ArrayList<>();
    private boolean isNeeded;
    private Member m1;
    private Member m2;
    private boolean clickBefore = false;
    private boolean dataChanged = false;


    public EmailAddressView(Context context, String title, boolean isNeeded) {
        super(context, null);
        this.mContext = context;
        this.titleText = title;
        this.isNeeded = isNeeded;
        View view = LayoutInflater.from(context).inflate(R.layout.email_address_view, null);
        addView(view);
        initView();
        initEvent();
    }


    public EmailAddressView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public EmailAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mRlText.setVisibility(INVISIBLE);
                    mRlList.setVisibility(VISIBLE);
                } else {
                    mRlText.setVisibility(VISIBLE);
                    mRlList.setVisibility(INVISIBLE);
                }
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRlAdd = (RelativeLayout) findViewById(R.id.rl_more_3);
        mRlList = (RelativeLayout) findViewById(R.id.rl_rv);
        mRlText = (RelativeLayout) findViewById(R.id.rl_text);
        mContent = (TextView) findViewById(R.id.content);
        mTitle = (TextView) findViewById(R.id.title);
        mTvStar = (TextView) findViewById(R.id.star);
        if (isNeeded) {
            mTvStar.setVisibility(VISIBLE);
        }
        mTitle.setText(titleText);
        initData();
        MyLayoutManager layout = new MyLayoutManager();
        //必须，防止recyclerview高度为wrap时测量item高度0
        layout.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layout);
        mAdapter = new InputEmailAdapter(mContext, this, mDataList);
        mAdapter.setOnDataSetChangedListener(new InputEmailAdapter.OnDataSetChangeListener() {
            @Override
            public void onChange() {
                dataChanged = true;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mDataList.clear();
        m1 = new Member();
        m1.setEmployee_name(titleText);
        m1.setCheck(isNeeded);
        m2 = new Member();
        /*if (isNeeded) {

            m2.setEmployee_name("请输入(必填)");

        } else {
            m2.setEmployee_name("请输入(选填)");
        }*/
        mDataList.add(m1);
        mDataList.add(m2);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                if (position == 0 || position == adapter.getItemCount() - 1) {
                    return;
                }
                boolean check = ((InputEmailAdapter) adapter).getData().get(position).isCheck();
                String temp = mAdapter.getTempText();
                mAdapter.setTempText(temp);

                for (int i = 0; i < ((InputEmailAdapter) adapter).getData().size(); i++) {
                    if (0 == i) {
                        ((InputEmailAdapter) adapter).getData().get(i).setCheck(isNeeded);
                    } else {
                        ((InputEmailAdapter) adapter).getData().get(i).setCheck(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
                if (!check) {
                    ((InputEmailAdapter) adapter).getData().get(position).setCheck(true);
                }
                mAdapter.notifyItemChanged(position);
                mRlText.setVisibility(INVISIBLE);
                mRlList.setVisibility(VISIBLE);
                mAdapter.requestFocus();

            }

        });
        mRlText.setOnClickListener(v -> {
            clickBefore = true;
            mRlText.setVisibility(INVISIBLE);
            mRlList.setVisibility(VISIBLE);
            mAdapter.requestFocus();
        });
       /* mRlAdd.setOnClickListener(v -> {

        });*/
    }

    /**
     * 设置标题
     *
     * @param text
     */
    public void setTitle(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTitle.setText(text);
        }

    }

    /**
     * 设置内容
     *
     * @param size
     * @param text
     */
    public void setContent(int size, String text) {
        Rect bounds = new Rect();
        TextPaint paint;
        paint = mContent.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        String num = " 等" + (mDataList.size() - 2) + "人";
        int textWidth = bounds.width();
        int width = mContent.getWidth();
        Log.e("字符宽度", "" + textWidth);
        Log.e("控件宽度", "" + width);
        if (textWidth > width) {
            text = text + num;
        }
        if (!TextUtils.isEmpty(text)) {
            mContent.setText(text);
        }

    }

    /**
     * 失去焦点隐藏输入
     */
    public void hideList() {
        mRlText.clearFocus();
        mRlList.clearFocus();

        if (mDataList.size() >= 3) {
            mRlList.setVisibility(GONE);
            mRlText.setVisibility(VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mDataList.size(); i++) {
                if (i == 0 || i == mDataList.size() - 1) {
                    continue;
                }
                if (mDataList.size() >= 3) {
                    if (i == mDataList.size() - 2) {
                        if (TextUtils.isEmpty(mDataList.get(i).getEmployee_name())) {

                            sb.append(mDataList.get(i).getEmail());
                        } else {
                            sb.append(mDataList.get(i).getEmployee_name() + mDataList.get(i).getEmail());
                        }
                    } else {
                        if (TextUtils.isEmpty(mDataList.get(i).getEmployee_name())) {
                            sb.append(mDataList.get(i).getEmail() + ",");
                        } else {
                            sb.append(mDataList.get(i).getEmployee_name() + mDataList.get(i).getEmail() + ",");
                        }
                    }
                }
            }
            setContent(mDataList.size() - 2, sb.toString());
        } else {
            mRlList.setVisibility(VISIBLE);
            mRlText.setVisibility(INVISIBLE);
        }


    }

    /**
     * 获取焦点显示输入
     */
    public void showList() {
        mRlList.setVisibility(VISIBLE);
        mRlText.setVisibility(INVISIBLE);
        mAdapter.requestFocus();
    }

    /**
     * 获取联系人数据
     *
     * @return
     */
    public ArrayList<Member> getMember() {
        mAdapter.collectData();
        ArrayList<Member> list = new ArrayList<>();

        for (int i = 0; i < mDataList.size(); i++) {
            if (i == 0 || i == mDataList.size() - 1) {
                continue;
            } else {
                if (!FormValidationUtils.isEmail(mDataList.get(i).getEmail())) {
                    continue;
                } else {
                    list.add(mDataList.get(i));
                }
            }
        }
        return list;
    }

    /**
     * 添加(选择的联系人)
     *
     * @param list
     */
    public void addMember(List<Member> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        //去重复
        List<Member> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Member member = list.get(i);
            boolean flag = false;
            for (int j = 0; j < list2.size(); j++) {
                Member member1 = list2.get(j);
                if (!TextUtils.isEmpty(member1.getEmail())
                        && member1.getEmail().equals(member.getEmail())
                    //如果允许不同人使用相同邮箱地址添加此判断
                    //   && member1.getId() == member.getId()
                        ) {
                    flag = true;
                }
            }
            if (!flag) {
                list2.add(member);
            } else {
                continue;
            }
        }


        dataChanged = true;
        //去重复
        ArrayList<Member> oldMemberList = getMember();
        if (oldMemberList.size() > 0) {
            for (int i = 0; i < list2.size(); i++) {
                String email = list2.get(i).getEmail();
                list2.get(i).setCheck(false);
                boolean hasAdded = false;
                for (int j = 0; j < oldMemberList.size(); j++) {
                    if (email.equals(oldMemberList.get(j).getEmail())) {
                        hasAdded = true;
                    }
                }
                if (hasAdded) {
                    continue;
                } else {
                    mDataList.add(mDataList.size() - 1, list2.get(i));
                }
            }
            mAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < list2.size(); i++) {
                list2.get(i).setCheck(false);
            }
            mDataList.clear();
            mDataList.add(m1);
            mDataList.addAll(list2);
            mDataList.add(m2);
            mAdapter.notifyDataSetChanged();
        }
        // hideList();

    }

    public void addMember(Member member) {
        member.setCheck(false);
        mDataList.add(mAdapter.getItemCount() - 1, member);
        mAdapter.notifyDataSetChanged();
        mAdapter.requestFocus();
        dataChanged = true;

    }

    /**
     * 隐藏添加按钮
     */
    public void hideAddButton() {
        if (mRlAdd != null) {
            mRlAdd.setVisibility(GONE);
        }
    }

    /**
     * 显示添加按钮
     */
    public void showAddButton() {
        if (mRlAdd != null) {
            mRlAdd.setVisibility(VISIBLE);
        }
    }

    /**
     * 添加按钮点击监听
     *
     * @param listener
     */
    public void setOnAddClickListener(OnClickListener listener) {
        mRlAdd.setOnClickListener(listener);

    }


    public void onFocusChange(View v, boolean hasFocus) {
        if (mListener != null && hasFocus) {
            mListener.onFocus();
        }
        if (hasFocus) {
            mRlList.setVisibility(VISIBLE);
            mRlText.setVisibility(INVISIBLE);
        } else {

        }
        /*if (!hasFocus) {
            mRlList.setVisibility(GONE);
            mRlText.setVisibility(VISIBLE);
            hideList();
        } else {
            mRlList.setVisibility(VISIBLE);
            mRlText.setVisibility(GONE);
        }*/


    }

    /**
     * 清空数据
     */
    public void clear() {
        initData();
    }

    public void releaseFocus() {
        mAdapter.clearFocus();
        clearFocus();
        hideList();

    }

    OnFocusListener mListener;

    public interface OnFocusListener {
        void onFocus();
    }

    public void setOnFocusListener(OnFocusListener l) {
        this.mListener = l;

    }

    public boolean isDataChanged() {
        return dataChanged;
    }

    public void setDataChanged(boolean dataChanged) {
        this.dataChanged = dataChanged;
    }
}



