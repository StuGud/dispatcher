package com.stugud.dispatcher.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecordMultiKeys implements Serializable {
    private long employeeId;
    private long taskId;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
