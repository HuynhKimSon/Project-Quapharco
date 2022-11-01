drop table if exists sys_config;

create table sys_config (
  config_id         bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '参数主键',
  config_name       nvarchar(100)    default ''                 ,--  comment '参数名称',
  config_key        nvarchar(100)    default ''                 ,--  comment '参数键名',
  config_value      nvarchar(500)    default ''                 ,--  comment '参数键值',
  config_type       nchar(1)         default 'N'                ,--  comment '系统内置（Y是 N否）',
  create_by         nvarchar(64)     default ''                 ,--  comment '创建者',
  create_time       datetime                                   ,--  comment '创建时间',
  update_by         nvarchar(64)     default ''                 ,--  comment '更新者',
  update_time       datetime                                   ,--  comment '更新时间',
  remark            nvarchar(500)    default null               ,--  comment '备注',
);

drop table if exists sys_dept; 

create table sys_dept (
  dept_id           bigint   IDENTITY(100, 1)   NOT NULL    PRIMARY KEY    ,--  comment '部门id',
  parent_id         bigint          default 0                ,--  comment '父部门id',
  ancestors         nvarchar(50)     default ''               ,--  comment '祖级列表',
  dept_name         nvarchar(30)     default ''               ,--  comment '部门名称',
  order_num         int             default 0                ,--  comment '显示顺序',
  leader            nvarchar(20)     default null             ,--  comment '负责人',
  phone             nvarchar(11)     default null             ,--  comment '联系电话',
  email             nvarchar(50)     default null             ,--  comment '邮箱',
  status            nchar(1)         default '0'              ,--  comment '部门状态（0正常 1停用）',
  del_flag          nchar(1)         default '0'              ,--  comment '删除标志（0代表存在 2代表删除）',
  create_by         nvarchar(64)     default ''               ,--  comment '创建者',
  create_time 	    datetime                                 ,--  comment '创建时间',
  update_by         nvarchar(64)     default ''               ,--  comment '更新者',
  update_time       datetime                                 ,--  comment '更新时间',
);


drop table if exists sys_dict_data;

create table sys_dict_data
(
  dict_code        bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY   ,--  comment '字典编码',
  dict_sort        int             default 0                  ,--  comment '字典排序',
  dict_label       nvarchar(100)    default ''                 ,--  comment '字典标签',
  dict_value       nvarchar(100)    default ''                 ,--  comment '字典键值',
  dict_type        nvarchar(100)    default ''                 ,--  comment '字典类型',
  css_class        nvarchar(100)    default null               ,--  comment '样式属性（其他样式扩展）',
  list_class       nvarchar(100)    default null               ,--  comment '表格回显样式',
  is_default       nchar(1)         default 'N'                ,--  comment '是否默认（Y是 N否）',
  status           nchar(1)         default '0'                ,--  comment '状态（0正常 1停用）',
  create_by        nvarchar(64)     default ''                 ,--  comment '创建者',
  create_time      datetime                                   ,--  comment '创建时间',
  update_by        nvarchar(64)     default ''                 ,--  comment '更新者',
  update_time      datetime                                   ,--  comment '更新时间',
  remark           nvarchar(500)    default null               ,--  comment '备注',
);


drop table if exists  sys_dict_type;

create table sys_dict_type
(
  dict_id          bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '字典主键',
  dict_name        nvarchar(100)    default ''                 ,--  comment '字典名称',
  dict_type        nvarchar(100)    default ''                 ,--  comment '字典类型',
  status           nchar(1)         default '0'                ,--  comment '状态（0正常 1停用）',
  create_by        nvarchar(64)     default ''                 ,--  comment '创建者',
  create_time      datetime                                   ,--  comment '创建时间',
  update_by        nvarchar(64)     default ''                 ,--  comment '更新者',
  update_time      datetime                                   ,--  comment '更新时间',
  remark           nvarchar(500)    default null               ,--  comment '备注',
  CONSTRAINT UC_sys_dict_type UNIQUE (dict_type)
);


drop table if exists  sys_job;

