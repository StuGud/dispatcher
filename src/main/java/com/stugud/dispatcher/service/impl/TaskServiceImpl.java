package com.stugud.dispatcher.service.impl;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Record;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repository.RecordRepository;
import com.stugud.dispatcher.repository.TaskRepository;
import com.stugud.dispatcher.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    final
    TaskRepository taskRepository;
    final
    RecordRepository recordRepository;

    public TaskServiceImpl(TaskRepository taskRepository, RecordRepository recordRepository) {
        this.taskRepository = taskRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    public Task findById(long id) {
        return taskRepository.findById(id).get();
    }

    @Override
    public List<Task> findByEmpId(long empId) {
        List<Record> records = recordRepository.findAllByEmployeeId(empId);
        List<Task> tasks=new ArrayList<>();
        for (Record record:records) {
            tasks.add(taskRepository.findById(record.getTaskId()).get());
        }
        return tasks;
    }

    @Override
    public Task release(Task task) {
        for (Employee employee:task.getInCharge()) {
            recordRepository.save(new Record(task.getId(),employee.getId(),0));
        }
        return taskRepository.save(task);
    }

    @Override
    public Task modify(Task modifiedTask) {
        Task task=taskRepository.findById(modifiedTask.getId()).get();
        if (modifiedTask.getSubject()!=null){
            task.setSubject(modifiedTask.getSubject());
        }
        if(modifiedTask.getContent()!=null){
            task.setContent(modifiedTask.getContent());
        }
        if(modifiedTask.getDeadline()!=null){
            task.setDeadline(modifiedTask.getDeadline());
        }
        if (modifiedTask.getInCharge()!=null){
            task.setInCharge(modifiedTask.getInCharge());
        }
        if (modifiedTask.getLevel()!=null){
            task.setLevel(modifiedTask.getLevel());
        }
        if(modifiedTask.getState()!=null){
            task.setState(modifiedTask.getState());
        }
        taskRepository.save(task);
        return taskRepository.save(task);
    }

    @Override
    public Task setCompleted(Task task) {
        Task task1 = taskRepository.findById(task.getId()).get();
        task1.setState("已完成");
        taskRepository.save(task1);
        long deadline=task.getDeadline().getTime();
        long nowDate=(new Date()).getTime();
        long delay=(nowDate-deadline)/(24*60*60*1000);
        //计算scoreChange
        int scoreChange= (int) (-5*delay);
        for (Employee employee:task1.getInCharge()) {
            recordRepository.save(new Record(task1.getId(), employee.getId(), scoreChange));
        }
        return task1;
    }

    @Override
    public List<Task> getList() {
        return (List<Task>) taskRepository.findAll();
    }

    @Override
    public List<Task> getNotCompletedList() {
        return null;
    }
}
