package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.search.TerPropertySearch;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AdminTerPropertyServiceI {
    ApiResult allTerPropertyInfoList(HttpServletRequest request);

    ApiResult terPropertySearch(HttpServletRequest request);

    ApiResult onlineFreeStart(HttpServletRequest request);

    ApiResult updateTerProperty(HttpServletRequest request,AdminTerPropertyDTO adminTerPropertyDTO);

    void buildTerOperateLog(Object object, TerInfoDTO terInfoDTO,WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum, String img, Boolean
            sucess);

    ApiResult terAllList(HttpServletRequest request);

    ResponseEntity<FileSystemResource> listExport(HttpServletRequest request, HttpServletResponse response);

    ApiResult deviceBindTer(HttpServletRequest request);

    Object check(String openId);

    ApiResult uploadCdrPicture(HttpServletRequest request, MultipartFile file);


}
