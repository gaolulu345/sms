package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.TerLogDao;
import com.tp.admin.data.dto.OrderDTO;
import com.tp.admin.data.entity.TerLog;
import com.tp.admin.data.search.WashLogSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.service.WashLogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class WashLogServiceImpl implements WashLogServiceI {

    @Autowired
    TerLogDao terLogDao;

    @Override
    public ApiResult list(HttpServletRequest request, WashLogSearch washLogSearch) {
        washLogSearch.builData();
        List<TerLog> list = terLogDao.listBySearch(washLogSearch);
        if (null != list && !list.isEmpty()) {
            washLogSearch.setResult(list);
            int cnt = terLogDao.cntBySearch(washLogSearch);
            washLogSearch.setTotalCnt(cnt);
        }else {
            washLogSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(washLogSearch));
    }
}
