package com.tp.admin.security;

import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.service.AccountServiceI;
import com.tp.admin.service.SystemServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
public class AuthCustomUserService implements UserDetailsService {

    @Autowired
    AccountServiceI accountService;

    @Autowired
    SystemServiceI systemService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String exceptionMsg = "";
        AdminAccount adminAccount = accountService.findByUsername(username);
        if (adminAccount == null) {
            exceptionMsg = "no user found math name:" + username;
            throw new UsernameNotFoundException(exceptionMsg);
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Set<AutoResource> autoResources = systemService.findAdminAutoResource(adminAccount);
        if (null != autoResources && !autoResources.isEmpty()) {
            authorities.addAll(autoResources);
        }
        return buildUserForAuthentication(adminAccount, authorities);
    }

    private AdminAccount buildUserForAuthentication(AdminAccount user, Collection<GrantedAuthority> authorities) {
        return new AdminAccount(user.getId(), user.getUsername() ,user.getName(),  user.getPassword(), authorities);
    }

}