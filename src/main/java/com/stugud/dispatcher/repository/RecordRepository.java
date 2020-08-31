package com.stugud.dispatcher.repository;

import com.stugud.dispatcher.entity.Record;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecordRepository extends CrudRepository<Record,Long> {
    //findAllByEmpId
    List<Record> findAllByEmployeeId(long id);
}
