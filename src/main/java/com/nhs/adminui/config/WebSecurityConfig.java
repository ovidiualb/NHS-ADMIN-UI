package com.nhs.adminui.config;

import com.nhs.adminui.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Authentication configuration:
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(adminService)
                .passwordEncoder(passwordEncoder);
    }

    /**
     * Authorization configuration:
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").anonymous()
                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home-initial", true)
                .and()

                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()

                .csrf().disable()
                .headers().frameOptions().disable();
    }

    /**
     * Web security configuration:
     */
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/assets/**"); // EVERYTHING IN "STATIC/ASSETS" IS SKIPPED.
    }
}
