alter table members
    add email varchar(255) null;

create index idx_members__email
    on members (email);

create table idempotency_keys
(
    id               bigint auto_increment primary key,
    created_at       datetime(6) not null,
    last_modified_at datetime(6) not null,
    domain_id        bigint      not null,
    domain_type      varchar(50) not null,
    idempotency_key  varchar(50) not null
);

create index idempotency_keys__domain_id_domain_type_created_at
    on idempotency_keys (domain_id, domain_type, idempotency_key);


