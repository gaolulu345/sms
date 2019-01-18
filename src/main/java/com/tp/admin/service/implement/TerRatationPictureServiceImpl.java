package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.ajax.ResultCode;
import com.tp.admin.common.Constant;
import com.tp.admin.config.AdminProperties;
import com.tp.admin.config.AliyunOssProperties;
import com.tp.admin.dao.AdminTerPropertyDao;
import com.tp.admin.dao.FileUploadLogDao;
import com.tp.admin.dao.TerRatationDao;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.data.dto.UploadFileDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.entity.FileUploadLog;
import com.tp.admin.data.entity.TerRatationPicture;
import com.tp.admin.data.entity.TerRatationPictureLog;
import com.tp.admin.data.search.Search;
import com.tp.admin.data.search.TerRatationPictureSearch;
import com.tp.admin.data.wash.TerDeviceRequest;
import com.tp.admin.enums.TerRatationPictureTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.AliyunOssManagerI;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.TerRatationPictureServiceI;
import com.tp.admin.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TerRatationPictureServiceImpl implements TerRatationPictureServiceI {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    AliyunOssProperties aliyunOssProperties;

    @Autowired
    AliyunOssManagerI aliyunOssManager;

    @Autowired
    TerRatationDao terRatationDao;

    @Autowired
    FileUploadLogDao fileUploadLogDao;

    @Autowired
    AdminTerPropertyDao adminTerPropertyDao;

    @Autowired
    AdminProperties adminProperties;

    @Override
    public ApiResult uploadAppointTerRatationPicture(HttpServletRequest request, MultipartFile file) {
        Integer deviceId = Integer.parseInt(request.getParameter("id"));
        Integer type = Integer.parseInt(request.getParameter("type"));
        TerRatationPictureSearch terRatationPictureSearch = new TerRatationPictureSearch();

        if (deviceId == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"empty terId");
        }
        terRatationPictureSearch.setDeviceId(deviceId);
        if (type == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"empty type");
        }
        terRatationPictureSearch.setEnable(true);
        terRatationPictureSearch.setType(type);
        List<TerRatationPicture> terRatationPictures = new ArrayList<>();
        if (type == TerRatationPictureTypeEnum.SECOND_AD_POSITION.getValue()){
            terRatationPictures = terRatationDao.terRatationPictureShow(terRatationPictureSearch);

        }
        if (terRatationPictures.size() > 1){
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        if (file == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        UploadFileDTO uploadFileDTO = aliyunOssManager.uploadFileToAliyunOss(file ,aliyunOssProperties.getPath());
        if (!uploadFileDTO.isSuccess()){
            throw new BaseException(ExceptionCode.ALI_OSS_UPDATE_ERROR);
        }
        terRatationPictureSearch.setCreateTime(new Timestamp(System.currentTimeMillis()));
        terRatationPictureSearch.setPicture(uploadFileDTO.getUrl());
        terRatationPictureSearch.setEnable(true);
        terRatationPictureSearch.setEnableTime(new Timestamp(System.currentTimeMillis()));
        int res = 0;
        if (type == TerRatationPictureTypeEnum.SECOND_AD_POSITION.getValue() && terRatationPictures.size() == 1){
            terRatationPictureSearch.setId(terRatationPictures.get(0).getId());
            terRatationPictureSearch.setModifyTime(new Timestamp(System.currentTimeMillis()));
            res = terRatationDao.updateAdPicture(terRatationPictureSearch);
        } else {
            res = terRatationDao.uploadTerRatationPicture(terRatationPictureSearch);
        }

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
    public ApiResult terRatationPictureShow(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerRatationPictureSearch terRatationPictureSearch = new Gson().fromJson(body, TerRatationPictureSearch.class);
        if (terRatationPictureSearch.getDeviceId() == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"empty deviceId");
        }
        terRatationPictureSearch.setEnable(true);
        terRatationPictureSearch.build();
        List<TerRatationPicture> terRatationPictureList = terRatationDao.terRatationPictureShow(terRatationPictureSearch);
        if (terRatationPictureList == null){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        for (TerRatationPicture terRatationPicture:terRatationPictureList) {
            terRatationPicture.build();
        }

        Integer terRatationPictureNum = terRatationDao.terRatationPictureCount(terRatationPictureSearch);
        if (terRatationPictureNum != null){
            terRatationPictureSearch.setTotalCnt(terRatationPictureNum);
        }
        terRatationPictureSearch.setResult(terRatationPictureList);
        return ApiResult.ok(terRatationPictureSearch);
    }

    @Override
    public ApiResult startAppointTerRatationPicture(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerRatationPictureSearch terRatationPictureSearch = new Gson().fromJson(body, TerRatationPictureSearch.class);
        if (terRatationPictureSearch.getId() == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"empty pictureId");
        }
        terRatationPictureSearch.setEnableTime(new Timestamp(System.currentTimeMillis()));
        int res = terRatationDao.startTerRatationPicture(terRatationPictureSearch);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        String info = adminAccount.getName() + "启用了id为" + terRatationPictureSearch.getId() + "轮播图";
        TerRatationPictureLog terRatationPictureLog = new TerRatationPictureLog(adminAccount.getName(),info);
        res = terRatationDao.addTerRatationLog(terRatationPictureLog);
        if (res == 0){
            log.error("add start taration picture log failed {}" + terRatationPictureLog.toString());
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult deleteAppointTerRatationPicture(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerRatationPictureSearch terRatationPictureSearch = new Gson().fromJson(body, TerRatationPictureSearch.class);
        if (terRatationPictureSearch.getIds() == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"empty pictureIds");
        }

        int res = terRatationDao.deleteTerRatationPicture(terRatationPictureSearch);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        String info = adminAccount.getName() + "批量删除id为" + Arrays.toString(terRatationPictureSearch.getIds().toArray()) + "轮播图";
        TerRatationPictureLog terRatationPictureLog = new TerRatationPictureLog(adminAccount.getName(),info);
        res = terRatationDao.addTerRatationLog(terRatationPictureLog);
        if (res == 0){
            log.error("add batch delete taration picture failed {}" + terRatationPictureLog.toString());
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult pushRatationPicture(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerRatationPictureSearch terRatationPictureSearch = new Gson().fromJson(body,TerRatationPictureSearch.class);
        terRatationPictureSearch.setDeleted(false);
        terRatationPictureSearch.setEnable(true);
        terRatationPictureSearch.setType(TerRatationPictureTypeEnum.GLOD_AD_POSITION.getValue());
        if (terRatationPictureSearch.getDeviceId() == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        //查询设备是否可推送广告
        AdminTerPropertyDTO adminTerPropertyDTO = adminTerPropertyDao.findTerStartInfo(terRatationPictureSearch.getDeviceId());
        if (!adminTerPropertyDTO.isAdExist() || adminTerPropertyDTO.isDeleted()){
            throw new BaseException(ExceptionCode.NOT_ALLOW_PUSH_AD);
        }
        List<String> imageList = new ArrayList<>();
        List<TerRatationPicture> list = terRatationDao.terRatationPictureShow(terRatationPictureSearch);
        if (list == null || list.size() == 0){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        for (TerRatationPicture terRatationPicture:list) {
            if (!terRatationPicture.isEnable() || !terRatationPicture.getType().equals(TerRatationPictureTypeEnum.GLOD_AD_POSITION.getValue())){
                throw new BaseException(ExceptionCode.PICTURE_NOT_ENABLE_OR_TYPE_NOT_ACCESS);
            }
            imageList.add(terRatationPicture.getPicture());
        }
        TerDeviceRequest terDeviceRequest = httpHelper.signTerInfo(adminTerPropertyDTO.getFrpIp(),null,"",adminTerPropertyDTO.getFrpPort());
        terDeviceRequest.setPictures(imageList);
        String jsonBody = new Gson().toJson(terDeviceRequest);
        System.out.println(jsonBody);
        String result = httpHelper.sendPostByJsonData(adminProperties.getWashManageServer() + Constant.RemoteTer
                .RATATION_PICTURE_PUSH, jsonBody);
        return buildApiResult(result,request,terRatationPictureSearch);
    }

    @Override
    public ApiResult pushAdPicture(HttpServletRequest request, String body) {
        TerRatationPictureSearch terRatationPictureSearch = new Gson().fromJson(body,TerRatationPictureSearch.class);
        if (terRatationPictureSearch.getDeviceId() == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        //查询设备是否可推送广告
        AdminTerPropertyDTO adminTerPropertyDTO = adminTerPropertyDao.findTerStartInfo(terRatationPictureSearch.getDeviceId());
        if (!adminTerPropertyDTO.isAdExist() || adminTerPropertyDTO.isDeleted()){
            throw new BaseException(ExceptionCode.NOT_ALLOW_PUSH_AD);
        }
        List<String> imageList = new ArrayList<>();
        terRatationPictureSearch.setType(1);
        List<TerRatationPicture> list = terRatationDao.terRatationPictureShow(terRatationPictureSearch);
        if (list == null || list.size() == 0 || list.size() != 1){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        for (TerRatationPicture terRatationPicture:list) {
            if (!terRatationPicture.isEnable() || !terRatationPicture.getType().equals(TerRatationPictureTypeEnum.SECOND_AD_POSITION.getValue())){
                throw new BaseException(ExceptionCode.PICTURE_NOT_ENABLE_OR_TYPE_NOT_ACCESS);
            }
            imageList.add(terRatationPicture.getPicture());
        }
        TerDeviceRequest terDeviceRequest = httpHelper.signTerInfo(adminTerPropertyDTO.getFrpIp(),null,"",adminTerPropertyDTO.getFrpPort());
        terDeviceRequest.setPictures(imageList);
        String jsonBody = new Gson().toJson(terDeviceRequest);
        String result = httpHelper.sendPostByJsonData(adminProperties.getWashManageServer() + Constant.RemoteTer
                .RATATION_PICTURE_PUSH, jsonBody);
        return buildApiResult(result,request,terRatationPictureSearch);
    }

    private final ApiResult buildApiResult(String result,HttpServletRequest request,TerRatationPictureSearch terRatationPictureSearch) {
        ApiResult apiResult = null;
        try {
            apiResult = new Gson().fromJson(result, ApiResult.class);
            if (null == apiResult) {
                throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
            }
            int res = 0;
            AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
            String info = adminAccount.getName() + " 推送设备id为 " + terRatationPictureSearch.getDeviceId() + " 的广告图";
            res = terRatationDao.addTerRatationLog(new TerRatationPictureLog(adminAccount.getName(),info));
            if (res == 0){
                throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
            }
        } catch (JsonSyntaxException ex) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        return apiResult;
    }
}
