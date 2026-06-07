package mk.ukim.finki.traveltoursbackend.service.application.impl;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.dto.domain.CreateOrUpdateTourRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayTourDetailsResponseDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayTourResponseDto;
import mk.ukim.finki.traveltoursbackend.model.domain.Destination;
import mk.ukim.finki.traveltoursbackend.model.exception.DestinationNotFoundException;
import mk.ukim.finki.traveltoursbackend.service.application.TourApplicationService;
import mk.ukim.finki.traveltoursbackend.service.domain.TourService;
import mk.ukim.finki.traveltoursbackend.service.domain.DestinationService;
import org.springframework.stereotype.Service;

@Service
public class TourApplicationServiceImpl implements TourApplicationService {
    private final DestinationService destinationService;
    private final TourService tourService;

    public TourApplicationServiceImpl(DestinationService destinationService, TourService tourService) {
        this.destinationService = destinationService;
        this.tourService = tourService;
    }

    @Override
    public List<DisplayTourResponseDto> findAll() {
        return DisplayTourResponseDto.from(tourService.findAll());
    }

    @Override
    public Optional<DisplayTourResponseDto> findById(Long id) {
        return tourService
            .findById(id)
            .map(DisplayTourResponseDto::from);
    }

    @Override
    public Optional<DisplayTourDetailsResponseDto> findByIdWithDetails(Long id) {
        return tourService
                .findById(id)
                .map(DisplayTourDetailsResponseDto::from);
    }

    @Override
    public DisplayTourResponseDto create(CreateOrUpdateTourRequestDto createTourRequestDto) {
        Destination destination = destinationService.findById(createTourRequestDto.destinationId()).orElseThrow(() -> new DestinationNotFoundException(createTourRequestDto.destinationId()));
        return DisplayTourResponseDto.from(tourService.create(createTourRequestDto.toTour(destination)));
    }

    @Override
    public Optional<DisplayTourResponseDto> update(Long id, CreateOrUpdateTourRequestDto updateTourRequestDto) {
        Destination destination = destinationService.findById(updateTourRequestDto.destinationId()).orElseThrow(() -> new DestinationNotFoundException(updateTourRequestDto.destinationId()));
        return tourService
                .update(id, updateTourRequestDto.toTour(destination))
                .map(DisplayTourResponseDto::from);
    }

    @Override
    public Optional<DisplayTourResponseDto> deleteById(Long id) {
        return tourService
                .deleteById(id)
                .map(DisplayTourResponseDto::from);
    }
}
