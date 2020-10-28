package com.stugud.dispatcher.service.impl;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Record;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repo.EmployeeRepo;
import com.stugud.dispatcher.repo.RecordRepo;
import com.stugud.dispatcher.repo.TaskRepo;
import com.stugud.dispatcher.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    final
    EmployeeRepo employeeRepo;

    final
    RecordRepo recordRepo;

    @Value("${dispatcher.employee.pageSize}")
    int pageSize;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo, RecordRepo recordRepo) {
        this.employeeRepo = employeeRepo;
        this.recordRepo = recordRepo;
    }

    @Override
    public Map<Employee, Integer> countScoresByMonth(int year,int month) {
        List<Record> records = recordRepo.findAllByMonth(year, month);
        Map<Long,Integer> map=new HashMap<>();
        for (Record record:records){
            if(map.containsKey(record.getEmployeeId())){
                map.put(record.getEmployeeId(),map.get(record.getEmployeeId())+record.getScoreChange());
            }else{
                map.put(record.getEmployeeId(),record.getScoreChange());
            }
        }
        Map<Employee,Integer> employeeIntegerMap=new HashMap<>();
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            Optional<Employee> optionalEmployee = employeeRepo.findById(entry.getKey());
            if(optionalEmployee.isPresent())
            employeeIntegerMap.put(optionalEmployee.get(), entry.getValue());
        }
        return employeeIntegerMap;
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
