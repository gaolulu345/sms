package com.sms.admin;

import com.sms.admin.security.AuthBasicAuthenticationFilter;
import com.sms.admin.security.AutoPasswordEncoder;
import com.sms.admin.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    RedisUtil redisUtil;

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return AutoPasswordEncoder.getInstance();
    }

    @Bean
    public SessionRegistry getSessionRegistry(){
        return new SessionRegistryImpl();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/cxr3duUAE2.txt", "/fuoLd9OtR7.txt" ,"/js/**", "/css/**", "/images/**",
                "/fonts/**", "favicon.ico");
        web.ignoring().antMatchers("/login",  "/error", "/api/user/login","/api/private/**" , "/api/user/logout");
        //可以仿照上面一句忽略静态资源
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .addFilter(new AuthBasicAuthenticationFilter(authenticationManagerBean()));
        http.csrf().disable();
    }

}