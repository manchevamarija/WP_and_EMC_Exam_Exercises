create table reservations (
    id bigserial primary key,
    tour_id bigint not null references tours(id) on delete cascade,
    user_id BIGINT not null references users(id) on delete cascade,
    created_at timestamp not null,
    updated_at timestamp not null,
    unique (tour_id, user_id)
);
