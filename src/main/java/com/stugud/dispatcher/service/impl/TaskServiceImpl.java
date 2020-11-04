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
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

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
        if (optionalTask.isPresent()) {
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
        if (state.equals("completed")) {
            return filter(allTasks, "已完成");
        } else if (state.equals("notCompleted")) {
            return filter(allTasks, "未完成");
        }
        return allTasks;
    }

    private List<Task> filter(List<Task> taskList, String state) {
        return taskList.stream()
                .filter(task -> task.getState().equals(state))
                .collect(Collectors.toList());
    }

    @Override
    public Task releaseWithInChargesName(Task task) {
        List<Employee> inChargeList = new ArrayList<>();
        if (task.getInCharge() != null) {
            inChargeList = findAllEmployeesByNameOrId(task.getInCharge());
        }
        if (inChargeList.isEmpty()) {
            return null;
        } else {
            task.setInCharge(inChargeList);
            Task savingTask=Task.release(task);
            Task savedTask = taskRepo.save(savingTask);
            LOGGER.info("新建任务{}",savedTask);
            //发送新任务提醒
            mailUtil.sendTaskRemindMail("新任务！ ", task);
            return savedTask;
        }
    }


    /**
     * @param employees 为null或为空时，返回空的list；
     * @return
     */
    private List<Employee> findAllEmployeesByNameOrId(List<Employee> employees) {
        List<Employee> inChargeList = new ArrayList<>();
        for (Employee inChargeP : employees) {
            if (inChargeP.getId() != 0) {
                Optional<Employee> optionalEmployee = employeeRepo.findById(inChargeP.getId());
                if (optionalEmployee.isPresent()) {
                    inChargeList.add(optionalEmployee.get());
                }
            } else if (inChargeP.getUsername() != null) {
                List<Employee> employeesWithSameName = employeeRepo.findAllByUsername(inChargeP.getUsername());
                for (Employee employee : employeesWithSameName) {
                    inChargeList.add(employee);
                }
            }
        }
        return inChargeList;
    }

    @Override
    public void downloadFile(HttpServletResponse response, Task task) {
        if (null != task) {
            try {
                fileUtil.downloadCommit(response, task.getFilePath());
            } catch (UnsupportedEncodingException e) {
                LOGGER.info("下载文件失败{}", e);
            }
        }
    }

    @Override
    @Transactional
    public Task modify(Task modifiedTask) {
        Task completed=null;
        Optional<Task> optionalTask = taskRepo.findById(modifiedTask.getId());
        if (optionalTask.isPresent()) {
            Task savingTask = optionalTask.get();
            //已完成的任务不可以修改
            if (savingTask.getState().equals("已完成")) {
                return null;
            }
            LOGGER.info("正在修改任务{}",savingTask);
            if (modifiedTask.getSubject() != null) {
                savingTask.setSubject(modifiedTask.getSubject());
            }
            if (modifiedTask.getContent() != null) {
                savingTask.setContent(modifiedTask.getContent());
            }
            if (modifiedTask.getLevel() != null) {
                savingTask.setLevel(modifiedTask.getLevel());
            }
            if (modifiedTask.getDeadline() != null) {
                savingTask.setDeadline(modifiedTask.getDeadline());
            }
            if (modifiedTask.getInCharge() != null) {
                savingTask.setInCharge(findAllEmployeesByNameOrId(modifiedTask.getInCharge()));
            }

            Task savedTask = taskRepo.save(savingTask);
            if (modifiedTask.getState() != null) {
                if(savingTask.getState().equals("未完成")&&modifiedTask.getState().equals("已完成")){
                     completed= setCompleted(savingTask.getId());
                }
            }
            if (null!=completed){
                mailUtil.sendTaskRemindMail("任务完成！ ", completed);
                LOGGER.info("任务修改为{}",completed);
                return completed;
            }else{
                mailUtil.sendTaskRemindMail("任务修改！ ", savedTask);
                LOGGER.info("任务修改为{}",savedTask);
                return savedTask;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Task setCompleted(long taskId) {
        Optional<Task> optionalTask = taskRepo.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task1 = optionalTask.get();
            task1.setState("已完成");
            task1.setFinishedAt(new Date());
            taskRepo.save(task1);

            int scoreChange = calculateScore(task1.getDeadline());
            for (Employee employee : task1.getInCharge()) {
                recordRepo.save(new Record(task1.getId(), employee.getId(), scoreChange, task1.getFinishedAt()));
                employee.setScore(employee.getScore() + scoreChange);
                employeeRepo.save(employee);
                LOGGER.info("任务{}员工{}得分{}",task1,employee,scoreChange);
            }
            return task1;
        }
        return null;
    }

    @Override
    @Transactional
    public Task setCompleted(long taskId, Commit passedCommit) {
        Task task = setCompleted(taskId);
        if (task != null) {
            task.setFilePath(passedCommit.getFilePath());
            return task;
        }
        return null;
    }

    private int calculateScore(Date deadline) {
        long deadlineLong = deadline.getTime();
        long nowDate = (new Date()).getTime();
        long delay = (nowDate - deadlineLong) / (24 * 60 * 60 * 1000);
        int scoreChange = (int) (-5 * delay);
        return scoreChange;
    }

    @Override
    @Transactional
    public Task setNotCompleted(long taskId) throws Exception {
        Optional<Task> optionalTask = taskRepo.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            for (Employee employee : task.getInCharge()) {
                Record record = recordRepo.findByEmployeeIdAndTaskId(employee.getId(), task.getId());
                if (record == null) {
                    throw new Exception();
                }
                record.setScoreChange(0);
                record.setFinishedAt(new Date(0L));
                recordRepo.save(record);
                employee.setScore(employee.getScore() - record.getScoreChange());
                employeeRepo.save(employee);
            }
            Optional<Task> optionalTask1 = taskRepo.findById(taskId);
            if (optionalTask1.isPresent()){
                optionalTask1.get().setState("未完成");
                optionalTask1.get().setFinishedAt(new Date(0L));
                Task save = taskRepo.save(optionalTask1.get());
                return save;
            }
        }
        return null;
    }
}
