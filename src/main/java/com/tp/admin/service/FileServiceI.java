package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.FileSearch;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileServiceI {

    ApiResult uoloadImges(HttpServletRequest request , MultipartFile file);

    ApiResult list(HttpServletRequest request, FileSearch fileSearch);

    ApiResult bachDeleteImges(HttpServletRequest request, FileSearch fileSearch);

}
