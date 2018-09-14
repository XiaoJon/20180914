package com.hjhq.teamface.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.bean.AddSingleChatResponseBean;
import com.hjhq.teamface.basis.bean.EmployeeResultBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CharacterParser;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
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
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * 发起聊天
 * Created by Administrator on 2017/4/17.
 */
@RouteNode(path = "/choose_member", desc = "选择要发送的对象")
public class ChooseSendMemberActivity extends BaseTitleActivity implements AdapterView.OnItemClickListener {
    ListView mListView;
    TextView stateTv;
    //右边的bar
    SideBar sideBar;
    TextView dialog;
    TextView searchEditText;
    RelativeLayout chooseGroup;
   /* @Bind(R.id.company_contacts_item_rl)
    LinearLayout contactsHeader;*/


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
    private List<Member> mListBeen = new ArrayList<>();
    private boolean chooseMember = false;
    private String chooseTag = "";


    @Override
    protected void initView() {
        super.initView();
        mListView = findViewById(R.id.lv_contacts);
        stateTv = findViewById(R.id.state_tv);
        //右边的bar
        sideBar = findViewById(R.id.sidrbar);
        dialog = findViewById(R.id.dialog);
        searchEditText = findViewById(R.id.search_edit_text);
        chooseGroup = (RelativeLayout) findViewById(R.id.choose_a_group_rl);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            chooseTag = bundle.getString(Constants.DATA_TAG1);
            if (bundle != null) {
                if (MsgConstant.MEMBER_TAG.equals(chooseTag)) {
                    chooseMember = true;
                    setActivityTitle(getResources().getString(R.string.im_send_to_sb));
                } else if (MsgConstant.SEND_FILE_TO_SB.equals(chooseTag)) {
                    chooseMember = true;
                    setActivityTitle(R.string.select_contact);
                } else {
                    setActivityTitle(R.string.im_create_a_gingle_chat);
                }
            }
        } else {
            ToastUtils.showError(mContext, "数据错误");
            finish();
        }


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
        return R.layout.activity_team_message_choose_send_member;
    }

    @Override
    protected void setListener() {
        chooseGroup.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
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
        /*if (SettingHelper.checkPermssion(Constants.ENTERPRISE_INFORM_CREATE_GROUP)) {
            contactsHeader.setVisibility(View.VISIBLE);
        }*/
        loadContacts();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (view.getId() == R.id.choose_a_group_rl) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DATA_TAG1, chooseTag);
            CommonUtil.startActivtiyForResult(ChooseSendMemberActivity.this,
                    ChooseGroupChatActivity.class, Constants.REQUEST_CODE5, bundle);
        }

    }


    private void loadContacts() {
        if (mListBeen == null) {
            mListBeen = new ArrayList<>();
        }
        mListBeen.clear();
        ImLogic.getInstance().getEmployee(ChooseSendMemberActivity.this, null,
                new ProgressSubscriber<EmployeeResultBean>(ChooseSendMemberActivity.this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.showToast(mContext, "获取成员失败!");
                    }

                    @Override
                    public void onNext(EmployeeResultBean employeeResultBean) {
                        super.onNext(employeeResultBean);
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
        Iterator<Member> iterator = mListBeen.iterator();
        while (iterator.hasNext()) {
            if (SPHelper.getEmployeeId().equals(iterator.next().getId() + "")) {
                iterator.remove();
            }
        }
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
        ImLogic.getInstance().addSingleChat(ChooseSendMemberActivity.this,
                mListBeen.get(position).getSign_id() + "",
                new ProgressSubscriber<AddSingleChatResponseBean>(this) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);


                    }

                    @Override
                    public void onNext(AddSingleChatResponseBean bean) {
                        super.onNext(bean);

                        Conversation conversation = new Conversation();

                        conversation.setCompanyId(bean.getData().getId());
                        conversation.setOneselfIMID(SPHelper.getUserId());
                        conversation.setReceiverId(bean.getData().getReceiver_id());
                        conversation.setConversationType(MsgConstant.SINGLE);
                        conversation.setTitle(bean.getData().getEmployee_name());
                        conversation.setConversationId(TextUtil.parseLong(bean.getData().getId()));
                        conversation.setSenderAvatarUrl(bean.getData().getPicture());
                        conversation.setAvatarUrl(bean.getData().getPicture());
                        conversation.setSenderName(bean.getData().getEmployee_name());
                        if (chooseMember) {
                            EventBusUtils.sendEvent(new MessageBean(Constants.REQUEST_CODE5, MsgConstant.RESEND_MEMBER_TAG, conversation));
                            Intent intent = new Intent();
                            intent.putExtra(Constants.DATA_TAG1, conversation);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(MsgConstant.CONVERSATION_TAG, conversation);
                            bundle.putString(MsgConstant.RECEIVER_ID, bean.getData().getReceiver_id());
                            bundle.putLong(MsgConstant.CONVERSATION_ID, TextUtil.parseLong(bean.getData().getId()));
                            bundle.putString(MsgConstant.CONV_TITLE, bean.getData().getEmployee_name());
                            bundle.putInt(MsgConstant.CHAT_TYPE, bean.getData().getChat_type());
                            CommonUtil.startActivtiy(ChooseSendMemberActivity.this, ChatActivity.class, bundle);
                            finish();
                        }

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE5 && resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}