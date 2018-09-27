package com.tp.admin.manage;

import com.tp.admin.data.entity.*;

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

    /**
     * 注册员工
     * @param adminAccount
     * @param adminRoles
     */
    void register(AdminAccount adminAccount , AdminRoles adminRoles);


    /**
     * 退款
     * @param refund 退款申请
     * @param order  订单信息
     */
    void payBack(Refund refund , Order order);

}
