package com.tp.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderSearch extends Search {

    private Integer status;
    private String startTime;
    private String endTime;
    private List<Integer> terIds;

}
