package com.stugud.dispatcher.repository;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.repo.EmployeeRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeRepoTest {

    @Autowired
    EmployeeRepo employeeRepo;

    @Test
    void findEmployeesByTaskId(){
        List<Employee> employees = employeeRepo.findEmployeesByTaskId(1);
        System.out.println(employees);
    }

    //success
    @Test
    void findById(){
        Employee employee = employeeRepo.findById((long) 0).get();
        System.out.println(employee);
    }

}