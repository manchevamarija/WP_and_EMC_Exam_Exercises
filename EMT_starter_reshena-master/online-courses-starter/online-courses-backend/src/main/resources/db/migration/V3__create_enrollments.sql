create table enrollments (
    id bigserial primary key,
    course_id bigint not null references courses(id) on delete cascade,
    user_id BIGINT not null references users(id) on delete cascade,
    created_at timestamp not null,
    updated_at timestamp not null,
    unique (course_id, user_id)
);
