drop table USERS if exists;
drop table USER_ROLES if exists;

create table USERS (
  username varchar(255) not null,
  password varchar(255) not null,
  primary key (username));


create table USER_ROLES (
  username varchar(255) not null,
  role_name varchar(255) not null);


alter table USER_ROLES add constraint username_role foreign key (username) references USERS;