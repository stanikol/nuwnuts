drop table if exists img;

create table img(
    filename    varchar primary key not null,
    bytes       bytea not null
);

drop table if exists img_meta;

create table img_meta(
    filename    varchar primary key not null,
    alt         varchar,
    album       varchar,
    title       varchar,
    description varchar
);
