package com.tp.admin.dao;

import com.tp.admin.data.entity.Sabis;

public interface SabisDao {

    Sabis findByTerId(int terId);
    
}
