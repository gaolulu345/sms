package com.tp.admin.controller.wash;



import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.service.AdminTerPropertyServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

}
