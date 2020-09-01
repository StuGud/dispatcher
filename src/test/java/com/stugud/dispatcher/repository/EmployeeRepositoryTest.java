package com.stugud.dispatcher.repository;

import com.stugud.dispatcher.entity.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    void findEmployeesByTaskId(){
        List<Employee> employees = employeeRepository.findEmployeesByTaskId(2);
        System.out.println(employees);
    }

    //success
    @Test
    void findById(){
        Employee employee = employeeRepository.findById((long) 0).get();
        System.out.println(employee);
    }

}