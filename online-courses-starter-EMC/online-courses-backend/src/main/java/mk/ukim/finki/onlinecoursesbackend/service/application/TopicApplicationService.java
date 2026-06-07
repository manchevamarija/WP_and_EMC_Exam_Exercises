package mk.ukim.finki.onlinecoursesbackend.service.application;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.CreateOrUpdateTopicRequestDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayTopicResponseDto;

public interface TopicApplicationService {
    List<DisplayTopicResponseDto> findAll();

    Optional<DisplayTopicResponseDto> findById(Long id);

    DisplayTopicResponseDto create(CreateOrUpdateTopicRequestDto createTopicRequestDto);

    Optional<DisplayTopicResponseDto> update(Long id, CreateOrUpdateTopicRequestDto updateTopicRequestDto);

    Optional<DisplayTopicResponseDto> deleteById(Long id);
}
