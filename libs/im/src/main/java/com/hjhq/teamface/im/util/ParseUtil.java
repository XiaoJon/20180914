package com.hjhq.teamface.im.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hjhq.teamface.basis.bean.FieldInfoBean;
import com.hjhq.teamface.basis.bean.ImMessage;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.database.PushMessage;
import com.hjhq.teamface.basis.database.SocketMessage;
import com.hjhq.teamface.basis.util.BytesTransUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.basis.constants.IMState;
import com.hjhq.teamface.im.db.DBManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Administrator
 * @description 解析Socket数据工具类
 */
public class ParseUtil {
    private static String TAG = "ParseUtil";
    private static Conversation conversation = new Conversation();
    private static long showTime = 0L;


    //int 转换成byte数组
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    //short 转换成byte数组
    public static byte[] toLH(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    // byte 数组与 long 的相互转换
    public static byte[] longToBytes(long n) {
        byte[] b = new byte[8];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        b[4] = (byte) (n >> 32 & 0xff);
        b[5] = (byte) (n >> 40 & 0xff);
        b[6] = (byte) (n >> 48 & 0xff);
        b[7] = (byte) (n >> 56 & 0xff);
        return b;
    }

    // byte转换成byte数组(其实,可不要)
    public static byte[] toLH(byte n) {
        byte[] b = new byte[1];
        b[0] = n;
        return b;

    }

    //三个BYTE值转换成一个BYTE值(6:1:1)
    public static byte[] toLH(byte a, byte b, byte c) {
        byte[] bb = new byte[1];
        bb[0] = (byte) (a + (b << 6) + (c << 7));
        return bb;
    }

    //一个BYTE值转换成三个BYTE值(6:1:1)
    public static byte[] toLH1(byte b) {
        byte[] bb = new byte[3];

        bb[0] = (byte) (b & ~(3 << 6));
        bb[1] = (byte) ((b & (1 << 6)) >> 6);
        bb[2] = (byte) ((b & (1 << 7)) >> 7);
        return bb;
    }

    //byte数组转换成INT
    public static int lBytesToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; ++i) {
            if (b[3 - i] >= 0) {
                s = s + b[3 - i];
            } else {
                s = s + 256 + b[3 - i];
            }

            s = s * 256;
        }

        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }

        return s;
    }

    //byte数组转换成SHORT
    public static short bytes2Short(byte[] b) {
        int s = 0;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }
        s = s * 256;

        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }

        short result = (short) s;
        return result;
    }

    /**
     * 将字节数组转为long
     *
     * @param b
     * @return
     */
    public static long bytes2Long(byte[] b) {
        long s = 0;
        try {
            for (int i = 0; i < 7; ++i) {
                if (b[7 - i] >= 0) {
                    s = s + b[7 - i];
                } else {
                    s = s + 256 + b[7 - i];
                }

                s = s * 256;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        return s;
    }

    /**
     * 将byte转换成数组
     *
     * @param b       源数据
     * @param start   开始位置
     * @param len     长度
     * @param charset 编码格式 默认UTF-8
     * @return
     */
    public static String byte2String(byte[] b, int start, int len, String charset) {
        if (charset == null || charset.trim().length() == 0) {
            charset = "UTF-8";
        }
        int count = 0;
        for (int i = 0; i < len; i++) {

            if (b[i + start] == 0) {
                break;
            }
            count++;

        }
        byte[] temp = new byte[count];
        for (int i = 0; i < count; i++) {

            temp[i] = b[i + start];
        }

        try {
            return new String(temp, charset);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static SocketMessage saveMessage(byte[] byteBuffer) {
        if (byteBuffer == null) {
            return null;
        }
        byte[] head = Arrays.copyOfRange(byteBuffer, 0, 49);
        byte[] body = Arrays.copyOfRange(byteBuffer, 49, byteBuffer.length);
        SocketMessage bean = new SocketMessage();
        int ucFlag = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 12, 13));

        //公司id
        bean.setCompanyId(IM.getInstance().getCompanyId());

        //设置我的id
        bean.setMyId(IM.getInstance().getImId());

        //类似账户
        bean.setOneselfIMID(bytes2Long(Arrays.copyOfRange(head, 0, 8)) + "");

        //命令类型
        bean.setUsCmdID(BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 8, 10)));


        //版本号
        bean.setUcVer(BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 10, 11)));

        //设备类型
        bean.setUcDeviceType(BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 11, 12)));

        //发送
        bean.setUcFlag(0);

        //当前群人数
        bean.setGroupMemeberNum(IM.getInstance().getGroupNumberNum());


        //服务器时间
        long serverTime = bytes2Long(Arrays.copyOfRange(head, 13, 21));
        bean.setServerTimes(serverTime);
        if (serverTime > 0L) {
            bean.setServerTimes(serverTime);
            //发送状态
            bean.setSendState(1);
        } else {
            if (System.currentTimeMillis() - IM.getInstance().getServerTimeOffsetValue() < 1514260636548L) {
                bean.setServerTimes(System.currentTimeMillis());
            } else {
                bean.setServerTimes(System.currentTimeMillis() - IM.getInstance().getServerTimeOffsetValue());
            }
            //发送状态
            bean.setSendState(6);
        }

        //客户端时间
        bean.setClientTimes(bytes2Long(Arrays.copyOfRange(head, 37, 45)));
        // bean.setClientTimes(System.currentTimeMillis());

        //发送人id
        bean.setSenderID(bytes2Long(Arrays.copyOfRange(head, 21, 29)) + "");


        //接收人id
        bean.setReceiverID(bytes2Long(Arrays.copyOfRange(head, 29, 37)) + "");

        //随机码
        bean.setRand(BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 45, 49)));

        //消息id
        bean.setMsgID(Long.parseLong(bean.getClientTimes() + "" + bean.getRand() + ""));

        //解析消息体
        switch (bean.getUsCmdID()) {

            case MsgConstant.IM_PERSONAL_CHAT_CMD:
            case MsgConstant.IM_TEAM_CHAT_CMD:
                try {
                    String result1 = new String(body, "UTF-8");
                    JSONObject jsonObject = new JSONObject(result1);
                    bean.setMsgContent(jsonObject.optString("msg"));
                    bean.setMsgType(jsonObject.optInt("type"));
                    bean.setFileName(jsonObject.optString("fileName"));
                    bean.setFileType(jsonObject.optString("fileType"));
                    bean.setFileId(jsonObject.optString("fileId"));
                    bean.setFileUrl(jsonObject.optString("fileUrl"));
                    bean.setFileSize(jsonObject.optInt("fileSize"));
                    bean.setFileLocalPath(jsonObject.optString("filePath"));
                    bean.setLongitude(jsonObject.optString("longitude"));
                    bean.setLatitude(jsonObject.optString("latitude"));
                    bean.setDuration(jsonObject.optInt("duration"));
                    bean.setLocation(jsonObject.optString("location"));
                    bean.setContent(result1);
                    bean.setSenderAvatar(jsonObject.optString("senderAvatar"));
                    bean.setSenderName(jsonObject.optString("senderName"));
                    bean.setAllPeoples(jsonObject.optString("allPeoples"));
                    bean.setReadPeoples("");
                    if (bean.getUsCmdID() == MsgConstant.IM_PERSONAL_CHAT_CMD) {
                        bean.setChatType(MsgConstant.SINGLE);
                    } else if (bean.getUsCmdID() == MsgConstant.IM_TEAM_CHAT_CMD) {
                        bean.setChatType(MsgConstant.GROUP);
                    }
                    //已读
                    bean.setIsRead(true);

                    //保存或更新会话
                    conversation = new Conversation();
                    conversation.setCompanyId(IM.getInstance().getCompanyId());
                    if (!TextUtils.isEmpty(IM.getInstance().getImId()) && IM.getInstance().getImId().equals(bean.getSenderID())) {
                        conversation.setReceiverId(bean.getReceiverID());
                    } else if (!TextUtils.isEmpty(IM.getInstance().getImId()) && !IM.getInstance().getImId().equals(bean.getSenderID())) {
                        conversation.setReceiverId(bean.getSenderID());
                    }
                    conversation.setSenderId(bean.getSenderID());
                    conversation.setTargetId(bean.getReceiverID() + "");
                    conversation.setUnreadMsgCount(1);
                    conversation.setLastMessageType(bean.getMsgType());
                    conversation.setConversationId(jsonObject.optLong("chatId"));
                    bean.setConversationId(conversation.getConversationId());
                    conversation.setLastMsgDate(bean.getServerTimes());
                    conversation.setNotEmpty(true);
                    conversation.setOneselfIMID(IM.getInstance().getImId());
                    conversation.setLatestMessage(bean.getMsgContent());
                    conversation.setIsHide(0);
                    if (bean.getChatType() == MsgConstant.SINGLE) {
                        conversation.setTitle(bean.getSenderName());
                        conversation.setSenderAvatarUrl(bean.getSenderAvatar());
                    }
                    if (bean.getUsCmdID() == MsgConstant.IM_PERSONAL_CHAT_CMD) {
                        conversation.setConversationType(MsgConstant.SINGLE);
                    } else if (bean.getUsCmdID() == MsgConstant.IM_TEAM_CHAT_CMD) {
                        conversation.setConversationType(MsgConstant.GROUP);
                    } else if (bean.getUsCmdID() == MsgConstant.IM_USER_DEFINED_PERSONAL_CMD) {
                        conversation.setConversationType(MsgConstant.SELF_DEFINED);
                    } else if (bean.getUsCmdID() == MsgConstant.IM_USER_DEFINED_TEAM_CMD) {
                        conversation.setConversationType(MsgConstant.SELF_DEFINED);
                    }

                    DBManager.getInstance().saveOrReplace(conversation);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    bean.setContent("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            //自定义消息发送到个人
            case MsgConstant.IM_USER_DEFINED_PERSONAL_CMD:

                break;
            //自定义消息发送到群
            case MsgConstant.IM_USER_DEFINED_TEAM_CMD:

                break;
            //服务器返回错误
            case MsgConstant.IM_ERROR_INFO_CMD:

                break;
            default:

                break;
        }
        if (bean.getUsCmdID() == 5 || bean.getUsCmdID() == 6 || bean.getUsCmdID() == 7 || bean.getUsCmdID() == 8) {
            if (ucFlag != 5) {
                boolean b = DBManager.getInstance().saveOrReplace(bean);
                IM.getInstance().writeFileToSDCard(new Gson().toJson(bean));

                //消息处理
                if (b) {
                    EventUtil.sendEvent(new ImMessage(bean.getUsCmdID(), MsgConstant.MSG_RESULT, bean));
                    return bean;
                }
            }

        }
        return null;
    }


    /**
     * 解析Socket数据
     *
     * @param byteBuffer
     */

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static SocketMessage parseMessage(ByteBuffer byteBuffer) {
        return parseMessage(byteBuffer.array());
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static void parseUrl(ByteBuffer byteBuffer) {
        parseUrl(byteBuffer.array());
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static void parseUrl(byte[] byteBuffer) {
        byte[] head = Arrays.copyOfRange(byteBuffer, 0, 21);

        int flag = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 0, 4));
        int state = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 4, 5));
        long imId = BytesTransUtil.getInstance().getLong(Arrays.copyOfRange(head, 5, 13));
        long serverTime = BytesTransUtil.getInstance().getLong(Arrays.copyOfRange(head, 13, 21));


        byte[] body = Arrays.copyOfRange(byteBuffer, 21, byteBuffer.length);
        try {
            if (MsgConstant.LL_IDENTITY == flag) {
                String string = new String(body, "UTF-8");
                if (!TextUtils.isEmpty(string)) {
                    String uri = "wss://" + string;
                    SPHelper.setImSocketUri(uri);
                    IMState.setImConnectLlOk(true);
                    IM.getInstance().login();
                } else {
                    IMState.setImConnectLlOk(false);
                }
            } else {
                LogUtil.e("负载均衡", "返回状态异常");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析Socket消息
     *
     * @param byteBuffer
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public static SocketMessage parseMessage(byte[] byteBuffer) {
        if (byteBuffer == null || byteBuffer.length < 49) {
            return null;
        }

        byte[] head = Arrays.copyOfRange(byteBuffer, 0, 49);
        byte[] body;
        SocketMessage bean = new SocketMessage();
        //1实时消息,3离线消息,4最后一条离线消息
        int messageType = 1;
        //公司id
        bean.setCompanyId(IM.getInstance().getCompanyId());
        //设置我的id
        bean.setMyId(IM.getInstance().getImId());

        //类似账户
        bean.setOneselfIMID(bytes2Long(Arrays.copyOfRange(head, 0, 8)) + "");


        //命令类型
        bean.setUsCmdID(BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 8, 10)));


        //版本号
        bean.setUcVer(BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 10, 11)));

        //设备类型
        bean.setUcDeviceType(BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 11, 12)));
        int ucFlag = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 12, 13));

        //服务器时间
        bean.setServerTimes(bytes2Long(Arrays.copyOfRange(head, 13, 21)));

        //更新客户端与服务器之间的时间差
        IM.getInstance().setServerTimeOffsetValue(System.currentTimeMillis() - bean.getServerTimes());
        LogUtil.e("时间差值", IM.getInstance().getServerTimeOffsetValue() + "");


        //发送人id
        bean.setSenderID(bytes2Long(Arrays.copyOfRange(head, 21, 29)) + "");

        //判断自己发送,只能用oneselfid,已读未读会改变此字段
        if (!TextUtils.isEmpty(bean.getOneselfIMID())
                && bean.getOneselfIMID().equals(IM.getInstance().getImId())
                && !TextUtils.isEmpty(bean.getSenderID())
                && bean.getSenderID().equals(bean.getOneselfIMID())) {

            if (bean.getUcDeviceType() == MsgConstant.DEVICE_TYPE && bean.getUsCmdID() == 5) {
                //收到自己发送的消息,丢弃掉
                //LogUtil.e("单聊消息", "收到自己发送的消息,丢弃掉");
                return null;
            } else if (bean.getUcDeviceType() == MsgConstant.DEVICE_TYPE && bean.getUsCmdID() == 6) {
                //收到自己发送的消息,丢弃掉
                //LogUtil.e("群聊消息", "收到自己发送的消息,丢弃掉");
                return null;
            }
            bean.setUcFlag(0);
            bean.setSendState(1);
        } else {
            //1表示是接收到的消息
            bean.setSendState(1);
            bean.setUcFlag(1);
        }
        //接收人id
        bean.setReceiverID(bytes2Long(Arrays.copyOfRange(head, 29, 37)) + "");
        //会话id

        //客户端时间
        bean.setClientTimes(bytes2Long(Arrays.copyOfRange(head, 37, 45)));
        //随机码
        bean.setRand(BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 45, 49)));
        //离线或实时消息
        messageType = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 12, 13));
        //自定义消息id
        bean.setMsgID(Long.parseLong(bean.getClientTimes() + "" + bean.getRand()));

        //解析消息体
        switch (bean.getUsCmdID()) {
            //登录
            case MsgConstant.IM_LOG_IN_CMD:
                bean.setResultCode(BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(byteBuffer, 49, 53)));
                parseLoginMessage(bean);
                break;
            //退出
            case MsgConstant.IM_LOG_OUT_CMD:

                break;
            //单聊,群聊消息
            case MsgConstant.IM_PERSONAL_CHAT_CMD:
            case MsgConstant.IM_TEAM_CHAT_CMD:
                body = Arrays.copyOfRange(byteBuffer, 49, byteBuffer.length);
                parseMessageBody(body, bean, false);
                break;
            //自定义消息发送到个人  推送
            case MsgConstant.IM_USER_DEFINED_PERSONAL_CMD:
                //自定义消息发送到群
            case MsgConstant.IM_USER_DEFINED_TEAM_CMD:
                parsePushMessage(byteBuffer, messageType, bean.getUsCmdID(), bean.getUcFlag());
                break;
            //历史消息
            case MsgConstant.IM_PULL_HISTORY_MESSAGE:
                processHistoryMessage(ucFlag, byteBuffer, bean);
                return null;
            case MsgConstant.IM_TEAM_HISTORY_MESSAGE:
                //历史个人消息
            case MsgConstant.IM_PERSONAL_HISTORY_MESSAGE:

            case MsgConstant.IM_PULL_MESSAGE_FINISH:
                break;
            //服务器返回错误
            case MsgConstant.IM_ERROR_INFO_CMD:
                IM.getInstance().writeFileToSDCard(new Gson().toJson(bean) + "错误信息");
                bean.setResultCode(BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(byteBuffer, 49, 53)));
                body = Arrays.copyOfRange(byteBuffer, 49, byteBuffer.length);
                byte[] errorInfo = new byte[0];
                int errorCodde = 0;
                try {
                    byte[] errorCode = new byte[4];
                    byte[] infoLength = new byte[4];
                    errorInfo = new byte[body.length - 8];
                    System.arraycopy(body, 0, errorCode, 0, 4);
                    errorCodde = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(errorCode, 0, 4));
                    LogUtil.e("错误17", "错误码==  " + errorCodde + "");
                    System.arraycopy(body, 4, infoLength, 0, 4);
                    System.arraycopy(body, 8, errorInfo, 0, errorInfo.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String errorInfoStr = new String(errorInfo, "UTF-8");
                    LogUtil.e("错误17", errorInfoStr);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                switch (errorCodde) {
                    case MsgConstant.IM_USER_LOGIN_ELSEWHERE:
                        //账号重复登录,需要退出,在MyApplication.java处理
                        IMState.setImOnlineState(false);
                        IM.getInstance().sendBoardcast(MsgConstant.LOGIN_ON_OTHER_DEVICES);
                        break;
                    case MsgConstant.IM_ERROR_NOT_LOGIN:
                        //没有登录
                        IMState.setImOnlineState(false);
                        if (Constants.USE_LOAD_LEVELING) {
                            IM.getInstance().getLlServerUrl();
                        } else {
                            IM.getInstance().login();
                        }
                        break;
                    case MsgConstant.IM_LOGIN_ERR_IMID_QUERY_FAIL:
                        //企信账号不存在
                        IM.getInstance().sendBoardcast(MsgConstant.IM_ACCOUNT_NOT_EXIST);
                        break;
                    default:
                        break;
                }
                break;
            default:

                break;
        }

        //收到消息
        if (bean.getUsCmdID() == 5 || bean.getUsCmdID() == 6 || bean.getUsCmdID() == 7 || bean.getUsCmdID() == 8) {
            if (bean.getUcFlag() == 1) {
                responseAck(bean.getUsCmdID(), head);
                DBManager.getInstance().saveOrReplace(conversation);
            }
            //保存聊天数据
            boolean b = DBManager.getInstance().saveOrReplace(bean);
            IM.getInstance().writeFileToSDCard(new Gson().toJson(bean));
        } else if (bean.getUsCmdID() == 9 || bean.getUsCmdID() == 10 || bean.getUsCmdID() == 11 || bean.getUsCmdID() == 12) {
            //收到发送成功ACK
            boolean b = DBManager.getInstance().updateMessage(bean);
            IM.getInstance().writeFileToSDCard(new Gson().toJson(bean) + "更新发送状态");
        } else if (bean.getUsCmdID() == MsgConstant.IM_PERSONSL_RESPONSE_READ_CMD || bean.getUsCmdID() == MsgConstant.IM_TEAM_RESPONSE_READ_CMD) {
            // 收到已读
            if (!IM.getInstance().getImId().equals(bean.getOneselfIMID())) {
                DBManager.getInstance().updateMessageReadState(bean.getOneselfIMID(), bean.getMsgID());
                EventBusUtils.sendEvent(new ImMessage(bean.getUsCmdID(), MsgConstant.READ_MESSAGE_TAG, bean.getReceiverID()));
                return null;
            }

        } else if (bean.getUsCmdID() == MsgConstant.IM_PERSONAL_HISTORY_MESSAGE || bean.getUsCmdID() == MsgConstant.IM_TEAM_HISTORY_MESSAGE) {
            //获取历史消息
            if (bean.getSenderID() == IM.getInstance().getImId()) {
                bean.setUcFlag(0);
            } else {
                bean.setUcFlag(1);
            }
            if (bean.getUsCmdID() == MsgConstant.IM_PERSONAL_HISTORY_MESSAGE) {
                bean.setUsCmdID(MsgConstant.SINGLE);
                bean.setSendState(MsgConstant.SINGLE_READ);
            }
            if (bean.getUsCmdID() == MsgConstant.IM_TEAM_HISTORY_MESSAGE) {
                bean.setUsCmdID(MsgConstant.GROUP);
                bean.setSendState(MsgConstant.ALL_READ);
            }
            DBManager.getInstance().updateMessageReadState(bean);
            IM.getInstance().writeFileToSDCard(new Gson().toJson(bean) + "更新已读");

            return bean;
        } else if (bean.getUsCmdID() == MsgConstant.IM_RECALL_PERSONAL_MSG_OK
                || bean.getUsCmdID() == MsgConstant.IM_RECALL_PERSONAL_MSG
                || bean.getUsCmdID() == MsgConstant.IM_RECALL_TEAM_MSG
                || bean.getUsCmdID() == MsgConstant.IM_RECAL_TEAM_MSG_OK
                ) {
            //撤回消息
            DBManager.getInstance().deleteMessageAndNotify(bean.getMsgID());
            return bean;

        }

        //LogUtil.e("完整数据结果=    ", new Gson().toJson(bean));
        //消息处理
        switch (messageType) {
            case 1:
                //在线消息
                EventUtil.sendEvent(new ImMessage(bean.getUsCmdID(), MsgConstant.MSG_RESULT, bean));
                break;
            case 3:

                break;
            case 4:
                //离线消息接收完成
                EventUtil.sendEvent(new ImMessage(bean.getUsCmdID(), MsgConstant.IM_PULL_OFFLINE_MSG_FINISH, bean));
                break;
            default:
                break;
        }

        return bean;
    }

    /**
     * 处理历史消息
     *
     * @param byteBuffer
     * @param bean
     */
    private static void processHistoryMessage(int ucFlag, byte[] byteBuffer, SocketMessage bean) {

        if (showTime == 0L) {
            bean.setShowTime(true);
            showTime = bean.getServerTimes() + MsgConstant.SHOW_TIME_INTERVAL;
        } else {
            if (bean.getServerTimes() > showTime) {
                bean.setShowTime(true);
                showTime = bean.getServerTimes() + MsgConstant.SHOW_TIME_INTERVAL;
            } else {
                bean.setShowTime(false);
            }
        }
        byte[] body;
        byte[] pageInfo;
        body = Arrays.copyOfRange(byteBuffer, 54, byteBuffer.length);
        pageInfo = Arrays.copyOfRange(byteBuffer, 49, 54);
        int type = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(pageInfo, 0, 1));
        int totalNum = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(pageInfo, 1, 3));
        int currentNum = BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(pageInfo, 3, 5));
        if (type == 1) {
            bean.setUsCmdID(MsgConstant.IM_PERSONAL_CHAT_CMD);
            bean.setSendState(3);
            bean.setRead(true);
            bean.setIsRead(true);
        }
        if (type == 2) {
            bean.setUsCmdID(MsgConstant.IM_TEAM_CHAT_CMD);
            bean.setSendState(7);
            bean.setRead(true);
            bean.setIsRead(true);
        }

        boolean pullFinish = false;
        if (ucFlag == 4 || totalNum == currentNum || currentNum == 20) {
            pullFinish = true;

        }
        if (IM.getInstance().getImId().equals(bean.getOneselfIMID())) {
            bean.setUcFlag(0);
        } else {
            bean.setUcFlag(1);
        }
        parseMessageBody(body, bean, true);
        if (bean.getUsCmdID() == 5 || bean.getUsCmdID() == 6) {
            //保存聊天数据
            boolean b = DBManager.getInstance().saveOrReplace(bean);
        }
        if (pullFinish) {
            noticePullHistoryMessageFinished(1, bean);
        }
    }

    /**
     * 拉取历史消息完成通知
     *
     * @param bean
     */
    private static void noticePullHistoryMessageFinished(int currentNum, SocketMessage bean) {
        showTime = 0L;
        EventBus.getDefault().post(new ImMessage(currentNum, MsgConstant.IM_PULL_HISTORY_MSG_FINISH, bean));
    }

    /**
     * 解析消息体
     *
     * @param body
     * @param bean
     */
    private static void parseMessageBody(byte[] body, SocketMessage bean, boolean isHistoryMessage) {
        try {
            String result1 = new String(body, "UTF-8");
            if (TextUtils.isEmpty(result1)) {
                return;
            }
            if (bean.getUsCmdID() == MsgConstant.IM_PERSONAL_CHAT_CMD) {
                bean.setChatType(MsgConstant.SINGLE);

            } else if (bean.getUsCmdID() == MsgConstant.IM_TEAM_CHAT_CMD) {
                bean.setChatType(MsgConstant.GROUP);
            }
            JSONObject jsonObject = new JSONObject(result1);
            bean.setMsgContent(jsonObject.optString("msg"));
            bean.setMsgType(jsonObject.optInt("type"));
            bean.setFileName(jsonObject.optString("fileName"));
            bean.setFileType(jsonObject.optString("fileType"));
            bean.setFileId(jsonObject.optString("fileId"));
            bean.setFileUrl(jsonObject.optString("fileUrl"));
            bean.setVideoUrl(jsonObject.optString("videoUrl"));
            bean.setFileSize(jsonObject.optInt("fileSize"));
            bean.setFileLocalPath(jsonObject.optString("filePath"));
            bean.setLongitude(jsonObject.optString("longitude"));
            bean.setLatitude(jsonObject.optString("latitude"));
            bean.setDuration(jsonObject.optInt("duration"));
            bean.setLocation(jsonObject.optString("location"));
            bean.setContent(result1);
            bean.setSenderAvatar(jsonObject.optString("senderAvatar"));
            bean.setSenderName(jsonObject.optString("senderName"));
            bean.setConversationId(TextUtil.parseLong(jsonObject.optString("chatId")));
            bean.setRead(true);
            if (!isHistoryMessage) {
                bean.setSendState(1);
                if (bean.getUsCmdID() == MsgConstant.IM_PERSONAL_CHAT_CMD) {
                    bean.setChatType(MsgConstant.SINGLE);
                } else if (bean.getUsCmdID() == MsgConstant.IM_TEAM_CHAT_CMD) {
                    bean.setChatType(MsgConstant.GROUP);
                }

                if (IM.getInstance().getImId().equals(bean.getOneselfIMID()) &&
                        IM.getInstance().getImId().equals(bean.getSenderID())) {
                    bean.setIsRead(true);
                } else {
                    bean.setIsRead(false);
                }
                if (bean.getChatType() == MsgConstant.GROUP) {
                    JSONObject jb = new JSONObject(bean.getContent());
                    JSONArray atList = jb.getJSONArray("atList");
                    if (atList != null && atList.length() > 0) {
                        for (int i = 0; i < atList.length(); i++) {
                            JSONObject jo = atList.getJSONObject(i);
                            //去掉自己发送的@所有人消息提醒
                            if (jo != null && SPHelper.getUserId().equals(jo.optString("id"))) {
                                bean.setIsAtMe(true);
                            }
                            if (jo != null && "0".equals(jo.optString("id"))) {
                                bean.setIsAtAll(true);
                            }
                        }
                    }

                }
            }
            if (isHistoryMessage) {
                //保存或更新会话
                conversation = new Conversation();
                conversation.setCompanyId(IM.getInstance().getCompanyId());
                conversation.setReceiverId(bean.getReceiverID());
                conversation.setSenderId(bean.getSenderID());
                if (bean.getChatType() == MsgConstant.SINGLE) {
                    if (!TextUtils.isEmpty(bean.getOneselfIMID()) &&
                            bean.getOneselfIMID().equals(IM.getInstance().getImId())) {
                        conversation.setTargetId(bean.getReceiverID() + "");
                    } else {
                        conversation.setTargetId(bean.getSenderID() + "");
                    }
                } else if (bean.getChatType() == MsgConstant.GROUP) {
                    conversation.setTargetId(bean.getReceiverID() + "");
                }
                conversation.setUnreadMsgCount(1);
                conversation.setLastMessageType(bean.getMsgType());
                conversation.setConversationId(jsonObject.optLong("chatId"));
                bean.setConversationId(conversation.getConversationId());
                conversation.setLastMsgDate(bean.getServerTimes());
                conversation.setNotEmpty(true);
                conversation.setOneselfIMID(IM.getInstance().getImId());
                conversation.setLatestMessage(bean.getMsgContent());
                conversation.setIsHide(0);
                if (bean.getChatType() == MsgConstant.SINGLE) {
                    conversation.setTitle(bean.getSenderName());
                    conversation.setSenderAvatarUrl(bean.getSenderAvatar());
                }
                if (bean.getUsCmdID() == MsgConstant.IM_PERSONAL_CHAT_CMD) {
                    conversation.setConversationType(MsgConstant.SINGLE);
                } else if (bean.getUsCmdID() == MsgConstant.IM_TEAM_CHAT_CMD) {
                    conversation.setConversationType(MsgConstant.GROUP);
                } else if (bean.getUsCmdID() == MsgConstant.IM_USER_DEFINED_PERSONAL_CMD) {
                    conversation.setConversationType(MsgConstant.SELF_DEFINED);
                } else if (bean.getUsCmdID() == MsgConstant.IM_USER_DEFINED_TEAM_CMD) {
                    conversation.setConversationType(MsgConstant.SELF_DEFINED);
                }
                DBManager.getInstance().saveOrReplace(conversation);
            }


        } catch (UnsupportedEncodingException e) {

            bean.setContent("");
        } catch (JSONException e) {

        }
    }

    /**
     * 解析推送消息
     *
     * @param byteBuffer
     * @param messageType
     * @param cmd
     * @param usFlag
     * @return
     */
    private static PushMessage parsePushMessage(byte[] byteBuffer, int messageType, int cmd, int usFlag) {
        byte[] head = Arrays.copyOfRange(byteBuffer, 0, 49);
        byte[] body = Arrays.copyOfRange(byteBuffer, 49, byteBuffer.length);
        try {
            PushMessage pm = new PushMessage();
            String pushResult = new String(body, "UTF-8");
            //Log.e("推送内容  =    ", pushResult);
            JSONObject jo = new JSONObject(pushResult);
            pm.setCompanyId(SPHelper.getCompanyId());
            pm.setMyId(SPHelper.getUserId());
            pm.setId(TextUtil.parseLong(jo.optString("id")));
            pm.setData_id(jo.optString("data_id"));
            pm.setType(jo.optString("type"));
            pm.setStyle(jo.optString("style"));
            pm.setPush_content(jo.optString("push_content"));
            pm.setAssistant_id(jo.optString("assistant_id"));
            pm.setBean_name(jo.optString("bean_name"));
            pm.setBean_name_chinese(jo.optString("bean_name_chinese"));
            pm.setTitle(jo.optString("title"));
            pm.setAssistant_name(jo.optString("assistant_name"));
            pm.setSender_name(jo.optString("sender_name"));
            pm.setIcon_type(jo.optString("icon_type"));
            pm.setIcon_color(jo.optString("icon_color"));
            pm.setIcon_url(jo.optString("icon_url"));
            //用于查找原来的推送
            pm.setIm_apr_id(jo.optString("im_apr_id"));
            //审批相关使用
            pm.setParam_fields(jo.optString("param_fields"));
            pm.setRead_status("0");
            pm.setGroup_id(jo.optString("group_id"));
            String fieldInfoString = jo.optString("field_info");
            pm.setOrginFieldInfo(fieldInfoString);
            if (!TextUtils.isEmpty(fieldInfoString)) {
                JSONArray arr = new JSONArray(fieldInfoString);
                if (arr != null) {
                    List<FieldInfoBean> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        FieldInfoBean fieldBean = new FieldInfoBean();
                        fieldBean.setField_label(obj.optString("field_label"));
                        fieldBean.setField_value(obj.optString("field_value"));
                        fieldBean.setId(obj.optString("id"));
                        fieldBean.setType(obj.optString("type"));
                        list.add(fieldBean);
                    }
                    String stringValue = com.hjhq.teamface.basis.util.ParseUtil.getStringValue(list);
                    pm.setFieldInfo(stringValue);
                }
            }
            pm.setCreate_time((System.currentTimeMillis() - IM.getInstance().getServerTimeOffsetValue()) + "");
            // DBManager.getInstance().saveOrReplace(pm);
            IM.getInstance().writeFileToSDCard(new Gson().toJson(pm) + "推送内容");

            responseAck(cmd, head);

            //丢弃离线推送?
                  /*  if (SPUtils.getLong(IM.getInstance().getContext(), MsgConstant.IM_LOGIN_TIME) > TextUtil.parseLong(pm.getCreate_time())) {
                        return null;
                    }
                    if (IM.getInstance().getServerTime() > TextUtil.parseLong(pm.getCreate_time()) + MsgConstant.RECALL_TIME) {
                        return null;
                    }*/
            if (messageType != 1) {
                return null;
            }
            switch (pm.getType()) {
                case EventConstant.TYPE_ORGANIZATIONAL_AND_PERSONNEL_CHANGES:
                    //人员与组织架构变更推送
                    EventBusUtils.sendEvent(new MessageBean(0, EventConstant.TYPE_ORGANIZATIONAL_AND_PERSONNEL_CHANGES, null));
                    return null;
                case EventConstant.TYPE_APPLICATION_DEL:
                    //应用删除
                    EventBusUtils.sendEvent(new ImMessage(0, EventConstant.TYPE_APPLICATION_DEL, pm));
                    return null;
                case EventConstant.TYPE_BE_FORCED_OFFLINE_BY_SYSTEM:
                    //被系统强制离线
                    IMState.setImOnlineState(false);
                    IM.getInstance().sendBoardcast(MsgConstant.BE_OFFLINE_BY_SYSTEM);
                    return null;
                case EventConstant.TYPE_FORCE_OFFLINE_BY_PC_CLIENT:
                    //被PC端强制离线
                    IMState.setImOnlineState(false);
                    IM.getInstance().sendBoardcast(MsgConstant.BE_OFFLINE_BY_PC_CLIENT);
                    return null;
                case EventConstant.TYPE_APPLICATION_UPDATE:
                    //应用修改
                    pm.setTitle(jo.optString("application_name"));
                    EventBusUtils.sendEvent(new ImMessage(0, EventConstant.TYPE_APPLICATION_UPDATE, pm));
                    return null;
                case EventConstant.TYPE_MODULE_UPDATE:
                    //模块修改
                    pm.setTitle(jo.optString("module_name"));
                    EventBusUtils.sendEvent(new ImMessage(0, EventConstant.TYPE_MODULE_UPDATE, pm));
                    return null;
                case EventConstant.TYPE_ASSISTANT_VISIBILE_STATE:
                    //小助手显示/隐藏状态变更
                    EventBusUtils.sendEvent(new ImMessage(0, EventConstant.TYPE_ASSISTANT_VISIBILE_STATE, pm));
                    return null;
               /* case EventConstant.TYPE_ASSISTANT_NAME_CHANGED:
                    //小助手名称变更
                    pm.setTitle(jo.optString("application_name"));
                    EventBusUtils.sendEvent(new ImMessage(-1, MsgConstant.RECEIVE_ASSISTANT_NAME_CHANGE_PUSH_MESSAGE, pm));
                    return null;*/
                case MsgConstant.TYPE_MEMO:
                    //备忘录推送
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.SHOW_PUSH_MESSAGE, pm));
                    //LogUtil.e("备忘录推送", new Gson().toJson(pm) + "备忘录");
                    DBManager.getInstance().saveOrReplace(pm);
                    return null;
                case MsgConstant.TYPE_REMOVE_MEMBER:
                case MsgConstant.TYPE_QUIT_GROUP:
                    if (MsgConstant.IM_USER_DEFINED_PERSONAL_CMD == cmd) {
                        //我被移出了群
                        if (messageType == 1) {
                            EventBus.getDefault().post(new ImMessage(1, MsgConstant.SHOW_PUSH_MESSAGE, pm));
                            EventBus.getDefault().post(new ImMessage(1, MsgConstant.RECEIVE_REMOVE_FROM_GROUP_PUSH_MESSAGE, pm.getGroup_id()));
                            DBManager.getInstance().deleteMessageByConversationId(TextUtil.parseLong(pm.getGroup_id()));
                        }
                    }
                    if (MsgConstant.IM_USER_DEFINED_TEAM_CMD == cmd) {
                        //别人被移出了群或群信息发生变化
                        EventBus.getDefault().post(new ImMessage(1, MsgConstant.RECEIVE_GROUP_MEMBER_CHANGE_PUSH_MESSAGE, pm.getGroup_id()));
                        return null;
                    }
                    break;
                case MsgConstant.TYPE_TRANSFER_GROUP:
                    //群被转让
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.RECEIVE_GROUP_ADMIN_CHANGE_PUSH_MESSAGE, pm.getGroup_id()));
                    return null;
                case MsgConstant.TYPE_ADD_MEMBER:
                case MsgConstant.TYPE_GROUP_INFO_CHANGED:
                    //群成员及群信息变化推送
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.RECEIVE_GROUP_MEMBER_CHANGE_PUSH_MESSAGE, pm.getGroup_id()));
                    // EventBus.getDefault().post(new ImMessage(1, MsgConstant.RECEIVE_GROUP_NAME_CHANGE_PUSH_MESSAGE, pm.getGroup_id()));
                    return null;
                case MsgConstant.TYPE_GROUP_OPERATION:
                    //解散群推送
                    if (messageType == 1) {
                        EventBus.getDefault().post(new ImMessage(1, MsgConstant.SHOW_PUSH_MESSAGE, pm));
                        EventBus.getDefault().post(new ImMessage(1, MsgConstant.RECEIVE_RELEASE_GROUP_PUSH_MESSAGE, pm.getGroup_id()));
                        DBManager.getInstance().deleteMessageByConversationId(TextUtil.parseLong(pm.getGroup_id()));
                        DBManager.getInstance().saveOrReplace(pm);
                    }
                    return null;
                case MsgConstant.TYPE_TOP_OR_NOT_TOP:
                    //置顶与取消置顶
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.TYPE_TOP_OR_NOT_TOP, pm.getAssistant_id()));
                    return null;
                case MsgConstant.TYPE_NOTIFY_OR_NOT_NOTIFY:
                    //免打扰与取消免打扰
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.TYPE_NOTIFY_OR_NOT_NOTIFY, pm.getAssistant_id()));
                    return null;
                case MsgConstant.TYPE_MARK_ALL_ITEM_READ:
                    //标记全部已读
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.TYPE_MARK_ALL_ITEM_READ, pm.getAssistant_id()));
                    return null;
                case MsgConstant.TYPE_MARK_ONE_ITEM_READ:
                    //标记一条推送为已读
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.TYPE_MARK_ONE_ITEM_READ, pm));
                    return null;
                case MsgConstant.TYPE_VIEW_READED_MESSAGE:
                    //只查看未读
                    EventBus.getDefault().post(new ImMessage(TextUtil.parseInt(pm.getPush_content()), MsgConstant.TYPE_VIEW_READED_MESSAGE, pm.getAssistant_id()));
                    return null;
                case MsgConstant.TYPE_FRIEND_CIRCLE:
                    //企业圈推送
                case MsgConstant.TYPE_FROM_FILE_LIB:
                    //文件库推送
                case MsgConstant.TYPE_APPROVE:
                    //审批推送
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.SHOW_PUSH_MESSAGE, pm));
                    DBManager.getInstance().saveOrReplace(pm);
                    return null;
                case MsgConstant.TYPE_APPROVE_OPERATION:
                    //审批操作(更新数据库)
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.TYPE_APPROVE_OPERATION, pm));
                    return null;
                case MsgConstant.TYPE_PROJECT_TASK:
                case MsgConstant.TYPE_PERSONAL_TASK:
                    //项目任务&个人任务
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.SHOW_PUSH_MESSAGE, pm));
                    DBManager.getInstance().saveOrReplace(pm);
                    return null;
                //审批推送
                case MsgConstant.TYPE_SELF_DEFINE:
                    //自定义模块推送
                case MsgConstant.TYPE_AT_ME:
                    //@推送
                case MsgConstant.TYPE_EMAIL:
                    //邮件推送
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.SHOW_PUSH_MESSAGE, pm));

                    DBManager.getInstance().saveOrReplace(pm);
                    return null;
                case MsgConstant.TYPE_FLOW:
                    //流程自动化推送
                    EventBus.getDefault().post(new ImMessage(1, MsgConstant.SHOW_PUSH_MESSAGE, pm));
                    DBManager.getInstance().saveOrReplace(pm);
                    return null;
                default:
                    break;
            }

            if (usFlag == 1 || usFlag == 3 || usFlag == 4) {
                //在线与离线推送
                responseAck(cmd, head);
                conversation = new Conversation();
                conversation.setConversationType(MsgConstant.SELF_DEFINED);
                //此字段用于标记助手类型
                conversation.setTotalMsgNum(TextUtil.parseInt(pm.getType()));
                long cId = 0;
                try {
                    cId = Long.parseLong(pm.getAssistant_id());
                    cId = cId + MsgConstant.DEFAULT_VALUE;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                conversation.setConversationId(cId);
                conversation.setCompanyId(IM.getInstance().getCompanyId());
                conversation.setMyId(IM.getInstance().getImId());
                conversation.setLastMessageType(MsgConstant.NOTIFICATION);
                conversation.setLatestMessage(pm.getPush_content());
                conversation.setLastMsgDate(System.currentTimeMillis() - IM.getInstance().getServerTimeOffsetValue());
                //更新数据库
                DBManager.getInstance().updatePushConversation(pm);
            }
            return pm;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析登录返回码
     *
     * @param bean
     */
    private static void parseLoginMessage(SocketMessage bean) {
        switch (bean.getResultCode()) {
            case MsgConstant.IM_LOGIN_SUCCESSS:
                EventBusUtils.sendEvent(new ImMessage(0, MsgConstant.IM_LOGIN_SUCCESSS_TAG, null));
                IMState.setImOnlineState(true);
                SPUtils.setLong(IM.getInstance().getContext(), MsgConstant.IM_LOGIN_TIME, bean.getServerTimes());
                //判断是否需要处理消息同步问题
                Log.e("登录成功", SPHelper.getImei());
                Log.e("登录成功", new Gson().toJson(bean));
                updateLocalMessage(bean);
                break;
            default:
                break;
        }

    }

    /**
     * 更新本地消息
     *
     * @param bean
     */
    private static void updateLocalMessage(SocketMessage bean) {
        if (!SPHelper.getImei().equals(bean.getSenderID()) || IMState.isImIsFirstTimeUse()) {
            EventBusUtils.sendEvent(new ImMessage(0, MsgConstant.IM_NEED_UPDATE_LOCAL_DATA, true));
        } else {
            EventBusUtils.sendEvent(new ImMessage(0, MsgConstant.IM_NEED_UPDATE_LOCAL_DATA, false));
        }
    }

    /**
     * 发送ack回应
     *
     * @param cmd
     * @param head
     */
    private static void responseAck(int cmd, byte[] head) {

        //发送Ack回应消息

        int ackCmd = 0;
        switch (cmd) {
            case MsgConstant.IM_PERSONAL_CHAT_CMD:
                ackCmd = MsgConstant.IM_ACK_CHAT_PERSONAL_CMD;
                break;
            case MsgConstant.IM_TEAM_CHAT_CMD:
                ackCmd = MsgConstant.IM_ACK_CHAT_TEAM_CMD;
                break;
            case MsgConstant.IM_USER_DEFINED_PERSONAL_CMD:
                ackCmd = MsgConstant.IM_ACK_USER_DEFINED_PERSONAL_CMD;
                break;
            case MsgConstant.IM_USER_DEFINED_TEAM_CMD:
                ackCmd = MsgConstant.IM_ACK_USER_DEFINED_TEAM_CMD;
                break;
            case MsgConstant.IM_ERROR_INFO_CMD:
                //socket断开后需要重新登录
                if (BytesTransUtil.getInstance().getInt(Arrays.copyOfRange(head, 49, 53)) == MsgConstant.IM_ERROR_SOCKET_DISAFFINITY) {
                    if (Constants.USE_LOAD_LEVELING) {
                        IM.getInstance().getLlServerUrl();
                    } else {
                        IM.getInstance().login();
                    }
                }
                break;
            default:
                break;
        }
        if (ackCmd != 0) {
            byte[] cmdHead = IM.getInstance().setCmdHead(IM.getInstance().getImIdLong(), ackCmd, 1, 1, MsgConstant.DEVICE_TYPE);
            byte[] ack = IM.getInstance().mergeRequestInfo(
                    Arrays.copyOfRange(cmdHead, 0, 10),
                    Arrays.copyOfRange(head, 10, 11),
                    Arrays.copyOfRange(cmdHead, 11, 12),
                    Arrays.copyOfRange(head, 12, head.length)
            );
            IM.getInstance().sendAckMessage(ack);
        }

    }

}