package com.stugud.dispatcher.periodictask;

import com.stugud.dispatcher.controller.EmployeeController;
import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Record;
import com.stugud.dispatcher.repo.EmployeeRepo;
import com.stugud.dispatcher.repo.RecordRepo;
import com.stugud.dispatcher.repo.TaskRepo;
import com.stugud.dispatcher.service.EmployeeService;
import com.stugud.dispatcher.service.RecordService;
import com.stugud.dispatcher.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created By Gud on 2020/11/4 1:16 下午
 */
@Component("ScoreCalibration")
public class ScoreCalibration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    RecordService recordService;



    /**
     * 每月25日的上午10:15触发 校准分数
     * 如果分数不匹配，输出WARN
     */
    @Scheduled(cron = "0 15 10 25 * ?")
    public void calibrate() {
        LOGGER.info("开始校准任务");
        for (int i = 0; ; i++) {
            List<Employee> employees = employeeService.findAllByPageNum(i);
            if (employees.isEmpty()){
                LOGGER.info("校准任务完成");
                return;
            }
            for (Employee employee:employees){
                List<Record> records = recordService.findAllByEmpId(employee.getId());
                int score=0;
                for (Record record:records){
                    score+=record.getScoreChange();
                }
                if (score!=employee.getScore()){
                    LOGGER.warn("校准时发现EmployeeId{}分数不匹配:原始分数{} 计算分数{}",employee.getId(),employee.getScore(),score);
                    LOGGER.info("正在修改");
                    employee.setScore(score);
                    employeeRepo.save(employee);
                }
            }
        }
    }


}
