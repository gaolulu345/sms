package com.sms.admin.service.implement;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.SupplySearch;
import com.sms.admin.dao.SupplyDao;
import com.sms.admin.data.dto.SupplyDTO;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.service.SupplyServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class SupplyServiceImpl implements SupplyServiceI {
    @Autowired
    SupplyDao supplyDao;

    @Override
    public ApiResult supplyList(HttpServletRequest request, SupplySearch supplySearch) {
        supplySearch.build();
        List<SupplyDTO> list = supplyDao.listBySearch(supplySearch);
        if (null != list && !list.isEmpty()) {
            int cnt = supplyDao.cntBySearch(supplySearch);
            supplySearch.setResult(list);
            supplySearch.setTotalCnt(cnt);
        }else {
            supplySearch.setTotalCnt(0);
        }
        return ApiResult.ok(supplySearch);
    }

    @Override
    public ApiResult supplyDetail(HttpServletRequest request, SupplySearch supplySearch) {
        if (null == supplySearch.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        List<SupplyDTO> list = supplyDao.listBySearch(supplySearch);
        SupplyDTO supplyDTO = null;
        if (null != list && !list.isEmpty()) {
            supplyDTO = list.get(0);
        }
        return ApiResult.ok(supplyDTO);
    }

    @Override
    public ApiResult updateSupplyDetail(HttpServletRequest request, SupplySearch supplySearch) {
        if (null == supplySearch.getId() ||
                null == supplySearch.getSupplyName() ||
                null == supplySearch.getContactName() ||
                null == supplySearch.getContactPhone() ||
                null == supplySearch.getContactAddress() ||
                null == supplySearch.getFax() ||
                null == supplySearch.getIntros()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        int res = supplyDao.updateSupply(supplySearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult updateSupplyDeleted(HttpServletRequest request, SupplySearch supplySearch) {
        if (null == supplySearch.getId() || null == supplySearch.getDeleted()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        int res = supplyDao.updateSupply(supplySearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult addSupply(HttpServletRequest request, SupplySearch supplySearch) {
        if (null == supplySearch.getSupplyCode() ||
                null == supplySearch.getSupplyName() ||
                null == supplySearch.getContactName() ||
                null == supplySearch.getContactPhone() ||
                null == supplySearch.getContactAddress() ||
                null == supplySearch.getFax() ||
                null == supplySearch.getIntros()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        int res = supplyDao.addSupply(supplySearch);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }
}
