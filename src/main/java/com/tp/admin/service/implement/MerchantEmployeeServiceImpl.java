package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminMerchantEmployeeDao;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.search.MerchantEmployeeSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.MerchantEmployeeServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class MerchantEmployeeServiceImpl implements MerchantEmployeeServiceI {

    @Autowired
    AdminMerchantEmployeeDao adminMerchantEmployeeDao;

    @Override
    public ApiResult list(HttpServletRequest request, MerchantEmployeeSearch merchantEmployeeSearch) {
        merchantEmployeeSearch.builData();
        List<AdminMaintionEmployee> list = adminMerchantEmployeeDao.listBySearch(merchantEmployeeSearch);
        if (null != list && !list.isEmpty()) {
            Integer cnt = adminMerchantEmployeeDao.cntBySearch(merchantEmployeeSearch);
            merchantEmployeeSearch.setResult(list);
            merchantEmployeeSearch.setTotalCnt(cnt);
        }else{
            merchantEmployeeSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(merchantEmployeeSearch));
    }

    @Override
    public ApiResult bachUpdateDeleted(HttpServletRequest request, MerchantEmployeeSearch merchantEmployeeSearch) {
        if (null == merchantEmployeeSearch.getIds() || merchantEmployeeSearch.getIds().length == 0 || null == merchantEmployeeSearch.getDeleted()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminMerchantEmployeeDao.bachUpdateDeleted(merchantEmployeeSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult updateEnable(HttpServletRequest request, MerchantEmployeeSearch merchantEmployeeSearch) {
        if (null == merchantEmployeeSearch.getId() || null == merchantEmployeeSearch.getPartnerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminMerchantEmployeeDao.updateEnable(merchantEmployeeSearch.getId(),merchantEmployeeSearch.getPartnerId());
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }
}
