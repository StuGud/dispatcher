package com.stugud.dispatcher.util;

import com.stugud.dispatcher.entity.Task;

public interface MailUtils {
     void sendTxtMail(String to,String subject ,String content);
     void sendTaskRemindMail(String ps, Task task);
     String getSimpleMailContent(Task task);
}
