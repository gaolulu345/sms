package com.tp.admin.manage;

import com.tp.admin.data.entity.Order;

public interface MiniOrderPayManagerI {

    void aliPayBack(Order order);

    void wxinPayBack(Order order);

    void aliPayBackCredence(Order order);

    void wxinPayBackCredence(Order order);
}
