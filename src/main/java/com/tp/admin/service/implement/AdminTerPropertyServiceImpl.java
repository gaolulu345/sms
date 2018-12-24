package com.tp.admin.service.implement;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.ajax.ResultCode;
import com.tp.admin.common.Constant;
import com.tp.admin.config.AdminProperties;
import com.tp.admin.config.AliyunOssProperties;
import com.tp.admin.dao.*;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.dto.UploadFileDTO;
import com.tp.admin.data.entity.*;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.search.TerPropertySearch;
import com.tp.admin.data.wash.WashSiteRequest;
import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.AliyunOssManagerI;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.AdminTerPropertyServiceI;
import com.tp.admin.service.WashSiteServiceI;
import com.tp.admin.service.WxMiniMerchantManageServiceI;
import com.tp.admin.utils.ExcelUtil;
import com.tp.admin.utils.SessionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminTerPropertyServiceImpl implements AdminTerPropertyServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TerDao terDao;

    @Autowired
    AdminMaintionEmployeeDao adminMaintionEmployeeDao;

    @Autowired
    AdminMerchantEmployeeDao adminMerchantEmployeeDao;

    @Autowired
    WashSiteServiceI washSiteServiceI;

    @Autowired
    WxMiniMerchantManageServiceI wxMiniMerchantManageServiceI;

    @Autowired
    AdminTerOperatingLogDao adminTerOperatingLogDao;

    @Autowired
    AdminProperties adminProperties;

    @Autowired
    AdminTerPropertyDao adminTerPropertyDao;

    @Autowired
    AliyunOssProperties aliyunOssProperties;

    @Autowired
    AliyunOssManagerI aliyunOssManager;

    @Autowired
    FileUploadLogDao fileUploadLogDao;

    @Override
    public ApiResult allTerPropertyInfoList(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerPropertySearch terPropertySearch = new Gson().fromJson(body, TerPropertySearch.class);
        if (null == terPropertySearch) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        terPropertySearch.build();
        List<AdminTerPropertyDTO> list = adminTerPropertyDao.findAllTerProperty(terPropertySearch);
        if (list == null) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        for (AdminTerPropertyDTO adminTerProperty:list) {
            adminTerProperty.build();
        }
        Integer num = adminTerPropertyDao.findAllTerPropertyCount();
        if (num == null){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        terPropertySearch.setTotalCnt(num);
        terPropertySearch.setResult(list);
        return ApiResult.ok(terPropertySearch);
    }


    @Override
    public ApiResult terPropertySearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerPropertySearch terPropertySearch = new Gson().fromJson(body, TerPropertySearch.class);
        if (null == terPropertySearch.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "设备id为空");
        }

        AdminTerPropertyDTO adminTerPropertyDTO =  adminTerPropertyDao.findTerStartInfo(terPropertySearch.getId());
        if (adminTerPropertyDTO == null){
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        adminTerPropertyDTO.build();
        return ApiResult.ok(adminTerPropertyDTO);
    }

    @Override
    public ApiResult onlineFreeStart(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerPropertySearch terPropertySearch = new Gson().fromJson(body, TerPropertySearch.class);
        if (null == terPropertySearch.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "设备id为空");
        }

        Object ob = check(terPropertySearch.getOpenId());
        if (ob == null){
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        AdminTerPropertyDTO adminTerPropertyDTO =  adminTerPropertyDao.findTerStartInfo(terPropertySearch.getId());
        if (adminTerPropertyDTO.getTerId() == 0){
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION,"未绑定网点，无法操作");
        }
        WxMiniSearch wxMiniSearch = new WxMiniSearch();
        wxMiniSearch.setTerId(adminTerPropertyDTO.getTerId());
        TerInfoDTO terInfoDTO = washSiteServiceI.terCheck(wxMiniSearch);
        WashSiteRequest washSiteRequest = httpHelper.signInfo(wxMiniSearch.getTerId(), "", "");
        String jsonBody = new Gson().toJson(washSiteRequest);
        String result = httpHelper.sendPostByJsonData(adminProperties.getWashManageServer() + Constant.RemoteTer
              .SITE_ONLINE_START, jsonBody);
        return buildApiResult(ob,result,terInfoDTO,"",WashTerOperatingLogTypeEnum.ONLINE_FREE_STARTED);
    }

    @Override
    public ApiResult updateTerProperty(HttpServletRequest request,AdminTerPropertyDTO adminTerPropertyDTO) {
        if (null == adminTerPropertyDTO.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty id");
        }
        int res = adminTerPropertyDao.updateTerProperty(adminTerPropertyDTO);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public Object check(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        AdminMaintionEmployee adminMaintionEmployee = adminMaintionEmployeeDao.findByWxMiniId(openId);

        AdminMerchantEmployee adminMerchantEmployee;
        if (null != adminMaintionEmployee && adminMaintionEmployee.isEnable() && !adminMaintionEmployee.isDeleted()) {
            return adminMaintionEmployee;
        }else {
            adminMerchantEmployee = adminMerchantEmployeeDao.findByWxMiniId(openId);
            if (null != adminMerchantEmployee && adminMerchantEmployee.isEnable() && !adminMerchantEmployee.isDeleted()) {
                return adminMerchantEmployee;
            }
        }
        return null;
    }

    @Override
    public ApiResult uploadCdrPicture(HttpServletRequest request, MultipartFile file) {
        Integer deviceId = Integer.parseInt(request.getParameter("id"));
        if (deviceId == null){
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        AdminTerPropertyDTO adminTerPropertyDTO = new AdminTerPropertyDTO();
        adminTerPropertyDTO.setId(deviceId);
        UploadFileDTO uploadFileDTO = aliyunOssManager.uploadFileToAliyunOss(file ,aliyunOssProperties.getPath());
        if (!uploadFileDTO.isSuccess()){
            throw new BaseException(ExceptionCode.ALI_OSS_UPDATE_ERROR);
        }
        adminTerPropertyDTO.setCdrPicture(uploadFileDTO.getUrl());
        int res =  adminTerPropertyDao.updateTerProperty(adminTerPropertyDTO);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        String fileName = uploadFileDTO.getKey();
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        res = fileUploadLogDao.insert(new FileUploadLog(adminAccount.getName(),fileName));
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public void buildTerOperateLog(Object object, TerInfoDTO terInfoDTO,WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum, String img, Boolean
            sucess) {
        AdminTerOperatingLog adminTerOperatingLog = new AdminTerOperatingLog();
        AdminMerchantEmployee adminMerchantEmployee;
        AdminMaintionEmployee adminMaintionEmployee;
        if (object instanceof AdminMerchantEmployee){
            adminMerchantEmployee = (AdminMerchantEmployee) object;
            String intros = adminMerchantEmployee.getName() + " 操作 " + terInfoDTO.getTitle() + washTerOperatingLogTypeEnum
                    .getDesc();
            adminTerOperatingLog.setMerchantId(adminMerchantEmployee.getId());
            adminTerOperatingLog.setUsername(adminMerchantEmployee.getName());
            adminTerOperatingLog.setIntros(intros);
            adminTerOperatingLog.setOpSource(AdminTerOperatingLogSourceEnum.MERCHANT.getValue());
        }else {
            adminMaintionEmployee = (AdminMaintionEmployee) object;
            String intros = adminMaintionEmployee.getName() + " 操作 " + terInfoDTO.getTitle() + washTerOperatingLogTypeEnum
                    .getDesc();
            adminTerOperatingLog.setMaintionId(adminMaintionEmployee.getId());
            adminTerOperatingLog.setUsername(adminMaintionEmployee.getName());
            adminTerOperatingLog.setIntros(intros);
            adminTerOperatingLog.setOpSource(AdminTerOperatingLogSourceEnum.MAINTAUN.getValue());
        }
        adminTerOperatingLog.setTerId(terInfoDTO.getId());
        adminTerOperatingLog.setTitle(washTerOperatingLogTypeEnum.getDesc());

        adminTerOperatingLog.setType(washTerOperatingLogTypeEnum.getValue());
        adminTerOperatingLog.setImgs(img);
        adminTerOperatingLog.setSuccess(sucess);
        int res = adminTerOperatingLogDao.insert(adminTerOperatingLog);
        if (res == 0) {
            log.error("操作日志存储失败 {} " + adminTerOperatingLog.toString());
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }

    @Override
    public ApiResult terAllList(HttpServletRequest request) {
        List<Map<String,Object>> list = terDao.findAllTerIdAndTitle();
        TerPropertySearch terPropertySearch = new TerPropertySearch();
        if (null != list && list.size() != 0){
            Integer num  = terDao.findAllTerInfoCount();
            terPropertySearch.setResult(list);
            terPropertySearch.setTotalCnt(num);
        }else {
            terPropertySearch.setTotalCnt(0);
        }
        return ApiResult.ok(terPropertySearch);
    }

    @Override
    public ResponseEntity<FileSystemResource> listExport(HttpServletRequest request, HttpServletResponse response) {
        List<AdminTerPropertyDTO> list = adminTerPropertyDao.findAllTerProperty(new TerPropertySearch());
        if (null != list && !list.isEmpty()){
            for (AdminTerPropertyDTO adminTerPropertyDTO:list) {
                adminTerPropertyDTO.build();
            }
        }
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = simpleDateFormat.format(date);
        String fileName = ExcelUtil.createXlxs(Constant.TER_DEVICE,startTime , (int)(Math.random()*100) + "");
        String path = System.getProperty(Constant.TMP_DIR) + Constant._XLSX_DIR;
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        try {
            ExcelUtils.getInstance().exportObjects2Excel(list, AdminTerPropertyDTO.class, true, "sheet0", true, path + fileName);
        } catch (Excel4JException | IOException e) {
            e.printStackTrace();
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        File file = new File(path + fileName);
        return ExcelUtil.fileExcel(request,fileName,file);
    }

    @Override
    public ApiResult deviceBindTer(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerPropertySearch terPropertySearch = new Gson().fromJson(body, TerPropertySearch.class);
        if (null == terPropertySearch.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "设备id为空");
        }
        AdminTerPropertyDTO adminTerPropertyDTO = adminTerPropertyDao.findTerStartInfo(terPropertySearch.getId());
        if (adminTerPropertyDTO.getTerId().equals(terPropertySearch.getTerId())){
            return ApiResult.ok();
        }
        WxMiniSearch wxMiniSearch = new WxMiniSearch();
        wxMiniSearch.setTerId(terPropertySearch.getTerId());
        List<TerInfoDTO> list = terDao.terInfoSearch(wxMiniSearch);
        TerInfoDTO terInfoDTO = null;
        if (null != list && !list.isEmpty()){
            terInfoDTO = list.get(0);
        }
        adminTerPropertyDTO.setTerId(terPropertySearch.getTerId());
        adminTerPropertyDTO.setTerRemark(terInfoDTO.getTitle());
        adminTerPropertyDTO.setTerModel(terInfoDTO.getCode());
        int res = adminTerPropertyDao.updateTerProperty(adminTerPropertyDTO);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }

    private final ApiResult buildApiResult(Object object,String result, TerInfoDTO dto, String img, WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum
    ) {
        ApiResult apiResult = null;
        try {
            apiResult = new Gson().fromJson(result, ApiResult.class);
            if (null == apiResult) {
                throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
            }
            if (apiResult.getCode().equals(ResultCode.SUCCESS.getCode())) {
                buildTerOperateLog(object,dto, washTerOperatingLogTypeEnum, img, true);
            } else {
                buildTerOperateLog(object,dto, washTerOperatingLogTypeEnum, img, false);
            }
        } catch (JsonSyntaxException ex) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        return apiResult;
    }


}
