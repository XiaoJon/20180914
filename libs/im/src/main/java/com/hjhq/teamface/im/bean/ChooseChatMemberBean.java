package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.SortToken;
import com.hjhq.teamface.common.bean.Contact;

/**
 * Created by Administrator on 2017/4/17.
 */

public class ChooseChatMemberBean extends Contact {

    /**
     * 显示数据拼音的首字母
     */
    public String sortLetters;

    public SortToken sortToken = new SortToken();
    public String name;
    public String job_name;
}
