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
     * 注册员工
     * @param employee
     * @return
     */
    Employee register(Employee employee);

    /**
     * 查找负责某任务的员工列表
     * @param taskId
     * @return
     */
    List<Employee> findAllByTaskId(long taskId);

    /**
     * 返回所有的员工列表
     * 没有限制结果集大小,可能导致内存溢出
     * @return
     */
    List<Employee> findAll();

    /**
     * 分页返回所有的员工列表
     * @param pageNum 页数
     * @return
     */
    List<Employee> findAllByPageNum(int pageNum);
}
