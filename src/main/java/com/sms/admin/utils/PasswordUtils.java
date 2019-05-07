package com.sms.admin.utils;

import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {

    public static String defaultPassword() {
        // 暂时采用MD5 32位编码
        String newPwd = "123456";
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(newPwd.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        return result;
    }

    /*public static String base64En(String value) {
        byte[] str = new byte[256];
        try {
            str = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encode(str).toString();
    }

    public static String base64DE(String value) {
        return Base64.decode(value).toString();
    }*/

    //下面两个方法是使用Java8中java.util中提供的Base64
    public static String base64En(String value) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] str = new byte[256];
        try {
            str = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoder.encodeToString(str);
    }

    public static String base64DE(String value) {
        Base64.Decoder decoder = Base64.getDecoder();
        String res = null;
        try {
            res = new String(decoder.decode(value), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }

}
