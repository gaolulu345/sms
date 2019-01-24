package com.tp.admin.manage.impl;

import com.tp.admin.dao.*;
import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.dto.PartnerUserWashCardDetailDTO;
import com.tp.admin.data.entity.*;
import com.tp.admin.enums.OrderStatusEnum;
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

    @Autowired
    AdminAccountDao adminAccountDao;

    @Autowired
    AdminPkAccountRolesDao adminPkAccountRolesDao;

    @Autowired
    RefundDao refundDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    PartnerUserWashCardDao partnerUserWashCardDao;


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

    @Override
    @Transactional
    public void register(AdminAccount adminAccount, AdminRoles adminRoles) {
        if (null == adminAccount || null == adminRoles) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        try {
            int res = adminAccountDao.insert(adminAccount);
            if (0 == res) {
                throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
            }
            AdminPkAccountRoles adminPkAccountRoles = new AdminPkAccountRoles();
            adminPkAccountRoles.setAdminId(adminAccount.getId());
            adminPkAccountRoles.setRolesId(adminRoles.getId());
            adminPkAccountRoles.setEnable(true);
            res = adminPkAccountRolesDao.insert(adminPkAccountRoles);
            if (0 == res) {
                throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }

    @Override
    @Transactional
    public void payBack(Refund refund, Order order) {
        if (null == refund || null == order) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        try {
            int res = refundDao.update(refund);
            if (0 == res) {
                throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
            }
            res = orderDao.updateOrderStatus(order.getId(),OrderStatusEnum.CANCEL.getValue());
            if (0 == res) {
                throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }


    @Override
    @Transactional
    public void recoveryWashCard(PartnerUserWashCardDetailDTO partnerUserWashCardDetailDTO) {
        if (null == partnerUserWashCardDetailDTO) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        try {
            int res = partnerUserWashCardDao.recoveryCardInvalid(partnerUserWashCardDetailDTO.getId());
            if (res == 0) {
                throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
            }
            res = partnerUserWashCardDao.addUpdateCnt(partnerUserWashCardDetailDTO.getId());
            if (res == 0) {
                throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
            }
        }catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }
}
