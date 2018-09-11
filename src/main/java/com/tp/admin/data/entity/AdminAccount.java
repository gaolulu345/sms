package com.tp.admin.data.entity;

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
    private String intros;
    private String name;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private boolean enable;
    private boolean deleted;
    private Timestamp lastLoginTime;

    private Collection<GrantedAuthority> authorities;

    public AdminAccount(int id ,String name , String password, Collection<GrantedAuthority> auths) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.authorities = auths;
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
        return true;
    }
}
