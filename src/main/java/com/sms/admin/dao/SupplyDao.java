package com.sms.admin.dao;


import com.sms.admin.data.search.SupplySearch;
import com.sms.admin.data.dto.SupplyDTO;

import java.util.List;

public interface SupplyDao {

    int cntBySearch(SupplySearch supplySearch);

    List<SupplyDTO> listBySearch(SupplySearch supplySearch);

    int updateSupply(SupplySearch supplySearch);

    int addSupply(SupplySearch supplySearch);

    List<SupplyDTO> listAllSupply();

    List<Integer> findSupplyIds();
}
