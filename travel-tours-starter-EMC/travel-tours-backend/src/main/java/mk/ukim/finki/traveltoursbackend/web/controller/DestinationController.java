package mk.ukim.finki.traveltoursbackend.web.controller;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.dto.domain.CreateOrUpdateDestinationRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayTourResponseDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayDestinationResponseDto;
import mk.ukim.finki.traveltoursbackend.service.application.DestinationApplicationService;
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

@RequestMapping("/api/destinations")
@RestController
public class DestinationController {
    private final DestinationApplicationService destinationApplicationService;

    public DestinationController(DestinationApplicationService destinationApplicationService) {
        this.destinationApplicationService = destinationApplicationService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<DisplayDestinationResponseDto>> findAll() {
        return ResponseEntity.ok(destinationApplicationService.findAll());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<DisplayDestinationResponseDto> findById(@PathVariable Long id) {
        return destinationApplicationService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/add")
    public ResponseEntity<DisplayDestinationResponseDto> create(
        @RequestBody CreateOrUpdateDestinationRequestDto createDestinationRequestDto
    ) {
        return ResponseEntity.ok(destinationApplicationService.create(createDestinationRequestDto));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}/edit")
    public ResponseEntity<DisplayDestinationResponseDto> update(
        @PathVariable Long id,
        @RequestBody CreateOrUpdateDestinationRequestDto updateDestinationRequestDto
    ) {
        return destinationApplicationService
            .update(id, updateDestinationRequestDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<DisplayDestinationResponseDto> deleteById(@PathVariable Long id) {
        return destinationApplicationService
            .deleteById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
