package com.tp.admin.manage;

import com.tp.admin.data.entity.AdminPkRolesMenu;
import com.tp.admin.data.entity.AdminPkRolesOperations;

import java.util.List;

public interface TransactionalServiceI {

    /**
     * 批量新增或者更新角色操作中间表
     * @param sysPkRolesOperations
     * @return
     */
    void bachInsertAndUpdateSysPkRolesOperations(List<AdminPkRolesOperations> sysPkRolesOperations);

    /**
     * 批量新增或者更新角色菜单中间表
     * @param sysPkRolesMenus
     * @return
     */
    void bachInsertAndUpdateSysPkRolesMenu(List<AdminPkRolesMenu> sysPkRolesMenus);


}