create table sys_job (
  job_id              bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '任务ID',
  job_name            nvarchar(64)   default ''                 ,--  comment '任务名称',
  job_group           nvarchar(64)   default 'DEFAULT'          ,--  comment '任务组名',
  invoke_target       nvarchar(500)  not null                   ,--  comment '调用目标字符串',
  cron_expression     nvarchar(255)  default ''                 ,--  comment 'cron执行表达式',
  misfire_policy      nvarchar(20)   default '3'                ,--  comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  concurrent          nchar(1)       default '1'                ,--  comment '是否并发执行（0允许 1禁止）',
  status              nchar(1)       default '0'                ,--  comment '状态（0正常 1暂停）',
  create_by           nvarchar(64)   default ''                 ,--  comment '创建者',
  create_time         datetime                                 ,--  comment '创建时间',
  update_by           nvarchar(64)   default ''                 ,--  comment '更新者',
  update_time         datetime                                 ,--  comment '更新时间',
  remark              nvarchar(500)  default ''                 ,--  comment '备注信息',
  CONSTRAINT UC_sys_job UNIQUE (job_name, job_group)
);


drop table if exists sys_job_log; 

create table sys_job_log (
  job_log_id          bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '任务日志ID',
  job_name            nvarchar(64)    not null                   ,--  comment '任务名称',
  job_group           nvarchar(64)    not null                   ,--  comment '任务组名',
  invoke_target       nvarchar(500)   not null                   ,--  comment '调用目标字符串',
  job_message         nvarchar(500)                              ,--  comment '日志信息',
  status              nchar(1)        default '0'                ,--  comment '执行状态（0正常 1失败）',
  exception_info      nvarchar(2000)  default ''                 ,--  comment '异常信息',
  create_time         datetime                                  ,--  comment '创建时间',
);


drop table if exists  sys_logininfor; 

create table sys_logininfor (
  info_id        bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY   ,--  comment '访问ID',
  login_name      nvarchar(50)    default ''                ,--  comment '用户账号',
  ipaddr         nvarchar(50)    default ''                ,--  comment '登录IP地址',
  login_location nvarchar(255)   default ''                ,--  comment '登录地点',
  browser        nvarchar(50)    default ''                ,--  comment '浏览器类型',
  os             nvarchar(50)    default ''                ,--  comment '操作系统',
  status         nchar(1)        default '0'               ,--  comment '登录状态（0成功 1失败）',
  msg            nvarchar(255)   default ''                ,--  comment '提示消息',
  login_time     datetime                                 ,--  comment '访问时间',
);


drop table if exists sys_menu; 

create table sys_menu (
  menu_id           bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '菜单ID',
  menu_name         nvarchar(50)     not null                   ,--  comment '菜单名称',
  parent_id         bigint      default 0                  ,--  comment '父菜单ID',
  order_num         int         default 0                  ,--  comment '显示顺序',
  url              nvarchar(200)    default '#'                 ,--  comment '路由地址',
  target              nvarchar(20)    default ''                 ,
  menu_type         nchar(1)         default ''                 ,--  comment '菜单类型（M目录 C菜单 F按钮）',
  visible           nchar(1)         default '0'                  ,--  comment '菜单状态（0显示 1隐藏）',
  is_refresh           nchar(1)         default '1'                  ,
  perms             nvarchar(100)    default null               ,--  comment '权限标识',
  icon              nvarchar(100)    default '#'                ,--  comment '菜单图标',
  create_by         nvarchar(64)     default ''                 ,--  comment '创建者',
  create_time       datetime                                   ,--  comment '创建时间',
  update_by         nvarchar(64)     default ''                 ,--  comment '更新者',
  update_time       datetime                                   ,--  comment '更新时间',
  remark            nvarchar(500)    default ''                 ,--  comment '备注',
);


drop table if exists sys_notice; 

create table sys_notice (
  notice_id         bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '公告ID',
  notice_title      nvarchar(250)     not null                   ,--  comment '公告标题',
  notice_type       nchar(1)         not null                   ,--  comment '公告类型（1通知 2公告）',
  notice_content    nvarchar(2000)       default null               ,--  comment '公告内容',
  status            nchar(1)         default '0'                ,--  comment '公告状态（0正常 1关闭）',
  create_by         nvarchar(64)     default ''                 ,--  comment '创建者',
  create_time       datetime                                   ,--  comment '创建时间',
  update_by         nvarchar(64)     default ''                 ,--  comment '更新者',
  update_time       datetime                                   ,--  comment '更新时间',
  remark            nvarchar(255)    default null               ,--  comment '备注',
);


