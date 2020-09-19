create table if not exists t_employee
(
    id         bigint      not null auto_increment primary key,
    username   varchar(32) not null ,
    password   varchar(16) not null,
    mail       varchar(64) not null,
    department varchar(32),
    score      int default 0
);

#content以及filePath应该单独建表，暂用。
create table if not exists t_task
(
    id         bigint        not null auto_increment primary key,
    subject    varchar(32)   not null,
    content    varchar(1024) not null,
    level      varchar(4)    not null,
    createdAt  date          not null,
    deadline   date          not null,
    state      varchar(8)    not null default '未完成',
    finishedAt date,
    filePath   varchar(128)
);

create table if not exists t_task_employees
(
    taskId      bigint not null,
    employeeId  bigint not null,
    scoreChange int default 0,
    primary key (taskId, employeeId)
);

alter table t_task_employees
    add foreign key (taskId) references t_task (id);

alter table t_task_employees
    add foreign key (employeeId) references t_employee (id);



