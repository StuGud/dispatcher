package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.service.EmployeeService;
import com.stugud.dispatcher.service.TaskService;
import com.stugud.dispatcher.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    final EmployeeService employeeService;

    final TaskService taskService;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String  tokenHead;

    public EmployeeController(EmployeeService employeeService,TaskService taskService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
    }


    /**
     * 使用 邮箱！以及密码登录
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/login")
    public Object login(String username, String password, HttpServletResponse response){
        String token = employeeService.login(username,password);
        if (token == null) {
            //return "用户名或密码错误";
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        response.setHeader(JwtUtil.AUTH_HEADER_KEY,tokenHead+" "+token);
        return "redirect:employee/test1";
    }



    @GetMapping("/details")
    public String showEmployeeDetails(Model model, @AuthenticationPrincipal Employee employee){
        Employee employee1 = employeeService.findById(employee.getId());
        model.addAttribute("employee",employee1);
        return "employeeDetails";
    }

    @GetMapping("/tasks")
    public String showTasks(Model model, @AuthenticationPrincipal Employee employee){
        List<Task> tasks = taskService.findAllByEmpId(employee.getId());
        model.addAttribute("tasks",tasks);
        return "tasks";
    }

    @GetMapping("/task/{id}")
    public String showTaskDetails(Model model, @PathVariable long id){
        Task task = taskService.findById(id);
        model.addAttribute("task",task);
        return "taskDetails";
    }


    @GetMapping("/test")
    @ResponseBody
    public String testSecurity(@AuthenticationPrincipal Employee employee){
        System.out.println(employee);
        return "大傻逼";
    }

    @PreAuthorize("hasAuthority('employee')")
    @GetMapping("/test1")
    @ResponseBody
    public String testSecurity1(@AuthenticationPrincipal Employee employee){
        System.out.println(employee);
        return "大傻逼";
    }
}
