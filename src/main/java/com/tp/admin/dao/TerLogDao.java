package com.tp.admin.dao;

import com.tp.admin.data.entity.TerLog;
import com.tp.admin.data.entity.User;
import com.tp.admin.data.search.SystemSearch;
import com.tp.admin.data.search.UserSearch;
import com.tp.admin.data.search.WashLogSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerLogDao {

    List<TerLog> listBySearch(WashLogSearch userSearch);

    TerLog latestRecordByCode(@Param("code") String code);

    int cntBySearch(WashLogSearch washLogSearch);

}
