package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.ajax.ResultCode;
import com.tp.admin.common.Constant;
import com.tp.admin.config.TpProperties;
import com.tp.admin.dao.*;
import com.tp.admin.data.dto.DataTotalDTO;
import com.tp.admin.data.dto.OrderDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.dto.UploadFileDTO;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.entity.AdminTerOperatingLog;
import com.tp.admin.data.entity.Partner;
import com.tp.admin.data.entity.Refund;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.data.search.RefundSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.data.wash.WashSiteRequest;
import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;
import com.tp.admin.enums.OrderStatusEnum;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.AliyunOssManagerI;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WashSiteServiceI;
import com.tp.admin.service.WxMiniMerchantManageServiceI;
import com.tp.admin.utils.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    WashSiteServiceI washSiteService;

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TpProperties tpProperties;

    @Autowired
    TerDao terDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    PartnerDao partnerDao;

    @Autowired
    AliyunOssManagerI aliyunOssManager;

    @Autowired
    RefundDao refundDao;

    @Override
    public ApiResult moneyTotal(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getOpenId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
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
                    , endDay.toString(), terIds);
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
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        List<Integer> terIds = terDao.findRelatedTerByPartnerId(adminMerchantEmployee.getPartnerId());
        if (null != terIds && !terIds.isEmpty()) {
            wxMiniSearch.builData();
            wxMiniSearch.setIds(terIds);
            List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniSearch);
            if (null != results && !results.isEmpty()) {
                for (TerInfoDTO t : results) {
                    t.build();
                }
                int cnt = terDao.cntTerInfoSearch(wxMiniSearch);
                wxMiniSearch.setTotalCnt(cnt);
            } else {
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
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        return ApiResult.ok(dto);
    }

    @Override
    public ApiResult siteStatus(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        check(wxMiniSearch.getOpenId());
        // 基础数据不够
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteDeviceReset(HttpServletRequest request, String body) {
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        StringBuffer imgs = new StringBuffer();
        if (null != wxMiniSearch.getImgs() || wxMiniSearch.getImgs().length != 0) {
            log.info(wxMiniSearch.getImgs().toString());
            int length = wxMiniSearch.getImgs().length;
            for (int i = 0; i < length; i++) {
                imgs.append(wxMiniSearch.getImgs()[i]);
                if ((length - 1) > i) {
                    imgs.append(",");
                }
            }
        }
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        WashSiteRequest washSiteRequest = httpHelper.signInfo(wxMiniSearch.getTerId(), "", "");
        String jsonBody = new Gson().toJson(washSiteRequest);
        String result = httpHelper.sendPostByJsonData(tpProperties.getWashManageServer() + Constant.RemoteTer
                .SITE_RESET, jsonBody);
        return buildApiResult(result, dto, adminMerchantEmployee, imgs.toString(), WashTerOperatingLogTypeEnum
                .TER_RESET);
    }

    @Override
    public ApiResult siteStatusReset(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        WashSiteRequest washSiteRequest = httpHelper.signInfo(wxMiniSearch.getTerId(), "", "");
        String jsonBody = new Gson().toJson(washSiteRequest);
        String result = httpHelper.sendPostByJsonData(tpProperties.getWashManageServer() + Constant.RemoteTer
                .SITE_STATUS_RESET, jsonBody);
        return buildApiResult(result, dto, adminMerchantEmployee, "", WashTerOperatingLogTypeEnum.TER_RESET_STATE);
    }

    @Override
    public ApiResult siteOnline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        WashSiteRequest washSiteRequest = httpHelper.signInfo(wxMiniSearch.getTerId(), "", "");
        String jsonBody = new Gson().toJson(washSiteRequest);
        String result = httpHelper.sendPostByJsonData(tpProperties.getWashManageServer() + Constant.RemoteTer
                .SITE_ONLINE, jsonBody);
        return buildApiResult(result, dto, adminMerchantEmployee, "", WashTerOperatingLogTypeEnum.ONLINE);
    }

    @Override
    public ApiResult siteOffline(HttpServletRequest request, String body) {
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId() || StringUtils.isBlank(wxMiniSearch.getMsg())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        WashSiteRequest washSiteRequest = httpHelper.signInfo(wxMiniSearch.getTerId(), "", wxMiniSearch.getMsg());
        String jsonBody = new Gson().toJson(washSiteRequest);
        String result = httpHelper.sendPostByJsonData(tpProperties.getWashManageServer() + Constant.RemoteTer
                .SITE_OFFLINE, jsonBody);
        return buildApiResult(result, dto, adminMerchantEmployee, "", WashTerOperatingLogTypeEnum.NOT_ONLINE);
    }

    @Override
    public ApiResult orderListSearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getOpenId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        List<Integer> terIds = terDao.findRelatedTerByPartnerId(adminMerchantEmployee.getPartnerId());
        OrderSearch orderSearch = new OrderSearch();
        if (null != terIds && !terIds.isEmpty()) {
            wxMiniSearch.setIds(terIds);
            orderSearch.setTerIds(terIds);
            orderSearch.setPageIndex(wxMiniSearch.getPageIndex());
            orderSearch.setPageSize(wxMiniSearch.getPageSize());
            orderSearch.builData();
            List<OrderDTO> list = orderDao.listBySearch(orderSearch);
            if (null != list && !list.isEmpty()) {
                for (OrderDTO o : list) {
                    o.build();
                }
                int cnt = orderDao.cntBySearch(orderSearch);
                orderSearch.setTotalCnt(cnt);
                orderSearch.setResult(list);
            } else {
                orderSearch.setTotalCnt(0);
            }
        }
        return ApiResult.ok(new ResultTable(orderSearch));
    }

    @Override
    public ApiResult siteOperationLog(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        wxMiniSearch.builData();
        check(wxMiniSearch.getOpenId());
        return washSiteService.siteOperationLog(wxMiniSearch);
    }

    @Override
    public ApiResult uploadSitePhoto(HttpServletRequest request, MultipartFile file, String openId) {
        if (Strings.isBlank(openId)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        check(openId);
        UploadFileDTO uploadFileDTO = aliyunOssManager.uploadFileToAliyunOss(file, Constant.WxMiniMaintain.MINI_SITE_RESET_PHOTO);
        if (!uploadFileDTO.isSuccess()) {
            throw new BaseException(ExceptionCode.ALI_OSS_UPDATE_ERROR);
        }
        return ApiResult.ok(uploadFileDTO);
    }

    @Override
    public AdminMerchantEmployee check(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = adminMerchantEmployeeDao.findByWxMiniId(openId);
        if (null == adminMerchantEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        if (!adminMerchantEmployee.isEnable()) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        if (adminMerchantEmployee.isDeleted()) {
            throw new BaseException(ExceptionCode.USER_DELETE_REGISTERED);
        }
        if (adminMerchantEmployee.getPartnerId() == 0) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        Partner partner = partnerDao.findById(adminMerchantEmployee.getPartnerId());
        if (null == partner) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        return adminMerchantEmployee;
    }

    @Override
    public ApiResult merchantRefundSearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getOpenId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        List<Integer> terIds = terDao.findRelatedTerByPartnerId(adminMerchantEmployee.getPartnerId());
        RefundSearch refundSearch = null;
        List<Refund> refundList = null;
        if (null != terIds && !terIds.isEmpty()) {
            refundSearch.setTerIds(terIds);
            refundList =  refundDao.findRefundInfo(refundSearch);
        }
        return ApiResult.ok(refundList);
    }

    @Override
    public void buildTerOperationLog(TerInfoDTO terInfoDTO, AdminMerchantEmployee adminMerchantEmployee,
                                     WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum, String img, Boolean
                                             sucess) {
        String intros = adminMerchantEmployee.getName() + " 操作 " + terInfoDTO.getTitle() + washTerOperatingLogTypeEnum
                .getDesc();
        AdminTerOperatingLog adminTerOperatingLog = new
                AdminTerOperatingLog();
        adminTerOperatingLog.setMerchantId(adminMerchantEmployee.getId());
        adminTerOperatingLog.setUsername(adminMerchantEmployee.getName());
        adminTerOperatingLog.setTerId(terInfoDTO.getId());
        adminTerOperatingLog.setTitle(washTerOperatingLogTypeEnum.getDesc());
        adminTerOperatingLog.setIntros(intros);
        adminTerOperatingLog.setType(washTerOperatingLogTypeEnum.getValue());
        adminTerOperatingLog.setOpSource(AdminTerOperatingLogSourceEnum.MERCHANT.getValue());
        adminTerOperatingLog.setImgs("");
        adminTerOperatingLog.setSuccess(sucess);
        int res = adminTerOperatingLogDao.insert(adminTerOperatingLog);
        if (res == 0) {
            log.error("维保人员操作日志存储失败 {} " + adminTerOperatingLog.toString());
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }

    private final ApiResult buildApiResult(String result, TerInfoDTO dto, AdminMerchantEmployee
            adminMerchantEmployee, String img, WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum
    ) {
        ApiResult apiResult = null;
        try {
            apiResult = new Gson().fromJson(result, ApiResult.class);
            if (null == apiResult) {
                throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
            }
            if (apiResult.getCode().equals(ResultCode.SUCCESS.getCode())) {
                buildTerOperationLog(dto, adminMerchantEmployee, washTerOperatingLogTypeEnum, img, true);
            } else {
                buildTerOperationLog(dto, adminMerchantEmployee, washTerOperatingLogTypeEnum, img, false);
            }
        } catch (JsonSyntaxException ex) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        return apiResult;
    }
}
