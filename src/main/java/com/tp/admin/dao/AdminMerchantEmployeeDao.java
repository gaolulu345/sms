package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.search.MaintionEmployeeSearch;
import com.tp.admin.data.search.MerchantEmployeeSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMerchantEmployeeDao {

    AdminMerchantEmployee findByWxMiniId(@Param("code") String code);

    AdminMerchantEmployee findByPhone(@Param("phone") String phone);

    int insert(AdminMerchantEmployee employee);

    int cntBySearch(MerchantEmployeeSearch merchantEmployeeSearch);

    List<AdminMaintionEmployee> listBySearch(MerchantEmployeeSearch merchantEmployeeSearch);

    int bachUpdateDeleted(MerchantEmployeeSearch merchantEmployeeSearch);

    int updateEnable(Integer id , Integer partnerId);

}
