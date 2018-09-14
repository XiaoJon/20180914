package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.ParseUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.common.adapter.SelectEmployeeAdapter;
import com.hjhq.teamface.common.bean.PinyinComparator2;
import com.hjhq.teamface.common.bean.SortModel;
import com.hjhq.teamface.common.view.SideBar;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.GroupMemberAdapter;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * @author Administrator
 */
public class GroupMemberActivity extends BaseTitleActivity {

    public static final int FLAG_NORMAL = 2101;
    public static final int FLAG_ADD = 2102;
    public static final int FLAG_REMOVE = 2103;
    public static final int FLAG_AT = 2104;
    public static final int FLAG_TRANSFER = 2105;
    private int flag;
    private List<SortModel> mAllContactsList = new ArrayList<>();
    //根据拼音来排列ListView里面的数据类
    private PinyinComparator2 pinyinComparator;
    private GroupMemberAdapter mAdapter;
    private SelectEmployeeAdapter mAdapter2;
    private RecyclerView recyclerView;
    private List<Member> memberList;
    TextView tvAtAll;
    RelativeLayout rlAtAll;
    SideBar mSideBar;
    TextView dialog;
    EditText etKeyword;
    LinearLayoutManager mLinearLayoutManager;
    String keyword;


    @Override
    protected int getChildView() {
        return R.layout.activity_group_member;
    }

    @Override
    protected void initView() {
        super.initView();
        tvAtAll = findViewById(R.id.tv_all);
        rlAtAll = findViewById(R.id.rl_at_all);
        mSideBar = findViewById(R.id.sidrbar);
        dialog = findViewById(R.id.dialog);
        etKeyword = findViewById(R.id.et);
        mSideBar.setTextView(dialog);
    }

