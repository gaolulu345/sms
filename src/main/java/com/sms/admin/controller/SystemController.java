package com.sms.admin.controller;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.entity.AdminMenu;
import com.sms.admin.data.entity.AdminOperations;
import com.sms.admin.data.entity.AdminRoles;
import com.sms.admin.data.search.SystemSearch;
import com.sms.admin.service.SystemServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(SystemController.ROUTER_INDEX)
public class SystemController {

    public static final String ROUTER_INDEX = "/api/private/sys";

    @Autowired
    SystemServiceI systemService;

    /**
     * 获取系统所有请求地址信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/operations/maps")
    public ApiResult findOperationsMaps(HttpServletRequest request) {
        return systemService.findOperationsMaps(request);
    }

    /**
     * 获取系统所有请求地址信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/menu/maps")
    public ApiResult findMenuMaps(HttpServletRequest request) {
        return systemService.findMenuMaps(request);
    }

    /**
     * 添加菜单
     * @param adminMenu
     * @return
     */
    @PostMapping(value = "/save/menu")
    public ApiResult insertSysMenu(HttpServletRequest request, @RequestBody AdminMenu adminMenu) {
        return systemService.insert(request, adminMenu);
    }

    /**
     * 更新菜单
     * @param adminMenu
     * @return
     */
    @PostMapping(value = "/update/menu")
    public ApiResult updateAdminMenu(HttpServletRequest request, @RequestBody AdminMenu adminMenu) {
        return systemService.update(request, adminMenu);
    }

    /**
     * 菜单列表
     *
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/list/menu")
    public ApiResult listSysMenu(HttpServletRequest request, @RequestBody SystemSearch systemSearch) {
        return systemService.listSysMenuBySearch(request, systemSearch);
    }

    /**
     * 批量修改菜单启用状态
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/update/menu/enable")
    public ApiResult bachUpdateSysMenuEnable(HttpServletRequest request, @RequestBody SystemSearch systemSearch) {
        return systemService.bachUpdateSysMenuEnable(request, systemSearch);
    }

    /**
     * 批量修改菜单删除状态
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/update/menu/deleted")
    public ApiResult bachUpdateSysMenuDeleted(HttpServletRequest request, @RequestBody SystemSearch systemSearch) {
        return systemService.bachUpdateSysMenuDeleted(request, systemSearch);
    }

    /**
     * 添加操作
     * @param adminOperations
     * @return
     */
    @PostMapping(value = "/save/operations")
    public ApiResult insertSysOperations(HttpServletRequest request, @RequestBody AdminOperations adminOperations) {
        return systemService.insert(request, adminOperations);
    }

    /**
     * 更新操作
     * @param adminOperations
     * @return
     */
    @PostMapping(value = "/update/operations")
    public ApiResult update(HttpServletRequest request, @RequestBody AdminOperations adminOperations) {
        return systemService.update(request, adminOperations);
    }

    /**
     * 根据菜单获取操作列表
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/list/operations")
    public ApiResult listSysOperations(HttpServletRequest request, @RequestBody SystemSearch systemSearch) {
        return systemService.listSysOperationsBySearch(request, systemSearch);
    }

    /**
     * 批量修改操作启用状态
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/update/operations/enable")
    public ApiResult bachUpdateSysOperationsEnable(HttpServletRequest request, @RequestBody SystemSearch systemSearch) {
        return systemService.bachUpdateSysOperationsEnable(request, systemSearch);
    }

    /**
     * 批量修改操作删除状态
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/update/operations/deleted")
    public ApiResult bachUpdateSysOperationsDeleted(HttpServletRequest request, @RequestBody SystemSearch systemSearch) {
        return systemService.bachUpdateSysOperationsDeleted(request, systemSearch);
    }

    /**
     * 添加角色
     * @param adminRoles
     * @return
     */
    @PostMapping(value = "/save/roles")
    public ApiResult insertSysRoles(HttpServletRequest request, @RequestBody AdminRoles adminRoles) {
        return systemService.insert(request, adminRoles);
    }

    /**
     * 更新角色
     * @param adminRoles
     * @return
     */
    @PostMapping(value = "/update/roles")
    public ApiResult update(HttpServletRequest request, @RequestBody AdminRoles adminRoles) {
        return systemService.update(request, adminRoles);
    }

    /**
     * 角色列表
     *
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/list/roles")
    public ApiResult listSysSysRoles(HttpServletRequest request, @RequestBody SystemSearch systemSearch) {
        return systemService.listSysRolesBySearch(request, systemSearch);
    }

    /**
     * 批量更新角色启用状态
     *
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/update/roles/enable")
    public ApiResult bachUpdateSysRolesEnable(HttpServletRequest request, @RequestBody SystemSearch systemSearch) {
        return systemService.bachUpdateSysRolesEnable(request, systemSearch);
    }

    /**
     * 批量更新角色删除状态
     *
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/update/roles/deleted")
    public ApiResult bachUpdateSysRolesDeleted(HttpServletRequest request, @RequestBody SystemSearch systemSearch) {
        return systemService.bachUpdateSysRolesDeleted(request, systemSearch);
    }


    /**
     * 获取角色所有权限
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/all/roles/permission")
    public ApiResult findAllPermission(HttpServletRequest request ,@RequestBody SystemSearch systemSearch){
        return systemService.findAllPermission(request,systemSearch);
    }

    /**
     * 更新角色菜单权限
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/update/roles/menu")
    public ApiResult updateRolesMenu(HttpServletRequest request ,@RequestBody SystemSearch systemSearch){
        return systemService.bachUpdateRolesMenu(request,systemSearch);
    }

    /**
     * 更新角色操作权限
     * @param systemSearch
     * @return
     */
    @PostMapping(value = "/update/roles/operations")
    public ApiResult updateRolesOperations(HttpServletRequest request ,@RequestBody SystemSearch systemSearch){
        return systemService.bachUpdateRolesOperations(request,systemSearch);
    }

    /**
     * 获取账号所有权限  已用
     * @return
     */
    @PostMapping(value = "/all/permission")
    public ApiResult adminAllPermission(HttpServletRequest request){
        return systemService.adminAllPermission(request);
    }
}
