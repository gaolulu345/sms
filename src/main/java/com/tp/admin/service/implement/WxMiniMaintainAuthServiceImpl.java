package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.manage.impl.JsonRequestWrapperImpl;
import com.tp.admin.service.WxMiniMaintainAuthServiceI;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class WxMiniMaintainAuthServiceImpl implements WxMiniMaintainAuthServiceI {
    @Override
    public ApiResult auth(HttpServletRequest request) {
        try {
            JsonRequestWrapperImpl jsonRequestWrapper = new JsonRequestWrapperImpl(request);
            System.out.println(jsonRequestWrapper.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult register(HttpServletRequest request) {
        return ApiResult.ok();
    }

    @Override
    public ApiResult registerCheck(HttpServletRequest request) {
        return ApiResult.ok();
    }
}
