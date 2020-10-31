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
    private int scoreChange;

    private Date finishedAt;

    public Record( long taskId,long employeeId, int scoreChange) {
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.scoreChange = scoreChange;
    }
}
