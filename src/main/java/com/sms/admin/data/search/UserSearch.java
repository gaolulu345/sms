package com.sms.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserSearch extends Search {

    Integer id;
    String phone;
    Integer userType;
    Integer washCardType;
    List<Integer> ids;

    @Override
    public void builData() {
        super.build();
    }
}
