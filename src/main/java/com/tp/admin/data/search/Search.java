package com.tp.admin.data.search;

import com.tp.admin.common.Constant;
import com.tp.admin.utils.StringUtil;
import lombok.Data;

import java.util.List;

@Data
public abstract class Search {

    protected int pageIndex;
    protected int pageSize;
    protected int offset;
    protected int totalCnt;
    private String startTime;
    private String endTime;
    protected List<? extends Object> result;

    protected final void build(){
        if (pageIndex < 1) {
            pageIndex = Constant.Page.DEFAULT_INDEX;
        }
        if (pageSize < 1) {
            pageSize = Constant.Page.DEFAULT_SIZE;
        }
        this.offset = (pageIndex - 1) * pageSize;
        if (!StringUtil.isEmpty(startTime) && StringUtil.toSearchDate(startTime) != null) {
            this.startTime = startTime + " 00:00:00";
        }
        if (!StringUtil.isEmpty(endTime) && StringUtil.toSearchDate(endTime) != null) {
            this.endTime = endTime + " 23:59:59";
        }
        if (StringUtil.isEmpty(this.startTime) ||
                this.startTime.trim().length() == 0
         || StringUtil.isEmpty(this.endTime) ||
                this.endTime.trim().length() == 0) {
            this.startTime = null;
            this.endTime = null;
        }
    }

    protected abstract void builData();

    
}
