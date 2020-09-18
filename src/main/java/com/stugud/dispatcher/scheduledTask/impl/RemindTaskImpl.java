package com.stugud.dispatcher.scheduledTask.impl;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repo.EmployeeRepo;
import com.stugud.dispatcher.repo.RecordRepo;
import com.stugud.dispatcher.repo.TaskRepo;
import com.stugud.dispatcher.scheduledTask.RemindTask;
import com.stugud.dispatcher.service.MailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component("ScheduledTask")
public class RemindTaskImpl implements RemindTask {

    final MailService mailService;
    final TaskRepo taskRepo;
    final RecordRepo recordRepo;
    final EmployeeRepo employeeRepo;

    public RemindTaskImpl(MailService mailService, TaskRepo taskRepo,
                          RecordRepo recordRepo, EmployeeRepo employeeRepo) {
        this.mailService = mailService;
        this.taskRepo = taskRepo;
        this.recordRepo = recordRepo;
        this.employeeRepo = employeeRepo;
    }

    @Scheduled(fixedRate = 24*60*60*1000)
    public void remindByEmailTask(){
        System.out.println("remindTask");

        //选取未完成的任务
        List<Task> tasks = taskRepo.findAllByState("未完成");

        //如果deadline-created满足条件，获取对应负责人，发送邮件提醒
        for (Task task:tasks) {
            long createdAt=task.getCreatedAt().getTime();
            long deadline=task.getDeadline().getTime();
            long interval= (long) (0.2*(deadline-createdAt));

            Date now=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, 1);//明天
            Date tomorrow=calendar.getTime();

            for (int i = 1; i < 4; i++) {
                Date remindAt=new Date(deadline-i*interval);
                if (now.before(remindAt)&&remindAt.before(tomorrow)){
                    sendRemindMail(task);
                    break;
                }
            }

        }

    }

    private String getEmailContent(Task task){
        String content="邮件提醒\n";
        content+="taskId:"+task.getId()+"\n";
        content+="taskSubject:"+task.getSubject()+"\n";
        content+="taskContent:"+task.getContent()+"\n";
        content+="taskDeadline"+task.getDeadline()+"\n";
        return content;
    }

    private void sendRemindMail(Task task){
        //查找该任务的负责人
        String content=getEmailContent(task);
        String subject="任务提醒： "+task.getSubject();
        List<Employee> employees = employeeRepo.findEmployeesByTaskId(task.getId());
        for (Employee employee :
                employees) {
            mailService.sendTxtMail(employee.getMail(),subject,content);
        }
    }
}
