package mk.ukim.finki.onlinecoursesbackend.service.domain.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import mk.ukim.finki.onlinecoursesbackend.model.exception.CourseNotFoundException;
import mk.ukim.finki.onlinecoursesbackend.repository.CourseRepository;
import mk.ukim.finki.onlinecoursesbackend.repository.EnrollmentRepository;
import mk.ukim.finki.onlinecoursesbackend.service.domain.CourseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseServiceImpl(CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> findAllByAvailability(Boolean available) {
        return findAll()
                .stream()
                .filter(course -> {
                    int enrollments = enrollmentRepository.countByCourse(course);
                    int capacity = course.getCapacity();
                    return available ? enrollments < capacity : enrollments == capacity;
                })
                .toList();
    }


    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Optional<Course> findWithLockById(Long id) {
        return courseRepository.findWithLockById(id);
    }

    @Override
    public Course create(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> update(Long id, Course course) {
        return courseRepository
                .findById(id)
                .map((existingCourse) -> {
                    existingCourse.setTitle(course.getTitle());
                    existingCourse.setDescription(course.getDescription());
                    existingCourse.setPrice(course.getPrice());
                    existingCourse.setCapacity(course.getCapacity());
                    existingCourse.setTopic(course.getTopic());
                    if (course.getSchedule() !=null) {
                        existingCourse.setSchedule(course.getSchedule());
                    }
                    return courseRepository.save(existingCourse);
                });
    }

    @Override
    public Optional<Course> deleteById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        course.ifPresent(courseRepository::delete);
        return course;
    }
    
}