drop table if exists sys_oper_log; 

create table sys_oper_log (
  oper_id           bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '日志主键',
  title             nvarchar(50)     default ''                 ,--  comment '模块标题',
  business_type     int             default 0                  ,--  comment '业务类型（0其它 1新增 2修改 3删除）',
  method            nvarchar(100)    default ''                 ,--  comment '方法名称',
  request_method    nvarchar(10)     default ''                 ,--  comment '请求方式',
  operator_type     int             default 0                  ,--  comment '操作类别（0其它 1后台用户 2手机端用户）',
  oper_name         nvarchar(50)     default ''                 ,--  comment '操作人员',
  dept_name         nvarchar(50)     default ''                 ,--  comment '部门名称',
  oper_url          nvarchar(255)    default ''                 ,--  comment '请求URL',
  oper_ip           nvarchar(50)     default ''                 ,--  comment '主机地址',
  oper_location     nvarchar(255)    default ''                 ,--  comment '操作地点',
  oper_param        nvarchar(2000)   default ''                 ,--  comment '请求参数',
  json_result       nvarchar(2000)   default ''                 ,--  comment '返回参数',
  status            int             default 0                  ,--  comment '操作状态（0正常 1异常）',
  error_msg         nvarchar(2000)   default ''                 ,--  comment '错误消息',
  oper_time         datetime                                   ,--  comment '操作时间',
);

drop table  if exists sys_post; 

create table sys_post
(
  post_id       bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY   ,--  comment '岗位ID',
  post_code     nvarchar(64)     not null                   ,--  comment '岗位编码',
  post_name     nvarchar(50)     not null                   ,--  comment '岗位名称',
  post_sort     int          not null                   ,--  comment '显示顺序',
  status        nchar(1)         not null                   ,--  comment '状态（0正常 1停用）',
  create_by     nvarchar(64)     default ''                 ,--  comment '创建者',
  create_time   datetime                                   ,--  comment '创建时间',
  update_by     nvarchar(64)     default ''                   ,--  comment '更新者',
  update_time   datetime                                   ,--  comment '更新时间',
  remark        nvarchar(500)    default null               ,--  comment '备注',
); 


drop table  if exists sys_role;

create table sys_role (
  role_id              bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '角色ID',
  role_name            nvarchar(30)     not null                   ,--  comment '角色名称',
  role_key             nvarchar(100)    not null                   ,--  comment '角色权限字符串',
  role_sort            int          not null                   ,--  comment '显示顺序',
  data_scope           nchar(1)         default '1'                ,--  comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  status               nchar(1)         not null                   ,--  comment '角色状态（0正常 1停用）',
  del_flag             nchar(1)         default '0'                ,--  comment '删除标志（0代表存在 2代表删除）',
  create_by            nvarchar(64)     default ''                 ,--  comment '创建者',
  create_time          datetime                                   ,--  comment '创建时间',
  update_by            nvarchar(64)     default ''                 ,--  comment '更新者',
  update_time          datetime                                   ,--  comment '更新时间',
  remark               nvarchar(500)    default null               ,--  comment '备注',
);

drop table  if exists sys_role_dept;

create table sys_role_dept (
  role_id   bigint not null ,--  comment '角色ID',
  dept_id   bigint not null ,--  comment '部门ID',
  primary key(role_id, dept_id)
);

drop table if exists sys_role_menu; 

create table sys_role_menu (
  role_id   bigint not null ,--  comment '角色ID',
  menu_id   bigint not null ,--  comment '菜单ID',
  primary key(role_id, menu_id)
);

drop table if exists sys_user;

