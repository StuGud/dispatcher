package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordRepo extends PagingAndSortingRepository<Record,Long> {

    List<Record> findAllByEmployeeId(long employeeId);

    List<Record> findAllByEmployeeId(long employeeId, Pageable pageable);

    @Query(value = "select * from t_task_employees where year(finishedAt)= :year and month(finishedAt)= :month",nativeQuery = true)
    List<Record> findAllByMonth(@Param("year") int year, @Param("month") int month);
}
