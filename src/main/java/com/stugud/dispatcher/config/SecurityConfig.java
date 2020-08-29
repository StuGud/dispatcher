package com.stugud.dispatcher.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin123456")
                .authorities("ROLE_ADMIN")
                .and()
                .withUser("employee").password("123456")
                .authorities("ROLE_EMPLOYEE");
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username,password from t_employee where username=?")
                .authoritiesByUsernameQuery("select username,authority from t_authorities where username=?");

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ROLE_ADMIN")
                .antMatchers("/employee").hasRole("ROLE_EMPLOYEE")
                .antMatchers("/","/**").permitAll();
    }
}
