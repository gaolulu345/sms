package com.tp.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = OpenApiController.ROUTER_INDEX)
public class OpenApiController {

    public static final String ROUTER_INDEX = "/open";


}
