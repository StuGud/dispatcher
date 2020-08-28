package com.stugud.dispatcher.entity;

import java.util.Date;
import java.util.List;

public class task {
    private long id;
    private String subject;
    private List<employee> inCharge;
    private String Content;
    private String level;
    private Date createdAt;
    private Date deadline;
    //任务状态 "已完成"，"未完成"
    private String state;
}
