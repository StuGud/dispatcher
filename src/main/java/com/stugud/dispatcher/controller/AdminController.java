package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repository.EmployeeRepository;
import com.stugud.dispatcher.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("admin")
@Controller
public class AdminController {

    final TaskRepository taskRepository;
    final EmployeeRepository employeeRepository;

    public AdminController(TaskRepository taskRepository,EmployeeRepository employeeRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository=employeeRepository;
    }

    @GetMapping("/task")
    public String showTasks(Model model){
        Iterable<Task> tasks = taskRepository.findAll();
        model.addAttribute("tasks",tasks);
        return "tasks";
    }

    @PostMapping("/task")
    public String publishTask(Task task){
        Task save = taskRepository.save(task);
        return "task/"+save.getId();
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

    @GetMapping("/employee")
    public String showEmployees(Model model){
        Iterable<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees",employees);
        return "employees";
    }

    @PostMapping("/employee")
    public String saveEmployee(@RequestBody Employee employee){
        employeeRepository.save(employee);
        return "employees";
    }
}