package com.tp.admin.controller;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.service.ALiMiniServiceI;
import com.tp.admin.service.WxMiniServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(TemplateController.ROUTER_INDEX)
public class TemplateController {
    public static final String ROUTER_INDEX = "/api/private/sys";

    @Autowired
    WxMiniServiceI wxMiniServiceI;

    @Autowired
    ALiMiniServiceI aLiMiniServiceI;

    /**
     * 微信发送模版
     * @param request
     * @return
     */
    @PostMapping(value = "/wx/template/send")
    public ApiResult sendWxTemplate(HttpServletRequest request){
        return wxMiniServiceI.sendWxTemplate(request);
    }

    @PostMapping(value = "/ali/template/send")
    public ApiResult sendAliTemplate(HttpServletRequest request){
        return aLiMiniServiceI.sendAliTemplate(request);
    }
}
