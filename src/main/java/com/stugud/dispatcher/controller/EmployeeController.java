package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.dto.EmployeeUserDetails;
import com.stugud.dispatcher.entity.Commit;
import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.service.CommitService;
import com.stugud.dispatcher.service.EmployeeService;
import com.stugud.dispatcher.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    private static final Logger LOGGER= LoggerFactory.getLogger(EmployeeController.class);

    final EmployeeService employeeService;

    final TaskService taskService;

    @Autowired
    CommitService commitService;

    public EmployeeController(EmployeeService employeeService, TaskService taskService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
    }

    /**
     * 获取员工个人信息
     * @param model
     * @return
     */
    @GetMapping("/details")
    public String showEmployeeDetails(Model model) {
        Employee employee = employeeService.getCurrentEmployee();
        model.addAttribute("employee", employee);
        return "/employee/details";
    }

    /**
     * 修改员工个人信息
     * 暂时说，只可以修改密码
     * @param modifiedEmp
     * @return
     */
    @PutMapping("/details")
    public String modifyEmpDetails(Model model,Employee modifiedEmp){
        Employee employee = employeeService.modifyByEmpself(modifiedEmp);
        model.addAttribute("employee",employee);
        return "/employee/details";
    }

    /**
     * 根据status获取任务列表：已完成、未完成、全部
     * @return
     */
    @GetMapping("/tasks")
    public String showTasks(Model model,String state) {
        if (state==null){
            state="all";
        }
        Employee currentEmployee = employeeService.getCurrentEmployee();
        List<Task> tasks = taskService.findAllByEmpIdAndState(currentEmployee.getId(),state);
        model.addAttribute("tasks", tasks);
        return "/employee/task/tasks";
    }

    /**
     * 查看任务详情;附带commit记录
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/task/{id}")
    public String showTaskDetails(Model model, @PathVariable long id) {
        Task task = taskService.findById(id);
        long commitDownloadId=0;
        if (task.getState().equals("已完成")){
            Commit lastPassedCommit = commitService.findLastPassedCommitByTaskId(id);
            if (null!=lastPassedCommit){
                commitDownloadId=lastPassedCommit.getId();
            }
        }

        List<Commit> commits=commitService.findAllByTaskId(id);
        model.addAttribute("task", task);
        model.addAttribute("commitDownloadId", commitDownloadId);
        model.addAttribute("commits",commits);
        return "/employee/task/details";
    }


    /**
     * 如果任务状态为已完成，则下载最新一次通过的commit
     * @param taskId
     * @return
     */
    @GetMapping("/task/{taskId}/download")
    public String downloadCommitByTaskId(Model model,@PathVariable(name = "taskId") long taskId){
        Commit commit = commitService.findLastPassedCommitByTaskId(taskId);

        return "noCSS/employee/commit";
    }

    /**
     * 来到指定任务的commit界面
     * @param taskId
     * @return
     */
    @GetMapping("/task/{taskId}/commit")
    public String showCommitPage(@PathVariable(name = "taskId") long taskId){
        return "noCSS/employee/commit";
    }

    @PostMapping("task/{taskId}/commit")
    public void commit(@PathVariable(name = "taskId") long taskId,@RequestParam("file") MultipartFile file ,Commit commit){
        System.out.println(commit);
        System.out.println(file);
    }

    /**
     * 下载commit
     * @param commitId
     */
    @GetMapping("/commit/{commitId}/download")
    public void downloadCommit(@PathVariable(name = "commitId") long commitId){

    }
}
