package com.tp.admin.manage;

import com.tp.admin.data.wash.TerDeviceRequest;
import com.tp.admin.data.wash.WashSiteRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Http-辅助接口
 */
public interface HttpHelperI {

    String jsonBody(HttpServletRequest request);

    String sendPostByJsonData(String url , String requestBody);

    WashSiteRequest signInfo(Integer deviceId, String orderId, String msg);

    TerDeviceRequest signTerInfo(Integer deviceId, List<String> pictures, String msg, Integer terId);

}
