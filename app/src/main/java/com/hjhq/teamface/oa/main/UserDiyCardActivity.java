package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.EmployeeCardDetailBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.oa.main.adaper.DIYCardFieldAdapter;
import com.hjhq.teamface.oa.main.adaper.DIYCardStyleAdapter;
import com.hjhq.teamface.oa.main.bean.DiyCardStyleBean;
import com.hjhq.teamface.oa.main.logic.MainLogic;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 二维码名片
 *
 * @author Administrator
 */
public class UserDiyCardActivity extends BaseActivity {
    @Bind(R.id.fl_user_info)
    FrameLayout flUserInfo;
    @Bind(R.id.rv_style)
    RecyclerView rvStyle;
    @Bind(R.id.rv_field)
    RecyclerView rvField;
    @Bind(R.id.tv_confirm)
    TextView tvConfirm;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;

    private ArrayList<String> userInfos = new ArrayList<>();
    private EmployeeCardDetailBean mCardBean;
    private List<DiyCardStyleBean> styleList = new ArrayList<>();
    private List<DiyCardStyleBean> fieldList = new ArrayList<>();
    private List<DiyCardStyleBean> tempFieldList = new ArrayList<>();
    String[] fieldArray = new String[]{"Logo", "座机", "手机", "邮件", "地址"};
    int[] fieldTypeArray = new int[]{0, 1, 2, 3, 4, 5};
    int[] resArray = new int[]{R.drawable.diy_card_style1, R.drawable.diy_card_style2};
    int[] layoutArray = new int[]{R.layout.user_card_diy_style1_layout, R.layout.user_card_diy_style2_layout};
    private TextView tvName;
    private TextView tvPost;
    private TextView tvEmail;
    private TextView tvCompanyName;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvFixedPhone;
    private ImageView ivLogo;
    RelativeLayout rlAddress;
    RelativeLayout rlPhone;
    RelativeLayout rlMobile;
    private String choosedType = "0";

    DIYCardStyleAdapter mStyleAdapter;
    DIYCardFieldAdapter mFieldAdapter;
    String styleArr = "[{\"id\":\"0\",\"styleId\":\"0\"},{\"id\":\"1\",\"styleId\":\"1\"}]";


