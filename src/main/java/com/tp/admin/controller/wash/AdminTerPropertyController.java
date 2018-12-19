package com.tp.admin.controller.wash;



import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.service.AdminTerPropertyServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(AdminTerPropertyController.ROUTER_INDEX)
public class AdminTerPropertyController {

    public static final String ROUTER_INDEX = "/api/private/wash/ter/property";

    @Autowired
    AdminTerPropertyServiceI adminTerPropertyServiceI;

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
     *
     * @param request
     * @return
     */
    @PostMapping("/upload/picture")
    public ApiResult uploadPicture(HttpServletRequest request){
        return null;
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
}
