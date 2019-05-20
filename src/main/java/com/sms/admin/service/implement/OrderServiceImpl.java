package com.sms.admin.service.implement;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.sms.admin.ajax.ApiResult;
import com.sms.admin.common.Constant;
import com.sms.admin.dao.OrderDao;
import com.sms.admin.dao.SupplyDao;
import com.sms.admin.data.dto.OrderDTO;
import com.sms.admin.data.dto.SupplyDTO;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.data.search.RangeSearch;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.manage.TransactionalServiceI;
import com.sms.admin.service.OrderServiceI;
import com.sms.admin.utils.ExcelUtil;
import com.sms.admin.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    TransactionalServiceI transactionalService;

    @Override
    public ApiResult addOrder(HttpServletRequest request, OrderSearch orderSearch) {
        if (null == orderSearch ||
                null == orderSearch.getOrderCode() ||
                null == orderSearch.getGoodName() ||
                null == orderSearch.getGoodCompany() ||
                null == orderSearch.getGoodNumber() ||
                null == orderSearch.getAmount() ||
                null == orderSearch.getSupplyId() ||
                null == orderSearch.getStatus()) {
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
            orderTatal = orderDao.orderTatal(startTime, endTime);
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
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "获取到的供应商为空");
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
}
