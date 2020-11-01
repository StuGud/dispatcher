package com.stugud.dispatcher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 任务提交
 * Created By Gud on 2020/11/1 1:34 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="t_commit")
public class Commit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long taskId;
    private long employeeId;
    private int commitNo;
    private Date commitAt;
    private String message;
    private String reply;
    /**
     * 0 -> pending待审核 ; 1 ->  没通过; 2 -> 通过
     */
    private int state;
    private String filePath;

    @PrePersist
    void commitAt() {
        this.commitAt = new Date();
    }

    public Commit(long taskId, long employeeId, String message) {
        this.taskId = taskId;
        this.employeeId = employeeId;
        this.message = message;
    }
}
