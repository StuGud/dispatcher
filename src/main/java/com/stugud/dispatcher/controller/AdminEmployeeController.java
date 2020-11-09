package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.service.EmployeeService;
import com.stugud.dispatcher.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RequestMapping("/admin")
@Controller
public class AdminEmployeeController {

    final TaskService taskService;
    final EmployeeService employeeService;

    public AdminEmployeeController(TaskService taskService, EmployeeService employeeService) {
        this.taskService = taskService;
        this.employeeService = employeeService;
    }

    /**
     * 来到注册员工页面
     * @param
     * @return
     */
    @GetMapping("/employee")
    public String showEmployeeRegisterPage() {
        return "admin/employee/register";
    }

    /**
     * TODO: 2020/11/4 邮箱存在后如何提示错误
     * 注册新员工，并转到员工列表
     * @param employee
     * @return
     */
    @PostMapping("/employee")
    public String registerEmployee(@Validated Employee employee) {
        if (employeeService.isMailExist(employee.getMail())){
            return null;
        }
        Employee registeredEmployee = employeeService.register(employee);
        if (registeredEmployee==null){
            return null;
        }
        return "redirect:/admin/employees";
    }

    /**
     * 员工列表
     * @return
     */
    @GetMapping("/employees")
    public String showEmployees(Model model) {
        Iterable<Employee> employees = employeeService.findAll();
        model.addAttribute("employees", employees);
        return "admin/employee/employees";
    }

    /**
     * @param model
     * @return
     */
    @GetMapping("/employees/monthlyScore")
    public String showEmpMonthlyScore(Model model, String date) {
        int year = 0;
        int month = 0;
        if (date != null) {
            String[] split = date.split("-");
            if (split.length == 2) {
                year = Integer.parseInt(split[0]);
                month = Integer.parseInt(split[1]);
            }
        }
        if (year == 0 || month == 0) {
            Calendar cal = Calendar.getInstance();
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);
        }

        Map<Employee, Integer> employeeIntegerMap = employeeService.countScoresByMonth(year, month);
        model.addAttribute("employees", employeeIntegerMap);
        model.addAttribute("year",year);
        model.addAttribute("month",month);
        return "/admin/employee/monthlyScore";
    }
}
