package com.stugud.dispatcher.repository;

import com.stugud.dispatcher.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee,Long> {

}
