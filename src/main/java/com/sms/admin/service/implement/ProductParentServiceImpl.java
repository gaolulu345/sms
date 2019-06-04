package com.sms.admin.service.implement;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.dao.ProductParentDao;
import com.sms.admin.data.dto.ProductParentDTO;
import com.sms.admin.data.search.ProductParentSearch;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.service.ProductParentServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ProductParentServiceImpl implements ProductParentServiceI {
    @Autowired
    ProductParentDao productParentDao;

    @Override
    public ApiResult list(HttpServletRequest request, ProductParentSearch productParentSearch) {
        productParentSearch.build();
        List<ProductParentDTO> list = productParentDao.listBySearch(productParentSearch);
        if (null != list && !list.isEmpty()) {
            productParentSearch.setResult(list);
            int cnt = productParentDao.cntBySearch(productParentSearch);
            productParentSearch.setTotalCnt(cnt);
        } else {
            productParentSearch.setTotalCnt(0);
        }
        return ApiResult.ok(productParentSearch);
    }

    @Override
    public ApiResult addProductParent(HttpServletRequest request, ProductParentSearch productParentSearch) {
        if (null == productParentSearch.getTypeName() || productParentSearch.getTypeName().equals("")){
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        int res = productParentDao.addProductParent(productParentSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult updateProductParentInfo(HttpServletRequest request, ProductParentSearch productParentSearch) {
        if (null == productParentSearch.getTypeName() || productParentSearch.getTypeName().equals("") || null == productParentSearch.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        int res = productParentDao.updateProductParent(productParentSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult updateProductParentDeleted(HttpServletRequest request, ProductParentSearch productParentSearch) {
        if (null == productParentSearch.getDeleted() || null == productParentSearch.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        int res = productParentDao.updateProductParent(productParentSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }


}
