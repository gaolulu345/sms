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
public class AdminAccount implements UserDetails {

    private int id;
    private String username;
    private String password;
    private String usercode;
    private String intros;
    private String name;
    private int gender;
    private Timestamp bornDate;
    private String telephone;
    private String address;
    private int adminId;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private boolean enable;
    private boolean deleted;
    private Timestamp lastLoginTime;

    private Collection<GrantedAuthority> authorities;

    public AdminAccount(AdminAccountDTO adminAccountDTO){
        this.username = adminAccountDTO.getUsername();
        this.name = adminAccountDTO.getName();
        this.intros = adminAccountDTO.getIntros();
        this.usercode = adminAccountDTO.getUsercode();
        this.gender = adminAccountDTO.getGender();
        this.bornDate = adminAccountDTO.getBornDate();
        this.telephone = adminAccountDTO.getTelephone();
        this.address = adminAccountDTO.getAddress();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }



    @Override
    public String toString() {
        return this.username;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

	@Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }
}
