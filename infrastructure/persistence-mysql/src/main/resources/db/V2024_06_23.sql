create table feeling
(
    id                           bigint auto_increment
        primary key,
    created_at                   datetime(6) not null,
    last_modified_at             datetime(6) not null,
    member_id                    bigint      not null,
    memory_marble_connect_status varchar(32) not null,
    memory_marble_id             bigint null,
    score                        bigint      not null,
    status                       varchar(32) not null,
    type                         varchar(64) not null
);

create index idx_feeling__created_at
    on feeling (created_at);

create index idx_feeling__member_id
    on feeling (member_id);

create index idx_feeling__memory_marble_id
    on feeling (memory_marble_id);

----

create table member
(
    id               bigint auto_increment
        primary key,
    created_at       datetime(6) not null,
    last_modified_at datetime(6) not null,
    version          varchar(32) not null
);

create index idx_member__created_at
    on member (created_at);


----

create table memory_marble
(
    id               bigint auto_increment
        primary key,
    created_at       datetime(6) not null,
    last_modified_at datetime(6) not null,
    description      text null,
    feeling_ids      varchar(255) not null,
    member_id        bigint       not null,
    status           varchar(32)  not null,
    store_type       varchar(32)  not null
);

create index idx_memory_marble__created_at
    on memory_marble (created_at);

create index idx_memory_marble__member_id_store_type
    on memory_marble (member_id, store_type);