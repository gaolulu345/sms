package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminMerchantEmployeeDao;
import com.tp.admin.dao.AdminTerOperatingLogDao;
import com.tp.admin.dao.OrderDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.DataTotalDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.entity.AdminTerOperatingLog;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.search.RangeSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;
import com.tp.admin.enums.OrderStatusEnum;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WashSiteServiceI;
import com.tp.admin.service.WxMiniMerchantManageServiceI;
import com.tp.admin.utils.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class WxMiniMerchantManageServiceImpl implements WxMiniMerchantManageServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    AdminTerOperatingLogDao adminTerOperatingLogDao;

    @Autowired
    AdminMerchantEmployeeDao adminMerchantEmployeeDao;

    @Autowired
    WashSiteServiceI  washSiteService;

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TerDao terDao;

    @Autowired
    OrderDao orderDao;

    @Override
    public ApiResult moneyTotal(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getOpenId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        Long orderTotal = 0L;
        Long sevenDaymoneyTotal = 0L;
        Long oneDayMoneyTotal = 0L;
        // 当天开始时间
        Date beginDay = TimeUtil.getDayBegin();
        // 当天结束时间
        Date endDay = TimeUtil.getDayEnd();
        // 30天前的时间。
        Date begin30Day = TimeUtil.getFrontDay(endDay, 29);
        // 七天前的时间
        Date sevenDays = TimeUtil.getFrontDay(endDay, 6);
        // 近30天订单数
        List<Integer> terIds = terDao.findRelatedTerByPartnerId(adminMerchantEmployee.getPartnerId());
        if (null != terIds && !terIds.isEmpty()) {
            orderTotal = orderDao.orderTatal(OrderStatusEnum.ASK_CHECK.getValue(), TimeUtil.getDayStartTime(begin30Day).toString()
                    , endDay.toString(),terIds);
            if (null == orderTotal) {
                orderTotal = 0L;
            }
            // 七天完成订单金额总和
            sevenDaymoneyTotal = orderDao.moneyTatal(OrderStatusEnum.ASK_CHECK.getValue(), TimeUtil.getDayStartTime(sevenDays).toString
                    (), endDay
                    .toString(), terIds);
            if (null == sevenDaymoneyTotal) {
                sevenDaymoneyTotal = 0L;
            }
            // 今天完成订单金额总和
            oneDayMoneyTotal = orderDao.moneyTatal(OrderStatusEnum.ASK_CHECK.getValue(), beginDay.toString(), endDay.toString()
                    , terIds);
            if (null == oneDayMoneyTotal) {
                oneDayMoneyTotal = 0L;
            }
        }
        DataTotalDTO dataTotalDTO = new DataTotalDTO();
        dataTotalDTO.setOrderTotal(orderTotal);
        dataTotalDTO.setSevenDayMoneyTotal(sevenDaymoneyTotal);
        dataTotalDTO.setOneDayMoneyTotal(oneDayMoneyTotal);
        return ApiResult.ok(dataTotalDTO);
    }

    @Override
    public ApiResult siteListSearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getOpenId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        List<Integer> terIds = terDao.findRelatedTerByPartnerId(adminMerchantEmployee.getPartnerId());
        if (null != terIds && !terIds.isEmpty()) {
            wxMiniSearch.setIds(terIds);
            List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniSearch);
            if (null != results && !results.isEmpty()) {
                for (TerInfoDTO t : results){
                    t.build();
                }
                int cnt = terDao.cntTerInfoSearch(wxMiniSearch);
                wxMiniSearch.setTotalCnt(cnt);
            }else {
                wxMiniSearch.setTotalCnt(0);
            }
            wxMiniSearch.setResult(results);
        }
        return ApiResult.ok(new ResultTable(wxMiniSearch));
    }

    @Override
    public ApiResult siteInfo(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        return ApiResult.ok(dto);
    }

    @Override
    public ApiResult siteStatus(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteOnline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        if (dto.isOnline()) {
            throw new BaseException(ExceptionCode.REPEAT_OPERATION , "该网点已经上线");
        }
        int res = terDao.updateOnline(wxMiniSearch.getTerId());
        if (res == 0) {
            buildTerOperationLog(dto , adminMerchantEmployee , WashTerOperatingLogTypeEnum.ONLINE,false);
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        buildTerOperationLog(dto , adminMerchantEmployee , WashTerOperatingLogTypeEnum.ONLINE,true);
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteOffline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId() || StringUtils.isBlank(wxMiniSearch.getMsg()) ) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        if (!dto.isOnline()) {
            throw new BaseException(ExceptionCode.REPEAT_OPERATION , "该网点已经下线");
        }
        int res = terDao.updateOffline(wxMiniSearch.getTerId(),wxMiniSearch.getMsg());
        if (res == 0) {
            buildTerOperationLog(dto , adminMerchantEmployee , WashTerOperatingLogTypeEnum.NOT_ONLINE,false);
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        buildTerOperationLog(dto , adminMerchantEmployee , WashTerOperatingLogTypeEnum.NOT_ONLINE,true);
        return ApiResult.ok();
    }

    @Override
    public ApiResult orderListSearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        wxMiniSearch.builData();
        check(wxMiniSearch.getOpenId());
        return washSiteService.siteOperationLog(wxMiniSearch);
    }

    @Override
    public ApiResult siteOperationLog(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        wxMiniSearch.builData();
        check(wxMiniSearch.getOpenId());
        return washSiteService.siteOperationLog(wxMiniSearch);
    }

    @Override
    public AdminMerchantEmployee check(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = adminMerchantEmployeeDao.findByWxMiniId(openId);
        if (null == adminMerchantEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        return adminMerchantEmployee;
    }

    @Override
    public void buildTerOperationLog(TerInfoDTO terInfoDTO, AdminMerchantEmployee adminMerchantEmployee,
                                     WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum, Boolean sucess) {
        String intros = adminMerchantEmployee.getName() + " 操作 " + terInfoDTO.getTitle() + washTerOperatingLogTypeEnum
                .getDesc();
        AdminTerOperatingLog adminTerOperatingLog = new
                AdminTerOperatingLog();
        adminTerOperatingLog.setEmployeeId(adminMerchantEmployee.getId());
        adminTerOperatingLog.setUsername(adminMerchantEmployee.getName());
        adminTerOperatingLog.setTerId(terInfoDTO.getId());
        adminTerOperatingLog.setTitle(washTerOperatingLogTypeEnum.getDesc());
        adminTerOperatingLog.setIntros(intros);
        adminTerOperatingLog.setType(washTerOperatingLogTypeEnum.getValue());
        adminTerOperatingLog.setOpSource(AdminTerOperatingLogSourceEnum.MERCHANT.getValue());
        adminTerOperatingLog.setSucess(sucess);
        int res = adminTerOperatingLogDao.insert(adminTerOperatingLog);
        if (res == 0) {
            log.error("维保人员操作日志存储失败 {} " + adminTerOperatingLog.toString());
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }
}
