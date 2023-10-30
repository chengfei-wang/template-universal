drop database if exists `template-universal`;
create database if not exists `template-universal` character set utf8mb4 collate utf8mb4_bin;

use `template-universal`;

create table form_data
(
    submit_id         varchar(64) not null primary key,
    submit_page       varchar(64) not null,
    submit_ip_address varchar(64) not null,
    submit_time       datetime    not null default current_timestamp,
    submit_content    longtext    not null,
    submit_user       varchar(64) null
);

create table access_log
(
    access_id         varchar(64) not null primary key,
    access_page       varchar(64) not null,
    access_ip_address varchar(64) not null,
    access_time       datetime    not null default current_timestamp
);

create table user_verify_code
(
    code_id     varchar(64) not null primary key,
    code_page   varchar(64) not null,
    code_key    varchar(64) not null,
    code_value  varchar(64) not null,
    code_expire datetime    not null
);

create table page_info
(
    page_id         varchar(64)  not null primary key,
    title           varchar(64)  not null,
    elements        text         not null,
    deploy_type     varchar(64)  not null,
    user_verify     varchar(64)  not null,
    deploy_addition varchar(128) not null
);