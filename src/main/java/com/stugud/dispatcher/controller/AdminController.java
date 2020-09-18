package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.service.EmployeeService;
import com.stugud.dispatcher.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    final TaskService taskService;
    final EmployeeService employeeService;

    public AdminController(TaskService taskService,EmployeeService employeeService) {
        this.taskService = taskService;
        this.employeeService=employeeService;
    }

    @RequestMapping("/test")
    public String test(){
        return "task";
    }

    /*
    =========================================================
    task
     */
    @GetMapping("/tasks")
    public String showTaskList(Model model){
        List<Task> tasks = taskService.findAll();
        model.addAttribute("tasks",tasks);
        return "task";
    }



    @GetMapping("/task/{id}")
    public String showTaskDetails(Model model,@PathVariable long id){
        Task task=taskService.findById(id);
        model.addAttribute("task",task);
        return "taskDetails";
    }

    @PatchMapping("/task/{id}")
    public String modifyTask(@PathVariable long id, @RequestBody Task patchTask){
        //这里逻辑有问题，如果本身就是已完成，未考虑页面回显
        if(patchTask.getState()=="已完成"){
            taskService.setCompleted(patchTask);
        }else{
            taskService.modify(patchTask);
        }
        return "taskDetails";
    }

    /*
    =========================================================
    employee
     */

    /**
     * 来到注册员工页面
     * @param
     * @return
     */
    @GetMapping("/employee")
    public String showEmployeeRegistration(){
        return "employeeRegistration";
    }

    /**
     * 注册新员工，并转到员工列表
     * @param employee
     * @return
     */
    @PostMapping("/employee")
    public String saveEmployee(@RequestBody Employee employee){
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
        return "employeeList";
    }
}
