package com.sms.admin.dao;

import com.sms.admin.data.entity.FileUploadLog;
import com.sms.admin.data.search.FileSearch;

import java.util.List;

public interface FileUploadLogDao {

    int insert(FileUploadLog fileUploadLog);

    int cntBySearch(FileSearch fileSearch);

    List<FileUploadLog> listBySearch(FileSearch fileSearch);

    int bachUpdateDeleted(FileSearch systemSearch);


}
