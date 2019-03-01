package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.AdminTerOperatingLogDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.dao.TerFaultOperationDao;
import com.tp.admin.data.dto.AdminTerOperatingLogDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.TerFaultInfo;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.enums.WashTerStateEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.WashSiteServiceI;
import com.tp.admin.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class WashSiteServiceImpl implements WashSiteServiceI {
    private Logger logger = LoggerFactory.getLogger(WashSiteServiceImpl.class);

    @Autowired
    TerDao terDao;

    @Autowired
    AdminTerOperatingLogDao adminTerOperatingLogDao;

    @Autowired
    TerFaultOperationDao terFaultOperationDao;

    @Override
    public ApiResult siteOperationLog(WxMiniSearch wxMiniSearch) {
        List<AdminTerOperatingLogDTO> results = adminTerOperatingLogDao.listLogBySearch(wxMiniSearch);
        if (null != results && !results.isEmpty()) {
            for (AdminTerOperatingLogDTO dto : results){
                dto.build();
            }
            int cnt = adminTerOperatingLogDao.cntBySearch(wxMiniSearch);
            wxMiniSearch.setResult(results);
            wxMiniSearch.setTotalCnt(cnt);
        }else {
            wxMiniSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(wxMiniSearch));
    }

    @Override
    public TerInfoDTO terCheck(WxMiniSearch wxMiniSearch) {
        if (null == wxMiniSearch) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        wxMiniSearch.builData();
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniSearch);
        TerInfoDTO dto = null;
        if (null != results && !results.isEmpty()) {
            dto = results.get(0);
            dto.build();
            //判断该网点是否故障
            if (dto.getStatus() == WashTerStateEnum.ERROR.getValue()) {
                //说明该网点故障，查明故障原因
                String startTime = TimeUtil.getFrontSeconds(Constant.FAULT_INTERRUPT);
                String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                TerFaultInfo terFaultInfo = terFaultOperationDao.selectLastByCode(dto.getCode(), startTime, endTime);
                if (null != terFaultInfo) {
                    dto.setFaultDesc(terFaultInfo.getFaultDescribe());
                }else {
                    dto.setFaultDesc(dto.getStatusDesc());
                    logger.error("故障信息还未写入到数据库中，查询失败！");
                }
            }

        }else {
            throw new BaseException(ExceptionCode.NOT_TER);
        }
        return dto;
    }
}
