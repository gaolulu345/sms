package com.sms.admin.dao;

import com.sms.admin.data.dto.PurchaseOrderDTO;
import com.sms.admin.data.search.PurchaseOrderSearch;

import java.util.List;

public interface PurchaseOrderDao {

    int cntBySearch(PurchaseOrderSearch purchaseOrderSearch);

    List<PurchaseOrderDTO> listBySearch(PurchaseOrderSearch purchaseOrderSearch);

    int addPurchaseOrder(PurchaseOrderSearch purchaseOrderSearch);
    /*

    ProductDTO findProductById(ProductSearch productSearch);

    int updateProductById(Product product);

    int updateProduct(ProductSearch productSearch);

    int addProduct(Product product);*/

}
