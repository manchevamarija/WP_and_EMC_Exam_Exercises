package mk.ukim.finki.onlinecoursesbackend.service.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.CreateOrUpdateCourseRequestDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayCourseDetailsResponseDto;
import mk.ukim.finki.onlinecoursesbackend.dto.domain.DisplayCourseResponseDto;
import mk.ukim.finki.onlinecoursesbackend.model.domain.Topic;
import mk.ukim.finki.onlinecoursesbackend.model.exception.TopicNotFoundException;
import mk.ukim.finki.onlinecoursesbackend.service.application.CourseApplicationService;
import mk.ukim.finki.onlinecoursesbackend.service.domain.CourseService;
import mk.ukim.finki.onlinecoursesbackend.service.domain.TopicService;
import org.springframework.stereotype.Service;

@Service
public class CourseApplicationServiceImpl implements CourseApplicationService {
    private final TopicService topicService;
    private final CourseService courseService;

    public CourseApplicationServiceImpl(TopicService topicService, CourseService courseService) {
        this.topicService = topicService;
        this.courseService = courseService;
    }

    @Override
    public List<DisplayCourseResponseDto> findAll() {
        return DisplayCourseResponseDto.from(courseService.findAll());
    }

    @Override
    public List<DisplayCourseResponseDto> findAllByAvailability(Boolean available) {
       return DisplayCourseResponseDto.from(courseService.findAllByAvailability(available));
    }

    @Override
    public Optional<DisplayCourseResponseDto> findById(Long id) {
        return courseService
            .findById(id)
            .map(DisplayCourseResponseDto::from);
    }

    @Override
    public Optional<DisplayCourseDetailsResponseDto> findByIdWithDetails(Long id) {
        return courseService
                .findById(id)
                .map(DisplayCourseDetailsResponseDto::from);
    }

    @Override
    public DisplayCourseResponseDto create(CreateOrUpdateCourseRequestDto createCourseRequestDto) {
        Topic topic = topicService.findById(createCourseRequestDto.topicId()).orElseThrow(()-> new TopicNotFoundException(createCourseRequestDto.topicId()));
        return DisplayCourseResponseDto.from(courseService.create(createCourseRequestDto.toCourse(topic)));
    }

    @Override
    public Optional<DisplayCourseResponseDto> update(Long id, CreateOrUpdateCourseRequestDto updateCourseRequestDto) {
       Topic topic = topicService.findById(updateCourseRequestDto.topicId()).orElseThrow(()-> new TopicNotFoundException(updateCourseRequestDto.topicId()));
        return courseService
                .update(id, updateCourseRequestDto.toCourse(topic))
                .map(DisplayCourseResponseDto::from);
    }

    @Override
    public Optional<DisplayCourseResponseDto> deleteById(Long id) {
        return courseService
                .deleteById(id)
                .map(DisplayCourseResponseDto::from);
    }

}
