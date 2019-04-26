package com.sms.admin.service.implement;

import com.sms.admin.data.table.ResultTable;
import com.sms.admin.ajax.ApiResult;
import com.sms.admin.dao.UserDao;
import com.sms.admin.data.dto.UserDTO;
import com.sms.admin.data.search.UserSearch;
import com.sms.admin.service.WashUserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class WashUserServiceImpl implements WashUserServiceI {

    @Autowired
    UserDao userDao;

    @Override
    public ApiResult list(HttpServletRequest request, UserSearch userSearch) {
        userSearch.builData();
        List<UserDTO> list = userDao.listBySearch(userSearch);
        if (null != list && !list.isEmpty()) {
            for (UserDTO u : list) {
                u.build();
            }
            int cnt = userDao.cntBySearch(userSearch);
            userSearch.setResult(list);
            userSearch.setTotalCnt(cnt);
        }else{
            userSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(userSearch));
    }

    @Override
    public ApiResult listExport(HttpServletRequest request, UserSearch userSearch) {
        return null;
    }
}
