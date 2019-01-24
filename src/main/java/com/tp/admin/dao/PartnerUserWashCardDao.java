package com.tp.admin.dao;


import com.tp.admin.data.dto.PartnerUserWashCardDTO;
import com.tp.admin.data.dto.PartnerUserWashCardDetailDTO;
import com.tp.admin.data.entity.PartnerUserWashCard;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface PartnerUserWashCardDao {

    PartnerUserWashCardDetailDTO findById(Integer id);

    int updateCardInvalid(@Param("timestamp") Timestamp timestamp);

    int updateCardInvalidByCnt();

    int recoveryCardInvalid(Integer id);

    Integer addUpdateCnt(Integer userId);

}
