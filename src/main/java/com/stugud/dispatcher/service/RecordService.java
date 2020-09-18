package com.stugud.dispatcher.service;

import com.stugud.dispatcher.entity.Record;

import java.util.List;

public interface RecordService {
    List<Record> findAllByEmpId(long empId);
}
