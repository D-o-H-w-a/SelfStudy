CREATE DATABASE todoList;

use todoList;

CREATE TABLE USER_INFO (
  num int primary key,
  name varchar(5) not null,
  id varchar(10) not null unique,
  pw varchar(16) not null,
  email varchar(20) not null unique
);

CREATE TABLE USER_SCHEDULE (
  num int primary key,
  subject varchar(50) not null,
  content varchar(500) not null,
  orders varchar(10) not null,
  sch_date datetime not null
);

ALTER TABLE USER_SCHEDULE ADD FOREIGN KEY(num) REFERENCES USER_INFO(num);