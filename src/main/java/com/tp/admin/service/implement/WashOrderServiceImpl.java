package com.tp.admin.service.implement;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.OrderDao;
import com.tp.admin.dao.SabisDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.OrderDTO;
import com.tp.admin.data.entity.Order;
import com.tp.admin.data.entity.Sabis;
import com.tp.admin.data.entity.TerInfo;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.enums.OrderChannelEnum;
import com.tp.admin.enums.OrderStatusEnum;
import com.tp.admin.enums.OrderTypeEnum;
import com.tp.admin.enums.WashTerStateEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.WashOrderServiceI;
import com.tp.admin.utils.ExcelUtil;
import com.tp.admin.utils.StringUtil;
import com.tp.admin.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class WashOrderServiceImpl implements WashOrderServiceI {

    private static final Logger logger = LoggerFactory.getLogger(WashOrderServiceImpl.class);

    @Autowired
    OrderDao orderDao;

    @Autowired
    TerDao terDao;

    @Autowired
    SabisDao sabisDao;

    @Override
    public ApiResult list(HttpServletRequest request, OrderSearch orderSearch) {
        orderSearch.builData();
        List<OrderDTO> list = orderDao.listBySearch(orderSearch);
        if (null != list && !list.isEmpty()) {
            for (OrderDTO o : list) {
                o.build();
            }
            int cnt = orderDao.cntBySearch(orderSearch);
            orderSearch.setTotalCnt(cnt);
            orderSearch.setResult(list);
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
            endTime = StringUtil.addOneDay(endTime);
            days = TimeUtil.getDiffDays(startTime,endTime);
            orderSearch.setEndTime(StringUtil.getDateTime(endTime));
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
        String fileName = ExcelUtil.createXlxs(Constant.WASH_ORDER, orderSearch.getStartTime(), StringUtil.downOneDay(orderSearch.getEndTime()));
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

    @Override
    public Order buildOrder(Integer terId, OrderChannelEnum orderChannelEnum, OrderTypeEnum orderTypeEnum) {
        Order order = new Order();
        TerInfo terInfo = terDao.findTerInfoById(terId);
        if (!terInfo.isOnline()){
            throw new BaseException(ExceptionCode.TER_OFFLINE);
        }
        if (terInfo.getStatus() == WashTerStateEnum.ERROR.getValue() || terInfo.getStatus() == WashTerStateEnum.PAUSED.getValue()) {
            throw new BaseException(ExceptionCode.INVALID_TER_STATE);
        }
        Sabis sabis = sabisDao.findByTerId(terId);
        if (null == sabis) {
            throw new BaseException(ExceptionCode.NO_THIS_SABIS_TER);
        }

        order.setTerId(terInfo.getTerId());
        order.setSabisId(sabis.getId());
        //生成的虚拟订单的用户id为太仆白手机的用户Id
        order.setUserId(127);
        order.setStatus(OrderStatusEnum.ASK_CHECK.getValue());
        order.setPayTime(new Timestamp(System.currentTimeMillis()));
        order.setChannel(orderChannelEnum.getValue());
        order.setType(orderTypeEnum.getValue());
        order.setNoticed(true);
        int res = orderDao.create(order);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return order;
    }
}