    @Override
    protected void initData() {
        flag = getIntent().getIntExtra(Constants.DATA_TAG2, FLAG_NORMAL);
        memberList = (List<Member>) getIntent().getSerializableExtra(Constants.DATA_TAG1);
        String text = "";
        if (flag == FLAG_NORMAL) {

            text = mContext.getString(R.string.group_members) + mContext.getString(R.string.combine_title);
            setActivityTitle(String.format(text, memberList.size()));
        } else if (flag == FLAG_REMOVE) {
            setActivityTitle(R.string.im_remove_group_member);
            setRightMenuColorTexts(getResources().getString(R.string.im_remove_member_menu));
        } else if (flag == FLAG_ADD) {
            setActivityTitle(R.string.im_add_group_member);
            setRightMenuColorTexts(getResources().getString(R.string.im_add_member_menu));
        } else if (flag == FLAG_AT) {
            setActivityTitle(R.string.im_choose_remind_member);
            rlAtAll.setVisibility(View.VISIBLE);
            TextUtil.setText(tvAtAll, "所有人(" + memberList.size() + ")");

        } else if (flag == FLAG_TRANSFER) {
            setActivityTitle(R.string.im_choose_new_admin);
            rlAtAll.setVisibility(View.GONE);
        }
        recyclerView = (RecyclerView) findViewById(R.id.rv_group_member);
        recyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MyLinearDeviderDecoration(this, R.color.red));
        mAdapter = new GroupMemberAdapter(memberList);
        pinyinComparator = PinyinComparator2.getInstance();
        Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
        mAdapter2 = new SelectEmployeeAdapter(new ArrayList<>());
        switch (flag) {
            case FLAG_NORMAL:
            case FLAG_TRANSFER:
                mAdapter2.setShowCheck(false);
                break;
            default:
                mAdapter2.setShowCheck(true);
                break;
        }
        sortContacts(memberList);
        recyclerView.setAdapter(mAdapter2);
    }

    /**
     * 搜索联系人
     */
    private void reSortContacts() {
        mAdapter2.getData().clear();
        mAdapter2.notifyDataSetChanged();
        List<Member> list = new ArrayList<>();
        if (TextUtils.isEmpty(keyword)) {
            list.addAll(memberList);
        } else {
            for (int i = 0; i < memberList.size(); i++) {
                if (memberList.get(i) == null || TextUtils.isEmpty(memberList.get(i).getEmployee_name())) {
                    continue;
                }
                if (memberList.get(i).getEmployee_name().contains(keyword)) {
                    list.add(memberList.get(i));
                }
            }
        }
        mAllContactsList.clear();
        sortContacts(list);
        Collections.sort(mAllContactsList, pinyinComparator);
        mAdapter2.notifyDataSetChanged();
    }

    /**
     * 联系人排序
     */
    private void sortContacts(List<Member> members) {

        Collections.sort(members, (o1, o2) ->
                ParseUtil.getSortLetterBySortKey(o1.getEmployee_name()).compareTo(ParseUtil.getSortLetterBySortKey(o2.getEmployee_name())));
        HashSet<String> letterSet = new HashSet<>();

        for (Member dataBean : members) {
            String employee_name = dataBean.getEmployee_name();
            String phone = dataBean.getPhone();

            SortModel sortModel = new SortModel(employee_name, phone, employee_name);
            sortModel.role = dataBean.getPost_name();
            sortModel.photo = dataBean.getPicture();
            sortModel.member = dataBean;
            String sortLetters = ParseUtil.getSortLetterBySortKey(employee_name);
            if (sortLetters == null) {
                sortLetters = ParseUtil.getSortLetter(employee_name);
            }

            letterSet.add(sortLetters);
            sortModel.sortLetters = sortLetters;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                sortModel.sortToken = ParseUtil.parseSortKey(employee_name);
            } else {
                sortModel.sortToken = ParseUtil.parseSortKeyLollipop(employee_name);
            }

            mAllContactsList.add(sortModel);
        }

        String[] letterArray = new String[]{};
        letterArray = letterSet.toArray(letterArray);
        Arrays.sort(letterArray);

        final String[] finalLetterArray = letterArray;
        mAdapter2.getData().addAll(mAllContactsList);
        mAdapter2.notifyDataSetChanged();
        mSideBar.refresh(finalLetterArray);
        // LogUtil.e("排序ing3");
        /*if (CollectionUtils.isEmpty(mAllContactsList)) {
            viewDelegate.setStateText("数据为空");
        } else {
            viewDelegate.setStateVisibility(View.GONE);
        }*/
    }

    @Override
    protected void setListener() {
        recyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (flag) {
                    case FLAG_NORMAL:
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.DATA_TAG3, mAdapter2.getData().get(position).member.getSign_id());
                        //CommonUtil.startActivtiy(mContext, EmployeeInfoActivity.class, bundle);
                        UIRouter.getInstance().openUri(mContext, "DDComp://app/employee/info", bundle);
                        break;
                    case FLAG_REMOVE:
                    case FLAG_ADD:
                        SortModel sortModel = mAdapter2.getData().get(position);
                        sortModel.member.setCheck(!sortModel.member.isCheck());
                        mAdapter2.notifyDataSetChanged();
                        break;
                    case FLAG_AT:
                    case FLAG_TRANSFER:
                        ArrayList<Member> list = new ArrayList<>();
                        list.add(mAdapter2.getData().get(position).member);
                        Intent intent = new Intent();
                        intent.putExtra(Constants.DATA_TAG1, list);
                        setResult(Activity.RESULT_OK, intent);
                        if (flag == FLAG_AT) {
                            EventBusUtils.sendEvent(new MessageBean(flag, Constants.MEMBER_LIST, list));
                        }
                        finish();
                        break;
                    default:

                        break;


                }
            }
        });
        rlAtAll.setOnClickListener(v -> {
            ArrayList<Member> list = new ArrayList<>();
            Member member = new Member();
            member.setSign_id(0L);
            member.setId(0L);
            member.setName("所有人");
            member.setEmployee_name("所有人");
            list.add(member);
            EventBusUtils.sendEvent(new MessageBean(FLAG_AT, Constants.MEMBER_LIST, list));
            finish();
        });
        mSideBar.setOnTouchingLetterChangedListener(s -> {
            //该字母首次出现的位置
            int position = mAdapter2.getPositionForSection(s.charAt(0));
            if (position != -1) {
                scrollToPosition(position + 1);
            }
        });
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
                reSortContacts();
            }
        });
        /*etKeyword.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    reSortContacts();
                    SoftKeyboardUtils.hide(etKeyword);
                } else {
                    return true;
                }
            }
            return false;
        });*/
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    reSortContacts();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 滚动到指定字母位置
     *
     * @param n
     */
    public void scrollToPosition(int n) {

        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            recyclerView.scrollToPosition(n - 1 < 0 ? 0 : n - 1);
        } else if (n <= lastItem) {
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            recyclerView.scrollToPosition(n);
        }
    }

    @Override
    protected void onRightMenuClick(int itemId) {
        ArrayList<Member> list = new ArrayList<>();
        for (int i = 0; i < memberList.size(); i++) {
            if (memberList.get(i).isCheck()) {
                list.add(memberList.get(i));
            }
        }
        if (list.size() <= 0) {
            ToastUtils.showToast(mContext, "请选择成员");
            return;
        }
        EventBusUtils.sendEvent(new MessageBean(1, MsgConstant.REMOVE_MEMBER_TAG, list));
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, list);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 这是@功能返回数据
     *
     * @param position
     */
    private void setResultAndFinish(int position) {
        ArrayList<Member> list = new ArrayList<>();
        list.add(memberList.get(position));
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, list);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    public void onClick(View view) {

    }
}
