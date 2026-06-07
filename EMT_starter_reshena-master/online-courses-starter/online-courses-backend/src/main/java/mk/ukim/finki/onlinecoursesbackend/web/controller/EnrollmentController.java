package mk.ukim.finki.onlinecoursesbackend.web.controller;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayEnrollmentDetailsResponseDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayEnrollmentResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.domain.User;
import mk.ukim.finki.onlinecoursesbackend.service.application.EnrollmentApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/enrollments")
@RestController
public class EnrollmentController {
    private final EnrollmentApplicationService enrollmentApplicationService;

    public EnrollmentController(EnrollmentApplicationService enrollmentApplicationService) {
        this.enrollmentApplicationService = enrollmentApplicationService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    public ResponseEntity<List<DisplayEnrollmentDetailsResponseDto>> findAllWithDetailsByUser(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(enrollmentApplicationService.findAllByUserWithDetails(user));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/course/{courseId}/is-enrolled")
    public ResponseEntity<Boolean> existsByCourseAndUser(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(enrollmentApplicationService.existsByCourseAndUser(courseId,user));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/course/{courseId}/available-spots")
    public ResponseEntity<Integer> getAvailableSpotsByCourse(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(enrollmentApplicationService.getAvailableSpotsByCourse(courseId));

    }

    @PreAuthorize("principal.role.name() == 'ROLE_STUDENT'")
    @PostMapping("/course/{courseId}/enroll")
    public ResponseEntity<DisplayEnrollmentResponseDto> enrollByCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(enrollmentApplicationService.enrollByCourseAndUser(courseId, user));
    }

    @PreAuthorize("principal.role.name() == 'ROLE_STUDENT'")
    @DeleteMapping("/course/{courseId}/unenroll")
    public ResponseEntity<DisplayEnrollmentResponseDto> unenrollByCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(enrollmentApplicationService.unenrollByCourseAndUser(courseId, user));

    }
}
