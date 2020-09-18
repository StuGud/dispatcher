package com.stugud.dispatcher.service.impl;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.repo.EmployeeRepo;
import com.stugud.dispatcher.service.EmployeeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    final
    EmployeeRepo employeeRepo;

    @Value("${dispatcher.employee.pageSize}")
    int pageSize;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Employee findById(long id) {
        Optional<Employee> optionalEmployee = employeeRepo.findById(id);
        if(optionalEmployee.isPresent()){
            return optionalEmployee.get();
        }
        return null;
    }

    @Override
    public List<Employee> findAll() {
        return (List<Employee>) employeeRepo.findAll();
    }

    @Override
    public Employee register(Employee employee) {
        return employeeRepo.save(employee);
    }

    @Override
    public List<Employee> findAllByTaskId(long taskId) {
        return employeeRepo.findEmployeesByTaskId(taskId);
    }

    @Override
    public List<Employee> findAllByPageNum(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<Employee> employees = (List<Employee>) employeeRepo.findAll(pageable);
        return employees;
    }
}
