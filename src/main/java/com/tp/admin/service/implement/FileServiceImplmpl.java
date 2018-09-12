package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.config.AliyunOssProperties;
import com.tp.admin.dao.FileUploadLogDao;
import com.tp.admin.data.dto.OrderDTO;
import com.tp.admin.data.dto.UploadFileDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.entity.FileUploadLog;
import com.tp.admin.data.search.FileSearch;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.AliyunOssManagerI;
import com.tp.admin.service.FileServiceI;
import com.tp.admin.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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

    @Override
    public ApiResult uoloadImges(HttpServletRequest request, MultipartFile file) {
        UploadFileDTO uploadFileDTO = aliyunOssManager.uploadFileToAliyunOss(file);
        if (!uploadFileDTO.isSuccess()) {
            throw new BaseException(ExceptionCode.ALI_OSS_UPDATE_ERROR);
        }
        String fileName = uploadFileDTO.getKey();
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        int res = fileUploadLogDao.insert(new FileUploadLog(adminAccount.getName(),fileName));
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult list(HttpServletRequest request, FileSearch fileSearch) {
        fileSearch.build();
        String p = aliyunOssProperties.getServerUrl();
        List<FileUploadLog> list = fileUploadLogDao.listBySearch(fileSearch);
        if (null != list && !list.isEmpty()) {
            for (FileUploadLog f : list){
                f.buildUrl(p);
            }
        }
        int cnt = fileUploadLogDao.cntBySearch(fileSearch);
        fileSearch.setResult(list);
        fileSearch.setTotalCnt(cnt);
        return ApiResult.ok(fileSearch);
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
