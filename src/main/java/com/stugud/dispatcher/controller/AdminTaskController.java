package com.stugud.dispatcher.controller;

import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * @return
     */
    @GetMapping("/task")
    public String showTaskRelease(){
        return "noCSS/task/release";
    }

    /**
     * 发布任务后转到任务详情界面
     * @return
     */
    @PostMapping
    public String releaseTask(Task task, Model model){
        //传来的是inCharge的username
        Task newTask=taskService.releaseByInChargeUsername(task);


//        model.addAttribute()
        return "noCSS/task/details";
    }


}
