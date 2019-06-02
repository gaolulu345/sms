package com.sms.admin.manage.impl;

import com.sms.admin.dao.*;
import com.sms.admin.data.dto.ProductDTO;
import com.sms.admin.data.entity.*;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.data.search.ProductSearch;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.manage.TransactionalServiceI;
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
    AdminAccountInfoDao adminAccountInfoDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    ProductDao productDao;



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
            } else {
                adminAccount.setAdminId(adminAccount.getId());
            }
            res = adminAccountInfoDao.insert(adminAccount);
            if (0 == res) {
                throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
            }
            AdminPkAccountRoles adminPkAccountRoles = new AdminPkAccountRoles();
            adminPkAccountRoles.setAdminId(adminAccount.getAdminId());
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
    public void updateOrder(OrderSearch orderSearch) {
        if (null == orderSearch ||
                null == orderSearch.getGoodName() ||
                null == orderSearch.getGoodCompany() ||
                null == orderSearch.getGoodNumber() ||
                null == orderSearch.getAmount() ||
                null == orderSearch.getSupplyId() ||
                null == orderSearch.getStatus()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        try {
            int res = orderDao.updateOrder(orderSearch);
            if (0 == res) {
                throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
            }
            res = orderDao.updateOrderDetail(orderSearch);
            if (0 == res) {
                throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }

    @Override
    public void insertOrder(OrderSearch orderSearch) {
        if (null == orderSearch ||
                null == orderSearch.getOrderCode() ||
                null == orderSearch.getProType() ||
                null == orderSearch.getGoodCompany() ||
                null == orderSearch.getGoodNumber() ||
                null == orderSearch.getAmount() ||
                null == orderSearch.getSupplyId() ||
                null == orderSearch.getStatus()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }

        try {
            int res = 0;
            //下面是更改商品
            if (null != orderSearch.getProductId()) {
                ProductSearch productSearch = new ProductSearch(orderSearch);
                ProductDTO productDTO = productDao.findProductById(productSearch);
                if (null != productDTO) {
                    if (null == orderSearch.getGoodName()) {
                        orderSearch.setGoodName(productDTO.getProName());
                    }
                    //更改相关
                    Product product = new Product(orderSearch, productDTO);
                    res = productDao.updateProductById(product);
                    if (res == 0) {
                        throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
                    }
                }
            }
            //下面是新增商品
            if (null == orderSearch.getProductId() && null != orderSearch.getGoodName()) {
                Product product = new Product(orderSearch);
                res = productDao.addProduct(product);
                if (res == 0) {
                    throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
                }
            }
            res = orderDao.addOrder(orderSearch);
            if (0 == res){
                throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
            }
            //orderSearch.setId(res);
            res = orderDao.addOrderDetail(orderSearch);
            if (0 == res) {
                throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }

}
