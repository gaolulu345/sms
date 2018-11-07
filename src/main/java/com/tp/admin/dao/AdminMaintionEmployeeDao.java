package com.tp.admin.dao;

import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.parameter.WxMiniRegisterDTO;
import com.tp.admin.data.search.AdminSearch;
import com.tp.admin.data.search.MaintionEmployeeSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMaintionEmployeeDao {

    AdminMaintionEmployee findByWxMiniId(@Param("code") String code);

    AdminMaintionEmployee findByPhone(@Param("phone") String phone);

    int insert(AdminMaintionEmployee employee);

    int cntBySearch(MaintionEmployeeSearch maintionEmployeeSearch);

    List<AdminMaintionEmployee> listBySearch(MaintionEmployeeSearch maintionEmployeeSearch);

    int bachUpdateDeleted(MaintionEmployeeSearch maintionEmployeeSearch);

    int bachUpdateEnable(MaintionEmployeeSearch maintionEmployeeSearch);

}
