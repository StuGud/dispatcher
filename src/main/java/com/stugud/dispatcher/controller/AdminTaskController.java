package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Commit;
import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.service.CommitService;
import com.stugud.dispatcher.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminTaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    final
    TaskService taskService;

    final
    CommitService commitService;

    public AdminTaskController(TaskService taskService, CommitService commitService) {
        this.taskService = taskService;
        this.commitService = commitService;
    }

    /**
     * @return 转到任务发布界面
     */
    @GetMapping("/task")
    public String showReleasePage() {
        return "admin/task/release";
    }

    /**
     * 发布任务
     *
     * @return 转到任务详情界面
     */
    @PostMapping("/task")
    public String releaseTask(Task task, @RequestParam("file") MultipartFile file, Model model) {
        //传来的是inCharge的username
        Task newTask = taskService.releaseWithInChargesName(task,file);
        if (newTask != null) {
            model.addAttribute("task", newTask);
        } else {
            //重新回到发布任务界面
            //应该可以直接返回错误信息而不跳转
            //这里有点问题
            model.addAttribute("task", task);
            return "admin/task/release";
        }
        return "admin/task/details";
    }

    @GetMapping("/task/{id}")
    public String showTaskDetails(Model model, @PathVariable long id) {
        Task task = taskService.findById(id);
        List<Commit> commits = commitService.findAllByTaskId(id);
        if (null == task) {
            LOGGER.info("查找任务taskId{}失败", id);
        }
        model.addAttribute("task", task);
        model.addAttribute("commits", commits);
        return "admin/task/details";
    }

    /**
     * 修改任务 以任务id为基础进行修改
     * 未完成-》已完成 单独处理;计算score;发送邮件提醒
     *
     * @param model
     * @param task
     * @return
     * @// TODO: 2020/11/4 返回错误页面
     */
    @PutMapping("/task")
    public String modifyTask(Model model, Task task) {
        LOGGER.info("修改{}",task);
        Task savedTask = taskService.modify(task);
        if (savedTask == null) {
            return "admin/task/details";
        }
        model.addAttribute("task", savedTask);
        return "admin/task/details";
    }

    @GetMapping("/task/{id}/download")
    @ResponseBody
    public void download(@PathVariable(name = "id") long taskId,HttpServletResponse response) {
        taskService.downloadFile(response,taskId);
    }

    @PatchMapping("/task/{id}/setCompleted")
    @ResponseBody
    public Task setCompleted(@PathVariable(name = "id") long taskId) {
        Task task = taskService.setCompleted(taskId);
        return task;
    }

    @GetMapping("/task/{id}/setNotCompleted")
    @ResponseBody
    public Task setNotCompleted(@PathVariable(name = "id") long taskId) {
        Task task = null;
        try {
            task = taskService.setNotCompleted(taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    /**
     * @param model
     * @return
     */
    @GetMapping("/tasks")
    public String showTaskList(Model model) {
        List<Task> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "admin/task/tasks";
    }

    @GetMapping("/taskPageInit")
    public List<Task> initTaskPage() {
        return taskService.findAllByPageNum(0);
    }

    @GetMapping("/taskPage")
    public List<Task> showTaskPage(int pageNum) {
        return taskService.findAllByPageNum(pageNum);
    }


}
