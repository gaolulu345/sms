package com.tp.admin.controller.wash;



import com.tp.admin.ajax.ApiResult;
import com.tp.admin.service.AdminTerPropertyServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(AdminTerPropertyController.ROUTER_INDEX)
public class AdminTerPropertyController {

    public static final String ROUTER_INDEX = "/api/open/wash/ter/property";

    @Autowired
    AdminTerPropertyServiceI adminTerPropertyServiceI;

    /**
     * 查询某个网点的属性信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/search")
    public ApiResult terPropertyInfoSearch(HttpServletRequest request){
        return adminTerPropertyServiceI.terPropertySearch(request);
    }

    /**
     * 线上免费开启
     *
     * @param request
     * @return
     */
    @PostMapping("/site/online/start")
    public ApiResult onlineFreeStart(HttpServletRequest request){
        //return null;
        return adminTerPropertyServiceI.onlineFreeStart(request);
    }


}
