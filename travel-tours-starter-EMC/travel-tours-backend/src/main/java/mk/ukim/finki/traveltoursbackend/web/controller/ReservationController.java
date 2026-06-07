package mk.ukim.finki.traveltoursbackend.web.controller;

import java.security.Principal;
import java.util.List;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayReservationDetailsResponseDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayReservationResponseDto;
import mk.ukim.finki.traveltoursbackend.model.domain.User;
import mk.ukim.finki.traveltoursbackend.service.application.ReservationApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/reservations")
@RestController
public class ReservationController {
    private final ReservationApplicationService reservationApplicationService;

    public ReservationController(ReservationApplicationService reservationApplicationService) {
        this.reservationApplicationService = reservationApplicationService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<DisplayReservationDetailsResponseDto>> findAllWithDetailsByUser(
        @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(reservationApplicationService.findAllByUserWithDetails(user));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/tour/{tourId}/is-reserved")
    public ResponseEntity<Boolean> existsByTourAndUser(
        @PathVariable Long tourId,
        @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(reservationApplicationService.existsByTourAndUser(tourId, user));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/tour/{tourId}/available-spots")
    public ResponseEntity<Integer> getAvailableSpotsByTour(
        @PathVariable Long tourId
    ) {
        return ResponseEntity.ok(reservationApplicationService.getAvailableSpotsByTour(tourId));
    }

    @PreAuthorize("principal.role.name() == 'ROLE_CUSTOMER'")
    @PostMapping("/tour/{tourId}/reserve")
    public ResponseEntity<DisplayReservationResponseDto> reserveByTour(
        @PathVariable Long tourId,
        @AuthenticationPrincipal User user,
        Principal principal) {
        return ResponseEntity.ok(reservationApplicationService.reserveByTourAndUser(tourId, user));
    }

    @PreAuthorize("principal.role.name() == 'ROLE_CUSTOMER'")
    @DeleteMapping("/tour/{tourId}/cancel")
    public ResponseEntity<DisplayReservationResponseDto> cancelByTour(
        @PathVariable Long tourId,
        @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(reservationApplicationService.cancelByTourAndUser(tourId, user));
    }
}
