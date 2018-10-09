package com.tp.admin.data.table;

import com.tp.admin.data.search.Search;
import lombok.Data;

import java.util.List;

@Data
public class ResultTable {

    protected int pageIndex;
    protected int pageSize;
    protected int offset;
    protected int totalCnt;
    private String startTime;
    private String endTime;
    protected List<? extends Object> result;

    public ResultTable(Search search) {
        this.pageIndex = search.getPageIndex();
        this.pageSize = search.getPageSize();
        this.offset = search.getOffset();
        this.totalCnt = search.getTotalCnt();
        this.startTime = search.getStartTime();
        this.endTime = search.getEndTime();
        this.result = search.getResult();
    }
}
