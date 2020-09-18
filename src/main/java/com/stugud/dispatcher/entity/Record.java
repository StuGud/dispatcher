package com.stugud.dispatcher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_task_employees")
@IdClass(RecordMultiKeys.class)
public class Record  {
    @Id
    @Column(name = "employeeId")
    private long employeeId;
    @Id
    @Column(name = "taskId")
    private long taskId;
    private int scoreChange;
}