create table sys_user (
  user_id           bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '用户ID',
  dept_id           bigint      default null               ,--  comment '部门ID',
  login_name         nvarchar(30)     not null                   ,--  comment '用户账号',
  user_name         nvarchar(30)     not null                   ,--  comment '用户昵称',
  user_type         nvarchar(2)      default '00'               ,--  comment '用户类型（00系统用户）',
  email             nvarchar(50)     default ''                 ,--  comment '用户邮箱',
  phonenumber       nvarchar(11)     default ''                 ,--  comment '手机号码',
  sex               nchar(1)         default '0'                ,--  comment '用户性别（0男 1女 2未知）',
  avatar            nvarchar(100)    default ''                 ,--  comment '头像地址',
  password          nvarchar(100)    default ''                 ,--  comment '密码',
  salt nvarchar(20) DEFAULT '',
  status            nchar(1)         default '0'                ,--  comment '帐号状态（0正常 1停用）',
  del_flag          nchar(1)         default '0'                ,--  comment '删除标志（0代表存在 2代表删除）',
  login_ip          nvarchar(50)     default ''                 ,--  comment '最后登录IP',
  login_date        datetime                                   ,--  comment '最后登录时间',
  pwd_update_date datetime DEFAULT NULL,
  create_by         nvarchar(64)     default ''                 ,--  comment '创建者',
  create_time       datetime                                   ,--  comment '创建时间',
  update_by         nvarchar(64)     default ''                 ,--  comment '更新者',
  update_time       datetime                                   ,--  comment '更新时间',
  remark            nvarchar(500)    default null               ,--  comment '备注',
);


DROP TABLE IF EXISTS sys_user_online;
CREATE TABLE sys_user_online (
  sessionId nvarchar(50) NOT NULL DEFAULT '',
  login_name nvarchar(50) DEFAULT '',
  dept_name nvarchar(50) DEFAULT '',
  ipaddr nvarchar(128) DEFAULT '',
  login_location nvarchar(255) DEFAULT '',
  browser nvarchar(50) DEFAULT '',
  os nvarchar(50) DEFAULT '',
  status nvarchar(10) DEFAULT '',
  start_timestamp datetime DEFAULT NULL,
  last_access_time datetime DEFAULT NULL,
  expire_time int DEFAULT 0,
  PRIMARY KEY (sessionId)
)

drop table if exists sys_user_post;

create table sys_user_post
(
  user_id   bigint not null ,--  comment '用户ID',
  post_id   bigint not null ,--  comment '岗位ID',
  primary key (user_id, post_id)
);

drop table if exists sys_user_role; 

create table sys_user_role (
  user_id   bigint not null ,--  comment '用户ID',
  role_id   bigint not null ,--  comment '角色ID',
  primary key(user_id, role_id)
);


DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;


drop table if exists QRTZ_JOB_DETAILS; 

create table QRTZ_JOB_DETAILS (
    sched_name           nvarchar(120)    not null,
    job_name             nvarchar(200)    not null,
    job_group            nvarchar(200)    not null,
    description          nvarchar(250)    null,
    job_class_name       nvarchar(250)    not null,
    is_durable           nvarchar(1)      not null,
    is_nonconcurrent     nvarchar(1)      not null,
    is_update_data       nvarchar(1)      not null,
    requests_recovery    nvarchar(1)      not null,
    job_data             IMAGE           null,
    primary key (sched_name,job_name,job_group)
);


drop table if exists QRTZ_TRIGGERS; 

create table QRTZ_TRIGGERS (
    sched_name           nvarchar(120)    not null,
    trigger_name         nvarchar(200)    not null,
    trigger_group        nvarchar(200)    not null,
    job_name             nvarchar(200)    not null,
    job_group            nvarchar(200)    not null,
    description          nvarchar(250)    null,
    next_fire_time       bigint          null,
    prev_fire_time       bigint          null,
    priority             integer         null,
    trigger_state        nvarchar(16)     not null,
    trigger_type         nvarchar(8)      not null,
    start_time           bigint          not null,
    end_time             bigint          null,
    calendar_name        nvarchar(200)    null,
    misfire_instr        smallint        null,
    job_data             IMAGE           null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,job_name,job_group) references QRTZ_JOB_DETAILS(sched_name,job_name,job_group)
);

drop table if exists QRTZ_SIMPLE_TRIGGERS; 

create table QRTZ_SIMPLE_TRIGGERS (
    sched_name           nvarchar(120)    not null,
    trigger_name         nvarchar(200)    not null,
    trigger_group        nvarchar(200)    not null,
    repeat_count         bigint       not null,
    repeat_interval      bigint      not null,
    times_triggered      bigint      not null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references QRTZ_TRIGGERS(sched_name,trigger_name,trigger_group)
);


