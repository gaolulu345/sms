package com.tp.admin.controller.wash;


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

    public static final String ROUTER_INDEX = "/api/private/ter/ratation/picture";


    /**
     * 上传轮播图
     * @param request
     * @param file
     * @return
     */
    @PostMapping(value = "/appoint/upload")
    public ApiResult uploadAppointTerRatationPicture(HttpServletRequest request , @RequestPart("file") MultipartFile file){
        return terRatationPictureServiceI.uploadAppointTerRatationPicture(request,file);
    }

    /**
     * 展示某个网点的轮播图
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/show")
    public ApiResult terRatationPictureShow(HttpServletRequest request){
        return terRatationPictureServiceI.terRatationPictureShow(request);
    }

    /**
     * 开启某张轮播图的使用
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/appoint/start")
    public ApiResult startAppointTerRatationPicture(HttpServletRequest request){
        return terRatationPictureServiceI.startAppointTerRatationPicture(request);
    }

    /**
     * 批量删除轮播图
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/appoint/delete")
    public ApiResult deleteTerRatationPicture(HttpServletRequest request){
        return terRatationPictureServiceI.deleteAppointTerRatationPicture(request);
    }

}

