
insert into t_employee (id,username,password,mail,department)
values (0,'employee1','password1','741498908@qq.com','department1'),
       (1,'employee2','password2','1174145941@qq.com','department2');

insert into t_task(id,subject, content, level, createdAt, deadline)
values (0,'task1_subject','task1_content','A','2020-8-1','2020-9-1'),
       (1,'task2_subject','task2_content','B','2020-8-30','2020-9-30');

insert into t_task_employees(taskId, employeeId)
values (0,0),
       (1,0),
       (1,1);


