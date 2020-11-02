package com.stugud.dispatcher.service.impl;

import com.stugud.dispatcher.controller.EmployeeController;
import com.stugud.dispatcher.entity.Commit;
import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Record;
import com.stugud.dispatcher.entity.Task;
import com.stugud.dispatcher.repo.EmployeeRepo;
import com.stugud.dispatcher.repo.RecordRepo;
import com.stugud.dispatcher.repo.TaskRepo;
import com.stugud.dispatcher.service.TaskService;
import com.stugud.dispatcher.util.FileUtil;
import com.stugud.dispatcher.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger LOGGER= LoggerFactory.getLogger(EmployeeController.class);

    final
    TaskRepo taskRepo;
    final
    RecordRepo recordRepo;
    final
    EmployeeRepo employeeRepo;
    final
    MailUtil mailUtil;

    @Autowired
    FileUtil fileUtil;

    @Value("${dispatcher.employee.pageSize}")
    int pageSize;

    public TaskServiceImpl(TaskRepo taskRepo, RecordRepo recordRepo, EmployeeRepo employeeRepo, MailUtil mailUtil) {
        this.taskRepo = taskRepo;
        this.recordRepo = recordRepo;
        this.employeeRepo = employeeRepo;
        this.mailUtil = mailUtil;
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
            mailUtil.sendTaskRemindMail("任务修改！ ",task);
            return taskRepo.save(task);
        }
        return null;
    }


    @Override
    @Transactional
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
                System.out.println(task1);
                recordRepo.save(new Record(task1.getId(), employee.getId(), scoreChange,task1.getFinishedAt()));
                employee.setScore(employee.getScore()+scoreChange);
                employeeRepo.save(employee);
            }
            return task1;
        }
        return null;
    }

    @Override
    @Transactional
    public Task setCompleted(long taskId, Commit passedCommit) {
        Task task = setCompleted(taskId);
        if(task!=null){
            task.setFilePath(passedCommit.getFilePath());
            return task;
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
    public List<Task> findAllByEmpIdAndState(long empId, String state) {
        List<Task> allTasks = findAllByEmpId(empId);
        if (state.equals("completed")){
            return filter(allTasks,"已完成");
        }else if(state.equals("notCompleted")){
            return filter(allTasks,"未完成");
        }
        return allTasks;
    }

    private List<Task> filter(List<Task> taskList,String state){
        return taskList.stream()
                .filter(task -> task.getState().equals(state))
                .collect(Collectors.toList());
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
            //发送新任务提醒
            mailUtil.sendTaskRemindMail("新任务！ ",task);
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

    @Override
    public void downloadFile(HttpServletResponse response, Task task) {
        if(null!=task){
            try {
                fileUtil.downloadCommit(response, task.getFilePath());
            } catch (UnsupportedEncodingException e) {
                LOGGER.info("下载文件失败{}",e);
            }
        }
    }
}
