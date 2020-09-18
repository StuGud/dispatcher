package com.stugud.dispatcher.repository;

import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repo.TaskRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
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
    void saveTask() {
        Task task=new Task("ces","content1","A",new Date(),new Date(),"未完成",null);
        System.out.println(task);
        //taskRepo.save(task);
    }



}