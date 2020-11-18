package com.stugud.dispatcher.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "t_task_employee")
@IdClass(RecordMultiKeys.class)
public class Record  {
    @Id
    private long employeeId;
    @Id
    private long taskId;

    /**
     * 0 -> 未通过 1-> 已通过
     */
//    private int state;

    private int scoreChange;

    private Date finishedAt;

    public Record(long employeeId, long taskId, int scoreChange, Date finishedAt) {
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.scoreChange = scoreChange;
        this.finishedAt = finishedAt;
    }

    /*
    public Record(long employeeId, long taskId, int state, int scoreChange, Date finishedAt) {
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.state = state;
        this.scoreChange = scoreChange;
        this.finishedAt = finishedAt;
    }
    */
}
