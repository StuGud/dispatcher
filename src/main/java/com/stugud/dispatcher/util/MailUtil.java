package com.stugud.dispatcher.util;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class MailUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JavaMailSender mailSender;

    @Value("${dispatcher.mail.fromMail.addr}")
    private String from;

    @Autowired
    public MailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTxtMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
            logger.info("邮件已经发送。to" + to);
        } catch (Exception e) {
            logger.error("发送邮件时发生异常！to " + to, e);
        }

    }

    public void sendTaskRemindMail(String ps, Task task) {
        String subject = ps + task.getSubject();
        for (Employee employee : task.getInCharge()) {
            sendTxtMail(employee.getMail(), subject, getSimpleMailContent(task));
        }
    }

    public void sendTaskCompletedMail(String ps, Task task,int scoreChange) {
        String subject = ps + task.getSubject();
        String content =getSimpleMailContent(task);
        content+="完成时间： "+task.getFinishedAt();
        content+="得分： "+scoreChange;

        for (Employee employee : task.getInCharge()) {
            sendTxtMail(employee.getMail(), subject,content );
        }
    }

    public String getSimpleMailContent(Task task) {
        String mailContent = "任务编号： " + task.getId()
                + "\n任务主题： " + task.getSubject()
                + "\n任务级别： " + task.getLevel()
                + "\n任务详情： " + task.getContent();
        String inCharge = "\n任务负责人：";
        for (Employee employee : task.getInCharge()) {
            inCharge += "\n     " + employee.getUsername() + " " + employee.getDepartment() + " " + employee.getMail();
        }
        mailContent += inCharge;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(task.getDeadline().getTime());//这个就是把时间戳经过处理得到期望格式的时间
        mailContent += "\n截止时间： " + time;
        return mailContent;
    }

    public void sendTaskRemindMail(String ps, Task task, List<Employee> allLeaders) {
        String subject = ps + task.getSubject();
        for (Employee employee : allLeaders) {
            sendTxtMail(employee.getMail(), subject, getSimpleMailContent(task));
        }
    }
}
