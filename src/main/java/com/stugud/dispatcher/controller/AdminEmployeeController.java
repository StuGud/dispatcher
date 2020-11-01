package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.service.EmployeeService;
import com.stugud.dispatcher.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 暂时不实现
 */
@RequestMapping("/admin")
@Controller
public class AdminEmployeeController {

    final TaskService taskService;
    final EmployeeService employeeService;

    public AdminEmployeeController(TaskService taskService, EmployeeService employeeService) {
        this.taskService = taskService;
        this.employeeService=employeeService;
    }

    /**
     * 来到注册员工页面
     * @param
     * @return
     */
    @GetMapping("/employee")
    public String showEmployeeRegistration(){
        return "admin/employee/register";
    }

    /**
     * 注册新员工，并转到员工列表
     * @param employee
     * @return
     */
    @PostMapping("/employee")
    public String saveEmployee( Employee employee){
        employeeService.register(employee);
        return "redirect:/admin/employees";
    }

    /**
     * 员工列表
     * @return
     */
    @GetMapping("/employees")
    public String showEmployees(Model model){
        Iterable<Employee> employees = employeeService.findAll();
        model.addAttribute("employees",employees);
        return "admin/employee/employees";
    }

    /**
     *
     * @param model
     * @param year url中指定年
     * @param month url中指定月份
     * @return
     */
    @GetMapping("/employees/monthlyScore")
    public String showEmpMonthlyScore(Model model,int year,int month){
        Map<Employee, Integer> employeeIntegerMap = employeeService.countScoresByMonth(year,month);
        model.addAttribute("employees",employeeIntegerMap);
        return "employee/monthlyScore";
    }
}
