package com.tp.admin.dao;

import com.tp.admin.data.entity.TerLog;
import com.tp.admin.data.entity.User;
import com.tp.admin.data.search.SystemSearch;
import com.tp.admin.data.search.UserSearch;
import com.tp.admin.data.search.WashLogSearch;

import java.util.List;

public interface TerLogDao {

    List<TerLog> listBySearch(WashLogSearch userSearch);

    int cntBySearch(WashLogSearch washLogSearch);

}
