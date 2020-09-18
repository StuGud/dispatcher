package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TaskRepo extends PagingAndSortingRepository<Task,Long> {
    List<Task> findAllByState(String state);
    List<Task> findAllByState(String state,Pageable pageable);
}
