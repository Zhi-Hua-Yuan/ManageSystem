-- auto-generated definition
create table employee
(
    employee_id       int auto_increment comment '员工工号'
        primary key,
    employee_name     varchar(64)                        null comment '员工姓名',
    employee_account  varchar(64)                        null comment '员工账号',
    employee_password varchar(255)                       null comment '员工密码',
    gender            tinyint                            null comment '员工性别 0-男 1-女',
    phone             varchar(16)                        null comment '员工电话',
    email             varchar(32)                        null comment '员工邮箱号',
    birthday          date                               null comment '员工出生日期',
    department_id     int                                null comment '所属部门的id号',
    job               varchar(32)                        null comment '员工职位',
    salary            decimal(10, 2)                     null comment '工资',
    employee_role     tinyint  default 2                 not null comment '用户权限 0-系统管理员 1-部门经理 2-普通员工',
    is_delete         tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除',
    create_time       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint employee_pk
        unique (employee_account),
    constraint employee_pk_2
        unique (employee_account),
    constraint employee_pk_3
        unique (phone),
    constraint employee_pk_4
        unique (email),
    constraint fk_employee_department
        foreign key (department_id) references department (department_id)
            on update cascade
)
    comment '员工表';

