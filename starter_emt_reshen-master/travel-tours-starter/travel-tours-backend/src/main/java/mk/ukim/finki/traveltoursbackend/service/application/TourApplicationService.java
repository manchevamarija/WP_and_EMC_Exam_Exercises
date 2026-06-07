package mk.ukim.finki.traveltoursbackend.service.application;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.traveltoursbackend.dto.domain.CreateOrUpdateTourRequestDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayTourDetailsResponseDto;
import mk.ukim.finki.traveltoursbackend.dto.domain.DisplayTourResponseDto;

public interface TourApplicationService {
    List<DisplayTourResponseDto> findAll();

    Optional<DisplayTourResponseDto> findById(Long id);

    Optional<DisplayTourDetailsResponseDto> findByIdWithDetails(Long id);

    DisplayTourResponseDto create(CreateOrUpdateTourRequestDto createTourRequestDto);

    Optional<DisplayTourResponseDto> update(Long id, CreateOrUpdateTourRequestDto updateTourRequestDto);

    Optional<DisplayTourResponseDto> deleteById(Long id);
}
