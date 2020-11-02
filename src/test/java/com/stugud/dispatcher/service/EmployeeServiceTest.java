package com.stugud.dispatcher.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {
    @Value("${dispatcher.commit.root}")
    private String root;

    @Test
    void findById() {
        System.out.println(root);
    }

    @Test
    void register() {
    }

    @Test
    void findAllByTaskId() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllByPageNum() {
    }
}