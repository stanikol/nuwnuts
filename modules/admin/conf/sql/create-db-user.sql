drop table if exists user_db;

create table if not exists user_db(
    user_id             UUID primary key,
    provider_id         varchar not null,
    provider_key        varchar not null,
    first_name          varchar,
    last_name           varchar,
    full_name           varchar,
    email               varchar unique,
    avatar_url          varchar,
    activated           bool not null,
    roles               varchar[] default null,
    unique(provider_id, provider_key)
);

drop table if exists auth_token_db;

create table if not exists auth_token_db(
    id              UUID not null primary key,
    user_id         UUID not null unique,
--    expiry          varchar not null
    expiry          timestamp

);

drop table if exists credential;

create table if not exists credential(
    provider_id         varchar not null,
    provider_key        varchar not null,
    hasher              varchar not null,
    password            varchar not null,
    salt                varchar,
    unique(provider_id, provider_key)
);