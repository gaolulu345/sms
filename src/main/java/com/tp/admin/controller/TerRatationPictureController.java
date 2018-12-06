package com.tp.admin.controller;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.service.TerRatationPictureServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(TerRatationPictureController.ROUTER_INDEX)
public class TerRatationPictureController {

    @Autowired
    TerRatationPictureServiceI terRatationPictureServiceI;

    public static final String ROUTER_INDEX = "/api/open/ter/ratation/picture";


    /**
     * 上传轮播图
     * @param request
     * @param file
     * @return
     */
    @PostMapping(value = "/appoint/upload")
    public ApiResult uploadTerRatationPicture(HttpServletRequest request , @RequestPart("file") MultipartFile file){
        return terRatationPictureServiceI.uploadTerRatationPicture(request,file);
    }

    /**
     * 展示某个网点的轮播图
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/show")
    public ApiResult terRatationPictureShow(HttpServletRequest request){
        return terRatationPictureServiceI.listTerRatationPicture(request);
    }

    /**
     * 开启某张轮播图的使用
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/appoint/start")
    public ApiResult startTerRatationPicture(HttpServletRequest request){
        return terRatationPictureServiceI.startTerRatationPicture(request);
    }

    /**
     * 批量删除轮播图
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/appoint/delete")
    public ApiResult deleteTerRatationPicture(HttpServletRequest request){
        return terRatationPictureServiceI.deleteTerRatationPicture(request);
    }

}
