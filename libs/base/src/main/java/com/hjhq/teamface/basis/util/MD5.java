package com.hjhq.teamface.basis.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * MD5加密  16位
     *
     * @param str 要加密的内容
     */
    public static String md5Encode16(String str) {
        return md5Encode(str).substring(8, 24);
    }


    /**
     * MD5加密
     *
     * @param str 原始文本
     * @return 加密后的文本
     */
    public static String md5Encode(String str) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(str.getBytes());
            return toHexString(algorithm.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 密码加密
     *
     * @param password 原始文本
     * @return 加密后的文本
     */
    public static String encodePasswordOrigin(String password) {
        String str1 = md5Encode(password + "hjhq2017Teamface").toLowerCase();
        String str2 = md5Encode(str1).toLowerCase();
        return str2;
    }
}
