package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EmployeeRepo extends PagingAndSortingRepository<Employee,Long> {

    @Query(value = "select * from t_employee,(select employeeId from t_task_employees where taskId=?) inCharge where t_employee.id=inCharge.employeeId",nativeQuery = true)
    //@Query(value = "select * from t_employee,t_task_employees where t_employee.id=t_task_employees.employee and t_task_employees.task=?",nativeQuery = true)
    List<Employee> findAllByTaskId(long taskId);

    List<Employee> findAllByUsername(String username);

    Employee findByMail(String mail);

    @Query(value = "update t_employee set username=:#{#employee.username},mail=:#{#employee.mail},password=:#{#employee.password} where id= :#{#employee.id} ",nativeQuery = true)
    Employee modifyByEmp(Employee employee);
}
