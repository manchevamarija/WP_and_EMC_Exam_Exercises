package mk.ukim.finki.traveltoursbackend.service.domain.impl;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.repository.TourRepository;
import mk.ukim.finki.traveltoursbackend.service.domain.TourService;
import org.springframework.stereotype.Service;

@Service
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;

    public TourServiceImpl(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    @Override
    public List<Tour> findAll() {
        return tourRepository.findAll();
    }

    @Override
    public Optional<Tour> findById(Long id) {
        return tourRepository.findById(id);
    }

    @Override
    public Optional<Tour> findWithLockById(Long id) {
        return tourRepository.findWithLockById(id);
    }

    @Override
    public Tour create(Tour tour) {
        return tourRepository.save(tour);
    }

    @Override
    public Optional<Tour> update(Long id, Tour tour) {
        return tourRepository
                .findById(id)
                .map((existingTour) -> {
                    existingTour.setTitle(tour.getTitle());
                    existingTour.setDescription(tour.getDescription());
                    existingTour.setDestination(tour.getDestination());
                    existingTour.setPrice(tour.getPrice());
                    existingTour.setCapacity(tour.getCapacity());
                    existingTour.setSchedule(tour.getSchedule());
                    return tourRepository.save(existingTour);
                });

    }

    @Override
    public Optional<Tour> deleteById(Long id) {
        Optional<Tour> tour = tourRepository.findById(id);
        tour.ifPresent(tourRepository::delete);
        return tour;
    }
}
