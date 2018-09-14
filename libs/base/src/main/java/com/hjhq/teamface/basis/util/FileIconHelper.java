package com.hjhq.teamface.basis.util;

import android.text.TextUtils;

import com.hjhq.teamface.basis.R;

public class FileIconHelper {

    public static int getFileIcon(String type) {
        int icon = R.drawable.icon_unknow;
        if (TextUtils.isEmpty(type)) {
            return icon;
        }
        type = type.toLowerCase();
        if (type.startsWith(".")) {
            type = type.replace(".", "");
        }
        switch (type) {
            case "doc":
            case "docx":
                icon = R.drawable.icon_doc;
                break;
            case "ai":
                icon = R.drawable.icon_ai;
                break;
            case "cdr":
                icon = R.drawable.icon_cdr;
                break;
            case "dwg":
                icon = R.drawable.icon_dwg;
                break;
            case "mp3":
            case "amr":
                icon = R.drawable.icon_mp3;
                break;
            case "pdf":
                icon = R.drawable.icon_pdf;
                break;
            case "ppt":
            case "pptx":
                icon = R.drawable.icon_ppt;
                break;
            case "ps":
                icon = R.drawable.icon_ps;
                break;
            case "txt":
                icon = R.drawable.icon_txt;
                break;
            case "xls":
            case "xlsx":
                icon = R.drawable.icon_xls;
                break;
            case "zip":
                icon = R.drawable.icon_zip;
                break;
            case "jpg":
            case "jpeg":
            case "tif":
            case "gif":
            case "bmp":
            case "png":
                icon = R.drawable.icon_default;
                break;
            default: {
                if (FileTypeUtils.isVideo(type)) {
                    icon = R.drawable.jmui_video;
                } else if (FileTypeUtils.isDocument(type)) {
                    icon = R.drawable.icon_text;
                } else if (FileTypeUtils.isAudio(type)) {
                    icon = R.drawable.icon_mp3;
                }
            }
        }
        return icon;
    }

    public static int getAssistantIcon(int type,String beanName) {
        int res = 0;


        return res;
    }
}
