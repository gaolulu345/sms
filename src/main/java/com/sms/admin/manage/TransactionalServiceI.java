package com.sms.admin.manage;

import com.sms.admin.data.entity.*;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.data.search.PurchaseOrderSearch;

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


    void updateOrder(OrderSearch orderSearch);

    void insertOrder(OrderSearch orderSearch);

    void addPurchaseOrder(Product product, PurchaseOrderSearch purchaseOrderSearch);
}
