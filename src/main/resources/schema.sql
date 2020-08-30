create table if not exists t_employee
(
    id         bigint      not null primary key,
    username   varchar(8)  not null,
    password   varchar(16) not null,
    mail       varchar(32) not null,
    department varchar(8)  not null,
    score      int default 0
);

create table if not exists t_task
(
    id bigint not null auto_increment primary key ,
    subject varchar(32) not null,
    content varchar(1024) not null ,
    level varchar(4) not null ,
    created date not null ,
    deadline date not null,
    state varchar(8) not null
);

create table if not exists t_task_employees
(
    task bigint not null ,
    employee bigint not null,
    scoreChange int default 0,
    primary key (task,employee)
);

alter table t_task_employees
    add foreign key (task) references t_task(id);

alter table t_task_employees
    add foreign key (employee) references t_employee(id);



