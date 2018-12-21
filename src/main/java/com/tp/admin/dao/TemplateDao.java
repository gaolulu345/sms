package com.tp.admin.dao;

import com.tp.admin.data.dto.UserDTO;
import com.tp.admin.data.dto.UserMemberDTO;
import com.tp.admin.data.entity.AdminServiceInfo;
import com.tp.admin.data.entity.AdminTemplateInfo;
import com.tp.admin.data.search.AdminAutoSearch;
import com.tp.admin.data.search.UserSearch;

import java.util.List;

public interface TemplateDao {

    int insertTemplateInfo(AdminTemplateInfo adminTemplateInfo);

    String searchMasterplateTool(AdminAutoSearch adminAutoSearch);

    List<AdminServiceInfo> searchServiceInfoList(AdminAutoSearch adminAutoSearch);

    AdminTemplateInfo searchTemplateId(Integer id);
}
