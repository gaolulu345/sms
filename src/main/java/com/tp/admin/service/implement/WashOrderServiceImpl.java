package com.tp.admin.service.implement;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.OrderDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.OrderDTO;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.WashOrderServiceI;
import com.tp.admin.utils.ExcelUtil;
import com.tp.admin.utils.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import java.util.List;

@Service
public class WashOrderServiceImpl implements WashOrderServiceI {

    @Autowired
    OrderDao orderDao;

    @Autowired
    TerDao terDao;

    @Override
    public ApiResult list(HttpServletRequest request, OrderSearch orderSearch) {
        orderSearch.build();
        List<OrderDTO> list = orderDao.listBySearch(orderSearch);
        if (null != list && !list.isEmpty()) {
            for (OrderDTO o : list) {
                o.build();
            }
        }
        int cnt = orderDao.cntBySearch(orderSearch);
        orderSearch.setResult(list);
        orderSearch.setTotalCnt(cnt);
        return ApiResult.ok(orderSearch);
    }

    @Override
    public ResponseEntity<FileSystemResource> listExport(HttpServletRequest request , HttpServletResponse response ,OrderSearch orderSearch) {
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
        HttpHeaders headers = new HttpHeaders();
        try {
        String agent = request.getHeader("User-Agent").toUpperCase(); //获得浏览器信息并转换为大写
        if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO")>0 && agent.indexOf("RV:11")>0)) {  //IE浏览器和Edge浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {  //其他浏览器
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment;filename=" + fileName);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new FileSystemResource(file));
    }


    @Override
    public ApiResult orderTerSelection(HttpServletRequest request) {
        return ApiResult.ok(terDao.findAllTerIdAndTitle());
    }
}
