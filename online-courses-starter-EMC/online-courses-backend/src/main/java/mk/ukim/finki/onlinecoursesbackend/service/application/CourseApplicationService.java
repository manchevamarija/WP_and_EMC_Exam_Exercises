package mk.ukim.finki.onlinecoursesbackend.service.application;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.CreateOrUpdateCourseRequestDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayCourseDetailsResponseDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayCourseResponseDto;

public interface CourseApplicationService {
    List<DisplayCourseResponseDto> findAll();

    List<DisplayCourseResponseDto> findAllByAvailability(Boolean available);

    Optional<DisplayCourseResponseDto> findById(Long id);

    Optional<DisplayCourseDetailsResponseDto> findByIdWithDetails(Long id);

    DisplayCourseResponseDto create(CreateOrUpdateCourseRequestDto createCourseRequestDto);

    Optional<DisplayCourseResponseDto> update(Long id, CreateOrUpdateCourseRequestDto updateCourseRequestDto);

    Optional<DisplayCourseResponseDto> deleteById(Long id);
}
