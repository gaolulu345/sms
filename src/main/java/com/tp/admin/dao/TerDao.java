package com.tp.admin.dao;

import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.parameter.WxMiniSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TerDao {

    List<Map<String, Object>> findAllTerIdAndTitle();

    List<Integer> listTerCityId();

    List<Integer> findRelatedTerByPartnerId(Integer id);

    List<TerInfoDTO> terInfoSearch(WxMiniSearch wxMiniTerSearch);

    Integer cntTerInfoSearch(WxMiniSearch wxMiniTerSearch);





}
