package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface TerRatationPictureServiceI {

    ApiResult uploadTerRatationPicture(HttpServletRequest request, MultipartFile file);

    ApiResult listTerRatationPicture(HttpServletRequest request);

    ApiResult startTerRatationPicture(HttpServletRequest request);

    ApiResult deleteTerRatationPicture(HttpServletRequest request);
}
