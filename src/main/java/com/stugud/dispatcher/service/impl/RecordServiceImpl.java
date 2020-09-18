package com.stugud.dispatcher.service.impl;

import com.stugud.dispatcher.entity.Record;
import com.stugud.dispatcher.repo.RecordRepo;
import com.stugud.dispatcher.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    final
    RecordRepo recordRepo;

    public RecordServiceImpl(RecordRepo recordRepo) {
        this.recordRepo = recordRepo;
    }

    @Override
    public List<Record> findAllByEmpId(long empId) {
        return recordRepo.findAllByEmployeeId(empId);
    }
}
