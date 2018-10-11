create sequence hibernate_sequence start with 1 increment by 1;


create table book (
  id bigint not null,
  author varchar(64) not null,
  title varchar(256)not null,
  year integer not null check (year<=2200),
  primary key (id)
);
