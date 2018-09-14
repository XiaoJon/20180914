package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.common.view.SearchBar;
import com.hjhq.teamface.im.db.DBManager;
import com.hjhq.teamface.oa.login.bean.QueryEmployeeBean;
import com.hjhq.teamface.im.adapter.SearchContactsAdapter;
import com.hjhq.teamface.oa.main.logic.MainLogic;
import com.hjhq.teamface.util.CommonUtil;
import com.hjhq.teamface.view.recycler.EmptyView;
import com.hjhq.teamface.view.recycler.SimpleItemClickListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Administrator
 */
public class SearchContactActivity extends BaseActivity {
    private RecyclerView historyRv;
    private RecyclerView resultRv;
    private SearchBar mSearchBar;
    private List<Member> resultList = new ArrayList<>();
    private List<String> historyItemList = new ArrayList<>();
    private EmptyView emptyView;
    private EmptyView emptyView2;
    private TextView clearAll;
    private View footerView;
    private String keyword;
    private SearchContactsAdapter mSearchResultListAdapter;

    private TextView title;
    private TextView num;
    private int actionType = MsgConstant.VIEW_DETAIL;
    private int selectType = C.RADIO_SELECT;
    private int dataType = MsgConstant.VIEW_LOCAL_DATA;
    private String departmentId = "";
    private String departmentName = "";

    @Override
    protected int getContentView() {
        return R.layout.search_chat_activity;
    }


