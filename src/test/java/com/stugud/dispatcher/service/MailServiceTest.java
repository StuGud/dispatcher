package com.stugud.dispatcher.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService MailService;

    @Test
    void sendTxtMail() {
        MailService.sendTxtMail("741498908@qq.com","test simple mail"," hello this is simple mail");
    }
}