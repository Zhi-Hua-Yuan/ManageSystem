-- auto-generated definition
create table holiday
(
    holiday_id       int auto_increment comment '主键ID'
        primary key,
    holiday_emp_id   int                                not null comment '请假',
    holiday_emp_name varchar(64)                        not null comment '请假',
    holiday_deptName varchar(64)                        not null comment '请假员工所属的部门名称',
    holiday_date     date                               not null comment '申请的请假时间',
    reason           text                               null comment '请假',
    is_approve       tinyint  default 0                 not null comment '是否审批 0-未审批 1-已经审批',
    is_pass          tinyint  default 0                 not null comment '是否通过 0-未通过 1-通过',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '创建请假申请表的时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '审批完成时间',
    is_delete        tinyint  default 0                 not null comment '是否删除 0-未删除 1-已经删除',
    constraint holiday_pk
        unique (holiday_emp_id)
)
    comment '请假表';

