package mk.ukim.finki.onlinecoursesbackend.web.controller;

import java.util.List;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.CreateOrUpdateTopicRequestDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayCourseResponseDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayTopicResponseDto;
import mk.ukim.finki.onlinecoursesbackend.service.application.TopicApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/topics")
@RestController
public class TopicController {
    private final TopicApplicationService topicApplicationService;

    public TopicController(TopicApplicationService topicApplicationService) {
        this.topicApplicationService = topicApplicationService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    public ResponseEntity<List<DisplayTopicResponseDto>> findAll() {
        return ResponseEntity.ok(topicApplicationService.findAll());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<DisplayTopicResponseDto> findById(@PathVariable Long id) {
        return topicApplicationService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/add")
    public ResponseEntity<DisplayTopicResponseDto> create(
        @RequestBody CreateOrUpdateTopicRequestDto createTopicRequestDto
    ) {
        return ResponseEntity.ok(topicApplicationService.create(createTopicRequestDto));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}/edit")
    public ResponseEntity<DisplayTopicResponseDto> update(
        @PathVariable Long id,
        @RequestBody CreateOrUpdateTopicRequestDto updateTopicRequestDto
    ) {
        return topicApplicationService
            .update(id, updateTopicRequestDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<DisplayTopicResponseDto> deleteById(@PathVariable Long id) {
        return topicApplicationService
            .deleteById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
