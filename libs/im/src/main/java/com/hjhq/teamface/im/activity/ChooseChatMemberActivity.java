package com.hjhq.teamface.im.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.AddSingleChatResponseBean;
import com.hjhq.teamface.basis.bean.EmployeeResultBean;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CharacterParser;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.ParseUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.common.bean.PinyinComparator2;
import com.hjhq.teamface.common.bean.SortModel;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.common.view.SideBar;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.adapter.ContactsSortAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


/**
 * 发起聊天
 * Created by Administrator on 2017/4/17.
 */

public class ChooseChatMemberActivity extends BaseTitleActivity implements AdapterView.OnItemClickListener {
    ListView mListView;
    TextView stateTv;
    //右边的bar
    SideBar sideBar;
    TextView dialog;
    TextView searchEditText;
    RelativeLayout headerCreateGroup;
    LinearLayout contactsHeader;

    private List<SortModel> mAllContactsList;
    private ContactsSortAdapter adapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator2 pinyinComparator;
    List<Member> mListBeen = new ArrayList<>();


    @Override
    protected void initView() {
        super.initView();
        contactsHeader.setVisibility(View.GONE);
        mListView = findViewById(R.id.lv_contacts);
        stateTv = findViewById(R.id.state_tv);
        //右边的bar
        sideBar = findViewById(R.id.sidrbar);
        dialog = findViewById(R.id.dialog);
        searchEditText = findViewById(R.id.search_edit_text);
        headerCreateGroup = findViewById(R.id.header_choose_act_createGroup);
        contactsHeader = findViewById(R.id.company_contacts_item_rl);
        setActivityTitle("发起聊天");
        setGradientVisiblilty(true);
//        侧边的字母
        // sideBar.setTextView(dialog);
        mAllContactsList = new ArrayList<>();

        /** 给ListView设置adapter **/
        characterParser = CharacterParser.getInstance();
        pinyinComparator = PinyinComparator2.getInstance();

        //排序
        Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
        adapter = new ContactsSortAdapter(this, mAllContactsList);

        sideBar.setTextView(dialog);
        mListView.setAdapter(adapter);
    }

    @Override
    protected int getChildView() {
        return R.layout.activity_team_message_contacts;
    }

