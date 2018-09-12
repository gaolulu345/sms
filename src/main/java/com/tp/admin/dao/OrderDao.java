package com.tp.admin.dao;

import com.tp.admin.data.dto.OrderDTO;
import com.tp.admin.data.search.OrderSearch;

import java.util.List;

public interface OrderDao {

    int cntBySearch(OrderSearch orderSearch);

    List<OrderDTO> listBySearch(OrderSearch orderSearch);

    OrderDTO findOrderDTOById(int id);

}
