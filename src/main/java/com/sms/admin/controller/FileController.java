package com.sms.admin.controller;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.FileSearch;
import com.sms.admin.service.FileServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(FileController.ROUTER_INDEX)
public class FileController {

    public static final String ROUTER_INDEX = "/api/private/file";

    @Autowired
    FileServiceI fileService;

    @PostMapping(value = "/upload/images")
    public ApiResult updateTerCover(HttpServletRequest request , @RequestPart("file")MultipartFile file) {
        return fileService.uploadImges(request ,file);
    }

    @PostMapping(value = "/upload/list")
    public ApiResult list(HttpServletRequest request, @RequestBody FileSearch fileSearch) {
        return fileService.list(request,fileSearch);
    }

    @PostMapping(value = "/delete")
    public ApiResult bachDeleteImges(HttpServletRequest request, @RequestBody FileSearch fileSearch){
        return fileService.bachDeleteImges(request,fileSearch);
    }

}
