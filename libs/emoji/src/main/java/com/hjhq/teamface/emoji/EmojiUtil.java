package com.hjhq.teamface.emoji;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmojiUtil {
    private static ArrayList<Emoji> emojiList;

    public static ArrayList<Emoji> getEmojiList() {
        if (emojiList == null) {
            emojiList = generateEmojis();
        }
        return emojiList;
    }

    private static ArrayList<Emoji> generateEmojis() {
        ArrayList<Emoji> list = new ArrayList<>();
        for (int i = 0; i < EmojiResArray.length; i++) {
            Emoji emoji = new Emoji();
            emoji.setImageUri(EmojiResArray[i]);
            emoji.setContent(EmojiTextArray[i]);
            list.add(emoji);
        }
        return list;
    }


    public static final int[] EmojiResArray = {
            R.drawable.d_ciya,
            R.drawable.d_tiaopi,
            R.drawable.d_han,
            R.drawable.d_touxiao,
            R.drawable.d_baibai,
            R.drawable.d_dani,
            R.drawable.d_cahan,
            R.drawable.d_zhutou,
            R.drawable.d_meigui,
            R.drawable.d_liulei,
            R.drawable.d_kuaikule,
            R.drawable.d_xu,
            R.drawable.d_ku,
            R.drawable.d_zhuakuang,
            R.drawable.d_weiqu,
            R.drawable.d_shi,
            R.drawable.d_zhadan,
            R.drawable.d_caidao,
            R.drawable.d_keai,
            R.drawable.d_se,
            R.drawable.d_haixiu,
            R.drawable.d_deyi,
            R.drawable.d_tu,
            R.drawable.d_weixiao,
            R.drawable.d_fahuo,
            R.drawable.d_ganga,
            R.drawable.d_jingkong,
            R.drawable.d_lenghan,
            R.drawable.d_xin,
            R.drawable.d_zuichun,
            R.drawable.d_baiyan,
            R.drawable.d_aoman,
            R.drawable.d_nanguo,
            R.drawable.d_jingya,
            R.drawable.d_yiwen,
            R.drawable.d_shui,
            R.drawable.d_qinqin,
            R.drawable.d_hanxiao,
            R.drawable.d_aiqing,
            R.drawable.d_shuai,
            R.drawable.d_piezui,
            R.drawable.d_jianxiao,
            R.drawable.d_fendou,
            R.drawable.d_fadai,
            R.drawable.d_youhengheng,
            R.drawable.d_baobao,
            R.drawable.d_huaixiao,
            R.drawable.d_qieqin,
            R.drawable.d_bishi,
            R.drawable.d_yun,
            R.drawable.d_dabing,
            R.drawable.d_baituo,
            R.drawable.d_qiang,
            R.drawable.d_laji,
            R.drawable.d_woshou,
            R.drawable.d_shengli,
            R.drawable.d_baoquan,
            R.drawable.d_kuwei,
            R.drawable.d_mifan,
            R.drawable.d_dangao,
            R.drawable.d_xigua,
            R.drawable.d_pijiu,
            R.drawable.d_piaochong,
            R.drawable.f_gouyin,
            R.drawable.f_ele,
            R.drawable.f_shoushi,
            R.drawable.f_kafei,
            R.drawable.f_yueliang,
            R.drawable.f_dagger,
            R.drawable.f_shiver,
            R.drawable.f_xiaosouzhi,
            R.drawable.h_fist,
            R.drawable.h_good,
            R.drawable.h_haha,
            R.drawable.h_lai,
            R.drawable.h_ok,
            R.drawable.h_quantou,
            R.drawable.h_ruo,
            R.drawable.h_woshou,
            R.drawable.h_ye,
            R.drawable.h_zan,
            R.drawable.h_zuoyi,
            R.drawable.l_shangxin,
            R.drawable.l_xin,
            R.drawable.o_dangao,
            R.drawable.o_feiji,
            R.drawable.o_ganbei,
            R.drawable.o_huatong,
            R.drawable.o_lazhu,
            R.drawable.o_liwu,
            R.drawable.o_lvsidai,
            R.drawable.o_weibo,
            R.drawable.o_weiguan,
            R.drawable.o_yinyue,
            R.drawable.o_zhaoxiangji,
            R.drawable.o_zhong,
            R.drawable.w_fuyun,
            R.drawable.w_shachenbao,
            R.drawable.w_taiyang,
            R.drawable.w_weifeng,
            R.drawable.w_xianhua,
            R.drawable.w_xiayu,
            R.drawable.w_yueliang,
    };

    public static final String[] EmojiTextArray = {
            "[呲牙]",
            "[调皮]",
            "[汗]",
            "[偷笑]",
            "[拜拜]",
            "[打你]",
            "[擦汗]",
            "[猪头]",
            "[玫瑰]",
            "[流泪]",
            "[快哭了]",
            "[嘘]",
            "[酷]",
            "[抓狂]",
            "[委屈]",
            "[屎]",
            "[炸弹]",
            "[菜刀]",
            "[可爱]",
            "[色]",
            "[害羞]",
            "[得意]",
            "[吐]",
            "[微笑]",
            "[发火]",
            "[尴尬]",
            "[惊恐]",
            "[冷汗]",
            "[心]",
            "[嘴唇]",
            "[白眼]",
            "[傲慢]",
            "[难过]",
            "[惊讶]",
            "[疑问]",
            "[睡]",
            "[亲亲]",
            "[憨笑]",
            "[爱情]",
            "[衰]",
            "[撇嘴]",
            "[奸笑]",
            "[奋斗]",
            "[发呆]",
            "[右哼哼]",
            "[抱抱]",
            "[坏笑]",
            "[企鹅亲]",
            "[鄙视]",
            "[晕]",
            "[大兵]",
            "[拜托]",
            "[强]",
            "[垃圾]",
            "[握手]",
            "[胜利]",
            "[抱拳]",
            "[枯萎]",
            "[米饭]",
            "[蛋糕]",
            "[西瓜]",
            "[啤酒]",
            "[瓢虫]",
            "[勾引]",
            "[哦了]",
            "[手势]",
            "[咖啡]",
            "[月亮]",
            "[匕首]",
            "[发抖]",
            "[菜]",
            "[拳头]",
            "[good]",
            "[haha]",
            "[来]",
            "[OK]",
            "[拳头]",
            "[弱]",
            "[握手]",
            "[耶]",
            "[赞]",
            "[作揖]",
            "[伤心]",
            "[心]",
            "[蛋糕]",
            "[飞机]",
            "[干杯]",
            "[话筒]",
            "[蜡烛]",
            "[礼物]",
            "[绿丝带]",
            "[围脖]",
            "[围观]",
            "[音乐]",
            "[照相机]",
            "[钟]",
            "[浮云]",
            "[沙尘暴]",
            "[太阳]",
            "[微风]",
            "[鲜花]",
            "[下雨]",
            "[月亮]",
    };

    static {
        emojiList = generateEmojis();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static SpannableStringBuilder handlerEmojiText2(TextView comment, CharSequence content, Context context) throws IOException {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    //转换为Span并设置Span的大小
                    sb.setSpan(new ImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getImageUri()
                            , dip2px(context, 18), dip2px(context, 18))),
                            m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        comment.setText(sb);
        return sb;
    }

    public static void handlerEmojiText(EditText comment, String content, Context context) throws IOException {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    //转换为Span并设置Span的大小
                    sb.setSpan(new ImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getImageUri()
                            , dip2px(context, 18), dip2px(context, 18))),
                            m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        comment.setText(sb);

        comment.setSelection(sb.length());
        ((EditText) comment).setSelection(sb.length());

    }
}
