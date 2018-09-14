package com.hjhq.teamface.customcomponent.widget2.input;

import android.content.Intent;
import android.net.Uri;
import android.text.InputType;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.customcomponent.R;
import com.hjhq.teamface.customcomponent.widget2.base.InputCommonView;


/**
 * 超链接组件
 *
 * @author xj
 * @date 2017/8/23
 */

public class UrlInputView extends InputCommonView {

    public UrlInputView(CustomBean bean) {
        super(bean);
    }

    @Override
    public int getLayout() {
        return R.layout.custom_input_single_widget_layout;
    }

    @Override
    public void initOption() {
        etInput.setTextColor(ColorUtils.resToColor(getContext(), R.color.url_color));
        etInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        if (isDetailState()) {
            etInput.setOnClickListener(v -> {
                String url = getContent();
                if (TextUtil.isEmpty(url)) {
                    return;
                }
                if (!url.startsWith("http")) {
                    url = "http://" + url;
                }
                openWeb(url);
            });
        }
    }


    /**
     * 打开网页
     *
     * @param url
     */
    private void openWeb(final String url) {
        DialogUtils.getInstance().sureOrCancel(getContext(), "打开网页", url,
                "打开", "取消", llRoot,
                () -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, (Uri.parse(url))).addCategory(Intent.CATEGORY_BROWSABLE).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public boolean formatCheck() {
        return true;
    }
}
