package com.tp.admin.dao;

import com.tp.admin.data.dto.UserDTO;
import com.tp.admin.data.dto.UserMemberDTO;
import com.tp.admin.data.entity.AdminTemplateInfo;
import com.tp.admin.data.search.UserSearch;

import java.util.List;

public interface TemplateDao {

    String searchMasterplateTool(String key);

    AdminTemplateInfo searchTemplateId(Integer id);
}
