package com.stugud.dispatcher.service.impl;

import com.stugud.dispatcher.dto.EmployeeUserDetails;
import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Record;
import com.stugud.dispatcher.entity.SimplePermission;
import com.stugud.dispatcher.repo.EmployeeRepo;
import com.stugud.dispatcher.repo.RecordRepo;
import com.stugud.dispatcher.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    /**
     * 只修改密码
     * @param modifiedEmp
     * @return
     */
    @Override
    public Employee modifyByEmpself(Employee modifiedEmp) {
        Employee currentEmp=getCurrentEmployee();
        LOGGER.info("员工{}想要修改信息为{}",currentEmp,modifiedEmp);
        currentEmp.setPassword(passwordEncoder.encode(modifiedEmp.getPassword()));
        return employeeRepo.save(currentEmp);
    }

    @Override
    public Employee getCurrentEmployee() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        EmployeeUserDetails employeeUserDetails = (EmployeeUserDetails) authentication.getPrincipal();
        return employeeUserDetails.getEmployee();
    }

    @Override
    public Employee findByMail(String mail) {
        Employee employee = employeeRepo.findByMail(mail);
        return employee;
    }

    @Override
    public Map<Employee, Integer> countScoresByMonth(int year, int month) {
        List<Record> records = recordRepo.findAllByMonth(year, month);
        Map<Long, Integer> map = new HashMap<>();
        for (Record record : records) {
            if (map.containsKey(record.getEmployeeId())) {
                map.put(record.getEmployeeId(), map.get(record.getEmployeeId()) + record.getScoreChange());
            } else {
                map.put(record.getEmployeeId(), record.getScoreChange());
            }
        }
        Map<Employee, Integer> employeeIntegerMap = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            Optional<Employee> optionalEmployee = employeeRepo.findById(entry.getKey());
            if (optionalEmployee.isPresent())
                employeeIntegerMap.put(optionalEmployee.get(), entry.getValue());
        }
        return employeeIntegerMap;
    }

    @Override
    public Employee findById(long id) {
        Optional<Employee> optionalEmployee = employeeRepo.findById(id);
        if (optionalEmployee.isPresent()) {
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
        Employee savingEmployee = Employee.register(employee);
        Employee savedEmployee = employeeRepo.save(savingEmployee);
        LOGGER.info("创建新员工{}",savedEmployee);
        return savedEmployee;
    }

    @Override
    public List<Employee> findAllByTaskId(long taskId) {
        return employeeRepo.findAllByTaskId(taskId);
    }

    @Override
    public List<Employee> findAllByPageNum(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<Employee> employees = (List<Employee>) employeeRepo.findAll(pageable);
        return employees;
    }

    @Override
    public List<SimplePermission> getPermissionList(long empId) {
        return null;
    }

    @Override
    public boolean isEmployeeInChargeTask(long employeeId, long taskId) {
        return recordRepo.findByEmployeeIdAndTaskId(employeeId,taskId)!=null;
    }

    @Override
    public boolean isMailExist(String mail) {
        return false;
    }

    @Override
    public List<Employee> findLeaders(long employeeId) {
        List<Employee> leaders = employeeRepo.findLeadersById(employeeId);
        return leaders;
    }
}
