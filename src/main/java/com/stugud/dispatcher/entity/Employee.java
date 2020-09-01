package com.stugud.dispatcher.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

@Data
@RequiredArgsConstructor
@Entity
@Table(name ="t_employee")
public class Employee {

    @Id
    private long id;
    @NotBlank
    @Size(min = 4,max = 12,message = "用户名长度为4-12字符")
    private String username;
    @NotBlank
    @Size(min = 8,max = 20,message = "密码长度为8-20字符")
    private String password;
    @NotBlank
    @Email
    private String mail;
    @NotBlank(message = "需要部门信息")
    private String department;
    private int score;

}
