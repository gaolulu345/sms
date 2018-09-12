package com.tp.admin.dao;

import com.tp.admin.data.entity.FileUploadLog;
import com.tp.admin.data.search.FileSearch;

import java.util.List;

public interface FileUploadLogDao {

    int insert(FileUploadLog fileUploadLog);

    int cntBySearch(FileSearch fileSearch);

    List<FileUploadLog> listBySearch(FileSearch fileSearch);


}
