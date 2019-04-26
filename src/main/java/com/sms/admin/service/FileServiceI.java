package com.sms.admin.service;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.FileSearch;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileServiceI {

    ApiResult uploadImges(HttpServletRequest request , MultipartFile file);

    ApiResult list(HttpServletRequest request, FileSearch fileSearch);

    ApiResult bachDeleteImges(HttpServletRequest request, FileSearch fileSearch);

}
