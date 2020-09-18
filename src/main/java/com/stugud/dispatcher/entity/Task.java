package com.stugud.dispatcher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Size(min = 4,max = 32,message = "任务主题4-32字符")
    private String subject;

    @NotBlank(message = "任务内容不为空")
    private String content;

    @Pattern(regexp = "A|B|C",message = "任务级别为A、B、C")
    private String level;

    @Column(name = "createdAt")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    //任务状态 "已完成"，"未完成"
    private String state;

    private Date finishedAt;

    private String filePath;

    @PrePersist
    void createdAt(){
        this.createdAt=new Date();
    }

    @OneToMany(targetEntity = Employee.class,fetch=FetchType.EAGER)
    @Size(min = 1,message = "最少选择一个负责人")
    @JoinTable(name = "t_task_employees",joinColumns = {@JoinColumn(name="taskId",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "employeeId",referencedColumnName = "id")})
    private List<Employee> inCharge;

    public Task(@NotBlank @Size(min = 4, max = 32, message = "任务主题4-32字符") String subject, @NotBlank(message = "任务内容不为空") String content, @Pattern(regexp = "A|B|C", message = "任务级别为A、B、C") String level, Date createdAt, Date deadline, String state, @Size(min = 1, message = "最少选择一个负责人") List<Employee> inCharge) {
        this.subject = subject;
        this.content = content;
        this.level = level;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.state = state;
        this.inCharge = inCharge;
    }
}
