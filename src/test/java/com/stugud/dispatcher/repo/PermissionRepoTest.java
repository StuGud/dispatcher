package com.stugud.dispatcher.repo;

import com.stugud.dispatcher.entity.SimplePermission;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * Created By Gud on 2020/10/31 2:53 下午
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PermissionRepoTest {
    @Autowired
    PermissionRepo permissionRepo;

    @Test
    void findAllByEmployeeMail(){
        List<SimplePermission> permissions = permissionRepo.findAllByEmployeeMail("741498908@qq.com");
        System.out.println(permissions);
    }

    @Test
    void findAllByEmployeeId(){
        List<SimplePermission> permissions = permissionRepo.findAllByEmployeeId(1L);
        System.out.println(permissions);
    }
}
