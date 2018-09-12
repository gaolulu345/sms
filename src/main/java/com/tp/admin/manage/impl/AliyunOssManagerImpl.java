package com.tp.admin.manage.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.tp.admin.config.AliyunOssProperties;
import com.tp.admin.data.dto.UploadFileDTO;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.AliyunOssManagerI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AliyunOssManagerImpl implements AliyunOssManagerI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    AliyunOssProperties aliyunOssProperties;

    // 直接扔到OSS
    @Override
    public UploadFileDTO uploadFileToAliyunOss(MultipartFile picture) {
        String exceptionMsg = "";
        String fileName = picture.getOriginalFilename();
        String uoloadPath = this.aliyunOssProperties.getPath() + "/" + fileName;
        String key = "/" + this.aliyunOssProperties.getPath() + "/" + fileName;
        OSSClient ossClient = new OSSClient(this.aliyunOssProperties.getEndpoint(), this.aliyunOssProperties
                .getAccessKeyId(), this.aliyunOssProperties.getAccessKeySecret());
        InputStream inputStream = null;
        UploadFileDTO dto = new UploadFileDTO();
        try {
            ObjectMetadata meta = new ObjectMetadata();
            inputStream = picture.getInputStream();
            meta.setContentType("text/plain");
            ossClient.putObject(this.aliyunOssProperties.getBucketName(), uoloadPath, inputStream);
        } catch (OSSException oe) {
            throw new BaseException(ExceptionCode.ALI_OSS_OPEN_STORAGE_SERVICE_ERROR);
        } catch (ClientException ce) {
            throw new BaseException(ExceptionCode.ALI_OSS_REMOTE_ERROR);
        } catch (IOException e) {
            throw new BaseException(ExceptionCode.ALI_OSS_UPDATE_ERROR);
        } finally {
            ossClient.shutdown();
        }
        dto.setNmae(fileName);
        dto.setUrl(this.aliyunOssProperties.getServerUrl()+key);
        return dto;
    }

}