    @Override
    protected void setListener() {
        headerCreateGroup.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        headerCreateGroup.setOnClickListener(this);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable e) {
                //searchEditText.drawableDel();
                String content = searchEditText.getText().toString();
                if (content.length() > 0) {
                    ArrayList<SortModel> fileterList = (ArrayList<SortModel>) search(content);
                    adapter.updateListView(fileterList);
                } else {
                    adapter.updateListView(mAllContactsList);
                }
                mListView.setSelection(0);
            }

        });

        //设置右侧[A-Z]快速导航栏触摸监听
        sideBar.setOnTouchingLetterChangedListener(s -> {
            //该字母首次出现的位置
            int position = adapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                mListView.setSelection(position + 1);
            }
        });
    }


    @Override
    protected void initData() {
       /* if (SettingHelper.checkPermssion(Constants.ENTERPRISE_INFORM_CREATE_GROUP)) {
            contactsHeader.setVisibility(View.VISIBLE);
        }*/
        loadContacts();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.header_choose_act_createGroup) {
            CommonUtil.startActivtiy(this, CreateGroupActivity.class);
        }
    }


    private void loadContacts() {
        if (mListBeen == null) {
            mListBeen = new ArrayList<>();
        }
        mListBeen.clear();
        ImLogic.getInstance().getEmployee(ChooseChatMemberActivity.this, null, new ProgressSubscriber<EmployeeResultBean>(ChooseChatMemberActivity.this) {
            @Override
            public void onError(Throwable e) {
                ToastUtils.showToast(mContext, "获取成员失败!");
            }

            @Override
            public void onNext(EmployeeResultBean employeeResultBean) {

                mListBeen = employeeResultBean.getData();
                sortContacts();


            }
        });

    }

    /**
     * 联系人排序
     */
    private void sortContacts() {
        Collections.sort(mListBeen, (o1, o2) ->
                ParseUtil.getSortLetterBySortKey(o1.getEmployee_name()).compareTo(ParseUtil.getSortLetterBySortKey(o2.getEmployee_name())));
        HashSet<String> letterSet = new HashSet<>();

        for (Member dataBean : mListBeen) {
            String employee_name = dataBean.getEmployee_name();
            String phone = dataBean.getPhone();

            SortModel sortModel = new SortModel(employee_name, phone, employee_name);
            sortModel.role = dataBean.getRole_name();
            sortModel.photo = dataBean.getPicture();
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

        adapter.updateListView(mAllContactsList);
        sideBar.refresh(finalLetterArray);
        // LogUtil.e("排序ing3");
        if (CollectionUtils.isEmpty(mAllContactsList)) {
            stateTv.setText("数据为空");
        } else {
            stateTv.setVisibility(View.GONE);
        }
    }


    /**
     * 名字转拼音,取首字母
     *
     * @param name
     * @return
     */
    private String getSortLetter(String name) {
        String letter = "#";
        if (name == null) {
            return letter;
        }
        //汉字转换成拼音
        String pinyin = characterParser.getSelling(name);
        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }

    /**
     * 取sort_key的首字母
     *
     * @param sortKey
     * @return
     */
    private String getSortLetterBySortKey(String sortKey) {
        if (sortKey == null || "".equals(sortKey.trim())) {
            return null;
        }
        String letter = "#";
        //汉字转换成拼音
        String sortString = sortKey.trim().substring(0, 1).toUpperCase(Locale.CHINESE);
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        } else
//			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {// 5.0以上需要判断汉字
            if (sortString.matches("^[\u4E00-\u9FFF]+$"))// 正则表达式，判断是否为汉字
                letter = getSortLetter(sortString.toUpperCase(Locale.CHINESE));
//		}
        return letter;
    }

    /**
     * 模糊查询
     *
     * @param str
     * @return
     */
    private List<SortModel> search(String str) {
        List<SortModel> filterList = new ArrayList<>();// 过滤后的list
        if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
            if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
                String simpleStr = str.replaceAll("\\-|\\s", "");
                for (SortModel contact : mAllContactsList) {
                    if (contact.name != null) {
                        if (contact.simpleNumber.contains(simpleStr) || contact.name.contains(str)) {
                            if (!filterList.contains(contact)) {
                                filterList.add(contact);
                            }
                        }
                    }
                }
            } else {
                for (SortModel contact : mAllContactsList) {
                    if (contact.name != null) {
                        //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                        boolean isNameContains = contact.name.toLowerCase(Locale.CHINESE)
                                .contains(str.toLowerCase(Locale.CHINESE));

                        boolean isSortKeyContains = contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "")
                                .contains(str.toLowerCase(Locale.CHINESE));

                        boolean isSimpleSpellContains = contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE)
                                .contains(str.toLowerCase(Locale.CHINESE));

                        boolean isWholeSpellContains = contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE)
                                .contains(str.toLowerCase(Locale.CHINESE));

                        if (isNameContains || isSortKeyContains || isSimpleSpellContains || isWholeSpellContains) {
                            if (!filterList.contains(contact)) {
                                filterList.add(contact);
                            }
                        }
                    }
                }
            }
            return filterList;
        }
        return filterList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImLogic.getInstance().addSingleChat(ChooseChatMemberActivity.this,
                mListBeen.get(position).getSign_id() + "",
                new ProgressSubscriber<AddSingleChatResponseBean>(this) {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AddSingleChatResponseBean bean) {
                        Bundle bundle = new Bundle();
                        Conversation conversation = new Conversation();
                        conversation.setCompanyId(bean.getData().getId());
                        conversation.setOneselfIMID(SPHelper.getUserId());
                        conversation.setReceiverId(bean.getData().getReceiver_id());
                        conversation.setConversationType(MsgConstant.SINGLE);
                        conversation.setTitle(bean.getData().getEmployee_name());
                        conversation.setSenderAvatarUrl(bean.getData().getPicture());
                        try {
                            conversation.setIsHide(Integer.parseInt(bean.getData().getIs_hide()));
                        } catch (NumberFormatException e) {
                            conversation.setIsHide(0);
                        }


                        bundle.putSerializable(MsgConstant.CONVERSATION_TAG, conversation);
                        bundle.putString(MsgConstant.RECEIVER_ID, bean.getData().getReceiver_id());
                        bundle.putLong(MsgConstant.CONVERSATION_ID, TextUtil.parseLong(bean.getData().getId()));
                        bundle.putString(MsgConstant.CONV_TITLE, bean.getData().getEmployee_name());
                        bundle.putInt(MsgConstant.CHAT_TYPE, bean.getData().getChat_type());
                        CommonUtil.startActivtiy(ChooseChatMemberActivity.this, ChatActivity.class, bundle);
                        finish();
                    }
                });
    }


}