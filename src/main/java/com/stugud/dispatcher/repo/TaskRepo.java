package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskRepo extends PagingAndSortingRepository<Task,Long> {
    List<Task> findAllByState(String state);
    List<Task> findAllByState(String state,Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update t_task set state=? where id=?",nativeQuery = true)
    Task updateStateById(String state,long id);

    /**
     *
     * @param year
     * @param month
     * @return
     */
    @Query(value = "select * from t_task where year(finishedAt)= :year and month(finishedAt)= :month",nativeQuery = true)
    List<Task> findAllByMonth(@Param("year") int year,@Param("month") int month);
}
