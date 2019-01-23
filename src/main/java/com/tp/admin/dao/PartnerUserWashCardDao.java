package com.tp.admin.dao;


import com.tp.admin.data.dto.PartnerUserWashCardDTO;
import com.tp.admin.data.dto.PartnerUserWashCardDetailDTO;
import com.tp.admin.data.entity.PartnerUserWashCard;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface PartnerUserWashCardDao {

    PartnerUserWashCardDetailDTO findById(Integer id);

    int insert(PartnerUserWashCard partnerUserWashCard);

    int updateCardInvalid(@Param("timestamp") Timestamp timestamp);

    int updateCardInvalidByCnt();

    int recoveryCardInvalid(Integer id);

    List<PartnerUserWashCardDTO> listPartnerUserWashCardDTOByUserId(Integer userId);

    Integer cntPartnerUserWashCardDTOByUserId(Integer userId);

    Integer addUpdateCnt(Integer userId);

    PartnerUserWashCardDTO findPartnerUserWashCardDTOByUserCardId(Integer id);

    PartnerUserWashCardDTO findPartnerUserWashCardDTOByCardIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);

    List<PartnerUserWashCardDTO> listPartnerUserWashCardDTOByUserIdAndTerId(@Param("userId") Integer userId, @Param("terId") Integer terId);


}
