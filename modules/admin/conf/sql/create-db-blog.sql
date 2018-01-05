drop table if exists blog;

create table if not exists blog(
    blog_id         serial primary key,
    filename        varchar,
    title           varchar,
    html            varchar,
    short_text      varchar,
    keywords        varchar[],
    sort_order      varchar,
    last_update     timestamp,
    created         timestamp,
    is_published    bool
);

alter sequence blog_blog_id_seq start with 100;