package com.sms.admin.utils;

import com.sms.admin.common.Constant;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ExcelUtil {

    public static String createXlxs(String name, String startTime, String endTime) {
        if (
                StringUtil.isEmpty(startTime) ||
                        StringUtil.isEmpty(endTime)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "请选择导出数据时间段");
        }
        return name + startTime + "_" + endTime + Constant._XLSX;
    }


    public static ResponseEntity<FileSystemResource> fileExcel(HttpServletRequest request, String exportFileName,
                                                               File file
    ) {
        HttpHeaders headers = new HttpHeaders();
        try {
            String agent = request.getHeader("User-Agent").toUpperCase(); //获得浏览器信息并转换为大写
            if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {  //IE浏览器和Edge浏览器
                exportFileName = URLEncoder.encode(exportFileName, "UTF-8");
            } else {  //其他浏览器
                exportFileName = new String(exportFileName.getBytes("UTF-8"), "iso-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment;filename=" + exportFileName);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new FileSystemResource(file));
    }

}