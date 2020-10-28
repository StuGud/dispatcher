package com.stugud.dispatcher.repository;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repo.TaskRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TaskRepoTest {

    @Autowired
    TaskRepo taskRepo;

    @Test
    @Transactional
    void findAllByState() {
        List<Task> tasks = taskRepo.findAllByState("未完成");
        System.out.println(tasks);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void saveTask() {
        ArrayList<Employee> list = new ArrayList<>();
//        list.add(new Employee(10,"ecaca","dafasaagdsg","maildfasfda","cdasvsav",12));
        list.add(new Employee(1));
        list.add(new Employee("employee2"));
        Task task=new Task("cedzss","content1","A",new Date(),new Date(),"未完成",list);
        System.out.println(task);
        //taskRepo.save(task);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void deleteTask() {
       taskRepo.deleteById((long) 4);
        //taskRepo.save(task);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void findAllByMonth(){
        List<Task> allByMonth = taskRepo.findAllByMonth(2020, 10);
        System.out.println(allByMonth);
    }


}