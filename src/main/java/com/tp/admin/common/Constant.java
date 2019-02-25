package com.tp.admin.common;

public interface Constant {

    String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    String ADMIN = "COM_EMP_";

    String WASH_ORDER = "WASH_ORDER_LIST_";

    String WASH_REFUND = "WASH_REFUNA_LIST_";

    String WASH_USER = "WASH_USER_LIST_";

    String TER_DEVICE = "TER_DEVICE_LIST";

    String _XLSX = ".xlsx";

    String TMP_DIR = "java.io.tmpdir";

    String _XLSX_DIR = "/xlsx/";

    String SUPER_ADMIN = "TP_AUTO";

    String PAGES_INDEX = "/pages/index";

    Integer FAULT_INTERRUPT = 60 * 3;

    interface RemoteTer {
        String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDOJTv3KQPDER0xcMA08ZAqapcWf+m4vn4zidpVuAnkIek2MqdEqpGaP6eXqfjNWjEtCczvRNNjgcxBvlepvRZHOhE9itJI3kr6LeD+BNRmHUnF8rzj6JBHGzPeRq9yvoEgQ8b+7HP19cYSYEeZF3tX+tMKMmMp0yJB9DaDfI87EQIDAQAB";

        String SITE_START = "/api/open/site/start";
        String SITE_STATE = "/api/open/site/state";
        String SITE_RESET = "/api/open/site/reset";
        String SITE_STATUS_RESET = "/api/open/site/status/reset";
        String SITE_STOP = "/api/open/site/stop";
        String SITE_ONLINE = "/api/open/site/online";
        String SITE_OFFLINE = "/api/open/site/offline";
        String SITE_ONLINE_START = "/api/open/site/online/start";
        String RATATION_PICTURE_PUSH = "/api/open/device/push/ratation/picture";
    }

    interface Page {
        int DEFAULT_SIZE = 10;
        int DEFAULT_INDEX = 1;
    }

    interface WxMiniMaintain {
        String APP_ID = "wxc76b3b03cc234f1c";
        String APP_SECRET = "47fcec6231f6322c9ba084a593126dcd";
        String TEMPLATE_ID = "gFk-jx-YMRg1UZrb4Z9OvEn5427L9SzOyLHdSQSBexU";
        String MINI_SITE_RESET_PHOTO = "mini/site/reset";
    }

    interface WxMiniMerchant {
        String APP_ID = "wxfb7dff8a45d33764";
        String APP_SECRET = "eea031dda83b4cbe2a01ced18d122614";
        String TEMPLATE_ID = "G9T6fXsWUbwgev80dv6lB3gNaxcH-CFsfUr0aYuHdto";
    }


}
