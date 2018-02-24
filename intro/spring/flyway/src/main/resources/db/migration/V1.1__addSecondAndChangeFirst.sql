create table Second (
  id numeric,
  data  varchar(50),
  constraint pk_Second primary key (id)
);

ALTER TABLE First ADD added_field INTEGER;