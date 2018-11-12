package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminMerchantEmployee;
import org.apache.ibatis.annotations.Param;

public interface AdminMerchantEmployeeDao {

    AdminMerchantEmployee findByWxMiniId(@Param("code") String code);

    AdminMerchantEmployee findByPhone(@Param("phone") String phone);

    int insert(AdminMerchantEmployee employee);

}
