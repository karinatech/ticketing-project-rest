package com.cybertek.config;

import com.cybertek.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationManager authenticationManager()throws Exception{
        return super.authenticationManagerBean();
    }
    private static final String[]permittedUrls={
            "/authenticate","/create-user","/api/p1/**","/v3/api-docs/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**"

    };

    @Autowired
    private SecurityFilter securityFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/authenticate","/create-user")
                .permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
