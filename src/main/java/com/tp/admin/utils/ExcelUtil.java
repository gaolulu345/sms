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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class ExcelUtil {

    public static String createXlxs(String name , String startTime , String endTime ){
        if (
                StringUtil.isEmpty(startTime) ||
                StringUtil.isEmpty(endTime)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "请选择导出数据时间段");
        }
        return name + startTime +"_"+ endTime + Constant._XLSX;
    }

    /**
     *
     * @param response
     * @param search
     * @param service
     * @param exportFileName
     *            excel文件名
     * @param sheetTitle
     *            表格名称
     * @param headJSON
     * @param checkStTime
     *            是否限制起始时间
     * @return
     */
//    public static ApiResult export(HttpServletResponse response, Search search, ExportService service,
//                                   String exportFileName, String sheetTitle, JsonObject headJSON, boolean checkStTime) {
//        String errMsg;
//        // 时间判断结果
//        boolean checkres = false;
//        if (checkStTime) {
//            String ftime = StringUtils.friendlyTime(search.getStTime());
//            checkres = search.getStTime() != null && !("3个月前".equals(ftime) || "Unknown".equals(ftime))
//                    && StringUtils.toDate(ftime) == null;
//        }
//        if (!checkStTime || (checkStTime && checkres)) {
//            search.setPageSize(0);
//            search.setPageIndex(0);
//
//            @SuppressWarnings("rawtypes")
//            List results = service.search(search);
//            if (results != null && results.size() > 0) {
//                boolean success = ExcelUtil.createSimpleExcel(response, exportFileName, sheetTitle, headJSON,
//                        JSONArray.parseArray(JSONObject.toJSONString(results)));
//                if (success)
//                    return AjaxResult.getOK();
//                else {
//                    errMsg = "fail to export!";
//                    throw new BaseException(baseErrCode.SystemException, errMsg);
//                }
//            }
//            errMsg = "non-existent matching for search --> " + search;
//            throw new BaseException(baseErrCode.NoDBResultException, errMsg);
//        } else {
//            errMsg = "illegal startTime to export --> " + search.getStTime();
//            throw new BaseException(baseErrCode.ParamException, errMsg);
//        }
//    }

    /**
     * 创建excel下载
     *
     * @param exportFileName
     *            excel文件名
     * @param sheetTitle
     *            表格名称
     * @param headJSON
     *            对应表头和转换json
     * @param dataJSON
     *            数据jsonArray，每个对应一行
     * @return 是否成功
     */
    public static boolean createSimpleExcel(HttpServletResponse response, String exportFileName, String sheetTitle,
                                            JsonObject headJSON, JsonObject dataJSON) {
        boolean result = false;
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + exportFileName + ".xls");
        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        try {
            wb.write(stream);
            stream.flush();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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