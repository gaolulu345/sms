package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface TerRatationPictureServiceI {

    ApiResult uploadAppointTerRatationPicture(HttpServletRequest request, MultipartFile file);

    ApiResult terRatationPictureShow(HttpServletRequest request);

    ApiResult startAppointTerRatationPicture(HttpServletRequest request);

    ApiResult deleteAppointTerRatationPicture(HttpServletRequest request);
}
