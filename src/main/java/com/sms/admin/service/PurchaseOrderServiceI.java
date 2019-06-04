package com.sms.admin.service;


import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.data.search.PurchaseOrderSearch;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PurchaseOrderServiceI {
    ApiResult listPurchaseOrder(HttpServletRequest request, PurchaseOrderSearch purchaseOrderSearch);

    ApiResult addPurchaseOrder(HttpServletRequest request, PurchaseOrderSearch purchaseOrderSearch);

    ResponseEntity<FileSystemResource> purchaseOrderExport(HttpServletRequest request, HttpServletResponse response, PurchaseOrderSearch purchaseOrderSearch);

}
