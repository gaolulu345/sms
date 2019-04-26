package com.sms.admin.manage;

import com.sms.admin.data.dto.UploadFileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AliyunOssManagerI {

    UploadFileDTO uploadFileToAliyunOss(MultipartFile paramMultipartFile , String basePath);

}
