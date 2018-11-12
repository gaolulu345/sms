package com.tp.admin.dao;

import com.tp.admin.data.dto.AdminTerOperatingLogDTO;
import com.tp.admin.data.entity.AdminTerOperatingLog;
import com.tp.admin.data.parameter.WxMiniSearch;

import java.util.List;

public interface AdminTerOperatingLogDao {

    int insert(AdminTerOperatingLog adminTerOperatingLog);

    List<AdminTerOperatingLogDTO> listMaintionEmployeeLogBySearch(WxMiniSearch wxMiniSearch);

    Integer cntMaintionEmployeeLogBySearch(WxMiniSearch wxMiniSearch);

    List<AdminTerOperatingLogDTO> listMerchantEmployeeLogBySearch(WxMiniSearch wxMiniSearch);

    Integer cntMerchantEmployeeLogBySearch(WxMiniSearch wxMiniSearch);


}
