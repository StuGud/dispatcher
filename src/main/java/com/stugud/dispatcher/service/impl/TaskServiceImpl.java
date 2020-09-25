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
            if (modifiedTask.getLevel() != null) {
                task.setLevel(modifiedTask.getLevel());
            }
            if (modifiedTask.getDeadline() != null) {
                task.setDeadline(modifiedTask.getDeadline());
            }
            if (modifiedTask.getInCharge() != null) {
                task.setInCharge(findAllEmployeesByNameOrId(modifiedTask.getInCharge()));
            }
            if (modifiedTask.getState() != null) {
                if(task.getState().equals("未完成")&&modifiedTask.getState().equals("已完成")){
                    Task completed = setCompleted(task.getId());
                    task.setState("已完成");
                    task.setFinishedAt(completed.getFinishedAt());
                }else{
                    task.setState(modifiedTask.getState());
                }
            }
            return taskRepo.save(task);
        }
        return null;
    }


    @Override
    public Task setCompleted(long taskId) {
        Optional<Task> optionalTask = taskRepo.findById(taskId);
        if(optionalTask.isPresent()) {
            Task task1 = optionalTask.get();
            task1.setState("已完成");
            task1.setFinishedAt(new Date());
            taskRepo.save(task1);

            //计算scoreChange
            long deadline = task1.getDeadline().getTime();
            long nowDate = (new Date()).getTime();
            long delay = (nowDate - deadline) / (24 * 60 * 60 * 1000);
            int scoreChange = (int) (-5 * delay);
            for (Employee employee : task1.getInCharge()) {
                recordRepo.save(new Record(task1.getId(), employee.getId(), scoreChange));
                employee.setScore(employee.getScore()+scoreChange);
                employeeRepo.save(employee);
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
    public List<Task> findAllByPageNum(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<Task> tasks = (List<Task>) taskRepo.findAll(pageable);
        return tasks;
    }

    @Override
    public Task releaseWithInChargesName(Task task) {
        List<Employee> inChargeList=new ArrayList<>();
        if (task.getInCharge()!=null){
            inChargeList=findAllEmployeesByNameOrId(task.getInCharge());
        }
        if(inChargeList.isEmpty()){
            return null;
        }else {
            task.setInCharge(inChargeList);
            return taskRepo.save(task);
        }
    }

    /**
     *
     * @param employees 为null或为空时，返回空的list；
     * @return
     */
    private List<Employee> findAllEmployeesByNameOrId(List<Employee> employees){
        List<Employee> inChargeList=new ArrayList<>();
        for (Employee inChargeP: employees){
            if(inChargeP.getId()!=0){
                Optional<Employee> optionalEmployee = employeeRepo.findById(inChargeP.getId());
                if(optionalEmployee.isPresent()){
                    inChargeList.add(optionalEmployee.get());
                }
            }else if(inChargeP.getUsername()!=null){
                List<Employee> employeesWithSameName = employeeRepo.findAllByUsername(inChargeP.getUsername());
                for(Employee employee:employeesWithSameName){
                    inChargeList.add(employee);
                }
            }
        }
        return inChargeList;
    }

}
