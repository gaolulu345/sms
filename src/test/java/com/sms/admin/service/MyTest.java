package com.sms.admin.service;

import com.sms.admin.utils.TimeUtil;
import org.junit.Test;

public class MyTest {
    @Test
    public void test01() {
        System.out.println(TimeUtil.getFrontSeconds(60 * 3));
        System.out.println(Math.random());
    }
}
