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
@IdClass(RecordMultiKeys.class)
public class Record  {
    @Id
    private long employeeId;
    @Id
    private long taskId;
    private int scoreChange;
}
