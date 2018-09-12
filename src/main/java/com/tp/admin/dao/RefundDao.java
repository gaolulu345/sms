package com.tp.admin.dao;

import com.tp.admin.data.entity.Refund;
import com.tp.admin.data.search.RefundSearch;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface RefundDao {

    int cntBySearch(RefundSearch refundSearch);

    List<Refund> listBySearch(RefundSearch refundSearch);

    Refund findById(int id);

    int update(Refund refund);

}
