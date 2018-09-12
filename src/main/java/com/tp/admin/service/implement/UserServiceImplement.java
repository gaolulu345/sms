package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.UserDao;
import com.tp.admin.data.entity.User;
import com.tp.admin.data.search.UserSearch;
import com.tp.admin.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class UserServiceImplement implements UserServiceI {

    @Autowired
    UserDao userDao;

    @Override
    public ApiResult list(HttpServletRequest request, UserSearch userSearch) {
        userSearch.build();
        List<User> list = userDao.listBySearch(userSearch);
        int cnt = userDao.cntBySearch(userSearch);
        userSearch.setResult(list);
        userSearch.setTotalCnt(cnt);
        return ApiResult.ok(userSearch);
    }
}
