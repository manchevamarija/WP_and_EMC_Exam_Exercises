package mk.ukim.finki.traveltoursbackend.service.application.impl;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.dto.domain.CreateOrUpdateDestinationRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayDestinationResponseDto;
import mk.ukim.finki.traveltoursbackend.service.application.DestinationApplicationService;
import mk.ukim.finki.traveltoursbackend.service.domain.DestinationService;
import org.springframework.stereotype.Service;

@Service
public class DestinationApplicationServiceImpl implements DestinationApplicationService {
    private final DestinationService destinationService;

    public DestinationApplicationServiceImpl(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @Override
    public List<DisplayDestinationResponseDto> findAll() {
        return DisplayDestinationResponseDto.from(destinationService.findAll());
    }

    @Override
    public Optional<DisplayDestinationResponseDto> findById(Long id) {
        return destinationService
            .findById(id)
            .map(DisplayDestinationResponseDto::from);
    }

    @Override
    public DisplayDestinationResponseDto create(CreateOrUpdateDestinationRequestDto createDestinationRequestDto) {
        return DisplayDestinationResponseDto.from(destinationService.create(createDestinationRequestDto.toDestination()));
    }

    @Override
    public Optional<DisplayDestinationResponseDto> update(Long id, CreateOrUpdateDestinationRequestDto updateDestinationRequestDto) {
        return destinationService
            .update(id, updateDestinationRequestDto.toDestination())
            .map(DisplayDestinationResponseDto::from);
    }

    @Override
    public Optional<DisplayDestinationResponseDto> deleteById(Long id) {
        return destinationService
            .deleteById(id)
            .map(DisplayDestinationResponseDto::from);
    }
}
