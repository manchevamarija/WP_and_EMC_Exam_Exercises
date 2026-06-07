create or replace view course_revenue as
select c.id as course_id,
       c.title,
       c.price,
       count(e.id) as enrollments,
       c.price * count(e.id) as revenue
from courses c
         left join enrollments e on e.course_id = c.id
group by c.id, c.title, c.price;