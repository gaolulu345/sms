package com.tp.admin.manage;

import javax.servlet.http.HttpServletRequest;

/**
 * Http-辅助接口
 */
public interface HttpHelperI {

    String jsonBody(HttpServletRequest request);

    String sendPostByJsonData(String url , String requestBody);

}
