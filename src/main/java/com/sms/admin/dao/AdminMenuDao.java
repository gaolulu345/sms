package com.sms.admin.dao;

import com.sms.admin.data.entity.AdminMenu;
import com.sms.admin.data.search.SystemSearch;

import java.util.List;

public interface AdminMenuDao {

    AdminMenu findById(int id);

    int insert(AdminMenu sysMenu);

    int update(AdminMenu sysOperations);

    List<AdminMenu> list();

    List<AdminMenu> all();

    List<AdminMenu> listByAdminId(int adminId);

    List<AdminMenu> listByRolesIds(List<Integer> ids);

    List<AdminMenu> listBySearch(SystemSearch systemSearch);

    int cntBySearch(SystemSearch systemSearch);

    int bachUpdateEnable(SystemSearch systemSearch);

    int bachUpdateEnableAndOperationsEnable(SystemSearch systemSearch);

    int bachUpdateDeleted(SystemSearch systemSearch);

    int bachUpdateDeletedAndOperationsDeleted(SystemSearch systemSearch);

}
