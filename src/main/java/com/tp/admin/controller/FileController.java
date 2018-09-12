package com.tp.admin.controller;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.FileSearch;
import com.tp.admin.data.search.RefundSearch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(FileController.ROUTER_INDEX)
public class FileController {

    public static final String ROUTER_INDEX = "/api/private/file";

    public ApiResult uploadImages(HttpServletRequest request, @RequestBody FileSearch fileSearch) {
        return null;
    }
}
