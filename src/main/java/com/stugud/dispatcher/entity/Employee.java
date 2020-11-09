package com.stugud.dispatcher.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="t_employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Size(min = 2,max = 32,message = "用户名长度为2-32字符")
    private String username;
    @NotBlank
    @Size(min = 8,max = 64,message = "密码长度为8-64字符")
    private String password;
    @NotBlank
    @Email
    private String mail;
    @NotBlank(message = "需要部门信息")
    private String department;
    private int score;


    public Employee(@NotBlank @Size(min = 2, max = 32, message = "用户名长度为2-32字符") String username, @NotBlank @Size(min = 8, max = 64, message = "密码长度为8-64字符") String password, @NotBlank @Email String mail, @NotBlank(message = "需要部门信息") String department) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.department = department;
    }

    public static Employee register(Employee employee){
        return new Employee(employee.getUsername(),new BCryptPasswordEncoder().encode("123456"), employee.getMail(), employee.getDepartment());

    }


}
