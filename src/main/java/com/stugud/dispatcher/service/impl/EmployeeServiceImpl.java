package com.stugud.dispatcher.service.impl;

import com.stugud.dispatcher.entity.Employee;
import com.stugud.dispatcher.entity.Record;
import com.stugud.dispatcher.entity.SimplePermission;
import com.stugud.dispatcher.repo.EmployeeRepo;
import com.stugud.dispatcher.repo.PermissionRepo;
import com.stugud.dispatcher.repo.RecordRepo;
import com.stugud.dispatcher.service.EmployeeService;
import com.stugud.dispatcher.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    final
    EmployeeRepo employeeRepo;

    final
    RecordRepo recordRepo;

    final
    PermissionRepo permissionRepo;

    @Value("${dispatcher.employee.pageSize}")
    int pageSize;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo, RecordRepo recordRepo, PermissionRepo permissionRepo) {
        this.employeeRepo = employeeRepo;
        this.recordRepo = recordRepo;
        this.permissionRepo = permissionRepo;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //数据库中的密码是明文
            if (!passwordEncoder.matches(password, passwordEncoder.encode(userDetails.getPassword()))) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
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
        return employeeRepo.save(employee);
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
        return permissionRepo.findAllByEmployeeId(empId);
    }
}
