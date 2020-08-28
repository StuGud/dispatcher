package com.stugud.dispatcher.repository;

import com.stugud.dispatcher.entity.Record;
import org.springframework.data.repository.CrudRepository;

public interface RecordRepository extends CrudRepository<Record,Long> {
}
