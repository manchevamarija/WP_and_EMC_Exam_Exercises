package mk.ukim.finki.traveltoursbackend.service.application.impl;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayReservationDetailsResponseDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayReservationResponseDto;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.model.domain.Reservation;
import mk.ukim.finki.traveltoursbackend.model.domain.User;
import mk.ukim.finki.traveltoursbackend.model.enumeration.Role;
import mk.ukim.finki.traveltoursbackend.model.event.UserReservedTourEvent;
import mk.ukim.finki.traveltoursbackend.model.exception.TourCapacityIsFullException;
import mk.ukim.finki.traveltoursbackend.model.exception.TourNotFoundException;
import mk.ukim.finki.traveltoursbackend.model.exception.UserHasAlreadyReservedTourException;
import mk.ukim.finki.traveltoursbackend.model.exception.UserHasNotReservedTourException;
import mk.ukim.finki.traveltoursbackend.service.application.ReservationApplicationService;
import mk.ukim.finki.traveltoursbackend.service.domain.TourService;
import mk.ukim.finki.traveltoursbackend.service.domain.ReservationService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationApplicationServiceImpl implements ReservationApplicationService {
    private final TourService tourService;
    private final ReservationService reservationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ReservationApplicationServiceImpl(
        TourService tourService,
        ReservationService reservationService,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        this.tourService = tourService;
        this.reservationService = reservationService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public List<DisplayReservationDetailsResponseDto> findAllByUserWithDetails(User user) {
        List<Reservation> reservations = user.getRole() == Role.ROLE_CUSTOMER
            ? reservationService.findAllByUser(user)
            : reservationService.findAll();
        return DisplayReservationDetailsResponseDto.from(reservations);
    }

    @Override
    public Boolean existsByTourAndUser(Long tourId, User user) {
        Tour tour = tourService
            .findById(tourId)
            .orElseThrow(() -> new TourNotFoundException(tourId));
        return reservationService.existsByTourAndUser(tour, user);
    }

    @Override
    public Integer getAvailableSpotsByTour(Long tourId) {
        Tour tour = tourService
            .findById(tourId)
            .orElseThrow(() -> new TourNotFoundException(tourId));
        return reservationService.getAvailableSpotsByTour(tour);
    }

    @Transactional
    @Override
    public DisplayReservationResponseDto reserveByTourAndUser(Long tourId, User user) {
        if (existsByTourAndUser(tourId,user)) {
            throw new UserHasAlreadyReservedTourException(tourId, user.getUsername());
        }
        if (getAvailableSpotsByTour(tourId)==0){
            throw new TourCapacityIsFullException(tourId);
        }
        Tour tour = tourService
                .findById(tourId)
                .orElseThrow(() -> new TourNotFoundException(tourId));

        applicationEventPublisher.publishEvent(new UserReservedTourEvent(
                user,
                tour.getId(),
                tour.getTitle()
        ));
        return DisplayReservationResponseDto.from(
                reservationService.reserveByTourAndUser(tour,user));
    }

    @Transactional
    @Override
    public DisplayReservationResponseDto cancelByTourAndUser(Long tourId, User user) {
        Tour tour = tourService
                .findById(tourId)
                .orElseThrow(() -> new TourNotFoundException(tourId));

        return DisplayReservationResponseDto.from(
                reservationService.cancelByTourAndUser(tour,user));
    }
}
