drop table if exists item_category cascade;

create table if not exists item_category(
      id         serial primary key not null,
      category   varchar unique,
      sort_order varchar
);

drop table if exists item;

create table if not exists item(
        id          serial primary key,
        category_id    int not null references item_category(id),
        title       varchar,
        img         varchar,
        html        varchar,
        price       money,
        is_published bool,
        sort_order  varchar,
        keywords    varchar[]
);

create view item_info as
    select i.*, c.category, c.sort_order as category_sort_order
        from item i inner join item_category c on c.id = i.category_id;