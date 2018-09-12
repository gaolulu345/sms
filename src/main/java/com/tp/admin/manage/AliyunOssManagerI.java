package com.tp.admin.manage;

import com.tp.admin.data.dto.UploadFileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AliyunOssManagerI {

    UploadFileDTO uploadFileToAliyunOss(MultipartFile paramMultipartFile);

}
