package com.sms.admin.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义密码匹配
 */
public class AutoPasswordEncoder implements PasswordEncoder {

    private static PasswordEncoder INSTANCE = null;

    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
    }

    public static PasswordEncoder getInstance() {
        if (null == INSTANCE) {
            synchronized (AutoPasswordEncoder.class){
                if (null == INSTANCE) {
                    INSTANCE = new AutoPasswordEncoder();
                }
            }
        }
        return INSTANCE;
    }

    private AutoPasswordEncoder() {
    }
}
