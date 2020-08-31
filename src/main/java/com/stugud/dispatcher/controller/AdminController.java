package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repository.EmployeeRepository;
import com.stugud.dispatcher.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@Controller
public class AdminController {

    final TaskRepository taskRepository;
    final EmployeeRepository employeeRepository;

    public AdminController(TaskRepository taskRepository,EmployeeRepository employeeRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository=employeeRepository;
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
        Iterable<Task> tasks = taskRepository.findAll();
        model.addAttribute("tasks",tasks);
        return "taskList";
    }

    /**
     * 转到发布任务界面
     * @param
     * @return
     */
    @GetMapping("/task")
    public String showTaskForm(){
        return "taskRelease";
    }

    /**
     *
     * @param task
     * @return
     */
    @PostMapping("/task")
    public String postTask(Task task){
        Task save = taskRepository.save(task);
        //redirect
        return "redirect:/admin/task/id="+save.getId();
    }

    @GetMapping("/task/{id}")
    public String showTaskDetails(Model model,@PathVariable long id){
        Task task=taskRepository.findById(id).get();
        model.addAttribute("task",task);
        return "taskDetails";
    }

    @PatchMapping("/task/{id}")
    public String modifyTask(@PathVariable long id, @RequestBody Task patchTask){
        Task task=taskRepository.findById(id).get();
        if (patchTask.getSubject()!=null){
            task.setSubject(patchTask.getSubject());
        }
        if(patchTask.getContent()!=null){
            task.setContent(patchTask.getContent());
        }
        if(patchTask.getDeadline()!=null){
            task.setDeadline(patchTask.getDeadline());
        }
        if (patchTask.getInCharge()!=null){
            task.setInCharge(patchTask.getInCharge());
        }
        if (patchTask.getLevel()!=null){
            task.setLevel(patchTask.getLevel());
        }
        if(patchTask.getState()!=null){
            task.setState(patchTask.getState());
        }
        taskRepository.save(task);
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
        employeeRepository.save(employee);
        return "redirect:/admin/employees";
    }

    /**
     * 员工列表
     * @return
     */
    @GetMapping("/employees")
    public String showEmployees(Model model){
        Iterable<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees",employees);
        return "employeeList";
    }
}
