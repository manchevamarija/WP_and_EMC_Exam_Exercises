package mk.ukim.finki.onlinecoursesbackend.service.domain.impl;

import java.util.List;
import java.util.Optional;

import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Enrollment;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;
import mk.ukim.finki.onlinecoursesbackend.model.exception.CourseCapacityIsFullException;
import mk.ukim.finki.onlinecoursesbackend.model.exception.UserIsAlreadyEnrolledInCourseException;
import mk.ukim.finki.onlinecoursesbackend.model.exception.UserIsNotEnrolledInCourseException;
import mk.ukim.finki.onlinecoursesbackend.repository.EnrollmentRepository;
import mk.ukim.finki.onlinecoursesbackend.service.domain.EnrollmentService;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    @Override
    public List<Enrollment> findAllByUser(User user) {
        return enrollmentRepository.findAllByUser(user);
    }

    @Override
    public Boolean existsByCourseAndUser(Course course, User user) {
        return enrollmentRepository.findByCourseAndUser(course, user).isPresent();
    }

    @Override
    public Integer getAvailableSpotsByCourse(Course course) {
       return course.getCapacity() - enrollmentRepository.countByCourse(course);
       }

    @Override
    public Enrollment enrollByCourseAndUser(Course course, User user) {
       if (existsByCourseAndUser(course, user)) {
           throw new UserIsAlreadyEnrolledInCourseException(course.getId() , user.getName());
       }
       if (getAvailableSpotsByCourse(course) == 0) {
           throw new CourseCapacityIsFullException(course.getId());
       }
       Enrollment enrollment = new Enrollment(course, user);
       return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment unenrollByCourseAndUser(Course course, User user) {
        Optional<Enrollment> enrollment = enrollmentRepository.findByCourseAndUser(course, user);
        if (enrollment.isEmpty()){
            throw new UserIsNotEnrolledInCourseException(course.getId() , user.getName());
        }
        enrollmentRepository.delete(enrollment.get());
        return enrollment.get();
    }
}
