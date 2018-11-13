package com.tp.admin.service;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;

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

}
