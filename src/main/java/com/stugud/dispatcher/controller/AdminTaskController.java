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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminTaskController {

    private static final Logger LOGGER= LoggerFactory.getLogger(EmployeeController.class);

    final
    TaskService taskService;


    @Autowired
    CommitService commitService;

    public AdminTaskController(TaskService taskService) {
        this.taskService = taskService;
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
     * @return 转到任务详情界面
     */
    @PostMapping("/task")
    public String releaseTask(Task task, Model model) {
        LOGGER.info("试图新建任务{}",task);
        task.setState("未完成");
        //传来的是inCharge的username
        Task newTask = taskService.releaseWithInChargesName(task);
        if (newTask != null) {
            setEmpPswInvisible(newTask);
            model.addAttribute("task", newTask);
        } else {
            //重新回到发布任务界面
            //应该可以直接返回错误信息而不跳转
            //这里有点问题
            model.addAttribute("task", task);
            return "/admin/task/release";
        }
        return "/admin/task/details";
    }

    @GetMapping("/task/{id}")
    public String showTaskDetails(Model model, @PathVariable long id) {
        Task task = taskService.findById(id);
        if (null!=task){
            setEmpPswInvisible(task);
            model.addAttribute("task", task);
        }
        return "/admin/task/details";
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
        Task savedTask=taskService.modify(task);
        if(savedTask!=null){
            setEmpPswInvisible(savedTask);
            model.addAttribute("task", savedTask);
            return "admin/task/details";
        }else{
            //来到修改不成功的页面
            return "";
        }
    }

    @PatchMapping("/task/{id}/setCompleted")
    @ResponseBody
    public Task setCompleted(@PathVariable(name = "id") long taskId) {
        Task task = taskService.setCompleted(taskId);
        if (task!=null){
            setEmpPswInvisible(task);
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
        for (Task task:tasks){
            setEmpPswInvisible(task);
        }
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

    private void setEmpPswInvisible(Task task){
        if (null!=task){
            for(Employee employee: task.getInCharge()){
                employee.setPassword("******");
            }
        }
    }

    /**
     *
     * @param model
     * @param state
     * @return
     */
    @GetMapping("/commits")
    public String showCommitsPage(Model model,String state){
        List<Commit> commits=null;
        if(state.equals("pending")){
            commits = commitService.findAllByState(0);
        }else if (state.equals("notPassed")){
            commits = commitService.findAllByState(1);
        }else if (state.equals("passed")){
            commits=commitService.findAllByState(2);
        }
        model.addAttribute("commits",commits);
        return "/admin/task/commit/commits";
    }

    /**
     *
     * @param commitId
     * @param reply
     * @return
     */
    @GetMapping("/commit/{commitId}/setPassed")
    @ResponseBody
    public Commit setCommitPassed(@PathVariable(name = "commitId") long commitId,String reply){
        Commit passedCommit = commitService.setPassed(commitId, reply);
        Task task = taskService.setCompleted(passedCommit.getTaskId(), passedCommit);
        return passedCommit;
    }

    @GetMapping("/commit/{commitId}/setNotPassed")
    @ResponseBody
    public Commit setCommitNotPassed(@PathVariable(name = "commitId") long commitId,String reply){
        LOGGER.info("{}:{}",commitId,reply);
        return null;
    }

    @GetMapping("/commit/{commitId}/download")
    public void downloadCommit(@PathVariable(name = "commitId") long commitId,
                               HttpServletResponse response){
        Commit commit = commitService.findById(commitId);
        commitService.downloadFile(response,commit);

    }
}
