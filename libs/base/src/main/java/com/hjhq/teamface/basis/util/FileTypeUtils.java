package com.hjhq.teamface.basis.util;

import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.hjhq.teamface.basis.R;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 文件类型工具类
 *
 * @author Administrator
 */
public class FileTypeUtils {

    private static List<String> audioFileType = new ArrayList<>();
    private static List<String> imageFileType = new ArrayList<>();
    private static List<String> documentFileType = new ArrayList<>();
    private static List<String> videoType = new ArrayList<>();

    static {
        //视频聊天
        videoType.add(".mp4");
        videoType.add("mp4");
        videoType.add(".mov");
        videoType.add("mov");
        videoType.add(".rm");
        videoType.add("rm");
        videoType.add(".rmvb");
        videoType.add("rmvb");
        videoType.add(".wmv");
        videoType.add("wmv");
        videoType.add(".avi");
        videoType.add("avi");
        videoType.add(".3gp");
        videoType.add("3gp");
        videoType.add(".mkv");
        videoType.add("mkv");
        videoType.add(".ts");
        videoType.add("ts");
        videoType.add(".flv");
        videoType.add("flv");
        //音频文件
        audioFileType.add(".mp3");
        audioFileType.add("mp3");
        audioFileType.add("amr");
        audioFileType.add(".amr");
        audioFileType.add("aac");
        audioFileType.add(".aac");
        audioFileType.add("wma");
        audioFileType.add(".wma");
        audioFileType.add("wav");
        audioFileType.add(".wav");
        //图片文件
        imageFileType.add(".png");
        imageFileType.add(".jpg");
        imageFileType.add(".jpeg");
        imageFileType.add(".gif");
        imageFileType.add(".bmp");
        imageFileType.add("png");
        imageFileType.add("jpg");
        imageFileType.add("jpeg");
        imageFileType.add("gif");
        imageFileType.add("bmp");
        //文档文件
        documentFileType.add(".doc");
        documentFileType.add(".docx");
        documentFileType.add(".ppt");
        documentFileType.add(".pptx");
        documentFileType.add(".xls");
        documentFileType.add(".xlsx");
        documentFileType.add(".pdf");
        documentFileType.add(".md");
        documentFileType.add(".java");
        documentFileType.add(".txt");
        documentFileType.add("txt");
        documentFileType.add("doc");
        documentFileType.add("docx");
        documentFileType.add("ppt");
        documentFileType.add("pptx");
        documentFileType.add("xls");
        documentFileType.add("xlsx");
        documentFileType.add("pdf");
        documentFileType.add("md");
        documentFileType.add("java");
    }

    /**
     * 根据传入的类型判断是否是图片
     *
     * @param fileType
     * @return
     */
    public static boolean isImage(String fileType) {
        boolean yesOrNo = false;
        if (TextUtils.isEmpty(fileType)) {
            yesOrNo = false;
        } else {
            String lowwerCase = fileType.toLowerCase();
            if (imageFileType.contains(lowwerCase)) {
                yesOrNo = true;
            }
        }

        return yesOrNo;
    }

    /**
     * 根据传入的类型判断是否是文档
     *
     * @param fileType
     * @return
     */
    public static boolean isDocument(String fileType) {
        boolean yesOrNo = false;
        if (TextUtils.isEmpty(fileType)) {
            yesOrNo = false;
        } else {
            String lowwerCase = fileType.toLowerCase();
            if (documentFileType.contains(lowwerCase)) {
                yesOrNo = true;
            }
        }

        return yesOrNo;
    }

    /**
     * 根据传入的类型判断是否是音频
     *
     * @param fileType
     * @return
     */
    public static boolean isAudio(String fileType) {
        boolean yesOrNo = false;
        if (TextUtils.isEmpty(fileType)) {
            yesOrNo = false;
        } else {
            String lowwerCase = fileType.toLowerCase();
            if (audioFileType.contains(lowwerCase)) {
                yesOrNo = true;
            }
        }

        return yesOrNo;
    }

