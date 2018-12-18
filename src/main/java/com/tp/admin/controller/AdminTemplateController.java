package com.tp.admin.controller;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.service.AdminTemplateInfoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(AdminTemplateController.ROUTER_INDEX)
public class AdminTemplateController {
    public static final String ROUTER_INDEX = "/api/open/template";

    @Autowired
    AdminTemplateInfoServiceI adminTemplateInfoServiceI;

    /**
     * 发送模板消息
     * @param request
     * @return
     */
    @PostMapping(value = "/info/send")
    public ApiResult sendTemplateInfo(HttpServletRequest request){
        //return null;
        return adminTemplateInfoServiceI.sendTemplate(request);
    }
}
