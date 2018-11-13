package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface WxMiniMerchantManageServiceI {

    /**
     * 登录用户信息
     * @param request
     * @return
     */
    ApiResult userInfo(HttpServletRequest request);

    /**
     * 相关网点金额统计
     * @param request
     * @return
     */
    ApiResult moneyTotal(HttpServletRequest request);

    /**
     * 相关网点列表查询
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
     * 站点洗车机运行状态
     * @param request
     * @return
     */
    ApiResult siteStatus(HttpServletRequest request);

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
     * 相关订单列表查询
     * @param request
     * @return
     */
    ApiResult orderListSearch(HttpServletRequest request);

    /**
     * 站点操作日志
     * @param request
     * @return
     */
    ApiResult siteOperationLog(HttpServletRequest request);

    void buildTerOperationLog(TerInfoDTO terInfoDTO, AdminMerchantEmployee adminMerchantEmployee,
                              WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum , Boolean sucess);

}