    @Override
    protected void initView() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            actionType = bundle.getInt(Constants.DATA_TAG1);
            dataType = bundle.getInt(Constants.DATA_TAG2);
            departmentId = bundle.getString(Constants.DATA_TAG3);
            departmentName = bundle.getString(Constants.DATA_TAG4);
            selectType = bundle.getInt(Constants.DATA_TAG5, C.RADIO_SELECT);
        }
        mSearchBar = (SearchBar) findViewById(R.id.search_bar);
        resultRv = (RecyclerView) findViewById(R.id.search_result_recycler_view);
        historyRv = (RecyclerView) findViewById(R.id.search_history_recycler_view);
        resultRv.setLayoutManager(new LinearLayoutManager(this));
        historyRv.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultListAdapter = new SearchContactsAdapter(null);
        if (selectType == C.MULTIL_SELECT) {
            mSearchResultListAdapter.setMultiCheck(true);
        } else if (selectType == C.RADIO_SELECT) {
            mSearchResultListAdapter.setMultiCheck(false);
        }
        resultRv.setAdapter(mSearchResultListAdapter);
        emptyView = new EmptyView(this);
        emptyView.setEmptyTitle(getString(R.string.im_search_hint));
        emptyView.setEmptyImage(R.drawable.icon_no_search_result);
        emptyView2 = new EmptyView(this);
        emptyView2.setEmptyTitle(getString(R.string.im_search_hint));
        emptyView2.setEmptyImage(R.drawable.icon_no_search_result);
        footerView = getLayoutInflater().inflate(R.layout.item_crm_search_goods_history_footer, null);
        clearAll = (TextView) footerView.findViewById(R.id.tv_clean_history_search_log);
        if (dataType == MsgConstant.VIEW_LOCAL_DATA) {
            mSearchBar.setHintText(getString(R.string.im_search_hint));
        } else if (dataType == MsgConstant.VIEW_NET_DATA) {
            mSearchBar.setHintText("搜索范围:" + departmentName);
        }

        mSearchBar.requestTextFocus();
        View headerView = inflater.inflate(R.layout.item_search_result_header, null);
        title = (TextView) headerView.findViewById(R.id.name);
        num = (TextView) headerView.findViewById(R.id.num);
        title.setText(getString(R.string.search_contacts));
        num.setVisibility(View.GONE);
        mSearchResultListAdapter.setEmptyView(emptyView);
        mSearchResultListAdapter.addHeaderView(headerView);

    }

    @Override
    protected void setListener() {
        mSearchBar.setSearchListener(new SearchBar.SearchListener() {
            @Override
            public void clear() {
                ArrayList<Member> list = new ArrayList<Member>();
                for (int i = 0; i < resultList.size(); i++) {
                    if (resultList.get(i).isCheck()) {
                        list.add(resultList.get(i));
                    }
                }
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG1, list);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void cancel() {
                SoftKeyboardUtils.hide(mSearchBar.getEditText());
                finish();
            }

            @Override
            public void search() {
                switch (dataType) {
                    case MsgConstant.VIEW_LOCAL_DATA:
                        searchLocalData();
                        break;
                    case MsgConstant.VIEW_NET_DATA:
                        searchNetData();
                        break;
                    default:

                        break;
                }

            }

            @Override
            public void getText(String text) {
                keyword = text;
                if (dataType == MsgConstant.VIEW_LOCAL_DATA) {
                    searchLocalData();
                }
            }
        });
        //企信聊天搜索联系人中的自己点击如何处理?企信创建聊天搜到自己并且只选择了自己如何处理?
        resultRv.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (actionType == MsgConstant.VIEW_DETAIL) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DATA_TAG1, resultList.get(position).getId() + "");
                    bundle.putString(Constants.DATA_TAG3, resultList.get(position).getSign_id());
                    CommonUtil.startActivtiy(mContext, EmployeeInfoActivity.class, bundle);
                } else if (actionType == MsgConstant.CREATE_CHAT) {
                    if (selectType == C.MULTIL_SELECT) {
                        resultList.get(position).setCheck(!resultList.get(position).isCheck());
                        mSearchResultListAdapter.notifyDataSetChanged();
                    } else if (selectType == C.RADIO_SELECT) {
                        ArrayList<Member> list = new ArrayList<Member>();
                        list.add(resultList.get(position));
                        Intent intent = new Intent();
                        intent.putExtra(Constants.DATA_TAG1, list);
                        setResult(RESULT_OK, intent);
                        finish();
                    }


                }

            }
        });
    }

    /**
     * 本地数据库搜索
     */
    private void searchLocalData() {
        if (TextUtils.isEmpty(keyword)) {
            return;
        }
        mSearchResultListAdapter.setKeyword(keyword);
        emptyView.setEmptyTitle(getString(R.string.im_searching));
        List<Member> list = DBManager.getInstance().queryMemberByName(keyword);
        final Iterator<Member> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Member member = iterator.next();
            if (SPHelper.getUserId().equals(member.getSign_id())) {
                iterator.remove();
            }
        }
        resultList.clear();
        if (list != null) {
            resultList.addAll(list);
            //title.setText("" + list.size());
        } else {
            resultList.addAll(new ArrayList<>());
            // title.setText("" + list.size());
        }
        mSearchResultListAdapter.setNewData(resultList);
        mSearchResultListAdapter.notifyDataSetChanged();
        emptyView.setEmptyTitle(getString(R.string.im_no_search_result_contact));
    }

    /**
     * 网络搜索
     */
    private void searchNetData() {
        if (TextUtils.isEmpty(keyword)) {
            // TODO: 2018/8/9 要不要提醒输入内容?
            ToastUtils.showToast(mContext, "请输入关键字");
            return;
        }
        MainLogic.getInstance().findEmployee(mContext, departmentId, keyword,
                new ProgressSubscriber<QueryEmployeeBean>(mContext) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(QueryEmployeeBean queryEmployeeBean) {
                        super.onNext(queryEmployeeBean);
                        mSearchResultListAdapter.getData().clear();
                        resultList.clear();
                        mSearchResultListAdapter.notifyDataSetChanged();
                        if (queryEmployeeBean != null && queryEmployeeBean.getData() != null && queryEmployeeBean.getData().size() > 0) {
                            final List<QueryEmployeeBean.DataBean> data = queryEmployeeBean.getData();
                            for (int i = 0; i < data.size(); i++) {
                                final QueryEmployeeBean.DataBean bean = data.get(i);
                                if (SPHelper.getUserId().equals(bean.getSign_id())) {
                                    continue;
                                }
                                Member member = new Member();
                                member.setEmployee_name(bean.getEmployee_name());
                                member.setId(TextUtil.parseLong(bean.getId()));
                                member.setSign_id(bean.getSign_id());
                                member.setPicture(bean.getPicture());
                                member.setPhone(bean.getPhone());
                                member.setEmail(bean.getEmail());
                                member.setName(bean.getEmployee_name());
                                member.setEmployee_name(bean.getEmployee_name());
                                // member.setAccount(bean.getEmployee_name());
                                resultList.add(member);
                            }
                            if (resultList.size() > 0) {
                                mSearchResultListAdapter.getData().addAll(resultList);
                                mSearchResultListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    protected void initData() {

    }


    public void dismissSoftInputAndShowMenu() {

    }


    @Override
    public void onClick(View v) {

    }
}
