package com.sms.admin.dao;

import com.sms.admin.data.dto.UserDTO;
import com.sms.admin.data.search.UserSearch;

import java.util.List;

public interface UserDao {

    int cntBySearch(UserSearch userSearch);

    List<UserDTO> listBySearch(UserSearch userSearch);
}
