package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.Commit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Created By Gud on 2020/11/2 1:21 下午
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommitRepoTest {

    @Autowired
    CommitRepo commitRepo;

    @Test
    void findAllByTaskId() {
    }

    @Test
    void findAllByEmployeeId() {
    }

    @Test
    void findAllByState() {
    }

    @Test
    void findMaxCommitNoByTaskId() {
        Commit maxCommitNoByTaskId = commitRepo.findMaxCommitNoByTaskId(11);
        System.out.println("/Users/gud/Desktop/task11/commit2/计网总结.pages".length());
        System.out.println(maxCommitNoByTaskId);
    }
}