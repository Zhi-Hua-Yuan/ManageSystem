-- auto-generated definition
create table department
(
    department_id   int auto_increment
        primary key,
    department_name varchar(64)                        not null,
    is_delete       tinyint  default 0                 not null,
    create_time     datetime default CURRENT_TIMESTAMP not null,
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint department_name
        unique (department_name)
)
    comment '部门表';

