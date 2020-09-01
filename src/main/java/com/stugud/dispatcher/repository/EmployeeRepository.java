package com.stugud.dispatcher.repository;

import com.stugud.dispatcher.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee,Long> {

    @Query(value = "select * from t_employee,(select employee from t_task_employees where task=?) inCharge where t_employee.id=inCharge.employee",nativeQuery = true)
    //@Query(value = "select * from t_employee,t_task_employees where t_employee.id=t_task_employees.employee and t_task_employees.task=?",nativeQuery = true)
    List<Employee> findEmployeesByTaskId(long taskId);
}
