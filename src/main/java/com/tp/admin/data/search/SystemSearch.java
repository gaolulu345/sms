package com.tp.admin.data.search;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SystemSearch extends Search {

    int id;

    int rolesId;

    int menuId;

    int operationsId;

    int[] ids;

    boolean enable = true;

    boolean deleted = false;

    boolean all = false;

}
