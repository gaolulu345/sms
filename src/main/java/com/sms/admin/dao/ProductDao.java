package com.sms.admin.dao;

import com.sms.admin.data.dto.OrderDTO;
import com.sms.admin.data.dto.ProductDTO;
import com.sms.admin.data.entity.Product;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.data.search.ProductSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductDao {

    int cntBySearch(ProductSearch productSearch);

    List<ProductDTO> listBySearch(ProductSearch productSearch);

    ProductDTO findProductById(ProductSearch productSearch);

    int updateProductById(Product product);

    int updateProduct(ProductSearch productSearch);

    int addProduct(Product product);
    /*

    int updateOrder(OrderSearch orderSearch);

    int updateOrderDetail(OrderSearch orderSearch);

    int addOrder(OrderSearch orderSearch);

    int addOrderDetail(OrderSearch orderSearch);

    Long orderTatal(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("supplyIds") List<Integer> supplyIds);

    List<Map<String, Long>> findNumTotal(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("supplyIds") List<Integer> supplyIds);

    Long moneyTotal(@Param("startTime") String startTime, @Param("endTime") String
            endTime, @Param("supplyIds") List<Integer> supplyIds);*/
}
