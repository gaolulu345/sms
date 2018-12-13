package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.TemplateSearch;


public interface AliMiniServiceI {

    ApiResult sendAliTemplate(TemplateSearch templateSearch);
}
