package com.tp.admin.dao;

import com.tp.admin.data.dto.AdminMaintionEmployeeLogTerOperatingDTO;
import com.tp.admin.data.entity.AdminMaintionEmployeeLogTerOperating;
import com.tp.admin.data.parameter.WxMiniSearch;

import java.util.List;

public interface AdminMaintionEmployeeLogTerOperatingDao {

    int insert(AdminMaintionEmployeeLogTerOperating adminMaintionEmployeeLogTerOperating);

    List<AdminMaintionEmployeeLogTerOperatingDTO> listBySearch(WxMiniSearch wxMiniSearch);

    Integer cntBySearch(WxMiniSearch wxMiniSearch);
}
