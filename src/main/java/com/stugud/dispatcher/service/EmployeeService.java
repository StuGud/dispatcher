package com.stugud.dispatcher.service;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.SimplePermission;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface EmployeeService {
    Employee modifyByEmpself(Employee modifiedEmployee);

    Employee getCurrentEmployee();

    /**
     *
     * @param month
     * @return 返回员工以及指定月份的总分
     */
    Map<Employee, Integer> countScoresByMonth(int year,int month);

    /**
     * 查找对应工号的员工的信息
     * @param id
     * @return
     */
    Employee findById(long id);

    Employee findByMail(String mail);


    /**
     * 注册员工；Ps.只有管理员才可以注册新员工
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

    List<SimplePermission> getPermissionList(long empId);

    boolean isEmployeeInChargeTask(long employeeId,long taskId);

    boolean isMailExist(String mail);

    List<Employee> findLeaders(long employeeId);

     Employee save(Employee employee);
}
