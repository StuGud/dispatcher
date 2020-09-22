package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminTaskController {
    final
    TaskService taskService;

    public AdminTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 转到任务发布界面
     *
     * @return
     */
    @GetMapping("/task")
    public String showTaskRelease() {
        return "noCSS/task/release";
    }

    /**
     * 发布任务后转到任务详情界面
     *
     * @return
     */
    @PostMapping("/task")
    public String releaseTask(Task task, Model model) {
        System.out.println("试图新建任务：\n" + task);
        task.setState("未完成");
        //传来的是inCharge的username
        Task newTask = taskService.releaseByInChargeUsername(task);
        if (newTask != null) {
            model.addAttribute("task", newTask);
        } else {
            //重新回到发布任务界面
            //应该可以直接返回错误信息而不跳转
            //这里有点问题
            model.addAttribute("task", task);
            return "noCSS/task/release";
        }
        return "noCSS/task/details";
    }

    @GetMapping("/task/{id}")
    public String showTaskDetails(Model model, @PathVariable long id) {
        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        return "noCSS/task/details";
    }

    /**
     * 修改任务 以任务id为基础进行修改
     * 未完成-》已完成 单独处理;计算score;发送邮件提醒
     *
     * @param model
     * @param task
     * @return
     */
    @PutMapping("/task")
    public String modifyTask(Model model, Task task) {
        System.out.println("PUT!!" + task);
        model.addAttribute("task", task);
        return "noCSS/task/details";
    }

    @PatchMapping("/task/{id}/setCompleted")
    @ResponseBody
    public Task setCompleted(@PathVariable(name = "id") long taskId) {
        Task task = taskService.setCompleted(taskId);
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
        return "task";
    }

    @GetMapping("/taskPageInit")
    public List<Task> initTaskPage() {
        return taskService.findTaskPage(0);
    }
    @GetMapping("/taskPage/{pageNum}")
    public List<Task> showTaskPage(@PathVariable int pageNum) {
        return taskService.findTaskPage(pageNum);
    }

    @GetMapping("/testJSON/{id}")
    @ResponseBody
    public Task testJSON(@PathVariable(name = "id") long taskId) {
        Task task = taskService.findById(taskId);
        return task;
    }


}
