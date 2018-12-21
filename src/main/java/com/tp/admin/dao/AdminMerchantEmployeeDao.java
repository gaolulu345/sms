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

    AdminMerchantEmployee findById(@Param("id") Integer id);

    int insert(AdminMerchantEmployee employee);

    int cntBySearch(MerchantEmployeeSearch merchantEmployeeSearch);

    List<AdminMaintionEmployee> listBySearch(MerchantEmployeeSearch merchantEmployeeSearch);

    int bachUpdateDeleted(MerchantEmployeeSearch merchantEmployeeSearch);

    int updateEnable(@Param("id") Integer id , @Param("enable") Boolean enable , @Param("partnerId") Integer partnerId);

    int updateMerchantLoginTime(AdminMerchantEmployee adminMerchantEmployee);

    List<AdminMerchantEmployee> findByIdsBach(MerchantEmployeeSearch merchantEmployeeSearch);
}
