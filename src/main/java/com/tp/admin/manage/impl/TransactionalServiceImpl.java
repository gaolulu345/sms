package com.tp.admin.manage.impl;

import com.tp.admin.dao.AdminPkRolesMenuDao;
import com.tp.admin.dao.AdminPkRolesOperationsDao;
import com.tp.admin.data.entity.AdminPkRolesMenu;
import com.tp.admin.data.entity.AdminPkRolesOperations;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.TransactionalServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class TransactionalServiceImpl implements TransactionalServiceI {

    @Autowired
    AdminPkRolesOperationsDao adminPkRolesOperationsDao;

    @Autowired
    AdminPkRolesMenuDao adminPkRolesMenuDao;

    @Override
    @Transactional
    public void bachInsertAndUpdateSysPkRolesOperations(List<AdminPkRolesOperations> sysPkRolesOperations) {
        if (null == sysPkRolesOperations || sysPkRolesOperations.isEmpty()) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        try {
            int res;
            for (int i = 0; i < sysPkRolesOperations.size(); i++) {
                if (0 == sysPkRolesOperations.get(i).getId()) {
                    res = adminPkRolesOperationsDao.insert(sysPkRolesOperations.get(i));
                    if (res == 0) {
                        throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
                    }
                } else {
                    res = adminPkRolesOperationsDao.update(sysPkRolesOperations.get(i));
                    if (res == 0) {
                        throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
                    }
                }
            }
        } catch (Exception e) {
            // 如果terinfo表插入失败就回滚。
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }

    @Override
    @Transactional
    public void bachInsertAndUpdateSysPkRolesMenu(List<AdminPkRolesMenu> sysPkRolesMenus) {
        if (null == sysPkRolesMenus || sysPkRolesMenus.isEmpty()) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        try {
            int res;
            for (int i = 0; i < sysPkRolesMenus.size(); i++) {
                if (0 == sysPkRolesMenus.get(i).getId()) {
                    res = adminPkRolesMenuDao.insert(sysPkRolesMenus.get(i));
                    if (res == 0) {
                        throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
                    }
                } else {
                    res = adminPkRolesMenuDao.update(sysPkRolesMenus.get(i));
                    if (res == 0) {
                        throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
                    }
                }
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }
}
