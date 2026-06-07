package mk.ukim.finki.traveltoursbackend.service.domain.impl;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.model.domain.Reservation;
import mk.ukim.finki.traveltoursbackend.model.domain.User;
import mk.ukim.finki.traveltoursbackend.model.exception.TourCapacityIsFullException;
import mk.ukim.finki.traveltoursbackend.model.exception.TourNotFoundException;
import mk.ukim.finki.traveltoursbackend.model.exception.UserHasAlreadyReservedTourException;
import mk.ukim.finki.traveltoursbackend.model.exception.UserHasNotReservedTourException;
import mk.ukim.finki.traveltoursbackend.repository.ReservationRepository;
import mk.ukim.finki.traveltoursbackend.service.domain.ReservationService;
import mk.ukim.finki.traveltoursbackend.service.domain.TourService;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final TourService tourService;

    public ReservationServiceImpl(ReservationRepository reservationRepository, TourService tourService) {
        this.reservationRepository = reservationRepository;
        this.tourService = tourService;
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> findAllByUser(User user) {
        return reservationRepository.findAllByUser(user);
    }

    @Override
    public Boolean existsByTourAndUser(Tour tour, User user) {
        return reservationRepository.existsByTourAndUser(tour, user);
    }

    @Override
    public Integer getAvailableSpotsByTour(Tour tour) {
        return tour.getCapacity() - reservationRepository.countByTour(tour);
    }

    @Override
    public Reservation reserveByTourAndUser(Tour tour, User user) {
        Tour tour1 = tourService.findWithLockById(tour.getId()).orElseThrow(()-> new TourNotFoundException(tour.getId()));

        if (reservationRepository.countByTour(tour1) >= tour1.getCapacity()){
            throw new TourCapacityIsFullException(tour.getId());
        }

        Reservation reservation = new Reservation(tour1,user);
        reservationRepository.save(reservation);
        return reservation;
    }

    @Override
    public Reservation cancelByTourAndUser(Tour tour, User user) {
        Tour lockedTour = tourService.findWithLockById(tour.getId())
                .orElseThrow(() -> new TourNotFoundException(tour.getId()));

        Reservation reservation = reservationRepository
                .findByTourAndUser(lockedTour, user)
                .orElseThrow(() -> new UserHasNotReservedTourException(lockedTour.getId(), user.getUsername()));

        reservationRepository.delete(reservation);


        return reservation;
    }
}
