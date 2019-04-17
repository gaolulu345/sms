package com.tp.admin.data.entity;

import com.tp.admin.data.dto.AdminAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountInfo {

    private int id;
    private String intros;
    private String name;
    private int gender;
    private Timestamp bornDate;
    private String telephone;
    private String address;
    private int adminId;


}
