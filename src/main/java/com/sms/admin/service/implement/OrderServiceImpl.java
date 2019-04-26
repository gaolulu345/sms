package com.sms.admin.service.implement;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.sms.admin.ajax.ApiResult;
import com.sms.admin.common.Constant;
import com.sms.admin.dao.OrderDao;
import com.sms.admin.data.dto.OrderDTO;
import com.sms.admin.data.dto.SupplyDTO;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.service.OrderServiceI;
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
public class OrderServiceImpl implements OrderServiceI {

    @Autowired
    OrderDao orderDao;

    @Override
    public ApiResult orderList(HttpServletRequest request, OrderSearch orderSearch) {
        orderSearch.builData();
        List<OrderDTO> list = orderDao.listBySearch(orderSearch);
        if (null != list && !list.isEmpty()) {
            for (OrderDTO temp:list) {
                temp.build();
            }
            int cnt = orderDao.cntBySearch(orderSearch);
            orderSearch.setResult(list);
            orderSearch.setTotalCnt(cnt);
        } else {
            orderSearch.setTotalCnt(0);
        }
        return ApiResult.ok(orderSearch);
    }

    @Override
    public ResponseEntity<FileSystemResource> orderExport(HttpServletRequest request, HttpServletResponse response, OrderSearch orderSearch) {
        List<OrderDTO> list = orderDao.listBySearch(orderSearch);
        if (null != list && !list.isEmpty()) {
            for (OrderDTO orderDTO:list) {
                orderDTO.build();
            }
        }
        String fileName = ExcelUtil.createXlxs(Constant.ORDER_LIST, Math.random() * 10 + "",Math.random() * 10 + "");
        String path = System.getProperty(Constant.TMP_DIR) + Constant._XLSX_DIR;
        File pathFile = new File(path);
        if (!pathFile.exists()){
            pathFile.mkdirs();
        }
        try {
            ExcelUtils.getInstance().exportObjects2Excel(list,OrderDTO.class,true,"sheet0", true,path + fileName);
        } catch (Excel4JException | IOException e) {
            e.printStackTrace();
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        File file = new File(path + fileName);
        return ExcelUtil.fileExcel(request,fileName,file);
    }
}
