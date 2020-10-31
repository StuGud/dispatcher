package com.stugud.dispatcher.dto;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.SimplePermission;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 注意!使用mail作为username
 * Created By Gud on 2020/10/31 3:12 下午
 */
@Data
public class EmployeeUserDetails implements UserDetails {
    private Employee employee;
    private List<SimplePermission> permissionList;

    public EmployeeUserDetails(Employee employee, List<SimplePermission> permissionList) {
        this.employee = employee;
        this.permissionList = permissionList;
    }


    @Override
    /**
     * @// TODO: 2020/11/1 细化permission
     * 目前只是简单地定义为ROLE_EMPLOYEE
     * @return 返回当前用户的权限
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        /*
        //使用permissionList进行授权
        return permissionList.stream()
                .filter(permission -> permission.getValue()!=null)
                .map(permission ->new SimpleGrantedAuthority(permission.getValue()))
                .collect(Collectors.toList());
         */
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getMail();
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
