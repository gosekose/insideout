alter table member
    add email varchar(255) null;

create index idx_member__email
    on member (email);