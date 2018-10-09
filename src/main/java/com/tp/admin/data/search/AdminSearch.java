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
public class AdminSearch extends  Search {

    Boolean deleted;

    Boolean more;

    Integer id;

    int[] ids;

    @Override
    public void builData() {
        super.build();
    }
}
