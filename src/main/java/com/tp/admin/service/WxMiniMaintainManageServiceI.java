package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信小程序-维保管理接口
 */
public interface WxMiniMaintainManageServiceI {

    /**
     * 区域（地市级）
     * @param request
     * @return
     */
    ApiResult region(HttpServletRequest request);

    /**
     * 洗车站点查询
     * @param request
     * @return
     */
    ApiResult siteListSearch(HttpServletRequest request);

    /**
     * 站点信息
     * @param request
     * @return
     */
    ApiResult siteInfo(HttpServletRequest request);

    /**
     * 站点上线
     * @param request
     * @return
     */
    ApiResult siteOnline(HttpServletRequest request);

    /**
     * 站点下线
     * @param request
     * @return
     */
    ApiResult siteOffline(HttpServletRequest request);

    /**
     * 网点设备复位
     * @param request
     * @return
     */
    ApiResult siteDeviceReset(HttpServletRequest request);


    /**
     * 网点状态复位
     * @param request
     * @return
     */
    ApiResult siteStatusReset(HttpServletRequest request);

    /**
     * 站点操作日志
     * @param request
     * @return
     */
    ApiResult siteOperationLog(HttpServletRequest request);

    /**
     * 检查用户
     * @param openId
     * @return
     */
    AdminMaintionEmployee check(String openId);

    /**
     * 构建操作日志
     * @param terInfoDTO
     * @param adminMaintionEmployee
     * @param washTerOperatingLogTypeEnum
     */
    void buildTerOperationLog(TerInfoDTO terInfoDTO, AdminMaintionEmployee adminMaintionEmployee,
                  WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum , Boolean sucess);

    TerInfoDTO terCheck(WxMiniSearch wxMiniSearch);

}
