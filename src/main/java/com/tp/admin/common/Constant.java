package com.tp.admin.common;

public interface Constant {

    String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    String ADMIN = "员工列表_";

    String WASH_ORDER = "洗车订单列表_";

    String WASH_REFUND = "洗车退款申请_";

    String WASH_USER = "洗车用户_";

    String _XLSX = ".xlsx";

    String TMP_DIR = "java.io.tmpdir";

    String _XLSX_DIR = "/xlsx/";

    public interface Page {
        static int DEFAULT_SIZE = 10;
        static int DEFAULT_INDEX = 1;
    }

}
