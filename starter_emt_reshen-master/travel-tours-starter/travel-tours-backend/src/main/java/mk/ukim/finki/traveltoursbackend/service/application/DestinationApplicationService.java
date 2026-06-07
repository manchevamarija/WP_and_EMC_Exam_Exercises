package mk.ukim.finki.traveltoursbackend.service.application;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.dto.domain.CreateOrUpdateDestinationRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayDestinationResponseDto;

public interface DestinationApplicationService {
    List<DisplayDestinationResponseDto> findAll();

    Optional<DisplayDestinationResponseDto> findById(Long id);

    DisplayDestinationResponseDto create(CreateOrUpdateDestinationRequestDto createDestinationRequestDto);

    Optional<DisplayDestinationResponseDto> update(Long id, CreateOrUpdateDestinationRequestDto updateDestinationRequestDto);

    Optional<DisplayDestinationResponseDto> deleteById(Long id);
}
