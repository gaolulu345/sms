package com.tp.admin.manage;

import com.tp.admin.data.entity.Order;

public interface OrderPayManagerI {

    void aliPayBack(boolean miniApp , Order order);

    void wxinPayBack(boolean miniApp , Order order);
}
