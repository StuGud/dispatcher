package com.stugud.dispatcher.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "t_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Size(min = 5,message = "任务主题最少5字符")
    private String subject;

    @OneToMany(targetEntity = Employee.class,fetch=FetchType.EAGER)
    @Size(min = 1,message = "最少选择一个负责人")
    @JoinTable(name = "t_task_employees",joinColumns = {@JoinColumn(name="task",referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "employee",referencedColumnName = "id")})
    private List<Employee> inCharge;

    @NotBlank(message = "任务内容不为空")
    private String content;

    @Pattern(regexp = "A|B|C",message = "任务级别为A、B、C")
    private String level;

    @Column(name = "created")
    private Date createdAt;
    private Date deadline;

    //任务状态 "已完成"，"未完成"
    @NotBlank
    private String state;

    @PrePersist
    void createdAt(){
        this.createdAt=new Date();
    }
}
