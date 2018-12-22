package com.tp.admin.dao;

import com.tp.admin.data.dto.PartnerWashCardDTO;
import com.tp.admin.data.entity.Partner;
import com.tp.admin.data.search.PartnerSearch;
import com.tp.admin.data.search.UserSearch;

import java.util.List;

public interface PartnerDao {

    Partner findById(Integer id);

    List<Partner> listBySearch(PartnerSearch partnerSearch);

    Integer cntBySearch(PartnerSearch partnerSearch);

    List<PartnerWashCardDTO> partnerWashCardIdSearch(int createPartnerId);


}
