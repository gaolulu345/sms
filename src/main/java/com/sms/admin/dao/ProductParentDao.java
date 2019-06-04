package com.sms.admin.dao;

import com.sms.admin.data.dto.ProductParentDTO;
import com.sms.admin.data.search.ProductParentSearch;

import java.util.List;

public interface ProductParentDao {

    List<ProductParentDTO> listBySearch(ProductParentSearch productParentSearch);

    int cntBySearch(ProductParentSearch productParentSearch);

    int updateProductParent(ProductParentSearch productParentSearch);

    int addProductParent(ProductParentSearch productParentSearch);
    /*

    List<OrderDTO> listBySearch(OrderSearch orderSearch);

    int updateOrder(OrderSearch orderSearch);

    int updateOrderDetail(OrderSearch orderSearch);

    int addOrder(OrderSearch orderSearch);

    int addOrderDetail(OrderSearch orderSearch);

    Long orderTatal(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("supplyIds") List<Integer> supplyIds);

    List<Map<String, Long>> findNumTotal(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("supplyIds") List<Integer> supplyIds);

    Long moneyTotal(@Param("startTime") String startTime, @Param("endTime") String
            endTime, @Param("supplyIds") List<Integer> supplyIds);*/
}
