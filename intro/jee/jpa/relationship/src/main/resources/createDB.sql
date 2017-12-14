drop table Address if exists;
drop table GroupAssignment if exists;
drop table Message if exists;
drop table User if exists;
drop table User_GroupAssignment if exists;
drop table User_Message if exists;
drop table User_roles if exists;
drop sequence if exists hibernate_sequence;


create sequence hibernate_sequence start with 1 increment by 1;

create table Address (
  DTYPE varchar(31) not null,
  id bigint not null,
  city varchar(255),
  country varchar(255),
  postcode bigint,
  primary key (id));

create table GroupAssignment (
  id bigint not null,
  text varchar(255),
  primary key (id));

create table Message (
  DTYPE varchar(31) not null,
  id bigint not null,
  text varchar(255),
  destination_id bigint,
  sender_id bigint,
  primary key (id));

create table User (
  id bigint not null,
  name varchar(255),
  surname varchar(255),
  address_id bigint,
  addressWithUserLink_id bigint,
  primary key (id));

create table User_GroupAssignment (
  authors_id bigint not null,
  assignments_id bigint not null,
  assignments_KEY bigint not null,
  primary key (authors_id, assignments_KEY));

create table User_Message (
  User_id bigint not null,
  sentMessages_id bigint not null);

create table User_roles (
  User_id bigint not null,
  roles varchar(255));

alter table User_Message add constraint UK_812qgr2woxiu6e0776qiioy12 unique (sentMessages_id);
alter table Message add constraint FKqchb6i03kpgj2gnq4xhmkydxc foreign key (destination_id) references User;
alter table Message add constraint FKcrnfc5k5lxdfurq3b97o52dnv foreign key (sender_id) references User;
alter table User add constraint FKlq0qkm58rh351bb84y4o5c447 foreign key (address_id) references Address;
alter table User add constraint FKe537pv7sahdo44b6y2lajtge1 foreign key (addressWithUserLink_id) references Address;
alter table User_GroupAssignment add constraint FK6d0gq3ig4kqv0ner5boes9yl2 foreign key (assignments_id) references GroupAssignment;
alter table User_GroupAssignment add constraint FKc8mwm9kd13wr9wtmbtvabd455 foreign key (authors_id) references User;
alter table User_Message add constraint FK6qbykpe0c8j68iwgm77hjtvhp foreign key (sentMessages_id) references Message;
alter table User_Message add constraint FKevixh4x7y5n4lc9pah5ndnqp9 foreign key (User_id) references User;
alter table User_roles add constraint FKi81fp6mx433heb7dvbxqaqvpv foreign key (User_id) references User;