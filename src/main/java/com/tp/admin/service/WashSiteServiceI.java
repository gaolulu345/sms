package com.tp.admin.service;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * 站点设备管理
 */
public interface WashSiteServiceI {

    /**
     * 站点操作日志
     * @param wxMiniSearch
     * @return
     */
    ApiResult siteOperationLog(WxMiniSearch wxMiniSearch);

    /**
     * 检查站点是否合法
     * @param wxMiniSearch
     * @return
     */
    TerInfoDTO terCheck(WxMiniSearch wxMiniSearch);


    /**
     * 洗车站点启动
     * @param wxMiniSearch
     * @return
     */
    ApiResult siteStart(WxMiniSearch wxMiniSearch);

    /**
     * 洗车站点状态
     * @param wxMiniSearch
     * @return
     */
    ApiResult siteState(WxMiniSearch wxMiniSearch);

    /**
     * 洗车站点复位
     * @param wxMiniSearch
     * @return
     */
    ApiResult siteReset(WxMiniSearch wxMiniSearch);

    /**
     * 洗车站点暂停
     * @param wxMiniSearch
     * @return
     */
    ApiResult siteStop(WxMiniSearch wxMiniSearch);

    /**
     * 站点上线
     * @param wxMiniSearch
     * @return
     */
    ApiResult siteOnline(WxMiniSearch wxMiniSearch);

    /**
     * 站点下线
     * @param wxMiniSearch
     * @return
     */
    ApiResult siteOffline(WxMiniSearch wxMiniSearch);

}
