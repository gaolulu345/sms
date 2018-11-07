package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminMaintionEmployeeDao;
import com.tp.admin.dao.AdminMaintionEmployeeLogTerOperatingDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.entity.AdminMaintionEmployeeLogTerOperating;
import com.tp.admin.data.parameter.WxMiniAuthDTO;
import com.tp.admin.data.parameter.WxMiniTerSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WxMiniMaintainAuthServiceI;
import com.tp.admin.service.WxMiniMaintainManageServiceI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class WxMiniMaintainManageServiceImpl implements WxMiniMaintainManageServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    AdminMaintionEmployeeDao adminMaintionEmployeeDao;

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TerDao terDao;

    @Autowired
    AdminMaintionEmployeeLogTerOperatingDao adminMaintionEmployeeLogTerOperatingDao;

    @Autowired
    WxMiniMaintainAuthServiceI wxMiniMaintainAuthService;

    @Override
    public ApiResult region(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniAuthDTO wxMiniAuthDTO = new Gson().fromJson(body, WxMiniAuthDTO.class);
        if (StringUtils.isBlank(wxMiniAuthDTO.getOpenId())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty openId");
        }
        List<Integer> cityIds = terDao.listTerCityId();
        return ApiResult.ok(cityIds);
    }

    @Override
    public ApiResult siteListSearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniTerSearch wxMiniTerSearch = new Gson().fromJson(body, WxMiniTerSearch.class);
        if (null == wxMiniTerSearch.getCityCode()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty cityCode");
        }
        wxMiniTerSearch.builData();
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniTerSearch);
        if (null != results && !results.isEmpty()) {
            for (TerInfoDTO t : results){
                t.build();
            }
            int cnt = terDao.cntTerInfoSearch(wxMiniTerSearch);
            wxMiniTerSearch.setTotalCnt(cnt);
        }else {
            wxMiniTerSearch.setTotalCnt(0);
        }
        wxMiniTerSearch.setResult(results);
        return ApiResult.ok(new ResultTable(wxMiniTerSearch));
    }

    @Override
    public ApiResult siteInfo(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniTerSearch wxMiniTerSearch = new Gson().fromJson(body, WxMiniTerSearch.class);
        if (null == wxMiniTerSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniTerSearch);
        TerInfoDTO dto = null;
        if (null != results && !results.isEmpty()) {
            dto = results.get(0);
            dto.build();
        }
        return ApiResult.ok(dto);
    }

    @Override
    public ApiResult siteOnline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniTerSearch wxMiniTerSearch = new Gson().fromJson(body, WxMiniTerSearch.class);
        if (null == wxMiniTerSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        AdminMaintionEmployee adminMaintionEmployee = wxMiniMaintainAuthService.check(wxMiniTerSearch.getOpenId());
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniTerSearch);
        TerInfoDTO dto = null;
        if (null != results && !results.isEmpty()) {
            dto = results.get(0);
            dto.build();
        }
        if (null == dto) {
            throw new BaseException(ExceptionCode.NOT_TER);
        }
        int res = terDao.updateOnline(wxMiniTerSearch.getTerId());
        if (res == 0) {
            buildTerOperationLog(dto , adminMaintionEmployee , WashTerOperatingLogTypeEnum.ONLINE,false);
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        buildTerOperationLog(dto , adminMaintionEmployee , WashTerOperatingLogTypeEnum.ONLINE,true);
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteOffline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniTerSearch wxMiniTerSearch = new Gson().fromJson(body, WxMiniTerSearch.class);
        if (null == wxMiniTerSearch.getTerId() || StringUtils.isBlank(wxMiniTerSearch.getMsg()) ) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMaintionEmployee adminMaintionEmployee = wxMiniMaintainAuthService.check(wxMiniTerSearch.getOpenId());
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniTerSearch);
        TerInfoDTO dto = null;
        if (null != results && !results.isEmpty()) {
            dto = results.get(0);
            dto.build();
        }
        if (null == dto) {
            throw new BaseException(ExceptionCode.NOT_TER);
        }
        int res = terDao.updateOffline(wxMiniTerSearch.getTerId(),wxMiniTerSearch.getMsg());
        if (res == 0) {
            buildTerOperationLog(dto , adminMaintionEmployee , WashTerOperatingLogTypeEnum.NOT_ONLINE,false);
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        buildTerOperationLog(dto , adminMaintionEmployee , WashTerOperatingLogTypeEnum.NOT_ONLINE,true);
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteDeviceReset(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);




        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteOperationLog(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);



        return ApiResult.ok(body);
    }

    @Override
    public void buildTerOperationLog(TerInfoDTO terInfoDTO ,
                                     AdminMaintionEmployee adminMaintionEmployee,
                                     WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum ,
                                     Boolean sucess
                                                      ) {
        String titile = terInfoDTO.getTitle() + washTerOperatingLogTypeEnum.getDesc();
        String intros = adminMaintionEmployee.getName() + " 将网点 " + terInfoDTO.getTitle() + washTerOperatingLogTypeEnum.getDesc();
        AdminMaintionEmployeeLogTerOperating adminMaintionEmployeeLogTerOperating = new
                AdminMaintionEmployeeLogTerOperating();
        adminMaintionEmployeeLogTerOperating.setEmployeeId(adminMaintionEmployee.getId());
        adminMaintionEmployeeLogTerOperating.setUsername(adminMaintionEmployee.getName());
        adminMaintionEmployeeLogTerOperating.setTerId(terInfoDTO.getId());
        adminMaintionEmployeeLogTerOperating.setTitle(titile);
        adminMaintionEmployeeLogTerOperating.setIntros(intros);
        adminMaintionEmployeeLogTerOperating.setType(washTerOperatingLogTypeEnum.getValue());
        adminMaintionEmployeeLogTerOperating.setSucess(sucess);
        int res = adminMaintionEmployeeLogTerOperatingDao.insert(adminMaintionEmployeeLogTerOperating);
        if (res == 0) {
            log.error("维保人员操作日志存储失败 {} " + adminMaintionEmployeeLogTerOperating.toString());
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }
}
