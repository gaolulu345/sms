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

    interface RemoteTer {
        String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM4lO/cpA8MRHTFwwDTxkCpqlxZ/6bi+fjOJ2lW4CeQh6TYyp0SqkZo/p5ep+M1aMS0JzO9E02OBzEG+V6m9Fkc6ET2K0kjeSvot4P4E1GYdScXyvOPokEcbM95Gr3K+gSBDxv7sc/X1xhJgR5kXe1f60woyYynTIkH0NoN8jzsRAgMBAAECgYEAg6kSSIRgi7ilfYs2p1nVKtITF2Kw4rZ/oekcknzNjHAAJAZsAJY0EmEvZ9U+O+Gr4MkOo47AuCARVbf9A0LqM4Oea9oxFBsFK3ZbSrzoIO4lladrz5XZqUAY1sDO+BqB6D+BPEZ+lfQe0NlQF03eDehtPLgCPce2ONW+zy7YFCUCQQD05VGDQ/tLf+uA0JUCPpmuCvGU0LYE9ijCxgEFWePAjI3Xe2GuZEKmVzRSYEJE4z8RLh5JxLMh5hWjG9ZQ2ZrvAkEA134c5KIyXlo2iYy00tZ+yho9Bl/aL8zvr/4FUUyAT4Aw4By9+JuxwKL9GUFk9pXAB/d+WzQjH2wIuLZkTlyJ/wJAa3r1snG0qacj2e4xZ8HHuv3slw6exECxFQLH0exoq8pEOm1ZKquwIMcnHHxIuWku9cBS5Ldikx8jIeoGvocymwJAQM7+5YwaVeIP0OJuZ5J241Vs0QP23LVnyKg+9Spq1H4bNOuv/prkpKjgMb5HHRoAfOTkLY3KmmKlZNgF5sNSBQJAXg9+88h7A3H7mgci4k9uX1CKY4WaxpStGTZBn261OEyDF7XeNuQYqVVOfV/DZL0PM5WNo2nAcdB/Qbe6eykqEQ==";
    }

    interface Page {
        int DEFAULT_SIZE = 10;
        int DEFAULT_INDEX = 1;
    }

    interface WxMiniMaintain{
        String APP_ID = "wxc76b3b03cc234f1c";
        String APP_SECRET  = "47fcec6231f6322c9ba084a593126dcd";
        String TEMPLATE_ID = "gFk-jx-YMRg1UZrb4Z9OvEn5427L9SzOyLHdSQSBexU";

    }

    interface WxMiniMerchant{
        String APP_ID = "wxfb7dff8a45d33764";
        String APP_SECRET  = "eea031dda83b4cbe2a01ced18d122614";
    }



}
