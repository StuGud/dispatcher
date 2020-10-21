package com.stugud.dispatcher.scheduledTask.impl;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repo.EmployeeRepo;
import com.stugud.dispatcher.repo.RecordRepo;
import com.stugud.dispatcher.repo.TaskRepo;
import com.stugud.dispatcher.scheduledTask.RemindTask;
import com.stugud.dispatcher.util.MailUtils;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component("ScheduledTask")
public class RemindTaskImpl implements RemindTask {

    final MailUtils mailUtils;
    final TaskRepo taskRepo;
    final RecordRepo recordRepo;
    final EmployeeRepo employeeRepo;

    public RemindTaskImpl(MailUtils mailUtils, TaskRepo taskRepo,
                          RecordRepo recordRepo, EmployeeRepo employeeRepo) {
        this.mailUtils = mailUtils;
        this.taskRepo = taskRepo;
        this.recordRepo = recordRepo;
        this.employeeRepo = employeeRepo;
    }

    // @Scheduled(fixedRate = 24*60*60*1000)
    public void remindByEmailTask() {
        System.out.println("remindTask");

        //选取未完成的任务
        List<Task> tasks = taskRepo.findAllByState("未完成");

        //如果deadline-created满足条件，获取对应负责人，发送邮件提醒
        for (Task task : tasks) {
            long createdAt = task.getCreatedAt().getTime();
            long deadline = task.getDeadline().getTime();
            long interval = (long) (0.2 * (deadline - createdAt));

            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, 1);//明天
            Date tomorrow = calendar.getTime();

            for (int i = 1; i < 4; i++) {
                Date remindAt = new Date(deadline - i * interval);
                if (now.before(remindAt) && remindAt.before(tomorrow)) {
                    mailUtils.sendTaskRemindMail("任务提醒！ ", task);
                    break;
                }
            }
        }
    }
}
