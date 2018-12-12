package com.tp.admin.dao;

import com.tp.admin.data.dto.AdminTerOperatingLogDTO;
import com.tp.admin.data.entity.AdminTerOperatingLog;
import com.tp.admin.data.parameter.WxMiniSearch;

import java.util.List;

public interface AdminTerOperatingLogDao {

    int insert(AdminTerOperatingLog adminTerOperatingLog);

    List<AdminTerOperatingLogDTO> listLogBySearch(WxMiniSearch wxMiniSearch);

    Integer cntBySearch(WxMiniSearch wxMiniSearch);

}
