package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.TemplateSearch;

import javax.servlet.http.HttpServletRequest;

public interface ALiMiniServiceI {

    ApiResult sendAliTemplate(TemplateSearch templateSearch);
}
