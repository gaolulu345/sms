package com.tp.admin.dao;

import com.tp.admin.data.dto.RefundDTO;
import com.tp.admin.data.search.RefundSearch;

import java.util.List;

public interface RefundDao {

    int cntBySearch(RefundSearch refundSearch);

    List<RefundDTO> listBySearch(RefundSearch refundSearch);
}
