create table notifications (
    id bigserial primary key,
    user_id bigint not null references users(id) on delete cascade,
    message varchar(1023) not null,
    created_at timestamp not null,
    updated_at timestamp not null
);