package com.stugud.dispatcher.repository;

import com.stugud.dispatcher.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task,Long> {
}
