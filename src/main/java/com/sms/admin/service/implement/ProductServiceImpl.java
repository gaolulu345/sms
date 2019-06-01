package com.sms.admin.service.implement;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.dao.ProductDao;
import com.sms.admin.data.dto.ProductDTO;
import com.sms.admin.data.search.ProductSearch;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.service.ProductServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductServiceI {

    @Autowired
    ProductDao productDao;

    @Override
    public ApiResult list(HttpServletRequest request, ProductSearch productSearch) {
        productSearch.build();
        List<ProductDTO> list = productDao.listBySearch(productSearch);
        if (null != list && !list.isEmpty()) {
            productSearch.setResult(list);
            int cnt = productDao.cntBySearch(productSearch);
            productSearch.setTotalCnt(cnt);
        } else {
            productSearch.setTotalCnt(0);
        }
        return ApiResult.ok(productSearch);
    }

    @Override
    public ApiResult findProductOne(HttpServletRequest request, ProductSearch productSearch) {
        if (null == productSearch.getProType() || null == productSearch.getProductId()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        ProductDTO productDTO = productDao.findProductById(productSearch);
        if (null == productDTO) {
            throw new BaseException(ExceptionCode.NOT_THIS_PRODUCT);
        }

        return ApiResult.ok(productDTO);
    }

    @Override
    public ApiResult updateProductOnline(HttpServletRequest request, ProductSearch productSearch) {
        if (null == productSearch.getProductId() || null == productSearch.getOnline()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        ProductDTO productDTO = productDao.findProductById(productSearch);
        if (null == productDTO) {
            throw new BaseException(ExceptionCode.NOT_THIS_PRODUCT);
        }
        int res = productDao.updateProduct(productSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult updateProduct(HttpServletRequest request, ProductSearch productSearch) {
        if (null == productSearch.getNewPrice() || null == productSearch.getProductId()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }

        return null;
    }
}
