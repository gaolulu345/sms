package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.PartnerDao;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.entity.Partner;
import com.tp.admin.data.search.PartnerSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.service.PartnerServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class PartnerServiceImpl implements PartnerServiceI {

    @Autowired
    PartnerDao partnerDao;

    @Override
    public ApiResult list(HttpServletRequest request, PartnerSearch partnerSearch) {
        partnerSearch.builData();
        List<Partner> list = partnerDao.listBySearch(partnerSearch);
        if (null != list && !list.isEmpty()) {
            Integer cnt = partnerDao.cntBySearch(partnerSearch);
            partnerSearch.setResult(list);
            partnerSearch.setTotalCnt(cnt);
        }else{
            partnerSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(partnerSearch));
    }
}
