package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.config.AliyunOssProperties;
import com.tp.admin.dao.TerRatationDao;
import com.tp.admin.data.dto.UploadFileDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.entity.TerRatationPicture;
import com.tp.admin.data.entity.TerRatationPictureLog;
import com.tp.admin.data.search.TerRatationPictureSearch;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.AliyunOssManagerI;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.TerRatationPictureServiceI;
import com.tp.admin.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
public class TerRatationPictureServiceImpl implements TerRatationPictureServiceI {
    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    AliyunOssProperties aliyunOssProperties;

    @Autowired
    AliyunOssManagerI aliyunOssManager;

    @Autowired
    TerRatationDao terRatationDao;

    @Override
    public ApiResult uploadTerRatationPicture(HttpServletRequest request, MultipartFile file) {
        Integer terId = Integer.parseInt(request.getParameter("terId"));
        Integer type = Integer.parseInt(request.getParameter("type"));
        TerRatationPictureSearch terRatationPictureSearch = new TerRatationPictureSearch();

        if (terId == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"empty terId");
        }
        terRatationPictureSearch.setTerId(terId);
        if (type == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"empty type");
        }
        terRatationPictureSearch.setType(type);
        if (file == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        UploadFileDTO uploadFileDTO = aliyunOssManager.uploadFileToAliyunOss(file ,aliyunOssProperties.getPath());
        if (!uploadFileDTO.isSuccess()){
            throw new BaseException(ExceptionCode.ALI_OSS_UPDATE_ERROR);
        }
        terRatationPictureSearch.setCreateTime(new Timestamp(System.currentTimeMillis()));
        terRatationPictureSearch.setPicture(uploadFileDTO.getUrl());
        int res = terRatationDao.uploadTerRatationPicture(terRatationPictureSearch);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult listTerRatationPicture(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerRatationPictureSearch terRatationPictureSearch = new Gson().fromJson(body, TerRatationPictureSearch.class);
        if (terRatationPictureSearch.getTerId() == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"empty terId");
        }
        List<TerRatationPicture> terRatationPictureList = terRatationDao.terRatationPictureShow(terRatationPictureSearch);
        if (terRatationPictureList == null || terRatationPictureList.size() == 0){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"该网点下无图片数据");
        }
        for (TerRatationPicture terRatationPicture:terRatationPictureList) {
            terRatationPicture.build();
        }
        return ApiResult.ok(terRatationPictureList);
    }

    @Override
    public ApiResult startTerRatationPicture(HttpServletRequest request) {
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
        res = terRatationDao.addTerRatationLog(new TerRatationPictureLog(terRatationPictureSearch.getId().toString(),adminAccount.getName(),info));
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult deleteTerRatationPicture(HttpServletRequest request) {
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
        String info = adminAccount.getName() + "批量删除id为" + Arrays.toString(terRatationPictureSearch.getIds()) + "数据";
        res = terRatationDao.addTerRatationLog(new TerRatationPictureLog(Arrays.toString(terRatationPictureSearch.getIds()),adminAccount.getName(),info));
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }
}
