package com.stugud.dispatcher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_task_employees")
@IdClass(RecordMultiKeys.class)
public class Record  {
    @Id
    @Column(name = "employee")
    private long employeeId;
    @Id
    @Column(name = "task")
    private long taskId;
    private int scoreChange;
}
