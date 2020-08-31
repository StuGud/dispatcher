package com.stugud.dispatcher.repository;

import com.stugud.dispatcher.entity.Record;
import com.stugud.dispatcher.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task,Long> {
    List<Task> findAllByState(String state);
}
