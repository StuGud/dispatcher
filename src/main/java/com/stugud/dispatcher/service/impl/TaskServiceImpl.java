package com.stugud.dispatcher.service.impl;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Record;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repo.EmployeeRepo;
import com.stugud.dispatcher.repo.RecordRepo;
import com.stugud.dispatcher.repo.TaskRepo;
import com.stugud.dispatcher.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    final
    TaskRepo taskRepo;
    final
    RecordRepo recordRepo;
    final
    EmployeeRepo employeeRepo;

    @Value("${dispatcher.employee.pageSize}")
    int pageSize;

    public TaskServiceImpl(TaskRepo taskRepo, RecordRepo recordRepo, EmployeeRepo employeeRepo) {
        this.taskRepo = taskRepo;
        this.recordRepo = recordRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Task findById(long id) {
        Optional<Task> optionalTask = taskRepo.findById(id);
        if (optionalTask.isPresent()){
            return optionalTask.get();
        }
        return null;
    }

    @Override
    public List<Task> findAllByEmpId(long empId) {
        List<Record> records = recordRepo.findAllByEmployeeId(empId);
        List<Task> tasks = new ArrayList<>();
        for (Record record : records) {
            tasks.add(taskRepo.findById(record.getTaskId()).get());
        }
        return tasks;
    }

    @Override
    public Task release(Task task) {
        for (Employee employee : task.getInCharge()) {
            recordRepo.save(new Record(task.getId(), employee.getId(), 0));
        }
        return taskRepo.save(task);
    }

    @Override
    public Task modify(Task modifiedTask) {
        Optional<Task> optionalTask = taskRepo.findById(modifiedTask.getId());
        if(optionalTask.isPresent()) {
            Task task=optionalTask.get();
            if (modifiedTask.getSubject() != null) {
                task.setSubject(modifiedTask.getSubject());
            }
            if (modifiedTask.getContent() != null) {
                task.setContent(modifiedTask.getContent());
            }
            if (modifiedTask.getDeadline() != null) {
                task.setDeadline(modifiedTask.getDeadline());
            }
            if (modifiedTask.getInCharge() != null) {
                task.setInCharge(modifiedTask.getInCharge());
            }
            if (modifiedTask.getLevel() != null) {
                task.setLevel(modifiedTask.getLevel());
            }
            if (modifiedTask.getState() != null) {
                task.setState(modifiedTask.getState());
            }
            taskRepo.save(task);
            return taskRepo.save(task);
        }
        return null;
    }

    @Override
    public Task setCompleted(Task task) {
        Optional<Task> optionalTask = taskRepo.findById(task.getId());
        if(optionalTask.isPresent()) {
            Task task1 = optionalTask.get();
            task1.setState("已完成");
            task1.setFinishedAt(new Date());
            taskRepo.save(task1);

            //计算scoreChange
            long deadline = task.getDeadline().getTime();
            long nowDate = (new Date()).getTime();
            long delay = (nowDate - deadline) / (24 * 60 * 60 * 1000);
            int scoreChange = (int) (-5 * delay);
            for (Employee employee : task1.getInCharge()) {
                recordRepo.save(new Record(task1.getId(), employee.getId(), scoreChange));
            }

            return task1;
        }
        return null;
    }

    @Override
    public List<Task> findAll() {
        return (List<Task>) taskRepo.findAll();
    }

    @Override
    public List<Task> findAllNotCompleted() {
        return taskRepo.findAllByState("未完成");
    }

    @Override
    public List<Task> findAllNotCompleted(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<Task> tasks = taskRepo.findAllByState("未完成", pageable);
        return tasks;
    }

    @Override
    public List<Task> findAll(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<Task> tasks = (List<Task>) taskRepo.findAll(pageable);
        return tasks;
    }

    @Override
    public Task releaseByInChargeUsername(Task task) {
        List<Employee> inChargeList=new ArrayList<>();
        if (task.getInCharge()!=null){
            for (Employee inChargeP: task.getInCharge()){
                List<Employee> employeesWithSameName = employeeRepo.findAllByUsername(inChargeP.getUsername());
                for(Employee employee:employeesWithSameName){
                    inChargeList.add(employee);
                }
            }
        }
        if(inChargeList.isEmpty()){
            return null;
        }else {
            task.setInCharge(inChargeList);
            Task savedTask=taskRepo.save(task);
//            System.out.println(task);
            System.out.println(savedTask);
            for (Employee inChargeP: task.getInCharge()){
                inChargeP.setPassword("********");
            }
            return task;
        }
    }
}
