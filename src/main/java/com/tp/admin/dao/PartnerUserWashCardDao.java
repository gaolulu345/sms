package com.tp.admin.dao;


import com.tp.admin.data.dto.PartnerUserWashCardDetailDTO;
import org.apache.ibatis.annotations.Param;
import java.sql.Timestamp;

public interface PartnerUserWashCardDao {

    PartnerUserWashCardDetailDTO findById(Integer id);

    int updateCardInvalid(@Param("timestamp") Timestamp timestamp);

    int updateCardInvalidByCnt();

    int recoveryCardInvalid(Integer id);

    Integer addUpdateCnt(Integer userId);

}
