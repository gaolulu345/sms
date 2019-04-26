package com.sms.admin.utils;

import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

}
