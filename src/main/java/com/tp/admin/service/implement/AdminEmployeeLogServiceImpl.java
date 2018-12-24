package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminEmployeeOperatingLogDao;
import com.tp.admin.data.entity.AdminEmployeeOperatingLog;
import com.tp.admin.data.search.AdminEmployeeSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.service.AdminEmployeeLogServiceI;
import com.tp.admin.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminEmployeeLogServiceImpl implements AdminEmployeeLogServiceI {

    @Autowired
    AdminEmployeeOperatingLogDao adminEmployeeOperatingLogDao;

    @Override
    public ApiResult list(HttpServletRequest request, AdminEmployeeSearch adminEmployeeSearch) {
        adminEmployeeSearch.build();
        if (adminEmployeeSearch.getDays() != null && adminEmployeeSearch.getDays() != 0){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String endTime = simpleDateFormat.format(new Date());
            String startTime = TimeUtil.getStartTime(adminEmployeeSearch.getDays());
            adminEmployeeSearch.setStartTime(startTime);
            adminEmployeeSearch.setEndTime(endTime);
        }
        List<AdminEmployeeOperatingLog> list = adminEmployeeOperatingLogDao.list(adminEmployeeSearch);
        if (list != null && list.size() != 0){
            Integer cnt = adminEmployeeOperatingLogDao.cntOfEmployeeLog(adminEmployeeSearch);
            adminEmployeeSearch.setTotalCnt(cnt);
            adminEmployeeSearch.setResult(list);
        }else {
            adminEmployeeSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(adminEmployeeSearch));
    }
}
