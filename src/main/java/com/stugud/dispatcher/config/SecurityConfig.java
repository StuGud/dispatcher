package com.stugud.dispatcher.config;

import com.stugud.dispatcher.dto.EmployeeUserDetails;
import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.SimplePermission;
import com.stugud.dispatcher.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeService employeeService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin").password(new BCryptPasswordEncoder().encode("admin4dfl"))
                .authorities("ROLE_ADMIN");
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/admin", "/admin/*").hasRole("ADMIN")
                .antMatchers("/employee", "/employee/*").hasRole("EMPLOYEE")
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
                .anyRequest()// 除上面外的所有请求全部需要鉴权认证
                .authenticated();
        http.formLogin().defaultSuccessUrl("/loginSuccess", true);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @// TODO: 2020/11/1 permissionList
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> {
            Employee employee = employeeService.findByMail(username);
            if (employee != null) {
                //permissionList为null
                List<SimplePermission> permissionList = employeeService.getPermissionList(employee.getId());
                return new EmployeeUserDetails(employee, permissionList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

}
