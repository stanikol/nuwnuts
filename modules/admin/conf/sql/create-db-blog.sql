drop table if exists blog;

create table if not exists blog(
    blog_id         serial,
    filename        varchar,
    title           varchar,
    html            varchar,
    keywords        varchar[],
    sort_order      varchar,
    last_update     timestamp,
    display         bool
);