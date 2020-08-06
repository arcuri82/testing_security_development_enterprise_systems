
create table user_stats (
    user_id varchar(255) not null,
    defeats integer not null check (defeats>=0),
    draws integer not null check (draws>=0),
    score integer not null check (score>=0),
    victories integer not null check (victories>=0),
    primary key (user_id));
