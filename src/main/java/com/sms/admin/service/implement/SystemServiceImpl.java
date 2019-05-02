package com.sms.admin.service.implement;

import com.sms.admin.dao.*;
import com.sms.admin.data.entity.*;
import com.sms.admin.data.table.ResultTable;
import com.sms.admin.ajax.ApiResult;
import com.sms.admin.common.Constant;
import com.sms.admin.data.dto.AutoResourceDTO;
import com.sms.admin.data.dto.PermissionDTO;
import com.sms.admin.data.dto.UserAutoResourceDTO;
import com.sms.admin.data.search.SystemSearch;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.manage.TransactionalServiceI;
import com.sms.admin.security.AuthResourceTypeEnum;
import com.sms.admin.security.AutoResource;
import com.sms.admin.service.SystemServiceI;
import com.sms.admin.utils.RedisUtil;
import com.sms.admin.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class SystemServiceImpl implements SystemServiceI {

    @Autowired
    AdminMenuDao adminMenuDao;

    @Autowired
    AdminOperationsDao adminOperationsDao;

    @Autowired
    AdminRolesDao adminRolesDao;

    @Autowired
    AdminPkRolesMenuDao adminPkRolesMenuDao;

    @Autowired
    AdminPkRolesOperationsDao adminPkRolesOperationsDao;

    @Autowired
    AdminPkAccountRolesDao adminPkAccountRolesDao;

    @Autowired
    TransactionalServiceI transactionalService;

    @Autowired
    RedisUtil redisUtil;
    @Override
    public ApiResult findOperationsMaps(HttpServletRequest request) {
        WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        RequestMappingHandlerMapping rmhp = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        List list = new ArrayList();
        for (RequestMappingInfo info : map.keySet()) {
            Set<String> url = info.getPatternsCondition().getPatterns();
            for (String u : url) {
                if (u.indexOf("/api/private", 0) == 0) {
                    list.add(u);
                }
            }
        }
        List<AdminOperations> adminOperations = adminOperationsDao.all();
        if (null != adminOperations && !adminOperations.isEmpty()) {
            for (AdminOperations o : adminOperations) {
                Iterator<String> idsIterator = list.iterator();
                while (idsIterator.hasNext()) {
                    String e = idsIterator.next();
                    if (e.equals(o.getResource())) {
                        idsIterator.remove();
                    }
                }
            }
        }
        return ApiResult.ok(list);
    }

    @Override
    public ApiResult findMenuMaps(HttpServletRequest request) {
        WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        RequestMappingHandlerMapping rmhp = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        List list = new ArrayList();
        for (RequestMappingInfo info : map.keySet()) {
            Set<String> url = info.getPatternsCondition().getPatterns();
            for (String u : url) {
                if (u.indexOf("/pages", 0) == 0) {
                    list.add(u);
                }
            }
        }
        List<AdminMenu> adminMenuss = adminMenuDao.all();
        if (null != adminMenuss && !adminMenuss.isEmpty()) {
            for (AdminMenu m : adminMenuss) {
                Iterator<String> idsIterator = list.iterator();
                while (idsIterator.hasNext()) {
                    String e = idsIterator.next();
                    if (e.equals(m.getResource())) {
                        idsIterator.remove();
                    }
                }
            }
        }
        return ApiResult.ok(list);
    }

    @Override
    public void verify(AdminMenu adminMenu) {

    }

    @Override
    public ApiResult insert(HttpServletRequest request, AdminMenu adminMenu) {
        verify(adminMenu);
        int res = adminMenuDao.insert(adminMenu);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult update(HttpServletRequest request, AdminMenu adminMenu) {
        if (0 >= adminMenu.getId()) {
            return ApiResult.error(ExceptionCode.PARAMETER_WRONG, "upload menu with illegal id");
        }
        int res = adminMenuDao.update(adminMenu);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult listSysMenuBySearch(HttpServletRequest request, SystemSearch systemSearch) {
        systemSearch.builData();
        List<AdminMenu> list = adminMenuDao.listBySearch(systemSearch);
        if (null != list && !list.isEmpty()) {
            int cnt = adminMenuDao.cntBySearch(systemSearch);
            systemSearch.setResult(list);
            systemSearch.setTotalCnt(cnt);
        } else {
            systemSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(systemSearch));
    }

    @Override
    public ApiResult bachUpdateSysMenuEnable(HttpServletRequest request, SystemSearch systemSearch) {
        // 校验参数
        int[] ids = systemSearch.getIds();
        if (null == ids || ids.length == 0) {
            return ApiResult.error(ExceptionCode.PARAMETER_WRONG);
        }
        int res = 0;
        // 如果是启用就只更新菜单表,如果是作废就同时更新菜单对应的操作表。
        if (systemSearch.isEnable()) {
            res = adminMenuDao.bachUpdateEnable(systemSearch);
        } else {
            res = adminMenuDao.bachUpdateEnableAndOperationsEnable(systemSearch);
        }
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult bachUpdateSysMenuDeleted(HttpServletRequest request, SystemSearch systemSearch) {
        // 校验参数
        int[] ids = systemSearch.getIds();
        if (null == ids || ids.length == 0) {
            return ApiResult.error(ExceptionCode.PARAMETER_WRONG);
        }
        int res = 0;
        if (systemSearch.isDeleted()) {
            res = adminMenuDao.bachUpdateDeletedAndOperationsDeleted(systemSearch);
        } else {
            res = adminMenuDao.bachUpdateDeleted(systemSearch);
        }
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public void verify(AdminOperations adminOperations) {
        int menuId = adminOperations.getMenuId();
        AdminMenu adminMenu = adminMenuDao.findById(menuId);
        if (null == adminMenu) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
    }

    @Override
    public ApiResult insert(HttpServletRequest request, AdminOperations adminOperations) {
        verify(adminOperations);
        int res = adminOperationsDao.insert(adminOperations);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult update(HttpServletRequest request, AdminOperations adminOperations) {
        int id = adminOperations.getId();
        if (0 >= id) {
            return ApiResult.error(ExceptionCode.PARAMETER_WRONG, "upload roles with illegal id");
        }
        int menuId = adminOperations.getMenuId();
        AdminOperations oldSysOperations = adminOperationsDao.findById(id);
        if (null == oldSysOperations) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMenu sysMenu = adminMenuDao.findById(menuId);
        if (null == sysMenu) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminOperationsDao.update(adminOperations);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult listSysOperationsBySearch(HttpServletRequest request, SystemSearch systemSearch) {
        systemSearch.builData();
        List<AdminOperations> list = adminOperationsDao.listBySearch(systemSearch);
        if (null != list && !list.isEmpty()) {
            int cnt = adminOperationsDao.cntBySearch(systemSearch);
            systemSearch.setResult(list);
            systemSearch.setTotalCnt(cnt);
        } else {
            systemSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(systemSearch));
    }

    @Override
    public ApiResult bachUpdateSysOperationsEnable(HttpServletRequest request, SystemSearch systemSearch) {
        // 校验参数
        int[] ids = systemSearch.getIds();
        if (null == ids || ids.length == 0) {
            return ApiResult.error(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminOperationsDao.bachUpdateEnable(systemSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult bachUpdateSysOperationsDeleted(HttpServletRequest request, SystemSearch systemSearch) {
        // 校验参数
        int[] ids = systemSearch.getIds();
        if (null == ids || ids.length == 0) {
            return ApiResult.error(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminOperationsDao.bachUpdateDeleted(systemSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public void verify(AdminRoles adminRoles) {

    }

    @Override
    public ApiResult insert(HttpServletRequest request, AdminRoles adminRoles) {
        verify(adminRoles);
        int res = adminRolesDao.insert(adminRoles);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult update(HttpServletRequest request, AdminRoles adminRoles) {
        if (0 >= adminRoles.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminRoles roles = adminRolesDao.findById(adminRoles.getId());
        if (null == roles) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminRolesDao.update(adminRoles);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult listSysRolesBySearch(HttpServletRequest request, SystemSearch systemSearch) {
        systemSearch.builData();
        List<AdminRoles> list = adminRolesDao.listBySearch(systemSearch);
        if (null != list && !list.isEmpty()) {
            int cnt = adminRolesDao.cntBySearch(systemSearch);
            systemSearch.setResult(list);
            systemSearch.setTotalCnt(cnt);
        } else {
            systemSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(systemSearch));
    }

    @Override
    public ApiResult bachUpdateSysRolesEnable(HttpServletRequest request, SystemSearch systemSearch) {
        int[] ids = systemSearch.getIds();
        if (null == ids || ids.length == 0) {
            return ApiResult.error(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminRolesDao.bachUpdateEnable(systemSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult bachUpdateSysRolesDeleted(HttpServletRequest request, SystemSearch systemSearch) {
        // 校验参数
        int[] ids = systemSearch.getIds();
        if (null == ids || ids.length == 0) {
            return ApiResult.error(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminRolesDao.bachUpdateDeleted(systemSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult findAllPermission(HttpServletRequest request, SystemSearch systemSearch) {
        int rolesId = systemSearch.getRolesId();
        List<AdminMenu> mList = adminMenuDao.list();
        List<AdminPkRolesMenu> pmList = adminPkRolesMenuDao.listByRolesId(rolesId);
        List<AdminOperations> opList = adminOperationsDao.list();
        List<AdminPkRolesOperations> popList = adminPkRolesOperationsDao.listByRolesId(rolesId);
        PermissionDTO permission = new PermissionDTO(mList, pmList, opList, popList);
        return ApiResult.ok(permission);
    }

    @Override
    public ApiResult bachUpdateRolesMenu(HttpServletRequest request, SystemSearch systemSearch) {
        int rolesId = systemSearch.getRolesId();
        int[] menuIds = systemSearch.getIds();
        boolean enable = systemSearch.isEnable();
        // 待补充校验方法
        AdminPkRolesMenu sysPkRolesMenu = null;
        List<Integer> ids = new ArrayList();
        for (int i = 0; i < menuIds.length; i++) {
            ids.add(menuIds[i]);
        }
        AdminRoles sysRoles = adminRolesDao.findById(rolesId);
        if (null == sysRoles) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        // 检查已经用户的角色
        List<AdminPkRolesMenu> pmList = adminPkRolesMenuDao.listByRolesIdAndIds(sysRoles.getId(), menuIds);
        if (null == pmList || pmList.isEmpty()) {
            pmList = new ArrayList<>();
            for (int i = 0; i < menuIds.length; i++) {
                sysPkRolesMenu = new AdminPkRolesMenu();
                sysPkRolesMenu.setRolesId(rolesId);
                sysPkRolesMenu.setMenuId(menuIds[i]);
                sysPkRolesMenu.setEnable(enable);
                pmList.add(sysPkRolesMenu);
            }
        } else {
            // 更新已经用户的角色
            if (!pmList.isEmpty()) {
                for (int i = 0; i < pmList.size(); i++) {
                    pmList.get(i).setEnable(enable);
                    Iterator<Integer> idsIterator = ids.iterator();
                    while (idsIterator.hasNext()) {
                        Integer e = idsIterator.next();
                        if (e.equals(pmList.get(i).getMenuId())) {
                            idsIterator.remove();
                        }
                    }
                }
            }
            // 没有的角色创建
            if (!ids.isEmpty()) {
                for (int i = 0; i < ids.size(); i++) {
                    sysPkRolesMenu = new AdminPkRolesMenu();
                    sysPkRolesMenu.setRolesId(rolesId);
                    sysPkRolesMenu.setMenuId(ids.get(i));
                    sysPkRolesMenu.setEnable(enable);
                    pmList.add(sysPkRolesMenu);
                }
            }
        }
        transactionalService.bachInsertAndUpdateSysPkRolesMenu(pmList);
        return ApiResult.ok();
    }

    @Override
    public ApiResult bachUpdateRolesOperations(HttpServletRequest request, SystemSearch systemSearch) {
        int rolesId = systemSearch.getRolesId();
        int[] operationsIds = systemSearch.getIds();
        boolean enable = systemSearch.isEnable();
        AdminRoles sysRoles = adminRolesDao.findById(rolesId);
        if (null == sysRoles) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminPkRolesOperations sysPkRolesOperations = null;
        List<Integer> ids = new ArrayList();
        for (int i = 0; i < operationsIds.length; i++) {
            ids.add(operationsIds[i]);
        }
        // 待补充校验方法
        List<AdminPkRolesOperations> pmList = adminPkRolesOperationsDao.listByRolesIdAndIds(sysRoles.getId(), ids);
        if (null == pmList || pmList.isEmpty()) {
            pmList = new ArrayList<>();
            for (int i = 0; i < operationsIds.length; i++) {
                sysPkRolesOperations = new AdminPkRolesOperations();
                sysPkRolesOperations.setRolesId(rolesId);
                sysPkRolesOperations.setOperationsId(operationsIds[i]);
                sysPkRolesOperations.setEnable(enable);
                pmList.add(sysPkRolesOperations);
            }
        } else {
            for (int i = 0; i < pmList.size(); i++) {
                pmList.get(i).setEnable(enable);
                Iterator<Integer> idsIterator = ids.iterator();
                while (idsIterator.hasNext()) {
                    Integer e = idsIterator.next();
                    if (e.equals(pmList.get(i).getOperationsId())) {
                        idsIterator.remove();
                    }
                }
            }
            if (!ids.isEmpty()) {
                for (int i = 0; i < ids.size(); i++) {
                    sysPkRolesOperations = new AdminPkRolesOperations();
                    sysPkRolesOperations.setRolesId(rolesId);
                    sysPkRolesOperations.setOperationsId(ids.get(i));
                    sysPkRolesOperations.setEnable(enable);
                    pmList.add(sysPkRolesOperations);
                }
            }
        }
        transactionalService.bachInsertAndUpdateSysPkRolesOperations(pmList);
        return ApiResult.ok();
    }

    @Override
    public ApiResult adminAllPermission(HttpServletRequest request) {
        // 返回菜单权限和页面操作权限

        AdminAccount adminAccount = redisUtil.findRedisAdminAccount(request);
        //AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(httpServletRequest);
        Set<AutoResource> autoResources = findAdminAutoResource(adminAccount);
        UserAutoResourceDTO dto = new UserAutoResourceDTO();
        if (null != autoResources && !autoResources.isEmpty()) {
            AutoResourceDTO autoResourceDTO = null;
            for (AutoResource auth : autoResources) {
                if (auth.getType() == AuthResourceTypeEnum.MENU.getValue()) {
                    autoResourceDTO = new AutoResourceDTO();
                    autoResourceDTO.setName(auth.getName());
                    autoResourceDTO.setUrl(auth.getUrl());
                    autoResourceDTO.setOrder(auth.getOrder());
                    dto.getMenu().add(autoResourceDTO);
                } else if (auth.getType() == AuthResourceTypeEnum.OP.getValue()) {
                    autoResourceDTO = new AutoResourceDTO();
                    autoResourceDTO.setName(auth.getName());
                    autoResourceDTO.setUrl(auth.getUrl());
                    dto.getOp().add(autoResourceDTO);
                }
            }
        }
        return ApiResult.ok(dto);
    }

    @Override
    public Set<AutoResource> findAdminAutoResource(AdminAccount adminAccount) {
        Set<AutoResource> set = new HashSet<>();
        AutoResource resource = null;
        // 超级管理员 所有权限
        if (adminAccount.equals(Constant.SUPER_ADMIN)) {
            List<AdminMenu> menu = adminMenuDao.list();
            if (null != menu && !menu.isEmpty()) {
                for (AdminMenu m : menu) {
                    resource = new AutoResource();
                    resource.setName(m.getMenuName());
                    resource.setUrl(m.getResource());
                    resource.setOrder(m.getOrderBy());
                    resource.setType(AuthResourceTypeEnum.MENU.getValue());
                    set.add(resource);
                }
            }
            List<AdminOperations> op = adminOperationsDao.list();
            if (null != op && !op.isEmpty()) {
                for (AdminOperations o : op) {
                    resource = new AutoResource();
                    resource.setName(o.getOperationsName());
                    resource.setUrl(o.getResource());
                    resource.setType(AuthResourceTypeEnum.OP.getValue());
                    set.add(resource);
                }
            }
        }  else {
            // 获取角色
            List<AdminPkAccountRoles> roles = adminPkAccountRolesDao.listByAdminId(adminAccount.getId());
            List<Integer> ids = new ArrayList<>();
            if (null != roles && !roles.isEmpty()) {
                for (AdminPkAccountRoles p : roles) {
                    ids.add(p.getRolesId());
                }
            }
            if (null != ids && !ids.isEmpty()) {
                // 根据角色查询菜单
                List<AdminMenu> menu = adminMenuDao.listByRolesIds(ids);
                if (null != menu && !menu.isEmpty()) {
                    for (AdminMenu m : menu) {
                        resource = new AutoResource();
                        resource.setName(m.getMenuName());
                        resource.setUrl(m.getResource());
                        resource.setOrder(m.getOrderBy());
                        resource.setType(AuthResourceTypeEnum.MENU.getValue());
                        set.add(resource);
                    }
                }
                // 根据角色查询操作资源
                List<AdminOperations> op = adminOperationsDao.listByRolesIds(ids);
                if (null != op && !op.isEmpty()) {
                    for (AdminOperations o : op) {
                        resource = new AutoResource();
                        resource.setName(o.getOperationsName());
                        resource.setUrl(o.getResource());
                        resource.setType(AuthResourceTypeEnum.OP.getValue());
                        set.add(resource);
                    }
                }
            }
        }
        return set;
    }
}
