create table file_metadatas
(
    id                 bigint auto_increment
        primary key,
    created_at         datetime(6)  not null,
    last_modified_at   datetime(6)  not null,
    member_id          bigint       not null,
    original_file_name varchar(255) not null,
    status             varchar(32)  not null,
    vendor             varchar(32)  null
);

create index idx_file_matadatas__created_at
    on file_metadatas (created_at);

create index idx_file_matadatas__member_id
    on file_metadatas (member_id);
