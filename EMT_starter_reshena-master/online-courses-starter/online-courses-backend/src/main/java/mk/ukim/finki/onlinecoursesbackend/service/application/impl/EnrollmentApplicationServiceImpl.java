package mk.ukim.finki.onlinecoursesbackend.service.application.impl;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayEnrollmentDetailsResponseDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayEnrollmentResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Course;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Enrollment;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;
import mk.ukim.finki.onlinecoursesbackend.model.enumeration.Role;
import mk.ukim.finki.onlinecoursesbackend.model.event.UserEnrolledInCourseEvent;
import mk.ukim.finki.onlinecoursesbackend.model.exception.CourseCapacityIsFullException;
import mk.ukim.finki.onlinecoursesbackend.model.exception.CourseNotFoundException;
import mk.ukim.finki.onlinecoursesbackend.repository.EnrollmentRepository;
import mk.ukim.finki.onlinecoursesbackend.service.application.EnrollmentApplicationService;
import mk.ukim.finki.onlinecoursesbackend.service.domain.CourseService;
import mk.ukim.finki.onlinecoursesbackend.service.domain.EnrollmentService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollmentApplicationServiceImpl implements EnrollmentApplicationService {
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentApplicationServiceImpl(
        CourseService courseService,
        EnrollmentService enrollmentService,
        ApplicationEventPublisher applicationEventPublisher,
        EnrollmentRepository enrollmentRepository) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<DisplayEnrollmentDetailsResponseDto> findAllByUserWithDetails(User user) {
        List<Enrollment> enrollments = user.getRole() == Role.ROLE_STUDENT
            ? enrollmentService.findAllByUser(user)
            : enrollmentService.findAll();
        return DisplayEnrollmentDetailsResponseDto.from(enrollments);
    }

    @Override
    public Boolean existsByCourseAndUser(Long courseId, User user) {
        Course course = courseService.findById(courseId).orElseThrow(()-> new CourseNotFoundException(courseId));
        return enrollmentService.existsByCourseAndUser(course,user);
    }

    @Override
    @Transactional

    public Integer getAvailableSpotsByCourse(Long courseId) {
        Course course = courseService.findById(courseId).orElseThrow(()-> new CourseNotFoundException(courseId));
        return enrollmentService.getAvailableSpotsByCourse(course);
    }

    @Override
    @Transactional

    public DisplayEnrollmentResponseDto enrollByCourseAndUser(Long courseId, User user) {
        Course course = courseService.findWithLockById(courseId).orElseThrow(()-> new CourseNotFoundException(courseId));
      Enrollment enrollment = enrollmentService.enrollByCourseAndUser(course,user);
      applicationEventPublisher.publishEvent(new UserEnrolledInCourseEvent(user,course.getId(), course.getTitle()));

      return DisplayEnrollmentResponseDto.from(enrollment);
    }

    @Override
    @Transactional
    public DisplayEnrollmentResponseDto unenrollByCourseAndUser(Long courseId, User user) {
        Course course = courseService.findWithLockById(courseId).orElseThrow(()-> new CourseNotFoundException(courseId));
        Enrollment enrollment = enrollmentService.unenrollByCourseAndUser(course,user);
        return DisplayEnrollmentResponseDto.from(enrollment);
    }
}
