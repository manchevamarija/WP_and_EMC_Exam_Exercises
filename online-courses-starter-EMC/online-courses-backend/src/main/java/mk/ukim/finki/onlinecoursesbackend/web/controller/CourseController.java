package mk.ukim.finki.onlinecoursesbackend.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.CreateOrUpdateCourseRequestDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayCourseDetailsResponseDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayCourseResponseDto;
import mk.ukim.finki.onlinecoursesbackend.service.application.CourseApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/courses")
@RestController
public class CourseController {
    private final CourseApplicationService courseApplicationService;

    public CourseController(CourseApplicationService courseApplicationService) {
        this.courseApplicationService = courseApplicationService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    public ResponseEntity<List<DisplayCourseResponseDto>> findAll(@RequestParam(required = false) Boolean available) {
        if (available == null) {
            return ResponseEntity.ok(courseApplicationService.findAll());
        }
        return ResponseEntity.ok(courseApplicationService.findAllByAvailability(available));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<DisplayCourseResponseDto> findById(@PathVariable Long id) {
        return courseApplicationService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{id}/details")
    public ResponseEntity<DisplayCourseDetailsResponseDto> findByIdWithDetails(@PathVariable Long id) {
        return courseApplicationService
                .findByIdWithDetails(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/add")
    public ResponseEntity<DisplayCourseResponseDto> create(
        @RequestBody @Valid CreateOrUpdateCourseRequestDto createCourseRequestDto
    ) {
        return ResponseEntity.ok(courseApplicationService.create(createCourseRequestDto));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}/edit")
    public ResponseEntity<DisplayCourseResponseDto> update(
        @PathVariable Long id,
        @RequestBody @Valid CreateOrUpdateCourseRequestDto updateCourseRequestDto
    ) {
        return courseApplicationService
                .update(id, updateCourseRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<DisplayCourseResponseDto> deleteById(@PathVariable Long id) {
        return courseApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

