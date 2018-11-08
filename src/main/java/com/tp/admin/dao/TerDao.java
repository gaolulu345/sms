package com.tp.admin.dao;

import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.parameter.WxMiniSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TerDao {

    List<Map<String, Object>> findAllTerIdAndTitle();

    List<Integer> listTerCityId();

    List<TerInfoDTO> terInfoSearch(WxMiniSearch wxMiniTerSearch);

    Integer cntTerInfoSearch(WxMiniSearch wxMiniTerSearch);

    Integer updateOnline(@Param("id") int id);

    Integer updateOffline(@Param("id") int id, @Param("offlineDesc") String offlineDesc);

    Integer updateStateDefault(@Param("id") int id);

}
