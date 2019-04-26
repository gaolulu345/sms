package com.sms.admin.data.search;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SystemSearch extends Search {

    Integer id;

    Integer rolesId;

    Integer menuId;

    Integer operationsId;

    int[] ids;

    boolean enable = true;

    boolean deleted = false;

    boolean all = false;

    @Override
    public void builData() {
        super.build();
    }
}
