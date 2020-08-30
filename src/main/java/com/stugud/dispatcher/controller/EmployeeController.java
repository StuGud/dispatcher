package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repository.EmployeeRepository;
import com.stugud.dispatcher.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    final
    EmployeeRepository employeeRepository;

    final
    TaskRepository taskRepository;


    public EmployeeController(EmployeeRepository employeeRepository,TaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/details")
    public String showEmployeeDetails(Model model, @AuthenticationPrincipal Employee employee){
        Optional<Employee> employee1 = employeeRepository.findById(employee.getId());
        model.addAttribute("employee",employee1.get());
        return "details";
    }

    @GetMapping("/task")
    public String showTasks(Model model, @AuthenticationPrincipal Employee employee){
        Optional<Employee> employee1 = employeeRepository.findById(employee.getId());
        model.addAttribute("employee",employee1.get());
        return "details";
    }

    @GetMapping("/task/{id}")
    public String showTaskDetails(Model model, @PathVariable long id){
        Optional<Task> taskById = taskRepository.findById(id);
        model.addAttribute("task",taskById.get());
        return "taskDetails";
    }
}
