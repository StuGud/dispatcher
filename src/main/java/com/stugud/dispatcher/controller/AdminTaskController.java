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
    @PostMapping("/task")
    public String releaseTask(Task task, Model model){
        System.out.println("试图新建任务：\n"+task);
        task.setState("未完成");
        //传来的是inCharge的username
        Task newTask=taskService.releaseByInChargeUsername(task);
        if (newTask!=null){
            model.addAttribute("task",newTask);
        }else {
            //重新回到发布任务界面
            //应该可以直接返回错误信息而不跳转
            //这里有点问题
            model.addAttribute("task",task);
            return "noCSS/task/release";
        }
        return "noCSS/task/details";
    }


}
