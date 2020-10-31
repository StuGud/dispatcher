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

@Component
public class MailUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JavaMailSender mailSender;

    @Value("${mail.fromMail.addr}")
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
            logger.info("邮件已经发送。to"+to);
        } catch (Exception e) {
            logger.error("发送邮件时发生异常！to "+to, e);
        }

    }

    public void sendTaskRemindMail(String ps, Task task){
        for(Employee employee:task.getInCharge()){
            String subject=ps+task.getSubject();
            sendTxtMail(employee.getMail(),subject,getSimpleMailContent(task));
        }
    }

    public String getSimpleMailContent(Task task) {
        String mailContent = "任务编号： " + task.getId()
                + "\n任务主题： " + task.getSubject()
                + "\n任务级别： " + task.getLevel()
                + "\n任务详情： " + task.getInCharge();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(task.getDeadline().getTime());//这个就是把时间戳经过处理得到期望格式的时间
        mailContent += "\n截止时间： " + time;
        return mailContent;
    }
}
