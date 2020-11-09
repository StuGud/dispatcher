package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeRepoTest {

    @Autowired
    EmployeeRepo employeeRepo;

    @Test
    void findEmployeesByTaskId(){
        List<Employee> employees = employeeRepo.findAllByTaskId(1);
        System.out.println(employees);
    }

    @Test
    void findById(){
        for (int i = 0; i < 100; i++) {
            String encode = new BCryptPasswordEncoder().encode("123456");
            System.out.println(encode.length());
        }

    }

    @Test
    void modifyByEmp(){
        List<Employee> employees = employeeRepo.findLeadersById(180);
        System.out.println(employees);
    }

}