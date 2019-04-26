package com.sms.admin.service.implement;

import com.sms.admin.data.table.ResultTable;
import com.sms.admin.ajax.ApiResult;
import com.sms.admin.config.AliyunOssProperties;
import com.sms.admin.dao.AdminAccountInfoDao;
import com.sms.admin.dao.FileUploadLogDao;
import com.sms.admin.data.dto.UploadFileDTO;
import com.sms.admin.data.entity.AdminAccount;
import com.sms.admin.data.entity.AdminAccountInfo;
import com.sms.admin.data.entity.FileUploadLog;
import com.sms.admin.data.search.FileSearch;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.manage.AliyunOssManagerI;
import com.sms.admin.service.FileServiceI;
import com.sms.admin.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class FileServiceImplmpl implements FileServiceI {

    @Autowired
    FileUploadLogDao fileUploadLogDao;

    @Autowired
    AliyunOssProperties aliyunOssProperties;

    @Autowired
    AliyunOssManagerI aliyunOssManager;

    @Autowired
    AdminAccountInfoDao adminAccountInfoDao;

    @Override
    public ApiResult uploadImges(HttpServletRequest request, MultipartFile file) {
        UploadFileDTO uploadFileDTO = aliyunOssManager.uploadFileToAliyunOss(file ,aliyunOssProperties.getPath());
        if (!uploadFileDTO.isSuccess()) {
            throw new BaseException(ExceptionCode.ALI_OSS_UPDATE_ERROR);
        }
        String fileName = uploadFileDTO.getKey();
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        AdminAccountInfo adminAccountInfo = adminAccountInfoDao.findByAdminId(adminAccount.getId());
        if (null == adminAccountInfo){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        int res = fileUploadLogDao.insert(new FileUploadLog(adminAccountInfo.getName(),fileName));
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult list(HttpServletRequest request, FileSearch fileSearch) {
        fileSearch.builData();
        String p = aliyunOssProperties.getServerUrl();
        List<FileUploadLog> list = fileUploadLogDao.listBySearch(fileSearch);
        if (null != list && !list.isEmpty()) {
            for (FileUploadLog f : list){
                f.buildUrl(p);
            }
            int cnt = fileUploadLogDao.cntBySearch(fileSearch);
            fileSearch.setResult(list);
            fileSearch.setTotalCnt(cnt);
        }else {
            fileSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(fileSearch));
    }

    @Override
    public ApiResult bachDeleteImges(HttpServletRequest request, FileSearch fileSearch) {
        if (fileSearch.getIds().length == 0) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        int res = fileUploadLogDao.bachUpdateDeleted(fileSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }
}
