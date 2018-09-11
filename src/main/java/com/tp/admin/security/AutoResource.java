package com.tp.admin.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class AutoResource implements GrantedAuthority {

    String name;

    String method;

    String url;

    int order;

    int type = AuthResourceTypeEnum.DEFAULT.getValue();

    @Override
    public String getAuthority() {
        return this.url + "-" + this.method;
    }
}
