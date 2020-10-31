package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.SimplePermission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created By Gud on 2020/10/31 2:39 下午
 */
public interface PermissionRepo extends Repository<SimplePermission,Long> {

    @Query(nativeQuery = true,value = "select p.id,p.value from (select * from t_employee where mail=?) emp,t_permission p,t_employee_permission ep where ep.employeeId=emp.id and ep.permissionId=p.id" )
    List<SimplePermission> findAllByEmployeeMail(String mail);

    @Query(nativeQuery = true,value = "select p.id,p.value from t_permission p,t_employee_permission ep where ep.employeeId=? and ep.permissionId=p.id" )
    List<SimplePermission> findAllByEmployeeId(long employeeId);
}
