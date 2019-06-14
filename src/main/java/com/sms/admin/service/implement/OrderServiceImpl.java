package com.sms.admin.service.implement;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.sms.admin.ajax.ApiResult;
import com.sms.admin.common.Constant;
import com.sms.admin.config.AliyunOssProperties;
import com.sms.admin.dao.OrderDao;
import com.sms.admin.dao.PurchaseOrderDao;
import com.sms.admin.dao.SupplyDao;
import com.sms.admin.data.dto.DataTotalDTO;
import com.sms.admin.data.dto.OrderDTO;
import com.sms.admin.data.dto.SupplyDTO;
import com.sms.admin.data.dto.UploadFileDTO;
import com.sms.admin.data.entity.PurchaseOrder;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.data.search.RangeSearch;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.manage.AliyunOssManagerI;
import com.sms.admin.manage.TransactionalServiceI;
import com.sms.admin.service.OrderServiceI;
import com.sms.admin.utils.ExcelUtil;
import com.sms.admin.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderServiceI {

    @Autowired
    OrderDao orderDao;

    @Autowired
    SupplyDao supplyDao;

    @Autowired
    AliyunOssManagerI aliyunOssManager;

    @Autowired
    AliyunOssProperties aliyunOssProperties;

    @Autowired
    TransactionalServiceI transactionalService;

    @Autowired
    PurchaseOrderDao purchaseOrderDao;

    @Override
    public ApiResult addOrder(HttpServletRequest request, OrderSearch orderSearch) {
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
        if (null == orderSearch.getProductId() && null == orderSearch.getGoodName()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        transactionalService.insertOrder(orderSearch);

        return ApiResult.ok();
    }

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
    public ApiResult orderDetail(HttpServletRequest request, OrderSearch orderSearch) {
        if (null == orderSearch.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        List<OrderDTO> list = orderDao.listBySearch(orderSearch);
        OrderDTO orderDTO = null;
        if (null != list && !list.isEmpty()) {
            orderDTO = list.get(0);
            orderDTO.build();
        }
        return ApiResult.ok(orderDTO);
    }

    @Override
    public ApiResult listAllSupply(HttpServletRequest request) {
        return ApiResult.ok(supplyDao.listAllSupply());
    }

    @Override
    public ApiResult updateOrder(HttpServletRequest request, OrderSearch orderSearch) {
        if (null == orderSearch ||
                null == orderSearch.getGoodName() ||
                null == orderSearch.getGoodCompany() ||
                null == orderSearch.getGoodNumber() ||
                null == orderSearch.getAmount() ||
                null == orderSearch.getSupplyId() ||
                null == orderSearch.getStatus()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        transactionalService.updateOrder(orderSearch);
        return ApiResult.ok();
    }

    @Override
    public ApiResult updateDeleted(HttpServletRequest request, OrderSearch orderSearch) {
        if (null == orderSearch.getId() || null == orderSearch.getDeleted()) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING);
        }
        int res = orderDao.updateOrder(orderSearch);
        if (0 == res) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
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

    @Override
    public ApiResult orderRangeSumTotal(HttpServletRequest request, RangeSearch rangeSearch) {
        List<Integer> supplyIds = new ArrayList<>();
        supplyIds = supplyDao.findSupplyIds();
        if (null == supplyIds || supplyIds.isEmpty()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "未获取到供应商");
        }
        Date dayEnd = TimeUtil.getDayEnd();
        // 七天前的时间
        Date sevenDays = TimeUtil.getFrontDay(dayEnd, 6);
        Map<String, Object> data = new LinkedHashMap<>();
        Map<String, Object> ojb = new LinkedHashMap<>();
        Date tempDate = null;
        long orderTatal = 0;
        for (int i = 6; i >= 0; i--) {
            tempDate = TimeUtil.getFrontDay(dayEnd, i);
            // 某日完成订单金额总和
            String startTime = TimeUtil.getDayStartTime(tempDate).toString();
            String endTime = TimeUtil.getDayEndTime(tempDate).toString();
            orderTatal = orderDao.orderTatal(startTime, endTime, supplyIds);
            ojb.put(new SimpleDateFormat("MM-dd").format(tempDate), orderTatal);
        }
        data.put("order", ojb);
        data.put("startTime", new SimpleDateFormat("MM-dd").format(TimeUtil.getDayStartTime(sevenDays)));
        data.put("endTime", new SimpleDateFormat("MM-dd").format(dayEnd));
        return ApiResult.ok(data);
    }

    @Override
    public ApiResult orderNumTotal(HttpServletRequest request, RangeSearch rangeSearch) {
        List<Integer> supplyIds = new ArrayList<>();
        supplyIds = supplyDao.findSupplyIds();
        if (null == supplyIds || supplyIds.isEmpty()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "未获取到供应商");
        }
        // 当天结束时间
        Date endDay = TimeUtil.getDayEnd();
        // 开始时间
        Date beginDayOfMonth = TimeUtil.getFrontDay(endDay, 29);
        String startTime = TimeUtil.getDayStartTime(beginDayOfMonth).toString();
        String endTime = endDay.toString();
        List<Map<String, Long>> map = orderDao.findNumTotal(startTime, endTime, supplyIds);
        return ApiResult.ok(map);
    }

    @Override
    public ApiResult dataTotal(HttpServletRequest request, RangeSearch rangeSearch) {
        List<Integer> supplyIds = new ArrayList<>();
        supplyIds = supplyDao.findSupplyIds();
        DataTotalDTO dataTotalDTO = new DataTotalDTO();
        if (null == supplyIds || supplyIds.isEmpty()) {
            return ApiResult.ok(dataTotalDTO);
        }
        Long orderTotal = 0L;
        Long sevenDaymoneyTotal = 0L;
        Long oneDayMoneyTotal = 0L;
        Long oneDayReceiveMoneyTotal = 0L;
        Long sevenDayReceiveMoneyTotal = 0L;
        // 当天开始时间
        Date beginDay = TimeUtil.getDayBegin();
        // 当天结束时间
        Date endDay = TimeUtil.getDayEnd();
        // 30天前的时间。
        Date begin30Day = TimeUtil.getFrontDay(endDay, 29);
        // 七天前的时间
        Date sevenDays = TimeUtil.getFrontDay(endDay, 6);
        // 近30天订单数
        orderTotal = orderDao.orderTatal(TimeUtil.getDayStartTime(begin30Day).toString()
                , endDay.toString(), supplyIds);
        //七天完成的订单金额总和
        sevenDaymoneyTotal = orderDao.moneyTotal(TimeUtil.getDayStartTime(sevenDays).toString(), endDay.toString(), supplyIds);
        oneDayReceiveMoneyTotal = purchaseOrderDao.receiveMoneyTotal(beginDay.toString(), endDay.toString());
        sevenDayReceiveMoneyTotal = purchaseOrderDao.receiveMoneyTotal(TimeUtil.getDayStartTime(sevenDays).toString(), endDay.toString());
        //今天完成订单金额总和
        oneDayMoneyTotal = orderDao.moneyTotal(beginDay.toString(), endDay.toString()
                , supplyIds);

        dataTotalDTO.setOrderTotal(orderTotal);
        dataTotalDTO.setSevenDayMoneyTotal(sevenDaymoneyTotal);
        dataTotalDTO.setOneDayMoneyTotal(oneDayMoneyTotal);
        dataTotalDTO.setOneDayReceiveMoneyTotal(oneDayReceiveMoneyTotal);
        dataTotalDTO.setSevenDayReceiveMoneyTotal(sevenDayReceiveMoneyTotal);
        return ApiResult.ok(dataTotalDTO);
    }

    @Override
    public ApiResult uploadPicture(HttpServletRequest request, MultipartFile multipartFile) {
        if (null == multipartFile) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        UploadFileDTO dto = aliyunOssManager.uploadFileToAliyunOss(multipartFile , aliyunOssProperties.getPath() + "/input");
        if (null == dto) {
            return ApiResult.error(ExceptionCode.UNKNOWN_EXCEPTION, "upload file failure");
        }
        return ApiResult.ok(dto);
    }
}