    @Override
    protected int getContentView() {
        return R.layout.acitivity_user_diy_card;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //软键盘弹出时,底部操作按钮不被顶起
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userInfos = (ArrayList<String>) bundle.getSerializable(Constants.DATA_TAG2);
            mCardBean = (EmployeeCardDetailBean) bundle.getSerializable(Constants.DATA_TAG3);
            if (mCardBean == null || mCardBean.getData() == null) {
                EmployeeCardDetailBean.DataBean bean = new EmployeeCardDetailBean.DataBean();
                bean.setChoice_template("0");
                bean.setHide_set("");
                bean.setCard_template(styleArr);
                mCardBean.setData(bean);
            }
            choosedType = TextUtils.isEmpty(mCardBean.getData().getChoice_template()) ? "0" : mCardBean.getData().getChoice_template();
            if (!TextUtils.isEmpty(mCardBean.getData().getCard_template())) {
                styleArr = mCardBean.getData().getCard_template();
            }
            initCard();
            fillData();
            formatData();
        }
    }


    @Override
    protected void initView() {
        rvStyle.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public void setOrientation(int orientation) {
                super.setOrientation(HORIZONTAL);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvField.setLayoutManager(gridLayoutManager);
        mStyleAdapter = new DIYCardStyleAdapter(styleList);
        mFieldAdapter = new DIYCardFieldAdapter(new ArrayList<>());
        rvField.setAdapter(mFieldAdapter);
        rvStyle.setAdapter(mStyleAdapter);

    }

    /**
     * 初始化模版
     */
    private void initCard() {
        flUserInfo.removeAllViews();
        int layout;
        int drawableRes = 0;
        switch (choosedType) {
            case "0":
                layout = R.layout.user_card_diy_style1_layout;
                drawableRes = R.drawable.diy_card_style0_bg;
                break;
            case "1":
                layout = R.layout.user_card_diy_style2_layout;
                drawableRes = R.drawable.diy_card_style1_bg;
                break;
            default:
                layout = R.layout.user_card_diy_style1_layout;
                drawableRes = R.drawable.diy_card_style0_bg;
                break;
        }
        View inflate = getLayoutInflater().inflate(layout, null);
        inflate.setBackgroundResource(drawableRes);
        flUserInfo.addView(inflate);
        tvName = inflate.findViewById(R.id.tv_name);
        tvPost = inflate.findViewById(R.id.tv_duty);
        tvEmail = inflate.findViewById(R.id.tv_email);
        tvCompanyName = inflate.findViewById(R.id.text1);
        tvAddress = inflate.findViewById(R.id.text2);
        tvPhone = inflate.findViewById(R.id.text3);
        tvFixedPhone = inflate.findViewById(R.id.text4);
        rlAddress = inflate.findViewById(R.id.rl2);
        rlMobile = inflate.findViewById(R.id.rl3);
        rlPhone = inflate.findViewById(R.id.rl4);
        ivLogo = inflate.findViewById(R.id.iv_logo);
    }

    /**
     * 填充数据
     */
    private void fillData() {
        TextUtil.setText(tvName, userInfos.get(0));
        TextUtil.setText(tvCompanyName, userInfos.get(1));
        TextUtil.setText(tvPost, userInfos.get(2));
        TextUtil.setText(tvPhone, userInfos.get(3));
        TextUtil.setText(tvAddress, userInfos.get(4));
        TextUtil.setText(tvFixedPhone, userInfos.get(5));
        TextUtil.setText(tvEmail, userInfos.get(6));
        ImageLoader.loadImage(mContext, ivLogo, userInfos.get(8), R.drawable.blank);
    }

    @Override
    protected void setListener() {
        tvCancel.setOnClickListener(v -> {
            finish();
        });
        tvConfirm.setOnClickListener(v -> {
            saveData();
        });
        rvStyle.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                for (int i = 0; i < styleList.size(); i++) {
                    styleList.get(i).setChecked(false);
                    if (position == i) {
                        styleList.get(i).setChecked(true);
                    }
                    choosedType = position + "";
                }
                mStyleAdapter.notifyDataSetChanged();
                changeStyle(position);
            }
        });
        rvField.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                mFieldAdapter.getData().get(position).setChecked(!mFieldAdapter.getData().get(position).isChecked());
                mFieldAdapter.notifyDataSetChanged();
                fieldChanged(position);
            }
        });
    }


    /**
     * 保存模板
     */
    private void saveData() {
        EmployeeCardDetailBean.DataBean bean = new EmployeeCardDetailBean.DataBean();
        bean.setChoice_template(choosedType);
        bean.setCard_template(styleArr);
        final List<DiyCardStyleBean> data = mFieldAdapter.getData();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isChecked()) {
                sb.append(i + ",");
            }
        }
        String hideItems = "";
        if (sb.toString().endsWith(",")) {
            hideItems = sb.substring(0, sb.length() - 1);
        }
        bean.setHide_set(hideItems);

        MainLogic.getInstance().savaCardInfo(UserDiyCardActivity.this, bean, new ProgressSubscriber<BaseBean>(mContext, true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                setResult(RESULT_OK);
                finish();
            }
        });


    }

    /**
     * 显示内容变化
     *
     * @param position
     */
    private void fieldChanged(int position) {
        switch (mFieldAdapter.getData().get(position).getType()) {
            case 0:
                //Logo
                if (mFieldAdapter.getData().get(position).isChecked()) {
                    ivLogo.setVisibility(View.GONE);
                } else {
                    ivLogo.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                //座机
                if (mFieldAdapter.getData().get(position).isChecked()) {
                    rlPhone.setVisibility(View.GONE);
                } else {
                    rlPhone.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                //手机
                if (mFieldAdapter.getData().get(position).isChecked()) {
                    rlMobile.setVisibility(View.GONE);
                } else {
                    rlMobile.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                //邮件
                if (mFieldAdapter.getData().get(position).isChecked()) {
                    tvEmail.setVisibility(View.GONE);
                } else {
                    tvEmail.setVisibility(View.VISIBLE);
                }
                break;
            case 4:
                //地址
                if (mFieldAdapter.getData().get(position).isChecked()) {
                    rlAddress.setVisibility(View.GONE);
                } else {
                    rlAddress.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    /**
     * 切换模板
     *
     * @param position
     */
    private void changeStyle(int position) {
        initCard();
        fillData();
        formatData();
    }


    @Override
    protected void initData() {

    }

    private void formatData() {
        mFieldAdapter.getData().clear();
        mStyleAdapter.getData().clear();
        styleList.clear();
        fieldList.clear();

        try {
            org.json.JSONArray list = new org.json.JSONArray(styleArr);
            for (int i = 0; i < list.length(); i++) {
                DiyCardStyleBean bean = new DiyCardStyleBean();
                bean.setChecked(false);
                bean.setName("");
                bean.setRes(resArray[i]);
                bean.setType(i);
                if (choosedType.equals("" + i)) {
                    bean.setChecked(true);
                }
                styleList.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String hideItem = mCardBean.getData().getHide_set();
        final String[] split = hideItem.split(",");
        for (int i = 0; i < fieldArray.length; i++) {
            DiyCardStyleBean bean = new DiyCardStyleBean();
            bean.setChecked(true);
            bean.setType(i);
            bean.setName(fieldArray[i]);
            bean.setChecked(false);
            if (split != null && split.length > 0) {
                for (int j = 0; j < split.length; j++) {
                    if (split[j].equals(i + "")) {
                        bean.setChecked(true);
                    }
                }
            }
            fieldList.add(bean);
        }
        mFieldAdapter.getData().addAll(fieldList);
        for (int i = 0; i < fieldList.size(); i++) {
            fieldChanged(i);
        }
        mFieldAdapter.notifyDataSetChanged();
        mStyleAdapter.notifyDataSetChanged();
    }

    /**
     * 组成NameCard名片格式
     *
     * @param nameCard
     * @return
     */
    public String enQrCodeOneContact(List<String> nameCard) {

        StringBuilder ss = new StringBuilder();
        ss.append(String.format("BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:%s", nameCard.get(0)))
                .append("\nORG:")
                .append(nameCard.get(1))
                .append("\nTITLE:")
                .append(nameCard.get(2))
                .append("\nTEL:")
                .append(nameCard.get(3))
                .append(String.format("\nADR;WORK:%s", nameCard.get(4)))
//                .append(String.format("\nADR;HOME:%s", nameCard.get(4)))
                .append(String.format("\nTEL;WORK;VOICE:%s", nameCard.get(5)))
                .append("\nURL:")
                .append(nameCard.get(6))
                .append("\nEMAIL:")
                .append(nameCard.get(7))
                .append("\nEND:VCARD");
        return ss.toString();

    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK) {

        }
    }
}
