package com.tp.admin.dao;

import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.parameter.WxMiniTerSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TerDao {

    List<Map<String, Object>> findAllTerIdAndTitle();

    List<Integer> listTerCityId();

    List<TerInfoDTO> terInfoSearch(WxMiniTerSearch wxMiniTerSearch);

    Integer cntTerInfoSearch(WxMiniTerSearch wxMiniTerSearch);

    Integer updateOnline(int id);

    Integer updateOffline(int id, String offlineDesc);

}
