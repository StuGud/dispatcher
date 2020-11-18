create table if not exists t_employee
(
    id         bigint      not null auto_increment primary key,
    username   varchar(32) not null,
    password   varchar(64) not null default '$2a$10$4s0Z2d0grlBxie2fynVUEOrRjMGRt.sBLRwfSul/rnQw1fMf4g6mm',
    mail       varchar(64) not null unique,
    department varchar(32),
    score      int default 0
);

create table if not exists t_task
(
    id         bigint        not null auto_increment primary key,
    subject    varchar(32)   not null,
    content    varchar(1024) not null,
    level      varchar(4)    not null,
    createdAt  date          not null,
    deadline   date          not null,
    state      int default 0,
    finishedAt date,
    filePath   varchar(256)
);

# state int default 0,
create table if not exists t_task_employee
(
    taskId      bigint not null,
    employeeId  bigint not null,
    scoreChange int default 0,
    finishedAt  date,
    primary key (taskId, employeeId)
);

alter table t_task_employee
    add foreign key (taskId) references t_task (id);

alter table t_task_employee
    add foreign key (employeeId) references t_employee (id);

create table if not exists t_commit
(
    id         bigint not null auto_increment primary key,
    taskId     bigint not null,
    employeeId bigint not null,
    commitNo   int    not null,
    commitAt   date   not null,
    message    varchar(64),
    reply      varchar(64),
    state      int default 0,
    filePath   varchar(256)
);

alter table t_commit
    add foreign key (employeeId) references t_employee (id);

alter table t_commit
    add foreign key (taskId) references t_task (id);

ALTER TABLE t_commit
    ADD unique (taskId, commitNo);

create table if not exists t_employee_leader
(
    id       bigint not null,
    level    int,
    leaderId bigint not null,
    priority int default 1,
    primary key (id, leaderId)
);

alter table t_employee_leader
    add foreign key (id) references t_employee (id);

alter table t_employee_leader
    add foreign key (leaderId) references t_employee (id);



