package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RecordRepo extends PagingAndSortingRepository<Record,Long> {
    //findAllByEmpId
    List<Record> findAllByEmployeeId(long employeeId);
    List<Record> findAllByEmployeeId(long employeeId, Pageable pageable);
}
