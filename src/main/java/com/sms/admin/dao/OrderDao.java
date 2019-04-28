package com.sms.admin.dao;

import com.sms.admin.data.dto.OrderDTO;
import com.sms.admin.data.search.OrderSearch;

import java.util.List;

public interface OrderDao {

    int cntBySearch(OrderSearch orderSearch);

    List<OrderDTO> listBySearch(OrderSearch orderSearch);

    int updateOrder(OrderSearch orderSearch);

    int updateOrderDetail(OrderSearch orderSearch);

    int addOrder(OrderSearch orderSearch);

    int addOrderDetail(OrderSearch orderSearch);
}
