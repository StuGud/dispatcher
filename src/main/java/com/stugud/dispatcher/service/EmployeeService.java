package com.stugud.dispatcher.service;

import com.stugud.dispatcher.entity.Employee;

import java.util.List;

public interface EmployeeService {
    /**
     * 查找对应工号的员工的信息
     * @param id
     * @return
     */
    Employee findById(long id);

    /**
     * 没有限制结果集大小,可能导致内存溢出
     * @return
     */
    List<Employee> getList();

    /**
     * 注册员工
     * @param employee
     * @return
     */
    Employee register(Employee employee);


}
