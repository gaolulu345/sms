package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminOperations;
import com.tp.admin.data.search.SystemSearch;

import java.util.List;

public interface AdminOperationsDao {

    AdminOperations findById(int id);

    int insert(AdminOperations sysOperations);

    int update(AdminOperations sysOperations);

    List<AdminOperations> list();

    List<AdminOperations> all();

    List<AdminOperations> listByAdminId(int partnerId);

    List<AdminOperations> listByRolesIds(List<Integer> ids);

    List<AdminOperations> listBySearch(SystemSearch systemSearch);

    int cntBySearch(SystemSearch systemSearch);

    int bachUpdateEnable(SystemSearch systemSearch);

    int bachUpdateDeleted(SystemSearch systemSearch);
}
