package com.tp.admin.manage;

import com.tp.admin.data.entity.Order;

public interface OrderPayManagerI {

    void aliPayBack(Order order);

    void wxinPayBack(Order order);
}
