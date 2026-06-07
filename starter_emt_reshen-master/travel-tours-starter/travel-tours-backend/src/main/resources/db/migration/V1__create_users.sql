create table users (
    id bigserial primary key,
    name varchar(255),
    surname varchar(255),
    email varchar(255) not null unique,
    username varchar(255) not null unique,
    password varchar(255),
    role varchar(255),
    created_at timestamp not null,
    updated_at timestamp not null
);
