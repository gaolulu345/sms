package com.tp.admin.common;

public interface Constant {

    String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    String ADMIN = "COM_EMP_";

    String WASH_ORDER = "WASH_ORDER_LIST_";

    String WASH_REFUND = "WASH_REFUNA_LIST_";

    String WASH_USER = "WASH_USER_LIST_";

    String _XLSX = ".xlsx";

    String TMP_DIR = "java.io.tmpdir";

    String _XLSX_DIR = "/xlsx/";

    String SUPER_ADMIN = "TP_AUTO";

    String PAGES_INDEX = "/pages/index";

    interface Page {
        int DEFAULT_SIZE = 10;
        int DEFAULT_INDEX = 1;
    }

    interface WxMiniMaintain{
        String APP_ID = "wxc76b3b03cc234f1c";
        String APP_SECRET  = "47fcec6231f6322c9ba084a593126dcd";

    }

}
