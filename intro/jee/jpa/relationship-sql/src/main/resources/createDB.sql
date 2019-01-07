drop table X_1_to_1_ind if exists;
drop table X_1_to_1_uni if exists;
drop table X_1_to_m_bi if exists;
drop table X_1_to_m_join if exists;
drop table X_1_to_m_mt1 if exists;
drop table X_1_to_m_uni if exists;
drop table X_1_to_m_uni_Y_1_to_m_uni if exists;
drop table X_m_to_m_bi if exists;
drop table X_m_to_m_bi_Y_m_to_m_bi if exists;
drop table X_m_to_m_uni if exists;
drop table X_m_to_m_uni_Y_m_to_m_uni if exists;
drop table Y_1_to_1_bi if exists;
drop table Y_1_to_1_ind if exists;
drop table Y_1_to_1_uni if exists;
drop table Y_1_to_m_bi if exists;
drop table Y_1_to_m_join if exists;
drop table Y_1_to_m_mt1 if exists;
drop table Y_1_to_m_uni if exists;
drop table Y_m_to_m_bi if exists;
drop table Y_m_to_m_uni if exists;
drop sequence if exists _sequence;

create sequence hibernate_sequence start with 1 increment by 1;

-- 1-to-1 Unidirectional
create table X_1_to_1_uni (id bigint not null, y_id bigint, primary key (id));
create table Y_1_to_1_uni (id bigint not null, primary key (id));
alter table X_1_to_1_uni add constraint FKll5a7lb6ocn97omrbtl6ysohv foreign key (y_id) references Y_1_to_1_uni;

-- 1-to-1 Bidirectional
-- same as Unidirectional
create table X_1_to_1_bi (id bigint not null, y_id bigint, primary key (id));
create table Y_1_to_1_bi (id bigint not null, primary key (id));
alter table X_1_to_1_bi add constraint FK8y8noysw7y2xaht2g37ipqg8s foreign key (y_id) references Y_1_to_1_bi;

-- 1-to-1 Independent
-- extra FK in Y
create table X_1_to_1_ind (id bigint not null, y_id bigint, primary key (id));
create table Y_1_to_1_ind (id bigint not null, x_id bigint, primary key (id));
alter table X_1_to_1_ind add constraint FKe4mwrmqu6oq904yt2tornhu6m foreign key (y_id) references Y_1_to_1_ind;
alter table Y_1_to_1_ind add constraint FK7f5nidfupb5g4glllgxyeglpo foreign key (x_id) references X_1_to_1_ind;

-- 1-to-Many Unidirectional
-- need intermediary table
-- note the "unique" constraint on ys_id. Otherwise, it would map a Many-to-Many
create table X_1_to_m_uni (id bigint not null, primary key (id));
create table X_1_to_m_uni_Y_1_to_m_uni (X_1_to_m_uni_id bigint not null, ys_id bigint not null);
create table Y_1_to_m_uni (id bigint not null, primary key (id));
alter table X_1_to_m_uni_Y_1_to_m_uni add constraint UK_dwihwd2hfyh6nje42ph943aet unique (ys_id);
alter table X_1_to_m_uni_Y_1_to_m_uni add constraint FKd4le3pnc8n7r282soler4jklg foreign key (ys_id) references Y_1_to_m_uni;
alter table X_1_to_m_uni_Y_1_to_m_uni add constraint FKl8mcge4rvb3wbibxnrlwrd7pm foreign key (X_1_to_m_uni_id) references X_1_to_m_uni;

-- Many-to-1
create table X_1_to_m_mt1 (id bigint not null, primary key (id));
create table Y_1_to_m_mt1 (id bigint not null, x_id bigint, primary key (id));
alter table Y_1_to_m_mt1 add constraint FKf9cyw7dl83sdkv8u9l03g8wk1 foreign key (x_id) references X_1_to_m_mt1;

-- 1-to-Many Bidirectional
-- note that is the same as Many-to-1
create table X_1_to_m_bi (id bigint not null, primary key (id));
create table Y_1_to_m_bi (id bigint not null, x_id bigint, primary key (id));
alter table Y_1_to_m_bi add constraint FKo6v1dg33lgn6ftd3fk4d0qfvn foreign key (x_id) references X_1_to_m_bi;

-- 1-to-Many JoinColumn
-- note that is the same as Many-to-1
create table X_1_to_m_join (id bigint not null, primary key (id));
create table Y_1_to_m_join (id bigint not null, foo_column bigint, primary key (id));
alter table Y_1_to_m_join add constraint FK8yf9hp542livuas8ixabmstkg foreign key (foo_column) references X_1_to_m_join;


-- Many-to-Many Unidirectional
-- similar to 1-to-Many Unidirectional, but without unique constraint
create table X_m_to_m_uni (id bigint not null, primary key (id));
create table X_m_to_m_uni_Y_m_to_m_uni (X_m_to_m_uni_id bigint not null, ys_id bigint not null);
create table Y_m_to_m_uni (id bigint not null, primary key (id));
alter table X_m_to_m_uni_Y_m_to_m_uni add constraint FK30rjwydlw47ttlxnx6yo78vb6 foreign key (ys_id) references Y_m_to_m_uni;
alter table X_m_to_m_uni_Y_m_to_m_uni add constraint FKb4jno5g9l8uqc9ppr17rkkpj4 foreign key (X_m_to_m_uni_id) references X_m_to_m_uni;

-- Many-to-Many Bidirectional
-- same as Unidirectional
create table X_m_to_m_bi (id bigint not null, primary key (id));
create table X_m_to_m_bi_Y_m_to_m_bi (xs_id bigint not null, ys_id bigint not null);
create table Y_m_to_m_bi (id bigint not null, primary key (id));
alter table X_m_to_m_bi_Y_m_to_m_bi add constraint FKfwk9iwl2apy6syb1549ka97n foreign key (ys_id) references Y_m_to_m_bi;
alter table X_m_to_m_bi_Y_m_to_m_bi add constraint FKp6d3wospr7mfw33r3rtqmelo1 foreign key (xs_id) references X_m_to_m_bi;



