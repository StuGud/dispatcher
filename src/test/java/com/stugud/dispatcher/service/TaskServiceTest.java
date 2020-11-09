package com.stugud.dispatcher.service;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TaskServiceTest {
    @Autowired
    TaskService taskService;

    @Transactional
    @Test
    @Rollback(value = false)
    void testReleaseWithInChargesName() {
        ArrayList<Employee> list = new ArrayList<>();
//        list.add(new Employee(1));
//        list.add(new Employee("employee2"));
        Task task=new Task("cedzss","content1","A",new Date(),new Date(),0,list);
        System.out.println(task);
        taskService.releaseWithInChargesName(task);
    }

    @Transactional
    @Test
    @Rollback(value = false)
    void testSetCompleted() {
        taskService.setCompleted(1);
    }
}