drop table if exists QRTZ_CRON_TRIGGERS; 

create table QRTZ_CRON_TRIGGERS (
    sched_name           nvarchar(120)    not null,
    trigger_name         nvarchar(200)    not null,
    trigger_group        nvarchar(200)    not null,
    cron_expression      nvarchar(200)    not null,
    time_zone_id         nvarchar(80),
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references QRTZ_TRIGGERS(sched_name,trigger_name,trigger_group)
);

drop table if exists QRTZ_BLOB_TRIGGERS; 

create table QRTZ_BLOB_TRIGGERS (
    sched_name           nvarchar(120)    not null,
    trigger_name         nvarchar(200)    not null,
    trigger_group        nvarchar(200)    not null,
    blob_data            image           null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references QRTZ_TRIGGERS(sched_name,trigger_name,trigger_group)
);

drop table if exists QRTZ_CALENDARS; 

create table QRTZ_CALENDARS (
    sched_name           nvarchar(120)    not null,
    calendar_name        nvarchar(200)    not null,
    calendar             IMAGE           not null,
    primary key (sched_name,calendar_name)
);

drop table if exists QRTZ_PAUSED_TRIGGER_GRPS; 

create table QRTZ_PAUSED_TRIGGER_GRPS (
    sched_name           nvarchar(120)    not null,
    trigger_group        nvarchar(200)    not null,
    primary key (sched_name,trigger_group)
);

drop table if exists QRTZ_FIRED_TRIGGERS;

create table QRTZ_FIRED_TRIGGERS (
    sched_name           nvarchar(120)    not null,
    entry_id             nvarchar(95)     not null,
    trigger_name         nvarchar(200)    not null,
    trigger_group        nvarchar(200)    not null,
    instance_name        nvarchar(200)    not null,
    fired_time           bigint      not null,
    sched_time           bigint      not null,
    priority             integer         not null,
    state                nvarchar(16)     not null,
    job_name             nvarchar(200)    null,
    job_group            nvarchar(200)    null,
    is_nonconcurrent     nvarchar(1)      null,
    requests_recovery    nvarchar(1)      null,
    primary key (sched_name,entry_id)
);

drop table if exists QRTZ_SCHEDULER_STATE; 

create table QRTZ_SCHEDULER_STATE (
    sched_name           nvarchar(120)    not null,
    instance_name        nvarchar(200)    not null,
    last_checkin_time    bigint      not null,
    checkin_interval     bigint      not null,
    primary key (sched_name,instance_name)
);


drop table if exists QRTZ_LOCKS; 

create table QRTZ_LOCKS (
    sched_name           nvarchar(120)    not null,
    lock_name            nvarchar(40)     not null,
    primary key (sched_name,lock_name)
);

drop table if exists QRTZ_SIMPROP_TRIGGERS; 

create table QRTZ_SIMPROP_TRIGGERS (
    sched_name           nvarchar(120)    not null,
    trigger_name         nvarchar(200)    not null,
    trigger_group        nvarchar(200)    not null,
    str_prop_1           nvarchar(512)    null,
    str_prop_2           nvarchar(512)    null,
    str_prop_3           nvarchar(512)    null,
    int_prop_1           int             null,
    int_prop_2           int             null,
    long_prop_1          bigint          null,
    long_prop_2          bigint          null,
    dec_prop_1           numeric(13,4)   null,
    dec_prop_2           numeric(13,4)   null,
    bool_prop_1          nvarchar(1)      null,
    bool_prop_2          nvarchar(1)      null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references QRTZ_TRIGGERS(sched_name,trigger_name,trigger_group)
);


drop table if exists gen_table; 

