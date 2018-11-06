package com.tp.admin.dao;

import java.util.List;
import java.util.Map;

public interface TerDao {

    List<Map<String, Object>> findAllTerIdAndTitle();

    List<Integer> listTerCityId();

}
