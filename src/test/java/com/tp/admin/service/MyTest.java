package com.tp.admin.service;

import com.tp.admin.utils.TimeUtil;
import org.junit.Test;

public class MyTest {
    @Test
    public void test01() {
        System.out.println(TimeUtil.getFrontSeconds(60 * 3));
    }
}