create table gen_table (
  table_id          bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '编号',
  table_name        nvarchar(200)    default ''                 ,--  comment '表名称',
  table_comment     nvarchar(500)    default ''                 ,--  comment '表描述',
  class_name        nvarchar(100)    default ''                 ,--  comment '实体类名称',
  tpl_category      nvarchar(200)    default 'crud'             ,--  comment '使用的模板（crud单表操作 tree树表操作）',
  package_name      nvarchar(100)                               ,--  comment '生成包路径',
  module_name       nvarchar(30)                                ,--  comment '生成模块名',
  business_name     nvarchar(30)                                ,--  comment '生成业务名',
  function_name     nvarchar(50)                                ,--  comment '生成功能名',
  function_author   nvarchar(50)                                ,--  comment '生成功能作者',
  gen_type          nchar(1)         default '0'                ,--  comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path          nvarchar(200)    default '/'                ,--  comment '生成路径（不填默认项目路径）',
  options           nvarchar(1000)                              ,--  comment '其它生成选项',
  create_by         nvarchar(64)     default ''                 ,--  comment '创建者',
  create_time         datetime                                   ,--  comment '创建时间',
  update_by         nvarchar(64)     default ''                 ,--  comment '更新者',
  update_time       datetime                                   ,--  comment '更新时间',
  remark            nvarchar(500)    default null               ,--  comment '备注',
);


drop table if exists gen_table_column; 

create table gen_table_column (
  column_id         bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY    ,--  comment '编号',
  table_id          nvarchar(64)                                ,--  comment '归属表编号',
  column_name       nvarchar(200)                               ,--  comment '列名称',
  column_comment    nvarchar(500)                               ,--  comment '列描述',
  column_type       nvarchar(100)                               ,--  comment '列类型',
  java_type         nvarchar(500)                               ,--  comment 'JAVA类型',
  java_field        nvarchar(200)                               ,--  comment 'JAVA字段名',
  is_pk             nchar(1)                                    ,--  comment '是否主键（1是）',
  is_increment      nchar(1)                                    ,--  comment '是否自增（1是）',
  is_required       nchar(1)                                    ,--  comment '是否必填（1是）',
  is_insert         nchar(1)                                    ,--  comment '是否为插入字段（1是）',
  is_edit           nchar(1)                                    ,--  comment '是否编辑字段（1是）',
  is_list           nchar(1)                                    ,--  comment '是否列表字段（1是）',
  is_query          nchar(1)                                    ,--  comment '是否查询字段（1是）',
  query_type        nvarchar(200)    default 'EQ'               ,--  comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type         nvarchar(200)                               ,--  comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type         nvarchar(200)    default ''                 ,--  comment '字典类型',
  sort              int                                        ,--  comment '排序',
  create_by         nvarchar(64)     default ''                 ,--  comment '创建者',
  create_time         datetime                                   ,--  comment '创建时间',
  update_by         nvarchar(64)     default ''                 ,--  comment '更新者',
  update_time       datetime                                   ,--  comment '更新时间',
); 

DROP TABLE IF EXISTS robot;
CREATE TABLE robot (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  uuid nvarchar(64) NOT NULL,
  ip_address nvarchar(120) DEFAULT NULL,
  status nchar(1) DEFAULT 0,
  disabled nchar(1) DEFAULT 0,
  ping_time datetime,
  del_flag nchar(1) DEFAULT 0,
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);

DROP TABLE IF EXISTS service;
CREATE TABLE service (
  id bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  name nvarchar(250),
  del_flag nchar(1) DEFAULT 0,
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);

DROP TABLE IF EXISTS robot_service;
CREATE TABLE robot_service (
  robot_id bigint NOT NULL,
  service_id bigint NOT NULL,
  PRIMARY KEY (robot_id,service_id)
);



DROP TABLE IF EXISTS work_process;
CREATE TABLE work_process (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  service_id int, 
  sync_id bigint, -- 0-Hoa don goc  >0-Hoa don dieu chinh  Null-Cap nhat trang thai
  sync_key nvarchar(255) UNIQUE,
  data_1 nvarchar(255), -- So hoa don
  data_2 nvarchar(255), -- 0-Trang thai GOC 1-Trang thai DA DIEU CHINH
  data_3 nvarchar(255),
  data_4 nvarchar(255),
  data_5 nvarchar(255),
  priority int DEFAULT 0,
  status int DEFAULT 0,  -- 0-Chua lam 1-Đa gui 2-Cho thuc hien 3-Dang lam 4-That bai 5-Thanh cong
  error ntext DEFAULT NULL,
  data_request ntext DEFAULT NULL,
  data_response ntext DEFAULT NULL,
  robot_uuid VARCHAR(100) DEFAULT NULL,
  start_date DATETIME DEFAULT NULL,
  end_date DATETIME DEFAULT NULL,
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);



