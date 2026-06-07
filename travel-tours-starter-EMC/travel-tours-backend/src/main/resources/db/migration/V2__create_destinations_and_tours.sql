create table destinations (
    id bigserial primary key,
    name varchar(255) not null,
    description text,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table tours (
    id bigserial primary key,
    title varchar(255) not null,
    description text,
    price numeric(19, 2),
    capacity integer,
    start_date date,
    end_date date,
    destination_id bigint references destinations(id) on delete set null,
    created_at timestamp not null,
    updated_at timestamp not null
);
