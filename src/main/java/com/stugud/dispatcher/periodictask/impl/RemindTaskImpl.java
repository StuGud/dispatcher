package com.stugud.dispatcher.periodictask.impl;

import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repo.EmployeeRepo;
import com.stugud.dispatcher.repo.RecordRepo;
import com.stugud.dispatcher.repo.TaskRepo;
import com.stugud.dispatcher.periodictask.RemindTask;
import com.stugud.dispatcher.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 任务提醒
 */
@Slf4j
@Component("ScheduledTask")
public class RemindTaskImpl implements RemindTask {

    final MailUtil mailUtil;
    final TaskRepo taskRepo;
    final RecordRepo recordRepo;
    final EmployeeRepo employeeRepo;

    public RemindTaskImpl(MailUtil mailUtil, TaskRepo taskRepo,
                          RecordRepo recordRepo, EmployeeRepo employeeRepo) {
        this.mailUtil = mailUtil;
        this.taskRepo = taskRepo;
        this.recordRepo = recordRepo;
        this.employeeRepo = employeeRepo;
    }

    /**
     * 每天下午三点，如果符合下列要求，邮件提醒
     * 要求：1、未完成；
     * 2、如果在截止日期内，则按照三等分；如果超过了截止日期，那么3天提醒一次
     */
    @Scheduled(cron = "0 0 15 * * ?")
    public void remindByEmailTask() {

        log.info("开始进行邮件提醒");

        List<Task> tasks = taskRepo.findAllByState(0);

        for (Task task : tasks) {
            long createdAt = task.getCreatedAt().getTime();
            long deadline = task.getDeadline().getTime();

            //明天
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date tomorrow = calendar.getTime();

            if (now.getTime() <= deadline) {
                long interval = (long) (0.25 * (deadline - createdAt));
                for (int i = 1; i < 4; i++) {
                    Date remindAt = new Date(deadline - i * interval);
                    //remindAt在now和tomorrow之间
                    if (now.before(remindAt) && remindAt.before(tomorrow)) {
                        mailUtil.sendTaskRemindMail("任务提醒！ ", task);
                        break;
                    }
                }
            }else {
                int days = (int) ((now.getTime() - deadline) / (1000*60*60*24));
                if (days%3==0){
                    mailUtil.sendTaskRemindMail("任务逾期提醒!已逾期"+days+"天！ ", task);
                }
            }

        }
    }
}
