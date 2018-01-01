drop table if exists user_db;

create table if not exists user_db(
    user_id             UUID,
    provider_id         varchar not null,
    provider_key        varchar not null,
    first_name          varchar,
    last_name           varchar,
    full_name           varchar,
    email               varchar unique,
    avatar_url          varchar,
    activated           bool not null,
    roles               varchar[] default null
);

drop table if exists auth_token_db;

create table if not exists auth_token_db(
    id              UUID not null,
    user_id         UUID not null,
--    expiry          varchar not null
    expiry          timestamp

);
