create table topics (
    id bigserial primary key,
    name varchar(255) not null,
    description text,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table courses (
    id bigserial primary key,
    title varchar(255) not null,
    description text,
    price numeric(19, 2),
    capacity integer,
    start_date date,
    end_date date,
    topic_id bigint references topics(id) on delete set null,
    created_at timestamp not null,
    updated_at timestamp not null
);
