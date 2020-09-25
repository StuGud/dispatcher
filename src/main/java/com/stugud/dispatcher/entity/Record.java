package com.stugud.dispatcher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "t_task_employees")
@IdClass(RecordMultiKeys.class)
public class Record  {
    @Id
    private long employeeId;
    @Id
    private long taskId;
    private int scoreChange;

    public Record( long taskId,long employeeId, int scoreChange) {
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.scoreChange = scoreChange;
    }
}
