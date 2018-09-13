package com.tp.admin.utils;

import com.google.gson.JsonObject;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.data.search.Search;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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


//        json.put("orderID", "订单编号");
//        json.put("terminalID", "洗车机编号");
//        json.put("userID", "用户");
//        json.put("status", "订单状态");
//        json.put("sabisStr", "服务编号");
//        json.put("payType", "支付方式");
//        json.put("createTime", "创建时间");
//        json.put("payTime", "支付时间");
//        json.put("finishTime", "完成时间");
//        json.put("amount", "总价");
//        json.put("ticketID", "优惠券");
//        json.put("cardID", "洗车卡");
//        json.put("refundID", "退款编号");


//        tmap.put(OrderState.OrderStateCreate.ordinal() + "", "创建");
//        tmap.put(OrderState.OrderStateCancel.ordinal() + "", "撤销");
//        tmap.put(OrderState.OrderStatePaid.ordinal() + "", "支付完成");
//        tmap.put(OrderState.OrderStateWashing.ordinal() + "", "正在洗车");
//        tmap.put(OrderState.OrderStateDone.ordinal() + "", "订单完成，洗车完毕");
//        tmap.put(OrderState.OrderStateUnfinished.ordinal() + "", "洗车中断");
//        tmap.put(OrderState.OrderStateDelayed.ordinal() + "", "延期洗车");
    ////
//        tmap.put(OrderType.AliPayOrder.ordinal() + "", "支付宝");
//        tmap.put(OrderType.WxPayOrder.ordinal() + "", "微信");
//        tmap.put(OrderType.TestOr0PayOrder.ordinal() + "", "测试或0元券");

//        json.put("userID", "用户ID");
//        json.put("type", "注册来源");
//        json.put("phone", "手机号码");
//        json.put("createTime", "注册时间");
//        json.put("lastLoginTime", "上次登录时间");

//        tmap.put(UserLoginType.LoginByPhoneMsg.ordinal() + "", "短信验证");
//        tmap.put(UserLoginType.LoginByPhonePw.ordinal() + "", "手机号+密码");
//        tmap.put(UserLoginType.LoginByWx.ordinal() + "", "APP内微信授权");
//        tmap.put(UserLoginType.LoginByMiniWx.ordinal() + "", "微信小程序");
//        tmap.put(UserLoginType.LoginByMiniAli.ordinal() + "", "支付宝小程序");
//        json.put("refundID", "退款编号");
//        json.put("reason", "退款原因");
//        json.put("amount", "退款数额");
//        json.put("orderID", "订单编号");
//        json.put("createTime", "创建时间");
//        json.put("modifyTime", "修改时间");
//        json.put("checkTime", "审核时间");
//        json.put("refundTime", "退款时间");
//        json.put("sysAdminNameCheck", "审核人");
//        json.put("sysAdminNamePay", "退款人");
//        json.put("msg", "退款说明");
//        json.put("status", "退款状态");
//        tmap.put(RefundReasonType.MisPurchase.ordinal() + "", "误购，请求退款");
//        tmap.put(RefundReasonType.TerDonnotStart.ordinal() + "", "设备无法启动");
//        tmap.put(RefundReasonType.TerBreakOff.ordinal() + "", "洗车中通故障");
//        tmap.put(RefundReasonType.UnSatified.ordinal() + "", "洗车服务不满意");
//        tmap.put(RefundReasonType.OtherReason.ordinal() + "", "其它原因");
//        tmap.put(RefundState.Default.ordinal() + "", "默认状态");
//        tmap.put(RefundState.Asked.ordinal() + "", "请求退款");
//        tmap.put(RefundState.Checked.ordinal() + "", "请求通过审核");
//        tmap.put(RefundState.Paying.ordinal() + "", "已退款");
//        tmap.put(RefundState.Denid.ordinal() + "", "请求被拒绝");
//        tmpRow.createCell(0).setCellValue("序号");
//        tmpRow.createCell(1).setCellValue("订单编号");
//        tmpRow.createCell(2).setCellValue("网点名称");
//        tmpRow.createCell(3).setCellValue("用户编号");
//        tmpRow.createCell(4).setCellValue("支付时间");
//        tmpRow.createCell(5).setCellValue("订单来源");
//        tmpRow.createCell(6).setCellValue("支付方式");
//        tmpRow.createCell(7).setCellValue("订单金额(分)");
//        tmpRow.createCell(8).setCellValue("优惠券编号");
//        tmpRow.createCell(9).setCellValue("洗车卡编号");
//        tmpRow.createCell(10).setCellValue("外观清洗服务编号");
//        tmpRow.createCell(11).setCellValue("内饰清洗服务编号");

}