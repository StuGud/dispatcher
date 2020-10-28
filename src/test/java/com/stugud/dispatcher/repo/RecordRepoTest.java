package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.Record;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Gud
 * @version 1.0
 * @Description
 * @date 2020/10/29 12:31 上午
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RecordRepoTest {

    @Autowired
    RecordRepo recordRepo;

    @Test
    void findAllByEmployeeId() {
    }

    @Test
    void testFindAllByEmployeeId() {
    }

    @Transactional
    @Rollback(value = false)
    @Test
    void findAllByMonth() {
        List<Record> allByMonth = recordRepo.findAllByMonth(2020, 10);
        System.out.println(allByMonth);
    }
}