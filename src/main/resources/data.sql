delete from t_employee;
delete from t_task;
delete from t_task_employees;

insert into t_employee (id,username,password,mail,department)
values (0,'employee1','password1','741498908@qq.com','department1'),
       (1,'employee2','password2','1174145941@qq.com','department2');

insert into t_task(subject, content, level, created, deadline, state)
values ('task1_subject','task1_content','A','2020-8-1','2020-9-1','未完成'),
       ('task2_subject','task2_content','B','2020-8-30','2020-9-30','未完成');

insert into t_task_employees(task, employee)
values (1,0),
       (2,0),
       (2,1);


