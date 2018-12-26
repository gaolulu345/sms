package com.tp.admin.controller.wash;



import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.service.AdminTerPropertyServiceI;
import com.tp.admin.service.TerRatationPictureServiceI;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(AdminTerPropertyController.ROUTER_INDEX)
public class AdminTerPropertyController {

    public static final String ROUTER_INDEX = "/api/private/wash/ter/property";

    @Autowired
    AdminTerPropertyServiceI adminTerPropertyServiceI;

    @Autowired
    TerRatationPictureServiceI terRatationPictureServiceI;

    /**
     * 列出所有的设备属性信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/all/list")
    public ApiResult allTerPropertyInfoList(HttpServletRequest request){
        return adminTerPropertyServiceI.allTerPropertyInfoList(request);
    }

    /**
     * 查询某个设备的属性信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/search")
    public ApiResult terPropertyInfoSearch(HttpServletRequest request){
        return adminTerPropertyServiceI.terPropertySearch(request);
    }


    /**
     * 修改设备属性信息
     *
     * @param request
     * @return
     */
    @PostMapping("/info/update")
    public ApiResult terPropertyInfoUpdate(HttpServletRequest request,@RequestBody AdminTerPropertyDTO adminTerPropertyDTO){
        return adminTerPropertyServiceI.updateTerProperty(request,adminTerPropertyDTO);
    }

    /**
     * 设备绑定网点
     * @param request
     * @return
     */
    @PostMapping("/device/bind")
    public ApiResult deviceBindTer(HttpServletRequest request){
        return adminTerPropertyServiceI.deviceBindTer(request);
    }

    /**
     * 查询所有网点信息
     *
     * @param request
     * @return
     */
    @PostMapping("/list/info")
    public ApiResult terAllList(HttpServletRequest request){
        return adminTerPropertyServiceI.terAllList(request);
    }


    /**
     *上传刻录机的凭证图片
     * @param request
     * @return
     */
    @PostMapping("/upload/cdr/picture")
    public ApiResult uploadCdrPicture(HttpServletRequest request, @RequestPart("file") MultipartFile file){
        return adminTerPropertyServiceI.uploadCdrPicture(request,file);
    }
    /**
     * 导出设备列表
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "/list/exprot")
    public ResponseEntity<FileSystemResource> listExport(HttpServletRequest request, HttpServletResponse response) {
        return adminTerPropertyServiceI.listExport(request,response);
    }

    /**
     * 推送轮播位置广告图
     * @param request
     *
     * @return
     */
    @PostMapping("/push/ratation/picture")
    public ApiResult pushRatationPicture(HttpServletRequest request){
        return terRatationPictureServiceI.pushRatationPicture(request);
    }

    /**
     * 推送广告位置的轮播图
     * @param request
     * @param body
     * @return
     */
    @PostMapping("/push/ad/picture")
    public ApiResult pushAdPicture(HttpServletRequest request,@RequestBody String body){
        return terRatationPictureServiceI.pushAdPicture(request,body);
    }

    /**
     * 添加设备信息
     * @param request
     * @param adminTerPropertyDTO
     * @return
     */
    @PostMapping("/insert")
    public ApiResult insertTerproperty(HttpServletRequest request,@RequestBody AdminTerPropertyDTO adminTerPropertyDTO){
        return adminTerPropertyServiceI.insertTerproperty(request,adminTerPropertyDTO);
    }

    /**
     * 删除某个设备的属性信息
     * @param request
     * @param adminTerPropertyDTO
     * @return
     */
    @PostMapping("/update/deleted")
    public ApiResult updateDeleteTerproperty(HttpServletRequest request,@RequestBody AdminTerPropertyDTO adminTerPropertyDTO){
        return  adminTerPropertyServiceI.updateDeleteTerProperty(request,adminTerPropertyDTO);
    }
}
