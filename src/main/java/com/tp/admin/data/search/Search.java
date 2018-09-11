package com.tp.admin.data.search;

import com.tp.admin.common.Constant;
import lombok.Data;

import java.util.List;

@Data
public class Search {

    protected int pageIndex;
    protected int pageSize;
    protected int offset;
    protected int totalCnt;
    protected List<? extends Object> result;

    public final void build(){
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize < 1) {
            pageSize = Constant.Page.DEFAULT_SIZE;
        }
        this.offset = (pageIndex - 1) * pageSize;
    }
    
}
