package com.hjhq.teamface.common.ui.email;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.EmailBean;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.CommonModel;

import java.util.Map;


/**
 * 邮件详情
 *
 * @author Administrator
 */

public class EmailDetailFragment extends FragmentPresenter<EmailDetailFragmentDelegate, CommonModel> {


    @Override
    protected void init() {

    }

    public void setData(Object mailDetail) {
        try{
            JSONObject jsonObject = null;
            if (mailDetail instanceof JSONObject) {
                jsonObject = (JSONObject) mailDetail;
            } else if (mailDetail instanceof Map) {
                jsonObject = JSONObject.parseObject(JSON.toJSONString(mailDetail));
            }
            EmailBean emailBean = jsonObject.toJavaObject(EmailBean.class);
//            EmailBean emailBean = JSONObject.parseObject(mailDetail + "", EmailBean.class);
            viewDelegate.showData(emailBean);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
