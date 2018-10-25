package com.tp.admin.dao;

import com.tp.admin.data.dto.UserDTO;
import com.tp.admin.data.entity.User;
import com.tp.admin.data.search.UserSearch;

import java.util.List;

public interface UserDao {

    int cntBySearch(UserSearch userSearch);

    List<UserDTO> listBySearch(UserSearch userSearch);
}
