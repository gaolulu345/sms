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

    public static final String ROUTER_INDEX = "/api/private/wash/ter/property";

    @Autowired
    AdminTerPropertyServiceI adminTerPropertyServiceI;

    @PostMapping(value = "/search")
    public ApiResult terPropertyInfoSearch(HttpServletRequest request){
        return adminTerPropertyServiceI.terPropertySearch(request);
    }
}
