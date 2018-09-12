package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.OrderDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.OrderDTO;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.service.WashOrderServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class WashOrderServiceImpl implements WashOrderServiceI {

    @Autowired
    OrderDao orderDao;

    @Autowired
    TerDao terDao;

    @Override
    public ApiResult list(HttpServletRequest request, OrderSearch orderSearch) {
        orderSearch.build();
        List<OrderDTO> list = orderDao.listBySearch(orderSearch);
        if (null != list && !list.isEmpty()) {
            for (OrderDTO o : list){
                o.build();
            }
        }
        int cnt = orderDao.cntBySearch(orderSearch);
        orderSearch.setResult(list);
        orderSearch.setTotalCnt(cnt);
        return ApiResult.ok(orderSearch);
    }

    @Override
    public ApiResult orderTerSelection(HttpServletRequest request) {
        return ApiResult.ok(terDao.findAllTerIdAndTitle());
    }
}
