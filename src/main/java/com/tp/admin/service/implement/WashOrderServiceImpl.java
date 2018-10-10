package com.tp.admin.service.implement;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.OrderDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.OrderDTO;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.WashOrderServiceI;
import com.tp.admin.utils.ExcelUtil;
import com.tp.admin.utils.StringUtil;
import com.tp.admin.utils.TimeUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Service
public class WashOrderServiceImpl implements WashOrderServiceI {

    private static final Logger logger = LoggerFactory.getLogger(WashOrderServiceImpl.class);

    @Autowired
    OrderDao orderDao;

    @Autowired
    TerDao terDao;

    @Override
    public ApiResult list(HttpServletRequest request, OrderSearch orderSearch) {
        orderSearch.builData();
        List<OrderDTO> list = orderDao.listBySearch(orderSearch);
        if (null != list && !list.isEmpty()) {
            for (OrderDTO o : list) {
                o.build();
            }
            int cnt = orderDao.cntBySearch(orderSearch);
            orderSearch.setResult(list);
            orderSearch.setTotalCnt(cnt);
        }else {
            orderSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(orderSearch));
    }

    @Override
    public ApiResult info(HttpServletRequest request, OrderSearch orderSearch) {
        Integer orderId = orderSearch.getOrderId();
        if (null == orderId || orderId == 0) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        OrderDTO orderDTO = orderDao.findOrderDTOById(orderId);
        if (null == orderDTO) {
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        orderDTO.build();
        return ApiResult.ok(orderDTO);
    }

    @Override
    public ResponseEntity<FileSystemResource> listExport(HttpServletRequest request , HttpServletResponse response ,OrderSearch orderSearch) {
        int days = 0;
        try {
            String st = orderSearch.getStartTime();
            String et = orderSearch.getEndTime();
            Date startTime = StringUtil.toSearchDate(st);
            Date endTime = StringUtil.toSearchDate(et);
            days = TimeUtil.getDiffDays(startTime,endTime);
            logger.info("导出数据天数间隔 {}",days);
        }catch (Exception e){
            logger.error("筛选时间请求参数异常{}" , e.getMessage());
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        if (days > 90) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"导出时间间隔天数不能大于9天");
        }
        List<OrderDTO> list = orderDao.listBySearch(orderSearch);
        if (null != list && !list.isEmpty()) {
            for (OrderDTO o : list) {
                o.build();
            }
        }
        String fileName = ExcelUtil.createXlxs(Constant.WASH_ORDER, orderSearch.getStartTime(), orderSearch.getEndTime());
        String path = System.getProperty(Constant.TMP_DIR) + Constant._XLSX_DIR;
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        try {
            ExcelUtils.getInstance().exportObjects2Excel(list, OrderDTO.class, true, "sheet0", true, path + fileName);
        } catch (Excel4JException | IOException e) {
            e.printStackTrace();
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        File file = new File(path + fileName);
        return ExcelUtil.fileExcel(request,fileName,file);
    }


    @Override
    public ApiResult orderTerSelection(HttpServletRequest request) {
        return ApiResult.ok(terDao.findAllTerIdAndTitle());
    }
}