    /**
     * 根据传入的类型判断是否是视频
     *
     * @param fileType
     * @return
     */
    public static boolean isVideo(String fileType) {
        boolean yesOrNo = false;
        if (TextUtils.isEmpty(fileType)) {
            yesOrNo = false;
        } else {
            String lowwerCase = fileType.toLowerCase();
            if (videoType.contains(lowwerCase)) {
                yesOrNo = true;
            }
        }

        return yesOrNo;
    }

    /**
     * 得到后缀名
     *
     * @param file 文件
     * @return 后缀名
     */
    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        return getSuffix(fileName);
    }

    /**
     * 得到后缀名
     *
     * @param fileName 文件名
     * @return 后缀名
     */
    private static String getSuffix(String fileName) {
        if (fileName == null || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    /**
     * 得到文件类型
     *
     * @param file 文件
     * @return 文件类型
     */
    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null && !TextUtils.isEmpty(type)) {
            return type;
        }
        return "file/*";
    }

    /**
     * 得到文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型
     */
    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "text/plain";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }

    public static int getFileTypeIcon(String fileType) {
        int res;
        if (TextUtils.isEmpty(fileType)) {
            res = R.drawable.icon_unknow;
            return res;
        }
        String type = fileType.toLowerCase().replace(".", "");

        if (isImage(type)) {
            res = R.drawable.icon_jpg;
            return res;
        } else if (isAudio(type)) {
            res = R.drawable.icon_mp3;
            return res;
        } else if (isVideo(type)) {
            res = R.drawable.jmui_video;
            return res;
        }
        switch (type) {
            case "txt":
                res = R.drawable.icon_txt;
                break;
            case "apk":
                res = R.drawable.icon_unknow;
                break;
            case "ps":
            case "psd":
                res = R.drawable.icon_ps;
                break;
            case "cdr":
                res = R.drawable.icon_cdr;
                break;
            case "pdf":
                res = R.drawable.icon_pdf;
                break;
            case "ai":
                res = R.drawable.icon_ai;
                break;
            case "dwg":
                res = R.drawable.icon_dwg;
                break;
            case "zip":
                res = R.drawable.icon_zip;
                break;
            case "ppt":
            case "pptx":
                res = R.drawable.icon_ppt;
                break;
            case "doc":
            case "docx":
                res = R.drawable.icon_doc;
                break;
            case "xls":
            case "xlsx":
                res = R.drawable.icon_xls;
                break;
            default:
                res = R.drawable.icon_unknow;
                break;
        }
        return res;
    }

    public static String getFileName(String url) {
        String filename = "";
        boolean isok = false;
        // 从UrlConnection中获取文件名称
        try {
            URL myURL = new URL(url);

            URLConnection conn = myURL.openConnection();
            if (conn == null) {
                return null;
            }
            Map<String, List<String>> hf = conn.getHeaderFields();
            Log.e("长度", conn.getContentLength() + " ");
            Log.e("类型", conn.getContentType() + " ");
            if (hf == null) {
                return null;
            }
            Set<String> key = hf.keySet();
            if (key == null) {
                return null;
            }

            for (String skey : key) {
                List<String> values = hf.get(skey);
                for (String value : values) {
                    String result;
                    try {
                        result = new String(value.getBytes("ISO-8859-1"), "UTF-8");
                        Log.e("skey", skey + " ");
                        Log.e("result", result + " ");
                        int location = result.indexOf("filename");
                        if (location >= 0) {
                            result = result.substring(location
                                    + "filename".length());
                            filename = result
                                    .substring(result.indexOf("=") + 1);
                            isok = true;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }// ISO-8859-1 UTF-8 gb2312
                }
                if (isok) {
                    break;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 从路径中获取
        if (filename == null || "".equals(filename)) {
            filename = url.substring(url.lastIndexOf("/") + 1);
        }
        return filename;
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        String name = "";
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                name = filename.substring(dot + 1);
            }
        }
        return name;
    }

}
