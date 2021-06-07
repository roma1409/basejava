create table if not exists resume
(
    uuid      char(36) not null
        constraint resume_pk
            primary key,
    full_name text     not null
);

create table if not exists contact
(
    id          serial   not null
        constraint contact_pk primary key,
    resume_uuid char(36) not null
        constraint contact_resume_uuid_fk
            references resume
            on update restrict on delete cascade,
    type        text     not null,
    value       text     not null
);

create table if not exists section
(
    id          serial
        constraint section_pk primary key,
    type        text not null,
    value       text not null,
    resume_uuid char(36)
        constraint section_resume_uuid_fk
            references resume
            on update restrict on delete cascade
);

create unique index if not exists contact_uuid_type_index on contact (resume_uuid, type);
create unique index if not exists section_resume_uuid_index on section (resume_uuid, type);
