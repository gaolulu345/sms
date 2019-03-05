package com.tp.admin.dao;


import com.tp.admin.data.entity.TerFaultInfo;
import com.tp.admin.data.parameter.WxMiniSearch;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TerFaultOperationDao {

    int insert(TerFaultInfo terFaultInfo);

    //根据网点编号查询最后一条故障信息
    TerFaultInfo selectLastByCode(@Param("code") String code, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<TerFaultInfo> terFaultReportList(WxMiniSearch wxMiniSearch);

    int cntTerFaultReportList(WxMiniSearch wxMiniSearc);
}
