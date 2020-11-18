package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Commit;
import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.service.CommitService;
import com.stugud.dispatcher.service.EmployeeService;
import com.stugud.dispatcher.service.TaskService;
import com.stugud.dispatcher.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    final EmployeeService employeeService;

    final TaskService taskService;

    final
    CommitService commitService;

    public EmployeeController(EmployeeService employeeService, TaskService taskService, CommitService commitService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
        this.commitService = commitService;
    }

    /**
     * 获取员工个人信息
     *
     * @param model
     * @return
     */
    @GetMapping("/details")
    public String showEmployeeDetails(Model model) {
        Employee employee = employeeService.getCurrentEmployee();
        model.addAttribute("employee", employee);
        return "employee/details";
    }

    /**
     * 修改员工个人信息
     * 暂时说，只可以修改密码
     *
     * @param modifiedEmp
     * @return
     * @// TODO: 2020/11/4 改为ajax，输出提示信息（成功或失败）
     */
    @PutMapping("/details")
    public String modifyEmpDetails(Model model, Employee modifiedEmp) {
        Employee employee = employeeService.modifyByEmpself(modifiedEmp);
        model.addAttribute("employee", employee);
        return "employee/details";
    }

    /**
     * 根据status获取任务列表：已完成、未完成、全部
     *
     * @return
     */
    @GetMapping("/tasks")
    public String showTasks(Model model, String state) {
        if (state == null) {
            state = "all";
        }
        Employee currentEmployee = employeeService.getCurrentEmployee();
        List<Task> tasks = taskService.findAllByEmpIdAndState(currentEmployee.getId(), state);
        model.addAttribute("tasks", tasks);
        return "employee/task/tasks";
    }

    /**
     * 查看任务详情;附带commit记录
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/task/{id}")
    public String showTaskDetails(Model model, @PathVariable long id) {
        Task task = taskService.findById(id);
        long commitDownloadId = 0;
        if (task.getState() == 1) {
            Commit lastPassedCommit = commitService.findLastPassedCommitByTaskId(id);
            if (null != lastPassedCommit) {
                commitDownloadId = lastPassedCommit.getId();
            }
        }
        List<Commit> commits = commitService.findAllByTaskId(id);
        model.addAttribute("task", task);
        model.addAttribute("commitDownloadId", commitDownloadId);
        model.addAttribute("commits", commits);
        return "employee/task/details";
    }


    /**
     *
     * @param taskId
     * @return
     */
    @GetMapping("/task/{taskId}/download")
    public void downloadTask(HttpServletResponse response, @PathVariable(name = "taskId") long taskId) {
        Employee currentEmployee = employeeService.getCurrentEmployee();
        //有无权限下载该task
        if (employeeService.isEmployeeInChargeTask(currentEmployee.getId(), taskId)) {
            taskService.downloadFile(response, taskId);
        }
    }

    /**
     * 来到指定任务的commit界面
     *
     * @param taskId
     * @return
     */
    @GetMapping("/task/{taskId}/commit")
    public String showCommitPage(Model model, @PathVariable(name = "taskId") long taskId) {
        Task task = taskService.findById(taskId);
        Employee currentEmployee = employeeService.getCurrentEmployee();
        model.addAttribute("task", task);
        model.addAttribute("employee", currentEmployee);

        return "employee/task/commit";
        //return "noCSS/employee/commit";
    }

    @PostMapping("task/{taskId}/commit")
    public String commit(@PathVariable(name = "taskId") long taskId, @RequestParam("file") MultipartFile file, Commit commit) {
        if (file == null || file.isEmpty() || commit == null) {
            return null;
        }
        Employee currentEmployee = employeeService.getCurrentEmployee();
        commit.setEmployeeId(currentEmployee.getId());
        //做一些认证：有没有权限提交该commit
        if (employeeService.isEmployeeInChargeTask(currentEmployee.getId(), taskId)) {
            commit.setTaskId(taskId);
            Commit savedCommit = commitService.commit(commit, file);
            return "redirect:/employee/task/" + savedCommit.getTaskId();
        }
        //转到没有权限的位置
        return null;
    }

    /**
     * 下载commit
     *
     * @param commitId
     */
    @GetMapping("/commit/{commitId}/download")
    public void downloadCommit(@PathVariable(name = "commitId") long commitId,
                               HttpServletResponse response) {
        //做一些认证：有没有权限下载该commit
        Employee currentEmployee = employeeService.getCurrentEmployee();
        Commit commit = commitService.findById(commitId);
        if (employeeService.isEmployeeInChargeTask(currentEmployee.getId(), commit.getTaskId())) {
            commitService.downloadFile(response, commit);
        }
    }

    @GetMapping("/commits")
    public String showCommitsPage(Model model) {
        Employee currentEmployee = employeeService.getCurrentEmployee();
        List<Commit> commits = commitService.findAllByEmpId(currentEmployee.getId());
        model.addAttribute("commits", commits);
        return "employee/task/commits";
    }

}
