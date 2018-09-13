package com.tp.admin.controller;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.FileSearch;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.service.FileServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping(FileController.ROUTER_INDEX)
public class FileController {

    public static final String ROUTER_INDEX = "/api/private/file";

    @Autowired
    FileServiceI fileService;

    @PostMapping(value = "/upload/images")
    public ApiResult updateTerCover(HttpServletRequest request , @RequestPart("file")MultipartFile file) {
        return fileService.uoloadImges(request ,file);
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
