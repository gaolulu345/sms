package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminMaintionEmployeeDao;
import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.search.MaintionEmployeeSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.MaintionEmployeeServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class MaintionEmployeeServiceImpl implements MaintionEmployeeServiceI {

    @Autowired
    AdminMaintionEmployeeDao adminMaintionEmployeeDao;

    @Override
    public ApiResult list(HttpServletRequest request, MaintionEmployeeSearch maintionEmployeeSearch) {
        maintionEmployeeSearch.builData();
        List<AdminMaintionEmployee> list = adminMaintionEmployeeDao.listBySearch(maintionEmployeeSearch);
        if (null != list && !list.isEmpty()) {
            Integer cnt = adminMaintionEmployeeDao.cntBySearch(maintionEmployeeSearch);
            maintionEmployeeSearch.setResult(list);
            maintionEmployeeSearch.setTotalCnt(cnt);
        }else{
            maintionEmployeeSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(maintionEmployeeSearch));
    }

    @Override
    public ApiResult bachUpdateDeleted(HttpServletRequest request, MaintionEmployeeSearch maintionEmployeeSearch) {
        if (null == maintionEmployeeSearch.getIds() || maintionEmployeeSearch.getIds().length == 0 || null == maintionEmployeeSearch.getDeleted()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminMaintionEmployeeDao.bachUpdateDeleted(maintionEmployeeSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult bachUpdateEnable(HttpServletRequest request, MaintionEmployeeSearch maintionEmployeeSearch) {
        if (null == maintionEmployeeSearch.getIds() || maintionEmployeeSearch.getIds().length == 0 || null ==
                maintionEmployeeSearch.getEnable()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminMaintionEmployeeDao.bachUpdateEnable(maintionEmployeeSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

}
