package com.tp.admin.common;

public interface Constant {

    String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    String ADMIN = "COM_EMP_";

    String ORDER = "WASH_ORDER_LIST_";

    String USER = "WASH_USER_LIST_";

    String _XLSX = ".xlsx";

    String TMP_DIR = "java.io.tmpdir";

    String _XLSX_DIR = "/xlsx/";

    String SUPER_ADMIN = "SMS_ADMIN";

    String PAGES_INDEX = "/pages/index";



    interface Page {
        int DEFAULT_SIZE = 10;
        int DEFAULT_INDEX = 1;
    }


}
