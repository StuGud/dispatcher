package com.stugud.dispatcher.util;

import com.stugud.dispatcher.entity.Commit;
import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@Component
public class MailUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JavaMailSender mailSender;

    @Value("${dispatcher.mail.fromMail.addr}")
    private String from;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public MailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 只有在新建任务并且有文件时才会用到
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    private void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message=mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            FileSystemResource file=new FileSystemResource(new File(filePath));
            String fileName=filePath.substring(filePath.lastIndexOf(File.separator));
            //添加多个附件可以使用多条
            //helper.addAttachment(fileName,file);
            helper.addAttachment(fileName,file);
            mailSender.send(message);
            System.out.println("带附件的邮件发送成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("发送带附件的邮件失败");
        }
    }

    private void sendTxtMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            logger.info("邮件已经发送。to " + to);
        } catch (Exception e) {
            logger.error("发送邮件时发生异常！to " + to, e);
        }
    }

    public void sendTaskCreatedMail(String ps,Task task){
        String filePath = task.getFilePath();
        String subject = ps + task.getSubject();
        if(filePath==null||filePath.length()==0){
            for (Employee employee : task.getInCharge()) {
                sendTxtMail(employee.getMail(), subject, getSimpleMailContent(task));
            }
        }else{
            for (Employee employee : task.getInCharge()) {
                sendAttachmentsMail(employee.getMail(), subject, getSimpleMailContent(task),filePath);
            }
        }
    }

    public void sendTaskRemindMail(String ps, Task task, Set<Employee> allLeaders) {
        String filePath = task.getFilePath();
        String subject = ps + task.getSubject();
        if(filePath==null||filePath.length()==0){
            for (Employee employee : allLeaders) {
                sendTxtMail(employee.getMail(), subject, getSimpleMailContent(task));
            }
        }else{
            for (Employee employee : allLeaders) {
                sendAttachmentsMail(employee.getMail(), subject, getSimpleMailContent(task),filePath);
            }
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
        String finishedAt = simpleDateFormat.format(task.getFinishedAt().getTime());
        content+="\n完成时间： "+finishedAt;
        content+="\n得分： "+scoreChange;

        for (Employee employee : task.getInCharge()) {
            sendTxtMail(employee.getMail(), subject,content );
        }
    }

    public void sendCommitNotPassedMail(String ps, Task task, Commit commit){
        String subject = ps + task.getSubject();
        String deadline=simpleDateFormat.format(task.getDeadline());
        String mailContent=
                "您对ID为:"+commit.getTaskId()+"的任务的第"+commit.getCommitNo()+"次提交被管理员审核为未通过。"
                +"\n管理员回复： "+commit.getReply()
                +"\n任务详情： "+task.getContent()
                +"\n任务截止时间： " +deadline;
        for (Employee employee : task.getInCharge()) {
            sendTxtMail(employee.getMail(), subject,mailContent );
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

        String time = simpleDateFormat.format(task.getDeadline());
        mailContent += "\n截止时间： " + time;
        return mailContent;
    }


}
