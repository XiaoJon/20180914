package com.hjhq.teamface.oa.main.adaper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.image.ImageLoader;

import java.util.List;

/**
 * 选择名片模版
 *
 * @author Administrator
 * @date 2018/3/29
 */

public class ChooseCardTeamAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int teamCode;

    public ChooseCardTeamAdapter(List<String> list,int teamCode) {
        super(R.layout.user_item_choose_card_team, list);
        this.teamCode = teamCode;
    }

    public int getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(int teamCode) {
        this.teamCode = teamCode;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int img;
        int layoutPosition = helper.getLayoutPosition();
        switch (layoutPosition) {
            case 0:
                img = R.drawable.user_card_team_01;
                break;
            case 1:
                img = R.drawable.user_card_team_02;
                break;
            case 2:
                img = R.drawable.user_card_team_03;
                break;
            case 3:
                img = R.drawable.user_card_team_04;
                break;
            default:
                img = R.drawable.user_card_team_01;
                break;
        }
        ImageLoader.loadImage(mContext, img, helper.getView(R.id.iv_team));
        helper.getView(R.id.iv_select).setSelected(layoutPosition== teamCode);
    }


}
