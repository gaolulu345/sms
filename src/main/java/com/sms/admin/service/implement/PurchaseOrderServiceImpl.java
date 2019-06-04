package com.sms.admin.service.implement;


import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.sms.admin.ajax.ApiResult;
import com.sms.admin.common.Constant;
import com.sms.admin.dao.ProductDao;
import com.sms.admin.dao.PurchaseOrderDao;
import com.sms.admin.data.dto.OrderDTO;
import com.sms.admin.data.dto.ProductDTO;
import com.sms.admin.data.dto.PurchaseOrderDTO;
import com.sms.admin.data.search.ProductSearch;
import com.sms.admin.data.search.PurchaseOrderSearch;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.service.PurchaseOrderServiceI;
import com.sms.admin.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderServiceI {

    @Autowired
    PurchaseOrderDao purchaseOrderDao;

    @Autowired
    ProductDao productDao;

    @Override
    public ApiResult listPurchaseOrder(HttpServletRequest request, PurchaseOrderSearch purchaseOrderSearch) {
        purchaseOrderSearch.build();
        List<PurchaseOrderDTO> list = purchaseOrderDao.listBySearch(purchaseOrderSearch);
        if (null != list && !list.isEmpty()) {
            for (PurchaseOrderDTO purchaseOrderDTO:list) {
                purchaseOrderDTO.build();
            }
            int cnt = purchaseOrderDao.cntBySearch(purchaseOrderSearch);
            purchaseOrderSearch.setResult(list);
            purchaseOrderSearch.setTotalCnt(cnt);
        } else {
            purchaseOrderSearch.setTotalCnt(0);
        }
        return ApiResult.ok(purchaseOrderSearch);
    }

    @Override
    public ApiResult addPurchaseOrder(HttpServletRequest request, PurchaseOrderSearch purchaseOrderSearch) {
        if (null == purchaseOrderSearch.getProType() || null == purchaseOrderSearch.getProId() || null == purchaseOrderSearch.getSaleNum()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        ProductSearch productSearch = new ProductSearch();
        productSearch.setProType(purchaseOrderSearch.getProType());
        productSearch.setProductId(purchaseOrderSearch.getProId());
        ProductDTO productDTO = productDao.findProductById(productSearch);
        if (null == productDTO) {
            throw new BaseException(ExceptionCode.NOT_THIS_PRODUCT);
        }
        if (!productDTO.getOnline() || null == productDTO.getNewPrice()) {
            throw new BaseException(ExceptionCode.NOT_ONLINE);
        }
        purchaseOrderSearch.setSaleValue(purchaseOrderSearch.getSaleNum() * productDTO.getNewPrice());
        purchaseOrderSearch.setNetProfits(purchaseOrderSearch.getSaleNum() * productDTO.getOldPrice() - purchaseOrderSearch.getSaleNum() * productDTO.getNewPrice());
        int res = purchaseOrderDao.addPurchaseOrder(purchaseOrderSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ResponseEntity<FileSystemResource> purchaseOrderExport(HttpServletRequest request, HttpServletResponse response, PurchaseOrderSearch purchaseOrderSearch) {
        List<PurchaseOrderDTO> list = purchaseOrderDao.listBySearch(purchaseOrderSearch);
        if (null != list && !list.isEmpty()) {
            for (PurchaseOrderDTO temp:list) {
                temp.build();
            }
        }
        String fileName = ExcelUtil.createXlxs(Constant.PURCHASE_ORDER_LIST, Math.random() * 10 + "",Math.random() * 10 + "");
        String path = System.getProperty(Constant.TMP_DIR) + Constant._XLSX_DIR;
        File pathFile = new File(path);
        if (!pathFile.exists()){
            pathFile.mkdirs();
        }
        try {
            ExcelUtils.getInstance().exportObjects2Excel(list,PurchaseOrderDTO.class,true,"sheet0", true,path + fileName);
        } catch (Excel4JException | IOException e) {
            e.printStackTrace();
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        File file = new File(path + fileName);
        return ExcelUtil.fileExcel(request,fileName,file);
    }
}
