package com.stugud.dispatcher.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created By Gud on 2020/10/31 2:35 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="t_employee_permission")
public class SimplePermission {
    @Id
    private Long id;

    @ApiModelProperty(value = "权限值")
    private String value;
}
