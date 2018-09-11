package com.tp.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(OpenApiController.ROUTER_INDEX)
public class OpenApiController {

    public static final String ROUTER_INDEX = "/api/open";

}
