package com.tp.admin.data.dto;

import lombok.Data;

@Data
public class UploadFileDTO {

    String name;

    String key;

    String url;

    /**
     * 是否上传成功
     */
    boolean success = true;

    /**
     * 错误消息
     */
    String errorMsg;

    public UploadFileDTO() {
    }

    public UploadFileDTO(boolean success, String errorMsg) {
        this.success = success;
        this.errorMsg = errorMsg;
    }

}
