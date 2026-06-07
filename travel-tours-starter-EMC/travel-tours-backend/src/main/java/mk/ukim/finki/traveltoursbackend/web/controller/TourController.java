package mk.ukim.finki.traveltoursbackend.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import mk.ukim.finki.traveltoursbackend.dto.domain.CreateOrUpdateTourRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayTourDetailsResponseDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayTourResponseDto;
import mk.ukim.finki.traveltoursbackend.service.application.TourApplicationService;
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

@RequestMapping("/api/tours")
@RestController
public class TourController {
    private final TourApplicationService tourApplicationService;

    public TourController(TourApplicationService tourApplicationService) {
        this.tourApplicationService = tourApplicationService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<DisplayTourResponseDto>> findAll(@RequestParam(required = false) Boolean available) {
        return ResponseEntity.ok(tourApplicationService.findAll());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<DisplayTourResponseDto> findById(@PathVariable Long id) {
        return tourApplicationService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{id}/details")
    public ResponseEntity<DisplayTourDetailsResponseDto> findByIdWithDetails(@PathVariable Long id) {
        return tourApplicationService
                .findByIdWithDetails(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/add")
    public ResponseEntity<DisplayTourResponseDto> create(
        @RequestBody @Valid CreateOrUpdateTourRequestDto createTourRequestDto
    ) {
        return ResponseEntity.ok(tourApplicationService.create(createTourRequestDto));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}/edit")
    public ResponseEntity<DisplayTourResponseDto> update(
        @PathVariable Long id,
        @RequestBody @Valid CreateOrUpdateTourRequestDto updateTourRequestDto
    ) {
        return tourApplicationService
                .update(id, updateTourRequestDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<DisplayTourResponseDto> deleteById(@PathVariable Long id) {
        return tourApplicationService
                .deleteById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
