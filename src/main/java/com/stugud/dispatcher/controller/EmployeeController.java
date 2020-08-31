package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repository.EmployeeRepository;
import com.stugud.dispatcher.repository.TaskRepository;
import com.stugud.dispatcher.service.EmployeeService;
import com.stugud.dispatcher.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    final EmployeeService employeeService;

    final TaskService taskService;


    public EmployeeController(EmployeeService employeeService,TaskService taskService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
    }

    @GetMapping("/details")
    public String showEmployeeDetails(Model model, @AuthenticationPrincipal Employee employee){
        Employee employee1 = employeeService.findById(employee.getId());
        model.addAttribute("employee",employee1);
        return "employeeDetails";
    }

    @GetMapping("/tasks")
    public String showTasks(Model model, @AuthenticationPrincipal Employee employee){
        List<Task> tasks = taskService.findByEmpId(employee.getId());
        model.addAttribute("tasks",tasks);
        return "tasks";
    }

    @GetMapping("/task/{id}")
    public String showTaskDetails(Model model, @PathVariable long id){
        Task task = taskService.findById(id);
        model.addAttribute("task",task);
        return "taskDetails";
    }
}
