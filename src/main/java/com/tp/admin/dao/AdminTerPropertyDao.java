package com.tp.admin.dao;

import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.search.TerPropertySearch;

import java.util.List;
import java.util.Map;

public interface AdminTerPropertyDao {


    Integer findAllTerPropertyCount();

    AdminTerPropertyDTO findTerStartInfo(int terId);

    int updateTerProperty(AdminTerPropertyDTO adminTerPropertyDTO);

    List<AdminTerPropertyDTO> findAllTerProperty(TerPropertySearch terPropertySearch);

    int insertTerProperty(AdminTerPropertyDTO adminTerPropertyDTO);

}
