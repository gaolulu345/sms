package com.sms.admin.dao;

import com.sms.admin.data.dto.OrderDTO;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.data.search.RangeSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    int cntBySearch(OrderSearch orderSearch);

    List<OrderDTO> listBySearch(OrderSearch orderSearch);

    int updateOrder(OrderSearch orderSearch);

    int updateOrderDetail(OrderSearch orderSearch);

    int addOrder(OrderSearch orderSearch);

    int addOrderDetail(OrderSearch orderSearch);

    Long orderTatal(@Param("startTime") String startTime, @Param("endTime") String
            endTime);
    List<Map<String, Long>> findNumTotal(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("supplyIds") List<Integer> supplyIds);
}
