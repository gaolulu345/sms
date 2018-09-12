package com.tp.admin.data.dto;

public class UploadFileDTO {

    String nmae;

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


    public String getNmae() {
        return nmae;
    }

    public void setNmae(String nmae) {
        this.nmae = nmae;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
