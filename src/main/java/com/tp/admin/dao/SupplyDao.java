package com.tp.admin.dao;


import com.tp.admin.data.dto.SupplyDTO;
import com.tp.admin.data.search.SupplySearch;

import java.util.List;

public interface SupplyDao {

    int cntBySearch(SupplySearch supplySearch);

    List<SupplyDTO> listBySearch(SupplySearch supplySearch);

    int updateSupply(SupplySearch supplySearch);

    int addSupply(SupplySearch supplySearch);
}
