package com.tp.admin.dao;

import com.tp.admin.data.dto.OrderDTO;
import com.tp.admin.data.entity.Order;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.search.OrderSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDao {

    int cntBySearch(OrderSearch orderSearch);

    List<OrderDTO> listBySearch(OrderSearch orderSearch);

    OrderDTO findOrderDTOById(int id);

    Order findById(int id);

    int updateOrderStatus(@Param("orderId") int orderId , @Param("status") int status);

    Long orderTatal(@Param("status") int status, @Param("startTime") String startTime, @Param("endTime") String
            endTime, @Param("ids") List<Integer> ids);

    Long moneyTatal(@Param("status") int status, @Param("startTime") String startTime, @Param("endTime") String
            endTime, @Param("ids") List<Integer> ids);

}
